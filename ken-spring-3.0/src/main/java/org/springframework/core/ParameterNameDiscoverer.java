package org.springframework.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public abstract interface ParameterNameDiscoverer
{
  public abstract String[] getParameterNames(Method paramMethod);
  
  public abstract String[] getParameterNames(Constructor paramConstructor);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.ParameterNameDiscoverer
 * JD-Core Version:    0.7.0.1
 */