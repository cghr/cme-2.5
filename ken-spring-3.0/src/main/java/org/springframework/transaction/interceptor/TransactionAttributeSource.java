package org.springframework.transaction.interceptor;

import java.lang.reflect.Method;

public abstract interface TransactionAttributeSource
{
  public abstract TransactionAttribute getTransactionAttribute(Method paramMethod, Class<?> paramClass);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.TransactionAttributeSource
 * JD-Core Version:    0.7.0.1
 */