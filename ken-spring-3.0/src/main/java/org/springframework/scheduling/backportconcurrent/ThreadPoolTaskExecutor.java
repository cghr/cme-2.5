/*   1:    */ package org.springframework.scheduling.backportconcurrent;
/*   2:    */ 
/*   3:    */ import edu.emory.mathcs.backport.java.util.concurrent.BlockingQueue;
/*   4:    */ import edu.emory.mathcs.backport.java.util.concurrent.Executor;
/*   5:    */ import edu.emory.mathcs.backport.java.util.concurrent.LinkedBlockingQueue;
/*   6:    */ import edu.emory.mathcs.backport.java.util.concurrent.RejectedExecutionException;
/*   7:    */ import edu.emory.mathcs.backport.java.util.concurrent.RejectedExecutionHandler;
/*   8:    */ import edu.emory.mathcs.backport.java.util.concurrent.SynchronousQueue;
/*   9:    */ import edu.emory.mathcs.backport.java.util.concurrent.ThreadFactory;
/*  10:    */ import edu.emory.mathcs.backport.java.util.concurrent.ThreadPoolExecutor;
/*  11:    */ import edu.emory.mathcs.backport.java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
/*  12:    */ import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
/*  13:    */ import java.util.concurrent.Callable;
/*  14:    */ import java.util.concurrent.Future;
/*  15:    */ import java.util.concurrent.FutureTask;
/*  16:    */ import org.apache.commons.logging.Log;
/*  17:    */ import org.apache.commons.logging.LogFactory;
/*  18:    */ import org.springframework.beans.factory.BeanNameAware;
/*  19:    */ import org.springframework.beans.factory.DisposableBean;
/*  20:    */ import org.springframework.beans.factory.InitializingBean;
/*  21:    */ import org.springframework.core.task.TaskRejectedException;
/*  22:    */ import org.springframework.scheduling.SchedulingTaskExecutor;
/*  23:    */ import org.springframework.util.Assert;
/*  24:    */ 
/*  25:    */ public class ThreadPoolTaskExecutor
/*  26:    */   extends CustomizableThreadFactory
/*  27:    */   implements SchedulingTaskExecutor, Executor, BeanNameAware, InitializingBean, DisposableBean
/*  28:    */ {
/*  29: 79 */   protected final Log logger = LogFactory.getLog(getClass());
/*  30: 81 */   private final Object poolSizeMonitor = new Object();
/*  31: 83 */   private int corePoolSize = 1;
/*  32: 85 */   private int maxPoolSize = 2147483647;
/*  33: 87 */   private int keepAliveSeconds = 60;
/*  34: 89 */   private boolean allowCoreThreadTimeOut = false;
/*  35: 91 */   private int queueCapacity = 2147483647;
/*  36: 93 */   private ThreadFactory threadFactory = this;
/*  37: 95 */   private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
/*  38: 97 */   private boolean waitForTasksToCompleteOnShutdown = false;
/*  39: 99 */   private boolean threadNamePrefixSet = false;
/*  40:    */   private String beanName;
/*  41:    */   private ThreadPoolExecutor threadPoolExecutor;
/*  42:    */   
/*  43:    */   public void setCorePoolSize(int corePoolSize)
/*  44:    */   {
/*  45:112 */     synchronized (this.poolSizeMonitor)
/*  46:    */     {
/*  47:113 */       this.corePoolSize = corePoolSize;
/*  48:114 */       if (this.threadPoolExecutor != null) {
/*  49:115 */         this.threadPoolExecutor.setCorePoolSize(corePoolSize);
/*  50:    */       }
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   public int getCorePoolSize()
/*  55:    */   {
/*  56:124 */     synchronized (this.poolSizeMonitor)
/*  57:    */     {
/*  58:125 */       return this.corePoolSize;
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setMaxPoolSize(int maxPoolSize)
/*  63:    */   {
/*  64:135 */     synchronized (this.poolSizeMonitor)
/*  65:    */     {
/*  66:136 */       this.maxPoolSize = maxPoolSize;
/*  67:137 */       if (this.threadPoolExecutor != null) {
/*  68:138 */         this.threadPoolExecutor.setMaximumPoolSize(maxPoolSize);
/*  69:    */       }
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   public int getMaxPoolSize()
/*  74:    */   {
/*  75:147 */     synchronized (this.poolSizeMonitor)
/*  76:    */     {
/*  77:148 */       return this.maxPoolSize;
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setKeepAliveSeconds(int keepAliveSeconds)
/*  82:    */   {
/*  83:158 */     synchronized (this.poolSizeMonitor)
/*  84:    */     {
/*  85:159 */       this.keepAliveSeconds = keepAliveSeconds;
/*  86:160 */       if (this.threadPoolExecutor != null) {
/*  87:161 */         this.threadPoolExecutor.setKeepAliveTime(keepAliveSeconds, TimeUnit.SECONDS);
/*  88:    */       }
/*  89:    */     }
/*  90:    */   }
/*  91:    */   
/*  92:    */   public int getKeepAliveSeconds()
/*  93:    */   {
/*  94:170 */     synchronized (this.poolSizeMonitor)
/*  95:    */     {
/*  96:171 */       return this.keepAliveSeconds;
/*  97:    */     }
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut)
/* 101:    */   {
/* 102:184 */     this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setQueueCapacity(int queueCapacity)
/* 106:    */   {
/* 107:196 */     this.queueCapacity = queueCapacity;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setThreadFactory(ThreadFactory threadFactory)
/* 111:    */   {
/* 112:208 */     this.threadFactory = (threadFactory != null ? threadFactory : this);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler)
/* 116:    */   {
/* 117:217 */     this.rejectedExecutionHandler = 
/* 118:218 */       (rejectedExecutionHandler != null ? rejectedExecutionHandler : new ThreadPoolExecutor.AbortPolicy());
/* 119:    */   }
/* 120:    */   
/* 121:    */   public void setWaitForTasksToCompleteOnShutdown(boolean waitForJobsToCompleteOnShutdown)
/* 122:    */   {
/* 123:229 */     this.waitForTasksToCompleteOnShutdown = waitForJobsToCompleteOnShutdown;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public void setThreadNamePrefix(String threadNamePrefix)
/* 127:    */   {
/* 128:234 */     super.setThreadNamePrefix(threadNamePrefix);
/* 129:235 */     this.threadNamePrefixSet = true;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public void setBeanName(String name)
/* 133:    */   {
/* 134:239 */     this.beanName = name;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public void afterPropertiesSet()
/* 138:    */   {
/* 139:248 */     initialize();
/* 140:    */   }
/* 141:    */   
/* 142:    */   public void initialize()
/* 143:    */   {
/* 144:256 */     if (this.logger.isInfoEnabled()) {
/* 145:257 */       this.logger.info("Initializing ThreadPoolExecutor" + (this.beanName != null ? " '" + this.beanName + "'" : ""));
/* 146:    */     }
/* 147:259 */     if ((!this.threadNamePrefixSet) && (this.beanName != null)) {
/* 148:260 */       setThreadNamePrefix(this.beanName + "-");
/* 149:    */     }
/* 150:262 */     BlockingQueue queue = createQueue(this.queueCapacity);
/* 151:263 */     this.threadPoolExecutor = new ThreadPoolExecutor(
/* 152:264 */       this.corePoolSize, this.maxPoolSize, this.keepAliveSeconds, TimeUnit.SECONDS, 
/* 153:265 */       queue, this.threadFactory, this.rejectedExecutionHandler);
/* 154:266 */     if (this.allowCoreThreadTimeOut) {
/* 155:267 */       this.threadPoolExecutor.allowCoreThreadTimeOut(true);
/* 156:    */     }
/* 157:    */   }
/* 158:    */   
/* 159:    */   protected BlockingQueue createQueue(int queueCapacity)
/* 160:    */   {
/* 161:281 */     if (queueCapacity > 0) {
/* 162:282 */       return new LinkedBlockingQueue(queueCapacity);
/* 163:    */     }
/* 164:285 */     return new SynchronousQueue();
/* 165:    */   }
/* 166:    */   
/* 167:    */   public ThreadPoolExecutor getThreadPoolExecutor()
/* 168:    */     throws IllegalStateException
/* 169:    */   {
/* 170:295 */     Assert.state(this.threadPoolExecutor != null, "ThreadPoolTaskExecutor not initialized");
/* 171:296 */     return this.threadPoolExecutor;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public void execute(Runnable task)
/* 175:    */   {
/* 176:307 */     Executor executor = getThreadPoolExecutor();
/* 177:    */     try
/* 178:    */     {
/* 179:309 */       executor.execute(task);
/* 180:    */     }
/* 181:    */     catch (RejectedExecutionException ex)
/* 182:    */     {
/* 183:312 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/* 184:    */     }
/* 185:    */   }
/* 186:    */   
/* 187:    */   public void execute(Runnable task, long startTimeout)
/* 188:    */   {
/* 189:317 */     execute(task);
/* 190:    */   }
/* 191:    */   
/* 192:    */   public Future<?> submit(Runnable task)
/* 193:    */   {
/* 194:321 */     FutureTask<Object> future = new FutureTask(task, null);
/* 195:322 */     execute(future);
/* 196:323 */     return future;
/* 197:    */   }
/* 198:    */   
/* 199:    */   public <T> Future<T> submit(Callable<T> task)
/* 200:    */   {
/* 201:327 */     FutureTask<T> future = new FutureTask(task);
/* 202:328 */     execute(future);
/* 203:329 */     return future;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public boolean prefersShortLivedTasks()
/* 207:    */   {
/* 208:336 */     return true;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public int getPoolSize()
/* 212:    */   {
/* 213:345 */     return getThreadPoolExecutor().getPoolSize();
/* 214:    */   }
/* 215:    */   
/* 216:    */   public int getActiveCount()
/* 217:    */   {
/* 218:353 */     return getThreadPoolExecutor().getActiveCount();
/* 219:    */   }
/* 220:    */   
/* 221:    */   public void destroy()
/* 222:    */   {
/* 223:363 */     shutdown();
/* 224:    */   }
/* 225:    */   
/* 226:    */   public void shutdown()
/* 227:    */   {
/* 228:371 */     if (this.logger.isInfoEnabled()) {
/* 229:372 */       this.logger.info("Shutting down ThreadPoolExecutor" + (this.beanName != null ? " '" + this.beanName + "'" : ""));
/* 230:    */     }
/* 231:374 */     if (this.waitForTasksToCompleteOnShutdown) {
/* 232:375 */       this.threadPoolExecutor.shutdown();
/* 233:    */     } else {
/* 234:378 */       this.threadPoolExecutor.shutdownNow();
/* 235:    */     }
/* 236:    */   }
/* 237:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.backportconcurrent.ThreadPoolTaskExecutor
 * JD-Core Version:    0.7.0.1
 */