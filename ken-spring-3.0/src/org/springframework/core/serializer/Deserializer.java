package org.springframework.core.serializer;

import java.io.IOException;
import java.io.InputStream;

public abstract interface Deserializer<T>
{
  public abstract T deserialize(InputStream paramInputStream)
    throws IOException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.serializer.Deserializer
 * JD-Core Version:    0.7.0.1
 */