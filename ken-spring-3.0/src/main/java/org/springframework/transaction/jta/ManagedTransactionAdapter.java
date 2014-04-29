/*  1:   */ package org.springframework.transaction.jta;
/*  2:   */ 
/*  3:   */ import javax.transaction.HeuristicMixedException;
/*  4:   */ import javax.transaction.HeuristicRollbackException;
/*  5:   */ import javax.transaction.RollbackException;
/*  6:   */ import javax.transaction.Synchronization;
/*  7:   */ import javax.transaction.SystemException;
/*  8:   */ import javax.transaction.Transaction;
/*  9:   */ import javax.transaction.TransactionManager;
/* 10:   */ import javax.transaction.xa.XAResource;
/* 11:   */ import org.springframework.util.Assert;
/* 12:   */ 
/* 13:   */ public class ManagedTransactionAdapter
/* 14:   */   implements Transaction
/* 15:   */ {
/* 16:   */   private final TransactionManager transactionManager;
/* 17:   */   
/* 18:   */   public ManagedTransactionAdapter(TransactionManager transactionManager)
/* 19:   */     throws SystemException
/* 20:   */   {
/* 21:48 */     Assert.notNull(transactionManager, "TransactionManager must not be null");
/* 22:49 */     this.transactionManager = transactionManager;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public final TransactionManager getTransactionManager()
/* 26:   */   {
/* 27:56 */     return this.transactionManager;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void commit()
/* 31:   */     throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, SystemException
/* 32:   */   {
/* 33:62 */     this.transactionManager.commit();
/* 34:   */   }
/* 35:   */   
/* 36:   */   public void rollback()
/* 37:   */     throws SystemException
/* 38:   */   {
/* 39:66 */     this.transactionManager.rollback();
/* 40:   */   }
/* 41:   */   
/* 42:   */   public void setRollbackOnly()
/* 43:   */     throws SystemException
/* 44:   */   {
/* 45:70 */     this.transactionManager.setRollbackOnly();
/* 46:   */   }
/* 47:   */   
/* 48:   */   public int getStatus()
/* 49:   */     throws SystemException
/* 50:   */   {
/* 51:74 */     return this.transactionManager.getStatus();
/* 52:   */   }
/* 53:   */   
/* 54:   */   public boolean enlistResource(XAResource xaRes)
/* 55:   */     throws RollbackException, SystemException
/* 56:   */   {
/* 57:78 */     return this.transactionManager.getTransaction().enlistResource(xaRes);
/* 58:   */   }
/* 59:   */   
/* 60:   */   public boolean delistResource(XAResource xaRes, int flag)
/* 61:   */     throws SystemException
/* 62:   */   {
/* 63:82 */     return this.transactionManager.getTransaction().delistResource(xaRes, flag);
/* 64:   */   }
/* 65:   */   
/* 66:   */   public void registerSynchronization(Synchronization sync)
/* 67:   */     throws RollbackException, SystemException
/* 68:   */   {
/* 69:86 */     this.transactionManager.getTransaction().registerSynchronization(sync);
/* 70:   */   }
/* 71:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.jta.ManagedTransactionAdapter
 * JD-Core Version:    0.7.0.1
 */