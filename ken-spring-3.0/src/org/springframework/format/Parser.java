package org.springframework.format;

import java.text.ParseException;
import java.util.Locale;

public abstract interface Parser<T>
{
  public abstract T parse(String paramString, Locale paramLocale)
    throws ParseException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.Parser
 * JD-Core Version:    0.7.0.1
 */