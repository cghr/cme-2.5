package org.springframework.jdbc.core;

public abstract interface InterruptibleBatchPreparedStatementSetter
  extends BatchPreparedStatementSetter
{
  public abstract boolean isBatchExhausted(int paramInt);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.InterruptibleBatchPreparedStatementSetter
 * JD-Core Version:    0.7.0.1
 */