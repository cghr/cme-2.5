package org.springframework.context;

import org.springframework.beans.factory.Aware;

public abstract interface ApplicationEventPublisherAware
  extends Aware
{
  public abstract void setApplicationEventPublisher(ApplicationEventPublisher paramApplicationEventPublisher);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.ApplicationEventPublisherAware
 * JD-Core Version:    0.7.0.1
 */