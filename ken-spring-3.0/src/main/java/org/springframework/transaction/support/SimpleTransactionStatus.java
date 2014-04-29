/*  1:   */ package org.springframework.transaction.support;
/*  2:   */ 
/*  3:   */ public class SimpleTransactionStatus
/*  4:   */   extends AbstractTransactionStatus
/*  5:   */ {
/*  6:   */   private final boolean newTransaction;
/*  7:   */   
/*  8:   */   public SimpleTransactionStatus()
/*  9:   */   {
/* 10:48 */     this(true);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public SimpleTransactionStatus(boolean newTransaction)
/* 14:   */   {
/* 15:56 */     this.newTransaction = newTransaction;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public boolean isNewTransaction()
/* 19:   */   {
/* 20:61 */     return this.newTransaction;
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.SimpleTransactionStatus
 * JD-Core Version:    0.7.0.1
 */