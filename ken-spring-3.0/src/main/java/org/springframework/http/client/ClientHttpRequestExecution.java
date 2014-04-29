package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpRequest;

public abstract interface ClientHttpRequestExecution
{
  public abstract ClientHttpResponse execute(HttpRequest paramHttpRequest, byte[] paramArrayOfByte)
    throws IOException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.ClientHttpRequestExecution
 * JD-Core Version:    0.7.0.1
 */