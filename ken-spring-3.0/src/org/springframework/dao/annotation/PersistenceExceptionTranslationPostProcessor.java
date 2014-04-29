/*   1:    */ package org.springframework.dao.annotation;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import org.springframework.aop.framework.Advised;
/*   5:    */ import org.springframework.aop.framework.AopInfrastructureBean;
/*   6:    */ import org.springframework.aop.framework.ProxyConfig;
/*   7:    */ import org.springframework.aop.framework.ProxyFactory;
/*   8:    */ import org.springframework.aop.support.AopUtils;
/*   9:    */ import org.springframework.beans.BeansException;
/*  10:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  11:    */ import org.springframework.beans.factory.BeanFactory;
/*  12:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*  13:    */ import org.springframework.beans.factory.ListableBeanFactory;
/*  14:    */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*  15:    */ import org.springframework.core.Ordered;
/*  16:    */ import org.springframework.stereotype.Repository;
/*  17:    */ import org.springframework.util.Assert;
/*  18:    */ import org.springframework.util.ClassUtils;
/*  19:    */ 
/*  20:    */ public class PersistenceExceptionTranslationPostProcessor
/*  21:    */   extends ProxyConfig
/*  22:    */   implements BeanPostProcessor, BeanClassLoaderAware, BeanFactoryAware, Ordered
/*  23:    */ {
/*  24: 73 */   private Class<? extends Annotation> repositoryAnnotationType = Repository.class;
/*  25: 75 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  26:    */   private PersistenceExceptionTranslationAdvisor persistenceExceptionTranslationAdvisor;
/*  27:    */   
/*  28:    */   public void setRepositoryAnnotationType(Class<? extends Annotation> repositoryAnnotationType)
/*  29:    */   {
/*  30: 89 */     Assert.notNull(repositoryAnnotationType, "'requiredAnnotationType' must not be null");
/*  31: 90 */     this.repositoryAnnotationType = repositoryAnnotationType;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setBeanClassLoader(ClassLoader classLoader)
/*  35:    */   {
/*  36: 94 */     this.beanClassLoader = classLoader;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  40:    */     throws BeansException
/*  41:    */   {
/*  42: 98 */     if (!(beanFactory instanceof ListableBeanFactory)) {
/*  43: 99 */       throw new IllegalArgumentException(
/*  44:100 */         "Cannot use PersistenceExceptionTranslator autodetection without ListableBeanFactory");
/*  45:    */     }
/*  46:102 */     this.persistenceExceptionTranslationAdvisor = new PersistenceExceptionTranslationAdvisor(
/*  47:103 */       (ListableBeanFactory)beanFactory, this.repositoryAnnotationType);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public int getOrder()
/*  51:    */   {
/*  52:109 */     return 2147483647;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public Object postProcessBeforeInitialization(Object bean, String beanName)
/*  56:    */   {
/*  57:114 */     return bean;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public Object postProcessAfterInitialization(Object bean, String beanName)
/*  61:    */   {
/*  62:118 */     if ((bean instanceof AopInfrastructureBean)) {
/*  63:120 */       return bean;
/*  64:    */     }
/*  65:122 */     Class<?> targetClass = AopUtils.getTargetClass(bean);
/*  66:123 */     if (AopUtils.canApply(this.persistenceExceptionTranslationAdvisor, targetClass))
/*  67:    */     {
/*  68:124 */       if ((bean instanceof Advised))
/*  69:    */       {
/*  70:125 */         ((Advised)bean).addAdvisor(this.persistenceExceptionTranslationAdvisor);
/*  71:126 */         return bean;
/*  72:    */       }
/*  73:129 */       ProxyFactory proxyFactory = new ProxyFactory(bean);
/*  74:    */       
/*  75:131 */       proxyFactory.copyFrom(this);
/*  76:132 */       proxyFactory.addAdvisor(this.persistenceExceptionTranslationAdvisor);
/*  77:133 */       return proxyFactory.getProxy(this.beanClassLoader);
/*  78:    */     }
/*  79:138 */     return bean;
/*  80:    */   }
/*  81:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor
 * JD-Core Version:    0.7.0.1
 */