/*  1:   */ package org.springframework.transaction;
/*  2:   */ 
/*  3:   */ public class TransactionSuspensionNotSupportedException
/*  4:   */   extends CannotCreateTransactionException
/*  5:   */ {
/*  6:   */   public TransactionSuspensionNotSupportedException(String msg)
/*  7:   */   {
/*  8:33 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public TransactionSuspensionNotSupportedException(String msg, Throwable cause)
/* 12:   */   {
/* 13:42 */     super(msg, cause);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.TransactionSuspensionNotSupportedException
 * JD-Core Version:    0.7.0.1
 */