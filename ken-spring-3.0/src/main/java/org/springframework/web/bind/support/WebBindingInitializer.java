package org.springframework.web.bind.support;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.WebRequest;

public abstract interface WebBindingInitializer
{
  public abstract void initBinder(WebDataBinder paramWebDataBinder, WebRequest paramWebRequest);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.support.WebBindingInitializer
 * JD-Core Version:    0.7.0.1
 */