package org.springframework.beans.factory.xml;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

public abstract interface DocumentLoader
{
  public abstract Document loadDocument(InputSource paramInputSource, EntityResolver paramEntityResolver, ErrorHandler paramErrorHandler, int paramInt, boolean paramBoolean)
    throws Exception;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.DocumentLoader
 * JD-Core Version:    0.7.0.1
 */