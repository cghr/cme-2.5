package org.springframework.jdbc.core;

import java.sql.CallableStatement;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;

public abstract interface CallableStatementCallback<T>
{
  public abstract T doInCallableStatement(CallableStatement paramCallableStatement)
    throws SQLException, DataAccessException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.CallableStatementCallback
 * JD-Core Version:    0.7.0.1
 */