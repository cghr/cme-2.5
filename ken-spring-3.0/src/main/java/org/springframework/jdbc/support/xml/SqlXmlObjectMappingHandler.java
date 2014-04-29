package org.springframework.jdbc.support.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract interface SqlXmlObjectMappingHandler
  extends SqlXmlHandler
{
  public abstract Object getXmlAsObject(ResultSet paramResultSet, String paramString)
    throws SQLException;
  
  public abstract Object getXmlAsObject(ResultSet paramResultSet, int paramInt)
    throws SQLException;
  
  public abstract SqlXmlValue newMarshallingSqlXmlValue(Object paramObject);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.xml.SqlXmlObjectMappingHandler
 * JD-Core Version:    0.7.0.1
 */