/*   1:    */ package org.springframework.scheduling.backportconcurrent;
/*   2:    */ 
/*   3:    */ import edu.emory.mathcs.backport.java.util.concurrent.Executor;
/*   4:    */ import edu.emory.mathcs.backport.java.util.concurrent.Executors;
/*   5:    */ import edu.emory.mathcs.backport.java.util.concurrent.RejectedExecutionException;
/*   6:    */ import java.util.concurrent.Callable;
/*   7:    */ import java.util.concurrent.Future;
/*   8:    */ import java.util.concurrent.FutureTask;
/*   9:    */ import org.springframework.core.task.TaskRejectedException;
/*  10:    */ import org.springframework.scheduling.SchedulingTaskExecutor;
/*  11:    */ 
/*  12:    */ public class ConcurrentTaskExecutor
/*  13:    */   implements SchedulingTaskExecutor, Executor
/*  14:    */ {
/*  15:    */   private Executor concurrentExecutor;
/*  16:    */   
/*  17:    */   public ConcurrentTaskExecutor()
/*  18:    */   {
/*  19: 69 */     setConcurrentExecutor(null);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public ConcurrentTaskExecutor(Executor concurrentExecutor)
/*  23:    */   {
/*  24: 78 */     setConcurrentExecutor(concurrentExecutor);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public final void setConcurrentExecutor(Executor concurrentExecutor)
/*  28:    */   {
/*  29: 86 */     this.concurrentExecutor = 
/*  30: 87 */       (concurrentExecutor != null ? concurrentExecutor : Executors.newSingleThreadExecutor());
/*  31:    */   }
/*  32:    */   
/*  33:    */   public final Executor getConcurrentExecutor()
/*  34:    */   {
/*  35: 95 */     return this.concurrentExecutor;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void execute(Runnable task)
/*  39:    */   {
/*  40:    */     try
/*  41:    */     {
/*  42:105 */       this.concurrentExecutor.execute(task);
/*  43:    */     }
/*  44:    */     catch (RejectedExecutionException ex)
/*  45:    */     {
/*  46:108 */       throw new TaskRejectedException(
/*  47:109 */         "Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void execute(Runnable task, long startTimeout)
/*  52:    */   {
/*  53:114 */     execute(task);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public Future<?> submit(Runnable task)
/*  57:    */   {
/*  58:118 */     FutureTask<Object> future = new FutureTask(task, null);
/*  59:119 */     execute(future);
/*  60:120 */     return future;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public <T> Future<T> submit(Callable<T> task)
/*  64:    */   {
/*  65:124 */     FutureTask<T> future = new FutureTask(task);
/*  66:125 */     execute(future);
/*  67:126 */     return future;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public boolean prefersShortLivedTasks()
/*  71:    */   {
/*  72:133 */     return true;
/*  73:    */   }
/*  74:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.backportconcurrent.ConcurrentTaskExecutor
 * JD-Core Version:    0.7.0.1
 */