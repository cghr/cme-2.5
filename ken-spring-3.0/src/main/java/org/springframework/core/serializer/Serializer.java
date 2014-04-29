package org.springframework.core.serializer;

import java.io.IOException;
import java.io.OutputStream;

public abstract interface Serializer<T>
{
  public abstract void serialize(T paramT, OutputStream paramOutputStream)
    throws IOException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.serializer.Serializer
 * JD-Core Version:    0.7.0.1
 */