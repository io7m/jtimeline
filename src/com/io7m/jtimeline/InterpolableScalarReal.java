package com.io7m.jtimeline;

import javax.annotation.Nonnull;

/**
 * Interface representing a scalar value that can be interpolated.
 */

public interface InterpolableScalarReal
{
  /**
   * Retrieve the current value.
   */

  double interpolableGet();

  /**
   * Retrieve the name of the group in which the interpolable value resides.
   * Group names are used to group interpolable values for organizational
   * purposes. In order to avoid name clashes between interpolable values in
   * different libraries, it is suggested that developers use the fully
   * qualified name of the Java package in which the interpolable value
   * resides as the group name.
   * 
   * @see InterpolableScalarReal#interpolableGetID()
   */

  @Nonnull String interpolableGetGroup();

  /**
   * Retrieve the unique ID of the interpolable value. The ID is used to
   * distinguish between different instances of interpolable values of the
   * same type/class. An interpolable value is uniquely identified by the
   * concatenation of the group name, name, and ID.
   * 
   * @see InterpolableScalarReal#interpolableGetGroup()
   * @see InterpolableScalarReal#interpolableGetName()
   */

  long interpolableGetID();

  /**
   * Retrieve the name of the interpolable value.
   * 
   * @see InterpolableScalarReal#interpolableGetID()
   */

  @Nonnull String interpolableGetName();

  /**
   * Retrieve the maximum value of the interpolable value for the purposes of
   * interpolation. The resulting interpolated value will be clamped to the
   * range <code>[interpolableMinimum() .. interpolableMaximum()]</code>
   * inclusive before being assigned with <code>interpolableSet()</code>.
   * 
   * @see InterpolableScalarReal#interpolableMinimum()
   * @see InterpolableScalarReal#interpolableSet(double)
   */

  double interpolableMaximum();

  /**
   * Retrieve the minimum value of the interpolable value for the purposes of
   * interpolation. The resulting interpolated value will be clamped to the
   * range <code>[interpolableMinimum() .. interpolableMaximum()]</code>
   * inclusive before being assigned with <code>interpolableSet()</code>.
   * 
   * @see InterpolableScalarReal#interpolableMaximum()
   * @see InterpolableScalarReal#interpolableSet(double)
   */

  double interpolableMinimum();

  /**
   * Set the value of the target. The value is guaranteed to be within the
   * range <code>[interpolableMinimum() .. interpolableMaximum()]</code>
   * inclusive.
   */

  void interpolableSet(
    final double x);
}
