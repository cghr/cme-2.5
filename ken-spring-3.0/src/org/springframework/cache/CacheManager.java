package org.springframework.cache;

import java.util.Collection;

public abstract interface CacheManager
{
  public abstract Cache getCache(String paramString);
  
  public abstract Collection<String> getCacheNames();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.CacheManager
 * JD-Core Version:    0.7.0.1
 */