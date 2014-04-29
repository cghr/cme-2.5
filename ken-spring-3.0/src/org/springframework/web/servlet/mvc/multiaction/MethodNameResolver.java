package org.springframework.web.servlet.mvc.multiaction;

import javax.servlet.http.HttpServletRequest;

public abstract interface MethodNameResolver
{
  public abstract String getHandlerMethodName(HttpServletRequest paramHttpServletRequest)
    throws NoSuchRequestHandlingMethodException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.multiaction.MethodNameResolver
 * JD-Core Version:    0.7.0.1
 */