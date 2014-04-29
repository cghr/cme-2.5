package org.springframework.context;

public abstract interface HierarchicalMessageSource
  extends MessageSource
{
  public abstract void setParentMessageSource(MessageSource paramMessageSource);
  
  public abstract MessageSource getParentMessageSource();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.HierarchicalMessageSource
 * JD-Core Version:    0.7.0.1
 */