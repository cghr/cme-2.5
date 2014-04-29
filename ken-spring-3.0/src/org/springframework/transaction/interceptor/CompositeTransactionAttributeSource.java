/*  1:   */ package org.springframework.transaction.interceptor;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import java.lang.reflect.Method;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ 
/*  7:   */ public class CompositeTransactionAttributeSource
/*  8:   */   implements TransactionAttributeSource, Serializable
/*  9:   */ {
/* 10:   */   private final TransactionAttributeSource[] transactionAttributeSources;
/* 11:   */   
/* 12:   */   public CompositeTransactionAttributeSource(TransactionAttributeSource[] transactionAttributeSources)
/* 13:   */   {
/* 14:41 */     Assert.notNull(transactionAttributeSources, "TransactionAttributeSource array must not be null");
/* 15:42 */     this.transactionAttributeSources = transactionAttributeSources;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public final TransactionAttributeSource[] getTransactionAttributeSources()
/* 19:   */   {
/* 20:50 */     return this.transactionAttributeSources;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass)
/* 24:   */   {
/* 25:55 */     for (TransactionAttributeSource tas : this.transactionAttributeSources)
/* 26:   */     {
/* 27:56 */       TransactionAttribute ta = tas.getTransactionAttribute(method, targetClass);
/* 28:57 */       if (ta != null) {
/* 29:58 */         return ta;
/* 30:   */       }
/* 31:   */     }
/* 32:61 */     return null;
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.CompositeTransactionAttributeSource
 * JD-Core Version:    0.7.0.1
 */