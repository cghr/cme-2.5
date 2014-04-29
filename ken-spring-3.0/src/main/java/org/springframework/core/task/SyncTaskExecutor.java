/*  1:   */ package org.springframework.core.task;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ 
/*  6:   */ public class SyncTaskExecutor
/*  7:   */   implements TaskExecutor, Serializable
/*  8:   */ {
/*  9:   */   public void execute(Runnable task)
/* 10:   */   {
/* 11:47 */     Assert.notNull(task, "Runnable must not be null");
/* 12:48 */     task.run();
/* 13:   */   }
/* 14:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.task.SyncTaskExecutor
 * JD-Core Version:    0.7.0.1
 */