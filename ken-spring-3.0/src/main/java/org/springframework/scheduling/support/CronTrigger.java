/*  1:   */ package org.springframework.scheduling.support;
/*  2:   */ 
/*  3:   */ import java.util.Date;
/*  4:   */ import java.util.TimeZone;
/*  5:   */ import org.springframework.scheduling.Trigger;
/*  6:   */ import org.springframework.scheduling.TriggerContext;
/*  7:   */ 
/*  8:   */ public class CronTrigger
/*  9:   */   implements Trigger
/* 10:   */ {
/* 11:   */   private final CronSequenceGenerator sequenceGenerator;
/* 12:   */   
/* 13:   */   public CronTrigger(String cronExpression)
/* 14:   */   {
/* 15:44 */     this(cronExpression, TimeZone.getDefault());
/* 16:   */   }
/* 17:   */   
/* 18:   */   public CronTrigger(String cronExpression, TimeZone timeZone)
/* 19:   */   {
/* 20:54 */     this.sequenceGenerator = new CronSequenceGenerator(cronExpression, timeZone);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public Date nextExecutionTime(TriggerContext triggerContext)
/* 24:   */   {
/* 25:59 */     Date date = triggerContext.lastCompletionTime();
/* 26:60 */     if (date != null)
/* 27:   */     {
/* 28:61 */       Date scheduled = triggerContext.lastScheduledExecutionTime();
/* 29:62 */       if ((scheduled != null) && (date.before(scheduled))) {
/* 30:66 */         date = scheduled;
/* 31:   */       }
/* 32:   */     }
/* 33:   */     else
/* 34:   */     {
/* 35:70 */       date = new Date();
/* 36:   */     }
/* 37:72 */     return this.sequenceGenerator.next(date);
/* 38:   */   }
/* 39:   */   
/* 40:   */   public boolean equals(Object obj)
/* 41:   */   {
/* 42:79 */     return (this == obj) || (((obj instanceof CronTrigger)) && (this.sequenceGenerator.equals(((CronTrigger)obj).sequenceGenerator)));
/* 43:   */   }
/* 44:   */   
/* 45:   */   public int hashCode()
/* 46:   */   {
/* 47:84 */     return this.sequenceGenerator.hashCode();
/* 48:   */   }
/* 49:   */   
/* 50:   */   public String toString()
/* 51:   */   {
/* 52:89 */     return this.sequenceGenerator.toString();
/* 53:   */   }
/* 54:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.support.CronTrigger
 * JD-Core Version:    0.7.0.1
 */