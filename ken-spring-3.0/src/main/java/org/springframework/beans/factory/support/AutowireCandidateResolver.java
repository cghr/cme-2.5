package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;

public abstract interface AutowireCandidateResolver
{
  public abstract boolean isAutowireCandidate(BeanDefinitionHolder paramBeanDefinitionHolder, DependencyDescriptor paramDependencyDescriptor);
  
  public abstract Object getSuggestedValue(DependencyDescriptor paramDependencyDescriptor);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.AutowireCandidateResolver
 * JD-Core Version:    0.7.0.1
 */