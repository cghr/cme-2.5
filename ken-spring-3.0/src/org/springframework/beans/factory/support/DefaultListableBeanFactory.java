/*    1:     */ package org.springframework.beans.factory.support;
/*    2:     */ 
/*    3:     */ import java.io.IOException;
/*    4:     */ import java.io.NotSerializableException;
/*    5:     */ import java.io.ObjectInputStream;
/*    6:     */ import java.io.ObjectStreamException;
/*    7:     */ import java.io.Serializable;
/*    8:     */ import java.lang.annotation.Annotation;
/*    9:     */ import java.lang.ref.Reference;
/*   10:     */ import java.lang.ref.WeakReference;
/*   11:     */ import java.lang.reflect.ParameterizedType;
/*   12:     */ import java.lang.reflect.Type;
/*   13:     */ import java.security.AccessController;
/*   14:     */ import java.security.PrivilegedAction;
/*   15:     */ import java.util.ArrayList;
/*   16:     */ import java.util.Arrays;
/*   17:     */ import java.util.Collection;
/*   18:     */ import java.util.HashMap;
/*   19:     */ import java.util.Iterator;
/*   20:     */ import java.util.LinkedHashMap;
/*   21:     */ import java.util.LinkedHashSet;
/*   22:     */ import java.util.List;
/*   23:     */ import java.util.Map;
/*   24:     */ import java.util.Map.Entry;
/*   25:     */ import java.util.Set;
/*   26:     */ import java.util.concurrent.ConcurrentHashMap;
/*   27:     */ import javax.inject.Provider;
/*   28:     */ import org.apache.commons.logging.Log;
/*   29:     */ import org.springframework.beans.BeansException;
/*   30:     */ import org.springframework.beans.FatalBeanException;
/*   31:     */ import org.springframework.beans.TypeConverter;
/*   32:     */ import org.springframework.beans.factory.BeanCreationException;
/*   33:     */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*   34:     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*   35:     */ import org.springframework.beans.factory.BeanFactory;
/*   36:     */ import org.springframework.beans.factory.BeanFactoryAware;
/*   37:     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*   38:     */ import org.springframework.beans.factory.CannotLoadBeanClassException;
/*   39:     */ import org.springframework.beans.factory.FactoryBean;
/*   40:     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*   41:     */ import org.springframework.beans.factory.ObjectFactory;
/*   42:     */ import org.springframework.beans.factory.SmartFactoryBean;
/*   43:     */ import org.springframework.beans.factory.config.BeanDefinition;
/*   44:     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*   45:     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*   46:     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*   47:     */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*   48:     */ import org.springframework.core.annotation.AnnotationUtils;
/*   49:     */ import org.springframework.util.Assert;
/*   50:     */ import org.springframework.util.ObjectUtils;
/*   51:     */ import org.springframework.util.StringUtils;
/*   52:     */ 
/*   53:     */ public class DefaultListableBeanFactory
/*   54:     */   extends AbstractAutowireCapableBeanFactory
/*   55:     */   implements ConfigurableListableBeanFactory, BeanDefinitionRegistry, Serializable
/*   56:     */ {
/*   57: 101 */   private static Class javaxInjectProviderClass = null;
/*   58:     */   
/*   59:     */   static
/*   60:     */   {
/*   61: 104 */     ClassLoader cl = DefaultListableBeanFactory.class.getClassLoader();
/*   62:     */     try
/*   63:     */     {
/*   64: 106 */       javaxInjectProviderClass = cl.loadClass("javax.inject.Provider");
/*   65:     */     }
/*   66:     */     catch (ClassNotFoundException localClassNotFoundException) {}
/*   67:     */   }
/*   68:     */   
/*   69: 116 */   private static final Map<String, Reference<DefaultListableBeanFactory>> serializableFactories = new ConcurrentHashMap();
/*   70:     */   private String serializationId;
/*   71: 122 */   private boolean allowBeanDefinitionOverriding = true;
/*   72: 125 */   private boolean allowEagerClassLoading = true;
/*   73: 128 */   private AutowireCandidateResolver autowireCandidateResolver = new SimpleAutowireCandidateResolver();
/*   74: 131 */   private final Map<Class, Object> resolvableDependencies = new HashMap();
/*   75: 134 */   private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap();
/*   76: 137 */   private final List<String> beanDefinitionNames = new ArrayList();
/*   77: 140 */   private boolean configurationFrozen = false;
/*   78:     */   private String[] frozenBeanDefinitionNames;
/*   79:     */   
/*   80:     */   public DefaultListableBeanFactory() {}
/*   81:     */   
/*   82:     */   public DefaultListableBeanFactory(BeanFactory parentBeanFactory)
/*   83:     */   {
/*   84: 158 */     super(parentBeanFactory);
/*   85:     */   }
/*   86:     */   
/*   87:     */   public void setSerializationId(String serializationId)
/*   88:     */   {
/*   89: 167 */     if (serializationId != null) {
/*   90: 168 */       serializableFactories.put(serializationId, new WeakReference(this));
/*   91: 170 */     } else if (this.serializationId != null) {
/*   92: 171 */       serializableFactories.remove(this.serializationId);
/*   93:     */     }
/*   94: 173 */     this.serializationId = serializationId;
/*   95:     */   }
/*   96:     */   
/*   97:     */   public void setAllowBeanDefinitionOverriding(boolean allowBeanDefinitionOverriding)
/*   98:     */   {
/*   99: 184 */     this.allowBeanDefinitionOverriding = allowBeanDefinitionOverriding;
/*  100:     */   }
/*  101:     */   
/*  102:     */   public void setAllowEagerClassLoading(boolean allowEagerClassLoading)
/*  103:     */   {
/*  104: 198 */     this.allowEagerClassLoading = allowEagerClassLoading;
/*  105:     */   }
/*  106:     */   
/*  107:     */   public void setAutowireCandidateResolver(final AutowireCandidateResolver autowireCandidateResolver)
/*  108:     */   {
/*  109: 207 */     Assert.notNull(autowireCandidateResolver, "AutowireCandidateResolver must not be null");
/*  110: 208 */     if ((autowireCandidateResolver instanceof BeanFactoryAware)) {
/*  111: 209 */       if (System.getSecurityManager() != null)
/*  112:     */       {
/*  113: 210 */         final BeanFactory target = this;
/*  114: 211 */         AccessController.doPrivileged(new PrivilegedAction()
/*  115:     */         {
/*  116:     */           public Object run()
/*  117:     */           {
/*  118: 213 */             ((BeanFactoryAware)autowireCandidateResolver).setBeanFactory(target);
/*  119: 214 */             return null;
/*  120:     */           }
/*  121: 216 */         }, getAccessControlContext());
/*  122:     */       }
/*  123:     */       else
/*  124:     */       {
/*  125: 219 */         ((BeanFactoryAware)autowireCandidateResolver).setBeanFactory(this);
/*  126:     */       }
/*  127:     */     }
/*  128: 222 */     this.autowireCandidateResolver = autowireCandidateResolver;
/*  129:     */   }
/*  130:     */   
/*  131:     */   public AutowireCandidateResolver getAutowireCandidateResolver()
/*  132:     */   {
/*  133: 229 */     return this.autowireCandidateResolver;
/*  134:     */   }
/*  135:     */   
/*  136:     */   public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory)
/*  137:     */   {
/*  138: 235 */     super.copyConfigurationFrom(otherFactory);
/*  139: 236 */     if ((otherFactory instanceof DefaultListableBeanFactory))
/*  140:     */     {
/*  141: 237 */       DefaultListableBeanFactory otherListableFactory = (DefaultListableBeanFactory)otherFactory;
/*  142: 238 */       this.allowBeanDefinitionOverriding = otherListableFactory.allowBeanDefinitionOverriding;
/*  143: 239 */       this.allowEagerClassLoading = otherListableFactory.allowEagerClassLoading;
/*  144: 240 */       this.autowireCandidateResolver = otherListableFactory.autowireCandidateResolver;
/*  145: 241 */       this.resolvableDependencies.putAll(otherListableFactory.resolvableDependencies);
/*  146:     */     }
/*  147:     */   }
/*  148:     */   
/*  149:     */   public <T> T getBean(Class<T> requiredType)
/*  150:     */     throws BeansException
/*  151:     */   {
/*  152: 251 */     Assert.notNull(requiredType, "Required type must not be null");
/*  153: 252 */     String[] beanNames = getBeanNamesForType(requiredType);
/*  154: 253 */     if (beanNames.length > 1)
/*  155:     */     {
/*  156: 254 */       ArrayList<String> autowireCandidates = new ArrayList();
/*  157: 255 */       for (String beanName : beanNames) {
/*  158: 256 */         if (getBeanDefinition(beanName).isAutowireCandidate()) {
/*  159: 257 */           autowireCandidates.add(beanName);
/*  160:     */         }
/*  161:     */       }
/*  162: 260 */       if (autowireCandidates.size() > 0) {
/*  163: 261 */         beanNames = (String[])autowireCandidates.toArray(new String[autowireCandidates.size()]);
/*  164:     */       }
/*  165:     */     }
/*  166: 264 */     if (beanNames.length == 1) {
/*  167: 265 */       return getBean(beanNames[0], requiredType);
/*  168:     */     }
/*  169: 267 */     if ((beanNames.length == 0) && (getParentBeanFactory() != null)) {
/*  170: 268 */       return getParentBeanFactory().getBean(requiredType);
/*  171:     */     }
/*  172: 271 */     throw new NoSuchBeanDefinitionException(requiredType, "expected single bean but found " + 
/*  173: 272 */       beanNames.length + ": " + StringUtils.arrayToCommaDelimitedString(beanNames));
/*  174:     */   }
/*  175:     */   
/*  176:     */   public boolean containsBeanDefinition(String beanName)
/*  177:     */   {
/*  178: 278 */     Assert.notNull(beanName, "Bean name must not be null");
/*  179: 279 */     return this.beanDefinitionMap.containsKey(beanName);
/*  180:     */   }
/*  181:     */   
/*  182:     */   public int getBeanDefinitionCount()
/*  183:     */   {
/*  184: 283 */     return this.beanDefinitionMap.size();
/*  185:     */   }
/*  186:     */   
/*  187:     */   public String[] getBeanDefinitionNames()
/*  188:     */   {
/*  189: 287 */     synchronized (this.beanDefinitionMap)
/*  190:     */     {
/*  191: 288 */       if (this.frozenBeanDefinitionNames != null) {
/*  192: 289 */         return this.frozenBeanDefinitionNames;
/*  193:     */       }
/*  194: 292 */       return StringUtils.toStringArray(this.beanDefinitionNames);
/*  195:     */     }
/*  196:     */   }
/*  197:     */   
/*  198:     */   public String[] getBeanNamesForType(Class type)
/*  199:     */   {
/*  200: 298 */     return getBeanNamesForType(type, true, true);
/*  201:     */   }
/*  202:     */   
/*  203:     */   public String[] getBeanNamesForType(Class type, boolean includeNonSingletons, boolean allowEagerInit)
/*  204:     */   {
/*  205: 302 */     List<String> result = new ArrayList();
/*  206:     */     
/*  207:     */ 
/*  208: 305 */     String[] beanDefinitionNames = getBeanDefinitionNames();
/*  209: 306 */     for (String beanName : beanDefinitionNames) {
/*  210: 309 */       if (!isAlias(beanName)) {
/*  211:     */         try
/*  212:     */         {
/*  213: 311 */           RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*  214: 313 */           if ((!mbd.isAbstract()) && ((allowEagerInit) || (
/*  215: 314 */             ((mbd.hasBeanClass()) || (!mbd.isLazyInit()) || (this.allowEagerClassLoading)) && 
/*  216: 315 */             (!requiresEagerInitForType(mbd.getFactoryBeanName())))))
/*  217:     */           {
/*  218: 317 */             boolean isFactoryBean = isFactoryBean(beanName, mbd);
/*  219: 318 */             boolean matchFound = ((allowEagerInit) || (!isFactoryBean) || (containsSingleton(beanName))) && 
/*  220: 319 */               ((includeNonSingletons) || (isSingleton(beanName))) && (isTypeMatch(beanName, type));
/*  221: 320 */             if ((!matchFound) && (isFactoryBean))
/*  222:     */             {
/*  223: 322 */               beanName = "&" + beanName;
/*  224: 323 */               matchFound = ((includeNonSingletons) || (mbd.isSingleton())) && (isTypeMatch(beanName, type));
/*  225:     */             }
/*  226: 325 */             if (matchFound) {
/*  227: 326 */               result.add(beanName);
/*  228:     */             }
/*  229:     */           }
/*  230:     */         }
/*  231:     */         catch (CannotLoadBeanClassException ex)
/*  232:     */         {
/*  233: 331 */           if (allowEagerInit) {
/*  234: 332 */             throw ex;
/*  235:     */           }
/*  236: 335 */           if (this.logger.isDebugEnabled()) {
/*  237: 336 */             this.logger.debug("Ignoring bean class loading failure for bean '" + beanName + "'", ex);
/*  238:     */           }
/*  239: 338 */           onSuppressedException(ex);
/*  240:     */         }
/*  241:     */         catch (BeanDefinitionStoreException ex)
/*  242:     */         {
/*  243: 341 */           if (allowEagerInit) {
/*  244: 342 */             throw ex;
/*  245:     */           }
/*  246: 345 */           if (this.logger.isDebugEnabled()) {
/*  247: 346 */             this.logger.debug("Ignoring unresolvable metadata in bean definition '" + beanName + "'", ex);
/*  248:     */           }
/*  249: 348 */           onSuppressedException(ex);
/*  250:     */         }
/*  251:     */       }
/*  252:     */     }
/*  253: 354 */     String[] singletonNames = getSingletonNames();
/*  254: 355 */     for (String beanName : singletonNames) {
/*  255: 357 */       if (!containsBeanDefinition(beanName)) {
/*  256: 359 */         if (isFactoryBean(beanName))
/*  257:     */         {
/*  258: 360 */           if (((includeNonSingletons) || (isSingleton(beanName))) && (isTypeMatch(beanName, type))) {
/*  259: 361 */             result.add(beanName);
/*  260:     */           } else {
/*  261: 366 */             beanName = "&" + beanName;
/*  262:     */           }
/*  263:     */         }
/*  264: 369 */         else if (isTypeMatch(beanName, type)) {
/*  265: 370 */           result.add(beanName);
/*  266:     */         }
/*  267:     */       }
/*  268:     */     }
/*  269: 375 */     return StringUtils.toStringArray(result);
/*  270:     */   }
/*  271:     */   
/*  272:     */   private boolean requiresEagerInitForType(String factoryBeanName)
/*  273:     */   {
/*  274: 386 */     return (factoryBeanName != null) && (isFactoryBean(factoryBeanName)) && (!containsSingleton(factoryBeanName));
/*  275:     */   }
/*  276:     */   
/*  277:     */   public <T> Map<String, T> getBeansOfType(Class<T> type)
/*  278:     */     throws BeansException
/*  279:     */   {
/*  280: 390 */     return getBeansOfType(type, true, true);
/*  281:     */   }
/*  282:     */   
/*  283:     */   public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
/*  284:     */     throws BeansException
/*  285:     */   {
/*  286: 396 */     String[] beanNames = getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
/*  287: 397 */     Map<String, T> result = new LinkedHashMap(beanNames.length);
/*  288: 398 */     for (String beanName : beanNames) {
/*  289:     */       try
/*  290:     */       {
/*  291: 400 */         result.put(beanName, getBean(beanName, type));
/*  292:     */       }
/*  293:     */       catch (BeanCreationException ex)
/*  294:     */       {
/*  295: 403 */         Throwable rootCause = ex.getMostSpecificCause();
/*  296: 404 */         if ((rootCause instanceof BeanCurrentlyInCreationException))
/*  297:     */         {
/*  298: 405 */           BeanCreationException bce = (BeanCreationException)rootCause;
/*  299: 406 */           if (isCurrentlyInCreation(bce.getBeanName()))
/*  300:     */           {
/*  301: 407 */             if (this.logger.isDebugEnabled()) {
/*  302: 408 */               this.logger.debug("Ignoring match to currently created bean '" + beanName + "': " + 
/*  303: 409 */                 ex.getMessage());
/*  304:     */             }
/*  305: 411 */             onSuppressedException(ex);
/*  306:     */             
/*  307:     */ 
/*  308: 414 */             continue;
/*  309:     */           }
/*  310:     */         }
/*  311: 417 */         throw ex;
/*  312:     */       }
/*  313:     */     }
/*  314: 420 */     return result;
/*  315:     */   }
/*  316:     */   
/*  317:     */   public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType)
/*  318:     */   {
/*  319: 424 */     Set<String> beanNames = new LinkedHashSet(getBeanDefinitionCount());
/*  320: 425 */     beanNames.addAll((Collection)Arrays.asList(getBeanDefinitionNames()));
/*  321: 426 */     beanNames.addAll((Collection)Arrays.asList(getSingletonNames()));
/*  322: 427 */     Map<String, Object> results = new LinkedHashMap();
/*  323: 428 */     for (String beanName : beanNames) {
/*  324: 429 */       if (findAnnotationOnBean(beanName, annotationType) != null) {
/*  325: 430 */         results.put(beanName, getBean(beanName));
/*  326:     */       }
/*  327:     */     }
/*  328: 433 */     return results;
/*  329:     */   }
/*  330:     */   
/*  331:     */   public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType)
/*  332:     */   {
/*  333: 443 */     A ann = null;
/*  334: 444 */     Class beanType = getType(beanName);
/*  335: 445 */     if (beanType != null) {
/*  336: 446 */       ann = AnnotationUtils.findAnnotation(beanType, annotationType);
/*  337:     */     }
/*  338: 448 */     if ((ann == null) && (containsBeanDefinition(beanName)))
/*  339:     */     {
/*  340: 449 */       BeanDefinition bd = getMergedBeanDefinition(beanName);
/*  341: 450 */       if ((bd instanceof AbstractBeanDefinition))
/*  342:     */       {
/*  343: 451 */         AbstractBeanDefinition abd = (AbstractBeanDefinition)bd;
/*  344: 452 */         if (abd.hasBeanClass()) {
/*  345: 453 */           ann = AnnotationUtils.findAnnotation(abd.getBeanClass(), annotationType);
/*  346:     */         }
/*  347:     */       }
/*  348:     */     }
/*  349: 457 */     return ann;
/*  350:     */   }
/*  351:     */   
/*  352:     */   public void registerResolvableDependency(Class<?> dependencyType, Object autowiredValue)
/*  353:     */   {
/*  354: 466 */     Assert.notNull(dependencyType, "Type must not be null");
/*  355: 467 */     if (autowiredValue != null)
/*  356:     */     {
/*  357: 468 */       Assert.isTrue(((autowiredValue instanceof ObjectFactory)) || (dependencyType.isInstance(autowiredValue)), 
/*  358: 469 */         "Value [" + autowiredValue + "] does not implement specified type [" + dependencyType.getName() + "]");
/*  359: 470 */       this.resolvableDependencies.put(dependencyType, autowiredValue);
/*  360:     */     }
/*  361:     */   }
/*  362:     */   
/*  363:     */   public boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor)
/*  364:     */     throws NoSuchBeanDefinitionException
/*  365:     */   {
/*  366: 478 */     boolean isFactoryBean = (descriptor != null) && (descriptor.getDependencyType() != null) && 
/*  367: 479 */       (FactoryBean.class.isAssignableFrom(descriptor.getDependencyType()));
/*  368: 480 */     if (isFactoryBean) {
/*  369: 481 */       beanName = BeanFactoryUtils.transformedBeanName(beanName);
/*  370:     */     }
/*  371: 484 */     if (containsBeanDefinition(beanName)) {
/*  372: 485 */       return isAutowireCandidate(beanName, getMergedLocalBeanDefinition(beanName), descriptor);
/*  373:     */     }
/*  374: 487 */     if (containsSingleton(beanName)) {
/*  375: 488 */       return isAutowireCandidate(beanName, new RootBeanDefinition(getType(beanName)), descriptor);
/*  376:     */     }
/*  377: 490 */     if ((getParentBeanFactory() instanceof ConfigurableListableBeanFactory)) {
/*  378: 492 */       return ((ConfigurableListableBeanFactory)getParentBeanFactory()).isAutowireCandidate(beanName, descriptor);
/*  379:     */     }
/*  380: 495 */     return true;
/*  381:     */   }
/*  382:     */   
/*  383:     */   protected boolean isAutowireCandidate(String beanName, RootBeanDefinition mbd, DependencyDescriptor descriptor)
/*  384:     */   {
/*  385: 508 */     resolveBeanClass(mbd, beanName, new Class[0]);
/*  386: 509 */     if (mbd.isFactoryMethodUnique)
/*  387:     */     {
/*  388:     */       boolean resolve;
/*  389: 511 */       synchronized (mbd.constructorArgumentLock)
/*  390:     */       {
/*  391: 512 */         resolve = mbd.resolvedConstructorOrFactoryMethod == null;
/*  392:     */       }
/*  393:     */       boolean resolve;
/*  394: 514 */       if (resolve) {
/*  395: 515 */         new ConstructorResolver(this).resolveFactoryMethodIfPossible(mbd);
/*  396:     */       }
/*  397:     */     }
/*  398: 518 */     return getAutowireCandidateResolver().isAutowireCandidate(
/*  399: 519 */       new BeanDefinitionHolder(mbd, beanName, getAliases(beanName)), descriptor);
/*  400:     */   }
/*  401:     */   
/*  402:     */   public BeanDefinition getBeanDefinition(String beanName)
/*  403:     */     throws NoSuchBeanDefinitionException
/*  404:     */   {
/*  405: 524 */     BeanDefinition bd = (BeanDefinition)this.beanDefinitionMap.get(beanName);
/*  406: 525 */     if (bd == null)
/*  407:     */     {
/*  408: 526 */       if (this.logger.isTraceEnabled()) {
/*  409: 527 */         this.logger.trace("No bean named '" + beanName + "' found in " + this);
/*  410:     */       }
/*  411: 529 */       throw new NoSuchBeanDefinitionException(beanName);
/*  412:     */     }
/*  413: 531 */     return bd;
/*  414:     */   }
/*  415:     */   
/*  416:     */   public void freezeConfiguration()
/*  417:     */   {
/*  418: 535 */     this.configurationFrozen = true;
/*  419: 536 */     synchronized (this.beanDefinitionMap)
/*  420:     */     {
/*  421: 537 */       this.frozenBeanDefinitionNames = StringUtils.toStringArray(this.beanDefinitionNames);
/*  422:     */     }
/*  423:     */   }
/*  424:     */   
/*  425:     */   public boolean isConfigurationFrozen()
/*  426:     */   {
/*  427: 542 */     return this.configurationFrozen;
/*  428:     */   }
/*  429:     */   
/*  430:     */   protected boolean isBeanEligibleForMetadataCaching(String beanName)
/*  431:     */   {
/*  432: 552 */     return (this.configurationFrozen) || (super.isBeanEligibleForMetadataCaching(beanName));
/*  433:     */   }
/*  434:     */   
/*  435:     */   public void preInstantiateSingletons()
/*  436:     */     throws BeansException
/*  437:     */   {
/*  438: 556 */     if (this.logger.isInfoEnabled()) {
/*  439: 557 */       this.logger.info("Pre-instantiating singletons in " + this);
/*  440:     */     }
/*  441: 559 */     synchronized (this.beanDefinitionMap)
/*  442:     */     {
/*  443: 562 */       List<String> beanNames = new ArrayList(this.beanDefinitionNames);
/*  444: 563 */       for (String beanName : beanNames)
/*  445:     */       {
/*  446: 564 */         RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
/*  447: 565 */         if ((!bd.isAbstract()) && (bd.isSingleton()) && (!bd.isLazyInit())) {
/*  448: 566 */           if (isFactoryBean(beanName))
/*  449:     */           {
/*  450: 567 */             final FactoryBean factory = (FactoryBean)getBean("&" + beanName);
/*  451:     */             boolean isEagerInit;
/*  452:     */             boolean isEagerInit;
/*  453: 569 */             if ((System.getSecurityManager() != null) && ((factory instanceof SmartFactoryBean))) {
/*  454: 570 */               isEagerInit = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*  455:     */               {
/*  456:     */                 public Boolean run()
/*  457:     */                 {
/*  458: 572 */                   return Boolean.valueOf(((SmartFactoryBean)factory).isEagerInit());
/*  459:     */                 }
/*  460: 574 */               }, getAccessControlContext())).booleanValue();
/*  461:     */             } else {
/*  462: 577 */               isEagerInit = ((factory instanceof SmartFactoryBean)) && 
/*  463: 578 */                 (((SmartFactoryBean)factory).isEagerInit());
/*  464:     */             }
/*  465: 580 */             if (isEagerInit) {
/*  466: 581 */               getBean(beanName);
/*  467:     */             }
/*  468:     */           }
/*  469:     */           else
/*  470:     */           {
/*  471: 585 */             getBean(beanName);
/*  472:     */           }
/*  473:     */         }
/*  474:     */       }
/*  475:     */     }
/*  476:     */   }
/*  477:     */   
/*  478:     */   public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
/*  479:     */     throws BeanDefinitionStoreException
/*  480:     */   {
/*  481: 600 */     Assert.hasText(beanName, "Bean name must not be empty");
/*  482: 601 */     Assert.notNull(beanDefinition, "BeanDefinition must not be null");
/*  483: 603 */     if ((beanDefinition instanceof AbstractBeanDefinition)) {
/*  484:     */       try
/*  485:     */       {
/*  486: 605 */         ((AbstractBeanDefinition)beanDefinition).validate();
/*  487:     */       }
/*  488:     */       catch (BeanDefinitionValidationException ex)
/*  489:     */       {
/*  490: 608 */         throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName, 
/*  491: 609 */           "Validation of bean definition failed", ex);
/*  492:     */       }
/*  493:     */     }
/*  494: 613 */     synchronized (this.beanDefinitionMap)
/*  495:     */     {
/*  496: 614 */       Object oldBeanDefinition = this.beanDefinitionMap.get(beanName);
/*  497: 615 */       if (oldBeanDefinition != null)
/*  498:     */       {
/*  499: 616 */         if (!this.allowBeanDefinitionOverriding) {
/*  500: 617 */           throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName, 
/*  501: 618 */             "Cannot register bean definition [" + beanDefinition + "] for bean '" + beanName + 
/*  502: 619 */             "': There is already [" + oldBeanDefinition + "] bound.");
/*  503:     */         }
/*  504: 622 */         if (this.logger.isInfoEnabled()) {
/*  505: 623 */           this.logger.info("Overriding bean definition for bean '" + beanName + 
/*  506: 624 */             "': replacing [" + oldBeanDefinition + "] with [" + beanDefinition + "]");
/*  507:     */         }
/*  508:     */       }
/*  509:     */       else
/*  510:     */       {
/*  511: 629 */         this.beanDefinitionNames.add(beanName);
/*  512: 630 */         this.frozenBeanDefinitionNames = null;
/*  513:     */       }
/*  514: 632 */       this.beanDefinitionMap.put(beanName, beanDefinition);
/*  515:     */       
/*  516: 634 */       resetBeanDefinition(beanName);
/*  517:     */     }
/*  518:     */   }
/*  519:     */   
/*  520:     */   public void removeBeanDefinition(String beanName)
/*  521:     */     throws NoSuchBeanDefinitionException
/*  522:     */   {
/*  523: 639 */     Assert.hasText(beanName, "'beanName' must not be empty");
/*  524: 641 */     synchronized (this.beanDefinitionMap)
/*  525:     */     {
/*  526: 642 */       BeanDefinition bd = (BeanDefinition)this.beanDefinitionMap.remove(beanName);
/*  527: 643 */       if (bd == null)
/*  528:     */       {
/*  529: 644 */         if (this.logger.isTraceEnabled()) {
/*  530: 645 */           this.logger.trace("No bean named '" + beanName + "' found in " + this);
/*  531:     */         }
/*  532: 647 */         throw new NoSuchBeanDefinitionException(beanName);
/*  533:     */       }
/*  534: 649 */       this.beanDefinitionNames.remove(beanName);
/*  535: 650 */       this.frozenBeanDefinitionNames = null;
/*  536:     */       
/*  537: 652 */       resetBeanDefinition(beanName);
/*  538:     */     }
/*  539:     */   }
/*  540:     */   
/*  541:     */   protected void resetBeanDefinition(String beanName)
/*  542:     */   {
/*  543: 663 */     clearMergedBeanDefinition(beanName);
/*  544: 668 */     synchronized (getSingletonMutex())
/*  545:     */     {
/*  546: 669 */       destroySingleton(beanName);
/*  547:     */     }
/*  548: 673 */     for (String bdName : this.beanDefinitionNames) {
/*  549: 674 */       if (!beanName.equals(bdName))
/*  550:     */       {
/*  551: 675 */         BeanDefinition bd = (BeanDefinition)this.beanDefinitionMap.get(bdName);
/*  552: 676 */         if (beanName.equals(bd.getParentName())) {
/*  553: 677 */           resetBeanDefinition(bdName);
/*  554:     */         }
/*  555:     */       }
/*  556:     */     }
/*  557:     */   }
/*  558:     */   
/*  559:     */   protected boolean allowAliasOverriding()
/*  560:     */   {
/*  561: 688 */     return this.allowBeanDefinitionOverriding;
/*  562:     */   }
/*  563:     */   
/*  564:     */   public Object resolveDependency(DependencyDescriptor descriptor, String beanName, Set<String> autowiredBeanNames, TypeConverter typeConverter)
/*  565:     */     throws BeansException
/*  566:     */   {
/*  567: 699 */     descriptor.initParameterNameDiscovery(getParameterNameDiscoverer());
/*  568: 700 */     if (descriptor.getDependencyType().equals(ObjectFactory.class)) {
/*  569: 701 */       return new DependencyObjectFactory(descriptor, beanName);
/*  570:     */     }
/*  571: 703 */     if (descriptor.getDependencyType().equals(javaxInjectProviderClass)) {
/*  572: 704 */       return new DependencyProviderFactory(null).createDependencyProvider(descriptor, beanName);
/*  573:     */     }
/*  574: 707 */     return doResolveDependency(descriptor, descriptor.getDependencyType(), beanName, autowiredBeanNames, typeConverter);
/*  575:     */   }
/*  576:     */   
/*  577:     */   protected Object doResolveDependency(DependencyDescriptor descriptor, Class<?> type, String beanName, Set<String> autowiredBeanNames, TypeConverter typeConverter)
/*  578:     */     throws BeansException
/*  579:     */   {
/*  580: 714 */     Object value = getAutowireCandidateResolver().getSuggestedValue(descriptor);
/*  581: 715 */     if (value != null)
/*  582:     */     {
/*  583: 716 */       if ((value instanceof String))
/*  584:     */       {
/*  585: 717 */         String strVal = resolveEmbeddedValue((String)value);
/*  586: 718 */         BeanDefinition bd = (beanName != null) && (containsBean(beanName)) ? getMergedBeanDefinition(beanName) : null;
/*  587: 719 */         value = evaluateBeanDefinitionString(strVal, bd);
/*  588:     */       }
/*  589: 721 */       TypeConverter converter = typeConverter != null ? typeConverter : getTypeConverter();
/*  590: 722 */       return converter.convertIfNecessary(value, type);
/*  591:     */     }
/*  592: 725 */     if (type.isArray())
/*  593:     */     {
/*  594: 726 */       Class componentType = type.getComponentType();
/*  595: 727 */       Map<String, Object> matchingBeans = findAutowireCandidates(beanName, componentType, descriptor);
/*  596: 728 */       if (matchingBeans.isEmpty())
/*  597:     */       {
/*  598: 729 */         if (descriptor.isRequired()) {
/*  599: 730 */           raiseNoSuchBeanDefinitionException(componentType, "array of " + componentType.getName(), descriptor);
/*  600:     */         }
/*  601: 732 */         return null;
/*  602:     */       }
/*  603: 734 */       if (autowiredBeanNames != null) {
/*  604: 735 */         autowiredBeanNames.addAll((Collection)matchingBeans.keySet());
/*  605:     */       }
/*  606: 737 */       TypeConverter converter = typeConverter != null ? typeConverter : getTypeConverter();
/*  607: 738 */       return converter.convertIfNecessary(matchingBeans.values(), type);
/*  608:     */     }
/*  609: 740 */     if ((Collection.class.isAssignableFrom(type)) && (type.isInterface()))
/*  610:     */     {
/*  611: 741 */       Class elementType = descriptor.getCollectionType();
/*  612: 742 */       if (elementType == null)
/*  613:     */       {
/*  614: 743 */         if (descriptor.isRequired()) {
/*  615: 744 */           throw new FatalBeanException("No element type declared for collection [" + type.getName() + "]");
/*  616:     */         }
/*  617: 746 */         return null;
/*  618:     */       }
/*  619: 748 */       Map<String, Object> matchingBeans = findAutowireCandidates(beanName, elementType, descriptor);
/*  620: 749 */       if (matchingBeans.isEmpty())
/*  621:     */       {
/*  622: 750 */         if (descriptor.isRequired()) {
/*  623: 751 */           raiseNoSuchBeanDefinitionException(elementType, "collection of " + elementType.getName(), descriptor);
/*  624:     */         }
/*  625: 753 */         return null;
/*  626:     */       }
/*  627: 755 */       if (autowiredBeanNames != null) {
/*  628: 756 */         autowiredBeanNames.addAll((Collection)matchingBeans.keySet());
/*  629:     */       }
/*  630: 758 */       TypeConverter converter = typeConverter != null ? typeConverter : getTypeConverter();
/*  631: 759 */       return converter.convertIfNecessary(matchingBeans.values(), type);
/*  632:     */     }
/*  633: 761 */     if ((Map.class.isAssignableFrom(type)) && (type.isInterface()))
/*  634:     */     {
/*  635: 762 */       Class keyType = descriptor.getMapKeyType();
/*  636: 763 */       if ((keyType == null) || (!String.class.isAssignableFrom(keyType)))
/*  637:     */       {
/*  638: 764 */         if (descriptor.isRequired()) {
/*  639: 765 */           throw new FatalBeanException("Key type [" + keyType + "] of map [" + type.getName() + 
/*  640: 766 */             "] must be assignable to [java.lang.String]");
/*  641:     */         }
/*  642: 768 */         return null;
/*  643:     */       }
/*  644: 770 */       Class valueType = descriptor.getMapValueType();
/*  645: 771 */       if (valueType == null)
/*  646:     */       {
/*  647: 772 */         if (descriptor.isRequired()) {
/*  648: 773 */           throw new FatalBeanException("No value type declared for map [" + type.getName() + "]");
/*  649:     */         }
/*  650: 775 */         return null;
/*  651:     */       }
/*  652: 777 */       Map<String, Object> matchingBeans = findAutowireCandidates(beanName, valueType, descriptor);
/*  653: 778 */       if (matchingBeans.isEmpty())
/*  654:     */       {
/*  655: 779 */         if (descriptor.isRequired()) {
/*  656: 780 */           raiseNoSuchBeanDefinitionException(valueType, "map with value type " + valueType.getName(), descriptor);
/*  657:     */         }
/*  658: 782 */         return null;
/*  659:     */       }
/*  660: 784 */       if (autowiredBeanNames != null) {
/*  661: 785 */         autowiredBeanNames.addAll((Collection)matchingBeans.keySet());
/*  662:     */       }
/*  663: 787 */       return matchingBeans;
/*  664:     */     }
/*  665: 790 */     Map<String, Object> matchingBeans = findAutowireCandidates(beanName, type, descriptor);
/*  666: 791 */     if (matchingBeans.isEmpty())
/*  667:     */     {
/*  668: 792 */       if (descriptor.isRequired()) {
/*  669: 793 */         raiseNoSuchBeanDefinitionException(type, "", descriptor);
/*  670:     */       }
/*  671: 795 */       return null;
/*  672:     */     }
/*  673: 797 */     if (matchingBeans.size() > 1)
/*  674:     */     {
/*  675: 798 */       String primaryBeanName = determinePrimaryCandidate(matchingBeans, descriptor);
/*  676: 799 */       if (primaryBeanName == null) {
/*  677: 800 */         throw new NoSuchBeanDefinitionException(type, "expected single matching bean but found " + 
/*  678: 801 */           matchingBeans.size() + ": " + matchingBeans.keySet());
/*  679:     */       }
/*  680: 803 */       if (autowiredBeanNames != null) {
/*  681: 804 */         autowiredBeanNames.add(primaryBeanName);
/*  682:     */       }
/*  683: 806 */       return matchingBeans.get(primaryBeanName);
/*  684:     */     }
/*  685: 809 */     Map.Entry<String, Object> entry = (Map.Entry)matchingBeans.entrySet().iterator().next();
/*  686: 810 */     if (autowiredBeanNames != null) {
/*  687: 811 */       autowiredBeanNames.add((String)entry.getKey());
/*  688:     */     }
/*  689: 813 */     return entry.getValue();
/*  690:     */   }
/*  691:     */   
/*  692:     */   protected Map<String, Object> findAutowireCandidates(String beanName, Class requiredType, DependencyDescriptor descriptor)
/*  693:     */   {
/*  694: 833 */     String[] candidateNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
/*  695: 834 */       this, requiredType, true, descriptor.isEager());
/*  696: 835 */     Map<String, Object> result = new LinkedHashMap(candidateNames.length);
/*  697: 836 */     for (Class autowiringType : this.resolvableDependencies.keySet()) {
/*  698: 837 */       if (autowiringType.isAssignableFrom(requiredType))
/*  699:     */       {
/*  700: 838 */         autowiringValue = this.resolvableDependencies.get(autowiringType);
/*  701: 839 */         autowiringValue = AutowireUtils.resolveAutowiringValue(autowiringValue, requiredType);
/*  702: 840 */         if (requiredType.isInstance(autowiringValue))
/*  703:     */         {
/*  704: 841 */           result.put(ObjectUtils.identityToString(autowiringValue), autowiringValue);
/*  705: 842 */           break;
/*  706:     */         }
/*  707:     */       }
/*  708:     */     }
/*  709:     */     String[] arrayOfString1;
/*  710: 846 */     Object autowiringValue = (arrayOfString1 = candidateNames).length;
/*  711: 846 */     for (Object localObject1 = 0; localObject1 < autowiringValue; localObject1++)
/*  712:     */     {
/*  713: 846 */       String candidateName = arrayOfString1[localObject1];
/*  714: 847 */       if ((!candidateName.equals(beanName)) && (isAutowireCandidate(candidateName, descriptor))) {
/*  715: 848 */         result.put(candidateName, getBean(candidateName));
/*  716:     */       }
/*  717:     */     }
/*  718: 851 */     return result;
/*  719:     */   }
/*  720:     */   
/*  721:     */   protected String determinePrimaryCandidate(Map<String, Object> candidateBeans, DependencyDescriptor descriptor)
/*  722:     */   {
/*  723: 862 */     String primaryBeanName = null;
/*  724: 863 */     String fallbackBeanName = null;
/*  725: 864 */     for (Map.Entry<String, Object> entry : candidateBeans.entrySet())
/*  726:     */     {
/*  727: 865 */       String candidateBeanName = (String)entry.getKey();
/*  728: 866 */       Object beanInstance = entry.getValue();
/*  729: 867 */       if (isPrimary(candidateBeanName, beanInstance)) {
/*  730: 868 */         if (primaryBeanName != null)
/*  731:     */         {
/*  732: 869 */           boolean candidateLocal = containsBeanDefinition(candidateBeanName);
/*  733: 870 */           boolean primaryLocal = containsBeanDefinition(primaryBeanName);
/*  734: 871 */           if (candidateLocal == primaryLocal) {
/*  735: 872 */             throw new NoSuchBeanDefinitionException(descriptor.getDependencyType(), 
/*  736: 873 */               "more than one 'primary' bean found among candidates: " + candidateBeans.keySet());
/*  737:     */           }
/*  738: 875 */           if ((candidateLocal) && (!primaryLocal)) {
/*  739: 876 */             primaryBeanName = candidateBeanName;
/*  740:     */           }
/*  741:     */         }
/*  742:     */         else
/*  743:     */         {
/*  744: 880 */           primaryBeanName = candidateBeanName;
/*  745:     */         }
/*  746:     */       }
/*  747: 883 */       if ((primaryBeanName == null) && (
/*  748: 884 */         (this.resolvableDependencies.values().contains(beanInstance)) || 
/*  749: 885 */         (matchesBeanName(candidateBeanName, descriptor.getDependencyName())))) {
/*  750: 886 */         fallbackBeanName = candidateBeanName;
/*  751:     */       }
/*  752:     */     }
/*  753: 889 */     return primaryBeanName != null ? primaryBeanName : fallbackBeanName;
/*  754:     */   }
/*  755:     */   
/*  756:     */   protected boolean isPrimary(String beanName, Object beanInstance)
/*  757:     */   {
/*  758: 900 */     if (containsBeanDefinition(beanName)) {
/*  759: 901 */       return getMergedLocalBeanDefinition(beanName).isPrimary();
/*  760:     */     }
/*  761: 903 */     BeanFactory parentFactory = getParentBeanFactory();
/*  762:     */     
/*  763: 905 */     return ((parentFactory instanceof DefaultListableBeanFactory)) && (((DefaultListableBeanFactory)parentFactory).isPrimary(beanName, beanInstance));
/*  764:     */   }
/*  765:     */   
/*  766:     */   protected boolean matchesBeanName(String beanName, String candidateName)
/*  767:     */   {
/*  768: 914 */     return (candidateName != null) && ((candidateName.equals(beanName)) || (ObjectUtils.containsElement(getAliases(beanName), candidateName)));
/*  769:     */   }
/*  770:     */   
/*  771:     */   private void raiseNoSuchBeanDefinitionException(Class type, String dependencyDescription, DependencyDescriptor descriptor)
/*  772:     */     throws NoSuchBeanDefinitionException
/*  773:     */   {
/*  774: 924 */     throw new NoSuchBeanDefinitionException(type, dependencyDescription, 
/*  775: 925 */       "expected at least 1 bean which qualifies as autowire candidate for this dependency. Dependency annotations: " + 
/*  776: 926 */       ObjectUtils.nullSafeToString(descriptor.getAnnotations()));
/*  777:     */   }
/*  778:     */   
/*  779:     */   public String toString()
/*  780:     */   {
/*  781: 932 */     StringBuilder sb = new StringBuilder(ObjectUtils.identityToString(this));
/*  782: 933 */     sb.append(": defining beans [");
/*  783: 934 */     sb.append(StringUtils.arrayToCommaDelimitedString(getBeanDefinitionNames()));
/*  784: 935 */     sb.append("]; ");
/*  785: 936 */     BeanFactory parent = getParentBeanFactory();
/*  786: 937 */     if (parent == null) {
/*  787: 938 */       sb.append("root of factory hierarchy");
/*  788:     */     } else {
/*  789: 941 */       sb.append("parent: ").append(ObjectUtils.identityToString(parent));
/*  790:     */     }
/*  791: 943 */     return sb.toString();
/*  792:     */   }
/*  793:     */   
/*  794:     */   private void readObject(ObjectInputStream ois)
/*  795:     */     throws IOException, ClassNotFoundException
/*  796:     */   {
/*  797: 952 */     throw new NotSerializableException("DefaultListableBeanFactory itself is not deserializable - just a SerializedBeanFactoryReference is");
/*  798:     */   }
/*  799:     */   
/*  800:     */   protected Object writeReplace()
/*  801:     */     throws ObjectStreamException
/*  802:     */   {
/*  803: 957 */     if (this.serializationId != null) {
/*  804: 958 */       return new SerializedBeanFactoryReference(this.serializationId);
/*  805:     */     }
/*  806: 961 */     throw new NotSerializableException("DefaultListableBeanFactory has no serialization id");
/*  807:     */   }
/*  808:     */   
/*  809:     */   private static class SerializedBeanFactoryReference
/*  810:     */     implements Serializable
/*  811:     */   {
/*  812:     */     private final String id;
/*  813:     */     
/*  814:     */     public SerializedBeanFactoryReference(String id)
/*  815:     */     {
/*  816: 975 */       this.id = id;
/*  817:     */     }
/*  818:     */     
/*  819:     */     private Object readResolve()
/*  820:     */     {
/*  821: 979 */       Reference ref = (Reference)DefaultListableBeanFactory.serializableFactories.get(this.id);
/*  822: 980 */       if (ref == null) {
/*  823: 981 */         throw new IllegalStateException(
/*  824: 982 */           "Cannot deserialize BeanFactory with id " + this.id + ": no factory registered for this id");
/*  825:     */       }
/*  826: 984 */       Object result = ref.get();
/*  827: 985 */       if (result == null) {
/*  828: 986 */         throw new IllegalStateException(
/*  829: 987 */           "Cannot deserialize BeanFactory with id " + this.id + ": factory has been garbage-collected");
/*  830:     */       }
/*  831: 989 */       return result;
/*  832:     */     }
/*  833:     */   }
/*  834:     */   
/*  835:     */   private class DependencyObjectFactory
/*  836:     */     implements ObjectFactory, Serializable
/*  837:     */   {
/*  838:     */     private final DependencyDescriptor descriptor;
/*  839:     */     private final String beanName;
/*  840:     */     private final Class type;
/*  841:     */     
/*  842:     */     public DependencyObjectFactory(DependencyDescriptor descriptor, String beanName)
/*  843:     */     {
/*  844:1006 */       this.descriptor = descriptor;
/*  845:1007 */       this.beanName = beanName;
/*  846:1008 */       this.type = determineObjectFactoryType();
/*  847:     */     }
/*  848:     */     
/*  849:     */     private Class determineObjectFactoryType()
/*  850:     */     {
/*  851:1012 */       Type type = this.descriptor.getGenericDependencyType();
/*  852:1013 */       if ((type instanceof ParameterizedType))
/*  853:     */       {
/*  854:1014 */         Type arg = ((ParameterizedType)type).getActualTypeArguments()[0];
/*  855:1015 */         if ((arg instanceof Class)) {
/*  856:1016 */           return (Class)arg;
/*  857:     */         }
/*  858:     */       }
/*  859:1019 */       return Object.class;
/*  860:     */     }
/*  861:     */     
/*  862:     */     public Object getObject()
/*  863:     */       throws BeansException
/*  864:     */     {
/*  865:1023 */       return DefaultListableBeanFactory.this.doResolveDependency(this.descriptor, this.type, this.beanName, null, null);
/*  866:     */     }
/*  867:     */   }
/*  868:     */   
/*  869:     */   private class DependencyProvider
/*  870:     */     extends DefaultListableBeanFactory.DependencyObjectFactory
/*  871:     */     implements Provider
/*  872:     */   {
/*  873:     */     public DependencyProvider(DependencyDescriptor descriptor, String beanName)
/*  874:     */     {
/*  875:1034 */       super(descriptor, beanName);
/*  876:     */     }
/*  877:     */     
/*  878:     */     public Object get()
/*  879:     */       throws BeansException
/*  880:     */     {
/*  881:1038 */       return getObject();
/*  882:     */     }
/*  883:     */   }
/*  884:     */   
/*  885:     */   private class DependencyProviderFactory
/*  886:     */   {
/*  887:     */     private DependencyProviderFactory() {}
/*  888:     */     
/*  889:     */     public Object createDependencyProvider(DependencyDescriptor descriptor, String beanName)
/*  890:     */     {
/*  891:1049 */       return new DefaultListableBeanFactory.DependencyProvider(DefaultListableBeanFactory.this, descriptor, beanName);
/*  892:     */     }
/*  893:     */   }
/*  894:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.DefaultListableBeanFactory
 * JD-Core Version:    0.7.0.1
 */