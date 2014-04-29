/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.ObjectInputStream;
/*   5:    */ import java.io.Serializable;
/*   6:    */ import org.apache.commons.logging.Log;
/*   7:    */ import org.apache.commons.logging.LogFactory;
/*   8:    */ 
/*   9:    */ public abstract class ConcurrencyThrottleSupport
/*  10:    */   implements Serializable
/*  11:    */ {
/*  12:    */   public static final int UNBOUNDED_CONCURRENCY = -1;
/*  13:    */   public static final int NO_CONCURRENCY = 0;
/*  14: 60 */   protected transient Log logger = LogFactory.getLog(getClass());
/*  15: 62 */   private transient Object monitor = new Object();
/*  16: 64 */   private int concurrencyLimit = -1;
/*  17: 66 */   private int concurrencyCount = 0;
/*  18:    */   
/*  19:    */   public void setConcurrencyLimit(int concurrencyLimit)
/*  20:    */   {
/*  21: 79 */     this.concurrencyLimit = concurrencyLimit;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public int getConcurrencyLimit()
/*  25:    */   {
/*  26: 86 */     return this.concurrencyLimit;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public boolean isThrottleActive()
/*  30:    */   {
/*  31: 95 */     return this.concurrencyLimit > 0;
/*  32:    */   }
/*  33:    */   
/*  34:    */   protected void beforeAccess()
/*  35:    */   {
/*  36:105 */     if (this.concurrencyLimit == 0) {
/*  37:106 */       throw new IllegalStateException(
/*  38:107 */         "Currently no invocations allowed - concurrency limit set to NO_CONCURRENCY");
/*  39:    */     }
/*  40:109 */     if (this.concurrencyLimit > 0)
/*  41:    */     {
/*  42:110 */       boolean debug = this.logger.isDebugEnabled();
/*  43:111 */       synchronized (this.monitor)
/*  44:    */       {
/*  45:112 */         boolean interrupted = false;
/*  46:113 */         while (this.concurrencyCount >= this.concurrencyLimit)
/*  47:    */         {
/*  48:114 */           if (interrupted) {
/*  49:115 */             throw new IllegalStateException("Thread was interrupted while waiting for invocation access, but concurrency limit still does not allow for entering");
/*  50:    */           }
/*  51:118 */           if (debug) {
/*  52:119 */             this.logger.debug("Concurrency count " + this.concurrencyCount + 
/*  53:120 */               " has reached limit " + this.concurrencyLimit + " - blocking");
/*  54:    */           }
/*  55:    */           try
/*  56:    */           {
/*  57:123 */             this.monitor.wait();
/*  58:    */           }
/*  59:    */           catch (InterruptedException localInterruptedException)
/*  60:    */           {
/*  61:127 */             Thread.currentThread().interrupt();
/*  62:128 */             interrupted = true;
/*  63:    */           }
/*  64:    */         }
/*  65:131 */         if (debug) {
/*  66:132 */           this.logger.debug("Entering throttle at concurrency count " + this.concurrencyCount);
/*  67:    */         }
/*  68:134 */         this.concurrencyCount += 1;
/*  69:    */       }
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   protected void afterAccess()
/*  74:    */   {
/*  75:144 */     if (this.concurrencyLimit >= 0) {
/*  76:145 */       synchronized (this.monitor)
/*  77:    */       {
/*  78:146 */         this.concurrencyCount -= 1;
/*  79:147 */         if (this.logger.isDebugEnabled()) {
/*  80:148 */           this.logger.debug("Returning from throttle at concurrency count " + this.concurrencyCount);
/*  81:    */         }
/*  82:150 */         this.monitor.notify();
/*  83:    */       }
/*  84:    */     }
/*  85:    */   }
/*  86:    */   
/*  87:    */   private void readObject(ObjectInputStream ois)
/*  88:    */     throws IOException, ClassNotFoundException
/*  89:    */   {
/*  90:162 */     ois.defaultReadObject();
/*  91:    */     
/*  92:    */ 
/*  93:165 */     this.logger = LogFactory.getLog(getClass());
/*  94:166 */     this.monitor = new Object();
/*  95:    */   }
/*  96:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.ConcurrencyThrottleSupport
 * JD-Core Version:    0.7.0.1
 */