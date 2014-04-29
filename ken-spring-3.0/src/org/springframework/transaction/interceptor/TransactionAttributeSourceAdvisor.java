/*  1:   */ package org.springframework.transaction.interceptor;
/*  2:   */ 
/*  3:   */ import org.aopalliance.aop.Advice;
/*  4:   */ import org.springframework.aop.ClassFilter;
/*  5:   */ import org.springframework.aop.Pointcut;
/*  6:   */ import org.springframework.aop.support.AbstractPointcutAdvisor;
/*  7:   */ 
/*  8:   */ public class TransactionAttributeSourceAdvisor
/*  9:   */   extends AbstractPointcutAdvisor
/* 10:   */ {
/* 11:   */   private TransactionInterceptor transactionInterceptor;
/* 12:42 */   private final TransactionAttributeSourcePointcut pointcut = new TransactionAttributeSourcePointcut()
/* 13:   */   {
/* 14:   */     protected TransactionAttributeSource getTransactionAttributeSource()
/* 15:   */     {
/* 16:45 */       return TransactionAttributeSourceAdvisor.this.transactionInterceptor != null ? TransactionAttributeSourceAdvisor.this.transactionInterceptor.getTransactionAttributeSource() : null;
/* 17:   */     }
/* 18:   */   };
/* 19:   */   
/* 20:   */   public TransactionAttributeSourceAdvisor() {}
/* 21:   */   
/* 22:   */   public TransactionAttributeSourceAdvisor(TransactionInterceptor interceptor)
/* 23:   */   {
/* 24:61 */     setTransactionInterceptor(interceptor);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void setTransactionInterceptor(TransactionInterceptor interceptor)
/* 28:   */   {
/* 29:69 */     this.transactionInterceptor = interceptor;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void setClassFilter(ClassFilter classFilter)
/* 33:   */   {
/* 34:77 */     this.pointcut.setClassFilter(classFilter);
/* 35:   */   }
/* 36:   */   
/* 37:   */   public Advice getAdvice()
/* 38:   */   {
/* 39:82 */     return this.transactionInterceptor;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public Pointcut getPointcut()
/* 43:   */   {
/* 44:86 */     return this.pointcut;
/* 45:   */   }
/* 46:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.TransactionAttributeSourceAdvisor
 * JD-Core Version:    0.7.0.1
 */