package org.springframework.dao.support;

import org.springframework.dao.DataAccessException;

public abstract interface PersistenceExceptionTranslator
{
  public abstract DataAccessException translateExceptionIfPossible(RuntimeException paramRuntimeException);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.support.PersistenceExceptionTranslator
 * JD-Core Version:    0.7.0.1
 */