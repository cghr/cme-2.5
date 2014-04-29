/*  1:   */ package org.springframework.transaction.jta;
/*  2:   */ 
/*  3:   */ import javax.transaction.HeuristicMixedException;
/*  4:   */ import javax.transaction.HeuristicRollbackException;
/*  5:   */ import javax.transaction.NotSupportedException;
/*  6:   */ import javax.transaction.RollbackException;
/*  7:   */ import javax.transaction.SystemException;
/*  8:   */ import javax.transaction.TransactionManager;
/*  9:   */ import javax.transaction.UserTransaction;
/* 10:   */ import org.springframework.util.Assert;
/* 11:   */ 
/* 12:   */ public class UserTransactionAdapter
/* 13:   */   implements UserTransaction
/* 14:   */ {
/* 15:   */   private final TransactionManager transactionManager;
/* 16:   */   
/* 17:   */   public UserTransactionAdapter(TransactionManager transactionManager)
/* 18:   */   {
/* 19:56 */     Assert.notNull(transactionManager, "TransactionManager must not be null");
/* 20:57 */     this.transactionManager = transactionManager;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public final TransactionManager getTransactionManager()
/* 24:   */   {
/* 25:64 */     return this.transactionManager;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void setTransactionTimeout(int timeout)
/* 29:   */     throws SystemException
/* 30:   */   {
/* 31:69 */     this.transactionManager.setTransactionTimeout(timeout);
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void begin()
/* 35:   */     throws NotSupportedException, SystemException
/* 36:   */   {
/* 37:73 */     this.transactionManager.begin();
/* 38:   */   }
/* 39:   */   
/* 40:   */   public void commit()
/* 41:   */     throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, SystemException
/* 42:   */   {
/* 43:79 */     this.transactionManager.commit();
/* 44:   */   }
/* 45:   */   
/* 46:   */   public void rollback()
/* 47:   */     throws SecurityException, SystemException
/* 48:   */   {
/* 49:83 */     this.transactionManager.rollback();
/* 50:   */   }
/* 51:   */   
/* 52:   */   public void setRollbackOnly()
/* 53:   */     throws SystemException
/* 54:   */   {
/* 55:87 */     this.transactionManager.setRollbackOnly();
/* 56:   */   }
/* 57:   */   
/* 58:   */   public int getStatus()
/* 59:   */     throws SystemException
/* 60:   */   {
/* 61:91 */     return this.transactionManager.getStatus();
/* 62:   */   }
/* 63:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.jta.UserTransactionAdapter
 * JD-Core Version:    0.7.0.1
 */