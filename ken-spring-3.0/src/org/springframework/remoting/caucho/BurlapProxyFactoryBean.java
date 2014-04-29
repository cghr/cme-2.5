/*  1:   */ package org.springframework.remoting.caucho;
/*  2:   */ 
/*  3:   */ import org.springframework.aop.framework.ProxyFactory;
/*  4:   */ import org.springframework.beans.factory.FactoryBean;
/*  5:   */ 
/*  6:   */ public class BurlapProxyFactoryBean
/*  7:   */   extends BurlapClientInterceptor
/*  8:   */   implements FactoryBean<Object>
/*  9:   */ {
/* 10:   */   private Object serviceProxy;
/* 11:   */   
/* 12:   */   public void afterPropertiesSet()
/* 13:   */   {
/* 14:50 */     super.afterPropertiesSet();
/* 15:51 */     this.serviceProxy = new ProxyFactory(getServiceInterface(), this).getProxy(getBeanClassLoader());
/* 16:   */   }
/* 17:   */   
/* 18:   */   public Object getObject()
/* 19:   */   {
/* 20:56 */     return this.serviceProxy;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public Class<?> getObjectType()
/* 24:   */   {
/* 25:60 */     return getServiceInterface();
/* 26:   */   }
/* 27:   */   
/* 28:   */   public boolean isSingleton()
/* 29:   */   {
/* 30:64 */     return true;
/* 31:   */   }
/* 32:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.caucho.BurlapProxyFactoryBean
 * JD-Core Version:    0.7.0.1
 */