package org.springframework.context.annotation;

import org.springframework.beans.factory.Aware;
import org.springframework.core.type.AnnotationMetadata;

public abstract interface ImportAware
  extends Aware
{
  public abstract void setImportMetadata(AnnotationMetadata paramAnnotationMetadata);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ImportAware
 * JD-Core Version:    0.7.0.1
 */