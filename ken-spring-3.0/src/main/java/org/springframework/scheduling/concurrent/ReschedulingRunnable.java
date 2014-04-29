/*   1:    */ package org.springframework.scheduling.concurrent;
/*   2:    */ 
/*   3:    */ import java.util.Date;
/*   4:    */ import java.util.concurrent.Delayed;
/*   5:    */ import java.util.concurrent.ExecutionException;
/*   6:    */ import java.util.concurrent.ScheduledExecutorService;
/*   7:    */ import java.util.concurrent.ScheduledFuture;
/*   8:    */ import java.util.concurrent.TimeUnit;
/*   9:    */ import java.util.concurrent.TimeoutException;
/*  10:    */ import org.springframework.scheduling.Trigger;
/*  11:    */ import org.springframework.scheduling.support.DelegatingErrorHandlingRunnable;
/*  12:    */ import org.springframework.scheduling.support.SimpleTriggerContext;
/*  13:    */ import org.springframework.util.ErrorHandler;
/*  14:    */ 
/*  15:    */ class ReschedulingRunnable
/*  16:    */   extends DelegatingErrorHandlingRunnable
/*  17:    */   implements ScheduledFuture<Object>
/*  18:    */ {
/*  19:    */   private final Trigger trigger;
/*  20: 48 */   private final SimpleTriggerContext triggerContext = new SimpleTriggerContext();
/*  21:    */   private final ScheduledExecutorService executor;
/*  22:    */   private volatile ScheduledFuture currentFuture;
/*  23:    */   private volatile Date scheduledExecutionTime;
/*  24: 56 */   private final Object triggerContextMonitor = new Object();
/*  25:    */   
/*  26:    */   public ReschedulingRunnable(Runnable delegate, Trigger trigger, ScheduledExecutorService executor, ErrorHandler errorHandler)
/*  27:    */   {
/*  28: 60 */     super(delegate, errorHandler);
/*  29: 61 */     this.trigger = trigger;
/*  30: 62 */     this.executor = executor;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public ScheduledFuture schedule()
/*  34:    */   {
/*  35: 67 */     synchronized (this.triggerContextMonitor)
/*  36:    */     {
/*  37: 68 */       this.scheduledExecutionTime = this.trigger.nextExecutionTime(this.triggerContext);
/*  38: 69 */       if (this.scheduledExecutionTime == null) {
/*  39: 70 */         return null;
/*  40:    */       }
/*  41: 72 */       long initialDelay = this.scheduledExecutionTime.getTime() - System.currentTimeMillis();
/*  42: 73 */       this.currentFuture = this.executor.schedule(this, initialDelay, TimeUnit.MILLISECONDS);
/*  43: 74 */       return this;
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void run()
/*  48:    */   {
/*  49: 80 */     Date actualExecutionTime = new Date();
/*  50: 81 */     super.run();
/*  51: 82 */     Date completionTime = new Date();
/*  52: 83 */     synchronized (this.triggerContextMonitor)
/*  53:    */     {
/*  54: 84 */       this.triggerContext.update(this.scheduledExecutionTime, actualExecutionTime, completionTime);
/*  55:    */     }
/*  56: 86 */     if (!this.currentFuture.isCancelled()) {
/*  57: 87 */       schedule();
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   public boolean cancel(boolean mayInterruptIfRunning)
/*  62:    */   {
/*  63: 93 */     return this.currentFuture.cancel(mayInterruptIfRunning);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public boolean isCancelled()
/*  67:    */   {
/*  68: 97 */     return this.currentFuture.isCancelled();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public boolean isDone()
/*  72:    */   {
/*  73:101 */     return this.currentFuture.isDone();
/*  74:    */   }
/*  75:    */   
/*  76:    */   public Object get()
/*  77:    */     throws InterruptedException, ExecutionException
/*  78:    */   {
/*  79:105 */     return this.currentFuture.get();
/*  80:    */   }
/*  81:    */   
/*  82:    */   public Object get(long timeout, TimeUnit unit)
/*  83:    */     throws InterruptedException, ExecutionException, TimeoutException
/*  84:    */   {
/*  85:109 */     return this.currentFuture.get(timeout, unit);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public long getDelay(TimeUnit unit)
/*  89:    */   {
/*  90:113 */     return this.currentFuture.getDelay(unit);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public int compareTo(Delayed other)
/*  94:    */   {
/*  95:117 */     if (this == other) {
/*  96:118 */       return 0;
/*  97:    */     }
/*  98:120 */     long diff = getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
/*  99:121 */     return diff < 0L ? -1 : diff == 0L ? 0 : 1;
/* 100:    */   }
/* 101:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.concurrent.ReschedulingRunnable
 * JD-Core Version:    0.7.0.1
 */