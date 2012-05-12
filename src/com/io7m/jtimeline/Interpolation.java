package com.io7m.jtimeline;

public final class Interpolation
{
  public static double interpolateExponential(
    final double factor,
    final double min,
    final double max)
  {
    return Interpolation.interpolateLinear(factor * factor, min, max);
  }

  public static double interpolateLinear(
    final double factor,
    final double min,
    final double max)
  {
    return min + (factor * (max - min));
  }

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
