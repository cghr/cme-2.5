package org.springframework.ui;

import java.util.Collection;
import java.util.Map;

public abstract interface Model
{
  public abstract Model addAttribute(String paramString, Object paramObject);
  
  public abstract Model addAttribute(Object paramObject);
  
  public abstract Model addAllAttributes(Collection<?> paramCollection);
  
  public abstract Model addAllAttributes(Map<String, ?> paramMap);
  
  public abstract Model mergeAttributes(Map<String, ?> paramMap);
  
  public abstract boolean containsAttribute(String paramString);
  
  public abstract Map<String, Object> asMap();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ui.Model
 * JD-Core Version:    0.7.0.1
 */