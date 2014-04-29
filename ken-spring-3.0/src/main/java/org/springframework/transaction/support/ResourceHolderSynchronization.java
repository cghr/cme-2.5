/*   1:    */ package org.springframework.transaction.support;
/*   2:    */ 
/*   3:    */ public abstract class ResourceHolderSynchronization<H extends ResourceHolder, K>
/*   4:    */   implements TransactionSynchronization
/*   5:    */ {
/*   6:    */   private final H resourceHolder;
/*   7:    */   private final K resourceKey;
/*   8: 33 */   private volatile boolean holderActive = true;
/*   9:    */   
/*  10:    */   public ResourceHolderSynchronization(H resourceHolder, K resourceKey)
/*  11:    */   {
/*  12: 43 */     this.resourceHolder = resourceHolder;
/*  13: 44 */     this.resourceKey = resourceKey;
/*  14:    */   }
/*  15:    */   
/*  16:    */   public void suspend()
/*  17:    */   {
/*  18: 49 */     if (this.holderActive) {
/*  19: 50 */       TransactionSynchronizationManager.unbindResource(this.resourceKey);
/*  20:    */     }
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void resume()
/*  24:    */   {
/*  25: 55 */     if (this.holderActive) {
/*  26: 56 */       TransactionSynchronizationManager.bindResource(this.resourceKey, this.resourceHolder);
/*  27:    */     }
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void flush()
/*  31:    */   {
/*  32: 61 */     flushResource(this.resourceHolder);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void beforeCommit(boolean readOnly) {}
/*  36:    */   
/*  37:    */   public void beforeCompletion()
/*  38:    */   {
/*  39: 68 */     if (shouldUnbindAtCompletion())
/*  40:    */     {
/*  41: 69 */       TransactionSynchronizationManager.unbindResource(this.resourceKey);
/*  42: 70 */       this.holderActive = false;
/*  43: 71 */       if (shouldReleaseBeforeCompletion()) {
/*  44: 72 */         releaseResource(this.resourceHolder, this.resourceKey);
/*  45:    */       }
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void afterCommit()
/*  50:    */   {
/*  51: 78 */     if (!shouldReleaseBeforeCompletion()) {
/*  52: 79 */       processResourceAfterCommit(this.resourceHolder);
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void afterCompletion(int status)
/*  57:    */   {
/*  58: 84 */     if (shouldUnbindAtCompletion())
/*  59:    */     {
/*  60: 85 */       boolean releaseNecessary = false;
/*  61: 86 */       if (this.holderActive)
/*  62:    */       {
/*  63: 89 */         this.holderActive = false;
/*  64: 90 */         TransactionSynchronizationManager.unbindResourceIfPossible(this.resourceKey);
/*  65: 91 */         this.resourceHolder.unbound();
/*  66: 92 */         releaseNecessary = true;
/*  67:    */       }
/*  68:    */       else
/*  69:    */       {
/*  70: 95 */         releaseNecessary = !shouldReleaseBeforeCompletion();
/*  71:    */       }
/*  72: 97 */       if (releaseNecessary) {
/*  73: 98 */         releaseResource(this.resourceHolder, this.resourceKey);
/*  74:    */       }
/*  75:    */     }
/*  76:    */     else
/*  77:    */     {
/*  78:103 */       cleanupResource(this.resourceHolder, this.resourceKey, status == 0);
/*  79:    */     }
/*  80:105 */     this.resourceHolder.reset();
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected boolean shouldUnbindAtCompletion()
/*  84:    */   {
/*  85:115 */     return true;
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected boolean shouldReleaseBeforeCompletion()
/*  89:    */   {
/*  90:128 */     return true;
/*  91:    */   }
/*  92:    */   
/*  93:    */   protected void flushResource(H resourceHolder) {}
/*  94:    */   
/*  95:    */   protected void processResourceAfterCommit(H resourceHolder) {}
/*  96:    */   
/*  97:    */   protected void releaseResource(H resourceHolder, K resourceKey) {}
/*  98:    */   
/*  99:    */   protected void cleanupResource(H resourceHolder, K resourceKey, boolean committed) {}
/* 100:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.ResourceHolderSynchronization
 * JD-Core Version:    0.7.0.1
 */