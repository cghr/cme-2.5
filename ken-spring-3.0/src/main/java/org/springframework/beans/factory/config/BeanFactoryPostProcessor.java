package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

public abstract interface BeanFactoryPostProcessor
{
  public abstract void postProcessBeanFactory(ConfigurableListableBeanFactory paramConfigurableListableBeanFactory)
    throws BeansException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.BeanFactoryPostProcessor
 * JD-Core Version:    0.7.0.1
 */