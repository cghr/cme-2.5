package org.springframework.validation;

public abstract interface Validator
{
  public abstract boolean supports(Class<?> paramClass);
  
  public abstract void validate(Object paramObject, Errors paramErrors);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.Validator
 * JD-Core Version:    0.7.0.1
 */