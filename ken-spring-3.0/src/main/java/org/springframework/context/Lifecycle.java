package org.springframework.context;

public abstract interface Lifecycle
{
  public abstract void start();
  
  public abstract void stop();
  
  public abstract boolean isRunning();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.Lifecycle
 * JD-Core Version:    0.7.0.1
 */