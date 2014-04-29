package org.springframework.beans.factory.config;

public abstract interface SingletonBeanRegistry
{
  public abstract void registerSingleton(String paramString, Object paramObject);
  
  public abstract Object getSingleton(String paramString);
  
  public abstract boolean containsSingleton(String paramString);
  
  public abstract String[] getSingletonNames();
  
  public abstract int getSingletonCount();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.SingletonBeanRegistry
 * JD-Core Version:    0.7.0.1
 */