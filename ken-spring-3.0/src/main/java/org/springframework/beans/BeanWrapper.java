package org.springframework.beans;

import java.beans.PropertyDescriptor;

public abstract interface BeanWrapper
  extends ConfigurablePropertyAccessor
{
  public abstract Object getWrappedInstance();
  
  public abstract Class getWrappedClass();
  
  public abstract PropertyDescriptor[] getPropertyDescriptors();
  
  public abstract PropertyDescriptor getPropertyDescriptor(String paramString)
    throws InvalidPropertyException;
  
  public abstract void setAutoGrowNestedPaths(boolean paramBoolean);
  
  public abstract boolean isAutoGrowNestedPaths();
  
  public abstract void setAutoGrowCollectionLimit(int paramInt);
  
  public abstract int getAutoGrowCollectionLimit();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.BeanWrapper
 * JD-Core Version:    0.7.0.1
 */