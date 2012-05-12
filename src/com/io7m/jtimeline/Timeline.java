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

    private final @Nonnull Interpolable              interpolable;

    State(
      final @Nonnull Interpolable i)
    {
      this.interpolable = i;
      this.keyframes = new TreeMap<Long, Keyframe>();
    }

    @Nonnull Interpolable getInterpolable()
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
    final @Nonnull Interpolable i)
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

  private long                                        time_current;
  private final @Nonnull HashMap<String, State>       interpolables;
  private final @Nonnull HashMap<String, Set<String>> interpolable_groups;

  public Timeline()
  {
    this.time_current = 0;
    this.interpolables = new HashMap<String, State>();
    this.interpolable_groups = new HashMap<String, Set<String>>();
  }

  public long currentTimeGet()
  {
    return this.time_current;
  }

  public void currentTimeSet(
    final long time)
  {
    this.time_current = time;
  }

  public ArrayList<Interpolable> getGroup(
    final @Nonnull String group)
    throws ConstraintError
  {
    Constraints.constrainNotNull(group, "Group name");

    final ArrayList<Interpolable> is = new ArrayList<Interpolable>();
    final Set<String> names = this.interpolable_groups.get(group);

    for (final String name : names) {
      final State state = this.interpolables.get(name);
      is.add(state.getInterpolable());
    }

    return is;
  }

  public void interpolableAdd(
    final @Nonnull Interpolable i)
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

  public void keyframeAdd(
    final @Nonnull Interpolable i,
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
