package org.springframework.remoting.httpinvoker;

import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

public abstract interface HttpInvokerRequestExecutor
{
  public abstract RemoteInvocationResult executeRequest(HttpInvokerClientConfiguration paramHttpInvokerClientConfiguration, RemoteInvocation paramRemoteInvocation)
    throws Exception;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.httpinvoker.HttpInvokerRequestExecutor
 * JD-Core Version:    0.7.0.1
 */