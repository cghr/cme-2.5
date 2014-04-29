/*  1:   */ package org.springframework.transaction.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
/*  4:   */ import org.w3c.dom.Element;
/*  5:   */ 
/*  6:   */ public class TxNamespaceHandler
/*  7:   */   extends NamespaceHandlerSupport
/*  8:   */ {
/*  9:   */   static final String TRANSACTION_MANAGER_ATTRIBUTE = "transaction-manager";
/* 10:   */   static final String DEFAULT_TRANSACTION_MANAGER_BEAN_NAME = "transactionManager";
/* 11:   */   
/* 12:   */   static String getTransactionManagerName(Element element)
/* 13:   */   {
/* 14:48 */     return element.hasAttribute("transaction-manager") ? 
/* 15:49 */       element.getAttribute("transaction-manager") : "transactionManager";
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void init()
/* 19:   */   {
/* 20:54 */     registerBeanDefinitionParser("advice", new TxAdviceBeanDefinitionParser());
/* 21:55 */     registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
/* 22:56 */     registerBeanDefinitionParser("jta-transaction-manager", new JtaTransactionManagerBeanDefinitionParser());
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.config.TxNamespaceHandler
 * JD-Core Version:    0.7.0.1
 */