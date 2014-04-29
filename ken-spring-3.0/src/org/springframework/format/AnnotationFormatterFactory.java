package org.springframework.format;

import java.lang.annotation.Annotation;
import java.util.Set;

public abstract interface AnnotationFormatterFactory<A extends Annotation>
{
  public abstract Set<Class<?>> getFieldTypes();
  
  public abstract Printer<?> getPrinter(A paramA, Class<?> paramClass);
  
  public abstract Parser<?> getParser(A paramA, Class<?> paramClass);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.AnnotationFormatterFactory
 * JD-Core Version:    0.7.0.1
 */