/*   1:    */ package org.springframework.transaction.support;
/*   2:    */ 
/*   3:    */ import org.springframework.transaction.NestedTransactionNotSupportedException;
/*   4:    */ import org.springframework.transaction.SavepointManager;
/*   5:    */ 
/*   6:    */ public class DefaultTransactionStatus
/*   7:    */   extends AbstractTransactionStatus
/*   8:    */ {
/*   9:    */   private final Object transaction;
/*  10:    */   private final boolean newTransaction;
/*  11:    */   private final boolean newSynchronization;
/*  12:    */   private final boolean readOnly;
/*  13:    */   private final boolean debug;
/*  14:    */   private final Object suspendedResources;
/*  15:    */   
/*  16:    */   public DefaultTransactionStatus(Object transaction, boolean newTransaction, boolean newSynchronization, boolean readOnly, boolean debug, Object suspendedResources)
/*  17:    */   {
/*  18: 83 */     this.transaction = transaction;
/*  19: 84 */     this.newTransaction = newTransaction;
/*  20: 85 */     this.newSynchronization = newSynchronization;
/*  21: 86 */     this.readOnly = readOnly;
/*  22: 87 */     this.debug = debug;
/*  23: 88 */     this.suspendedResources = suspendedResources;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public Object getTransaction()
/*  27:    */   {
/*  28: 96 */     return this.transaction;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public boolean hasTransaction()
/*  32:    */   {
/*  33:103 */     return this.transaction != null;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public boolean isNewTransaction()
/*  37:    */   {
/*  38:107 */     return (hasTransaction()) && (this.newTransaction);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public boolean isNewSynchronization()
/*  42:    */   {
/*  43:115 */     return this.newSynchronization;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public boolean isReadOnly()
/*  47:    */   {
/*  48:122 */     return this.readOnly;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public boolean isDebug()
/*  52:    */   {
/*  53:131 */     return this.debug;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public Object getSuspendedResources()
/*  57:    */   {
/*  58:139 */     return this.suspendedResources;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public boolean isGlobalRollbackOnly()
/*  62:    */   {
/*  63:157 */     return ((this.transaction instanceof SmartTransactionObject)) && (((SmartTransactionObject)this.transaction).isRollbackOnly());
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void flush()
/*  67:    */   {
/*  68:166 */     if ((this.transaction instanceof SmartTransactionObject)) {
/*  69:167 */       ((SmartTransactionObject)this.transaction).flush();
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   protected SavepointManager getSavepointManager()
/*  74:    */   {
/*  75:177 */     if (!isTransactionSavepointManager()) {
/*  76:178 */       throw new NestedTransactionNotSupportedException(
/*  77:179 */         "Transaction object [" + getTransaction() + "] does not support savepoints");
/*  78:    */     }
/*  79:181 */     return (SavepointManager)getTransaction();
/*  80:    */   }
/*  81:    */   
/*  82:    */   public boolean isTransactionSavepointManager()
/*  83:    */   {
/*  84:191 */     return getTransaction() instanceof SavepointManager;
/*  85:    */   }
/*  86:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.DefaultTransactionStatus
 * JD-Core Version:    0.7.0.1
 */