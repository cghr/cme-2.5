package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.w3c.dom.Element;

public abstract interface BeanDefinitionParser
{
  public abstract BeanDefinition parse(Element paramElement, ParserContext paramParserContext);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.BeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */