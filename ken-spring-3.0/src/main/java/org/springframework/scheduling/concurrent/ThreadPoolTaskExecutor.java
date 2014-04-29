/*   1:    */ package org.springframework.scheduling.concurrent;
/*   2:    */ 
/*   3:    */ import java.util.concurrent.BlockingQueue;
/*   4:    */ import java.util.concurrent.Callable;
/*   5:    */ import java.util.concurrent.Executor;
/*   6:    */ import java.util.concurrent.ExecutorService;
/*   7:    */ import java.util.concurrent.Future;
/*   8:    */ import java.util.concurrent.LinkedBlockingQueue;
/*   9:    */ import java.util.concurrent.RejectedExecutionException;
/*  10:    */ import java.util.concurrent.RejectedExecutionHandler;
/*  11:    */ import java.util.concurrent.SynchronousQueue;
/*  12:    */ import java.util.concurrent.ThreadFactory;
/*  13:    */ import java.util.concurrent.ThreadPoolExecutor;
/*  14:    */ import java.util.concurrent.TimeUnit;
/*  15:    */ import org.springframework.core.task.TaskRejectedException;
/*  16:    */ import org.springframework.scheduling.SchedulingTaskExecutor;
/*  17:    */ import org.springframework.util.Assert;
/*  18:    */ 
/*  19:    */ public class ThreadPoolTaskExecutor
/*  20:    */   extends ExecutorConfigurationSupport
/*  21:    */   implements SchedulingTaskExecutor
/*  22:    */ {
/*  23: 68 */   private final Object poolSizeMonitor = new Object();
/*  24: 70 */   private int corePoolSize = 1;
/*  25: 72 */   private int maxPoolSize = 2147483647;
/*  26: 74 */   private int keepAliveSeconds = 60;
/*  27: 76 */   private boolean allowCoreThreadTimeOut = false;
/*  28: 78 */   private int queueCapacity = 2147483647;
/*  29:    */   private ThreadPoolExecutor threadPoolExecutor;
/*  30:    */   
/*  31:    */   public void setCorePoolSize(int corePoolSize)
/*  32:    */   {
/*  33: 89 */     synchronized (this.poolSizeMonitor)
/*  34:    */     {
/*  35: 90 */       this.corePoolSize = corePoolSize;
/*  36: 91 */       if (this.threadPoolExecutor != null) {
/*  37: 92 */         this.threadPoolExecutor.setCorePoolSize(corePoolSize);
/*  38:    */       }
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public int getCorePoolSize()
/*  43:    */   {
/*  44:101 */     synchronized (this.poolSizeMonitor)
/*  45:    */     {
/*  46:102 */       return this.corePoolSize;
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setMaxPoolSize(int maxPoolSize)
/*  51:    */   {
/*  52:112 */     synchronized (this.poolSizeMonitor)
/*  53:    */     {
/*  54:113 */       this.maxPoolSize = maxPoolSize;
/*  55:114 */       if (this.threadPoolExecutor != null) {
/*  56:115 */         this.threadPoolExecutor.setMaximumPoolSize(maxPoolSize);
/*  57:    */       }
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   public int getMaxPoolSize()
/*  62:    */   {
/*  63:124 */     synchronized (this.poolSizeMonitor)
/*  64:    */     {
/*  65:125 */       return this.maxPoolSize;
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setKeepAliveSeconds(int keepAliveSeconds)
/*  70:    */   {
/*  71:135 */     synchronized (this.poolSizeMonitor)
/*  72:    */     {
/*  73:136 */       this.keepAliveSeconds = keepAliveSeconds;
/*  74:137 */       if (this.threadPoolExecutor != null) {
/*  75:138 */         this.threadPoolExecutor.setKeepAliveTime(keepAliveSeconds, TimeUnit.SECONDS);
/*  76:    */       }
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   public int getKeepAliveSeconds()
/*  81:    */   {
/*  82:147 */     synchronized (this.poolSizeMonitor)
/*  83:    */     {
/*  84:148 */       return this.keepAliveSeconds;
/*  85:    */     }
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut)
/*  89:    */   {
/*  90:162 */     this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void setQueueCapacity(int queueCapacity)
/*  94:    */   {
/*  95:174 */     this.queueCapacity = queueCapacity;
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler)
/*  99:    */   {
/* 100:181 */     BlockingQueue<Runnable> queue = createQueue(this.queueCapacity);
/* 101:182 */     ThreadPoolExecutor executor = new ThreadPoolExecutor(
/* 102:183 */       this.corePoolSize, this.maxPoolSize, this.keepAliveSeconds, TimeUnit.SECONDS, 
/* 103:184 */       queue, threadFactory, rejectedExecutionHandler);
/* 104:185 */     if (this.allowCoreThreadTimeOut) {
/* 105:186 */       executor.allowCoreThreadTimeOut(true);
/* 106:    */     }
/* 107:189 */     this.threadPoolExecutor = executor;
/* 108:190 */     return executor;
/* 109:    */   }
/* 110:    */   
/* 111:    */   protected BlockingQueue<Runnable> createQueue(int queueCapacity)
/* 112:    */   {
/* 113:203 */     if (queueCapacity > 0) {
/* 114:204 */       return new LinkedBlockingQueue(queueCapacity);
/* 115:    */     }
/* 116:207 */     return new SynchronousQueue();
/* 117:    */   }
/* 118:    */   
/* 119:    */   public ThreadPoolExecutor getThreadPoolExecutor()
/* 120:    */     throws IllegalStateException
/* 121:    */   {
/* 122:217 */     Assert.state(this.threadPoolExecutor != null, "ThreadPoolTaskExecutor not initialized");
/* 123:218 */     return this.threadPoolExecutor;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public int getPoolSize()
/* 127:    */   {
/* 128:226 */     return getThreadPoolExecutor().getPoolSize();
/* 129:    */   }
/* 130:    */   
/* 131:    */   public int getActiveCount()
/* 132:    */   {
/* 133:234 */     return getThreadPoolExecutor().getActiveCount();
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void execute(Runnable task)
/* 137:    */   {
/* 138:239 */     Executor executor = getThreadPoolExecutor();
/* 139:    */     try
/* 140:    */     {
/* 141:241 */       executor.execute(task);
/* 142:    */     }
/* 143:    */     catch (RejectedExecutionException ex)
/* 144:    */     {
/* 145:244 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/* 146:    */     }
/* 147:    */   }
/* 148:    */   
/* 149:    */   public void execute(Runnable task, long startTimeout)
/* 150:    */   {
/* 151:249 */     execute(task);
/* 152:    */   }
/* 153:    */   
/* 154:    */   public Future<?> submit(Runnable task)
/* 155:    */   {
/* 156:253 */     ExecutorService executor = getThreadPoolExecutor();
/* 157:    */     try
/* 158:    */     {
/* 159:255 */       return executor.submit(task);
/* 160:    */     }
/* 161:    */     catch (RejectedExecutionException ex)
/* 162:    */     {
/* 163:258 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/* 164:    */     }
/* 165:    */   }
/* 166:    */   
/* 167:    */   public <T> Future<T> submit(Callable<T> task)
/* 168:    */   {
/* 169:263 */     ExecutorService executor = getThreadPoolExecutor();
/* 170:    */     try
/* 171:    */     {
/* 172:265 */       return executor.submit(task);
/* 173:    */     }
/* 174:    */     catch (RejectedExecutionException ex)
/* 175:    */     {
/* 176:268 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/* 177:    */     }
/* 178:    */   }
/* 179:    */   
/* 180:    */   public boolean prefersShortLivedTasks()
/* 181:    */   {
/* 182:276 */     return true;
/* 183:    */   }
/* 184:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
 * JD-Core Version:    0.7.0.1
 */