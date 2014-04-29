package org.springframework.jca.cci.core;

import javax.resource.ResourceException;
import javax.resource.cci.Record;
import javax.resource.cci.RecordFactory;
import org.springframework.dao.DataAccessException;

public abstract interface RecordCreator
{
  public abstract Record createRecord(RecordFactory paramRecordFactory)
    throws ResourceException, DataAccessException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.core.RecordCreator
 * JD-Core Version:    0.7.0.1
 */