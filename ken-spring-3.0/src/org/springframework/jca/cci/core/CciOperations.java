package org.springframework.jca.cci.core;

import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;
import org.springframework.dao.DataAccessException;

public abstract interface CciOperations
{
  public abstract <T> T execute(ConnectionCallback<T> paramConnectionCallback)
    throws DataAccessException;
  
  public abstract <T> T execute(InteractionCallback<T> paramInteractionCallback)
    throws DataAccessException;
  
  public abstract Record execute(InteractionSpec paramInteractionSpec, Record paramRecord)
    throws DataAccessException;
  
  public abstract void execute(InteractionSpec paramInteractionSpec, Record paramRecord1, Record paramRecord2)
    throws DataAccessException;
  
  public abstract Record execute(InteractionSpec paramInteractionSpec, RecordCreator paramRecordCreator)
    throws DataAccessException;
  
  public abstract <T> T execute(InteractionSpec paramInteractionSpec, Record paramRecord, RecordExtractor<T> paramRecordExtractor)
    throws DataAccessException;
  
  public abstract <T> T execute(InteractionSpec paramInteractionSpec, RecordCreator paramRecordCreator, RecordExtractor<T> paramRecordExtractor)
    throws DataAccessException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.core.CciOperations
 * JD-Core Version:    0.7.0.1
 */