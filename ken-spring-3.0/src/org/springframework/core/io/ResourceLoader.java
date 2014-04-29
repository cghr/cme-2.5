package org.springframework.core.io;

public abstract interface ResourceLoader
{
  public static final String CLASSPATH_URL_PREFIX = "classpath:";
  
  public abstract Resource getResource(String paramString);
  
  public abstract ClassLoader getClassLoader();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.ResourceLoader
 * JD-Core Version:    0.7.0.1
 */