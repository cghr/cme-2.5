/*  1:   */ package org.springframework.jca.work.jboss;
/*  2:   */ 
/*  3:   */ import javax.resource.spi.work.WorkManager;
/*  4:   */ import org.springframework.jca.work.WorkManagerTaskExecutor;
/*  5:   */ 
/*  6:   */ public class JBossWorkManagerTaskExecutor
/*  7:   */   extends WorkManagerTaskExecutor
/*  8:   */ {
/*  9:   */   public void setWorkManagerMBeanName(String mbeanName)
/* 10:   */   {
/* 11:45 */     setWorkManager(JBossWorkManagerUtils.getWorkManager(mbeanName));
/* 12:   */   }
/* 13:   */   
/* 14:   */   protected WorkManager getDefaultWorkManager()
/* 15:   */   {
/* 16:55 */     return JBossWorkManagerUtils.getWorkManager();
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.work.jboss.JBossWorkManagerTaskExecutor
 * JD-Core Version:    0.7.0.1
 */