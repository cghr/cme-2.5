package org.springframework.context;

import org.springframework.beans.factory.Aware;

public abstract interface MessageSourceAware
  extends Aware
{
  public abstract void setMessageSource(MessageSource paramMessageSource);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.MessageSourceAware
 * JD-Core Version:    0.7.0.1
 */