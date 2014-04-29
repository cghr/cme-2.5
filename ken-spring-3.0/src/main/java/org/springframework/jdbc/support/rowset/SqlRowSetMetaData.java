package org.springframework.jdbc.support.rowset;

import org.springframework.jdbc.InvalidResultSetAccessException;

public abstract interface SqlRowSetMetaData
{
  public abstract String getCatalogName(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract String getColumnClassName(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract int getColumnCount()
    throws InvalidResultSetAccessException;
  
  public abstract String[] getColumnNames()
    throws InvalidResultSetAccessException;
  
  public abstract int getColumnDisplaySize(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract String getColumnLabel(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract String getColumnName(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract int getColumnType(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract String getColumnTypeName(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract int getPrecision(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract int getScale(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract String getSchemaName(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract String getTableName(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract boolean isCaseSensitive(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract boolean isCurrency(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract boolean isSigned(int paramInt)
    throws InvalidResultSetAccessException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.rowset.SqlRowSetMetaData
 * JD-Core Version:    0.7.0.1
 */