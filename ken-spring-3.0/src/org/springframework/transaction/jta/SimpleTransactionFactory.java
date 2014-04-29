/*  1:   */ package org.springframework.transaction.jta;
/*  2:   */ 
/*  3:   */ import javax.transaction.NotSupportedException;
/*  4:   */ import javax.transaction.SystemException;
/*  5:   */ import javax.transaction.Transaction;
/*  6:   */ import javax.transaction.TransactionManager;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ 
/*  9:   */ public class SimpleTransactionFactory
/* 10:   */   implements TransactionFactory
/* 11:   */ {
/* 12:   */   private final TransactionManager transactionManager;
/* 13:   */   
/* 14:   */   public SimpleTransactionFactory(TransactionManager transactionManager)
/* 15:   */   {
/* 16:48 */     Assert.notNull(transactionManager, "TransactionManager must not be null");
/* 17:49 */     this.transactionManager = transactionManager;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public Transaction createTransaction(String name, int timeout)
/* 21:   */     throws NotSupportedException, SystemException
/* 22:   */   {
/* 23:54 */     if (timeout >= 0) {
/* 24:55 */       this.transactionManager.setTransactionTimeout(timeout);
/* 25:   */     }
/* 26:57 */     this.transactionManager.begin();
/* 27:58 */     return new ManagedTransactionAdapter(this.transactionManager);
/* 28:   */   }
/* 29:   */   
/* 30:   */   public boolean supportsResourceAdapterManagedTransactions()
/* 31:   */   {
/* 32:62 */     return false;
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.jta.SimpleTransactionFactory
 * JD-Core Version:    0.7.0.1
 */