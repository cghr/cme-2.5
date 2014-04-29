package org.springframework.remoting.support;

import java.lang.reflect.InvocationTargetException;

public abstract interface RemoteInvocationExecutor
{
  public abstract Object invoke(RemoteInvocation paramRemoteInvocation, Object paramObject)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.support.RemoteInvocationExecutor
 * JD-Core Version:    0.7.0.1
 */