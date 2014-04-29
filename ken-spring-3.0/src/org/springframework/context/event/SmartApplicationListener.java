package org.springframework.context.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

public abstract interface SmartApplicationListener
  extends ApplicationListener<ApplicationEvent>, Ordered
{
  public abstract boolean supportsEventType(Class<? extends ApplicationEvent> paramClass);
  
  public abstract boolean supportsSourceType(Class<?> paramClass);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.event.SmartApplicationListener
 * JD-Core Version:    0.7.0.1
 */