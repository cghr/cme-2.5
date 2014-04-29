package org.springframework.expression;

import java.lang.reflect.Method;
import java.util.List;

public abstract interface MethodFilter
{
  public abstract List<Method> filter(List<Method> paramList);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.MethodFilter
 * JD-Core Version:    0.7.0.1
 */