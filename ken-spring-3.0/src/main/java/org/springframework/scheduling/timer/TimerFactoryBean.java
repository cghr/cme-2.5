/*   1:    */ package org.springframework.scheduling.timer;
/*   2:    */ 
/*   3:    */ import java.util.Timer;
/*   4:    */ import org.apache.commons.logging.Log;
/*   5:    */ import org.apache.commons.logging.LogFactory;
/*   6:    */ import org.springframework.beans.factory.BeanNameAware;
/*   7:    */ import org.springframework.beans.factory.DisposableBean;
/*   8:    */ import org.springframework.beans.factory.FactoryBean;
/*   9:    */ import org.springframework.beans.factory.InitializingBean;
/*  10:    */ import org.springframework.util.ObjectUtils;
/*  11:    */ import org.springframework.util.StringUtils;
/*  12:    */ 
/*  13:    */ @Deprecated
/*  14:    */ public class TimerFactoryBean
/*  15:    */   implements FactoryBean<Timer>, BeanNameAware, InitializingBean, DisposableBean
/*  16:    */ {
/*  17: 55 */   protected final Log logger = LogFactory.getLog(getClass());
/*  18:    */   private ScheduledTimerTask[] scheduledTimerTasks;
/*  19: 59 */   private boolean daemon = false;
/*  20:    */   private String beanName;
/*  21:    */   private Timer timer;
/*  22:    */   
/*  23:    */   public void setScheduledTimerTasks(ScheduledTimerTask[] scheduledTimerTasks)
/*  24:    */   {
/*  25: 75 */     this.scheduledTimerTasks = scheduledTimerTasks;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setDaemon(boolean daemon)
/*  29:    */   {
/*  30: 88 */     this.daemon = daemon;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setBeanName(String beanName)
/*  34:    */   {
/*  35: 92 */     this.beanName = beanName;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void afterPropertiesSet()
/*  39:    */   {
/*  40: 97 */     this.logger.info("Initializing Timer");
/*  41: 98 */     this.timer = createTimer(this.beanName, this.daemon);
/*  42:101 */     if (!ObjectUtils.isEmpty(this.scheduledTimerTasks)) {
/*  43:102 */       registerTasks(this.scheduledTimerTasks, this.timer);
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected Timer createTimer(String name, boolean daemon)
/*  48:    */   {
/*  49:116 */     if (StringUtils.hasText(name)) {
/*  50:117 */       return new Timer(name, daemon);
/*  51:    */     }
/*  52:120 */     return new Timer(daemon);
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected void registerTasks(ScheduledTimerTask[] tasks, Timer timer)
/*  56:    */   {
/*  57:131 */     for (ScheduledTimerTask task : tasks) {
/*  58:132 */       if (task.isOneTimeTask()) {
/*  59:133 */         timer.schedule(task.getTimerTask(), task.getDelay());
/*  60:136 */       } else if (task.isFixedRate()) {
/*  61:137 */         timer.scheduleAtFixedRate(task.getTimerTask(), task.getDelay(), task.getPeriod());
/*  62:    */       } else {
/*  63:140 */         timer.schedule(task.getTimerTask(), task.getDelay(), task.getPeriod());
/*  64:    */       }
/*  65:    */     }
/*  66:    */   }
/*  67:    */   
/*  68:    */   public Timer getObject()
/*  69:    */   {
/*  70:148 */     return this.timer;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public Class<? extends Timer> getObjectType()
/*  74:    */   {
/*  75:152 */     return Timer.class;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public boolean isSingleton()
/*  79:    */   {
/*  80:156 */     return true;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void destroy()
/*  84:    */   {
/*  85:165 */     this.logger.info("Cancelling Timer");
/*  86:166 */     this.timer.cancel();
/*  87:    */   }
/*  88:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.timer.TimerFactoryBean
 * JD-Core Version:    0.7.0.1
 */