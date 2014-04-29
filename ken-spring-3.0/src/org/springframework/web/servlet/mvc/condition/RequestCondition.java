package org.springframework.web.servlet.mvc.condition;

import javax.servlet.http.HttpServletRequest;

public abstract interface RequestCondition<T>
{
  public abstract T combine(T paramT);
  
  public abstract T getMatchingCondition(HttpServletRequest paramHttpServletRequest);
  
  public abstract int compareTo(T paramT, HttpServletRequest paramHttpServletRequest);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.condition.RequestCondition
 * JD-Core Version:    0.7.0.1
 */