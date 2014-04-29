package org.springframework.format;

import java.lang.annotation.Annotation;
import org.springframework.core.convert.converter.ConverterRegistry;

public abstract interface FormatterRegistry
  extends ConverterRegistry
{
  public abstract void addFormatter(Formatter<?> paramFormatter);
  
  public abstract void addFormatterForFieldType(Class<?> paramClass, Formatter<?> paramFormatter);
  
  public abstract void addFormatterForFieldType(Class<?> paramClass, Printer<?> paramPrinter, Parser<?> paramParser);
  
  public abstract void addFormatterForFieldAnnotation(AnnotationFormatterFactory<? extends Annotation> paramAnnotationFormatterFactory);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.FormatterRegistry
 * JD-Core Version:    0.7.0.1
 */