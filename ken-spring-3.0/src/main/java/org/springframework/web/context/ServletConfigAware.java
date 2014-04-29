package org.springframework.web.context;

import javax.servlet.ServletConfig;
import org.springframework.beans.factory.Aware;

public abstract interface ServletConfigAware
  extends Aware
{
  public abstract void setServletConfig(ServletConfig paramServletConfig);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.ServletConfigAware
 * JD-Core Version:    0.7.0.1
 */