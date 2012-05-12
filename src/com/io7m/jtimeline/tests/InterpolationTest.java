package com.io7m.jtimeline.tests;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.ApproximatelyEqualDouble;
import com.io7m.jtimeline.Interpolation;

public class InterpolationTest
{
  @Test public void testExp0()
  {
    final double x = Interpolation.interpolateExponential(0.0, 0.0, 10.0);
    Assert.assertTrue(0.0 == x);
  }

  @Test public void testExp10()
  {
    final double x = Interpolation.interpolateExponential(1.0, 0.0, 10.0);
    Assert.assertTrue(10.0 == x);
  }

  @Test public void testExp2()
  {
    final double x = Interpolation.interpolateExponential(0.5, 0.0, 10.0);
    Assert.assertTrue(2.5 == x);
  }

  @Test public void testLinear0()
  {
    final double x = Interpolation.interpolateLinear(0.0, 0.0, 10.0);
    Assert.assertTrue(0.0 == x);
  }

  @Test public void testLinear10()
  {
    final double x = Interpolation.interpolateLinear(1.0, 0.0, 10.0);
    Assert.assertTrue(10.0 == x);
  }

  @Test public void testLinear5()
  {
    final double x = Interpolation.interpolateLinear(0.5, 0.0, 10.0);
    Assert.assertTrue(5.0 == x);
  }

  @Test public void testLog0()
  {
    final double x = Interpolation.interpolateLogarithmic(0.0, 0.0, 10.0);
    Assert.assertTrue(0.0 == x);
  }

  @Test public void testLog10()
  {
    final double x = Interpolation.interpolateLogarithmic(1.0, 0.0, 10.0);
    Assert.assertTrue(10.0 == x);
  }

  @Test public void testLog7()
  {
    final double x = Interpolation.interpolateLogarithmic(0.5, 0.0, 10.0);
    Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqualExplicit(
      7.0,
      x,
      0.05));
  }
}
