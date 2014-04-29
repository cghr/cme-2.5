package org.springframework.transaction.support;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;

public abstract interface CallbackPreferringPlatformTransactionManager
  extends PlatformTransactionManager
{
  public abstract <T> T execute(TransactionDefinition paramTransactionDefinition, TransactionCallback<T> paramTransactionCallback)
    throws TransactionException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.CallbackPreferringPlatformTransactionManager
 * JD-Core Version:    0.7.0.1
 */