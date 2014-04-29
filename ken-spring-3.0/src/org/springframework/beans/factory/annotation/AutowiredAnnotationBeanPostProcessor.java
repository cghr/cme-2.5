/*   1:    */ package org.springframework.beans.factory.annotation;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyDescriptor;
/*   4:    */ import java.lang.annotation.Annotation;
/*   5:    */ import java.lang.reflect.AccessibleObject;
/*   6:    */ import java.lang.reflect.Constructor;
/*   7:    */ import java.lang.reflect.Field;
/*   8:    */ import java.lang.reflect.InvocationTargetException;
/*   9:    */ import java.lang.reflect.Method;
/*  10:    */ import java.lang.reflect.Modifier;
/*  11:    */ import java.util.ArrayList;
/*  12:    */ import java.util.Iterator;
/*  13:    */ import java.util.LinkedHashSet;
/*  14:    */ import java.util.LinkedList;
/*  15:    */ import java.util.List;
/*  16:    */ import java.util.Map;
/*  17:    */ import java.util.Set;
/*  18:    */ import java.util.concurrent.ConcurrentHashMap;
/*  19:    */ import org.apache.commons.logging.Log;
/*  20:    */ import org.apache.commons.logging.LogFactory;
/*  21:    */ import org.springframework.beans.BeanUtils;
/*  22:    */ import org.springframework.beans.BeansException;
/*  23:    */ import org.springframework.beans.PropertyValues;
/*  24:    */ import org.springframework.beans.TypeConverter;
/*  25:    */ import org.springframework.beans.factory.BeanCreationException;
/*  26:    */ import org.springframework.beans.factory.BeanFactory;
/*  27:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*  28:    */ import org.springframework.beans.factory.BeanFactoryUtils;
/*  29:    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*  30:    */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*  31:    */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
/*  32:    */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*  33:    */ import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
/*  34:    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  35:    */ import org.springframework.core.BridgeMethodResolver;
/*  36:    */ import org.springframework.core.GenericTypeResolver;
/*  37:    */ import org.springframework.core.MethodParameter;
/*  38:    */ import org.springframework.core.PriorityOrdered;
/*  39:    */ import org.springframework.util.Assert;
/*  40:    */ import org.springframework.util.ClassUtils;
/*  41:    */ import org.springframework.util.ReflectionUtils;
/*  42:    */ 
/*  43:    */ public class AutowiredAnnotationBeanPostProcessor
/*  44:    */   extends InstantiationAwareBeanPostProcessorAdapter
/*  45:    */   implements MergedBeanDefinitionPostProcessor, PriorityOrdered, BeanFactoryAware
/*  46:    */ {
/*  47:108 */   protected final Log logger = LogFactory.getLog(getClass());
/*  48:111 */   private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet();
/*  49:113 */   private String requiredParameterName = "required";
/*  50:115 */   private boolean requiredParameterValue = true;
/*  51:117 */   private int order = 2147483645;
/*  52:    */   private ConfigurableListableBeanFactory beanFactory;
/*  53:122 */   private final Map<Class<?>, Constructor<?>[]> candidateConstructorsCache = new ConcurrentHashMap();
/*  54:125 */   private final Map<Class<?>, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap();
/*  55:    */   
/*  56:    */   public AutowiredAnnotationBeanPostProcessor()
/*  57:    */   {
/*  58:135 */     this.autowiredAnnotationTypes.add(Autowired.class);
/*  59:136 */     this.autowiredAnnotationTypes.add(Value.class);
/*  60:137 */     ClassLoader cl = AutowiredAnnotationBeanPostProcessor.class.getClassLoader();
/*  61:    */     try
/*  62:    */     {
/*  63:139 */       this.autowiredAnnotationTypes.add(cl.loadClass("javax.inject.Inject"));
/*  64:140 */       this.logger.info("JSR-330 'javax.inject.Inject' annotation found and supported for autowiring");
/*  65:    */     }
/*  66:    */     catch (ClassNotFoundException localClassNotFoundException) {}
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setAutowiredAnnotationType(Class<? extends Annotation> autowiredAnnotationType)
/*  70:    */   {
/*  71:158 */     Assert.notNull(autowiredAnnotationType, "'autowiredAnnotationType' must not be null");
/*  72:159 */     this.autowiredAnnotationTypes.clear();
/*  73:160 */     this.autowiredAnnotationTypes.add(autowiredAnnotationType);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void setAutowiredAnnotationTypes(Set<Class<? extends Annotation>> autowiredAnnotationTypes)
/*  77:    */   {
/*  78:173 */     Assert.notEmpty(autowiredAnnotationTypes, "'autowiredAnnotationTypes' must not be empty");
/*  79:174 */     this.autowiredAnnotationTypes.clear();
/*  80:175 */     this.autowiredAnnotationTypes.addAll(autowiredAnnotationTypes);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void setRequiredParameterName(String requiredParameterName)
/*  84:    */   {
/*  85:184 */     this.requiredParameterName = requiredParameterName;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void setRequiredParameterValue(boolean requiredParameterValue)
/*  89:    */   {
/*  90:195 */     this.requiredParameterValue = requiredParameterValue;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void setOrder(int order)
/*  94:    */   {
/*  95:199 */     this.order = order;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public int getOrder()
/*  99:    */   {
/* 100:203 */     return this.order;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void setBeanFactory(BeanFactory beanFactory)
/* 104:    */     throws BeansException
/* 105:    */   {
/* 106:207 */     if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
/* 107:208 */       throw new IllegalArgumentException(
/* 108:209 */         "AutowiredAnnotationBeanPostProcessor requires a ConfigurableListableBeanFactory");
/* 109:    */     }
/* 110:211 */     this.beanFactory = ((ConfigurableListableBeanFactory)beanFactory);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName)
/* 114:    */   {
/* 115:216 */     if (beanType != null)
/* 116:    */     {
/* 117:217 */       InjectionMetadata metadata = findAutowiringMetadata(beanType);
/* 118:218 */       metadata.checkConfigMembers(beanDefinition);
/* 119:    */     }
/* 120:    */   }
/* 121:    */   
/* 122:    */   public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName)
/* 123:    */     throws BeansException
/* 124:    */   {
/* 125:225 */     Constructor[] candidateConstructors = (Constructor[])this.candidateConstructorsCache.get(beanClass);
/* 126:226 */     if (candidateConstructors == null) {
/* 127:227 */       synchronized (this.candidateConstructorsCache)
/* 128:    */       {
/* 129:228 */         candidateConstructors = (Constructor[])this.candidateConstructorsCache.get(beanClass);
/* 130:229 */         if (candidateConstructors == null)
/* 131:    */         {
/* 132:230 */           Constructor[] rawCandidates = beanClass.getDeclaredConstructors();
/* 133:231 */           List<Constructor<?>> candidates = new ArrayList(rawCandidates.length);
/* 134:232 */           Constructor<?> requiredConstructor = null;
/* 135:233 */           Constructor<?> defaultConstructor = null;
/* 136:234 */           for (Constructor<?> candidate : rawCandidates)
/* 137:    */           {
/* 138:235 */             Annotation annotation = findAutowiredAnnotation(candidate);
/* 139:236 */             if (annotation != null)
/* 140:    */             {
/* 141:237 */               if (requiredConstructor != null) {
/* 142:238 */                 throw new BeanCreationException("Invalid autowire-marked constructor: " + candidate + 
/* 143:239 */                   ". Found another constructor with 'required' Autowired annotation: " + 
/* 144:240 */                   requiredConstructor);
/* 145:    */               }
/* 146:242 */               if (candidate.getParameterTypes().length == 0) {
/* 147:243 */                 throw new IllegalStateException(
/* 148:244 */                   "Autowired annotation requires at least one argument: " + candidate);
/* 149:    */               }
/* 150:246 */               boolean required = determineRequiredStatus(annotation);
/* 151:247 */               if (required)
/* 152:    */               {
/* 153:248 */                 if (!candidates.isEmpty()) {
/* 154:249 */                   throw new BeanCreationException(
/* 155:250 */                     "Invalid autowire-marked constructors: " + candidates + 
/* 156:251 */                     ". Found another constructor with 'required' Autowired annotation: " + 
/* 157:252 */                     requiredConstructor);
/* 158:    */                 }
/* 159:254 */                 requiredConstructor = candidate;
/* 160:    */               }
/* 161:256 */               candidates.add(candidate);
/* 162:    */             }
/* 163:258 */             else if (candidate.getParameterTypes().length == 0)
/* 164:    */             {
/* 165:259 */               defaultConstructor = candidate;
/* 166:    */             }
/* 167:    */           }
/* 168:262 */           if (!candidates.isEmpty())
/* 169:    */           {
/* 170:264 */             if ((requiredConstructor == null) && (defaultConstructor != null)) {
/* 171:265 */               candidates.add(defaultConstructor);
/* 172:    */             }
/* 173:267 */             candidateConstructors = (Constructor[])candidates.toArray(new Constructor[candidates.size()]);
/* 174:    */           }
/* 175:    */           else
/* 176:    */           {
/* 177:270 */             candidateConstructors = new Constructor[0];
/* 178:    */           }
/* 179:272 */           this.candidateConstructorsCache.put(beanClass, candidateConstructors);
/* 180:    */         }
/* 181:    */       }
/* 182:    */     }
/* 183:276 */     return candidateConstructors.length > 0 ? candidateConstructors : null;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName)
/* 187:    */     throws BeansException
/* 188:    */   {
/* 189:283 */     InjectionMetadata metadata = findAutowiringMetadata(bean.getClass());
/* 190:    */     try
/* 191:    */     {
/* 192:285 */       metadata.inject(bean, beanName, pvs);
/* 193:    */     }
/* 194:    */     catch (Throwable ex)
/* 195:    */     {
/* 196:288 */       throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);
/* 197:    */     }
/* 198:290 */     return pvs;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public void processInjection(Object bean)
/* 202:    */     throws BeansException
/* 203:    */   {
/* 204:300 */     Class<?> clazz = bean.getClass();
/* 205:301 */     InjectionMetadata metadata = findAutowiringMetadata(clazz);
/* 206:    */     try
/* 207:    */     {
/* 208:303 */       metadata.inject(bean, null, null);
/* 209:    */     }
/* 210:    */     catch (Throwable ex)
/* 211:    */     {
/* 212:306 */       throw new BeanCreationException("Injection of autowired dependencies failed for class [" + clazz + "]", ex);
/* 213:    */     }
/* 214:    */   }
/* 215:    */   
/* 216:    */   private InjectionMetadata findAutowiringMetadata(Class<?> clazz)
/* 217:    */   {
/* 218:313 */     InjectionMetadata metadata = (InjectionMetadata)this.injectionMetadataCache.get(clazz);
/* 219:314 */     if (metadata == null) {
/* 220:315 */       synchronized (this.injectionMetadataCache)
/* 221:    */       {
/* 222:316 */         metadata = (InjectionMetadata)this.injectionMetadataCache.get(clazz);
/* 223:317 */         if (metadata == null)
/* 224:    */         {
/* 225:318 */           metadata = buildAutowiringMetadata(clazz);
/* 226:319 */           this.injectionMetadataCache.put(clazz, metadata);
/* 227:    */         }
/* 228:    */       }
/* 229:    */     }
/* 230:323 */     return metadata;
/* 231:    */   }
/* 232:    */   
/* 233:    */   private InjectionMetadata buildAutowiringMetadata(Class<?> clazz)
/* 234:    */   {
/* 235:327 */     LinkedList<InjectionMetadata.InjectedElement> elements = new LinkedList();
/* 236:328 */     Class<?> targetClass = clazz;
/* 237:    */     do
/* 238:    */     {
/* 239:331 */       LinkedList<InjectionMetadata.InjectedElement> currElements = new LinkedList();
/* 240:332 */       for (Field field : targetClass.getDeclaredFields())
/* 241:    */       {
/* 242:333 */         Annotation annotation = findAutowiredAnnotation(field);
/* 243:334 */         if (annotation != null) {
/* 244:335 */           if (Modifier.isStatic(field.getModifiers()))
/* 245:    */           {
/* 246:336 */             if (this.logger.isWarnEnabled()) {
/* 247:337 */               this.logger.warn("Autowired annotation is not supported on static fields: " + field);
/* 248:    */             }
/* 249:    */           }
/* 250:    */           else
/* 251:    */           {
/* 252:341 */             boolean required = determineRequiredStatus(annotation);
/* 253:342 */             currElements.add(new AutowiredFieldElement(field, required));
/* 254:    */           }
/* 255:    */         }
/* 256:    */       }
/* 257:345 */       for (Method method : targetClass.getDeclaredMethods())
/* 258:    */       {
/* 259:346 */         Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
/* 260:347 */         Annotation annotation = BridgeMethodResolver.isJava6VisibilityBridgeMethodPair(method, bridgedMethod) ? 
/* 261:348 */           findAutowiredAnnotation(bridgedMethod) : 
/* 262:349 */           findAutowiredAnnotation(method);
/* 263:350 */         if ((annotation != null) && (method.equals(ClassUtils.getMostSpecificMethod(method, clazz)))) {
/* 264:351 */           if (Modifier.isStatic(method.getModifiers()))
/* 265:    */           {
/* 266:352 */             if (this.logger.isWarnEnabled()) {
/* 267:353 */               this.logger.warn("Autowired annotation is not supported on static methods: " + method);
/* 268:    */             }
/* 269:    */           }
/* 270:    */           else
/* 271:    */           {
/* 272:357 */             if ((method.getParameterTypes().length == 0) && 
/* 273:358 */               (this.logger.isWarnEnabled())) {
/* 274:359 */               this.logger.warn("Autowired annotation should be used on methods with actual parameters: " + method);
/* 275:    */             }
/* 276:362 */             boolean required = determineRequiredStatus(annotation);
/* 277:363 */             PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
/* 278:364 */             currElements.add(new AutowiredMethodElement(method, required, pd));
/* 279:    */           }
/* 280:    */         }
/* 281:    */       }
/* 282:367 */       elements.addAll(0, currElements);
/* 283:368 */       targetClass = targetClass.getSuperclass();
/* 284:370 */     } while ((targetClass != null) && (targetClass != Object.class));
/* 285:372 */     return new InjectionMetadata(clazz, elements);
/* 286:    */   }
/* 287:    */   
/* 288:    */   private Annotation findAutowiredAnnotation(AccessibleObject ao)
/* 289:    */   {
/* 290:376 */     for (Class<? extends Annotation> type : this.autowiredAnnotationTypes)
/* 291:    */     {
/* 292:377 */       Annotation annotation = ao.getAnnotation(type);
/* 293:378 */       if (annotation != null) {
/* 294:379 */         return annotation;
/* 295:    */       }
/* 296:    */     }
/* 297:382 */     return null;
/* 298:    */   }
/* 299:    */   
/* 300:    */   protected <T> Map<String, T> findAutowireCandidates(Class<T> type)
/* 301:    */     throws BeansException
/* 302:    */   {
/* 303:392 */     if (this.beanFactory == null) {
/* 304:393 */       throw new IllegalStateException("No BeanFactory configured - override the getBeanOfType method or specify the 'beanFactory' property");
/* 305:    */     }
/* 306:396 */     return BeanFactoryUtils.beansOfTypeIncludingAncestors(this.beanFactory, type);
/* 307:    */   }
/* 308:    */   
/* 309:    */   protected boolean determineRequiredStatus(Annotation annotation)
/* 310:    */   {
/* 311:    */     try
/* 312:    */     {
/* 313:409 */       Method method = ReflectionUtils.findMethod(annotation.annotationType(), this.requiredParameterName);
/* 314:410 */       return this.requiredParameterValue == ((Boolean)ReflectionUtils.invokeMethod(method, annotation)).booleanValue();
/* 315:    */     }
/* 316:    */     catch (Exception localException) {}
/* 317:414 */     return true;
/* 318:    */   }
/* 319:    */   
/* 320:    */   private void registerDependentBeans(String beanName, Set<String> autowiredBeanNames)
/* 321:    */   {
/* 322:422 */     if (beanName != null) {
/* 323:423 */       for (String autowiredBeanName : autowiredBeanNames)
/* 324:    */       {
/* 325:424 */         this.beanFactory.registerDependentBean(autowiredBeanName, beanName);
/* 326:425 */         if (this.logger.isDebugEnabled()) {
/* 327:426 */           this.logger.debug(
/* 328:427 */             "Autowiring by type from bean name '" + beanName + "' to bean named '" + autowiredBeanName + 
/* 329:428 */             "'");
/* 330:    */         }
/* 331:    */       }
/* 332:    */     }
/* 333:    */   }
/* 334:    */   
/* 335:    */   private Object resolvedCachedArgument(String beanName, Object cachedArgument)
/* 336:    */   {
/* 337:438 */     if ((cachedArgument instanceof DependencyDescriptor))
/* 338:    */     {
/* 339:439 */       DependencyDescriptor descriptor = (DependencyDescriptor)cachedArgument;
/* 340:440 */       TypeConverter typeConverter = this.beanFactory.getTypeConverter();
/* 341:441 */       return this.beanFactory.resolveDependency(descriptor, beanName, null, typeConverter);
/* 342:    */     }
/* 343:443 */     if ((cachedArgument instanceof RuntimeBeanReference)) {
/* 344:444 */       return this.beanFactory.getBean(((RuntimeBeanReference)cachedArgument).getBeanName());
/* 345:    */     }
/* 346:447 */     return cachedArgument;
/* 347:    */   }
/* 348:    */   
/* 349:    */   private class AutowiredFieldElement
/* 350:    */     extends InjectionMetadata.InjectedElement
/* 351:    */   {
/* 352:    */     private final boolean required;
/* 353:459 */     private volatile boolean cached = false;
/* 354:    */     private volatile Object cachedFieldValue;
/* 355:    */     
/* 356:    */     public AutowiredFieldElement(Field field, boolean required)
/* 357:    */     {
/* 358:464 */       super(null);
/* 359:465 */       this.required = required;
/* 360:    */     }
/* 361:    */     
/* 362:    */     protected void inject(Object bean, String beanName, PropertyValues pvs)
/* 363:    */       throws Throwable
/* 364:    */     {
/* 365:470 */       Field field = (Field)this.member;
/* 366:    */       try
/* 367:    */       {
/* 368:    */         Object value;
/* 369:    */         Object value;
/* 370:473 */         if (this.cached)
/* 371:    */         {
/* 372:474 */           value = AutowiredAnnotationBeanPostProcessor.this.resolvedCachedArgument(beanName, this.cachedFieldValue);
/* 373:    */         }
/* 374:    */         else
/* 375:    */         {
/* 376:477 */           DependencyDescriptor descriptor = new DependencyDescriptor(field, this.required);
/* 377:478 */           Set<String> autowiredBeanNames = new LinkedHashSet(1);
/* 378:479 */           TypeConverter typeConverter = AutowiredAnnotationBeanPostProcessor.this.beanFactory.getTypeConverter();
/* 379:480 */           value = AutowiredAnnotationBeanPostProcessor.this.beanFactory.resolveDependency(descriptor, beanName, autowiredBeanNames, typeConverter);
/* 380:481 */           synchronized (this)
/* 381:    */           {
/* 382:482 */             if (!this.cached)
/* 383:    */             {
/* 384:483 */               if ((value != null) || (this.required))
/* 385:    */               {
/* 386:484 */                 this.cachedFieldValue = descriptor;
/* 387:485 */                 AutowiredAnnotationBeanPostProcessor.this.registerDependentBeans(beanName, autowiredBeanNames);
/* 388:486 */                 if (autowiredBeanNames.size() == 1)
/* 389:    */                 {
/* 390:487 */                   String autowiredBeanName = (String)autowiredBeanNames.iterator().next();
/* 391:488 */                   if ((AutowiredAnnotationBeanPostProcessor.this.beanFactory.containsBean(autowiredBeanName)) && 
/* 392:489 */                     (AutowiredAnnotationBeanPostProcessor.this.beanFactory.isTypeMatch(autowiredBeanName, field.getType()))) {
/* 393:490 */                     this.cachedFieldValue = new RuntimeBeanReference(autowiredBeanName);
/* 394:    */                   }
/* 395:    */                 }
/* 396:    */               }
/* 397:    */               else
/* 398:    */               {
/* 399:496 */                 this.cachedFieldValue = null;
/* 400:    */               }
/* 401:498 */               this.cached = true;
/* 402:    */             }
/* 403:    */           }
/* 404:    */         }
/* 405:502 */         if (value != null)
/* 406:    */         {
/* 407:503 */           ReflectionUtils.makeAccessible(field);
/* 408:504 */           field.set(bean, value);
/* 409:    */         }
/* 410:    */       }
/* 411:    */       catch (Throwable ex)
/* 412:    */       {
/* 413:508 */         throw new BeanCreationException("Could not autowire field: " + field, ex);
/* 414:    */       }
/* 415:    */     }
/* 416:    */   }
/* 417:    */   
/* 418:    */   private class AutowiredMethodElement
/* 419:    */     extends InjectionMetadata.InjectedElement
/* 420:    */   {
/* 421:    */     private final boolean required;
/* 422:521 */     private volatile boolean cached = false;
/* 423:    */     private volatile Object[] cachedMethodArguments;
/* 424:    */     
/* 425:    */     public AutowiredMethodElement(Method method, boolean required, PropertyDescriptor pd)
/* 426:    */     {
/* 427:526 */       super(pd);
/* 428:527 */       this.required = required;
/* 429:    */     }
/* 430:    */     
/* 431:    */     protected void inject(Object bean, String beanName, PropertyValues pvs)
/* 432:    */       throws Throwable
/* 433:    */     {
/* 434:532 */       if (checkPropertySkipping(pvs)) {
/* 435:533 */         return;
/* 436:    */       }
/* 437:535 */       Method method = (Method)this.member;
/* 438:    */       try
/* 439:    */       {
/* 440:    */         Object[] arguments;
/* 441:    */         Object[] arguments;
/* 442:538 */         if (this.cached)
/* 443:    */         {
/* 444:540 */           arguments = resolveCachedArguments(beanName);
/* 445:    */         }
/* 446:    */         else
/* 447:    */         {
/* 448:543 */           Class[] paramTypes = method.getParameterTypes();
/* 449:544 */           arguments = new Object[paramTypes.length];
/* 450:545 */           DependencyDescriptor[] descriptors = new DependencyDescriptor[paramTypes.length];
/* 451:546 */           Set<String> autowiredBeanNames = new LinkedHashSet(paramTypes.length);
/* 452:547 */           TypeConverter typeConverter = AutowiredAnnotationBeanPostProcessor.this.beanFactory.getTypeConverter();
/* 453:548 */           for (int i = 0; i < arguments.length; i++)
/* 454:    */           {
/* 455:549 */             MethodParameter methodParam = new MethodParameter(method, i);
/* 456:550 */             GenericTypeResolver.resolveParameterType(methodParam, bean.getClass());
/* 457:551 */             descriptors[i] = new DependencyDescriptor(methodParam, this.required);
/* 458:552 */             arguments[i] = AutowiredAnnotationBeanPostProcessor.this.beanFactory
/* 459:553 */               .resolveDependency(descriptors[i], beanName, autowiredBeanNames, typeConverter);
/* 460:554 */             if ((arguments[i] == null) && (!this.required))
/* 461:    */             {
/* 462:555 */               arguments = (Object[])null;
/* 463:556 */               break;
/* 464:    */             }
/* 465:    */           }
/* 466:559 */           synchronized (this)
/* 467:    */           {
/* 468:560 */             if (!this.cached)
/* 469:    */             {
/* 470:561 */               if (arguments != null)
/* 471:    */               {
/* 472:562 */                 this.cachedMethodArguments = new Object[arguments.length];
/* 473:563 */                 for (int i = 0; i < arguments.length; i++) {
/* 474:564 */                   this.cachedMethodArguments[i] = descriptors[i];
/* 475:    */                 }
/* 476:566 */                 AutowiredAnnotationBeanPostProcessor.this.registerDependentBeans(beanName, autowiredBeanNames);
/* 477:567 */                 if (autowiredBeanNames.size() == paramTypes.length)
/* 478:    */                 {
/* 479:568 */                   Iterator<String> it = autowiredBeanNames.iterator();
/* 480:569 */                   for (int i = 0; i < paramTypes.length; i++)
/* 481:    */                   {
/* 482:570 */                     String autowiredBeanName = (String)it.next();
/* 483:571 */                     if ((AutowiredAnnotationBeanPostProcessor.this.beanFactory.containsBean(autowiredBeanName)) && 
/* 484:572 */                       (AutowiredAnnotationBeanPostProcessor.this.beanFactory.isTypeMatch(autowiredBeanName, paramTypes[i]))) {
/* 485:573 */                       this.cachedMethodArguments[i] = new RuntimeBeanReference(autowiredBeanName);
/* 486:    */                     }
/* 487:    */                   }
/* 488:    */                 }
/* 489:    */               }
/* 490:    */               else
/* 491:    */               {
/* 492:580 */                 this.cachedMethodArguments = null;
/* 493:    */               }
/* 494:582 */               this.cached = true;
/* 495:    */             }
/* 496:    */           }
/* 497:    */         }
/* 498:586 */         if (arguments != null)
/* 499:    */         {
/* 500:587 */           ReflectionUtils.makeAccessible(method);
/* 501:588 */           method.invoke(bean, arguments);
/* 502:    */         }
/* 503:    */       }
/* 504:    */       catch (InvocationTargetException ex)
/* 505:    */       {
/* 506:592 */         throw ex.getTargetException();
/* 507:    */       }
/* 508:    */       catch (Throwable ex)
/* 509:    */       {
/* 510:595 */         throw new BeanCreationException("Could not autowire method: " + method, ex);
/* 511:    */       }
/* 512:    */     }
/* 513:    */     
/* 514:    */     private Object[] resolveCachedArguments(String beanName)
/* 515:    */     {
/* 516:600 */       if (this.cachedMethodArguments == null) {
/* 517:601 */         return null;
/* 518:    */       }
/* 519:603 */       Object[] arguments = new Object[this.cachedMethodArguments.length];
/* 520:604 */       for (int i = 0; i < arguments.length; i++) {
/* 521:605 */         arguments[i] = AutowiredAnnotationBeanPostProcessor.this.resolvedCachedArgument(beanName, this.cachedMethodArguments[i]);
/* 522:    */       }
/* 523:607 */       return arguments;
/* 524:    */     }
/* 525:    */   }
/* 526:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
 * JD-Core Version:    0.7.0.1
 */