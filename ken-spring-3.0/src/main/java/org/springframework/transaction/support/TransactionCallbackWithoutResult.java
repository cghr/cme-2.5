/*  1:   */ package org.springframework.transaction.support;
/*  2:   */ 
/*  3:   */ import org.springframework.transaction.TransactionStatus;
/*  4:   */ 
/*  5:   */ public abstract class TransactionCallbackWithoutResult
/*  6:   */   implements TransactionCallback<Object>
/*  7:   */ {
/*  8:   */   public final Object doInTransaction(TransactionStatus status)
/*  9:   */   {
/* 10:33 */     doInTransactionWithoutResult(status);
/* 11:34 */     return null;
/* 12:   */   }
/* 13:   */   
/* 14:   */   protected abstract void doInTransactionWithoutResult(TransactionStatus paramTransactionStatus);
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.TransactionCallbackWithoutResult
 * JD-Core Version:    0.7.0.1
 */