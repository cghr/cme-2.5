package org.springframework.web.servlet.support;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public abstract interface RequestDataValueProcessor
{
  public abstract String processAction(HttpServletRequest paramHttpServletRequest, String paramString);
  
  public abstract String processFormFieldValue(HttpServletRequest paramHttpServletRequest, String paramString1, String paramString2, String paramString3);
  
  public abstract Map<String, String> getExtraHiddenFields(HttpServletRequest paramHttpServletRequest);
  
  public abstract String processUrl(HttpServletRequest paramHttpServletRequest, String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.support.RequestDataValueProcessor
 * JD-Core Version:    0.7.0.1
 */