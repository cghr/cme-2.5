package org.springframework.beans.factory;

public abstract interface FactoryBean<T>
{
  public abstract T getObject()
    throws Exception;
  
  public abstract Class<?> getObjectType();
  
  public abstract boolean isSingleton();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.FactoryBean
 * JD-Core Version:    0.7.0.1
 */