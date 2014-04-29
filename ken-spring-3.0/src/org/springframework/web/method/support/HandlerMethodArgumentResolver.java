package org.springframework.web.method.support;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;

public abstract interface HandlerMethodArgumentResolver
{
  public abstract boolean supportsParameter(MethodParameter paramMethodParameter);
  
  public abstract Object resolveArgument(MethodParameter paramMethodParameter, ModelAndViewContainer paramModelAndViewContainer, NativeWebRequest paramNativeWebRequest, WebDataBinderFactory paramWebDataBinderFactory)
    throws Exception;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.support.HandlerMethodArgumentResolver
 * JD-Core Version:    0.7.0.1
 */