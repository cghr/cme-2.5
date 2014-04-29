package org.springframework.validation;

import org.springframework.beans.PropertyAccessException;

public abstract interface BindingErrorProcessor
{
  public abstract void processMissingFieldError(String paramString, BindingResult paramBindingResult);
  
  public abstract void processPropertyAccessException(PropertyAccessException paramPropertyAccessException, BindingResult paramBindingResult);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.BindingErrorProcessor
 * JD-Core Version:    0.7.0.1
 */