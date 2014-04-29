/*   1:    */ package org.springframework.scheduling.concurrent;
/*   2:    */ 
/*   3:    */ import java.util.concurrent.TimeUnit;
/*   4:    */ 
/*   5:    */ public class ScheduledExecutorTask
/*   6:    */ {
/*   7:    */   private Runnable runnable;
/*   8: 43 */   private long delay = 0L;
/*   9: 45 */   private long period = -1L;
/*  10: 47 */   private TimeUnit timeUnit = TimeUnit.MILLISECONDS;
/*  11: 49 */   private boolean fixedRate = false;
/*  12:    */   
/*  13:    */   public ScheduledExecutorTask() {}
/*  14:    */   
/*  15:    */   public ScheduledExecutorTask(Runnable executorTask)
/*  16:    */   {
/*  17: 68 */     this.runnable = executorTask;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public ScheduledExecutorTask(Runnable executorTask, long delay)
/*  21:    */   {
/*  22: 78 */     this.runnable = executorTask;
/*  23: 79 */     this.delay = delay;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public ScheduledExecutorTask(Runnable executorTask, long delay, long period, boolean fixedRate)
/*  27:    */   {
/*  28: 90 */     this.runnable = executorTask;
/*  29: 91 */     this.delay = delay;
/*  30: 92 */     this.period = period;
/*  31: 93 */     this.fixedRate = fixedRate;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setRunnable(Runnable executorTask)
/*  35:    */   {
/*  36:101 */     this.runnable = executorTask;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public Runnable getRunnable()
/*  40:    */   {
/*  41:108 */     return this.runnable;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setDelay(long delay)
/*  45:    */   {
/*  46:117 */     this.delay = delay;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public long getDelay()
/*  50:    */   {
/*  51:124 */     return this.delay;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setPeriod(long period)
/*  55:    */   {
/*  56:142 */     this.period = period;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public long getPeriod()
/*  60:    */   {
/*  61:149 */     return this.period;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public boolean isOneTimeTask()
/*  65:    */   {
/*  66:158 */     return this.period <= 0L;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setTimeUnit(TimeUnit timeUnit)
/*  70:    */   {
/*  71:168 */     this.timeUnit = (timeUnit != null ? timeUnit : TimeUnit.MILLISECONDS);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public TimeUnit getTimeUnit()
/*  75:    */   {
/*  76:175 */     return this.timeUnit;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setFixedRate(boolean fixedRate)
/*  80:    */   {
/*  81:186 */     this.fixedRate = fixedRate;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public boolean isFixedRate()
/*  85:    */   {
/*  86:193 */     return this.fixedRate;
/*  87:    */   }
/*  88:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.concurrent.ScheduledExecutorTask
 * JD-Core Version:    0.7.0.1
 */