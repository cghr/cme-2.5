package org.springframework.beans.factory.parsing;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;

public abstract interface ComponentDefinition
  extends BeanMetadataElement
{
  public abstract String getName();
  
  public abstract String getDescription();
  
  public abstract BeanDefinition[] getBeanDefinitions();
  
  public abstract BeanDefinition[] getInnerBeanDefinitions();
  
  public abstract BeanReference[] getBeanReferences();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.ComponentDefinition
 * JD-Core Version:    0.7.0.1
 */