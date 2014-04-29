package org.springframework.core.type.classreading;

import java.io.IOException;
import org.springframework.core.io.Resource;

public abstract interface MetadataReaderFactory
{
  public abstract MetadataReader getMetadataReader(String paramString)
    throws IOException;
  
  public abstract MetadataReader getMetadataReader(Resource paramResource)
    throws IOException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.classreading.MetadataReaderFactory
 * JD-Core Version:    0.7.0.1
 */