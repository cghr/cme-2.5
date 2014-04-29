package org.springframework.context.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public abstract interface ApplicationEventMulticaster
{
  public abstract void addApplicationListener(ApplicationListener paramApplicationListener);
  
  public abstract void addApplicationListenerBean(String paramString);
  
  public abstract void removeApplicationListener(ApplicationListener paramApplicationListener);
  
  public abstract void removeApplicationListenerBean(String paramString);
  
  public abstract void removeAllListeners();
  
  public abstract void multicastEvent(ApplicationEvent paramApplicationEvent);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.event.ApplicationEventMulticaster
 * JD-Core Version:    0.7.0.1
 */