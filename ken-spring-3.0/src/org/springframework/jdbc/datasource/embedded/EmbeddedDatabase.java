package org.springframework.jdbc.datasource.embedded;

import javax.sql.DataSource;

public abstract interface EmbeddedDatabase
  extends DataSource
{
  public abstract void shutdown();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.embedded.EmbeddedDatabase
 * JD-Core Version:    0.7.0.1
 */