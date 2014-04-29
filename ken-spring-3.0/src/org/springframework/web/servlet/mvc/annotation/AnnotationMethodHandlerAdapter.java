/*    1:     */ package org.springframework.web.servlet.mvc.annotation;
/*    2:     */ 
/*    3:     */ import java.io.IOException;
/*    4:     */ import java.io.InputStream;
/*    5:     */ import java.io.OutputStream;
/*    6:     */ import java.io.Reader;
/*    7:     */ import java.io.Writer;
/*    8:     */ import java.lang.reflect.Method;
/*    9:     */ import java.security.Principal;
/*   10:     */ import java.util.ArrayList;
/*   11:     */ import java.util.Arrays;
/*   12:     */ import java.util.Collection;
/*   13:     */ import java.util.Collections;
/*   14:     */ import java.util.Comparator;
/*   15:     */ import java.util.HashMap;
/*   16:     */ import java.util.Iterator;
/*   17:     */ import java.util.LinkedHashMap;
/*   18:     */ import java.util.LinkedHashSet;
/*   19:     */ import java.util.List;
/*   20:     */ import java.util.Locale;
/*   21:     */ import java.util.Map;
/*   22:     */ import java.util.Set;
/*   23:     */ import java.util.concurrent.ConcurrentHashMap;
/*   24:     */ import javax.servlet.ServletException;
/*   25:     */ import javax.servlet.ServletRequest;
/*   26:     */ import javax.servlet.ServletResponse;
/*   27:     */ import javax.servlet.http.Cookie;
/*   28:     */ import javax.servlet.http.HttpServletRequest;
/*   29:     */ import javax.servlet.http.HttpServletResponse;
/*   30:     */ import javax.servlet.http.HttpSession;
/*   31:     */ import org.apache.commons.logging.Log;
/*   32:     */ import org.apache.commons.logging.LogFactory;
/*   33:     */ import org.springframework.beans.BeanUtils;
/*   34:     */ import org.springframework.beans.factory.BeanFactory;
/*   35:     */ import org.springframework.beans.factory.BeanFactoryAware;
/*   36:     */ import org.springframework.beans.factory.config.BeanExpressionContext;
/*   37:     */ import org.springframework.beans.factory.config.BeanExpressionResolver;
/*   38:     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*   39:     */ import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
/*   40:     */ import org.springframework.core.Ordered;
/*   41:     */ import org.springframework.core.ParameterNameDiscoverer;
/*   42:     */ import org.springframework.core.annotation.AnnotationUtils;
/*   43:     */ import org.springframework.http.HttpEntity;
/*   44:     */ import org.springframework.http.HttpHeaders;
/*   45:     */ import org.springframework.http.HttpInputMessage;
/*   46:     */ import org.springframework.http.HttpOutputMessage;
/*   47:     */ import org.springframework.http.HttpStatus;
/*   48:     */ import org.springframework.http.MediaType;
/*   49:     */ import org.springframework.http.ResponseEntity;
/*   50:     */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*   51:     */ import org.springframework.http.converter.HttpMessageConverter;
/*   52:     */ import org.springframework.http.converter.StringHttpMessageConverter;
/*   53:     */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*   54:     */ import org.springframework.http.converter.xml.XmlAwareFormHttpMessageConverter;
/*   55:     */ import org.springframework.http.server.ServerHttpRequest;
/*   56:     */ import org.springframework.http.server.ServerHttpResponse;
/*   57:     */ import org.springframework.http.server.ServletServerHttpRequest;
/*   58:     */ import org.springframework.http.server.ServletServerHttpResponse;
/*   59:     */ import org.springframework.ui.ExtendedModelMap;
/*   60:     */ import org.springframework.ui.Model;
/*   61:     */ import org.springframework.ui.ModelMap;
/*   62:     */ import org.springframework.util.AntPathMatcher;
/*   63:     */ import org.springframework.util.Assert;
/*   64:     */ import org.springframework.util.ClassUtils;
/*   65:     */ import org.springframework.util.ObjectUtils;
/*   66:     */ import org.springframework.util.PathMatcher;
/*   67:     */ import org.springframework.util.StringUtils;
/*   68:     */ import org.springframework.validation.support.BindingAwareModelMap;
/*   69:     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*   70:     */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*   71:     */ import org.springframework.web.HttpSessionRequiredException;
/*   72:     */ import org.springframework.web.bind.MissingServletRequestParameterException;
/*   73:     */ import org.springframework.web.bind.ServletRequestDataBinder;
/*   74:     */ import org.springframework.web.bind.WebDataBinder;
/*   75:     */ import org.springframework.web.bind.annotation.ModelAttribute;
/*   76:     */ import org.springframework.web.bind.annotation.RequestMapping;
/*   77:     */ import org.springframework.web.bind.annotation.RequestMethod;
/*   78:     */ import org.springframework.web.bind.annotation.ResponseBody;
/*   79:     */ import org.springframework.web.bind.annotation.ResponseStatus;
/*   80:     */ import org.springframework.web.bind.annotation.SessionAttributes;
/*   81:     */ import org.springframework.web.bind.annotation.support.HandlerMethodInvoker;
/*   82:     */ import org.springframework.web.bind.annotation.support.HandlerMethodResolver;
/*   83:     */ import org.springframework.web.bind.support.DefaultSessionAttributeStore;
/*   84:     */ import org.springframework.web.bind.support.SessionAttributeStore;
/*   85:     */ import org.springframework.web.bind.support.WebArgumentResolver;
/*   86:     */ import org.springframework.web.bind.support.WebBindingInitializer;
/*   87:     */ import org.springframework.web.context.request.NativeWebRequest;
/*   88:     */ import org.springframework.web.context.request.RequestScope;
/*   89:     */ import org.springframework.web.context.request.ServletWebRequest;
/*   90:     */ import org.springframework.web.multipart.MultipartRequest;
/*   91:     */ import org.springframework.web.servlet.HandlerAdapter;
/*   92:     */ import org.springframework.web.servlet.HandlerMapping;
/*   93:     */ import org.springframework.web.servlet.ModelAndView;
/*   94:     */ import org.springframework.web.servlet.View;
/*   95:     */ import org.springframework.web.servlet.mvc.multiaction.InternalPathMethodNameResolver;
/*   96:     */ import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
/*   97:     */ import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
/*   98:     */ import org.springframework.web.servlet.support.RequestContextUtils;
/*   99:     */ import org.springframework.web.servlet.support.WebContentGenerator;
/*  100:     */ import org.springframework.web.util.UrlPathHelper;
/*  101:     */ import org.springframework.web.util.WebUtils;
/*  102:     */ 
/*  103:     */ public class AnnotationMethodHandlerAdapter
/*  104:     */   extends WebContentGenerator
/*  105:     */   implements HandlerAdapter, Ordered, BeanFactoryAware
/*  106:     */ {
/*  107:     */   public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";
/*  108: 155 */   protected static final Log pageNotFoundLogger = LogFactory.getLog("org.springframework.web.servlet.PageNotFound");
/*  109: 158 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*  110: 160 */   private PathMatcher pathMatcher = new AntPathMatcher();
/*  111: 162 */   private MethodNameResolver methodNameResolver = new InternalPathMethodNameResolver();
/*  112:     */   private WebBindingInitializer webBindingInitializer;
/*  113: 166 */   private SessionAttributeStore sessionAttributeStore = new DefaultSessionAttributeStore();
/*  114: 168 */   private int cacheSecondsForSessionAttributeHandlers = 0;
/*  115: 170 */   private boolean synchronizeOnSession = false;
/*  116: 172 */   private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
/*  117:     */   private WebArgumentResolver[] customArgumentResolvers;
/*  118:     */   private ModelAndViewResolver[] customModelAndViewResolvers;
/*  119:     */   private HttpMessageConverter<?>[] messageConverters;
/*  120: 180 */   private int order = 2147483647;
/*  121:     */   private ConfigurableBeanFactory beanFactory;
/*  122:     */   private BeanExpressionContext expressionContext;
/*  123: 187 */   private final Map<Class<?>, ServletHandlerMethodResolver> methodResolverCache = new ConcurrentHashMap();
/*  124: 189 */   private final Map<Class<?>, Boolean> sessionAnnotatedClassesCache = new ConcurrentHashMap();
/*  125:     */   
/*  126:     */   public AnnotationMethodHandlerAdapter()
/*  127:     */   {
/*  128: 194 */     super(false);
/*  129:     */     
/*  130:     */ 
/*  131: 197 */     StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
/*  132: 198 */     stringHttpMessageConverter.setWriteAcceptCharset(false);
/*  133: 199 */     this.messageConverters = new HttpMessageConverter[] { new ByteArrayHttpMessageConverter(), stringHttpMessageConverter, 
/*  134: 200 */       new SourceHttpMessageConverter(), new XmlAwareFormHttpMessageConverter() };
/*  135:     */   }
/*  136:     */   
/*  137:     */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath)
/*  138:     */   {
/*  139: 212 */     this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
/*  140:     */   }
/*  141:     */   
/*  142:     */   public void setUrlDecode(boolean urlDecode)
/*  143:     */   {
/*  144: 223 */     this.urlPathHelper.setUrlDecode(urlDecode);
/*  145:     */   }
/*  146:     */   
/*  147:     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/*  148:     */   {
/*  149: 232 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/*  150: 233 */     this.urlPathHelper = urlPathHelper;
/*  151:     */   }
/*  152:     */   
/*  153:     */   public void setPathMatcher(PathMatcher pathMatcher)
/*  154:     */   {
/*  155: 241 */     Assert.notNull(pathMatcher, "PathMatcher must not be null");
/*  156: 242 */     this.pathMatcher = pathMatcher;
/*  157:     */   }
/*  158:     */   
/*  159:     */   public void setMethodNameResolver(MethodNameResolver methodNameResolver)
/*  160:     */   {
/*  161: 252 */     this.methodNameResolver = methodNameResolver;
/*  162:     */   }
/*  163:     */   
/*  164:     */   public void setWebBindingInitializer(WebBindingInitializer webBindingInitializer)
/*  165:     */   {
/*  166: 260 */     this.webBindingInitializer = webBindingInitializer;
/*  167:     */   }
/*  168:     */   
/*  169:     */   public void setSessionAttributeStore(SessionAttributeStore sessionAttributeStore)
/*  170:     */   {
/*  171: 269 */     Assert.notNull(sessionAttributeStore, "SessionAttributeStore must not be null");
/*  172: 270 */     this.sessionAttributeStore = sessionAttributeStore;
/*  173:     */   }
/*  174:     */   
/*  175:     */   public void setCacheSecondsForSessionAttributeHandlers(int cacheSecondsForSessionAttributeHandlers)
/*  176:     */   {
/*  177: 283 */     this.cacheSecondsForSessionAttributeHandlers = cacheSecondsForSessionAttributeHandlers;
/*  178:     */   }
/*  179:     */   
/*  180:     */   public void setSynchronizeOnSession(boolean synchronizeOnSession)
/*  181:     */   {
/*  182: 305 */     this.synchronizeOnSession = synchronizeOnSession;
/*  183:     */   }
/*  184:     */   
/*  185:     */   public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer)
/*  186:     */   {
/*  187: 314 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*  188:     */   }
/*  189:     */   
/*  190:     */   public void setCustomArgumentResolver(WebArgumentResolver argumentResolver)
/*  191:     */   {
/*  192: 323 */     this.customArgumentResolvers = new WebArgumentResolver[] { argumentResolver };
/*  193:     */   }
/*  194:     */   
/*  195:     */   public void setCustomArgumentResolvers(WebArgumentResolver[] argumentResolvers)
/*  196:     */   {
/*  197: 332 */     this.customArgumentResolvers = argumentResolvers;
/*  198:     */   }
/*  199:     */   
/*  200:     */   public void setCustomModelAndViewResolver(ModelAndViewResolver customModelAndViewResolver)
/*  201:     */   {
/*  202: 341 */     this.customModelAndViewResolvers = new ModelAndViewResolver[] { customModelAndViewResolver };
/*  203:     */   }
/*  204:     */   
/*  205:     */   public void setCustomModelAndViewResolvers(ModelAndViewResolver[] customModelAndViewResolvers)
/*  206:     */   {
/*  207: 350 */     this.customModelAndViewResolvers = customModelAndViewResolvers;
/*  208:     */   }
/*  209:     */   
/*  210:     */   public void setMessageConverters(HttpMessageConverter<?>[] messageConverters)
/*  211:     */   {
/*  212: 358 */     this.messageConverters = messageConverters;
/*  213:     */   }
/*  214:     */   
/*  215:     */   public HttpMessageConverter<?>[] getMessageConverters()
/*  216:     */   {
/*  217: 365 */     return this.messageConverters;
/*  218:     */   }
/*  219:     */   
/*  220:     */   public void setOrder(int order)
/*  221:     */   {
/*  222: 374 */     this.order = order;
/*  223:     */   }
/*  224:     */   
/*  225:     */   public int getOrder()
/*  226:     */   {
/*  227: 378 */     return this.order;
/*  228:     */   }
/*  229:     */   
/*  230:     */   public void setBeanFactory(BeanFactory beanFactory)
/*  231:     */   {
/*  232: 382 */     if ((beanFactory instanceof ConfigurableBeanFactory))
/*  233:     */     {
/*  234: 383 */       this.beanFactory = ((ConfigurableBeanFactory)beanFactory);
/*  235: 384 */       this.expressionContext = new BeanExpressionContext(this.beanFactory, new RequestScope());
/*  236:     */     }
/*  237:     */   }
/*  238:     */   
/*  239:     */   public boolean supports(Object handler)
/*  240:     */   {
/*  241: 390 */     return getMethodResolver(handler).hasHandlerMethods();
/*  242:     */   }
/*  243:     */   
/*  244:     */   public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*  245:     */     throws Exception
/*  246:     */   {
/*  247: 396 */     Class<?> clazz = ClassUtils.getUserClass(handler);
/*  248: 397 */     Boolean annotatedWithSessionAttributes = (Boolean)this.sessionAnnotatedClassesCache.get(clazz);
/*  249: 398 */     if (annotatedWithSessionAttributes == null)
/*  250:     */     {
/*  251: 399 */       annotatedWithSessionAttributes = Boolean.valueOf(AnnotationUtils.findAnnotation(clazz, SessionAttributes.class) != null);
/*  252: 400 */       this.sessionAnnotatedClassesCache.put(clazz, annotatedWithSessionAttributes);
/*  253:     */     }
/*  254: 403 */     if (annotatedWithSessionAttributes.booleanValue()) {
/*  255: 405 */       checkAndPrepare(request, response, this.cacheSecondsForSessionAttributeHandlers, true);
/*  256:     */     } else {
/*  257: 410 */       checkAndPrepare(request, response, true);
/*  258:     */     }
/*  259: 414 */     if (this.synchronizeOnSession)
/*  260:     */     {
/*  261: 415 */       HttpSession session = request.getSession(false);
/*  262: 416 */       if (session != null)
/*  263:     */       {
/*  264: 417 */         Object mutex = WebUtils.getSessionMutex(session);
/*  265: 418 */         synchronized (mutex)
/*  266:     */         {
/*  267: 419 */           return invokeHandlerMethod(request, response, handler);
/*  268:     */         }
/*  269:     */       }
/*  270:     */     }
/*  271: 424 */     return invokeHandlerMethod(request, response, handler);
/*  272:     */   }
/*  273:     */   
/*  274:     */   protected ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler)
/*  275:     */     throws Exception
/*  276:     */   {
/*  277: 430 */     ServletHandlerMethodResolver methodResolver = getMethodResolver(handler);
/*  278: 431 */     Method handlerMethod = methodResolver.resolveHandlerMethod(request);
/*  279: 432 */     ServletHandlerMethodInvoker methodInvoker = new ServletHandlerMethodInvoker(methodResolver, null);
/*  280: 433 */     ServletWebRequest webRequest = new ServletWebRequest(request, response);
/*  281: 434 */     ExtendedModelMap implicitModel = new BindingAwareModelMap();
/*  282:     */     
/*  283: 436 */     Object result = methodInvoker.invokeHandlerMethod(handlerMethod, handler, webRequest, implicitModel);
/*  284: 437 */     ModelAndView mav = 
/*  285: 438 */       methodInvoker.getModelAndView(handlerMethod, handler.getClass(), result, implicitModel, webRequest);
/*  286: 439 */     methodInvoker.updateModelAttributes(handler, mav != null ? mav.getModel() : null, implicitModel, webRequest);
/*  287: 440 */     return mav;
/*  288:     */   }
/*  289:     */   
/*  290:     */   public long getLastModified(HttpServletRequest request, Object handler)
/*  291:     */   {
/*  292: 452 */     return -1L;
/*  293:     */   }
/*  294:     */   
/*  295:     */   private ServletHandlerMethodResolver getMethodResolver(Object handler)
/*  296:     */   {
/*  297: 460 */     Class handlerClass = ClassUtils.getUserClass(handler);
/*  298: 461 */     ServletHandlerMethodResolver resolver = (ServletHandlerMethodResolver)this.methodResolverCache.get(handlerClass);
/*  299: 462 */     if (resolver == null) {
/*  300: 463 */       synchronized (this.methodResolverCache)
/*  301:     */       {
/*  302: 464 */         resolver = (ServletHandlerMethodResolver)this.methodResolverCache.get(handlerClass);
/*  303: 465 */         if (resolver == null)
/*  304:     */         {
/*  305: 466 */           resolver = new ServletHandlerMethodResolver(handlerClass, null);
/*  306: 467 */           this.methodResolverCache.put(handlerClass, resolver);
/*  307:     */         }
/*  308:     */       }
/*  309:     */     }
/*  310: 471 */     return resolver;
/*  311:     */   }
/*  312:     */   
/*  313:     */   protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object target, String objectName)
/*  314:     */     throws Exception
/*  315:     */   {
/*  316: 489 */     return new ServletRequestDataBinder(target, objectName);
/*  317:     */   }
/*  318:     */   
/*  319:     */   protected HttpInputMessage createHttpInputMessage(HttpServletRequest servletRequest)
/*  320:     */     throws Exception
/*  321:     */   {
/*  322: 501 */     return new ServletServerHttpRequest(servletRequest);
/*  323:     */   }
/*  324:     */   
/*  325:     */   protected HttpOutputMessage createHttpOutputMessage(HttpServletResponse servletResponse)
/*  326:     */     throws Exception
/*  327:     */   {
/*  328: 513 */     return new ServletServerHttpResponse(servletResponse);
/*  329:     */   }
/*  330:     */   
/*  331:     */   private class ServletHandlerMethodResolver
/*  332:     */     extends HandlerMethodResolver
/*  333:     */   {
/*  334: 522 */     private final Map<Method, AnnotationMethodHandlerAdapter.RequestMappingInfo> mappings = new HashMap();
/*  335:     */     
/*  336:     */     private ServletHandlerMethodResolver()
/*  337:     */     {
/*  338: 525 */       init(handlerType);
/*  339:     */     }
/*  340:     */     
/*  341:     */     protected boolean isHandlerMethod(Method method)
/*  342:     */     {
/*  343: 530 */       if (this.mappings.containsKey(method)) {
/*  344: 531 */         return true;
/*  345:     */       }
/*  346: 533 */       RequestMapping mapping = (RequestMapping)AnnotationUtils.findAnnotation(method, RequestMapping.class);
/*  347: 534 */       if (mapping != null)
/*  348:     */       {
/*  349: 535 */         String[] patterns = mapping.value();
/*  350: 536 */         RequestMethod[] methods = new RequestMethod[0];
/*  351: 537 */         String[] params = new String[0];
/*  352: 538 */         String[] headers = new String[0];
/*  353: 539 */         if ((!hasTypeLevelMapping()) || (!Arrays.equals(mapping.method(), getTypeLevelMapping().method()))) {
/*  354: 540 */           methods = mapping.method();
/*  355:     */         }
/*  356: 542 */         if ((!hasTypeLevelMapping()) || (!Arrays.equals(mapping.params(), getTypeLevelMapping().params()))) {
/*  357: 543 */           params = mapping.params();
/*  358:     */         }
/*  359: 545 */         if ((!hasTypeLevelMapping()) || (!Arrays.equals(mapping.headers(), getTypeLevelMapping().headers()))) {
/*  360: 546 */           headers = mapping.headers();
/*  361:     */         }
/*  362: 548 */         AnnotationMethodHandlerAdapter.RequestMappingInfo mappingInfo = new AnnotationMethodHandlerAdapter.RequestMappingInfo(patterns, methods, params, headers);
/*  363: 549 */         this.mappings.put(method, mappingInfo);
/*  364: 550 */         return true;
/*  365:     */       }
/*  366: 552 */       return false;
/*  367:     */     }
/*  368:     */     
/*  369:     */     public Method resolveHandlerMethod(HttpServletRequest request)
/*  370:     */       throws ServletException
/*  371:     */     {
/*  372: 556 */       String lookupPath = AnnotationMethodHandlerAdapter.this.urlPathHelper.getLookupPathForRequest(request);
/*  373: 557 */       Comparator<String> pathComparator = AnnotationMethodHandlerAdapter.this.pathMatcher.getPatternComparator(lookupPath);
/*  374: 558 */       Map<AnnotationMethodHandlerAdapter.RequestSpecificMappingInfo, Method> targetHandlerMethods = new LinkedHashMap();
/*  375: 559 */       Set<String> allowedMethods = new LinkedHashSet(7);
/*  376: 560 */       String resolvedMethodName = null;
/*  377: 561 */       for (Method handlerMethod : getHandlerMethods())
/*  378:     */       {
/*  379: 562 */         AnnotationMethodHandlerAdapter.RequestSpecificMappingInfo mappingInfo = new AnnotationMethodHandlerAdapter.RequestSpecificMappingInfo((AnnotationMethodHandlerAdapter.RequestMappingInfo)this.mappings.get(handlerMethod));
/*  380: 563 */         boolean match = false;
/*  381:     */         String combinedPattern;
/*  382: 564 */         if (mappingInfo.hasPatterns())
/*  383:     */         {
/*  384: 565 */           for (String pattern : mappingInfo.getPatterns())
/*  385:     */           {
/*  386: 566 */             if ((!hasTypeLevelMapping()) && (!pattern.startsWith("/"))) {
/*  387: 567 */               pattern = "/" + pattern;
/*  388:     */             }
/*  389: 569 */             combinedPattern = getCombinedPattern(pattern, lookupPath, request);
/*  390: 570 */             if (combinedPattern != null) {
/*  391: 571 */               if (mappingInfo.matches(request))
/*  392:     */               {
/*  393: 572 */                 match = true;
/*  394: 573 */                 mappingInfo.addMatchedPattern(combinedPattern);
/*  395:     */               }
/*  396:     */               else
/*  397:     */               {
/*  398: 576 */                 if (mappingInfo.matchesRequestMethod(request)) {
/*  399:     */                   break;
/*  400:     */                 }
/*  401: 577 */                 allowedMethods.addAll(mappingInfo.methodNames());
/*  402:     */                 
/*  403: 579 */                 break;
/*  404:     */               }
/*  405:     */             }
/*  406:     */           }
/*  407: 583 */           mappingInfo.sortMatchedPatterns(pathComparator);
/*  408:     */         }
/*  409: 585 */         else if (useTypeLevelMapping(request))
/*  410:     */         {
/*  411: 586 */           String[] typeLevelPatterns = getTypeLevelMapping().value();
/*  412: 587 */           for (String typeLevelPattern : typeLevelPatterns)
/*  413:     */           {
/*  414: 588 */             if (!typeLevelPattern.startsWith("/")) {
/*  415: 589 */               typeLevelPattern = "/" + typeLevelPattern;
/*  416:     */             }
/*  417: 591 */             if (getMatchingPattern(typeLevelPattern, lookupPath) != null) {
/*  418: 592 */               if (mappingInfo.matches(request))
/*  419:     */               {
/*  420: 593 */                 match = true;
/*  421: 594 */                 mappingInfo.addMatchedPattern(typeLevelPattern);
/*  422:     */               }
/*  423:     */               else
/*  424:     */               {
/*  425: 597 */                 if (mappingInfo.matchesRequestMethod(request)) {
/*  426:     */                   break;
/*  427:     */                 }
/*  428: 598 */                 allowedMethods.addAll(mappingInfo.methodNames());
/*  429:     */                 
/*  430: 600 */                 break;
/*  431:     */               }
/*  432:     */             }
/*  433:     */           }
/*  434: 604 */           mappingInfo.sortMatchedPatterns(pathComparator);
/*  435:     */         }
/*  436:     */         else
/*  437:     */         {
/*  438: 608 */           match = mappingInfo.matches(request);
/*  439: 609 */           if ((match) && (mappingInfo.getMethodCount() == 0) && (mappingInfo.getParamCount() == 0) && 
/*  440: 610 */             (resolvedMethodName != null) && (!resolvedMethodName.equals(handlerMethod.getName()))) {
/*  441: 611 */             match = false;
/*  442: 614 */           } else if (!mappingInfo.matchesRequestMethod(request)) {
/*  443: 615 */             allowedMethods.addAll(mappingInfo.methodNames());
/*  444:     */           }
/*  445:     */         }
/*  446: 619 */         if (match)
/*  447:     */         {
/*  448: 620 */           Method oldMappedMethod = (Method)targetHandlerMethods.put(mappingInfo, handlerMethod);
/*  449: 621 */           if ((oldMappedMethod != null) && (oldMappedMethod != handlerMethod))
/*  450:     */           {
/*  451: 622 */             if ((AnnotationMethodHandlerAdapter.this.methodNameResolver != null) && (!mappingInfo.hasPatterns()) && 
/*  452: 623 */               (!oldMappedMethod.getName().equals(handlerMethod.getName())))
/*  453:     */             {
/*  454: 624 */               if (resolvedMethodName == null) {
/*  455: 625 */                 resolvedMethodName = AnnotationMethodHandlerAdapter.this.methodNameResolver.getHandlerMethodName(request);
/*  456:     */               }
/*  457: 627 */               if (!resolvedMethodName.equals(oldMappedMethod.getName())) {
/*  458: 628 */                 oldMappedMethod = null;
/*  459:     */               }
/*  460: 630 */               if (!resolvedMethodName.equals(handlerMethod.getName())) {
/*  461: 631 */                 if (oldMappedMethod != null)
/*  462:     */                 {
/*  463: 632 */                   targetHandlerMethods.put(mappingInfo, oldMappedMethod);
/*  464: 633 */                   oldMappedMethod = null;
/*  465:     */                 }
/*  466:     */                 else
/*  467:     */                 {
/*  468: 636 */                   targetHandlerMethods.remove(mappingInfo);
/*  469:     */                 }
/*  470:     */               }
/*  471:     */             }
/*  472: 641 */             if (oldMappedMethod != null) {
/*  473: 642 */               throw new IllegalStateException(
/*  474: 643 */                 "Ambiguous handler methods mapped for HTTP path '" + lookupPath + "': {" + 
/*  475: 644 */                 oldMappedMethod + ", " + handlerMethod + 
/*  476: 645 */                 "}. If you intend to handle the same path in multiple methods, then factor " + 
/*  477: 646 */                 "them out into a dedicated handler class with that path mapped at the type level!");
/*  478:     */             }
/*  479:     */           }
/*  480:     */         }
/*  481:     */       }
/*  482: 651 */       if (!targetHandlerMethods.isEmpty())
/*  483:     */       {
/*  484: 652 */         List<AnnotationMethodHandlerAdapter.RequestSpecificMappingInfo> matches = new ArrayList((Collection)targetHandlerMethods.keySet());
/*  485: 653 */         AnnotationMethodHandlerAdapter.RequestSpecificMappingInfoComparator requestMappingInfoComparator = 
/*  486: 654 */           new AnnotationMethodHandlerAdapter.RequestSpecificMappingInfoComparator(pathComparator, request);
/*  487: 655 */         Collections.sort(matches, requestMappingInfoComparator);
/*  488: 656 */         AnnotationMethodHandlerAdapter.RequestSpecificMappingInfo bestMappingMatch = (AnnotationMethodHandlerAdapter.RequestSpecificMappingInfo)matches.get(0);
/*  489: 657 */         String bestMatchedPath = bestMappingMatch.bestMatchedPattern();
/*  490: 658 */         if (bestMatchedPath != null) {
/*  491: 659 */           extractHandlerMethodUriTemplates(bestMatchedPath, lookupPath, request);
/*  492:     */         }
/*  493: 661 */         return (Method)targetHandlerMethods.get(bestMappingMatch);
/*  494:     */       }
/*  495: 664 */       if (!allowedMethods.isEmpty()) {
/*  496: 665 */         throw new HttpRequestMethodNotSupportedException(request.getMethod(), StringUtils.toStringArray(allowedMethods));
/*  497:     */       }
/*  498: 667 */       throw new NoSuchRequestHandlingMethodException(lookupPath, request.getMethod(), request.getParameterMap());
/*  499:     */     }
/*  500:     */     
/*  501:     */     private boolean useTypeLevelMapping(HttpServletRequest request)
/*  502:     */     {
/*  503: 672 */       if ((!hasTypeLevelMapping()) || (ObjectUtils.isEmpty(getTypeLevelMapping().value()))) {
/*  504: 673 */         return false;
/*  505:     */       }
/*  506: 675 */       return ((Boolean)request.getAttribute(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING)).booleanValue();
/*  507:     */     }
/*  508:     */     
/*  509:     */     private String getCombinedPattern(String methodLevelPattern, String lookupPath, HttpServletRequest request)
/*  510:     */     {
/*  511: 690 */       if (useTypeLevelMapping(request))
/*  512:     */       {
/*  513: 691 */         String[] typeLevelPatterns = getTypeLevelMapping().value();
/*  514: 692 */         for (String typeLevelPattern : typeLevelPatterns)
/*  515:     */         {
/*  516: 693 */           if (!typeLevelPattern.startsWith("/")) {
/*  517: 694 */             typeLevelPattern = "/" + typeLevelPattern;
/*  518:     */           }
/*  519: 696 */           String combinedPattern = AnnotationMethodHandlerAdapter.this.pathMatcher.combine(typeLevelPattern, methodLevelPattern);
/*  520: 697 */           String matchingPattern = getMatchingPattern(combinedPattern, lookupPath);
/*  521: 698 */           if (matchingPattern != null) {
/*  522: 699 */             return matchingPattern;
/*  523:     */           }
/*  524:     */         }
/*  525: 702 */         return null;
/*  526:     */       }
/*  527: 704 */       String bestMatchingPattern = (String)request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
/*  528: 705 */       if ((StringUtils.hasText(bestMatchingPattern)) && (bestMatchingPattern.endsWith("*")))
/*  529:     */       {
/*  530: 706 */         String combinedPattern = AnnotationMethodHandlerAdapter.this.pathMatcher.combine(bestMatchingPattern, methodLevelPattern);
/*  531: 707 */         String matchingPattern = getMatchingPattern(combinedPattern, lookupPath);
/*  532: 708 */         if ((matchingPattern != null) && (!matchingPattern.equals(bestMatchingPattern))) {
/*  533: 709 */           return matchingPattern;
/*  534:     */         }
/*  535:     */       }
/*  536: 712 */       return getMatchingPattern(methodLevelPattern, lookupPath);
/*  537:     */     }
/*  538:     */     
/*  539:     */     private String getMatchingPattern(String pattern, String lookupPath)
/*  540:     */     {
/*  541: 716 */       if (pattern.equals(lookupPath)) {
/*  542: 717 */         return pattern;
/*  543:     */       }
/*  544: 719 */       boolean hasSuffix = pattern.indexOf('.') != -1;
/*  545: 720 */       if (!hasSuffix)
/*  546:     */       {
/*  547: 721 */         String patternWithSuffix = pattern + ".*";
/*  548: 722 */         if (AnnotationMethodHandlerAdapter.this.pathMatcher.match(patternWithSuffix, lookupPath)) {
/*  549: 723 */           return patternWithSuffix;
/*  550:     */         }
/*  551:     */       }
/*  552: 726 */       if (AnnotationMethodHandlerAdapter.this.pathMatcher.match(pattern, lookupPath)) {
/*  553: 727 */         return pattern;
/*  554:     */       }
/*  555: 729 */       boolean endsWithSlash = pattern.endsWith("/");
/*  556: 730 */       if (!endsWithSlash)
/*  557:     */       {
/*  558: 731 */         String patternWithSlash = pattern + "/";
/*  559: 732 */         if (AnnotationMethodHandlerAdapter.this.pathMatcher.match(patternWithSlash, lookupPath)) {
/*  560: 733 */           return patternWithSlash;
/*  561:     */         }
/*  562:     */       }
/*  563: 736 */       return null;
/*  564:     */     }
/*  565:     */     
/*  566:     */     private void extractHandlerMethodUriTemplates(String mappedPattern, String lookupPath, HttpServletRequest request)
/*  567:     */     {
/*  568: 741 */       Map<String, String> variables = 
/*  569: 742 */         (Map)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
/*  570: 743 */       int patternVariableCount = StringUtils.countOccurrencesOf(mappedPattern, "{");
/*  571: 744 */       if (((variables == null) || (patternVariableCount != variables.size())) && (AnnotationMethodHandlerAdapter.this.pathMatcher.match(mappedPattern, lookupPath)))
/*  572:     */       {
/*  573: 745 */         variables = AnnotationMethodHandlerAdapter.this.pathMatcher.extractUriTemplateVariables(mappedPattern, lookupPath);
/*  574: 746 */         request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, variables);
/*  575:     */       }
/*  576:     */     }
/*  577:     */   }
/*  578:     */   
/*  579:     */   private class ServletHandlerMethodInvoker
/*  580:     */     extends HandlerMethodInvoker
/*  581:     */   {
/*  582: 757 */     private boolean responseArgumentUsed = false;
/*  583:     */     
/*  584:     */     private ServletHandlerMethodInvoker(HandlerMethodResolver resolver)
/*  585:     */     {
/*  586: 761 */       super(AnnotationMethodHandlerAdapter.this.webBindingInitializer, AnnotationMethodHandlerAdapter.this.sessionAttributeStore, AnnotationMethodHandlerAdapter.this.parameterNameDiscoverer, AnnotationMethodHandlerAdapter.this.customArgumentResolvers, AnnotationMethodHandlerAdapter.this.messageConverters);
/*  587:     */     }
/*  588:     */     
/*  589:     */     protected void raiseMissingParameterException(String paramName, Class paramType)
/*  590:     */       throws Exception
/*  591:     */     {
/*  592: 766 */       throw new MissingServletRequestParameterException(paramName, paramType.getSimpleName());
/*  593:     */     }
/*  594:     */     
/*  595:     */     protected void raiseSessionRequiredException(String message)
/*  596:     */       throws Exception
/*  597:     */     {
/*  598: 771 */       throw new HttpSessionRequiredException(message);
/*  599:     */     }
/*  600:     */     
/*  601:     */     protected WebDataBinder createBinder(NativeWebRequest webRequest, Object target, String objectName)
/*  602:     */       throws Exception
/*  603:     */     {
/*  604: 778 */       return AnnotationMethodHandlerAdapter.this.createBinder(
/*  605: 779 */         (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class), target, objectName);
/*  606:     */     }
/*  607:     */     
/*  608:     */     protected void doBind(WebDataBinder binder, NativeWebRequest webRequest)
/*  609:     */       throws Exception
/*  610:     */     {
/*  611: 784 */       ServletRequestDataBinder servletBinder = (ServletRequestDataBinder)binder;
/*  612: 785 */       servletBinder.bind((ServletRequest)webRequest.getNativeRequest(ServletRequest.class));
/*  613:     */     }
/*  614:     */     
/*  615:     */     protected HttpInputMessage createHttpInputMessage(NativeWebRequest webRequest)
/*  616:     */       throws Exception
/*  617:     */     {
/*  618: 790 */       HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/*  619: 791 */       return AnnotationMethodHandlerAdapter.this.createHttpInputMessage(servletRequest);
/*  620:     */     }
/*  621:     */     
/*  622:     */     protected HttpOutputMessage createHttpOutputMessage(NativeWebRequest webRequest)
/*  623:     */       throws Exception
/*  624:     */     {
/*  625: 796 */       HttpServletResponse servletResponse = (HttpServletResponse)webRequest.getNativeResponse();
/*  626: 797 */       return AnnotationMethodHandlerAdapter.this.createHttpOutputMessage(servletResponse);
/*  627:     */     }
/*  628:     */     
/*  629:     */     protected Object resolveDefaultValue(String value)
/*  630:     */     {
/*  631: 802 */       if (AnnotationMethodHandlerAdapter.this.beanFactory == null) {
/*  632: 803 */         return value;
/*  633:     */       }
/*  634: 805 */       String placeholdersResolved = AnnotationMethodHandlerAdapter.this.beanFactory.resolveEmbeddedValue(value);
/*  635: 806 */       BeanExpressionResolver exprResolver = AnnotationMethodHandlerAdapter.this.beanFactory.getBeanExpressionResolver();
/*  636: 807 */       if (exprResolver == null) {
/*  637: 808 */         return value;
/*  638:     */       }
/*  639: 810 */       return exprResolver.evaluate(placeholdersResolved, AnnotationMethodHandlerAdapter.this.expressionContext);
/*  640:     */     }
/*  641:     */     
/*  642:     */     protected Object resolveCookieValue(String cookieName, Class paramType, NativeWebRequest webRequest)
/*  643:     */       throws Exception
/*  644:     */     {
/*  645: 817 */       HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/*  646: 818 */       Cookie cookieValue = WebUtils.getCookie(servletRequest, cookieName);
/*  647: 819 */       if (Cookie.class.isAssignableFrom(paramType)) {
/*  648: 820 */         return cookieValue;
/*  649:     */       }
/*  650: 822 */       if (cookieValue != null) {
/*  651: 823 */         return AnnotationMethodHandlerAdapter.this.urlPathHelper.decodeRequestString(servletRequest, cookieValue.getValue());
/*  652:     */       }
/*  653: 826 */       return null;
/*  654:     */     }
/*  655:     */     
/*  656:     */     protected String resolvePathVariable(String pathVarName, Class paramType, NativeWebRequest webRequest)
/*  657:     */       throws Exception
/*  658:     */     {
/*  659: 835 */       HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/*  660: 836 */       Map<String, String> uriTemplateVariables = 
/*  661: 837 */         (Map)servletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
/*  662: 838 */       if ((uriTemplateVariables == null) || (!uriTemplateVariables.containsKey(pathVarName))) {
/*  663: 839 */         throw new IllegalStateException(
/*  664: 840 */           "Could not find @PathVariable [" + pathVarName + "] in @RequestMapping");
/*  665:     */       }
/*  666: 842 */       return (String)uriTemplateVariables.get(pathVarName);
/*  667:     */     }
/*  668:     */     
/*  669:     */     protected Object resolveStandardArgument(Class<?> parameterType, NativeWebRequest webRequest)
/*  670:     */       throws Exception
/*  671:     */     {
/*  672: 847 */       HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/*  673: 848 */       HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class);
/*  674: 850 */       if ((ServletRequest.class.isAssignableFrom(parameterType)) || 
/*  675: 851 */         (MultipartRequest.class.isAssignableFrom(parameterType)))
/*  676:     */       {
/*  677: 852 */         Object nativeRequest = webRequest.getNativeRequest(parameterType);
/*  678: 853 */         if (nativeRequest == null) {
/*  679: 854 */           throw new IllegalStateException(
/*  680: 855 */             "Current request is not of type [" + parameterType.getName() + "]: " + request);
/*  681:     */         }
/*  682: 857 */         return nativeRequest;
/*  683:     */       }
/*  684: 859 */       if (ServletResponse.class.isAssignableFrom(parameterType))
/*  685:     */       {
/*  686: 860 */         this.responseArgumentUsed = true;
/*  687: 861 */         Object nativeResponse = webRequest.getNativeResponse(parameterType);
/*  688: 862 */         if (nativeResponse == null) {
/*  689: 863 */           throw new IllegalStateException(
/*  690: 864 */             "Current response is not of type [" + parameterType.getName() + "]: " + response);
/*  691:     */         }
/*  692: 866 */         return nativeResponse;
/*  693:     */       }
/*  694: 868 */       if (HttpSession.class.isAssignableFrom(parameterType)) {
/*  695: 869 */         return request.getSession();
/*  696:     */       }
/*  697: 871 */       if (Principal.class.isAssignableFrom(parameterType)) {
/*  698: 872 */         return request.getUserPrincipal();
/*  699:     */       }
/*  700: 874 */       if (Locale.class.equals(parameterType)) {
/*  701: 875 */         return RequestContextUtils.getLocale(request);
/*  702:     */       }
/*  703: 877 */       if (InputStream.class.isAssignableFrom(parameterType)) {
/*  704: 878 */         return request.getInputStream();
/*  705:     */       }
/*  706: 880 */       if (Reader.class.isAssignableFrom(parameterType)) {
/*  707: 881 */         return request.getReader();
/*  708:     */       }
/*  709: 883 */       if (OutputStream.class.isAssignableFrom(parameterType))
/*  710:     */       {
/*  711: 884 */         this.responseArgumentUsed = true;
/*  712: 885 */         return response.getOutputStream();
/*  713:     */       }
/*  714: 887 */       if (Writer.class.isAssignableFrom(parameterType))
/*  715:     */       {
/*  716: 888 */         this.responseArgumentUsed = true;
/*  717: 889 */         return response.getWriter();
/*  718:     */       }
/*  719: 891 */       return super.resolveStandardArgument(parameterType, webRequest);
/*  720:     */     }
/*  721:     */     
/*  722:     */     public ModelAndView getModelAndView(Method handlerMethod, Class handlerType, Object returnValue, ExtendedModelMap implicitModel, ServletWebRequest webRequest)
/*  723:     */       throws Exception
/*  724:     */     {
/*  725: 898 */       ResponseStatus responseStatusAnn = (ResponseStatus)AnnotationUtils.findAnnotation(handlerMethod, ResponseStatus.class);
/*  726:     */       String reason;
/*  727: 899 */       if (responseStatusAnn != null)
/*  728:     */       {
/*  729: 900 */         HttpStatus responseStatus = responseStatusAnn.value();
/*  730: 901 */         reason = responseStatusAnn.reason();
/*  731: 902 */         if (!StringUtils.hasText(reason)) {
/*  732: 903 */           webRequest.getResponse().setStatus(responseStatus.value());
/*  733:     */         } else {
/*  734: 906 */           webRequest.getResponse().sendError(responseStatus.value(), reason);
/*  735:     */         }
/*  736: 910 */         webRequest.getRequest().setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, responseStatus);
/*  737:     */         
/*  738: 912 */         this.responseArgumentUsed = true;
/*  739:     */       }
/*  740: 916 */       if (AnnotationMethodHandlerAdapter.this.customModelAndViewResolvers != null)
/*  741:     */       {
/*  742:     */         ModelAndViewResolver[] arrayOfModelAndViewResolver;
/*  743: 917 */         String str1 = (arrayOfModelAndViewResolver = AnnotationMethodHandlerAdapter.this.customModelAndViewResolvers).length;
/*  744: 917 */         for (reason = 0; reason < str1; reason++)
/*  745:     */         {
/*  746: 917 */           ModelAndViewResolver mavResolver = arrayOfModelAndViewResolver[reason];
/*  747: 918 */           ModelAndView mav = mavResolver.resolveModelAndView(
/*  748: 919 */             handlerMethod, handlerType, returnValue, implicitModel, webRequest);
/*  749: 920 */           if (mav != ModelAndViewResolver.UNRESOLVED) {
/*  750: 921 */             return mav;
/*  751:     */           }
/*  752:     */         }
/*  753:     */       }
/*  754: 926 */       if ((returnValue instanceof HttpEntity))
/*  755:     */       {
/*  756: 927 */         handleHttpEntityResponse((HttpEntity)returnValue, webRequest);
/*  757: 928 */         return null;
/*  758:     */       }
/*  759: 930 */       if (AnnotationUtils.findAnnotation(handlerMethod, ResponseBody.class) != null)
/*  760:     */       {
/*  761: 931 */         handleResponseBody(returnValue, webRequest);
/*  762: 932 */         return null;
/*  763:     */       }
/*  764: 934 */       if ((returnValue instanceof ModelAndView))
/*  765:     */       {
/*  766: 935 */         ModelAndView mav = (ModelAndView)returnValue;
/*  767: 936 */         mav.getModelMap().mergeAttributes(implicitModel);
/*  768: 937 */         return mav;
/*  769:     */       }
/*  770: 939 */       if ((returnValue instanceof Model)) {
/*  771: 940 */         return new ModelAndView().addAllObjects(implicitModel).addAllObjects(((Model)returnValue).asMap());
/*  772:     */       }
/*  773: 942 */       if ((returnValue instanceof View)) {
/*  774: 943 */         return new ModelAndView((View)returnValue).addAllObjects(implicitModel);
/*  775:     */       }
/*  776: 945 */       if (AnnotationUtils.findAnnotation(handlerMethod, ModelAttribute.class) != null)
/*  777:     */       {
/*  778: 946 */         addReturnValueAsModelAttribute(handlerMethod, handlerType, returnValue, implicitModel);
/*  779: 947 */         return new ModelAndView().addAllObjects(implicitModel);
/*  780:     */       }
/*  781: 949 */       if ((returnValue instanceof Map)) {
/*  782: 950 */         return new ModelAndView().addAllObjects(implicitModel).addAllObjects((Map)returnValue);
/*  783:     */       }
/*  784: 952 */       if ((returnValue instanceof String)) {
/*  785: 953 */         return new ModelAndView((String)returnValue).addAllObjects(implicitModel);
/*  786:     */       }
/*  787: 955 */       if (returnValue == null)
/*  788:     */       {
/*  789: 957 */         if ((this.responseArgumentUsed) || (webRequest.isNotModified())) {
/*  790: 958 */           return null;
/*  791:     */         }
/*  792: 962 */         return new ModelAndView().addAllObjects(implicitModel);
/*  793:     */       }
/*  794: 965 */       if (!BeanUtils.isSimpleProperty(returnValue.getClass()))
/*  795:     */       {
/*  796: 967 */         addReturnValueAsModelAttribute(handlerMethod, handlerType, returnValue, implicitModel);
/*  797: 968 */         return new ModelAndView().addAllObjects(implicitModel);
/*  798:     */       }
/*  799: 971 */       throw new IllegalArgumentException("Invalid handler method return value: " + returnValue);
/*  800:     */     }
/*  801:     */     
/*  802:     */     private void handleResponseBody(Object returnValue, ServletWebRequest webRequest)
/*  803:     */       throws Exception
/*  804:     */     {
/*  805: 977 */       if (returnValue == null) {
/*  806: 978 */         return;
/*  807:     */       }
/*  808: 980 */       HttpInputMessage inputMessage = createHttpInputMessage(webRequest);
/*  809: 981 */       HttpOutputMessage outputMessage = createHttpOutputMessage(webRequest);
/*  810: 982 */       writeWithMessageConverters(returnValue, inputMessage, outputMessage);
/*  811:     */     }
/*  812:     */     
/*  813:     */     private void handleHttpEntityResponse(HttpEntity<?> responseEntity, ServletWebRequest webRequest)
/*  814:     */       throws Exception
/*  815:     */     {
/*  816: 987 */       if (responseEntity == null) {
/*  817: 988 */         return;
/*  818:     */       }
/*  819: 990 */       HttpInputMessage inputMessage = createHttpInputMessage(webRequest);
/*  820: 991 */       HttpOutputMessage outputMessage = createHttpOutputMessage(webRequest);
/*  821: 992 */       if (((responseEntity instanceof ResponseEntity)) && ((outputMessage instanceof ServerHttpResponse))) {
/*  822: 993 */         ((ServerHttpResponse)outputMessage).setStatusCode(((ResponseEntity)responseEntity).getStatusCode());
/*  823:     */       }
/*  824: 995 */       HttpHeaders entityHeaders = responseEntity.getHeaders();
/*  825: 996 */       if (!entityHeaders.isEmpty()) {
/*  826: 997 */         outputMessage.getHeaders().putAll(entityHeaders);
/*  827:     */       }
/*  828: 999 */       Object body = responseEntity.getBody();
/*  829:1000 */       if (body != null) {
/*  830:1001 */         writeWithMessageConverters(body, inputMessage, outputMessage);
/*  831:     */       } else {
/*  832:1005 */         outputMessage.getBody();
/*  833:     */       }
/*  834:     */     }
/*  835:     */     
/*  836:     */     private void writeWithMessageConverters(Object returnValue, HttpInputMessage inputMessage, HttpOutputMessage outputMessage)
/*  837:     */       throws IOException, HttpMediaTypeNotAcceptableException
/*  838:     */     {
/*  839:1013 */       List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
/*  840:1014 */       if (acceptedMediaTypes.isEmpty()) {
/*  841:1015 */         acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
/*  842:     */       }
/*  843:1017 */       MediaType.sortByQualityValue(acceptedMediaTypes);
/*  844:1018 */       Class<?> returnValueType = returnValue.getClass();
/*  845:1019 */       List<MediaType> allSupportedMediaTypes = new ArrayList();
/*  846:1020 */       if (AnnotationMethodHandlerAdapter.this.getMessageConverters() != null)
/*  847:     */       {
/*  848:     */         int j;
/*  849:     */         int i;
/*  850:1021 */         for (Iterator localIterator = acceptedMediaTypes.iterator(); localIterator.hasNext(); i < j)
/*  851:     */         {
/*  852:1021 */           MediaType acceptedMediaType = (MediaType)localIterator.next();
/*  853:     */           HttpMessageConverter[] arrayOfHttpMessageConverter2;
/*  854:1022 */           j = (arrayOfHttpMessageConverter2 = AnnotationMethodHandlerAdapter.this.getMessageConverters()).length;i = 0; continue;messageConverter = arrayOfHttpMessageConverter2[i];
/*  855:1023 */           if (messageConverter.canWrite(returnValueType, acceptedMediaType))
/*  856:     */           {
/*  857:1024 */             messageConverter.write(returnValue, acceptedMediaType, outputMessage);
/*  858:1025 */             if (AnnotationMethodHandlerAdapter.access$11(AnnotationMethodHandlerAdapter.this).isDebugEnabled())
/*  859:     */             {
/*  860:1026 */               MediaType contentType = outputMessage.getHeaders().getContentType();
/*  861:1027 */               if (contentType == null) {
/*  862:1028 */                 contentType = acceptedMediaType;
/*  863:     */               }
/*  864:1030 */               AnnotationMethodHandlerAdapter.access$11(AnnotationMethodHandlerAdapter.this).debug("Written [" + returnValue + "] as \"" + contentType + 
/*  865:1031 */                 "\" using [" + messageConverter + "]");
/*  866:     */             }
/*  867:1033 */             this.responseArgumentUsed = true; return;
/*  868:     */           }
/*  869:1022 */           i++;
/*  870:     */         }
/*  871:     */         HttpMessageConverter[] arrayOfHttpMessageConverter1;
/*  872:1038 */         HttpMessageConverter messageConverter = (arrayOfHttpMessageConverter1 = AnnotationMethodHandlerAdapter.this.messageConverters).length;
/*  873:1038 */         for (HttpMessageConverter localHttpMessageConverter1 = 0; localHttpMessageConverter1 < messageConverter; localHttpMessageConverter1++)
/*  874:     */         {
/*  875:1038 */           HttpMessageConverter messageConverter = arrayOfHttpMessageConverter1[localHttpMessageConverter1];
/*  876:1039 */           allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
/*  877:     */         }
/*  878:     */       }
/*  879:1042 */       throw new HttpMediaTypeNotAcceptableException(allSupportedMediaTypes);
/*  880:     */     }
/*  881:     */   }
/*  882:     */   
/*  883:     */   static class RequestMappingInfo
/*  884:     */   {
/*  885:     */     private final String[] patterns;
/*  886:     */     private final RequestMethod[] methods;
/*  887:     */     private final String[] params;
/*  888:     */     private final String[] headers;
/*  889:     */     
/*  890:     */     RequestMappingInfo(String[] patterns, RequestMethod[] methods, String[] params, String[] headers)
/*  891:     */     {
/*  892:1062 */       this.patterns = (patterns != null ? patterns : new String[0]);
/*  893:1063 */       this.methods = (methods != null ? methods : new RequestMethod[0]);
/*  894:1064 */       this.params = (params != null ? params : new String[0]);
/*  895:1065 */       this.headers = (headers != null ? headers : new String[0]);
/*  896:     */     }
/*  897:     */     
/*  898:     */     public boolean hasPatterns()
/*  899:     */     {
/*  900:1069 */       return this.patterns.length > 0;
/*  901:     */     }
/*  902:     */     
/*  903:     */     public String[] getPatterns()
/*  904:     */     {
/*  905:1073 */       return this.patterns;
/*  906:     */     }
/*  907:     */     
/*  908:     */     public int getMethodCount()
/*  909:     */     {
/*  910:1077 */       return this.methods.length;
/*  911:     */     }
/*  912:     */     
/*  913:     */     public int getParamCount()
/*  914:     */     {
/*  915:1081 */       return this.params.length;
/*  916:     */     }
/*  917:     */     
/*  918:     */     public int getHeaderCount()
/*  919:     */     {
/*  920:1085 */       return this.headers.length;
/*  921:     */     }
/*  922:     */     
/*  923:     */     public boolean matches(HttpServletRequest request)
/*  924:     */     {
/*  925:1089 */       return (matchesRequestMethod(request)) && (matchesParameters(request)) && (matchesHeaders(request));
/*  926:     */     }
/*  927:     */     
/*  928:     */     public boolean matchesHeaders(HttpServletRequest request)
/*  929:     */     {
/*  930:1093 */       return ServletAnnotationMappingUtils.checkHeaders(this.headers, request);
/*  931:     */     }
/*  932:     */     
/*  933:     */     public boolean matchesParameters(HttpServletRequest request)
/*  934:     */     {
/*  935:1097 */       return ServletAnnotationMappingUtils.checkParameters(this.params, request);
/*  936:     */     }
/*  937:     */     
/*  938:     */     public boolean matchesRequestMethod(HttpServletRequest request)
/*  939:     */     {
/*  940:1101 */       return ServletAnnotationMappingUtils.checkRequestMethod(this.methods, request);
/*  941:     */     }
/*  942:     */     
/*  943:     */     public Set<String> methodNames()
/*  944:     */     {
/*  945:1105 */       Set<String> methodNames = new LinkedHashSet(this.methods.length);
/*  946:1106 */       for (RequestMethod method : this.methods) {
/*  947:1107 */         methodNames.add(method.name());
/*  948:     */       }
/*  949:1109 */       return methodNames;
/*  950:     */     }
/*  951:     */     
/*  952:     */     public boolean equals(Object obj)
/*  953:     */     {
/*  954:1114 */       RequestMappingInfo other = (RequestMappingInfo)obj;
/*  955:     */       
/*  956:1116 */       return (Arrays.equals(this.patterns, other.patterns)) && (Arrays.equals(this.methods, other.methods)) && (Arrays.equals(this.params, other.params)) && (Arrays.equals(this.headers, other.headers));
/*  957:     */     }
/*  958:     */     
/*  959:     */     public int hashCode()
/*  960:     */     {
/*  961:1121 */       return Arrays.hashCode(this.patterns) * 23 + Arrays.hashCode(this.methods) * 29 + 
/*  962:1122 */         Arrays.hashCode(this.params) * 31 + Arrays.hashCode(this.headers);
/*  963:     */     }
/*  964:     */     
/*  965:     */     public String toString()
/*  966:     */     {
/*  967:1127 */       StringBuilder builder = new StringBuilder();
/*  968:1128 */       builder.append(Arrays.asList(this.patterns));
/*  969:1129 */       if (this.methods.length > 0)
/*  970:     */       {
/*  971:1130 */         builder.append(',');
/*  972:1131 */         builder.append(Arrays.asList(this.methods));
/*  973:     */       }
/*  974:1133 */       if (this.headers.length > 0)
/*  975:     */       {
/*  976:1134 */         builder.append(',');
/*  977:1135 */         builder.append(Arrays.asList(this.headers));
/*  978:     */       }
/*  979:1137 */       if (this.params.length > 0)
/*  980:     */       {
/*  981:1138 */         builder.append(',');
/*  982:1139 */         builder.append(Arrays.asList(this.params));
/*  983:     */       }
/*  984:1141 */       return builder.toString();
/*  985:     */     }
/*  986:     */   }
/*  987:     */   
/*  988:     */   static class RequestSpecificMappingInfo
/*  989:     */     extends AnnotationMethodHandlerAdapter.RequestMappingInfo
/*  990:     */   {
/*  991:1151 */     private final List<String> matchedPatterns = new ArrayList();
/*  992:     */     
/*  993:     */     RequestSpecificMappingInfo(String[] patterns, RequestMethod[] methods, String[] params, String[] headers)
/*  994:     */     {
/*  995:1154 */       super(methods, params, headers);
/*  996:     */     }
/*  997:     */     
/*  998:     */     RequestSpecificMappingInfo(AnnotationMethodHandlerAdapter.RequestMappingInfo other)
/*  999:     */     {
/* 1000:1158 */       super(other.methods, other.params, other.headers);
/* 1001:     */     }
/* 1002:     */     
/* 1003:     */     public void addMatchedPattern(String matchedPattern)
/* 1004:     */     {
/* 1005:1162 */       this.matchedPatterns.add(matchedPattern);
/* 1006:     */     }
/* 1007:     */     
/* 1008:     */     public void sortMatchedPatterns(Comparator<String> pathComparator)
/* 1009:     */     {
/* 1010:1166 */       Collections.sort(this.matchedPatterns, pathComparator);
/* 1011:     */     }
/* 1012:     */     
/* 1013:     */     public String bestMatchedPattern()
/* 1014:     */     {
/* 1015:1170 */       return !this.matchedPatterns.isEmpty() ? (String)this.matchedPatterns.get(0) : null;
/* 1016:     */     }
/* 1017:     */   }
/* 1018:     */   
/* 1019:     */   static class RequestSpecificMappingInfoComparator
/* 1020:     */     implements Comparator<AnnotationMethodHandlerAdapter.RequestSpecificMappingInfo>
/* 1021:     */   {
/* 1022:     */     private final Comparator<String> pathComparator;
/* 1023:     */     private final ServerHttpRequest request;
/* 1024:     */     
/* 1025:     */     RequestSpecificMappingInfoComparator(Comparator<String> pathComparator, HttpServletRequest request)
/* 1026:     */     {
/* 1027:1196 */       this.pathComparator = pathComparator;
/* 1028:1197 */       this.request = new ServletServerHttpRequest(request);
/* 1029:     */     }
/* 1030:     */     
/* 1031:     */     public int compare(AnnotationMethodHandlerAdapter.RequestSpecificMappingInfo info1, AnnotationMethodHandlerAdapter.RequestSpecificMappingInfo info2)
/* 1032:     */     {
/* 1033:1201 */       int pathComparison = this.pathComparator.compare(info1.bestMatchedPattern(), info2.bestMatchedPattern());
/* 1034:1202 */       if (pathComparison != 0) {
/* 1035:1203 */         return pathComparison;
/* 1036:     */       }
/* 1037:1205 */       int info1ParamCount = info1.getParamCount();
/* 1038:1206 */       int info2ParamCount = info2.getParamCount();
/* 1039:1207 */       if (info1ParamCount != info2ParamCount) {
/* 1040:1208 */         return info2ParamCount - info1ParamCount;
/* 1041:     */       }
/* 1042:1210 */       int info1HeaderCount = info1.getHeaderCount();
/* 1043:1211 */       int info2HeaderCount = info2.getHeaderCount();
/* 1044:1212 */       if (info1HeaderCount != info2HeaderCount) {
/* 1045:1213 */         return info2HeaderCount - info1HeaderCount;
/* 1046:     */       }
/* 1047:1215 */       int acceptComparison = compareAcceptHeaders(info1, info2);
/* 1048:1216 */       if (acceptComparison != 0) {
/* 1049:1217 */         return acceptComparison;
/* 1050:     */       }
/* 1051:1219 */       int info1MethodCount = info1.getMethodCount();
/* 1052:1220 */       int info2MethodCount = info2.getMethodCount();
/* 1053:1221 */       if ((info1MethodCount == 0) && (info2MethodCount > 0)) {
/* 1054:1222 */         return 1;
/* 1055:     */       }
/* 1056:1224 */       if ((info2MethodCount == 0) && (info1MethodCount > 0)) {
/* 1057:1225 */         return -1;
/* 1058:     */       }
/* 1059:1227 */       if (((info1MethodCount == 1 ? 1 : 0) & (info2MethodCount > 1 ? 1 : 0)) != 0) {
/* 1060:1228 */         return -1;
/* 1061:     */       }
/* 1062:1230 */       if (((info2MethodCount == 1 ? 1 : 0) & (info1MethodCount > 1 ? 1 : 0)) != 0) {
/* 1063:1231 */         return 1;
/* 1064:     */       }
/* 1065:1233 */       return 0;
/* 1066:     */     }
/* 1067:     */     
/* 1068:     */     private int compareAcceptHeaders(AnnotationMethodHandlerAdapter.RequestMappingInfo info1, AnnotationMethodHandlerAdapter.RequestMappingInfo info2)
/* 1069:     */     {
/* 1070:1237 */       List<MediaType> requestAccepts = this.request.getHeaders().getAccept();
/* 1071:1238 */       MediaType.sortByQualityValue(requestAccepts);
/* 1072:     */       
/* 1073:1240 */       List<MediaType> info1Accepts = getAcceptHeaderValue(info1);
/* 1074:1241 */       List<MediaType> info2Accepts = getAcceptHeaderValue(info2);
/* 1075:1243 */       for (MediaType requestAccept : requestAccepts)
/* 1076:     */       {
/* 1077:1244 */         int pos1 = indexOfIncluded(info1Accepts, requestAccept);
/* 1078:1245 */         int pos2 = indexOfIncluded(info2Accepts, requestAccept);
/* 1079:1246 */         if (pos1 != pos2) {
/* 1080:1247 */           return pos2 - pos1;
/* 1081:     */         }
/* 1082:     */       }
/* 1083:1250 */       return 0;
/* 1084:     */     }
/* 1085:     */     
/* 1086:     */     private int indexOfIncluded(List<MediaType> infoAccepts, MediaType requestAccept)
/* 1087:     */     {
/* 1088:1254 */       for (int i = 0; i < infoAccepts.size(); i++)
/* 1089:     */       {
/* 1090:1255 */         MediaType info1Accept = (MediaType)infoAccepts.get(i);
/* 1091:1256 */         if (requestAccept.includes(info1Accept)) {
/* 1092:1257 */           return i;
/* 1093:     */         }
/* 1094:     */       }
/* 1095:1260 */       return -1;
/* 1096:     */     }
/* 1097:     */     
/* 1098:     */     private List<MediaType> getAcceptHeaderValue(AnnotationMethodHandlerAdapter.RequestMappingInfo info)
/* 1099:     */     {
/* 1100:1264 */       for (String header : info.headers)
/* 1101:     */       {
/* 1102:1265 */         int separator = header.indexOf('=');
/* 1103:1266 */         if (separator != -1)
/* 1104:     */         {
/* 1105:1267 */           String key = header.substring(0, separator);
/* 1106:1268 */           String value = header.substring(separator + 1);
/* 1107:1269 */           if ("Accept".equalsIgnoreCase(key)) {
/* 1108:1270 */             return MediaType.parseMediaTypes(value);
/* 1109:     */           }
/* 1110:     */         }
/* 1111:     */       }
/* 1112:1274 */       return Collections.emptyList();
/* 1113:     */     }
/* 1114:     */   }
/* 1115:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter
 * JD-Core Version:    0.7.0.1
 */