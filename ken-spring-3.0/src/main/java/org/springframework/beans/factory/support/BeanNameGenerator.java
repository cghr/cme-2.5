package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinition;

public abstract interface BeanNameGenerator
{
  public abstract String generateBeanName(BeanDefinition paramBeanDefinition, BeanDefinitionRegistry paramBeanDefinitionRegistry);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.BeanNameGenerator
 * JD-Core Version:    0.7.0.1
 */