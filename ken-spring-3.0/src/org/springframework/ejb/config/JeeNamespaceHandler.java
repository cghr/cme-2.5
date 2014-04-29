/*  1:   */ package org.springframework.ejb.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
/*  4:   */ 
/*  5:   */ public class JeeNamespaceHandler
/*  6:   */   extends NamespaceHandlerSupport
/*  7:   */ {
/*  8:   */   public void init()
/*  9:   */   {
/* 10:31 */     registerBeanDefinitionParser("jndi-lookup", new JndiLookupBeanDefinitionParser());
/* 11:32 */     registerBeanDefinitionParser("local-slsb", new LocalStatelessSessionBeanDefinitionParser());
/* 12:33 */     registerBeanDefinitionParser("remote-slsb", new RemoteStatelessSessionBeanDefinitionParser());
/* 13:   */   }
/* 14:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ejb.config.JeeNamespaceHandler
 * JD-Core Version:    0.7.0.1
 */