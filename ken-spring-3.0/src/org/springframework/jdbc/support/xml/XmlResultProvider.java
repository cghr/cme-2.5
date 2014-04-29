package org.springframework.jdbc.support.xml;

import javax.xml.transform.Result;

public abstract interface XmlResultProvider
{
  public abstract void provideXml(Result paramResult);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.xml.XmlResultProvider
 * JD-Core Version:    0.7.0.1
 */