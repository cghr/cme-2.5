/*   1:    */ package org.springframework.scheduling.concurrent;
/*   2:    */ 
/*   3:    */ import java.util.concurrent.BlockingQueue;
/*   4:    */ import java.util.concurrent.ExecutorService;
/*   5:    */ import java.util.concurrent.Executors;
/*   6:    */ import java.util.concurrent.LinkedBlockingQueue;
/*   7:    */ import java.util.concurrent.RejectedExecutionHandler;
/*   8:    */ import java.util.concurrent.SynchronousQueue;
/*   9:    */ import java.util.concurrent.ThreadFactory;
/*  10:    */ import java.util.concurrent.ThreadPoolExecutor;
/*  11:    */ import java.util.concurrent.TimeUnit;
/*  12:    */ import org.springframework.beans.factory.DisposableBean;
/*  13:    */ import org.springframework.beans.factory.FactoryBean;
/*  14:    */ import org.springframework.beans.factory.InitializingBean;
/*  15:    */ 
/*  16:    */ public class ThreadPoolExecutorFactoryBean
/*  17:    */   extends ExecutorConfigurationSupport
/*  18:    */   implements FactoryBean<ExecutorService>, InitializingBean, DisposableBean
/*  19:    */ {
/*  20: 55 */   private int corePoolSize = 1;
/*  21: 57 */   private int maxPoolSize = 2147483647;
/*  22: 59 */   private int keepAliveSeconds = 60;
/*  23: 61 */   private boolean allowCoreThreadTimeOut = false;
/*  24: 63 */   private int queueCapacity = 2147483647;
/*  25: 65 */   private boolean exposeUnconfigurableExecutor = false;
/*  26:    */   private ExecutorService exposedExecutor;
/*  27:    */   
/*  28:    */   public void setCorePoolSize(int corePoolSize)
/*  29:    */   {
/*  30: 76 */     this.corePoolSize = corePoolSize;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setMaxPoolSize(int maxPoolSize)
/*  34:    */   {
/*  35: 85 */     this.maxPoolSize = maxPoolSize;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setKeepAliveSeconds(int keepAliveSeconds)
/*  39:    */   {
/*  40: 94 */     this.keepAliveSeconds = keepAliveSeconds;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut)
/*  44:    */   {
/*  45:107 */     this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setQueueCapacity(int queueCapacity)
/*  49:    */   {
/*  50:119 */     this.queueCapacity = queueCapacity;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setExposeUnconfigurableExecutor(boolean exposeUnconfigurableExecutor)
/*  54:    */   {
/*  55:131 */     this.exposeUnconfigurableExecutor = exposeUnconfigurableExecutor;
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler)
/*  59:    */   {
/*  60:138 */     BlockingQueue<Runnable> queue = createQueue(this.queueCapacity);
/*  61:139 */     ThreadPoolExecutor executor = new ThreadPoolExecutor(
/*  62:140 */       this.corePoolSize, this.maxPoolSize, this.keepAliveSeconds, TimeUnit.SECONDS, 
/*  63:141 */       queue, threadFactory, rejectedExecutionHandler);
/*  64:142 */     if (this.allowCoreThreadTimeOut) {
/*  65:143 */       executor.allowCoreThreadTimeOut(true);
/*  66:    */     }
/*  67:147 */     this.exposedExecutor = (this.exposeUnconfigurableExecutor ? 
/*  68:148 */       Executors.unconfigurableExecutorService(executor) : executor);
/*  69:    */     
/*  70:150 */     return executor;
/*  71:    */   }
/*  72:    */   
/*  73:    */   protected BlockingQueue<Runnable> createQueue(int queueCapacity)
/*  74:    */   {
/*  75:163 */     if (queueCapacity > 0) {
/*  76:164 */       return new LinkedBlockingQueue(queueCapacity);
/*  77:    */     }
/*  78:167 */     return new SynchronousQueue();
/*  79:    */   }
/*  80:    */   
/*  81:    */   public ExecutorService getObject()
/*  82:    */     throws Exception
/*  83:    */   {
/*  84:173 */     return this.exposedExecutor;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public Class<? extends ExecutorService> getObjectType()
/*  88:    */   {
/*  89:177 */     return this.exposedExecutor != null ? this.exposedExecutor.getClass() : ExecutorService.class;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public boolean isSingleton()
/*  93:    */   {
/*  94:181 */     return true;
/*  95:    */   }
/*  96:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean
 * JD-Core Version:    0.7.0.1
 */