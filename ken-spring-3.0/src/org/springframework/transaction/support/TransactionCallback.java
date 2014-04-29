package org.springframework.transaction.support;

import org.springframework.transaction.TransactionStatus;

public abstract interface TransactionCallback<T>
{
  public abstract T doInTransaction(TransactionStatus paramTransactionStatus);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.TransactionCallback
 * JD-Core Version:    0.7.0.1
 */