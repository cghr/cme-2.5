/*  1:   */ package org.springframework.remoting.rmi;
/*  2:   */ 
/*  3:   */ import javax.naming.NamingException;
/*  4:   */ import org.springframework.aop.framework.ProxyFactory;
/*  5:   */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  6:   */ import org.springframework.beans.factory.FactoryBean;
/*  7:   */ import org.springframework.util.ClassUtils;
/*  8:   */ 
/*  9:   */ public class JndiRmiProxyFactoryBean
/* 10:   */   extends JndiRmiClientInterceptor
/* 11:   */   implements FactoryBean<Object>, BeanClassLoaderAware
/* 12:   */ {
/* 13:67 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/* 14:   */   private Object serviceProxy;
/* 15:   */   
/* 16:   */   public void setBeanClassLoader(ClassLoader classLoader)
/* 17:   */   {
/* 18:73 */     this.beanClassLoader = classLoader;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void afterPropertiesSet()
/* 22:   */     throws NamingException
/* 23:   */   {
/* 24:78 */     super.afterPropertiesSet();
/* 25:79 */     if (getServiceInterface() == null) {
/* 26:80 */       throw new IllegalArgumentException("Property 'serviceInterface' is required");
/* 27:   */     }
/* 28:82 */     this.serviceProxy = new ProxyFactory(getServiceInterface(), this).getProxy(this.beanClassLoader);
/* 29:   */   }
/* 30:   */   
/* 31:   */   public Object getObject()
/* 32:   */   {
/* 33:87 */     return this.serviceProxy;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public Class<?> getObjectType()
/* 37:   */   {
/* 38:91 */     return getServiceInterface();
/* 39:   */   }
/* 40:   */   
/* 41:   */   public boolean isSingleton()
/* 42:   */   {
/* 43:95 */     return true;
/* 44:   */   }
/* 45:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.rmi.JndiRmiProxyFactoryBean
 * JD-Core Version:    0.7.0.1
 */