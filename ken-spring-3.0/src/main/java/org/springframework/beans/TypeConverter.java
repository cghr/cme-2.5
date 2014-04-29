package org.springframework.beans;

import org.springframework.core.MethodParameter;

public abstract interface TypeConverter
{
  public abstract <T> T convertIfNecessary(Object paramObject, Class<T> paramClass)
    throws TypeMismatchException;
  
  public abstract <T> T convertIfNecessary(Object paramObject, Class<T> paramClass, MethodParameter paramMethodParameter)
    throws TypeMismatchException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.TypeConverter
 * JD-Core Version:    0.7.0.1
 */