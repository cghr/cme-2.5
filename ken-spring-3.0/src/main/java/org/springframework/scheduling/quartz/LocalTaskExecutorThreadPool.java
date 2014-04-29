/*  1:   */ package org.springframework.scheduling.quartz;
/*  2:   */ 
/*  3:   */ import java.util.concurrent.Executor;
/*  4:   */ import java.util.concurrent.RejectedExecutionException;
/*  5:   */ import org.apache.commons.logging.Log;
/*  6:   */ import org.apache.commons.logging.LogFactory;
/*  7:   */ import org.quartz.SchedulerConfigException;
/*  8:   */ import org.quartz.spi.ThreadPool;
/*  9:   */ 
/* 10:   */ public class LocalTaskExecutorThreadPool
/* 11:   */   implements ThreadPool
/* 12:   */ {
/* 13:38 */   protected final Log logger = LogFactory.getLog(getClass());
/* 14:   */   private Executor taskExecutor;
/* 15:   */   
/* 16:   */   public void setInstanceId(String schedInstId) {}
/* 17:   */   
/* 18:   */   public void setInstanceName(String schedName) {}
/* 19:   */   
/* 20:   */   public void initialize()
/* 21:   */     throws SchedulerConfigException
/* 22:   */   {
/* 23:52 */     this.taskExecutor = SchedulerFactoryBean.getConfigTimeTaskExecutor();
/* 24:53 */     if (this.taskExecutor == null) {
/* 25:54 */       throw new SchedulerConfigException(
/* 26:55 */         "No local TaskExecutor found for configuration - 'taskExecutor' property must be set on SchedulerFactoryBean");
/* 27:   */     }
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void shutdown(boolean waitForJobsToComplete) {}
/* 31:   */   
/* 32:   */   public int getPoolSize()
/* 33:   */   {
/* 34:64 */     return -1;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public boolean runInThread(Runnable runnable)
/* 38:   */   {
/* 39:69 */     if (runnable == null) {
/* 40:70 */       return false;
/* 41:   */     }
/* 42:   */     try
/* 43:   */     {
/* 44:73 */       this.taskExecutor.execute(runnable);
/* 45:74 */       return true;
/* 46:   */     }
/* 47:   */     catch (RejectedExecutionException ex)
/* 48:   */     {
/* 49:77 */       this.logger.error("Task has been rejected by TaskExecutor", ex);
/* 50:   */     }
/* 51:78 */     return false;
/* 52:   */   }
/* 53:   */   
/* 54:   */   public int blockForAvailableThreads()
/* 55:   */   {
/* 56:88 */     return 1;
/* 57:   */   }
/* 58:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.quartz.LocalTaskExecutorThreadPool
 * JD-Core Version:    0.7.0.1
 */