package org.springframework.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

public abstract interface PropertiesPersister
{
  public abstract void load(Properties paramProperties, InputStream paramInputStream)
    throws IOException;
  
  public abstract void load(Properties paramProperties, Reader paramReader)
    throws IOException;
  
  public abstract void store(Properties paramProperties, OutputStream paramOutputStream, String paramString)
    throws IOException;
  
  public abstract void store(Properties paramProperties, Writer paramWriter, String paramString)
    throws IOException;
  
  public abstract void loadFromXml(Properties paramProperties, InputStream paramInputStream)
    throws IOException;
  
  public abstract void storeToXml(Properties paramProperties, OutputStream paramOutputStream, String paramString)
    throws IOException;
  
  public abstract void storeToXml(Properties paramProperties, OutputStream paramOutputStream, String paramString1, String paramString2)
    throws IOException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.PropertiesPersister
 * JD-Core Version:    0.7.0.1
 */