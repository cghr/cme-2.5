package org.springframework.jdbc.core;

import java.sql.CallableStatement;
import java.sql.SQLException;

public abstract interface SqlReturnType
{
  public static final int TYPE_UNKNOWN = -2147483648;
  
  public abstract Object getTypeValue(CallableStatement paramCallableStatement, int paramInt1, int paramInt2, String paramString)
    throws SQLException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.SqlReturnType
 * JD-Core Version:    0.7.0.1
 */