package org.springframework.jdbc.datasource.embedded;

import javax.sql.DataSource;

public abstract interface DataSourceFactory
{
  public abstract ConnectionProperties getConnectionProperties();
  
  public abstract DataSource getDataSource();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.embedded.DataSourceFactory
 * JD-Core Version:    0.7.0.1
 */