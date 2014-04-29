package org.springframework.jdbc.datasource;

import java.sql.Connection;
import javax.sql.DataSource;

public abstract interface SmartDataSource
  extends DataSource
{
  public abstract boolean shouldClose(Connection paramConnection);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.SmartDataSource
 * JD-Core Version:    0.7.0.1
 */