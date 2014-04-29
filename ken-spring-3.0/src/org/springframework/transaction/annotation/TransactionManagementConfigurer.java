package org.springframework.transaction.annotation;

import org.springframework.transaction.PlatformTransactionManager;

public abstract interface TransactionManagementConfigurer
{
  public abstract PlatformTransactionManager annotationDrivenTransactionManager();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.annotation.TransactionManagementConfigurer
 * JD-Core Version:    0.7.0.1
 */