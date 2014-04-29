/*  1:   */ package org.springframework.jdbc.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*  4:   */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*  5:   */ import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
/*  6:   */ import org.springframework.beans.factory.xml.ParserContext;
/*  7:   */ import org.springframework.jdbc.datasource.init.DataSourceInitializer;
/*  8:   */ import org.w3c.dom.Element;
/*  9:   */ 
/* 10:   */ class InitializeDatabaseBeanDefinitionParser
/* 11:   */   extends AbstractBeanDefinitionParser
/* 12:   */ {
/* 13:   */   protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext)
/* 14:   */   {
/* 15:42 */     BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(DataSourceInitializer.class);
/* 16:43 */     builder.addPropertyReference("dataSource", element.getAttribute("data-source"));
/* 17:44 */     builder.addPropertyValue("enabled", element.getAttribute("enabled"));
/* 18:45 */     DatabasePopulatorConfigUtils.setDatabasePopulator(element, builder);
/* 19:46 */     builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));
/* 20:47 */     return builder.getBeanDefinition();
/* 21:   */   }
/* 22:   */   
/* 23:   */   protected boolean shouldGenerateId()
/* 24:   */   {
/* 25:52 */     return true;
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.config.InitializeDatabaseBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */