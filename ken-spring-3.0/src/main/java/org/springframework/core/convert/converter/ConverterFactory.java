package org.springframework.core.convert.converter;

public abstract interface ConverterFactory<S, R>
{
  public abstract <T extends R> Converter<S, T> getConverter(Class<T> paramClass);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.converter.ConverterFactory
 * JD-Core Version:    0.7.0.1
 */