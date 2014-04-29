package org.springframework.jdbc.support;

import java.sql.SQLException;
import org.springframework.dao.DataAccessException;

public abstract interface SQLExceptionTranslator
{
  public abstract DataAccessException translate(String paramString1, String paramString2, SQLException paramSQLException);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.SQLExceptionTranslator
 * JD-Core Version:    0.7.0.1
 */