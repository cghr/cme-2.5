package org.springframework.core.env;

public abstract interface PropertySources
  extends Iterable<PropertySource<?>>
{
  public abstract boolean contains(String paramString);
  
  public abstract PropertySource<?> get(String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.PropertySources
 * JD-Core Version:    0.7.0.1
 */