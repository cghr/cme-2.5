/*  1:   */ package org.springframework.remoting.rmi;
/*  2:   */ 
/*  3:   */ import org.springframework.aop.framework.ProxyFactory;
/*  4:   */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  5:   */ import org.springframework.beans.factory.FactoryBean;
/*  6:   */ 
/*  7:   */ public class RmiProxyFactoryBean
/*  8:   */   extends RmiClientInterceptor
/*  9:   */   implements FactoryBean<Object>, BeanClassLoaderAware
/* 10:   */ {
/* 11:   */   private Object serviceProxy;
/* 12:   */   
/* 13:   */   public void afterPropertiesSet()
/* 14:   */   {
/* 15:68 */     super.afterPropertiesSet();
/* 16:69 */     if (getServiceInterface() == null) {
/* 17:70 */       throw new IllegalArgumentException("Property 'serviceInterface' is required");
/* 18:   */     }
/* 19:72 */     this.serviceProxy = new ProxyFactory(getServiceInterface(), this).getProxy(getBeanClassLoader());
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Object getObject()
/* 23:   */   {
/* 24:77 */     return this.serviceProxy;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public Class<?> getObjectType()
/* 28:   */   {
/* 29:81 */     return getServiceInterface();
/* 30:   */   }
/* 31:   */   
/* 32:   */   public boolean isSingleton()
/* 33:   */   {
/* 34:85 */     return true;
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.rmi.RmiProxyFactoryBean
 * JD-Core Version:    0.7.0.1
 */