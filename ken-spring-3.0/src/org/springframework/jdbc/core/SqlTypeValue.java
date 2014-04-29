package org.springframework.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract interface SqlTypeValue
{
  public static final int TYPE_UNKNOWN = -2147483648;
  
  public abstract void setTypeValue(PreparedStatement paramPreparedStatement, int paramInt1, int paramInt2, String paramString)
    throws SQLException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.SqlTypeValue
 * JD-Core Version:    0.7.0.1
 */