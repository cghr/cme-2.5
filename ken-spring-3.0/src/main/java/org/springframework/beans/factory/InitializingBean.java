package org.springframework.beans.factory;

public abstract interface InitializingBean
{
  public abstract void afterPropertiesSet()
    throws Exception;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.InitializingBean
 * JD-Core Version:    0.7.0.1
 */