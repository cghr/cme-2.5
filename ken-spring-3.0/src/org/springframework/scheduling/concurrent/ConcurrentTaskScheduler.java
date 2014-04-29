/*   1:    */ package org.springframework.scheduling.concurrent;
/*   2:    */ 
/*   3:    */ import java.util.Date;
/*   4:    */ import java.util.concurrent.Executor;
/*   5:    */ import java.util.concurrent.Executors;
/*   6:    */ import java.util.concurrent.RejectedExecutionException;
/*   7:    */ import java.util.concurrent.ScheduledExecutorService;
/*   8:    */ import java.util.concurrent.ScheduledFuture;
/*   9:    */ import java.util.concurrent.TimeUnit;
/*  10:    */ import org.springframework.core.task.TaskRejectedException;
/*  11:    */ import org.springframework.scheduling.TaskScheduler;
/*  12:    */ import org.springframework.scheduling.Trigger;
/*  13:    */ import org.springframework.scheduling.support.TaskUtils;
/*  14:    */ import org.springframework.util.Assert;
/*  15:    */ import org.springframework.util.ErrorHandler;
/*  16:    */ 
/*  17:    */ public class ConcurrentTaskScheduler
/*  18:    */   extends ConcurrentTaskExecutor
/*  19:    */   implements TaskScheduler
/*  20:    */ {
/*  21:    */   private volatile ScheduledExecutorService scheduledExecutor;
/*  22:    */   private volatile ErrorHandler errorHandler;
/*  23:    */   
/*  24:    */   public ConcurrentTaskScheduler()
/*  25:    */   {
/*  26: 68 */     setScheduledExecutor(null);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public ConcurrentTaskScheduler(ScheduledExecutorService scheduledExecutor)
/*  30:    */   {
/*  31: 79 */     super(scheduledExecutor);
/*  32: 80 */     setScheduledExecutor(scheduledExecutor);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public ConcurrentTaskScheduler(Executor concurrentExecutor, ScheduledExecutorService scheduledExecutor)
/*  36:    */   {
/*  37: 92 */     super(concurrentExecutor);
/*  38: 93 */     setScheduledExecutor(scheduledExecutor);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public final void setScheduledExecutor(ScheduledExecutorService scheduledExecutor)
/*  42:    */   {
/*  43:106 */     this.scheduledExecutor = 
/*  44:107 */       (scheduledExecutor != null ? scheduledExecutor : Executors.newSingleThreadScheduledExecutor());
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setErrorHandler(ErrorHandler errorHandler)
/*  48:    */   {
/*  49:114 */     Assert.notNull(errorHandler, "'errorHandler' must not be null");
/*  50:115 */     this.errorHandler = errorHandler;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public ScheduledFuture schedule(Runnable task, Trigger trigger)
/*  54:    */   {
/*  55:    */     try
/*  56:    */     {
/*  57:121 */       ErrorHandler errorHandler = 
/*  58:122 */         this.errorHandler != null ? this.errorHandler : TaskUtils.getDefaultErrorHandler(true);
/*  59:123 */       return new ReschedulingRunnable(task, trigger, this.scheduledExecutor, errorHandler).schedule();
/*  60:    */     }
/*  61:    */     catch (RejectedExecutionException ex)
/*  62:    */     {
/*  63:126 */       throw new TaskRejectedException("Executor [" + this.scheduledExecutor + "] did not accept task: " + task, ex);
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   public ScheduledFuture schedule(Runnable task, Date startTime)
/*  68:    */   {
/*  69:131 */     long initialDelay = startTime.getTime() - System.currentTimeMillis();
/*  70:    */     try
/*  71:    */     {
/*  72:133 */       return this.scheduledExecutor.schedule(
/*  73:134 */         errorHandlingTask(task, false), initialDelay, TimeUnit.MILLISECONDS);
/*  74:    */     }
/*  75:    */     catch (RejectedExecutionException ex)
/*  76:    */     {
/*  77:137 */       throw new TaskRejectedException("Executor [" + this.scheduledExecutor + "] did not accept task: " + task, ex);
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   public ScheduledFuture scheduleAtFixedRate(Runnable task, Date startTime, long period)
/*  82:    */   {
/*  83:142 */     long initialDelay = startTime.getTime() - System.currentTimeMillis();
/*  84:    */     try
/*  85:    */     {
/*  86:144 */       return this.scheduledExecutor.scheduleAtFixedRate(
/*  87:145 */         errorHandlingTask(task, true), initialDelay, period, TimeUnit.MILLISECONDS);
/*  88:    */     }
/*  89:    */     catch (RejectedExecutionException ex)
/*  90:    */     {
/*  91:148 */       throw new TaskRejectedException("Executor [" + this.scheduledExecutor + "] did not accept task: " + task, ex);
/*  92:    */     }
/*  93:    */   }
/*  94:    */   
/*  95:    */   public ScheduledFuture scheduleAtFixedRate(Runnable task, long period)
/*  96:    */   {
/*  97:    */     try
/*  98:    */     {
/*  99:154 */       return this.scheduledExecutor.scheduleAtFixedRate(
/* 100:155 */         errorHandlingTask(task, true), 0L, period, TimeUnit.MILLISECONDS);
/* 101:    */     }
/* 102:    */     catch (RejectedExecutionException ex)
/* 103:    */     {
/* 104:158 */       throw new TaskRejectedException("Executor [" + this.scheduledExecutor + "] did not accept task: " + task, ex);
/* 105:    */     }
/* 106:    */   }
/* 107:    */   
/* 108:    */   public ScheduledFuture scheduleWithFixedDelay(Runnable task, Date startTime, long delay)
/* 109:    */   {
/* 110:163 */     long initialDelay = startTime.getTime() - System.currentTimeMillis();
/* 111:    */     try
/* 112:    */     {
/* 113:165 */       return this.scheduledExecutor.scheduleWithFixedDelay(
/* 114:166 */         errorHandlingTask(task, true), initialDelay, delay, TimeUnit.MILLISECONDS);
/* 115:    */     }
/* 116:    */     catch (RejectedExecutionException ex)
/* 117:    */     {
/* 118:169 */       throw new TaskRejectedException("Executor [" + this.scheduledExecutor + "] did not accept task: " + task, ex);
/* 119:    */     }
/* 120:    */   }
/* 121:    */   
/* 122:    */   public ScheduledFuture scheduleWithFixedDelay(Runnable task, long delay)
/* 123:    */   {
/* 124:    */     try
/* 125:    */     {
/* 126:175 */       return this.scheduledExecutor.scheduleWithFixedDelay(
/* 127:176 */         errorHandlingTask(task, true), 0L, delay, TimeUnit.MILLISECONDS);
/* 128:    */     }
/* 129:    */     catch (RejectedExecutionException ex)
/* 130:    */     {
/* 131:179 */       throw new TaskRejectedException("Executor [" + this.scheduledExecutor + "] did not accept task: " + task, ex);
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   private Runnable errorHandlingTask(Runnable task, boolean isRepeatingTask)
/* 136:    */   {
/* 137:184 */     return TaskUtils.decorateTaskWithErrorHandler(task, this.errorHandler, isRepeatingTask);
/* 138:    */   }
/* 139:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.concurrent.ConcurrentTaskScheduler
 * JD-Core Version:    0.7.0.1
 */