package org.springframework.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;

public abstract interface PreparedStatementCallback<T>
{
  public abstract T doInPreparedStatement(PreparedStatement paramPreparedStatement)
    throws SQLException, DataAccessException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.PreparedStatementCallback
 * JD-Core Version:    0.7.0.1
 */