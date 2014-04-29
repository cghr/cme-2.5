package org.springframework.http.client;

import java.io.IOException;
import java.net.URI;
import org.springframework.http.HttpMethod;

public abstract interface ClientHttpRequestFactory
{
  public abstract ClientHttpRequest createRequest(URI paramURI, HttpMethod paramHttpMethod)
    throws IOException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.ClientHttpRequestFactory
 * JD-Core Version:    0.7.0.1
 */