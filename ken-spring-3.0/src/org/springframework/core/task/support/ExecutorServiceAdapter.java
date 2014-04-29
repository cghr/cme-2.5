/*  1:   */ package org.springframework.core.task.support;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ import java.util.concurrent.AbstractExecutorService;
/*  5:   */ import java.util.concurrent.TimeUnit;
/*  6:   */ import org.springframework.core.task.TaskExecutor;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ 
/*  9:   */ public class ExecutorServiceAdapter
/* 10:   */   extends AbstractExecutorService
/* 11:   */ {
/* 12:   */   private final TaskExecutor taskExecutor;
/* 13:   */   
/* 14:   */   public ExecutorServiceAdapter(TaskExecutor taskExecutor)
/* 15:   */   {
/* 16:55 */     Assert.notNull(taskExecutor, "TaskExecutor must not be null");
/* 17:56 */     this.taskExecutor = taskExecutor;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void execute(Runnable task)
/* 21:   */   {
/* 22:61 */     this.taskExecutor.execute(task);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void shutdown()
/* 26:   */   {
/* 27:65 */     throw new IllegalStateException(
/* 28:66 */       "Manual shutdown not supported - ExecutorServiceAdapter is dependent on an external lifecycle");
/* 29:   */   }
/* 30:   */   
/* 31:   */   public List<Runnable> shutdownNow()
/* 32:   */   {
/* 33:70 */     throw new IllegalStateException(
/* 34:71 */       "Manual shutdown not supported - ExecutorServiceAdapter is dependent on an external lifecycle");
/* 35:   */   }
/* 36:   */   
/* 37:   */   public boolean awaitTermination(long timeout, TimeUnit unit)
/* 38:   */     throws InterruptedException
/* 39:   */   {
/* 40:75 */     throw new IllegalStateException(
/* 41:76 */       "Manual shutdown not supported - ExecutorServiceAdapter is dependent on an external lifecycle");
/* 42:   */   }
/* 43:   */   
/* 44:   */   public boolean isShutdown()
/* 45:   */   {
/* 46:80 */     return false;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public boolean isTerminated()
/* 50:   */   {
/* 51:84 */     return false;
/* 52:   */   }
/* 53:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.task.support.ExecutorServiceAdapter
 * JD-Core Version:    0.7.0.1
 */