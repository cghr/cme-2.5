package org.springframework.http;

import java.net.URI;

public abstract interface HttpRequest
  extends HttpMessage
{
  public abstract HttpMethod getMethod();
  
  public abstract URI getURI();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.HttpRequest
 * JD-Core Version:    0.7.0.1
 */