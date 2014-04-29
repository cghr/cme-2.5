/*  1:   */ package org.springframework.transaction.jta;
/*  2:   */ 
/*  3:   */ import javax.transaction.SystemException;
/*  4:   */ import javax.transaction.UserTransaction;
/*  5:   */ import org.springframework.transaction.TransactionSystemException;
/*  6:   */ import org.springframework.transaction.support.SmartTransactionObject;
/*  7:   */ import org.springframework.transaction.support.TransactionSynchronizationUtils;
/*  8:   */ 
/*  9:   */ public class JtaTransactionObject
/* 10:   */   implements SmartTransactionObject
/* 11:   */ {
/* 12:   */   private final UserTransaction userTransaction;
/* 13:   */   
/* 14:   */   public JtaTransactionObject(UserTransaction userTransaction)
/* 15:   */   {
/* 16:49 */     this.userTransaction = userTransaction;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public final UserTransaction getUserTransaction()
/* 20:   */   {
/* 21:56 */     return this.userTransaction;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public boolean isRollbackOnly()
/* 25:   */   {
/* 26:64 */     if (this.userTransaction == null) {
/* 27:65 */       return false;
/* 28:   */     }
/* 29:   */     try
/* 30:   */     {
/* 31:68 */       int jtaStatus = this.userTransaction.getStatus();
/* 32:69 */       return (jtaStatus == 1) || (jtaStatus == 4);
/* 33:   */     }
/* 34:   */     catch (SystemException ex)
/* 35:   */     {
/* 36:72 */       throw new TransactionSystemException("JTA failure on getStatus", ex);
/* 37:   */     }
/* 38:   */   }
/* 39:   */   
/* 40:   */   public void flush() {}
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.jta.JtaTransactionObject
 * JD-Core Version:    0.7.0.1
 */