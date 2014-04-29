package org.springframework.jdbc.support;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public abstract interface DatabaseMetaDataCallback
{
  public abstract Object processMetaData(DatabaseMetaData paramDatabaseMetaData)
    throws SQLException, MetaDataAccessException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.DatabaseMetaDataCallback
 * JD-Core Version:    0.7.0.1
 */