package org.springframework.beans.factory;

public abstract interface SmartFactoryBean<T>
  extends FactoryBean<T>
{
  public abstract boolean isPrototype();
  
  public abstract boolean isEagerInit();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.SmartFactoryBean
 * JD-Core Version:    0.7.0.1
 */