/*  1:   */ package org.springframework.beans.factory.config;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import javax.inject.Provider;
/*  5:   */ import org.springframework.beans.BeansException;
/*  6:   */ import org.springframework.beans.factory.BeanFactory;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ 
/*  9:   */ public class ProviderCreatingFactoryBean
/* 10:   */   extends AbstractFactoryBean<Provider>
/* 11:   */ {
/* 12:   */   private String targetBeanName;
/* 13:   */   
/* 14:   */   public void setTargetBeanName(String targetBeanName)
/* 15:   */   {
/* 16:55 */     this.targetBeanName = targetBeanName;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void afterPropertiesSet()
/* 20:   */     throws Exception
/* 21:   */   {
/* 22:60 */     Assert.hasText(this.targetBeanName, "Property 'targetBeanName' is required");
/* 23:61 */     super.afterPropertiesSet();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public Class getObjectType()
/* 27:   */   {
/* 28:67 */     return Provider.class;
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected Provider createInstance()
/* 32:   */   {
/* 33:72 */     return new TargetBeanProvider(getBeanFactory(), this.targetBeanName);
/* 34:   */   }
/* 35:   */   
/* 36:   */   private static class TargetBeanProvider
/* 37:   */     implements Provider, Serializable
/* 38:   */   {
/* 39:   */     private final BeanFactory beanFactory;
/* 40:   */     private final String targetBeanName;
/* 41:   */     
/* 42:   */     public TargetBeanProvider(BeanFactory beanFactory, String targetBeanName)
/* 43:   */     {
/* 44:86 */       this.beanFactory = beanFactory;
/* 45:87 */       this.targetBeanName = targetBeanName;
/* 46:   */     }
/* 47:   */     
/* 48:   */     public Object get()
/* 49:   */       throws BeansException
/* 50:   */     {
/* 51:91 */       return this.beanFactory.getBean(this.targetBeanName);
/* 52:   */     }
/* 53:   */   }
/* 54:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.ProviderCreatingFactoryBean
 * JD-Core Version:    0.7.0.1
 */