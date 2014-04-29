package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public abstract interface ConfigurableListableBeanFactory
  extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory
{
  public abstract void ignoreDependencyType(Class<?> paramClass);
  
  public abstract void ignoreDependencyInterface(Class<?> paramClass);
  
  public abstract void registerResolvableDependency(Class<?> paramClass, Object paramObject);
  
  public abstract boolean isAutowireCandidate(String paramString, DependencyDescriptor paramDependencyDescriptor)
    throws NoSuchBeanDefinitionException;
  
  public abstract BeanDefinition getBeanDefinition(String paramString)
    throws NoSuchBeanDefinitionException;
  
  public abstract void freezeConfiguration();
  
  public abstract boolean isConfigurationFrozen();
  
  public abstract void preInstantiateSingletons()
    throws BeansException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.ConfigurableListableBeanFactory
 * JD-Core Version:    0.7.0.1
 */