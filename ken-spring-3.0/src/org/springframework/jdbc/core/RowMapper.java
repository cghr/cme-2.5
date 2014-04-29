package org.springframework.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract interface RowMapper<T>
{
  public abstract T mapRow(ResultSet paramResultSet, int paramInt)
    throws SQLException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.RowMapper
 * JD-Core Version:    0.7.0.1
 */