package org.springframework.jdbc.support;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract interface SqlValue
{
  public abstract void setValue(PreparedStatement paramPreparedStatement, int paramInt)
    throws SQLException;
  
  public abstract void cleanup();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.SqlValue
 * JD-Core Version:    0.7.0.1
 */