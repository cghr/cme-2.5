package org.springframework.core;

import java.util.Map;

@Deprecated
public abstract interface ConcurrentMap
  extends Map
{
  public abstract Object putIfAbsent(Object paramObject1, Object paramObject2);
  
  public abstract boolean remove(Object paramObject1, Object paramObject2);
  
  public abstract boolean replace(Object paramObject1, Object paramObject2, Object paramObject3);
  
  public abstract Object replace(Object paramObject1, Object paramObject2);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.ConcurrentMap
 * JD-Core Version:    0.7.0.1
 */