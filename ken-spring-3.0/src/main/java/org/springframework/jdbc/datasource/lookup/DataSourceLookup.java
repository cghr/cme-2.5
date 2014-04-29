package org.springframework.jdbc.datasource.lookup;

import javax.sql.DataSource;

public abstract interface DataSourceLookup
{
  public abstract DataSource getDataSource(String paramString)
    throws DataSourceLookupFailureException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.lookup.DataSourceLookup
 * JD-Core Version:    0.7.0.1
 */