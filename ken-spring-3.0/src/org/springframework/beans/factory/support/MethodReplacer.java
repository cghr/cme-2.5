package org.springframework.beans.factory.support;

import java.lang.reflect.Method;

public abstract interface MethodReplacer
{
  public abstract Object reimplement(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
    throws Throwable;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.MethodReplacer
 * JD-Core Version:    0.7.0.1
 */