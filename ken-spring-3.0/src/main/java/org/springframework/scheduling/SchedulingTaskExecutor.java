package org.springframework.scheduling;

import org.springframework.core.task.AsyncTaskExecutor;

public abstract interface SchedulingTaskExecutor
  extends AsyncTaskExecutor
{
  public abstract boolean prefersShortLivedTasks();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.SchedulingTaskExecutor
 * JD-Core Version:    0.7.0.1
 */