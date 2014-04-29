package org.springframework.core.env;

public abstract interface PropertyResolver
{
  public abstract boolean containsProperty(String paramString);
  
  public abstract String getProperty(String paramString);
  
  public abstract String getProperty(String paramString1, String paramString2);
  
  public abstract <T> T getProperty(String paramString, Class<T> paramClass);
  
  public abstract <T> T getProperty(String paramString, Class<T> paramClass, T paramT);
  
  public abstract <T> Class<T> getPropertyAsClass(String paramString, Class<T> paramClass);
  
  public abstract String getRequiredProperty(String paramString)
    throws IllegalStateException;
  
  public abstract <T> T getRequiredProperty(String paramString, Class<T> paramClass)
    throws IllegalStateException;
  
  public abstract String resolvePlaceholders(String paramString);
  
  public abstract String resolveRequiredPlaceholders(String paramString)
    throws IllegalArgumentException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.PropertyResolver
 * JD-Core Version:    0.7.0.1
 */