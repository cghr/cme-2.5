package org.springframework.context;

import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.io.support.ResourcePatternResolver;

public abstract interface ApplicationContext
  extends EnvironmentCapable, ListableBeanFactory, HierarchicalBeanFactory, MessageSource, ApplicationEventPublisher, ResourcePatternResolver
{
  public abstract String getId();
  
  public abstract String getDisplayName();
  
  public abstract long getStartupDate();
  
  public abstract ApplicationContext getParent();
  
  public abstract AutowireCapableBeanFactory getAutowireCapableBeanFactory()
    throws IllegalStateException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.ApplicationContext
 * JD-Core Version:    0.7.0.1
 */