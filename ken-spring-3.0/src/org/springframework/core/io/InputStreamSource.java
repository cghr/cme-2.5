package org.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;

public abstract interface InputStreamSource
{
  public abstract InputStream getInputStream()
    throws IOException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.InputStreamSource
 * JD-Core Version:    0.7.0.1
 */