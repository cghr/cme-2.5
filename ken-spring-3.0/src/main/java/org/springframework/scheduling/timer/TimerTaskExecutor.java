/*   1:    */ package org.springframework.scheduling.timer;
/*   2:    */ 
/*   3:    */ import java.util.Timer;
/*   4:    */ import java.util.concurrent.Callable;
/*   5:    */ import java.util.concurrent.Future;
/*   6:    */ import java.util.concurrent.FutureTask;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.springframework.beans.factory.BeanNameAware;
/*  10:    */ import org.springframework.beans.factory.DisposableBean;
/*  11:    */ import org.springframework.beans.factory.InitializingBean;
/*  12:    */ import org.springframework.scheduling.SchedulingTaskExecutor;
/*  13:    */ import org.springframework.util.Assert;
/*  14:    */ import org.springframework.util.StringUtils;
/*  15:    */ 
/*  16:    */ @Deprecated
/*  17:    */ public class TimerTaskExecutor
/*  18:    */   implements SchedulingTaskExecutor, BeanNameAware, InitializingBean, DisposableBean
/*  19:    */ {
/*  20: 48 */   protected final Log logger = LogFactory.getLog(getClass());
/*  21:    */   private Timer timer;
/*  22: 52 */   private long delay = 0L;
/*  23:    */   private String beanName;
/*  24: 56 */   private boolean timerInternal = false;
/*  25:    */   
/*  26:    */   public TimerTaskExecutor() {}
/*  27:    */   
/*  28:    */   public TimerTaskExecutor(Timer timer)
/*  29:    */   {
/*  30: 72 */     Assert.notNull(timer, "Timer must not be null");
/*  31: 73 */     this.timer = timer;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setTimer(Timer timer)
/*  35:    */   {
/*  36: 85 */     this.timer = timer;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setDelay(long delay)
/*  40:    */   {
/*  41: 96 */     this.delay = delay;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setBeanName(String beanName)
/*  45:    */   {
/*  46:100 */     this.beanName = beanName;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void afterPropertiesSet()
/*  50:    */   {
/*  51:105 */     if (this.timer == null)
/*  52:    */     {
/*  53:106 */       this.logger.info("Initializing Timer");
/*  54:107 */       this.timer = createTimer();
/*  55:108 */       this.timerInternal = true;
/*  56:    */     }
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected Timer createTimer()
/*  60:    */   {
/*  61:122 */     if (StringUtils.hasText(this.beanName)) {
/*  62:123 */       return new Timer(this.beanName);
/*  63:    */     }
/*  64:126 */     return new Timer();
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected final Timer getTimer()
/*  68:    */   {
/*  69:134 */     Assert.notNull(this.timer, "Timer not initialized yet");
/*  70:135 */     return this.timer;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void execute(Runnable task)
/*  74:    */   {
/*  75:145 */     getTimer().schedule(new DelegatingTimerTask(task), this.delay);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void execute(Runnable task, long startTimeout)
/*  79:    */   {
/*  80:149 */     long actualDelay = startTimeout < this.delay ? startTimeout : this.delay;
/*  81:150 */     getTimer().schedule(new DelegatingTimerTask(task), actualDelay);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public Future<?> submit(Runnable task)
/*  85:    */   {
/*  86:154 */     FutureTask<Object> future = new FutureTask(task, null);
/*  87:155 */     execute(future);
/*  88:156 */     return future;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public <T> Future<T> submit(Callable<T> task)
/*  92:    */   {
/*  93:160 */     FutureTask<T> future = new FutureTask(task);
/*  94:161 */     execute(future);
/*  95:162 */     return future;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public boolean prefersShortLivedTasks()
/*  99:    */   {
/* 100:169 */     return true;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void destroy()
/* 104:    */   {
/* 105:178 */     if (this.timerInternal)
/* 106:    */     {
/* 107:179 */       this.logger.info("Cancelling Timer");
/* 108:180 */       this.timer.cancel();
/* 109:    */     }
/* 110:    */   }
/* 111:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.timer.TimerTaskExecutor
 * JD-Core Version:    0.7.0.1
 */