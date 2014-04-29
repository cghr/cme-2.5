/*   1:    */ package org.springframework.scheduling.commonj;
/*   2:    */ 
/*   3:    */ import commonj.work.Work;
/*   4:    */ import commonj.work.WorkException;
/*   5:    */ import commonj.work.WorkItem;
/*   6:    */ import commonj.work.WorkListener;
/*   7:    */ import commonj.work.WorkManager;
/*   8:    */ import commonj.work.WorkRejectedException;
/*   9:    */ import java.util.Collection;
/*  10:    */ import java.util.concurrent.Callable;
/*  11:    */ import java.util.concurrent.Future;
/*  12:    */ import java.util.concurrent.FutureTask;
/*  13:    */ import javax.naming.NamingException;
/*  14:    */ import org.springframework.beans.factory.InitializingBean;
/*  15:    */ import org.springframework.core.task.TaskRejectedException;
/*  16:    */ import org.springframework.jndi.JndiLocatorSupport;
/*  17:    */ import org.springframework.scheduling.SchedulingException;
/*  18:    */ import org.springframework.scheduling.SchedulingTaskExecutor;
/*  19:    */ import org.springframework.util.Assert;
/*  20:    */ 
/*  21:    */ public class WorkManagerTaskExecutor
/*  22:    */   extends JndiLocatorSupport
/*  23:    */   implements SchedulingTaskExecutor, WorkManager, InitializingBean
/*  24:    */ {
/*  25:    */   private WorkManager workManager;
/*  26:    */   private String workManagerName;
/*  27:    */   private WorkListener workListener;
/*  28:    */   
/*  29:    */   public void setWorkManager(WorkManager workManager)
/*  30:    */   {
/*  31:108 */     this.workManager = workManager;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setWorkManagerName(String workManagerName)
/*  35:    */   {
/*  36:120 */     this.workManagerName = workManagerName;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setWorkListener(WorkListener workListener)
/*  40:    */   {
/*  41:129 */     this.workListener = workListener;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void afterPropertiesSet()
/*  45:    */     throws NamingException
/*  46:    */   {
/*  47:133 */     if (this.workManager == null)
/*  48:    */     {
/*  49:134 */       if (this.workManagerName == null) {
/*  50:135 */         throw new IllegalArgumentException("Either 'workManager' or 'workManagerName' must be specified");
/*  51:    */       }
/*  52:137 */       this.workManager = ((WorkManager)lookup(this.workManagerName, WorkManager.class));
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void execute(Runnable task)
/*  57:    */   {
/*  58:147 */     Assert.state(this.workManager != null, "No WorkManager specified");
/*  59:148 */     Work work = new DelegatingWork(task);
/*  60:    */     try
/*  61:    */     {
/*  62:150 */       if (this.workListener != null) {
/*  63:151 */         this.workManager.schedule(work, this.workListener);
/*  64:    */       } else {
/*  65:154 */         this.workManager.schedule(work);
/*  66:    */       }
/*  67:    */     }
/*  68:    */     catch (WorkRejectedException ex)
/*  69:    */     {
/*  70:158 */       throw new TaskRejectedException("CommonJ WorkManager did not accept task: " + task, ex);
/*  71:    */     }
/*  72:    */     catch (WorkException ex)
/*  73:    */     {
/*  74:161 */       throw new SchedulingException("Could not schedule task on CommonJ WorkManager", ex);
/*  75:    */     }
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void execute(Runnable task, long startTimeout)
/*  79:    */   {
/*  80:166 */     execute(task);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public Future<?> submit(Runnable task)
/*  84:    */   {
/*  85:170 */     FutureTask<Object> future = new FutureTask(task, null);
/*  86:171 */     execute(future);
/*  87:172 */     return future;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public <T> Future<T> submit(Callable<T> task)
/*  91:    */   {
/*  92:176 */     FutureTask<T> future = new FutureTask(task);
/*  93:177 */     execute(future);
/*  94:178 */     return future;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public boolean prefersShortLivedTasks()
/*  98:    */   {
/*  99:185 */     return true;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public WorkItem schedule(Work work)
/* 103:    */     throws WorkException, IllegalArgumentException
/* 104:    */   {
/* 105:196 */     return this.workManager.schedule(work);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public WorkItem schedule(Work work, WorkListener workListener)
/* 109:    */     throws WorkException, IllegalArgumentException
/* 110:    */   {
/* 111:202 */     return this.workManager.schedule(work, workListener);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public boolean waitForAll(Collection workItems, long timeout)
/* 115:    */     throws InterruptedException, IllegalArgumentException
/* 116:    */   {
/* 117:208 */     return this.workManager.waitForAll(workItems, timeout);
/* 118:    */   }
/* 119:    */   
/* 120:    */   public Collection waitForAny(Collection workItems, long timeout)
/* 121:    */     throws InterruptedException, IllegalArgumentException
/* 122:    */   {
/* 123:214 */     return this.workManager.waitForAny(workItems, timeout);
/* 124:    */   }
/* 125:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.commonj.WorkManagerTaskExecutor
 * JD-Core Version:    0.7.0.1
 */