/*   1:    */ package org.springframework.context.annotation;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
/*   5:    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*   6:    */ import org.springframework.beans.factory.support.AutowireCandidateQualifier;
/*   7:    */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*   8:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*   9:    */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*  10:    */ import org.springframework.core.env.Environment;
/*  11:    */ import org.springframework.core.env.EnvironmentCapable;
/*  12:    */ import org.springframework.core.env.StandardEnvironment;
/*  13:    */ import org.springframework.core.type.AnnotationMetadata;
/*  14:    */ import org.springframework.util.Assert;
/*  15:    */ 
/*  16:    */ public class AnnotatedBeanDefinitionReader
/*  17:    */ {
/*  18:    */   private final BeanDefinitionRegistry registry;
/*  19: 48 */   private Environment environment = new StandardEnvironment();
/*  20: 50 */   private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
/*  21: 52 */   private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
/*  22:    */   
/*  23:    */   public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry)
/*  24:    */   {
/*  25: 60 */     Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
/*  26: 61 */     this.registry = registry;
/*  27: 64 */     if ((this.registry instanceof EnvironmentCapable)) {
/*  28: 65 */       this.environment = ((EnvironmentCapable)this.registry).getEnvironment();
/*  29:    */     }
/*  30: 68 */     AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public final BeanDefinitionRegistry getRegistry()
/*  34:    */   {
/*  35: 75 */     return this.registry;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setEnvironment(Environment environment)
/*  39:    */   {
/*  40: 85 */     this.environment = environment;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator)
/*  44:    */   {
/*  45: 93 */     this.beanNameGenerator = (beanNameGenerator != null ? beanNameGenerator : new AnnotationBeanNameGenerator());
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setScopeMetadataResolver(ScopeMetadataResolver scopeMetadataResolver)
/*  49:    */   {
/*  50:101 */     this.scopeMetadataResolver = (scopeMetadataResolver != null ? scopeMetadataResolver : 
/*  51:102 */       new AnnotationScopeMetadataResolver());
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void register(Class<?>... annotatedClasses)
/*  55:    */   {
/*  56:106 */     for (Class<?> annotatedClass : annotatedClasses) {
/*  57:107 */       registerBean(annotatedClass);
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void registerBean(Class<?> annotatedClass)
/*  62:    */   {
/*  63:112 */     registerBean(annotatedClass, null, null);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void registerBean(Class<?> annotatedClass, Class<? extends Annotation>... qualifiers)
/*  67:    */   {
/*  68:116 */     registerBean(annotatedClass, null, qualifiers);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void registerBean(Class<?> annotatedClass, String name, Class<? extends Annotation>... qualifiers)
/*  72:    */   {
/*  73:120 */     AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(annotatedClass);
/*  74:121 */     AnnotationMetadata metadata = abd.getMetadata();
/*  75:123 */     if ((ProfileHelper.isProfileAnnotationPresent(metadata)) && 
/*  76:124 */       (!this.environment.acceptsProfiles(ProfileHelper.getCandidateProfiles(metadata)))) {
/*  77:125 */       return;
/*  78:    */     }
/*  79:128 */     ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(abd);
/*  80:129 */     abd.setScope(scopeMetadata.getScopeName());
/*  81:130 */     String beanName = name != null ? name : this.beanNameGenerator.generateBeanName(abd, this.registry);
/*  82:131 */     AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
/*  83:132 */     if (qualifiers != null) {
/*  84:133 */       for (Class<? extends Annotation> qualifier : qualifiers) {
/*  85:134 */         if (Primary.class.equals(qualifier)) {
/*  86:135 */           abd.setPrimary(true);
/*  87:136 */         } else if (Lazy.class.equals(qualifier)) {
/*  88:137 */           abd.setLazyInit(true);
/*  89:    */         } else {
/*  90:139 */           abd.addQualifier(new AutowireCandidateQualifier(qualifier));
/*  91:    */         }
/*  92:    */       }
/*  93:    */     }
/*  94:143 */     BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(abd, beanName);
/*  95:144 */     definitionHolder = AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);
/*  96:145 */     BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, this.registry);
/*  97:    */   }
/*  98:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.AnnotatedBeanDefinitionReader
 * JD-Core Version:    0.7.0.1
 */