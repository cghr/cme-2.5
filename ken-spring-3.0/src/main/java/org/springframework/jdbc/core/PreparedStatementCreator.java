package org.springframework.jdbc.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract interface PreparedStatementCreator
{
  public abstract PreparedStatement createPreparedStatement(Connection paramConnection)
    throws SQLException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.PreparedStatementCreator
 * JD-Core Version:    0.7.0.1
 */