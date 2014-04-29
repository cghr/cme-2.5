package org.springframework.cache.interceptor;

import java.lang.reflect.Method;

public abstract interface CacheOperationSource
{
  public abstract CacheOperation getCacheOperation(Method paramMethod, Class<?> paramClass);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.interceptor.CacheOperationSource
 * JD-Core Version:    0.7.0.1
 */