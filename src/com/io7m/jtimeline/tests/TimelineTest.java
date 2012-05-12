package com.io7m.jtimeline.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.ApproximatelyEqualDouble;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Function;
import com.io7m.jaux.functional.Pair;
import com.io7m.jaux.functional.Procedure;
import com.io7m.jtimeline.Interpolable;
import com.io7m.jtimeline.InterpolationType;
import com.io7m.jtimeline.Keyframe;
import com.io7m.jtimeline.Timeline;

public class TimelineTest
{
  private static class IGroup implements Interpolable
  {
    private static final AtomicLong id_pool;
    private double                  value = 0.0;
    private final long              id;
    private final String            name;

    static {
      id_pool = new AtomicLong(0);
    }

    IGroup(
      final @Nonnull String name)
    {
      this.id = IGroup.id_pool.incrementAndGet();
      this.name = name;
    }

    @Override public double interpolableGet()
    {
      return this.value;
    }

    @Override public String interpolableGetGroup()
    {
      return "com.io7m.jtimeline";
    }

    @Override public long interpolableGetID()
    {
      return this.id;
    }

    @Override public String interpolableGetName()
    {
      return this.name;
    }

    @Override public double interpolableMaximum()
    {
      return 1.0;
    }

    @Override public double interpolableMinimum()
    {
      return 0.0;
    }

    @Override public void interpolableSet(
      final double x)
    {
      this.value = x;
    }
  }

  private static class INullGroup implements Interpolable
  {
    private static final AtomicLong id_pool;
    private double                  value = 0.0;
    private final long              id;

    static {
      id_pool = new AtomicLong(0);
    }

    INullGroup()
    {
      this.id = INullGroup.id_pool.incrementAndGet();
    }

    @Override public double interpolableGet()
    {
      return this.value;
    }

    @Override public String interpolableGetGroup()
    {
      return null;
    }

    @Override public long interpolableGetID()
    {
      return this.id;
    }

    @Override public String interpolableGetName()
    {
      return "inullgroup";
    }

    @Override public double interpolableMaximum()
    {
      return 1.0;
    }

    @Override public double interpolableMinimum()
    {
      return 0.0;
    }

    @Override public void interpolableSet(
      final double x)
    {
      this.value = x;
    }
  }

  private static class INullName implements Interpolable
  {
    private static final AtomicLong id_pool;
    private double                  value = 0.0;
    private final long              id;

    static {
      id_pool = new AtomicLong(0);
    }

    INullName()
    {
      this.id = INullName.id_pool.incrementAndGet();
    }

    @Override public double interpolableGet()
    {
      return this.value;
    }

    @Override public String interpolableGetGroup()
    {
      return "com.io7m.jtimeline";
    }

    @Override public long interpolableGetID()
    {
      return this.id;
    }

    @Override public String interpolableGetName()
    {
      return null;
    }

    @Override public double interpolableMaximum()
    {
      return 1.0;
    }

    @Override public double interpolableMinimum()
    {
      return 0.0;
    }

    @Override public void interpolableSet(
      final double x)
    {
      this.value = x;
    }
  }

  private static class ISimple implements Interpolable
  {
    private static final AtomicLong id_pool;
    private double                  value = 0.0;
    private final long              id;
    private final String            group;
    private final String            name;

    static {
      id_pool = new AtomicLong(0);
    }

    ISimple(
      final @Nonnull String group,
      final @Nonnull String name)
    {
      this.id = ISimple.id_pool.incrementAndGet();
      this.group = group;
      this.name = name;
    }

    @Override public double interpolableGet()
    {
      return this.value;
    }

    @Override public String interpolableGetGroup()
    {
      return this.group;
    }

    @Override public long interpolableGetID()
    {
      return this.id;
    }

    @Override public String interpolableGetName()
    {
      return this.name;
    }

    @Override public double interpolableMaximum()
    {
      return 1.0;
    }

    @Override public double interpolableMinimum()
    {
      return 0.0;
    }

    @Override public void interpolableSet(
      final double x)
    {
      this.value = x;
    }
  }

  private static class ITrivial implements Interpolable
  {
    private static final AtomicLong id_pool;
    private double                  value = 0.0;
    private final long              id;

    static {
      id_pool = new AtomicLong(0);
    }

    ITrivial()
    {
      this.id = ITrivial.id_pool.incrementAndGet();
    }

    @Override public double interpolableGet()
    {
      return this.value;
    }

    @Override public String interpolableGetGroup()
    {
      return "com.io7m.jtimeline";
    }

    @Override public long interpolableGetID()
    {
      return this.id;
    }

    @Override public String interpolableGetName()
    {
      return "itrivial";
    }

    @Override public double interpolableMaximum()
    {
      return 1.0;
    }

    @Override public double interpolableMinimum()
    {
      return 0.0;
    }

    @Override public void interpolableSet(
      final double x)
    {
      this.value = x;
    }
  }

  private static final class KCallCounter implements
    Procedure<Pair<Interpolable, Keyframe>>
  {
    private int called = 0;

    KCallCounter()
    {

    }

    @Override public void call(
      final Pair<Interpolable, Keyframe> pair)
    {
      this.called = this.called + 1;
    }

    int getCalled()
    {
      return this.called;
    }
  }

  private static <A> boolean exists(
    final @Nonnull List<A> list,
    final @Nonnull Function<A, Boolean> f)
  {
    for (final A x : list) {
      final Boolean r = f.call(x);
      if (r.booleanValue()) {
        return true;
      }
    }
    return false;
  }

  @Test public void testAddGroupedName()
    throws ConstraintError
  {
    final Timeline timeline = new Timeline();
    final IGroup ig0 = new IGroup("one");
    final IGroup ig1 = new IGroup("two");
    timeline.interpolableAdd(ig0);
    timeline.interpolableAdd(ig1);
  }

  @Test(expected = ConstraintError.class) public void testAddNull()
    throws ConstraintError
  {
    final Timeline timeline = new Timeline();
    timeline.interpolableAdd(null);
  }

  @Test(expected = ConstraintError.class) public void testAddNullGroup()
    throws ConstraintError
  {
    final Timeline timeline = new Timeline();
    final INullGroup ing = new INullGroup();
    timeline.interpolableAdd(ing);
  }

  @Test(expected = ConstraintError.class) public void testAddNullName()
    throws ConstraintError
  {
    final Timeline timeline = new Timeline();
    final INullName inn = new INullName();
    timeline.interpolableAdd(inn);
  }

  @Test public void testGetGroups()
    throws ConstraintError
  {
    final Timeline timeline = new Timeline();
    final ISimple ig0s0 = new ISimple("group0", "g0-name0");
    final ISimple ig0s1 = new ISimple("group0", "g0-name1");
    final ISimple ig0s2 = new ISimple("group0", "g0-name2");
    final ISimple ig1s0 = new ISimple("group1", "g1-name0");
    final ISimple ig1s1 = new ISimple("group1", "g1-name1");

    timeline.interpolableAdd(ig0s0);
    timeline.interpolableAdd(ig0s1);
    timeline.interpolableAdd(ig0s2);
    timeline.interpolableAdd(ig1s0);
    timeline.interpolableAdd(ig1s1);

    final ArrayList<Interpolable> g0 = timeline.getGroup("group0");
    final ArrayList<Interpolable> g1 = timeline.getGroup("group1");

    Assert.assertEquals(3, g0.size());
    Assert.assertEquals(2, g1.size());

    Assert.assertTrue(TimelineTest.exists(
      g0,
      new Function<Interpolable, Boolean>() {
        @SuppressWarnings("boxing") @Override public Boolean call(
          final Interpolable i)
        {
          return i.interpolableGetName().equals("g0-name0")
            && i.interpolableGetGroup().equals("group0");
        }
      }));

    Assert.assertTrue(TimelineTest.exists(
      g0,
      new Function<Interpolable, Boolean>() {
        @SuppressWarnings("boxing") @Override public Boolean call(
          final Interpolable i)
        {
          return i.interpolableGetName().equals("g0-name1")
            && i.interpolableGetGroup().equals("group0");
        }
      }));

    Assert.assertTrue(TimelineTest.exists(
      g0,
      new Function<Interpolable, Boolean>() {
        @SuppressWarnings("boxing") @Override public Boolean call(
          final Interpolable i)
        {
          return i.interpolableGetName().equals("g0-name2")
            && i.interpolableGetGroup().equals("group0");
        }
      }));

    Assert.assertTrue(TimelineTest.exists(
      g1,
      new Function<Interpolable, Boolean>() {
        @SuppressWarnings("boxing") @Override public Boolean call(
          final Interpolable i)
        {
          return i.interpolableGetName().equals("g1-name0")
            && i.interpolableGetGroup().equals("group1");
        }
      }));

    Assert.assertTrue(TimelineTest.exists(
      g1,
      new Function<Interpolable, Boolean>() {
        @SuppressWarnings("boxing") @Override public Boolean call(
          final Interpolable i)
        {
          return i.interpolableGetName().equals("g1-name1")
            && i.interpolableGetGroup().equals("group1");
        }
      }));
  }

  @Test public void testInitialize()
  {
    final Timeline timeline = new Timeline();
    Assert.assertEquals(0, timeline.currentTimeGet());
  }

  @Test public void testKeyframe2OneStep()
    throws ConstraintError
  {
    final Timeline timeline = new Timeline();
    final ITrivial it = new ITrivial();

    final KCallCounter kc = new KCallCounter();
    final Keyframe k0 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0.5, kc);
    final Keyframe k1 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 1, 0.5, kc);

    timeline.interpolableAdd(it);
    timeline.keyframeAdd(it, k0);
    timeline.keyframeAdd(it, k1);

    Assert.assertEquals(0, timeline.currentTimeGet());
    Assert.assertEquals(0, kc.getCalled());
    timeline.step();
    Assert.assertEquals(1, timeline.currentTimeGet());
    Assert.assertEquals(1, kc.getCalled());
    timeline.step();
    Assert.assertEquals(2, timeline.currentTimeGet());
    Assert.assertEquals(2, kc.getCalled());
  }

  @Test(expected = ConstraintError.class) public void testKeyframeAddTwice()
    throws ConstraintError
  {
    Timeline timeline = null;
    ITrivial it = null;
    Keyframe k = null;

    try {
      timeline = new Timeline();
      it = new ITrivial();
      k =
        new Keyframe(
          InterpolationType.INTERPOLATE_LINEAR,
          0,
          0.5,
          new Procedure<Pair<Interpolable, Keyframe>>() {
            @Override public void call(
              final Pair<Interpolable, Keyframe> x)
            {
              // TODO Auto-generated method stub
            }
          });
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    timeline.interpolableAdd(it);
    timeline.keyframeAdd(it, k);
    timeline.keyframeAdd(it, k);
  }

  @Test public void testKeyframeInterpExp()
    throws ConstraintError
  {
    final Timeline timeline = new Timeline();
    final ITrivial it = new ITrivial();

    final KCallCounter counter = new KCallCounter();
    final Keyframe k0 =
      new Keyframe(InterpolationType.INTERPOLATE_EXPONENTIAL, 0, 0.0, counter);
    final Keyframe k10 =
      new Keyframe(
        InterpolationType.INTERPOLATE_EXPONENTIAL,
        10,
        1.0,
        counter);
    final Keyframe k20 =
      new Keyframe(
        InterpolationType.INTERPOLATE_EXPONENTIAL,
        20,
        0.0,
        counter);

    timeline.interpolableAdd(it);
    timeline.keyframeAdd(it, k0);
    timeline.keyframeAdd(it, k10);
    timeline.keyframeAdd(it, k20);

    Assert.assertEquals(0, counter.getCalled());

    {
      Assert.assertEquals(0, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(1, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.01));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(2, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.04));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(3, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.09));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(4, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.16));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(5, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.25));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(6, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.36));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(7, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(
        x,
        0.48999999999999994));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(8, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.64));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(9, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.81));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(10, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(11, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.99));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(12, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.96));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(13, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.91));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(14, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.84));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(15, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.75));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(16, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.64));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(17, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.51));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(18, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.36));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(19, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.19));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(20, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
      Assert.assertEquals(3, counter.getCalled());
    }

    {
      Assert.assertEquals(21, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
      Assert.assertEquals(3, counter.getCalled());
    }
  }

  @Test public void testKeyframeInterpLinear()
    throws ConstraintError
  {
    final Timeline timeline = new Timeline();
    final ITrivial it = new ITrivial();

    final KCallCounter counter = new KCallCounter();
    final Keyframe k0 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0.0, counter);
    final Keyframe k10 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 10, 1.0, counter);
    final Keyframe k20 =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 20, 0.0, counter);

    timeline.interpolableAdd(it);
    timeline.keyframeAdd(it, k0);
    timeline.keyframeAdd(it, k10);
    timeline.keyframeAdd(it, k20);

    Assert.assertEquals(0, counter.getCalled());

    {
      Assert.assertEquals(0, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(1, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.1));
    }

    {
      Assert.assertEquals(2, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.2));
    }

    {
      Assert.assertEquals(3, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.3));
    }

    {
      Assert.assertEquals(4, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.4));
    }

    {
      Assert.assertEquals(5, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.5));
    }

    {
      Assert.assertEquals(6, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.6));
    }

    {
      Assert.assertEquals(7, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.7));
    }

    {
      Assert.assertEquals(8, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.8));
    }

    {
      Assert.assertEquals(9, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.9));
    }

    {
      Assert.assertEquals(1, counter.getCalled());
      Assert.assertEquals(10, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(11, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.9));
    }

    {
      Assert.assertEquals(12, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.8));
    }

    {
      Assert.assertEquals(13, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.7));
    }

    {
      Assert.assertEquals(14, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.6));
    }

    {
      Assert.assertEquals(15, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.5));
    }

    {
      Assert.assertEquals(16, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.4));
    }

    {
      Assert.assertEquals(17, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.3));
    }

    {
      Assert.assertEquals(18, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.2));
    }

    {
      Assert.assertEquals(19, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.1));
    }

    {
      Assert.assertEquals(2, counter.getCalled());
      Assert.assertEquals(20, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
      Assert.assertEquals(3, counter.getCalled());
    }

    {
      Assert.assertEquals(21, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }
  }

  @Test public void testKeyframeInterpLog()
    throws ConstraintError
  {
    final Timeline timeline = new Timeline();
    final ITrivial it = new ITrivial();

    final KCallCounter counter = new KCallCounter();
    final Keyframe k0 =
      new Keyframe(InterpolationType.INTERPOLATE_LOGARITHMIC, 0, 0.0, counter);
    final Keyframe k10 =
      new Keyframe(
        InterpolationType.INTERPOLATE_LOGARITHMIC,
        10,
        1.0,
        counter);
    final Keyframe k20 =
      new Keyframe(
        InterpolationType.INTERPOLATE_LOGARITHMIC,
        20,
        0.0,
        counter);

    timeline.interpolableAdd(it);
    timeline.keyframeAdd(it, k0);
    timeline.keyframeAdd(it, k10);
    timeline.keyframeAdd(it, k20);

    Assert.assertEquals(0, counter.getCalled());

    {
      Assert.assertEquals(0, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(1, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(
        x,
        0.31622776601683794));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(2, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(
        x,
        0.4472135954999579));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(3, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(
        x,
        0.5477225575051661));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(4, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(
        x,
        0.6324555320336759));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(5, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(
        x,
        0.7071067811865476));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(6, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(
        x,
        0.7745966692414834));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(7, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(
        x,
        0.8366600265340756));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(8, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(
        x,
        0.8944271909999159));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(9, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(
        x,
        0.9486832980505138));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(10, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(11, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(
        x,
        0.683772233983162));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(12, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(
        x,
        0.5527864045000421));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(13, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(
        x,
        0.4522774424948339));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(14, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(
        x,
        0.3675444679663241));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(15, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(
        x,
        0.2928932188134524));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(16, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(
        x,
        0.2254033307585166));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(17, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(
        x,
        0.16333997346592444));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(18, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(
        x,
        0.10557280900008414));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(19, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(
        x,
        0.05131670194948623));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(20, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
      Assert.assertEquals(3, counter.getCalled());
    }

    {
      Assert.assertEquals(21, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
      Assert.assertEquals(3, counter.getCalled());
    }
  }

  @Test public void testKeyframeInterpStepMax()
    throws ConstraintError
  {
    final Timeline timeline = new Timeline();
    final ITrivial it = new ITrivial();

    final KCallCounter counter = new KCallCounter();
    final Keyframe k0 =
      new Keyframe(
        InterpolationType.INTERPOLATE_STEP_MAXIMUM,
        0,
        0.0,
        counter);
    final Keyframe k10 =
      new Keyframe(
        InterpolationType.INTERPOLATE_STEP_MAXIMUM,
        10,
        1.0,
        counter);
    final Keyframe k20 =
      new Keyframe(
        InterpolationType.INTERPOLATE_STEP_MAXIMUM,
        20,
        0.0,
        counter);

    timeline.interpolableAdd(it);
    timeline.keyframeAdd(it, k0);
    timeline.keyframeAdd(it, k10);
    timeline.keyframeAdd(it, k20);

    Assert.assertEquals(0, counter.getCalled());

    {
      Assert.assertEquals(0, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(1, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
    }

    {
      Assert.assertEquals(2, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
    }

    {
      Assert.assertEquals(3, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
    }

    {
      Assert.assertEquals(4, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
    }

    {
      Assert.assertEquals(5, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
    }

    {
      Assert.assertEquals(6, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
    }

    {
      Assert.assertEquals(7, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
    }

    {
      Assert.assertEquals(8, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
    }

    {
      Assert.assertEquals(9, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
    }

    {
      Assert.assertEquals(1, counter.getCalled());
      Assert.assertEquals(10, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(11, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }

    {
      Assert.assertEquals(12, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }

    {
      Assert.assertEquals(13, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }

    {
      Assert.assertEquals(14, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }

    {
      Assert.assertEquals(15, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }

    {
      Assert.assertEquals(16, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }

    {
      Assert.assertEquals(17, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }

    {
      Assert.assertEquals(18, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }

    {
      Assert.assertEquals(19, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }

    {
      Assert.assertEquals(2, counter.getCalled());
      Assert.assertEquals(20, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
      Assert.assertEquals(3, counter.getCalled());
    }

    {
      Assert.assertEquals(21, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }
  }

  @Test public void testKeyframeInterpStepMin()
    throws ConstraintError
  {
    final Timeline timeline = new Timeline();
    final ITrivial it = new ITrivial();

    final KCallCounter counter = new KCallCounter();
    final Keyframe k0 =
      new Keyframe(
        InterpolationType.INTERPOLATE_STEP_MINIMUM,
        0,
        0.0,
        counter);
    final Keyframe k10 =
      new Keyframe(
        InterpolationType.INTERPOLATE_STEP_MINIMUM,
        10,
        1.0,
        counter);
    final Keyframe k20 =
      new Keyframe(
        InterpolationType.INTERPOLATE_STEP_MINIMUM,
        20,
        0.0,
        counter);

    timeline.interpolableAdd(it);
    timeline.keyframeAdd(it, k0);
    timeline.keyframeAdd(it, k10);
    timeline.keyframeAdd(it, k20);

    Assert.assertEquals(0, counter.getCalled());

    {
      Assert.assertEquals(0, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
      Assert.assertEquals(1, counter.getCalled());
    }

    {
      Assert.assertEquals(1, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }

    {
      Assert.assertEquals(2, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }

    {
      Assert.assertEquals(3, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }

    {
      Assert.assertEquals(4, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }

    {
      Assert.assertEquals(5, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }

    {
      Assert.assertEquals(6, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }

    {
      Assert.assertEquals(7, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }

    {
      Assert.assertEquals(8, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }

    {
      Assert.assertEquals(9, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }

    {
      Assert.assertEquals(1, counter.getCalled());
      Assert.assertEquals(10, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
      Assert.assertEquals(2, counter.getCalled());
    }

    {
      Assert.assertEquals(11, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
    }

    {
      Assert.assertEquals(12, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
    }

    {
      Assert.assertEquals(13, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
    }

    {
      Assert.assertEquals(14, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
    }

    {
      Assert.assertEquals(15, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
    }

    {
      Assert.assertEquals(16, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
    }

    {
      Assert.assertEquals(17, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
    }

    {
      Assert.assertEquals(18, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
    }

    {
      Assert.assertEquals(19, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 1.0));
    }

    {
      Assert.assertEquals(2, counter.getCalled());
      Assert.assertEquals(20, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
      Assert.assertEquals(3, counter.getCalled());
    }

    {
      Assert.assertEquals(21, timeline.currentTimeGet());
      timeline.step();
      final double x = it.interpolableGet();
      Assert.assertTrue(ApproximatelyEqualDouble.approximatelyEqual(x, 0.0));
    }
  }

  @Test(expected = ConstraintError.class) public
    void
    testKeyframeNoInterpolable()
      throws ConstraintError
  {
    Timeline timeline = null;
    ITrivial it = null;
    Keyframe k = null;

    try {
      timeline = new Timeline();
      it = new ITrivial();
      k =
        new Keyframe(
          InterpolationType.INTERPOLATE_LINEAR,
          0,
          0.5,
          new Procedure<Pair<Interpolable, Keyframe>>() {
            @Override public void call(
              final Pair<Interpolable, Keyframe> x)
            {
              // TODO Auto-generated method stub
            }
          });
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    timeline.keyframeAdd(it, k);
  }

  @Test public void testKeyframeOneStep()
    throws ConstraintError
  {
    final Timeline timeline = new Timeline();
    final ITrivial it = new ITrivial();

    final KCallCounter kc = new KCallCounter();
    final Keyframe k =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0.5, kc);

    timeline.interpolableAdd(it);
    timeline.keyframeAdd(it, k);

    Assert.assertEquals(0, timeline.currentTimeGet());
    Assert.assertEquals(0, kc.getCalled());
    timeline.step();
    Assert.assertEquals(1, timeline.currentTimeGet());
    Assert.assertEquals(1, kc.getCalled());
  }

  @Test public void testKeyframeSetTime()
    throws ConstraintError
  {
    final Timeline timeline = new Timeline();
    final ITrivial it = new ITrivial();

    final KCallCounter kc = new KCallCounter();
    final Keyframe k =
      new Keyframe(InterpolationType.INTERPOLATE_LINEAR, 0, 0.5, kc);

    timeline.interpolableAdd(it);
    timeline.keyframeAdd(it, k);

    Assert.assertEquals(0, timeline.currentTimeGet());
    Assert.assertEquals(0, kc.getCalled());
    timeline.step();
    Assert.assertEquals(1, timeline.currentTimeGet());
    Assert.assertEquals(1, kc.getCalled());
    timeline.step();
    Assert.assertEquals(2, timeline.currentTimeGet());
    Assert.assertEquals(1, kc.getCalled());
    timeline.step();
    Assert.assertEquals(3, timeline.currentTimeGet());
    Assert.assertEquals(1, kc.getCalled());

    timeline.currentTimeSet(0);

    Assert.assertEquals(0, timeline.currentTimeGet());
    Assert.assertEquals(1, kc.getCalled());
    timeline.step();
    Assert.assertEquals(1, timeline.currentTimeGet());
    Assert.assertEquals(2, kc.getCalled());
    timeline.step();
    Assert.assertEquals(2, timeline.currentTimeGet());
    Assert.assertEquals(2, kc.getCalled());
    timeline.step();
    Assert.assertEquals(3, timeline.currentTimeGet());
    Assert.assertEquals(2, kc.getCalled());
  }

  @Test public void testStepNothing()
    throws ConstraintError
  {
    final Timeline timeline = new Timeline();
    final ITrivial it = new ITrivial();
    timeline.interpolableAdd(it);

    Assert.assertEquals(0, timeline.currentTimeGet());
    Assert.assertTrue(0.0 == it.interpolableGet());
    timeline.step();
    Assert.assertEquals(1, timeline.currentTimeGet());
    Assert.assertTrue(0.0 == it.interpolableGet());
  }
}
