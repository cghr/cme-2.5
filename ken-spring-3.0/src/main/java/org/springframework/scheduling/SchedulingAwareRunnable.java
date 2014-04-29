package org.springframework.scheduling;

public abstract interface SchedulingAwareRunnable
  extends Runnable
{
  public abstract boolean isLongLived();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.SchedulingAwareRunnable
 * JD-Core Version:    0.7.0.1
 */