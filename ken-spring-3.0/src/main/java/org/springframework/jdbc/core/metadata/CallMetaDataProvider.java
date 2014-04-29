package org.springframework.jdbc.core.metadata;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.SqlParameter;

public abstract interface CallMetaDataProvider
{
  public abstract void initializeWithMetaData(DatabaseMetaData paramDatabaseMetaData)
    throws SQLException;
  
  public abstract void initializeWithProcedureColumnMetaData(DatabaseMetaData paramDatabaseMetaData, String paramString1, String paramString2, String paramString3)
    throws SQLException;
  
  public abstract String procedureNameToUse(String paramString);
  
  public abstract String catalogNameToUse(String paramString);
  
  public abstract String schemaNameToUse(String paramString);
  
  public abstract String metaDataCatalogNameToUse(String paramString);
  
  public abstract String metaDataSchemaNameToUse(String paramString);
  
  public abstract String parameterNameToUse(String paramString);
  
  public abstract SqlParameter createDefaultOutParameter(String paramString, CallParameterMetaData paramCallParameterMetaData);
  
  public abstract SqlParameter createDefaultInOutParameter(String paramString, CallParameterMetaData paramCallParameterMetaData);
  
  public abstract SqlParameter createDefaultInParameter(String paramString, CallParameterMetaData paramCallParameterMetaData);
  
  public abstract String getUserName();
  
  public abstract boolean isReturnResultSetSupported();
  
  public abstract boolean isRefCursorSupported();
  
  public abstract int getRefCursorSqlType();
  
  public abstract boolean isProcedureColumnMetaDataUsed();
  
  public abstract boolean byPassReturnParameter(String paramString);
  
  public abstract List<CallParameterMetaData> getCallParameterMetaData();
  
  public abstract boolean isSupportsCatalogsInProcedureCalls();
  
  public abstract boolean isSupportsSchemasInProcedureCalls();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.metadata.CallMetaDataProvider
 * JD-Core Version:    0.7.0.1
 */