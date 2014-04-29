/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.BeansException;
/*   4:    */ import org.springframework.beans.factory.BeanFactory;
/*   5:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*   6:    */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
/*   7:    */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*   8:    */ import org.springframework.beans.factory.SmartFactoryBean;
/*   9:    */ 
/*  10:    */ public class BeanReferenceFactoryBean
/*  11:    */   implements SmartFactoryBean, BeanFactoryAware
/*  12:    */ {
/*  13:    */   private String targetBeanName;
/*  14:    */   private BeanFactory beanFactory;
/*  15:    */   
/*  16:    */   public void setTargetBeanName(String targetBeanName)
/*  17:    */   {
/*  18: 62 */     this.targetBeanName = targetBeanName;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  22:    */   {
/*  23: 66 */     this.beanFactory = beanFactory;
/*  24: 67 */     if (this.targetBeanName == null) {
/*  25: 68 */       throw new IllegalArgumentException("'targetBeanName' is required");
/*  26:    */     }
/*  27: 70 */     if (!this.beanFactory.containsBean(this.targetBeanName)) {
/*  28: 71 */       throw new NoSuchBeanDefinitionException(this.targetBeanName, this.beanFactory.toString());
/*  29:    */     }
/*  30:    */   }
/*  31:    */   
/*  32:    */   public Object getObject()
/*  33:    */     throws BeansException
/*  34:    */   {
/*  35: 77 */     if (this.beanFactory == null) {
/*  36: 78 */       throw new FactoryBeanNotInitializedException();
/*  37:    */     }
/*  38: 80 */     return this.beanFactory.getBean(this.targetBeanName);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public Class getObjectType()
/*  42:    */   {
/*  43: 84 */     if (this.beanFactory == null) {
/*  44: 85 */       return null;
/*  45:    */     }
/*  46: 87 */     return this.beanFactory.getType(this.targetBeanName);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean isSingleton()
/*  50:    */   {
/*  51: 91 */     if (this.beanFactory == null) {
/*  52: 92 */       throw new FactoryBeanNotInitializedException();
/*  53:    */     }
/*  54: 94 */     return this.beanFactory.isSingleton(this.targetBeanName);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public boolean isPrototype()
/*  58:    */   {
/*  59: 98 */     if (this.beanFactory == null) {
/*  60: 99 */       throw new FactoryBeanNotInitializedException();
/*  61:    */     }
/*  62:101 */     return this.beanFactory.isPrototype(this.targetBeanName);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public boolean isEagerInit()
/*  66:    */   {
/*  67:105 */     return false;
/*  68:    */   }
/*  69:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.BeanReferenceFactoryBean
 * JD-Core Version:    0.7.0.1
 */