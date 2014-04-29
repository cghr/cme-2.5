package org.springframework.jdbc.support.nativejdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract interface NativeJdbcExtractor
{
  public abstract boolean isNativeConnectionNecessaryForNativeStatements();
  
  public abstract boolean isNativeConnectionNecessaryForNativePreparedStatements();
  
  public abstract boolean isNativeConnectionNecessaryForNativeCallableStatements();
  
  public abstract Connection getNativeConnection(Connection paramConnection)
    throws SQLException;
  
  public abstract Connection getNativeConnectionFromStatement(Statement paramStatement)
    throws SQLException;
  
  public abstract Statement getNativeStatement(Statement paramStatement)
    throws SQLException;
  
  public abstract PreparedStatement getNativePreparedStatement(PreparedStatement paramPreparedStatement)
    throws SQLException;
  
  public abstract CallableStatement getNativeCallableStatement(CallableStatement paramCallableStatement)
    throws SQLException;
  
  public abstract ResultSet getNativeResultSet(ResultSet paramResultSet)
    throws SQLException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
 * JD-Core Version:    0.7.0.1
 */