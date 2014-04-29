package org.springframework.core;

public abstract interface AttributeAccessor
{
  public abstract void setAttribute(String paramString, Object paramObject);
  
  public abstract Object getAttribute(String paramString);
  
  public abstract Object removeAttribute(String paramString);
  
  public abstract boolean hasAttribute(String paramString);
  
  public abstract String[] attributeNames();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.AttributeAccessor
 * JD-Core Version:    0.7.0.1
 */