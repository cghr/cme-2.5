/*  1:   */ package org.springframework.transaction.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.Ordered;
/*  4:   */ 
/*  5:   */ public abstract class TransactionSynchronizationAdapter
/*  6:   */   implements TransactionSynchronization, Ordered
/*  7:   */ {
/*  8:   */   public int getOrder()
/*  9:   */   {
/* 10:36 */     return 2147483647;
/* 11:   */   }
/* 12:   */   
/* 13:   */   public void suspend() {}
/* 14:   */   
/* 15:   */   public void resume() {}
/* 16:   */   
/* 17:   */   public void flush() {}
/* 18:   */   
/* 19:   */   public void beforeCommit(boolean readOnly) {}
/* 20:   */   
/* 21:   */   public void beforeCompletion() {}
/* 22:   */   
/* 23:   */   public void afterCommit() {}
/* 24:   */   
/* 25:   */   public void afterCompletion(int status) {}
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.TransactionSynchronizationAdapter
 * JD-Core Version:    0.7.0.1
 */