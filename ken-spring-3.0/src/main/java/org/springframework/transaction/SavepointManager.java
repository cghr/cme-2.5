package org.springframework.transaction;

public abstract interface SavepointManager
{
  public abstract Object createSavepoint()
    throws TransactionException;
  
  public abstract void rollbackToSavepoint(Object paramObject)
    throws TransactionException;
  
  public abstract void releaseSavepoint(Object paramObject)
    throws TransactionException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.SavepointManager
 * JD-Core Version:    0.7.0.1
 */