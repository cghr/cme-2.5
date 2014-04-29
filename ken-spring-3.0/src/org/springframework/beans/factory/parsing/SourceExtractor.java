package org.springframework.beans.factory.parsing;

import org.springframework.core.io.Resource;

public abstract interface SourceExtractor
{
  public abstract Object extractSource(Object paramObject, Resource paramResource);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.SourceExtractor
 * JD-Core Version:    0.7.0.1
 */