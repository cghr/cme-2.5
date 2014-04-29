package org.springframework.jdbc.core.simple;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

@Deprecated
public abstract interface SimpleJdbcOperations
{
  public abstract JdbcOperations getJdbcOperations();
  
  public abstract NamedParameterJdbcOperations getNamedParameterJdbcOperations();
  
  public abstract int queryForInt(String paramString, Map<String, ?> paramMap)
    throws DataAccessException;
  
  public abstract int queryForInt(String paramString, SqlParameterSource paramSqlParameterSource)
    throws DataAccessException;
  
  public abstract int queryForInt(String paramString, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract long queryForLong(String paramString, Map<String, ?> paramMap)
    throws DataAccessException;
  
  public abstract long queryForLong(String paramString, SqlParameterSource paramSqlParameterSource)
    throws DataAccessException;
  
  public abstract long queryForLong(String paramString, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract <T> T queryForObject(String paramString, Class<T> paramClass, Map<String, ?> paramMap)
    throws DataAccessException;
  
  public abstract <T> T queryForObject(String paramString, Class<T> paramClass, SqlParameterSource paramSqlParameterSource)
    throws DataAccessException;
  
  public abstract <T> T queryForObject(String paramString, Class<T> paramClass, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract <T> T queryForObject(String paramString, RowMapper<T> paramRowMapper, Map<String, ?> paramMap)
    throws DataAccessException;
  
  @Deprecated
  public abstract <T> T queryForObject(String paramString, ParameterizedRowMapper<T> paramParameterizedRowMapper, Map<String, ?> paramMap)
    throws DataAccessException;
  
  public abstract <T> T queryForObject(String paramString, RowMapper<T> paramRowMapper, SqlParameterSource paramSqlParameterSource)
    throws DataAccessException;
  
  @Deprecated
  public abstract <T> T queryForObject(String paramString, ParameterizedRowMapper<T> paramParameterizedRowMapper, SqlParameterSource paramSqlParameterSource)
    throws DataAccessException;
  
  public abstract <T> T queryForObject(String paramString, RowMapper<T> paramRowMapper, Object... paramVarArgs)
    throws DataAccessException;
  
  @Deprecated
  public abstract <T> T queryForObject(String paramString, ParameterizedRowMapper<T> paramParameterizedRowMapper, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract <T> List<T> query(String paramString, RowMapper<T> paramRowMapper, Map<String, ?> paramMap)
    throws DataAccessException;
  
  @Deprecated
  public abstract <T> List<T> query(String paramString, ParameterizedRowMapper<T> paramParameterizedRowMapper, Map<String, ?> paramMap)
    throws DataAccessException;
  
  public abstract <T> List<T> query(String paramString, RowMapper<T> paramRowMapper, SqlParameterSource paramSqlParameterSource)
    throws DataAccessException;
  
  @Deprecated
  public abstract <T> List<T> query(String paramString, ParameterizedRowMapper<T> paramParameterizedRowMapper, SqlParameterSource paramSqlParameterSource)
    throws DataAccessException;
  
  public abstract <T> List<T> query(String paramString, RowMapper<T> paramRowMapper, Object... paramVarArgs)
    throws DataAccessException;
  
  @Deprecated
  public abstract <T> List<T> query(String paramString, ParameterizedRowMapper<T> paramParameterizedRowMapper, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract Map<String, Object> queryForMap(String paramString, Map<String, ?> paramMap)
    throws DataAccessException;
  
  public abstract Map<String, Object> queryForMap(String paramString, SqlParameterSource paramSqlParameterSource)
    throws DataAccessException;
  
  public abstract Map<String, Object> queryForMap(String paramString, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract List<Map<String, Object>> queryForList(String paramString, Map<String, ?> paramMap)
    throws DataAccessException;
  
  public abstract List<Map<String, Object>> queryForList(String paramString, SqlParameterSource paramSqlParameterSource)
    throws DataAccessException;
  
  public abstract List<Map<String, Object>> queryForList(String paramString, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract int update(String paramString, Map<String, ?> paramMap)
    throws DataAccessException;
  
  public abstract int update(String paramString, SqlParameterSource paramSqlParameterSource)
    throws DataAccessException;
  
  public abstract int update(String paramString, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract int[] batchUpdate(String paramString, Map<String, ?>[] paramArrayOfMap);
  
  public abstract int[] batchUpdate(String paramString, SqlParameterSource[] paramArrayOfSqlParameterSource);
  
  public abstract int[] batchUpdate(String paramString, List<Object[]> paramList);
  
  public abstract int[] batchUpdate(String paramString, List<Object[]> paramList, int[] paramArrayOfInt);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.simple.SimpleJdbcOperations
 * JD-Core Version:    0.7.0.1
 */