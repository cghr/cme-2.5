/*   1:    */ package org.springframework.scheduling.support;
/*   2:    */ 
/*   3:    */ import java.util.Date;
/*   4:    */ import java.util.concurrent.TimeUnit;
/*   5:    */ import org.springframework.scheduling.Trigger;
/*   6:    */ import org.springframework.scheduling.TriggerContext;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ 
/*   9:    */ public class PeriodicTrigger
/*  10:    */   implements Trigger
/*  11:    */ {
/*  12:    */   private final long period;
/*  13:    */   private final TimeUnit timeUnit;
/*  14: 52 */   private volatile long initialDelay = 0L;
/*  15: 54 */   private volatile boolean fixedRate = false;
/*  16:    */   
/*  17:    */   public PeriodicTrigger(long period)
/*  18:    */   {
/*  19: 61 */     this(period, null);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public PeriodicTrigger(long period, TimeUnit timeUnit)
/*  23:    */   {
/*  24: 70 */     Assert.isTrue(period >= 0L, "period must not be negative");
/*  25: 71 */     this.timeUnit = (timeUnit != null ? timeUnit : TimeUnit.MILLISECONDS);
/*  26: 72 */     this.period = this.timeUnit.toMillis(period);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setInitialDelay(long initialDelay)
/*  30:    */   {
/*  31: 82 */     this.initialDelay = this.timeUnit.toMillis(initialDelay);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setFixedRate(boolean fixedRate)
/*  35:    */   {
/*  36: 91 */     this.fixedRate = fixedRate;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public Date nextExecutionTime(TriggerContext triggerContext)
/*  40:    */   {
/*  41: 98 */     if (triggerContext.lastScheduledExecutionTime() == null) {
/*  42: 99 */       return new Date(System.currentTimeMillis() + this.initialDelay);
/*  43:    */     }
/*  44:101 */     if (this.fixedRate) {
/*  45:102 */       return new Date(triggerContext.lastScheduledExecutionTime().getTime() + this.period);
/*  46:    */     }
/*  47:104 */     return new Date(triggerContext.lastCompletionTime().getTime() + this.period);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public boolean equals(Object obj)
/*  51:    */   {
/*  52:109 */     if (this == obj) {
/*  53:110 */       return true;
/*  54:    */     }
/*  55:112 */     if (!(obj instanceof PeriodicTrigger)) {
/*  56:113 */       return false;
/*  57:    */     }
/*  58:115 */     PeriodicTrigger other = (PeriodicTrigger)obj;
/*  59:    */     
/*  60:    */ 
/*  61:118 */     return (this.fixedRate == other.fixedRate) && (this.initialDelay == other.initialDelay) && (this.period == other.period);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public int hashCode()
/*  65:    */   {
/*  66:123 */     return (this.fixedRate ? 17 : 29) + 
/*  67:124 */       (int)(37L * this.period) + 
/*  68:125 */       (int)(41L * this.initialDelay);
/*  69:    */   }
/*  70:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.support.PeriodicTrigger
 * JD-Core Version:    0.7.0.1
 */