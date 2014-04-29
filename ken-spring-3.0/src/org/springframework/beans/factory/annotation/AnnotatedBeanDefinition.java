package org.springframework.beans.factory.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.type.AnnotationMetadata;

public abstract interface AnnotatedBeanDefinition
  extends BeanDefinition
{
  public abstract AnnotationMetadata getMetadata();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.annotation.AnnotatedBeanDefinition
 * JD-Core Version:    0.7.0.1
 */