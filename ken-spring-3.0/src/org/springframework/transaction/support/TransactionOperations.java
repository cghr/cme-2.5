package org.springframework.transaction.support;

import org.springframework.transaction.TransactionException;

public abstract interface TransactionOperations
{
  public abstract <T> T execute(TransactionCallback<T> paramTransactionCallback)
    throws TransactionException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.TransactionOperations
 * JD-Core Version:    0.7.0.1
 */