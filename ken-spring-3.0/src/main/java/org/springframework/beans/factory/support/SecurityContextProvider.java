package org.springframework.beans.factory.support;

import java.security.AccessControlContext;

public abstract interface SecurityContextProvider
{
  public abstract AccessControlContext getAccessControlContext();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.SecurityContextProvider
 * JD-Core Version:    0.7.0.1
 */