package org.springframework.http.server;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;

public abstract interface ServerHttpResponse
  extends HttpOutputMessage
{
  public abstract void setStatusCode(HttpStatus paramHttpStatus);
  
  public abstract void close();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.server.ServerHttpResponse
 * JD-Core Version:    0.7.0.1
 */