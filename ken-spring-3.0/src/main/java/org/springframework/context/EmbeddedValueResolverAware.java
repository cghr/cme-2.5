package org.springframework.context;

import org.springframework.beans.factory.Aware;
import org.springframework.util.StringValueResolver;

public abstract interface EmbeddedValueResolverAware
  extends Aware
{
  public abstract void setEmbeddedValueResolver(StringValueResolver paramStringValueResolver);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.EmbeddedValueResolverAware
 * JD-Core Version:    0.7.0.1
 */