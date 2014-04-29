package org.springframework.beans.factory.access;

import org.springframework.beans.BeansException;

public abstract interface BeanFactoryLocator
{
  public abstract BeanFactoryReference useBeanFactory(String paramString)
    throws BeansException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.access.BeanFactoryLocator
 * JD-Core Version:    0.7.0.1
 */