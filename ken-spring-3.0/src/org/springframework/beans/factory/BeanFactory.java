package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

public abstract interface BeanFactory
{
  public static final String FACTORY_BEAN_PREFIX = "&";
  
  public abstract Object getBean(String paramString)
    throws BeansException;
  
  public abstract <T> T getBean(String paramString, Class<T> paramClass)
    throws BeansException;
  
  public abstract <T> T getBean(Class<T> paramClass)
    throws BeansException;
  
  public abstract Object getBean(String paramString, Object... paramVarArgs)
    throws BeansException;
  
  public abstract boolean containsBean(String paramString);
  
  public abstract boolean isSingleton(String paramString)
    throws NoSuchBeanDefinitionException;
  
  public abstract boolean isPrototype(String paramString)
    throws NoSuchBeanDefinitionException;
  
  public abstract boolean isTypeMatch(String paramString, Class<?> paramClass)
    throws NoSuchBeanDefinitionException;
  
  public abstract Class<?> getType(String paramString)
    throws NoSuchBeanDefinitionException;
  
  public abstract String[] getAliases(String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.BeanFactory
 * JD-Core Version:    0.7.0.1
 */