/*   1:    */ package org.springframework.scheduling.config;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.LinkedHashSet;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Map.Entry;
/*   7:    */ import java.util.Set;
/*   8:    */ import java.util.concurrent.Executors;
/*   9:    */ import java.util.concurrent.ScheduledExecutorService;
/*  10:    */ import java.util.concurrent.ScheduledFuture;
/*  11:    */ import org.springframework.beans.factory.DisposableBean;
/*  12:    */ import org.springframework.beans.factory.InitializingBean;
/*  13:    */ import org.springframework.scheduling.TaskScheduler;
/*  14:    */ import org.springframework.scheduling.Trigger;
/*  15:    */ import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
/*  16:    */ import org.springframework.scheduling.support.CronTrigger;
/*  17:    */ import org.springframework.util.Assert;
/*  18:    */ 
/*  19:    */ public class ScheduledTaskRegistrar
/*  20:    */   implements InitializingBean, DisposableBean
/*  21:    */ {
/*  22:    */   private TaskScheduler taskScheduler;
/*  23:    */   private ScheduledExecutorService localExecutor;
/*  24:    */   private Map<Runnable, Trigger> triggerTasks;
/*  25:    */   private Map<Runnable, String> cronTasks;
/*  26:    */   private Map<Runnable, Long> fixedRateTasks;
/*  27:    */   private Map<Runnable, Long> fixedDelayTasks;
/*  28: 64 */   private final Set<ScheduledFuture<?>> scheduledFutures = new LinkedHashSet();
/*  29:    */   
/*  30:    */   public void setTaskScheduler(TaskScheduler taskScheduler)
/*  31:    */   {
/*  32: 71 */     Assert.notNull(taskScheduler, "TaskScheduler must not be null");
/*  33: 72 */     this.taskScheduler = taskScheduler;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setScheduler(Object scheduler)
/*  37:    */   {
/*  38: 81 */     Assert.notNull(scheduler, "Scheduler object must not be null");
/*  39: 82 */     if ((scheduler instanceof TaskScheduler)) {
/*  40: 83 */       this.taskScheduler = ((TaskScheduler)scheduler);
/*  41: 85 */     } else if ((scheduler instanceof ScheduledExecutorService)) {
/*  42: 86 */       this.taskScheduler = new ConcurrentTaskScheduler((ScheduledExecutorService)scheduler);
/*  43:    */     } else {
/*  44: 89 */       throw new IllegalArgumentException("Unsupported scheduler type: " + scheduler.getClass());
/*  45:    */     }
/*  46:    */   }
/*  47:    */   
/*  48:    */   public TaskScheduler getScheduler()
/*  49:    */   {
/*  50: 97 */     return this.taskScheduler;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setTriggerTasks(Map<Runnable, Trigger> triggerTasks)
/*  54:    */   {
/*  55:105 */     this.triggerTasks = triggerTasks;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setCronTasks(Map<Runnable, String> cronTasks)
/*  59:    */   {
/*  60:113 */     this.cronTasks = cronTasks;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setFixedRateTasks(Map<Runnable, Long> fixedRateTasks)
/*  64:    */   {
/*  65:121 */     this.fixedRateTasks = fixedRateTasks;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void addTriggerTask(Runnable task, Trigger trigger)
/*  69:    */   {
/*  70:129 */     if (this.triggerTasks == null) {
/*  71:130 */       this.triggerTasks = new HashMap();
/*  72:    */     }
/*  73:132 */     this.triggerTasks.put(task, trigger);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void addCronTask(Runnable task, String cronExpression)
/*  77:    */   {
/*  78:139 */     if (this.cronTasks == null) {
/*  79:140 */       this.cronTasks = new HashMap();
/*  80:    */     }
/*  81:142 */     this.cronTasks.put(task, cronExpression);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void addFixedDelayTask(Runnable task, long delay)
/*  85:    */   {
/*  86:150 */     if (this.fixedDelayTasks == null) {
/*  87:151 */       this.fixedDelayTasks = new HashMap();
/*  88:    */     }
/*  89:153 */     this.fixedDelayTasks.put(task, Long.valueOf(delay));
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void addFixedRateTask(Runnable task, long period)
/*  93:    */   {
/*  94:161 */     if (this.fixedRateTasks == null) {
/*  95:162 */       this.fixedRateTasks = new HashMap();
/*  96:    */     }
/*  97:164 */     this.fixedRateTasks.put(task, Long.valueOf(period));
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setFixedDelayTasks(Map<Runnable, Long> fixedDelayTasks)
/* 101:    */   {
/* 102:172 */     this.fixedDelayTasks = fixedDelayTasks;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void afterPropertiesSet()
/* 106:    */   {
/* 107:177 */     if (this.taskScheduler == null)
/* 108:    */     {
/* 109:178 */       this.localExecutor = Executors.newSingleThreadScheduledExecutor();
/* 110:179 */       this.taskScheduler = new ConcurrentTaskScheduler(this.localExecutor);
/* 111:    */     }
/* 112:181 */     if (this.triggerTasks != null) {
/* 113:182 */       for (Map.Entry<Runnable, Trigger> entry : this.triggerTasks.entrySet()) {
/* 114:183 */         this.scheduledFutures.add(this.taskScheduler.schedule((Runnable)entry.getKey(), (Trigger)entry.getValue()));
/* 115:    */       }
/* 116:    */     }
/* 117:186 */     if (this.cronTasks != null) {
/* 118:187 */       for (Map.Entry<Runnable, String> entry : this.cronTasks.entrySet()) {
/* 119:188 */         this.scheduledFutures.add(this.taskScheduler.schedule((Runnable)entry.getKey(), new CronTrigger((String)entry.getValue())));
/* 120:    */       }
/* 121:    */     }
/* 122:191 */     if (this.fixedRateTasks != null) {
/* 123:192 */       for (Map.Entry<Runnable, Long> entry : this.fixedRateTasks.entrySet()) {
/* 124:193 */         this.scheduledFutures.add(this.taskScheduler.scheduleAtFixedRate((Runnable)entry.getKey(), ((Long)entry.getValue()).longValue()));
/* 125:    */       }
/* 126:    */     }
/* 127:196 */     if (this.fixedDelayTasks != null) {
/* 128:197 */       for (Map.Entry<Runnable, Long> entry : this.fixedDelayTasks.entrySet()) {
/* 129:198 */         this.scheduledFutures.add(this.taskScheduler.scheduleWithFixedDelay((Runnable)entry.getKey(), ((Long)entry.getValue()).longValue()));
/* 130:    */       }
/* 131:    */     }
/* 132:    */   }
/* 133:    */   
/* 134:    */   public void destroy()
/* 135:    */   {
/* 136:205 */     for (ScheduledFuture<?> future : this.scheduledFutures) {
/* 137:206 */       future.cancel(true);
/* 138:    */     }
/* 139:208 */     if (this.localExecutor != null) {
/* 140:209 */       this.localExecutor.shutdownNow();
/* 141:    */     }
/* 142:    */   }
/* 143:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.config.ScheduledTaskRegistrar
 * JD-Core Version:    0.7.0.1
 */