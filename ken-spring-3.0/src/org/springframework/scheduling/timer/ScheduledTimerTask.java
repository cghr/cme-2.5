/*   1:    */ package org.springframework.scheduling.timer;
/*   2:    */ 
/*   3:    */ import java.util.TimerTask;
/*   4:    */ 
/*   5:    */ @Deprecated
/*   6:    */ public class ScheduledTimerTask
/*   7:    */ {
/*   8:    */   private TimerTask timerTask;
/*   9: 48 */   private long delay = 0L;
/*  10: 50 */   private long period = -1L;
/*  11: 52 */   private boolean fixedRate = false;
/*  12:    */   
/*  13:    */   public ScheduledTimerTask() {}
/*  14:    */   
/*  15:    */   public ScheduledTimerTask(TimerTask timerTask)
/*  16:    */   {
/*  17: 72 */     this.timerTask = timerTask;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public ScheduledTimerTask(TimerTask timerTask, long delay)
/*  21:    */   {
/*  22: 82 */     this.timerTask = timerTask;
/*  23: 83 */     this.delay = delay;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public ScheduledTimerTask(TimerTask timerTask, long delay, long period, boolean fixedRate)
/*  27:    */   {
/*  28: 94 */     this.timerTask = timerTask;
/*  29: 95 */     this.delay = delay;
/*  30: 96 */     this.period = period;
/*  31: 97 */     this.fixedRate = fixedRate;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public ScheduledTimerTask(Runnable timerTask)
/*  35:    */   {
/*  36:106 */     setRunnable(timerTask);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public ScheduledTimerTask(Runnable timerTask, long delay)
/*  40:    */   {
/*  41:116 */     setRunnable(timerTask);
/*  42:117 */     this.delay = delay;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public ScheduledTimerTask(Runnable timerTask, long delay, long period, boolean fixedRate)
/*  46:    */   {
/*  47:128 */     setRunnable(timerTask);
/*  48:129 */     this.delay = delay;
/*  49:130 */     this.period = period;
/*  50:131 */     this.fixedRate = fixedRate;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setRunnable(Runnable timerTask)
/*  54:    */   {
/*  55:140 */     this.timerTask = new DelegatingTimerTask(timerTask);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setTimerTask(TimerTask timerTask)
/*  59:    */   {
/*  60:147 */     this.timerTask = timerTask;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public TimerTask getTimerTask()
/*  64:    */   {
/*  65:154 */     return this.timerTask;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setDelay(long delay)
/*  69:    */   {
/*  70:163 */     this.delay = delay;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public long getDelay()
/*  74:    */   {
/*  75:170 */     return this.delay;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setPeriod(long period)
/*  79:    */   {
/*  80:189 */     this.period = period;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public long getPeriod()
/*  84:    */   {
/*  85:196 */     return this.period;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public boolean isOneTimeTask()
/*  89:    */   {
/*  90:205 */     return this.period <= 0L;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void setFixedRate(boolean fixedRate)
/*  94:    */   {
/*  95:216 */     this.fixedRate = fixedRate;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public boolean isFixedRate()
/*  99:    */   {
/* 100:223 */     return this.fixedRate;
/* 101:    */   }
/* 102:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.timer.ScheduledTimerTask
 * JD-Core Version:    0.7.0.1
 */