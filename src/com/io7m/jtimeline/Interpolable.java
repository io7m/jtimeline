package com.io7m.jtimeline;

import javax.annotation.Nonnull;

public interface Interpolable
{
  double interpolableGet();

  @Nonnull String interpolableGetGroup();

  long interpolableGetID();

  @Nonnull String interpolableGetName();

  double interpolableMaximum();

  double interpolableMinimum();

  void interpolableSet(
    final double x);
}
