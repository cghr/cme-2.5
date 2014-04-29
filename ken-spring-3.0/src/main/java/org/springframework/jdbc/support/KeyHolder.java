package org.springframework.jdbc.support;

import java.util.List;
import java.util.Map;
import org.springframework.dao.InvalidDataAccessApiUsageException;

public abstract interface KeyHolder
{
  public abstract Number getKey()
    throws InvalidDataAccessApiUsageException;
  
  public abstract Map<String, Object> getKeys()
    throws InvalidDataAccessApiUsageException;
  
  public abstract List<Map<String, Object>> getKeyList();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.KeyHolder
 * JD-Core Version:    0.7.0.1
 */