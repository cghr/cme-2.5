package org.springframework.scheduling.annotation;

import java.util.concurrent.Executor;

public abstract interface AsyncConfigurer
{
  public abstract Executor getAsyncExecutor();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.annotation.AsyncConfigurer
 * JD-Core Version:    0.7.0.1
 */