/*   1:    */ package org.springframework.scheduling.concurrent;
/*   2:    */ 
/*   3:    */ import java.util.concurrent.ExecutorService;
/*   4:    */ import java.util.concurrent.RejectedExecutionHandler;
/*   5:    */ import java.util.concurrent.ThreadFactory;
/*   6:    */ import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.springframework.beans.factory.BeanNameAware;
/*  10:    */ import org.springframework.beans.factory.DisposableBean;
/*  11:    */ import org.springframework.beans.factory.InitializingBean;
/*  12:    */ 
/*  13:    */ public abstract class ExecutorConfigurationSupport
/*  14:    */   extends CustomizableThreadFactory
/*  15:    */   implements BeanNameAware, InitializingBean, DisposableBean
/*  16:    */ {
/*  17: 46 */   protected final Log logger = LogFactory.getLog(getClass());
/*  18: 48 */   private ThreadFactory threadFactory = this;
/*  19: 50 */   private boolean threadNamePrefixSet = false;
/*  20: 52 */   private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
/*  21: 54 */   private boolean waitForTasksToCompleteOnShutdown = false;
/*  22:    */   private String beanName;
/*  23:    */   private ExecutorService executor;
/*  24:    */   
/*  25:    */   public void setThreadFactory(ThreadFactory threadFactory)
/*  26:    */   {
/*  27: 67 */     this.threadFactory = (threadFactory != null ? threadFactory : this);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setThreadNamePrefix(String threadNamePrefix)
/*  31:    */   {
/*  32: 72 */     super.setThreadNamePrefix(threadNamePrefix);
/*  33: 73 */     this.threadNamePrefixSet = true;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler)
/*  37:    */   {
/*  38: 82 */     this.rejectedExecutionHandler = 
/*  39: 83 */       (rejectedExecutionHandler != null ? rejectedExecutionHandler : new ThreadPoolExecutor.AbortPolicy());
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setWaitForTasksToCompleteOnShutdown(boolean waitForJobsToCompleteOnShutdown)
/*  43:    */   {
/*  44: 94 */     this.waitForTasksToCompleteOnShutdown = waitForJobsToCompleteOnShutdown;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setBeanName(String name)
/*  48:    */   {
/*  49: 98 */     this.beanName = name;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void afterPropertiesSet()
/*  53:    */   {
/*  54:107 */     initialize();
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void initialize()
/*  58:    */   {
/*  59:114 */     if (this.logger.isInfoEnabled()) {
/*  60:115 */       this.logger.info("Initializing ExecutorService " + (this.beanName != null ? " '" + this.beanName + "'" : ""));
/*  61:    */     }
/*  62:117 */     if ((!this.threadNamePrefixSet) && (this.beanName != null)) {
/*  63:118 */       setThreadNamePrefix(this.beanName + "-");
/*  64:    */     }
/*  65:120 */     this.executor = initializeExecutor(this.threadFactory, this.rejectedExecutionHandler);
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected abstract ExecutorService initializeExecutor(ThreadFactory paramThreadFactory, RejectedExecutionHandler paramRejectedExecutionHandler);
/*  69:    */   
/*  70:    */   public void destroy()
/*  71:    */   {
/*  72:141 */     shutdown();
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void shutdown()
/*  76:    */   {
/*  77:149 */     if (this.logger.isInfoEnabled()) {
/*  78:150 */       this.logger.info("Shutting down ExecutorService" + (this.beanName != null ? " '" + this.beanName + "'" : ""));
/*  79:    */     }
/*  80:152 */     if (this.waitForTasksToCompleteOnShutdown) {
/*  81:153 */       this.executor.shutdown();
/*  82:    */     } else {
/*  83:156 */       this.executor.shutdownNow();
/*  84:    */     }
/*  85:    */   }
/*  86:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.concurrent.ExecutorConfigurationSupport
 * JD-Core Version:    0.7.0.1
 */