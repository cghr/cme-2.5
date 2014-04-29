package org.springframework.jdbc.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public abstract interface JdbcOperations
{
  public abstract <T> T execute(ConnectionCallback<T> paramConnectionCallback)
    throws DataAccessException;
  
  public abstract <T> T execute(StatementCallback<T> paramStatementCallback)
    throws DataAccessException;
  
  public abstract void execute(String paramString)
    throws DataAccessException;
  
  public abstract <T> T query(String paramString, ResultSetExtractor<T> paramResultSetExtractor)
    throws DataAccessException;
  
  public abstract void query(String paramString, RowCallbackHandler paramRowCallbackHandler)
    throws DataAccessException;
  
  public abstract <T> List<T> query(String paramString, RowMapper<T> paramRowMapper)
    throws DataAccessException;
  
  public abstract <T> T queryForObject(String paramString, RowMapper<T> paramRowMapper)
    throws DataAccessException;
  
  public abstract <T> T queryForObject(String paramString, Class<T> paramClass)
    throws DataAccessException;
  
  public abstract Map<String, Object> queryForMap(String paramString)
    throws DataAccessException;
  
  public abstract long queryForLong(String paramString)
    throws DataAccessException;
  
  public abstract int queryForInt(String paramString)
    throws DataAccessException;
  
  public abstract <T> List<T> queryForList(String paramString, Class<T> paramClass)
    throws DataAccessException;
  
  public abstract List<Map<String, Object>> queryForList(String paramString)
    throws DataAccessException;
  
  public abstract SqlRowSet queryForRowSet(String paramString)
    throws DataAccessException;
  
  public abstract int update(String paramString)
    throws DataAccessException;
  
  public abstract int[] batchUpdate(String[] paramArrayOfString)
    throws DataAccessException;
  
  public abstract <T> T execute(PreparedStatementCreator paramPreparedStatementCreator, PreparedStatementCallback<T> paramPreparedStatementCallback)
    throws DataAccessException;
  
  public abstract <T> T execute(String paramString, PreparedStatementCallback<T> paramPreparedStatementCallback)
    throws DataAccessException;
  
  public abstract <T> T query(PreparedStatementCreator paramPreparedStatementCreator, ResultSetExtractor<T> paramResultSetExtractor)
    throws DataAccessException;
  
  public abstract <T> T query(String paramString, PreparedStatementSetter paramPreparedStatementSetter, ResultSetExtractor<T> paramResultSetExtractor)
    throws DataAccessException;
  
  public abstract <T> T query(String paramString, Object[] paramArrayOfObject, int[] paramArrayOfInt, ResultSetExtractor<T> paramResultSetExtractor)
    throws DataAccessException;
  
  public abstract <T> T query(String paramString, Object[] paramArrayOfObject, ResultSetExtractor<T> paramResultSetExtractor)
    throws DataAccessException;
  
  public abstract <T> T query(String paramString, ResultSetExtractor<T> paramResultSetExtractor, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract void query(PreparedStatementCreator paramPreparedStatementCreator, RowCallbackHandler paramRowCallbackHandler)
    throws DataAccessException;
  
  public abstract void query(String paramString, PreparedStatementSetter paramPreparedStatementSetter, RowCallbackHandler paramRowCallbackHandler)
    throws DataAccessException;
  
  public abstract void query(String paramString, Object[] paramArrayOfObject, int[] paramArrayOfInt, RowCallbackHandler paramRowCallbackHandler)
    throws DataAccessException;
  
  public abstract void query(String paramString, Object[] paramArrayOfObject, RowCallbackHandler paramRowCallbackHandler)
    throws DataAccessException;
  
  public abstract void query(String paramString, RowCallbackHandler paramRowCallbackHandler, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract <T> List<T> query(PreparedStatementCreator paramPreparedStatementCreator, RowMapper<T> paramRowMapper)
    throws DataAccessException;
  
  public abstract <T> List<T> query(String paramString, PreparedStatementSetter paramPreparedStatementSetter, RowMapper<T> paramRowMapper)
    throws DataAccessException;
  
  public abstract <T> List<T> query(String paramString, Object[] paramArrayOfObject, int[] paramArrayOfInt, RowMapper<T> paramRowMapper)
    throws DataAccessException;
  
  public abstract <T> List<T> query(String paramString, Object[] paramArrayOfObject, RowMapper<T> paramRowMapper)
    throws DataAccessException;
  
  public abstract <T> List<T> query(String paramString, RowMapper<T> paramRowMapper, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract <T> T queryForObject(String paramString, Object[] paramArrayOfObject, int[] paramArrayOfInt, RowMapper<T> paramRowMapper)
    throws DataAccessException;
  
  public abstract <T> T queryForObject(String paramString, Object[] paramArrayOfObject, RowMapper<T> paramRowMapper)
    throws DataAccessException;
  
  public abstract <T> T queryForObject(String paramString, RowMapper<T> paramRowMapper, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract <T> T queryForObject(String paramString, Object[] paramArrayOfObject, int[] paramArrayOfInt, Class<T> paramClass)
    throws DataAccessException;
  
  public abstract <T> T queryForObject(String paramString, Object[] paramArrayOfObject, Class<T> paramClass)
    throws DataAccessException;
  
  public abstract <T> T queryForObject(String paramString, Class<T> paramClass, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract Map<String, Object> queryForMap(String paramString, Object[] paramArrayOfObject, int[] paramArrayOfInt)
    throws DataAccessException;
  
  public abstract Map<String, Object> queryForMap(String paramString, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract long queryForLong(String paramString, Object[] paramArrayOfObject, int[] paramArrayOfInt)
    throws DataAccessException;
  
  public abstract long queryForLong(String paramString, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract int queryForInt(String paramString, Object[] paramArrayOfObject, int[] paramArrayOfInt)
    throws DataAccessException;
  
  public abstract int queryForInt(String paramString, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract <T> List<T> queryForList(String paramString, Object[] paramArrayOfObject, int[] paramArrayOfInt, Class<T> paramClass)
    throws DataAccessException;
  
  public abstract <T> List<T> queryForList(String paramString, Object[] paramArrayOfObject, Class<T> paramClass)
    throws DataAccessException;
  
  public abstract <T> List<T> queryForList(String paramString, Class<T> paramClass, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract List<Map<String, Object>> queryForList(String paramString, Object[] paramArrayOfObject, int[] paramArrayOfInt)
    throws DataAccessException;
  
  public abstract List<Map<String, Object>> queryForList(String paramString, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract SqlRowSet queryForRowSet(String paramString, Object[] paramArrayOfObject, int[] paramArrayOfInt)
    throws DataAccessException;
  
  public abstract SqlRowSet queryForRowSet(String paramString, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract int update(PreparedStatementCreator paramPreparedStatementCreator)
    throws DataAccessException;
  
  public abstract int update(PreparedStatementCreator paramPreparedStatementCreator, KeyHolder paramKeyHolder)
    throws DataAccessException;
  
  public abstract int update(String paramString, PreparedStatementSetter paramPreparedStatementSetter)
    throws DataAccessException;
  
  public abstract int update(String paramString, Object[] paramArrayOfObject, int[] paramArrayOfInt)
    throws DataAccessException;
  
  public abstract int update(String paramString, Object... paramVarArgs)
    throws DataAccessException;
  
  public abstract int[] batchUpdate(String paramString, BatchPreparedStatementSetter paramBatchPreparedStatementSetter)
    throws DataAccessException;
  
  public abstract int[] batchUpdate(String paramString, List<Object[]> paramList);
  
  public abstract int[] batchUpdate(String paramString, List<Object[]> paramList, int[] paramArrayOfInt);
  
  public abstract <T> int[][] batchUpdate(String paramString, Collection<T> paramCollection, int paramInt, ParameterizedPreparedStatementSetter<T> paramParameterizedPreparedStatementSetter);
  
  public abstract <T> T execute(CallableStatementCreator paramCallableStatementCreator, CallableStatementCallback<T> paramCallableStatementCallback)
    throws DataAccessException;
  
  public abstract <T> T execute(String paramString, CallableStatementCallback<T> paramCallableStatementCallback)
    throws DataAccessException;
  
  public abstract Map<String, Object> call(CallableStatementCreator paramCallableStatementCreator, List<SqlParameter> paramList)
    throws DataAccessException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.JdbcOperations
 * JD-Core Version:    0.7.0.1
 */