package org.springframework.web.context.request;

public abstract interface NativeWebRequest
  extends WebRequest
{
  public abstract Object getNativeRequest();
  
  public abstract Object getNativeResponse();
  
  public abstract <T> T getNativeRequest(Class<T> paramClass);
  
  public abstract <T> T getNativeResponse(Class<T> paramClass);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.request.NativeWebRequest
 * JD-Core Version:    0.7.0.1
 */