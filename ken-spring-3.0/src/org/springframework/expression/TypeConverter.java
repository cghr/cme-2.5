package org.springframework.expression;

import org.springframework.core.convert.TypeDescriptor;

public abstract interface TypeConverter
{
  public abstract boolean canConvert(TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
  
  public abstract Object convertValue(Object paramObject, TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.TypeConverter
 * JD-Core Version:    0.7.0.1
 */