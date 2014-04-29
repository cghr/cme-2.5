/*   1:    */ package org.springframework.beans.factory.xml;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*   6:    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*   7:    */ import org.w3c.dom.Attr;
/*   8:    */ import org.w3c.dom.Element;
/*   9:    */ import org.w3c.dom.Node;
/*  10:    */ 
/*  11:    */ public abstract class NamespaceHandlerSupport
/*  12:    */   implements NamespaceHandler
/*  13:    */ {
/*  14: 51 */   private final Map<String, BeanDefinitionParser> parsers = new HashMap();
/*  15: 58 */   private final Map<String, BeanDefinitionDecorator> decorators = new HashMap();
/*  16: 65 */   private final Map<String, BeanDefinitionDecorator> attributeDecorators = new HashMap();
/*  17:    */   
/*  18:    */   public BeanDefinition parse(Element element, ParserContext parserContext)
/*  19:    */   {
/*  20: 73 */     return findParserForElement(element, parserContext).parse(element, parserContext);
/*  21:    */   }
/*  22:    */   
/*  23:    */   private BeanDefinitionParser findParserForElement(Element element, ParserContext parserContext)
/*  24:    */   {
/*  25: 81 */     String localName = parserContext.getDelegate().getLocalName(element);
/*  26: 82 */     BeanDefinitionParser parser = (BeanDefinitionParser)this.parsers.get(localName);
/*  27: 83 */     if (parser == null) {
/*  28: 84 */       parserContext.getReaderContext().fatal(
/*  29: 85 */         "Cannot locate BeanDefinitionParser for element [" + localName + "]", element);
/*  30:    */     }
/*  31: 87 */     return parser;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext)
/*  35:    */   {
/*  36: 97 */     return findDecoratorForNode(node, parserContext).decorate(node, definition, parserContext);
/*  37:    */   }
/*  38:    */   
/*  39:    */   private BeanDefinitionDecorator findDecoratorForNode(Node node, ParserContext parserContext)
/*  40:    */   {
/*  41:106 */     BeanDefinitionDecorator decorator = null;
/*  42:107 */     String localName = parserContext.getDelegate().getLocalName(node);
/*  43:108 */     if ((node instanceof Element)) {
/*  44:109 */       decorator = (BeanDefinitionDecorator)this.decorators.get(localName);
/*  45:111 */     } else if ((node instanceof Attr)) {
/*  46:112 */       decorator = (BeanDefinitionDecorator)this.attributeDecorators.get(localName);
/*  47:    */     } else {
/*  48:115 */       parserContext.getReaderContext().fatal(
/*  49:116 */         "Cannot decorate based on Nodes of type [" + node.getClass().getName() + "]", node);
/*  50:    */     }
/*  51:118 */     if (decorator == null) {
/*  52:119 */       parserContext.getReaderContext().fatal("Cannot locate BeanDefinitionDecorator for " + (
/*  53:120 */         (node instanceof Element) ? "element" : "attribute") + " [" + localName + "]", node);
/*  54:    */     }
/*  55:122 */     return decorator;
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected final void registerBeanDefinitionParser(String elementName, BeanDefinitionParser parser)
/*  59:    */   {
/*  60:132 */     this.parsers.put(elementName, parser);
/*  61:    */   }
/*  62:    */   
/*  63:    */   protected final void registerBeanDefinitionDecorator(String elementName, BeanDefinitionDecorator dec)
/*  64:    */   {
/*  65:141 */     this.decorators.put(elementName, dec);
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected final void registerBeanDefinitionDecoratorForAttribute(String attrName, BeanDefinitionDecorator dec)
/*  69:    */   {
/*  70:150 */     this.attributeDecorators.put(attrName, dec);
/*  71:    */   }
/*  72:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.NamespaceHandlerSupport
 * JD-Core Version:    0.7.0.1
 */