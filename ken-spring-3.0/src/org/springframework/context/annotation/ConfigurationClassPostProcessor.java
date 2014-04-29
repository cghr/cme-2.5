/*   1:    */ package org.springframework.context.annotation;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.HashSet;
/*   5:    */ import java.util.LinkedHashMap;
/*   6:    */ import java.util.LinkedHashSet;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Map.Entry;
/*   9:    */ import java.util.Set;
/*  10:    */ import java.util.Stack;
/*  11:    */ import org.apache.commons.logging.Log;
/*  12:    */ import org.apache.commons.logging.LogFactory;
/*  13:    */ import org.springframework.beans.BeansException;
/*  14:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  15:    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*  16:    */ import org.springframework.beans.factory.BeanFactory;
/*  17:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*  18:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*  19:    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*  20:    */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*  21:    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*  22:    */ import org.springframework.beans.factory.config.SingletonBeanRegistry;
/*  23:    */ import org.springframework.beans.factory.parsing.FailFastProblemReporter;
/*  24:    */ import org.springframework.beans.factory.parsing.PassThroughSourceExtractor;
/*  25:    */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*  26:    */ import org.springframework.beans.factory.parsing.SourceExtractor;
/*  27:    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*  28:    */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*  29:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  30:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
/*  31:    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  32:    */ import org.springframework.context.EnvironmentAware;
/*  33:    */ import org.springframework.context.ResourceLoaderAware;
/*  34:    */ import org.springframework.core.PriorityOrdered;
/*  35:    */ import org.springframework.core.env.ConfigurableEnvironment;
/*  36:    */ import org.springframework.core.env.Environment;
/*  37:    */ import org.springframework.core.env.MutablePropertySources;
/*  38:    */ import org.springframework.core.env.PropertySource;
/*  39:    */ import org.springframework.core.io.DefaultResourceLoader;
/*  40:    */ import org.springframework.core.io.ResourceLoader;
/*  41:    */ import org.springframework.core.type.AnnotationMetadata;
/*  42:    */ import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
/*  43:    */ import org.springframework.core.type.classreading.MetadataReader;
/*  44:    */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*  45:    */ import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
/*  46:    */ import org.springframework.util.Assert;
/*  47:    */ import org.springframework.util.ClassUtils;
/*  48:    */ 
/*  49:    */ public class ConfigurationClassPostProcessor
/*  50:    */   implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware
/*  51:    */ {
/*  52: 89 */   private static final boolean cglibAvailable = ClassUtils.isPresent(
/*  53: 90 */     "net.sf.cglib.proxy.Enhancer", ConfigurationClassPostProcessor.class.getClassLoader());
/*  54: 93 */   private final Log logger = LogFactory.getLog(getClass());
/*  55: 95 */   private SourceExtractor sourceExtractor = new PassThroughSourceExtractor();
/*  56: 97 */   private ProblemReporter problemReporter = new FailFastProblemReporter();
/*  57:    */   private Environment environment;
/*  58:101 */   private ResourceLoader resourceLoader = new DefaultResourceLoader();
/*  59:103 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  60:105 */   private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
/*  61:107 */   private boolean setMetadataReaderFactoryCalled = false;
/*  62:109 */   private final Set<Integer> registriesPostProcessed = new HashSet();
/*  63:111 */   private final Set<Integer> factoriesPostProcessed = new HashSet();
/*  64:    */   private ConfigurationClassBeanDefinitionReader reader;
/*  65:    */   
/*  66:    */   public void setSourceExtractor(SourceExtractor sourceExtractor)
/*  67:    */   {
/*  68:121 */     this.sourceExtractor = (sourceExtractor != null ? sourceExtractor : new PassThroughSourceExtractor());
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setProblemReporter(ProblemReporter problemReporter)
/*  72:    */   {
/*  73:131 */     this.problemReporter = (problemReporter != null ? problemReporter : new FailFastProblemReporter());
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void setMetadataReaderFactory(MetadataReaderFactory metadataReaderFactory)
/*  77:    */   {
/*  78:140 */     Assert.notNull(metadataReaderFactory, "MetadataReaderFactory must not be null");
/*  79:141 */     this.metadataReaderFactory = metadataReaderFactory;
/*  80:142 */     this.setMetadataReaderFactoryCalled = true;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void setEnvironment(Environment environment)
/*  84:    */   {
/*  85:146 */     Assert.notNull(environment, "Environment must not be null");
/*  86:147 */     this.environment = environment;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setResourceLoader(ResourceLoader resourceLoader)
/*  90:    */   {
/*  91:151 */     Assert.notNull(resourceLoader, "ResourceLoader must not be null");
/*  92:152 */     this.resourceLoader = resourceLoader;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setBeanClassLoader(ClassLoader beanClassLoader)
/*  96:    */   {
/*  97:156 */     this.beanClassLoader = beanClassLoader;
/*  98:157 */     if (!this.setMetadataReaderFactoryCalled) {
/*  99:158 */       this.metadataReaderFactory = new CachingMetadataReaderFactory(beanClassLoader);
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
/* 104:    */   {
/* 105:167 */     BeanDefinitionReaderUtils.registerWithGeneratedName(new RootBeanDefinition(ImportAwareBeanPostProcessor.class), registry);
/* 106:168 */     int registryId = System.identityHashCode(registry);
/* 107:169 */     if (this.registriesPostProcessed.contains(Integer.valueOf(registryId))) {
/* 108:170 */       throw new IllegalStateException(
/* 109:171 */         "postProcessBeanDefinitionRegistry already called for this post-processor against " + registry);
/* 110:    */     }
/* 111:173 */     if (this.factoriesPostProcessed.contains(Integer.valueOf(registryId))) {
/* 112:174 */       throw new IllegalStateException(
/* 113:175 */         "postProcessBeanFactory already called for this post-processor against " + registry);
/* 114:    */     }
/* 115:177 */     this.registriesPostProcessed.add(Integer.valueOf(registryId));
/* 116:178 */     processConfigBeanDefinitions(registry);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/* 120:    */   {
/* 121:186 */     int factoryId = System.identityHashCode(beanFactory);
/* 122:187 */     if (this.factoriesPostProcessed.contains(Integer.valueOf(factoryId))) {
/* 123:188 */       throw new IllegalStateException(
/* 124:189 */         "postProcessBeanFactory already called for this post-processor against " + beanFactory);
/* 125:    */     }
/* 126:191 */     this.factoriesPostProcessed.add(Integer.valueOf(factoryId));
/* 127:192 */     if (!this.registriesPostProcessed.contains(Integer.valueOf(factoryId))) {
/* 128:195 */       processConfigBeanDefinitions((BeanDefinitionRegistry)beanFactory);
/* 129:    */     }
/* 130:197 */     enhanceConfigurationClasses(beanFactory);
/* 131:    */   }
/* 132:    */   
/* 133:    */   private ConfigurationClassBeanDefinitionReader getConfigurationClassBeanDefinitionReader(BeanDefinitionRegistry registry)
/* 134:    */   {
/* 135:201 */     if (this.reader == null) {
/* 136:202 */       this.reader = new ConfigurationClassBeanDefinitionReader(
/* 137:203 */         registry, this.sourceExtractor, this.problemReporter, this.metadataReaderFactory, this.resourceLoader);
/* 138:    */     }
/* 139:205 */     return this.reader;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public void processConfigBeanDefinitions(BeanDefinitionRegistry registry)
/* 143:    */   {
/* 144:213 */     Set<BeanDefinitionHolder> configCandidates = new LinkedHashSet();
/* 145:214 */     for (String beanName : registry.getBeanDefinitionNames())
/* 146:    */     {
/* 147:215 */       BeanDefinition beanDef = registry.getBeanDefinition(beanName);
/* 148:216 */       if (ConfigurationClassUtils.checkConfigurationClassCandidate(beanDef, this.metadataReaderFactory)) {
/* 149:217 */         configCandidates.add(new BeanDefinitionHolder(beanDef, beanName));
/* 150:    */       }
/* 151:    */     }
/* 152:222 */     if (configCandidates.isEmpty()) {
/* 153:223 */       return;
/* 154:    */     }
/* 155:227 */     ConfigurationClassParser parser = new ConfigurationClassParser(
/* 156:228 */       this.metadataReaderFactory, this.problemReporter, this.environment, this.resourceLoader, registry);
/* 157:229 */     for (BeanDefinitionHolder holder : configCandidates)
/* 158:    */     {
/* 159:230 */       BeanDefinition bd = holder.getBeanDefinition();
/* 160:    */       try
/* 161:    */       {
/* 162:232 */         if (((bd instanceof AbstractBeanDefinition)) && (((AbstractBeanDefinition)bd).hasBeanClass())) {
/* 163:233 */           parser.parse(((AbstractBeanDefinition)bd).getBeanClass(), holder.getBeanName());
/* 164:    */         } else {
/* 165:236 */           parser.parse(bd.getBeanClassName(), holder.getBeanName());
/* 166:    */         }
/* 167:    */       }
/* 168:    */       catch (IOException ex)
/* 169:    */       {
/* 170:240 */         throw new BeanDefinitionStoreException("Failed to load bean class: " + bd.getBeanClassName(), ex);
/* 171:    */       }
/* 172:    */     }
/* 173:243 */     parser.validate();
/* 174:    */     
/* 175:    */ 
/* 176:246 */     Object parsedPropertySources = parser.getPropertySources();
/* 177:247 */     if (!((Stack)parsedPropertySources).isEmpty()) {
/* 178:248 */       if (!(this.environment instanceof ConfigurableEnvironment))
/* 179:    */       {
/* 180:249 */         this.logger.warn("Ignoring @PropertySource annotations. Reason: Environment must implement ConfigurableEnvironment");
/* 181:    */       }
/* 182:    */       else
/* 183:    */       {
/* 184:253 */         MutablePropertySources envPropertySources = ((ConfigurableEnvironment)this.environment).getPropertySources();
/* 185:254 */         while (!((Stack)parsedPropertySources).isEmpty()) {
/* 186:255 */           envPropertySources.addLast((PropertySource)((Stack)parsedPropertySources).pop());
/* 187:    */         }
/* 188:    */       }
/* 189:    */     }
/* 190:261 */     getConfigurationClassBeanDefinitionReader(registry).loadBeanDefinitions(parser.getConfigurationClasses());
/* 191:264 */     if (((registry instanceof SingletonBeanRegistry)) && 
/* 192:265 */       (!((SingletonBeanRegistry)registry).containsSingleton("importRegistry"))) {
/* 193:266 */       ((SingletonBeanRegistry)registry).registerSingleton("importRegistry", parser.getImportRegistry());
/* 194:    */     }
/* 195:    */   }
/* 196:    */   
/* 197:    */   public void enhanceConfigurationClasses(ConfigurableListableBeanFactory beanFactory)
/* 198:    */   {
/* 199:278 */     Map<String, AbstractBeanDefinition> configBeanDefs = new LinkedHashMap();
/* 200:279 */     for (String beanName : beanFactory.getBeanDefinitionNames())
/* 201:    */     {
/* 202:280 */       BeanDefinition beanDef = beanFactory.getBeanDefinition(beanName);
/* 203:281 */       if (ConfigurationClassUtils.isFullConfigurationClass(beanDef))
/* 204:    */       {
/* 205:282 */         if (!(beanDef instanceof AbstractBeanDefinition)) {
/* 206:283 */           throw new BeanDefinitionStoreException("Cannot enhance @Configuration bean definition '" + 
/* 207:284 */             beanName + "' since it is not stored in an AbstractBeanDefinition subclass");
/* 208:    */         }
/* 209:286 */         configBeanDefs.put(beanName, (AbstractBeanDefinition)beanDef);
/* 210:    */       }
/* 211:    */     }
/* 212:289 */     if (configBeanDefs.isEmpty()) {
/* 213:291 */       return;
/* 214:    */     }
/* 215:293 */     if (!cglibAvailable) {
/* 216:294 */       throw new IllegalStateException("CGLIB is required to process @Configuration classes. Either add CGLIB to the classpath or remove the following @Configuration bean definitions: " + 
/* 217:    */       
/* 218:296 */         configBeanDefs.keySet());
/* 219:    */     }
/* 220:298 */     ConfigurationClassEnhancer enhancer = new ConfigurationClassEnhancer(beanFactory);
/* 221:299 */     for (Object entry : configBeanDefs.entrySet())
/* 222:    */     {
/* 223:300 */       AbstractBeanDefinition beanDef = (AbstractBeanDefinition)((Map.Entry)entry).getValue();
/* 224:    */       try
/* 225:    */       {
/* 226:302 */         Class<?> configClass = beanDef.resolveBeanClass(this.beanClassLoader);
/* 227:303 */         Class<?> enhancedClass = enhancer.enhance(configClass);
/* 228:304 */         if (this.logger.isDebugEnabled()) {
/* 229:305 */           this.logger.debug(
/* 230:306 */             String.format("Replacing bean definition '%s' existing class name '%s' with enhanced class name '%s'", new Object[] {((Map.Entry)entry).getKey(), configClass.getName(), enhancedClass.getName() }));
/* 231:    */         }
/* 232:308 */         beanDef.setBeanClass(enhancedClass);
/* 233:    */       }
/* 234:    */       catch (Throwable ex)
/* 235:    */       {
/* 236:311 */         throw new IllegalStateException("Cannot load configuration class: " + beanDef.getBeanClassName(), ex);
/* 237:    */       }
/* 238:    */     }
/* 239:    */   }
/* 240:    */   
/* 241:    */   private static class ImportAwareBeanPostProcessor
/* 242:    */     implements PriorityOrdered, BeanFactoryAware, BeanPostProcessor
/* 243:    */   {
/* 244:    */     private BeanFactory beanFactory;
/* 245:    */     
/* 246:    */     public void setBeanFactory(BeanFactory beanFactory)
/* 247:    */       throws BeansException
/* 248:    */     {
/* 249:322 */       this.beanFactory = beanFactory;
/* 250:    */     }
/* 251:    */     
/* 252:    */     public Object postProcessBeforeInitialization(Object bean, String beanName)
/* 253:    */       throws BeansException
/* 254:    */     {
/* 255:326 */       if ((bean instanceof ImportAware))
/* 256:    */       {
/* 257:327 */         ConfigurationClassParser.ImportRegistry importRegistry = (ConfigurationClassParser.ImportRegistry)this.beanFactory.getBean(ConfigurationClassParser.ImportRegistry.class);
/* 258:328 */         String importingClass = importRegistry.getImportingClassFor(bean.getClass().getSuperclass().getName());
/* 259:329 */         if (importingClass != null) {
/* 260:    */           try
/* 261:    */           {
/* 262:331 */             AnnotationMetadata metadata = 
/* 263:332 */               new SimpleMetadataReaderFactory().getMetadataReader(importingClass).getAnnotationMetadata();
/* 264:333 */             ((ImportAware)bean).setImportMetadata(metadata);
/* 265:    */           }
/* 266:    */           catch (IOException ex)
/* 267:    */           {
/* 268:337 */             throw new IllegalStateException(ex);
/* 269:    */           }
/* 270:    */         }
/* 271:    */       }
/* 272:344 */       return bean;
/* 273:    */     }
/* 274:    */     
/* 275:    */     public Object postProcessAfterInitialization(Object bean, String beanName)
/* 276:    */       throws BeansException
/* 277:    */     {
/* 278:348 */       return bean;
/* 279:    */     }
/* 280:    */     
/* 281:    */     public int getOrder()
/* 282:    */     {
/* 283:352 */       return -2147483648;
/* 284:    */     }
/* 285:    */   }
/* 286:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ConfigurationClassPostProcessor
 * JD-Core Version:    0.7.0.1
 */