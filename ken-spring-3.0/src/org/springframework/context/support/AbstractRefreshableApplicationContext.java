/*   1:    */ package org.springframework.context.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import org.springframework.beans.BeansException;
/*   5:    */ import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
/*   6:    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*   7:    */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*   8:    */ import org.springframework.context.ApplicationContext;
/*   9:    */ import org.springframework.context.ApplicationContextException;
/*  10:    */ 
/*  11:    */ public abstract class AbstractRefreshableApplicationContext
/*  12:    */   extends AbstractApplicationContext
/*  13:    */ {
/*  14:    */   private Boolean allowBeanDefinitionOverriding;
/*  15:    */   private Boolean allowCircularReferences;
/*  16:    */   private DefaultListableBeanFactory beanFactory;
/*  17: 76 */   private final Object beanFactoryMonitor = new Object();
/*  18:    */   
/*  19:    */   public AbstractRefreshableApplicationContext() {}
/*  20:    */   
/*  21:    */   public AbstractRefreshableApplicationContext(ApplicationContext parent)
/*  22:    */   {
/*  23: 90 */     super(parent);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setAllowBeanDefinitionOverriding(boolean allowBeanDefinitionOverriding)
/*  27:    */   {
/*  28:101 */     this.allowBeanDefinitionOverriding = Boolean.valueOf(allowBeanDefinitionOverriding);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setAllowCircularReferences(boolean allowCircularReferences)
/*  32:    */   {
/*  33:112 */     this.allowCircularReferences = Boolean.valueOf(allowCircularReferences);
/*  34:    */   }
/*  35:    */   
/*  36:    */   protected final void refreshBeanFactory()
/*  37:    */     throws BeansException
/*  38:    */   {
/*  39:123 */     if (hasBeanFactory())
/*  40:    */     {
/*  41:124 */       destroyBeans();
/*  42:125 */       closeBeanFactory();
/*  43:    */     }
/*  44:    */     try
/*  45:    */     {
/*  46:128 */       DefaultListableBeanFactory beanFactory = createBeanFactory();
/*  47:129 */       beanFactory.setSerializationId(getId());
/*  48:130 */       customizeBeanFactory(beanFactory);
/*  49:131 */       loadBeanDefinitions(beanFactory);
/*  50:132 */       synchronized (this.beanFactoryMonitor)
/*  51:    */       {
/*  52:133 */         this.beanFactory = beanFactory;
/*  53:    */       }
/*  54:    */     }
/*  55:    */     catch (IOException ex)
/*  56:    */     {
/*  57:137 */       throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected void cancelRefresh(BeansException ex)
/*  62:    */   {
/*  63:143 */     synchronized (this.beanFactoryMonitor)
/*  64:    */     {
/*  65:144 */       if (this.beanFactory != null) {
/*  66:145 */         this.beanFactory.setSerializationId(null);
/*  67:    */       }
/*  68:    */     }
/*  69:147 */     super.cancelRefresh(ex);
/*  70:    */   }
/*  71:    */   
/*  72:    */   protected final void closeBeanFactory()
/*  73:    */   {
/*  74:152 */     synchronized (this.beanFactoryMonitor)
/*  75:    */     {
/*  76:153 */       this.beanFactory.setSerializationId(null);
/*  77:154 */       this.beanFactory = null;
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   protected final boolean hasBeanFactory()
/*  82:    */   {
/*  83:163 */     synchronized (this.beanFactoryMonitor)
/*  84:    */     {
/*  85:164 */       return this.beanFactory != null;
/*  86:    */     }
/*  87:    */   }
/*  88:    */   
/*  89:    */   public final ConfigurableListableBeanFactory getBeanFactory()
/*  90:    */   {
/*  91:170 */     synchronized (this.beanFactoryMonitor)
/*  92:    */     {
/*  93:171 */       if (this.beanFactory == null) {
/*  94:172 */         throw new IllegalStateException("BeanFactory not initialized or already closed - call 'refresh' before accessing beans via the ApplicationContext");
/*  95:    */       }
/*  96:175 */       return this.beanFactory;
/*  97:    */     }
/*  98:    */   }
/*  99:    */   
/* 100:    */   protected DefaultListableBeanFactory createBeanFactory()
/* 101:    */   {
/* 102:195 */     return new DefaultListableBeanFactory(getInternalParentBeanFactory());
/* 103:    */   }
/* 104:    */   
/* 105:    */   protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory)
/* 106:    */   {
/* 107:213 */     if (this.allowBeanDefinitionOverriding != null) {
/* 108:214 */       beanFactory.setAllowBeanDefinitionOverriding(this.allowBeanDefinitionOverriding.booleanValue());
/* 109:    */     }
/* 110:216 */     if (this.allowCircularReferences != null) {
/* 111:217 */       beanFactory.setAllowCircularReferences(this.allowCircularReferences.booleanValue());
/* 112:    */     }
/* 113:219 */     beanFactory.setAutowireCandidateResolver(new QualifierAnnotationAutowireCandidateResolver());
/* 114:    */   }
/* 115:    */   
/* 116:    */   protected abstract void loadBeanDefinitions(DefaultListableBeanFactory paramDefaultListableBeanFactory)
/* 117:    */     throws BeansException, IOException;
/* 118:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.AbstractRefreshableApplicationContext
 * JD-Core Version:    0.7.0.1
 */