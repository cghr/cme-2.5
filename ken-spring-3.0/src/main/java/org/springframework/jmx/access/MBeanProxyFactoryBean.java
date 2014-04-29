/*   1:    */ package org.springframework.jmx.access;
/*   2:    */ 
/*   3:    */ import org.springframework.aop.framework.ProxyFactory;
/*   4:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*   5:    */ import org.springframework.beans.factory.FactoryBean;
/*   6:    */ import org.springframework.beans.factory.InitializingBean;
/*   7:    */ import org.springframework.jmx.MBeanServerNotFoundException;
/*   8:    */ import org.springframework.util.ClassUtils;
/*   9:    */ 
/*  10:    */ public class MBeanProxyFactoryBean
/*  11:    */   extends MBeanClientInterceptor
/*  12:    */   implements FactoryBean<Object>, BeanClassLoaderAware, InitializingBean
/*  13:    */ {
/*  14:    */   private Class proxyInterface;
/*  15: 53 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  16:    */   private Object mbeanProxy;
/*  17:    */   
/*  18:    */   public void setProxyInterface(Class proxyInterface)
/*  19:    */   {
/*  20: 66 */     this.proxyInterface = proxyInterface;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void setBeanClassLoader(ClassLoader classLoader)
/*  24:    */   {
/*  25: 71 */     this.beanClassLoader = classLoader;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void afterPropertiesSet()
/*  29:    */     throws MBeanServerNotFoundException, MBeanInfoRetrievalException
/*  30:    */   {
/*  31: 80 */     super.afterPropertiesSet();
/*  32: 82 */     if (this.proxyInterface == null)
/*  33:    */     {
/*  34: 83 */       this.proxyInterface = getManagementInterface();
/*  35: 84 */       if (this.proxyInterface == null) {
/*  36: 85 */         throw new IllegalArgumentException("Property 'proxyInterface' or 'managementInterface' is required");
/*  37:    */       }
/*  38:    */     }
/*  39: 89 */     else if (getManagementInterface() == null)
/*  40:    */     {
/*  41: 90 */       setManagementInterface(this.proxyInterface);
/*  42:    */     }
/*  43: 93 */     this.mbeanProxy = new ProxyFactory(this.proxyInterface, this).getProxy(this.beanClassLoader);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public Object getObject()
/*  47:    */   {
/*  48: 98 */     return this.mbeanProxy;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public Class<?> getObjectType()
/*  52:    */   {
/*  53:102 */     return this.proxyInterface;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public boolean isSingleton()
/*  57:    */   {
/*  58:106 */     return true;
/*  59:    */   }
/*  60:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.access.MBeanProxyFactoryBean
 * JD-Core Version:    0.7.0.1
 */