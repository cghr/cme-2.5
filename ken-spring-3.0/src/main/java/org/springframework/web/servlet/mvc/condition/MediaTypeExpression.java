package org.springframework.web.servlet.mvc.condition;

import org.springframework.http.MediaType;

public abstract interface MediaTypeExpression
{
  public abstract MediaType getMediaType();
  
  public abstract boolean isNegated();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.condition.MediaTypeExpression
 * JD-Core Version:    0.7.0.1
 */