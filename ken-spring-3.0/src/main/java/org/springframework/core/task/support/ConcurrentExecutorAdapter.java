/*  1:   */ package org.springframework.core.task.support;
/*  2:   */ 
/*  3:   */ import java.util.concurrent.Executor;
/*  4:   */ import org.springframework.core.task.TaskExecutor;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ 
/*  7:   */ public class ConcurrentExecutorAdapter
/*  8:   */   implements Executor
/*  9:   */ {
/* 10:   */   private final TaskExecutor taskExecutor;
/* 11:   */   
/* 12:   */   public ConcurrentExecutorAdapter(TaskExecutor taskExecutor)
/* 13:   */   {
/* 14:48 */     Assert.notNull(taskExecutor, "TaskExecutor must not be null");
/* 15:49 */     this.taskExecutor = taskExecutor;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void execute(Runnable command)
/* 19:   */   {
/* 20:54 */     this.taskExecutor.execute(command);
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.task.support.ConcurrentExecutorAdapter
 * JD-Core Version:    0.7.0.1
 */