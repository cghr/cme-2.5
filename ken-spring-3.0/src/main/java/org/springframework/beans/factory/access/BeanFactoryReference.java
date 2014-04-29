package org.springframework.beans.factory.access;

import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.BeanFactory;

public abstract interface BeanFactoryReference
{
  public abstract BeanFactory getFactory();
  
  public abstract void release()
    throws FatalBeanException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.access.BeanFactoryReference
 * JD-Core Version:    0.7.0.1
 */