package org.springframework.remoting.jaxrpc;

import javax.xml.rpc.Service;

@Deprecated
public abstract interface JaxRpcServicePostProcessor
{
  public abstract void postProcessJaxRpcService(Service paramService);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.jaxrpc.JaxRpcServicePostProcessor
 * JD-Core Version:    0.7.0.1
 */