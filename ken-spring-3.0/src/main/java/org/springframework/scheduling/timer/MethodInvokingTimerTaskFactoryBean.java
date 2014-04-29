/*  1:   */ package org.springframework.scheduling.timer;
/*  2:   */ 
/*  3:   */ import java.util.TimerTask;
/*  4:   */ import org.springframework.beans.factory.FactoryBean;
/*  5:   */ import org.springframework.scheduling.support.MethodInvokingRunnable;
/*  6:   */ 
/*  7:   */ @Deprecated
/*  8:   */ public class MethodInvokingTimerTaskFactoryBean
/*  9:   */   extends MethodInvokingRunnable
/* 10:   */   implements FactoryBean<TimerTask>
/* 11:   */ {
/* 12:   */   private TimerTask timerTask;
/* 13:   */   
/* 14:   */   public void afterPropertiesSet()
/* 15:   */     throws ClassNotFoundException, NoSuchMethodException
/* 16:   */   {
/* 17:51 */     super.afterPropertiesSet();
/* 18:52 */     this.timerTask = new DelegatingTimerTask(this);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public TimerTask getObject()
/* 22:   */   {
/* 23:57 */     return this.timerTask;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public Class<TimerTask> getObjectType()
/* 27:   */   {
/* 28:61 */     return TimerTask.class;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public boolean isSingleton()
/* 32:   */   {
/* 33:65 */     return true;
/* 34:   */   }
/* 35:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.timer.MethodInvokingTimerTaskFactoryBean
 * JD-Core Version:    0.7.0.1
 */