package org.springframework.core.convert.converter;

import org.springframework.core.convert.TypeDescriptor;

public abstract interface ConditionalGenericConverter
  extends GenericConverter
{
  public abstract boolean matches(TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.converter.ConditionalGenericConverter
 * JD-Core Version:    0.7.0.1
 */