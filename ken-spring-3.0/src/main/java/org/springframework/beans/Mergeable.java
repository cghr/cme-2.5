package org.springframework.beans;

public abstract interface Mergeable
{
  public abstract boolean isMergeEnabled();
  
  public abstract Object merge(Object paramObject);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.Mergeable
 * JD-Core Version:    0.7.0.1
 */