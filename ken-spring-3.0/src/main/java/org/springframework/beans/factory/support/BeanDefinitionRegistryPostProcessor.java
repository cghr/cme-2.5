package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;

public abstract interface BeanDefinitionRegistryPostProcessor
  extends BeanFactoryPostProcessor
{
  public abstract void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry paramBeanDefinitionRegistry)
    throws BeansException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
 * JD-Core Version:    0.7.0.1
 */