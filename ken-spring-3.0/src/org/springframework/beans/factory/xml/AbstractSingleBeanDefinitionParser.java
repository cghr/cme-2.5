/*   1:    */ package org.springframework.beans.factory.xml;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*   4:    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*   5:    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*   6:    */ import org.w3c.dom.Element;
/*   7:    */ 
/*   8:    */ public abstract class AbstractSingleBeanDefinitionParser
/*   9:    */   extends AbstractBeanDefinitionParser
/*  10:    */ {
/*  11:    */   protected final AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext)
/*  12:    */   {
/*  13: 61 */     BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
/*  14: 62 */     String parentName = getParentName(element);
/*  15: 63 */     if (parentName != null) {
/*  16: 64 */       builder.getRawBeanDefinition().setParentName(parentName);
/*  17:    */     }
/*  18: 66 */     Class<?> beanClass = getBeanClass(element);
/*  19: 67 */     if (beanClass != null)
/*  20:    */     {
/*  21: 68 */       builder.getRawBeanDefinition().setBeanClass(beanClass);
/*  22:    */     }
/*  23:    */     else
/*  24:    */     {
/*  25: 71 */       String beanClassName = getBeanClassName(element);
/*  26: 72 */       if (beanClassName != null) {
/*  27: 73 */         builder.getRawBeanDefinition().setBeanClassName(beanClassName);
/*  28:    */       }
/*  29:    */     }
/*  30: 76 */     builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));
/*  31: 77 */     if (parserContext.isNested()) {
/*  32: 79 */       builder.setScope(parserContext.getContainingBeanDefinition().getScope());
/*  33:    */     }
/*  34: 81 */     if (parserContext.isDefaultLazyInit()) {
/*  35: 83 */       builder.setLazyInit(true);
/*  36:    */     }
/*  37: 85 */     doParse(element, parserContext, builder);
/*  38: 86 */     return builder.getBeanDefinition();
/*  39:    */   }
/*  40:    */   
/*  41:    */   protected String getParentName(Element element)
/*  42:    */   {
/*  43: 99 */     return null;
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected Class<?> getBeanClass(Element element)
/*  47:    */   {
/*  48:115 */     return null;
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected String getBeanClassName(Element element)
/*  52:    */   {
/*  53:126 */     return null;
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
/*  57:    */   {
/*  58:140 */     doParse(element, builder);
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected void doParse(Element element, BeanDefinitionBuilder builder) {}
/*  62:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */