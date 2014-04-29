package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanPostProcessor;

public abstract interface MergedBeanDefinitionPostProcessor
  extends BeanPostProcessor
{
  public abstract void postProcessMergedBeanDefinition(RootBeanDefinition paramRootBeanDefinition, Class<?> paramClass, String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor
 * JD-Core Version:    0.7.0.1
 */