/*  1:   */ package org.springframework.scheduling.quartz;
/*  2:   */ 
/*  3:   */ import org.quartz.Job;
/*  4:   */ import org.quartz.JobExecutionContext;
/*  5:   */ import org.quartz.JobExecutionException;
/*  6:   */ import org.springframework.util.Assert;
/*  7:   */ 
/*  8:   */ public class DelegatingJob
/*  9:   */   implements Job
/* 10:   */ {
/* 11:   */   private final Runnable delegate;
/* 12:   */   
/* 13:   */   public DelegatingJob(Runnable delegate)
/* 14:   */   {
/* 15:48 */     Assert.notNull(delegate, "Delegate must not be null");
/* 16:49 */     this.delegate = delegate;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public final Runnable getDelegate()
/* 20:   */   {
/* 21:56 */     return this.delegate;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void execute(JobExecutionContext context)
/* 25:   */     throws JobExecutionException
/* 26:   */   {
/* 27:64 */     this.delegate.run();
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.quartz.DelegatingJob
 * JD-Core Version:    0.7.0.1
 */