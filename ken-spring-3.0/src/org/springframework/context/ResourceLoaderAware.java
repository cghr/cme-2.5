package org.springframework.context;

import org.springframework.beans.factory.Aware;
import org.springframework.core.io.ResourceLoader;

public abstract interface ResourceLoaderAware
  extends Aware
{
  public abstract void setResourceLoader(ResourceLoader paramResourceLoader);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.ResourceLoaderAware
 * JD-Core Version:    0.7.0.1
 */