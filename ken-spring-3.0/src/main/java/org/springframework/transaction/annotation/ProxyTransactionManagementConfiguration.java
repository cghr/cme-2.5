/*  1:   */ package org.springframework.transaction.annotation;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import org.springframework.context.annotation.Bean;
/*  5:   */ import org.springframework.context.annotation.Configuration;
/*  6:   */ import org.springframework.context.annotation.Role;
/*  7:   */ import org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor;
/*  8:   */ import org.springframework.transaction.interceptor.TransactionAttributeSource;
/*  9:   */ import org.springframework.transaction.interceptor.TransactionInterceptor;
/* 10:   */ 
/* 11:   */ @Configuration
/* 12:   */ public class ProxyTransactionManagementConfiguration
/* 13:   */   extends AbstractTransactionManagementConfiguration
/* 14:   */ {
/* 15:   */   @Bean(name={"org.springframework.transaction.config.internalTransactionAdvisor"})
/* 16:   */   @Role(2)
/* 17:   */   public BeanFactoryTransactionAttributeSourceAdvisor transactionAdvisor()
/* 18:   */   {
/* 19:43 */     BeanFactoryTransactionAttributeSourceAdvisor advisor = 
/* 20:44 */       new BeanFactoryTransactionAttributeSourceAdvisor();
/* 21:45 */     advisor.setTransactionAttributeSource(transactionAttributeSource());
/* 22:46 */     advisor.setAdvice(transactionInterceptor());
/* 23:47 */     advisor.setOrder(((Integer)this.enableTx.get("order")).intValue());
/* 24:48 */     return advisor;
/* 25:   */   }
/* 26:   */   
/* 27:   */   @Bean
/* 28:   */   @Role(2)
/* 29:   */   public TransactionAttributeSource transactionAttributeSource()
/* 30:   */   {
/* 31:54 */     return new AnnotationTransactionAttributeSource();
/* 32:   */   }
/* 33:   */   
/* 34:   */   @Bean
/* 35:   */   @Role(2)
/* 36:   */   public TransactionInterceptor transactionInterceptor()
/* 37:   */   {
/* 38:60 */     TransactionInterceptor interceptor = new TransactionInterceptor();
/* 39:61 */     interceptor.setTransactionAttributeSource(transactionAttributeSource());
/* 40:62 */     if (this.txManager != null) {
/* 41:63 */       interceptor.setTransactionManager(this.txManager);
/* 42:   */     }
/* 43:65 */     return interceptor;
/* 44:   */   }
/* 45:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration
 * JD-Core Version:    0.7.0.1
 */