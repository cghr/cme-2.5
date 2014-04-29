package org.springframework.jdbc.support.incrementer;

import org.springframework.dao.DataAccessException;

public abstract interface DataFieldMaxValueIncrementer
{
  public abstract int nextIntValue()
    throws DataAccessException;
  
  public abstract long nextLongValue()
    throws DataAccessException;
  
  public abstract String nextStringValue()
    throws DataAccessException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer
 * JD-Core Version:    0.7.0.1
 */