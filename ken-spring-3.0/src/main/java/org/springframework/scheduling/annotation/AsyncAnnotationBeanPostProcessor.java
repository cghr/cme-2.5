/*   1:    */ package org.springframework.scheduling.annotation;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.util.concurrent.Executor;
/*   5:    */ import org.springframework.aop.framework.Advised;
/*   6:    */ import org.springframework.aop.framework.AopInfrastructureBean;
/*   7:    */ import org.springframework.aop.framework.ProxyConfig;
/*   8:    */ import org.springframework.aop.framework.ProxyFactory;
/*   9:    */ import org.springframework.aop.support.AopUtils;
/*  10:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  11:    */ import org.springframework.beans.factory.InitializingBean;
/*  12:    */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*  13:    */ import org.springframework.core.Ordered;
/*  14:    */ import org.springframework.util.Assert;
/*  15:    */ import org.springframework.util.ClassUtils;
/*  16:    */ 
/*  17:    */ public class AsyncAnnotationBeanPostProcessor
/*  18:    */   extends ProxyConfig
/*  19:    */   implements BeanPostProcessor, BeanClassLoaderAware, InitializingBean, Ordered
/*  20:    */ {
/*  21:    */   private Class<? extends Annotation> asyncAnnotationType;
/*  22:    */   private Executor executor;
/*  23: 62 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  24:    */   private AsyncAnnotationAdvisor asyncAnnotationAdvisor;
/*  25: 70 */   private int order = 2147483647;
/*  26:    */   
/*  27:    */   public void setAsyncAnnotationType(Class<? extends Annotation> asyncAnnotationType)
/*  28:    */   {
/*  29: 83 */     Assert.notNull(asyncAnnotationType, "'asyncAnnotationType' must not be null");
/*  30: 84 */     this.asyncAnnotationType = asyncAnnotationType;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setExecutor(Executor executor)
/*  34:    */   {
/*  35: 91 */     this.executor = executor;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setBeanClassLoader(ClassLoader classLoader)
/*  39:    */   {
/*  40: 95 */     this.beanClassLoader = classLoader;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void afterPropertiesSet()
/*  44:    */   {
/*  45: 99 */     this.asyncAnnotationAdvisor = (this.executor != null ? 
/*  46:100 */       new AsyncAnnotationAdvisor(this.executor) : new AsyncAnnotationAdvisor());
/*  47:101 */     if (this.asyncAnnotationType != null) {
/*  48:102 */       this.asyncAnnotationAdvisor.setAsyncAnnotationType(this.asyncAnnotationType);
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   public int getOrder()
/*  53:    */   {
/*  54:107 */     return this.order;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setOrder(int order)
/*  58:    */   {
/*  59:111 */     this.order = order;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public Object postProcessBeforeInitialization(Object bean, String beanName)
/*  63:    */   {
/*  64:116 */     return bean;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public Object postProcessAfterInitialization(Object bean, String beanName)
/*  68:    */   {
/*  69:120 */     if ((bean instanceof AopInfrastructureBean)) {
/*  70:122 */       return bean;
/*  71:    */     }
/*  72:124 */     Class<?> targetClass = AopUtils.getTargetClass(bean);
/*  73:125 */     if (AopUtils.canApply(this.asyncAnnotationAdvisor, targetClass))
/*  74:    */     {
/*  75:126 */       if ((bean instanceof Advised))
/*  76:    */       {
/*  77:127 */         ((Advised)bean).addAdvisor(0, this.asyncAnnotationAdvisor);
/*  78:128 */         return bean;
/*  79:    */       }
/*  80:131 */       ProxyFactory proxyFactory = new ProxyFactory(bean);
/*  81:    */       
/*  82:133 */       proxyFactory.copyFrom(this);
/*  83:134 */       proxyFactory.addAdvisor(this.asyncAnnotationAdvisor);
/*  84:135 */       return proxyFactory.getProxy(this.beanClassLoader);
/*  85:    */     }
/*  86:140 */     return bean;
/*  87:    */   }
/*  88:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor
 * JD-Core Version:    0.7.0.1
 */