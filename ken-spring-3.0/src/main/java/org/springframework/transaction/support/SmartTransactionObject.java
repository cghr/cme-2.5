package org.springframework.transaction.support;

public abstract interface SmartTransactionObject
{
  public abstract boolean isRollbackOnly();
  
  public abstract void flush();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.SmartTransactionObject
 * JD-Core Version:    0.7.0.1
 */