/*  1:   */ package org.springframework.scheduling.commonj;
/*  2:   */ 
/*  3:   */ import commonj.timers.Timer;
/*  4:   */ import commonj.timers.TimerListener;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ 
/*  7:   */ public class DelegatingTimerListener
/*  8:   */   implements TimerListener
/*  9:   */ {
/* 10:   */   private final Runnable runnable;
/* 11:   */   
/* 12:   */   public DelegatingTimerListener(Runnable runnable)
/* 13:   */   {
/* 14:42 */     Assert.notNull(runnable, "Runnable is required");
/* 15:43 */     this.runnable = runnable;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void timerExpired(Timer timer)
/* 19:   */   {
/* 20:51 */     this.runnable.run();
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.commonj.DelegatingTimerListener
 * JD-Core Version:    0.7.0.1
 */