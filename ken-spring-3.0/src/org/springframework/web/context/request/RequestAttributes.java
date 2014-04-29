package org.springframework.web.context.request;

public abstract interface RequestAttributes
{
  public static final int SCOPE_REQUEST = 0;
  public static final int SCOPE_SESSION = 1;
  public static final int SCOPE_GLOBAL_SESSION = 2;
  public static final String REFERENCE_REQUEST = "request";
  public static final String REFERENCE_SESSION = "session";
  
  public abstract Object getAttribute(String paramString, int paramInt);
  
  public abstract void setAttribute(String paramString, Object paramObject, int paramInt);
  
  public abstract void removeAttribute(String paramString, int paramInt);
  
  public abstract String[] getAttributeNames(int paramInt);
  
  public abstract void registerDestructionCallback(String paramString, Runnable paramRunnable, int paramInt);
  
  public abstract Object resolveReference(String paramString);
  
  public abstract String getSessionId();
  
  public abstract Object getSessionMutex();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.request.RequestAttributes
 * JD-Core Version:    0.7.0.1
 */