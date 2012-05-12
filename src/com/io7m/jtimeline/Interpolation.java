package com.io7m.jtimeline;

/**
 * Interpolation functions.
 */

public final class Interpolation
{
  /**
   * Interpolate between <code>min</code> and <code>max</code> based on
   * <code>factor</code>, using an exponential scale. When
   * <code>factor == 0</code>, the function returns <code>min</code>, when
   * <code>factor == 1</code>, the function returns <code>max</code>. More
   * formally, the function returns <code>min + (factor² * (max - min))</code>
   * .
   * 
   * @param factor
   *          The interpolation factor, in the range <code>0.0 .. 1.0</code>
   *          inclusive.
   * @param min
   *          The lower bound.
   * @param max
   *          The upper bound.
   * @return An interpolated value.
   */

  public static double interpolateExponential(
    final double factor,
    final double min,
    final double max)
  {
    return Interpolation.interpolateLinear(factor * factor, min, max);
  }

  /**
   * Linearly interpolate between <code>min</code> and <code>max</code> based
   * on <code>factor</code>. When <code>factor == 0</code>, the function
   * returns <code>min</code>, when <code>factor == 1</code>, the function
   * returns <code>max</code>. More formally, the function returns
   * <code>min + (factor * (max - min))</code>.
   * 
   * @param factor
   *          The interpolation factor, in the range <code>0.0 .. 1.0</code>
   *          inclusive.
   * @param min
   *          The lower bound.
   * @param max
   *          The upper bound.
   * @return A linearly interpolated value.
   */

  public static double interpolateLinear(
    final double factor,
    final double min,
    final double max)
  {
    return min + (factor * (max - min));
  }

  /**
   * Interpolate between <code>min</code> and <code>max</code> based on
   * <code>factor</code>, using a logarithmic scale. When
   * <code>factor == 0</code>, the function returns <code>min</code>, when
   * <code>factor == 1</code>, the function returns <code>max</code>. More
   * formally, the function returns
   * <code>min + (sqrt(factor) * (max - min))</code>.
   * 
   * @param factor
   *          The interpolation factor, in the range <code>0.0 .. 1.0</code>
   *          inclusive.
   * @param min
   *          The lower bound.
   * @param max
   *          The upper bound.
   * @return An interpolated value.
   */

  public static double interpolateLogarithmic(
    final double factor,
    final double min,
    final double max)
  {
    return Interpolation.interpolateLinear(Math.sqrt(factor), min, max);
  }

  private Interpolation()
  {
    throw new AssertionError("unreachable code: report this bug!");
  }
}
