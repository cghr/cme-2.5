package org.springframework.cache.annotation;

import java.lang.reflect.AnnotatedElement;
import org.springframework.cache.interceptor.CacheOperation;

public abstract interface CacheAnnotationParser
{
  public abstract CacheOperation parseCacheAnnotation(AnnotatedElement paramAnnotatedElement);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.annotation.CacheAnnotationParser
 * JD-Core Version:    0.7.0.1
 */