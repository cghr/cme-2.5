package org.springframework.beans.factory;

public abstract interface HierarchicalBeanFactory
  extends BeanFactory
{
  public abstract BeanFactory getParentBeanFactory();
  
  public abstract boolean containsLocalBean(String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.HierarchicalBeanFactory
 * JD-Core Version:    0.7.0.1
 */