package org.springframework.beans.factory.config;

import java.lang.reflect.Constructor;
import org.springframework.beans.BeansException;

public abstract interface SmartInstantiationAwareBeanPostProcessor
  extends InstantiationAwareBeanPostProcessor
{
  public abstract Class<?> predictBeanType(Class<?> paramClass, String paramString)
    throws BeansException;
  
  public abstract Constructor<?>[] determineCandidateConstructors(Class<?> paramClass, String paramString)
    throws BeansException;
  
  public abstract Object getEarlyBeanReference(Object paramObject, String paramString)
    throws BeansException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor
 * JD-Core Version:    0.7.0.1
 */