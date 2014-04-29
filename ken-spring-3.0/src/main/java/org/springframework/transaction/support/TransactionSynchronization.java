package org.springframework.transaction.support;

public abstract interface TransactionSynchronization
{
  public static final int STATUS_COMMITTED = 0;
  public static final int STATUS_ROLLED_BACK = 1;
  public static final int STATUS_UNKNOWN = 2;
  
  public abstract void suspend();
  
  public abstract void resume();
  
  public abstract void flush();
  
  public abstract void beforeCommit(boolean paramBoolean);
  
  public abstract void beforeCompletion();
  
  public abstract void afterCommit();
  
  public abstract void afterCompletion(int paramInt);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.TransactionSynchronization
 * JD-Core Version:    0.7.0.1
 */