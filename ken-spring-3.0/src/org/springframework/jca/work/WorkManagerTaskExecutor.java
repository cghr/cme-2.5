/*   1:    */ package org.springframework.jca.work;
/*   2:    */ 
/*   3:    */ import java.util.concurrent.Callable;
/*   4:    */ import java.util.concurrent.Future;
/*   5:    */ import java.util.concurrent.FutureTask;
/*   6:    */ import javax.naming.NamingException;
/*   7:    */ import javax.resource.spi.BootstrapContext;
/*   8:    */ import javax.resource.spi.work.ExecutionContext;
/*   9:    */ import javax.resource.spi.work.Work;
/*  10:    */ import javax.resource.spi.work.WorkException;
/*  11:    */ import javax.resource.spi.work.WorkListener;
/*  12:    */ import javax.resource.spi.work.WorkManager;
/*  13:    */ import javax.resource.spi.work.WorkRejectedException;
/*  14:    */ import org.springframework.beans.factory.InitializingBean;
/*  15:    */ import org.springframework.core.task.AsyncTaskExecutor;
/*  16:    */ import org.springframework.core.task.TaskRejectedException;
/*  17:    */ import org.springframework.core.task.TaskTimeoutException;
/*  18:    */ import org.springframework.jca.context.BootstrapContextAware;
/*  19:    */ import org.springframework.jndi.JndiLocatorSupport;
/*  20:    */ import org.springframework.scheduling.SchedulingException;
/*  21:    */ import org.springframework.scheduling.SchedulingTaskExecutor;
/*  22:    */ import org.springframework.util.Assert;
/*  23:    */ 
/*  24:    */ public class WorkManagerTaskExecutor
/*  25:    */   extends JndiLocatorSupport
/*  26:    */   implements SchedulingTaskExecutor, AsyncTaskExecutor, WorkManager, BootstrapContextAware, InitializingBean
/*  27:    */ {
/*  28:    */   private WorkManager workManager;
/*  29:    */   private String workManagerName;
/*  30: 79 */   private boolean blockUntilStarted = false;
/*  31: 81 */   private boolean blockUntilCompleted = false;
/*  32:    */   private WorkListener workListener;
/*  33:    */   
/*  34:    */   public WorkManagerTaskExecutor() {}
/*  35:    */   
/*  36:    */   public WorkManagerTaskExecutor(WorkManager workManager)
/*  37:    */   {
/*  38: 98 */     setWorkManager(workManager);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setWorkManager(WorkManager workManager)
/*  42:    */   {
/*  43:106 */     Assert.notNull(workManager, "WorkManager must not be null");
/*  44:107 */     this.workManager = workManager;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setWorkManagerName(String workManagerName)
/*  48:    */   {
/*  49:119 */     this.workManagerName = workManagerName;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setBootstrapContext(BootstrapContext bootstrapContext)
/*  53:    */   {
/*  54:127 */     Assert.notNull(bootstrapContext, "BootstrapContext must not be null");
/*  55:128 */     this.workManager = bootstrapContext.getWorkManager();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setBlockUntilStarted(boolean blockUntilStarted)
/*  59:    */   {
/*  60:140 */     this.blockUntilStarted = blockUntilStarted;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setBlockUntilCompleted(boolean blockUntilCompleted)
/*  64:    */   {
/*  65:152 */     this.blockUntilCompleted = blockUntilCompleted;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setWorkListener(WorkListener workListener)
/*  69:    */   {
/*  70:161 */     this.workListener = workListener;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void afterPropertiesSet()
/*  74:    */     throws NamingException
/*  75:    */   {
/*  76:165 */     if (this.workManager == null) {
/*  77:166 */       if (this.workManagerName != null) {
/*  78:167 */         this.workManager = ((WorkManager)lookup(this.workManagerName, WorkManager.class));
/*  79:    */       } else {
/*  80:170 */         this.workManager = getDefaultWorkManager();
/*  81:    */       }
/*  82:    */     }
/*  83:    */   }
/*  84:    */   
/*  85:    */   protected WorkManager getDefaultWorkManager()
/*  86:    */   {
/*  87:182 */     return new SimpleTaskWorkManager();
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void execute(Runnable task)
/*  91:    */   {
/*  92:191 */     execute(task, 9223372036854775807L);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void execute(Runnable task, long startTimeout)
/*  96:    */   {
/*  97:195 */     Assert.state(this.workManager != null, "No WorkManager specified");
/*  98:196 */     Work work = new DelegatingWork(task);
/*  99:    */     try
/* 100:    */     {
/* 101:198 */       if (this.blockUntilCompleted)
/* 102:    */       {
/* 103:199 */         if ((startTimeout != 9223372036854775807L) || (this.workListener != null)) {
/* 104:200 */           this.workManager.doWork(work, startTimeout, null, this.workListener);
/* 105:    */         } else {
/* 106:203 */           this.workManager.doWork(work);
/* 107:    */         }
/* 108:    */       }
/* 109:206 */       else if (this.blockUntilStarted)
/* 110:    */       {
/* 111:207 */         if ((startTimeout != 9223372036854775807L) || (this.workListener != null)) {
/* 112:208 */           this.workManager.startWork(work, startTimeout, null, this.workListener);
/* 113:    */         } else {
/* 114:211 */           this.workManager.startWork(work);
/* 115:    */         }
/* 116:    */       }
/* 117:215 */       else if ((startTimeout != 9223372036854775807L) || (this.workListener != null)) {
/* 118:216 */         this.workManager.scheduleWork(work, startTimeout, null, this.workListener);
/* 119:    */       } else {
/* 120:219 */         this.workManager.scheduleWork(work);
/* 121:    */       }
/* 122:    */     }
/* 123:    */     catch (WorkRejectedException ex)
/* 124:    */     {
/* 125:224 */       if ("1".equals(ex.getErrorCode())) {
/* 126:225 */         throw new TaskTimeoutException("JCA WorkManager rejected task because of timeout: " + task, ex);
/* 127:    */       }
/* 128:228 */       throw new TaskRejectedException("JCA WorkManager rejected task: " + task, ex);
/* 129:    */     }
/* 130:    */     catch (WorkException ex)
/* 131:    */     {
/* 132:232 */       throw new SchedulingException("Could not schedule task on JCA WorkManager", ex);
/* 133:    */     }
/* 134:    */   }
/* 135:    */   
/* 136:    */   public Future<?> submit(Runnable task)
/* 137:    */   {
/* 138:237 */     FutureTask<Object> future = new FutureTask(task, null);
/* 139:238 */     execute(future, 9223372036854775807L);
/* 140:239 */     return future;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public <T> Future<T> submit(Callable<T> task)
/* 144:    */   {
/* 145:243 */     FutureTask<T> future = new FutureTask(task);
/* 146:244 */     execute(future, 9223372036854775807L);
/* 147:245 */     return future;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public boolean prefersShortLivedTasks()
/* 151:    */   {
/* 152:252 */     return true;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void doWork(Work work)
/* 156:    */     throws WorkException
/* 157:    */   {
/* 158:261 */     this.workManager.doWork(work);
/* 159:    */   }
/* 160:    */   
/* 161:    */   public void doWork(Work work, long delay, ExecutionContext executionContext, WorkListener workListener)
/* 162:    */     throws WorkException
/* 163:    */   {
/* 164:267 */     this.workManager.doWork(work, delay, executionContext, workListener);
/* 165:    */   }
/* 166:    */   
/* 167:    */   public long startWork(Work work)
/* 168:    */     throws WorkException
/* 169:    */   {
/* 170:271 */     return this.workManager.startWork(work);
/* 171:    */   }
/* 172:    */   
/* 173:    */   public long startWork(Work work, long delay, ExecutionContext executionContext, WorkListener workListener)
/* 174:    */     throws WorkException
/* 175:    */   {
/* 176:277 */     return this.workManager.startWork(work, delay, executionContext, workListener);
/* 177:    */   }
/* 178:    */   
/* 179:    */   public void scheduleWork(Work work)
/* 180:    */     throws WorkException
/* 181:    */   {
/* 182:281 */     this.workManager.scheduleWork(work);
/* 183:    */   }
/* 184:    */   
/* 185:    */   public void scheduleWork(Work work, long delay, ExecutionContext executionContext, WorkListener workListener)
/* 186:    */     throws WorkException
/* 187:    */   {
/* 188:287 */     this.workManager.scheduleWork(work, delay, executionContext, workListener);
/* 189:    */   }
/* 190:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.work.WorkManagerTaskExecutor
 * JD-Core Version:    0.7.0.1
 */