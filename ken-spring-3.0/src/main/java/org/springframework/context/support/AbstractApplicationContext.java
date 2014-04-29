/*    1:     */ package org.springframework.context.support;
/*    2:     */ 
/*    3:     */ import java.io.IOException;
/*    4:     */ import java.lang.annotation.Annotation;
/*    5:     */ import java.util.ArrayList;
/*    6:     */ import java.util.Collection;
/*    7:     */ import java.util.Date;
/*    8:     */ import java.util.HashSet;
/*    9:     */ import java.util.Iterator;
/*   10:     */ import java.util.LinkedHashSet;
/*   11:     */ import java.util.LinkedList;
/*   12:     */ import java.util.List;
/*   13:     */ import java.util.Locale;
/*   14:     */ import java.util.Map;
/*   15:     */ import java.util.Set;
/*   16:     */ import java.util.concurrent.ConcurrentHashMap;
/*   17:     */ import org.apache.commons.logging.Log;
/*   18:     */ import org.apache.commons.logging.LogFactory;
/*   19:     */ import org.springframework.beans.BeansException;
/*   20:     */ import org.springframework.beans.factory.BeanFactory;
/*   21:     */ import org.springframework.beans.factory.DisposableBean;
/*   22:     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*   23:     */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*   24:     */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*   25:     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*   26:     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*   27:     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*   28:     */ import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
/*   29:     */ import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
/*   30:     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*   31:     */ import org.springframework.beans.support.ResourceEditorRegistrar;
/*   32:     */ import org.springframework.context.ApplicationContext;
/*   33:     */ import org.springframework.context.ApplicationContextAware;
/*   34:     */ import org.springframework.context.ApplicationEvent;
/*   35:     */ import org.springframework.context.ApplicationEventPublisher;
/*   36:     */ import org.springframework.context.ApplicationEventPublisherAware;
/*   37:     */ import org.springframework.context.ApplicationListener;
/*   38:     */ import org.springframework.context.ConfigurableApplicationContext;
/*   39:     */ import org.springframework.context.EnvironmentAware;
/*   40:     */ import org.springframework.context.HierarchicalMessageSource;
/*   41:     */ import org.springframework.context.LifecycleProcessor;
/*   42:     */ import org.springframework.context.MessageSource;
/*   43:     */ import org.springframework.context.MessageSourceAware;
/*   44:     */ import org.springframework.context.MessageSourceResolvable;
/*   45:     */ import org.springframework.context.NoSuchMessageException;
/*   46:     */ import org.springframework.context.ResourceLoaderAware;
/*   47:     */ import org.springframework.context.event.ApplicationEventMulticaster;
/*   48:     */ import org.springframework.context.event.ContextClosedEvent;
/*   49:     */ import org.springframework.context.event.ContextRefreshedEvent;
/*   50:     */ import org.springframework.context.event.ContextStartedEvent;
/*   51:     */ import org.springframework.context.event.ContextStoppedEvent;
/*   52:     */ import org.springframework.context.event.SimpleApplicationEventMulticaster;
/*   53:     */ import org.springframework.context.expression.StandardBeanExpressionResolver;
/*   54:     */ import org.springframework.context.weaving.LoadTimeWeaverAwareProcessor;
/*   55:     */ import org.springframework.core.OrderComparator;
/*   56:     */ import org.springframework.core.Ordered;
/*   57:     */ import org.springframework.core.PriorityOrdered;
/*   58:     */ import org.springframework.core.convert.ConversionService;
/*   59:     */ import org.springframework.core.env.ConfigurableEnvironment;
/*   60:     */ import org.springframework.core.env.StandardEnvironment;
/*   61:     */ import org.springframework.core.io.DefaultResourceLoader;
/*   62:     */ import org.springframework.core.io.Resource;
/*   63:     */ import org.springframework.core.io.ResourceLoader;
/*   64:     */ import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/*   65:     */ import org.springframework.core.io.support.ResourcePatternResolver;
/*   66:     */ import org.springframework.util.Assert;
/*   67:     */ import org.springframework.util.ObjectUtils;
/*   68:     */ 
/*   69:     */ public abstract class AbstractApplicationContext
/*   70:     */   extends DefaultResourceLoader
/*   71:     */   implements ConfigurableApplicationContext, DisposableBean
/*   72:     */ {
/*   73:     */   public static final String MESSAGE_SOURCE_BEAN_NAME = "messageSource";
/*   74:     */   public static final String LIFECYCLE_PROCESSOR_BEAN_NAME = "lifecycleProcessor";
/*   75:     */   public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";
/*   76:     */   
/*   77:     */   static
/*   78:     */   {
/*   79: 156 */     ContextClosedEvent.class.getName();
/*   80:     */   }
/*   81:     */   
/*   82: 161 */   protected final Log logger = LogFactory.getLog(getClass());
/*   83: 164 */   private String id = ObjectUtils.identityToString(this);
/*   84: 167 */   private String displayName = ObjectUtils.identityToString(this);
/*   85:     */   private ApplicationContext parent;
/*   86: 174 */   private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList();
/*   87:     */   private long startupDate;
/*   88: 180 */   private boolean active = false;
/*   89: 183 */   private boolean closed = false;
/*   90: 186 */   private final Object activeMonitor = new Object();
/*   91: 189 */   private final Object startupShutdownMonitor = new Object();
/*   92:     */   private Thread shutdownHook;
/*   93:     */   private ResourcePatternResolver resourcePatternResolver;
/*   94:     */   private LifecycleProcessor lifecycleProcessor;
/*   95:     */   private MessageSource messageSource;
/*   96:     */   private ApplicationEventMulticaster applicationEventMulticaster;
/*   97: 207 */   private Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet();
/*   98:     */   private ConfigurableEnvironment environment;
/*   99:     */   
/*  100:     */   public AbstractApplicationContext()
/*  101:     */   {
/*  102: 217 */     this(null);
/*  103:     */   }
/*  104:     */   
/*  105:     */   public AbstractApplicationContext(ApplicationContext parent)
/*  106:     */   {
/*  107: 225 */     this.parent = parent;
/*  108: 226 */     this.resourcePatternResolver = getResourcePatternResolver();
/*  109: 227 */     this.environment = createEnvironment();
/*  110:     */   }
/*  111:     */   
/*  112:     */   public void setId(String id)
/*  113:     */   {
/*  114: 242 */     this.id = id;
/*  115:     */   }
/*  116:     */   
/*  117:     */   public String getId()
/*  118:     */   {
/*  119: 250 */     return this.id;
/*  120:     */   }
/*  121:     */   
/*  122:     */   public void setDisplayName(String displayName)
/*  123:     */   {
/*  124: 259 */     Assert.hasLength(displayName, "Display name must not be empty");
/*  125: 260 */     this.displayName = displayName;
/*  126:     */   }
/*  127:     */   
/*  128:     */   public String getDisplayName()
/*  129:     */   {
/*  130: 268 */     return this.displayName;
/*  131:     */   }
/*  132:     */   
/*  133:     */   public ApplicationContext getParent()
/*  134:     */   {
/*  135: 276 */     return this.parent;
/*  136:     */   }
/*  137:     */   
/*  138:     */   public ConfigurableEnvironment getEnvironment()
/*  139:     */   {
/*  140: 280 */     return this.environment;
/*  141:     */   }
/*  142:     */   
/*  143:     */   public void setEnvironment(ConfigurableEnvironment environment)
/*  144:     */   {
/*  145: 292 */     this.environment = environment;
/*  146:     */   }
/*  147:     */   
/*  148:     */   public AutowireCapableBeanFactory getAutowireCapableBeanFactory()
/*  149:     */     throws IllegalStateException
/*  150:     */   {
/*  151: 301 */     return getBeanFactory();
/*  152:     */   }
/*  153:     */   
/*  154:     */   public long getStartupDate()
/*  155:     */   {
/*  156: 308 */     return this.startupDate;
/*  157:     */   }
/*  158:     */   
/*  159:     */   public void publishEvent(ApplicationEvent event)
/*  160:     */   {
/*  161: 320 */     Assert.notNull(event, "Event must not be null");
/*  162: 321 */     if (this.logger.isTraceEnabled()) {
/*  163: 322 */       this.logger.trace("Publishing event in " + getDisplayName() + ": " + event);
/*  164:     */     }
/*  165: 324 */     getApplicationEventMulticaster().multicastEvent(event);
/*  166: 325 */     if (this.parent != null) {
/*  167: 326 */       this.parent.publishEvent(event);
/*  168:     */     }
/*  169:     */   }
/*  170:     */   
/*  171:     */   private ApplicationEventMulticaster getApplicationEventMulticaster()
/*  172:     */     throws IllegalStateException
/*  173:     */   {
/*  174: 336 */     if (this.applicationEventMulticaster == null) {
/*  175: 337 */       throw new IllegalStateException("ApplicationEventMulticaster not initialized - call 'refresh' before multicasting events via the context: " + 
/*  176: 338 */         this);
/*  177:     */     }
/*  178: 340 */     return this.applicationEventMulticaster;
/*  179:     */   }
/*  180:     */   
/*  181:     */   private LifecycleProcessor getLifecycleProcessor()
/*  182:     */   {
/*  183: 349 */     if (this.lifecycleProcessor == null) {
/*  184: 350 */       throw new IllegalStateException("LifecycleProcessor not initialized - call 'refresh' before invoking lifecycle methods via the context: " + 
/*  185: 351 */         this);
/*  186:     */     }
/*  187: 353 */     return this.lifecycleProcessor;
/*  188:     */   }
/*  189:     */   
/*  190:     */   protected ResourcePatternResolver getResourcePatternResolver()
/*  191:     */   {
/*  192: 371 */     return new PathMatchingResourcePatternResolver(this);
/*  193:     */   }
/*  194:     */   
/*  195:     */   public void setParent(ApplicationContext parent)
/*  196:     */   {
/*  197: 386 */     this.parent = parent;
/*  198: 387 */     if ((parent instanceof ConfigurableApplicationContext)) {
/*  199: 388 */       setEnvironment(((ConfigurableApplicationContext)parent).getEnvironment());
/*  200:     */     }
/*  201:     */   }
/*  202:     */   
/*  203:     */   public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor)
/*  204:     */   {
/*  205: 393 */     this.beanFactoryPostProcessors.add(beanFactoryPostProcessor);
/*  206:     */   }
/*  207:     */   
/*  208:     */   public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors()
/*  209:     */   {
/*  210: 402 */     return this.beanFactoryPostProcessors;
/*  211:     */   }
/*  212:     */   
/*  213:     */   public void addApplicationListener(ApplicationListener<?> listener)
/*  214:     */   {
/*  215: 406 */     if (this.applicationEventMulticaster != null) {
/*  216: 407 */       this.applicationEventMulticaster.addApplicationListener(listener);
/*  217:     */     } else {
/*  218: 410 */       this.applicationListeners.add(listener);
/*  219:     */     }
/*  220:     */   }
/*  221:     */   
/*  222:     */   public Collection<ApplicationListener<?>> getApplicationListeners()
/*  223:     */   {
/*  224: 418 */     return this.applicationListeners;
/*  225:     */   }
/*  226:     */   
/*  227:     */   protected ConfigurableEnvironment createEnvironment()
/*  228:     */   {
/*  229: 427 */     return new StandardEnvironment();
/*  230:     */   }
/*  231:     */   
/*  232:     */   public void refresh()
/*  233:     */     throws BeansException, IllegalStateException
/*  234:     */   {
/*  235: 431 */     synchronized (this.startupShutdownMonitor)
/*  236:     */     {
/*  237: 433 */       prepareRefresh();
/*  238:     */       
/*  239:     */ 
/*  240: 436 */       ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
/*  241:     */       
/*  242:     */ 
/*  243: 439 */       prepareBeanFactory(beanFactory);
/*  244:     */       try
/*  245:     */       {
/*  246: 443 */         postProcessBeanFactory(beanFactory);
/*  247:     */         
/*  248:     */ 
/*  249: 446 */         invokeBeanFactoryPostProcessors(beanFactory);
/*  250:     */         
/*  251:     */ 
/*  252: 449 */         registerBeanPostProcessors(beanFactory);
/*  253:     */         
/*  254:     */ 
/*  255: 452 */         initMessageSource();
/*  256:     */         
/*  257:     */ 
/*  258: 455 */         initApplicationEventMulticaster();
/*  259:     */         
/*  260:     */ 
/*  261: 458 */         onRefresh();
/*  262:     */         
/*  263:     */ 
/*  264: 461 */         registerListeners();
/*  265:     */         
/*  266:     */ 
/*  267: 464 */         finishBeanFactoryInitialization(beanFactory);
/*  268:     */         
/*  269:     */ 
/*  270: 467 */         finishRefresh();
/*  271:     */       }
/*  272:     */       catch (BeansException ex)
/*  273:     */       {
/*  274: 472 */         destroyBeans();
/*  275:     */         
/*  276:     */ 
/*  277: 475 */         cancelRefresh(ex);
/*  278:     */         
/*  279:     */ 
/*  280: 478 */         throw ex;
/*  281:     */       }
/*  282:     */     }
/*  283:     */   }
/*  284:     */   
/*  285:     */   protected void prepareRefresh()
/*  286:     */   {
/*  287: 488 */     this.startupDate = System.currentTimeMillis();
/*  288: 490 */     synchronized (this.activeMonitor)
/*  289:     */     {
/*  290: 491 */       this.active = true;
/*  291:     */     }
/*  292: 494 */     if (this.logger.isInfoEnabled()) {
/*  293: 495 */       this.logger.info("Refreshing " + this);
/*  294:     */     }
/*  295: 499 */     initPropertySources();
/*  296:     */     
/*  297:     */ 
/*  298:     */ 
/*  299: 503 */     this.environment.validateRequiredProperties();
/*  300:     */   }
/*  301:     */   
/*  302:     */   protected void initPropertySources() {}
/*  303:     */   
/*  304:     */   protected ConfigurableListableBeanFactory obtainFreshBeanFactory()
/*  305:     */   {
/*  306: 522 */     refreshBeanFactory();
/*  307: 523 */     ConfigurableListableBeanFactory beanFactory = getBeanFactory();
/*  308: 524 */     if (this.logger.isDebugEnabled()) {
/*  309: 525 */       this.logger.debug("Bean factory for " + getDisplayName() + ": " + beanFactory);
/*  310:     */     }
/*  311: 527 */     return beanFactory;
/*  312:     */   }
/*  313:     */   
/*  314:     */   protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*  315:     */   {
/*  316: 537 */     beanFactory.setBeanClassLoader(getClassLoader());
/*  317: 538 */     beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver());
/*  318: 539 */     beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));
/*  319:     */     
/*  320:     */ 
/*  321: 542 */     beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
/*  322: 543 */     beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
/*  323: 544 */     beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
/*  324: 545 */     beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
/*  325: 546 */     beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);
/*  326: 547 */     beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
/*  327:     */     
/*  328:     */ 
/*  329:     */ 
/*  330: 551 */     beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
/*  331: 552 */     beanFactory.registerResolvableDependency(ResourceLoader.class, this);
/*  332: 553 */     beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
/*  333: 554 */     beanFactory.registerResolvableDependency(ApplicationContext.class, this);
/*  334: 557 */     if (beanFactory.containsBean("loadTimeWeaver"))
/*  335:     */     {
/*  336: 558 */       beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
/*  337:     */       
/*  338: 560 */       beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
/*  339:     */     }
/*  340: 564 */     if (!beanFactory.containsBean("environment")) {
/*  341: 565 */       beanFactory.registerSingleton("environment", getEnvironment());
/*  342:     */     }
/*  343: 568 */     if (!beanFactory.containsBean("systemProperties")) {
/*  344: 569 */       beanFactory.registerSingleton("systemProperties", getEnvironment().getSystemProperties());
/*  345:     */     }
/*  346: 572 */     if (!beanFactory.containsBean("systemEnvironment")) {
/*  347: 573 */       beanFactory.registerSingleton("systemEnvironment", getEnvironment().getSystemEnvironment());
/*  348:     */     }
/*  349:     */   }
/*  350:     */   
/*  351:     */   protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {}
/*  352:     */   
/*  353:     */   protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory)
/*  354:     */   {
/*  355: 594 */     Set<String> processedBeans = new HashSet();
/*  356: 595 */     if ((beanFactory instanceof BeanDefinitionRegistry))
/*  357:     */     {
/*  358: 596 */       BeanDefinitionRegistry registry = (BeanDefinitionRegistry)beanFactory;
/*  359: 597 */       List<BeanFactoryPostProcessor> regularPostProcessors = new LinkedList();
/*  360: 598 */       List<BeanDefinitionRegistryPostProcessor> registryPostProcessors = 
/*  361: 599 */         new LinkedList();
/*  362: 600 */       for (BeanFactoryPostProcessor postProcessor : getBeanFactoryPostProcessors()) {
/*  363: 601 */         if ((postProcessor instanceof BeanDefinitionRegistryPostProcessor))
/*  364:     */         {
/*  365: 602 */           BeanDefinitionRegistryPostProcessor registryPostProcessor = 
/*  366: 603 */             (BeanDefinitionRegistryPostProcessor)postProcessor;
/*  367: 604 */           registryPostProcessor.postProcessBeanDefinitionRegistry(registry);
/*  368: 605 */           registryPostProcessors.add(registryPostProcessor);
/*  369:     */         }
/*  370:     */         else
/*  371:     */         {
/*  372: 608 */           regularPostProcessors.add(postProcessor);
/*  373:     */         }
/*  374:     */       }
/*  375: 611 */       Map<String, BeanDefinitionRegistryPostProcessor> beanMap = 
/*  376: 612 */         beanFactory.getBeansOfType(BeanDefinitionRegistryPostProcessor.class, true, false);
/*  377: 613 */       Object registryPostProcessorBeans = 
/*  378: 614 */         new ArrayList(beanMap.values());
/*  379: 615 */       OrderComparator.sort((List)registryPostProcessorBeans);
/*  380: 616 */       for (Iterator localIterator2 = ((List)registryPostProcessorBeans).iterator(); localIterator2.hasNext();)
/*  381:     */       {
/*  382: 616 */         postProcessor = (BeanDefinitionRegistryPostProcessor)localIterator2.next();
/*  383: 617 */         postProcessor.postProcessBeanDefinitionRegistry(registry);
/*  384:     */       }
/*  385: 619 */       invokeBeanFactoryPostProcessors(registryPostProcessors, beanFactory);
/*  386: 620 */       invokeBeanFactoryPostProcessors((Collection)registryPostProcessorBeans, beanFactory);
/*  387: 621 */       invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
/*  388: 622 */       processedBeans.addAll((Collection)beanMap.keySet());
/*  389:     */     }
/*  390:     */     else
/*  391:     */     {
/*  392: 626 */       invokeBeanFactoryPostProcessors(getBeanFactoryPostProcessors(), beanFactory);
/*  393:     */     }
/*  394: 631 */     String[] postProcessorNames = 
/*  395: 632 */       beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);
/*  396:     */     
/*  397:     */ 
/*  398:     */ 
/*  399: 636 */     List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList();
/*  400: 637 */     List<String> orderedPostProcessorNames = new ArrayList();
/*  401: 638 */     List<String> nonOrderedPostProcessorNames = new ArrayList();
/*  402: 639 */     Object localObject1 = (localObject2 = postProcessorNames).length;
/*  403: 639 */     for (BeanDefinitionRegistryPostProcessor postProcessor = 0; postProcessor < localObject1; postProcessor++)
/*  404:     */     {
/*  405: 639 */       String ppName = localObject2[postProcessor];
/*  406: 640 */       if (!processedBeans.contains(ppName)) {
/*  407: 643 */         if (isTypeMatch(ppName, PriorityOrdered.class)) {
/*  408: 644 */           priorityOrderedPostProcessors.add((BeanFactoryPostProcessor)beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
/*  409: 646 */         } else if (isTypeMatch(ppName, Ordered.class)) {
/*  410: 647 */           orderedPostProcessorNames.add(ppName);
/*  411:     */         } else {
/*  412: 650 */           nonOrderedPostProcessorNames.add(ppName);
/*  413:     */         }
/*  414:     */       }
/*  415:     */     }
/*  416: 655 */     OrderComparator.sort(priorityOrderedPostProcessors);
/*  417: 656 */     invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);
/*  418:     */     
/*  419:     */ 
/*  420: 659 */     Object orderedPostProcessors = new ArrayList();
/*  421: 660 */     for (localObject1 = orderedPostProcessorNames.iterator(); ((Iterator)localObject1).hasNext();)
/*  422:     */     {
/*  423: 660 */       String postProcessorName = (String)((Iterator)localObject1).next();
/*  424: 661 */       ((List)orderedPostProcessors).add((BeanFactoryPostProcessor)getBean(postProcessorName, BeanFactoryPostProcessor.class));
/*  425:     */     }
/*  426: 663 */     OrderComparator.sort((List)orderedPostProcessors);
/*  427: 664 */     invokeBeanFactoryPostProcessors((Collection)orderedPostProcessors, beanFactory);
/*  428:     */     
/*  429:     */ 
/*  430: 667 */     List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList();
/*  431: 668 */     for (Object localObject2 = nonOrderedPostProcessorNames.iterator(); ((Iterator)localObject2).hasNext();)
/*  432:     */     {
/*  433: 668 */       String postProcessorName = (String)((Iterator)localObject2).next();
/*  434: 669 */       nonOrderedPostProcessors.add((BeanFactoryPostProcessor)getBean(postProcessorName, BeanFactoryPostProcessor.class));
/*  435:     */     }
/*  436: 671 */     invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);
/*  437:     */   }
/*  438:     */   
/*  439:     */   private void invokeBeanFactoryPostProcessors(Collection<? extends BeanFactoryPostProcessor> postProcessors, ConfigurableListableBeanFactory beanFactory)
/*  440:     */   {
/*  441: 680 */     for (BeanFactoryPostProcessor postProcessor : postProcessors) {
/*  442: 681 */       postProcessor.postProcessBeanFactory(beanFactory);
/*  443:     */     }
/*  444:     */   }
/*  445:     */   
/*  446:     */   protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory)
/*  447:     */   {
/*  448: 691 */     String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);
/*  449:     */     
/*  450:     */ 
/*  451:     */ 
/*  452:     */ 
/*  453: 696 */     int beanProcessorTargetCount = beanFactory.getBeanPostProcessorCount() + 1 + postProcessorNames.length;
/*  454: 697 */     beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));
/*  455:     */     
/*  456:     */ 
/*  457:     */ 
/*  458: 701 */     List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList();
/*  459: 702 */     List<BeanPostProcessor> internalPostProcessors = new ArrayList();
/*  460: 703 */     List<String> orderedPostProcessorNames = new ArrayList();
/*  461: 704 */     List<String> nonOrderedPostProcessorNames = new ArrayList();
/*  462: 705 */     for (String ppName : postProcessorNames) {
/*  463: 706 */       if (isTypeMatch(ppName, PriorityOrdered.class))
/*  464:     */       {
/*  465: 707 */         BeanPostProcessor pp = (BeanPostProcessor)beanFactory.getBean(ppName, BeanPostProcessor.class);
/*  466: 708 */         priorityOrderedPostProcessors.add(pp);
/*  467: 709 */         if ((pp instanceof MergedBeanDefinitionPostProcessor)) {
/*  468: 710 */           internalPostProcessors.add(pp);
/*  469:     */         }
/*  470:     */       }
/*  471: 713 */       else if (isTypeMatch(ppName, Ordered.class))
/*  472:     */       {
/*  473: 714 */         orderedPostProcessorNames.add(ppName);
/*  474:     */       }
/*  475:     */       else
/*  476:     */       {
/*  477: 717 */         nonOrderedPostProcessorNames.add(ppName);
/*  478:     */       }
/*  479:     */     }
/*  480: 722 */     OrderComparator.sort(priorityOrderedPostProcessors);
/*  481: 723 */     registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);
/*  482:     */     
/*  483:     */ 
/*  484: 726 */     List<BeanPostProcessor> orderedPostProcessors = new ArrayList();
/*  485: 727 */     for (String ppName : orderedPostProcessorNames)
/*  486:     */     {
/*  487: 728 */       pp = (BeanPostProcessor)beanFactory.getBean(ppName, BeanPostProcessor.class);
/*  488: 729 */       orderedPostProcessors.add(pp);
/*  489: 730 */       if ((pp instanceof MergedBeanDefinitionPostProcessor)) {
/*  490: 731 */         internalPostProcessors.add(pp);
/*  491:     */       }
/*  492:     */     }
/*  493: 734 */     OrderComparator.sort(orderedPostProcessors);
/*  494: 735 */     registerBeanPostProcessors(beanFactory, orderedPostProcessors);
/*  495:     */     
/*  496:     */ 
/*  497: 738 */     Object nonOrderedPostProcessors = new ArrayList();
/*  498: 739 */     for (Object pp = nonOrderedPostProcessorNames.iterator(); ((Iterator)pp).hasNext();)
/*  499:     */     {
/*  500: 739 */       String ppName = (String)((Iterator)pp).next();
/*  501: 740 */       BeanPostProcessor pp = (BeanPostProcessor)beanFactory.getBean(ppName, BeanPostProcessor.class);
/*  502: 741 */       ((List)nonOrderedPostProcessors).add(pp);
/*  503: 742 */       if ((pp instanceof MergedBeanDefinitionPostProcessor)) {
/*  504: 743 */         internalPostProcessors.add(pp);
/*  505:     */       }
/*  506:     */     }
/*  507: 746 */     registerBeanPostProcessors(beanFactory, (List)nonOrderedPostProcessors);
/*  508:     */     
/*  509:     */ 
/*  510: 749 */     OrderComparator.sort(internalPostProcessors);
/*  511: 750 */     registerBeanPostProcessors(beanFactory, internalPostProcessors);
/*  512:     */     
/*  513: 752 */     beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(null));
/*  514:     */   }
/*  515:     */   
/*  516:     */   private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory, List<BeanPostProcessor> postProcessors)
/*  517:     */   {
/*  518: 761 */     for (BeanPostProcessor postProcessor : postProcessors) {
/*  519: 762 */       beanFactory.addBeanPostProcessor(postProcessor);
/*  520:     */     }
/*  521:     */   }
/*  522:     */   
/*  523:     */   protected void initMessageSource()
/*  524:     */   {
/*  525: 771 */     ConfigurableListableBeanFactory beanFactory = getBeanFactory();
/*  526: 772 */     if (beanFactory.containsLocalBean("messageSource"))
/*  527:     */     {
/*  528: 773 */       this.messageSource = ((MessageSource)beanFactory.getBean("messageSource", MessageSource.class));
/*  529: 775 */       if ((this.parent != null) && ((this.messageSource instanceof HierarchicalMessageSource)))
/*  530:     */       {
/*  531: 776 */         HierarchicalMessageSource hms = (HierarchicalMessageSource)this.messageSource;
/*  532: 777 */         if (hms.getParentMessageSource() == null) {
/*  533: 780 */           hms.setParentMessageSource(getInternalParentMessageSource());
/*  534:     */         }
/*  535:     */       }
/*  536: 783 */       if (this.logger.isDebugEnabled()) {
/*  537: 784 */         this.logger.debug("Using MessageSource [" + this.messageSource + "]");
/*  538:     */       }
/*  539:     */     }
/*  540:     */     else
/*  541:     */     {
/*  542: 789 */       DelegatingMessageSource dms = new DelegatingMessageSource();
/*  543: 790 */       dms.setParentMessageSource(getInternalParentMessageSource());
/*  544: 791 */       this.messageSource = dms;
/*  545: 792 */       beanFactory.registerSingleton("messageSource", this.messageSource);
/*  546: 793 */       if (this.logger.isDebugEnabled()) {
/*  547: 794 */         this.logger.debug("Unable to locate MessageSource with name 'messageSource': using default [" + 
/*  548: 795 */           this.messageSource + "]");
/*  549:     */       }
/*  550:     */     }
/*  551:     */   }
/*  552:     */   
/*  553:     */   protected void initApplicationEventMulticaster()
/*  554:     */   {
/*  555: 806 */     ConfigurableListableBeanFactory beanFactory = getBeanFactory();
/*  556: 807 */     if (beanFactory.containsLocalBean("applicationEventMulticaster"))
/*  557:     */     {
/*  558: 808 */       this.applicationEventMulticaster = 
/*  559: 809 */         ((ApplicationEventMulticaster)beanFactory.getBean("applicationEventMulticaster", ApplicationEventMulticaster.class));
/*  560: 810 */       if (this.logger.isDebugEnabled()) {
/*  561: 811 */         this.logger.debug("Using ApplicationEventMulticaster [" + this.applicationEventMulticaster + "]");
/*  562:     */       }
/*  563:     */     }
/*  564:     */     else
/*  565:     */     {
/*  566: 815 */       this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
/*  567: 816 */       beanFactory.registerSingleton("applicationEventMulticaster", this.applicationEventMulticaster);
/*  568: 817 */       if (this.logger.isDebugEnabled()) {
/*  569: 818 */         this.logger.debug("Unable to locate ApplicationEventMulticaster with name 'applicationEventMulticaster': using default [" + 
/*  570:     */         
/*  571: 820 */           this.applicationEventMulticaster + "]");
/*  572:     */       }
/*  573:     */     }
/*  574:     */   }
/*  575:     */   
/*  576:     */   protected void initLifecycleProcessor()
/*  577:     */   {
/*  578: 831 */     ConfigurableListableBeanFactory beanFactory = getBeanFactory();
/*  579: 832 */     if (beanFactory.containsLocalBean("lifecycleProcessor"))
/*  580:     */     {
/*  581: 833 */       this.lifecycleProcessor = 
/*  582: 834 */         ((LifecycleProcessor)beanFactory.getBean("lifecycleProcessor", LifecycleProcessor.class));
/*  583: 835 */       if (this.logger.isDebugEnabled()) {
/*  584: 836 */         this.logger.debug("Using LifecycleProcessor [" + this.lifecycleProcessor + "]");
/*  585:     */       }
/*  586:     */     }
/*  587:     */     else
/*  588:     */     {
/*  589: 840 */       DefaultLifecycleProcessor defaultProcessor = new DefaultLifecycleProcessor();
/*  590: 841 */       defaultProcessor.setBeanFactory(beanFactory);
/*  591: 842 */       this.lifecycleProcessor = defaultProcessor;
/*  592: 843 */       beanFactory.registerSingleton("lifecycleProcessor", this.lifecycleProcessor);
/*  593: 844 */       if (this.logger.isDebugEnabled()) {
/*  594: 845 */         this.logger.debug("Unable to locate LifecycleProcessor with name 'lifecycleProcessor': using default [" + 
/*  595:     */         
/*  596: 847 */           this.lifecycleProcessor + "]");
/*  597:     */       }
/*  598:     */     }
/*  599:     */   }
/*  600:     */   
/*  601:     */   protected void onRefresh()
/*  602:     */     throws BeansException
/*  603:     */   {}
/*  604:     */   
/*  605:     */   protected void registerListeners()
/*  606:     */   {
/*  607: 869 */     for (ApplicationListener<?> listener : getApplicationListeners()) {
/*  608: 870 */       getApplicationEventMulticaster().addApplicationListener(listener);
/*  609:     */     }
/*  610: 874 */     String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
/*  611: 875 */     for (String lisName : listenerBeanNames) {
/*  612: 876 */       getApplicationEventMulticaster().addApplicationListenerBean(lisName);
/*  613:     */     }
/*  614:     */   }
/*  615:     */   
/*  616:     */   @Deprecated
/*  617:     */   protected void addListener(ApplicationListener<?> listener)
/*  618:     */   {
/*  619: 891 */     getApplicationEventMulticaster().addApplicationListener(listener);
/*  620:     */   }
/*  621:     */   
/*  622:     */   protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory)
/*  623:     */   {
/*  624: 900 */     if ((beanFactory.containsBean("conversionService")) && 
/*  625: 901 */       (beanFactory.isTypeMatch("conversionService", ConversionService.class))) {
/*  626: 902 */       beanFactory.setConversionService(
/*  627: 903 */         (ConversionService)beanFactory.getBean("conversionService", ConversionService.class));
/*  628:     */     }
/*  629: 907 */     beanFactory.setTempClassLoader(null);
/*  630:     */     
/*  631:     */ 
/*  632: 910 */     beanFactory.freezeConfiguration();
/*  633:     */     
/*  634:     */ 
/*  635: 913 */     beanFactory.preInstantiateSingletons();
/*  636:     */   }
/*  637:     */   
/*  638:     */   protected void finishRefresh()
/*  639:     */   {
/*  640: 923 */     initLifecycleProcessor();
/*  641:     */     
/*  642:     */ 
/*  643: 926 */     getLifecycleProcessor().onRefresh();
/*  644:     */     
/*  645:     */ 
/*  646: 929 */     publishEvent(new ContextRefreshedEvent(this));
/*  647:     */   }
/*  648:     */   
/*  649:     */   protected void cancelRefresh(BeansException ex)
/*  650:     */   {
/*  651: 938 */     synchronized (this.activeMonitor)
/*  652:     */     {
/*  653: 939 */       this.active = false;
/*  654:     */     }
/*  655:     */   }
/*  656:     */   
/*  657:     */   public void registerShutdownHook()
/*  658:     */   {
/*  659: 953 */     if (this.shutdownHook == null)
/*  660:     */     {
/*  661: 955 */       this.shutdownHook = new Thread()
/*  662:     */       {
/*  663:     */         public void run()
/*  664:     */         {
/*  665: 958 */           AbstractApplicationContext.this.doClose();
/*  666:     */         }
/*  667: 960 */       };
/*  668: 961 */       Runtime.getRuntime().addShutdownHook(this.shutdownHook);
/*  669:     */     }
/*  670:     */   }
/*  671:     */   
/*  672:     */   public void destroy()
/*  673:     */   {
/*  674: 976 */     close();
/*  675:     */   }
/*  676:     */   
/*  677:     */   public void close()
/*  678:     */   {
/*  679: 987 */     synchronized (this.startupShutdownMonitor)
/*  680:     */     {
/*  681: 988 */       doClose();
/*  682: 991 */       if (this.shutdownHook != null) {
/*  683:     */         try
/*  684:     */         {
/*  685: 993 */           Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
/*  686:     */         }
/*  687:     */         catch (IllegalStateException localIllegalStateException) {}
/*  688:     */       }
/*  689:     */     }
/*  690:     */   }
/*  691:     */   
/*  692:     */   protected void doClose()
/*  693:     */   {
/*  694:1013 */     synchronized (this.activeMonitor)
/*  695:     */     {
/*  696:1014 */       boolean actuallyClose = (this.active) && (!this.closed);
/*  697:1015 */       this.closed = true;
/*  698:     */     }
/*  699:     */     boolean actuallyClose;
/*  700:1018 */     if (actuallyClose)
/*  701:     */     {
/*  702:1019 */       if (this.logger.isInfoEnabled()) {
/*  703:1020 */         this.logger.info("Closing " + this);
/*  704:     */       }
/*  705:     */       try
/*  706:     */       {
/*  707:1025 */         publishEvent(new ContextClosedEvent(this));
/*  708:     */       }
/*  709:     */       catch (Throwable ex)
/*  710:     */       {
/*  711:1028 */         this.logger.warn("Exception thrown from ApplicationListener handling ContextClosedEvent", ex);
/*  712:     */       }
/*  713:     */       try
/*  714:     */       {
/*  715:1033 */         getLifecycleProcessor().onClose();
/*  716:     */       }
/*  717:     */       catch (Throwable ex)
/*  718:     */       {
/*  719:1036 */         this.logger.warn("Exception thrown from LifecycleProcessor on context close", ex);
/*  720:     */       }
/*  721:1040 */       destroyBeans();
/*  722:     */       
/*  723:     */ 
/*  724:1043 */       closeBeanFactory();
/*  725:     */       
/*  726:     */ 
/*  727:1046 */       onClose();
/*  728:1048 */       synchronized (this.activeMonitor)
/*  729:     */       {
/*  730:1049 */         this.active = false;
/*  731:     */       }
/*  732:     */     }
/*  733:     */   }
/*  734:     */   
/*  735:     */   protected void destroyBeans()
/*  736:     */   {
/*  737:1066 */     getBeanFactory().destroySingletons();
/*  738:     */   }
/*  739:     */   
/*  740:     */   protected void onClose() {}
/*  741:     */   
/*  742:     */   public boolean isActive()
/*  743:     */   {
/*  744:1082 */     synchronized (this.activeMonitor)
/*  745:     */     {
/*  746:1083 */       return this.active;
/*  747:     */     }
/*  748:     */   }
/*  749:     */   
/*  750:     */   public Object getBean(String name)
/*  751:     */     throws BeansException
/*  752:     */   {
/*  753:1093 */     return getBeanFactory().getBean(name);
/*  754:     */   }
/*  755:     */   
/*  756:     */   public <T> T getBean(String name, Class<T> requiredType)
/*  757:     */     throws BeansException
/*  758:     */   {
/*  759:1097 */     return getBeanFactory().getBean(name, requiredType);
/*  760:     */   }
/*  761:     */   
/*  762:     */   public <T> T getBean(Class<T> requiredType)
/*  763:     */     throws BeansException
/*  764:     */   {
/*  765:1101 */     return getBeanFactory().getBean(requiredType);
/*  766:     */   }
/*  767:     */   
/*  768:     */   public Object getBean(String name, Object... args)
/*  769:     */     throws BeansException
/*  770:     */   {
/*  771:1105 */     return getBeanFactory().getBean(name, args);
/*  772:     */   }
/*  773:     */   
/*  774:     */   public boolean containsBean(String name)
/*  775:     */   {
/*  776:1109 */     return getBeanFactory().containsBean(name);
/*  777:     */   }
/*  778:     */   
/*  779:     */   public boolean isSingleton(String name)
/*  780:     */     throws NoSuchBeanDefinitionException
/*  781:     */   {
/*  782:1113 */     return getBeanFactory().isSingleton(name);
/*  783:     */   }
/*  784:     */   
/*  785:     */   public boolean isPrototype(String name)
/*  786:     */     throws NoSuchBeanDefinitionException
/*  787:     */   {
/*  788:1117 */     return getBeanFactory().isPrototype(name);
/*  789:     */   }
/*  790:     */   
/*  791:     */   public boolean isTypeMatch(String name, Class<?> targetType)
/*  792:     */     throws NoSuchBeanDefinitionException
/*  793:     */   {
/*  794:1121 */     return getBeanFactory().isTypeMatch(name, targetType);
/*  795:     */   }
/*  796:     */   
/*  797:     */   public Class<?> getType(String name)
/*  798:     */     throws NoSuchBeanDefinitionException
/*  799:     */   {
/*  800:1125 */     return getBeanFactory().getType(name);
/*  801:     */   }
/*  802:     */   
/*  803:     */   public String[] getAliases(String name)
/*  804:     */   {
/*  805:1129 */     return getBeanFactory().getAliases(name);
/*  806:     */   }
/*  807:     */   
/*  808:     */   public boolean containsBeanDefinition(String beanName)
/*  809:     */   {
/*  810:1138 */     return getBeanFactory().containsBeanDefinition(beanName);
/*  811:     */   }
/*  812:     */   
/*  813:     */   public int getBeanDefinitionCount()
/*  814:     */   {
/*  815:1142 */     return getBeanFactory().getBeanDefinitionCount();
/*  816:     */   }
/*  817:     */   
/*  818:     */   public String[] getBeanDefinitionNames()
/*  819:     */   {
/*  820:1146 */     return getBeanFactory().getBeanDefinitionNames();
/*  821:     */   }
/*  822:     */   
/*  823:     */   public String[] getBeanNamesForType(Class<?> type)
/*  824:     */   {
/*  825:1150 */     return getBeanFactory().getBeanNamesForType(type);
/*  826:     */   }
/*  827:     */   
/*  828:     */   public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit)
/*  829:     */   {
/*  830:1154 */     return getBeanFactory().getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
/*  831:     */   }
/*  832:     */   
/*  833:     */   public <T> Map<String, T> getBeansOfType(Class<T> type)
/*  834:     */     throws BeansException
/*  835:     */   {
/*  836:1158 */     return getBeanFactory().getBeansOfType(type);
/*  837:     */   }
/*  838:     */   
/*  839:     */   public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
/*  840:     */     throws BeansException
/*  841:     */   {
/*  842:1164 */     return getBeanFactory().getBeansOfType(type, includeNonSingletons, allowEagerInit);
/*  843:     */   }
/*  844:     */   
/*  845:     */   public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType)
/*  846:     */     throws BeansException
/*  847:     */   {
/*  848:1170 */     return getBeanFactory().getBeansWithAnnotation(annotationType);
/*  849:     */   }
/*  850:     */   
/*  851:     */   public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType)
/*  852:     */   {
/*  853:1174 */     return getBeanFactory().findAnnotationOnBean(beanName, annotationType);
/*  854:     */   }
/*  855:     */   
/*  856:     */   public BeanFactory getParentBeanFactory()
/*  857:     */   {
/*  858:1183 */     return getParent();
/*  859:     */   }
/*  860:     */   
/*  861:     */   public boolean containsLocalBean(String name)
/*  862:     */   {
/*  863:1187 */     return getBeanFactory().containsLocalBean(name);
/*  864:     */   }
/*  865:     */   
/*  866:     */   protected BeanFactory getInternalParentBeanFactory()
/*  867:     */   {
/*  868:1196 */     return (getParent() instanceof ConfigurableApplicationContext) ? 
/*  869:1197 */       ((ConfigurableApplicationContext)getParent()).getBeanFactory() : getParent();
/*  870:     */   }
/*  871:     */   
/*  872:     */   public String getMessage(String code, Object[] args, String defaultMessage, Locale locale)
/*  873:     */   {
/*  874:1206 */     return getMessageSource().getMessage(code, args, defaultMessage, locale);
/*  875:     */   }
/*  876:     */   
/*  877:     */   public String getMessage(String code, Object[] args, Locale locale)
/*  878:     */     throws NoSuchMessageException
/*  879:     */   {
/*  880:1210 */     return getMessageSource().getMessage(code, args, locale);
/*  881:     */   }
/*  882:     */   
/*  883:     */   public String getMessage(MessageSourceResolvable resolvable, Locale locale)
/*  884:     */     throws NoSuchMessageException
/*  885:     */   {
/*  886:1214 */     return getMessageSource().getMessage(resolvable, locale);
/*  887:     */   }
/*  888:     */   
/*  889:     */   private MessageSource getMessageSource()
/*  890:     */     throws IllegalStateException
/*  891:     */   {
/*  892:1223 */     if (this.messageSource == null) {
/*  893:1224 */       throw new IllegalStateException("MessageSource not initialized - call 'refresh' before accessing messages via the context: " + 
/*  894:1225 */         this);
/*  895:     */     }
/*  896:1227 */     return this.messageSource;
/*  897:     */   }
/*  898:     */   
/*  899:     */   protected MessageSource getInternalParentMessageSource()
/*  900:     */   {
/*  901:1235 */     return (getParent() instanceof AbstractApplicationContext) ? 
/*  902:1236 */       ((AbstractApplicationContext)getParent()).messageSource : getParent();
/*  903:     */   }
/*  904:     */   
/*  905:     */   public Resource[] getResources(String locationPattern)
/*  906:     */     throws IOException
/*  907:     */   {
/*  908:1245 */     return this.resourcePatternResolver.getResources(locationPattern);
/*  909:     */   }
/*  910:     */   
/*  911:     */   public void start()
/*  912:     */   {
/*  913:1254 */     getLifecycleProcessor().start();
/*  914:1255 */     publishEvent(new ContextStartedEvent(this));
/*  915:     */   }
/*  916:     */   
/*  917:     */   public void stop()
/*  918:     */   {
/*  919:1259 */     getLifecycleProcessor().stop();
/*  920:1260 */     publishEvent(new ContextStoppedEvent(this));
/*  921:     */   }
/*  922:     */   
/*  923:     */   public boolean isRunning()
/*  924:     */   {
/*  925:1264 */     return getLifecycleProcessor().isRunning();
/*  926:     */   }
/*  927:     */   
/*  928:     */   protected abstract void refreshBeanFactory()
/*  929:     */     throws BeansException, IllegalStateException;
/*  930:     */   
/*  931:     */   protected abstract void closeBeanFactory();
/*  932:     */   
/*  933:     */   public abstract ConfigurableListableBeanFactory getBeanFactory()
/*  934:     */     throws IllegalStateException;
/*  935:     */   
/*  936:     */   public String toString()
/*  937:     */   {
/*  938:1312 */     StringBuilder sb = new StringBuilder(getDisplayName());
/*  939:1313 */     sb.append(": startup date [").append(new Date(getStartupDate()));
/*  940:1314 */     sb.append("]; ");
/*  941:1315 */     ApplicationContext parent = getParent();
/*  942:1316 */     if (parent == null) {
/*  943:1317 */       sb.append("root of context hierarchy");
/*  944:     */     } else {
/*  945:1320 */       sb.append("parent: ").append(parent.getDisplayName());
/*  946:     */     }
/*  947:1322 */     return sb.toString();
/*  948:     */   }
/*  949:     */   
/*  950:     */   private class BeanPostProcessorChecker
/*  951:     */     implements BeanPostProcessor
/*  952:     */   {
/*  953:     */     private final ConfigurableListableBeanFactory beanFactory;
/*  954:     */     private final int beanPostProcessorTargetCount;
/*  955:     */     
/*  956:     */     public BeanPostProcessorChecker(ConfigurableListableBeanFactory beanFactory, int beanPostProcessorTargetCount)
/*  957:     */     {
/*  958:1338 */       this.beanFactory = beanFactory;
/*  959:1339 */       this.beanPostProcessorTargetCount = beanPostProcessorTargetCount;
/*  960:     */     }
/*  961:     */     
/*  962:     */     public Object postProcessBeforeInitialization(Object bean, String beanName)
/*  963:     */     {
/*  964:1343 */       return bean;
/*  965:     */     }
/*  966:     */     
/*  967:     */     public Object postProcessAfterInitialization(Object bean, String beanName)
/*  968:     */     {
/*  969:1347 */       if ((bean != null) && (!(bean instanceof BeanPostProcessor)) && 
/*  970:1348 */         (this.beanFactory.getBeanPostProcessorCount() < this.beanPostProcessorTargetCount) && 
/*  971:1349 */         (AbstractApplicationContext.this.logger.isInfoEnabled())) {
/*  972:1350 */         AbstractApplicationContext.this.logger.info("Bean '" + beanName + "' of type [" + bean.getClass() + 
/*  973:1351 */           "] is not eligible for getting processed by all BeanPostProcessors " + 
/*  974:1352 */           "(for example: not eligible for auto-proxying)");
/*  975:     */       }
/*  976:1355 */       return bean;
/*  977:     */     }
/*  978:     */   }
/*  979:     */   
/*  980:     */   private class ApplicationListenerDetector
/*  981:     */     implements MergedBeanDefinitionPostProcessor
/*  982:     */   {
/*  983:1366 */     private final Map<String, Boolean> singletonNames = new ConcurrentHashMap();
/*  984:     */     
/*  985:     */     private ApplicationListenerDetector() {}
/*  986:     */     
/*  987:     */     public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName)
/*  988:     */     {
/*  989:1369 */       if (beanDefinition.isSingleton()) {
/*  990:1370 */         this.singletonNames.put(beanName, Boolean.TRUE);
/*  991:     */       }
/*  992:     */     }
/*  993:     */     
/*  994:     */     public Object postProcessBeforeInitialization(Object bean, String beanName)
/*  995:     */     {
/*  996:1375 */       return bean;
/*  997:     */     }
/*  998:     */     
/*  999:     */     public Object postProcessAfterInitialization(Object bean, String beanName)
/* 1000:     */     {
/* 1001:1379 */       if ((bean instanceof ApplicationListener))
/* 1002:     */       {
/* 1003:1381 */         Boolean flag = (Boolean)this.singletonNames.get(beanName);
/* 1004:1382 */         if (Boolean.TRUE.equals(flag))
/* 1005:     */         {
/* 1006:1384 */           AbstractApplicationContext.this.addApplicationListener((ApplicationListener)bean);
/* 1007:     */         }
/* 1008:1386 */         else if (flag == null)
/* 1009:     */         {
/* 1010:1387 */           if ((AbstractApplicationContext.this.logger.isWarnEnabled()) && (!AbstractApplicationContext.this.containsBean(beanName))) {
/* 1011:1389 */             AbstractApplicationContext.this.logger.warn("Inner bean '" + beanName + "' implements ApplicationListener interface " + 
/* 1012:1390 */               "but is not reachable for event multicasting by its containing ApplicationContext " + 
/* 1013:1391 */               "because it does not have singleton scope. Only top-level listener beans are allowed " + 
/* 1014:1392 */               "to be of non-singleton scope.");
/* 1015:     */           }
/* 1016:1394 */           this.singletonNames.put(beanName, Boolean.FALSE);
/* 1017:     */         }
/* 1018:     */       }
/* 1019:1397 */       return bean;
/* 1020:     */     }
/* 1021:     */   }
/* 1022:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.AbstractApplicationContext
 * JD-Core Version:    0.7.0.1
 */