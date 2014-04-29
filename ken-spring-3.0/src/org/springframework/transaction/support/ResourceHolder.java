package org.springframework.transaction.support;

public abstract interface ResourceHolder
{
  public abstract void reset();
  
  public abstract void unbound();
  
  public abstract boolean isVoid();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.ResourceHolder
 * JD-Core Version:    0.7.0.1
 */