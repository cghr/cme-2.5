package org.springframework.jdbc.datasource.embedded;

import java.sql.Driver;

public abstract interface ConnectionProperties
{
  public abstract void setDriverClass(Class<? extends Driver> paramClass);
  
  public abstract void setUrl(String paramString);
  
  public abstract void setUsername(String paramString);
  
  public abstract void setPassword(String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.embedded.ConnectionProperties
 * JD-Core Version:    0.7.0.1
 */