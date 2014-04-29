/*  1:   */ package org.springframework.beans.factory.serviceloader;
/*  2:   */ 
/*  3:   */ import java.util.ServiceLoader;
/*  4:   */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  5:   */ import org.springframework.beans.factory.config.AbstractFactoryBean;
/*  6:   */ import org.springframework.util.Assert;
/*  7:   */ import org.springframework.util.ClassUtils;
/*  8:   */ 
/*  9:   */ public abstract class AbstractServiceLoaderBasedFactoryBean
/* 10:   */   extends AbstractFactoryBean
/* 11:   */   implements BeanClassLoaderAware
/* 12:   */ {
/* 13:   */   private Class serviceType;
/* 14:39 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/* 15:   */   
/* 16:   */   public void setServiceType(Class serviceType)
/* 17:   */   {
/* 18:46 */     this.serviceType = serviceType;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public Class getServiceType()
/* 22:   */   {
/* 23:53 */     return this.serviceType;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void setBeanClassLoader(ClassLoader beanClassLoader)
/* 27:   */   {
/* 28:58 */     this.beanClassLoader = beanClassLoader;
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected Object createInstance()
/* 32:   */   {
/* 33:68 */     Assert.notNull(getServiceType(), "Property 'serviceType' is required");
/* 34:69 */     return getObjectToExpose(ServiceLoader.load(getServiceType(), this.beanClassLoader));
/* 35:   */   }
/* 36:   */   
/* 37:   */   protected abstract Object getObjectToExpose(ServiceLoader paramServiceLoader);
/* 38:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.serviceloader.AbstractServiceLoaderBasedFactoryBean
 * JD-Core Version:    0.7.0.1
 */