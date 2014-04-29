package org.springframework.ui.context;

import org.springframework.context.MessageSource;

public abstract interface Theme
{
  public abstract String getName();
  
  public abstract MessageSource getMessageSource();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ui.context.Theme
 * JD-Core Version:    0.7.0.1
 */