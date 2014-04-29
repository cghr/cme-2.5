/*   1:    */ package org.springframework.context.annotation;
/*   2:    */ 
/*   3:    */ import java.util.LinkedHashSet;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.Set;
/*   6:    */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*   7:    */ import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
/*   8:    */ import org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor;
/*   9:    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*  10:    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*  11:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  12:    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  13:    */ import org.springframework.core.type.AnnotationMetadata;
/*  14:    */ import org.springframework.util.ClassUtils;
/*  15:    */ 
/*  16:    */ public class AnnotationConfigUtils
/*  17:    */ {
/*  18:    */   public static final String CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalConfigurationAnnotationProcessor";
/*  19:    */   public static final String AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalAutowiredAnnotationProcessor";
/*  20:    */   public static final String REQUIRED_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalRequiredAnnotationProcessor";
/*  21:    */   public static final String COMMON_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalCommonAnnotationProcessor";
/*  22:    */   public static final String SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalScheduledAnnotationProcessor";
/*  23:    */   public static final String ASYNC_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalAsyncAnnotationProcessor";
/*  24:    */   public static final String ASYNC_EXECUTION_ASPECT_BEAN_NAME = "org.springframework.scheduling.config.internalAsyncExecutionAspect";
/*  25:    */   public static final String ASYNC_EXECUTION_ASPECT_CLASS_NAME = "org.springframework.scheduling.aspectj.AnnotationAsyncExecutionAspect";
/*  26:    */   public static final String ASYNC_EXECUTION_ASPECT_CONFIGURATION_CLASS_NAME = "org.springframework.scheduling.aspectj.AspectJAsyncConfiguration";
/*  27:    */   public static final String CACHE_ADVISOR_BEAN_NAME = "org.springframework.cache.config.internalCacheAdvisor";
/*  28:    */   public static final String CACHE_ASPECT_BEAN_NAME = "org.springframework.cache.config.internalCacheAspect";
/*  29:    */   public static final String CACHE_ASPECT_CLASS_NAME = "org.springframework.cache.aspectj.AnnotationCacheAspect";
/*  30:    */   public static final String PERSISTENCE_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalPersistenceAnnotationProcessor";
/*  31:    */   private static final String PERSISTENCE_ANNOTATION_PROCESSOR_CLASS_NAME = "org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor";
/*  32:134 */   private static final boolean jsr250Present = ClassUtils.isPresent("javax.annotation.Resource", AnnotationConfigUtils.class.getClassLoader());
/*  33:137 */   private static final boolean jpaPresent = (ClassUtils.isPresent("javax.persistence.EntityManagerFactory", AnnotationConfigUtils.class.getClassLoader())) && 
/*  34:138 */     (ClassUtils.isPresent("org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor", AnnotationConfigUtils.class.getClassLoader()));
/*  35:    */   
/*  36:    */   public static void registerAnnotationConfigProcessors(BeanDefinitionRegistry registry)
/*  37:    */   {
/*  38:146 */     registerAnnotationConfigProcessors(registry, null);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public static Set<BeanDefinitionHolder> registerAnnotationConfigProcessors(BeanDefinitionRegistry registry, Object source)
/*  42:    */   {
/*  43:160 */     Set<BeanDefinitionHolder> beanDefs = new LinkedHashSet(4);
/*  44:162 */     if (!registry.containsBeanDefinition("org.springframework.context.annotation.internalConfigurationAnnotationProcessor"))
/*  45:    */     {
/*  46:163 */       RootBeanDefinition def = new RootBeanDefinition(ConfigurationClassPostProcessor.class);
/*  47:164 */       def.setSource(source);
/*  48:165 */       beanDefs.add(registerPostProcessor(registry, def, "org.springframework.context.annotation.internalConfigurationAnnotationProcessor"));
/*  49:    */     }
/*  50:168 */     if (!registry.containsBeanDefinition("org.springframework.context.annotation.internalAutowiredAnnotationProcessor"))
/*  51:    */     {
/*  52:169 */       RootBeanDefinition def = new RootBeanDefinition(AutowiredAnnotationBeanPostProcessor.class);
/*  53:170 */       def.setSource(source);
/*  54:171 */       beanDefs.add(registerPostProcessor(registry, def, "org.springframework.context.annotation.internalAutowiredAnnotationProcessor"));
/*  55:    */     }
/*  56:174 */     if (!registry.containsBeanDefinition("org.springframework.context.annotation.internalRequiredAnnotationProcessor"))
/*  57:    */     {
/*  58:175 */       RootBeanDefinition def = new RootBeanDefinition(RequiredAnnotationBeanPostProcessor.class);
/*  59:176 */       def.setSource(source);
/*  60:177 */       beanDefs.add(registerPostProcessor(registry, def, "org.springframework.context.annotation.internalRequiredAnnotationProcessor"));
/*  61:    */     }
/*  62:181 */     if ((jsr250Present) && (!registry.containsBeanDefinition("org.springframework.context.annotation.internalCommonAnnotationProcessor")))
/*  63:    */     {
/*  64:182 */       RootBeanDefinition def = new RootBeanDefinition(CommonAnnotationBeanPostProcessor.class);
/*  65:183 */       def.setSource(source);
/*  66:184 */       beanDefs.add(registerPostProcessor(registry, def, "org.springframework.context.annotation.internalCommonAnnotationProcessor"));
/*  67:    */     }
/*  68:188 */     if ((jpaPresent) && (!registry.containsBeanDefinition("org.springframework.context.annotation.internalPersistenceAnnotationProcessor")))
/*  69:    */     {
/*  70:189 */       RootBeanDefinition def = new RootBeanDefinition();
/*  71:    */       try
/*  72:    */       {
/*  73:191 */         ClassLoader cl = AnnotationConfigUtils.class.getClassLoader();
/*  74:192 */         def.setBeanClass(cl.loadClass("org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor"));
/*  75:    */       }
/*  76:    */       catch (ClassNotFoundException ex)
/*  77:    */       {
/*  78:195 */         throw new IllegalStateException(
/*  79:196 */           "Cannot load optional framework class: org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor", ex);
/*  80:    */       }
/*  81:198 */       def.setSource(source);
/*  82:199 */       beanDefs.add(registerPostProcessor(registry, def, "org.springframework.context.annotation.internalPersistenceAnnotationProcessor"));
/*  83:    */     }
/*  84:202 */     return beanDefs;
/*  85:    */   }
/*  86:    */   
/*  87:    */   private static BeanDefinitionHolder registerPostProcessor(BeanDefinitionRegistry registry, RootBeanDefinition definition, String beanName)
/*  88:    */   {
/*  89:208 */     definition.setRole(2);
/*  90:209 */     registry.registerBeanDefinition(beanName, definition);
/*  91:210 */     return new BeanDefinitionHolder(definition, beanName);
/*  92:    */   }
/*  93:    */   
/*  94:    */   static void processCommonDefinitionAnnotations(AnnotatedBeanDefinition abd)
/*  95:    */   {
/*  96:214 */     if (abd.getMetadata().isAnnotated(Primary.class.getName())) {
/*  97:215 */       abd.setPrimary(true);
/*  98:    */     }
/*  99:217 */     if (abd.getMetadata().isAnnotated(Lazy.class.getName()))
/* 100:    */     {
/* 101:218 */       Boolean value = (Boolean)abd.getMetadata().getAnnotationAttributes(Lazy.class.getName()).get("value");
/* 102:219 */       abd.setLazyInit(value.booleanValue());
/* 103:    */     }
/* 104:221 */     if (abd.getMetadata().isAnnotated(DependsOn.class.getName()))
/* 105:    */     {
/* 106:222 */       String[] value = (String[])abd.getMetadata().getAnnotationAttributes(DependsOn.class.getName()).get("value");
/* 107:223 */       abd.setDependsOn(value);
/* 108:    */     }
/* 109:225 */     if (((abd instanceof AbstractBeanDefinition)) && 
/* 110:226 */       (abd.getMetadata().isAnnotated(Role.class.getName())))
/* 111:    */     {
/* 112:227 */       int value = ((Integer)abd.getMetadata().getAnnotationAttributes(Role.class.getName()).get("value")).intValue();
/* 113:228 */       ((AbstractBeanDefinition)abd).setRole(value);
/* 114:    */     }
/* 115:    */   }
/* 116:    */   
/* 117:    */   static BeanDefinitionHolder applyScopedProxyMode(ScopeMetadata metadata, BeanDefinitionHolder definition, BeanDefinitionRegistry registry)
/* 118:    */   {
/* 119:236 */     ScopedProxyMode scopedProxyMode = metadata.getScopedProxyMode();
/* 120:237 */     if (scopedProxyMode.equals(ScopedProxyMode.NO)) {
/* 121:238 */       return definition;
/* 122:    */     }
/* 123:240 */     boolean proxyTargetClass = scopedProxyMode.equals(ScopedProxyMode.TARGET_CLASS);
/* 124:241 */     return ScopedProxyCreator.createScopedProxy(definition, registry, proxyTargetClass);
/* 125:    */   }
/* 126:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.AnnotationConfigUtils
 * JD-Core Version:    0.7.0.1
 */