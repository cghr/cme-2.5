package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract interface HandlerAdapter
{
  public abstract boolean supports(Object paramObject);
  
  public abstract ModelAndView handle(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Object paramObject)
    throws Exception;
  
  public abstract long getLastModified(HttpServletRequest paramHttpServletRequest, Object paramObject);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.HandlerAdapter
 * JD-Core Version:    0.7.0.1
 */