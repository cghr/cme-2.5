package org.springframework.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract interface ParameterizedPreparedStatementSetter<T>
{
  public abstract void setValues(PreparedStatement paramPreparedStatement, T paramT)
    throws SQLException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.ParameterizedPreparedStatementSetter
 * JD-Core Version:    0.7.0.1
 */