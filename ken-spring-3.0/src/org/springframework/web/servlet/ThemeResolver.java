package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract interface ThemeResolver
{
  public abstract String resolveThemeName(HttpServletRequest paramHttpServletRequest);
  
  public abstract void setThemeName(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.ThemeResolver
 * JD-Core Version:    0.7.0.1
 */