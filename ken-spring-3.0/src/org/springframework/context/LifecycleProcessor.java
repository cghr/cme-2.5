package org.springframework.context;

public abstract interface LifecycleProcessor
  extends Lifecycle
{
  public abstract void onRefresh();
  
  public abstract void onClose();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.LifecycleProcessor
 * JD-Core Version:    0.7.0.1
 */