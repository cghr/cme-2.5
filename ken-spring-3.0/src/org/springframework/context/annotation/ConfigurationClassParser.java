/*   1:    */ package org.springframework.context.annotation;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.Comparator;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.LinkedHashSet;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Map;
/*  11:    */ import java.util.Set;
/*  12:    */ import java.util.Stack;
/*  13:    */ import org.springframework.beans.BeanUtils;
/*  14:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*  15:    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*  16:    */ import org.springframework.beans.factory.parsing.Location;
/*  17:    */ import org.springframework.beans.factory.parsing.Problem;
/*  18:    */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*  19:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  20:    */ import org.springframework.core.annotation.AnnotationUtils;
/*  21:    */ import org.springframework.core.env.Environment;
/*  22:    */ import org.springframework.core.io.ResourceLoader;
/*  23:    */ import org.springframework.core.io.ResourcePropertySource;
/*  24:    */ import org.springframework.core.type.AnnotationMetadata;
/*  25:    */ import org.springframework.core.type.MethodMetadata;
/*  26:    */ import org.springframework.core.type.StandardAnnotationMetadata;
/*  27:    */ import org.springframework.core.type.classreading.MetadataReader;
/*  28:    */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*  29:    */ import org.springframework.core.type.filter.AssignableTypeFilter;
/*  30:    */ import org.springframework.util.StringUtils;
/*  31:    */ 
/*  32:    */ class ConfigurationClassParser
/*  33:    */ {
/*  34:    */   private final MetadataReaderFactory metadataReaderFactory;
/*  35:    */   private final ProblemReporter problemReporter;
/*  36: 75 */   private final ImportStack importStack = new ImportStack(null);
/*  37: 78 */   private final Set<ConfigurationClass> configurationClasses = new LinkedHashSet();
/*  38: 81 */   private final Stack<org.springframework.core.env.PropertySource<?>> propertySources = new Stack();
/*  39:    */   private final Environment environment;
/*  40:    */   private final ResourceLoader resourceLoader;
/*  41:    */   private final BeanDefinitionRegistry registry;
/*  42:    */   private final ComponentScanAnnotationParser componentScanParser;
/*  43:    */   
/*  44:    */   public ConfigurationClassParser(MetadataReaderFactory metadataReaderFactory, ProblemReporter problemReporter, Environment environment, ResourceLoader resourceLoader, BeanDefinitionRegistry registry)
/*  45:    */   {
/*  46: 99 */     this.metadataReaderFactory = metadataReaderFactory;
/*  47:100 */     this.problemReporter = problemReporter;
/*  48:101 */     this.environment = environment;
/*  49:102 */     this.resourceLoader = resourceLoader;
/*  50:103 */     this.registry = registry;
/*  51:104 */     this.componentScanParser = 
/*  52:105 */       new ComponentScanAnnotationParser(this.resourceLoader, this.environment, this.registry);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void parse(String className, String beanName)
/*  56:    */     throws IOException
/*  57:    */   {
/*  58:116 */     MetadataReader reader = this.metadataReaderFactory.getMetadataReader(className);
/*  59:117 */     processConfigurationClass(new ConfigurationClass(reader, beanName));
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void parse(Class<?> clazz, String beanName)
/*  63:    */     throws IOException
/*  64:    */   {
/*  65:127 */     processConfigurationClass(new ConfigurationClass(clazz, beanName));
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected void processConfigurationClass(ConfigurationClass configClass)
/*  69:    */     throws IOException
/*  70:    */   {
/*  71:131 */     AnnotationMetadata metadata = configClass.getMetadata();
/*  72:132 */     if ((this.environment != null) && (ProfileHelper.isProfileAnnotationPresent(metadata)) && 
/*  73:133 */       (!this.environment.acceptsProfiles(ProfileHelper.getCandidateProfiles(metadata)))) {
/*  74:134 */       return;
/*  75:    */     }
/*  76:138 */     while (metadata != null)
/*  77:    */     {
/*  78:139 */       doProcessConfigurationClass(configClass, metadata);
/*  79:140 */       String superClassName = metadata.getSuperClassName();
/*  80:141 */       if ((superClassName != null) && (!Object.class.getName().equals(superClassName)))
/*  81:    */       {
/*  82:142 */         if ((metadata instanceof StandardAnnotationMetadata))
/*  83:    */         {
/*  84:143 */           Class<?> clazz = ((StandardAnnotationMetadata)metadata).getIntrospectedClass();
/*  85:144 */           metadata = new StandardAnnotationMetadata(clazz.getSuperclass());
/*  86:    */         }
/*  87:    */         else
/*  88:    */         {
/*  89:147 */           MetadataReader reader = this.metadataReaderFactory.getMetadataReader(superClassName);
/*  90:148 */           metadata = reader.getAnnotationMetadata();
/*  91:    */         }
/*  92:    */       }
/*  93:    */       else {
/*  94:152 */         metadata = null;
/*  95:    */       }
/*  96:    */     }
/*  97:155 */     if ((this.configurationClasses.contains(configClass)) && (configClass.getBeanName() != null)) {
/*  98:158 */       this.configurationClasses.remove(configClass);
/*  99:    */     }
/* 100:161 */     this.configurationClasses.add(configClass);
/* 101:    */   }
/* 102:    */   
/* 103:    */   protected void doProcessConfigurationClass(ConfigurationClass configClass, AnnotationMetadata metadata)
/* 104:    */     throws IOException
/* 105:    */   {
/* 106:    */     AnnotationMetadata memberClassMetadata;
/* 107:167 */     for (String memberClassName : metadata.getMemberClassNames())
/* 108:    */     {
/* 109:168 */       MetadataReader reader = this.metadataReaderFactory.getMetadataReader(memberClassName);
/* 110:169 */       memberClassMetadata = reader.getAnnotationMetadata();
/* 111:170 */       if (ConfigurationClassUtils.isConfigurationCandidate(memberClassMetadata)) {
/* 112:171 */         processConfigurationClass(new ConfigurationClass(reader, null));
/* 113:    */       }
/* 114:    */     }
/* 115:176 */     Map<String, Object> propertySourceAttributes = 
/* 116:177 */       metadata.getAnnotationAttributes(PropertySource.class.getName());
/* 117:    */     String location;
/* 118:    */     ResourcePropertySource ps;
/* 119:178 */     if (propertySourceAttributes != null)
/* 120:    */     {
/* 121:179 */       String name = (String)propertySourceAttributes.get("name");
/* 122:180 */       String[] locations = (String[])propertySourceAttributes.get("value");
/* 123:181 */       ClassLoader classLoader = this.resourceLoader.getClassLoader();
/* 124:    */       String[] arrayOfString2;
/* 125:182 */       AnnotationMetadata localAnnotationMetadata1 = (arrayOfString2 = locations).length;
/* 126:182 */       for (memberClassMetadata = 0; memberClassMetadata < localAnnotationMetadata1; memberClassMetadata++)
/* 127:    */       {
/* 128:182 */         location = arrayOfString2[memberClassMetadata];
/* 129:183 */         location = this.environment.resolveRequiredPlaceholders(location);
/* 130:184 */         ps = StringUtils.hasText(name) ? 
/* 131:185 */           new ResourcePropertySource(name, location, classLoader) : 
/* 132:186 */           new ResourcePropertySource(location, classLoader);
/* 133:187 */         this.propertySources.push(ps);
/* 134:    */       }
/* 135:    */     }
/* 136:192 */     Object componentScanAttributes = metadata.getAnnotationAttributes(ComponentScan.class.getName());
/* 137:193 */     if (componentScanAttributes != null)
/* 138:    */     {
/* 139:195 */       Object scannedBeanDefinitions = this.componentScanParser.parse((Map)componentScanAttributes);
/* 140:198 */       for (BeanDefinitionHolder holder : (Set)scannedBeanDefinitions) {
/* 141:199 */         if (ConfigurationClassUtils.checkConfigurationClassCandidate(holder.getBeanDefinition(), this.metadataReaderFactory)) {
/* 142:    */           try
/* 143:    */           {
/* 144:201 */             parse(holder.getBeanDefinition().getBeanClassName(), holder.getBeanName());
/* 145:    */           }
/* 146:    */           catch (ConflictingBeanDefinitionException ex)
/* 147:    */           {
/* 148:203 */             throw new CircularComponentScanException(
/* 149:204 */               "A conflicting bean definition was detected while processing @ComponentScan annotations. This usually indicates a circle between scanned packages.", 
/* 150:205 */               ex);
/* 151:    */           }
/* 152:    */         }
/* 153:    */       }
/* 154:    */     }
/* 155:212 */     Object allImportAttribs = 
/* 156:213 */       AnnotationUtils.findAllAnnotationAttributes(Import.class, metadata.getClassName(), true, this.metadataReaderFactory);
/* 157:214 */     for (Object importAttribs : (List)allImportAttribs) {
/* 158:215 */       processImport(configClass, (String[])((Map)importAttribs).get("value"), true);
/* 159:    */     }
/* 160:    */     String resource;
/* 161:219 */     if (metadata.isAnnotated(ImportResource.class.getName()))
/* 162:    */     {
/* 163:220 */       String[] resources = (String[])metadata.getAnnotationAttributes(ImportResource.class.getName()).get("value");
/* 164:221 */       Class<?> readerClass = (Class)metadata.getAnnotationAttributes(ImportResource.class.getName()).get("reader");
/* 165:222 */       if (readerClass == null) {
/* 166:223 */         throw new IllegalStateException("No reader class associated with imported resources: " + 
/* 167:224 */           StringUtils.arrayToCommaDelimitedString(resources));
/* 168:    */       }
/* 169:226 */       for (resource : resources) {
/* 170:227 */         configClass.addImportedResource(resource, readerClass);
/* 171:    */       }
/* 172:    */     }
/* 173:232 */     Object beanMethods = metadata.getAnnotatedMethods(Bean.class.getName());
/* 174:233 */     for (MethodMetadata methodMetadata : (Set)beanMethods) {
/* 175:234 */       configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
/* 176:    */     }
/* 177:    */   }
/* 178:    */   
/* 179:    */   private void processImport(ConfigurationClass configClass, String[] classesToImport, boolean checkForCircularImports)
/* 180:    */     throws IOException
/* 181:    */   {
/* 182:239 */     if ((checkForCircularImports) && (this.importStack.contains(configClass)))
/* 183:    */     {
/* 184:240 */       this.problemReporter.error(new CircularImportProblem(configClass, this.importStack, configClass.getMetadata()));
/* 185:    */     }
/* 186:    */     else
/* 187:    */     {
/* 188:243 */       this.importStack.push(configClass);
/* 189:244 */       AnnotationMetadata importingClassMetadata = configClass.getMetadata();
/* 190:245 */       for (String candidate : classesToImport)
/* 191:    */       {
/* 192:246 */         MetadataReader reader = this.metadataReaderFactory.getMetadataReader(candidate);
/* 193:247 */         if (new AssignableTypeFilter(ImportSelector.class).match(reader, this.metadataReaderFactory))
/* 194:    */         {
/* 195:    */           try
/* 196:    */           {
/* 197:250 */             ImportSelector selector = (ImportSelector)BeanUtils.instantiateClass(Class.forName(candidate), ImportSelector.class);
/* 198:251 */             ImportSelectorContext context = new ImportSelectorContext(importingClassMetadata, this.registry);
/* 199:252 */             processImport(configClass, selector.selectImports(context), false);
/* 200:    */           }
/* 201:    */           catch (ClassNotFoundException ex)
/* 202:    */           {
/* 203:254 */             throw new IllegalStateException(ex);
/* 204:    */           }
/* 205:    */         }
/* 206:    */         else
/* 207:    */         {
/* 208:259 */           this.importStack.registerImport(importingClassMetadata.getClassName(), candidate);
/* 209:260 */           processConfigurationClass(new ConfigurationClass(reader, null));
/* 210:    */         }
/* 211:    */       }
/* 212:263 */       this.importStack.pop();
/* 213:    */     }
/* 214:    */   }
/* 215:    */   
/* 216:    */   public void validate()
/* 217:    */   {
/* 218:272 */     for (ConfigurationClass configClass : this.configurationClasses) {
/* 219:273 */       configClass.validate(this.problemReporter);
/* 220:    */     }
/* 221:    */   }
/* 222:    */   
/* 223:    */   public Set<ConfigurationClass> getConfigurationClasses()
/* 224:    */   {
/* 225:278 */     return this.configurationClasses;
/* 226:    */   }
/* 227:    */   
/* 228:    */   public Stack<org.springframework.core.env.PropertySource<?>> getPropertySources()
/* 229:    */   {
/* 230:282 */     return this.propertySources;
/* 231:    */   }
/* 232:    */   
/* 233:    */   public ImportRegistry getImportRegistry()
/* 234:    */   {
/* 235:286 */     return this.importStack;
/* 236:    */   }
/* 237:    */   
/* 238:    */   private static class ImportStack
/* 239:    */     extends Stack<ConfigurationClass>
/* 240:    */     implements ConfigurationClassParser.ImportRegistry
/* 241:    */   {
/* 242:298 */     private Map<String, String> imports = new HashMap();
/* 243:    */     
/* 244:    */     public String getImportingClassFor(String importedClass)
/* 245:    */     {
/* 246:301 */       return (String)this.imports.get(importedClass);
/* 247:    */     }
/* 248:    */     
/* 249:    */     public void registerImport(String importingClass, String importedClass)
/* 250:    */     {
/* 251:305 */       this.imports.put(importedClass, importingClass);
/* 252:    */     }
/* 253:    */     
/* 254:    */     public boolean contains(Object elem)
/* 255:    */     {
/* 256:315 */       ConfigurationClass configClass = (ConfigurationClass)elem;
/* 257:316 */       Comparator<ConfigurationClass> comparator = new Comparator()
/* 258:    */       {
/* 259:    */         public int compare(ConfigurationClass first, ConfigurationClass second)
/* 260:    */         {
/* 261:318 */           return first.getMetadata().getClassName().equals(second.getMetadata().getClassName()) ? 0 : 1;
/* 262:    */         }
/* 263:320 */       };
/* 264:321 */       return Collections.binarySearch(this, configClass, comparator) != -1;
/* 265:    */     }
/* 266:    */     
/* 267:    */     public String toString()
/* 268:    */     {
/* 269:335 */       StringBuilder builder = new StringBuilder();
/* 270:336 */       Iterator<ConfigurationClass> iterator = iterator();
/* 271:337 */       while (iterator.hasNext())
/* 272:    */       {
/* 273:338 */         builder.append(((ConfigurationClass)iterator.next()).getSimpleName());
/* 274:339 */         if (iterator.hasNext()) {
/* 275:340 */           builder.append("->");
/* 276:    */         }
/* 277:    */       }
/* 278:343 */       return builder.toString();
/* 279:    */     }
/* 280:    */   }
/* 281:    */   
/* 282:    */   static abstract interface ImportRegistry
/* 283:    */   {
/* 284:    */     public abstract String getImportingClassFor(String paramString);
/* 285:    */   }
/* 286:    */   
/* 287:    */   private static class CircularImportProblem
/* 288:    */     extends Problem
/* 289:    */   {
/* 290:    */     public CircularImportProblem(ConfigurationClass attemptedImport, Stack<ConfigurationClass> importStack, AnnotationMetadata metadata)
/* 291:    */     {
/* 292:358 */       super(new Location(((ConfigurationClass)importStack.peek()).getResource(), metadata));
/* 293:    */     }
/* 294:    */   }
/* 295:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ConfigurationClassParser
 * JD-Core Version:    0.7.0.1
 */