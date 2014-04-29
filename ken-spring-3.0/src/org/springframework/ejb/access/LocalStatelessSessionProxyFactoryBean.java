/*   1:    */ package org.springframework.ejb.access;
/*   2:    */ 
/*   3:    */ import javax.naming.NamingException;
/*   4:    */ import org.springframework.aop.framework.ProxyFactory;
/*   5:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*   6:    */ import org.springframework.beans.factory.FactoryBean;
/*   7:    */ import org.springframework.util.ClassUtils;
/*   8:    */ 
/*   9:    */ public class LocalStatelessSessionProxyFactoryBean
/*  10:    */   extends LocalSlsbInvokerInterceptor
/*  11:    */   implements FactoryBean<Object>, BeanClassLoaderAware
/*  12:    */ {
/*  13:    */   private Class businessInterface;
/*  14: 57 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  15:    */   private Object proxy;
/*  16:    */   
/*  17:    */   public void setBusinessInterface(Class businessInterface)
/*  18:    */   {
/*  19: 70 */     this.businessInterface = businessInterface;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public Class getBusinessInterface()
/*  23:    */   {
/*  24: 77 */     return this.businessInterface;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setBeanClassLoader(ClassLoader classLoader)
/*  28:    */   {
/*  29: 81 */     this.beanClassLoader = classLoader;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void afterPropertiesSet()
/*  33:    */     throws NamingException
/*  34:    */   {
/*  35: 86 */     super.afterPropertiesSet();
/*  36: 87 */     if (this.businessInterface == null) {
/*  37: 88 */       throw new IllegalArgumentException("businessInterface is required");
/*  38:    */     }
/*  39: 90 */     this.proxy = new ProxyFactory(this.businessInterface, this).getProxy(this.beanClassLoader);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public Object getObject()
/*  43:    */   {
/*  44: 95 */     return this.proxy;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public Class<?> getObjectType()
/*  48:    */   {
/*  49: 99 */     return this.businessInterface;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public boolean isSingleton()
/*  53:    */   {
/*  54:103 */     return true;
/*  55:    */   }
/*  56:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ejb.access.LocalStatelessSessionProxyFactoryBean
 * JD-Core Version:    0.7.0.1
 */