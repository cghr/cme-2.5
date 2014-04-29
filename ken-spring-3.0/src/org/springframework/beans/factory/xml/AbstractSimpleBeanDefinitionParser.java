/*   1:    */ package org.springframework.beans.factory.xml;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*   4:    */ import org.springframework.core.Conventions;
/*   5:    */ import org.springframework.util.Assert;
/*   6:    */ import org.springframework.util.StringUtils;
/*   7:    */ import org.w3c.dom.Attr;
/*   8:    */ import org.w3c.dom.Element;
/*   9:    */ import org.w3c.dom.NamedNodeMap;
/*  10:    */ 
/*  11:    */ public abstract class AbstractSimpleBeanDefinitionParser
/*  12:    */   extends AbstractSingleBeanDefinitionParser
/*  13:    */ {
/*  14:    */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
/*  15:    */   {
/*  16:128 */     NamedNodeMap attributes = element.getAttributes();
/*  17:129 */     for (int x = 0; x < attributes.getLength(); x++)
/*  18:    */     {
/*  19:130 */       Attr attribute = (Attr)attributes.item(x);
/*  20:131 */       if (isEligibleAttribute(attribute, parserContext))
/*  21:    */       {
/*  22:132 */         String propertyName = extractPropertyName(attribute.getLocalName());
/*  23:133 */         Assert.state(StringUtils.hasText(propertyName), 
/*  24:134 */           "Illegal property name returned from 'extractPropertyName(String)': cannot be null or empty.");
/*  25:135 */         builder.addPropertyValue(propertyName, attribute.getValue());
/*  26:    */       }
/*  27:    */     }
/*  28:138 */     postProcess(builder, element);
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected boolean isEligibleAttribute(Attr attribute, ParserContext parserContext)
/*  32:    */   {
/*  33:151 */     boolean eligible = isEligibleAttribute(attribute);
/*  34:152 */     if (!eligible)
/*  35:    */     {
/*  36:153 */       String fullName = attribute.getName();
/*  37:154 */       eligible = (!fullName.equals("xmlns")) && (!fullName.startsWith("xmlns:")) && 
/*  38:155 */         (isEligibleAttribute(parserContext.getDelegate().getLocalName(attribute)));
/*  39:    */     }
/*  40:157 */     return eligible;
/*  41:    */   }
/*  42:    */   
/*  43:    */   @Deprecated
/*  44:    */   protected boolean isEligibleAttribute(Attr attribute)
/*  45:    */   {
/*  46:171 */     return false;
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected boolean isEligibleAttribute(String attributeName)
/*  50:    */   {
/*  51:183 */     return !"id".equals(attributeName);
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected String extractPropertyName(String attributeName)
/*  55:    */   {
/*  56:200 */     return Conventions.attributeNameToPropertyName(attributeName);
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected void postProcess(BeanDefinitionBuilder beanDefinition, Element element) {}
/*  60:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */