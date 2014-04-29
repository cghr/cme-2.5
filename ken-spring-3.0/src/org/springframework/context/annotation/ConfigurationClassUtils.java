/*   1:    */ package org.springframework.context.annotation;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import org.apache.commons.logging.Log;
/*   5:    */ import org.apache.commons.logging.LogFactory;
/*   6:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*   7:    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*   8:    */ import org.springframework.core.Conventions;
/*   9:    */ import org.springframework.core.type.AnnotationMetadata;
/*  10:    */ import org.springframework.core.type.StandardAnnotationMetadata;
/*  11:    */ import org.springframework.core.type.classreading.MetadataReader;
/*  12:    */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*  13:    */ import org.springframework.stereotype.Component;
/*  14:    */ 
/*  15:    */ abstract class ConfigurationClassUtils
/*  16:    */ {
/*  17: 40 */   private static final Log logger = LogFactory.getLog(ConfigurationClassUtils.class);
/*  18:    */   private static final String CONFIGURATION_CLASS_FULL = "full";
/*  19:    */   private static final String CONFIGURATION_CLASS_LITE = "lite";
/*  20: 47 */   private static final String CONFIGURATION_CLASS_ATTRIBUTE = Conventions.getQualifiedAttributeName(ConfigurationClassPostProcessor.class, "configurationClass");
/*  21:    */   
/*  22:    */   public static boolean checkConfigurationClassCandidate(BeanDefinition beanDef, MetadataReaderFactory metadataReaderFactory)
/*  23:    */   {
/*  24: 58 */     AnnotationMetadata metadata = null;
/*  25: 62 */     if (((beanDef instanceof AbstractBeanDefinition)) && (((AbstractBeanDefinition)beanDef).hasBeanClass()))
/*  26:    */     {
/*  27: 63 */       metadata = new StandardAnnotationMetadata(((AbstractBeanDefinition)beanDef).getBeanClass());
/*  28:    */     }
/*  29:    */     else
/*  30:    */     {
/*  31: 66 */       String className = beanDef.getBeanClassName();
/*  32: 67 */       if (className != null) {
/*  33:    */         try
/*  34:    */         {
/*  35: 69 */           MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(className);
/*  36: 70 */           metadata = metadataReader.getAnnotationMetadata();
/*  37:    */         }
/*  38:    */         catch (IOException ex)
/*  39:    */         {
/*  40: 73 */           if (logger.isDebugEnabled()) {
/*  41: 74 */             logger.debug("Could not find class file for introspecting factory methods: " + className, ex);
/*  42:    */           }
/*  43: 76 */           return false;
/*  44:    */         }
/*  45:    */       }
/*  46:    */     }
/*  47: 81 */     if (metadata != null)
/*  48:    */     {
/*  49: 82 */       if (isFullConfigurationCandidate(metadata))
/*  50:    */       {
/*  51: 83 */         beanDef.setAttribute(CONFIGURATION_CLASS_ATTRIBUTE, "full");
/*  52: 84 */         return true;
/*  53:    */       }
/*  54: 86 */       if (isLiteConfigurationCandidate(metadata))
/*  55:    */       {
/*  56: 87 */         beanDef.setAttribute(CONFIGURATION_CLASS_ATTRIBUTE, "lite");
/*  57: 88 */         return true;
/*  58:    */       }
/*  59:    */     }
/*  60: 91 */     return false;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public static boolean isConfigurationCandidate(AnnotationMetadata metadata)
/*  64:    */   {
/*  65: 95 */     return (isFullConfigurationCandidate(metadata)) || (isLiteConfigurationCandidate(metadata));
/*  66:    */   }
/*  67:    */   
/*  68:    */   public static boolean isFullConfigurationCandidate(AnnotationMetadata metadata)
/*  69:    */   {
/*  70: 99 */     return metadata.isAnnotated(Configuration.class.getName());
/*  71:    */   }
/*  72:    */   
/*  73:    */   public static boolean isLiteConfigurationCandidate(AnnotationMetadata metadata)
/*  74:    */   {
/*  75:104 */     return (metadata.isAnnotated(Component.class.getName())) || (metadata.hasAnnotatedMethods(Bean.class.getName()));
/*  76:    */   }
/*  77:    */   
/*  78:    */   public static boolean isFullConfigurationClass(BeanDefinition beanDef)
/*  79:    */   {
/*  80:112 */     return "full".equals(beanDef.getAttribute(CONFIGURATION_CLASS_ATTRIBUTE));
/*  81:    */   }
/*  82:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ConfigurationClassUtils
 * JD-Core Version:    0.7.0.1
 */