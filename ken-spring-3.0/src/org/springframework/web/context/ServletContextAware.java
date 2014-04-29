package org.springframework.web.context;

import javax.servlet.ServletContext;
import org.springframework.beans.factory.Aware;

public abstract interface ServletContextAware
  extends Aware
{
  public abstract void setServletContext(ServletContext paramServletContext);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.ServletContextAware
 * JD-Core Version:    0.7.0.1
 */