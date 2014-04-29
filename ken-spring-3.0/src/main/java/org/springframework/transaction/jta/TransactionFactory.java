package org.springframework.transaction.jta;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

public abstract interface TransactionFactory
{
  public abstract Transaction createTransaction(String paramString, int paramInt)
    throws NotSupportedException, SystemException;
  
  public abstract boolean supportsResourceAdapterManagedTransactions();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.jta.TransactionFactory
 * JD-Core Version:    0.7.0.1
 */