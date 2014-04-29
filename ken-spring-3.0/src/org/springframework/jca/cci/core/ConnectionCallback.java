package org.springframework.jca.cci.core;

import java.sql.SQLException;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import org.springframework.dao.DataAccessException;

public abstract interface ConnectionCallback<T>
{
  public abstract T doInConnection(Connection paramConnection, ConnectionFactory paramConnectionFactory)
    throws ResourceException, SQLException, DataAccessException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.core.ConnectionCallback
 * JD-Core Version:    0.7.0.1
 */