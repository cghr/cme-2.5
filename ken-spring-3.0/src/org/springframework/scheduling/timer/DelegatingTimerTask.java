/*  1:   */ package org.springframework.scheduling.timer;
/*  2:   */ 
/*  3:   */ import java.util.TimerTask;
/*  4:   */ import org.apache.commons.logging.Log;
/*  5:   */ import org.apache.commons.logging.LogFactory;
/*  6:   */ import org.springframework.util.Assert;
/*  7:   */ 
/*  8:   */ @Deprecated
/*  9:   */ public class DelegatingTimerTask
/* 10:   */   extends TimerTask
/* 11:   */ {
/* 12:41 */   private static final Log logger = LogFactory.getLog(DelegatingTimerTask.class);
/* 13:   */   private final Runnable delegate;
/* 14:   */   
/* 15:   */   public DelegatingTimerTask(Runnable delegate)
/* 16:   */   {
/* 17:51 */     Assert.notNull(delegate, "Delegate must not be null");
/* 18:52 */     this.delegate = delegate;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public final Runnable getDelegate()
/* 22:   */   {
/* 23:59 */     return this.delegate;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void run()
/* 27:   */   {
/* 28:   */     try
/* 29:   */     {
/* 30:70 */       this.delegate.run();
/* 31:   */     }
/* 32:   */     catch (Throwable ex)
/* 33:   */     {
/* 34:73 */       logger.error("Unexpected exception thrown from Runnable: " + this.delegate, ex);
/* 35:   */     }
/* 36:   */   }
/* 37:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.timer.DelegatingTimerTask
 * JD-Core Version:    0.7.0.1
 */