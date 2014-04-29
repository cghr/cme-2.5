/*  1:   */ package org.springframework.remoting.jaxrpc;
/*  2:   */ 
/*  3:   */ import org.springframework.aop.framework.ProxyFactory;
/*  4:   */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  5:   */ import org.springframework.beans.factory.FactoryBean;
/*  6:   */ import org.springframework.util.ClassUtils;
/*  7:   */ 
/*  8:   */ @Deprecated
/*  9:   */ public class JaxRpcPortProxyFactoryBean
/* 10:   */   extends JaxRpcPortClientInterceptor
/* 11:   */   implements FactoryBean<Object>, BeanClassLoaderAware
/* 12:   */ {
/* 13:52 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/* 14:   */   private Object serviceProxy;
/* 15:   */   
/* 16:   */   public void setBeanClassLoader(ClassLoader classLoader)
/* 17:   */   {
/* 18:58 */     this.beanClassLoader = classLoader;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void afterPropertiesSet()
/* 22:   */   {
/* 23:63 */     if (getServiceInterface() == null) {
/* 24:66 */       if (getPortInterface() != null) {
/* 25:67 */         setServiceInterface(getPortInterface());
/* 26:   */       } else {
/* 27:70 */         throw new IllegalArgumentException("Property 'serviceInterface' is required");
/* 28:   */       }
/* 29:   */     }
/* 30:73 */     super.afterPropertiesSet();
/* 31:74 */     this.serviceProxy = new ProxyFactory(getServiceInterface(), this).getProxy(this.beanClassLoader);
/* 32:   */   }
/* 33:   */   
/* 34:   */   public Object getObject()
/* 35:   */   {
/* 36:79 */     return this.serviceProxy;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public Class<?> getObjectType()
/* 40:   */   {
/* 41:83 */     return getServiceInterface();
/* 42:   */   }
/* 43:   */   
/* 44:   */   public boolean isSingleton()
/* 45:   */   {
/* 46:87 */     return true;
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.jaxrpc.JaxRpcPortProxyFactoryBean
 * JD-Core Version:    0.7.0.1
 */