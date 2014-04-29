/*  1:   */ package org.springframework.transaction.interceptor;
/*  2:   */ 
/*  3:   */ import org.springframework.aop.ClassFilter;
/*  4:   */ import org.springframework.aop.Pointcut;
/*  5:   */ import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
/*  6:   */ 
/*  7:   */ public class BeanFactoryTransactionAttributeSourceAdvisor
/*  8:   */   extends AbstractBeanFactoryPointcutAdvisor
/*  9:   */ {
/* 10:   */   private TransactionAttributeSource transactionAttributeSource;
/* 11:37 */   private final TransactionAttributeSourcePointcut pointcut = new TransactionAttributeSourcePointcut()
/* 12:   */   {
/* 13:   */     protected TransactionAttributeSource getTransactionAttributeSource()
/* 14:   */     {
/* 15:40 */       return BeanFactoryTransactionAttributeSourceAdvisor.this.transactionAttributeSource;
/* 16:   */     }
/* 17:   */   };
/* 18:   */   
/* 19:   */   public void setTransactionAttributeSource(TransactionAttributeSource transactionAttributeSource)
/* 20:   */   {
/* 21:52 */     this.transactionAttributeSource = transactionAttributeSource;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void setClassFilter(ClassFilter classFilter)
/* 25:   */   {
/* 26:60 */     this.pointcut.setClassFilter(classFilter);
/* 27:   */   }
/* 28:   */   
/* 29:   */   public Pointcut getPointcut()
/* 30:   */   {
/* 31:64 */     return this.pointcut;
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor
 * JD-Core Version:    0.7.0.1
 */