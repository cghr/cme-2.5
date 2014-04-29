/*   1:    */ package org.springframework.dao.support;
/*   2:    */ 
/*   3:    */ import java.util.Map;
/*   4:    */ import org.aopalliance.intercept.MethodInterceptor;
/*   5:    */ import org.aopalliance.intercept.MethodInvocation;
/*   6:    */ import org.springframework.beans.BeansException;
/*   7:    */ import org.springframework.beans.factory.BeanFactory;
/*   8:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*   9:    */ import org.springframework.beans.factory.BeanFactoryUtils;
/*  10:    */ import org.springframework.beans.factory.InitializingBean;
/*  11:    */ import org.springframework.beans.factory.ListableBeanFactory;
/*  12:    */ import org.springframework.util.Assert;
/*  13:    */ import org.springframework.util.ReflectionUtils;
/*  14:    */ 
/*  15:    */ public class PersistenceExceptionTranslationInterceptor
/*  16:    */   implements MethodInterceptor, BeanFactoryAware, InitializingBean
/*  17:    */ {
/*  18:    */   private PersistenceExceptionTranslator persistenceExceptionTranslator;
/*  19: 52 */   private boolean alwaysTranslate = false;
/*  20:    */   
/*  21:    */   public PersistenceExceptionTranslationInterceptor() {}
/*  22:    */   
/*  23:    */   public PersistenceExceptionTranslationInterceptor(PersistenceExceptionTranslator persistenceExceptionTranslator)
/*  24:    */   {
/*  25: 69 */     setPersistenceExceptionTranslator(persistenceExceptionTranslator);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public PersistenceExceptionTranslationInterceptor(ListableBeanFactory beanFactory)
/*  29:    */   {
/*  30: 79 */     this.persistenceExceptionTranslator = detectPersistenceExceptionTranslators(beanFactory);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setPersistenceExceptionTranslator(PersistenceExceptionTranslator pet)
/*  34:    */   {
/*  35: 90 */     Assert.notNull(pet, "PersistenceExceptionTranslator must not be null");
/*  36: 91 */     this.persistenceExceptionTranslator = pet;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setAlwaysTranslate(boolean alwaysTranslate)
/*  40:    */   {
/*  41:107 */     this.alwaysTranslate = alwaysTranslate;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  45:    */     throws BeansException
/*  46:    */   {
/*  47:111 */     if (this.persistenceExceptionTranslator == null)
/*  48:    */     {
/*  49:113 */       if (!(beanFactory instanceof ListableBeanFactory)) {
/*  50:114 */         throw new IllegalArgumentException(
/*  51:115 */           "Cannot use PersistenceExceptionTranslator autodetection without ListableBeanFactory");
/*  52:    */       }
/*  53:117 */       this.persistenceExceptionTranslator = 
/*  54:118 */         detectPersistenceExceptionTranslators((ListableBeanFactory)beanFactory);
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void afterPropertiesSet()
/*  59:    */   {
/*  60:123 */     if (this.persistenceExceptionTranslator == null) {
/*  61:124 */       throw new IllegalArgumentException("Property 'persistenceExceptionTranslator' is required");
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected PersistenceExceptionTranslator detectPersistenceExceptionTranslators(ListableBeanFactory beanFactory)
/*  66:    */   {
/*  67:139 */     Map<String, PersistenceExceptionTranslator> pets = BeanFactoryUtils.beansOfTypeIncludingAncestors(
/*  68:140 */       beanFactory, PersistenceExceptionTranslator.class, false, false);
/*  69:141 */     if (pets.isEmpty()) {
/*  70:142 */       throw new IllegalStateException(
/*  71:143 */         "No persistence exception translators found in bean factory. Cannot perform exception translation.");
/*  72:    */     }
/*  73:145 */     ChainedPersistenceExceptionTranslator cpet = new ChainedPersistenceExceptionTranslator();
/*  74:146 */     for (PersistenceExceptionTranslator pet : pets.values()) {
/*  75:147 */       cpet.addDelegate(pet);
/*  76:    */     }
/*  77:149 */     return cpet;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public Object invoke(MethodInvocation mi)
/*  81:    */     throws Throwable
/*  82:    */   {
/*  83:    */     try
/*  84:    */     {
/*  85:155 */       return mi.proceed();
/*  86:    */     }
/*  87:    */     catch (RuntimeException ex)
/*  88:    */     {
/*  89:159 */       if ((!this.alwaysTranslate) && (ReflectionUtils.declaresException(mi.getMethod(), ex.getClass()))) {
/*  90:160 */         throw ex;
/*  91:    */       }
/*  92:163 */       throw DataAccessUtils.translateIfNecessary(ex, this.persistenceExceptionTranslator);
/*  93:    */     }
/*  94:    */   }
/*  95:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.support.PersistenceExceptionTranslationInterceptor
 * JD-Core Version:    0.7.0.1
 */