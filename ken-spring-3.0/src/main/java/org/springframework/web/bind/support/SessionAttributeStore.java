package org.springframework.web.bind.support;

import org.springframework.web.context.request.WebRequest;

public abstract interface SessionAttributeStore
{
  public abstract void storeAttribute(WebRequest paramWebRequest, String paramString, Object paramObject);
  
  public abstract Object retrieveAttribute(WebRequest paramWebRequest, String paramString);
  
  public abstract void cleanupAttribute(WebRequest paramWebRequest, String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.support.SessionAttributeStore
 * JD-Core Version:    0.7.0.1
 */