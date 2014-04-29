/*   1:    */ package org.springframework.scheduling.commonj;
/*   2:    */ 
/*   3:    */ import commonj.timers.Timer;
/*   4:    */ import commonj.timers.TimerManager;
/*   5:    */ import java.util.LinkedList;
/*   6:    */ import java.util.List;
/*   7:    */ import javax.naming.NamingException;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.springframework.beans.factory.DisposableBean;
/*  10:    */ import org.springframework.beans.factory.FactoryBean;
/*  11:    */ import org.springframework.beans.factory.InitializingBean;
/*  12:    */ import org.springframework.context.Lifecycle;
/*  13:    */ 
/*  14:    */ public class TimerManagerFactoryBean
/*  15:    */   extends TimerManagerAccessor
/*  16:    */   implements FactoryBean<TimerManager>, InitializingBean, DisposableBean, Lifecycle
/*  17:    */ {
/*  18:    */   private ScheduledTimerListener[] scheduledTimerListeners;
/*  19: 59 */   private final List<Timer> timers = new LinkedList();
/*  20:    */   
/*  21:    */   public void setScheduledTimerListeners(ScheduledTimerListener[] scheduledTimerListeners)
/*  22:    */   {
/*  23: 71 */     this.scheduledTimerListeners = scheduledTimerListeners;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void afterPropertiesSet()
/*  27:    */     throws NamingException
/*  28:    */   {
/*  29: 80 */     super.afterPropertiesSet();
/*  30: 81 */     if (this.scheduledTimerListeners != null)
/*  31:    */     {
/*  32: 82 */       TimerManager timerManager = getTimerManager();
/*  33: 83 */       for (ScheduledTimerListener scheduledTask : this.scheduledTimerListeners)
/*  34:    */       {
/*  35:    */         Timer timer;
/*  36:    */         Timer timer;
/*  37: 85 */         if (scheduledTask.isOneTimeTask())
/*  38:    */         {
/*  39: 86 */           timer = timerManager.schedule(scheduledTask.getTimerListener(), scheduledTask.getDelay());
/*  40:    */         }
/*  41:    */         else
/*  42:    */         {
/*  43:    */           Timer timer;
/*  44: 89 */           if (scheduledTask.isFixedRate()) {
/*  45: 90 */             timer = timerManager.scheduleAtFixedRate(
/*  46: 91 */               scheduledTask.getTimerListener(), scheduledTask.getDelay(), scheduledTask.getPeriod());
/*  47:    */           } else {
/*  48: 94 */             timer = timerManager.schedule(
/*  49: 95 */               scheduledTask.getTimerListener(), scheduledTask.getDelay(), scheduledTask.getPeriod());
/*  50:    */           }
/*  51:    */         }
/*  52: 98 */         this.timers.add(timer);
/*  53:    */       }
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public TimerManager getObject()
/*  58:    */   {
/*  59:109 */     return getTimerManager();
/*  60:    */   }
/*  61:    */   
/*  62:    */   public Class<? extends TimerManager> getObjectType()
/*  63:    */   {
/*  64:113 */     TimerManager timerManager = getTimerManager();
/*  65:114 */     return timerManager != null ? timerManager.getClass() : TimerManager.class;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public boolean isSingleton()
/*  69:    */   {
/*  70:118 */     return true;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void destroy()
/*  74:    */   {
/*  75:135 */     for (Timer timer : this.timers) {
/*  76:    */       try
/*  77:    */       {
/*  78:137 */         timer.cancel();
/*  79:    */       }
/*  80:    */       catch (Throwable ex)
/*  81:    */       {
/*  82:140 */         this.logger.warn("Could not cancel CommonJ Timer", ex);
/*  83:    */       }
/*  84:    */     }
/*  85:143 */     this.timers.clear();
/*  86:    */     
/*  87:    */ 
/*  88:146 */     super.destroy();
/*  89:    */   }
/*  90:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.commonj.TimerManagerFactoryBean
 * JD-Core Version:    0.7.0.1
 */