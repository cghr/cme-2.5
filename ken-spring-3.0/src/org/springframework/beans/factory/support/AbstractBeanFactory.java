/*    1:     */ package org.springframework.beans.factory.support;
/*    2:     */ 
/*    3:     */ import java.beans.PropertyEditor;
/*    4:     */ import java.security.AccessControlContext;
/*    5:     */ import java.security.AccessController;
/*    6:     */ import java.security.PrivilegedAction;
/*    7:     */ import java.security.PrivilegedActionException;
/*    8:     */ import java.security.PrivilegedExceptionAction;
/*    9:     */ import java.util.ArrayList;
/*   10:     */ import java.util.Arrays;
/*   11:     */ import java.util.Collection;
/*   12:     */ import java.util.Collections;
/*   13:     */ import java.util.HashMap;
/*   14:     */ import java.util.HashSet;
/*   15:     */ import java.util.LinkedHashSet;
/*   16:     */ import java.util.LinkedList;
/*   17:     */ import java.util.List;
/*   18:     */ import java.util.Map;
/*   19:     */ import java.util.Map.Entry;
/*   20:     */ import java.util.Set;
/*   21:     */ import java.util.concurrent.ConcurrentHashMap;
/*   22:     */ import org.apache.commons.logging.Log;
/*   23:     */ import org.springframework.beans.BeanUtils;
/*   24:     */ import org.springframework.beans.BeanWrapper;
/*   25:     */ import org.springframework.beans.BeansException;
/*   26:     */ import org.springframework.beans.PropertyEditorRegistrar;
/*   27:     */ import org.springframework.beans.PropertyEditorRegistry;
/*   28:     */ import org.springframework.beans.PropertyEditorRegistrySupport;
/*   29:     */ import org.springframework.beans.SimpleTypeConverter;
/*   30:     */ import org.springframework.beans.TypeConverter;
/*   31:     */ import org.springframework.beans.TypeMismatchException;
/*   32:     */ import org.springframework.beans.factory.BeanCreationException;
/*   33:     */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*   34:     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*   35:     */ import org.springframework.beans.factory.BeanFactory;
/*   36:     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*   37:     */ import org.springframework.beans.factory.BeanIsAbstractException;
/*   38:     */ import org.springframework.beans.factory.BeanIsNotAFactoryException;
/*   39:     */ import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
/*   40:     */ import org.springframework.beans.factory.CannotLoadBeanClassException;
/*   41:     */ import org.springframework.beans.factory.DisposableBean;
/*   42:     */ import org.springframework.beans.factory.FactoryBean;
/*   43:     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*   44:     */ import org.springframework.beans.factory.ObjectFactory;
/*   45:     */ import org.springframework.beans.factory.SmartFactoryBean;
/*   46:     */ import org.springframework.beans.factory.config.BeanDefinition;
/*   47:     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*   48:     */ import org.springframework.beans.factory.config.BeanExpressionContext;
/*   49:     */ import org.springframework.beans.factory.config.BeanExpressionResolver;
/*   50:     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*   51:     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*   52:     */ import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
/*   53:     */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
/*   54:     */ import org.springframework.beans.factory.config.Scope;
/*   55:     */ import org.springframework.core.DecoratingClassLoader;
/*   56:     */ import org.springframework.core.NamedThreadLocal;
/*   57:     */ import org.springframework.core.convert.ConversionService;
/*   58:     */ import org.springframework.util.Assert;
/*   59:     */ import org.springframework.util.ClassUtils;
/*   60:     */ import org.springframework.util.ObjectUtils;
/*   61:     */ import org.springframework.util.StringUtils;
/*   62:     */ import org.springframework.util.StringValueResolver;
/*   63:     */ 
/*   64:     */ public abstract class AbstractBeanFactory
/*   65:     */   extends FactoryBeanRegistrySupport
/*   66:     */   implements ConfigurableBeanFactory
/*   67:     */ {
/*   68:     */   private BeanFactory parentBeanFactory;
/*   69: 117 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*   70:     */   private ClassLoader tempClassLoader;
/*   71: 123 */   private boolean cacheBeanMetadata = true;
/*   72:     */   private BeanExpressionResolver beanExpressionResolver;
/*   73:     */   private ConversionService conversionService;
/*   74: 133 */   private final Set<PropertyEditorRegistrar> propertyEditorRegistrars = new LinkedHashSet(4);
/*   75:     */   private TypeConverter typeConverter;
/*   76: 140 */   private final Map<Class, Class<? extends PropertyEditor>> customEditors = new HashMap(4);
/*   77: 143 */   private final List<StringValueResolver> embeddedValueResolvers = new LinkedList();
/*   78: 146 */   private final List<BeanPostProcessor> beanPostProcessors = new ArrayList();
/*   79:     */   private boolean hasInstantiationAwareBeanPostProcessors;
/*   80:     */   private boolean hasDestructionAwareBeanPostProcessors;
/*   81: 155 */   private final Map<String, Scope> scopes = new HashMap();
/*   82:     */   private SecurityContextProvider securityContextProvider;
/*   83: 162 */   private final Map<String, RootBeanDefinition> mergedBeanDefinitions = new ConcurrentHashMap();
/*   84: 165 */   private final Set<String> alreadyCreated = Collections.synchronizedSet(new HashSet());
/*   85: 169 */   private final ThreadLocal<Object> prototypesCurrentlyInCreation = new NamedThreadLocal("Prototype beans currently in creation");
/*   86:     */   
/*   87:     */   public AbstractBeanFactory() {}
/*   88:     */   
/*   89:     */   public AbstractBeanFactory(BeanFactory parentBeanFactory)
/*   90:     */   {
/*   91: 184 */     this.parentBeanFactory = parentBeanFactory;
/*   92:     */   }
/*   93:     */   
/*   94:     */   public Object getBean(String name)
/*   95:     */     throws BeansException
/*   96:     */   {
/*   97: 193 */     return doGetBean(name, null, null, false);
/*   98:     */   }
/*   99:     */   
/*  100:     */   public <T> T getBean(String name, Class<T> requiredType)
/*  101:     */     throws BeansException
/*  102:     */   {
/*  103: 197 */     return doGetBean(name, requiredType, null, false);
/*  104:     */   }
/*  105:     */   
/*  106:     */   public Object getBean(String name, Object... args)
/*  107:     */     throws BeansException
/*  108:     */   {
/*  109: 201 */     return doGetBean(name, null, args, false);
/*  110:     */   }
/*  111:     */   
/*  112:     */   public <T> T getBean(String name, Class<T> requiredType, Object... args)
/*  113:     */     throws BeansException
/*  114:     */   {
/*  115: 214 */     return doGetBean(name, requiredType, args, false);
/*  116:     */   }
/*  117:     */   
/*  118:     */   protected <T> T doGetBean(String name, Class<T> requiredType, final Object[] args, boolean typeCheckOnly)
/*  119:     */     throws BeansException
/*  120:     */   {
/*  121: 233 */     final String beanName = transformedBeanName(name);
/*  122:     */     
/*  123:     */ 
/*  124:     */ 
/*  125: 237 */     Object sharedInstance = getSingleton(beanName);
/*  126:     */     Object bean;
/*  127: 238 */     if ((sharedInstance != null) && (args == null))
/*  128:     */     {
/*  129: 239 */       if (this.logger.isDebugEnabled()) {
/*  130: 240 */         if (isSingletonCurrentlyInCreation(beanName)) {
/*  131: 241 */           this.logger.debug("Returning eagerly cached instance of singleton bean '" + beanName + 
/*  132: 242 */             "' that is not fully initialized yet - a consequence of a circular reference");
/*  133:     */         } else {
/*  134: 245 */           this.logger.debug("Returning cached instance of singleton bean '" + beanName + "'");
/*  135:     */         }
/*  136:     */       }
/*  137: 248 */       bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
/*  138:     */     }
/*  139:     */     else
/*  140:     */     {
/*  141: 254 */       if (isPrototypeCurrentlyInCreation(beanName)) {
/*  142: 255 */         throw new BeanCurrentlyInCreationException(beanName);
/*  143:     */       }
/*  144: 259 */       BeanFactory parentBeanFactory = getParentBeanFactory();
/*  145: 260 */       if ((parentBeanFactory != null) && (!containsBeanDefinition(beanName)))
/*  146:     */       {
/*  147: 262 */         String nameToLookup = originalBeanName(name);
/*  148: 263 */         if (args != null) {
/*  149: 265 */           return parentBeanFactory.getBean(nameToLookup, args);
/*  150:     */         }
/*  151: 269 */         return parentBeanFactory.getBean(nameToLookup, requiredType);
/*  152:     */       }
/*  153: 273 */       if (!typeCheckOnly) {
/*  154: 274 */         markBeanAsCreated(beanName);
/*  155:     */       }
/*  156: 277 */       final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*  157: 278 */       checkMergedBeanDefinition(mbd, beanName, args);
/*  158:     */       
/*  159:     */ 
/*  160: 281 */       String[] dependsOn = mbd.getDependsOn();
/*  161: 282 */       if (dependsOn != null) {
/*  162: 283 */         for (String dependsOnBean : dependsOn)
/*  163:     */         {
/*  164: 284 */           getBean(dependsOnBean);
/*  165: 285 */           registerDependentBean(dependsOnBean, beanName);
/*  166:     */         }
/*  167:     */       }
/*  168:     */       Object bean;
/*  169: 290 */       if (mbd.isSingleton())
/*  170:     */       {
/*  171: 291 */         sharedInstance = getSingleton(beanName, new ObjectFactory()
/*  172:     */         {
/*  173:     */           public Object getObject()
/*  174:     */             throws BeansException
/*  175:     */           {
/*  176:     */             try
/*  177:     */             {
/*  178: 294 */               return AbstractBeanFactory.this.createBean(beanName, mbd, args);
/*  179:     */             }
/*  180:     */             catch (BeansException ex)
/*  181:     */             {
/*  182: 300 */               AbstractBeanFactory.this.destroySingleton(beanName);
/*  183: 301 */               throw ex;
/*  184:     */             }
/*  185:     */           }
/*  186: 304 */         });
/*  187: 305 */         bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
/*  188:     */       }
/*  189:     */       else
/*  190:     */       {
/*  191:     */         Object bean;
/*  192: 308 */         if (mbd.isPrototype())
/*  193:     */         {
/*  194: 310 */           Object prototypeInstance = null;
/*  195:     */           try
/*  196:     */           {
/*  197: 312 */             beforePrototypeCreation(beanName);
/*  198: 313 */             prototypeInstance = createBean(beanName, mbd, args);
/*  199:     */           }
/*  200:     */           finally
/*  201:     */           {
/*  202: 316 */             afterPrototypeCreation(beanName);
/*  203:     */           }
/*  204: 318 */           bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
/*  205:     */         }
/*  206:     */         else
/*  207:     */         {
/*  208: 322 */           String scopeName = mbd.getScope();
/*  209: 323 */           Scope scope = (Scope)this.scopes.get(scopeName);
/*  210: 324 */           if (scope == null) {
/*  211: 325 */             throw new IllegalStateException("No Scope registered for scope '" + scopeName + "'");
/*  212:     */           }
/*  213:     */           try
/*  214:     */           {
/*  215: 328 */             Object scopedInstance = scope.get(beanName, new ObjectFactory()
/*  216:     */             {
/*  217:     */               public Object getObject()
/*  218:     */                 throws BeansException
/*  219:     */               {
/*  220: 330 */                 AbstractBeanFactory.this.beforePrototypeCreation(beanName);
/*  221:     */                 try
/*  222:     */                 {
/*  223: 332 */                   return AbstractBeanFactory.this.createBean(beanName, mbd, args);
/*  224:     */                 }
/*  225:     */                 finally
/*  226:     */                 {
/*  227: 335 */                   AbstractBeanFactory.this.afterPrototypeCreation(beanName);
/*  228:     */                 }
/*  229:     */               }
/*  230: 338 */             });
/*  231: 339 */             bean = getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
/*  232:     */           }
/*  233:     */           catch (IllegalStateException ex)
/*  234:     */           {
/*  235:     */             Object bean;
/*  236: 342 */             throw new BeanCreationException(beanName, 
/*  237: 343 */               "Scope '" + scopeName + "' is not active for the current thread; " + 
/*  238: 344 */               "consider defining a scoped proxy for this bean if you intend to refer to it from a singleton", 
/*  239: 345 */               ex);
/*  240:     */           }
/*  241:     */         }
/*  242:     */       }
/*  243:     */     }
/*  244:     */     Object bean;
/*  245: 351 */     if ((requiredType != null) && (bean != null) && (!requiredType.isAssignableFrom(bean.getClass()))) {
/*  246:     */       try
/*  247:     */       {
/*  248: 353 */         return getTypeConverter().convertIfNecessary(bean, requiredType);
/*  249:     */       }
/*  250:     */       catch (TypeMismatchException ex)
/*  251:     */       {
/*  252: 356 */         if (this.logger.isDebugEnabled()) {
/*  253: 357 */           this.logger.debug("Failed to convert bean '" + name + "' to required type [" + 
/*  254: 358 */             ClassUtils.getQualifiedName(requiredType) + "]", ex);
/*  255:     */         }
/*  256: 360 */         throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
/*  257:     */       }
/*  258:     */     }
/*  259: 363 */     return bean;
/*  260:     */   }
/*  261:     */   
/*  262:     */   public boolean containsBean(String name)
/*  263:     */   {
/*  264: 367 */     String beanName = transformedBeanName(name);
/*  265: 368 */     if ((containsSingleton(beanName)) || (containsBeanDefinition(beanName))) {
/*  266: 369 */       return (!BeanFactoryUtils.isFactoryDereference(name)) || (isFactoryBean(name));
/*  267:     */     }
/*  268: 372 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  269: 373 */     return (parentBeanFactory != null) && (parentBeanFactory.containsBean(originalBeanName(name)));
/*  270:     */   }
/*  271:     */   
/*  272:     */   public boolean isSingleton(String name)
/*  273:     */     throws NoSuchBeanDefinitionException
/*  274:     */   {
/*  275: 377 */     String beanName = transformedBeanName(name);
/*  276:     */     
/*  277: 379 */     Object beanInstance = getSingleton(beanName, false);
/*  278: 380 */     if (beanInstance != null)
/*  279:     */     {
/*  280: 381 */       if ((beanInstance instanceof FactoryBean)) {
/*  281: 382 */         return (BeanFactoryUtils.isFactoryDereference(name)) || (((FactoryBean)beanInstance).isSingleton());
/*  282:     */       }
/*  283: 385 */       return !BeanFactoryUtils.isFactoryDereference(name);
/*  284:     */     }
/*  285: 388 */     if (containsSingleton(beanName)) {
/*  286: 389 */       return true;
/*  287:     */     }
/*  288: 394 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  289: 395 */     if ((parentBeanFactory != null) && (!containsBeanDefinition(beanName))) {
/*  290: 397 */       return parentBeanFactory.isSingleton(originalBeanName(name));
/*  291:     */     }
/*  292: 400 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*  293: 403 */     if (mbd.isSingleton())
/*  294:     */     {
/*  295: 404 */       if (isFactoryBean(beanName, mbd))
/*  296:     */       {
/*  297: 405 */         if (BeanFactoryUtils.isFactoryDereference(name)) {
/*  298: 406 */           return true;
/*  299:     */         }
/*  300: 408 */         FactoryBean factoryBean = (FactoryBean)getBean("&" + beanName);
/*  301: 409 */         return factoryBean.isSingleton();
/*  302:     */       }
/*  303: 412 */       return !BeanFactoryUtils.isFactoryDereference(name);
/*  304:     */     }
/*  305: 416 */     return false;
/*  306:     */   }
/*  307:     */   
/*  308:     */   public boolean isPrototype(String name)
/*  309:     */     throws NoSuchBeanDefinitionException
/*  310:     */   {
/*  311: 422 */     String beanName = transformedBeanName(name);
/*  312:     */     
/*  313: 424 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  314: 425 */     if ((parentBeanFactory != null) && (!containsBeanDefinition(beanName))) {
/*  315: 427 */       return parentBeanFactory.isPrototype(originalBeanName(name));
/*  316:     */     }
/*  317: 430 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*  318: 431 */     if (mbd.isPrototype()) {
/*  319: 433 */       return (!BeanFactoryUtils.isFactoryDereference(name)) || (isFactoryBean(beanName, mbd));
/*  320:     */     }
/*  321: 438 */     if (BeanFactoryUtils.isFactoryDereference(name)) {
/*  322: 439 */       return false;
/*  323:     */     }
/*  324: 441 */     if (isFactoryBean(beanName, mbd))
/*  325:     */     {
/*  326: 442 */       final FactoryBean factoryBean = (FactoryBean)getBean("&" + beanName);
/*  327: 443 */       if (System.getSecurityManager() != null) {
/*  328: 444 */         ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*  329:     */         {
/*  330:     */           public Boolean run()
/*  331:     */           {
/*  332: 446 */             if (((!(factoryBean instanceof SmartFactoryBean)) || (!((SmartFactoryBean)factoryBean).isPrototype())) && 
/*  333: 447 */               (factoryBean.isSingleton())) {
/*  334: 447 */               return Boolean.valueOf(false);
/*  335:     */             }
/*  336: 446 */             return 
/*  337: 447 */               Boolean.valueOf(true);
/*  338:     */           }
/*  339: 449 */         }, getAccessControlContext())).booleanValue();
/*  340:     */       }
/*  341: 453 */       return (((factoryBean instanceof SmartFactoryBean)) && (((SmartFactoryBean)factoryBean).isPrototype())) || (!factoryBean.isSingleton());
/*  342:     */     }
/*  343: 457 */     return false;
/*  344:     */   }
/*  345:     */   
/*  346:     */   public boolean isTypeMatch(String name, Class targetType)
/*  347:     */     throws NoSuchBeanDefinitionException
/*  348:     */   {
/*  349: 463 */     String beanName = transformedBeanName(name);
/*  350: 464 */     Class typeToMatch = targetType != null ? targetType : Object.class;
/*  351:     */     
/*  352:     */ 
/*  353: 467 */     Object beanInstance = getSingleton(beanName, false);
/*  354: 468 */     if (beanInstance != null)
/*  355:     */     {
/*  356: 469 */       if ((beanInstance instanceof FactoryBean))
/*  357:     */       {
/*  358: 470 */         if (!BeanFactoryUtils.isFactoryDereference(name))
/*  359:     */         {
/*  360: 471 */           Class type = getTypeForFactoryBean((FactoryBean)beanInstance);
/*  361: 472 */           return (type != null) && (typeToMatch.isAssignableFrom(type));
/*  362:     */         }
/*  363: 475 */         return typeToMatch.isAssignableFrom(beanInstance.getClass());
/*  364:     */       }
/*  365: 480 */       return (!BeanFactoryUtils.isFactoryDereference(name)) && (typeToMatch.isAssignableFrom(beanInstance.getClass()));
/*  366:     */     }
/*  367: 483 */     if ((containsSingleton(beanName)) && (!containsBeanDefinition(beanName))) {
/*  368: 485 */       return false;
/*  369:     */     }
/*  370: 490 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  371: 491 */     if ((parentBeanFactory != null) && (!containsBeanDefinition(beanName))) {
/*  372: 493 */       return parentBeanFactory.isTypeMatch(originalBeanName(name), targetType);
/*  373:     */     }
/*  374: 497 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*  375:     */     
/*  376:     */ 
/*  377:     */ 
/*  378: 501 */     BeanDefinitionHolder dbd = mbd.getDecoratedDefinition();
/*  379: 502 */     if ((dbd != null) && (!BeanFactoryUtils.isFactoryDereference(name)))
/*  380:     */     {
/*  381: 503 */       RootBeanDefinition tbd = getMergedBeanDefinition(dbd.getBeanName(), dbd.getBeanDefinition(), mbd);
/*  382: 504 */       Class targetClass = predictBeanType(dbd.getBeanName(), tbd, new Class[] { FactoryBean.class, typeToMatch });
/*  383: 505 */       if ((targetClass != null) && (!FactoryBean.class.isAssignableFrom(targetClass))) {
/*  384: 506 */         return typeToMatch.isAssignableFrom(targetClass);
/*  385:     */       }
/*  386:     */     }
/*  387: 510 */     Class beanClass = predictBeanType(beanName, mbd, new Class[] { FactoryBean.class, typeToMatch });
/*  388: 511 */     if (beanClass == null) {
/*  389: 512 */       return false;
/*  390:     */     }
/*  391: 516 */     if (FactoryBean.class.isAssignableFrom(beanClass))
/*  392:     */     {
/*  393: 517 */       if (!BeanFactoryUtils.isFactoryDereference(name))
/*  394:     */       {
/*  395: 519 */         Class type = getTypeForFactoryBean(beanName, mbd);
/*  396: 520 */         return (type != null) && (typeToMatch.isAssignableFrom(type));
/*  397:     */       }
/*  398: 523 */       return typeToMatch.isAssignableFrom(beanClass);
/*  399:     */     }
/*  400: 528 */     return (!BeanFactoryUtils.isFactoryDereference(name)) && (typeToMatch.isAssignableFrom(beanClass));
/*  401:     */   }
/*  402:     */   
/*  403:     */   public Class<?> getType(String name)
/*  404:     */     throws NoSuchBeanDefinitionException
/*  405:     */   {
/*  406: 534 */     String beanName = transformedBeanName(name);
/*  407:     */     
/*  408:     */ 
/*  409: 537 */     Object beanInstance = getSingleton(beanName, false);
/*  410: 538 */     if (beanInstance != null)
/*  411:     */     {
/*  412: 539 */       if (((beanInstance instanceof FactoryBean)) && (!BeanFactoryUtils.isFactoryDereference(name))) {
/*  413: 540 */         return getTypeForFactoryBean((FactoryBean)beanInstance);
/*  414:     */       }
/*  415: 543 */       return beanInstance.getClass();
/*  416:     */     }
/*  417: 546 */     if ((containsSingleton(beanName)) && (!containsBeanDefinition(beanName))) {
/*  418: 548 */       return null;
/*  419:     */     }
/*  420: 553 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  421: 554 */     if ((parentBeanFactory != null) && (!containsBeanDefinition(beanName))) {
/*  422: 556 */       return parentBeanFactory.getType(originalBeanName(name));
/*  423:     */     }
/*  424: 559 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*  425:     */     
/*  426:     */ 
/*  427:     */ 
/*  428: 563 */     BeanDefinitionHolder dbd = mbd.getDecoratedDefinition();
/*  429: 564 */     if ((dbd != null) && (!BeanFactoryUtils.isFactoryDereference(name)))
/*  430:     */     {
/*  431: 565 */       RootBeanDefinition tbd = getMergedBeanDefinition(dbd.getBeanName(), dbd.getBeanDefinition(), mbd);
/*  432: 566 */       Class targetClass = predictBeanType(dbd.getBeanName(), tbd, new Class[0]);
/*  433: 567 */       if ((targetClass != null) && (!FactoryBean.class.isAssignableFrom(targetClass))) {
/*  434: 568 */         return targetClass;
/*  435:     */       }
/*  436:     */     }
/*  437: 572 */     Class beanClass = predictBeanType(beanName, mbd, new Class[0]);
/*  438: 575 */     if ((beanClass != null) && (FactoryBean.class.isAssignableFrom(beanClass)))
/*  439:     */     {
/*  440: 576 */       if (!BeanFactoryUtils.isFactoryDereference(name)) {
/*  441: 578 */         return getTypeForFactoryBean(beanName, mbd);
/*  442:     */       }
/*  443: 581 */       return beanClass;
/*  444:     */     }
/*  445: 585 */     return !BeanFactoryUtils.isFactoryDereference(name) ? beanClass : null;
/*  446:     */   }
/*  447:     */   
/*  448:     */   public String[] getAliases(String name)
/*  449:     */   {
/*  450: 592 */     String beanName = transformedBeanName(name);
/*  451: 593 */     List<String> aliases = new ArrayList();
/*  452: 594 */     boolean factoryPrefix = name.startsWith("&");
/*  453: 595 */     String fullBeanName = beanName;
/*  454: 596 */     if (factoryPrefix) {
/*  455: 597 */       fullBeanName = "&" + beanName;
/*  456:     */     }
/*  457: 599 */     if (!fullBeanName.equals(name)) {
/*  458: 600 */       aliases.add(fullBeanName);
/*  459:     */     }
/*  460: 602 */     String[] retrievedAliases = super.getAliases(beanName);
/*  461: 603 */     for (String retrievedAlias : retrievedAliases)
/*  462:     */     {
/*  463: 604 */       String alias = (factoryPrefix ? "&" : "") + retrievedAlias;
/*  464: 605 */       if (!alias.equals(name)) {
/*  465: 606 */         aliases.add(alias);
/*  466:     */       }
/*  467:     */     }
/*  468: 609 */     if ((!containsSingleton(beanName)) && (!containsBeanDefinition(beanName)))
/*  469:     */     {
/*  470: 610 */       BeanFactory parentBeanFactory = getParentBeanFactory();
/*  471: 611 */       if (parentBeanFactory != null) {
/*  472: 612 */         aliases.addAll((Collection)Arrays.asList(parentBeanFactory.getAliases(fullBeanName)));
/*  473:     */       }
/*  474:     */     }
/*  475: 615 */     return StringUtils.toStringArray(aliases);
/*  476:     */   }
/*  477:     */   
/*  478:     */   public BeanFactory getParentBeanFactory()
/*  479:     */   {
/*  480: 624 */     return this.parentBeanFactory;
/*  481:     */   }
/*  482:     */   
/*  483:     */   public boolean containsLocalBean(String name)
/*  484:     */   {
/*  485: 628 */     String beanName = transformedBeanName(name);
/*  486:     */     
/*  487: 630 */     return ((containsSingleton(beanName)) || (containsBeanDefinition(beanName))) && ((!BeanFactoryUtils.isFactoryDereference(name)) || (isFactoryBean(beanName)));
/*  488:     */   }
/*  489:     */   
/*  490:     */   public void setParentBeanFactory(BeanFactory parentBeanFactory)
/*  491:     */   {
/*  492: 639 */     if ((this.parentBeanFactory != null) && (this.parentBeanFactory != parentBeanFactory)) {
/*  493: 640 */       throw new IllegalStateException("Already associated with parent BeanFactory: " + this.parentBeanFactory);
/*  494:     */     }
/*  495: 642 */     this.parentBeanFactory = parentBeanFactory;
/*  496:     */   }
/*  497:     */   
/*  498:     */   public void setBeanClassLoader(ClassLoader beanClassLoader)
/*  499:     */   {
/*  500: 646 */     this.beanClassLoader = (beanClassLoader != null ? beanClassLoader : ClassUtils.getDefaultClassLoader());
/*  501:     */   }
/*  502:     */   
/*  503:     */   public ClassLoader getBeanClassLoader()
/*  504:     */   {
/*  505: 650 */     return this.beanClassLoader;
/*  506:     */   }
/*  507:     */   
/*  508:     */   public void setTempClassLoader(ClassLoader tempClassLoader)
/*  509:     */   {
/*  510: 654 */     this.tempClassLoader = tempClassLoader;
/*  511:     */   }
/*  512:     */   
/*  513:     */   public ClassLoader getTempClassLoader()
/*  514:     */   {
/*  515: 658 */     return this.tempClassLoader;
/*  516:     */   }
/*  517:     */   
/*  518:     */   public void setCacheBeanMetadata(boolean cacheBeanMetadata)
/*  519:     */   {
/*  520: 662 */     this.cacheBeanMetadata = cacheBeanMetadata;
/*  521:     */   }
/*  522:     */   
/*  523:     */   public boolean isCacheBeanMetadata()
/*  524:     */   {
/*  525: 666 */     return this.cacheBeanMetadata;
/*  526:     */   }
/*  527:     */   
/*  528:     */   public void setBeanExpressionResolver(BeanExpressionResolver resolver)
/*  529:     */   {
/*  530: 670 */     this.beanExpressionResolver = resolver;
/*  531:     */   }
/*  532:     */   
/*  533:     */   public BeanExpressionResolver getBeanExpressionResolver()
/*  534:     */   {
/*  535: 674 */     return this.beanExpressionResolver;
/*  536:     */   }
/*  537:     */   
/*  538:     */   public void setConversionService(ConversionService conversionService)
/*  539:     */   {
/*  540: 678 */     this.conversionService = conversionService;
/*  541:     */   }
/*  542:     */   
/*  543:     */   public ConversionService getConversionService()
/*  544:     */   {
/*  545: 682 */     return this.conversionService;
/*  546:     */   }
/*  547:     */   
/*  548:     */   public void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar)
/*  549:     */   {
/*  550: 686 */     Assert.notNull(registrar, "PropertyEditorRegistrar must not be null");
/*  551: 687 */     this.propertyEditorRegistrars.add(registrar);
/*  552:     */   }
/*  553:     */   
/*  554:     */   public Set<PropertyEditorRegistrar> getPropertyEditorRegistrars()
/*  555:     */   {
/*  556: 694 */     return this.propertyEditorRegistrars;
/*  557:     */   }
/*  558:     */   
/*  559:     */   public void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass)
/*  560:     */   {
/*  561: 698 */     Assert.notNull(requiredType, "Required type must not be null");
/*  562: 699 */     Assert.isAssignable(PropertyEditor.class, propertyEditorClass);
/*  563: 700 */     this.customEditors.put(requiredType, propertyEditorClass);
/*  564:     */   }
/*  565:     */   
/*  566:     */   public void copyRegisteredEditorsTo(PropertyEditorRegistry registry)
/*  567:     */   {
/*  568: 704 */     registerCustomEditors(registry);
/*  569:     */   }
/*  570:     */   
/*  571:     */   public Map<Class, Class<? extends PropertyEditor>> getCustomEditors()
/*  572:     */   {
/*  573: 711 */     return this.customEditors;
/*  574:     */   }
/*  575:     */   
/*  576:     */   public void setTypeConverter(TypeConverter typeConverter)
/*  577:     */   {
/*  578: 715 */     this.typeConverter = typeConverter;
/*  579:     */   }
/*  580:     */   
/*  581:     */   protected TypeConverter getCustomTypeConverter()
/*  582:     */   {
/*  583: 723 */     return this.typeConverter;
/*  584:     */   }
/*  585:     */   
/*  586:     */   public TypeConverter getTypeConverter()
/*  587:     */   {
/*  588: 727 */     TypeConverter customConverter = getCustomTypeConverter();
/*  589: 728 */     if (customConverter != null) {
/*  590: 729 */       return customConverter;
/*  591:     */     }
/*  592: 733 */     SimpleTypeConverter typeConverter = new SimpleTypeConverter();
/*  593: 734 */     typeConverter.setConversionService(getConversionService());
/*  594: 735 */     registerCustomEditors(typeConverter);
/*  595: 736 */     return typeConverter;
/*  596:     */   }
/*  597:     */   
/*  598:     */   public void addEmbeddedValueResolver(StringValueResolver valueResolver)
/*  599:     */   {
/*  600: 741 */     Assert.notNull(valueResolver, "StringValueResolver must not be null");
/*  601: 742 */     this.embeddedValueResolvers.add(valueResolver);
/*  602:     */   }
/*  603:     */   
/*  604:     */   public String resolveEmbeddedValue(String value)
/*  605:     */   {
/*  606: 746 */     String result = value;
/*  607: 747 */     for (StringValueResolver resolver : this.embeddedValueResolvers) {
/*  608: 748 */       result = resolver.resolveStringValue(result);
/*  609:     */     }
/*  610: 750 */     return result;
/*  611:     */   }
/*  612:     */   
/*  613:     */   public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor)
/*  614:     */   {
/*  615: 754 */     Assert.notNull(beanPostProcessor, "BeanPostProcessor must not be null");
/*  616: 755 */     this.beanPostProcessors.remove(beanPostProcessor);
/*  617: 756 */     this.beanPostProcessors.add(beanPostProcessor);
/*  618: 757 */     if ((beanPostProcessor instanceof InstantiationAwareBeanPostProcessor)) {
/*  619: 758 */       this.hasInstantiationAwareBeanPostProcessors = true;
/*  620:     */     }
/*  621: 760 */     if ((beanPostProcessor instanceof DestructionAwareBeanPostProcessor)) {
/*  622: 761 */       this.hasDestructionAwareBeanPostProcessors = true;
/*  623:     */     }
/*  624:     */   }
/*  625:     */   
/*  626:     */   public int getBeanPostProcessorCount()
/*  627:     */   {
/*  628: 766 */     return this.beanPostProcessors.size();
/*  629:     */   }
/*  630:     */   
/*  631:     */   public List<BeanPostProcessor> getBeanPostProcessors()
/*  632:     */   {
/*  633: 774 */     return this.beanPostProcessors;
/*  634:     */   }
/*  635:     */   
/*  636:     */   protected boolean hasInstantiationAwareBeanPostProcessors()
/*  637:     */   {
/*  638: 784 */     return this.hasInstantiationAwareBeanPostProcessors;
/*  639:     */   }
/*  640:     */   
/*  641:     */   protected boolean hasDestructionAwareBeanPostProcessors()
/*  642:     */   {
/*  643: 794 */     return this.hasDestructionAwareBeanPostProcessors;
/*  644:     */   }
/*  645:     */   
/*  646:     */   public void registerScope(String scopeName, Scope scope)
/*  647:     */   {
/*  648: 798 */     Assert.notNull(scopeName, "Scope identifier must not be null");
/*  649: 799 */     Assert.notNull(scope, "Scope must not be null");
/*  650: 800 */     if (("singleton".equals(scopeName)) || ("prototype".equals(scopeName))) {
/*  651: 801 */       throw new IllegalArgumentException("Cannot replace existing scopes 'singleton' and 'prototype'");
/*  652:     */     }
/*  653: 803 */     this.scopes.put(scopeName, scope);
/*  654:     */   }
/*  655:     */   
/*  656:     */   public String[] getRegisteredScopeNames()
/*  657:     */   {
/*  658: 807 */     return StringUtils.toStringArray((Collection)this.scopes.keySet());
/*  659:     */   }
/*  660:     */   
/*  661:     */   public Scope getRegisteredScope(String scopeName)
/*  662:     */   {
/*  663: 811 */     Assert.notNull(scopeName, "Scope identifier must not be null");
/*  664: 812 */     return (Scope)this.scopes.get(scopeName);
/*  665:     */   }
/*  666:     */   
/*  667:     */   public void setSecurityContextProvider(SecurityContextProvider securityProvider)
/*  668:     */   {
/*  669: 821 */     this.securityContextProvider = securityProvider;
/*  670:     */   }
/*  671:     */   
/*  672:     */   public AccessControlContext getAccessControlContext()
/*  673:     */   {
/*  674: 830 */     return this.securityContextProvider != null ? 
/*  675: 831 */       this.securityContextProvider.getAccessControlContext() : 
/*  676: 832 */       AccessController.getContext();
/*  677:     */   }
/*  678:     */   
/*  679:     */   public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory)
/*  680:     */   {
/*  681: 836 */     Assert.notNull(otherFactory, "BeanFactory must not be null");
/*  682: 837 */     setBeanClassLoader(otherFactory.getBeanClassLoader());
/*  683: 838 */     setCacheBeanMetadata(otherFactory.isCacheBeanMetadata());
/*  684: 839 */     setBeanExpressionResolver(otherFactory.getBeanExpressionResolver());
/*  685: 840 */     if ((otherFactory instanceof AbstractBeanFactory))
/*  686:     */     {
/*  687: 841 */       AbstractBeanFactory otherAbstractFactory = (AbstractBeanFactory)otherFactory;
/*  688: 842 */       this.customEditors.putAll(otherAbstractFactory.customEditors);
/*  689: 843 */       this.propertyEditorRegistrars.addAll(otherAbstractFactory.propertyEditorRegistrars);
/*  690: 844 */       this.beanPostProcessors.addAll(otherAbstractFactory.beanPostProcessors);
/*  691: 845 */       this.hasInstantiationAwareBeanPostProcessors = ((this.hasInstantiationAwareBeanPostProcessors) || 
/*  692: 846 */         (otherAbstractFactory.hasInstantiationAwareBeanPostProcessors));
/*  693: 847 */       this.hasDestructionAwareBeanPostProcessors = ((this.hasDestructionAwareBeanPostProcessors) || 
/*  694: 848 */         (otherAbstractFactory.hasDestructionAwareBeanPostProcessors));
/*  695: 849 */       this.scopes.putAll(otherAbstractFactory.scopes);
/*  696: 850 */       this.securityContextProvider = otherAbstractFactory.securityContextProvider;
/*  697:     */     }
/*  698:     */     else
/*  699:     */     {
/*  700: 853 */       setTypeConverter(otherFactory.getTypeConverter());
/*  701:     */     }
/*  702:     */   }
/*  703:     */   
/*  704:     */   public BeanDefinition getMergedBeanDefinition(String name)
/*  705:     */     throws BeansException
/*  706:     */   {
/*  707: 869 */     String beanName = transformedBeanName(name);
/*  708: 872 */     if ((!containsBeanDefinition(beanName)) && ((getParentBeanFactory() instanceof ConfigurableBeanFactory))) {
/*  709: 873 */       return ((ConfigurableBeanFactory)getParentBeanFactory()).getMergedBeanDefinition(beanName);
/*  710:     */     }
/*  711: 876 */     return getMergedLocalBeanDefinition(beanName);
/*  712:     */   }
/*  713:     */   
/*  714:     */   public boolean isFactoryBean(String name)
/*  715:     */     throws NoSuchBeanDefinitionException
/*  716:     */   {
/*  717: 880 */     String beanName = transformedBeanName(name);
/*  718:     */     
/*  719: 882 */     Object beanInstance = getSingleton(beanName, false);
/*  720: 883 */     if (beanInstance != null) {
/*  721: 884 */       return beanInstance instanceof FactoryBean;
/*  722:     */     }
/*  723: 886 */     if (containsSingleton(beanName)) {
/*  724: 888 */       return false;
/*  725:     */     }
/*  726: 892 */     if ((!containsBeanDefinition(beanName)) && ((getParentBeanFactory() instanceof ConfigurableBeanFactory))) {
/*  727: 894 */       return ((ConfigurableBeanFactory)getParentBeanFactory()).isFactoryBean(name);
/*  728:     */     }
/*  729: 897 */     return isFactoryBean(beanName, getMergedLocalBeanDefinition(beanName));
/*  730:     */   }
/*  731:     */   
/*  732:     */   protected void beforePrototypeCreation(String beanName)
/*  733:     */   {
/*  734: 908 */     Object curVal = this.prototypesCurrentlyInCreation.get();
/*  735: 909 */     if (curVal == null)
/*  736:     */     {
/*  737: 910 */       this.prototypesCurrentlyInCreation.set(beanName);
/*  738:     */     }
/*  739: 912 */     else if ((curVal instanceof String))
/*  740:     */     {
/*  741: 913 */       Set<String> beanNameSet = new HashSet(2);
/*  742: 914 */       beanNameSet.add((String)curVal);
/*  743: 915 */       beanNameSet.add(beanName);
/*  744: 916 */       this.prototypesCurrentlyInCreation.set(beanNameSet);
/*  745:     */     }
/*  746:     */     else
/*  747:     */     {
/*  748: 919 */       Set<String> beanNameSet = (Set)curVal;
/*  749: 920 */       beanNameSet.add(beanName);
/*  750:     */     }
/*  751:     */   }
/*  752:     */   
/*  753:     */   protected void afterPrototypeCreation(String beanName)
/*  754:     */   {
/*  755: 932 */     Object curVal = this.prototypesCurrentlyInCreation.get();
/*  756: 933 */     if ((curVal instanceof String))
/*  757:     */     {
/*  758: 934 */       this.prototypesCurrentlyInCreation.remove();
/*  759:     */     }
/*  760: 936 */     else if ((curVal instanceof Set))
/*  761:     */     {
/*  762: 937 */       Set<String> beanNameSet = (Set)curVal;
/*  763: 938 */       beanNameSet.remove(beanName);
/*  764: 939 */       if (beanNameSet.isEmpty()) {
/*  765: 940 */         this.prototypesCurrentlyInCreation.remove();
/*  766:     */       }
/*  767:     */     }
/*  768:     */   }
/*  769:     */   
/*  770:     */   protected final boolean isPrototypeCurrentlyInCreation(String beanName)
/*  771:     */   {
/*  772: 951 */     Object curVal = this.prototypesCurrentlyInCreation.get();
/*  773:     */     
/*  774: 953 */     return (curVal != null) && ((curVal.equals(beanName)) || (((curVal instanceof Set)) && (((Set)curVal).contains(beanName))));
/*  775:     */   }
/*  776:     */   
/*  777:     */   public boolean isCurrentlyInCreation(String beanName)
/*  778:     */   {
/*  779: 957 */     Assert.notNull(beanName, "Bean name must not be null");
/*  780: 958 */     return (isSingletonCurrentlyInCreation(beanName)) || (isPrototypeCurrentlyInCreation(beanName));
/*  781:     */   }
/*  782:     */   
/*  783:     */   public void destroyBean(String beanName, Object beanInstance)
/*  784:     */   {
/*  785: 962 */     destroyBean(beanName, beanInstance, getMergedLocalBeanDefinition(beanName));
/*  786:     */   }
/*  787:     */   
/*  788:     */   protected void destroyBean(String beanName, Object beanInstance, RootBeanDefinition mbd)
/*  789:     */   {
/*  790: 973 */     new DisposableBeanAdapter(beanInstance, beanName, mbd, getBeanPostProcessors(), getAccessControlContext()).destroy();
/*  791:     */   }
/*  792:     */   
/*  793:     */   public void destroyScopedBean(String beanName)
/*  794:     */   {
/*  795: 977 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*  796: 978 */     if ((mbd.isSingleton()) || (mbd.isPrototype())) {
/*  797: 979 */       throw new IllegalArgumentException(
/*  798: 980 */         "Bean name '" + beanName + "' does not correspond to an object in a mutable scope");
/*  799:     */     }
/*  800: 982 */     String scopeName = mbd.getScope();
/*  801: 983 */     Scope scope = (Scope)this.scopes.get(scopeName);
/*  802: 984 */     if (scope == null) {
/*  803: 985 */       throw new IllegalStateException("No Scope SPI registered for scope '" + scopeName + "'");
/*  804:     */     }
/*  805: 987 */     Object bean = scope.remove(beanName);
/*  806: 988 */     if (bean != null) {
/*  807: 989 */       destroyBean(beanName, bean, mbd);
/*  808:     */     }
/*  809:     */   }
/*  810:     */   
/*  811:     */   protected String transformedBeanName(String name)
/*  812:     */   {
/*  813:1005 */     return canonicalName(BeanFactoryUtils.transformedBeanName(name));
/*  814:     */   }
/*  815:     */   
/*  816:     */   protected String originalBeanName(String name)
/*  817:     */   {
/*  818:1014 */     String beanName = transformedBeanName(name);
/*  819:1015 */     if (name.startsWith("&")) {
/*  820:1016 */       beanName = "&" + beanName;
/*  821:     */     }
/*  822:1018 */     return beanName;
/*  823:     */   }
/*  824:     */   
/*  825:     */   protected void initBeanWrapper(BeanWrapper bw)
/*  826:     */   {
/*  827:1030 */     bw.setConversionService(getConversionService());
/*  828:1031 */     registerCustomEditors(bw);
/*  829:     */   }
/*  830:     */   
/*  831:     */   protected void registerCustomEditors(PropertyEditorRegistry registry)
/*  832:     */   {
/*  833:1043 */     PropertyEditorRegistrySupport registrySupport = 
/*  834:1044 */       (registry instanceof PropertyEditorRegistrySupport) ? (PropertyEditorRegistrySupport)registry : null;
/*  835:1045 */     if (registrySupport != null) {
/*  836:1046 */       registrySupport.useConfigValueEditors();
/*  837:     */     }
/*  838:1048 */     if (!this.propertyEditorRegistrars.isEmpty()) {
/*  839:1049 */       for (PropertyEditorRegistrar registrar : this.propertyEditorRegistrars) {
/*  840:     */         try
/*  841:     */         {
/*  842:1051 */           registrar.registerCustomEditors(registry);
/*  843:     */         }
/*  844:     */         catch (BeanCreationException ex)
/*  845:     */         {
/*  846:1054 */           Throwable rootCause = ex.getMostSpecificCause();
/*  847:1055 */           if ((rootCause instanceof BeanCurrentlyInCreationException))
/*  848:     */           {
/*  849:1056 */             BeanCreationException bce = (BeanCreationException)rootCause;
/*  850:1057 */             if (isCurrentlyInCreation(bce.getBeanName()))
/*  851:     */             {
/*  852:1058 */               if (this.logger.isDebugEnabled()) {
/*  853:1059 */                 this.logger.debug("PropertyEditorRegistrar [" + registrar.getClass().getName() + 
/*  854:1060 */                   "] failed because it tried to obtain currently created bean '" + 
/*  855:1061 */                   ex.getBeanName() + "': " + ex.getMessage());
/*  856:     */               }
/*  857:1063 */               onSuppressedException(ex);
/*  858:1064 */               continue;
/*  859:     */             }
/*  860:     */           }
/*  861:1067 */           throw ex;
/*  862:     */         }
/*  863:     */       }
/*  864:     */     }
/*  865:1071 */     if (!this.customEditors.isEmpty()) {
/*  866:1072 */       for (Map.Entry<Class, Class<? extends PropertyEditor>> entry : this.customEditors.entrySet())
/*  867:     */       {
/*  868:1073 */         Class requiredType = (Class)entry.getKey();
/*  869:1074 */         Class<? extends PropertyEditor> editorClass = (Class)entry.getValue();
/*  870:1075 */         registry.registerCustomEditor(requiredType, (PropertyEditor)BeanUtils.instantiateClass(editorClass));
/*  871:     */       }
/*  872:     */     }
/*  873:     */   }
/*  874:     */   
/*  875:     */   protected RootBeanDefinition getMergedLocalBeanDefinition(String beanName)
/*  876:     */     throws BeansException
/*  877:     */   {
/*  878:1091 */     RootBeanDefinition mbd = (RootBeanDefinition)this.mergedBeanDefinitions.get(beanName);
/*  879:1092 */     if (mbd != null) {
/*  880:1093 */       return mbd;
/*  881:     */     }
/*  882:1095 */     return getMergedBeanDefinition(beanName, getBeanDefinition(beanName));
/*  883:     */   }
/*  884:     */   
/*  885:     */   protected RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd)
/*  886:     */     throws BeanDefinitionStoreException
/*  887:     */   {
/*  888:1109 */     return getMergedBeanDefinition(beanName, bd, null);
/*  889:     */   }
/*  890:     */   
/*  891:     */   protected RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd, BeanDefinition containingBd)
/*  892:     */     throws BeanDefinitionStoreException
/*  893:     */   {
/*  894:1126 */     synchronized (this.mergedBeanDefinitions)
/*  895:     */     {
/*  896:1127 */       RootBeanDefinition mbd = null;
/*  897:1130 */       if (containingBd == null) {
/*  898:1131 */         mbd = (RootBeanDefinition)this.mergedBeanDefinitions.get(beanName);
/*  899:     */       }
/*  900:1134 */       if (mbd == null)
/*  901:     */       {
/*  902:1135 */         if (bd.getParentName() == null)
/*  903:     */         {
/*  904:1137 */           if ((bd instanceof RootBeanDefinition)) {
/*  905:1138 */             mbd = ((RootBeanDefinition)bd).cloneBeanDefinition();
/*  906:     */           } else {
/*  907:1141 */             mbd = new RootBeanDefinition(bd);
/*  908:     */           }
/*  909:     */         }
/*  910:     */         else
/*  911:     */         {
/*  912:     */           try
/*  913:     */           {
/*  914:1148 */             String parentBeanName = transformedBeanName(bd.getParentName());
/*  915:     */             BeanDefinition pbd;
/*  916:1149 */             if (!beanName.equals(parentBeanName))
/*  917:     */             {
/*  918:1150 */               pbd = getMergedBeanDefinition(parentBeanName);
/*  919:     */             }
/*  920:     */             else
/*  921:     */             {
/*  922:     */               BeanDefinition pbd;
/*  923:1153 */               if ((getParentBeanFactory() instanceof ConfigurableBeanFactory)) {
/*  924:1154 */                 pbd = ((ConfigurableBeanFactory)getParentBeanFactory()).getMergedBeanDefinition(parentBeanName);
/*  925:     */               } else {
/*  926:1157 */                 throw new NoSuchBeanDefinitionException(bd.getParentName(), 
/*  927:1158 */                   "Parent name '" + bd.getParentName() + "' is equal to bean name '" + beanName + 
/*  928:1159 */                   "': cannot be resolved without an AbstractBeanFactory parent");
/*  929:     */               }
/*  930:     */             }
/*  931:     */           }
/*  932:     */           catch (NoSuchBeanDefinitionException ex)
/*  933:     */           {
/*  934:1164 */             throw new BeanDefinitionStoreException(bd.getResourceDescription(), beanName, 
/*  935:1165 */               "Could not resolve parent bean definition '" + bd.getParentName() + "'", ex);
/*  936:     */           }
/*  937:     */           BeanDefinition pbd;
/*  938:1168 */           mbd = new RootBeanDefinition(pbd);
/*  939:1169 */           mbd.overrideFrom(bd);
/*  940:     */         }
/*  941:1173 */         if (!StringUtils.hasLength(mbd.getScope())) {
/*  942:1174 */           mbd.setScope("singleton");
/*  943:     */         }
/*  944:1181 */         if ((containingBd != null) && (!containingBd.isSingleton()) && (mbd.isSingleton())) {
/*  945:1182 */           mbd.setScope(containingBd.getScope());
/*  946:     */         }
/*  947:1187 */         if ((containingBd == null) && (isCacheBeanMetadata()) && (isBeanEligibleForMetadataCaching(beanName))) {
/*  948:1188 */           this.mergedBeanDefinitions.put(beanName, mbd);
/*  949:     */         }
/*  950:     */       }
/*  951:1192 */       return mbd;
/*  952:     */     }
/*  953:     */   }
/*  954:     */   
/*  955:     */   protected void checkMergedBeanDefinition(RootBeanDefinition mbd, String beanName, Object[] args)
/*  956:     */     throws BeanDefinitionStoreException
/*  957:     */   {
/*  958:1208 */     if (mbd.isAbstract()) {
/*  959:1209 */       throw new BeanIsAbstractException(beanName);
/*  960:     */     }
/*  961:1214 */     if ((args != null) && (!mbd.isPrototype())) {
/*  962:1215 */       throw new BeanDefinitionStoreException(
/*  963:1216 */         "Can only specify arguments for the getBean method when referring to a prototype bean definition");
/*  964:     */     }
/*  965:     */   }
/*  966:     */   
/*  967:     */   protected void clearMergedBeanDefinition(String beanName)
/*  968:     */   {
/*  969:1226 */     this.mergedBeanDefinitions.remove(beanName);
/*  970:     */   }
/*  971:     */   
/*  972:     */   protected Class resolveBeanClass(final RootBeanDefinition mbd, String beanName, final Class... typesToMatch)
/*  973:     */     throws CannotLoadBeanClassException
/*  974:     */   {
/*  975:     */     try
/*  976:     */     {
/*  977:1243 */       if (mbd.hasBeanClass()) {
/*  978:1244 */         return mbd.getBeanClass();
/*  979:     */       }
/*  980:1246 */       if (System.getSecurityManager() != null) {
/*  981:1247 */         (Class)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*  982:     */         {
/*  983:     */           public Class run()
/*  984:     */             throws Exception
/*  985:     */           {
/*  986:1249 */             return AbstractBeanFactory.this.doResolveBeanClass(mbd, typesToMatch);
/*  987:     */           }
/*  988:1251 */         }, getAccessControlContext());
/*  989:     */       }
/*  990:1254 */       return doResolveBeanClass(mbd, typesToMatch);
/*  991:     */     }
/*  992:     */     catch (PrivilegedActionException pae)
/*  993:     */     {
/*  994:1258 */       ClassNotFoundException ex = (ClassNotFoundException)pae.getException();
/*  995:1259 */       throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
/*  996:     */     }
/*  997:     */     catch (ClassNotFoundException ex)
/*  998:     */     {
/*  999:1262 */       throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
/* 1000:     */     }
/* 1001:     */     catch (LinkageError err)
/* 1002:     */     {
/* 1003:1265 */       throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), err);
/* 1004:     */     }
/* 1005:     */   }
/* 1006:     */   
/* 1007:     */   private Class doResolveBeanClass(RootBeanDefinition mbd, Class... typesToMatch)
/* 1008:     */     throws ClassNotFoundException
/* 1009:     */   {
/* 1010:1270 */     if (!ObjectUtils.isEmpty(typesToMatch))
/* 1011:     */     {
/* 1012:1271 */       ClassLoader tempClassLoader = getTempClassLoader();
/* 1013:1272 */       if (tempClassLoader != null)
/* 1014:     */       {
/* 1015:1273 */         if ((tempClassLoader instanceof DecoratingClassLoader))
/* 1016:     */         {
/* 1017:1274 */           DecoratingClassLoader dcl = (DecoratingClassLoader)tempClassLoader;
/* 1018:1275 */           for (Class<?> typeToMatch : typesToMatch) {
/* 1019:1276 */             dcl.excludeClass(typeToMatch.getName());
/* 1020:     */           }
/* 1021:     */         }
/* 1022:1279 */         String className = mbd.getBeanClassName();
/* 1023:1280 */         return className != null ? ClassUtils.forName(className, tempClassLoader) : null;
/* 1024:     */       }
/* 1025:     */     }
/* 1026:1283 */     return mbd.resolveBeanClass(getBeanClassLoader());
/* 1027:     */   }
/* 1028:     */   
/* 1029:     */   protected Object evaluateBeanDefinitionString(String value, BeanDefinition beanDefinition)
/* 1030:     */   {
/* 1031:1295 */     if (this.beanExpressionResolver == null) {
/* 1032:1296 */       return value;
/* 1033:     */     }
/* 1034:1298 */     Scope scope = beanDefinition != null ? getRegisteredScope(beanDefinition.getScope()) : null;
/* 1035:1299 */     return this.beanExpressionResolver.evaluate(value, new BeanExpressionContext(this, scope));
/* 1036:     */   }
/* 1037:     */   
/* 1038:     */   protected Class predictBeanType(String beanName, RootBeanDefinition mbd, Class... typesToMatch)
/* 1039:     */   {
/* 1040:1319 */     if (mbd.getFactoryMethodName() != null) {
/* 1041:1320 */       return null;
/* 1042:     */     }
/* 1043:1322 */     return resolveBeanClass(mbd, beanName, typesToMatch);
/* 1044:     */   }
/* 1045:     */   
/* 1046:     */   protected boolean isFactoryBean(String beanName, RootBeanDefinition mbd)
/* 1047:     */   {
/* 1048:1331 */     Class beanClass = predictBeanType(beanName, mbd, new Class[] { FactoryBean.class });
/* 1049:1332 */     return (beanClass != null) && (FactoryBean.class.isAssignableFrom(beanClass));
/* 1050:     */   }
/* 1051:     */   
/* 1052:     */   protected Class getTypeForFactoryBean(String beanName, RootBeanDefinition mbd)
/* 1053:     */   {
/* 1054:1351 */     if (!mbd.isSingleton()) {
/* 1055:1352 */       return null;
/* 1056:     */     }
/* 1057:     */     try
/* 1058:     */     {
/* 1059:1355 */       FactoryBean factoryBean = (FactoryBean)doGetBean("&" + beanName, FactoryBean.class, null, true);
/* 1060:1356 */       return getTypeForFactoryBean(factoryBean);
/* 1061:     */     }
/* 1062:     */     catch (BeanCreationException ex)
/* 1063:     */     {
/* 1064:1360 */       if (this.logger.isDebugEnabled()) {
/* 1065:1361 */         this.logger.debug("Ignoring bean creation exception on FactoryBean type check: " + ex);
/* 1066:     */       }
/* 1067:1363 */       onSuppressedException(ex);
/* 1068:     */     }
/* 1069:1364 */     return null;
/* 1070:     */   }
/* 1071:     */   
/* 1072:     */   protected void markBeanAsCreated(String beanName)
/* 1073:     */   {
/* 1074:1375 */     this.alreadyCreated.add(beanName);
/* 1075:     */   }
/* 1076:     */   
/* 1077:     */   protected boolean isBeanEligibleForMetadataCaching(String beanName)
/* 1078:     */   {
/* 1079:1386 */     return this.alreadyCreated.contains(beanName);
/* 1080:     */   }
/* 1081:     */   
/* 1082:     */   protected boolean removeSingletonIfCreatedForTypeCheckOnly(String beanName)
/* 1083:     */   {
/* 1084:1396 */     if (!this.alreadyCreated.contains(beanName))
/* 1085:     */     {
/* 1086:1397 */       removeSingleton(beanName);
/* 1087:1398 */       return true;
/* 1088:     */     }
/* 1089:1401 */     return false;
/* 1090:     */   }
/* 1091:     */   
/* 1092:     */   protected Object getObjectForBeanInstance(Object beanInstance, String name, String beanName, RootBeanDefinition mbd)
/* 1093:     */   {
/* 1094:1418 */     if ((BeanFactoryUtils.isFactoryDereference(name)) && (!(beanInstance instanceof FactoryBean))) {
/* 1095:1419 */       throw new BeanIsNotAFactoryException(transformedBeanName(name), beanInstance.getClass());
/* 1096:     */     }
/* 1097:1425 */     if ((!(beanInstance instanceof FactoryBean)) || (BeanFactoryUtils.isFactoryDereference(name))) {
/* 1098:1426 */       return beanInstance;
/* 1099:     */     }
/* 1100:1429 */     Object object = null;
/* 1101:1430 */     if (mbd == null) {
/* 1102:1431 */       object = getCachedObjectForFactoryBean(beanName);
/* 1103:     */     }
/* 1104:1433 */     if (object == null)
/* 1105:     */     {
/* 1106:1435 */       FactoryBean factory = (FactoryBean)beanInstance;
/* 1107:1437 */       if ((mbd == null) && (containsBeanDefinition(beanName))) {
/* 1108:1438 */         mbd = getMergedLocalBeanDefinition(beanName);
/* 1109:     */       }
/* 1110:1440 */       boolean synthetic = (mbd != null) && (mbd.isSynthetic());
/* 1111:1441 */       object = getObjectFromFactoryBean(factory, beanName, !synthetic);
/* 1112:     */     }
/* 1113:1443 */     return object;
/* 1114:     */   }
/* 1115:     */   
/* 1116:     */   public boolean isBeanNameInUse(String beanName)
/* 1117:     */   {
/* 1118:1453 */     return (isAlias(beanName)) || (containsLocalBean(beanName)) || (hasDependentBean(beanName));
/* 1119:     */   }
/* 1120:     */   
/* 1121:     */   protected boolean requiresDestruction(Object bean, RootBeanDefinition mbd)
/* 1122:     */   {
/* 1123:1469 */     return (bean != null) && (((bean instanceof DisposableBean)) || (mbd.getDestroyMethodName() != null) || (hasDestructionAwareBeanPostProcessors()));
/* 1124:     */   }
/* 1125:     */   
/* 1126:     */   protected void registerDisposableBeanIfNecessary(String beanName, Object bean, RootBeanDefinition mbd)
/* 1127:     */   {
/* 1128:1485 */     AccessControlContext acc = System.getSecurityManager() != null ? getAccessControlContext() : null;
/* 1129:1486 */     if ((!mbd.isPrototype()) && (requiresDestruction(bean, mbd))) {
/* 1130:1487 */       if (mbd.isSingleton())
/* 1131:     */       {
/* 1132:1491 */         registerDisposableBean(beanName, 
/* 1133:1492 */           new DisposableBeanAdapter(bean, beanName, mbd, getBeanPostProcessors(), acc));
/* 1134:     */       }
/* 1135:     */       else
/* 1136:     */       {
/* 1137:1496 */         Scope scope = (Scope)this.scopes.get(mbd.getScope());
/* 1138:1497 */         if (scope == null) {
/* 1139:1498 */           throw new IllegalStateException("No Scope registered for scope '" + mbd.getScope() + "'");
/* 1140:     */         }
/* 1141:1500 */         scope.registerDestructionCallback(beanName, 
/* 1142:1501 */           new DisposableBeanAdapter(bean, beanName, mbd, getBeanPostProcessors(), acc));
/* 1143:     */       }
/* 1144:     */     }
/* 1145:     */   }
/* 1146:     */   
/* 1147:     */   protected abstract boolean containsBeanDefinition(String paramString);
/* 1148:     */   
/* 1149:     */   protected abstract BeanDefinition getBeanDefinition(String paramString)
/* 1150:     */     throws BeansException;
/* 1151:     */   
/* 1152:     */   protected abstract Object createBean(String paramString, RootBeanDefinition paramRootBeanDefinition, Object[] paramArrayOfObject)
/* 1153:     */     throws BeanCreationException;
/* 1154:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.AbstractBeanFactory
 * JD-Core Version:    0.7.0.1
 */