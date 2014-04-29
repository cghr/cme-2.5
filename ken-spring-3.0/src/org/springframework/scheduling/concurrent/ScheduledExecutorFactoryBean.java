/*   1:    */ package org.springframework.scheduling.concurrent;
/*   2:    */ 
/*   3:    */ import java.util.concurrent.ExecutorService;
/*   4:    */ import java.util.concurrent.Executors;
/*   5:    */ import java.util.concurrent.RejectedExecutionHandler;
/*   6:    */ import java.util.concurrent.ScheduledExecutorService;
/*   7:    */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*   8:    */ import java.util.concurrent.ThreadFactory;
/*   9:    */ import org.springframework.beans.factory.FactoryBean;
/*  10:    */ import org.springframework.scheduling.support.DelegatingErrorHandlingRunnable;
/*  11:    */ import org.springframework.scheduling.support.TaskUtils;
/*  12:    */ import org.springframework.util.Assert;
/*  13:    */ import org.springframework.util.ObjectUtils;
/*  14:    */ 
/*  15:    */ public class ScheduledExecutorFactoryBean
/*  16:    */   extends ExecutorConfigurationSupport
/*  17:    */   implements FactoryBean<ScheduledExecutorService>
/*  18:    */ {
/*  19: 66 */   private int poolSize = 1;
/*  20:    */   private ScheduledExecutorTask[] scheduledExecutorTasks;
/*  21: 70 */   private boolean continueScheduledExecutionAfterException = false;
/*  22: 72 */   private boolean exposeUnconfigurableExecutor = false;
/*  23:    */   private ScheduledExecutorService exposedExecutor;
/*  24:    */   
/*  25:    */   public void setPoolSize(int poolSize)
/*  26:    */   {
/*  27: 82 */     Assert.isTrue(poolSize > 0, "'poolSize' must be 1 or higher");
/*  28: 83 */     this.poolSize = poolSize;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setScheduledExecutorTasks(ScheduledExecutorTask[] scheduledExecutorTasks)
/*  32:    */   {
/*  33: 95 */     this.scheduledExecutorTasks = scheduledExecutorTasks;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setContinueScheduledExecutionAfterException(boolean continueScheduledExecutionAfterException)
/*  37:    */   {
/*  38:108 */     this.continueScheduledExecutionAfterException = continueScheduledExecutionAfterException;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setExposeUnconfigurableExecutor(boolean exposeUnconfigurableExecutor)
/*  42:    */   {
/*  43:120 */     this.exposeUnconfigurableExecutor = exposeUnconfigurableExecutor;
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler)
/*  47:    */   {
/*  48:127 */     ScheduledExecutorService executor = 
/*  49:128 */       createExecutor(this.poolSize, threadFactory, rejectedExecutionHandler);
/*  50:131 */     if (!ObjectUtils.isEmpty(this.scheduledExecutorTasks)) {
/*  51:132 */       registerTasks(this.scheduledExecutorTasks, executor);
/*  52:    */     }
/*  53:136 */     this.exposedExecutor = (this.exposeUnconfigurableExecutor ? 
/*  54:137 */       Executors.unconfigurableScheduledExecutorService(executor) : executor);
/*  55:    */     
/*  56:139 */     return executor;
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected ScheduledExecutorService createExecutor(int poolSize, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler)
/*  60:    */   {
/*  61:156 */     return new ScheduledThreadPoolExecutor(poolSize, threadFactory, rejectedExecutionHandler);
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected void registerTasks(ScheduledExecutorTask[] tasks, ScheduledExecutorService executor)
/*  65:    */   {
/*  66:166 */     for (ScheduledExecutorTask task : tasks)
/*  67:    */     {
/*  68:167 */       Runnable runnable = getRunnableToSchedule(task);
/*  69:168 */       if (task.isOneTimeTask()) {
/*  70:169 */         executor.schedule(runnable, task.getDelay(), task.getTimeUnit());
/*  71:172 */       } else if (task.isFixedRate()) {
/*  72:173 */         executor.scheduleAtFixedRate(runnable, task.getDelay(), task.getPeriod(), task.getTimeUnit());
/*  73:    */       } else {
/*  74:176 */         executor.scheduleWithFixedDelay(runnable, task.getDelay(), task.getPeriod(), task.getTimeUnit());
/*  75:    */       }
/*  76:    */     }
/*  77:    */   }
/*  78:    */   
/*  79:    */   protected Runnable getRunnableToSchedule(ScheduledExecutorTask task)
/*  80:    */   {
/*  81:194 */     return this.continueScheduledExecutionAfterException ? 
/*  82:195 */       new DelegatingErrorHandlingRunnable(task.getRunnable(), TaskUtils.LOG_AND_SUPPRESS_ERROR_HANDLER) : 
/*  83:196 */       new DelegatingErrorHandlingRunnable(task.getRunnable(), TaskUtils.LOG_AND_PROPAGATE_ERROR_HANDLER);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public ScheduledExecutorService getObject()
/*  87:    */   {
/*  88:201 */     return this.exposedExecutor;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public Class<? extends ScheduledExecutorService> getObjectType()
/*  92:    */   {
/*  93:205 */     return this.exposedExecutor != null ? this.exposedExecutor.getClass() : ScheduledExecutorService.class;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public boolean isSingleton()
/*  97:    */   {
/*  98:209 */     return true;
/*  99:    */   }
/* 100:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean
 * JD-Core Version:    0.7.0.1
 */