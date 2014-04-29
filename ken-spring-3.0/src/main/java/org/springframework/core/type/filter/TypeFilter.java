package org.springframework.core.type.filter;

import java.io.IOException;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

public abstract interface TypeFilter
{
  public abstract boolean match(MetadataReader paramMetadataReader, MetadataReaderFactory paramMetadataReaderFactory)
    throws IOException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.filter.TypeFilter
 * JD-Core Version:    0.7.0.1
 */