package org.springframework.web.multipart;

import javax.servlet.http.HttpServletRequest;

public abstract interface MultipartResolver
{
  public abstract boolean isMultipart(HttpServletRequest paramHttpServletRequest);
  
  public abstract MultipartHttpServletRequest resolveMultipart(HttpServletRequest paramHttpServletRequest)
    throws MultipartException;
  
  public abstract void cleanupMultipart(MultipartHttpServletRequest paramMultipartHttpServletRequest);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.multipart.MultipartResolver
 * JD-Core Version:    0.7.0.1
 */