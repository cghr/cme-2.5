package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;

public abstract interface RequestToViewNameTranslator
{
  public abstract String getViewName(HttpServletRequest paramHttpServletRequest)
    throws Exception;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.RequestToViewNameTranslator
 * JD-Core Version:    0.7.0.1
 */