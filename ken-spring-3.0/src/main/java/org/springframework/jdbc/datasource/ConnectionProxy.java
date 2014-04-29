package org.springframework.jdbc.datasource;

import java.sql.Connection;

public abstract interface ConnectionProxy
  extends Connection
{
  public abstract Connection getTargetConnection();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.ConnectionProxy
 * JD-Core Version:    0.7.0.1
 */