package org.springframework.jdbc.support.lob;

import java.io.InputStream;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract interface LobHandler
{
  public abstract byte[] getBlobAsBytes(ResultSet paramResultSet, String paramString)
    throws SQLException;
  
  public abstract byte[] getBlobAsBytes(ResultSet paramResultSet, int paramInt)
    throws SQLException;
  
  public abstract InputStream getBlobAsBinaryStream(ResultSet paramResultSet, String paramString)
    throws SQLException;
  
  public abstract InputStream getBlobAsBinaryStream(ResultSet paramResultSet, int paramInt)
    throws SQLException;
  
  public abstract String getClobAsString(ResultSet paramResultSet, String paramString)
    throws SQLException;
  
  public abstract String getClobAsString(ResultSet paramResultSet, int paramInt)
    throws SQLException;
  
  public abstract InputStream getClobAsAsciiStream(ResultSet paramResultSet, String paramString)
    throws SQLException;
  
  public abstract InputStream getClobAsAsciiStream(ResultSet paramResultSet, int paramInt)
    throws SQLException;
  
  public abstract Reader getClobAsCharacterStream(ResultSet paramResultSet, String paramString)
    throws SQLException;
  
  public abstract Reader getClobAsCharacterStream(ResultSet paramResultSet, int paramInt)
    throws SQLException;
  
  public abstract LobCreator getLobCreator();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.lob.LobHandler
 * JD-Core Version:    0.7.0.1
 */