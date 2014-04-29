package org.springframework.web.client;

import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;

public abstract interface ResponseExtractor<T>
{
  public abstract T extractData(ClientHttpResponse paramClientHttpResponse)
    throws IOException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.client.ResponseExtractor
 * JD-Core Version:    0.7.0.1
 */