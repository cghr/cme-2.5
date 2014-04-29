package org.springframework.web.bind.support;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;

public abstract interface WebDataBinderFactory
{
  public abstract WebDataBinder createBinder(NativeWebRequest paramNativeWebRequest, Object paramObject, String paramString)
    throws Exception;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.support.WebDataBinderFactory
 * JD-Core Version:    0.7.0.1
 */