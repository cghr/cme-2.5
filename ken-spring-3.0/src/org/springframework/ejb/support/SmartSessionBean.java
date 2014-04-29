package org.springframework.ejb.support;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

public abstract interface SmartSessionBean
  extends SessionBean
{
  public abstract SessionContext getSessionContext();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ejb.support.SmartSessionBean
 * JD-Core Version:    0.7.0.1
 */