/*   1:    */ package org.springframework.beans.factory.annotation;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyDescriptor;
/*   4:    */ import java.lang.annotation.Annotation;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Collections;
/*   8:    */ import java.util.HashSet;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Set;
/*  11:    */ import org.springframework.beans.BeansException;
/*  12:    */ import org.springframework.beans.PropertyValues;
/*  13:    */ import org.springframework.beans.factory.BeanFactory;
/*  14:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*  15:    */ import org.springframework.beans.factory.BeanInitializationException;
/*  16:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*  17:    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*  18:    */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
/*  19:    */ import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
/*  20:    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  21:    */ import org.springframework.core.Conventions;
/*  22:    */ import org.springframework.core.PriorityOrdered;
/*  23:    */ import org.springframework.core.annotation.AnnotationUtils;
/*  24:    */ import org.springframework.util.Assert;
/*  25:    */ 
/*  26:    */ public class RequiredAnnotationBeanPostProcessor
/*  27:    */   extends InstantiationAwareBeanPostProcessorAdapter
/*  28:    */   implements MergedBeanDefinitionPostProcessor, PriorityOrdered, BeanFactoryAware
/*  29:    */ {
/*  30: 83 */   public static final String SKIP_REQUIRED_CHECK_ATTRIBUTE = Conventions.getQualifiedAttributeName(RequiredAnnotationBeanPostProcessor.class, "skipRequiredCheck");
/*  31: 86 */   private Class<? extends Annotation> requiredAnnotationType = Required.class;
/*  32: 88 */   private int order = 2147483646;
/*  33:    */   private ConfigurableListableBeanFactory beanFactory;
/*  34: 93 */   private final Set<String> validatedBeanNames = Collections.synchronizedSet(new HashSet());
/*  35:    */   
/*  36:    */   public void setRequiredAnnotationType(Class<? extends Annotation> requiredAnnotationType)
/*  37:    */   {
/*  38:106 */     Assert.notNull(requiredAnnotationType, "'requiredAnnotationType' must not be null");
/*  39:107 */     this.requiredAnnotationType = requiredAnnotationType;
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected Class<? extends Annotation> getRequiredAnnotationType()
/*  43:    */   {
/*  44:114 */     return this.requiredAnnotationType;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  48:    */   {
/*  49:118 */     if ((beanFactory instanceof ConfigurableListableBeanFactory)) {
/*  50:119 */       this.beanFactory = ((ConfigurableListableBeanFactory)beanFactory);
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setOrder(int order)
/*  55:    */   {
/*  56:124 */     this.order = order;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public int getOrder()
/*  60:    */   {
/*  61:128 */     return this.order;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {}
/*  65:    */   
/*  66:    */   public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName)
/*  67:    */     throws BeansException
/*  68:    */   {
/*  69:140 */     if (!this.validatedBeanNames.contains(beanName))
/*  70:    */     {
/*  71:141 */       if (!shouldSkip(this.beanFactory, beanName))
/*  72:    */       {
/*  73:142 */         List<String> invalidProperties = new ArrayList();
/*  74:143 */         for (PropertyDescriptor pd : pds) {
/*  75:144 */           if ((isRequiredProperty(pd)) && (!pvs.contains(pd.getName()))) {
/*  76:145 */             invalidProperties.add(pd.getName());
/*  77:    */           }
/*  78:    */         }
/*  79:148 */         if (!invalidProperties.isEmpty()) {
/*  80:149 */           throw new BeanInitializationException(buildExceptionMessage(invalidProperties, beanName));
/*  81:    */         }
/*  82:    */       }
/*  83:152 */       this.validatedBeanNames.add(beanName);
/*  84:    */     }
/*  85:154 */     return pvs;
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected boolean shouldSkip(ConfigurableListableBeanFactory beanFactory, String beanName)
/*  89:    */   {
/*  90:167 */     if ((beanFactory == null) || (!beanFactory.containsBeanDefinition(beanName))) {
/*  91:168 */       return false;
/*  92:    */     }
/*  93:170 */     Object value = beanFactory.getBeanDefinition(beanName).getAttribute(SKIP_REQUIRED_CHECK_ATTRIBUTE);
/*  94:171 */     return (value != null) && ((Boolean.TRUE.equals(value)) || (Boolean.valueOf(value.toString()).booleanValue()));
/*  95:    */   }
/*  96:    */   
/*  97:    */   protected boolean isRequiredProperty(PropertyDescriptor propertyDescriptor)
/*  98:    */   {
/*  99:184 */     Method setter = propertyDescriptor.getWriteMethod();
/* 100:185 */     return (setter != null) && (AnnotationUtils.getAnnotation(setter, getRequiredAnnotationType()) != null);
/* 101:    */   }
/* 102:    */   
/* 103:    */   private String buildExceptionMessage(List<String> invalidProperties, String beanName)
/* 104:    */   {
/* 105:195 */     int size = invalidProperties.size();
/* 106:196 */     StringBuilder sb = new StringBuilder();
/* 107:197 */     sb.append(size == 1 ? "Property" : "Properties");
/* 108:198 */     for (int i = 0; i < size; i++)
/* 109:    */     {
/* 110:199 */       String propertyName = (String)invalidProperties.get(i);
/* 111:200 */       if (i > 0) {
/* 112:201 */         if (i == size - 1) {
/* 113:202 */           sb.append(" and");
/* 114:    */         } else {
/* 115:205 */           sb.append(",");
/* 116:    */         }
/* 117:    */       }
/* 118:208 */       sb.append(" '").append(propertyName).append("'");
/* 119:    */     }
/* 120:210 */     sb.append(size == 1 ? " is" : " are");
/* 121:211 */     sb.append(" required for bean '").append(beanName).append("'");
/* 122:212 */     return sb.toString();
/* 123:    */   }
/* 124:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor
 * JD-Core Version:    0.7.0.1
 */