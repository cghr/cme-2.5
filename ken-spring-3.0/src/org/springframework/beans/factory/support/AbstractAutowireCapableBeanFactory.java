/*    1:     */ package org.springframework.beans.factory.support;
/*    2:     */ 
/*    3:     */ import java.beans.PropertyDescriptor;
/*    4:     */ import java.lang.reflect.Constructor;
/*    5:     */ import java.lang.reflect.InvocationTargetException;
/*    6:     */ import java.lang.reflect.Method;
/*    7:     */ import java.lang.reflect.Modifier;
/*    8:     */ import java.security.AccessController;
/*    9:     */ import java.security.PrivilegedAction;
/*   10:     */ import java.security.PrivilegedActionException;
/*   11:     */ import java.security.PrivilegedExceptionAction;
/*   12:     */ import java.util.ArrayList;
/*   13:     */ import java.util.Arrays;
/*   14:     */ import java.util.Collection;
/*   15:     */ import java.util.HashMap;
/*   16:     */ import java.util.HashSet;
/*   17:     */ import java.util.Iterator;
/*   18:     */ import java.util.LinkedHashSet;
/*   19:     */ import java.util.LinkedList;
/*   20:     */ import java.util.List;
/*   21:     */ import java.util.Map;
/*   22:     */ import java.util.Set;
/*   23:     */ import java.util.TreeSet;
/*   24:     */ import java.util.concurrent.ConcurrentHashMap;
/*   25:     */ import org.apache.commons.logging.Log;
/*   26:     */ import org.springframework.beans.BeanUtils;
/*   27:     */ import org.springframework.beans.BeanWrapper;
/*   28:     */ import org.springframework.beans.BeanWrapperImpl;
/*   29:     */ import org.springframework.beans.BeansException;
/*   30:     */ import org.springframework.beans.MutablePropertyValues;
/*   31:     */ import org.springframework.beans.PropertyAccessorUtils;
/*   32:     */ import org.springframework.beans.PropertyValue;
/*   33:     */ import org.springframework.beans.PropertyValues;
/*   34:     */ import org.springframework.beans.TypeConverter;
/*   35:     */ import org.springframework.beans.factory.Aware;
/*   36:     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*   37:     */ import org.springframework.beans.factory.BeanCreationException;
/*   38:     */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*   39:     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*   40:     */ import org.springframework.beans.factory.BeanFactory;
/*   41:     */ import org.springframework.beans.factory.BeanFactoryAware;
/*   42:     */ import org.springframework.beans.factory.BeanNameAware;
/*   43:     */ import org.springframework.beans.factory.FactoryBean;
/*   44:     */ import org.springframework.beans.factory.InitializingBean;
/*   45:     */ import org.springframework.beans.factory.ObjectFactory;
/*   46:     */ import org.springframework.beans.factory.UnsatisfiedDependencyException;
/*   47:     */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*   48:     */ import org.springframework.beans.factory.config.BeanDefinition;
/*   49:     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*   50:     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*   51:     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*   52:     */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*   53:     */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
/*   54:     */ import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
/*   55:     */ import org.springframework.beans.factory.config.TypedStringValue;
/*   56:     */ import org.springframework.core.GenericTypeResolver;
/*   57:     */ import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
/*   58:     */ import org.springframework.core.MethodParameter;
/*   59:     */ import org.springframework.core.ParameterNameDiscoverer;
/*   60:     */ import org.springframework.core.PriorityOrdered;
/*   61:     */ import org.springframework.util.ClassUtils;
/*   62:     */ import org.springframework.util.ObjectUtils;
/*   63:     */ import org.springframework.util.ReflectionUtils;
/*   64:     */ import org.springframework.util.StringUtils;
/*   65:     */ 
/*   66:     */ public abstract class AbstractAutowireCapableBeanFactory
/*   67:     */   extends AbstractBeanFactory
/*   68:     */   implements AutowireCapableBeanFactory
/*   69:     */ {
/*   70: 121 */   private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();
/*   71: 124 */   private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
/*   72: 127 */   private boolean allowCircularReferences = true;
/*   73: 133 */   private boolean allowRawInjectionDespiteWrapping = false;
/*   74: 139 */   private final Set<Class> ignoredDependencyTypes = new HashSet();
/*   75: 145 */   private final Set<Class> ignoredDependencyInterfaces = new HashSet();
/*   76: 149 */   private final Map<String, BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap();
/*   77: 153 */   private final Map<Class, PropertyDescriptor[]> filteredPropertyDescriptorsCache = new HashMap();
/*   78:     */   
/*   79:     */   public AbstractAutowireCapableBeanFactory()
/*   80:     */   {
/*   81: 161 */     ignoreDependencyInterface(BeanNameAware.class);
/*   82: 162 */     ignoreDependencyInterface(BeanFactoryAware.class);
/*   83: 163 */     ignoreDependencyInterface(BeanClassLoaderAware.class);
/*   84:     */   }
/*   85:     */   
/*   86:     */   public AbstractAutowireCapableBeanFactory(BeanFactory parentBeanFactory)
/*   87:     */   {
/*   88: 171 */     this();
/*   89: 172 */     setParentBeanFactory(parentBeanFactory);
/*   90:     */   }
/*   91:     */   
/*   92:     */   public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy)
/*   93:     */   {
/*   94: 181 */     this.instantiationStrategy = instantiationStrategy;
/*   95:     */   }
/*   96:     */   
/*   97:     */   protected InstantiationStrategy getInstantiationStrategy()
/*   98:     */   {
/*   99: 188 */     return this.instantiationStrategy;
/*  100:     */   }
/*  101:     */   
/*  102:     */   public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer)
/*  103:     */   {
/*  104: 197 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*  105:     */   }
/*  106:     */   
/*  107:     */   protected ParameterNameDiscoverer getParameterNameDiscoverer()
/*  108:     */   {
/*  109: 205 */     return this.parameterNameDiscoverer;
/*  110:     */   }
/*  111:     */   
/*  112:     */   public void setAllowCircularReferences(boolean allowCircularReferences)
/*  113:     */   {
/*  114: 222 */     this.allowCircularReferences = allowCircularReferences;
/*  115:     */   }
/*  116:     */   
/*  117:     */   public void setAllowRawInjectionDespiteWrapping(boolean allowRawInjectionDespiteWrapping)
/*  118:     */   {
/*  119: 240 */     this.allowRawInjectionDespiteWrapping = allowRawInjectionDespiteWrapping;
/*  120:     */   }
/*  121:     */   
/*  122:     */   public void ignoreDependencyType(Class type)
/*  123:     */   {
/*  124: 248 */     this.ignoredDependencyTypes.add(type);
/*  125:     */   }
/*  126:     */   
/*  127:     */   public void ignoreDependencyInterface(Class ifc)
/*  128:     */   {
/*  129: 262 */     this.ignoredDependencyInterfaces.add(ifc);
/*  130:     */   }
/*  131:     */   
/*  132:     */   public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory)
/*  133:     */   {
/*  134: 268 */     super.copyConfigurationFrom(otherFactory);
/*  135: 269 */     if ((otherFactory instanceof AbstractAutowireCapableBeanFactory))
/*  136:     */     {
/*  137: 270 */       AbstractAutowireCapableBeanFactory otherAutowireFactory = 
/*  138: 271 */         (AbstractAutowireCapableBeanFactory)otherFactory;
/*  139: 272 */       this.instantiationStrategy = otherAutowireFactory.instantiationStrategy;
/*  140: 273 */       this.allowCircularReferences = otherAutowireFactory.allowCircularReferences;
/*  141: 274 */       this.ignoredDependencyTypes.addAll(otherAutowireFactory.ignoredDependencyTypes);
/*  142: 275 */       this.ignoredDependencyInterfaces.addAll(otherAutowireFactory.ignoredDependencyInterfaces);
/*  143:     */     }
/*  144:     */   }
/*  145:     */   
/*  146:     */   public <T> T createBean(Class<T> beanClass)
/*  147:     */     throws BeansException
/*  148:     */   {
/*  149: 287 */     RootBeanDefinition bd = new RootBeanDefinition(beanClass);
/*  150: 288 */     bd.setScope("prototype");
/*  151: 289 */     return createBean(beanClass.getName(), bd, null);
/*  152:     */   }
/*  153:     */   
/*  154:     */   public void autowireBean(Object existingBean)
/*  155:     */   {
/*  156: 294 */     RootBeanDefinition bd = new RootBeanDefinition(ClassUtils.getUserClass(existingBean));
/*  157: 295 */     bd.setScope("prototype");
/*  158: 296 */     BeanWrapper bw = new BeanWrapperImpl(existingBean);
/*  159: 297 */     initBeanWrapper(bw);
/*  160: 298 */     populateBean(bd.getBeanClass().getName(), bd, bw);
/*  161:     */   }
/*  162:     */   
/*  163:     */   public Object configureBean(Object existingBean, String beanName)
/*  164:     */     throws BeansException
/*  165:     */   {
/*  166: 302 */     markBeanAsCreated(beanName);
/*  167: 303 */     BeanDefinition mbd = getMergedBeanDefinition(beanName);
/*  168: 304 */     RootBeanDefinition bd = null;
/*  169: 305 */     if ((mbd instanceof RootBeanDefinition))
/*  170:     */     {
/*  171: 306 */       RootBeanDefinition rbd = (RootBeanDefinition)mbd;
/*  172: 307 */       if (rbd.isPrototype()) {
/*  173: 308 */         bd = rbd;
/*  174:     */       }
/*  175:     */     }
/*  176: 311 */     if (bd == null)
/*  177:     */     {
/*  178: 312 */       bd = new RootBeanDefinition(mbd);
/*  179: 313 */       bd.setScope("prototype");
/*  180:     */     }
/*  181: 315 */     BeanWrapper bw = new BeanWrapperImpl(existingBean);
/*  182: 316 */     initBeanWrapper(bw);
/*  183: 317 */     populateBean(beanName, bd, bw);
/*  184: 318 */     return initializeBean(beanName, existingBean, bd);
/*  185:     */   }
/*  186:     */   
/*  187:     */   public Object resolveDependency(DependencyDescriptor descriptor, String beanName)
/*  188:     */     throws BeansException
/*  189:     */   {
/*  190: 322 */     return resolveDependency(descriptor, beanName, null, null);
/*  191:     */   }
/*  192:     */   
/*  193:     */   public Object createBean(Class beanClass, int autowireMode, boolean dependencyCheck)
/*  194:     */     throws BeansException
/*  195:     */   {
/*  196: 332 */     RootBeanDefinition bd = new RootBeanDefinition(beanClass, autowireMode, dependencyCheck);
/*  197: 333 */     bd.setScope("prototype");
/*  198: 334 */     return createBean(beanClass.getName(), bd, null);
/*  199:     */   }
/*  200:     */   
/*  201:     */   public Object autowire(Class beanClass, int autowireMode, boolean dependencyCheck)
/*  202:     */     throws BeansException
/*  203:     */   {
/*  204: 339 */     final RootBeanDefinition bd = new RootBeanDefinition(beanClass, autowireMode, dependencyCheck);
/*  205: 340 */     bd.setScope("prototype");
/*  206: 341 */     if (bd.getResolvedAutowireMode() == 3) {
/*  207: 342 */       return autowireConstructor(beanClass.getName(), bd, null, null).getWrappedInstance();
/*  208:     */     }
/*  209: 346 */     final BeanFactory parent = this;
/*  210:     */     Object bean;
/*  211:     */     Object bean;
/*  212: 348 */     if (System.getSecurityManager() != null) {
/*  213: 349 */       bean = AccessController.doPrivileged(new PrivilegedAction()
/*  214:     */       {
/*  215:     */         public Object run()
/*  216:     */         {
/*  217: 352 */           return AbstractAutowireCapableBeanFactory.this.getInstantiationStrategy().instantiate(bd, null, parent);
/*  218:     */         }
/*  219: 354 */       }, getAccessControlContext());
/*  220:     */     } else {
/*  221: 357 */       bean = getInstantiationStrategy().instantiate(bd, null, parent);
/*  222:     */     }
/*  223: 360 */     populateBean(beanClass.getName(), bd, new BeanWrapperImpl(bean));
/*  224: 361 */     return bean;
/*  225:     */   }
/*  226:     */   
/*  227:     */   public void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck)
/*  228:     */     throws BeansException
/*  229:     */   {
/*  230: 368 */     if (autowireMode == 3) {
/*  231: 369 */       throw new IllegalArgumentException("AUTOWIRE_CONSTRUCTOR not supported for existing bean instance");
/*  232:     */     }
/*  233: 372 */     RootBeanDefinition bd = 
/*  234: 373 */       new RootBeanDefinition(ClassUtils.getUserClass(existingBean), autowireMode, dependencyCheck);
/*  235: 374 */     bd.setScope("prototype");
/*  236: 375 */     BeanWrapper bw = new BeanWrapperImpl(existingBean);
/*  237: 376 */     initBeanWrapper(bw);
/*  238: 377 */     populateBean(bd.getBeanClass().getName(), bd, bw);
/*  239:     */   }
/*  240:     */   
/*  241:     */   public void applyBeanPropertyValues(Object existingBean, String beanName)
/*  242:     */     throws BeansException
/*  243:     */   {
/*  244: 381 */     markBeanAsCreated(beanName);
/*  245: 382 */     BeanDefinition bd = getMergedBeanDefinition(beanName);
/*  246: 383 */     BeanWrapper bw = new BeanWrapperImpl(existingBean);
/*  247: 384 */     initBeanWrapper(bw);
/*  248: 385 */     applyPropertyValues(beanName, bd, bw, bd.getPropertyValues());
/*  249:     */   }
/*  250:     */   
/*  251:     */   public Object initializeBean(Object existingBean, String beanName)
/*  252:     */   {
/*  253: 389 */     return initializeBean(beanName, existingBean, null);
/*  254:     */   }
/*  255:     */   
/*  256:     */   public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
/*  257:     */     throws BeansException
/*  258:     */   {
/*  259: 395 */     Object result = existingBean;
/*  260: 396 */     for (BeanPostProcessor beanProcessor : getBeanPostProcessors())
/*  261:     */     {
/*  262: 397 */       result = beanProcessor.postProcessBeforeInitialization(result, beanName);
/*  263: 398 */       if (result == null) {
/*  264: 399 */         return result;
/*  265:     */       }
/*  266:     */     }
/*  267: 402 */     return result;
/*  268:     */   }
/*  269:     */   
/*  270:     */   public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
/*  271:     */     throws BeansException
/*  272:     */   {
/*  273: 408 */     Object result = existingBean;
/*  274: 409 */     for (BeanPostProcessor beanProcessor : getBeanPostProcessors())
/*  275:     */     {
/*  276: 410 */       result = beanProcessor.postProcessAfterInitialization(result, beanName);
/*  277: 411 */       if (result == null) {
/*  278: 412 */         return result;
/*  279:     */       }
/*  280:     */     }
/*  281: 415 */     return result;
/*  282:     */   }
/*  283:     */   
/*  284:     */   protected Object createBean(String beanName, RootBeanDefinition mbd, Object[] args)
/*  285:     */     throws BeanCreationException
/*  286:     */   {
/*  287: 432 */     if (this.logger.isDebugEnabled()) {
/*  288: 433 */       this.logger.debug("Creating instance of bean '" + beanName + "'");
/*  289:     */     }
/*  290: 436 */     resolveBeanClass(mbd, beanName, new Class[0]);
/*  291:     */     try
/*  292:     */     {
/*  293: 440 */       mbd.prepareMethodOverrides();
/*  294:     */     }
/*  295:     */     catch (BeanDefinitionValidationException ex)
/*  296:     */     {
/*  297: 443 */       throw new BeanDefinitionStoreException(mbd.getResourceDescription(), 
/*  298: 444 */         beanName, "Validation of method overrides failed", ex);
/*  299:     */     }
/*  300:     */     try
/*  301:     */     {
/*  302: 449 */       Object bean = resolveBeforeInstantiation(beanName, mbd);
/*  303: 450 */       if (bean != null) {
/*  304: 451 */         return bean;
/*  305:     */       }
/*  306:     */     }
/*  307:     */     catch (Throwable ex)
/*  308:     */     {
/*  309: 455 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, 
/*  310: 456 */         "BeanPostProcessor before instantiation of bean failed", ex);
/*  311:     */     }
/*  312: 459 */     Object beanInstance = doCreateBean(beanName, mbd, args);
/*  313: 460 */     if (this.logger.isDebugEnabled()) {
/*  314: 461 */       this.logger.debug("Finished creating instance of bean '" + beanName + "'");
/*  315:     */     }
/*  316: 463 */     return beanInstance;
/*  317:     */   }
/*  318:     */   
/*  319:     */   protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, Object[] args)
/*  320:     */   {
/*  321: 483 */     BeanWrapper instanceWrapper = null;
/*  322: 484 */     if (mbd.isSingleton()) {
/*  323: 485 */       instanceWrapper = (BeanWrapper)this.factoryBeanInstanceCache.remove(beanName);
/*  324:     */     }
/*  325: 487 */     if (instanceWrapper == null) {
/*  326: 488 */       instanceWrapper = createBeanInstance(beanName, mbd, args);
/*  327:     */     }
/*  328: 490 */     final Object bean = instanceWrapper != null ? instanceWrapper.getWrappedInstance() : null;
/*  329: 491 */     Class beanType = instanceWrapper != null ? instanceWrapper.getWrappedClass() : null;
/*  330: 494 */     synchronized (mbd.postProcessingLock)
/*  331:     */     {
/*  332: 495 */       if (!mbd.postProcessed)
/*  333:     */       {
/*  334: 496 */         applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);
/*  335: 497 */         mbd.postProcessed = true;
/*  336:     */       }
/*  337:     */     }
/*  338: 503 */     boolean earlySingletonExposure = (mbd.isSingleton()) && (this.allowCircularReferences) && 
/*  339: 504 */       (isSingletonCurrentlyInCreation(beanName));
/*  340: 505 */     if (earlySingletonExposure)
/*  341:     */     {
/*  342: 506 */       if (this.logger.isDebugEnabled()) {
/*  343: 507 */         this.logger.debug("Eagerly caching bean '" + beanName + 
/*  344: 508 */           "' to allow for resolving potential circular references");
/*  345:     */       }
/*  346: 510 */       addSingletonFactory(beanName, new ObjectFactory()
/*  347:     */       {
/*  348:     */         public Object getObject()
/*  349:     */           throws BeansException
/*  350:     */         {
/*  351: 512 */           return AbstractAutowireCapableBeanFactory.this.getEarlyBeanReference(beanName, mbd, bean);
/*  352:     */         }
/*  353:     */       });
/*  354:     */     }
/*  355: 518 */     Object exposedObject = bean;
/*  356:     */     try
/*  357:     */     {
/*  358: 520 */       populateBean(beanName, mbd, instanceWrapper);
/*  359: 521 */       if (exposedObject != null) {
/*  360: 522 */         exposedObject = initializeBean(beanName, exposedObject, mbd);
/*  361:     */       }
/*  362:     */     }
/*  363:     */     catch (Throwable ex)
/*  364:     */     {
/*  365: 526 */       if (((ex instanceof BeanCreationException)) && (beanName.equals(((BeanCreationException)ex).getBeanName()))) {
/*  366: 527 */         throw ((BeanCreationException)ex);
/*  367:     */       }
/*  368: 530 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Initialization of bean failed", ex);
/*  369:     */     }
/*  370: 534 */     if (earlySingletonExposure)
/*  371:     */     {
/*  372: 535 */       Object earlySingletonReference = getSingleton(beanName, false);
/*  373: 536 */       if (earlySingletonReference != null) {
/*  374: 537 */         if (exposedObject == bean)
/*  375:     */         {
/*  376: 538 */           exposedObject = earlySingletonReference;
/*  377:     */         }
/*  378: 540 */         else if ((!this.allowRawInjectionDespiteWrapping) && (hasDependentBean(beanName)))
/*  379:     */         {
/*  380: 541 */           String[] dependentBeans = getDependentBeans(beanName);
/*  381: 542 */           Set<String> actualDependentBeans = new LinkedHashSet(dependentBeans.length);
/*  382: 543 */           for (String dependentBean : dependentBeans) {
/*  383: 544 */             if (!removeSingletonIfCreatedForTypeCheckOnly(dependentBean)) {
/*  384: 545 */               actualDependentBeans.add(dependentBean);
/*  385:     */             }
/*  386:     */           }
/*  387: 548 */           if (!actualDependentBeans.isEmpty()) {
/*  388: 549 */             throw new BeanCurrentlyInCreationException(beanName, 
/*  389: 550 */               "Bean with name '" + beanName + "' has been injected into other beans [" + 
/*  390: 551 */               StringUtils.collectionToCommaDelimitedString(actualDependentBeans) + 
/*  391: 552 */               "] in its raw version as part of a circular reference, but has eventually been " + 
/*  392: 553 */               "wrapped. This means that said other beans do not use the final version of the " + 
/*  393: 554 */               "bean. This is often the result of over-eager type matching - consider using " + 
/*  394: 555 */               "'getBeanNamesOfType' with the 'allowEagerInit' flag turned off, for example.");
/*  395:     */           }
/*  396:     */         }
/*  397:     */       }
/*  398:     */     }
/*  399:     */     try
/*  400:     */     {
/*  401: 563 */       registerDisposableBeanIfNecessary(beanName, bean, mbd);
/*  402:     */     }
/*  403:     */     catch (BeanDefinitionValidationException ex)
/*  404:     */     {
/*  405: 566 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Invalid destruction signature", ex);
/*  406:     */     }
/*  407: 569 */     return exposedObject;
/*  408:     */   }
/*  409:     */   
/*  410:     */   protected Class predictBeanType(String beanName, RootBeanDefinition mbd, Class... typesToMatch)
/*  411:     */   {
/*  412:     */     Class beanClass;
/*  413:     */     Class beanClass;
/*  414: 575 */     if (mbd.getFactoryMethodName() != null) {
/*  415: 576 */       beanClass = getTypeForFactoryMethod(beanName, mbd, typesToMatch);
/*  416:     */     } else {
/*  417: 579 */       beanClass = resolveBeanClass(mbd, beanName, typesToMatch);
/*  418:     */     }
/*  419: 583 */     if ((beanClass != null) && (!mbd.isSynthetic()) && (hasInstantiationAwareBeanPostProcessors())) {
/*  420: 584 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/*  421: 585 */         if ((bp instanceof SmartInstantiationAwareBeanPostProcessor))
/*  422:     */         {
/*  423: 586 */           SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor)bp;
/*  424: 587 */           Class processedType = ibp.predictBeanType(beanClass, beanName);
/*  425: 588 */           if (processedType != null) {
/*  426: 589 */             return processedType;
/*  427:     */           }
/*  428:     */         }
/*  429:     */       }
/*  430:     */     }
/*  431: 594 */     return beanClass;
/*  432:     */   }
/*  433:     */   
/*  434:     */   protected Class getTypeForFactoryMethod(String beanName, RootBeanDefinition mbd, Class[] typesToMatch)
/*  435:     */   {
/*  436: 613 */     boolean isStatic = true;
/*  437:     */     
/*  438: 615 */     String factoryBeanName = mbd.getFactoryBeanName();
/*  439:     */     Class factoryClass;
/*  440: 616 */     if (factoryBeanName != null)
/*  441:     */     {
/*  442: 617 */       if (factoryBeanName.equals(beanName)) {
/*  443: 618 */         throw new BeanDefinitionStoreException(mbd.getResourceDescription(), beanName, 
/*  444: 619 */           "factory-bean reference points back to the same bean definition");
/*  445:     */       }
/*  446: 622 */       Class factoryClass = getType(factoryBeanName);
/*  447: 623 */       isStatic = false;
/*  448:     */     }
/*  449:     */     else
/*  450:     */     {
/*  451: 627 */       factoryClass = resolveBeanClass(mbd, beanName, typesToMatch);
/*  452:     */     }
/*  453: 630 */     if (factoryClass == null) {
/*  454: 631 */       return null;
/*  455:     */     }
/*  456: 636 */     int minNrOfArgs = mbd.getConstructorArgumentValues().getArgumentCount();
/*  457: 637 */     Method[] candidates = ReflectionUtils.getUniqueDeclaredMethods(factoryClass);
/*  458: 638 */     Set<Class> returnTypes = new HashSet(1);
/*  459: 639 */     for (Method factoryMethod : candidates) {
/*  460: 640 */       if ((Modifier.isStatic(factoryMethod.getModifiers()) == isStatic) && 
/*  461: 641 */         (factoryMethod.getName().equals(mbd.getFactoryMethodName())) && 
/*  462: 642 */         (factoryMethod.getParameterTypes().length >= minNrOfArgs)) {
/*  463: 643 */         returnTypes.add(factoryMethod.getReturnType());
/*  464:     */       }
/*  465:     */     }
/*  466: 647 */     if (returnTypes.size() == 1) {
/*  467: 649 */       return (Class)returnTypes.iterator().next();
/*  468:     */     }
/*  469: 653 */     return null;
/*  470:     */   }
/*  471:     */   
/*  472:     */   protected Class<?> getTypeForFactoryBean(String beanName, RootBeanDefinition mbd)
/*  473:     */   {
/*  474: 670 */     Class<?> objectType = null;
/*  475: 671 */     String factoryBeanName = mbd.getFactoryBeanName();
/*  476: 672 */     String factoryMethodName = mbd.getFactoryMethodName();
/*  477: 673 */     if ((factoryBeanName != null) && (factoryMethodName != null))
/*  478:     */     {
/*  479: 675 */       BeanDefinition fbDef = getBeanDefinition(factoryBeanName);
/*  480: 676 */       if ((fbDef instanceof AbstractBeanDefinition))
/*  481:     */       {
/*  482: 677 */         Class<?> fbClass = ((AbstractBeanDefinition)fbDef).getBeanClass();
/*  483: 678 */         if (ClassUtils.isCglibProxyClass(fbClass)) {
/*  484: 680 */           fbClass = fbClass.getSuperclass();
/*  485:     */         }
/*  486: 682 */         Method m = ReflectionUtils.findMethod(fbClass, factoryMethodName);
/*  487: 683 */         objectType = GenericTypeResolver.resolveReturnTypeArgument(m, FactoryBean.class);
/*  488: 684 */         if (objectType != null) {
/*  489: 685 */           return objectType;
/*  490:     */         }
/*  491:     */       }
/*  492:     */     }
/*  493: 690 */     FactoryBean<?> fb = mbd.isSingleton() ? 
/*  494: 691 */       getSingletonFactoryBeanForTypeCheck(beanName, mbd) : 
/*  495: 692 */       getNonSingletonFactoryBeanForTypeCheck(beanName, mbd);
/*  496: 694 */     if (fb != null)
/*  497:     */     {
/*  498: 696 */       objectType = getTypeForFactoryBean(fb);
/*  499: 697 */       if (objectType != null) {
/*  500: 698 */         return objectType;
/*  501:     */       }
/*  502:     */     }
/*  503: 703 */     return super.getTypeForFactoryBean(beanName, mbd);
/*  504:     */   }
/*  505:     */   
/*  506:     */   protected Object getEarlyBeanReference(String beanName, RootBeanDefinition mbd, Object bean)
/*  507:     */   {
/*  508: 715 */     Object exposedObject = bean;
/*  509: 716 */     if ((bean != null) && (!mbd.isSynthetic()) && (hasInstantiationAwareBeanPostProcessors())) {
/*  510: 717 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/*  511: 718 */         if ((bp instanceof SmartInstantiationAwareBeanPostProcessor))
/*  512:     */         {
/*  513: 719 */           SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor)bp;
/*  514: 720 */           exposedObject = ibp.getEarlyBeanReference(exposedObject, beanName);
/*  515: 721 */           if (exposedObject == null) {
/*  516: 722 */             return exposedObject;
/*  517:     */           }
/*  518:     */         }
/*  519:     */       }
/*  520:     */     }
/*  521: 727 */     return exposedObject;
/*  522:     */   }
/*  523:     */   
/*  524:     */   private FactoryBean getSingletonFactoryBeanForTypeCheck(String beanName, RootBeanDefinition mbd)
/*  525:     */   {
/*  526: 745 */     synchronized (getSingletonMutex())
/*  527:     */     {
/*  528: 746 */       BeanWrapper bw = (BeanWrapper)this.factoryBeanInstanceCache.get(beanName);
/*  529: 747 */       if (bw != null) {
/*  530: 748 */         return (FactoryBean)bw.getWrappedInstance();
/*  531:     */       }
/*  532: 750 */       if (isSingletonCurrentlyInCreation(beanName)) {
/*  533: 751 */         return null;
/*  534:     */       }
/*  535: 753 */       Object instance = null;
/*  536:     */       try
/*  537:     */       {
/*  538: 756 */         beforeSingletonCreation(beanName);
/*  539:     */         
/*  540: 758 */         instance = resolveBeforeInstantiation(beanName, mbd);
/*  541: 759 */         if (instance == null)
/*  542:     */         {
/*  543: 760 */           bw = createBeanInstance(beanName, mbd, null);
/*  544: 761 */           instance = bw.getWrappedInstance();
/*  545:     */         }
/*  546:     */       }
/*  547:     */       finally
/*  548:     */       {
/*  549: 766 */         afterSingletonCreation(beanName);
/*  550:     */       }
/*  551: 768 */       FactoryBean fb = getFactoryBean(beanName, instance);
/*  552: 769 */       if (bw != null) {
/*  553: 770 */         this.factoryBeanInstanceCache.put(beanName, bw);
/*  554:     */       }
/*  555: 772 */       return fb;
/*  556:     */     }
/*  557:     */   }
/*  558:     */   
/*  559:     */   private FactoryBean getNonSingletonFactoryBeanForTypeCheck(String beanName, RootBeanDefinition mbd)
/*  560:     */   {
/*  561: 786 */     if (isPrototypeCurrentlyInCreation(beanName)) {
/*  562: 787 */       return null;
/*  563:     */     }
/*  564: 789 */     Object instance = null;
/*  565:     */     try
/*  566:     */     {
/*  567: 792 */       beforePrototypeCreation(beanName);
/*  568:     */       
/*  569: 794 */       instance = resolveBeforeInstantiation(beanName, mbd);
/*  570: 795 */       if (instance == null)
/*  571:     */       {
/*  572: 796 */         BeanWrapper bw = createBeanInstance(beanName, mbd, null);
/*  573: 797 */         instance = bw.getWrappedInstance();
/*  574:     */       }
/*  575:     */     }
/*  576:     */     finally
/*  577:     */     {
/*  578: 802 */       afterPrototypeCreation(beanName);
/*  579:     */     }
/*  580: 804 */     return getFactoryBean(beanName, instance);
/*  581:     */   }
/*  582:     */   
/*  583:     */   protected void applyMergedBeanDefinitionPostProcessors(RootBeanDefinition mbd, Class beanType, String beanName)
/*  584:     */     throws BeansException
/*  585:     */   {
/*  586:     */     try
/*  587:     */     {
/*  588: 820 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/*  589: 821 */         if ((bp instanceof MergedBeanDefinitionPostProcessor))
/*  590:     */         {
/*  591: 822 */           MergedBeanDefinitionPostProcessor bdp = (MergedBeanDefinitionPostProcessor)bp;
/*  592: 823 */           bdp.postProcessMergedBeanDefinition(mbd, beanType, beanName);
/*  593:     */         }
/*  594:     */       }
/*  595:     */     }
/*  596:     */     catch (Exception ex)
/*  597:     */     {
/*  598: 828 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, 
/*  599: 829 */         "Post-processing failed of bean type [" + beanType + "] failed", ex);
/*  600:     */     }
/*  601:     */   }
/*  602:     */   
/*  603:     */   protected Object resolveBeforeInstantiation(String beanName, RootBeanDefinition mbd)
/*  604:     */   {
/*  605: 841 */     Object bean = null;
/*  606: 842 */     if (!Boolean.FALSE.equals(mbd.beforeInstantiationResolved))
/*  607:     */     {
/*  608: 844 */       if ((mbd.hasBeanClass()) && (!mbd.isSynthetic()) && (hasInstantiationAwareBeanPostProcessors()))
/*  609:     */       {
/*  610: 845 */         bean = applyBeanPostProcessorsBeforeInstantiation(mbd.getBeanClass(), beanName);
/*  611: 846 */         if (bean != null) {
/*  612: 847 */           bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
/*  613:     */         }
/*  614:     */       }
/*  615: 850 */       mbd.beforeInstantiationResolved = Boolean.valueOf(bean != null);
/*  616:     */     }
/*  617: 852 */     return bean;
/*  618:     */   }
/*  619:     */   
/*  620:     */   protected Object applyBeanPostProcessorsBeforeInstantiation(Class beanClass, String beanName)
/*  621:     */     throws BeansException
/*  622:     */   {
/*  623: 870 */     for (BeanPostProcessor bp : getBeanPostProcessors()) {
/*  624: 871 */       if ((bp instanceof InstantiationAwareBeanPostProcessor))
/*  625:     */       {
/*  626: 872 */         InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor)bp;
/*  627: 873 */         Object result = ibp.postProcessBeforeInstantiation(beanClass, beanName);
/*  628: 874 */         if (result != null) {
/*  629: 875 */           return result;
/*  630:     */         }
/*  631:     */       }
/*  632:     */     }
/*  633: 879 */     return null;
/*  634:     */   }
/*  635:     */   
/*  636:     */   protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, Object[] args)
/*  637:     */   {
/*  638: 896 */     Class beanClass = resolveBeanClass(mbd, beanName, new Class[0]);
/*  639: 898 */     if ((beanClass != null) && (!Modifier.isPublic(beanClass.getModifiers())) && (!mbd.isNonPublicAccessAllowed())) {
/*  640: 899 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, 
/*  641: 900 */         "Bean class isn't public, and non-public access not allowed: " + beanClass.getName());
/*  642:     */     }
/*  643: 903 */     if (mbd.getFactoryMethodName() != null) {
/*  644: 904 */       return instantiateUsingFactoryMethod(beanName, mbd, args);
/*  645:     */     }
/*  646: 908 */     boolean resolved = false;
/*  647: 909 */     boolean autowireNecessary = false;
/*  648: 910 */     if (args == null) {
/*  649: 911 */       synchronized (mbd.constructorArgumentLock)
/*  650:     */       {
/*  651: 912 */         if (mbd.resolvedConstructorOrFactoryMethod != null)
/*  652:     */         {
/*  653: 913 */           resolved = true;
/*  654: 914 */           autowireNecessary = mbd.constructorArgumentsResolved;
/*  655:     */         }
/*  656:     */       }
/*  657:     */     }
/*  658: 918 */     if (resolved)
/*  659:     */     {
/*  660: 919 */       if (autowireNecessary) {
/*  661: 920 */         return autowireConstructor(beanName, mbd, null, null);
/*  662:     */       }
/*  663: 923 */       return instantiateBean(beanName, mbd);
/*  664:     */     }
/*  665: 928 */     Constructor[] ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName);
/*  666: 929 */     if ((ctors != null) || 
/*  667: 930 */       (mbd.getResolvedAutowireMode() == 3) || 
/*  668: 931 */       (mbd.hasConstructorArgumentValues()) || (!ObjectUtils.isEmpty(args))) {
/*  669: 932 */       return autowireConstructor(beanName, mbd, ctors, args);
/*  670:     */     }
/*  671: 936 */     return instantiateBean(beanName, mbd);
/*  672:     */   }
/*  673:     */   
/*  674:     */   protected Constructor[] determineConstructorsFromBeanPostProcessors(Class beanClass, String beanName)
/*  675:     */     throws BeansException
/*  676:     */   {
/*  677: 951 */     if ((beanClass != null) && (hasInstantiationAwareBeanPostProcessors())) {
/*  678: 952 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/*  679: 953 */         if ((bp instanceof SmartInstantiationAwareBeanPostProcessor))
/*  680:     */         {
/*  681: 954 */           SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor)bp;
/*  682: 955 */           Constructor[] ctors = ibp.determineCandidateConstructors(beanClass, beanName);
/*  683: 956 */           if (ctors != null) {
/*  684: 957 */             return ctors;
/*  685:     */           }
/*  686:     */         }
/*  687:     */       }
/*  688:     */     }
/*  689: 962 */     return null;
/*  690:     */   }
/*  691:     */   
/*  692:     */   protected BeanWrapper instantiateBean(final String beanName, final RootBeanDefinition mbd)
/*  693:     */   {
/*  694:     */     try
/*  695:     */     {
/*  696: 974 */       final BeanFactory parent = this;
/*  697:     */       Object beanInstance;
/*  698:     */       Object beanInstance;
/*  699: 975 */       if (System.getSecurityManager() != null) {
/*  700: 976 */         beanInstance = AccessController.doPrivileged(new PrivilegedAction()
/*  701:     */         {
/*  702:     */           public Object run()
/*  703:     */           {
/*  704: 978 */             return AbstractAutowireCapableBeanFactory.this.getInstantiationStrategy().instantiate(mbd, beanName, parent);
/*  705:     */           }
/*  706: 980 */         }, getAccessControlContext());
/*  707:     */       } else {
/*  708: 983 */         beanInstance = getInstantiationStrategy().instantiate(mbd, beanName, parent);
/*  709:     */       }
/*  710: 985 */       BeanWrapper bw = new BeanWrapperImpl(beanInstance);
/*  711: 986 */       initBeanWrapper(bw);
/*  712: 987 */       return bw;
/*  713:     */     }
/*  714:     */     catch (Throwable ex)
/*  715:     */     {
/*  716: 990 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Instantiation of bean failed", ex);
/*  717:     */     }
/*  718:     */   }
/*  719:     */   
/*  720:     */   protected BeanWrapper instantiateUsingFactoryMethod(String beanName, RootBeanDefinition mbd, Object[] explicitArgs)
/*  721:     */   {
/*  722:1008 */     return new ConstructorResolver(this).instantiateUsingFactoryMethod(beanName, mbd, explicitArgs);
/*  723:     */   }
/*  724:     */   
/*  725:     */   protected BeanWrapper autowireConstructor(String beanName, RootBeanDefinition mbd, Constructor[] ctors, Object[] explicitArgs)
/*  726:     */   {
/*  727:1028 */     return new ConstructorResolver(this).autowireConstructor(beanName, mbd, ctors, explicitArgs);
/*  728:     */   }
/*  729:     */   
/*  730:     */   protected void populateBean(String beanName, AbstractBeanDefinition mbd, BeanWrapper bw)
/*  731:     */   {
/*  732:1039 */     PropertyValues pvs = mbd.getPropertyValues();
/*  733:1041 */     if (bw == null)
/*  734:     */     {
/*  735:1042 */       if (!pvs.isEmpty()) {
/*  736:1043 */         throw new BeanCreationException(
/*  737:1044 */           mbd.getResourceDescription(), beanName, "Cannot apply property values to null instance");
/*  738:     */       }
/*  739:1048 */       return;
/*  740:     */     }
/*  741:1055 */     boolean continueWithPropertyPopulation = true;
/*  742:1057 */     if ((!mbd.isSynthetic()) && (hasInstantiationAwareBeanPostProcessors())) {
/*  743:1058 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/*  744:1059 */         if ((bp instanceof InstantiationAwareBeanPostProcessor))
/*  745:     */         {
/*  746:1060 */           InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor)bp;
/*  747:1061 */           if (!ibp.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName))
/*  748:     */           {
/*  749:1062 */             continueWithPropertyPopulation = false;
/*  750:1063 */             break;
/*  751:     */           }
/*  752:     */         }
/*  753:     */       }
/*  754:     */     }
/*  755:1069 */     if (!continueWithPropertyPopulation) {
/*  756:1070 */       return;
/*  757:     */     }
/*  758:1073 */     if ((mbd.getResolvedAutowireMode() == 1) || 
/*  759:1074 */       (mbd.getResolvedAutowireMode() == 2))
/*  760:     */     {
/*  761:1075 */       MutablePropertyValues newPvs = new MutablePropertyValues(pvs);
/*  762:1078 */       if (mbd.getResolvedAutowireMode() == 1) {
/*  763:1079 */         autowireByName(beanName, mbd, bw, newPvs);
/*  764:     */       }
/*  765:1083 */       if (mbd.getResolvedAutowireMode() == 2) {
/*  766:1084 */         autowireByType(beanName, mbd, bw, newPvs);
/*  767:     */       }
/*  768:1087 */       pvs = newPvs;
/*  769:     */     }
/*  770:1090 */     boolean hasInstAwareBpps = hasInstantiationAwareBeanPostProcessors();
/*  771:1091 */     boolean needsDepCheck = mbd.getDependencyCheck() != 0;
/*  772:1093 */     if ((hasInstAwareBpps) || (needsDepCheck))
/*  773:     */     {
/*  774:1094 */       PropertyDescriptor[] filteredPds = filterPropertyDescriptorsForDependencyCheck(bw);
/*  775:1095 */       if (hasInstAwareBpps) {
/*  776:1096 */         for (BeanPostProcessor bp : getBeanPostProcessors()) {
/*  777:1097 */           if ((bp instanceof InstantiationAwareBeanPostProcessor))
/*  778:     */           {
/*  779:1098 */             InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor)bp;
/*  780:1099 */             pvs = ibp.postProcessPropertyValues(pvs, filteredPds, bw.getWrappedInstance(), beanName);
/*  781:1100 */             if (pvs == null) {
/*  782:1101 */               return;
/*  783:     */             }
/*  784:     */           }
/*  785:     */         }
/*  786:     */       }
/*  787:1106 */       if (needsDepCheck) {
/*  788:1107 */         checkDependencies(beanName, mbd, filteredPds, pvs);
/*  789:     */       }
/*  790:     */     }
/*  791:1111 */     applyPropertyValues(beanName, mbd, bw, pvs);
/*  792:     */   }
/*  793:     */   
/*  794:     */   protected void autowireByName(String beanName, AbstractBeanDefinition mbd, BeanWrapper bw, MutablePropertyValues pvs)
/*  795:     */   {
/*  796:1126 */     String[] propertyNames = unsatisfiedNonSimpleProperties(mbd, bw);
/*  797:1127 */     for (String propertyName : propertyNames) {
/*  798:1128 */       if (containsBean(propertyName))
/*  799:     */       {
/*  800:1129 */         Object bean = getBean(propertyName);
/*  801:1130 */         pvs.add(propertyName, bean);
/*  802:1131 */         registerDependentBean(propertyName, beanName);
/*  803:1132 */         if (this.logger.isDebugEnabled()) {
/*  804:1133 */           this.logger.debug("Added autowiring by name from bean name '" + beanName + 
/*  805:1134 */             "' via property '" + propertyName + "' to bean named '" + propertyName + "'");
/*  806:     */         }
/*  807:     */       }
/*  808:1138 */       else if (this.logger.isTraceEnabled())
/*  809:     */       {
/*  810:1139 */         this.logger.trace("Not autowiring property '" + propertyName + "' of bean '" + beanName + 
/*  811:1140 */           "' by name: no matching bean found");
/*  812:     */       }
/*  813:     */     }
/*  814:     */   }
/*  815:     */   
/*  816:     */   protected void autowireByType(String beanName, AbstractBeanDefinition mbd, BeanWrapper bw, MutablePropertyValues pvs)
/*  817:     */   {
/*  818:1160 */     TypeConverter converter = getCustomTypeConverter();
/*  819:1161 */     if (converter == null) {
/*  820:1162 */       converter = bw;
/*  821:     */     }
/*  822:1165 */     Set<String> autowiredBeanNames = new LinkedHashSet(4);
/*  823:1166 */     String[] propertyNames = unsatisfiedNonSimpleProperties(mbd, bw);
/*  824:1167 */     for (String propertyName : propertyNames) {
/*  825:     */       try
/*  826:     */       {
/*  827:1169 */         PropertyDescriptor pd = bw.getPropertyDescriptor(propertyName);
/*  828:1172 */         if (!Object.class.equals(pd.getPropertyType()))
/*  829:     */         {
/*  830:1173 */           MethodParameter methodParam = BeanUtils.getWriteMethodParameter(pd);
/*  831:     */           
/*  832:1175 */           boolean eager = !PriorityOrdered.class.isAssignableFrom(bw.getWrappedClass());
/*  833:1176 */           DependencyDescriptor desc = new AutowireByTypeDependencyDescriptor(methodParam, eager);
/*  834:1177 */           Object autowiredArgument = resolveDependency(desc, beanName, autowiredBeanNames, converter);
/*  835:1178 */           if (autowiredArgument != null) {
/*  836:1179 */             pvs.add(propertyName, autowiredArgument);
/*  837:     */           }
/*  838:1181 */           for (String autowiredBeanName : autowiredBeanNames)
/*  839:     */           {
/*  840:1182 */             registerDependentBean(autowiredBeanName, beanName);
/*  841:1183 */             if (this.logger.isDebugEnabled()) {
/*  842:1184 */               this.logger.debug("Autowiring by type from bean name '" + beanName + "' via property '" + 
/*  843:1185 */                 propertyName + "' to bean named '" + autowiredBeanName + "'");
/*  844:     */             }
/*  845:     */           }
/*  846:1188 */           autowiredBeanNames.clear();
/*  847:     */         }
/*  848:     */       }
/*  849:     */       catch (BeansException ex)
/*  850:     */       {
/*  851:1192 */         throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, propertyName, ex);
/*  852:     */       }
/*  853:     */     }
/*  854:     */   }
/*  855:     */   
/*  856:     */   protected String[] unsatisfiedNonSimpleProperties(AbstractBeanDefinition mbd, BeanWrapper bw)
/*  857:     */   {
/*  858:1208 */     Set<String> result = new TreeSet();
/*  859:1209 */     PropertyValues pvs = mbd.getPropertyValues();
/*  860:1210 */     PropertyDescriptor[] pds = bw.getPropertyDescriptors();
/*  861:1211 */     for (PropertyDescriptor pd : pds) {
/*  862:1212 */       if ((pd.getWriteMethod() != null) && (!isExcludedFromDependencyCheck(pd)) && (!pvs.contains(pd.getName())) && 
/*  863:1213 */         (!BeanUtils.isSimpleProperty(pd.getPropertyType()))) {
/*  864:1214 */         result.add(pd.getName());
/*  865:     */       }
/*  866:     */     }
/*  867:1217 */     return StringUtils.toStringArray(result);
/*  868:     */   }
/*  869:     */   
/*  870:     */   protected PropertyDescriptor[] filterPropertyDescriptorsForDependencyCheck(BeanWrapper bw)
/*  871:     */   {
/*  872:1229 */     synchronized (this.filteredPropertyDescriptorsCache)
/*  873:     */     {
/*  874:1230 */       PropertyDescriptor[] filtered = (PropertyDescriptor[])this.filteredPropertyDescriptorsCache.get(bw.getWrappedClass());
/*  875:1231 */       if (filtered == null)
/*  876:     */       {
/*  877:1232 */         List<PropertyDescriptor> pds = 
/*  878:1233 */           new LinkedList((Collection)Arrays.asList(bw.getPropertyDescriptors()));
/*  879:1234 */         for (Iterator<PropertyDescriptor> it = pds.iterator(); it.hasNext();)
/*  880:     */         {
/*  881:1235 */           PropertyDescriptor pd = (PropertyDescriptor)it.next();
/*  882:1236 */           if (isExcludedFromDependencyCheck(pd)) {
/*  883:1237 */             it.remove();
/*  884:     */           }
/*  885:     */         }
/*  886:1240 */         filtered = (PropertyDescriptor[])pds.toArray(new PropertyDescriptor[pds.size()]);
/*  887:1241 */         this.filteredPropertyDescriptorsCache.put(bw.getWrappedClass(), filtered);
/*  888:     */       }
/*  889:1243 */       return filtered;
/*  890:     */     }
/*  891:     */   }
/*  892:     */   
/*  893:     */   protected boolean isExcludedFromDependencyCheck(PropertyDescriptor pd)
/*  894:     */   {
/*  895:1260 */     return (AutowireUtils.isExcludedFromDependencyCheck(pd)) || (this.ignoredDependencyTypes.contains(pd.getPropertyType())) || (AutowireUtils.isSetterDefinedInInterface(pd, this.ignoredDependencyInterfaces));
/*  896:     */   }
/*  897:     */   
/*  898:     */   protected void checkDependencies(String beanName, AbstractBeanDefinition mbd, PropertyDescriptor[] pds, PropertyValues pvs)
/*  899:     */     throws UnsatisfiedDependencyException
/*  900:     */   {
/*  901:1277 */     int dependencyCheck = mbd.getDependencyCheck();
/*  902:1278 */     for (PropertyDescriptor pd : pds) {
/*  903:1279 */       if ((pd.getWriteMethod() != null) && (!pvs.contains(pd.getName())))
/*  904:     */       {
/*  905:1280 */         boolean isSimple = BeanUtils.isSimpleProperty(pd.getPropertyType());
/*  906:1281 */         boolean unsatisfied = (dependencyCheck == 3) || 
/*  907:1282 */           ((isSimple) && (dependencyCheck == 2)) || (
/*  908:1283 */           (!isSimple) && (dependencyCheck == 1));
/*  909:1284 */         if (unsatisfied) {
/*  910:1285 */           throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, pd.getName(), 
/*  911:1286 */             "Set this property value or disable dependency checking for this bean.");
/*  912:     */         }
/*  913:     */       }
/*  914:     */     }
/*  915:     */   }
/*  916:     */   
/*  917:     */   protected void applyPropertyValues(String beanName, BeanDefinition mbd, BeanWrapper bw, PropertyValues pvs)
/*  918:     */   {
/*  919:1302 */     if ((pvs == null) || (pvs.isEmpty())) {
/*  920:1303 */       return;
/*  921:     */     }
/*  922:1306 */     MutablePropertyValues mpvs = null;
/*  923:1309 */     if ((System.getSecurityManager() != null) && 
/*  924:1310 */       ((bw instanceof BeanWrapperImpl))) {
/*  925:1311 */       ((BeanWrapperImpl)bw).setSecurityContext(getAccessControlContext());
/*  926:     */     }
/*  927:     */     List<PropertyValue> original;
/*  928:     */     List<PropertyValue> original;
/*  929:1315 */     if ((pvs instanceof MutablePropertyValues))
/*  930:     */     {
/*  931:1316 */       mpvs = (MutablePropertyValues)pvs;
/*  932:1317 */       if (mpvs.isConverted()) {
/*  933:     */         try
/*  934:     */         {
/*  935:1320 */           bw.setPropertyValues(mpvs);
/*  936:1321 */           return;
/*  937:     */         }
/*  938:     */         catch (BeansException ex)
/*  939:     */         {
/*  940:1324 */           throw new BeanCreationException(
/*  941:1325 */             mbd.getResourceDescription(), beanName, "Error setting property values", ex);
/*  942:     */         }
/*  943:     */       }
/*  944:1328 */       original = mpvs.getPropertyValueList();
/*  945:     */     }
/*  946:     */     else
/*  947:     */     {
/*  948:1331 */       original = Arrays.asList(pvs.getPropertyValues());
/*  949:     */     }
/*  950:1334 */     TypeConverter converter = getCustomTypeConverter();
/*  951:1335 */     if (converter == null) {
/*  952:1336 */       converter = bw;
/*  953:     */     }
/*  954:1338 */     BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this, beanName, mbd, converter);
/*  955:     */     
/*  956:     */ 
/*  957:1341 */     List<PropertyValue> deepCopy = new ArrayList(original.size());
/*  958:1342 */     boolean resolveNecessary = false;
/*  959:1343 */     for (PropertyValue pv : original) {
/*  960:1344 */       if (pv.isConverted())
/*  961:     */       {
/*  962:1345 */         deepCopy.add(pv);
/*  963:     */       }
/*  964:     */       else
/*  965:     */       {
/*  966:1348 */         String propertyName = pv.getName();
/*  967:1349 */         Object originalValue = pv.getValue();
/*  968:1350 */         Object resolvedValue = valueResolver.resolveValueIfNecessary(pv, originalValue);
/*  969:1351 */         Object convertedValue = resolvedValue;
/*  970:1352 */         boolean convertible = (bw.isWritableProperty(propertyName)) && 
/*  971:1353 */           (!PropertyAccessorUtils.isNestedOrIndexedProperty(propertyName));
/*  972:1354 */         if (convertible) {
/*  973:1355 */           convertedValue = convertForProperty(resolvedValue, propertyName, bw, converter);
/*  974:     */         }
/*  975:1359 */         if (resolvedValue == originalValue)
/*  976:     */         {
/*  977:1360 */           if (convertible) {
/*  978:1361 */             pv.setConvertedValue(convertedValue);
/*  979:     */           }
/*  980:1363 */           deepCopy.add(pv);
/*  981:     */         }
/*  982:1365 */         else if ((convertible) && ((originalValue instanceof TypedStringValue)) && 
/*  983:1366 */           (!((TypedStringValue)originalValue).isDynamic()) && 
/*  984:1367 */           (!(convertedValue instanceof Collection)) && (!ObjectUtils.isArray(convertedValue)))
/*  985:     */         {
/*  986:1368 */           pv.setConvertedValue(convertedValue);
/*  987:1369 */           deepCopy.add(pv);
/*  988:     */         }
/*  989:     */         else
/*  990:     */         {
/*  991:1372 */           resolveNecessary = true;
/*  992:1373 */           deepCopy.add(new PropertyValue(pv, convertedValue));
/*  993:     */         }
/*  994:     */       }
/*  995:     */     }
/*  996:1377 */     if ((mpvs != null) && (!resolveNecessary)) {
/*  997:1378 */       mpvs.setConverted();
/*  998:     */     }
/*  999:     */     try
/* 1000:     */     {
/* 1001:1383 */       bw.setPropertyValues(new MutablePropertyValues(deepCopy));
/* 1002:     */     }
/* 1003:     */     catch (BeansException ex)
/* 1004:     */     {
/* 1005:1386 */       throw new BeanCreationException(
/* 1006:1387 */         mbd.getResourceDescription(), beanName, "Error setting property values", ex);
/* 1007:     */     }
/* 1008:     */   }
/* 1009:     */   
/* 1010:     */   private Object convertForProperty(Object value, String propertyName, BeanWrapper bw, TypeConverter converter)
/* 1011:     */   {
/* 1012:1395 */     if ((converter instanceof BeanWrapperImpl)) {
/* 1013:1396 */       return ((BeanWrapperImpl)converter).convertForProperty(value, propertyName);
/* 1014:     */     }
/* 1015:1399 */     PropertyDescriptor pd = bw.getPropertyDescriptor(propertyName);
/* 1016:1400 */     MethodParameter methodParam = BeanUtils.getWriteMethodParameter(pd);
/* 1017:1401 */     return converter.convertIfNecessary(value, pd.getPropertyType(), methodParam);
/* 1018:     */   }
/* 1019:     */   
/* 1020:     */   protected Object initializeBean(final String beanName, final Object bean, RootBeanDefinition mbd)
/* 1021:     */   {
/* 1022:1424 */     if (System.getSecurityManager() != null) {
/* 1023:1425 */       AccessController.doPrivileged(new PrivilegedAction()
/* 1024:     */       {
/* 1025:     */         public Object run()
/* 1026:     */         {
/* 1027:1427 */           AbstractAutowireCapableBeanFactory.this.invokeAwareMethods(beanName, bean);
/* 1028:1428 */           return null;
/* 1029:     */         }
/* 1030:1430 */       }, getAccessControlContext());
/* 1031:     */     } else {
/* 1032:1433 */       invokeAwareMethods(beanName, bean);
/* 1033:     */     }
/* 1034:1436 */     Object wrappedBean = bean;
/* 1035:1437 */     if ((mbd == null) || (!mbd.isSynthetic())) {
/* 1036:1438 */       wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
/* 1037:     */     }
/* 1038:     */     try
/* 1039:     */     {
/* 1040:1442 */       invokeInitMethods(beanName, wrappedBean, mbd);
/* 1041:     */     }
/* 1042:     */     catch (Throwable ex)
/* 1043:     */     {
/* 1044:1445 */       throw new BeanCreationException(
/* 1045:1446 */         mbd != null ? mbd.getResourceDescription() : null, 
/* 1046:1447 */         beanName, "Invocation of init method failed", ex);
/* 1047:     */     }
/* 1048:1450 */     if ((mbd == null) || (!mbd.isSynthetic())) {
/* 1049:1451 */       wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
/* 1050:     */     }
/* 1051:1453 */     return wrappedBean;
/* 1052:     */   }
/* 1053:     */   
/* 1054:     */   private void invokeAwareMethods(String beanName, Object bean)
/* 1055:     */   {
/* 1056:1457 */     if ((bean instanceof Aware))
/* 1057:     */     {
/* 1058:1458 */       if ((bean instanceof BeanNameAware)) {
/* 1059:1459 */         ((BeanNameAware)bean).setBeanName(beanName);
/* 1060:     */       }
/* 1061:1461 */       if ((bean instanceof BeanClassLoaderAware)) {
/* 1062:1462 */         ((BeanClassLoaderAware)bean).setBeanClassLoader(getBeanClassLoader());
/* 1063:     */       }
/* 1064:1464 */       if ((bean instanceof BeanFactoryAware)) {
/* 1065:1465 */         ((BeanFactoryAware)bean).setBeanFactory(this);
/* 1066:     */       }
/* 1067:     */     }
/* 1068:     */   }
/* 1069:     */   
/* 1070:     */   protected void invokeInitMethods(String beanName, final Object bean, RootBeanDefinition mbd)
/* 1071:     */     throws Throwable
/* 1072:     */   {
/* 1073:1485 */     boolean isInitializingBean = bean instanceof InitializingBean;
/* 1074:1486 */     if ((isInitializingBean) && ((mbd == null) || (!mbd.isExternallyManagedInitMethod("afterPropertiesSet"))))
/* 1075:     */     {
/* 1076:1487 */       if (this.logger.isDebugEnabled()) {
/* 1077:1488 */         this.logger.debug("Invoking afterPropertiesSet() on bean with name '" + beanName + "'");
/* 1078:     */       }
/* 1079:1490 */       if (System.getSecurityManager() != null) {
/* 1080:     */         try
/* 1081:     */         {
/* 1082:1492 */           AccessController.doPrivileged(new PrivilegedExceptionAction()
/* 1083:     */           {
/* 1084:     */             public Object run()
/* 1085:     */               throws Exception
/* 1086:     */             {
/* 1087:1494 */               ((InitializingBean)bean).afterPropertiesSet();
/* 1088:1495 */               return null;
/* 1089:     */             }
/* 1090:1497 */           }, getAccessControlContext());
/* 1091:     */         }
/* 1092:     */         catch (PrivilegedActionException pae)
/* 1093:     */         {
/* 1094:1500 */           throw pae.getException();
/* 1095:     */         }
/* 1096:     */       } else {
/* 1097:1504 */         ((InitializingBean)bean).afterPropertiesSet();
/* 1098:     */       }
/* 1099:     */     }
/* 1100:1508 */     if (mbd != null)
/* 1101:     */     {
/* 1102:1509 */       String initMethodName = mbd.getInitMethodName();
/* 1103:1510 */       if ((initMethodName != null) && ((!isInitializingBean) || (!"afterPropertiesSet".equals(initMethodName))) && 
/* 1104:1511 */         (!mbd.isExternallyManagedInitMethod(initMethodName))) {
/* 1105:1512 */         invokeCustomInitMethod(beanName, bean, mbd);
/* 1106:     */       }
/* 1107:     */     }
/* 1108:     */   }
/* 1109:     */   
/* 1110:     */   protected void invokeCustomInitMethod(String beanName, final Object bean, RootBeanDefinition mbd)
/* 1111:     */     throws Throwable
/* 1112:     */   {
/* 1113:1525 */     String initMethodName = mbd.getInitMethodName();
/* 1114:1526 */     final Method initMethod = mbd.isNonPublicAccessAllowed() ? 
/* 1115:1527 */       BeanUtils.findMethod(bean.getClass(), initMethodName, new Class[0]) : 
/* 1116:1528 */       ClassUtils.getMethodIfAvailable(bean.getClass(), initMethodName, new Class[0]);
/* 1117:1529 */     if (initMethod == null)
/* 1118:     */     {
/* 1119:1530 */       if (mbd.isEnforceInitMethod()) {
/* 1120:1531 */         throw new BeanDefinitionValidationException("Couldn't find an init method named '" + 
/* 1121:1532 */           initMethodName + "' on bean with name '" + beanName + "'");
/* 1122:     */       }
/* 1123:1535 */       if (this.logger.isDebugEnabled()) {
/* 1124:1536 */         this.logger.debug("No default init method named '" + initMethodName + 
/* 1125:1537 */           "' found on bean with name '" + beanName + "'");
/* 1126:     */       }
/* 1127:1540 */       return;
/* 1128:     */     }
/* 1129:1544 */     if (this.logger.isDebugEnabled()) {
/* 1130:1545 */       this.logger.debug("Invoking init method  '" + initMethodName + "' on bean with name '" + beanName + "'");
/* 1131:     */     }
/* 1132:1548 */     if (System.getSecurityManager() != null)
/* 1133:     */     {
/* 1134:1549 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/* 1135:     */       {
/* 1136:     */         public Object run()
/* 1137:     */           throws Exception
/* 1138:     */         {
/* 1139:1551 */           ReflectionUtils.makeAccessible(initMethod);
/* 1140:1552 */           return null;
/* 1141:     */         }
/* 1142:     */       });
/* 1143:     */       try
/* 1144:     */       {
/* 1145:1556 */         AccessController.doPrivileged(new PrivilegedExceptionAction()
/* 1146:     */         {
/* 1147:     */           public Object run()
/* 1148:     */             throws Exception
/* 1149:     */           {
/* 1150:1558 */             initMethod.invoke(bean, new Object[0]);
/* 1151:1559 */             return null;
/* 1152:     */           }
/* 1153:1561 */         }, getAccessControlContext());
/* 1154:     */       }
/* 1155:     */       catch (PrivilegedActionException pae)
/* 1156:     */       {
/* 1157:1564 */         InvocationTargetException ex = (InvocationTargetException)pae.getException();
/* 1158:1565 */         throw ex.getTargetException();
/* 1159:     */       }
/* 1160:     */     }
/* 1161:     */     else
/* 1162:     */     {
/* 1163:     */       try
/* 1164:     */       {
/* 1165:1570 */         ReflectionUtils.makeAccessible(initMethod);
/* 1166:1571 */         initMethod.invoke(bean, new Object[0]);
/* 1167:     */       }
/* 1168:     */       catch (InvocationTargetException ex)
/* 1169:     */       {
/* 1170:1574 */         throw ex.getTargetException();
/* 1171:     */       }
/* 1172:     */     }
/* 1173:     */   }
/* 1174:     */   
/* 1175:     */   protected Object postProcessObjectFromFactoryBean(Object object, String beanName)
/* 1176:     */   {
/* 1177:1588 */     return applyBeanPostProcessorsAfterInitialization(object, beanName);
/* 1178:     */   }
/* 1179:     */   
/* 1180:     */   protected void removeSingleton(String beanName)
/* 1181:     */   {
/* 1182:1596 */     super.removeSingleton(beanName);
/* 1183:1597 */     this.factoryBeanInstanceCache.remove(beanName);
/* 1184:     */   }
/* 1185:     */   
/* 1186:     */   private static class AutowireByTypeDependencyDescriptor
/* 1187:     */     extends DependencyDescriptor
/* 1188:     */   {
/* 1189:     */     public AutowireByTypeDependencyDescriptor(MethodParameter methodParameter, boolean eager)
/* 1190:     */     {
/* 1191:1608 */       super(false, eager);
/* 1192:     */     }
/* 1193:     */     
/* 1194:     */     public String getDependencyName()
/* 1195:     */     {
/* 1196:1613 */       return null;
/* 1197:     */     }
/* 1198:     */   }
/* 1199:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory
 * JD-Core Version:    0.7.0.1
 */