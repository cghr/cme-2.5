package org.springframework.beans.support;

public abstract interface SortDefinition
{
  public abstract String getProperty();
  
  public abstract boolean isIgnoreCase();
  
  public abstract boolean isAscending();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.support.SortDefinition
 * JD-Core Version:    0.7.0.1
 */