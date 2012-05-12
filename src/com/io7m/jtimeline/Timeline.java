package com.io7m.jtimeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Type representing a generic timeline.
 */

public final class Timeline
{
  private static final class State
  {
    private static double interpolateKeyframes(
      final long time_current,
      final @Nonnull Keyframe k0,
      final @Nonnull Keyframe k1)
    {
      final InterpolationType it = k0.getInterpolationType();
      final double kv0 = time_current - k0.getTime();
      final double kv1 = k1.getTime() - k0.getTime();
      final double position_factor = kv0 / kv1;
      double value = 0.0;

      switch (it) {
        case INTERPOLATE_EXPONENTIAL:
        {
          value =
            Interpolation.interpolateExponential(
              position_factor,
              k0.getValue(),
              k1.getValue());
          break;
        }
        case INTERPOLATE_LINEAR:
        {
          value =
            Interpolation.interpolateLinear(
              position_factor,
              k0.getValue(),
              k1.getValue());
          break;
        }
        case INTERPOLATE_LOGARITHMIC:
        {
          value =
            Interpolation.interpolateLogarithmic(
              position_factor,
              k0.getValue(),
              k1.getValue());
          break;
        }
        case INTERPOLATE_STEP_MAXIMUM:
        {
          value = k1.getValue();
          break;
        }
        case INTERPOLATE_STEP_MINIMUM:
        {
          value = k0.getValue();
          break;
        }
      }

      return value;
    }

    private final @Nonnull SortedMap<Long, Keyframe> keyframes;
    private final @Nonnull InterpolableScalarReal    interpolable;

    State(
      final @Nonnull InterpolableScalarReal i)
    {
      this.interpolable = i;
      this.keyframes = new TreeMap<Long, Keyframe>();
    }

    @Nonnull InterpolableScalarReal getInterpolable()
    {
      return this.interpolable;
    }

    void keyframeAdd(
      final @Nonnull Keyframe k)
      throws ConstraintError
    {
      final Long time = Long.valueOf(k.getTime());

      Constraints.constrainArbitrary(
        this.keyframes.containsKey(time) == false,
        "Keyframe not added for frame '" + time + "'");

      this.keyframes.put(time, k);
    }

    private @CheckForNull Keyframe keyframeSource(
      final long time_current)
    {
      final Long key = Long.valueOf(time_current);

      if (this.keyframes.containsKey(key)) {
        return this.keyframes.get(key);
      }

      final SortedMap<Long, Keyframe> before = this.keyframes.headMap(key);
      if (before.size() == 0) {
        return null;
      }
      return before.get(before.lastKey());
    }

    private @CheckForNull Keyframe keyframeTarget(
      final long time_current)
    {
      final Long key = Long.valueOf(time_current + 1);
      final SortedMap<Long, Keyframe> after = this.keyframes.tailMap(key);

      try {
        final Long first = after.firstKey();
        return after.get(first);
      } catch (final NoSuchElementException e) {
        return null;
      }
    }

    void run(
      final long time_current)
      throws ConstraintError
    {
      final Keyframe k0 = this.keyframeSource(time_current);

      if (k0 == null) {
        return;
      }
      if (k0.getTime() == time_current) {
        k0.runCallback(this.interpolable);
      }

      double value;
      final Keyframe k1 = this.keyframeTarget(time_current);
      if (k1 != null) {
        value = State.interpolateKeyframes(time_current, k0, k1);
      } else {
        value = k0.getValue();
      }

      value =
        Timeline.clamp(
          value,
          this.interpolable.interpolableMinimum(),
          this.interpolable.interpolableMaximum());
      this.interpolable.interpolableSet(value);
    }
  }

  static double clamp(
    final double x,
    final double min,
    final double max)
  {
    return Math.max(Math.min(x, max), min);
  }

  private static @Nonnull String makeUniqueID(
    final @Nonnull InterpolableScalarReal i)
    throws ConstraintError
  {
    Constraints.constrainNotNull(i, "Interpolable");

    final String group = i.interpolableGetGroup();
    final String name = i.interpolableGetName();
    final long id = i.interpolableGetID();
    Constraints.constrainNotNull(group, "Interpolable domain");
    Constraints.constrainNotNull(name, "Interpolable name");

    final StringBuilder s = new StringBuilder();
    s.append(group);
    s.append("/");
    s.append(name);
    s.append("[");
    s.append(id);
    s.append("]");
    return s.toString();
  }

  private boolean                                     time_loop_enabled;
  private long                                        time_current;
  private long                                        time_loop;
  private final @Nonnull HashMap<String, State>       interpolables;
  private final @Nonnull HashMap<String, Set<String>> interpolable_groups;

  public Timeline()
  {
    this.time_loop_enabled = false;
    this.time_loop = 0;
    this.time_current = 0;
    this.interpolables = new HashMap<String, State>();
    this.interpolable_groups = new HashMap<String, Set<String>>();
  }

  /**
   * Retrieve the current time in frames for the timeline.
   */

  public long currentTimeGet()
  {
    return this.time_current;
  }

  /**
   * Set the current time in frames for the timeline.
   */

  public void currentTimeSet(
    final long time)
  {
    this.time_current = time;
  }

  /**
   * Retrieve the set of {@link InterpolableScalarReal} values added to the
   * timeline with group <code>group</code>.
   * 
   * @param group
   *          The name of the group.
   * @throws ConstraintError
   *           Iff <code>group == null</code>.
   */

  public ArrayList<InterpolableScalarReal> getGroup(
    final @Nonnull String group)
    throws ConstraintError
  {
    Constraints.constrainNotNull(group, "Group name");

    final ArrayList<InterpolableScalarReal> is =
      new ArrayList<InterpolableScalarReal>();
    final Set<String> names = this.interpolable_groups.get(group);

    for (final String name : names) {
      final State state = this.interpolables.get(name);
      is.add(state.getInterpolable());
    }

    return is;
  }

  /**
   * Add the {@link InterpolableScalarReal} value <code>i</code> to the
   * timeline. A value must be added before keyframes can be assigned.
   * 
   * @param i
   *          The {@link InterpolableScalarReal} value.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>i == null</code></li>
   *           <li><code>i</code> violates contracts on any of the
   *           {@link InterpolableScalarReal} methods by, for example,
   *           returning <code>null</code> for
   *           {@link InterpolableScalarReal#interpolableGetName()}</li>
   *           </ul>
   */

  public void interpolableAdd(
    final @Nonnull InterpolableScalarReal i)
    throws ConstraintError
  {
    Constraints.constrainNotNull(i, "Interpolable");

    final String ident = Timeline.makeUniqueID(i);
    Constraints.constrainArbitrary(
      this.interpolables.containsKey(ident) == false,
      "Interpolable '" + ident + "' not already added");

    final State state = new State(i);
    this.interpolables.put(ident, state);

    final String group = i.interpolableGetGroup();
    Set<String> names = null;

    if (this.interpolable_groups.containsKey(group)) {
      names = this.interpolable_groups.get(group);
      names.add(ident);
    } else {
      names = new TreeSet<String>();
      names.add(ident);
      this.interpolable_groups.put(group, names);
    }
  }

  /**
   * Add the keyframe <code>k</code> for the {@link InterpolableScalarReal}
   * value <code>i</code>. <code>i</code> must have been previously added to
   * the timeline with
   * {@link Timeline#interpolableAdd(InterpolableScalarReal)}.
   * 
   * @param i
   * @param k
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>i == null</code></li>
   *           <li><code>k == null</code></li>
   *           <li><code>i</code> violates contracts on any of the
   *           {@link InterpolableScalarReal} methods by, for example,
   *           returning <code>null</code> for
   *           {@link InterpolableScalarReal#interpolableGetName()}</li>
   *           <li><code>i</code> was not previously added to the timeline</li>
   *           <li>A keyframe for <code>i</code> already exists at the frame
   *           given by <code>k</code></li>
   *           </ul>
   */

  public void keyframeAdd(
    final @Nonnull InterpolableScalarReal i,
    final @Nonnull Keyframe k)
    throws ConstraintError
  {
    Constraints.constrainNotNull(i, "Interpolable");
    Constraints.constrainNotNull(k, "Keyframe");

    final String ident = Timeline.makeUniqueID(i);
    Constraints.constrainArbitrary(
      this.interpolables.containsKey(ident),
      "Interpolable '" + ident + "' added");

    final State state = this.interpolables.get(ident);
    assert state != null;

    state.keyframeAdd(k);
  }

  /**
   * Retrieve the frame at which the timeline will rewind to frame
   * <code>0</code>, iff looping is enabled.
   * 
   * @see #loopIsEnabled()
   * @see #loopSetEnabled(long)
   * @see #loopSetDisabled()
   */

  public long loopGetTime()
  {
    return this.time_loop;
  }

  /**
   * Return <code>true</code> if looping is currently enabled.
   * 
   * @see #loopSetEnabled(long)
   * @see #loopSetDisabled()
   */

  public boolean loopIsEnabled()
  {
    return this.time_loop_enabled;
  }

  /**
   * Disable looping.
   * 
   * @see #loopSetEnabled(long)
   */

  public void loopSetDisabled()
  {
    this.time_loop_enabled = false;
  }

  /**
   * Enable looping for the given timeline. The timeline will reset to frame
   * <code>0</code> after frame <code>frame</code> is reached.
   * 
   * @see #loopIsEnabled()
   * @see #loopGetTime()
   * @see #loopSetDisabled()
   */

  public void loopSetEnabled(
    final long frame)
  {
    this.time_loop_enabled = true;
    this.time_loop = frame;
  }

  /**
   * Step the timeline forward by one frame. The function executes all
   * keyframes for the current time, and then advances the time forward by one
   * frame.
   * 
   * @throws ConstraintError
   *           Iff an internal constraint error occurs.
   */

  public void step()
    throws ConstraintError
  {
    try {
      for (final Entry<String, State> e : this.interpolables.entrySet()) {
        final State state = e.getValue();
        state.run(this.time_current);
      }
    } finally {
      this.time_current = this.time_current + 1;
    }
  }
}
