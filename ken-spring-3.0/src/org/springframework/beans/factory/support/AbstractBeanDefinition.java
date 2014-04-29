/*    1:     */ package org.springframework.beans.factory.support;
/*    2:     */ 
/*    3:     */ import java.lang.reflect.Constructor;
/*    4:     */ import java.util.Arrays;
/*    5:     */ import java.util.LinkedHashMap;
/*    6:     */ import java.util.LinkedHashSet;
/*    7:     */ import java.util.Map;
/*    8:     */ import java.util.Set;
/*    9:     */ import org.springframework.beans.BeanMetadataAttributeAccessor;
/*   10:     */ import org.springframework.beans.MutablePropertyValues;
/*   11:     */ import org.springframework.beans.factory.config.BeanDefinition;
/*   12:     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*   13:     */ import org.springframework.core.io.DescriptiveResource;
/*   14:     */ import org.springframework.core.io.Resource;
/*   15:     */ import org.springframework.util.Assert;
/*   16:     */ import org.springframework.util.ClassUtils;
/*   17:     */ import org.springframework.util.ObjectUtils;
/*   18:     */ import org.springframework.util.StringUtils;
/*   19:     */ 
/*   20:     */ public abstract class AbstractBeanDefinition
/*   21:     */   extends BeanMetadataAttributeAccessor
/*   22:     */   implements BeanDefinition, Cloneable
/*   23:     */ {
/*   24:     */   public static final String SCOPE_DEFAULT = "";
/*   25:     */   public static final int AUTOWIRE_NO = 0;
/*   26:     */   public static final int AUTOWIRE_BY_NAME = 1;
/*   27:     */   public static final int AUTOWIRE_BY_TYPE = 2;
/*   28:     */   public static final int AUTOWIRE_CONSTRUCTOR = 3;
/*   29:     */   @Deprecated
/*   30:     */   public static final int AUTOWIRE_AUTODETECT = 4;
/*   31:     */   public static final int DEPENDENCY_CHECK_NONE = 0;
/*   32:     */   public static final int DEPENDENCY_CHECK_OBJECTS = 1;
/*   33:     */   public static final int DEPENDENCY_CHECK_SIMPLE = 2;
/*   34:     */   public static final int DEPENDENCY_CHECK_ALL = 3;
/*   35:     */   public static final String INFER_METHOD = "(inferred)";
/*   36:     */   private volatile Object beanClass;
/*   37: 138 */   private String scope = "";
/*   38: 140 */   private boolean singleton = true;
/*   39: 142 */   private boolean prototype = false;
/*   40: 144 */   private boolean abstractFlag = false;
/*   41: 146 */   private boolean lazyInit = false;
/*   42: 148 */   private int autowireMode = 0;
/*   43: 150 */   private int dependencyCheck = 0;
/*   44:     */   private String[] dependsOn;
/*   45: 154 */   private boolean autowireCandidate = true;
/*   46: 156 */   private boolean primary = false;
/*   47: 159 */   private final Map<String, AutowireCandidateQualifier> qualifiers = new LinkedHashMap(0);
/*   48: 161 */   private boolean nonPublicAccessAllowed = true;
/*   49: 163 */   private boolean lenientConstructorResolution = true;
/*   50:     */   private ConstructorArgumentValues constructorArgumentValues;
/*   51:     */   private MutablePropertyValues propertyValues;
/*   52: 169 */   private MethodOverrides methodOverrides = new MethodOverrides();
/*   53:     */   private String factoryBeanName;
/*   54:     */   private String factoryMethodName;
/*   55:     */   private String initMethodName;
/*   56:     */   private String destroyMethodName;
/*   57: 179 */   private boolean enforceInitMethod = true;
/*   58: 181 */   private boolean enforceDestroyMethod = true;
/*   59: 183 */   private boolean synthetic = false;
/*   60: 185 */   private int role = 0;
/*   61:     */   private String description;
/*   62:     */   private Resource resource;
/*   63:     */   
/*   64:     */   protected AbstractBeanDefinition()
/*   65:     */   {
/*   66: 196 */     this(null, null);
/*   67:     */   }
/*   68:     */   
/*   69:     */   protected AbstractBeanDefinition(ConstructorArgumentValues cargs, MutablePropertyValues pvs)
/*   70:     */   {
/*   71: 204 */     setConstructorArgumentValues(cargs);
/*   72: 205 */     setPropertyValues(pvs);
/*   73:     */   }
/*   74:     */   
/*   75:     */   @Deprecated
/*   76:     */   protected AbstractBeanDefinition(AbstractBeanDefinition original)
/*   77:     */   {
/*   78: 216 */     this(original);
/*   79:     */   }
/*   80:     */   
/*   81:     */   protected AbstractBeanDefinition(BeanDefinition original)
/*   82:     */   {
/*   83: 225 */     setParentName(original.getParentName());
/*   84: 226 */     setBeanClassName(original.getBeanClassName());
/*   85: 227 */     setFactoryBeanName(original.getFactoryBeanName());
/*   86: 228 */     setFactoryMethodName(original.getFactoryMethodName());
/*   87: 229 */     setScope(original.getScope());
/*   88: 230 */     setAbstract(original.isAbstract());
/*   89: 231 */     setLazyInit(original.isLazyInit());
/*   90: 232 */     setRole(original.getRole());
/*   91: 233 */     setConstructorArgumentValues(new ConstructorArgumentValues(original.getConstructorArgumentValues()));
/*   92: 234 */     setPropertyValues(new MutablePropertyValues(original.getPropertyValues()));
/*   93: 235 */     setSource(original.getSource());
/*   94: 236 */     copyAttributesFrom(original);
/*   95: 238 */     if ((original instanceof AbstractBeanDefinition))
/*   96:     */     {
/*   97: 239 */       AbstractBeanDefinition originalAbd = (AbstractBeanDefinition)original;
/*   98: 240 */       if (originalAbd.hasBeanClass()) {
/*   99: 241 */         setBeanClass(originalAbd.getBeanClass());
/*  100:     */       }
/*  101: 243 */       setAutowireMode(originalAbd.getAutowireMode());
/*  102: 244 */       setDependencyCheck(originalAbd.getDependencyCheck());
/*  103: 245 */       setDependsOn(originalAbd.getDependsOn());
/*  104: 246 */       setAutowireCandidate(originalAbd.isAutowireCandidate());
/*  105: 247 */       copyQualifiersFrom(originalAbd);
/*  106: 248 */       setPrimary(originalAbd.isPrimary());
/*  107: 249 */       setNonPublicAccessAllowed(originalAbd.isNonPublicAccessAllowed());
/*  108: 250 */       setLenientConstructorResolution(originalAbd.isLenientConstructorResolution());
/*  109: 251 */       setInitMethodName(originalAbd.getInitMethodName());
/*  110: 252 */       setEnforceInitMethod(originalAbd.isEnforceInitMethod());
/*  111: 253 */       setDestroyMethodName(originalAbd.getDestroyMethodName());
/*  112: 254 */       setEnforceDestroyMethod(originalAbd.isEnforceDestroyMethod());
/*  113: 255 */       setMethodOverrides(new MethodOverrides(originalAbd.getMethodOverrides()));
/*  114: 256 */       setSynthetic(originalAbd.isSynthetic());
/*  115: 257 */       setResource(originalAbd.getResource());
/*  116:     */     }
/*  117:     */     else
/*  118:     */     {
/*  119: 260 */       setResourceDescription(original.getResourceDescription());
/*  120:     */     }
/*  121:     */   }
/*  122:     */   
/*  123:     */   @Deprecated
/*  124:     */   public void overrideFrom(AbstractBeanDefinition other)
/*  125:     */   {
/*  126: 273 */     overrideFrom(other);
/*  127:     */   }
/*  128:     */   
/*  129:     */   public void overrideFrom(BeanDefinition other)
/*  130:     */   {
/*  131: 293 */     if (StringUtils.hasLength(other.getBeanClassName())) {
/*  132: 294 */       setBeanClassName(other.getBeanClassName());
/*  133:     */     }
/*  134: 296 */     if (StringUtils.hasLength(other.getFactoryBeanName())) {
/*  135: 297 */       setFactoryBeanName(other.getFactoryBeanName());
/*  136:     */     }
/*  137: 299 */     if (StringUtils.hasLength(other.getFactoryMethodName())) {
/*  138: 300 */       setFactoryMethodName(other.getFactoryMethodName());
/*  139:     */     }
/*  140: 302 */     if (StringUtils.hasLength(other.getScope())) {
/*  141: 303 */       setScope(other.getScope());
/*  142:     */     }
/*  143: 305 */     setAbstract(other.isAbstract());
/*  144: 306 */     setLazyInit(other.isLazyInit());
/*  145: 307 */     setRole(other.getRole());
/*  146: 308 */     getConstructorArgumentValues().addArgumentValues(other.getConstructorArgumentValues());
/*  147: 309 */     getPropertyValues().addPropertyValues(other.getPropertyValues());
/*  148: 310 */     setSource(other.getSource());
/*  149: 311 */     copyAttributesFrom(other);
/*  150: 313 */     if ((other instanceof AbstractBeanDefinition))
/*  151:     */     {
/*  152: 314 */       AbstractBeanDefinition otherAbd = (AbstractBeanDefinition)other;
/*  153: 315 */       if (otherAbd.hasBeanClass()) {
/*  154: 316 */         setBeanClass(otherAbd.getBeanClass());
/*  155:     */       }
/*  156: 318 */       setAutowireCandidate(otherAbd.isAutowireCandidate());
/*  157: 319 */       setAutowireMode(otherAbd.getAutowireMode());
/*  158: 320 */       copyQualifiersFrom(otherAbd);
/*  159: 321 */       setPrimary(otherAbd.isPrimary());
/*  160: 322 */       setDependencyCheck(otherAbd.getDependencyCheck());
/*  161: 323 */       setDependsOn(otherAbd.getDependsOn());
/*  162: 324 */       setNonPublicAccessAllowed(otherAbd.isNonPublicAccessAllowed());
/*  163: 325 */       setLenientConstructorResolution(otherAbd.isLenientConstructorResolution());
/*  164: 326 */       if (StringUtils.hasLength(otherAbd.getInitMethodName()))
/*  165:     */       {
/*  166: 327 */         setInitMethodName(otherAbd.getInitMethodName());
/*  167: 328 */         setEnforceInitMethod(otherAbd.isEnforceInitMethod());
/*  168:     */       }
/*  169: 330 */       if (StringUtils.hasLength(otherAbd.getDestroyMethodName()))
/*  170:     */       {
/*  171: 331 */         setDestroyMethodName(otherAbd.getDestroyMethodName());
/*  172: 332 */         setEnforceDestroyMethod(otherAbd.isEnforceDestroyMethod());
/*  173:     */       }
/*  174: 334 */       getMethodOverrides().addOverrides(otherAbd.getMethodOverrides());
/*  175: 335 */       setSynthetic(otherAbd.isSynthetic());
/*  176: 336 */       setResource(otherAbd.getResource());
/*  177:     */     }
/*  178:     */     else
/*  179:     */     {
/*  180: 339 */       setResourceDescription(other.getResourceDescription());
/*  181:     */     }
/*  182:     */   }
/*  183:     */   
/*  184:     */   public void applyDefaults(BeanDefinitionDefaults defaults)
/*  185:     */   {
/*  186: 348 */     setLazyInit(defaults.isLazyInit());
/*  187: 349 */     setAutowireMode(defaults.getAutowireMode());
/*  188: 350 */     setDependencyCheck(defaults.getDependencyCheck());
/*  189: 351 */     setInitMethodName(defaults.getInitMethodName());
/*  190: 352 */     setEnforceInitMethod(false);
/*  191: 353 */     setDestroyMethodName(defaults.getDestroyMethodName());
/*  192: 354 */     setEnforceDestroyMethod(false);
/*  193:     */   }
/*  194:     */   
/*  195:     */   public boolean hasBeanClass()
/*  196:     */   {
/*  197: 362 */     return this.beanClass instanceof Class;
/*  198:     */   }
/*  199:     */   
/*  200:     */   public void setBeanClass(Class<?> beanClass)
/*  201:     */   {
/*  202: 369 */     this.beanClass = beanClass;
/*  203:     */   }
/*  204:     */   
/*  205:     */   public Class<?> getBeanClass()
/*  206:     */     throws IllegalStateException
/*  207:     */   {
/*  208: 379 */     Object beanClassObject = this.beanClass;
/*  209: 380 */     if (beanClassObject == null) {
/*  210: 381 */       throw new IllegalStateException("No bean class specified on bean definition");
/*  211:     */     }
/*  212: 383 */     if (!(beanClassObject instanceof Class)) {
/*  213: 384 */       throw new IllegalStateException(
/*  214: 385 */         "Bean class name [" + beanClassObject + "] has not been resolved into an actual Class");
/*  215:     */     }
/*  216: 387 */     return (Class)beanClassObject;
/*  217:     */   }
/*  218:     */   
/*  219:     */   public void setBeanClassName(String beanClassName)
/*  220:     */   {
/*  221: 391 */     this.beanClass = beanClassName;
/*  222:     */   }
/*  223:     */   
/*  224:     */   public String getBeanClassName()
/*  225:     */   {
/*  226: 395 */     Object beanClassObject = this.beanClass;
/*  227: 396 */     if ((beanClassObject instanceof Class)) {
/*  228: 397 */       return ((Class)beanClassObject).getName();
/*  229:     */     }
/*  230: 400 */     return (String)beanClassObject;
/*  231:     */   }
/*  232:     */   
/*  233:     */   public Class resolveBeanClass(ClassLoader classLoader)
/*  234:     */     throws ClassNotFoundException
/*  235:     */   {
/*  236: 413 */     String className = getBeanClassName();
/*  237: 414 */     if (className == null) {
/*  238: 415 */       return null;
/*  239:     */     }
/*  240: 417 */     Class resolvedClass = ClassUtils.forName(className, classLoader);
/*  241: 418 */     this.beanClass = resolvedClass;
/*  242: 419 */     return resolvedClass;
/*  243:     */   }
/*  244:     */   
/*  245:     */   public void setScope(String scope)
/*  246:     */   {
/*  247: 434 */     this.scope = scope;
/*  248: 435 */     this.singleton = (("singleton".equals(scope)) || ("".equals(scope)));
/*  249: 436 */     this.prototype = "prototype".equals(scope);
/*  250:     */   }
/*  251:     */   
/*  252:     */   public String getScope()
/*  253:     */   {
/*  254: 443 */     return this.scope;
/*  255:     */   }
/*  256:     */   
/*  257:     */   @Deprecated
/*  258:     */   public void setSingleton(boolean singleton)
/*  259:     */   {
/*  260: 461 */     this.scope = (singleton ? "singleton" : "prototype");
/*  261: 462 */     this.singleton = singleton;
/*  262: 463 */     this.prototype = (!singleton);
/*  263:     */   }
/*  264:     */   
/*  265:     */   public boolean isSingleton()
/*  266:     */   {
/*  267: 472 */     return this.singleton;
/*  268:     */   }
/*  269:     */   
/*  270:     */   public boolean isPrototype()
/*  271:     */   {
/*  272: 481 */     return this.prototype;
/*  273:     */   }
/*  274:     */   
/*  275:     */   public void setAbstract(boolean abstractFlag)
/*  276:     */   {
/*  277: 491 */     this.abstractFlag = abstractFlag;
/*  278:     */   }
/*  279:     */   
/*  280:     */   public boolean isAbstract()
/*  281:     */   {
/*  282: 499 */     return this.abstractFlag;
/*  283:     */   }
/*  284:     */   
/*  285:     */   public void setLazyInit(boolean lazyInit)
/*  286:     */   {
/*  287: 508 */     this.lazyInit = lazyInit;
/*  288:     */   }
/*  289:     */   
/*  290:     */   public boolean isLazyInit()
/*  291:     */   {
/*  292: 516 */     return this.lazyInit;
/*  293:     */   }
/*  294:     */   
/*  295:     */   public void setAutowireMode(int autowireMode)
/*  296:     */   {
/*  297: 533 */     this.autowireMode = autowireMode;
/*  298:     */   }
/*  299:     */   
/*  300:     */   public int getAutowireMode()
/*  301:     */   {
/*  302: 540 */     return this.autowireMode;
/*  303:     */   }
/*  304:     */   
/*  305:     */   public int getResolvedAutowireMode()
/*  306:     */   {
/*  307: 551 */     if (this.autowireMode == 4)
/*  308:     */     {
/*  309: 555 */       Constructor[] constructors = getBeanClass().getConstructors();
/*  310: 556 */       for (Constructor constructor : constructors) {
/*  311: 557 */         if (constructor.getParameterTypes().length == 0) {
/*  312: 558 */           return 2;
/*  313:     */         }
/*  314:     */       }
/*  315: 561 */       return 3;
/*  316:     */     }
/*  317: 564 */     return this.autowireMode;
/*  318:     */   }
/*  319:     */   
/*  320:     */   public void setDependencyCheck(int dependencyCheck)
/*  321:     */   {
/*  322: 578 */     this.dependencyCheck = dependencyCheck;
/*  323:     */   }
/*  324:     */   
/*  325:     */   public int getDependencyCheck()
/*  326:     */   {
/*  327: 585 */     return this.dependencyCheck;
/*  328:     */   }
/*  329:     */   
/*  330:     */   public void setDependsOn(String[] dependsOn)
/*  331:     */   {
/*  332: 596 */     this.dependsOn = dependsOn;
/*  333:     */   }
/*  334:     */   
/*  335:     */   public String[] getDependsOn()
/*  336:     */   {
/*  337: 603 */     return this.dependsOn;
/*  338:     */   }
/*  339:     */   
/*  340:     */   public void setAutowireCandidate(boolean autowireCandidate)
/*  341:     */   {
/*  342: 610 */     this.autowireCandidate = autowireCandidate;
/*  343:     */   }
/*  344:     */   
/*  345:     */   public boolean isAutowireCandidate()
/*  346:     */   {
/*  347: 617 */     return this.autowireCandidate;
/*  348:     */   }
/*  349:     */   
/*  350:     */   public void setPrimary(boolean primary)
/*  351:     */   {
/*  352: 626 */     this.primary = primary;
/*  353:     */   }
/*  354:     */   
/*  355:     */   public boolean isPrimary()
/*  356:     */   {
/*  357: 635 */     return this.primary;
/*  358:     */   }
/*  359:     */   
/*  360:     */   public void addQualifier(AutowireCandidateQualifier qualifier)
/*  361:     */   {
/*  362: 644 */     this.qualifiers.put(qualifier.getTypeName(), qualifier);
/*  363:     */   }
/*  364:     */   
/*  365:     */   public boolean hasQualifier(String typeName)
/*  366:     */   {
/*  367: 651 */     return this.qualifiers.keySet().contains(typeName);
/*  368:     */   }
/*  369:     */   
/*  370:     */   public AutowireCandidateQualifier getQualifier(String typeName)
/*  371:     */   {
/*  372: 658 */     return (AutowireCandidateQualifier)this.qualifiers.get(typeName);
/*  373:     */   }
/*  374:     */   
/*  375:     */   public Set<AutowireCandidateQualifier> getQualifiers()
/*  376:     */   {
/*  377: 666 */     return new LinkedHashSet(this.qualifiers.values());
/*  378:     */   }
/*  379:     */   
/*  380:     */   public void copyQualifiersFrom(AbstractBeanDefinition source)
/*  381:     */   {
/*  382: 674 */     Assert.notNull(source, "Source must not be null");
/*  383: 675 */     this.qualifiers.putAll(source.qualifiers);
/*  384:     */   }
/*  385:     */   
/*  386:     */   public void setNonPublicAccessAllowed(boolean nonPublicAccessAllowed)
/*  387:     */   {
/*  388: 690 */     this.nonPublicAccessAllowed = nonPublicAccessAllowed;
/*  389:     */   }
/*  390:     */   
/*  391:     */   public boolean isNonPublicAccessAllowed()
/*  392:     */   {
/*  393: 697 */     return this.nonPublicAccessAllowed;
/*  394:     */   }
/*  395:     */   
/*  396:     */   public void setLenientConstructorResolution(boolean lenientConstructorResolution)
/*  397:     */   {
/*  398: 707 */     this.lenientConstructorResolution = lenientConstructorResolution;
/*  399:     */   }
/*  400:     */   
/*  401:     */   public boolean isLenientConstructorResolution()
/*  402:     */   {
/*  403: 714 */     return this.lenientConstructorResolution;
/*  404:     */   }
/*  405:     */   
/*  406:     */   public void setConstructorArgumentValues(ConstructorArgumentValues constructorArgumentValues)
/*  407:     */   {
/*  408: 721 */     this.constructorArgumentValues = 
/*  409: 722 */       (constructorArgumentValues != null ? constructorArgumentValues : new ConstructorArgumentValues());
/*  410:     */   }
/*  411:     */   
/*  412:     */   public ConstructorArgumentValues getConstructorArgumentValues()
/*  413:     */   {
/*  414: 729 */     return this.constructorArgumentValues;
/*  415:     */   }
/*  416:     */   
/*  417:     */   public boolean hasConstructorArgumentValues()
/*  418:     */   {
/*  419: 736 */     return !this.constructorArgumentValues.isEmpty();
/*  420:     */   }
/*  421:     */   
/*  422:     */   public void setPropertyValues(MutablePropertyValues propertyValues)
/*  423:     */   {
/*  424: 743 */     this.propertyValues = (propertyValues != null ? propertyValues : new MutablePropertyValues());
/*  425:     */   }
/*  426:     */   
/*  427:     */   public MutablePropertyValues getPropertyValues()
/*  428:     */   {
/*  429: 750 */     return this.propertyValues;
/*  430:     */   }
/*  431:     */   
/*  432:     */   public void setMethodOverrides(MethodOverrides methodOverrides)
/*  433:     */   {
/*  434: 757 */     this.methodOverrides = (methodOverrides != null ? methodOverrides : new MethodOverrides());
/*  435:     */   }
/*  436:     */   
/*  437:     */   public MethodOverrides getMethodOverrides()
/*  438:     */   {
/*  439: 766 */     return this.methodOverrides;
/*  440:     */   }
/*  441:     */   
/*  442:     */   public void setFactoryBeanName(String factoryBeanName)
/*  443:     */   {
/*  444: 771 */     this.factoryBeanName = factoryBeanName;
/*  445:     */   }
/*  446:     */   
/*  447:     */   public String getFactoryBeanName()
/*  448:     */   {
/*  449: 775 */     return this.factoryBeanName;
/*  450:     */   }
/*  451:     */   
/*  452:     */   public void setFactoryMethodName(String factoryMethodName)
/*  453:     */   {
/*  454: 779 */     this.factoryMethodName = factoryMethodName;
/*  455:     */   }
/*  456:     */   
/*  457:     */   public String getFactoryMethodName()
/*  458:     */   {
/*  459: 783 */     return this.factoryMethodName;
/*  460:     */   }
/*  461:     */   
/*  462:     */   public void setInitMethodName(String initMethodName)
/*  463:     */   {
/*  464: 791 */     this.initMethodName = initMethodName;
/*  465:     */   }
/*  466:     */   
/*  467:     */   public String getInitMethodName()
/*  468:     */   {
/*  469: 798 */     return this.initMethodName;
/*  470:     */   }
/*  471:     */   
/*  472:     */   public void setEnforceInitMethod(boolean enforceInitMethod)
/*  473:     */   {
/*  474: 807 */     this.enforceInitMethod = enforceInitMethod;
/*  475:     */   }
/*  476:     */   
/*  477:     */   public boolean isEnforceInitMethod()
/*  478:     */   {
/*  479: 815 */     return this.enforceInitMethod;
/*  480:     */   }
/*  481:     */   
/*  482:     */   public void setDestroyMethodName(String destroyMethodName)
/*  483:     */   {
/*  484: 823 */     this.destroyMethodName = destroyMethodName;
/*  485:     */   }
/*  486:     */   
/*  487:     */   public String getDestroyMethodName()
/*  488:     */   {
/*  489: 830 */     return this.destroyMethodName;
/*  490:     */   }
/*  491:     */   
/*  492:     */   public void setEnforceDestroyMethod(boolean enforceDestroyMethod)
/*  493:     */   {
/*  494: 839 */     this.enforceDestroyMethod = enforceDestroyMethod;
/*  495:     */   }
/*  496:     */   
/*  497:     */   public boolean isEnforceDestroyMethod()
/*  498:     */   {
/*  499: 847 */     return this.enforceDestroyMethod;
/*  500:     */   }
/*  501:     */   
/*  502:     */   public void setSynthetic(boolean synthetic)
/*  503:     */   {
/*  504: 857 */     this.synthetic = synthetic;
/*  505:     */   }
/*  506:     */   
/*  507:     */   public boolean isSynthetic()
/*  508:     */   {
/*  509: 865 */     return this.synthetic;
/*  510:     */   }
/*  511:     */   
/*  512:     */   public void setRole(int role)
/*  513:     */   {
/*  514: 872 */     this.role = role;
/*  515:     */   }
/*  516:     */   
/*  517:     */   public int getRole()
/*  518:     */   {
/*  519: 879 */     return this.role;
/*  520:     */   }
/*  521:     */   
/*  522:     */   public void setDescription(String description)
/*  523:     */   {
/*  524: 887 */     this.description = description;
/*  525:     */   }
/*  526:     */   
/*  527:     */   public String getDescription()
/*  528:     */   {
/*  529: 891 */     return this.description;
/*  530:     */   }
/*  531:     */   
/*  532:     */   public void setResource(Resource resource)
/*  533:     */   {
/*  534: 899 */     this.resource = resource;
/*  535:     */   }
/*  536:     */   
/*  537:     */   public Resource getResource()
/*  538:     */   {
/*  539: 906 */     return this.resource;
/*  540:     */   }
/*  541:     */   
/*  542:     */   public void setResourceDescription(String resourceDescription)
/*  543:     */   {
/*  544: 914 */     this.resource = new DescriptiveResource(resourceDescription);
/*  545:     */   }
/*  546:     */   
/*  547:     */   public String getResourceDescription()
/*  548:     */   {
/*  549: 918 */     return this.resource != null ? this.resource.getDescription() : null;
/*  550:     */   }
/*  551:     */   
/*  552:     */   public void setOriginatingBeanDefinition(BeanDefinition originatingBd)
/*  553:     */   {
/*  554: 925 */     this.resource = new BeanDefinitionResource(originatingBd);
/*  555:     */   }
/*  556:     */   
/*  557:     */   public BeanDefinition getOriginatingBeanDefinition()
/*  558:     */   {
/*  559: 929 */     return (this.resource instanceof BeanDefinitionResource) ? 
/*  560: 930 */       ((BeanDefinitionResource)this.resource).getBeanDefinition() : null;
/*  561:     */   }
/*  562:     */   
/*  563:     */   public void validate()
/*  564:     */     throws BeanDefinitionValidationException
/*  565:     */   {
/*  566: 938 */     if ((!getMethodOverrides().isEmpty()) && (getFactoryMethodName() != null)) {
/*  567: 939 */       throw new BeanDefinitionValidationException(
/*  568: 940 */         "Cannot combine static factory method with method overrides: the static factory method must create the instance");
/*  569:     */     }
/*  570: 944 */     if (hasBeanClass()) {
/*  571: 945 */       prepareMethodOverrides();
/*  572:     */     }
/*  573:     */   }
/*  574:     */   
/*  575:     */   public void prepareMethodOverrides()
/*  576:     */     throws BeanDefinitionValidationException
/*  577:     */   {
/*  578: 956 */     MethodOverrides methodOverrides = getMethodOverrides();
/*  579: 957 */     if (!methodOverrides.isEmpty()) {
/*  580: 958 */       for (MethodOverride mo : methodOverrides.getOverrides()) {
/*  581: 959 */         prepareMethodOverride(mo);
/*  582:     */       }
/*  583:     */     }
/*  584:     */   }
/*  585:     */   
/*  586:     */   protected void prepareMethodOverride(MethodOverride mo)
/*  587:     */     throws BeanDefinitionValidationException
/*  588:     */   {
/*  589: 972 */     int count = ClassUtils.getMethodCountForName(getBeanClass(), mo.getMethodName());
/*  590: 973 */     if (count == 0) {
/*  591: 974 */       throw new BeanDefinitionValidationException(
/*  592: 975 */         "Invalid method override: no method with name '" + mo.getMethodName() + 
/*  593: 976 */         "' on class [" + getBeanClassName() + "]");
/*  594:     */     }
/*  595: 978 */     if (count == 1) {
/*  596: 980 */       mo.setOverloaded(false);
/*  597:     */     }
/*  598:     */   }
/*  599:     */   
/*  600:     */   public Object clone()
/*  601:     */   {
/*  602: 992 */     return cloneBeanDefinition();
/*  603:     */   }
/*  604:     */   
/*  605:     */   public abstract AbstractBeanDefinition cloneBeanDefinition();
/*  606:     */   
/*  607:     */   public boolean equals(Object other)
/*  608:     */   {
/*  609:1005 */     if (this == other) {
/*  610:1006 */       return true;
/*  611:     */     }
/*  612:1008 */     if (!(other instanceof AbstractBeanDefinition)) {
/*  613:1009 */       return false;
/*  614:     */     }
/*  615:1012 */     AbstractBeanDefinition that = (AbstractBeanDefinition)other;
/*  616:1014 */     if (!ObjectUtils.nullSafeEquals(getBeanClassName(), that.getBeanClassName())) {
/*  617:1014 */       return false;
/*  618:     */     }
/*  619:1015 */     if (!ObjectUtils.nullSafeEquals(this.scope, that.scope)) {
/*  620:1015 */       return false;
/*  621:     */     }
/*  622:1016 */     if (this.abstractFlag != that.abstractFlag) {
/*  623:1016 */       return false;
/*  624:     */     }
/*  625:1017 */     if (this.lazyInit != that.lazyInit) {
/*  626:1017 */       return false;
/*  627:     */     }
/*  628:1019 */     if (this.autowireMode != that.autowireMode) {
/*  629:1019 */       return false;
/*  630:     */     }
/*  631:1020 */     if (this.dependencyCheck != that.dependencyCheck) {
/*  632:1020 */       return false;
/*  633:     */     }
/*  634:1021 */     if (!Arrays.equals(this.dependsOn, that.dependsOn)) {
/*  635:1021 */       return false;
/*  636:     */     }
/*  637:1022 */     if (this.autowireCandidate != that.autowireCandidate) {
/*  638:1022 */       return false;
/*  639:     */     }
/*  640:1023 */     if (!ObjectUtils.nullSafeEquals(this.qualifiers, that.qualifiers)) {
/*  641:1023 */       return false;
/*  642:     */     }
/*  643:1024 */     if (this.primary != that.primary) {
/*  644:1024 */       return false;
/*  645:     */     }
/*  646:1026 */     if (this.nonPublicAccessAllowed != that.nonPublicAccessAllowed) {
/*  647:1026 */       return false;
/*  648:     */     }
/*  649:1027 */     if (this.lenientConstructorResolution != that.lenientConstructorResolution) {
/*  650:1027 */       return false;
/*  651:     */     }
/*  652:1028 */     if (!ObjectUtils.nullSafeEquals(this.constructorArgumentValues, that.constructorArgumentValues)) {
/*  653:1028 */       return false;
/*  654:     */     }
/*  655:1029 */     if (!ObjectUtils.nullSafeEquals(this.propertyValues, that.propertyValues)) {
/*  656:1029 */       return false;
/*  657:     */     }
/*  658:1030 */     if (!ObjectUtils.nullSafeEquals(this.methodOverrides, that.methodOverrides)) {
/*  659:1030 */       return false;
/*  660:     */     }
/*  661:1032 */     if (!ObjectUtils.nullSafeEquals(this.factoryBeanName, that.factoryBeanName)) {
/*  662:1032 */       return false;
/*  663:     */     }
/*  664:1033 */     if (!ObjectUtils.nullSafeEquals(this.factoryMethodName, that.factoryMethodName)) {
/*  665:1033 */       return false;
/*  666:     */     }
/*  667:1034 */     if (!ObjectUtils.nullSafeEquals(this.initMethodName, that.initMethodName)) {
/*  668:1034 */       return false;
/*  669:     */     }
/*  670:1035 */     if (this.enforceInitMethod != that.enforceInitMethod) {
/*  671:1035 */       return false;
/*  672:     */     }
/*  673:1036 */     if (!ObjectUtils.nullSafeEquals(this.destroyMethodName, that.destroyMethodName)) {
/*  674:1036 */       return false;
/*  675:     */     }
/*  676:1037 */     if (this.enforceDestroyMethod != that.enforceDestroyMethod) {
/*  677:1037 */       return false;
/*  678:     */     }
/*  679:1039 */     if (this.synthetic != that.synthetic) {
/*  680:1039 */       return false;
/*  681:     */     }
/*  682:1040 */     if (this.role != that.role) {
/*  683:1040 */       return false;
/*  684:     */     }
/*  685:1042 */     return super.equals(other);
/*  686:     */   }
/*  687:     */   
/*  688:     */   public int hashCode()
/*  689:     */   {
/*  690:1047 */     int hashCode = ObjectUtils.nullSafeHashCode(getBeanClassName());
/*  691:1048 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.scope);
/*  692:1049 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.constructorArgumentValues);
/*  693:1050 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.propertyValues);
/*  694:1051 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.factoryBeanName);
/*  695:1052 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.factoryMethodName);
/*  696:1053 */     hashCode = 29 * hashCode + super.hashCode();
/*  697:1054 */     return hashCode;
/*  698:     */   }
/*  699:     */   
/*  700:     */   public String toString()
/*  701:     */   {
/*  702:1059 */     StringBuilder sb = new StringBuilder("class [");
/*  703:1060 */     sb.append(getBeanClassName()).append("]");
/*  704:1061 */     sb.append("; scope=").append(this.scope);
/*  705:1062 */     sb.append("; abstract=").append(this.abstractFlag);
/*  706:1063 */     sb.append("; lazyInit=").append(this.lazyInit);
/*  707:1064 */     sb.append("; autowireMode=").append(this.autowireMode);
/*  708:1065 */     sb.append("; dependencyCheck=").append(this.dependencyCheck);
/*  709:1066 */     sb.append("; autowireCandidate=").append(this.autowireCandidate);
/*  710:1067 */     sb.append("; primary=").append(this.primary);
/*  711:1068 */     sb.append("; factoryBeanName=").append(this.factoryBeanName);
/*  712:1069 */     sb.append("; factoryMethodName=").append(this.factoryMethodName);
/*  713:1070 */     sb.append("; initMethodName=").append(this.initMethodName);
/*  714:1071 */     sb.append("; destroyMethodName=").append(this.destroyMethodName);
/*  715:1072 */     if (this.resource != null) {
/*  716:1073 */       sb.append("; defined in ").append(this.resource.getDescription());
/*  717:     */     }
/*  718:1075 */     return sb.toString();
/*  719:     */   }
/*  720:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.AbstractBeanDefinition
 * JD-Core Version:    0.7.0.1
 */