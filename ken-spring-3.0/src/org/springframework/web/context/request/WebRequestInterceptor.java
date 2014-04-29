package org.springframework.web.context.request;

import org.springframework.ui.ModelMap;

public abstract interface WebRequestInterceptor
{
  public abstract void preHandle(WebRequest paramWebRequest)
    throws Exception;
  
  public abstract void postHandle(WebRequest paramWebRequest, ModelMap paramModelMap)
    throws Exception;
  
  public abstract void afterCompletion(WebRequest paramWebRequest, Exception paramException)
    throws Exception;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.request.WebRequestInterceptor
 * JD-Core Version:    0.7.0.1
 */