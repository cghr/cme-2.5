package org.springframework.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract interface BatchPreparedStatementSetter
{
  public abstract void setValues(PreparedStatement paramPreparedStatement, int paramInt)
    throws SQLException;
  
  public abstract int getBatchSize();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.BatchPreparedStatementSetter
 * JD-Core Version:    0.7.0.1
 */