package org.springframework.jdbc.core;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public abstract interface CallableStatementCreator
{
  public abstract CallableStatement createCallableStatement(Connection paramConnection)
    throws SQLException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.CallableStatementCreator
 * JD-Core Version:    0.7.0.1
 */