package org.springframework.context.annotation;

import org.springframework.beans.factory.config.BeanDefinition;

public abstract interface ScopeMetadataResolver
{
  public abstract ScopeMetadata resolveScopeMetadata(BeanDefinition paramBeanDefinition);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ScopeMetadataResolver
 * JD-Core Version:    0.7.0.1
 */