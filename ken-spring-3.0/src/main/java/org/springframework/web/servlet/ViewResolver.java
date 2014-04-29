package org.springframework.web.servlet;

import java.util.Locale;

public abstract interface ViewResolver
{
  public abstract View resolveViewName(String paramString, Locale paramLocale)
    throws Exception;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.ViewResolver
 * JD-Core Version:    0.7.0.1
 */