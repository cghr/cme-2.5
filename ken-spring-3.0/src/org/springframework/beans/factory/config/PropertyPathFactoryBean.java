/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import org.apache.commons.logging.Log;
/*   4:    */ import org.apache.commons.logging.LogFactory;
/*   5:    */ import org.springframework.beans.BeanWrapper;
/*   6:    */ import org.springframework.beans.BeansException;
/*   7:    */ import org.springframework.beans.PropertyAccessorFactory;
/*   8:    */ import org.springframework.beans.factory.BeanFactory;
/*   9:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*  10:    */ import org.springframework.beans.factory.BeanFactoryUtils;
/*  11:    */ import org.springframework.beans.factory.BeanNameAware;
/*  12:    */ import org.springframework.beans.factory.FactoryBean;
/*  13:    */ import org.springframework.util.StringUtils;
/*  14:    */ 
/*  15:    */ public class PropertyPathFactoryBean
/*  16:    */   implements FactoryBean<Object>, BeanNameAware, BeanFactoryAware
/*  17:    */ {
/*  18: 86 */   private static final Log logger = LogFactory.getLog(PropertyPathFactoryBean.class);
/*  19:    */   private BeanWrapper targetBeanWrapper;
/*  20:    */   private String targetBeanName;
/*  21:    */   private String propertyPath;
/*  22:    */   private Class resultType;
/*  23:    */   private String beanName;
/*  24:    */   private BeanFactory beanFactory;
/*  25:    */   
/*  26:    */   public void setTargetObject(Object targetObject)
/*  27:    */   {
/*  28:109 */     this.targetBeanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(targetObject);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setTargetBeanName(String targetBeanName)
/*  32:    */   {
/*  33:120 */     this.targetBeanName = StringUtils.trimAllWhitespace(targetBeanName);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setPropertyPath(String propertyPath)
/*  37:    */   {
/*  38:129 */     this.propertyPath = StringUtils.trimAllWhitespace(propertyPath);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setResultType(Class resultType)
/*  42:    */   {
/*  43:141 */     this.resultType = resultType;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setBeanName(String beanName)
/*  47:    */   {
/*  48:151 */     this.beanName = StringUtils.trimAllWhitespace(BeanFactoryUtils.originalBeanName(beanName));
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  52:    */   {
/*  53:156 */     this.beanFactory = beanFactory;
/*  54:158 */     if ((this.targetBeanWrapper != null) && (this.targetBeanName != null)) {
/*  55:159 */       throw new IllegalArgumentException("Specify either 'targetObject' or 'targetBeanName', not both");
/*  56:    */     }
/*  57:162 */     if ((this.targetBeanWrapper == null) && (this.targetBeanName == null))
/*  58:    */     {
/*  59:163 */       if (this.propertyPath != null) {
/*  60:164 */         throw new IllegalArgumentException(
/*  61:165 */           "Specify 'targetObject' or 'targetBeanName' in combination with 'propertyPath'");
/*  62:    */       }
/*  63:169 */       int dotIndex = this.beanName.indexOf('.');
/*  64:170 */       if (dotIndex == -1) {
/*  65:171 */         throw new IllegalArgumentException(
/*  66:172 */           "Neither 'targetObject' nor 'targetBeanName' specified, and PropertyPathFactoryBean bean name '" + 
/*  67:173 */           this.beanName + "' does not follow 'beanName.property' syntax");
/*  68:    */       }
/*  69:175 */       this.targetBeanName = this.beanName.substring(0, dotIndex);
/*  70:176 */       this.propertyPath = this.beanName.substring(dotIndex + 1);
/*  71:    */     }
/*  72:179 */     else if (this.propertyPath == null)
/*  73:    */     {
/*  74:181 */       throw new IllegalArgumentException("'propertyPath' is required");
/*  75:    */     }
/*  76:184 */     if ((this.targetBeanWrapper == null) && (this.beanFactory.isSingleton(this.targetBeanName)))
/*  77:    */     {
/*  78:186 */       Object bean = this.beanFactory.getBean(this.targetBeanName);
/*  79:187 */       this.targetBeanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
/*  80:188 */       this.resultType = this.targetBeanWrapper.getPropertyType(this.propertyPath);
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public Object getObject()
/*  85:    */     throws BeansException
/*  86:    */   {
/*  87:194 */     BeanWrapper target = this.targetBeanWrapper;
/*  88:195 */     if (target != null)
/*  89:    */     {
/*  90:196 */       if ((logger.isWarnEnabled()) && (this.targetBeanName != null) && 
/*  91:197 */         ((this.beanFactory instanceof ConfigurableBeanFactory)) && 
/*  92:198 */         (((ConfigurableBeanFactory)this.beanFactory).isCurrentlyInCreation(this.targetBeanName))) {
/*  93:199 */         logger.warn("Target bean '" + this.targetBeanName + "' is still in creation due to a circular " + 
/*  94:200 */           "reference - obtained value for property '" + this.propertyPath + "' may be outdated!");
/*  95:    */       }
/*  96:    */     }
/*  97:    */     else
/*  98:    */     {
/*  99:205 */       Object bean = this.beanFactory.getBean(this.targetBeanName);
/* 100:206 */       target = PropertyAccessorFactory.forBeanPropertyAccess(bean);
/* 101:    */     }
/* 102:208 */     return target.getPropertyValue(this.propertyPath);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public Class<?> getObjectType()
/* 106:    */   {
/* 107:212 */     return this.resultType;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public boolean isSingleton()
/* 111:    */   {
/* 112:222 */     return false;
/* 113:    */   }
/* 114:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.PropertyPathFactoryBean
 * JD-Core Version:    0.7.0.1
 */