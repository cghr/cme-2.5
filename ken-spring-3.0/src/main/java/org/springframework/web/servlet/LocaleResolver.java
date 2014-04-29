package org.springframework.web.servlet;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract interface LocaleResolver
{
  public abstract Locale resolveLocale(HttpServletRequest paramHttpServletRequest);
  
  public abstract void setLocale(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Locale paramLocale);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.LocaleResolver
 * JD-Core Version:    0.7.0.1
 */