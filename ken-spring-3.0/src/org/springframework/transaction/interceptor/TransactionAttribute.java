package org.springframework.transaction.interceptor;

import org.springframework.transaction.TransactionDefinition;

public abstract interface TransactionAttribute
  extends TransactionDefinition
{
  public abstract String getQualifier();
  
  public abstract boolean rollbackOn(Throwable paramThrowable);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.TransactionAttribute
 * JD-Core Version:    0.7.0.1
 */