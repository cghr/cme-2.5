package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

public abstract interface ObjectFactory<T>
{
  public abstract T getObject()
    throws BeansException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.ObjectFactory
 * JD-Core Version:    0.7.0.1
 */