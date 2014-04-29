package org.springframework.transaction;

public abstract interface TransactionStatus
  extends SavepointManager
{
  public abstract boolean isNewTransaction();
  
  public abstract boolean hasSavepoint();
  
  public abstract void setRollbackOnly();
  
  public abstract boolean isRollbackOnly();
  
  public abstract void flush();
  
  public abstract boolean isCompleted();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.TransactionStatus
 * JD-Core Version:    0.7.0.1
 */