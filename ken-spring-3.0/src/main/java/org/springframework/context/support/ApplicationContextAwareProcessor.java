/*   1:    */ package org.springframework.context.support;
/*   2:    */ 
/*   3:    */ import java.security.AccessControlContext;
/*   4:    */ import java.security.AccessController;
/*   5:    */ import java.security.PrivilegedAction;
/*   6:    */ import org.springframework.beans.BeansException;
/*   7:    */ import org.springframework.beans.factory.Aware;
/*   8:    */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*   9:    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*  10:    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*  11:    */ import org.springframework.context.ApplicationContextAware;
/*  12:    */ import org.springframework.context.ApplicationEventPublisherAware;
/*  13:    */ import org.springframework.context.ConfigurableApplicationContext;
/*  14:    */ import org.springframework.context.EmbeddedValueResolverAware;
/*  15:    */ import org.springframework.context.EnvironmentAware;
/*  16:    */ import org.springframework.context.MessageSourceAware;
/*  17:    */ import org.springframework.context.ResourceLoaderAware;
/*  18:    */ import org.springframework.util.StringValueResolver;
/*  19:    */ 
/*  20:    */ class ApplicationContextAwareProcessor
/*  21:    */   implements BeanPostProcessor
/*  22:    */ {
/*  23:    */   private final ConfigurableApplicationContext applicationContext;
/*  24:    */   
/*  25:    */   public ApplicationContextAwareProcessor(ConfigurableApplicationContext applicationContext)
/*  26:    */   {
/*  27: 69 */     this.applicationContext = applicationContext;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public Object postProcessBeforeInitialization(final Object bean, String beanName)
/*  31:    */     throws BeansException
/*  32:    */   {
/*  33: 74 */     AccessControlContext acc = null;
/*  34: 76 */     if ((System.getSecurityManager() != null) && (
/*  35: 77 */       ((bean instanceof EnvironmentAware)) || ((bean instanceof EmbeddedValueResolverAware)) || 
/*  36: 78 */       ((bean instanceof ResourceLoaderAware)) || ((bean instanceof ApplicationEventPublisherAware)) || 
/*  37: 79 */       ((bean instanceof MessageSourceAware)) || ((bean instanceof ApplicationContextAware)))) {
/*  38: 80 */       acc = this.applicationContext.getBeanFactory().getAccessControlContext();
/*  39:    */     }
/*  40: 83 */     if (acc != null) {
/*  41: 84 */       AccessController.doPrivileged(new PrivilegedAction()
/*  42:    */       {
/*  43:    */         public Object run()
/*  44:    */         {
/*  45: 86 */           ApplicationContextAwareProcessor.this.invokeAwareInterfaces(bean);
/*  46: 87 */           return null;
/*  47:    */         }
/*  48: 89 */       }, acc);
/*  49:    */     } else {
/*  50: 92 */       invokeAwareInterfaces(bean);
/*  51:    */     }
/*  52: 95 */     return bean;
/*  53:    */   }
/*  54:    */   
/*  55:    */   private void invokeAwareInterfaces(Object bean)
/*  56:    */   {
/*  57: 99 */     if ((bean instanceof Aware))
/*  58:    */     {
/*  59:100 */       if ((bean instanceof EnvironmentAware)) {
/*  60:101 */         ((EnvironmentAware)bean).setEnvironment(this.applicationContext.getEnvironment());
/*  61:    */       }
/*  62:103 */       if ((bean instanceof EmbeddedValueResolverAware)) {
/*  63:104 */         ((EmbeddedValueResolverAware)bean).setEmbeddedValueResolver(
/*  64:105 */           new EmbeddedValueResolver(this.applicationContext.getBeanFactory()));
/*  65:    */       }
/*  66:107 */       if ((bean instanceof ResourceLoaderAware)) {
/*  67:108 */         ((ResourceLoaderAware)bean).setResourceLoader(this.applicationContext);
/*  68:    */       }
/*  69:110 */       if ((bean instanceof ApplicationEventPublisherAware)) {
/*  70:111 */         ((ApplicationEventPublisherAware)bean).setApplicationEventPublisher(this.applicationContext);
/*  71:    */       }
/*  72:113 */       if ((bean instanceof MessageSourceAware)) {
/*  73:114 */         ((MessageSourceAware)bean).setMessageSource(this.applicationContext);
/*  74:    */       }
/*  75:116 */       if ((bean instanceof ApplicationContextAware)) {
/*  76:117 */         ((ApplicationContextAware)bean).setApplicationContext(this.applicationContext);
/*  77:    */       }
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   public Object postProcessAfterInitialization(Object bean, String beanName)
/*  82:    */   {
/*  83:123 */     return bean;
/*  84:    */   }
/*  85:    */   
/*  86:    */   private static class EmbeddedValueResolver
/*  87:    */     implements StringValueResolver
/*  88:    */   {
/*  89:    */     private final ConfigurableBeanFactory beanFactory;
/*  90:    */     
/*  91:    */     public EmbeddedValueResolver(ConfigurableBeanFactory beanFactory)
/*  92:    */     {
/*  93:132 */       this.beanFactory = beanFactory;
/*  94:    */     }
/*  95:    */     
/*  96:    */     public String resolveStringValue(String strVal)
/*  97:    */     {
/*  98:136 */       return this.beanFactory.resolveEmbeddedValue(strVal);
/*  99:    */     }
/* 100:    */   }
/* 101:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.ApplicationContextAwareProcessor
 * JD-Core Version:    0.7.0.1
 */