package org.springframework.jdbc.datasource.init;

import java.sql.Connection;
import java.sql.SQLException;

public abstract interface DatabasePopulator
{
  public abstract void populate(Connection paramConnection)
    throws SQLException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.init.DatabasePopulator
 * JD-Core Version:    0.7.0.1
 */