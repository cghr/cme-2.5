/*    1:     */ package org.springframework.web.servlet;
/*    2:     */ 
/*    3:     */ import java.io.IOException;
/*    4:     */ import java.util.ArrayList;
/*    5:     */ import java.util.Collections;
/*    6:     */ import java.util.Enumeration;
/*    7:     */ import java.util.HashMap;
/*    8:     */ import java.util.HashSet;
/*    9:     */ import java.util.LinkedList;
/*   10:     */ import java.util.List;
/*   11:     */ import java.util.Locale;
/*   12:     */ import java.util.Map;
/*   13:     */ import java.util.Properties;
/*   14:     */ import java.util.Set;
/*   15:     */ import javax.servlet.ServletException;
/*   16:     */ import javax.servlet.http.HttpServletRequest;
/*   17:     */ import javax.servlet.http.HttpServletResponse;
/*   18:     */ import org.apache.commons.logging.Log;
/*   19:     */ import org.apache.commons.logging.LogFactory;
/*   20:     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*   21:     */ import org.springframework.beans.factory.BeanInitializationException;
/*   22:     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*   23:     */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*   24:     */ import org.springframework.context.ApplicationContext;
/*   25:     */ import org.springframework.context.i18n.LocaleContext;
/*   26:     */ import org.springframework.core.OrderComparator;
/*   27:     */ import org.springframework.core.io.ClassPathResource;
/*   28:     */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*   29:     */ import org.springframework.ui.context.ThemeSource;
/*   30:     */ import org.springframework.util.ClassUtils;
/*   31:     */ import org.springframework.util.StringUtils;
/*   32:     */ import org.springframework.web.context.WebApplicationContext;
/*   33:     */ import org.springframework.web.context.request.ServletWebRequest;
/*   34:     */ import org.springframework.web.multipart.MultipartException;
/*   35:     */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*   36:     */ import org.springframework.web.multipart.MultipartResolver;
/*   37:     */ import org.springframework.web.util.NestedServletException;
/*   38:     */ import org.springframework.web.util.UrlPathHelper;
/*   39:     */ import org.springframework.web.util.WebUtils;
/*   40:     */ 
/*   41:     */ public class DispatcherServlet
/*   42:     */   extends FrameworkServlet
/*   43:     */ {
/*   44:     */   public static final String MULTIPART_RESOLVER_BEAN_NAME = "multipartResolver";
/*   45:     */   public static final String LOCALE_RESOLVER_BEAN_NAME = "localeResolver";
/*   46:     */   public static final String THEME_RESOLVER_BEAN_NAME = "themeResolver";
/*   47:     */   public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";
/*   48:     */   public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";
/*   49:     */   public static final String HANDLER_EXCEPTION_RESOLVER_BEAN_NAME = "handlerExceptionResolver";
/*   50:     */   public static final String REQUEST_TO_VIEW_NAME_TRANSLATOR_BEAN_NAME = "viewNameTranslator";
/*   51:     */   public static final String VIEW_RESOLVER_BEAN_NAME = "viewResolver";
/*   52:     */   public static final String FLASH_MAP_MANAGER_BEAN_NAME = "flashMapManager";
/*   53: 192 */   public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";
/*   54: 198 */   public static final String LOCALE_RESOLVER_ATTRIBUTE = DispatcherServlet.class.getName() + ".LOCALE_RESOLVER";
/*   55: 204 */   public static final String THEME_RESOLVER_ATTRIBUTE = DispatcherServlet.class.getName() + ".THEME_RESOLVER";
/*   56: 210 */   public static final String THEME_SOURCE_ATTRIBUTE = DispatcherServlet.class.getName() + ".THEME_SOURCE";
/*   57:     */   public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";
/*   58:     */   private static final String DEFAULT_STRATEGIES_PATH = "DispatcherServlet.properties";
/*   59: 223 */   protected static final Log pageNotFoundLogger = LogFactory.getLog("org.springframework.web.servlet.PageNotFound");
/*   60: 225 */   private static final UrlPathHelper urlPathHelper = new UrlPathHelper();
/*   61:     */   private static final Properties defaultStrategies;
/*   62:     */   
/*   63:     */   static
/*   64:     */   {
/*   65:     */     try
/*   66:     */     {
/*   67: 234 */       ClassPathResource resource = new ClassPathResource("DispatcherServlet.properties", DispatcherServlet.class);
/*   68: 235 */       defaultStrategies = PropertiesLoaderUtils.loadProperties(resource);
/*   69:     */     }
/*   70:     */     catch (IOException ex)
/*   71:     */     {
/*   72: 238 */       throw new IllegalStateException("Could not load 'DispatcherServlet.properties': " + ex.getMessage());
/*   73:     */     }
/*   74:     */   }
/*   75:     */   
/*   76: 243 */   private boolean detectAllHandlerMappings = true;
/*   77: 246 */   private boolean detectAllHandlerAdapters = true;
/*   78: 249 */   private boolean detectAllHandlerExceptionResolvers = true;
/*   79: 252 */   private boolean detectAllViewResolvers = true;
/*   80: 255 */   private boolean cleanupAfterInclude = true;
/*   81:     */   private MultipartResolver multipartResolver;
/*   82:     */   private LocaleResolver localeResolver;
/*   83:     */   private ThemeResolver themeResolver;
/*   84:     */   private List<HandlerMapping> handlerMappings;
/*   85:     */   private List<HandlerAdapter> handlerAdapters;
/*   86:     */   private List<HandlerExceptionResolver> handlerExceptionResolvers;
/*   87:     */   private RequestToViewNameTranslator viewNameTranslator;
/*   88:     */   private FlashMapManager flashMapManager;
/*   89:     */   private List<ViewResolver> viewResolvers;
/*   90:     */   
/*   91:     */   public DispatcherServlet() {}
/*   92:     */   
/*   93:     */   public DispatcherServlet(WebApplicationContext webApplicationContext)
/*   94:     */   {
/*   95: 346 */     super(webApplicationContext);
/*   96:     */   }
/*   97:     */   
/*   98:     */   public void setDetectAllHandlerMappings(boolean detectAllHandlerMappings)
/*   99:     */   {
/*  100: 356 */     this.detectAllHandlerMappings = detectAllHandlerMappings;
/*  101:     */   }
/*  102:     */   
/*  103:     */   public void setDetectAllHandlerAdapters(boolean detectAllHandlerAdapters)
/*  104:     */   {
/*  105: 366 */     this.detectAllHandlerAdapters = detectAllHandlerAdapters;
/*  106:     */   }
/*  107:     */   
/*  108:     */   public void setDetectAllHandlerExceptionResolvers(boolean detectAllHandlerExceptionResolvers)
/*  109:     */   {
/*  110: 376 */     this.detectAllHandlerExceptionResolvers = detectAllHandlerExceptionResolvers;
/*  111:     */   }
/*  112:     */   
/*  113:     */   public void setDetectAllViewResolvers(boolean detectAllViewResolvers)
/*  114:     */   {
/*  115: 386 */     this.detectAllViewResolvers = detectAllViewResolvers;
/*  116:     */   }
/*  117:     */   
/*  118:     */   public void setCleanupAfterInclude(boolean cleanupAfterInclude)
/*  119:     */   {
/*  120: 402 */     this.cleanupAfterInclude = cleanupAfterInclude;
/*  121:     */   }
/*  122:     */   
/*  123:     */   protected void onRefresh(ApplicationContext context)
/*  124:     */   {
/*  125: 410 */     initStrategies(context);
/*  126:     */   }
/*  127:     */   
/*  128:     */   protected void initStrategies(ApplicationContext context)
/*  129:     */   {
/*  130: 418 */     initMultipartResolver(context);
/*  131: 419 */     initLocaleResolver(context);
/*  132: 420 */     initThemeResolver(context);
/*  133: 421 */     initHandlerMappings(context);
/*  134: 422 */     initHandlerAdapters(context);
/*  135: 423 */     initHandlerExceptionResolvers(context);
/*  136: 424 */     initRequestToViewNameTranslator(context);
/*  137: 425 */     initViewResolvers(context);
/*  138: 426 */     initFlashMapManager(context);
/*  139:     */   }
/*  140:     */   
/*  141:     */   private void initMultipartResolver(ApplicationContext context)
/*  142:     */   {
/*  143:     */     try
/*  144:     */     {
/*  145: 436 */       this.multipartResolver = ((MultipartResolver)context.getBean("multipartResolver", MultipartResolver.class));
/*  146: 437 */       if (this.logger.isDebugEnabled()) {
/*  147: 438 */         this.logger.debug("Using MultipartResolver [" + this.multipartResolver + "]");
/*  148:     */       }
/*  149:     */     }
/*  150:     */     catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException)
/*  151:     */     {
/*  152: 443 */       this.multipartResolver = null;
/*  153: 444 */       if (this.logger.isDebugEnabled()) {
/*  154: 445 */         this.logger.debug("Unable to locate MultipartResolver with name 'multipartResolver': no multipart request handling provided");
/*  155:     */       }
/*  156:     */     }
/*  157:     */   }
/*  158:     */   
/*  159:     */   private void initLocaleResolver(ApplicationContext context)
/*  160:     */   {
/*  161:     */     try
/*  162:     */     {
/*  163: 458 */       this.localeResolver = ((LocaleResolver)context.getBean("localeResolver", LocaleResolver.class));
/*  164: 459 */       if (this.logger.isDebugEnabled()) {
/*  165: 460 */         this.logger.debug("Using LocaleResolver [" + this.localeResolver + "]");
/*  166:     */       }
/*  167:     */     }
/*  168:     */     catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException)
/*  169:     */     {
/*  170: 465 */       this.localeResolver = ((LocaleResolver)getDefaultStrategy(context, LocaleResolver.class));
/*  171: 466 */       if (this.logger.isDebugEnabled()) {
/*  172: 467 */         this.logger.debug("Unable to locate LocaleResolver with name 'localeResolver': using default [" + 
/*  173: 468 */           this.localeResolver + "]");
/*  174:     */       }
/*  175:     */     }
/*  176:     */   }
/*  177:     */   
/*  178:     */   private void initThemeResolver(ApplicationContext context)
/*  179:     */   {
/*  180:     */     try
/*  181:     */     {
/*  182: 480 */       this.themeResolver = ((ThemeResolver)context.getBean("themeResolver", ThemeResolver.class));
/*  183: 481 */       if (this.logger.isDebugEnabled()) {
/*  184: 482 */         this.logger.debug("Using ThemeResolver [" + this.themeResolver + "]");
/*  185:     */       }
/*  186:     */     }
/*  187:     */     catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException)
/*  188:     */     {
/*  189: 487 */       this.themeResolver = ((ThemeResolver)getDefaultStrategy(context, ThemeResolver.class));
/*  190: 488 */       if (this.logger.isDebugEnabled()) {
/*  191: 489 */         this.logger.debug(
/*  192: 490 */           "Unable to locate ThemeResolver with name 'themeResolver': using default [" + 
/*  193: 491 */           this.themeResolver + "]");
/*  194:     */       }
/*  195:     */     }
/*  196:     */   }
/*  197:     */   
/*  198:     */   private void initHandlerMappings(ApplicationContext context)
/*  199:     */   {
/*  200: 502 */     this.handlerMappings = null;
/*  201: 504 */     if (this.detectAllHandlerMappings)
/*  202:     */     {
/*  203: 506 */       Map<String, HandlerMapping> matchingBeans = 
/*  204: 507 */         BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerMapping.class, true, false);
/*  205: 508 */       if (!matchingBeans.isEmpty())
/*  206:     */       {
/*  207: 509 */         this.handlerMappings = new ArrayList(matchingBeans.values());
/*  208:     */         
/*  209: 511 */         OrderComparator.sort(this.handlerMappings);
/*  210:     */       }
/*  211:     */     }
/*  212:     */     else
/*  213:     */     {
/*  214:     */       try
/*  215:     */       {
/*  216: 516 */         HandlerMapping hm = (HandlerMapping)context.getBean("handlerMapping", HandlerMapping.class);
/*  217: 517 */         this.handlerMappings = Collections.singletonList(hm);
/*  218:     */       }
/*  219:     */       catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException) {}
/*  220:     */     }
/*  221: 526 */     if (this.handlerMappings == null)
/*  222:     */     {
/*  223: 527 */       this.handlerMappings = getDefaultStrategies(context, HandlerMapping.class);
/*  224: 528 */       if (this.logger.isDebugEnabled()) {
/*  225: 529 */         this.logger.debug("No HandlerMappings found in servlet '" + getServletName() + "': using default");
/*  226:     */       }
/*  227:     */     }
/*  228:     */   }
/*  229:     */   
/*  230:     */   private void initHandlerAdapters(ApplicationContext context)
/*  231:     */   {
/*  232: 540 */     this.handlerAdapters = null;
/*  233: 542 */     if (this.detectAllHandlerAdapters)
/*  234:     */     {
/*  235: 544 */       Map<String, HandlerAdapter> matchingBeans = 
/*  236: 545 */         BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerAdapter.class, true, false);
/*  237: 546 */       if (!matchingBeans.isEmpty())
/*  238:     */       {
/*  239: 547 */         this.handlerAdapters = new ArrayList(matchingBeans.values());
/*  240:     */         
/*  241: 549 */         OrderComparator.sort(this.handlerAdapters);
/*  242:     */       }
/*  243:     */     }
/*  244:     */     else
/*  245:     */     {
/*  246:     */       try
/*  247:     */       {
/*  248: 554 */         HandlerAdapter ha = (HandlerAdapter)context.getBean("handlerAdapter", HandlerAdapter.class);
/*  249: 555 */         this.handlerAdapters = Collections.singletonList(ha);
/*  250:     */       }
/*  251:     */       catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException) {}
/*  252:     */     }
/*  253: 564 */     if (this.handlerAdapters == null)
/*  254:     */     {
/*  255: 565 */       this.handlerAdapters = getDefaultStrategies(context, HandlerAdapter.class);
/*  256: 566 */       if (this.logger.isDebugEnabled()) {
/*  257: 567 */         this.logger.debug("No HandlerAdapters found in servlet '" + getServletName() + "': using default");
/*  258:     */       }
/*  259:     */     }
/*  260:     */   }
/*  261:     */   
/*  262:     */   private void initHandlerExceptionResolvers(ApplicationContext context)
/*  263:     */   {
/*  264: 578 */     this.handlerExceptionResolvers = null;
/*  265: 580 */     if (this.detectAllHandlerExceptionResolvers)
/*  266:     */     {
/*  267: 582 */       Map<String, HandlerExceptionResolver> matchingBeans = 
/*  268: 583 */         BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerExceptionResolver.class, true, false);
/*  269: 584 */       if (!matchingBeans.isEmpty())
/*  270:     */       {
/*  271: 585 */         this.handlerExceptionResolvers = new ArrayList(matchingBeans.values());
/*  272:     */         
/*  273: 587 */         OrderComparator.sort(this.handlerExceptionResolvers);
/*  274:     */       }
/*  275:     */     }
/*  276:     */     else
/*  277:     */     {
/*  278:     */       try
/*  279:     */       {
/*  280: 592 */         HandlerExceptionResolver her = 
/*  281: 593 */           (HandlerExceptionResolver)context.getBean("handlerExceptionResolver", HandlerExceptionResolver.class);
/*  282: 594 */         this.handlerExceptionResolvers = Collections.singletonList(her);
/*  283:     */       }
/*  284:     */       catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException) {}
/*  285:     */     }
/*  286: 603 */     if (this.handlerExceptionResolvers == null)
/*  287:     */     {
/*  288: 604 */       this.handlerExceptionResolvers = getDefaultStrategies(context, HandlerExceptionResolver.class);
/*  289: 605 */       if (this.logger.isDebugEnabled()) {
/*  290: 606 */         this.logger.debug("No HandlerExceptionResolvers found in servlet '" + getServletName() + "': using default");
/*  291:     */       }
/*  292:     */     }
/*  293:     */   }
/*  294:     */   
/*  295:     */   private void initRequestToViewNameTranslator(ApplicationContext context)
/*  296:     */   {
/*  297:     */     try
/*  298:     */     {
/*  299: 617 */       this.viewNameTranslator = 
/*  300: 618 */         ((RequestToViewNameTranslator)context.getBean("viewNameTranslator", RequestToViewNameTranslator.class));
/*  301: 619 */       if (this.logger.isDebugEnabled()) {
/*  302: 620 */         this.logger.debug("Using RequestToViewNameTranslator [" + this.viewNameTranslator + "]");
/*  303:     */       }
/*  304:     */     }
/*  305:     */     catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException)
/*  306:     */     {
/*  307: 625 */       this.viewNameTranslator = ((RequestToViewNameTranslator)getDefaultStrategy(context, RequestToViewNameTranslator.class));
/*  308: 626 */       if (this.logger.isDebugEnabled()) {
/*  309: 627 */         this.logger.debug("Unable to locate RequestToViewNameTranslator with name 'viewNameTranslator': using default [" + 
/*  310: 628 */           this.viewNameTranslator + 
/*  311: 629 */           "]");
/*  312:     */       }
/*  313:     */     }
/*  314:     */   }
/*  315:     */   
/*  316:     */   private void initViewResolvers(ApplicationContext context)
/*  317:     */   {
/*  318: 640 */     this.viewResolvers = null;
/*  319: 642 */     if (this.detectAllViewResolvers)
/*  320:     */     {
/*  321: 644 */       Map<String, ViewResolver> matchingBeans = 
/*  322: 645 */         BeanFactoryUtils.beansOfTypeIncludingAncestors(context, ViewResolver.class, true, false);
/*  323: 646 */       if (!matchingBeans.isEmpty())
/*  324:     */       {
/*  325: 647 */         this.viewResolvers = new ArrayList(matchingBeans.values());
/*  326:     */         
/*  327: 649 */         OrderComparator.sort(this.viewResolvers);
/*  328:     */       }
/*  329:     */     }
/*  330:     */     else
/*  331:     */     {
/*  332:     */       try
/*  333:     */       {
/*  334: 654 */         ViewResolver vr = (ViewResolver)context.getBean("viewResolver", ViewResolver.class);
/*  335: 655 */         this.viewResolvers = Collections.singletonList(vr);
/*  336:     */       }
/*  337:     */       catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException) {}
/*  338:     */     }
/*  339: 664 */     if (this.viewResolvers == null)
/*  340:     */     {
/*  341: 665 */       this.viewResolvers = getDefaultStrategies(context, ViewResolver.class);
/*  342: 666 */       if (this.logger.isDebugEnabled()) {
/*  343: 667 */         this.logger.debug("No ViewResolvers found in servlet '" + getServletName() + "': using default");
/*  344:     */       }
/*  345:     */     }
/*  346:     */   }
/*  347:     */   
/*  348:     */   private void initFlashMapManager(ApplicationContext context)
/*  349:     */   {
/*  350:     */     try
/*  351:     */     {
/*  352: 679 */       this.flashMapManager = 
/*  353: 680 */         ((FlashMapManager)context.getBean("flashMapManager", FlashMapManager.class));
/*  354: 681 */       if (this.logger.isDebugEnabled()) {
/*  355: 682 */         this.logger.debug("Using FlashMapManager [" + this.flashMapManager + "]");
/*  356:     */       }
/*  357:     */     }
/*  358:     */     catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException)
/*  359:     */     {
/*  360: 687 */       this.flashMapManager = ((FlashMapManager)getDefaultStrategy(context, FlashMapManager.class));
/*  361: 688 */       if (this.logger.isDebugEnabled()) {
/*  362: 689 */         this.logger.debug("Unable to locate FlashMapManager with name 'flashMapManager': using default [" + 
/*  363: 690 */           this.flashMapManager + "]");
/*  364:     */       }
/*  365:     */     }
/*  366:     */   }
/*  367:     */   
/*  368:     */   public final ThemeSource getThemeSource()
/*  369:     */   {
/*  370: 703 */     if ((getWebApplicationContext() instanceof ThemeSource)) {
/*  371: 704 */       return (ThemeSource)getWebApplicationContext();
/*  372:     */     }
/*  373: 707 */     return null;
/*  374:     */   }
/*  375:     */   
/*  376:     */   public final MultipartResolver getMultipartResolver()
/*  377:     */   {
/*  378: 717 */     return this.multipartResolver;
/*  379:     */   }
/*  380:     */   
/*  381:     */   protected <T> T getDefaultStrategy(ApplicationContext context, Class<T> strategyInterface)
/*  382:     */   {
/*  383: 730 */     List<T> strategies = getDefaultStrategies(context, strategyInterface);
/*  384: 731 */     if (strategies.size() != 1) {
/*  385: 732 */       throw new BeanInitializationException(
/*  386: 733 */         "DispatcherServlet needs exactly 1 strategy for interface [" + strategyInterface.getName() + "]");
/*  387:     */     }
/*  388: 735 */     return strategies.get(0);
/*  389:     */   }
/*  390:     */   
/*  391:     */   protected <T> List<T> getDefaultStrategies(ApplicationContext context, Class<T> strategyInterface)
/*  392:     */   {
/*  393: 749 */     String key = strategyInterface.getName();
/*  394: 750 */     String value = defaultStrategies.getProperty(key);
/*  395: 751 */     if (value != null)
/*  396:     */     {
/*  397: 752 */       String[] classNames = StringUtils.commaDelimitedListToStringArray(value);
/*  398: 753 */       List<T> strategies = new ArrayList(classNames.length);
/*  399: 754 */       for (String className : classNames) {
/*  400:     */         try
/*  401:     */         {
/*  402: 756 */           Class<?> clazz = ClassUtils.forName(className, DispatcherServlet.class.getClassLoader());
/*  403: 757 */           Object strategy = createDefaultStrategy(context, clazz);
/*  404: 758 */           strategies.add(strategy);
/*  405:     */         }
/*  406:     */         catch (ClassNotFoundException ex)
/*  407:     */         {
/*  408: 761 */           throw new BeanInitializationException(
/*  409: 762 */             "Could not find DispatcherServlet's default strategy class [" + className + 
/*  410: 763 */             "] for interface [" + key + "]", ex);
/*  411:     */         }
/*  412:     */         catch (LinkageError err)
/*  413:     */         {
/*  414: 766 */           throw new BeanInitializationException(
/*  415: 767 */             "Error loading DispatcherServlet's default strategy class [" + className + 
/*  416: 768 */             "] for interface [" + key + "]: problem with class file or dependent class", err);
/*  417:     */         }
/*  418:     */       }
/*  419: 771 */       return strategies;
/*  420:     */     }
/*  421: 774 */     return new LinkedList();
/*  422:     */   }
/*  423:     */   
/*  424:     */   protected Object createDefaultStrategy(ApplicationContext context, Class<?> clazz)
/*  425:     */   {
/*  426: 788 */     return context.getAutowireCapableBeanFactory().createBean(clazz);
/*  427:     */   }
/*  428:     */   
/*  429:     */   protected void doService(HttpServletRequest request, HttpServletResponse response)
/*  430:     */     throws Exception
/*  431:     */   {
/*  432: 797 */     if (this.logger.isDebugEnabled())
/*  433:     */     {
/*  434: 798 */       String requestUri = urlPathHelper.getRequestUri(request);
/*  435: 799 */       this.logger.debug("DispatcherServlet with name '" + getServletName() + "' processing " + request.getMethod() + 
/*  436: 800 */         " request for [" + requestUri + "]");
/*  437:     */     }
/*  438: 805 */     Map<String, Object> attributesSnapshot = null;
/*  439: 806 */     if (WebUtils.isIncludeRequest(request))
/*  440:     */     {
/*  441: 807 */       this.logger.debug("Taking snapshot of request attributes before include");
/*  442: 808 */       attributesSnapshot = new HashMap();
/*  443: 809 */       attrNames = request.getAttributeNames();
/*  444: 810 */       while (attrNames.hasMoreElements())
/*  445:     */       {
/*  446: 811 */         String attrName = (String)attrNames.nextElement();
/*  447: 812 */         if ((this.cleanupAfterInclude) || (attrName.startsWith("org.springframework.web.servlet"))) {
/*  448: 813 */           attributesSnapshot.put(attrName, request.getAttribute(attrName));
/*  449:     */         }
/*  450:     */       }
/*  451:     */     }
/*  452: 818 */     this.flashMapManager.requestStarted(request);
/*  453:     */     
/*  454:     */ 
/*  455: 821 */     request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebApplicationContext());
/*  456: 822 */     request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, this.localeResolver);
/*  457: 823 */     request.setAttribute(THEME_RESOLVER_ATTRIBUTE, this.themeResolver);
/*  458: 824 */     request.setAttribute(THEME_SOURCE_ATTRIBUTE, getThemeSource());
/*  459:     */     try
/*  460:     */     {
/*  461: 827 */       doDispatch(request, response);
/*  462:     */     }
/*  463:     */     finally
/*  464:     */     {
/*  465: 830 */       this.flashMapManager.requestCompleted(request);
/*  466: 833 */       if (attributesSnapshot != null) {
/*  467: 834 */         restoreAttributesAfterInclude(request, attributesSnapshot);
/*  468:     */       }
/*  469:     */     }
/*  470:     */   }
/*  471:     */   
/*  472:     */   protected void doDispatch(HttpServletRequest request, HttpServletResponse response)
/*  473:     */     throws Exception
/*  474:     */   {
/*  475: 851 */     HttpServletRequest processedRequest = request;
/*  476: 852 */     HandlerExecutionChain mappedHandler = null;
/*  477: 853 */     int interceptorIndex = -1;
/*  478:     */     try
/*  479:     */     {
/*  480: 857 */       boolean errorView = false;
/*  481:     */       boolean errorView;
/*  482:     */       ModelAndView mv;
/*  483:     */       try
/*  484:     */       {
/*  485: 860 */         processedRequest = checkMultipart(request);
/*  486:     */         
/*  487:     */ 
/*  488: 863 */         mappedHandler = getHandler(processedRequest, false);
/*  489: 864 */         if ((mappedHandler == null) || (mappedHandler.getHandler() == null)) {
/*  490: 865 */           noHandlerFound(processedRequest, response);
/*  491:     */         }
/*  492:     */         HandlerAdapter ha;
/*  493:     */         boolean isGet;
/*  494:     */         long lastModified;
/*  495:     */         do
/*  496:     */         {
/*  497:     */           ModelAndView mv;
/*  498: 866 */           return;
/*  499:     */           
/*  500:     */ 
/*  501:     */ 
/*  502: 870 */           ha = getHandlerAdapter(mappedHandler.getHandler());
/*  503:     */           
/*  504:     */ 
/*  505: 873 */           String method = request.getMethod();
/*  506: 874 */           isGet = "GET".equals(method);
/*  507: 875 */           if ((!isGet) && (!"HEAD".equals(method))) {
/*  508:     */             break;
/*  509:     */           }
/*  510: 876 */           lastModified = ha.getLastModified(request, mappedHandler.getHandler());
/*  511: 877 */           if (this.logger.isDebugEnabled())
/*  512:     */           {
/*  513: 878 */             String requestUri = urlPathHelper.getRequestUri(request);
/*  514: 879 */             this.logger.debug("Last-Modified value for [" + requestUri + "] is: " + lastModified);
/*  515:     */           }
/*  516: 881 */         } while ((new ServletWebRequest(request, response).checkNotModified(lastModified)) && (isGet));
/*  517: 887 */         HandlerInterceptor[] interceptors = mappedHandler.getInterceptors();
/*  518: 888 */         if (interceptors != null) {
/*  519: 889 */           for (int i = 0; i < interceptors.length; i++)
/*  520:     */           {
/*  521: 890 */             HandlerInterceptor interceptor = interceptors[i];
/*  522: 891 */             if (!interceptor.preHandle(processedRequest, response, mappedHandler.getHandler()))
/*  523:     */             {
/*  524: 892 */               triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, null);
/*  525: 893 */               break;
/*  526:     */             }
/*  527: 895 */             interceptorIndex = i;
/*  528:     */           }
/*  529:     */         }
/*  530: 900 */         ModelAndView mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
/*  531: 903 */         if ((mv != null) && (!mv.hasView())) {
/*  532: 904 */           mv.setViewName(getDefaultViewName(request));
/*  533:     */         }
/*  534: 908 */         if (interceptors != null) {
/*  535: 909 */           for (int i = interceptors.length - 1; i >= 0; i--)
/*  536:     */           {
/*  537: 910 */             HandlerInterceptor interceptor = interceptors[i];
/*  538: 911 */             interceptor.postHandle(processedRequest, response, mappedHandler.getHandler(), mv);
/*  539:     */           }
/*  540:     */         }
/*  541:     */       }
/*  542:     */       catch (ModelAndViewDefiningException ex)
/*  543:     */       {
/*  544: 916 */         this.logger.debug("ModelAndViewDefiningException encountered", ex);
/*  545: 917 */         mv = ex.getModelAndView();
/*  546:     */       }
/*  547:     */       catch (Exception ex)
/*  548:     */       {
/*  549:     */         ModelAndView mv;
/*  550: 920 */         Object handler = mappedHandler != null ? mappedHandler.getHandler() : null;
/*  551: 921 */         mv = processHandlerException(processedRequest, response, handler, ex);
/*  552: 922 */         errorView = mv != null;
/*  553:     */       }
/*  554: 926 */       if ((mv != null) && (!mv.wasCleared()))
/*  555:     */       {
/*  556: 927 */         render(mv, processedRequest, response);
/*  557: 928 */         if (errorView) {
/*  558: 929 */           WebUtils.clearErrorRequestAttributes(request);
/*  559:     */         }
/*  560:     */       }
/*  561: 933 */       else if (this.logger.isDebugEnabled())
/*  562:     */       {
/*  563: 934 */         this.logger.debug("Null ModelAndView returned to DispatcherServlet with name '" + getServletName() + 
/*  564: 935 */           "': assuming HandlerAdapter completed request handling");
/*  565:     */       }
/*  566: 940 */       triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, null);
/*  567:     */     }
/*  568:     */     catch (Exception ex)
/*  569:     */     {
/*  570: 945 */       triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, ex);
/*  571: 946 */       throw ex;
/*  572:     */     }
/*  573:     */     catch (Error err)
/*  574:     */     {
/*  575: 949 */       ServletException ex = new NestedServletException("Handler processing failed", err);
/*  576:     */       
/*  577: 951 */       triggerAfterCompletion(mappedHandler, interceptorIndex, processedRequest, response, ex);
/*  578: 952 */       throw ex;
/*  579:     */     }
/*  580:     */     finally
/*  581:     */     {
/*  582: 957 */       if (processedRequest != request) {
/*  583: 958 */         cleanupMultipart(processedRequest);
/*  584:     */       }
/*  585:     */     }
/*  586: 957 */     if (processedRequest != request) {
/*  587: 958 */       cleanupMultipart(processedRequest);
/*  588:     */     }
/*  589:     */   }
/*  590:     */   
/*  591:     */   protected LocaleContext buildLocaleContext(final HttpServletRequest request)
/*  592:     */   {
/*  593: 972 */     new LocaleContext()
/*  594:     */     {
/*  595:     */       public Locale getLocale()
/*  596:     */       {
/*  597: 974 */         return DispatcherServlet.this.localeResolver.resolveLocale(request);
/*  598:     */       }
/*  599:     */       
/*  600:     */       public String toString()
/*  601:     */       {
/*  602: 978 */         return getLocale().toString();
/*  603:     */       }
/*  604:     */     };
/*  605:     */   }
/*  606:     */   
/*  607:     */   protected HttpServletRequest checkMultipart(HttpServletRequest request)
/*  608:     */     throws MultipartException
/*  609:     */   {
/*  610: 991 */     if ((this.multipartResolver != null) && (this.multipartResolver.isMultipart(request))) {
/*  611: 992 */       if ((request instanceof MultipartHttpServletRequest)) {
/*  612: 993 */         this.logger.debug("Request is already a MultipartHttpServletRequest - if not in a forward, this typically results from an additional MultipartFilter in web.xml");
/*  613:     */       } else {
/*  614: 997 */         return this.multipartResolver.resolveMultipart(request);
/*  615:     */       }
/*  616:     */     }
/*  617:1001 */     return request;
/*  618:     */   }
/*  619:     */   
/*  620:     */   protected void cleanupMultipart(HttpServletRequest request)
/*  621:     */   {
/*  622:1010 */     if ((request instanceof MultipartHttpServletRequest)) {
/*  623:1011 */       this.multipartResolver.cleanupMultipart((MultipartHttpServletRequest)request);
/*  624:     */     }
/*  625:     */   }
/*  626:     */   
/*  627:     */   @Deprecated
/*  628:     */   protected HandlerExecutionChain getHandler(HttpServletRequest request, boolean cache)
/*  629:     */     throws Exception
/*  630:     */   {
/*  631:1025 */     return getHandler(request);
/*  632:     */   }
/*  633:     */   
/*  634:     */   protected HandlerExecutionChain getHandler(HttpServletRequest request)
/*  635:     */     throws Exception
/*  636:     */   {
/*  637:1035 */     for (HandlerMapping hm : this.handlerMappings)
/*  638:     */     {
/*  639:1036 */       if (this.logger.isTraceEnabled()) {
/*  640:1037 */         this.logger.trace(
/*  641:1038 */           "Testing handler map [" + hm + "] in DispatcherServlet with name '" + getServletName() + "'");
/*  642:     */       }
/*  643:1040 */       HandlerExecutionChain handler = hm.getHandler(request);
/*  644:1041 */       if (handler != null) {
/*  645:1042 */         return handler;
/*  646:     */       }
/*  647:     */     }
/*  648:1045 */     return null;
/*  649:     */   }
/*  650:     */   
/*  651:     */   protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response)
/*  652:     */     throws Exception
/*  653:     */   {
/*  654:1055 */     if (pageNotFoundLogger.isWarnEnabled())
/*  655:     */     {
/*  656:1056 */       String requestUri = urlPathHelper.getRequestUri(request);
/*  657:1057 */       pageNotFoundLogger.warn("No mapping found for HTTP request with URI [" + requestUri + 
/*  658:1058 */         "] in DispatcherServlet with name '" + getServletName() + "'");
/*  659:     */     }
/*  660:1060 */     response.sendError(404);
/*  661:     */   }
/*  662:     */   
/*  663:     */   protected HandlerAdapter getHandlerAdapter(Object handler)
/*  664:     */     throws ServletException
/*  665:     */   {
/*  666:1069 */     for (HandlerAdapter ha : this.handlerAdapters)
/*  667:     */     {
/*  668:1070 */       if (this.logger.isTraceEnabled()) {
/*  669:1071 */         this.logger.trace("Testing handler adapter [" + ha + "]");
/*  670:     */       }
/*  671:1073 */       if (ha.supports(handler)) {
/*  672:1074 */         return ha;
/*  673:     */       }
/*  674:     */     }
/*  675:1077 */     throw new ServletException("No adapter for handler [" + handler + 
/*  676:1078 */       "]: Does your handler implement a supported interface like Controller?");
/*  677:     */   }
/*  678:     */   
/*  679:     */   protected ModelAndView processHandlerException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*  680:     */     throws Exception
/*  681:     */   {
/*  682:1095 */     ModelAndView exMv = null;
/*  683:1096 */     for (HandlerExceptionResolver handlerExceptionResolver : this.handlerExceptionResolvers)
/*  684:     */     {
/*  685:1097 */       exMv = handlerExceptionResolver.resolveException(request, response, handler, ex);
/*  686:1098 */       if (exMv != null) {
/*  687:     */         break;
/*  688:     */       }
/*  689:     */     }
/*  690:1102 */     if (exMv != null)
/*  691:     */     {
/*  692:1103 */       if (exMv.isEmpty()) {
/*  693:1104 */         return null;
/*  694:     */       }
/*  695:1107 */       if (!exMv.hasView()) {
/*  696:1108 */         exMv.setViewName(getDefaultViewName(request));
/*  697:     */       }
/*  698:1110 */       if (this.logger.isDebugEnabled()) {
/*  699:1111 */         this.logger.debug("Handler execution resulted in exception - forwarding to resolved error view: " + exMv, ex);
/*  700:     */       }
/*  701:1113 */       WebUtils.exposeErrorRequestAttributes(request, ex, getServletName());
/*  702:1114 */       return exMv;
/*  703:     */     }
/*  704:1117 */     throw ex;
/*  705:     */   }
/*  706:     */   
/*  707:     */   protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response)
/*  708:     */     throws Exception
/*  709:     */   {
/*  710:1131 */     Locale locale = this.localeResolver.resolveLocale(request);
/*  711:1132 */     response.setLocale(locale);
/*  712:     */     View view;
/*  713:1135 */     if (mv.isReference())
/*  714:     */     {
/*  715:1137 */       View view = resolveViewName(mv.getViewName(), mv.getModelInternal(), locale, request);
/*  716:1138 */       if (view == null) {
/*  717:1139 */         throw new ServletException(
/*  718:1140 */           "Could not resolve view with name '" + mv.getViewName() + "' in servlet with name '" + 
/*  719:1141 */           getServletName() + "'");
/*  720:     */       }
/*  721:     */     }
/*  722:     */     else
/*  723:     */     {
/*  724:1146 */       view = mv.getView();
/*  725:1147 */       if (view == null) {
/*  726:1148 */         throw new ServletException("ModelAndView [" + mv + "] neither contains a view name nor a " + 
/*  727:1149 */           "View object in servlet with name '" + getServletName() + "'");
/*  728:     */       }
/*  729:     */     }
/*  730:1154 */     if (this.logger.isDebugEnabled()) {
/*  731:1155 */       this.logger.debug("Rendering view [" + view + "] in DispatcherServlet with name '" + getServletName() + "'");
/*  732:     */     }
/*  733:1157 */     view.render(mv.getModelInternal(), request, response);
/*  734:     */   }
/*  735:     */   
/*  736:     */   protected String getDefaultViewName(HttpServletRequest request)
/*  737:     */     throws Exception
/*  738:     */   {
/*  739:1167 */     return this.viewNameTranslator.getViewName(request);
/*  740:     */   }
/*  741:     */   
/*  742:     */   protected View resolveViewName(String viewName, Map<String, Object> model, Locale locale, HttpServletRequest request)
/*  743:     */     throws Exception
/*  744:     */   {
/*  745:1187 */     for (ViewResolver viewResolver : this.viewResolvers)
/*  746:     */     {
/*  747:1188 */       View view = viewResolver.resolveViewName(viewName, locale);
/*  748:1189 */       if (view != null) {
/*  749:1190 */         return view;
/*  750:     */       }
/*  751:     */     }
/*  752:1193 */     return null;
/*  753:     */   }
/*  754:     */   
/*  755:     */   private void triggerAfterCompletion(HandlerExecutionChain mappedHandler, int interceptorIndex, HttpServletRequest request, HttpServletResponse response, Exception ex)
/*  756:     */     throws Exception
/*  757:     */   {
/*  758:1212 */     if (mappedHandler != null)
/*  759:     */     {
/*  760:1213 */       HandlerInterceptor[] interceptors = mappedHandler.getInterceptors();
/*  761:1214 */       if (interceptors != null) {
/*  762:1215 */         for (int i = interceptorIndex; i >= 0; i--)
/*  763:     */         {
/*  764:1216 */           HandlerInterceptor interceptor = interceptors[i];
/*  765:     */           try
/*  766:     */           {
/*  767:1218 */             interceptor.afterCompletion(request, response, mappedHandler.getHandler(), ex);
/*  768:     */           }
/*  769:     */           catch (Throwable ex2)
/*  770:     */           {
/*  771:1221 */             this.logger.error("HandlerInterceptor.afterCompletion threw exception", ex2);
/*  772:     */           }
/*  773:     */         }
/*  774:     */       }
/*  775:     */     }
/*  776:     */   }
/*  777:     */   
/*  778:     */   private void restoreAttributesAfterInclude(HttpServletRequest request, Map<?, ?> attributesSnapshot)
/*  779:     */   {
/*  780:1234 */     this.logger.debug("Restoring snapshot of request attributes after include");
/*  781:     */     
/*  782:     */ 
/*  783:     */ 
/*  784:1238 */     Set<String> attrsToCheck = new HashSet();
/*  785:1239 */     Enumeration<?> attrNames = request.getAttributeNames();
/*  786:1240 */     while (attrNames.hasMoreElements())
/*  787:     */     {
/*  788:1241 */       String attrName = (String)attrNames.nextElement();
/*  789:1242 */       if ((this.cleanupAfterInclude) || (attrName.startsWith("org.springframework.web.servlet"))) {
/*  790:1243 */         attrsToCheck.add(attrName);
/*  791:     */       }
/*  792:     */     }
/*  793:1249 */     for (String attrName : attrsToCheck)
/*  794:     */     {
/*  795:1250 */       Object attrValue = attributesSnapshot.get(attrName);
/*  796:1251 */       if (attrValue == null)
/*  797:     */       {
/*  798:1252 */         if (this.logger.isDebugEnabled()) {
/*  799:1253 */           this.logger.debug("Removing attribute [" + attrName + "] after include");
/*  800:     */         }
/*  801:1255 */         request.removeAttribute(attrName);
/*  802:     */       }
/*  803:1257 */       else if (attrValue != request.getAttribute(attrName))
/*  804:     */       {
/*  805:1258 */         if (this.logger.isDebugEnabled()) {
/*  806:1259 */           this.logger.debug("Restoring original value of attribute [" + attrName + "] after include");
/*  807:     */         }
/*  808:1261 */         request.setAttribute(attrName, attrValue);
/*  809:     */       }
/*  810:     */     }
/*  811:     */   }
/*  812:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.DispatcherServlet
 * JD-Core Version:    0.7.0.1
 */