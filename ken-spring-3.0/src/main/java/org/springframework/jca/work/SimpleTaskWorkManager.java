/*   1:    */ package org.springframework.jca.work;
/*   2:    */ 
/*   3:    */ import javax.resource.spi.work.ExecutionContext;
/*   4:    */ import javax.resource.spi.work.Work;
/*   5:    */ import javax.resource.spi.work.WorkAdapter;
/*   6:    */ import javax.resource.spi.work.WorkCompletedException;
/*   7:    */ import javax.resource.spi.work.WorkEvent;
/*   8:    */ import javax.resource.spi.work.WorkException;
/*   9:    */ import javax.resource.spi.work.WorkListener;
/*  10:    */ import javax.resource.spi.work.WorkManager;
/*  11:    */ import javax.resource.spi.work.WorkRejectedException;
/*  12:    */ import org.springframework.core.task.AsyncTaskExecutor;
/*  13:    */ import org.springframework.core.task.SimpleAsyncTaskExecutor;
/*  14:    */ import org.springframework.core.task.SyncTaskExecutor;
/*  15:    */ import org.springframework.core.task.TaskExecutor;
/*  16:    */ import org.springframework.core.task.TaskRejectedException;
/*  17:    */ import org.springframework.core.task.TaskTimeoutException;
/*  18:    */ import org.springframework.util.Assert;
/*  19:    */ 
/*  20:    */ public class SimpleTaskWorkManager
/*  21:    */   implements WorkManager
/*  22:    */ {
/*  23: 65 */   private TaskExecutor syncTaskExecutor = new SyncTaskExecutor();
/*  24: 67 */   private AsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
/*  25:    */   
/*  26:    */   public void setSyncTaskExecutor(TaskExecutor syncTaskExecutor)
/*  27:    */   {
/*  28: 76 */     this.syncTaskExecutor = syncTaskExecutor;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setAsyncTaskExecutor(AsyncTaskExecutor asyncTaskExecutor)
/*  32:    */   {
/*  33: 87 */     this.asyncTaskExecutor = asyncTaskExecutor;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void doWork(Work work)
/*  37:    */     throws WorkException
/*  38:    */   {
/*  39: 92 */     doWork(work, 9223372036854775807L, null, null);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void doWork(Work work, long startTimeout, ExecutionContext executionContext, WorkListener workListener)
/*  43:    */     throws WorkException
/*  44:    */   {
/*  45: 98 */     Assert.state(this.syncTaskExecutor != null, "No 'syncTaskExecutor' set");
/*  46: 99 */     executeWork(this.syncTaskExecutor, work, startTimeout, false, executionContext, workListener);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public long startWork(Work work)
/*  50:    */     throws WorkException
/*  51:    */   {
/*  52:103 */     return startWork(work, 9223372036854775807L, null, null);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public long startWork(Work work, long startTimeout, ExecutionContext executionContext, WorkListener workListener)
/*  56:    */     throws WorkException
/*  57:    */   {
/*  58:109 */     Assert.state(this.asyncTaskExecutor != null, "No 'asyncTaskExecutor' set");
/*  59:110 */     return executeWork(this.asyncTaskExecutor, work, startTimeout, true, executionContext, workListener);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void scheduleWork(Work work)
/*  63:    */     throws WorkException
/*  64:    */   {
/*  65:114 */     scheduleWork(work, 9223372036854775807L, null, null);
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void scheduleWork(Work work, long startTimeout, ExecutionContext executionContext, WorkListener workListener)
/*  69:    */     throws WorkException
/*  70:    */   {
/*  71:120 */     Assert.state(this.asyncTaskExecutor != null, "No 'asyncTaskExecutor' set");
/*  72:121 */     executeWork(this.asyncTaskExecutor, work, startTimeout, false, executionContext, workListener);
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected long executeWork(TaskExecutor taskExecutor, Work work, long startTimeout, boolean blockUntilStarted, ExecutionContext executionContext, WorkListener workListener)
/*  76:    */     throws WorkException
/*  77:    */   {
/*  78:141 */     if ((executionContext != null) && (executionContext.getXid() != null)) {
/*  79:142 */       throw new WorkException("SimpleTaskWorkManager does not supported imported XIDs: " + executionContext.getXid());
/*  80:    */     }
/*  81:144 */     WorkListener workListenerToUse = workListener;
/*  82:145 */     if (workListenerToUse == null) {
/*  83:146 */       workListenerToUse = new WorkAdapter();
/*  84:    */     }
/*  85:149 */     boolean isAsync = taskExecutor instanceof AsyncTaskExecutor;
/*  86:150 */     DelegatingWorkAdapter workHandle = new DelegatingWorkAdapter(work, workListenerToUse, !isAsync);
/*  87:    */     try
/*  88:    */     {
/*  89:152 */       if (isAsync) {
/*  90:153 */         ((AsyncTaskExecutor)taskExecutor).execute(workHandle, startTimeout);
/*  91:    */       } else {
/*  92:156 */         taskExecutor.execute(workHandle);
/*  93:    */       }
/*  94:    */     }
/*  95:    */     catch (TaskTimeoutException ex)
/*  96:    */     {
/*  97:160 */       WorkException wex = new WorkRejectedException("TaskExecutor rejected Work because of timeout: " + work, ex);
/*  98:161 */       wex.setErrorCode("1");
/*  99:162 */       workListenerToUse.workRejected(new WorkEvent(this, 2, work, wex));
/* 100:163 */       throw wex;
/* 101:    */     }
/* 102:    */     catch (TaskRejectedException ex)
/* 103:    */     {
/* 104:166 */       WorkException wex = new WorkRejectedException("TaskExecutor rejected Work: " + work, ex);
/* 105:167 */       wex.setErrorCode("-1");
/* 106:168 */       workListenerToUse.workRejected(new WorkEvent(this, 2, work, wex));
/* 107:169 */       throw wex;
/* 108:    */     }
/* 109:    */     catch (Throwable ex)
/* 110:    */     {
/* 111:172 */       WorkException wex = new WorkException("TaskExecutor failed to execute Work: " + work, ex);
/* 112:173 */       wex.setErrorCode("-1");
/* 113:174 */       throw wex;
/* 114:    */     }
/* 115:176 */     if (isAsync) {
/* 116:177 */       workListenerToUse.workAccepted(new WorkEvent(this, 1, work, null));
/* 117:    */     }
/* 118:180 */     if (blockUntilStarted)
/* 119:    */     {
/* 120:181 */       long acceptanceTime = System.currentTimeMillis();
/* 121:182 */       synchronized (workHandle.monitor)
/* 122:    */       {
/* 123:    */         try
/* 124:    */         {
/* 125:184 */           while (!workHandle.started) {
/* 126:185 */             workHandle.monitor.wait();
/* 127:    */           }
/* 128:    */         }
/* 129:    */         catch (InterruptedException localInterruptedException)
/* 130:    */         {
/* 131:189 */           Thread.currentThread().interrupt();
/* 132:    */         }
/* 133:    */       }
/* 134:192 */       return System.currentTimeMillis() - acceptanceTime;
/* 135:    */     }
/* 136:195 */     return -1L;
/* 137:    */   }
/* 138:    */   
/* 139:    */   private static class DelegatingWorkAdapter
/* 140:    */     implements Work
/* 141:    */   {
/* 142:    */     private final Work work;
/* 143:    */     private final WorkListener workListener;
/* 144:    */     private final boolean acceptOnExecution;
/* 145:212 */     public final Object monitor = new Object();
/* 146:214 */     public boolean started = false;
/* 147:    */     
/* 148:    */     public DelegatingWorkAdapter(Work work, WorkListener workListener, boolean acceptOnExecution)
/* 149:    */     {
/* 150:217 */       this.work = work;
/* 151:218 */       this.workListener = workListener;
/* 152:219 */       this.acceptOnExecution = acceptOnExecution;
/* 153:    */     }
/* 154:    */     
/* 155:    */     public void run()
/* 156:    */     {
/* 157:223 */       if (this.acceptOnExecution) {
/* 158:224 */         this.workListener.workAccepted(new WorkEvent(this, 1, this.work, null));
/* 159:    */       }
/* 160:226 */       synchronized (this.monitor)
/* 161:    */       {
/* 162:227 */         this.started = true;
/* 163:228 */         this.monitor.notify();
/* 164:    */       }
/* 165:230 */       this.workListener.workStarted(new WorkEvent(this, 3, this.work, null));
/* 166:    */       try
/* 167:    */       {
/* 168:232 */         this.work.run();
/* 169:    */       }
/* 170:    */       catch (RuntimeException ex)
/* 171:    */       {
/* 172:235 */         this.workListener.workCompleted(
/* 173:236 */           new WorkEvent(this, 4, this.work, new WorkCompletedException(ex)));
/* 174:237 */         throw ex;
/* 175:    */       }
/* 176:    */       catch (Error err)
/* 177:    */       {
/* 178:240 */         this.workListener.workCompleted(
/* 179:241 */           new WorkEvent(this, 4, this.work, new WorkCompletedException(err)));
/* 180:242 */         throw err;
/* 181:    */       }
/* 182:244 */       this.workListener.workCompleted(new WorkEvent(this, 4, this.work, null));
/* 183:    */     }
/* 184:    */     
/* 185:    */     public void release()
/* 186:    */     {
/* 187:248 */       this.work.release();
/* 188:    */     }
/* 189:    */   }
/* 190:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.work.SimpleTaskWorkManager
 * JD-Core Version:    0.7.0.1
 */