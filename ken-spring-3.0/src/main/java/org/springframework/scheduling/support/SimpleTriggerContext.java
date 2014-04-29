/*  1:   */ package org.springframework.scheduling.support;
/*  2:   */ 
/*  3:   */ import java.util.Date;
/*  4:   */ import org.springframework.scheduling.TriggerContext;
/*  5:   */ 
/*  6:   */ public class SimpleTriggerContext
/*  7:   */   implements TriggerContext
/*  8:   */ {
/*  9:   */   private volatile Date lastScheduledExecutionTime;
/* 10:   */   private volatile Date lastActualExecutionTime;
/* 11:   */   private volatile Date lastCompletionTime;
/* 12:   */   
/* 13:   */   public void update(Date lastScheduledExecutionTime, Date lastActualExecutionTime, Date lastCompletionTime)
/* 14:   */   {
/* 15:45 */     this.lastScheduledExecutionTime = lastScheduledExecutionTime;
/* 16:46 */     this.lastActualExecutionTime = lastActualExecutionTime;
/* 17:47 */     this.lastCompletionTime = lastCompletionTime;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public Date lastScheduledExecutionTime()
/* 21:   */   {
/* 22:52 */     return this.lastScheduledExecutionTime;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public Date lastActualExecutionTime()
/* 26:   */   {
/* 27:56 */     return this.lastActualExecutionTime;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public Date lastCompletionTime()
/* 31:   */   {
/* 32:60 */     return this.lastCompletionTime;
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.support.SimpleTriggerContext
 * JD-Core Version:    0.7.0.1
 */