/*  1:   */ package org.springframework.beans.factory.xml;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.MutablePropertyValues;
/*  4:   */ import org.springframework.beans.factory.config.BeanDefinition;
/*  5:   */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*  6:   */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*  7:   */ import org.springframework.core.Conventions;
/*  8:   */ import org.w3c.dom.Attr;
/*  9:   */ import org.w3c.dom.Element;
/* 10:   */ import org.w3c.dom.Node;
/* 11:   */ 
/* 12:   */ public class SimplePropertyNamespaceHandler
/* 13:   */   implements NamespaceHandler
/* 14:   */ {
/* 15:   */   private static final String REF_SUFFIX = "-ref";
/* 16:   */   
/* 17:   */   public void init() {}
/* 18:   */   
/* 19:   */   public BeanDefinition parse(Element element, ParserContext parserContext)
/* 20:   */   {
/* 21:59 */     parserContext.getReaderContext().error(
/* 22:60 */       "Class [" + getClass().getName() + "] does not support custom elements.", element);
/* 23:61 */     return null;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext)
/* 27:   */   {
/* 28:65 */     if ((node instanceof Attr))
/* 29:   */     {
/* 30:66 */       Attr attr = (Attr)node;
/* 31:67 */       String propertyName = parserContext.getDelegate().getLocalName(attr);
/* 32:68 */       String propertyValue = attr.getValue();
/* 33:69 */       MutablePropertyValues pvs = definition.getBeanDefinition().getPropertyValues();
/* 34:70 */       if (pvs.contains(propertyName)) {
/* 35:71 */         parserContext.getReaderContext().error("Property '" + propertyName + "' is already defined using " + 
/* 36:72 */           "both <property> and inline syntax. Only one approach may be used per property.", attr);
/* 37:   */       }
/* 38:74 */       if (propertyName.endsWith("-ref"))
/* 39:   */       {
/* 40:75 */         propertyName = propertyName.substring(0, propertyName.length() - "-ref".length());
/* 41:76 */         pvs.add(Conventions.attributeNameToPropertyName(propertyName), new RuntimeBeanReference(propertyValue));
/* 42:   */       }
/* 43:   */       else
/* 44:   */       {
/* 45:79 */         pvs.add(Conventions.attributeNameToPropertyName(propertyName), propertyValue);
/* 46:   */       }
/* 47:   */     }
/* 48:82 */     return definition;
/* 49:   */   }
/* 50:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.SimplePropertyNamespaceHandler
 * JD-Core Version:    0.7.0.1
 */