/*  1:   */ package org.springframework.core.task;
/*  2:   */ 
/*  3:   */ public class TaskTimeoutException
/*  4:   */   extends TaskRejectedException
/*  5:   */ {
/*  6:   */   public TaskTimeoutException(String msg)
/*  7:   */   {
/*  8:36 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public TaskTimeoutException(String msg, Throwable cause)
/* 12:   */   {
/* 13:48 */     super(msg, cause);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.task.TaskTimeoutException
 * JD-Core Version:    0.7.0.1
 */