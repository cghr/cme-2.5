/*  1:   */ package org.springframework.scheduling.quartz;
/*  2:   */ 
/*  3:   */ import java.util.concurrent.Callable;
/*  4:   */ import java.util.concurrent.Future;
/*  5:   */ import java.util.concurrent.FutureTask;
/*  6:   */ import org.quartz.SchedulerConfigException;
/*  7:   */ import org.quartz.simpl.SimpleThreadPool;
/*  8:   */ import org.springframework.beans.factory.DisposableBean;
/*  9:   */ import org.springframework.beans.factory.InitializingBean;
/* 10:   */ import org.springframework.scheduling.SchedulingException;
/* 11:   */ import org.springframework.scheduling.SchedulingTaskExecutor;
/* 12:   */ import org.springframework.util.Assert;
/* 13:   */ 
/* 14:   */ public class SimpleThreadPoolTaskExecutor
/* 15:   */   extends SimpleThreadPool
/* 16:   */   implements SchedulingTaskExecutor, InitializingBean, DisposableBean
/* 17:   */ {
/* 18:50 */   private boolean waitForJobsToCompleteOnShutdown = false;
/* 19:   */   
/* 20:   */   public void setWaitForJobsToCompleteOnShutdown(boolean waitForJobsToCompleteOnShutdown)
/* 21:   */   {
/* 22:59 */     this.waitForJobsToCompleteOnShutdown = waitForJobsToCompleteOnShutdown;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void afterPropertiesSet()
/* 26:   */     throws SchedulerConfigException
/* 27:   */   {
/* 28:63 */     initialize();
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void execute(Runnable task)
/* 32:   */   {
/* 33:68 */     Assert.notNull(task, "Runnable must not be null");
/* 34:69 */     if (!runInThread(task)) {
/* 35:70 */       throw new SchedulingException("Quartz SimpleThreadPool already shut down");
/* 36:   */     }
/* 37:   */   }
/* 38:   */   
/* 39:   */   public void execute(Runnable task, long startTimeout)
/* 40:   */   {
/* 41:75 */     execute(task);
/* 42:   */   }
/* 43:   */   
/* 44:   */   public Future<?> submit(Runnable task)
/* 45:   */   {
/* 46:79 */     FutureTask<Object> future = new FutureTask(task, null);
/* 47:80 */     execute(future);
/* 48:81 */     return future;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public <T> Future<T> submit(Callable<T> task)
/* 52:   */   {
/* 53:85 */     FutureTask<T> future = new FutureTask(task);
/* 54:86 */     execute(future);
/* 55:87 */     return future;
/* 56:   */   }
/* 57:   */   
/* 58:   */   public boolean prefersShortLivedTasks()
/* 59:   */   {
/* 60:94 */     return true;
/* 61:   */   }
/* 62:   */   
/* 63:   */   public void destroy()
/* 64:   */   {
/* 65:99 */     shutdown(this.waitForJobsToCompleteOnShutdown);
/* 66:   */   }
/* 67:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.quartz.SimpleThreadPoolTaskExecutor
 * JD-Core Version:    0.7.0.1
 */