package org.springframework.jdbc.core.simple;

import java.util.Map;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public abstract interface SimpleJdbcCallOperations
{
  public abstract SimpleJdbcCallOperations withProcedureName(String paramString);
  
  public abstract SimpleJdbcCallOperations withFunctionName(String paramString);
  
  public abstract SimpleJdbcCallOperations withSchemaName(String paramString);
  
  public abstract SimpleJdbcCallOperations withCatalogName(String paramString);
  
  public abstract SimpleJdbcCallOperations withReturnValue();
  
  public abstract SimpleJdbcCallOperations declareParameters(SqlParameter... paramVarArgs);
  
  public abstract SimpleJdbcCallOperations useInParameterNames(String... paramVarArgs);
  
  public abstract SimpleJdbcCallOperations returningResultSet(String paramString, RowMapper paramRowMapper);
  
  @Deprecated
  public abstract SimpleJdbcCallOperations returningResultSet(String paramString, ParameterizedRowMapper paramParameterizedRowMapper);
  
  public abstract SimpleJdbcCallOperations withoutProcedureColumnMetaDataAccess();
  
  public abstract <T> T executeFunction(Class<T> paramClass, Object... paramVarArgs);
  
  public abstract <T> T executeFunction(Class<T> paramClass, Map<String, ?> paramMap);
  
  public abstract <T> T executeFunction(Class<T> paramClass, SqlParameterSource paramSqlParameterSource);
  
  public abstract <T> T executeObject(Class<T> paramClass, Object... paramVarArgs);
  
  public abstract <T> T executeObject(Class<T> paramClass, Map<String, ?> paramMap);
  
  public abstract <T> T executeObject(Class<T> paramClass, SqlParameterSource paramSqlParameterSource);
  
  public abstract Map<String, Object> execute(Object... paramVarArgs);
  
  public abstract Map<String, Object> execute(Map<String, ?> paramMap);
  
  public abstract Map<String, Object> execute(SqlParameterSource paramSqlParameterSource);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
 * JD-Core Version:    0.7.0.1
 */