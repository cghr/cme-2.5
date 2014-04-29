package org.springframework.core.io;

import java.io.IOException;
import java.io.OutputStream;

public abstract interface WritableResource
  extends Resource
{
  public abstract boolean isWritable();
  
  public abstract OutputStream getOutputStream()
    throws IOException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.WritableResource
 * JD-Core Version:    0.7.0.1
 */