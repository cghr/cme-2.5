package org.springframework.scheduling;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

public abstract interface TaskScheduler
{
  public abstract ScheduledFuture schedule(Runnable paramRunnable, Trigger paramTrigger);
  
  public abstract ScheduledFuture schedule(Runnable paramRunnable, Date paramDate);
  
  public abstract ScheduledFuture scheduleAtFixedRate(Runnable paramRunnable, Date paramDate, long paramLong);
  
  public abstract ScheduledFuture scheduleAtFixedRate(Runnable paramRunnable, long paramLong);
  
  public abstract ScheduledFuture scheduleWithFixedDelay(Runnable paramRunnable, Date paramDate, long paramLong);
  
  public abstract ScheduledFuture scheduleWithFixedDelay(Runnable paramRunnable, long paramLong);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.TaskScheduler
 * JD-Core Version:    0.7.0.1
 */