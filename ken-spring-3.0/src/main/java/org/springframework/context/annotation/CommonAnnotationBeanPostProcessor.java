/*   1:    */ package org.springframework.context.annotation;
/*   2:    */ 
/*   3:    */ import java.beans.Introspector;
/*   4:    */ import java.beans.PropertyDescriptor;
/*   5:    */ import java.io.Serializable;
/*   6:    */ import java.lang.annotation.Annotation;
/*   7:    */ import java.lang.reflect.AnnotatedElement;
/*   8:    */ import java.lang.reflect.Constructor;
/*   9:    */ import java.lang.reflect.Field;
/*  10:    */ import java.lang.reflect.Member;
/*  11:    */ import java.lang.reflect.Method;
/*  12:    */ import java.lang.reflect.Modifier;
/*  13:    */ import java.net.MalformedURLException;
/*  14:    */ import java.net.URL;
/*  15:    */ import java.util.Collections;
/*  16:    */ import java.util.HashSet;
/*  17:    */ import java.util.LinkedHashSet;
/*  18:    */ import java.util.LinkedList;
/*  19:    */ import java.util.Map;
/*  20:    */ import java.util.Set;
/*  21:    */ import java.util.concurrent.ConcurrentHashMap;
/*  22:    */ import javax.annotation.PostConstruct;
/*  23:    */ import javax.annotation.PreDestroy;
/*  24:    */ import javax.annotation.Resource;
/*  25:    */ import javax.ejb.EJB;
/*  26:    */ import javax.xml.namespace.QName;
/*  27:    */ import javax.xml.ws.Service;
/*  28:    */ import javax.xml.ws.WebServiceClient;
/*  29:    */ import javax.xml.ws.WebServiceRef;
/*  30:    */ import org.springframework.beans.BeanUtils;
/*  31:    */ import org.springframework.beans.BeansException;
/*  32:    */ import org.springframework.beans.PropertyValues;
/*  33:    */ import org.springframework.beans.factory.BeanCreationException;
/*  34:    */ import org.springframework.beans.factory.BeanFactory;
/*  35:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*  36:    */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*  37:    */ import org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor;
/*  38:    */ import org.springframework.beans.factory.annotation.InjectionMetadata;
/*  39:    */ import org.springframework.beans.factory.annotation.InjectionMetadata.InjectedElement;
/*  40:    */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*  41:    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*  42:    */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*  43:    */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
/*  44:    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  45:    */ import org.springframework.core.BridgeMethodResolver;
/*  46:    */ import org.springframework.core.MethodParameter;
/*  47:    */ import org.springframework.jndi.support.SimpleJndiBeanFactory;
/*  48:    */ import org.springframework.util.Assert;
/*  49:    */ import org.springframework.util.ClassUtils;
/*  50:    */ import org.springframework.util.StringUtils;
/*  51:    */ 
/*  52:    */ public class CommonAnnotationBeanPostProcessor
/*  53:    */   extends InitDestroyAnnotationBeanPostProcessor
/*  54:    */   implements InstantiationAwareBeanPostProcessor, BeanFactoryAware, Serializable
/*  55:    */ {
/*  56:143 */   private static Class<? extends Annotation> webServiceRefClass = null;
/*  57:145 */   private static Class<? extends Annotation> ejbRefClass = null;
/*  58:    */   
/*  59:    */   static
/*  60:    */   {
/*  61:148 */     ClassLoader cl = CommonAnnotationBeanPostProcessor.class.getClassLoader();
/*  62:    */     try
/*  63:    */     {
/*  64:151 */       Class<? extends Annotation> clazz = cl.loadClass("javax.xml.ws.WebServiceRef");
/*  65:152 */       webServiceRefClass = clazz;
/*  66:    */     }
/*  67:    */     catch (ClassNotFoundException localClassNotFoundException1)
/*  68:    */     {
/*  69:155 */       webServiceRefClass = null;
/*  70:    */     }
/*  71:    */     try
/*  72:    */     {
/*  73:159 */       Class<? extends Annotation> clazz = cl.loadClass("javax.ejb.EJB");
/*  74:160 */       ejbRefClass = clazz;
/*  75:    */     }
/*  76:    */     catch (ClassNotFoundException localClassNotFoundException2)
/*  77:    */     {
/*  78:163 */       ejbRefClass = null;
/*  79:    */     }
/*  80:    */   }
/*  81:    */   
/*  82:168 */   private final Set<String> ignoredResourceTypes = new HashSet(1);
/*  83:170 */   private boolean fallbackToDefaultTypeMatch = true;
/*  84:172 */   private boolean alwaysUseJndiLookup = false;
/*  85:174 */   private transient BeanFactory jndiFactory = new SimpleJndiBeanFactory();
/*  86:    */   private transient BeanFactory resourceFactory;
/*  87:    */   private transient BeanFactory beanFactory;
/*  88:181 */   private final transient Map<Class<?>, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap();
/*  89:    */   
/*  90:    */   public CommonAnnotationBeanPostProcessor()
/*  91:    */   {
/*  92:191 */     setOrder(2147483644);
/*  93:192 */     setInitAnnotationType(PostConstruct.class);
/*  94:193 */     setDestroyAnnotationType(PreDestroy.class);
/*  95:194 */     ignoreResourceType("javax.xml.ws.WebServiceContext");
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void ignoreResourceType(String resourceType)
/*  99:    */   {
/* 100:206 */     Assert.notNull(resourceType, "Ignored resource type must not be null");
/* 101:207 */     this.ignoredResourceTypes.add(resourceType);
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setFallbackToDefaultTypeMatch(boolean fallbackToDefaultTypeMatch)
/* 105:    */   {
/* 106:221 */     this.fallbackToDefaultTypeMatch = fallbackToDefaultTypeMatch;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void setAlwaysUseJndiLookup(boolean alwaysUseJndiLookup)
/* 110:    */   {
/* 111:235 */     this.alwaysUseJndiLookup = alwaysUseJndiLookup;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void setJndiFactory(BeanFactory jndiFactory)
/* 115:    */   {
/* 116:250 */     Assert.notNull(jndiFactory, "BeanFactory must not be null");
/* 117:251 */     this.jndiFactory = jndiFactory;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void setResourceFactory(BeanFactory resourceFactory)
/* 121:    */   {
/* 122:268 */     Assert.notNull(resourceFactory, "BeanFactory must not be null");
/* 123:269 */     this.resourceFactory = resourceFactory;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public void setBeanFactory(BeanFactory beanFactory)
/* 127:    */     throws BeansException
/* 128:    */   {
/* 129:273 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/* 130:274 */     this.beanFactory = beanFactory;
/* 131:275 */     if (this.resourceFactory == null) {
/* 132:276 */       this.resourceFactory = beanFactory;
/* 133:    */     }
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName)
/* 137:    */   {
/* 138:283 */     super.postProcessMergedBeanDefinition(beanDefinition, beanType, beanName);
/* 139:284 */     if (beanType != null)
/* 140:    */     {
/* 141:285 */       InjectionMetadata metadata = findResourceMetadata(beanType);
/* 142:286 */       metadata.checkConfigMembers(beanDefinition);
/* 143:    */     }
/* 144:    */   }
/* 145:    */   
/* 146:    */   public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName)
/* 147:    */     throws BeansException
/* 148:    */   {
/* 149:291 */     return null;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public boolean postProcessAfterInstantiation(Object bean, String beanName)
/* 153:    */     throws BeansException
/* 154:    */   {
/* 155:295 */     return true;
/* 156:    */   }
/* 157:    */   
/* 158:    */   public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName)
/* 159:    */     throws BeansException
/* 160:    */   {
/* 161:301 */     InjectionMetadata metadata = findResourceMetadata(bean.getClass());
/* 162:    */     try
/* 163:    */     {
/* 164:303 */       metadata.inject(bean, beanName, pvs);
/* 165:    */     }
/* 166:    */     catch (Throwable ex)
/* 167:    */     {
/* 168:306 */       throw new BeanCreationException(beanName, "Injection of resource dependencies failed", ex);
/* 169:    */     }
/* 170:308 */     return pvs;
/* 171:    */   }
/* 172:    */   
/* 173:    */   private InjectionMetadata findResourceMetadata(Class<?> clazz)
/* 174:    */   {
/* 175:314 */     InjectionMetadata metadata = (InjectionMetadata)this.injectionMetadataCache.get(clazz);
/* 176:315 */     if (metadata == null) {
/* 177:316 */       synchronized (this.injectionMetadataCache)
/* 178:    */       {
/* 179:317 */         metadata = (InjectionMetadata)this.injectionMetadataCache.get(clazz);
/* 180:318 */         if (metadata == null)
/* 181:    */         {
/* 182:319 */           LinkedList<InjectionMetadata.InjectedElement> elements = new LinkedList();
/* 183:320 */           Class<?> targetClass = clazz;
/* 184:    */           do
/* 185:    */           {
/* 186:323 */             LinkedList<InjectionMetadata.InjectedElement> currElements = new LinkedList();
/* 187:324 */             for (Field field : targetClass.getDeclaredFields()) {
/* 188:325 */               if ((webServiceRefClass != null) && (field.isAnnotationPresent(webServiceRefClass)))
/* 189:    */               {
/* 190:326 */                 if (Modifier.isStatic(field.getModifiers())) {
/* 191:327 */                   throw new IllegalStateException("@WebServiceRef annotation is not supported on static fields");
/* 192:    */                 }
/* 193:329 */                 currElements.add(new WebServiceRefElement(field, null));
/* 194:    */               }
/* 195:331 */               else if ((ejbRefClass != null) && (field.isAnnotationPresent(ejbRefClass)))
/* 196:    */               {
/* 197:332 */                 if (Modifier.isStatic(field.getModifiers())) {
/* 198:333 */                   throw new IllegalStateException("@EJB annotation is not supported on static fields");
/* 199:    */                 }
/* 200:335 */                 currElements.add(new EjbRefElement(field, null));
/* 201:    */               }
/* 202:337 */               else if (field.isAnnotationPresent(Resource.class))
/* 203:    */               {
/* 204:338 */                 if (Modifier.isStatic(field.getModifiers())) {
/* 205:339 */                   throw new IllegalStateException("@Resource annotation is not supported on static fields");
/* 206:    */                 }
/* 207:341 */                 if (!this.ignoredResourceTypes.contains(field.getType().getName())) {
/* 208:342 */                   currElements.add(new ResourceElement(field, null));
/* 209:    */                 }
/* 210:    */               }
/* 211:    */             }
/* 212:346 */             for (Method method : targetClass.getDeclaredMethods())
/* 213:    */             {
/* 214:347 */               method = BridgeMethodResolver.findBridgedMethod(method);
/* 215:348 */               Method mostSpecificMethod = BridgeMethodResolver.findBridgedMethod(ClassUtils.getMostSpecificMethod(method, clazz));
/* 216:349 */               if (method.equals(mostSpecificMethod)) {
/* 217:350 */                 if ((webServiceRefClass != null) && (method.isAnnotationPresent(webServiceRefClass)))
/* 218:    */                 {
/* 219:351 */                   if (Modifier.isStatic(method.getModifiers())) {
/* 220:352 */                     throw new IllegalStateException("@WebServiceRef annotation is not supported on static methods");
/* 221:    */                   }
/* 222:354 */                   if (method.getParameterTypes().length != 1) {
/* 223:355 */                     throw new IllegalStateException("@WebServiceRef annotation requires a single-arg method: " + method);
/* 224:    */                   }
/* 225:357 */                   PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
/* 226:358 */                   currElements.add(new WebServiceRefElement(method, pd));
/* 227:    */                 }
/* 228:360 */                 else if ((ejbRefClass != null) && (method.isAnnotationPresent(ejbRefClass)))
/* 229:    */                 {
/* 230:361 */                   if (Modifier.isStatic(method.getModifiers())) {
/* 231:362 */                     throw new IllegalStateException("@EJB annotation is not supported on static methods");
/* 232:    */                   }
/* 233:364 */                   if (method.getParameterTypes().length != 1) {
/* 234:365 */                     throw new IllegalStateException("@EJB annotation requires a single-arg method: " + method);
/* 235:    */                   }
/* 236:367 */                   PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
/* 237:368 */                   currElements.add(new EjbRefElement(method, pd));
/* 238:    */                 }
/* 239:370 */                 else if (method.isAnnotationPresent(Resource.class))
/* 240:    */                 {
/* 241:371 */                   if (Modifier.isStatic(method.getModifiers())) {
/* 242:372 */                     throw new IllegalStateException("@Resource annotation is not supported on static methods");
/* 243:    */                   }
/* 244:374 */                   Class[] paramTypes = method.getParameterTypes();
/* 245:375 */                   if (paramTypes.length != 1) {
/* 246:376 */                     throw new IllegalStateException("@Resource annotation requires a single-arg method: " + method);
/* 247:    */                   }
/* 248:378 */                   if (!this.ignoredResourceTypes.contains(paramTypes[0].getName()))
/* 249:    */                   {
/* 250:379 */                     PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
/* 251:380 */                     currElements.add(new ResourceElement(method, pd));
/* 252:    */                   }
/* 253:    */                 }
/* 254:    */               }
/* 255:    */             }
/* 256:385 */             elements.addAll(0, currElements);
/* 257:386 */             targetClass = targetClass.getSuperclass();
/* 258:388 */           } while ((targetClass != null) && (targetClass != Object.class));
/* 259:390 */           metadata = new InjectionMetadata(clazz, elements);
/* 260:391 */           this.injectionMetadataCache.put(clazz, metadata);
/* 261:    */         }
/* 262:    */       }
/* 263:    */     }
/* 264:395 */     return metadata;
/* 265:    */   }
/* 266:    */   
/* 267:    */   protected Object getResource(LookupElement element, String requestingBeanName)
/* 268:    */     throws BeansException
/* 269:    */   {
/* 270:406 */     if (StringUtils.hasLength(element.mappedName)) {
/* 271:407 */       return this.jndiFactory.getBean(element.mappedName, element.lookupType);
/* 272:    */     }
/* 273:409 */     if (this.alwaysUseJndiLookup) {
/* 274:410 */       return this.jndiFactory.getBean(element.name, element.lookupType);
/* 275:    */     }
/* 276:412 */     if (this.resourceFactory == null) {
/* 277:413 */       throw new NoSuchBeanDefinitionException(element.lookupType, 
/* 278:414 */         "No resource factory configured - specify the 'resourceFactory' property");
/* 279:    */     }
/* 280:416 */     return autowireResource(this.resourceFactory, element, requestingBeanName);
/* 281:    */   }
/* 282:    */   
/* 283:    */   protected Object autowireResource(BeanFactory factory, LookupElement element, String requestingBeanName)
/* 284:    */     throws BeansException
/* 285:    */   {
/* 286:433 */     String name = element.name;
/* 287:    */     Object resource;
/* 288:    */     Object resource;
/* 289:    */     Set<String> autowiredBeanNames;
/* 290:435 */     if ((this.fallbackToDefaultTypeMatch) && (element.isDefaultName) && 
/* 291:436 */       ((factory instanceof AutowireCapableBeanFactory)) && (!factory.containsBean(name)))
/* 292:    */     {
/* 293:437 */       Set<String> autowiredBeanNames = new LinkedHashSet();
/* 294:438 */       resource = ((AutowireCapableBeanFactory)factory).resolveDependency(
/* 295:439 */         element.getDependencyDescriptor(), requestingBeanName, autowiredBeanNames, null);
/* 296:    */     }
/* 297:    */     else
/* 298:    */     {
/* 299:442 */       resource = factory.getBean(name, element.lookupType);
/* 300:443 */       autowiredBeanNames = Collections.singleton(name);
/* 301:    */     }
/* 302:446 */     if ((factory instanceof ConfigurableBeanFactory))
/* 303:    */     {
/* 304:447 */       ConfigurableBeanFactory beanFactory = (ConfigurableBeanFactory)factory;
/* 305:448 */       for (String autowiredBeanName : autowiredBeanNames) {
/* 306:449 */         beanFactory.registerDependentBean(autowiredBeanName, requestingBeanName);
/* 307:    */       }
/* 308:    */     }
/* 309:453 */     return resource;
/* 310:    */   }
/* 311:    */   
/* 312:    */   protected abstract class LookupElement
/* 313:    */     extends InjectionMetadata.InjectedElement
/* 314:    */   {
/* 315:    */     protected String name;
/* 316:465 */     protected boolean isDefaultName = false;
/* 317:    */     protected Class<?> lookupType;
/* 318:    */     protected String mappedName;
/* 319:    */     
/* 320:    */     public LookupElement(Member member, PropertyDescriptor pd)
/* 321:    */     {
/* 322:472 */       super(pd);
/* 323:473 */       initAnnotation((AnnotatedElement)member);
/* 324:    */     }
/* 325:    */     
/* 326:    */     protected abstract void initAnnotation(AnnotatedElement paramAnnotatedElement);
/* 327:    */     
/* 328:    */     public final String getName()
/* 329:    */     {
/* 330:482 */       return this.name;
/* 331:    */     }
/* 332:    */     
/* 333:    */     public final Class<?> getLookupType()
/* 334:    */     {
/* 335:489 */       return this.lookupType;
/* 336:    */     }
/* 337:    */     
/* 338:    */     public final DependencyDescriptor getDependencyDescriptor()
/* 339:    */     {
/* 340:496 */       if (this.isField) {
/* 341:497 */         return new CommonAnnotationBeanPostProcessor.LookupDependencyDescriptor((Field)this.member, this.lookupType);
/* 342:    */       }
/* 343:500 */       return new CommonAnnotationBeanPostProcessor.LookupDependencyDescriptor((Method)this.member, this.lookupType);
/* 344:    */     }
/* 345:    */   }
/* 346:    */   
/* 347:    */   private class ResourceElement
/* 348:    */     extends CommonAnnotationBeanPostProcessor.LookupElement
/* 349:    */   {
/* 350:513 */     protected boolean shareable = true;
/* 351:    */     
/* 352:    */     public ResourceElement(Member member, PropertyDescriptor pd)
/* 353:    */     {
/* 354:516 */       super(member, pd);
/* 355:    */     }
/* 356:    */     
/* 357:    */     protected void initAnnotation(AnnotatedElement ae)
/* 358:    */     {
/* 359:521 */       Resource resource = (Resource)ae.getAnnotation(Resource.class);
/* 360:522 */       String resourceName = resource.name();
/* 361:523 */       Class<?> resourceType = resource.type();
/* 362:524 */       this.isDefaultName = (!StringUtils.hasLength(resourceName));
/* 363:525 */       if (this.isDefaultName)
/* 364:    */       {
/* 365:526 */         resourceName = this.member.getName();
/* 366:527 */         if (((this.member instanceof Method)) && (resourceName.startsWith("set")) && (resourceName.length() > 3)) {
/* 367:528 */           resourceName = Introspector.decapitalize(resourceName.substring(3));
/* 368:    */         }
/* 369:    */       }
/* 370:531 */       else if ((CommonAnnotationBeanPostProcessor.this.beanFactory instanceof ConfigurableBeanFactory))
/* 371:    */       {
/* 372:532 */         resourceName = ((ConfigurableBeanFactory)CommonAnnotationBeanPostProcessor.this.beanFactory).resolveEmbeddedValue(resourceName);
/* 373:    */       }
/* 374:534 */       if ((resourceType != null) && (!Object.class.equals(resourceType))) {
/* 375:535 */         checkResourceType(resourceType);
/* 376:    */       } else {
/* 377:539 */         resourceType = getResourceType();
/* 378:    */       }
/* 379:541 */       this.name = resourceName;
/* 380:542 */       this.lookupType = resourceType;
/* 381:543 */       this.mappedName = resource.mappedName();
/* 382:544 */       this.shareable = resource.shareable();
/* 383:    */     }
/* 384:    */     
/* 385:    */     protected Object getResourceToInject(Object target, String requestingBeanName)
/* 386:    */     {
/* 387:549 */       return CommonAnnotationBeanPostProcessor.this.getResource(this, requestingBeanName);
/* 388:    */     }
/* 389:    */   }
/* 390:    */   
/* 391:    */   private class WebServiceRefElement
/* 392:    */     extends CommonAnnotationBeanPostProcessor.LookupElement
/* 393:    */   {
/* 394:    */     private Class<?> elementType;
/* 395:    */     private String wsdlLocation;
/* 396:    */     
/* 397:    */     public WebServiceRefElement(Member member, PropertyDescriptor pd)
/* 398:    */     {
/* 399:565 */       super(member, pd);
/* 400:    */     }
/* 401:    */     
/* 402:    */     protected void initAnnotation(AnnotatedElement ae)
/* 403:    */     {
/* 404:570 */       WebServiceRef resource = (WebServiceRef)ae.getAnnotation(WebServiceRef.class);
/* 405:571 */       String resourceName = resource.name();
/* 406:572 */       Class<?> resourceType = resource.type();
/* 407:573 */       this.isDefaultName = (!StringUtils.hasLength(resourceName));
/* 408:574 */       if (this.isDefaultName)
/* 409:    */       {
/* 410:575 */         resourceName = this.member.getName();
/* 411:576 */         if (((this.member instanceof Method)) && (resourceName.startsWith("set")) && (resourceName.length() > 3)) {
/* 412:577 */           resourceName = Introspector.decapitalize(resourceName.substring(3));
/* 413:    */         }
/* 414:    */       }
/* 415:580 */       if ((resourceType != null) && (!Object.class.equals(resourceType))) {
/* 416:581 */         checkResourceType(resourceType);
/* 417:    */       } else {
/* 418:585 */         resourceType = getResourceType();
/* 419:    */       }
/* 420:587 */       this.name = resourceName;
/* 421:588 */       this.elementType = resourceType;
/* 422:589 */       if (Service.class.isAssignableFrom(resourceType)) {
/* 423:590 */         this.lookupType = resourceType;
/* 424:    */       } else {
/* 425:593 */         this.lookupType = (!Object.class.equals(resource.value()) ? resource.value() : Service.class);
/* 426:    */       }
/* 427:595 */       this.mappedName = resource.mappedName();
/* 428:596 */       this.wsdlLocation = resource.wsdlLocation();
/* 429:    */     }
/* 430:    */     
/* 431:    */     protected Object getResourceToInject(Object target, String requestingBeanName)
/* 432:    */     {
/* 433:    */       Service service;
/* 434:    */       try
/* 435:    */       {
/* 436:603 */         service = (Service)CommonAnnotationBeanPostProcessor.this.getResource(this, requestingBeanName);
/* 437:    */       }
/* 438:    */       catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException)
/* 439:    */       {
/* 440:    */         Service service;
/* 441:607 */         if (Service.class.equals(this.lookupType)) {
/* 442:608 */           throw new IllegalStateException("No resource with name '" + this.name + "' found in context, " + 
/* 443:609 */             "and no specific JAX-WS Service subclass specified. The typical solution is to either specify " + 
/* 444:610 */             "a LocalJaxWsServiceFactoryBean with the given name or to specify the (generated) Service " + 
/* 445:611 */             "subclass as @WebServiceRef(...) value.");
/* 446:    */         }
/* 447:613 */         if (StringUtils.hasLength(this.wsdlLocation)) {
/* 448:    */           try
/* 449:    */           {
/* 450:615 */             Constructor<?> ctor = this.lookupType.getConstructor(new Class[] { URL.class, QName.class });
/* 451:616 */             WebServiceClient clientAnn = (WebServiceClient)this.lookupType.getAnnotation(WebServiceClient.class);
/* 452:617 */             if (clientAnn == null) {
/* 453:618 */               throw new IllegalStateException("JAX-WS Service class [" + this.lookupType.getName() + 
/* 454:619 */                 "] does not carry a WebServiceClient annotation");
/* 455:    */             }
/* 456:621 */             service = (Service)BeanUtils.instantiateClass(ctor, new Object[] {
/* 457:622 */               new URL(this.wsdlLocation), new QName(clientAnn.targetNamespace(), clientAnn.name()) });
/* 458:    */           }
/* 459:    */           catch (NoSuchMethodException localNoSuchMethodException)
/* 460:    */           {
/* 461:    */             Service service;
/* 462:625 */             throw new IllegalStateException("JAX-WS Service class [" + this.lookupType.getName() + 
/* 463:626 */               "] does not have a (URL, QName) constructor. Cannot apply specified WSDL location [" + 
/* 464:627 */               this.wsdlLocation + "].");
/* 465:    */           }
/* 466:    */           catch (MalformedURLException localMalformedURLException)
/* 467:    */           {
/* 468:630 */             throw new IllegalArgumentException(
/* 469:631 */               "Specified WSDL location [" + this.wsdlLocation + "] isn't a valid URL");
/* 470:    */           }
/* 471:    */         } else {
/* 472:635 */           service = (Service)BeanUtils.instantiateClass(this.lookupType);
/* 473:    */         }
/* 474:    */       }
/* 475:638 */       return service.getPort(this.elementType);
/* 476:    */     }
/* 477:    */   }
/* 478:    */   
/* 479:    */   private class EjbRefElement
/* 480:    */     extends CommonAnnotationBeanPostProcessor.LookupElement
/* 481:    */   {
/* 482:    */     private String beanName;
/* 483:    */     
/* 484:    */     public EjbRefElement(Member member, PropertyDescriptor pd)
/* 485:    */     {
/* 486:652 */       super(member, pd);
/* 487:    */     }
/* 488:    */     
/* 489:    */     protected void initAnnotation(AnnotatedElement ae)
/* 490:    */     {
/* 491:657 */       EJB resource = (EJB)ae.getAnnotation(EJB.class);
/* 492:658 */       String resourceBeanName = resource.beanName();
/* 493:659 */       String resourceName = resource.name();
/* 494:660 */       this.isDefaultName = (!StringUtils.hasLength(resourceName));
/* 495:661 */       if (this.isDefaultName)
/* 496:    */       {
/* 497:662 */         resourceName = this.member.getName();
/* 498:663 */         if (((this.member instanceof Method)) && (resourceName.startsWith("set")) && (resourceName.length() > 3)) {
/* 499:664 */           resourceName = Introspector.decapitalize(resourceName.substring(3));
/* 500:    */         }
/* 501:    */       }
/* 502:667 */       Class<?> resourceType = resource.beanInterface();
/* 503:668 */       if ((resourceType != null) && (!Object.class.equals(resourceType))) {
/* 504:669 */         checkResourceType(resourceType);
/* 505:    */       } else {
/* 506:673 */         resourceType = getResourceType();
/* 507:    */       }
/* 508:675 */       this.beanName = resourceBeanName;
/* 509:676 */       this.name = resourceName;
/* 510:677 */       this.lookupType = resourceType;
/* 511:678 */       this.mappedName = resource.mappedName();
/* 512:    */     }
/* 513:    */     
/* 514:    */     protected Object getResourceToInject(Object target, String requestingBeanName)
/* 515:    */     {
/* 516:683 */       if (StringUtils.hasLength(this.beanName))
/* 517:    */       {
/* 518:684 */         if ((CommonAnnotationBeanPostProcessor.this.beanFactory != null) && (CommonAnnotationBeanPostProcessor.this.beanFactory.containsBean(this.beanName)))
/* 519:    */         {
/* 520:686 */           Object bean = CommonAnnotationBeanPostProcessor.this.beanFactory.getBean(this.beanName, this.lookupType);
/* 521:687 */           if ((CommonAnnotationBeanPostProcessor.this.beanFactory instanceof ConfigurableBeanFactory)) {
/* 522:688 */             ((ConfigurableBeanFactory)CommonAnnotationBeanPostProcessor.this.beanFactory).registerDependentBean(this.beanName, requestingBeanName);
/* 523:    */           }
/* 524:690 */           return bean;
/* 525:    */         }
/* 526:692 */         if ((this.isDefaultName) && (!StringUtils.hasLength(this.mappedName))) {
/* 527:693 */           throw new NoSuchBeanDefinitionException(this.beanName, 
/* 528:694 */             "Cannot resolve 'beanName' in local BeanFactory. Consider specifying a general 'name' value instead.");
/* 529:    */         }
/* 530:    */       }
/* 531:698 */       return CommonAnnotationBeanPostProcessor.this.getResource(this, requestingBeanName);
/* 532:    */     }
/* 533:    */   }
/* 534:    */   
/* 535:    */   private static class LookupDependencyDescriptor
/* 536:    */     extends DependencyDescriptor
/* 537:    */   {
/* 538:    */     private final Class<?> lookupType;
/* 539:    */     
/* 540:    */     public LookupDependencyDescriptor(Field field, Class<?> lookupType)
/* 541:    */     {
/* 542:712 */       super(true);
/* 543:713 */       this.lookupType = lookupType;
/* 544:    */     }
/* 545:    */     
/* 546:    */     public LookupDependencyDescriptor(Method method, Class<?> lookupType)
/* 547:    */     {
/* 548:717 */       super(true);
/* 549:718 */       this.lookupType = lookupType;
/* 550:    */     }
/* 551:    */     
/* 552:    */     public Class<?> getDependencyType()
/* 553:    */     {
/* 554:723 */       return this.lookupType;
/* 555:    */     }
/* 556:    */   }
/* 557:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.CommonAnnotationBeanPostProcessor
 * JD-Core Version:    0.7.0.1
 */