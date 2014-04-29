/*  1:   */ package org.springframework.core.task;
/*  2:   */ 
/*  3:   */ import java.util.concurrent.RejectedExecutionException;
/*  4:   */ 
/*  5:   */ public class TaskRejectedException
/*  6:   */   extends RejectedExecutionException
/*  7:   */ {
/*  8:   */   public TaskRejectedException(String msg)
/*  9:   */   {
/* 10:38 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public TaskRejectedException(String msg, Throwable cause)
/* 14:   */   {
/* 15:50 */     super(msg, cause);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.task.TaskRejectedException
 * JD-Core Version:    0.7.0.1
 */