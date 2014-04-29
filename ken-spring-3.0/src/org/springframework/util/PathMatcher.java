package org.springframework.util;

import java.util.Comparator;
import java.util.Map;

public abstract interface PathMatcher
{
  public abstract boolean isPattern(String paramString);
  
  public abstract boolean match(String paramString1, String paramString2);
  
  public abstract boolean matchStart(String paramString1, String paramString2);
  
  public abstract String extractPathWithinPattern(String paramString1, String paramString2);
  
  public abstract Map<String, String> extractUriTemplateVariables(String paramString1, String paramString2);
  
  public abstract Comparator<String> getPatternComparator(String paramString);
  
  public abstract String combine(String paramString1, String paramString2);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.PathMatcher
 * JD-Core Version:    0.7.0.1
 */