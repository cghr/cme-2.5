/*   1:    */ package org.springframework.web.context.support;
/*   2:    */ 
/*   3:    */ import org.apache.commons.logging.Log;
/*   4:    */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*   5:    */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*   6:    */ import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
/*   7:    */ import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
/*   8:    */ import org.springframework.context.annotation.ScopeMetadataResolver;
/*   9:    */ import org.springframework.util.Assert;
/*  10:    */ import org.springframework.util.ObjectUtils;
/*  11:    */ import org.springframework.util.StringUtils;
/*  12:    */ 
/*  13:    */ public class AnnotationConfigWebApplicationContext
/*  14:    */   extends AbstractRefreshableWebApplicationContext
/*  15:    */ {
/*  16:    */   private Class<?>[] annotatedClasses;
/*  17:    */   private String[] basePackages;
/*  18:    */   private BeanNameGenerator beanNameGenerator;
/*  19:    */   private ScopeMetadataResolver scopeMetadataResolver;
/*  20:    */   
/*  21:    */   public void setConfigLocation(String location)
/*  22:    */   {
/*  23:107 */     super.setConfigLocation(location);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setConfigLocations(String[] locations)
/*  27:    */   {
/*  28:129 */     super.setConfigLocations(locations);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void register(Class<?>... annotatedClasses)
/*  32:    */   {
/*  33:146 */     Assert.notEmpty(annotatedClasses, "At least one annotated class must be specified");
/*  34:147 */     this.annotatedClasses = annotatedClasses;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void scan(String... basePackages)
/*  38:    */   {
/*  39:161 */     Assert.notEmpty(basePackages, "At least one base package must be specified");
/*  40:162 */     this.basePackages = basePackages;
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
/*  44:    */   {
/*  45:188 */     AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanFactory);
/*  46:189 */     reader.setEnvironment(getEnvironment());
/*  47:    */     
/*  48:191 */     ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);
/*  49:192 */     scanner.setEnvironment(getEnvironment());
/*  50:    */     
/*  51:194 */     BeanNameGenerator beanNameGenerator = getBeanNameGenerator();
/*  52:195 */     ScopeMetadataResolver scopeMetadataResolver = getScopeMetadataResolver();
/*  53:196 */     if (beanNameGenerator != null)
/*  54:    */     {
/*  55:197 */       reader.setBeanNameGenerator(beanNameGenerator);
/*  56:198 */       scanner.setBeanNameGenerator(beanNameGenerator);
/*  57:    */     }
/*  58:200 */     if (scopeMetadataResolver != null)
/*  59:    */     {
/*  60:201 */       reader.setScopeMetadataResolver(scopeMetadataResolver);
/*  61:202 */       scanner.setScopeMetadataResolver(scopeMetadataResolver);
/*  62:    */     }
/*  63:205 */     if (!ObjectUtils.isEmpty(this.annotatedClasses))
/*  64:    */     {
/*  65:206 */       if (this.logger.isInfoEnabled()) {
/*  66:207 */         this.logger.info("Registering annotated classes: [" + 
/*  67:208 */           StringUtils.arrayToCommaDelimitedString(this.annotatedClasses) + "]");
/*  68:    */       }
/*  69:210 */       reader.register(this.annotatedClasses);
/*  70:    */     }
/*  71:213 */     if (!ObjectUtils.isEmpty(this.basePackages))
/*  72:    */     {
/*  73:214 */       if (this.logger.isInfoEnabled()) {
/*  74:215 */         this.logger.info("Scanning base packages: [" + 
/*  75:216 */           StringUtils.arrayToCommaDelimitedString(this.basePackages) + "]");
/*  76:    */       }
/*  77:218 */       scanner.scan(this.basePackages);
/*  78:    */     }
/*  79:221 */     String[] configLocations = getConfigLocations();
/*  80:222 */     if (configLocations != null) {
/*  81:223 */       for (String configLocation : configLocations) {
/*  82:    */         try
/*  83:    */         {
/*  84:225 */           Class<?> clazz = getClassLoader().loadClass(configLocation);
/*  85:226 */           if (this.logger.isInfoEnabled()) {
/*  86:227 */             this.logger.info("Successfully resolved class for [" + configLocation + "]");
/*  87:    */           }
/*  88:229 */           reader.register(new Class[] { clazz });
/*  89:    */         }
/*  90:    */         catch (ClassNotFoundException ex)
/*  91:    */         {
/*  92:232 */           if (this.logger.isDebugEnabled()) {
/*  93:233 */             this.logger.debug("Could not load class for config location [" + configLocation + 
/*  94:234 */               "] - trying package scan. " + ex);
/*  95:    */           }
/*  96:236 */           int count = scanner.scan(new String[] { configLocation });
/*  97:237 */           if (this.logger.isInfoEnabled()) {
/*  98:238 */             if (count == 0) {
/*  99:239 */               this.logger.info("No annotated classes found for specified class/package [" + configLocation + "]");
/* 100:    */             } else {
/* 101:242 */               this.logger.info("Found " + count + " annotated classes in package [" + configLocation + "]");
/* 102:    */             }
/* 103:    */           }
/* 104:    */         }
/* 105:    */       }
/* 106:    */     }
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator)
/* 110:    */   {
/* 111:251 */     this.beanNameGenerator = beanNameGenerator;
/* 112:    */   }
/* 113:    */   
/* 114:    */   protected BeanNameGenerator getBeanNameGenerator()
/* 115:    */   {
/* 116:262 */     return this.beanNameGenerator;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setScopeMetadataResolver(ScopeMetadataResolver scopeMetadataResolver)
/* 120:    */   {
/* 121:270 */     this.scopeMetadataResolver = scopeMetadataResolver;
/* 122:    */   }
/* 123:    */   
/* 124:    */   protected ScopeMetadataResolver getScopeMetadataResolver()
/* 125:    */   {
/* 126:281 */     return this.scopeMetadataResolver;
/* 127:    */   }
/* 128:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.AnnotationConfigWebApplicationContext
 * JD-Core Version:    0.7.0.1
 */