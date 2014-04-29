package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;

public abstract interface ClientHttpResponse
  extends HttpInputMessage
{
  public abstract HttpStatus getStatusCode()
    throws IOException;
  
  public abstract String getStatusText()
    throws IOException;
  
  public abstract void close();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.ClientHttpResponse
 * JD-Core Version:    0.7.0.1
 */