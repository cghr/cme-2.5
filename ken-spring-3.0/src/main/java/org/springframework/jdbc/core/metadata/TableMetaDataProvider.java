package org.springframework.jdbc.core.metadata;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

public abstract interface TableMetaDataProvider
{
  public abstract void initializeWithMetaData(DatabaseMetaData paramDatabaseMetaData)
    throws SQLException;
  
  public abstract void initializeWithTableColumnMetaData(DatabaseMetaData paramDatabaseMetaData, String paramString1, String paramString2, String paramString3)
    throws SQLException;
  
  public abstract String tableNameToUse(String paramString);
  
  public abstract String catalogNameToUse(String paramString);
  
  public abstract String schemaNameToUse(String paramString);
  
  public abstract String metaDataCatalogNameToUse(String paramString);
  
  public abstract String metaDataSchemaNameToUse(String paramString);
  
  public abstract boolean isTableColumnMetaDataUsed();
  
  public abstract boolean isGetGeneratedKeysSupported();
  
  public abstract boolean isGetGeneratedKeysSimulated();
  
  public abstract String getSimpleQueryForGetGeneratedKey(String paramString1, String paramString2);
  
  public abstract boolean isGeneratedKeysColumnNameArraySupported();
  
  public abstract List<TableParameterMetaData> getTableParameterMetaData();
  
  public abstract void setNativeJdbcExtractor(NativeJdbcExtractor paramNativeJdbcExtractor);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.metadata.TableMetaDataProvider
 * JD-Core Version:    0.7.0.1
 */