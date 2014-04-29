package org.springframework.core.convert;

public abstract interface ConversionService
{
  public abstract boolean canConvert(Class<?> paramClass1, Class<?> paramClass2);
  
  public abstract boolean canConvert(TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
  
  public abstract <T> T convert(Object paramObject, Class<T> paramClass);
  
  public abstract Object convert(Object paramObject, TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.ConversionService
 * JD-Core Version:    0.7.0.1
 */