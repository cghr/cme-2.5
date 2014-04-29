/*   1:    */ package org.springframework.transaction.support;
/*   2:    */ 
/*   3:    */ import java.util.Date;
/*   4:    */ import org.springframework.transaction.TransactionTimedOutException;
/*   5:    */ 
/*   6:    */ public abstract class ResourceHolderSupport
/*   7:    */   implements ResourceHolder
/*   8:    */ {
/*   9: 37 */   private boolean synchronizedWithTransaction = false;
/*  10: 39 */   private boolean rollbackOnly = false;
/*  11:    */   private Date deadline;
/*  12: 43 */   private int referenceCount = 0;
/*  13: 45 */   private boolean isVoid = false;
/*  14:    */   
/*  15:    */   public void setSynchronizedWithTransaction(boolean synchronizedWithTransaction)
/*  16:    */   {
/*  17: 52 */     this.synchronizedWithTransaction = synchronizedWithTransaction;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public boolean isSynchronizedWithTransaction()
/*  21:    */   {
/*  22: 59 */     return this.synchronizedWithTransaction;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setRollbackOnly()
/*  26:    */   {
/*  27: 66 */     this.rollbackOnly = true;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public boolean isRollbackOnly()
/*  31:    */   {
/*  32: 73 */     return this.rollbackOnly;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setTimeoutInSeconds(int seconds)
/*  36:    */   {
/*  37: 81 */     setTimeoutInMillis(seconds * 1000);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setTimeoutInMillis(long millis)
/*  41:    */   {
/*  42: 89 */     this.deadline = new Date(System.currentTimeMillis() + millis);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public boolean hasTimeout()
/*  46:    */   {
/*  47: 96 */     return this.deadline != null;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public Date getDeadline()
/*  51:    */   {
/*  52:104 */     return this.deadline;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public int getTimeToLiveInSeconds()
/*  56:    */   {
/*  57:114 */     double diff = getTimeToLiveInMillis() / 1000.0D;
/*  58:115 */     int secs = (int)Math.ceil(diff);
/*  59:116 */     checkTransactionTimeout(secs <= 0);
/*  60:117 */     return secs;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public long getTimeToLiveInMillis()
/*  64:    */     throws TransactionTimedOutException
/*  65:    */   {
/*  66:126 */     if (this.deadline == null) {
/*  67:127 */       throw new IllegalStateException("No timeout specified for this resource holder");
/*  68:    */     }
/*  69:129 */     long timeToLive = this.deadline.getTime() - System.currentTimeMillis();
/*  70:130 */     checkTransactionTimeout(timeToLive <= 0L);
/*  71:131 */     return timeToLive;
/*  72:    */   }
/*  73:    */   
/*  74:    */   private void checkTransactionTimeout(boolean deadlineReached)
/*  75:    */     throws TransactionTimedOutException
/*  76:    */   {
/*  77:139 */     if (deadlineReached)
/*  78:    */     {
/*  79:140 */       setRollbackOnly();
/*  80:141 */       throw new TransactionTimedOutException("Transaction timed out: deadline was " + this.deadline);
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void requested()
/*  85:    */   {
/*  86:150 */     this.referenceCount += 1;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void released()
/*  90:    */   {
/*  91:158 */     this.referenceCount -= 1;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public boolean isOpen()
/*  95:    */   {
/*  96:165 */     return this.referenceCount > 0;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void clear()
/* 100:    */   {
/* 101:172 */     this.synchronizedWithTransaction = false;
/* 102:173 */     this.rollbackOnly = false;
/* 103:174 */     this.deadline = null;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void reset()
/* 107:    */   {
/* 108:181 */     clear();
/* 109:182 */     this.referenceCount = 0;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void unbound()
/* 113:    */   {
/* 114:186 */     this.isVoid = true;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public boolean isVoid()
/* 118:    */   {
/* 119:190 */     return this.isVoid;
/* 120:    */   }
/* 121:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.ResourceHolderSupport
 * JD-Core Version:    0.7.0.1
 */