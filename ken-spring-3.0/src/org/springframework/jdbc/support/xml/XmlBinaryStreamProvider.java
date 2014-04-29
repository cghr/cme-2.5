package org.springframework.jdbc.support.xml;

import java.io.IOException;
import java.io.OutputStream;

public abstract interface XmlBinaryStreamProvider
{
  public abstract void provideXml(OutputStream paramOutputStream)
    throws IOException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.xml.XmlBinaryStreamProvider
 * JD-Core Version:    0.7.0.1
 */