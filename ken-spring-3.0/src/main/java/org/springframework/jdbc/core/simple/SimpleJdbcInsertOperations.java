package org.springframework.jdbc.core.simple;

import java.util.Map;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

public abstract interface SimpleJdbcInsertOperations
{
  public abstract SimpleJdbcInsertOperations withTableName(String paramString);
  
  public abstract SimpleJdbcInsertOperations withSchemaName(String paramString);
  
  public abstract SimpleJdbcInsertOperations withCatalogName(String paramString);
  
  public abstract SimpleJdbcInsertOperations usingColumns(String... paramVarArgs);
  
  public abstract SimpleJdbcInsertOperations usingGeneratedKeyColumns(String... paramVarArgs);
  
  public abstract SimpleJdbcInsertOperations withoutTableColumnMetaDataAccess();
  
  public abstract SimpleJdbcInsertOperations includeSynonymsForTableColumnMetaData();
  
  public abstract SimpleJdbcInsertOperations useNativeJdbcExtractorForMetaData(NativeJdbcExtractor paramNativeJdbcExtractor);
  
  public abstract int execute(Map<String, Object> paramMap);
  
  public abstract int execute(SqlParameterSource paramSqlParameterSource);
  
  public abstract Number executeAndReturnKey(Map<String, Object> paramMap);
  
  public abstract Number executeAndReturnKey(SqlParameterSource paramSqlParameterSource);
  
  public abstract KeyHolder executeAndReturnKeyHolder(Map<String, Object> paramMap);
  
  public abstract KeyHolder executeAndReturnKeyHolder(SqlParameterSource paramSqlParameterSource);
  
  public abstract int[] executeBatch(Map<String, Object>[] paramArrayOfMap);
  
  public abstract int[] executeBatch(SqlParameterSource[] paramArrayOfSqlParameterSource);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations
 * JD-Core Version:    0.7.0.1
 */