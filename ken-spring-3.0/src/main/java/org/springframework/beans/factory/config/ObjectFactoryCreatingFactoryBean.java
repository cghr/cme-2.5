/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.springframework.beans.BeansException;
/*   5:    */ import org.springframework.beans.factory.BeanFactory;
/*   6:    */ import org.springframework.beans.factory.ObjectFactory;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ 
/*   9:    */ public class ObjectFactoryCreatingFactoryBean
/*  10:    */   extends AbstractFactoryBean<ObjectFactory>
/*  11:    */ {
/*  12:    */   private String targetBeanName;
/*  13:    */   
/*  14:    */   public void setTargetBeanName(String targetBeanName)
/*  15:    */   {
/*  16:110 */     this.targetBeanName = targetBeanName;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public void afterPropertiesSet()
/*  20:    */     throws Exception
/*  21:    */   {
/*  22:115 */     Assert.hasText(this.targetBeanName, "Property 'targetBeanName' is required");
/*  23:116 */     super.afterPropertiesSet();
/*  24:    */   }
/*  25:    */   
/*  26:    */   public Class getObjectType()
/*  27:    */   {
/*  28:122 */     return ObjectFactory.class;
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected ObjectFactory createInstance()
/*  32:    */   {
/*  33:127 */     return new TargetBeanObjectFactory(getBeanFactory(), this.targetBeanName);
/*  34:    */   }
/*  35:    */   
/*  36:    */   private static class TargetBeanObjectFactory
/*  37:    */     implements ObjectFactory, Serializable
/*  38:    */   {
/*  39:    */     private final BeanFactory beanFactory;
/*  40:    */     private final String targetBeanName;
/*  41:    */     
/*  42:    */     public TargetBeanObjectFactory(BeanFactory beanFactory, String targetBeanName)
/*  43:    */     {
/*  44:141 */       this.beanFactory = beanFactory;
/*  45:142 */       this.targetBeanName = targetBeanName;
/*  46:    */     }
/*  47:    */     
/*  48:    */     public Object getObject()
/*  49:    */       throws BeansException
/*  50:    */     {
/*  51:146 */       return this.beanFactory.getBean(this.targetBeanName);
/*  52:    */     }
/*  53:    */   }
/*  54:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.ObjectFactoryCreatingFactoryBean
 * JD-Core Version:    0.7.0.1
 */