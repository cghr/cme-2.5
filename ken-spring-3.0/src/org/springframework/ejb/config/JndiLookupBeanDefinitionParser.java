/*  1:   */ package org.springframework.ejb.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*  4:   */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*  5:   */ import org.springframework.beans.factory.xml.ParserContext;
/*  6:   */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*  7:   */ import org.springframework.jndi.JndiObjectFactoryBean;
/*  8:   */ import org.springframework.util.StringUtils;
/*  9:   */ import org.w3c.dom.Element;
/* 10:   */ 
/* 11:   */ class JndiLookupBeanDefinitionParser
/* 12:   */   extends AbstractJndiLocatingBeanDefinitionParser
/* 13:   */ {
/* 14:   */   public static final String DEFAULT_VALUE = "default-value";
/* 15:   */   public static final String DEFAULT_REF = "default-ref";
/* 16:   */   public static final String DEFAULT_OBJECT = "defaultObject";
/* 17:   */   
/* 18:   */   protected Class getBeanClass(Element element)
/* 19:   */   {
/* 20:47 */     return JndiObjectFactoryBean.class;
/* 21:   */   }
/* 22:   */   
/* 23:   */   protected boolean isEligibleAttribute(String attributeName)
/* 24:   */   {
/* 25:53 */     return (super.isEligibleAttribute(attributeName)) && (!"default-value".equals(attributeName)) && (!"default-ref".equals(attributeName));
/* 26:   */   }
/* 27:   */   
/* 28:   */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
/* 29:   */   {
/* 30:58 */     super.doParse(element, parserContext, builder);
/* 31:   */     
/* 32:60 */     String defaultValue = element.getAttribute("default-value");
/* 33:61 */     String defaultRef = element.getAttribute("default-ref");
/* 34:62 */     if (StringUtils.hasLength(defaultValue))
/* 35:   */     {
/* 36:63 */       if (StringUtils.hasLength(defaultRef)) {
/* 37:64 */         parserContext.getReaderContext().error("<jndi-lookup> element is only allowed to contain either 'default-value' attribute OR 'default-ref' attribute, not both", 
/* 38:65 */           element);
/* 39:   */       }
/* 40:67 */       builder.addPropertyValue("defaultObject", defaultValue);
/* 41:   */     }
/* 42:69 */     else if (StringUtils.hasLength(defaultRef))
/* 43:   */     {
/* 44:70 */       builder.addPropertyValue("defaultObject", new RuntimeBeanReference(defaultRef));
/* 45:   */     }
/* 46:   */   }
/* 47:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ejb.config.JndiLookupBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */