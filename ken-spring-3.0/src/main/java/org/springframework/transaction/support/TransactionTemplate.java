/*   1:    */ package org.springframework.transaction.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.UndeclaredThrowableException;
/*   4:    */ import org.apache.commons.logging.Log;
/*   5:    */ import org.apache.commons.logging.LogFactory;
/*   6:    */ import org.springframework.beans.factory.InitializingBean;
/*   7:    */ import org.springframework.transaction.PlatformTransactionManager;
/*   8:    */ import org.springframework.transaction.TransactionDefinition;
/*   9:    */ import org.springframework.transaction.TransactionException;
/*  10:    */ import org.springframework.transaction.TransactionStatus;
/*  11:    */ import org.springframework.transaction.TransactionSystemException;
/*  12:    */ 
/*  13:    */ public class TransactionTemplate
/*  14:    */   extends DefaultTransactionDefinition
/*  15:    */   implements TransactionOperations, InitializingBean
/*  16:    */ {
/*  17: 66 */   protected final Log logger = LogFactory.getLog(getClass());
/*  18:    */   private PlatformTransactionManager transactionManager;
/*  19:    */   
/*  20:    */   public TransactionTemplate() {}
/*  21:    */   
/*  22:    */   public TransactionTemplate(PlatformTransactionManager transactionManager)
/*  23:    */   {
/*  24: 85 */     this.transactionManager = transactionManager;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public TransactionTemplate(PlatformTransactionManager transactionManager, TransactionDefinition transactionDefinition)
/*  28:    */   {
/*  29: 96 */     super(transactionDefinition);
/*  30: 97 */     this.transactionManager = transactionManager;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setTransactionManager(PlatformTransactionManager transactionManager)
/*  34:    */   {
/*  35:105 */     this.transactionManager = transactionManager;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public PlatformTransactionManager getTransactionManager()
/*  39:    */   {
/*  40:112 */     return this.transactionManager;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void afterPropertiesSet()
/*  44:    */   {
/*  45:116 */     if (this.transactionManager == null) {
/*  46:117 */       throw new IllegalArgumentException("Property 'transactionManager' is required");
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   public <T> T execute(TransactionCallback<T> action)
/*  51:    */     throws TransactionException
/*  52:    */   {
/*  53:123 */     if ((this.transactionManager instanceof CallbackPreferringPlatformTransactionManager)) {
/*  54:124 */       return ((CallbackPreferringPlatformTransactionManager)this.transactionManager).execute(this, action);
/*  55:    */     }
/*  56:127 */     TransactionStatus status = this.transactionManager.getTransaction(this);
/*  57:    */     try
/*  58:    */     {
/*  59:130 */       result = action.doInTransaction(status);
/*  60:    */     }
/*  61:    */     catch (RuntimeException ex)
/*  62:    */     {
/*  63:    */       T result;
/*  64:134 */       rollbackOnException(status, ex);
/*  65:135 */       throw ex;
/*  66:    */     }
/*  67:    */     catch (Error err)
/*  68:    */     {
/*  69:139 */       rollbackOnException(status, err);
/*  70:140 */       throw err;
/*  71:    */     }
/*  72:    */     catch (Exception ex)
/*  73:    */     {
/*  74:144 */       rollbackOnException(status, ex);
/*  75:145 */       throw new UndeclaredThrowableException(ex, "TransactionCallback threw undeclared checked exception");
/*  76:    */     }
/*  77:    */     T result;
/*  78:147 */     this.transactionManager.commit(status);
/*  79:148 */     return result;
/*  80:    */   }
/*  81:    */   
/*  82:    */   private void rollbackOnException(TransactionStatus status, Throwable ex)
/*  83:    */     throws TransactionException
/*  84:    */   {
/*  85:159 */     this.logger.debug("Initiating transaction rollback on application exception", ex);
/*  86:    */     try
/*  87:    */     {
/*  88:161 */       this.transactionManager.rollback(status);
/*  89:    */     }
/*  90:    */     catch (TransactionSystemException ex2)
/*  91:    */     {
/*  92:164 */       this.logger.error("Application exception overridden by rollback exception", ex);
/*  93:165 */       ex2.initApplicationException(ex);
/*  94:166 */       throw ex2;
/*  95:    */     }
/*  96:    */     catch (RuntimeException ex2)
/*  97:    */     {
/*  98:169 */       this.logger.error("Application exception overridden by rollback exception", ex);
/*  99:170 */       throw ex2;
/* 100:    */     }
/* 101:    */     catch (Error err)
/* 102:    */     {
/* 103:173 */       this.logger.error("Application exception overridden by rollback error", ex);
/* 104:174 */       throw err;
/* 105:    */     }
/* 106:    */   }
/* 107:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.TransactionTemplate
 * JD-Core Version:    0.7.0.1
 */