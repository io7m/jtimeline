package com.io7m.jtimeline;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Pair;
import com.io7m.jaux.functional.Procedure;

/**
 * Type representing a time in frames, an interpolation type, a value, and
 * possibly a callback to be called when the given time is reached.
 */

@Immutable public final class Keyframe
{
  private final double                                                          value;
  private final long                                                            time;
  private final @Nonnull InterpolationType                                      interpolation;
  private final @CheckForNull Procedure<Pair<InterpolableScalarReal, Keyframe>> callback;

  public Keyframe(
    final @Nonnull InterpolationType interpolation,
    final long time,
    final double value)
    throws ConstraintError
  {
    this.interpolation =
      Constraints.constrainNotNull(interpolation, "Interpolation type");
    this.time = time;
    this.value = value;
    this.callback = null;
  }

  public Keyframe(
    final @Nonnull InterpolationType interpolation,
    final long time,
    final double value,
    final @Nonnull Procedure<Pair<InterpolableScalarReal, Keyframe>> callback)
    throws ConstraintError
  {
    this.interpolation =
      Constraints.constrainNotNull(interpolation, "Interpolation type");
    this.time = time;
    this.value = value;
    this.callback = Constraints.constrainNotNull(callback, "Callback");
  }

  @Override public boolean equals(
    final Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final Keyframe other = (Keyframe) obj;
    if (this.interpolation != other.interpolation) {
      return false;
    }
    if (this.time != other.time) {
      return false;
    }
    if (Double.doubleToLongBits(this.value) != Double
      .doubleToLongBits(other.value)) {
      return false;
    }
    return true;
  }

  /**
   * Return the interpolation type of the keyframe.
   */

  public @Nonnull InterpolationType getInterpolationType()
  {
    return this.interpolation;
  }

  /**
   * Return the time in frames of the keyframe.
   */

  public long getTime()
  {
    return this.time;
  }

  /**
   * Return the value of the keyframe.
   */

  public double getValue()
  {
    return this.value;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result =
      (prime * result)
        + ((this.interpolation == null) ? 0 : this.interpolation.hashCode());
    result = (prime * result) + (int) (this.time ^ (this.time >>> 32));
    long temp;
    temp = Double.doubleToLongBits(this.value);
    result = (prime * result) + (int) (temp ^ (temp >>> 32));
    return result;
  }

  /**
   * Execute the callback for the keyframe, if any. This function is called by
   * the {@link Timeline#step()} function.
   * 
   * @param i
   *          The interpolable value passed to the keyframe.
   * @throws ConstraintError
   *           Iff <code>i == null</code>.
   */

  public void runCallback(
    final @Nonnull InterpolableScalarReal i)
    throws ConstraintError
  {
    Constraints.constrainNotNull(i, "Interpolable");

    if (this.callback != null) {
      this.callback.call(new Pair<InterpolableScalarReal, Keyframe>(i, this));
    }
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[Keyframe ");
    builder.append(this.value);
    builder.append(" ");
    builder.append(this.time);
    builder.append(" ");
    builder.append(this.interpolation);
    builder.append("]");
    return builder.toString();
  }
}
