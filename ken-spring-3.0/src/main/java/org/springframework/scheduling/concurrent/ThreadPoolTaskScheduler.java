/*   1:    */ package org.springframework.scheduling.concurrent;
/*   2:    */ 
/*   3:    */ import java.util.Date;
/*   4:    */ import java.util.concurrent.Callable;
/*   5:    */ import java.util.concurrent.Executor;
/*   6:    */ import java.util.concurrent.ExecutorService;
/*   7:    */ import java.util.concurrent.Future;
/*   8:    */ import java.util.concurrent.RejectedExecutionException;
/*   9:    */ import java.util.concurrent.RejectedExecutionHandler;
/*  10:    */ import java.util.concurrent.ScheduledExecutorService;
/*  11:    */ import java.util.concurrent.ScheduledFuture;
/*  12:    */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*  13:    */ import java.util.concurrent.ThreadFactory;
/*  14:    */ import java.util.concurrent.TimeUnit;
/*  15:    */ import org.springframework.core.task.TaskRejectedException;
/*  16:    */ import org.springframework.scheduling.SchedulingTaskExecutor;
/*  17:    */ import org.springframework.scheduling.TaskScheduler;
/*  18:    */ import org.springframework.scheduling.Trigger;
/*  19:    */ import org.springframework.scheduling.support.TaskUtils;
/*  20:    */ import org.springframework.util.Assert;
/*  21:    */ import org.springframework.util.ErrorHandler;
/*  22:    */ 
/*  23:    */ public class ThreadPoolTaskScheduler
/*  24:    */   extends ExecutorConfigurationSupport
/*  25:    */   implements TaskScheduler, SchedulingTaskExecutor
/*  26:    */ {
/*  27: 54 */   private volatile int poolSize = 1;
/*  28:    */   private volatile ScheduledExecutorService scheduledExecutor;
/*  29:    */   private volatile ErrorHandler errorHandler;
/*  30:    */   
/*  31:    */   public void setPoolSize(int poolSize)
/*  32:    */   {
/*  33: 66 */     Assert.isTrue(poolSize > 0, "'poolSize' must be 1 or higher");
/*  34: 67 */     this.poolSize = poolSize;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setErrorHandler(ErrorHandler errorHandler)
/*  38:    */   {
/*  39: 74 */     Assert.notNull(errorHandler, "'errorHandler' must not be null");
/*  40: 75 */     this.errorHandler = errorHandler;
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler)
/*  44:    */   {
/*  45: 81 */     this.scheduledExecutor = createExecutor(this.poolSize, threadFactory, rejectedExecutionHandler);
/*  46: 82 */     return this.scheduledExecutor;
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected ScheduledExecutorService createExecutor(int poolSize, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler)
/*  50:    */   {
/*  51: 99 */     return new ScheduledThreadPoolExecutor(poolSize, threadFactory, rejectedExecutionHandler);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public ScheduledExecutorService getScheduledExecutor()
/*  55:    */     throws IllegalStateException
/*  56:    */   {
/*  57:108 */     Assert.state(this.scheduledExecutor != null, "ThreadPoolTaskScheduler not initialized");
/*  58:109 */     return this.scheduledExecutor;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void execute(Runnable task)
/*  62:    */   {
/*  63:116 */     Executor executor = getScheduledExecutor();
/*  64:    */     try
/*  65:    */     {
/*  66:118 */       executor.execute(errorHandlingTask(task, false));
/*  67:    */     }
/*  68:    */     catch (RejectedExecutionException ex)
/*  69:    */     {
/*  70:121 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void execute(Runnable task, long startTimeout)
/*  75:    */   {
/*  76:126 */     execute(task);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public Future<?> submit(Runnable task)
/*  80:    */   {
/*  81:130 */     ExecutorService executor = getScheduledExecutor();
/*  82:    */     try
/*  83:    */     {
/*  84:132 */       return executor.submit(errorHandlingTask(task, false));
/*  85:    */     }
/*  86:    */     catch (RejectedExecutionException ex)
/*  87:    */     {
/*  88:135 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*  89:    */     }
/*  90:    */   }
/*  91:    */   
/*  92:    */   public <T> Future<T> submit(Callable<T> task)
/*  93:    */   {
/*  94:140 */     ExecutorService executor = getScheduledExecutor();
/*  95:    */     try
/*  96:    */     {
/*  97:142 */       if (this.errorHandler != null) {
/*  98:143 */         task = new DelegatingErrorHandlingCallable(task, this.errorHandler);
/*  99:    */       }
/* 100:145 */       return executor.submit(task);
/* 101:    */     }
/* 102:    */     catch (RejectedExecutionException ex)
/* 103:    */     {
/* 104:148 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/* 105:    */     }
/* 106:    */   }
/* 107:    */   
/* 108:    */   public boolean prefersShortLivedTasks()
/* 109:    */   {
/* 110:153 */     return true;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public ScheduledFuture schedule(Runnable task, Trigger trigger)
/* 114:    */   {
/* 115:160 */     ScheduledExecutorService executor = getScheduledExecutor();
/* 116:    */     try
/* 117:    */     {
/* 118:162 */       ErrorHandler errorHandler = 
/* 119:163 */         this.errorHandler != null ? this.errorHandler : TaskUtils.getDefaultErrorHandler(true);
/* 120:164 */       return new ReschedulingRunnable(task, trigger, executor, errorHandler).schedule();
/* 121:    */     }
/* 122:    */     catch (RejectedExecutionException ex)
/* 123:    */     {
/* 124:167 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/* 125:    */     }
/* 126:    */   }
/* 127:    */   
/* 128:    */   public ScheduledFuture schedule(Runnable task, Date startTime)
/* 129:    */   {
/* 130:172 */     ScheduledExecutorService executor = getScheduledExecutor();
/* 131:173 */     long initialDelay = startTime.getTime() - System.currentTimeMillis();
/* 132:    */     try
/* 133:    */     {
/* 134:175 */       return executor.schedule(errorHandlingTask(task, false), initialDelay, TimeUnit.MILLISECONDS);
/* 135:    */     }
/* 136:    */     catch (RejectedExecutionException ex)
/* 137:    */     {
/* 138:178 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/* 139:    */     }
/* 140:    */   }
/* 141:    */   
/* 142:    */   public ScheduledFuture scheduleAtFixedRate(Runnable task, Date startTime, long period)
/* 143:    */   {
/* 144:183 */     ScheduledExecutorService executor = getScheduledExecutor();
/* 145:184 */     long initialDelay = startTime.getTime() - System.currentTimeMillis();
/* 146:    */     try
/* 147:    */     {
/* 148:186 */       return executor.scheduleAtFixedRate(errorHandlingTask(task, true), initialDelay, period, TimeUnit.MILLISECONDS);
/* 149:    */     }
/* 150:    */     catch (RejectedExecutionException ex)
/* 151:    */     {
/* 152:189 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/* 153:    */     }
/* 154:    */   }
/* 155:    */   
/* 156:    */   public ScheduledFuture scheduleAtFixedRate(Runnable task, long period)
/* 157:    */   {
/* 158:194 */     ScheduledExecutorService executor = getScheduledExecutor();
/* 159:    */     try
/* 160:    */     {
/* 161:196 */       return executor.scheduleAtFixedRate(errorHandlingTask(task, true), 0L, period, TimeUnit.MILLISECONDS);
/* 162:    */     }
/* 163:    */     catch (RejectedExecutionException ex)
/* 164:    */     {
/* 165:199 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/* 166:    */     }
/* 167:    */   }
/* 168:    */   
/* 169:    */   public ScheduledFuture scheduleWithFixedDelay(Runnable task, Date startTime, long delay)
/* 170:    */   {
/* 171:204 */     ScheduledExecutorService executor = getScheduledExecutor();
/* 172:205 */     long initialDelay = startTime.getTime() - System.currentTimeMillis();
/* 173:    */     try
/* 174:    */     {
/* 175:207 */       return executor.scheduleWithFixedDelay(errorHandlingTask(task, true), initialDelay, delay, TimeUnit.MILLISECONDS);
/* 176:    */     }
/* 177:    */     catch (RejectedExecutionException ex)
/* 178:    */     {
/* 179:210 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/* 180:    */     }
/* 181:    */   }
/* 182:    */   
/* 183:    */   public ScheduledFuture scheduleWithFixedDelay(Runnable task, long delay)
/* 184:    */   {
/* 185:215 */     ScheduledExecutorService executor = getScheduledExecutor();
/* 186:    */     try
/* 187:    */     {
/* 188:217 */       return executor.scheduleWithFixedDelay(errorHandlingTask(task, true), 0L, delay, TimeUnit.MILLISECONDS);
/* 189:    */     }
/* 190:    */     catch (RejectedExecutionException ex)
/* 191:    */     {
/* 192:220 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/* 193:    */     }
/* 194:    */   }
/* 195:    */   
/* 196:    */   private Runnable errorHandlingTask(Runnable task, boolean isRepeatingTask)
/* 197:    */   {
/* 198:225 */     return TaskUtils.decorateTaskWithErrorHandler(task, this.errorHandler, isRepeatingTask);
/* 199:    */   }
/* 200:    */   
/* 201:    */   private static class DelegatingErrorHandlingCallable<V>
/* 202:    */     implements Callable<V>
/* 203:    */   {
/* 204:    */     private final Callable<V> delegate;
/* 205:    */     private final ErrorHandler errorHandler;
/* 206:    */     
/* 207:    */     DelegatingErrorHandlingCallable(Callable<V> delegate, ErrorHandler errorHandler)
/* 208:    */     {
/* 209:236 */       this.delegate = delegate;
/* 210:237 */       this.errorHandler = errorHandler;
/* 211:    */     }
/* 212:    */     
/* 213:    */     public V call()
/* 214:    */       throws Exception
/* 215:    */     {
/* 216:    */       try
/* 217:    */       {
/* 218:242 */         return this.delegate.call();
/* 219:    */       }
/* 220:    */       catch (Throwable t)
/* 221:    */       {
/* 222:245 */         this.errorHandler.handleError(t);
/* 223:    */       }
/* 224:246 */       return null;
/* 225:    */     }
/* 226:    */   }
/* 227:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
 * JD-Core Version:    0.7.0.1
 */