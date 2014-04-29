package org.springframework.jdbc.support.lob;

import java.io.InputStream;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract interface LobCreator
{
  public abstract void setBlobAsBytes(PreparedStatement paramPreparedStatement, int paramInt, byte[] paramArrayOfByte)
    throws SQLException;
  
  public abstract void setBlobAsBinaryStream(PreparedStatement paramPreparedStatement, int paramInt1, InputStream paramInputStream, int paramInt2)
    throws SQLException;
  
  public abstract void setClobAsString(PreparedStatement paramPreparedStatement, int paramInt, String paramString)
    throws SQLException;
  
  public abstract void setClobAsAsciiStream(PreparedStatement paramPreparedStatement, int paramInt1, InputStream paramInputStream, int paramInt2)
    throws SQLException;
  
  public abstract void setClobAsCharacterStream(PreparedStatement paramPreparedStatement, int paramInt1, Reader paramReader, int paramInt2)
    throws SQLException;
  
  public abstract void close();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.lob.LobCreator
 * JD-Core Version:    0.7.0.1
 */