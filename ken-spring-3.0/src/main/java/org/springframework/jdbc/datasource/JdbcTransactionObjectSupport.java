/*   1:    */ package org.springframework.jdbc.datasource;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.sql.Savepoint;
/*   5:    */ import org.apache.commons.logging.Log;
/*   6:    */ import org.apache.commons.logging.LogFactory;
/*   7:    */ import org.springframework.transaction.CannotCreateTransactionException;
/*   8:    */ import org.springframework.transaction.NestedTransactionNotSupportedException;
/*   9:    */ import org.springframework.transaction.SavepointManager;
/*  10:    */ import org.springframework.transaction.TransactionException;
/*  11:    */ import org.springframework.transaction.TransactionSystemException;
/*  12:    */ import org.springframework.transaction.TransactionUsageException;
/*  13:    */ import org.springframework.transaction.support.SmartTransactionObject;
/*  14:    */ 
/*  15:    */ public abstract class JdbcTransactionObjectSupport
/*  16:    */   implements SavepointManager, SmartTransactionObject
/*  17:    */ {
/*  18: 52 */   private static final Log logger = LogFactory.getLog(JdbcTransactionObjectSupport.class);
/*  19:    */   private ConnectionHolder connectionHolder;
/*  20:    */   private Integer previousIsolationLevel;
/*  21: 59 */   private boolean savepointAllowed = false;
/*  22:    */   
/*  23:    */   public void setConnectionHolder(ConnectionHolder connectionHolder)
/*  24:    */   {
/*  25: 63 */     this.connectionHolder = connectionHolder;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public ConnectionHolder getConnectionHolder()
/*  29:    */   {
/*  30: 67 */     return this.connectionHolder;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public boolean hasConnectionHolder()
/*  34:    */   {
/*  35: 71 */     return this.connectionHolder != null;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setPreviousIsolationLevel(Integer previousIsolationLevel)
/*  39:    */   {
/*  40: 75 */     this.previousIsolationLevel = previousIsolationLevel;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public Integer getPreviousIsolationLevel()
/*  44:    */   {
/*  45: 79 */     return this.previousIsolationLevel;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setSavepointAllowed(boolean savepointAllowed)
/*  49:    */   {
/*  50: 83 */     this.savepointAllowed = savepointAllowed;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public boolean isSavepointAllowed()
/*  54:    */   {
/*  55: 87 */     return this.savepointAllowed;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void flush() {}
/*  59:    */   
/*  60:    */   public Object createSavepoint()
/*  61:    */     throws TransactionException
/*  62:    */   {
/*  63:104 */     ConnectionHolder conHolder = getConnectionHolderForSavepoint();
/*  64:    */     try
/*  65:    */     {
/*  66:106 */       if (!conHolder.supportsSavepoints()) {
/*  67:107 */         throw new NestedTransactionNotSupportedException(
/*  68:108 */           "Cannot create a nested transaction because savepoints are not supported by your JDBC driver");
/*  69:    */       }
/*  70:    */     }
/*  71:    */     catch (Throwable ex)
/*  72:    */     {
/*  73:112 */       throw new NestedTransactionNotSupportedException(
/*  74:113 */         "Cannot create a nested transaction because your JDBC driver is not a JDBC 3.0 driver", ex);
/*  75:    */     }
/*  76:    */     try
/*  77:    */     {
/*  78:116 */       return conHolder.createSavepoint();
/*  79:    */     }
/*  80:    */     catch (Throwable ex)
/*  81:    */     {
/*  82:119 */       throw new CannotCreateTransactionException("Could not create JDBC savepoint", ex);
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void rollbackToSavepoint(Object savepoint)
/*  87:    */     throws TransactionException
/*  88:    */   {
/*  89:    */     try
/*  90:    */     {
/*  91:129 */       getConnectionHolderForSavepoint().getConnection().rollback((Savepoint)savepoint);
/*  92:    */     }
/*  93:    */     catch (Throwable ex)
/*  94:    */     {
/*  95:132 */       throw new TransactionSystemException("Could not roll back to JDBC savepoint", ex);
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void releaseSavepoint(Object savepoint)
/* 100:    */     throws TransactionException
/* 101:    */   {
/* 102:    */     try
/* 103:    */     {
/* 104:142 */       getConnectionHolderForSavepoint().getConnection().releaseSavepoint((Savepoint)savepoint);
/* 105:    */     }
/* 106:    */     catch (Throwable ex)
/* 107:    */     {
/* 108:145 */       logger.debug("Could not explicitly release JDBC savepoint", ex);
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   protected ConnectionHolder getConnectionHolderForSavepoint()
/* 113:    */     throws TransactionException
/* 114:    */   {
/* 115:150 */     if (!isSavepointAllowed()) {
/* 116:151 */       throw new NestedTransactionNotSupportedException(
/* 117:152 */         "Transaction manager does not allow nested transactions");
/* 118:    */     }
/* 119:154 */     if (!hasConnectionHolder()) {
/* 120:155 */       throw new TransactionUsageException(
/* 121:156 */         "Cannot create nested transaction if not exposing a JDBC transaction");
/* 122:    */     }
/* 123:158 */     return getConnectionHolder();
/* 124:    */   }
/* 125:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.JdbcTransactionObjectSupport
 * JD-Core Version:    0.7.0.1
 */