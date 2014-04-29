package org.springframework.web.multipart;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public abstract interface MultipartHttpServletRequest
  extends HttpServletRequest, MultipartRequest
{
  public abstract HttpMethod getRequestMethod();
  
  public abstract HttpHeaders getRequestHeaders();
  
  public abstract HttpHeaders getMultipartHeaders(String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.multipart.MultipartHttpServletRequest
 * JD-Core Version:    0.7.0.1
 */