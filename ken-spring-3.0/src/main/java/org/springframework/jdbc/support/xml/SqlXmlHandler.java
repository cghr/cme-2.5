package org.springframework.jdbc.support.xml;

import java.io.InputStream;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.xml.transform.Source;
import org.w3c.dom.Document;

public abstract interface SqlXmlHandler
{
  public abstract String getXmlAsString(ResultSet paramResultSet, String paramString)
    throws SQLException;
  
  public abstract String getXmlAsString(ResultSet paramResultSet, int paramInt)
    throws SQLException;
  
  public abstract InputStream getXmlAsBinaryStream(ResultSet paramResultSet, String paramString)
    throws SQLException;
  
  public abstract InputStream getXmlAsBinaryStream(ResultSet paramResultSet, int paramInt)
    throws SQLException;
  
  public abstract Reader getXmlAsCharacterStream(ResultSet paramResultSet, String paramString)
    throws SQLException;
  
  public abstract Reader getXmlAsCharacterStream(ResultSet paramResultSet, int paramInt)
    throws SQLException;
  
  public abstract Source getXmlAsSource(ResultSet paramResultSet, String paramString, Class paramClass)
    throws SQLException;
  
  public abstract Source getXmlAsSource(ResultSet paramResultSet, int paramInt, Class paramClass)
    throws SQLException;
  
  public abstract SqlXmlValue newSqlXmlValue(String paramString);
  
  public abstract SqlXmlValue newSqlXmlValue(XmlBinaryStreamProvider paramXmlBinaryStreamProvider);
  
  public abstract SqlXmlValue newSqlXmlValue(XmlCharacterStreamProvider paramXmlCharacterStreamProvider);
  
  public abstract SqlXmlValue newSqlXmlValue(Class paramClass, XmlResultProvider paramXmlResultProvider);
  
  public abstract SqlXmlValue newSqlXmlValue(Document paramDocument);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.xml.SqlXmlHandler
 * JD-Core Version:    0.7.0.1
 */