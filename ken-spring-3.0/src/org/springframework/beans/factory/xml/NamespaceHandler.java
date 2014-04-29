package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract interface NamespaceHandler
{
  public abstract void init();
  
  public abstract BeanDefinition parse(Element paramElement, ParserContext paramParserContext);
  
  public abstract BeanDefinitionHolder decorate(Node paramNode, BeanDefinitionHolder paramBeanDefinitionHolder, ParserContext paramParserContext);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.NamespaceHandler
 * JD-Core Version:    0.7.0.1
 */