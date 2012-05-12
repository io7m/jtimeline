package com.io7m.jtimeline;

/**
 * Interpolation types supported by timelines.
 */

public enum InterpolationType
{
  /**
   * Linearly interpolate the relevant value.
   * 
   * @see Interpolation#interpolateLinear(double, double, double)
   */

  INTERPOLATE_LINEAR,

  /**
   * Interpolate the relevant value using an exponential scale.
   * 
   * @see Interpolation#interpolateExponential(double, double, double)
   */

  INTERPOLATE_EXPONENTIAL,

  /**
   * Interpolate the relevant value using a logarithmic scale.
   * 
   * @see Interpolation#interpolateLogarithmic(double, double, double)
   */

  INTERPOLATE_LOGARITHMIC,

  /**
   * Do not interpolate the relevant value. Instead, use the value set at the
   * previous keyframe for the entirety of the interpolation period.
   */

  INTERPOLATE_STEP_MINIMUM,

  /**
   * Do not interpolate the relevant value. Instead, use the value set at the
   * next keyframe for the entirety of the interpolation period.
   */

  INTERPOLATE_STEP_MAXIMUM
}
