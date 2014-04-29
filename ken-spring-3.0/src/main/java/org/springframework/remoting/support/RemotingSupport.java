/*  1:   */ package org.springframework.remoting.support;
/*  2:   */ 
/*  3:   */ import org.apache.commons.logging.Log;
/*  4:   */ import org.apache.commons.logging.LogFactory;
/*  5:   */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  6:   */ import org.springframework.util.ClassUtils;
/*  7:   */ 
/*  8:   */ public abstract class RemotingSupport
/*  9:   */   implements BeanClassLoaderAware
/* 10:   */ {
/* 11:35 */   protected final Log logger = LogFactory.getLog(getClass());
/* 12:37 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/* 13:   */   
/* 14:   */   public void setBeanClassLoader(ClassLoader classLoader)
/* 15:   */   {
/* 16:41 */     this.beanClassLoader = classLoader;
/* 17:   */   }
/* 18:   */   
/* 19:   */   protected ClassLoader getBeanClassLoader()
/* 20:   */   {
/* 21:49 */     return this.beanClassLoader;
/* 22:   */   }
/* 23:   */   
/* 24:   */   protected ClassLoader overrideThreadContextClassLoader()
/* 25:   */   {
/* 26:60 */     return ClassUtils.overrideThreadContextClassLoader(getBeanClassLoader());
/* 27:   */   }
/* 28:   */   
/* 29:   */   protected void resetThreadContextClassLoader(ClassLoader original)
/* 30:   */   {
/* 31:69 */     if (original != null) {
/* 32:70 */       Thread.currentThread().setContextClassLoader(original);
/* 33:   */     }
/* 34:   */   }
/* 35:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.support.RemotingSupport
 * JD-Core Version:    0.7.0.1
 */