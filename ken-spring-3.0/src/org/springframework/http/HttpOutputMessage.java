package org.springframework.http;

import java.io.IOException;
import java.io.OutputStream;

public abstract interface HttpOutputMessage
  extends HttpMessage
{
  public abstract OutputStream getBody()
    throws IOException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.HttpOutputMessage
 * JD-Core Version:    0.7.0.1
 */