/*   1:    */ package org.springframework.context.annotation;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.lang.reflect.Constructor;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Arrays;
/*   8:    */ import java.util.Collection;
/*   9:    */ import java.util.HashMap;
/*  10:    */ import java.util.List;
/*  11:    */ import java.util.Map;
/*  12:    */ import java.util.Map.Entry;
/*  13:    */ import java.util.Set;
/*  14:    */ import org.apache.commons.logging.Log;
/*  15:    */ import org.apache.commons.logging.LogFactory;
/*  16:    */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*  17:    */ import org.springframework.beans.factory.annotation.Autowire;
/*  18:    */ import org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor;
/*  19:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*  20:    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*  21:    */ import org.springframework.beans.factory.parsing.Location;
/*  22:    */ import org.springframework.beans.factory.parsing.Problem;
/*  23:    */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*  24:    */ import org.springframework.beans.factory.parsing.SourceExtractor;
/*  25:    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*  26:    */ import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
/*  27:    */ import org.springframework.beans.factory.support.BeanDefinitionReader;
/*  28:    */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*  29:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  30:    */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*  31:    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  32:    */ import org.springframework.core.io.Resource;
/*  33:    */ import org.springframework.core.io.ResourceLoader;
/*  34:    */ import org.springframework.core.type.AnnotationMetadata;
/*  35:    */ import org.springframework.core.type.MethodMetadata;
/*  36:    */ import org.springframework.core.type.classreading.MetadataReader;
/*  37:    */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*  38:    */ import org.springframework.util.StringUtils;
/*  39:    */ 
/*  40:    */ class ConfigurationClassBeanDefinitionReader
/*  41:    */ {
/*  42: 69 */   private static final Log logger = LogFactory.getLog(ConfigurationClassBeanDefinitionReader.class);
/*  43:    */   private final BeanDefinitionRegistry registry;
/*  44:    */   private final SourceExtractor sourceExtractor;
/*  45:    */   private final ProblemReporter problemReporter;
/*  46:    */   private final MetadataReaderFactory metadataReaderFactory;
/*  47:    */   private ResourceLoader resourceLoader;
/*  48:    */   
/*  49:    */   public ConfigurationClassBeanDefinitionReader(BeanDefinitionRegistry registry, SourceExtractor sourceExtractor, ProblemReporter problemReporter, MetadataReaderFactory metadataReaderFactory, ResourceLoader resourceLoader)
/*  50:    */   {
/*  51: 91 */     this.registry = registry;
/*  52: 92 */     this.sourceExtractor = sourceExtractor;
/*  53: 93 */     this.problemReporter = problemReporter;
/*  54: 94 */     this.metadataReaderFactory = metadataReaderFactory;
/*  55: 95 */     this.resourceLoader = resourceLoader;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void loadBeanDefinitions(Set<ConfigurationClass> configurationModel)
/*  59:    */   {
/*  60:104 */     for (ConfigurationClass configClass : configurationModel) {
/*  61:105 */       loadBeanDefinitionsForConfigurationClass(configClass);
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   private void loadBeanDefinitionsForConfigurationClass(ConfigurationClass configClass)
/*  66:    */   {
/*  67:114 */     doLoadBeanDefinitionForConfigurationClassIfNecessary(configClass);
/*  68:115 */     for (BeanMethod beanMethod : configClass.getBeanMethods()) {
/*  69:116 */       loadBeanDefinitionsForBeanMethod(beanMethod);
/*  70:    */     }
/*  71:118 */     loadBeanDefinitionsFromImportedResources(configClass.getImportedResources());
/*  72:    */   }
/*  73:    */   
/*  74:    */   private void doLoadBeanDefinitionForConfigurationClassIfNecessary(ConfigurationClass configClass)
/*  75:    */   {
/*  76:125 */     if (configClass.getBeanName() != null) {
/*  77:127 */       return;
/*  78:    */     }
/*  79:131 */     BeanDefinition configBeanDef = new GenericBeanDefinition();
/*  80:132 */     String className = configClass.getMetadata().getClassName();
/*  81:133 */     configBeanDef.setBeanClassName(className);
/*  82:134 */     if (ConfigurationClassUtils.checkConfigurationClassCandidate(configBeanDef, this.metadataReaderFactory))
/*  83:    */     {
/*  84:135 */       String configBeanName = BeanDefinitionReaderUtils.registerWithGeneratedName((AbstractBeanDefinition)configBeanDef, this.registry);
/*  85:136 */       configClass.setBeanName(configBeanName);
/*  86:137 */       if (logger.isDebugEnabled()) {
/*  87:138 */         logger.debug(String.format("Registered bean definition for imported @Configuration class %s", new Object[] { configBeanName }));
/*  88:    */       }
/*  89:    */     }
/*  90:    */     else
/*  91:    */     {
/*  92:    */       try
/*  93:    */       {
/*  94:143 */         MetadataReader reader = this.metadataReaderFactory.getMetadataReader(className);
/*  95:144 */         AnnotationMetadata metadata = reader.getAnnotationMetadata();
/*  96:145 */         this.problemReporter.error(
/*  97:146 */           new InvalidConfigurationImportProblem(className, reader.getResource(), metadata));
/*  98:    */       }
/*  99:    */       catch (IOException localIOException)
/* 100:    */       {
/* 101:149 */         throw new IllegalStateException("Could not create MetadataReader for class " + className);
/* 102:    */       }
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   private void loadBeanDefinitionsForBeanMethod(BeanMethod beanMethod)
/* 107:    */   {
/* 108:159 */     ConfigurationClass configClass = beanMethod.getConfigurationClass();
/* 109:160 */     MethodMetadata metadata = beanMethod.getMetadata();
/* 110:    */     
/* 111:162 */     RootBeanDefinition beanDef = new ConfigurationClassBeanDefinition(configClass);
/* 112:163 */     beanDef.setResource(configClass.getResource());
/* 113:164 */     beanDef.setSource(this.sourceExtractor.extractSource(metadata, configClass.getResource()));
/* 114:165 */     if (metadata.isStatic())
/* 115:    */     {
/* 116:167 */       beanDef.setBeanClassName(configClass.getMetadata().getClassName());
/* 117:168 */       beanDef.setFactoryMethodName(metadata.getMethodName());
/* 118:    */     }
/* 119:    */     else
/* 120:    */     {
/* 121:172 */       beanDef.setFactoryBeanName(configClass.getBeanName());
/* 122:173 */       beanDef.setUniqueFactoryMethodName(metadata.getMethodName());
/* 123:    */     }
/* 124:175 */     beanDef.setAutowireMode(3);
/* 125:176 */     beanDef.setAttribute(RequiredAnnotationBeanPostProcessor.SKIP_REQUIRED_CHECK_ATTRIBUTE, Boolean.TRUE);
/* 126:    */     
/* 127:    */ 
/* 128:179 */     Map<String, Object> roleAttributes = metadata.getAnnotationAttributes(Role.class.getName());
/* 129:180 */     if (roleAttributes != null)
/* 130:    */     {
/* 131:181 */       int role = ((Integer)roleAttributes.get("value")).intValue();
/* 132:182 */       beanDef.setRole(role);
/* 133:    */     }
/* 134:186 */     Map<String, Object> beanAttributes = metadata.getAnnotationAttributes(Bean.class.getName());
/* 135:187 */     List<String> names = new ArrayList((Collection)Arrays.asList((String[])beanAttributes.get("name")));
/* 136:188 */     String beanName = names.size() > 0 ? (String)names.remove(0) : beanMethod.getMetadata().getMethodName();
/* 137:189 */     for (String alias : names) {
/* 138:190 */       this.registry.registerAlias(beanName, alias);
/* 139:    */     }
/* 140:194 */     if (this.registry.containsBeanDefinition(beanName))
/* 141:    */     {
/* 142:195 */       BeanDefinition existingBeanDef = this.registry.getBeanDefinition(beanName);
/* 143:197 */       if (!(existingBeanDef instanceof ConfigurationClassBeanDefinition))
/* 144:    */       {
/* 145:200 */         if (logger.isDebugEnabled()) {
/* 146:201 */           logger.debug(
/* 147:202 */             String.format("Skipping loading bean definition for %s: a definition for bean '%s' already exists. This is likely due to an override in XML.", new Object[] {beanMethod, beanName }));
/* 148:    */         }
/* 149:204 */         return;
/* 150:    */       }
/* 151:    */     }
/* 152:208 */     if (metadata.isAnnotated(Primary.class.getName())) {
/* 153:209 */       beanDef.setPrimary(true);
/* 154:    */     }
/* 155:213 */     if (metadata.isAnnotated(Lazy.class.getName())) {
/* 156:214 */       beanDef.setLazyInit(((Boolean)metadata.getAnnotationAttributes(Lazy.class.getName()).get("value")).booleanValue());
/* 157:216 */     } else if (configClass.getMetadata().isAnnotated(Lazy.class.getName())) {
/* 158:217 */       beanDef.setLazyInit(((Boolean)configClass.getMetadata().getAnnotationAttributes(Lazy.class.getName()).get("value")).booleanValue());
/* 159:    */     }
/* 160:220 */     if (metadata.isAnnotated(DependsOn.class.getName()))
/* 161:    */     {
/* 162:221 */       String[] dependsOn = (String[])metadata.getAnnotationAttributes(DependsOn.class.getName()).get("value");
/* 163:222 */       if (dependsOn.length > 0) {
/* 164:223 */         beanDef.setDependsOn(dependsOn);
/* 165:    */       }
/* 166:    */     }
/* 167:227 */     Autowire autowire = (Autowire)beanAttributes.get("autowire");
/* 168:228 */     if (autowire.isAutowire()) {
/* 169:229 */       beanDef.setAutowireMode(autowire.value());
/* 170:    */     }
/* 171:232 */     String initMethodName = (String)beanAttributes.get("initMethod");
/* 172:233 */     if (StringUtils.hasText(initMethodName)) {
/* 173:234 */       beanDef.setInitMethodName(initMethodName);
/* 174:    */     }
/* 175:237 */     String destroyMethodName = (String)beanAttributes.get("destroyMethod");
/* 176:238 */     if (StringUtils.hasText(destroyMethodName)) {
/* 177:239 */       beanDef.setDestroyMethodName(destroyMethodName);
/* 178:    */     }
/* 179:243 */     ScopedProxyMode proxyMode = ScopedProxyMode.NO;
/* 180:244 */     Map<String, Object> scopeAttributes = metadata.getAnnotationAttributes(Scope.class.getName());
/* 181:245 */     if (scopeAttributes != null)
/* 182:    */     {
/* 183:246 */       beanDef.setScope((String)scopeAttributes.get("value"));
/* 184:247 */       proxyMode = (ScopedProxyMode)scopeAttributes.get("proxyMode");
/* 185:248 */       if (proxyMode == ScopedProxyMode.DEFAULT) {
/* 186:249 */         proxyMode = ScopedProxyMode.NO;
/* 187:    */       }
/* 188:    */     }
/* 189:254 */     BeanDefinition beanDefToRegister = beanDef;
/* 190:255 */     if (proxyMode != ScopedProxyMode.NO)
/* 191:    */     {
/* 192:256 */       BeanDefinitionHolder proxyDef = ScopedProxyCreator.createScopedProxy(
/* 193:257 */         new BeanDefinitionHolder(beanDef, beanName), this.registry, proxyMode == ScopedProxyMode.TARGET_CLASS);
/* 194:258 */       beanDefToRegister = proxyDef.getBeanDefinition();
/* 195:    */     }
/* 196:261 */     if (logger.isDebugEnabled()) {
/* 197:262 */       logger.debug(String.format("Registering bean definition for @Bean method %s.%s()", new Object[] { configClass.getMetadata().getClassName(), beanName }));
/* 198:    */     }
/* 199:265 */     this.registry.registerBeanDefinition(beanName, beanDefToRegister);
/* 200:    */   }
/* 201:    */   
/* 202:    */   private void loadBeanDefinitionsFromImportedResources(Map<String, Class<?>> importedResources)
/* 203:    */   {
/* 204:270 */     Map<Class<?>, BeanDefinitionReader> readerInstanceCache = new HashMap();
/* 205:271 */     for (Map.Entry<String, Class<?>> entry : importedResources.entrySet())
/* 206:    */     {
/* 207:272 */       String resource = (String)entry.getKey();
/* 208:273 */       Class<?> readerClass = (Class)entry.getValue();
/* 209:274 */       if (!readerInstanceCache.containsKey(readerClass)) {
/* 210:    */         try
/* 211:    */         {
/* 212:277 */           BeanDefinitionReader readerInstance = 
/* 213:278 */             (BeanDefinitionReader)readerClass.getConstructor(new Class[] { BeanDefinitionRegistry.class }).newInstance(new Object[] { this.registry });
/* 214:281 */           if ((readerInstance instanceof AbstractBeanDefinitionReader)) {
/* 215:282 */             ((AbstractBeanDefinitionReader)readerInstance).setResourceLoader(this.resourceLoader);
/* 216:    */           }
/* 217:285 */           readerInstanceCache.put(readerClass, readerInstance);
/* 218:    */         }
/* 219:    */         catch (Exception localException)
/* 220:    */         {
/* 221:288 */           throw new IllegalStateException("Could not instantiate BeanDefinitionReader class [" + readerClass.getName() + "]");
/* 222:    */         }
/* 223:    */       }
/* 224:291 */       BeanDefinitionReader reader = (BeanDefinitionReader)readerInstanceCache.get(readerClass);
/* 225:    */       
/* 226:293 */       reader.loadBeanDefinitions(resource);
/* 227:    */     }
/* 228:    */   }
/* 229:    */   
/* 230:    */   private static class ConfigurationClassBeanDefinition
/* 231:    */     extends RootBeanDefinition
/* 232:    */     implements AnnotatedBeanDefinition
/* 233:    */   {
/* 234:    */     private AnnotationMetadata annotationMetadata;
/* 235:    */     
/* 236:    */     public ConfigurationClassBeanDefinition(ConfigurationClass configClass)
/* 237:    */     {
/* 238:310 */       this.annotationMetadata = configClass.getMetadata();
/* 239:    */     }
/* 240:    */     
/* 241:    */     private ConfigurationClassBeanDefinition(ConfigurationClassBeanDefinition original)
/* 242:    */     {
/* 243:314 */       super();
/* 244:315 */       this.annotationMetadata = original.annotationMetadata;
/* 245:    */     }
/* 246:    */     
/* 247:    */     public AnnotationMetadata getMetadata()
/* 248:    */     {
/* 249:319 */       return this.annotationMetadata;
/* 250:    */     }
/* 251:    */     
/* 252:    */     public boolean isFactoryMethod(Method candidate)
/* 253:    */     {
/* 254:324 */       return (super.isFactoryMethod(candidate)) && (BeanAnnotationHelper.isBeanAnnotated(candidate));
/* 255:    */     }
/* 256:    */     
/* 257:    */     public ConfigurationClassBeanDefinition cloneBeanDefinition()
/* 258:    */     {
/* 259:329 */       return new ConfigurationClassBeanDefinition(this);
/* 260:    */     }
/* 261:    */   }
/* 262:    */   
/* 263:    */   private static class InvalidConfigurationImportProblem
/* 264:    */     extends Problem
/* 265:    */   {
/* 266:    */     public InvalidConfigurationImportProblem(String className, Resource resource, AnnotationMetadata metadata)
/* 267:    */     {
/* 268:343 */       super(new Location(resource, metadata));
/* 269:    */     }
/* 270:    */   }
/* 271:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader
 * JD-Core Version:    0.7.0.1
 */