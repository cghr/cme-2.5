/*  1:   */ package org.springframework.scheduling.commonj;
/*  2:   */ 
/*  3:   */ import commonj.work.Work;
/*  4:   */ import org.springframework.scheduling.SchedulingAwareRunnable;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ 
/*  7:   */ public class DelegatingWork
/*  8:   */   implements Work
/*  9:   */ {
/* 10:   */   private final Runnable delegate;
/* 11:   */   
/* 12:   */   public DelegatingWork(Runnable delegate)
/* 13:   */   {
/* 14:45 */     Assert.notNull(delegate, "Delegate must not be null");
/* 15:46 */     this.delegate = delegate;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public final Runnable getDelegate()
/* 19:   */   {
/* 20:53 */     return this.delegate;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void run()
/* 24:   */   {
/* 25:61 */     this.delegate.run();
/* 26:   */   }
/* 27:   */   
/* 28:   */   public boolean isDaemon()
/* 29:   */   {
/* 30:71 */     return ((this.delegate instanceof SchedulingAwareRunnable)) && (((SchedulingAwareRunnable)this.delegate).isLongLived());
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void release() {}
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.commonj.DelegatingWork
 * JD-Core Version:    0.7.0.1
 */