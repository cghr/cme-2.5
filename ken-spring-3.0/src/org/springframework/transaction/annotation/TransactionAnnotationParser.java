package org.springframework.transaction.annotation;

import java.lang.reflect.AnnotatedElement;
import org.springframework.transaction.interceptor.TransactionAttribute;

public abstract interface TransactionAnnotationParser
{
  public abstract TransactionAttribute parseTransactionAnnotation(AnnotatedElement paramAnnotatedElement);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.annotation.TransactionAnnotationParser
 * JD-Core Version:    0.7.0.1
 */