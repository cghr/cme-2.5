/*   1:    */ package org.springframework.core.task.support;
/*   2:    */ 
/*   3:    */ import java.util.concurrent.Callable;
/*   4:    */ import java.util.concurrent.Executor;
/*   5:    */ import java.util.concurrent.ExecutorService;
/*   6:    */ import java.util.concurrent.Future;
/*   7:    */ import java.util.concurrent.FutureTask;
/*   8:    */ import java.util.concurrent.RejectedExecutionException;
/*   9:    */ import org.springframework.core.task.AsyncTaskExecutor;
/*  10:    */ import org.springframework.core.task.TaskRejectedException;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ 
/*  13:    */ public class TaskExecutorAdapter
/*  14:    */   implements AsyncTaskExecutor
/*  15:    */ {
/*  16:    */   private Executor concurrentExecutor;
/*  17:    */   
/*  18:    */   public TaskExecutorAdapter(Executor concurrentExecutor)
/*  19:    */   {
/*  20: 53 */     Assert.notNull(concurrentExecutor, "Executor must not be null");
/*  21: 54 */     this.concurrentExecutor = concurrentExecutor;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void execute(Runnable task)
/*  25:    */   {
/*  26:    */     try
/*  27:    */     {
/*  28: 64 */       this.concurrentExecutor.execute(task);
/*  29:    */     }
/*  30:    */     catch (RejectedExecutionException ex)
/*  31:    */     {
/*  32: 67 */       throw new TaskRejectedException(
/*  33: 68 */         "Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
/*  34:    */     }
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void execute(Runnable task, long startTimeout)
/*  38:    */   {
/*  39: 73 */     execute(task);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public Future<?> submit(Runnable task)
/*  43:    */   {
/*  44:    */     try
/*  45:    */     {
/*  46: 78 */       if ((this.concurrentExecutor instanceof ExecutorService)) {
/*  47: 79 */         return ((ExecutorService)this.concurrentExecutor).submit(task);
/*  48:    */       }
/*  49: 82 */       FutureTask<Object> future = new FutureTask(task, null);
/*  50: 83 */       this.concurrentExecutor.execute(future);
/*  51: 84 */       return future;
/*  52:    */     }
/*  53:    */     catch (RejectedExecutionException ex)
/*  54:    */     {
/*  55: 88 */       throw new TaskRejectedException(
/*  56: 89 */         "Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   public <T> Future<T> submit(Callable<T> task)
/*  61:    */   {
/*  62:    */     try
/*  63:    */     {
/*  64: 95 */       if ((this.concurrentExecutor instanceof ExecutorService)) {
/*  65: 96 */         return ((ExecutorService)this.concurrentExecutor).submit(task);
/*  66:    */       }
/*  67: 99 */       FutureTask<T> future = new FutureTask(task);
/*  68:100 */       this.concurrentExecutor.execute(future);
/*  69:101 */       return future;
/*  70:    */     }
/*  71:    */     catch (RejectedExecutionException ex)
/*  72:    */     {
/*  73:105 */       throw new TaskRejectedException(
/*  74:106 */         "Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
/*  75:    */     }
/*  76:    */   }
/*  77:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.task.support.TaskExecutorAdapter
 * JD-Core Version:    0.7.0.1
 */