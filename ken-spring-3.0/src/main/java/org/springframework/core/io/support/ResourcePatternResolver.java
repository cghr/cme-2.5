package org.springframework.core.io.support;

import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public abstract interface ResourcePatternResolver
  extends ResourceLoader
{
  public static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
  
  public abstract Resource[] getResources(String paramString)
    throws IOException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.support.ResourcePatternResolver
 * JD-Core Version:    0.7.0.1
 */