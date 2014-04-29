package org.springframework.web.method.support;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;

public abstract interface HandlerMethodReturnValueHandler
{
  public abstract boolean supportsReturnType(MethodParameter paramMethodParameter);
  
  public abstract void handleReturnValue(Object paramObject, MethodParameter paramMethodParameter, ModelAndViewContainer paramModelAndViewContainer, NativeWebRequest paramNativeWebRequest)
    throws Exception;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.support.HandlerMethodReturnValueHandler
 * JD-Core Version:    0.7.0.1
 */