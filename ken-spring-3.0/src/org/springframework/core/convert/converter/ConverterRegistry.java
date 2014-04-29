package org.springframework.core.convert.converter;

public abstract interface ConverterRegistry
{
  public abstract void addConverter(Converter<?, ?> paramConverter);
  
  public abstract void addConverter(Class<?> paramClass1, Class<?> paramClass2, Converter<?, ?> paramConverter);
  
  public abstract void addConverter(GenericConverter paramGenericConverter);
  
  public abstract void addConverterFactory(ConverterFactory<?, ?> paramConverterFactory);
  
  public abstract void removeConvertible(Class<?> paramClass1, Class<?> paramClass2);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.converter.ConverterRegistry
 * JD-Core Version:    0.7.0.1
 */