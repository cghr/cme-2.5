package org.springframework.jdbc.core;

public abstract interface DisposableSqlTypeValue
  extends SqlTypeValue
{
  public abstract void cleanup();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.DisposableSqlTypeValue
 * JD-Core Version:    0.7.0.1
 */