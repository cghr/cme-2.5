/*   1:    */ package org.springframework.scheduling.concurrent;
/*   2:    */ 
/*   3:    */ import java.util.concurrent.Callable;
/*   4:    */ import java.util.concurrent.Executor;
/*   5:    */ import java.util.concurrent.Executors;
/*   6:    */ import java.util.concurrent.Future;
/*   7:    */ import org.springframework.core.task.support.TaskExecutorAdapter;
/*   8:    */ import org.springframework.scheduling.SchedulingTaskExecutor;
/*   9:    */ 
/*  10:    */ public class ConcurrentTaskExecutor
/*  11:    */   implements SchedulingTaskExecutor
/*  12:    */ {
/*  13:    */   private Executor concurrentExecutor;
/*  14:    */   private TaskExecutorAdapter adaptedExecutor;
/*  15:    */   
/*  16:    */   public ConcurrentTaskExecutor()
/*  17:    */   {
/*  18: 64 */     setConcurrentExecutor(null);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public ConcurrentTaskExecutor(Executor concurrentExecutor)
/*  22:    */   {
/*  23: 73 */     setConcurrentExecutor(concurrentExecutor);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public final void setConcurrentExecutor(Executor concurrentExecutor)
/*  27:    */   {
/*  28: 81 */     this.concurrentExecutor = 
/*  29: 82 */       (concurrentExecutor != null ? concurrentExecutor : Executors.newSingleThreadExecutor());
/*  30: 83 */     this.adaptedExecutor = new TaskExecutorAdapter(this.concurrentExecutor);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public final Executor getConcurrentExecutor()
/*  34:    */   {
/*  35: 90 */     return this.concurrentExecutor;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void execute(Runnable task)
/*  39:    */   {
/*  40: 95 */     this.adaptedExecutor.execute(task);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void execute(Runnable task, long startTimeout)
/*  44:    */   {
/*  45: 99 */     this.adaptedExecutor.execute(task, startTimeout);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public Future<?> submit(Runnable task)
/*  49:    */   {
/*  50:103 */     return this.adaptedExecutor.submit(task);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public <T> Future<T> submit(Callable<T> task)
/*  54:    */   {
/*  55:107 */     return this.adaptedExecutor.submit(task);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public boolean prefersShortLivedTasks()
/*  59:    */   {
/*  60:114 */     return true;
/*  61:    */   }
/*  62:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.concurrent.ConcurrentTaskExecutor
 * JD-Core Version:    0.7.0.1
 */