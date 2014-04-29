package org.springframework.jdbc.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public abstract interface ParameterMapper
{
  public abstract Map<String, ?> createMap(Connection paramConnection)
    throws SQLException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.ParameterMapper
 * JD-Core Version:    0.7.0.1
 */