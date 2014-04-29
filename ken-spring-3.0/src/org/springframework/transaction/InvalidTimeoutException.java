/*  1:   */ package org.springframework.transaction;
/*  2:   */ 
/*  3:   */ public class InvalidTimeoutException
/*  4:   */   extends TransactionUsageException
/*  5:   */ {
/*  6:   */   private int timeout;
/*  7:   */   
/*  8:   */   public InvalidTimeoutException(String msg, int timeout)
/*  9:   */   {
/* 10:38 */     super(msg);
/* 11:39 */     this.timeout = timeout;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public int getTimeout()
/* 15:   */   {
/* 16:46 */     return this.timeout;
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.InvalidTimeoutException
 * JD-Core Version:    0.7.0.1
 */