package com.io7m.jtimeline.tests;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Pair;
import com.io7m.jaux.functional.Procedure;
import com.io7m.jtimeline.Interpolable;
import com.io7m.jtimeline.InterpolationType;
import com.io7m.jtimeline.Keyframe;

public class KeyframeTest
{
  private static class CallCheck implements
    Procedure<Pair<Interpolable, Keyframe>>
  {
    int calls = 0;

    public CallCheck()
    {
      // Unused.
    }

    @Override public void call(
      final Pair<Interpolable, Keyframe> x)
    {
      this.calls = this.calls + 1;
    }

    int getCalls()
    {
      return this.calls;
    }
  }

  private static class ITrivial implements Interpolable
  {
    public ITrivial()
    {
      // Unused.
    }

    @Override public double interpolableGet()
    {
      throw new AssertionError("unreachable code: report this bug!");
    }

    @Override public String interpolableGetGroup()
    {
      throw new AssertionError("unreachable code: report this bug!");
    }

    @Override public long interpolableGetID()
    {
      throw new AssertionError("unreachable code: report this bug!");
    }

    @Override public String interpolableGetName()
    {
      throw new AssertionError("unreachable code: report this bug!");
    }

    @Override public double interpolableMaximum()
    {
      throw new AssertionError("unreachable code: report this bug!");
    }

    @Override public double interpolableMinimum()
    {
      throw new AssertionError("unreachable code: report this bug!");
    }

    @Override public void interpolableSet(
      final double x)
    {
      throw new AssertionError("unreachable code: report this bug!");
    }
  }

  @Test public void testEqualsHashcodeFalse()
    throws ConstraintError
  {
    final Keyframe k0 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0);
    final Keyframe k1 =
      new Keyframe(InterpolationType.INTERPOLATE_EXPONENTIAL, 0, 0);
    final Keyframe k2 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 1, 0);
    final Keyframe k3 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 1);

    Assert.assertFalse(k0.hashCode() == k1.hashCode());
    Assert.assertFalse(k0.hashCode() == k2.hashCode());
    Assert.assertFalse(k0.hashCode() == k3.hashCode());

    Assert.assertFalse(k1.hashCode() == k0.hashCode());
    Assert.assertFalse(k1.hashCode() == k2.hashCode());
    Assert.assertFalse(k1.hashCode() == k3.hashCode());

    Assert.assertFalse(k2.hashCode() == k0.hashCode());
    Assert.assertFalse(k2.hashCode() == k1.hashCode());
    Assert.assertFalse(k2.hashCode() == k3.hashCode());

    Assert.assertFalse(k3.hashCode() == k0.hashCode());
    Assert.assertFalse(k3.hashCode() == k1.hashCode());
    Assert.assertFalse(k3.hashCode() == k2.hashCode());
  }

  @Test public void testEqualsNullFalse()
    throws ConstraintError
  {
    final Keyframe k0 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0);
    Assert.assertFalse(k0.equals(null));
  }

  @Test public void testEqualsOtherClassFalse()
    throws ConstraintError
  {
    final Keyframe k0 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0);
    Assert.assertFalse(k0.equals(Integer.valueOf(23)));
  }

  @Test public void testEqualsOtherInterpolationFalse()
    throws ConstraintError
  {
    final Keyframe k0 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0);
    final Keyframe k1 =
      new Keyframe(InterpolationType.INTERPOLATE_EXPONENTIAL, 0, 0);
    Assert.assertFalse(k0.equals(k1));
  }

  @Test public void testEqualsOtherTimeFalse()
    throws ConstraintError
  {
    final Keyframe k0 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0);
    final Keyframe k1 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 1, 0);
    Assert.assertFalse(k0.equals(k1));
  }

  @Test public void testEqualsOtherValueFalse()
    throws ConstraintError
  {
    final Keyframe k0 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0);
    final Keyframe k1 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 1);
    Assert.assertFalse(k0.equals(k1));
  }

  @Test public void testEqualsReflexive()
    throws ConstraintError
  {
    final Keyframe k0 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0);
    Assert.assertEquals(k0, k0);
  }

  @Test public void testEqualsStringFalse()
    throws ConstraintError
  {
    final Keyframe k0 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0);
    final Keyframe k1 =
      new Keyframe(InterpolationType.INTERPOLATE_EXPONENTIAL, 0, 0);
    final Keyframe k2 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 1, 0);
    final Keyframe k3 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 1);

    Assert.assertFalse(k0.toString().equals(k1.toString()));
    Assert.assertFalse(k0.toString().equals(k2.toString()));
    Assert.assertFalse(k0.toString().equals(k3.toString()));

    Assert.assertFalse(k1.toString().equals(k0.toString()));
    Assert.assertFalse(k1.toString().equals(k2.toString()));
    Assert.assertFalse(k1.toString().equals(k3.toString()));

    Assert.assertFalse(k2.toString().equals(k0.toString()));
    Assert.assertFalse(k2.toString().equals(k1.toString()));
    Assert.assertFalse(k2.toString().equals(k3.toString()));

    Assert.assertFalse(k3.toString().equals(k0.toString()));
    Assert.assertFalse(k3.toString().equals(k1.toString()));
    Assert.assertFalse(k3.toString().equals(k2.toString()));

    Assert.assertEquals(k0.toString(), k0.toString());
  }

  @Test public void testEqualsSymmetric()
    throws ConstraintError
  {
    final Keyframe k0 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0);
    final Keyframe k1 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0);
    Assert.assertEquals(k0, k1);
    Assert.assertEquals(k1, k0);
  }

  @Test public void testEqualsTransitive()
    throws ConstraintError
  {
    final Keyframe k0 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0);
    final Keyframe k1 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0);
    final Keyframe k2 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0);
    Assert.assertEquals(k0, k1);
    Assert.assertEquals(k1, k2);
    Assert.assertEquals(k0, k2);
  }

  @Test public void testNew0()
    throws ConstraintError
  {
    final Keyframe k =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0);
    Assert.assertEquals(0, k.getTime());
    Assert.assertTrue(0.0 == k.getValue());
    Assert.assertEquals(
      InterpolationType.INTERPOLATE_LINEAR,
      k.getInterpolationType());
  }

  @SuppressWarnings("unused") @Test(expected = ConstraintError.class) public
    void
    testNewCallbackNull()
      throws ConstraintError
  {
    new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0, null);
  }

  @SuppressWarnings("unused") @Test(expected = ConstraintError.class) public
    void
    testNewNull()
      throws ConstraintError
  {
    new Keyframe(null, 0, 0);
  }

  @Test public void testRunCallback()
    throws ConstraintError
  {
    final ITrivial i = new ITrivial();
    final CallCheck cc = new CallCheck();
    final Keyframe k =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0, cc);

    Assert.assertEquals(0, cc.getCalls());
    k.runCallback(i);
    Assert.assertEquals(1, cc.getCalls());
  }

  @Test(expected = ConstraintError.class) public void testRunCallbackNull()
    throws ConstraintError
  {
    final Keyframe k =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0);
    k.runCallback(null);
  }
}
