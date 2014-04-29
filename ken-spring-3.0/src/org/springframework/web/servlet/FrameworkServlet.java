/*   1:    */ package org.springframework.web.servlet;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.security.Principal;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Collections;
/*   7:    */ import javax.servlet.ServletConfig;
/*   8:    */ import javax.servlet.ServletContext;
/*   9:    */ import javax.servlet.ServletException;
/*  10:    */ import javax.servlet.http.HttpServletRequest;
/*  11:    */ import javax.servlet.http.HttpServletResponse;
/*  12:    */ import org.apache.commons.logging.Log;
/*  13:    */ import org.springframework.beans.BeanUtils;
/*  14:    */ import org.springframework.context.ApplicationContext;
/*  15:    */ import org.springframework.context.ApplicationContextException;
/*  16:    */ import org.springframework.context.ApplicationContextInitializer;
/*  17:    */ import org.springframework.context.ApplicationListener;
/*  18:    */ import org.springframework.context.ConfigurableApplicationContext;
/*  19:    */ import org.springframework.context.event.ContextRefreshedEvent;
/*  20:    */ import org.springframework.context.event.SourceFilteringListener;
/*  21:    */ import org.springframework.context.i18n.LocaleContext;
/*  22:    */ import org.springframework.context.i18n.LocaleContextHolder;
/*  23:    */ import org.springframework.context.i18n.SimpleLocaleContext;
/*  24:    */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*  25:    */ import org.springframework.util.ClassUtils;
/*  26:    */ import org.springframework.util.ObjectUtils;
/*  27:    */ import org.springframework.util.StringUtils;
/*  28:    */ import org.springframework.web.context.ConfigurableWebApplicationContext;
/*  29:    */ import org.springframework.web.context.WebApplicationContext;
/*  30:    */ import org.springframework.web.context.request.RequestAttributes;
/*  31:    */ import org.springframework.web.context.request.RequestContextHolder;
/*  32:    */ import org.springframework.web.context.request.ServletRequestAttributes;
/*  33:    */ import org.springframework.web.context.support.ServletRequestHandledEvent;
/*  34:    */ import org.springframework.web.context.support.WebApplicationContextUtils;
/*  35:    */ import org.springframework.web.context.support.XmlWebApplicationContext;
/*  36:    */ import org.springframework.web.util.NestedServletException;
/*  37:    */ import org.springframework.web.util.WebUtils;
/*  38:    */ 
/*  39:    */ public abstract class FrameworkServlet
/*  40:    */   extends HttpServletBean
/*  41:    */ {
/*  42:    */   public static final String DEFAULT_NAMESPACE_SUFFIX = "-servlet";
/*  43:135 */   public static final Class<?> DEFAULT_CONTEXT_CLASS = XmlWebApplicationContext.class;
/*  44:141 */   public static final String SERVLET_CONTEXT_PREFIX = FrameworkServlet.class.getName() + ".CONTEXT.";
/*  45:148 */   private String INIT_PARAM_DELIMITERS = ",; \t\n";
/*  46:    */   private String contextAttribute;
/*  47:154 */   private Class<?> contextClass = DEFAULT_CONTEXT_CLASS;
/*  48:    */   private String contextId;
/*  49:    */   private String namespace;
/*  50:    */   private String contextConfigLocation;
/*  51:166 */   private boolean publishContext = true;
/*  52:169 */   private boolean publishEvents = true;
/*  53:172 */   private boolean threadContextInheritable = false;
/*  54:175 */   private boolean dispatchOptionsRequest = false;
/*  55:178 */   private boolean dispatchTraceRequest = false;
/*  56:    */   private WebApplicationContext webApplicationContext;
/*  57:184 */   private boolean refreshEventReceived = false;
/*  58:    */   private String contextInitializerClasses;
/*  59:191 */   private ArrayList<ApplicationContextInitializer<ConfigurableApplicationContext>> contextInitializers = new ArrayList();
/*  60:    */   
/*  61:    */   public FrameworkServlet() {}
/*  62:    */   
/*  63:    */   public FrameworkServlet(WebApplicationContext webApplicationContext)
/*  64:    */   {
/*  65:255 */     this.webApplicationContext = webApplicationContext;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setContextAttribute(String contextAttribute)
/*  69:    */   {
/*  70:264 */     this.contextAttribute = contextAttribute;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public String getContextAttribute()
/*  74:    */   {
/*  75:272 */     return this.contextAttribute;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setContextClass(Class<?> contextClass)
/*  79:    */   {
/*  80:285 */     this.contextClass = contextClass;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public Class<?> getContextClass()
/*  84:    */   {
/*  85:292 */     return this.contextClass;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void setContextId(String contextId)
/*  89:    */   {
/*  90:300 */     this.contextId = contextId;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public String getContextId()
/*  94:    */   {
/*  95:307 */     return this.contextId;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void setNamespace(String namespace)
/*  99:    */   {
/* 100:315 */     this.namespace = namespace;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public String getNamespace()
/* 104:    */   {
/* 105:323 */     return getServletName() + "-servlet";
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void setContextInitializerClasses(String contextInitializerClasses)
/* 109:    */   {
/* 110:333 */     this.contextInitializerClasses = contextInitializerClasses;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void setContextInitializers(ApplicationContextInitializer<ConfigurableApplicationContext>... contextInitializers)
/* 114:    */   {
/* 115:343 */     for (ApplicationContextInitializer<ConfigurableApplicationContext> initializer : contextInitializers) {
/* 116:344 */       this.contextInitializers.add(initializer);
/* 117:    */     }
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void setContextConfigLocation(String contextConfigLocation)
/* 121:    */   {
/* 122:354 */     this.contextConfigLocation = contextConfigLocation;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public String getContextConfigLocation()
/* 126:    */   {
/* 127:361 */     return this.contextConfigLocation;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public void setPublishContext(boolean publishContext)
/* 131:    */   {
/* 132:371 */     this.publishContext = publishContext;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void setPublishEvents(boolean publishEvents)
/* 136:    */   {
/* 137:381 */     this.publishEvents = publishEvents;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public void setThreadContextInheritable(boolean threadContextInheritable)
/* 141:    */   {
/* 142:397 */     this.threadContextInheritable = threadContextInheritable;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void setDispatchOptionsRequest(boolean dispatchOptionsRequest)
/* 146:    */   {
/* 147:415 */     this.dispatchOptionsRequest = dispatchOptionsRequest;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public void setDispatchTraceRequest(boolean dispatchTraceRequest)
/* 151:    */   {
/* 152:433 */     this.dispatchTraceRequest = dispatchTraceRequest;
/* 153:    */   }
/* 154:    */   
/* 155:    */   protected final void initServletBean()
/* 156:    */     throws ServletException
/* 157:    */   {
/* 158:443 */     getServletContext().log("Initializing Spring FrameworkServlet '" + getServletName() + "'");
/* 159:444 */     if (this.logger.isInfoEnabled()) {
/* 160:445 */       this.logger.info("FrameworkServlet '" + getServletName() + "': initialization started");
/* 161:    */     }
/* 162:447 */     long startTime = System.currentTimeMillis();
/* 163:    */     try
/* 164:    */     {
/* 165:450 */       this.webApplicationContext = initWebApplicationContext();
/* 166:451 */       initFrameworkServlet();
/* 167:    */     }
/* 168:    */     catch (ServletException ex)
/* 169:    */     {
/* 170:454 */       this.logger.error("Context initialization failed", ex);
/* 171:455 */       throw ex;
/* 172:    */     }
/* 173:    */     catch (RuntimeException ex)
/* 174:    */     {
/* 175:458 */       this.logger.error("Context initialization failed", ex);
/* 176:459 */       throw ex;
/* 177:    */     }
/* 178:462 */     if (this.logger.isInfoEnabled())
/* 179:    */     {
/* 180:463 */       long elapsedTime = System.currentTimeMillis() - startTime;
/* 181:464 */       this.logger.info("FrameworkServlet '" + getServletName() + "': initialization completed in " + 
/* 182:465 */         elapsedTime + " ms");
/* 183:    */     }
/* 184:    */   }
/* 185:    */   
/* 186:    */   protected WebApplicationContext initWebApplicationContext()
/* 187:    */   {
/* 188:479 */     WebApplicationContext rootContext = 
/* 189:480 */       WebApplicationContextUtils.getWebApplicationContext(getServletContext());
/* 190:481 */     WebApplicationContext wac = null;
/* 191:483 */     if (this.webApplicationContext != null)
/* 192:    */     {
/* 193:485 */       wac = this.webApplicationContext;
/* 194:486 */       if ((wac instanceof ConfigurableWebApplicationContext))
/* 195:    */       {
/* 196:487 */         ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext)wac;
/* 197:488 */         if (!cwac.isActive())
/* 198:    */         {
/* 199:491 */           if (cwac.getParent() == null) {
/* 200:494 */             cwac.setParent(rootContext);
/* 201:    */           }
/* 202:496 */           configureAndRefreshWebApplicationContext(cwac);
/* 203:    */         }
/* 204:    */       }
/* 205:    */     }
/* 206:500 */     if (wac == null) {
/* 207:505 */       wac = findWebApplicationContext();
/* 208:    */     }
/* 209:507 */     if (wac == null) {
/* 210:509 */       wac = createWebApplicationContext(rootContext);
/* 211:    */     }
/* 212:512 */     if (!this.refreshEventReceived) {
/* 213:516 */       onRefresh(wac);
/* 214:    */     }
/* 215:519 */     if (this.publishContext)
/* 216:    */     {
/* 217:521 */       String attrName = getServletContextAttributeName();
/* 218:522 */       getServletContext().setAttribute(attrName, wac);
/* 219:523 */       if (this.logger.isDebugEnabled()) {
/* 220:524 */         this.logger.debug("Published WebApplicationContext of servlet '" + getServletName() + 
/* 221:525 */           "' as ServletContext attribute with name [" + attrName + "]");
/* 222:    */       }
/* 223:    */     }
/* 224:529 */     return wac;
/* 225:    */   }
/* 226:    */   
/* 227:    */   protected WebApplicationContext findWebApplicationContext()
/* 228:    */   {
/* 229:543 */     String attrName = getContextAttribute();
/* 230:544 */     if (attrName == null) {
/* 231:545 */       return null;
/* 232:    */     }
/* 233:547 */     WebApplicationContext wac = 
/* 234:548 */       WebApplicationContextUtils.getWebApplicationContext(getServletContext(), attrName);
/* 235:549 */     if (wac == null) {
/* 236:550 */       throw new IllegalStateException("No WebApplicationContext found: initializer not registered?");
/* 237:    */     }
/* 238:552 */     return wac;
/* 239:    */   }
/* 240:    */   
/* 241:    */   protected WebApplicationContext createWebApplicationContext(ApplicationContext parent)
/* 242:    */   {
/* 243:571 */     Class<?> contextClass = getContextClass();
/* 244:572 */     if (this.logger.isDebugEnabled()) {
/* 245:573 */       this.logger.debug("Servlet with name '" + getServletName() + 
/* 246:574 */         "' will try to create custom WebApplicationContext context of class '" + 
/* 247:575 */         contextClass.getName() + "'" + ", using parent context [" + parent + "]");
/* 248:    */     }
/* 249:577 */     if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
/* 250:578 */       throw new ApplicationContextException(
/* 251:579 */         "Fatal initialization error in servlet with name '" + getServletName() + 
/* 252:580 */         "': custom WebApplicationContext class [" + contextClass.getName() + 
/* 253:581 */         "] is not of type ConfigurableWebApplicationContext");
/* 254:    */     }
/* 255:583 */     ConfigurableWebApplicationContext wac = 
/* 256:584 */       (ConfigurableWebApplicationContext)BeanUtils.instantiateClass(contextClass);
/* 257:    */     
/* 258:586 */     wac.setParent(parent);
/* 259:587 */     wac.setConfigLocation(getContextConfigLocation());
/* 260:    */     
/* 261:589 */     configureAndRefreshWebApplicationContext(wac);
/* 262:    */     
/* 263:591 */     return wac;
/* 264:    */   }
/* 265:    */   
/* 266:    */   protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac)
/* 267:    */   {
/* 268:595 */     if (ObjectUtils.identityToString(wac).equals(wac.getId())) {
/* 269:598 */       if (this.contextId != null)
/* 270:    */       {
/* 271:599 */         wac.setId(this.contextId);
/* 272:    */       }
/* 273:    */       else
/* 274:    */       {
/* 275:603 */         ServletContext sc = getServletContext();
/* 276:604 */         if ((sc.getMajorVersion() == 2) && (sc.getMinorVersion() < 5))
/* 277:    */         {
/* 278:606 */           String servletContextName = sc.getServletContextName();
/* 279:607 */           if (servletContextName != null) {
/* 280:608 */             wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + servletContextName + 
/* 281:609 */               "." + getServletName());
/* 282:    */           } else {
/* 283:612 */             wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + getServletName());
/* 284:    */           }
/* 285:    */         }
/* 286:    */         else
/* 287:    */         {
/* 288:617 */           wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + 
/* 289:618 */             ObjectUtils.getDisplayString(sc.getContextPath()) + "/" + getServletName());
/* 290:    */         }
/* 291:    */       }
/* 292:    */     }
/* 293:623 */     wac.setServletContext(getServletContext());
/* 294:624 */     wac.setServletConfig(getServletConfig());
/* 295:625 */     wac.setNamespace(getNamespace());
/* 296:626 */     wac.addApplicationListener(new SourceFilteringListener(wac, new ContextRefreshListener(null)));
/* 297:    */     
/* 298:628 */     postProcessWebApplicationContext(wac);
/* 299:    */     
/* 300:630 */     applyInitializers(wac);
/* 301:    */     
/* 302:632 */     wac.refresh();
/* 303:    */   }
/* 304:    */   
/* 305:    */   protected WebApplicationContext createWebApplicationContext(WebApplicationContext parent)
/* 306:    */   {
/* 307:646 */     return createWebApplicationContext(parent);
/* 308:    */   }
/* 309:    */   
/* 310:    */   protected void applyInitializers(ConfigurableApplicationContext wac)
/* 311:    */   {
/* 312:    */     String initializerClassName;
/* 313:663 */     if (this.contextInitializerClasses != null)
/* 314:    */     {
/* 315:664 */       String[] initializerClassNames = StringUtils.tokenizeToStringArray(this.contextInitializerClasses, this.INIT_PARAM_DELIMITERS);
/* 316:665 */       for (initializerClassName : initializerClassNames)
/* 317:    */       {
/* 318:666 */         ApplicationContextInitializer<ConfigurableApplicationContext> initializer = null;
/* 319:    */         try
/* 320:    */         {
/* 321:668 */           Class<?> initializerClass = ClassUtils.forName(initializerClassName, wac.getClassLoader());
/* 322:669 */           initializer = (ApplicationContextInitializer)BeanUtils.instantiateClass(initializerClass, ApplicationContextInitializer.class);
/* 323:    */         }
/* 324:    */         catch (Exception ex)
/* 325:    */         {
/* 326:671 */           throw new IllegalArgumentException(
/* 327:672 */             String.format("Could not instantiate class [%s] specified via 'contextInitializerClasses' init-param", new Object[] {
/* 328:673 */             initializerClassName }), ex);
/* 329:    */         }
/* 330:675 */         this.contextInitializers.add(initializer);
/* 331:    */       }
/* 332:    */     }
/* 333:679 */     Collections.sort(this.contextInitializers, new AnnotationAwareOrderComparator());
/* 334:681 */     for (ApplicationContextInitializer<ConfigurableApplicationContext> initializer : this.contextInitializers) {
/* 335:682 */       initializer.initialize(wac);
/* 336:    */     }
/* 337:    */   }
/* 338:    */   
/* 339:    */   protected void postProcessWebApplicationContext(ConfigurableWebApplicationContext wac) {}
/* 340:    */   
/* 341:    */   public String getServletContextAttributeName()
/* 342:    */   {
/* 343:711 */     return SERVLET_CONTEXT_PREFIX + getServletName();
/* 344:    */   }
/* 345:    */   
/* 346:    */   public final WebApplicationContext getWebApplicationContext()
/* 347:    */   {
/* 348:718 */     return this.webApplicationContext;
/* 349:    */   }
/* 350:    */   
/* 351:    */   protected void initFrameworkServlet()
/* 352:    */     throws ServletException
/* 353:    */   {}
/* 354:    */   
/* 355:    */   public void refresh()
/* 356:    */   {
/* 357:738 */     WebApplicationContext wac = getWebApplicationContext();
/* 358:739 */     if (!(wac instanceof ConfigurableApplicationContext)) {
/* 359:740 */       throw new IllegalStateException("WebApplicationContext does not support refresh: " + wac);
/* 360:    */     }
/* 361:742 */     ((ConfigurableApplicationContext)wac).refresh();
/* 362:    */   }
/* 363:    */   
/* 364:    */   public void onApplicationEvent(ContextRefreshedEvent event)
/* 365:    */   {
/* 366:752 */     this.refreshEventReceived = true;
/* 367:753 */     onRefresh(event.getApplicationContext());
/* 368:    */   }
/* 369:    */   
/* 370:    */   protected void onRefresh(ApplicationContext context) {}
/* 371:    */   
/* 372:    */   protected final void doGet(HttpServletRequest request, HttpServletResponse response)
/* 373:    */     throws ServletException, IOException
/* 374:    */   {
/* 375:779 */     processRequest(request, response);
/* 376:    */   }
/* 377:    */   
/* 378:    */   protected final void doPost(HttpServletRequest request, HttpServletResponse response)
/* 379:    */     throws ServletException, IOException
/* 380:    */   {
/* 381:790 */     processRequest(request, response);
/* 382:    */   }
/* 383:    */   
/* 384:    */   protected final void doPut(HttpServletRequest request, HttpServletResponse response)
/* 385:    */     throws ServletException, IOException
/* 386:    */   {
/* 387:801 */     processRequest(request, response);
/* 388:    */   }
/* 389:    */   
/* 390:    */   protected final void doDelete(HttpServletRequest request, HttpServletResponse response)
/* 391:    */     throws ServletException, IOException
/* 392:    */   {
/* 393:812 */     processRequest(request, response);
/* 394:    */   }
/* 395:    */   
/* 396:    */   protected void doOptions(HttpServletRequest request, HttpServletResponse response)
/* 397:    */     throws ServletException, IOException
/* 398:    */   {
/* 399:824 */     super.doOptions(request, response);
/* 400:825 */     if (this.dispatchOptionsRequest) {
/* 401:826 */       processRequest(request, response);
/* 402:    */     }
/* 403:    */   }
/* 404:    */   
/* 405:    */   protected void doTrace(HttpServletRequest request, HttpServletResponse response)
/* 406:    */     throws ServletException, IOException
/* 407:    */   {
/* 408:839 */     super.doTrace(request, response);
/* 409:840 */     if (this.dispatchTraceRequest) {
/* 410:841 */       processRequest(request, response);
/* 411:    */     }
/* 412:    */   }
/* 413:    */   
/* 414:    */   protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
/* 415:    */     throws ServletException, IOException
/* 416:    */   {
/* 417:854 */     long startTime = System.currentTimeMillis();
/* 418:855 */     Throwable failureCause = null;
/* 419:    */     
/* 420:    */ 
/* 421:858 */     LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
/* 422:859 */     LocaleContextHolder.setLocaleContext(buildLocaleContext(request), this.threadContextInheritable);
/* 423:    */     
/* 424:    */ 
/* 425:862 */     RequestAttributes previousRequestAttributes = RequestContextHolder.getRequestAttributes();
/* 426:863 */     ServletRequestAttributes requestAttributes = null;
/* 427:864 */     if ((previousRequestAttributes == null) || (previousRequestAttributes.getClass().equals(ServletRequestAttributes.class)))
/* 428:    */     {
/* 429:865 */       requestAttributes = new ServletRequestAttributes(request);
/* 430:866 */       RequestContextHolder.setRequestAttributes(requestAttributes, this.threadContextInheritable);
/* 431:    */     }
/* 432:869 */     if (this.logger.isTraceEnabled()) {
/* 433:870 */       this.logger.trace("Bound request context to thread: " + request);
/* 434:    */     }
/* 435:    */     long processingTime;
/* 436:    */     try
/* 437:    */     {
/* 438:874 */       doService(request, response);
/* 439:    */     }
/* 440:    */     catch (ServletException ex)
/* 441:    */     {
/* 442:877 */       failureCause = ex;
/* 443:878 */       throw ex;
/* 444:    */     }
/* 445:    */     catch (IOException ex)
/* 446:    */     {
/* 447:881 */       failureCause = ex;
/* 448:882 */       throw ex;
/* 449:    */     }
/* 450:    */     catch (Throwable ex)
/* 451:    */     {
/* 452:885 */       failureCause = ex;
/* 453:886 */       throw new NestedServletException("Request processing failed", ex);
/* 454:    */     }
/* 455:    */     finally
/* 456:    */     {
/* 457:891 */       LocaleContextHolder.setLocaleContext(previousLocaleContext, this.threadContextInheritable);
/* 458:892 */       if (requestAttributes != null)
/* 459:    */       {
/* 460:893 */         RequestContextHolder.setRequestAttributes(previousRequestAttributes, this.threadContextInheritable);
/* 461:894 */         requestAttributes.requestCompleted();
/* 462:    */       }
/* 463:896 */       if (this.logger.isTraceEnabled()) {
/* 464:897 */         this.logger.trace("Cleared thread-bound request context: " + request);
/* 465:    */       }
/* 466:900 */       if (this.logger.isDebugEnabled()) {
/* 467:901 */         if (failureCause != null) {
/* 468:902 */           this.logger.debug("Could not complete request", failureCause);
/* 469:    */         } else {
/* 470:905 */           this.logger.debug("Successfully completed request");
/* 471:    */         }
/* 472:    */       }
/* 473:908 */       if (this.publishEvents)
/* 474:    */       {
/* 475:910 */         long processingTime = System.currentTimeMillis() - startTime;
/* 476:911 */         this.webApplicationContext.publishEvent(
/* 477:912 */           new ServletRequestHandledEvent(this, 
/* 478:913 */           request.getRequestURI(), request.getRemoteAddr(), 
/* 479:914 */           request.getMethod(), getServletConfig().getServletName(), 
/* 480:915 */           WebUtils.getSessionId(request), getUsernameForRequest(request), 
/* 481:916 */           processingTime, failureCause));
/* 482:    */       }
/* 483:    */     }
/* 484:    */   }
/* 485:    */   
/* 486:    */   protected LocaleContext buildLocaleContext(HttpServletRequest request)
/* 487:    */   {
/* 488:928 */     return new SimpleLocaleContext(request.getLocale());
/* 489:    */   }
/* 490:    */   
/* 491:    */   protected String getUsernameForRequest(HttpServletRequest request)
/* 492:    */   {
/* 493:940 */     Principal userPrincipal = request.getUserPrincipal();
/* 494:941 */     return userPrincipal != null ? userPrincipal.getName() : null;
/* 495:    */   }
/* 496:    */   
/* 497:    */   protected abstract void doService(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/* 498:    */     throws Exception;
/* 499:    */   
/* 500:    */   public void destroy()
/* 501:    */   {
/* 502:967 */     getServletContext().log("Destroying Spring FrameworkServlet '" + getServletName() + "'");
/* 503:968 */     if ((this.webApplicationContext instanceof ConfigurableApplicationContext)) {
/* 504:969 */       ((ConfigurableApplicationContext)this.webApplicationContext).close();
/* 505:    */     }
/* 506:    */   }
/* 507:    */   
/* 508:    */   private class ContextRefreshListener
/* 509:    */     implements ApplicationListener<ContextRefreshedEvent>
/* 510:    */   {
/* 511:    */     private ContextRefreshListener() {}
/* 512:    */     
/* 513:    */     public void onApplicationEvent(ContextRefreshedEvent event)
/* 514:    */     {
/* 515:981 */       FrameworkServlet.this.onApplicationEvent(event);
/* 516:    */     }
/* 517:    */   }
/* 518:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.FrameworkServlet
 * JD-Core Version:    0.7.0.1
 */