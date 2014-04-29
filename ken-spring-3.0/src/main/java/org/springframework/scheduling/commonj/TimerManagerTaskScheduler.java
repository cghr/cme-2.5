/*   1:    */ package org.springframework.scheduling.commonj;
/*   2:    */ 
/*   3:    */ import commonj.timers.Timer;
/*   4:    */ import commonj.timers.TimerListener;
/*   5:    */ import commonj.timers.TimerManager;
/*   6:    */ import java.util.Date;
/*   7:    */ import java.util.concurrent.Delayed;
/*   8:    */ import java.util.concurrent.FutureTask;
/*   9:    */ import java.util.concurrent.ScheduledFuture;
/*  10:    */ import java.util.concurrent.TimeUnit;
/*  11:    */ import org.springframework.scheduling.TaskScheduler;
/*  12:    */ import org.springframework.scheduling.Trigger;
/*  13:    */ import org.springframework.scheduling.support.SimpleTriggerContext;
/*  14:    */ import org.springframework.scheduling.support.TaskUtils;
/*  15:    */ import org.springframework.util.ErrorHandler;
/*  16:    */ 
/*  17:    */ public class TimerManagerTaskScheduler
/*  18:    */   extends TimerManagerAccessor
/*  19:    */   implements TaskScheduler
/*  20:    */ {
/*  21:    */   private volatile ErrorHandler errorHandler;
/*  22:    */   
/*  23:    */   public void setErrorHandler(ErrorHandler errorHandler)
/*  24:    */   {
/*  25: 47 */     this.errorHandler = errorHandler;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public ScheduledFuture schedule(Runnable task, Trigger trigger)
/*  29:    */   {
/*  30: 51 */     return new ReschedulingTimerListener(errorHandlingTask(task, true), trigger).schedule();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public ScheduledFuture schedule(Runnable task, Date startTime)
/*  34:    */   {
/*  35: 55 */     TimerScheduledFuture futureTask = new TimerScheduledFuture(errorHandlingTask(task, false));
/*  36: 56 */     Timer timer = getTimerManager().schedule(futureTask, startTime);
/*  37: 57 */     futureTask.setTimer(timer);
/*  38: 58 */     return futureTask;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public ScheduledFuture scheduleAtFixedRate(Runnable task, Date startTime, long period)
/*  42:    */   {
/*  43: 62 */     TimerScheduledFuture futureTask = new TimerScheduledFuture(errorHandlingTask(task, true));
/*  44: 63 */     Timer timer = getTimerManager().scheduleAtFixedRate(futureTask, startTime, period);
/*  45: 64 */     futureTask.setTimer(timer);
/*  46: 65 */     return futureTask;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public ScheduledFuture scheduleAtFixedRate(Runnable task, long period)
/*  50:    */   {
/*  51: 69 */     TimerScheduledFuture futureTask = new TimerScheduledFuture(errorHandlingTask(task, true));
/*  52: 70 */     Timer timer = getTimerManager().scheduleAtFixedRate(futureTask, 0L, period);
/*  53: 71 */     futureTask.setTimer(timer);
/*  54: 72 */     return futureTask;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public ScheduledFuture scheduleWithFixedDelay(Runnable task, Date startTime, long delay)
/*  58:    */   {
/*  59: 76 */     TimerScheduledFuture futureTask = new TimerScheduledFuture(errorHandlingTask(task, true));
/*  60: 77 */     Timer timer = getTimerManager().schedule(futureTask, startTime, delay);
/*  61: 78 */     futureTask.setTimer(timer);
/*  62: 79 */     return futureTask;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public ScheduledFuture scheduleWithFixedDelay(Runnable task, long delay)
/*  66:    */   {
/*  67: 83 */     TimerScheduledFuture futureTask = new TimerScheduledFuture(errorHandlingTask(task, true));
/*  68: 84 */     Timer timer = getTimerManager().schedule(futureTask, 0L, delay);
/*  69: 85 */     futureTask.setTimer(timer);
/*  70: 86 */     return futureTask;
/*  71:    */   }
/*  72:    */   
/*  73:    */   private Runnable errorHandlingTask(Runnable delegate, boolean isRepeatingTask)
/*  74:    */   {
/*  75: 90 */     return TaskUtils.decorateTaskWithErrorHandler(delegate, this.errorHandler, isRepeatingTask);
/*  76:    */   }
/*  77:    */   
/*  78:    */   private static class TimerScheduledFuture
/*  79:    */     extends FutureTask<Object>
/*  80:    */     implements TimerListener, ScheduledFuture<Object>
/*  81:    */   {
/*  82:    */     protected transient Timer timer;
/*  83:101 */     protected transient boolean cancelled = false;
/*  84:    */     
/*  85:    */     public TimerScheduledFuture(Runnable runnable)
/*  86:    */     {
/*  87:104 */       super(null);
/*  88:    */     }
/*  89:    */     
/*  90:    */     public void setTimer(Timer timer)
/*  91:    */     {
/*  92:108 */       this.timer = timer;
/*  93:    */     }
/*  94:    */     
/*  95:    */     public void timerExpired(Timer timer)
/*  96:    */     {
/*  97:112 */       runAndReset();
/*  98:    */     }
/*  99:    */     
/* 100:    */     public boolean cancel(boolean mayInterruptIfRunning)
/* 101:    */     {
/* 102:117 */       boolean result = super.cancel(mayInterruptIfRunning);
/* 103:118 */       this.timer.cancel();
/* 104:119 */       this.cancelled = true;
/* 105:120 */       return result;
/* 106:    */     }
/* 107:    */     
/* 108:    */     public long getDelay(TimeUnit unit)
/* 109:    */     {
/* 110:124 */       return unit.convert(System.currentTimeMillis() - this.timer.getScheduledExecutionTime(), TimeUnit.MILLISECONDS);
/* 111:    */     }
/* 112:    */     
/* 113:    */     public int compareTo(Delayed other)
/* 114:    */     {
/* 115:128 */       if (this == other) {
/* 116:129 */         return 0;
/* 117:    */       }
/* 118:131 */       long diff = getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
/* 119:132 */       return diff < 0L ? -1 : diff == 0L ? 0 : 1;
/* 120:    */     }
/* 121:    */   }
/* 122:    */   
/* 123:    */   private class ReschedulingTimerListener
/* 124:    */     extends TimerManagerTaskScheduler.TimerScheduledFuture
/* 125:    */   {
/* 126:    */     private final Trigger trigger;
/* 127:144 */     private final SimpleTriggerContext triggerContext = new SimpleTriggerContext();
/* 128:    */     private volatile Date scheduledExecutionTime;
/* 129:    */     
/* 130:    */     public ReschedulingTimerListener(Runnable runnable, Trigger trigger)
/* 131:    */     {
/* 132:149 */       super();
/* 133:150 */       this.trigger = trigger;
/* 134:    */     }
/* 135:    */     
/* 136:    */     public ScheduledFuture schedule()
/* 137:    */     {
/* 138:154 */       this.scheduledExecutionTime = this.trigger.nextExecutionTime(this.triggerContext);
/* 139:155 */       if (this.scheduledExecutionTime == null) {
/* 140:156 */         return null;
/* 141:    */       }
/* 142:158 */       setTimer(TimerManagerTaskScheduler.this.getTimerManager().schedule(this, this.scheduledExecutionTime));
/* 143:159 */       return this;
/* 144:    */     }
/* 145:    */     
/* 146:    */     public void timerExpired(Timer timer)
/* 147:    */     {
/* 148:164 */       Date actualExecutionTime = new Date();
/* 149:165 */       super.timerExpired(timer);
/* 150:166 */       Date completionTime = new Date();
/* 151:167 */       this.triggerContext.update(this.scheduledExecutionTime, actualExecutionTime, completionTime);
/* 152:168 */       if (!this.cancelled) {
/* 153:169 */         schedule();
/* 154:    */       }
/* 155:    */     }
/* 156:    */   }
/* 157:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.commonj.TimerManagerTaskScheduler
 * JD-Core Version:    0.7.0.1
 */