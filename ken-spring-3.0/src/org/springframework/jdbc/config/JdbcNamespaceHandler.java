/*  1:   */ package org.springframework.jdbc.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
/*  4:   */ 
/*  5:   */ public class JdbcNamespaceHandler
/*  6:   */   extends NamespaceHandlerSupport
/*  7:   */ {
/*  8:   */   public void init()
/*  9:   */   {
/* 10:30 */     registerBeanDefinitionParser("embedded-database", new EmbeddedDatabaseBeanDefinitionParser());
/* 11:31 */     registerBeanDefinitionParser("initialize-database", new InitializeDatabaseBeanDefinitionParser());
/* 12:   */   }
/* 13:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.config.JdbcNamespaceHandler
 * JD-Core Version:    0.7.0.1
 */