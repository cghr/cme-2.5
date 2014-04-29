package org.springframework.jdbc.core;

import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;

public abstract interface ConnectionCallback<T>
{
  public abstract T doInConnection(Connection paramConnection)
    throws SQLException, DataAccessException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.ConnectionCallback
 * JD-Core Version:    0.7.0.1
 */