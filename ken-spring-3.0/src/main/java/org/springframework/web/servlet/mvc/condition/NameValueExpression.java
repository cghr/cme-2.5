package org.springframework.web.servlet.mvc.condition;

public abstract interface NameValueExpression<T>
{
  public abstract String getName();
  
  public abstract T getValue();
  
  public abstract boolean isNegated();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.condition.NameValueExpression
 * JD-Core Version:    0.7.0.1
 */