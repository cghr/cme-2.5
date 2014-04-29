package org.springframework.context;

import java.util.Locale;

public abstract interface MessageSource
{
  public abstract String getMessage(String paramString1, Object[] paramArrayOfObject, String paramString2, Locale paramLocale);
  
  public abstract String getMessage(String paramString, Object[] paramArrayOfObject, Locale paramLocale)
    throws NoSuchMessageException;
  
  public abstract String getMessage(MessageSourceResolvable paramMessageSourceResolvable, Locale paramLocale)
    throws NoSuchMessageException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.MessageSource
 * JD-Core Version:    0.7.0.1
 */