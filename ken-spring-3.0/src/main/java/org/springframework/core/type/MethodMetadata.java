package org.springframework.core.type;

import java.util.Map;

public abstract interface MethodMetadata
{
  public abstract String getMethodName();
  
  public abstract String getDeclaringClassName();
  
  public abstract boolean isStatic();
  
  public abstract boolean isFinal();
  
  public abstract boolean isOverridable();
  
  public abstract boolean isAnnotated(String paramString);
  
  public abstract Map<String, Object> getAnnotationAttributes(String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.MethodMetadata
 * JD-Core Version:    0.7.0.1
 */