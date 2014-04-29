/*  1:   */ package org.springframework.transaction;
/*  2:   */ 
/*  3:   */ public class NoTransactionException
/*  4:   */   extends TransactionUsageException
/*  5:   */ {
/*  6:   */   public NoTransactionException(String msg)
/*  7:   */   {
/*  8:35 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public NoTransactionException(String msg, Throwable cause)
/* 12:   */   {
/* 13:44 */     super(msg, cause);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.NoTransactionException
 * JD-Core Version:    0.7.0.1
 */