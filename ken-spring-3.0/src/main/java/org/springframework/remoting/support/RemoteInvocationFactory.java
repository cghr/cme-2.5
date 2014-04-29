package org.springframework.remoting.support;

import org.aopalliance.intercept.MethodInvocation;

public abstract interface RemoteInvocationFactory
{
  public abstract RemoteInvocation createRemoteInvocation(MethodInvocation paramMethodInvocation);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.support.RemoteInvocationFactory
 * JD-Core Version:    0.7.0.1
 */