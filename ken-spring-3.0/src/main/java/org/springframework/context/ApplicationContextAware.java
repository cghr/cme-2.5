package org.springframework.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.Aware;

public abstract interface ApplicationContextAware
  extends Aware
{
  public abstract void setApplicationContext(ApplicationContext paramApplicationContext)
    throws BeansException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.ApplicationContextAware
 * JD-Core Version:    0.7.0.1
 */