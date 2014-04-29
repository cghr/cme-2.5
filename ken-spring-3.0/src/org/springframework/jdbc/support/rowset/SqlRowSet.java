package org.springframework.jdbc.support.rowset;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import org.springframework.jdbc.InvalidResultSetAccessException;

public abstract interface SqlRowSet
  extends Serializable
{
  public abstract SqlRowSetMetaData getMetaData();
  
  public abstract int findColumn(String paramString)
    throws InvalidResultSetAccessException;
  
  public abstract BigDecimal getBigDecimal(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract BigDecimal getBigDecimal(String paramString)
    throws InvalidResultSetAccessException;
  
  public abstract boolean getBoolean(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract boolean getBoolean(String paramString)
    throws InvalidResultSetAccessException;
  
  public abstract byte getByte(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract byte getByte(String paramString)
    throws InvalidResultSetAccessException;
  
  public abstract Date getDate(int paramInt, Calendar paramCalendar)
    throws InvalidResultSetAccessException;
  
  public abstract Date getDate(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract Date getDate(String paramString, Calendar paramCalendar)
    throws InvalidResultSetAccessException;
  
  public abstract Date getDate(String paramString)
    throws InvalidResultSetAccessException;
  
  public abstract double getDouble(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract double getDouble(String paramString)
    throws InvalidResultSetAccessException;
  
  public abstract float getFloat(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract float getFloat(String paramString)
    throws InvalidResultSetAccessException;
  
  public abstract int getInt(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract int getInt(String paramString)
    throws InvalidResultSetAccessException;
  
  public abstract long getLong(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract long getLong(String paramString)
    throws InvalidResultSetAccessException;
  
  public abstract Object getObject(int paramInt, Map<String, Class<?>> paramMap)
    throws InvalidResultSetAccessException;
  
  public abstract Object getObject(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract Object getObject(String paramString, Map<String, Class<?>> paramMap)
    throws InvalidResultSetAccessException;
  
  public abstract Object getObject(String paramString)
    throws InvalidResultSetAccessException;
  
  public abstract short getShort(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract short getShort(String paramString)
    throws InvalidResultSetAccessException;
  
  public abstract String getString(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract String getString(String paramString)
    throws InvalidResultSetAccessException;
  
  public abstract Time getTime(int paramInt, Calendar paramCalendar)
    throws InvalidResultSetAccessException;
  
  public abstract Time getTime(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract Time getTime(String paramString, Calendar paramCalendar)
    throws InvalidResultSetAccessException;
  
  public abstract Time getTime(String paramString)
    throws InvalidResultSetAccessException;
  
  public abstract Timestamp getTimestamp(int paramInt, Calendar paramCalendar)
    throws InvalidResultSetAccessException;
  
  public abstract Timestamp getTimestamp(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract Timestamp getTimestamp(String paramString, Calendar paramCalendar)
    throws InvalidResultSetAccessException;
  
  public abstract Timestamp getTimestamp(String paramString)
    throws InvalidResultSetAccessException;
  
  public abstract boolean absolute(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract void afterLast()
    throws InvalidResultSetAccessException;
  
  public abstract void beforeFirst()
    throws InvalidResultSetAccessException;
  
  public abstract boolean first()
    throws InvalidResultSetAccessException;
  
  public abstract int getRow()
    throws InvalidResultSetAccessException;
  
  public abstract boolean isAfterLast()
    throws InvalidResultSetAccessException;
  
  public abstract boolean isBeforeFirst()
    throws InvalidResultSetAccessException;
  
  public abstract boolean isFirst()
    throws InvalidResultSetAccessException;
  
  public abstract boolean isLast()
    throws InvalidResultSetAccessException;
  
  public abstract boolean last()
    throws InvalidResultSetAccessException;
  
  public abstract boolean next()
    throws InvalidResultSetAccessException;
  
  public abstract boolean previous()
    throws InvalidResultSetAccessException;
  
  public abstract boolean relative(int paramInt)
    throws InvalidResultSetAccessException;
  
  public abstract boolean wasNull()
    throws InvalidResultSetAccessException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.rowset.SqlRowSet
 * JD-Core Version:    0.7.0.1
 */