/*  1:   */ package org.springframework.jmx.support;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.InvocationTargetException;
/*  4:   */ import java.lang.reflect.Method;
/*  5:   */ import javax.management.MBeanServer;
/*  6:   */ import org.springframework.beans.factory.FactoryBean;
/*  7:   */ import org.springframework.beans.factory.InitializingBean;
/*  8:   */ import org.springframework.jmx.MBeanServerNotFoundException;
/*  9:   */ 
/* 10:   */ public class WebSphereMBeanServerFactoryBean
/* 11:   */   implements FactoryBean<MBeanServer>, InitializingBean
/* 12:   */ {
/* 13:   */   private static final String ADMIN_SERVICE_FACTORY_CLASS = "com.ibm.websphere.management.AdminServiceFactory";
/* 14:   */   private static final String GET_MBEAN_FACTORY_METHOD = "getMBeanFactory";
/* 15:   */   private static final String GET_MBEAN_SERVER_METHOD = "getMBeanServer";
/* 16:   */   private MBeanServer mbeanServer;
/* 17:   */   
/* 18:   */   public void afterPropertiesSet()
/* 19:   */     throws MBeanServerNotFoundException
/* 20:   */   {
/* 21:   */     try
/* 22:   */     {
/* 23:62 */       Class adminServiceClass = getClass().getClassLoader().loadClass("com.ibm.websphere.management.AdminServiceFactory");
/* 24:63 */       Method getMBeanFactoryMethod = adminServiceClass.getMethod("getMBeanFactory", new Class[0]);
/* 25:64 */       Object mbeanFactory = getMBeanFactoryMethod.invoke(null, new Object[0]);
/* 26:65 */       Method getMBeanServerMethod = mbeanFactory.getClass().getMethod("getMBeanServer", new Class[0]);
/* 27:66 */       this.mbeanServer = ((MBeanServer)getMBeanServerMethod.invoke(mbeanFactory, new Object[0]));
/* 28:   */     }
/* 29:   */     catch (ClassNotFoundException ex)
/* 30:   */     {
/* 31:69 */       throw new MBeanServerNotFoundException("Could not find WebSphere's AdminServiceFactory class", ex);
/* 32:   */     }
/* 33:   */     catch (InvocationTargetException ex)
/* 34:   */     {
/* 35:72 */       throw new MBeanServerNotFoundException(
/* 36:73 */         "WebSphere's AdminServiceFactory.getMBeanFactory/getMBeanServer method failed", ex.getTargetException());
/* 37:   */     }
/* 38:   */     catch (Exception ex)
/* 39:   */     {
/* 40:76 */       throw new MBeanServerNotFoundException(
/* 41:77 */         "Could not access WebSphere's AdminServiceFactory.getMBeanFactory/getMBeanServer method", ex);
/* 42:   */     }
/* 43:   */   }
/* 44:   */   
/* 45:   */   public MBeanServer getObject()
/* 46:   */   {
/* 47:83 */     return this.mbeanServer;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public Class<? extends MBeanServer> getObjectType()
/* 51:   */   {
/* 52:87 */     return this.mbeanServer != null ? this.mbeanServer.getClass() : MBeanServer.class;
/* 53:   */   }
/* 54:   */   
/* 55:   */   public boolean isSingleton()
/* 56:   */   {
/* 57:91 */     return true;
/* 58:   */   }
/* 59:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.support.WebSphereMBeanServerFactoryBean
 * JD-Core Version:    0.7.0.1
 */