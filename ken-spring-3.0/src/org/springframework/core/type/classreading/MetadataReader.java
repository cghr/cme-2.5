package org.springframework.core.type.classreading;

import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;

public abstract interface MetadataReader
{
  public abstract Resource getResource();
  
  public abstract ClassMetadata getClassMetadata();
  
  public abstract AnnotationMetadata getAnnotationMetadata();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.classreading.MetadataReader
 * JD-Core Version:    0.7.0.1
 */