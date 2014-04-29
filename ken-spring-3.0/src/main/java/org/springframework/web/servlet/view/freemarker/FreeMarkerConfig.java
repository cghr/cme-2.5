package org.springframework.web.servlet.view.freemarker;

import freemarker.ext.jsp.TaglibFactory;
import freemarker.template.Configuration;

public abstract interface FreeMarkerConfig
{
  public abstract Configuration getConfiguration();
  
  public abstract TaglibFactory getTaglibFactory();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.freemarker.FreeMarkerConfig
 * JD-Core Version:    0.7.0.1
 */