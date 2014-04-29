/*  1:   */ package org.springframework.transaction.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*  4:   */ import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
/*  5:   */ import org.springframework.beans.factory.xml.ParserContext;
/*  6:   */ import org.springframework.util.ClassUtils;
/*  7:   */ import org.w3c.dom.Element;
/*  8:   */ 
/*  9:   */ public class JtaTransactionManagerBeanDefinitionParser
/* 10:   */   extends AbstractSingleBeanDefinitionParser
/* 11:   */ {
/* 12:   */   private static final String WEBLOGIC_JTA_TRANSACTION_MANAGER_CLASS_NAME = "org.springframework.transaction.jta.WebLogicJtaTransactionManager";
/* 13:   */   private static final String WEBSPHERE_TRANSACTION_MANAGER_CLASS_NAME = "org.springframework.transaction.jta.WebSphereUowTransactionManager";
/* 14:   */   private static final String OC4J_TRANSACTION_MANAGER_CLASS_NAME = "org.springframework.transaction.jta.OC4JJtaTransactionManager";
/* 15:   */   private static final String JTA_TRANSACTION_MANAGER_CLASS_NAME = "org.springframework.transaction.jta.JtaTransactionManager";
/* 16:49 */   private static final boolean weblogicPresent = ClassUtils.isPresent(
/* 17:50 */     "weblogic.transaction.UserTransaction", JtaTransactionManagerBeanDefinitionParser.class.getClassLoader());
/* 18:52 */   private static final boolean webspherePresent = ClassUtils.isPresent(
/* 19:53 */     "com.ibm.wsspi.uow.UOWManager", JtaTransactionManagerBeanDefinitionParser.class.getClassLoader());
/* 20:55 */   private static final boolean oc4jPresent = ClassUtils.isPresent(
/* 21:56 */     "oracle.j2ee.transaction.OC4JTransactionManager", JtaTransactionManagerBeanDefinitionParser.class.getClassLoader());
/* 22:   */   
/* 23:   */   protected String getBeanClassName(Element element)
/* 24:   */   {
/* 25:61 */     if (weblogicPresent) {
/* 26:62 */       return "org.springframework.transaction.jta.WebLogicJtaTransactionManager";
/* 27:   */     }
/* 28:64 */     if (webspherePresent) {
/* 29:65 */       return "org.springframework.transaction.jta.WebSphereUowTransactionManager";
/* 30:   */     }
/* 31:67 */     if (oc4jPresent) {
/* 32:68 */       return "org.springframework.transaction.jta.OC4JJtaTransactionManager";
/* 33:   */     }
/* 34:71 */     return "org.springframework.transaction.jta.JtaTransactionManager";
/* 35:   */   }
/* 36:   */   
/* 37:   */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
/* 38:   */   {
/* 39:77 */     return "transactionManager";
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.config.JtaTransactionManagerBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */