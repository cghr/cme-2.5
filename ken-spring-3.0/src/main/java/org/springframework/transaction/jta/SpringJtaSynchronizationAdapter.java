/*   1:    */ package org.springframework.transaction.jta;
/*   2:    */ 
/*   3:    */ import javax.transaction.Synchronization;
/*   4:    */ import javax.transaction.TransactionManager;
/*   5:    */ import javax.transaction.UserTransaction;
/*   6:    */ import org.apache.commons.logging.Log;
/*   7:    */ import org.apache.commons.logging.LogFactory;
/*   8:    */ import org.springframework.transaction.support.TransactionSynchronization;
/*   9:    */ import org.springframework.transaction.support.TransactionSynchronizationManager;
/*  10:    */ import org.springframework.util.Assert;
/*  11:    */ 
/*  12:    */ public class SpringJtaSynchronizationAdapter
/*  13:    */   implements Synchronization
/*  14:    */ {
/*  15: 47 */   protected static final Log logger = LogFactory.getLog(SpringJtaSynchronizationAdapter.class);
/*  16:    */   private final TransactionSynchronization springSynchronization;
/*  17:    */   private UserTransaction jtaTransaction;
/*  18: 53 */   private boolean beforeCompletionCalled = false;
/*  19:    */   
/*  20:    */   public SpringJtaSynchronizationAdapter(TransactionSynchronization springSynchronization)
/*  21:    */   {
/*  22: 62 */     Assert.notNull(springSynchronization, "TransactionSynchronization must not be null");
/*  23: 63 */     this.springSynchronization = springSynchronization;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public SpringJtaSynchronizationAdapter(TransactionSynchronization springSynchronization, UserTransaction jtaUserTransaction)
/*  27:    */   {
/*  28: 82 */     this(springSynchronization);
/*  29: 83 */     if ((jtaUserTransaction != null) && (!jtaUserTransaction.getClass().getName().startsWith("weblogic."))) {
/*  30: 84 */       this.jtaTransaction = jtaUserTransaction;
/*  31:    */     }
/*  32:    */   }
/*  33:    */   
/*  34:    */   public SpringJtaSynchronizationAdapter(TransactionSynchronization springSynchronization, TransactionManager jtaTransactionManager)
/*  35:    */   {
/*  36:104 */     this(springSynchronization);
/*  37:105 */     if ((jtaTransactionManager != null) && (!jtaTransactionManager.getClass().getName().startsWith("weblogic."))) {
/*  38:106 */       this.jtaTransaction = new UserTransactionAdapter(jtaTransactionManager);
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void beforeCompletion()
/*  43:    */   {
/*  44:    */     try
/*  45:    */     {
/*  46:118 */       boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
/*  47:119 */       this.springSynchronization.beforeCommit(readOnly);
/*  48:    */     }
/*  49:    */     catch (RuntimeException ex)
/*  50:    */     {
/*  51:122 */       setRollbackOnlyIfPossible();
/*  52:123 */       throw ex;
/*  53:    */     }
/*  54:    */     catch (Error err)
/*  55:    */     {
/*  56:126 */       setRollbackOnlyIfPossible();
/*  57:127 */       throw err;
/*  58:    */     }
/*  59:    */     finally
/*  60:    */     {
/*  61:133 */       this.beforeCompletionCalled = true;
/*  62:134 */       this.springSynchronization.beforeCompletion();
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   private void setRollbackOnlyIfPossible()
/*  67:    */   {
/*  68:142 */     if (this.jtaTransaction != null) {
/*  69:    */       try
/*  70:    */       {
/*  71:144 */         this.jtaTransaction.setRollbackOnly();
/*  72:    */       }
/*  73:    */       catch (UnsupportedOperationException ex)
/*  74:    */       {
/*  75:148 */         logger.debug("JTA transaction handle does not support setRollbackOnly method - relying on JTA provider to mark the transaction as rollback-only based on the exception thrown from beforeCompletion", 
/*  76:    */         
/*  77:150 */           ex);
/*  78:    */       }
/*  79:    */       catch (Throwable ex)
/*  80:    */       {
/*  81:153 */         logger.error("Could not set JTA transaction rollback-only", ex);
/*  82:    */       }
/*  83:    */     } else {
/*  84:157 */       logger.debug("No JTA transaction handle available and/or running on WebLogic - relying on JTA provider to mark the transaction as rollback-only based on the exception thrown from beforeCompletion");
/*  85:    */     }
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void afterCompletion(int status)
/*  89:    */   {
/*  90:172 */     if (!this.beforeCompletionCalled) {
/*  91:175 */       this.springSynchronization.beforeCompletion();
/*  92:    */     }
/*  93:178 */     switch (status)
/*  94:    */     {
/*  95:    */     case 3: 
/*  96:180 */       this.springSynchronization.afterCompletion(0);
/*  97:181 */       break;
/*  98:    */     case 4: 
/*  99:183 */       this.springSynchronization.afterCompletion(1);
/* 100:184 */       break;
/* 101:    */     default: 
/* 102:186 */       this.springSynchronization.afterCompletion(2);
/* 103:    */     }
/* 104:    */   }
/* 105:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.jta.SpringJtaSynchronizationAdapter
 * JD-Core Version:    0.7.0.1
 */