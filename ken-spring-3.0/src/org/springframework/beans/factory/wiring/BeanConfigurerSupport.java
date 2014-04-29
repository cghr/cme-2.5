/*   1:    */ package org.springframework.beans.factory.wiring;
/*   2:    */ 
/*   3:    */ import org.apache.commons.logging.Log;
/*   4:    */ import org.apache.commons.logging.LogFactory;
/*   5:    */ import org.springframework.beans.factory.BeanCreationException;
/*   6:    */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*   7:    */ import org.springframework.beans.factory.BeanFactory;
/*   8:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*   9:    */ import org.springframework.beans.factory.DisposableBean;
/*  10:    */ import org.springframework.beans.factory.InitializingBean;
/*  11:    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*  12:    */ import org.springframework.util.Assert;
/*  13:    */ import org.springframework.util.ClassUtils;
/*  14:    */ 
/*  15:    */ public class BeanConfigurerSupport
/*  16:    */   implements BeanFactoryAware, InitializingBean, DisposableBean
/*  17:    */ {
/*  18: 53 */   protected final Log logger = LogFactory.getLog(getClass());
/*  19:    */   private volatile BeanWiringInfoResolver beanWiringInfoResolver;
/*  20:    */   private volatile ConfigurableListableBeanFactory beanFactory;
/*  21:    */   
/*  22:    */   public void setBeanWiringInfoResolver(BeanWiringInfoResolver beanWiringInfoResolver)
/*  23:    */   {
/*  24: 68 */     Assert.notNull(beanWiringInfoResolver, "BeanWiringInfoResolver must not be null");
/*  25: 69 */     this.beanWiringInfoResolver = beanWiringInfoResolver;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  29:    */   {
/*  30: 76 */     if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
/*  31: 77 */       throw new IllegalArgumentException(
/*  32: 78 */         "Bean configurer aspect needs to run in a ConfigurableListableBeanFactory: " + beanFactory);
/*  33:    */     }
/*  34: 80 */     this.beanFactory = ((ConfigurableListableBeanFactory)beanFactory);
/*  35: 81 */     if (this.beanWiringInfoResolver == null) {
/*  36: 82 */       this.beanWiringInfoResolver = createDefaultBeanWiringInfoResolver();
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   protected BeanWiringInfoResolver createDefaultBeanWiringInfoResolver()
/*  41:    */   {
/*  42: 93 */     return new ClassNameBeanWiringInfoResolver();
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void afterPropertiesSet()
/*  46:    */   {
/*  47:100 */     Assert.notNull(this.beanFactory, "BeanFactory must be set");
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void destroy()
/*  51:    */   {
/*  52:108 */     this.beanFactory = null;
/*  53:109 */     this.beanWiringInfoResolver = null;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void configureBean(Object beanInstance)
/*  57:    */   {
/*  58:121 */     if (this.beanFactory == null)
/*  59:    */     {
/*  60:122 */       if (this.logger.isDebugEnabled()) {
/*  61:123 */         this.logger.debug("BeanFactory has not been set on " + ClassUtils.getShortName(getClass()) + ": " + 
/*  62:124 */           "Make sure this configurer runs in a Spring container. Unable to configure bean of type [" + 
/*  63:125 */           ClassUtils.getDescriptiveType(beanInstance) + "]. Proceeding without injection.");
/*  64:    */       }
/*  65:127 */       return;
/*  66:    */     }
/*  67:130 */     BeanWiringInfo bwi = this.beanWiringInfoResolver.resolveWiringInfo(beanInstance);
/*  68:131 */     if (bwi == null) {
/*  69:133 */       return;
/*  70:    */     }
/*  71:    */     try
/*  72:    */     {
/*  73:137 */       if ((bwi.indicatesAutowiring()) || (
/*  74:138 */         (bwi.isDefaultBeanName()) && (!this.beanFactory.containsBean(bwi.getBeanName()))))
/*  75:    */       {
/*  76:140 */         this.beanFactory.autowireBeanProperties(beanInstance, bwi.getAutowireMode(), bwi.getDependencyCheck());
/*  77:141 */         Object result = this.beanFactory.initializeBean(beanInstance, bwi.getBeanName());
/*  78:142 */         checkExposedObject(result, beanInstance);
/*  79:    */       }
/*  80:    */       else
/*  81:    */       {
/*  82:146 */         Object result = this.beanFactory.configureBean(beanInstance, bwi.getBeanName());
/*  83:147 */         checkExposedObject(result, beanInstance);
/*  84:    */       }
/*  85:    */     }
/*  86:    */     catch (BeanCreationException ex)
/*  87:    */     {
/*  88:151 */       Throwable rootCause = ex.getMostSpecificCause();
/*  89:152 */       if ((rootCause instanceof BeanCurrentlyInCreationException))
/*  90:    */       {
/*  91:153 */         BeanCreationException bce = (BeanCreationException)rootCause;
/*  92:154 */         if (this.beanFactory.isCurrentlyInCreation(bce.getBeanName()))
/*  93:    */         {
/*  94:155 */           if (this.logger.isDebugEnabled()) {
/*  95:156 */             this.logger.debug("Failed to create target bean '" + bce.getBeanName() + 
/*  96:157 */               "' while configuring object of type [" + beanInstance.getClass().getName() + 
/*  97:158 */               "] - probably due to a circular reference. This is a common startup situation " + 
/*  98:159 */               "and usually not fatal. Proceeding without injection. Original exception: " + ex);
/*  99:    */           }
/* 100:161 */           return;
/* 101:    */         }
/* 102:    */       }
/* 103:164 */       throw ex;
/* 104:    */     }
/* 105:    */   }
/* 106:    */   
/* 107:    */   private void checkExposedObject(Object exposedObject, Object originalBeanInstance)
/* 108:    */   {
/* 109:169 */     if (exposedObject != originalBeanInstance) {
/* 110:170 */       throw new IllegalStateException("Post-processor tried to replace bean instance of type [" + 
/* 111:171 */         originalBeanInstance.getClass().getName() + "] with (proxy) object of type [" + 
/* 112:172 */         exposedObject.getClass().getName() + "] - not supported for aspect-configured classes!");
/* 113:    */     }
/* 114:    */   }
/* 115:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.wiring.BeanConfigurerSupport
 * JD-Core Version:    0.7.0.1
 */