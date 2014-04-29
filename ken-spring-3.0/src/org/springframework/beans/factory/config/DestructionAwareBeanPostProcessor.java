package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

public abstract interface DestructionAwareBeanPostProcessor
  extends BeanPostProcessor
{
  public abstract void postProcessBeforeDestruction(Object paramObject, String paramString)
    throws BeansException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor
 * JD-Core Version:    0.7.0.1
 */