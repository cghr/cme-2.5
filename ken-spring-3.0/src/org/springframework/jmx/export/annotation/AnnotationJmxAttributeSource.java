/*   1:    */ package org.springframework.jmx.export.annotation;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import org.springframework.beans.annotation.AnnotationBeanUtils;
/*   6:    */ import org.springframework.core.annotation.AnnotationUtils;
/*   7:    */ import org.springframework.jmx.export.metadata.InvalidMetadataException;
/*   8:    */ import org.springframework.jmx.export.metadata.JmxAttributeSource;
/*   9:    */ import org.springframework.jmx.export.metadata.ManagedNotification;
/*  10:    */ import org.springframework.jmx.export.metadata.ManagedOperationParameter;
/*  11:    */ import org.springframework.util.StringUtils;
/*  12:    */ 
/*  13:    */ public class AnnotationJmxAttributeSource
/*  14:    */   implements JmxAttributeSource
/*  15:    */ {
/*  16:    */   public org.springframework.jmx.export.metadata.ManagedResource getManagedResource(Class<?> beanClass)
/*  17:    */     throws InvalidMetadataException
/*  18:    */   {
/*  19: 51 */     ManagedResource ann = 
/*  20: 52 */       (ManagedResource)beanClass.getAnnotation(ManagedResource.class);
/*  21: 53 */     if (ann == null) {
/*  22: 54 */       return null;
/*  23:    */     }
/*  24: 56 */     org.springframework.jmx.export.metadata.ManagedResource managedResource = new org.springframework.jmx.export.metadata.ManagedResource();
/*  25: 57 */     AnnotationBeanUtils.copyPropertiesToBean(ann, managedResource, new String[0]);
/*  26: 58 */     if ((!"".equals(ann.value())) && (!StringUtils.hasLength(managedResource.getObjectName()))) {
/*  27: 59 */       managedResource.setObjectName(ann.value());
/*  28:    */     }
/*  29: 61 */     return managedResource;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public org.springframework.jmx.export.metadata.ManagedAttribute getManagedAttribute(Method method)
/*  33:    */     throws InvalidMetadataException
/*  34:    */   {
/*  35: 65 */     ManagedAttribute ann = 
/*  36: 66 */       (ManagedAttribute)AnnotationUtils.findAnnotation(method, ManagedAttribute.class);
/*  37: 67 */     if (ann == null) {
/*  38: 68 */       return null;
/*  39:    */     }
/*  40: 70 */     org.springframework.jmx.export.metadata.ManagedAttribute managedAttribute = new org.springframework.jmx.export.metadata.ManagedAttribute();
/*  41: 71 */     AnnotationBeanUtils.copyPropertiesToBean(ann, managedAttribute, new String[] { "defaultValue" });
/*  42: 72 */     if (ann.defaultValue().length() > 0) {
/*  43: 73 */       managedAttribute.setDefaultValue(ann.defaultValue());
/*  44:    */     }
/*  45: 75 */     return managedAttribute;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public org.springframework.jmx.export.metadata.ManagedMetric getManagedMetric(Method method)
/*  49:    */     throws InvalidMetadataException
/*  50:    */   {
/*  51: 79 */     ManagedMetric ann = 
/*  52: 80 */       (ManagedMetric)AnnotationUtils.findAnnotation(method, ManagedMetric.class);
/*  53: 81 */     if (ann == null) {
/*  54: 82 */       return null;
/*  55:    */     }
/*  56: 84 */     org.springframework.jmx.export.metadata.ManagedMetric managedMetric = new org.springframework.jmx.export.metadata.ManagedMetric();
/*  57: 85 */     AnnotationBeanUtils.copyPropertiesToBean(ann, managedMetric, new String[0]);
/*  58: 86 */     return managedMetric;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public org.springframework.jmx.export.metadata.ManagedOperation getManagedOperation(Method method)
/*  62:    */     throws InvalidMetadataException
/*  63:    */   {
/*  64: 90 */     Annotation ann = AnnotationUtils.findAnnotation(method, ManagedOperation.class);
/*  65: 91 */     if (ann == null) {
/*  66: 92 */       return null;
/*  67:    */     }
/*  68: 94 */     org.springframework.jmx.export.metadata.ManagedOperation op = new org.springframework.jmx.export.metadata.ManagedOperation();
/*  69: 95 */     AnnotationBeanUtils.copyPropertiesToBean(ann, op, new String[0]);
/*  70: 96 */     return op;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public ManagedOperationParameter[] getManagedOperationParameters(Method method)
/*  74:    */     throws InvalidMetadataException
/*  75:    */   {
/*  76:102 */     ManagedOperationParameters params = (ManagedOperationParameters)AnnotationUtils.findAnnotation(method, ManagedOperationParameters.class);
/*  77:103 */     ManagedOperationParameter[] result = (ManagedOperationParameter[])null;
/*  78:104 */     if (params == null)
/*  79:    */     {
/*  80:105 */       result = new ManagedOperationParameter[0];
/*  81:    */     }
/*  82:    */     else
/*  83:    */     {
/*  84:108 */       Annotation[] paramData = params.value();
/*  85:109 */       result = new ManagedOperationParameter[paramData.length];
/*  86:110 */       for (int i = 0; i < paramData.length; i++)
/*  87:    */       {
/*  88:111 */         Annotation annotation = paramData[i];
/*  89:112 */         ManagedOperationParameter managedOperationParameter = new ManagedOperationParameter();
/*  90:113 */         AnnotationBeanUtils.copyPropertiesToBean(annotation, managedOperationParameter, new String[0]);
/*  91:114 */         result[i] = managedOperationParameter;
/*  92:    */       }
/*  93:    */     }
/*  94:117 */     return result;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public ManagedNotification[] getManagedNotifications(Class<?> clazz)
/*  98:    */     throws InvalidMetadataException
/*  99:    */   {
/* 100:121 */     ManagedNotifications notificationsAnn = (ManagedNotifications)clazz.getAnnotation(ManagedNotifications.class);
/* 101:122 */     if (notificationsAnn == null) {
/* 102:123 */       return new ManagedNotification[0];
/* 103:    */     }
/* 104:125 */     Annotation[] notifications = notificationsAnn.value();
/* 105:126 */     ManagedNotification[] result = new ManagedNotification[notifications.length];
/* 106:127 */     for (int i = 0; i < notifications.length; i++)
/* 107:    */     {
/* 108:128 */       Annotation notification = notifications[i];
/* 109:129 */       ManagedNotification managedNotification = new ManagedNotification();
/* 110:130 */       AnnotationBeanUtils.copyPropertiesToBean(notification, managedNotification, new String[0]);
/* 111:131 */       result[i] = managedNotification;
/* 112:    */     }
/* 113:133 */     return result;
/* 114:    */   }
/* 115:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource
 * JD-Core Version:    0.7.0.1
 */