package org.springframework.jdbc.datasource.embedded;

import javax.sql.DataSource;

public abstract interface EmbeddedDatabaseConfigurer
{
  public abstract void configureConnectionProperties(ConnectionProperties paramConnectionProperties, String paramString);
  
  public abstract void shutdown(DataSource paramDataSource, String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseConfigurer
 * JD-Core Version:    0.7.0.1
 */