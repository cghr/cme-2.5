/*   1:    */ package org.springframework.transaction.support;
/*   2:    */ 
/*   3:    */ import org.springframework.transaction.NestedTransactionNotSupportedException;
/*   4:    */ import org.springframework.transaction.SavepointManager;
/*   5:    */ import org.springframework.transaction.TransactionException;
/*   6:    */ import org.springframework.transaction.TransactionStatus;
/*   7:    */ import org.springframework.transaction.TransactionUsageException;
/*   8:    */ 
/*   9:    */ public abstract class AbstractTransactionStatus
/*  10:    */   implements TransactionStatus
/*  11:    */ {
/*  12: 48 */   private boolean rollbackOnly = false;
/*  13: 50 */   private boolean completed = false;
/*  14:    */   private Object savepoint;
/*  15:    */   
/*  16:    */   public void setRollbackOnly()
/*  17:    */   {
/*  18: 60 */     this.rollbackOnly = true;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public boolean isRollbackOnly()
/*  22:    */   {
/*  23: 71 */     return (isLocalRollbackOnly()) || (isGlobalRollbackOnly());
/*  24:    */   }
/*  25:    */   
/*  26:    */   public boolean isLocalRollbackOnly()
/*  27:    */   {
/*  28: 80 */     return this.rollbackOnly;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public boolean isGlobalRollbackOnly()
/*  32:    */   {
/*  33: 89 */     return false;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void flush() {}
/*  37:    */   
/*  38:    */   public void setCompleted()
/*  39:    */   {
/*  40:102 */     this.completed = true;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public boolean isCompleted()
/*  44:    */   {
/*  45:106 */     return this.completed;
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected void setSavepoint(Object savepoint)
/*  49:    */   {
/*  50:119 */     this.savepoint = savepoint;
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected Object getSavepoint()
/*  54:    */   {
/*  55:126 */     return this.savepoint;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public boolean hasSavepoint()
/*  59:    */   {
/*  60:130 */     return this.savepoint != null;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void createAndHoldSavepoint()
/*  64:    */     throws TransactionException
/*  65:    */   {
/*  66:139 */     setSavepoint(getSavepointManager().createSavepoint());
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void rollbackToHeldSavepoint()
/*  70:    */     throws TransactionException
/*  71:    */   {
/*  72:146 */     if (!hasSavepoint()) {
/*  73:147 */       throw new TransactionUsageException("No savepoint associated with current transaction");
/*  74:    */     }
/*  75:149 */     getSavepointManager().rollbackToSavepoint(getSavepoint());
/*  76:150 */     setSavepoint(null);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void releaseHeldSavepoint()
/*  80:    */     throws TransactionException
/*  81:    */   {
/*  82:157 */     if (!hasSavepoint()) {
/*  83:158 */       throw new TransactionUsageException("No savepoint associated with current transaction");
/*  84:    */     }
/*  85:160 */     getSavepointManager().releaseSavepoint(getSavepoint());
/*  86:161 */     setSavepoint(null);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public Object createSavepoint()
/*  90:    */     throws TransactionException
/*  91:    */   {
/*  92:176 */     return getSavepointManager().createSavepoint();
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void rollbackToSavepoint(Object savepoint)
/*  96:    */     throws TransactionException
/*  97:    */   {
/*  98:187 */     getSavepointManager().rollbackToSavepoint(savepoint);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void releaseSavepoint(Object savepoint)
/* 102:    */     throws TransactionException
/* 103:    */   {
/* 104:197 */     getSavepointManager().releaseSavepoint(savepoint);
/* 105:    */   }
/* 106:    */   
/* 107:    */   protected SavepointManager getSavepointManager()
/* 108:    */   {
/* 109:207 */     throw new NestedTransactionNotSupportedException("This transaction does not support savepoints");
/* 110:    */   }
/* 111:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.AbstractTransactionStatus
 * JD-Core Version:    0.7.0.1
 */