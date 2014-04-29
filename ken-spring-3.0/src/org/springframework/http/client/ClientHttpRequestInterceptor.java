package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpRequest;

public abstract interface ClientHttpRequestInterceptor
{
  public abstract ClientHttpResponse intercept(HttpRequest paramHttpRequest, byte[] paramArrayOfByte, ClientHttpRequestExecution paramClientHttpRequestExecution)
    throws IOException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.ClientHttpRequestInterceptor
 * JD-Core Version:    0.7.0.1
 */