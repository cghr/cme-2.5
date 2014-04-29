/*   1:    */ package org.springframework.context.annotation;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.LinkedHashSet;
/*   5:    */ import java.util.LinkedList;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Set;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.apache.commons.logging.LogFactory;
/*  10:    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*  11:    */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*  12:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*  13:    */ import org.springframework.context.ResourceLoaderAware;
/*  14:    */ import org.springframework.core.env.Environment;
/*  15:    */ import org.springframework.core.env.EnvironmentCapable;
/*  16:    */ import org.springframework.core.env.StandardEnvironment;
/*  17:    */ import org.springframework.core.io.Resource;
/*  18:    */ import org.springframework.core.io.ResourceLoader;
/*  19:    */ import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/*  20:    */ import org.springframework.core.io.support.ResourcePatternResolver;
/*  21:    */ import org.springframework.core.io.support.ResourcePatternUtils;
/*  22:    */ import org.springframework.core.type.AnnotationMetadata;
/*  23:    */ import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
/*  24:    */ import org.springframework.core.type.classreading.MetadataReader;
/*  25:    */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*  26:    */ import org.springframework.core.type.filter.AnnotationTypeFilter;
/*  27:    */ import org.springframework.core.type.filter.TypeFilter;
/*  28:    */ import org.springframework.stereotype.Component;
/*  29:    */ import org.springframework.util.Assert;
/*  30:    */ import org.springframework.util.ClassUtils;
/*  31:    */ 
/*  32:    */ public class ClassPathScanningCandidateComponentProvider
/*  33:    */   implements EnvironmentCapable, ResourceLoaderAware
/*  34:    */ {
/*  35:    */   static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
/*  36: 74 */   protected final Log logger = LogFactory.getLog(getClass());
/*  37: 76 */   private Environment environment = new StandardEnvironment();
/*  38: 78 */   private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
/*  39: 80 */   private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
/*  40: 82 */   private String resourcePattern = "**/*.class";
/*  41: 84 */   private final List<TypeFilter> includeFilters = new LinkedList();
/*  42: 86 */   private final List<TypeFilter> excludeFilters = new LinkedList();
/*  43:    */   
/*  44:    */   public ClassPathScanningCandidateComponentProvider(boolean useDefaultFilters)
/*  45:    */   {
/*  46: 98 */     if (useDefaultFilters) {
/*  47: 99 */       registerDefaultFilters();
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setResourceLoader(ResourceLoader resourceLoader)
/*  52:    */   {
/*  53:113 */     this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
/*  54:114 */     this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setEnvironment(Environment environment)
/*  58:    */   {
/*  59:124 */     this.environment = environment;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public Environment getEnvironment()
/*  63:    */   {
/*  64:128 */     return this.environment;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public final ResourceLoader getResourceLoader()
/*  68:    */   {
/*  69:135 */     return this.resourcePatternResolver;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setResourcePattern(String resourcePattern)
/*  73:    */   {
/*  74:145 */     Assert.notNull(resourcePattern, "'resourcePattern' must not be null");
/*  75:146 */     this.resourcePattern = resourcePattern;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void addIncludeFilter(TypeFilter includeFilter)
/*  79:    */   {
/*  80:153 */     this.includeFilters.add(includeFilter);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void addExcludeFilter(TypeFilter excludeFilter)
/*  84:    */   {
/*  85:160 */     this.excludeFilters.add(0, excludeFilter);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void resetFilters(boolean useDefaultFilters)
/*  89:    */   {
/*  90:172 */     this.includeFilters.clear();
/*  91:173 */     this.excludeFilters.clear();
/*  92:174 */     if (useDefaultFilters) {
/*  93:175 */       registerDefaultFilters();
/*  94:    */     }
/*  95:    */   }
/*  96:    */   
/*  97:    */   protected void registerDefaultFilters()
/*  98:    */   {
/*  99:191 */     this.includeFilters.add(new AnnotationTypeFilter(Component.class));
/* 100:192 */     ClassLoader cl = ClassPathScanningCandidateComponentProvider.class.getClassLoader();
/* 101:    */     try
/* 102:    */     {
/* 103:194 */       this.includeFilters.add(new AnnotationTypeFilter(
/* 104:195 */         cl.loadClass("javax.annotation.ManagedBean"), false));
/* 105:196 */       this.logger.info("JSR-250 'javax.annotation.ManagedBean' found and supported for component scanning");
/* 106:    */     }
/* 107:    */     catch (ClassNotFoundException localClassNotFoundException1) {}
/* 108:    */     try
/* 109:    */     {
/* 110:202 */       this.includeFilters.add(new AnnotationTypeFilter(
/* 111:203 */         cl.loadClass("javax.inject.Named"), false));
/* 112:204 */       this.logger.info("JSR-330 'javax.inject.Named' annotation found and supported for component scanning");
/* 113:    */     }
/* 114:    */     catch (ClassNotFoundException localClassNotFoundException2) {}
/* 115:    */   }
/* 116:    */   
/* 117:    */   public Set<BeanDefinition> findCandidateComponents(String basePackage)
/* 118:    */   {
/* 119:218 */     Set<BeanDefinition> candidates = new LinkedHashSet();
/* 120:    */     try
/* 121:    */     {
/* 122:220 */       String packageSearchPath = "classpath*:" + 
/* 123:221 */         resolveBasePackage(basePackage) + "/" + this.resourcePattern;
/* 124:222 */       Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
/* 125:223 */       boolean traceEnabled = this.logger.isTraceEnabled();
/* 126:224 */       boolean debugEnabled = this.logger.isDebugEnabled();
/* 127:225 */       for (Resource resource : resources)
/* 128:    */       {
/* 129:226 */         if (traceEnabled) {
/* 130:227 */           this.logger.trace("Scanning " + resource);
/* 131:    */         }
/* 132:229 */         if (resource.isReadable()) {
/* 133:    */           try
/* 134:    */           {
/* 135:231 */             MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
/* 136:232 */             if (isCandidateComponent(metadataReader))
/* 137:    */             {
/* 138:233 */               ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
/* 139:234 */               sbd.setResource(resource);
/* 140:235 */               sbd.setSource(resource);
/* 141:236 */               if (isCandidateComponent(sbd))
/* 142:    */               {
/* 143:237 */                 if (debugEnabled) {
/* 144:238 */                   this.logger.debug("Identified candidate component class: " + resource);
/* 145:    */                 }
/* 146:240 */                 candidates.add(sbd);
/* 147:    */               }
/* 148:243 */               else if (debugEnabled)
/* 149:    */               {
/* 150:244 */                 this.logger.debug("Ignored because not a concrete top-level class: " + resource);
/* 151:    */               }
/* 152:    */             }
/* 153:249 */             else if (traceEnabled)
/* 154:    */             {
/* 155:250 */               this.logger.trace("Ignored because not matching any filter: " + resource);
/* 156:    */             }
/* 157:    */           }
/* 158:    */           catch (Throwable ex)
/* 159:    */           {
/* 160:255 */             throw new BeanDefinitionStoreException(
/* 161:256 */               "Failed to read candidate component class: " + resource, ex);
/* 162:    */           }
/* 163:260 */         } else if (traceEnabled) {
/* 164:261 */           this.logger.trace("Ignored because not readable: " + resource);
/* 165:    */         }
/* 166:    */       }
/* 167:    */     }
/* 168:    */     catch (IOException ex)
/* 169:    */     {
/* 170:267 */       throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
/* 171:    */     }
/* 172:269 */     return candidates;
/* 173:    */   }
/* 174:    */   
/* 175:    */   protected String resolveBasePackage(String basePackage)
/* 176:    */   {
/* 177:282 */     return ClassUtils.convertClassNameToResourcePath(this.environment.resolveRequiredPlaceholders(basePackage));
/* 178:    */   }
/* 179:    */   
/* 180:    */   protected boolean isCandidateComponent(MetadataReader metadataReader)
/* 181:    */     throws IOException
/* 182:    */   {
/* 183:292 */     for (TypeFilter tf : this.excludeFilters) {
/* 184:293 */       if (tf.match(metadataReader, this.metadataReaderFactory)) {
/* 185:294 */         return false;
/* 186:    */       }
/* 187:    */     }
/* 188:297 */     for (TypeFilter tf : this.includeFilters) {
/* 189:298 */       if (tf.match(metadataReader, this.metadataReaderFactory))
/* 190:    */       {
/* 191:299 */         AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
/* 192:300 */         if (!ProfileHelper.isProfileAnnotationPresent(metadata)) {
/* 193:301 */           return true;
/* 194:    */         }
/* 195:303 */         return this.environment.acceptsProfiles(ProfileHelper.getCandidateProfiles(metadata));
/* 196:    */       }
/* 197:    */     }
/* 198:306 */     return false;
/* 199:    */   }
/* 200:    */   
/* 201:    */   protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition)
/* 202:    */   {
/* 203:317 */     return (beanDefinition.getMetadata().isConcrete()) && (beanDefinition.getMetadata().isIndependent());
/* 204:    */   }
/* 205:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
 * JD-Core Version:    0.7.0.1
 */