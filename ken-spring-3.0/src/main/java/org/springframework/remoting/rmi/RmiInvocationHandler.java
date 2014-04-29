package org.springframework.remoting.rmi;

import java.lang.reflect.InvocationTargetException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import org.springframework.remoting.support.RemoteInvocation;

public abstract interface RmiInvocationHandler
  extends Remote
{
  public abstract String getTargetInterfaceName()
    throws RemoteException;
  
  public abstract Object invoke(RemoteInvocation paramRemoteInvocation)
    throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.rmi.RmiInvocationHandler
 * JD-Core Version:    0.7.0.1
 */