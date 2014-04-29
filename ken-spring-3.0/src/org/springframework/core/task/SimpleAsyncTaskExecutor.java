/*   1:    */ package org.springframework.core.task;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.concurrent.Callable;
/*   5:    */ import java.util.concurrent.Future;
/*   6:    */ import java.util.concurrent.FutureTask;
/*   7:    */ import java.util.concurrent.ThreadFactory;
/*   8:    */ import org.springframework.util.Assert;
/*   9:    */ import org.springframework.util.ConcurrencyThrottleSupport;
/*  10:    */ import org.springframework.util.CustomizableThreadCreator;
/*  11:    */ 
/*  12:    */ public class SimpleAsyncTaskExecutor
/*  13:    */   extends CustomizableThreadCreator
/*  14:    */   implements AsyncTaskExecutor, Serializable
/*  15:    */ {
/*  16:    */   public static final int UNBOUNDED_CONCURRENCY = -1;
/*  17:    */   public static final int NO_CONCURRENCY = 0;
/*  18: 61 */   private final ConcurrencyThrottleAdapter concurrencyThrottle = new ConcurrencyThrottleAdapter(null);
/*  19:    */   private ThreadFactory threadFactory;
/*  20:    */   
/*  21:    */   public SimpleAsyncTaskExecutor() {}
/*  22:    */   
/*  23:    */   public SimpleAsyncTaskExecutor(String threadNamePrefix)
/*  24:    */   {
/*  25: 78 */     super(threadNamePrefix);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public SimpleAsyncTaskExecutor(ThreadFactory threadFactory)
/*  29:    */   {
/*  30: 86 */     this.threadFactory = threadFactory;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setThreadFactory(ThreadFactory threadFactory)
/*  34:    */   {
/*  35: 99 */     this.threadFactory = threadFactory;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public final ThreadFactory getThreadFactory()
/*  39:    */   {
/*  40:106 */     return this.threadFactory;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setConcurrencyLimit(int concurrencyLimit)
/*  44:    */   {
/*  45:120 */     this.concurrencyThrottle.setConcurrencyLimit(concurrencyLimit);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public final int getConcurrencyLimit()
/*  49:    */   {
/*  50:127 */     return this.concurrencyThrottle.getConcurrencyLimit();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public final boolean isThrottleActive()
/*  54:    */   {
/*  55:137 */     return this.concurrencyThrottle.isThrottleActive();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void execute(Runnable task)
/*  59:    */   {
/*  60:147 */     execute(task, 9223372036854775807L);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void execute(Runnable task, long startTimeout)
/*  64:    */   {
/*  65:160 */     Assert.notNull(task, "Runnable must not be null");
/*  66:161 */     if ((isThrottleActive()) && (startTimeout > 0L))
/*  67:    */     {
/*  68:162 */       this.concurrencyThrottle.beforeAccess();
/*  69:163 */       doExecute(new ConcurrencyThrottlingRunnable(task));
/*  70:    */     }
/*  71:    */     else
/*  72:    */     {
/*  73:166 */       doExecute(task);
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   public Future<?> submit(Runnable task)
/*  78:    */   {
/*  79:171 */     FutureTask<Object> future = new FutureTask(task, null);
/*  80:172 */     execute(future, 9223372036854775807L);
/*  81:173 */     return future;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public <T> Future<T> submit(Callable<T> task)
/*  85:    */   {
/*  86:177 */     FutureTask<T> future = new FutureTask(task);
/*  87:178 */     execute(future, 9223372036854775807L);
/*  88:179 */     return future;
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected void doExecute(Runnable task)
/*  92:    */   {
/*  93:191 */     Thread thread = this.threadFactory != null ? this.threadFactory.newThread(task) : createThread(task);
/*  94:192 */     thread.start();
/*  95:    */   }
/*  96:    */   
/*  97:    */   private static class ConcurrencyThrottleAdapter
/*  98:    */     extends ConcurrencyThrottleSupport
/*  99:    */   {
/* 100:    */     protected void beforeAccess()
/* 101:    */     {
/* 102:205 */       super.beforeAccess();
/* 103:    */     }
/* 104:    */     
/* 105:    */     protected void afterAccess()
/* 106:    */     {
/* 107:210 */       super.afterAccess();
/* 108:    */     }
/* 109:    */   }
/* 110:    */   
/* 111:    */   private class ConcurrencyThrottlingRunnable
/* 112:    */     implements Runnable
/* 113:    */   {
/* 114:    */     private final Runnable target;
/* 115:    */     
/* 116:    */     public ConcurrencyThrottlingRunnable(Runnable target)
/* 117:    */     {
/* 118:224 */       this.target = target;
/* 119:    */     }
/* 120:    */     
/* 121:    */     public void run()
/* 122:    */     {
/* 123:    */       try
/* 124:    */       {
/* 125:229 */         this.target.run();
/* 126:    */       }
/* 127:    */       finally
/* 128:    */       {
/* 129:232 */         SimpleAsyncTaskExecutor.this.concurrencyThrottle.afterAccess();
/* 130:    */       }
/* 131:    */     }
/* 132:    */   }
/* 133:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.task.SimpleAsyncTaskExecutor
 * JD-Core Version:    0.7.0.1
 */