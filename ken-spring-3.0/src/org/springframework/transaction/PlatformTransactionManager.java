package org.springframework.transaction;

public abstract interface PlatformTransactionManager
{
  public abstract TransactionStatus getTransaction(TransactionDefinition paramTransactionDefinition)
    throws TransactionException;
  
  public abstract void commit(TransactionStatus paramTransactionStatus)
    throws TransactionException;
  
  public abstract void rollback(TransactionStatus paramTransactionStatus)
    throws TransactionException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.PlatformTransactionManager
 * JD-Core Version:    0.7.0.1
 */