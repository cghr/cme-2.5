package org.springframework.web.client;

import java.io.IOException;
import org.springframework.http.client.ClientHttpRequest;

public abstract interface RequestCallback
{
  public abstract void doWithRequest(ClientHttpRequest paramClientHttpRequest)
    throws IOException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.client.RequestCallback
 * JD-Core Version:    0.7.0.1
 */