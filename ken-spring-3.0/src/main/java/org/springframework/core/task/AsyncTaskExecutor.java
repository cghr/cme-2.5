package org.springframework.core.task;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public abstract interface AsyncTaskExecutor
  extends TaskExecutor
{
  public static final long TIMEOUT_IMMEDIATE = 0L;
  public static final long TIMEOUT_INDEFINITE = 9223372036854775807L;
  
  public abstract void execute(Runnable paramRunnable, long paramLong);
  
  public abstract Future<?> submit(Runnable paramRunnable);
  
  public abstract <T> Future<T> submit(Callable<T> paramCallable);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.task.AsyncTaskExecutor
 * JD-Core Version:    0.7.0.1
 */