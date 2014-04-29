/*   1:    */ package org.springframework.scheduling.commonj;
/*   2:    */ 
/*   3:    */ import commonj.timers.TimerListener;
/*   4:    */ 
/*   5:    */ public class ScheduledTimerListener
/*   6:    */ {
/*   7:    */   private TimerListener timerListener;
/*   8: 45 */   private long delay = 0L;
/*   9: 47 */   private long period = -1L;
/*  10: 49 */   private boolean fixedRate = false;
/*  11:    */   
/*  12:    */   public ScheduledTimerListener() {}
/*  13:    */   
/*  14:    */   public ScheduledTimerListener(TimerListener timerListener)
/*  15:    */   {
/*  16: 69 */     this.timerListener = timerListener;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public ScheduledTimerListener(TimerListener timerListener, long delay)
/*  20:    */   {
/*  21: 79 */     this.timerListener = timerListener;
/*  22: 80 */     this.delay = delay;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public ScheduledTimerListener(TimerListener timerListener, long delay, long period, boolean fixedRate)
/*  26:    */   {
/*  27: 91 */     this.timerListener = timerListener;
/*  28: 92 */     this.delay = delay;
/*  29: 93 */     this.period = period;
/*  30: 94 */     this.fixedRate = fixedRate;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public ScheduledTimerListener(Runnable timerTask)
/*  34:    */   {
/*  35:103 */     setRunnable(timerTask);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public ScheduledTimerListener(Runnable timerTask, long delay)
/*  39:    */   {
/*  40:113 */     setRunnable(timerTask);
/*  41:114 */     this.delay = delay;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public ScheduledTimerListener(Runnable timerTask, long delay, long period, boolean fixedRate)
/*  45:    */   {
/*  46:125 */     setRunnable(timerTask);
/*  47:126 */     this.delay = delay;
/*  48:127 */     this.period = period;
/*  49:128 */     this.fixedRate = fixedRate;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setRunnable(Runnable timerTask)
/*  53:    */   {
/*  54:137 */     this.timerListener = new DelegatingTimerListener(timerTask);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setTimerListener(TimerListener timerListener)
/*  58:    */   {
/*  59:144 */     this.timerListener = timerListener;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public TimerListener getTimerListener()
/*  63:    */   {
/*  64:151 */     return this.timerListener;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setDelay(long delay)
/*  68:    */   {
/*  69:162 */     this.delay = delay;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public long getDelay()
/*  73:    */   {
/*  74:169 */     return this.delay;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void setPeriod(long period)
/*  78:    */   {
/*  79:188 */     this.period = period;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public long getPeriod()
/*  83:    */   {
/*  84:195 */     return this.period;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public boolean isOneTimeTask()
/*  88:    */   {
/*  89:204 */     return this.period < 0L;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void setFixedRate(boolean fixedRate)
/*  93:    */   {
/*  94:215 */     this.fixedRate = fixedRate;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public boolean isFixedRate()
/*  98:    */   {
/*  99:222 */     return this.fixedRate;
/* 100:    */   }
/* 101:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.commonj.ScheduledTimerListener
 * JD-Core Version:    0.7.0.1
 */