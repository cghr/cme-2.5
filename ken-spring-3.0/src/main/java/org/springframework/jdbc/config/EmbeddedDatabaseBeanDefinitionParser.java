/*  1:   */ package org.springframework.jdbc.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*  4:   */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*  5:   */ import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
/*  6:   */ import org.springframework.beans.factory.xml.ParserContext;
/*  7:   */ import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactoryBean;
/*  8:   */ import org.springframework.util.StringUtils;
/*  9:   */ import org.w3c.dom.Element;
/* 10:   */ 
/* 11:   */ class EmbeddedDatabaseBeanDefinitionParser
/* 12:   */   extends AbstractBeanDefinitionParser
/* 13:   */ {
/* 14:   */   protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext)
/* 15:   */   {
/* 16:43 */     BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(EmbeddedDatabaseFactoryBean.class);
/* 17:44 */     setDatabaseType(element, builder);
/* 18:45 */     DatabasePopulatorConfigUtils.setDatabasePopulator(element, builder);
/* 19:46 */     useIdAsDatabaseNameIfGiven(element, builder);
/* 20:47 */     builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));
/* 21:48 */     return builder.getBeanDefinition();
/* 22:   */   }
/* 23:   */   
/* 24:   */   private void useIdAsDatabaseNameIfGiven(Element element, BeanDefinitionBuilder builder)
/* 25:   */   {
/* 26:52 */     String id = element.getAttribute("id");
/* 27:53 */     if (StringUtils.hasText(id)) {
/* 28:54 */       builder.addPropertyValue("databaseName", id);
/* 29:   */     }
/* 30:   */   }
/* 31:   */   
/* 32:   */   private void setDatabaseType(Element element, BeanDefinitionBuilder builder)
/* 33:   */   {
/* 34:59 */     String type = element.getAttribute("type");
/* 35:60 */     if (StringUtils.hasText(type)) {
/* 36:61 */       builder.addPropertyValue("databaseType", type);
/* 37:   */     }
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.config.EmbeddedDatabaseBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */