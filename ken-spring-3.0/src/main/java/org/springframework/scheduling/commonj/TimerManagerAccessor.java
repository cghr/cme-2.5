/*   1:    */ package org.springframework.scheduling.commonj;
/*   2:    */ 
/*   3:    */ import commonj.timers.TimerManager;
/*   4:    */ import javax.naming.NamingException;
/*   5:    */ import org.springframework.beans.factory.DisposableBean;
/*   6:    */ import org.springframework.beans.factory.InitializingBean;
/*   7:    */ import org.springframework.context.Lifecycle;
/*   8:    */ import org.springframework.jndi.JndiLocatorSupport;
/*   9:    */ 
/*  10:    */ public abstract class TimerManagerAccessor
/*  11:    */   extends JndiLocatorSupport
/*  12:    */   implements InitializingBean, DisposableBean, Lifecycle
/*  13:    */ {
/*  14:    */   private TimerManager timerManager;
/*  15:    */   private String timerManagerName;
/*  16: 43 */   private boolean shared = false;
/*  17:    */   
/*  18:    */   public void setTimerManager(TimerManager timerManager)
/*  19:    */   {
/*  20: 55 */     this.timerManager = timerManager;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void setTimerManagerName(String timerManagerName)
/*  24:    */   {
/*  25: 66 */     this.timerManagerName = timerManagerName;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setShared(boolean shared)
/*  29:    */   {
/*  30: 94 */     this.shared = shared;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void afterPropertiesSet()
/*  34:    */     throws NamingException
/*  35:    */   {
/*  36: 99 */     if (this.timerManager == null)
/*  37:    */     {
/*  38:100 */       if (this.timerManagerName == null) {
/*  39:101 */         throw new IllegalArgumentException("Either 'timerManager' or 'timerManagerName' must be specified");
/*  40:    */       }
/*  41:103 */       this.timerManager = ((TimerManager)lookup(this.timerManagerName, TimerManager.class));
/*  42:    */     }
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected final TimerManager getTimerManager()
/*  46:    */   {
/*  47:108 */     return this.timerManager;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void start()
/*  51:    */   {
/*  52:121 */     if (!this.shared) {
/*  53:122 */       this.timerManager.resume();
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void stop()
/*  58:    */   {
/*  59:131 */     if (!this.shared) {
/*  60:132 */       this.timerManager.suspend();
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   public boolean isRunning()
/*  65:    */   {
/*  66:143 */     return (!this.timerManager.isSuspending()) && (!this.timerManager.isStopping());
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void destroy()
/*  70:    */   {
/*  71:157 */     if (!this.shared) {
/*  72:159 */       this.timerManager.stop();
/*  73:    */     }
/*  74:    */   }
/*  75:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.commonj.TimerManagerAccessor
 * JD-Core Version:    0.7.0.1
 */