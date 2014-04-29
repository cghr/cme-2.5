package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.env.Environment;
import org.w3c.dom.Document;

public abstract interface BeanDefinitionDocumentReader
{
  public abstract void registerBeanDefinitions(Document paramDocument, XmlReaderContext paramXmlReaderContext)
    throws BeanDefinitionStoreException;
  
  public abstract void setEnvironment(Environment paramEnvironment);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.BeanDefinitionDocumentReader
 * JD-Core Version:    0.7.0.1
 */