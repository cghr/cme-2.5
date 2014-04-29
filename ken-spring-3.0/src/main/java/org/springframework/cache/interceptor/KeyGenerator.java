package org.springframework.cache.interceptor;

import java.lang.reflect.Method;

public abstract interface KeyGenerator
{
  public abstract Object extract(Object paramObject, Method paramMethod, Object... paramVarArgs);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.interceptor.KeyGenerator
 * JD-Core Version:    0.7.0.1
 */