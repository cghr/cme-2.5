/*    1:     */ package org.springframework.jmx.export;
/*    2:     */ 
/*    3:     */ import java.util.ArrayList;
/*    4:     */ import java.util.Arrays;
/*    5:     */ import java.util.Collection;
/*    6:     */ import java.util.HashMap;
/*    7:     */ import java.util.HashSet;
/*    8:     */ import java.util.Iterator;
/*    9:     */ import java.util.LinkedHashMap;
/*   10:     */ import java.util.LinkedHashSet;
/*   11:     */ import java.util.List;
/*   12:     */ import java.util.Map;
/*   13:     */ import java.util.Map.Entry;
/*   14:     */ import java.util.Set;
/*   15:     */ import javax.management.DynamicMBean;
/*   16:     */ import javax.management.JMException;
/*   17:     */ import javax.management.MBeanException;
/*   18:     */ import javax.management.MBeanServer;
/*   19:     */ import javax.management.MalformedObjectNameException;
/*   20:     */ import javax.management.NotCompliantMBeanException;
/*   21:     */ import javax.management.NotificationListener;
/*   22:     */ import javax.management.ObjectName;
/*   23:     */ import javax.management.StandardMBean;
/*   24:     */ import javax.management.modelmbean.ModelMBean;
/*   25:     */ import javax.management.modelmbean.ModelMBeanInfo;
/*   26:     */ import javax.management.modelmbean.RequiredModelMBean;
/*   27:     */ import org.apache.commons.logging.Log;
/*   28:     */ import org.springframework.aop.framework.ProxyFactory;
/*   29:     */ import org.springframework.aop.support.AopUtils;
/*   30:     */ import org.springframework.aop.target.LazyInitTargetSource;
/*   31:     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*   32:     */ import org.springframework.beans.factory.BeanFactory;
/*   33:     */ import org.springframework.beans.factory.BeanFactoryAware;
/*   34:     */ import org.springframework.beans.factory.CannotLoadBeanClassException;
/*   35:     */ import org.springframework.beans.factory.DisposableBean;
/*   36:     */ import org.springframework.beans.factory.InitializingBean;
/*   37:     */ import org.springframework.beans.factory.ListableBeanFactory;
/*   38:     */ import org.springframework.beans.factory.config.BeanDefinition;
/*   39:     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*   40:     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*   41:     */ import org.springframework.core.Constants;
/*   42:     */ import org.springframework.jmx.export.assembler.AutodetectCapableMBeanInfoAssembler;
/*   43:     */ import org.springframework.jmx.export.assembler.MBeanInfoAssembler;
/*   44:     */ import org.springframework.jmx.export.assembler.SimpleReflectiveMBeanInfoAssembler;
/*   45:     */ import org.springframework.jmx.export.naming.KeyNamingStrategy;
/*   46:     */ import org.springframework.jmx.export.naming.ObjectNamingStrategy;
/*   47:     */ import org.springframework.jmx.export.naming.SelfNaming;
/*   48:     */ import org.springframework.jmx.export.notification.ModelMBeanNotificationPublisher;
/*   49:     */ import org.springframework.jmx.export.notification.NotificationPublisherAware;
/*   50:     */ import org.springframework.jmx.support.JmxUtils;
/*   51:     */ import org.springframework.jmx.support.MBeanRegistrationSupport;
/*   52:     */ import org.springframework.util.Assert;
/*   53:     */ import org.springframework.util.ClassUtils;
/*   54:     */ import org.springframework.util.CollectionUtils;
/*   55:     */ import org.springframework.util.ObjectUtils;
/*   56:     */ 
/*   57:     */ public class MBeanExporter
/*   58:     */   extends MBeanRegistrationSupport
/*   59:     */   implements MBeanExportOperations, BeanClassLoaderAware, BeanFactoryAware, InitializingBean, DisposableBean
/*   60:     */ {
/*   61:     */   public static final int AUTODETECT_NONE = 0;
/*   62:     */   public static final int AUTODETECT_MBEAN = 1;
/*   63:     */   public static final int AUTODETECT_ASSEMBLER = 2;
/*   64:     */   public static final int AUTODETECT_ALL = 3;
/*   65:     */   private static final String WILDCARD = "*";
/*   66:     */   private static final String MR_TYPE_OBJECT_REFERENCE = "ObjectReference";
/*   67:     */   private static final String CONSTANT_PREFIX_AUTODETECT = "AUTODETECT_";
/*   68: 138 */   private static final Constants constants = new Constants(MBeanExporter.class);
/*   69:     */   private Map<String, Object> beans;
/*   70:     */   private Integer autodetectMode;
/*   71: 147 */   private boolean allowEagerInit = false;
/*   72: 150 */   private boolean ensureUniqueRuntimeObjectNames = true;
/*   73: 153 */   private boolean exposeManagedResourceClassLoader = true;
/*   74:     */   private Set<String> excludedBeans;
/*   75:     */   private MBeanExporterListener[] listeners;
/*   76:     */   private NotificationListenerBean[] notificationListeners;
/*   77: 166 */   private final Map<NotificationListenerBean, ObjectName[]> registeredNotificationListeners = new LinkedHashMap();
/*   78: 169 */   private MBeanInfoAssembler assembler = new SimpleReflectiveMBeanInfoAssembler();
/*   79: 172 */   private ObjectNamingStrategy namingStrategy = new KeyNamingStrategy();
/*   80: 175 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*   81:     */   private ListableBeanFactory beanFactory;
/*   82:     */   
/*   83:     */   public void setBeans(Map<String, Object> beans)
/*   84:     */   {
/*   85: 199 */     this.beans = beans;
/*   86:     */   }
/*   87:     */   
/*   88:     */   public void setAutodetect(boolean autodetect)
/*   89:     */   {
/*   90: 213 */     this.autodetectMode = Integer.valueOf(autodetect ? 3 : 0);
/*   91:     */   }
/*   92:     */   
/*   93:     */   public void setAutodetectMode(int autodetectMode)
/*   94:     */   {
/*   95: 227 */     if (!constants.getValues("AUTODETECT_").contains(Integer.valueOf(autodetectMode))) {
/*   96: 228 */       throw new IllegalArgumentException("Only values of autodetect constants allowed");
/*   97:     */     }
/*   98: 230 */     this.autodetectMode = Integer.valueOf(autodetectMode);
/*   99:     */   }
/*  100:     */   
/*  101:     */   public void setAutodetectModeName(String constantName)
/*  102:     */   {
/*  103: 244 */     if ((constantName == null) || (!constantName.startsWith("AUTODETECT_"))) {
/*  104: 245 */       throw new IllegalArgumentException("Only autodetect constants allowed");
/*  105:     */     }
/*  106: 247 */     this.autodetectMode = ((Integer)constants.asNumber(constantName));
/*  107:     */   }
/*  108:     */   
/*  109:     */   public void setAllowEagerInit(boolean allowEagerInit)
/*  110:     */   {
/*  111: 258 */     this.allowEagerInit = allowEagerInit;
/*  112:     */   }
/*  113:     */   
/*  114:     */   public void setAssembler(MBeanInfoAssembler assembler)
/*  115:     */   {
/*  116: 273 */     this.assembler = assembler;
/*  117:     */   }
/*  118:     */   
/*  119:     */   public void setNamingStrategy(ObjectNamingStrategy namingStrategy)
/*  120:     */   {
/*  121: 283 */     this.namingStrategy = namingStrategy;
/*  122:     */   }
/*  123:     */   
/*  124:     */   public void setListeners(MBeanExporterListener[] listeners)
/*  125:     */   {
/*  126: 292 */     this.listeners = listeners;
/*  127:     */   }
/*  128:     */   
/*  129:     */   public void setExcludedBeans(String[] excludedBeans)
/*  130:     */   {
/*  131: 299 */     this.excludedBeans = (excludedBeans != null ? new HashSet((Collection)Arrays.asList(excludedBeans)) : null);
/*  132:     */   }
/*  133:     */   
/*  134:     */   public void setEnsureUniqueRuntimeObjectNames(boolean ensureUniqueRuntimeObjectNames)
/*  135:     */   {
/*  136: 312 */     this.ensureUniqueRuntimeObjectNames = ensureUniqueRuntimeObjectNames;
/*  137:     */   }
/*  138:     */   
/*  139:     */   public void setExposeManagedResourceClassLoader(boolean exposeManagedResourceClassLoader)
/*  140:     */   {
/*  141: 324 */     this.exposeManagedResourceClassLoader = exposeManagedResourceClassLoader;
/*  142:     */   }
/*  143:     */   
/*  144:     */   public void setNotificationListeners(NotificationListenerBean[] notificationListeners)
/*  145:     */   {
/*  146: 336 */     this.notificationListeners = notificationListeners;
/*  147:     */   }
/*  148:     */   
/*  149:     */   public void setNotificationListenerMappings(Map<?, ? extends NotificationListener> listeners)
/*  150:     */   {
/*  151: 354 */     Assert.notNull(listeners, "'listeners' must not be null");
/*  152: 355 */     List<NotificationListenerBean> notificationListeners = 
/*  153: 356 */       new ArrayList(listeners.size());
/*  154: 358 */     for (Map.Entry<?, ? extends NotificationListener> entry : listeners.entrySet())
/*  155:     */     {
/*  156: 360 */       NotificationListenerBean bean = new NotificationListenerBean((NotificationListener)entry.getValue());
/*  157:     */       
/*  158: 362 */       Object key = entry.getKey();
/*  159: 363 */       if ((key != null) && (!"*".equals(key))) {
/*  160: 365 */         bean.setMappedObjectName(entry.getKey());
/*  161:     */       }
/*  162: 367 */       notificationListeners.add(bean);
/*  163:     */     }
/*  164: 370 */     this.notificationListeners = 
/*  165: 371 */       ((NotificationListenerBean[])notificationListeners.toArray(new NotificationListenerBean[notificationListeners.size()]));
/*  166:     */   }
/*  167:     */   
/*  168:     */   public void setBeanClassLoader(ClassLoader classLoader)
/*  169:     */   {
/*  170: 375 */     this.beanClassLoader = classLoader;
/*  171:     */   }
/*  172:     */   
/*  173:     */   public void setBeanFactory(BeanFactory beanFactory)
/*  174:     */   {
/*  175: 387 */     if ((beanFactory instanceof ListableBeanFactory)) {
/*  176: 388 */       this.beanFactory = ((ListableBeanFactory)beanFactory);
/*  177:     */     } else {
/*  178: 391 */       this.logger.info("MBeanExporter not running in a ListableBeanFactory: autodetection of MBeans not available.");
/*  179:     */     }
/*  180:     */   }
/*  181:     */   
/*  182:     */   public void afterPropertiesSet()
/*  183:     */   {
/*  184: 408 */     if (this.server == null) {
/*  185: 409 */       this.server = JmxUtils.locateMBeanServer();
/*  186:     */     }
/*  187:     */     try
/*  188:     */     {
/*  189: 412 */       this.logger.info("Registering beans for JMX exposure on startup");
/*  190: 413 */       registerBeans();
/*  191: 414 */       registerNotificationListeners();
/*  192:     */     }
/*  193:     */     catch (RuntimeException ex)
/*  194:     */     {
/*  195: 418 */       unregisterNotificationListeners();
/*  196: 419 */       unregisterBeans();
/*  197: 420 */       throw ex;
/*  198:     */     }
/*  199:     */   }
/*  200:     */   
/*  201:     */   public void destroy()
/*  202:     */   {
/*  203: 429 */     this.logger.info("Unregistering JMX-exposed beans on shutdown");
/*  204: 430 */     unregisterNotificationListeners();
/*  205: 431 */     unregisterBeans();
/*  206:     */   }
/*  207:     */   
/*  208:     */   public ObjectName registerManagedResource(Object managedResource)
/*  209:     */     throws MBeanExportException
/*  210:     */   {
/*  211: 440 */     Assert.notNull(managedResource, "Managed resource must not be null");
/*  212:     */     try
/*  213:     */     {
/*  214: 443 */       ObjectName objectName = getObjectName(managedResource, null);
/*  215: 444 */       if (this.ensureUniqueRuntimeObjectNames) {
/*  216: 445 */         objectName = JmxUtils.appendIdentityToObjectName(objectName, managedResource);
/*  217:     */       }
/*  218:     */     }
/*  219:     */     catch (Exception ex)
/*  220:     */     {
/*  221: 449 */       throw new MBeanExportException("Unable to generate ObjectName for MBean [" + managedResource + "]", ex);
/*  222:     */     }
/*  223:     */     ObjectName objectName;
/*  224: 451 */     registerManagedResource(managedResource, objectName);
/*  225: 452 */     return objectName;
/*  226:     */   }
/*  227:     */   
/*  228:     */   public void registerManagedResource(Object managedResource, ObjectName objectName)
/*  229:     */     throws MBeanExportException
/*  230:     */   {
/*  231: 456 */     Assert.notNull(managedResource, "Managed resource must not be null");
/*  232: 457 */     Assert.notNull(objectName, "ObjectName must not be null");
/*  233:     */     try
/*  234:     */     {
/*  235: 459 */       if (isMBean(managedResource.getClass()))
/*  236:     */       {
/*  237: 460 */         doRegister(managedResource, objectName);
/*  238:     */       }
/*  239:     */       else
/*  240:     */       {
/*  241: 463 */         ModelMBean mbean = createAndConfigureMBean(managedResource, managedResource.getClass().getName());
/*  242: 464 */         doRegister(mbean, objectName);
/*  243: 465 */         injectNotificationPublisherIfNecessary(managedResource, mbean, objectName);
/*  244:     */       }
/*  245:     */     }
/*  246:     */     catch (JMException ex)
/*  247:     */     {
/*  248: 469 */       throw new UnableToRegisterMBeanException(
/*  249: 470 */         "Unable to register MBean [" + managedResource + "] with object name [" + objectName + "]", ex);
/*  250:     */     }
/*  251:     */   }
/*  252:     */   
/*  253:     */   public void unregisterManagedResource(ObjectName objectName)
/*  254:     */   {
/*  255: 475 */     Assert.notNull(objectName, "ObjectName must not be null");
/*  256: 476 */     doUnregister(objectName);
/*  257:     */   }
/*  258:     */   
/*  259:     */   protected void registerBeans()
/*  260:     */   {
/*  261: 499 */     if (this.beans == null)
/*  262:     */     {
/*  263: 500 */       this.beans = new HashMap();
/*  264: 502 */       if (this.autodetectMode == null) {
/*  265: 503 */         this.autodetectMode = Integer.valueOf(3);
/*  266:     */       }
/*  267:     */     }
/*  268: 508 */     int mode = this.autodetectMode != null ? this.autodetectMode.intValue() : 0;
/*  269: 509 */     if (mode != 0)
/*  270:     */     {
/*  271: 510 */       if (this.beanFactory == null) {
/*  272: 511 */         throw new MBeanExportException("Cannot autodetect MBeans if not running in a BeanFactory");
/*  273:     */       }
/*  274: 513 */       if ((mode == 1) || (mode == 3))
/*  275:     */       {
/*  276: 515 */         this.logger.debug("Autodetecting user-defined JMX MBeans");
/*  277: 516 */         autodetectMBeans();
/*  278:     */       }
/*  279: 519 */       if (((mode == 2) || (mode == 3)) && 
/*  280: 520 */         ((this.assembler instanceof AutodetectCapableMBeanInfoAssembler))) {
/*  281: 521 */         autodetectBeans((AutodetectCapableMBeanInfoAssembler)this.assembler);
/*  282:     */       }
/*  283:     */     }
/*  284: 525 */     if (!this.beans.isEmpty()) {
/*  285: 526 */       for (Map.Entry<String, Object> entry : this.beans.entrySet()) {
/*  286: 527 */         registerBeanNameOrInstance(entry.getValue(), (String)entry.getKey());
/*  287:     */       }
/*  288:     */     }
/*  289:     */   }
/*  290:     */   
/*  291:     */   protected boolean isBeanDefinitionLazyInit(ListableBeanFactory beanFactory, String beanName)
/*  292:     */   {
/*  293: 541 */     return ((beanFactory instanceof ConfigurableListableBeanFactory)) && (beanFactory.containsBeanDefinition(beanName)) && (((ConfigurableListableBeanFactory)beanFactory).getBeanDefinition(beanName).isLazyInit());
/*  294:     */   }
/*  295:     */   
/*  296:     */   protected ObjectName registerBeanNameOrInstance(Object mapValue, String beanKey)
/*  297:     */     throws MBeanExportException
/*  298:     */   {
/*  299:     */     try
/*  300:     */     {
/*  301:     */       ObjectName objectName;
/*  302: 566 */       if ((mapValue instanceof String))
/*  303:     */       {
/*  304: 568 */         if (this.beanFactory == null) {
/*  305: 569 */           throw new MBeanExportException("Cannot resolve bean names if not running in a BeanFactory");
/*  306:     */         }
/*  307: 571 */         String beanName = (String)mapValue;
/*  308: 572 */         if (isBeanDefinitionLazyInit(this.beanFactory, beanName))
/*  309:     */         {
/*  310: 573 */           ObjectName objectName = registerLazyInit(beanName, beanKey);
/*  311: 574 */           replaceNotificationListenerBeanNameKeysIfNecessary(beanName, objectName);
/*  312: 575 */           return objectName;
/*  313:     */         }
/*  314: 578 */         Object bean = this.beanFactory.getBean(beanName);
/*  315: 579 */         objectName = registerBeanInstance(bean, beanKey);
/*  316: 580 */         replaceNotificationListenerBeanNameKeysIfNecessary(beanName, objectName);
/*  317: 581 */         return objectName;
/*  318:     */       }
/*  319: 586 */       if (this.beanFactory != null)
/*  320:     */       {
/*  321: 587 */         Map<String, ?> beansOfSameType = 
/*  322: 588 */           this.beanFactory.getBeansOfType(mapValue.getClass(), false, this.allowEagerInit);
/*  323: 589 */         for (Map.Entry<String, ?> entry : beansOfSameType.entrySet()) {
/*  324: 590 */           if (entry.getValue() == mapValue)
/*  325:     */           {
/*  326: 591 */             String beanName = (String)entry.getKey();
/*  327: 592 */             ObjectName objectName = registerBeanInstance(mapValue, beanKey);
/*  328: 593 */             replaceNotificationListenerBeanNameKeysIfNecessary(beanName, objectName);
/*  329: 594 */             return objectName;
/*  330:     */           }
/*  331:     */         }
/*  332:     */       }
/*  333: 598 */       return registerBeanInstance(mapValue, beanKey);
/*  334:     */     }
/*  335:     */     catch (Exception ex)
/*  336:     */     {
/*  337: 602 */       throw new UnableToRegisterMBeanException(
/*  338: 603 */         "Unable to register MBean [" + mapValue + "] with key '" + beanKey + "'", ex);
/*  339:     */     }
/*  340:     */   }
/*  341:     */   
/*  342:     */   private void replaceNotificationListenerBeanNameKeysIfNecessary(String beanName, ObjectName objectName)
/*  343:     */   {
/*  344: 615 */     if (this.notificationListeners != null) {
/*  345: 616 */       for (NotificationListenerBean notificationListener : this.notificationListeners) {
/*  346: 617 */         notificationListener.replaceObjectName(beanName, objectName);
/*  347:     */       }
/*  348:     */     }
/*  349:     */   }
/*  350:     */   
/*  351:     */   private ObjectName registerBeanInstance(Object bean, String beanKey)
/*  352:     */     throws JMException
/*  353:     */   {
/*  354: 631 */     ObjectName objectName = getObjectName(bean, beanKey);
/*  355: 632 */     Object mbeanToExpose = null;
/*  356: 633 */     if (isMBean(bean.getClass()))
/*  357:     */     {
/*  358: 634 */       mbeanToExpose = bean;
/*  359:     */     }
/*  360:     */     else
/*  361:     */     {
/*  362: 637 */       DynamicMBean adaptedBean = adaptMBeanIfPossible(bean);
/*  363: 638 */       if (adaptedBean != null) {
/*  364: 639 */         mbeanToExpose = adaptedBean;
/*  365:     */       }
/*  366:     */     }
/*  367: 642 */     if (mbeanToExpose != null)
/*  368:     */     {
/*  369: 643 */       if (this.logger.isInfoEnabled()) {
/*  370: 644 */         this.logger.info("Located MBean '" + beanKey + "': registering with JMX server as MBean [" + 
/*  371: 645 */           objectName + "]");
/*  372:     */       }
/*  373: 647 */       doRegister(mbeanToExpose, objectName);
/*  374:     */     }
/*  375:     */     else
/*  376:     */     {
/*  377: 650 */       if (this.logger.isInfoEnabled()) {
/*  378: 651 */         this.logger.info("Located managed bean '" + beanKey + "': registering with JMX server as MBean [" + 
/*  379: 652 */           objectName + "]");
/*  380:     */       }
/*  381: 654 */       ModelMBean mbean = createAndConfigureMBean(bean, beanKey);
/*  382: 655 */       doRegister(mbean, objectName);
/*  383: 656 */       injectNotificationPublisherIfNecessary(bean, mbean, objectName);
/*  384:     */     }
/*  385: 658 */     return objectName;
/*  386:     */   }
/*  387:     */   
/*  388:     */   private ObjectName registerLazyInit(String beanName, String beanKey)
/*  389:     */     throws JMException
/*  390:     */   {
/*  391: 670 */     ProxyFactory proxyFactory = new ProxyFactory();
/*  392: 671 */     proxyFactory.setProxyTargetClass(true);
/*  393: 672 */     proxyFactory.setFrozen(true);
/*  394: 674 */     if (isMBean(this.beanFactory.getType(beanName)))
/*  395:     */     {
/*  396: 676 */       LazyInitTargetSource targetSource = new LazyInitTargetSource();
/*  397: 677 */       targetSource.setTargetBeanName(beanName);
/*  398: 678 */       targetSource.setBeanFactory(this.beanFactory);
/*  399: 679 */       proxyFactory.setTargetSource(targetSource);
/*  400:     */       
/*  401: 681 */       Object proxy = proxyFactory.getProxy(this.beanClassLoader);
/*  402: 682 */       ObjectName objectName = getObjectName(proxy, beanKey);
/*  403: 683 */       if (this.logger.isDebugEnabled()) {
/*  404: 684 */         this.logger.debug("Located MBean '" + beanKey + "': registering with JMX server as lazy-init MBean [" + 
/*  405: 685 */           objectName + "]");
/*  406:     */       }
/*  407: 687 */       doRegister(proxy, objectName);
/*  408: 688 */       return objectName;
/*  409:     */     }
/*  410: 693 */     NotificationPublisherAwareLazyTargetSource targetSource = new NotificationPublisherAwareLazyTargetSource(null);
/*  411: 694 */     targetSource.setTargetBeanName(beanName);
/*  412: 695 */     targetSource.setBeanFactory(this.beanFactory);
/*  413: 696 */     proxyFactory.setTargetSource(targetSource);
/*  414:     */     
/*  415: 698 */     Object proxy = proxyFactory.getProxy(this.beanClassLoader);
/*  416: 699 */     ObjectName objectName = getObjectName(proxy, beanKey);
/*  417: 700 */     if (this.logger.isDebugEnabled()) {
/*  418: 701 */       this.logger.debug("Located simple bean '" + beanKey + "': registering with JMX server as lazy-init MBean [" + 
/*  419: 702 */         objectName + "]");
/*  420:     */     }
/*  421: 704 */     ModelMBean mbean = createAndConfigureMBean(proxy, beanKey);
/*  422: 705 */     targetSource.setModelMBean(mbean);
/*  423: 706 */     targetSource.setObjectName(objectName);
/*  424: 707 */     doRegister(mbean, objectName);
/*  425: 708 */     return objectName;
/*  426:     */   }
/*  427:     */   
/*  428:     */   protected ObjectName getObjectName(Object bean, String beanKey)
/*  429:     */     throws MalformedObjectNameException
/*  430:     */   {
/*  431: 724 */     if ((bean instanceof SelfNaming)) {
/*  432: 725 */       return ((SelfNaming)bean).getObjectName();
/*  433:     */     }
/*  434: 728 */     return this.namingStrategy.getObjectName(bean, beanKey);
/*  435:     */   }
/*  436:     */   
/*  437:     */   protected boolean isMBean(Class beanClass)
/*  438:     */   {
/*  439: 743 */     return JmxUtils.isMBean(beanClass);
/*  440:     */   }
/*  441:     */   
/*  442:     */   protected DynamicMBean adaptMBeanIfPossible(Object bean)
/*  443:     */     throws JMException
/*  444:     */   {
/*  445: 756 */     Class targetClass = AopUtils.getTargetClass(bean);
/*  446: 757 */     if (targetClass != bean.getClass())
/*  447:     */     {
/*  448: 758 */       Class ifc = JmxUtils.getMXBeanInterface(targetClass);
/*  449: 759 */       if (ifc != null)
/*  450:     */       {
/*  451: 760 */         if (!ifc.isInstance(bean)) {
/*  452: 761 */           throw new NotCompliantMBeanException("Managed bean [" + bean + 
/*  453: 762 */             "] has a target class with an MXBean interface but does not expose it in the proxy");
/*  454:     */         }
/*  455: 764 */         return new StandardMBean(bean, ifc, true);
/*  456:     */       }
/*  457: 767 */       ifc = JmxUtils.getMBeanInterface(targetClass);
/*  458: 768 */       if (ifc != null)
/*  459:     */       {
/*  460: 769 */         if (!ifc.isInstance(bean)) {
/*  461: 770 */           throw new NotCompliantMBeanException("Managed bean [" + bean + 
/*  462: 771 */             "] has a target class with an MBean interface but does not expose it in the proxy");
/*  463:     */         }
/*  464: 773 */         return new StandardMBean(bean, ifc);
/*  465:     */       }
/*  466:     */     }
/*  467: 777 */     return null;
/*  468:     */   }
/*  469:     */   
/*  470:     */   protected ModelMBean createAndConfigureMBean(Object managedResource, String beanKey)
/*  471:     */     throws MBeanExportException
/*  472:     */   {
/*  473:     */     try
/*  474:     */     {
/*  475: 791 */       ModelMBean mbean = createModelMBean();
/*  476: 792 */       mbean.setModelMBeanInfo(getMBeanInfo(managedResource, beanKey));
/*  477: 793 */       mbean.setManagedResource(managedResource, "ObjectReference");
/*  478: 794 */       return mbean;
/*  479:     */     }
/*  480:     */     catch (Exception ex)
/*  481:     */     {
/*  482: 797 */       throw new MBeanExportException("Could not create ModelMBean for managed resource [" + 
/*  483: 798 */         managedResource + "] with key '" + beanKey + "'", ex);
/*  484:     */     }
/*  485:     */   }
/*  486:     */   
/*  487:     */   protected ModelMBean createModelMBean()
/*  488:     */     throws MBeanException
/*  489:     */   {
/*  490: 811 */     return this.exposeManagedResourceClassLoader ? new SpringModelMBean() : new RequiredModelMBean();
/*  491:     */   }
/*  492:     */   
/*  493:     */   private ModelMBeanInfo getMBeanInfo(Object managedBean, String beanKey)
/*  494:     */     throws JMException
/*  495:     */   {
/*  496: 819 */     ModelMBeanInfo info = this.assembler.getMBeanInfo(managedBean, beanKey);
/*  497: 820 */     if ((this.logger.isWarnEnabled()) && (ObjectUtils.isEmpty(info.getAttributes())) && 
/*  498: 821 */       (ObjectUtils.isEmpty(info.getOperations()))) {
/*  499: 822 */       this.logger.warn("Bean with key '" + beanKey + 
/*  500: 823 */         "' has been registered as an MBean but has no exposed attributes or operations");
/*  501:     */     }
/*  502: 825 */     return info;
/*  503:     */   }
/*  504:     */   
/*  505:     */   private void autodetectBeans(final AutodetectCapableMBeanInfoAssembler assembler)
/*  506:     */   {
/*  507: 842 */     autodetect(new AutodetectCallback()
/*  508:     */     {
/*  509:     */       public boolean include(Class beanClass, String beanName)
/*  510:     */       {
/*  511: 844 */         return assembler.includeBean(beanClass, beanName);
/*  512:     */       }
/*  513:     */     });
/*  514:     */   }
/*  515:     */   
/*  516:     */   private void autodetectMBeans()
/*  517:     */   {
/*  518: 854 */     autodetect(new AutodetectCallback()
/*  519:     */     {
/*  520:     */       public boolean include(Class beanClass, String beanName)
/*  521:     */       {
/*  522: 856 */         return MBeanExporter.this.isMBean(beanClass);
/*  523:     */       }
/*  524:     */     });
/*  525:     */   }
/*  526:     */   
/*  527:     */   private void autodetect(AutodetectCallback callback)
/*  528:     */   {
/*  529: 869 */     Set<String> beanNames = new LinkedHashSet(this.beanFactory.getBeanDefinitionCount());
/*  530: 870 */     beanNames.addAll((Collection)Arrays.asList(this.beanFactory.getBeanDefinitionNames()));
/*  531: 871 */     if ((this.beanFactory instanceof ConfigurableBeanFactory)) {
/*  532: 872 */       beanNames.addAll((Collection)Arrays.asList(((ConfigurableBeanFactory)this.beanFactory).getSingletonNames()));
/*  533:     */     }
/*  534: 874 */     for (String beanName : beanNames) {
/*  535: 875 */       if ((!isExcluded(beanName)) && (!isBeanDefinitionAbstract(this.beanFactory, beanName))) {
/*  536:     */         try
/*  537:     */         {
/*  538: 877 */           Class beanClass = this.beanFactory.getType(beanName);
/*  539: 878 */           if ((beanClass != null) && (callback.include(beanClass, beanName)))
/*  540:     */           {
/*  541: 879 */             boolean lazyInit = isBeanDefinitionLazyInit(this.beanFactory, beanName);
/*  542: 880 */             Object beanInstance = !lazyInit ? this.beanFactory.getBean(beanName) : null;
/*  543: 881 */             if ((!this.beans.containsValue(beanName)) && ((beanInstance == null) || 
/*  544: 882 */               (!CollectionUtils.containsInstance(this.beans.values(), beanInstance))))
/*  545:     */             {
/*  546: 884 */               this.beans.put(beanName, beanInstance != null ? beanInstance : beanName);
/*  547: 885 */               if (this.logger.isInfoEnabled()) {
/*  548: 886 */                 this.logger.info("Bean with name '" + beanName + "' has been autodetected for JMX exposure");
/*  549:     */               }
/*  550:     */             }
/*  551: 890 */             else if (this.logger.isDebugEnabled())
/*  552:     */             {
/*  553: 891 */               this.logger.debug("Bean with name '" + beanName + "' is already registered for JMX exposure");
/*  554:     */             }
/*  555:     */           }
/*  556:     */         }
/*  557:     */         catch (CannotLoadBeanClassException ex)
/*  558:     */         {
/*  559: 897 */           if (this.allowEagerInit) {
/*  560: 898 */             throw ex;
/*  561:     */           }
/*  562:     */         }
/*  563:     */       }
/*  564:     */     }
/*  565:     */   }
/*  566:     */   
/*  567:     */   private boolean isExcluded(String beanName)
/*  568:     */   {
/*  569: 913 */     return (this.excludedBeans != null) && ((this.excludedBeans.contains(beanName)) || ((beanName.startsWith("&")) && (this.excludedBeans.contains(beanName.substring("&".length())))));
/*  570:     */   }
/*  571:     */   
/*  572:     */   private boolean isBeanDefinitionAbstract(ListableBeanFactory beanFactory, String beanName)
/*  573:     */   {
/*  574: 921 */     return ((beanFactory instanceof ConfigurableListableBeanFactory)) && (beanFactory.containsBeanDefinition(beanName)) && (((ConfigurableListableBeanFactory)beanFactory).getBeanDefinition(beanName).isAbstract());
/*  575:     */   }
/*  576:     */   
/*  577:     */   private void injectNotificationPublisherIfNecessary(Object managedResource, ModelMBean modelMBean, ObjectName objectName)
/*  578:     */   {
/*  579: 936 */     if ((managedResource instanceof NotificationPublisherAware)) {
/*  580: 937 */       ((NotificationPublisherAware)managedResource).setNotificationPublisher(
/*  581: 938 */         new ModelMBeanNotificationPublisher(modelMBean, objectName, managedResource));
/*  582:     */     }
/*  583:     */   }
/*  584:     */   
/*  585:     */   private void registerNotificationListeners()
/*  586:     */     throws MBeanExportException
/*  587:     */   {
/*  588: 947 */     if (this.notificationListeners != null) {
/*  589: 948 */       for (NotificationListenerBean bean : this.notificationListeners) {
/*  590:     */         try
/*  591:     */         {
/*  592: 950 */           ObjectName[] mappedObjectNames = bean.getResolvedObjectNames();
/*  593: 951 */           if (mappedObjectNames == null) {
/*  594: 953 */             mappedObjectNames = getRegisteredObjectNames();
/*  595:     */           }
/*  596: 955 */           if (this.registeredNotificationListeners.put(bean, mappedObjectNames) == null) {
/*  597: 956 */             for (ObjectName mappedObjectName : mappedObjectNames) {
/*  598: 957 */               this.server.addNotificationListener(mappedObjectName, bean.getNotificationListener(), 
/*  599: 958 */                 bean.getNotificationFilter(), bean.getHandback());
/*  600:     */             }
/*  601:     */           }
/*  602:     */         }
/*  603:     */         catch (Exception ex)
/*  604:     */         {
/*  605: 963 */           throw new MBeanExportException("Unable to register NotificationListener", ex);
/*  606:     */         }
/*  607:     */       }
/*  608:     */     }
/*  609:     */   }
/*  610:     */   
/*  611:     */   private void unregisterNotificationListeners()
/*  612:     */   {
/*  613:     */     int j;
/*  614:     */     int i;
/*  615: 974 */     for (Iterator localIterator = this.registeredNotificationListeners.entrySet().iterator(); localIterator.hasNext(); i < j)
/*  616:     */     {
/*  617: 974 */       Map.Entry<NotificationListenerBean, ObjectName[]> entry = (Map.Entry)localIterator.next();
/*  618: 975 */       NotificationListenerBean bean = (NotificationListenerBean)entry.getKey();
/*  619: 976 */       ObjectName[] mappedObjectNames = (ObjectName[])entry.getValue();
/*  620:     */       ObjectName[] arrayOfObjectName1;
/*  621: 977 */       j = (arrayOfObjectName1 = mappedObjectNames).length;i = 0; continue;ObjectName mappedObjectName = arrayOfObjectName1[i];
/*  622:     */       try
/*  623:     */       {
/*  624: 979 */         this.server.removeNotificationListener(mappedObjectName, bean.getNotificationListener(), 
/*  625: 980 */           bean.getNotificationFilter(), bean.getHandback());
/*  626:     */       }
/*  627:     */       catch (Exception ex)
/*  628:     */       {
/*  629: 983 */         if (this.logger.isDebugEnabled()) {
/*  630: 984 */           this.logger.debug("Unable to unregister NotificationListener", ex);
/*  631:     */         }
/*  632:     */       }
/*  633: 977 */       i++;
/*  634:     */     }
/*  635: 989 */     this.registeredNotificationListeners.clear();
/*  636:     */   }
/*  637:     */   
/*  638:     */   protected void onRegister(ObjectName objectName)
/*  639:     */   {
/*  640:1004 */     notifyListenersOfRegistration(objectName);
/*  641:     */   }
/*  642:     */   
/*  643:     */   protected void onUnregister(ObjectName objectName)
/*  644:     */   {
/*  645:1019 */     notifyListenersOfUnregistration(objectName);
/*  646:     */   }
/*  647:     */   
/*  648:     */   private void notifyListenersOfRegistration(ObjectName objectName)
/*  649:     */   {
/*  650:1028 */     if (this.listeners != null) {
/*  651:1029 */       for (MBeanExporterListener listener : this.listeners) {
/*  652:1030 */         listener.mbeanRegistered(objectName);
/*  653:     */       }
/*  654:     */     }
/*  655:     */   }
/*  656:     */   
/*  657:     */   private void notifyListenersOfUnregistration(ObjectName objectName)
/*  658:     */   {
/*  659:1040 */     if (this.listeners != null) {
/*  660:1041 */       for (MBeanExporterListener listener : this.listeners) {
/*  661:1042 */         listener.mbeanUnregistered(objectName);
/*  662:     */       }
/*  663:     */     }
/*  664:     */   }
/*  665:     */   
/*  666:     */   private static abstract interface AutodetectCallback
/*  667:     */   {
/*  668:     */     public abstract boolean include(Class paramClass, String paramString);
/*  669:     */   }
/*  670:     */   
/*  671:     */   private class NotificationPublisherAwareLazyTargetSource
/*  672:     */     extends LazyInitTargetSource
/*  673:     */   {
/*  674:     */     private ModelMBean modelMBean;
/*  675:     */     private ObjectName objectName;
/*  676:     */     
/*  677:     */     private NotificationPublisherAwareLazyTargetSource() {}
/*  678:     */     
/*  679:     */     public void setModelMBean(ModelMBean modelMBean)
/*  680:     */     {
/*  681:1079 */       this.modelMBean = modelMBean;
/*  682:     */     }
/*  683:     */     
/*  684:     */     public void setObjectName(ObjectName objectName)
/*  685:     */     {
/*  686:1083 */       this.objectName = objectName;
/*  687:     */     }
/*  688:     */     
/*  689:     */     protected void postProcessTargetObject(Object targetObject)
/*  690:     */     {
/*  691:1088 */       MBeanExporter.this.injectNotificationPublisherIfNecessary(targetObject, this.modelMBean, this.objectName);
/*  692:     */     }
/*  693:     */   }
/*  694:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.MBeanExporter
 * JD-Core Version:    0.7.0.1
 */