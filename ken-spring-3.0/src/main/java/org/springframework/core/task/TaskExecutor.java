package org.springframework.core.task;

import java.util.concurrent.Executor;

public abstract interface TaskExecutor
  extends Executor
{
  public abstract void execute(Runnable paramRunnable);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.task.TaskExecutor
 * JD-Core Version:    0.7.0.1
 */