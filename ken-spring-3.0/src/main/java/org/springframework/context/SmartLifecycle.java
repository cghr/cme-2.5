package org.springframework.context;

public abstract interface SmartLifecycle
  extends Lifecycle, Phased
{
  public abstract boolean isAutoStartup();
  
  public abstract void stop(Runnable paramRunnable);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.SmartLifecycle
 * JD-Core Version:    0.7.0.1
 */