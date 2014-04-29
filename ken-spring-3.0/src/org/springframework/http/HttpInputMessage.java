package org.springframework.http;

import java.io.IOException;
import java.io.InputStream;

public abstract interface HttpInputMessage
  extends HttpMessage
{
  public abstract InputStream getBody()
    throws IOException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.HttpInputMessage
 * JD-Core Version:    0.7.0.1
 */