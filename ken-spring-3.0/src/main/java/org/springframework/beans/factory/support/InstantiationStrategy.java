package org.springframework.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

public abstract interface InstantiationStrategy
{
  public abstract Object instantiate(RootBeanDefinition paramRootBeanDefinition, String paramString, BeanFactory paramBeanFactory)
    throws BeansException;
  
  public abstract Object instantiate(RootBeanDefinition paramRootBeanDefinition, String paramString, BeanFactory paramBeanFactory, Constructor<?> paramConstructor, Object[] paramArrayOfObject)
    throws BeansException;
  
  public abstract Object instantiate(RootBeanDefinition paramRootBeanDefinition, String paramString, BeanFactory paramBeanFactory, Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
    throws BeansException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.InstantiationStrategy
 * JD-Core Version:    0.7.0.1
 */