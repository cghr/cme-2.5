package org.springframework.transaction.support;

import org.springframework.transaction.PlatformTransactionManager;

public abstract interface ResourceTransactionManager
  extends PlatformTransactionManager
{
  public abstract Object getResourceFactory();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.ResourceTransactionManager
 * JD-Core Version:    0.7.0.1
 */