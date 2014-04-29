/*  1:   */ package org.springframework.remoting.httpinvoker;
/*  2:   */ 
/*  3:   */ import org.springframework.aop.framework.ProxyFactory;
/*  4:   */ import org.springframework.beans.factory.FactoryBean;
/*  5:   */ 
/*  6:   */ public class HttpInvokerProxyFactoryBean
/*  7:   */   extends HttpInvokerClientInterceptor
/*  8:   */   implements FactoryBean<Object>
/*  9:   */ {
/* 10:   */   private Object serviceProxy;
/* 11:   */   
/* 12:   */   public void afterPropertiesSet()
/* 13:   */   {
/* 14:58 */     super.afterPropertiesSet();
/* 15:59 */     if (getServiceInterface() == null) {
/* 16:60 */       throw new IllegalArgumentException("Property 'serviceInterface' is required");
/* 17:   */     }
/* 18:62 */     this.serviceProxy = new ProxyFactory(getServiceInterface(), this).getProxy(getBeanClassLoader());
/* 19:   */   }
/* 20:   */   
/* 21:   */   public Object getObject()
/* 22:   */   {
/* 23:67 */     return this.serviceProxy;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public Class<?> getObjectType()
/* 27:   */   {
/* 28:71 */     return getServiceInterface();
/* 29:   */   }
/* 30:   */   
/* 31:   */   public boolean isSingleton()
/* 32:   */   {
/* 33:75 */     return true;
/* 34:   */   }
/* 35:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean
 * JD-Core Version:    0.7.0.1
 */