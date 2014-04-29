/*  1:   */ package org.springframework.jca.support;
/*  2:   */ 
/*  3:   */ import java.util.Timer;
/*  4:   */ import javax.resource.spi.BootstrapContext;
/*  5:   */ import javax.resource.spi.UnavailableException;
/*  6:   */ import javax.resource.spi.XATerminator;
/*  7:   */ import javax.resource.spi.work.WorkManager;
/*  8:   */ 
/*  9:   */ public class SimpleBootstrapContext
/* 10:   */   implements BootstrapContext
/* 11:   */ {
/* 12:   */   private WorkManager workManager;
/* 13:   */   private XATerminator xaTerminator;
/* 14:   */   
/* 15:   */   public SimpleBootstrapContext(WorkManager workManager)
/* 16:   */   {
/* 17:51 */     this.workManager = workManager;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public SimpleBootstrapContext(WorkManager workManager, XATerminator xaTerminator)
/* 21:   */   {
/* 22:60 */     this.workManager = workManager;
/* 23:61 */     this.xaTerminator = xaTerminator;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public WorkManager getWorkManager()
/* 27:   */   {
/* 28:66 */     if (this.workManager == null) {
/* 29:67 */       throw new IllegalStateException("No WorkManager available");
/* 30:   */     }
/* 31:69 */     return this.workManager;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public XATerminator getXATerminator()
/* 35:   */   {
/* 36:73 */     return this.xaTerminator;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public Timer createTimer()
/* 40:   */     throws UnavailableException
/* 41:   */   {
/* 42:77 */     return new Timer();
/* 43:   */   }
/* 44:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.support.SimpleBootstrapContext
 * JD-Core Version:    0.7.0.1
 */