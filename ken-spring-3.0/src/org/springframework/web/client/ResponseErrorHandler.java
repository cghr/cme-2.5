package org.springframework.web.client;

import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;

public abstract interface ResponseErrorHandler
{
  public abstract boolean hasError(ClientHttpResponse paramClientHttpResponse)
    throws IOException;
  
  public abstract void handleError(ClientHttpResponse paramClientHttpResponse)
    throws IOException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.client.ResponseErrorHandler
 * JD-Core Version:    0.7.0.1
 */