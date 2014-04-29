package org.springframework.web.servlet.mvc;

import javax.servlet.http.HttpServletRequest;

public abstract interface LastModified
{
  public abstract long getLastModified(HttpServletRequest paramHttpServletRequest);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.LastModified
 * JD-Core Version:    0.7.0.1
 */