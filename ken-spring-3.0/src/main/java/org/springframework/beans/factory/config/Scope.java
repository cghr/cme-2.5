package org.springframework.beans.factory.config;

import org.springframework.beans.factory.ObjectFactory;

public abstract interface Scope
{
  public abstract Object get(String paramString, ObjectFactory<?> paramObjectFactory);
  
  public abstract Object remove(String paramString);
  
  public abstract void registerDestructionCallback(String paramString, Runnable paramRunnable);
  
  public abstract Object resolveContextualObject(String paramString);
  
  public abstract String getConversationId();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.Scope
 * JD-Core Version:    0.7.0.1
 */