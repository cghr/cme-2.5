package org.springframework.beans;

public abstract interface PropertyValues
{
  public abstract PropertyValue[] getPropertyValues();
  
  public abstract PropertyValue getPropertyValue(String paramString);
  
  public abstract PropertyValues changesSince(PropertyValues paramPropertyValues);
  
  public abstract boolean contains(String paramString);
  
  public abstract boolean isEmpty();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.PropertyValues
 * JD-Core Version:    0.7.0.1
 */