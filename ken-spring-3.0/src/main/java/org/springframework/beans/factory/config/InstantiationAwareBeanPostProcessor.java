package org.springframework.beans.factory.config;

import java.beans.PropertyDescriptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;

public abstract interface InstantiationAwareBeanPostProcessor
  extends BeanPostProcessor
{
  public abstract Object postProcessBeforeInstantiation(Class<?> paramClass, String paramString)
    throws BeansException;
  
  public abstract boolean postProcessAfterInstantiation(Object paramObject, String paramString)
    throws BeansException;
  
  public abstract PropertyValues postProcessPropertyValues(PropertyValues paramPropertyValues, PropertyDescriptor[] paramArrayOfPropertyDescriptor, Object paramObject, String paramString)
    throws BeansException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor
 * JD-Core Version:    0.7.0.1
 */