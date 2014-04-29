package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

public abstract interface BeanPostProcessor
{
  public abstract Object postProcessBeforeInitialization(Object paramObject, String paramString)
    throws BeansException;
  
  public abstract Object postProcessAfterInitialization(Object paramObject, String paramString)
    throws BeansException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.BeanPostProcessor
 * JD-Core Version:    0.7.0.1
 */