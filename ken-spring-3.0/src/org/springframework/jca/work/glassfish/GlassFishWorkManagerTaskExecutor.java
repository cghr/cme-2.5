/*  1:   */ package org.springframework.jca.work.glassfish;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Method;
/*  4:   */ import javax.resource.spi.work.WorkManager;
/*  5:   */ import org.springframework.jca.work.WorkManagerTaskExecutor;
/*  6:   */ import org.springframework.util.ReflectionUtils;
/*  7:   */ 
/*  8:   */ public class GlassFishWorkManagerTaskExecutor
/*  9:   */   extends WorkManagerTaskExecutor
/* 10:   */ {
/* 11:   */   private static final String WORK_MANAGER_FACTORY_CLASS = "com.sun.enterprise.connectors.work.WorkManagerFactory";
/* 12:   */   private final Method getWorkManagerMethod;
/* 13:   */   
/* 14:   */   public GlassFishWorkManagerTaskExecutor()
/* 15:   */   {
/* 16:   */     try
/* 17:   */     {
/* 18:47 */       Class wmf = getClass().getClassLoader().loadClass("com.sun.enterprise.connectors.work.WorkManagerFactory");
/* 19:48 */       this.getWorkManagerMethod = wmf.getMethod("getWorkManager", new Class[] { String.class });
/* 20:   */     }
/* 21:   */     catch (Exception ex)
/* 22:   */     {
/* 23:51 */       throw new IllegalStateException(
/* 24:52 */         "Could not initialize GlassFishWorkManagerTaskExecutor because GlassFish API is not available: " + ex);
/* 25:   */     }
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void setThreadPoolName(String threadPoolName)
/* 29:   */   {
/* 30:62 */     WorkManager wm = (WorkManager)ReflectionUtils.invokeMethod(this.getWorkManagerMethod, null, new Object[] { threadPoolName });
/* 31:63 */     if (wm == null) {
/* 32:64 */       throw new IllegalArgumentException("Specified thread pool name '" + threadPoolName + 
/* 33:65 */         "' does not correspond to an actual pool definition in GlassFish. Check your configuration!");
/* 34:   */     }
/* 35:67 */     setWorkManager(wm);
/* 36:   */   }
/* 37:   */   
/* 38:   */   protected WorkManager getDefaultWorkManager()
/* 39:   */   {
/* 40:75 */     return (WorkManager)ReflectionUtils.invokeMethod(this.getWorkManagerMethod, null, new Object[1]);
/* 41:   */   }
/* 42:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.work.glassfish.GlassFishWorkManagerTaskExecutor
 * JD-Core Version:    0.7.0.1
 */