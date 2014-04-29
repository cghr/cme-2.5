/*   1:    */ package org.springframework.context.annotation;
/*   2:    */ 
/*   3:    */ import java.beans.Introspector;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.Set;
/*   6:    */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*   7:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*   8:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*   9:    */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*  10:    */ import org.springframework.core.type.AnnotationMetadata;
/*  11:    */ import org.springframework.util.ClassUtils;
/*  12:    */ import org.springframework.util.StringUtils;
/*  13:    */ 
/*  14:    */ public class AnnotationBeanNameGenerator
/*  15:    */   implements BeanNameGenerator
/*  16:    */ {
/*  17:    */   private static final String COMPONENT_ANNOTATION_CLASSNAME = "org.springframework.stereotype.Component";
/*  18:    */   
/*  19:    */   public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry)
/*  20:    */   {
/*  21: 67 */     if ((definition instanceof AnnotatedBeanDefinition))
/*  22:    */     {
/*  23: 68 */       String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition)definition);
/*  24: 69 */       if (StringUtils.hasText(beanName)) {
/*  25: 71 */         return beanName;
/*  26:    */       }
/*  27:    */     }
/*  28: 75 */     return buildDefaultBeanName(definition);
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected String determineBeanNameFromAnnotation(AnnotatedBeanDefinition annotatedDef)
/*  32:    */   {
/*  33: 84 */     AnnotationMetadata amd = annotatedDef.getMetadata();
/*  34: 85 */     Set<String> types = amd.getAnnotationTypes();
/*  35: 86 */     String beanName = null;
/*  36: 87 */     for (String type : types)
/*  37:    */     {
/*  38: 88 */       Map<String, Object> attributes = amd.getAnnotationAttributes(type);
/*  39: 89 */       if (isStereotypeWithNameValue(type, amd.getMetaAnnotationTypes(type), attributes))
/*  40:    */       {
/*  41: 90 */         String value = (String)attributes.get("value");
/*  42: 91 */         if (StringUtils.hasLength(value))
/*  43:    */         {
/*  44: 92 */           if ((beanName != null) && (!value.equals(beanName))) {
/*  45: 93 */             throw new IllegalStateException("Stereotype annotations suggest inconsistent component names: '" + 
/*  46: 94 */               beanName + "' versus '" + value + "'");
/*  47:    */           }
/*  48: 96 */           beanName = value;
/*  49:    */         }
/*  50:    */       }
/*  51:    */     }
/*  52:100 */     return beanName;
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected boolean isStereotypeWithNameValue(String annotationType, Set<String> metaAnnotationTypes, Map<String, Object> attributes)
/*  56:    */   {
/*  57:114 */     boolean isStereotype = (annotationType.equals("org.springframework.stereotype.Component")) || 
/*  58:115 */       ((metaAnnotationTypes != null) && (metaAnnotationTypes.contains("org.springframework.stereotype.Component"))) || 
/*  59:116 */       (annotationType.equals("javax.annotation.ManagedBean")) || 
/*  60:117 */       (annotationType.equals("javax.inject.Named"));
/*  61:118 */     return (isStereotype) && (attributes != null) && (attributes.containsKey("value"));
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected String buildDefaultBeanName(BeanDefinition definition)
/*  65:    */   {
/*  66:132 */     String shortClassName = ClassUtils.getShortName(definition.getBeanClassName());
/*  67:133 */     return Introspector.decapitalize(shortClassName);
/*  68:    */   }
/*  69:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.AnnotationBeanNameGenerator
 * JD-Core Version:    0.7.0.1
 */