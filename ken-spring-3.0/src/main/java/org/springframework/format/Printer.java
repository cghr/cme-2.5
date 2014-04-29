package org.springframework.format;

import java.util.Locale;

public abstract interface Printer<T>
{
  public abstract String print(T paramT, Locale paramLocale);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.Printer
 * JD-Core Version:    0.7.0.1
 */