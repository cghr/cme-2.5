package org.springframework.jdbc.core;

import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.dao.DataAccessException;

public abstract interface StatementCallback<T>
{
  public abstract T doInStatement(Statement paramStatement)
    throws SQLException, DataAccessException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.StatementCallback
 * JD-Core Version:    0.7.0.1
 */