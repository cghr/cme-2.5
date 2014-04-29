/*   1:    */ package org.springframework.context.annotation;
/*   2:    */ 
/*   3:    */ import java.util.LinkedHashSet;
/*   4:    */ import java.util.Set;
/*   5:    */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*   6:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*   7:    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*   8:    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*   9:    */ import org.springframework.beans.factory.support.BeanDefinitionDefaults;
/*  10:    */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*  11:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  12:    */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*  13:    */ import org.springframework.core.env.EnvironmentCapable;
/*  14:    */ import org.springframework.core.io.ResourceLoader;
/*  15:    */ import org.springframework.util.Assert;
/*  16:    */ import org.springframework.util.PatternMatchUtils;
/*  17:    */ 
/*  18:    */ public class ClassPathBeanDefinitionScanner
/*  19:    */   extends ClassPathScanningCandidateComponentProvider
/*  20:    */ {
/*  21:    */   private final BeanDefinitionRegistry registry;
/*  22: 64 */   private BeanDefinitionDefaults beanDefinitionDefaults = new BeanDefinitionDefaults();
/*  23:    */   private String[] autowireCandidatePatterns;
/*  24: 68 */   private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
/*  25: 70 */   private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
/*  26: 72 */   private boolean includeAnnotationConfig = true;
/*  27:    */   
/*  28:    */   public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry)
/*  29:    */   {
/*  30: 81 */     this(registry, true);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters)
/*  34:    */   {
/*  35:109 */     super(useDefaultFilters);
/*  36:    */     
/*  37:111 */     Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
/*  38:112 */     this.registry = registry;
/*  39:115 */     if ((this.registry instanceof ResourceLoader)) {
/*  40:116 */       setResourceLoader((ResourceLoader)this.registry);
/*  41:    */     }
/*  42:120 */     if ((this.registry instanceof EnvironmentCapable)) {
/*  43:121 */       setEnvironment(((EnvironmentCapable)this.registry).getEnvironment());
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   public final BeanDefinitionRegistry getRegistry()
/*  48:    */   {
/*  49:130 */     return this.registry;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setBeanDefinitionDefaults(BeanDefinitionDefaults beanDefinitionDefaults)
/*  53:    */   {
/*  54:138 */     this.beanDefinitionDefaults = 
/*  55:139 */       (beanDefinitionDefaults != null ? beanDefinitionDefaults : new BeanDefinitionDefaults());
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setAutowireCandidatePatterns(String[] autowireCandidatePatterns)
/*  59:    */   {
/*  60:147 */     this.autowireCandidatePatterns = autowireCandidatePatterns;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator)
/*  64:    */   {
/*  65:155 */     this.beanNameGenerator = (beanNameGenerator != null ? beanNameGenerator : new AnnotationBeanNameGenerator());
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setScopeMetadataResolver(ScopeMetadataResolver scopeMetadataResolver)
/*  69:    */   {
/*  70:165 */     this.scopeMetadataResolver = (scopeMetadataResolver != null ? scopeMetadataResolver : new AnnotationScopeMetadataResolver());
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setScopedProxyMode(ScopedProxyMode scopedProxyMode)
/*  74:    */   {
/*  75:175 */     this.scopeMetadataResolver = new AnnotationScopeMetadataResolver(scopedProxyMode);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setIncludeAnnotationConfig(boolean includeAnnotationConfig)
/*  79:    */   {
/*  80:184 */     this.includeAnnotationConfig = includeAnnotationConfig;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public int scan(String... basePackages)
/*  84:    */   {
/*  85:194 */     int beanCountAtScanStart = this.registry.getBeanDefinitionCount();
/*  86:    */     
/*  87:196 */     doScan(basePackages);
/*  88:199 */     if (this.includeAnnotationConfig) {
/*  89:200 */       AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry);
/*  90:    */     }
/*  91:203 */     return this.registry.getBeanDefinitionCount() - beanCountAtScanStart;
/*  92:    */   }
/*  93:    */   
/*  94:    */   protected Set<BeanDefinitionHolder> doScan(String... basePackages)
/*  95:    */   {
/*  96:215 */     Assert.notEmpty(basePackages, "At least one base package must be specified");
/*  97:216 */     Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet();
/*  98:217 */     for (String basePackage : basePackages)
/*  99:    */     {
/* 100:218 */       Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
/* 101:219 */       for (BeanDefinition candidate : candidates)
/* 102:    */       {
/* 103:220 */         ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(candidate);
/* 104:221 */         candidate.setScope(scopeMetadata.getScopeName());
/* 105:222 */         String beanName = this.beanNameGenerator.generateBeanName(candidate, this.registry);
/* 106:223 */         if ((candidate instanceof AbstractBeanDefinition)) {
/* 107:224 */           postProcessBeanDefinition((AbstractBeanDefinition)candidate, beanName);
/* 108:    */         }
/* 109:226 */         if ((candidate instanceof AnnotatedBeanDefinition)) {
/* 110:227 */           AnnotationConfigUtils.processCommonDefinitionAnnotations((AnnotatedBeanDefinition)candidate);
/* 111:    */         }
/* 112:229 */         if (checkCandidate(beanName, candidate))
/* 113:    */         {
/* 114:230 */           BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(candidate, beanName);
/* 115:231 */           definitionHolder = AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);
/* 116:232 */           beanDefinitions.add(definitionHolder);
/* 117:233 */           registerBeanDefinition(definitionHolder, this.registry);
/* 118:    */         }
/* 119:    */       }
/* 120:    */     }
/* 121:237 */     return beanDefinitions;
/* 122:    */   }
/* 123:    */   
/* 124:    */   protected void postProcessBeanDefinition(AbstractBeanDefinition beanDefinition, String beanName)
/* 125:    */   {
/* 126:247 */     beanDefinition.applyDefaults(this.beanDefinitionDefaults);
/* 127:248 */     if (this.autowireCandidatePatterns != null) {
/* 128:249 */       beanDefinition.setAutowireCandidate(PatternMatchUtils.simpleMatch(this.autowireCandidatePatterns, beanName));
/* 129:    */     }
/* 130:    */   }
/* 131:    */   
/* 132:    */   protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
/* 133:    */   {
/* 134:261 */     BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
/* 135:    */   }
/* 136:    */   
/* 137:    */   protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition)
/* 138:    */     throws IllegalStateException
/* 139:    */   {
/* 140:277 */     if (!this.registry.containsBeanDefinition(beanName)) {
/* 141:278 */       return true;
/* 142:    */     }
/* 143:280 */     BeanDefinition existingDef = this.registry.getBeanDefinition(beanName);
/* 144:281 */     BeanDefinition originatingDef = existingDef.getOriginatingBeanDefinition();
/* 145:282 */     if (originatingDef != null) {
/* 146:283 */       existingDef = originatingDef;
/* 147:    */     }
/* 148:285 */     if (isCompatible(beanDefinition, existingDef)) {
/* 149:286 */       return false;
/* 150:    */     }
/* 151:288 */     throw new ConflictingBeanDefinitionException("Annotation-specified bean name '" + beanName + 
/* 152:289 */       "' for bean class [" + beanDefinition.getBeanClassName() + "] conflicts with existing, " + 
/* 153:290 */       "non-compatible bean definition of same name and class [" + existingDef.getBeanClassName() + "]");
/* 154:    */   }
/* 155:    */   
/* 156:    */   protected boolean isCompatible(BeanDefinition newDefinition, BeanDefinition existingDefinition)
/* 157:    */   {
/* 158:307 */     return (!(existingDefinition instanceof AnnotatedBeanDefinition)) || (newDefinition.getSource().equals(existingDefinition.getSource())) || (newDefinition.equals(existingDefinition));
/* 159:    */   }
/* 160:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ClassPathBeanDefinitionScanner
 * JD-Core Version:    0.7.0.1
 */