package org.springframework.core.convert.converter;

public abstract interface Converter<S, T>
{
  public abstract T convert(S paramS);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.converter.Converter
 * JD-Core Version:    0.7.0.1
 */