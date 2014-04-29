package org.springframework.jdbc.core.namedparam;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public abstract interface NamedParameterJdbcOperations
{
  public abstract JdbcOperations getJdbcOperations();
  
  public abstract <T> T execute(String paramString, SqlParameterSource paramSqlParameterSource, PreparedStatementCallback<T> paramPreparedStatementCallback)
    throws DataAccessException;
  
  public abstract <T> T execute(String paramString, Map<String, ?> paramMap, PreparedStatementCallback<T> paramPreparedStatementCallback)
    throws DataAccessException;
  
  public abstract <T> T query(String paramString, SqlParameterSource paramSqlParameterSource, ResultSetExtractor<T> paramResultSetExtractor)
    throws DataAccessException;
  
  public abstract <T> T query(String paramString, Map<String, ?> paramMap, ResultSetExtractor<T> paramResultSetExtractor)
    throws DataAccessException;
  
  public abstract void query(String paramString, SqlParameterSource paramSqlParameterSource, RowCallbackHandler paramRowCallbackHandler)
    throws DataAccessException;
  
  public abstract void query(String paramString, Map<String, ?> paramMap, RowCallbackHandler paramRowCallbackHandler)
    throws DataAccessException;
  
  public abstract <T> List<T> query(String paramString, SqlParameterSource paramSqlParameterSource, RowMapper<T> paramRowMapper)
    throws DataAccessException;
  
  public abstract <T> List<T> query(String paramString, Map<String, ?> paramMap, RowMapper<T> paramRowMapper)
    throws DataAccessException;
  
  public abstract <T> T queryForObject(String paramString, SqlParameterSource paramSqlParameterSource, RowMapper<T> paramRowMapper)
    throws DataAccessException;
  
  public abstract <T> T queryForObject(String paramString, Map<String, ?> paramMap, RowMapper<T> paramRowMapper)
    throws DataAccessException;
  
  public abstract <T> T queryForObject(String paramString, SqlParameterSource paramSqlParameterSource, Class<T> paramClass)
    throws DataAccessException;
  
  public abstract <T> T queryForObject(String paramString, Map<String, ?> paramMap, Class<T> paramClass)
    throws DataAccessException;
  
  public abstract Map<String, Object> queryForMap(String paramString, SqlParameterSource paramSqlParameterSource)
    throws DataAccessException;
  
  public abstract Map<String, Object> queryForMap(String paramString, Map<String, ?> paramMap)
    throws DataAccessException;
  
  public abstract long queryForLong(String paramString, SqlParameterSource paramSqlParameterSource)
    throws DataAccessException;
  
  public abstract long queryForLong(String paramString, Map<String, ?> paramMap)
    throws DataAccessException;
  
  public abstract int queryForInt(String paramString, SqlParameterSource paramSqlParameterSource)
    throws DataAccessException;
  
  public abstract int queryForInt(String paramString, Map<String, ?> paramMap)
    throws DataAccessException;
  
  public abstract <T> List<T> queryForList(String paramString, SqlParameterSource paramSqlParameterSource, Class<T> paramClass)
    throws DataAccessException;
  
  public abstract <T> List<T> queryForList(String paramString, Map<String, ?> paramMap, Class<T> paramClass)
    throws DataAccessException;
  
  public abstract List<Map<String, Object>> queryForList(String paramString, SqlParameterSource paramSqlParameterSource)
    throws DataAccessException;
  
  public abstract List<Map<String, Object>> queryForList(String paramString, Map<String, ?> paramMap)
    throws DataAccessException;
  
  public abstract SqlRowSet queryForRowSet(String paramString, SqlParameterSource paramSqlParameterSource)
    throws DataAccessException;
  
  public abstract SqlRowSet queryForRowSet(String paramString, Map<String, ?> paramMap)
    throws DataAccessException;
  
  public abstract int update(String paramString, SqlParameterSource paramSqlParameterSource)
    throws DataAccessException;
  
  public abstract int update(String paramString, Map<String, ?> paramMap)
    throws DataAccessException;
  
  public abstract int update(String paramString, SqlParameterSource paramSqlParameterSource, KeyHolder paramKeyHolder)
    throws DataAccessException;
  
  public abstract int update(String paramString, SqlParameterSource paramSqlParameterSource, KeyHolder paramKeyHolder, String[] paramArrayOfString)
    throws DataAccessException;
  
  public abstract int[] batchUpdate(String paramString, Map<String, ?>[] paramArrayOfMap);
  
  public abstract int[] batchUpdate(String paramString, SqlParameterSource[] paramArrayOfSqlParameterSource);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
 * JD-Core Version:    0.7.0.1
 */