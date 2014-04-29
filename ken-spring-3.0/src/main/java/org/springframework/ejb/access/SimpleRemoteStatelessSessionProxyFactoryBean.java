/*   1:    */ package org.springframework.ejb.access;
/*   2:    */ 
/*   3:    */ import javax.naming.NamingException;
/*   4:    */ import org.springframework.aop.framework.ProxyFactory;
/*   5:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*   6:    */ import org.springframework.beans.factory.FactoryBean;
/*   7:    */ import org.springframework.util.ClassUtils;
/*   8:    */ 
/*   9:    */ public class SimpleRemoteStatelessSessionProxyFactoryBean
/*  10:    */   extends SimpleRemoteSlsbInvokerInterceptor
/*  11:    */   implements FactoryBean<Object>, BeanClassLoaderAware
/*  12:    */ {
/*  13:    */   private Class businessInterface;
/*  14: 67 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  15:    */   private Object proxy;
/*  16:    */   
/*  17:    */   public void setBusinessInterface(Class businessInterface)
/*  18:    */   {
/*  19: 84 */     this.businessInterface = businessInterface;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public Class getBusinessInterface()
/*  23:    */   {
/*  24: 91 */     return this.businessInterface;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setBeanClassLoader(ClassLoader classLoader)
/*  28:    */   {
/*  29: 95 */     this.beanClassLoader = classLoader;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void afterPropertiesSet()
/*  33:    */     throws NamingException
/*  34:    */   {
/*  35:100 */     super.afterPropertiesSet();
/*  36:101 */     if (this.businessInterface == null) {
/*  37:102 */       throw new IllegalArgumentException("businessInterface is required");
/*  38:    */     }
/*  39:104 */     this.proxy = new ProxyFactory(this.businessInterface, this).getProxy(this.beanClassLoader);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public Object getObject()
/*  43:    */   {
/*  44:109 */     return this.proxy;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public Class<?> getObjectType()
/*  48:    */   {
/*  49:113 */     return this.businessInterface;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public boolean isSingleton()
/*  53:    */   {
/*  54:117 */     return true;
/*  55:    */   }
/*  56:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ejb.access.SimpleRemoteStatelessSessionProxyFactoryBean
 * JD-Core Version:    0.7.0.1
 */