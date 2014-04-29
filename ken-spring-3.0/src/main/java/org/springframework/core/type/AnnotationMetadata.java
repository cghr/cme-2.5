package org.springframework.core.type;

import java.util.Map;
import java.util.Set;

public abstract interface AnnotationMetadata
  extends ClassMetadata
{
  public abstract Set<String> getAnnotationTypes();
  
  public abstract Set<String> getMetaAnnotationTypes(String paramString);
  
  public abstract boolean hasAnnotation(String paramString);
  
  public abstract boolean hasMetaAnnotation(String paramString);
  
  public abstract boolean isAnnotated(String paramString);
  
  public abstract Map<String, Object> getAnnotationAttributes(String paramString);
  
  public abstract Map<String, Object> getAnnotationAttributes(String paramString, boolean paramBoolean);
  
  public abstract boolean hasAnnotatedMethods(String paramString);
  
  public abstract Set<MethodMetadata> getAnnotatedMethods(String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.AnnotationMetadata
 * JD-Core Version:    0.7.0.1
 */