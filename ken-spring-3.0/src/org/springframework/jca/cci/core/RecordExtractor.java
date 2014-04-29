package org.springframework.jca.cci.core;

import java.sql.SQLException;
import javax.resource.ResourceException;
import javax.resource.cci.Record;
import org.springframework.dao.DataAccessException;

public abstract interface RecordExtractor<T>
{
  public abstract T extractData(Record paramRecord)
    throws ResourceException, SQLException, DataAccessException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.core.RecordExtractor
 * JD-Core Version:    0.7.0.1
 */