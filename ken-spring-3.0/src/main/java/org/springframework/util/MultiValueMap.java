package org.springframework.util;

import java.util.List;
import java.util.Map;

public abstract interface MultiValueMap<K, V>
  extends Map<K, List<V>>
{
  public abstract V getFirst(K paramK);
  
  public abstract void add(K paramK, V paramV);
  
  public abstract void set(K paramK, V paramV);
  
  public abstract void setAll(Map<K, V> paramMap);
  
  public abstract Map<K, V> toSingleValueMap();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.MultiValueMap
 * JD-Core Version:    0.7.0.1
 */