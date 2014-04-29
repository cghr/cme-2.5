package org.springframework.core;

public abstract interface ControlFlow
{
  public abstract boolean under(Class paramClass);
  
  public abstract boolean under(Class paramClass, String paramString);
  
  public abstract boolean underToken(String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.ControlFlow
 * JD-Core Version:    0.7.0.1
 */