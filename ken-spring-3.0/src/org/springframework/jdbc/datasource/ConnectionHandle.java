package org.springframework.jdbc.datasource;

import java.sql.Connection;

public abstract interface ConnectionHandle
{
  public abstract Connection getConnection();
  
  public abstract void releaseConnection(Connection paramConnection);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.ConnectionHandle
 * JD-Core Version:    0.7.0.1
 */