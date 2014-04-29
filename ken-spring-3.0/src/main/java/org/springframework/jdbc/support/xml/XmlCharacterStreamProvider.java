package org.springframework.jdbc.support.xml;

import java.io.IOException;
import java.io.Writer;

public abstract interface XmlCharacterStreamProvider
{
  public abstract void provideXml(Writer paramWriter)
    throws IOException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.xml.XmlCharacterStreamProvider
 * JD-Core Version:    0.7.0.1
 */