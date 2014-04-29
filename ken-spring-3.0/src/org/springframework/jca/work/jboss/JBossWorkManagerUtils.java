/*  1:   */ package org.springframework.jca.work.jboss;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Method;
/*  4:   */ import javax.management.MBeanServerConnection;
/*  5:   */ import javax.management.MBeanServerInvocationHandler;
/*  6:   */ import javax.management.ObjectName;
/*  7:   */ import javax.naming.InitialContext;
/*  8:   */ import javax.resource.spi.work.WorkManager;
/*  9:   */ import org.springframework.util.Assert;
/* 10:   */ 
/* 11:   */ public abstract class JBossWorkManagerUtils
/* 12:   */ {
/* 13:   */   private static final String JBOSS_WORK_MANAGER_MBEAN_CLASS_NAME = "org.jboss.resource.work.JBossWorkManagerMBean";
/* 14:   */   private static final String MBEAN_SERVER_CONNECTION_JNDI_NAME = "jmx/invoker/RMIAdaptor";
/* 15:   */   private static final String DEFAULT_WORK_MANAGER_MBEAN_NAME = "jboss.jca:service=WorkManager";
/* 16:   */   
/* 17:   */   public static WorkManager getWorkManager()
/* 18:   */   {
/* 19:50 */     return getWorkManager("jboss.jca:service=WorkManager");
/* 20:   */   }
/* 21:   */   
/* 22:   */   public static WorkManager getWorkManager(String mbeanName)
/* 23:   */   {
/* 24:60 */     Assert.hasLength(mbeanName, "JBossWorkManagerMBean name must not be empty");
/* 25:   */     try
/* 26:   */     {
/* 27:62 */       Class<?> mbeanClass = JBossWorkManagerUtils.class.getClassLoader().loadClass("org.jboss.resource.work.JBossWorkManagerMBean");
/* 28:63 */       InitialContext jndiContext = new InitialContext();
/* 29:64 */       MBeanServerConnection mconn = (MBeanServerConnection)jndiContext.lookup("jmx/invoker/RMIAdaptor");
/* 30:65 */       ObjectName objectName = ObjectName.getInstance(mbeanName);
/* 31:66 */       Object workManagerMBean = MBeanServerInvocationHandler.newProxyInstance(mconn, objectName, mbeanClass, false);
/* 32:67 */       Method getInstanceMethod = workManagerMBean.getClass().getMethod("getInstance", new Class[0]);
/* 33:68 */       return (WorkManager)getInstanceMethod.invoke(workManagerMBean, new Object[0]);
/* 34:   */     }
/* 35:   */     catch (Exception ex)
/* 36:   */     {
/* 37:71 */       throw new IllegalStateException(
/* 38:72 */         "Could not initialize JBossWorkManagerTaskExecutor because JBoss API is not available: " + ex);
/* 39:   */     }
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.work.jboss.JBossWorkManagerUtils
 * JD-Core Version:    0.7.0.1
 */