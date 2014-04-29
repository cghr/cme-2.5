package org.springframework.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public abstract interface WebApplicationInitializer
{
  public abstract void onStartup(ServletContext paramServletContext)
    throws ServletException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.WebApplicationInitializer
 * JD-Core Version:    0.7.0.1
 */