package org.springframework.web.servlet.mvc.support;

import java.util.Collection;
import java.util.Map;
import org.springframework.ui.Model;

public abstract interface RedirectAttributes
  extends Model
{
  public abstract RedirectAttributes addAttribute(String paramString, Object paramObject);
  
  public abstract RedirectAttributes addAttribute(Object paramObject);
  
  public abstract RedirectAttributes addAllAttributes(Collection<?> paramCollection);
  
  public abstract RedirectAttributes mergeAttributes(Map<String, ?> paramMap);
  
  public abstract RedirectAttributes addFlashAttribute(String paramString, Object paramObject);
  
  public abstract RedirectAttributes addFlashAttribute(Object paramObject);
  
  public abstract Map<String, ?> getFlashAttributes();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.support.RedirectAttributes
 * JD-Core Version:    0.7.0.1
 */