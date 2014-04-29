/*  1:   */ package org.springframework.scheduling.support;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.InvocationTargetException;
/*  4:   */ import org.apache.commons.logging.Log;
/*  5:   */ import org.apache.commons.logging.LogFactory;
/*  6:   */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  7:   */ import org.springframework.beans.factory.InitializingBean;
/*  8:   */ import org.springframework.beans.support.ArgumentConvertingMethodInvoker;
/*  9:   */ import org.springframework.util.ClassUtils;
/* 10:   */ 
/* 11:   */ public class MethodInvokingRunnable
/* 12:   */   extends ArgumentConvertingMethodInvoker
/* 13:   */   implements Runnable, BeanClassLoaderAware, InitializingBean
/* 14:   */ {
/* 15:44 */   protected final Log logger = LogFactory.getLog(getClass());
/* 16:46 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/* 17:   */   
/* 18:   */   public void setBeanClassLoader(ClassLoader classLoader)
/* 19:   */   {
/* 20:50 */     this.beanClassLoader = classLoader;
/* 21:   */   }
/* 22:   */   
/* 23:   */   protected Class resolveClassName(String className)
/* 24:   */     throws ClassNotFoundException
/* 25:   */   {
/* 26:55 */     return ClassUtils.forName(className, this.beanClassLoader);
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void afterPropertiesSet()
/* 30:   */     throws ClassNotFoundException, NoSuchMethodException
/* 31:   */   {
/* 32:59 */     prepare();
/* 33:   */   }
/* 34:   */   
/* 35:   */   public void run()
/* 36:   */   {
/* 37:   */     try
/* 38:   */     {
/* 39:65 */       invoke();
/* 40:   */     }
/* 41:   */     catch (InvocationTargetException ex)
/* 42:   */     {
/* 43:68 */       this.logger.error(getInvocationFailureMessage(), ex.getTargetException());
/* 44:   */     }
/* 45:   */     catch (Throwable ex)
/* 46:   */     {
/* 47:72 */       this.logger.error(getInvocationFailureMessage(), ex);
/* 48:   */     }
/* 49:   */   }
/* 50:   */   
/* 51:   */   protected String getInvocationFailureMessage()
/* 52:   */   {
/* 53:82 */     return 
/* 54:83 */       "Invocation of method '" + getTargetMethod() + "' on target class [" + getTargetClass() + "] failed";
/* 55:   */   }
/* 56:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.support.MethodInvokingRunnable
 * JD-Core Version:    0.7.0.1
 */