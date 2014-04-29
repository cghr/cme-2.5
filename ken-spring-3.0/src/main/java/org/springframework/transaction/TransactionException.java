/*  1:   */ package org.springframework.transaction;
/*  2:   */ 
/*  3:   */ import org.springframework.core.NestedRuntimeException;
/*  4:   */ 
/*  5:   */ public abstract class TransactionException
/*  6:   */   extends NestedRuntimeException
/*  7:   */ {
/*  8:   */   public TransactionException(String msg)
/*  9:   */   {
/* 10:34 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public TransactionException(String msg, Throwable cause)
/* 14:   */   {
/* 15:43 */     super(msg, cause);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.TransactionException
 * JD-Core Version:    0.7.0.1
 */