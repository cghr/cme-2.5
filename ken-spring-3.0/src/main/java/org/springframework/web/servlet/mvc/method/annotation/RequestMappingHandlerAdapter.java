/*   1:    */ package org.springframework.web.servlet.mvc.method.annotation;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.concurrent.ConcurrentHashMap;
/*   8:    */ import javax.servlet.http.HttpServletRequest;
/*   9:    */ import javax.servlet.http.HttpServletResponse;
/*  10:    */ import javax.servlet.http.HttpSession;
/*  11:    */ import org.springframework.beans.factory.BeanFactory;
/*  12:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*  13:    */ import org.springframework.beans.factory.InitializingBean;
/*  14:    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*  15:    */ import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
/*  16:    */ import org.springframework.core.MethodParameter;
/*  17:    */ import org.springframework.core.ParameterNameDiscoverer;
/*  18:    */ import org.springframework.core.annotation.AnnotationUtils;
/*  19:    */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*  20:    */ import org.springframework.http.converter.HttpMessageConverter;
/*  21:    */ import org.springframework.http.converter.StringHttpMessageConverter;
/*  22:    */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*  23:    */ import org.springframework.http.converter.xml.XmlAwareFormHttpMessageConverter;
/*  24:    */ import org.springframework.ui.ModelMap;
/*  25:    */ import org.springframework.util.CollectionUtils;
/*  26:    */ import org.springframework.util.ReflectionUtils.MethodFilter;
/*  27:    */ import org.springframework.web.bind.annotation.InitBinder;
/*  28:    */ import org.springframework.web.bind.annotation.ModelAttribute;
/*  29:    */ import org.springframework.web.bind.annotation.RequestMapping;
/*  30:    */ import org.springframework.web.bind.support.DefaultDataBinderFactory;
/*  31:    */ import org.springframework.web.bind.support.DefaultSessionAttributeStore;
/*  32:    */ import org.springframework.web.bind.support.SessionAttributeStore;
/*  33:    */ import org.springframework.web.bind.support.WebBindingInitializer;
/*  34:    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*  35:    */ import org.springframework.web.context.request.ServletWebRequest;
/*  36:    */ import org.springframework.web.method.HandlerMethod;
/*  37:    */ import org.springframework.web.method.HandlerMethodSelector;
/*  38:    */ import org.springframework.web.method.annotation.ModelFactory;
/*  39:    */ import org.springframework.web.method.annotation.SessionAttributesHandler;
/*  40:    */ import org.springframework.web.method.annotation.support.ErrorsMethodArgumentResolver;
/*  41:    */ import org.springframework.web.method.annotation.support.ExpressionValueMethodArgumentResolver;
/*  42:    */ import org.springframework.web.method.annotation.support.MapMethodProcessor;
/*  43:    */ import org.springframework.web.method.annotation.support.ModelAttributeMethodProcessor;
/*  44:    */ import org.springframework.web.method.annotation.support.ModelMethodProcessor;
/*  45:    */ import org.springframework.web.method.annotation.support.RequestHeaderMapMethodArgumentResolver;
/*  46:    */ import org.springframework.web.method.annotation.support.RequestHeaderMethodArgumentResolver;
/*  47:    */ import org.springframework.web.method.annotation.support.RequestParamMapMethodArgumentResolver;
/*  48:    */ import org.springframework.web.method.annotation.support.RequestParamMethodArgumentResolver;
/*  49:    */ import org.springframework.web.method.annotation.support.SessionStatusMethodArgumentResolver;
/*  50:    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*  51:    */ import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
/*  52:    */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*  53:    */ import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
/*  54:    */ import org.springframework.web.method.support.InvocableHandlerMethod;
/*  55:    */ import org.springframework.web.method.support.ModelAndViewContainer;
/*  56:    */ import org.springframework.web.servlet.FlashMap;
/*  57:    */ import org.springframework.web.servlet.ModelAndView;
/*  58:    */ import org.springframework.web.servlet.View;
/*  59:    */ import org.springframework.web.servlet.mvc.annotation.ModelAndViewResolver;
/*  60:    */ import org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter;
/*  61:    */ import org.springframework.web.servlet.mvc.method.annotation.support.HttpEntityMethodProcessor;
/*  62:    */ import org.springframework.web.servlet.mvc.method.annotation.support.ModelAndViewMethodReturnValueHandler;
/*  63:    */ import org.springframework.web.servlet.mvc.method.annotation.support.ModelAndViewResolverMethodReturnValueHandler;
/*  64:    */ import org.springframework.web.servlet.mvc.method.annotation.support.PathVariableMethodArgumentResolver;
/*  65:    */ import org.springframework.web.servlet.mvc.method.annotation.support.RedirectAttributesMethodArgumentResolver;
/*  66:    */ import org.springframework.web.servlet.mvc.method.annotation.support.RequestPartMethodArgumentResolver;
/*  67:    */ import org.springframework.web.servlet.mvc.method.annotation.support.RequestResponseBodyMethodProcessor;
/*  68:    */ import org.springframework.web.servlet.mvc.method.annotation.support.ServletCookieValueMethodArgumentResolver;
/*  69:    */ import org.springframework.web.servlet.mvc.method.annotation.support.ServletModelAttributeMethodProcessor;
/*  70:    */ import org.springframework.web.servlet.mvc.method.annotation.support.ServletRequestMethodArgumentResolver;
/*  71:    */ import org.springframework.web.servlet.mvc.method.annotation.support.ServletResponseMethodArgumentResolver;
/*  72:    */ import org.springframework.web.servlet.mvc.method.annotation.support.ViewMethodReturnValueHandler;
/*  73:    */ import org.springframework.web.servlet.mvc.method.annotation.support.ViewNameMethodReturnValueHandler;
/*  74:    */ import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/*  75:    */ import org.springframework.web.servlet.support.RequestContextUtils;
/*  76:    */ import org.springframework.web.util.WebUtils;
/*  77:    */ 
/*  78:    */ public class RequestMappingHandlerAdapter
/*  79:    */   extends AbstractHandlerMethodAdapter
/*  80:    */   implements BeanFactoryAware, InitializingBean
/*  81:    */ {
/*  82:    */   private List<HandlerMethodArgumentResolver> customArgumentResolvers;
/*  83:    */   private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;
/*  84:    */   private List<ModelAndViewResolver> modelAndViewResolvers;
/*  85:    */   private List<HttpMessageConverter<?>> messageConverters;
/*  86:    */   private WebBindingInitializer webBindingInitializer;
/*  87:126 */   private int cacheSecondsForSessionAttributeHandlers = 0;
/*  88:128 */   private boolean synchronizeOnSession = false;
/*  89:130 */   private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
/*  90:    */   private ConfigurableBeanFactory beanFactory;
/*  91:134 */   private SessionAttributeStore sessionAttributeStore = new DefaultSessionAttributeStore();
/*  92:136 */   private boolean ignoreDefaultModelOnRedirect = false;
/*  93:139 */   private final Map<Class<?>, SessionAttributesHandler> sessionAttributesHandlerCache = new ConcurrentHashMap();
/*  94:    */   private HandlerMethodArgumentResolverComposite argumentResolvers;
/*  95:    */   private HandlerMethodArgumentResolverComposite initBinderArgumentResolvers;
/*  96:    */   private HandlerMethodReturnValueHandlerComposite returnValueHandlers;
/*  97:148 */   private final Map<Class<?>, WebDataBinderFactory> dataBinderFactoryCache = new ConcurrentHashMap();
/*  98:150 */   private final Map<Class<?>, ModelFactory> modelFactoryCache = new ConcurrentHashMap();
/*  99:    */   
/* 100:    */   public RequestMappingHandlerAdapter()
/* 101:    */   {
/* 102:157 */     StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
/* 103:158 */     stringHttpMessageConverter.setWriteAcceptCharset(false);
/* 104:    */     
/* 105:160 */     this.messageConverters = new ArrayList();
/* 106:161 */     this.messageConverters.add(new ByteArrayHttpMessageConverter());
/* 107:162 */     this.messageConverters.add(stringHttpMessageConverter);
/* 108:163 */     this.messageConverters.add(new SourceHttpMessageConverter());
/* 109:164 */     this.messageConverters.add(new XmlAwareFormHttpMessageConverter());
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void setCustomArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
/* 113:    */   {
/* 114:173 */     this.customArgumentResolvers = argumentResolvers;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public List<HandlerMethodArgumentResolver> getCustomArgumentResolvers()
/* 118:    */   {
/* 119:180 */     return this.customArgumentResolvers;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void setArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
/* 123:    */   {
/* 124:188 */     if (argumentResolvers == null)
/* 125:    */     {
/* 126:189 */       this.argumentResolvers = null;
/* 127:    */     }
/* 128:    */     else
/* 129:    */     {
/* 130:192 */       this.argumentResolvers = new HandlerMethodArgumentResolverComposite();
/* 131:193 */       this.argumentResolvers.addResolvers(argumentResolvers);
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   public HandlerMethodArgumentResolverComposite getArgumentResolvers()
/* 136:    */   {
/* 137:202 */     return this.argumentResolvers;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public void setInitBinderArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
/* 141:    */   {
/* 142:209 */     if (argumentResolvers == null)
/* 143:    */     {
/* 144:210 */       this.initBinderArgumentResolvers = null;
/* 145:    */     }
/* 146:    */     else
/* 147:    */     {
/* 148:213 */       this.initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite();
/* 149:214 */       this.initBinderArgumentResolvers.addResolvers(argumentResolvers);
/* 150:    */     }
/* 151:    */   }
/* 152:    */   
/* 153:    */   public HandlerMethodArgumentResolverComposite getInitBinderArgumentResolvers()
/* 154:    */   {
/* 155:223 */     return this.initBinderArgumentResolvers;
/* 156:    */   }
/* 157:    */   
/* 158:    */   public void setCustomReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers)
/* 159:    */   {
/* 160:232 */     this.customReturnValueHandlers = returnValueHandlers;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public List<HandlerMethodReturnValueHandler> getCustomReturnValueHandlers()
/* 164:    */   {
/* 165:239 */     return this.customReturnValueHandlers;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public void setReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers)
/* 169:    */   {
/* 170:247 */     if (returnValueHandlers == null)
/* 171:    */     {
/* 172:248 */       this.returnValueHandlers = null;
/* 173:    */     }
/* 174:    */     else
/* 175:    */     {
/* 176:251 */       this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
/* 177:252 */       this.returnValueHandlers.addHandlers(returnValueHandlers);
/* 178:    */     }
/* 179:    */   }
/* 180:    */   
/* 181:    */   public HandlerMethodReturnValueHandlerComposite getReturnValueHandlers()
/* 182:    */   {
/* 183:261 */     return this.returnValueHandlers;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public void setModelAndViewResolvers(List<ModelAndViewResolver> modelAndViewResolvers)
/* 187:    */   {
/* 188:279 */     this.modelAndViewResolvers = modelAndViewResolvers;
/* 189:    */   }
/* 190:    */   
/* 191:    */   public List<ModelAndViewResolver> getModelAndViewResolvers()
/* 192:    */   {
/* 193:286 */     return this.modelAndViewResolvers;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters)
/* 197:    */   {
/* 198:295 */     this.messageConverters = messageConverters;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public List<HttpMessageConverter<?>> getMessageConverters()
/* 202:    */   {
/* 203:302 */     return this.messageConverters;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public void setWebBindingInitializer(WebBindingInitializer webBindingInitializer)
/* 207:    */   {
/* 208:310 */     this.webBindingInitializer = webBindingInitializer;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public WebBindingInitializer getWebBindingInitializer()
/* 212:    */   {
/* 213:317 */     return this.webBindingInitializer;
/* 214:    */   }
/* 215:    */   
/* 216:    */   public void setSessionAttributeStore(SessionAttributeStore sessionAttributeStore)
/* 217:    */   {
/* 218:327 */     this.sessionAttributeStore = sessionAttributeStore;
/* 219:    */   }
/* 220:    */   
/* 221:    */   public void setCacheSecondsForSessionAttributeHandlers(int cacheSecondsForSessionAttributeHandlers)
/* 222:    */   {
/* 223:340 */     this.cacheSecondsForSessionAttributeHandlers = cacheSecondsForSessionAttributeHandlers;
/* 224:    */   }
/* 225:    */   
/* 226:    */   public void setSynchronizeOnSession(boolean synchronizeOnSession)
/* 227:    */   {
/* 228:362 */     this.synchronizeOnSession = synchronizeOnSession;
/* 229:    */   }
/* 230:    */   
/* 231:    */   public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer)
/* 232:    */   {
/* 233:371 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/* 234:    */   }
/* 235:    */   
/* 236:    */   public void setIgnoreDefaultModelOnRedirect(boolean ignoreDefaultModelOnRedirect)
/* 237:    */   {
/* 238:388 */     this.ignoreDefaultModelOnRedirect = ignoreDefaultModelOnRedirect;
/* 239:    */   }
/* 240:    */   
/* 241:    */   public void setBeanFactory(BeanFactory beanFactory)
/* 242:    */   {
/* 243:397 */     if ((beanFactory instanceof ConfigurableBeanFactory)) {
/* 244:398 */       this.beanFactory = ((ConfigurableBeanFactory)beanFactory);
/* 245:    */     }
/* 246:    */   }
/* 247:    */   
/* 248:    */   protected ConfigurableBeanFactory getBeanFactory()
/* 249:    */   {
/* 250:406 */     return this.beanFactory;
/* 251:    */   }
/* 252:    */   
/* 253:    */   public void afterPropertiesSet()
/* 254:    */   {
/* 255:410 */     if (this.argumentResolvers == null)
/* 256:    */     {
/* 257:411 */       List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
/* 258:412 */       this.argumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
/* 259:    */     }
/* 260:414 */     if (this.initBinderArgumentResolvers == null)
/* 261:    */     {
/* 262:415 */       List<HandlerMethodArgumentResolver> resolvers = getDefaultInitBinderArgumentResolvers();
/* 263:416 */       this.initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
/* 264:    */     }
/* 265:418 */     if (this.returnValueHandlers == null)
/* 266:    */     {
/* 267:419 */       List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
/* 268:420 */       this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite().addHandlers(handlers);
/* 269:    */     }
/* 270:    */   }
/* 271:    */   
/* 272:    */   protected List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers()
/* 273:    */   {
/* 274:429 */     List<HandlerMethodArgumentResolver> resolvers = new ArrayList();
/* 275:    */     
/* 276:    */ 
/* 277:432 */     resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), false));
/* 278:433 */     resolvers.add(new RequestParamMapMethodArgumentResolver());
/* 279:434 */     resolvers.add(new PathVariableMethodArgumentResolver());
/* 280:435 */     resolvers.add(new ServletModelAttributeMethodProcessor(false));
/* 281:436 */     resolvers.add(new RequestResponseBodyMethodProcessor(getMessageConverters()));
/* 282:437 */     resolvers.add(new RequestPartMethodArgumentResolver(getMessageConverters()));
/* 283:438 */     resolvers.add(new RequestHeaderMethodArgumentResolver(getBeanFactory()));
/* 284:439 */     resolvers.add(new RequestHeaderMapMethodArgumentResolver());
/* 285:440 */     resolvers.add(new ServletCookieValueMethodArgumentResolver(getBeanFactory()));
/* 286:441 */     resolvers.add(new ExpressionValueMethodArgumentResolver(getBeanFactory()));
/* 287:    */     
/* 288:    */ 
/* 289:444 */     resolvers.add(new ServletRequestMethodArgumentResolver());
/* 290:445 */     resolvers.add(new ServletResponseMethodArgumentResolver());
/* 291:446 */     resolvers.add(new HttpEntityMethodProcessor(getMessageConverters()));
/* 292:447 */     resolvers.add(new RedirectAttributesMethodArgumentResolver());
/* 293:448 */     resolvers.add(new ModelMethodProcessor());
/* 294:449 */     resolvers.add(new MapMethodProcessor());
/* 295:450 */     resolvers.add(new ErrorsMethodArgumentResolver());
/* 296:451 */     resolvers.add(new SessionStatusMethodArgumentResolver());
/* 297:454 */     if (getCustomArgumentResolvers() != null) {
/* 298:455 */       resolvers.addAll(getCustomArgumentResolvers());
/* 299:    */     }
/* 300:459 */     resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), true));
/* 301:460 */     resolvers.add(new ServletModelAttributeMethodProcessor(true));
/* 302:    */     
/* 303:462 */     return resolvers;
/* 304:    */   }
/* 305:    */   
/* 306:    */   protected List<HandlerMethodArgumentResolver> getDefaultInitBinderArgumentResolvers()
/* 307:    */   {
/* 308:470 */     List<HandlerMethodArgumentResolver> resolvers = new ArrayList();
/* 309:    */     
/* 310:    */ 
/* 311:473 */     resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), false));
/* 312:474 */     resolvers.add(new RequestParamMapMethodArgumentResolver());
/* 313:475 */     resolvers.add(new PathVariableMethodArgumentResolver());
/* 314:476 */     resolvers.add(new ExpressionValueMethodArgumentResolver(getBeanFactory()));
/* 315:    */     
/* 316:    */ 
/* 317:479 */     resolvers.add(new ServletRequestMethodArgumentResolver());
/* 318:480 */     resolvers.add(new ServletResponseMethodArgumentResolver());
/* 319:483 */     if (getCustomArgumentResolvers() != null) {
/* 320:484 */       resolvers.addAll(getCustomArgumentResolvers());
/* 321:    */     }
/* 322:488 */     resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), true));
/* 323:    */     
/* 324:490 */     return resolvers;
/* 325:    */   }
/* 326:    */   
/* 327:    */   protected List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers()
/* 328:    */   {
/* 329:498 */     List<HandlerMethodReturnValueHandler> handlers = new ArrayList();
/* 330:    */     
/* 331:    */ 
/* 332:501 */     handlers.add(new ModelAndViewMethodReturnValueHandler());
/* 333:502 */     handlers.add(new ModelMethodProcessor());
/* 334:503 */     handlers.add(new ViewMethodReturnValueHandler());
/* 335:504 */     handlers.add(new HttpEntityMethodProcessor(getMessageConverters()));
/* 336:    */     
/* 337:    */ 
/* 338:507 */     handlers.add(new ModelAttributeMethodProcessor(false));
/* 339:508 */     handlers.add(new RequestResponseBodyMethodProcessor(getMessageConverters()));
/* 340:    */     
/* 341:    */ 
/* 342:511 */     handlers.add(new ViewNameMethodReturnValueHandler());
/* 343:512 */     handlers.add(new MapMethodProcessor());
/* 344:515 */     if (getCustomReturnValueHandlers() != null) {
/* 345:516 */       handlers.addAll(getCustomReturnValueHandlers());
/* 346:    */     }
/* 347:520 */     if (!CollectionUtils.isEmpty(getModelAndViewResolvers())) {
/* 348:521 */       handlers.add(new ModelAndViewResolverMethodReturnValueHandler(getModelAndViewResolvers()));
/* 349:    */     } else {
/* 350:524 */       handlers.add(new ModelAttributeMethodProcessor(true));
/* 351:    */     }
/* 352:527 */     return handlers;
/* 353:    */   }
/* 354:    */   
/* 355:    */   protected boolean supportsInternal(HandlerMethod handlerMethod)
/* 356:    */   {
/* 357:537 */     return (supportsMethodParameters(handlerMethod.getMethodParameters())) && (supportsReturnType(handlerMethod.getReturnType()));
/* 358:    */   }
/* 359:    */   
/* 360:    */   private boolean supportsMethodParameters(MethodParameter[] methodParameters)
/* 361:    */   {
/* 362:541 */     for (MethodParameter methodParameter : methodParameters) {
/* 363:542 */       if (!this.argumentResolvers.supportsParameter(methodParameter)) {
/* 364:543 */         return false;
/* 365:    */       }
/* 366:    */     }
/* 367:546 */     return true;
/* 368:    */   }
/* 369:    */   
/* 370:    */   private boolean supportsReturnType(MethodParameter methodReturnType)
/* 371:    */   {
/* 372:551 */     return (this.returnValueHandlers.supportsReturnType(methodReturnType)) || (Void.TYPE.equals(methodReturnType.getParameterType()));
/* 373:    */   }
/* 374:    */   
/* 375:    */   protected long getLastModifiedInternal(HttpServletRequest request, HandlerMethod handlerMethod)
/* 376:    */   {
/* 377:562 */     return -1L;
/* 378:    */   }
/* 379:    */   
/* 380:    */   protected final ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod)
/* 381:    */     throws Exception
/* 382:    */   {
/* 383:570 */     if (getSessionAttributesHandler(handlerMethod).hasSessionAttributes()) {
/* 384:572 */       checkAndPrepare(request, response, this.cacheSecondsForSessionAttributeHandlers, true);
/* 385:    */     } else {
/* 386:576 */       checkAndPrepare(request, response, true);
/* 387:    */     }
/* 388:580 */     if (this.synchronizeOnSession)
/* 389:    */     {
/* 390:581 */       HttpSession session = request.getSession(false);
/* 391:582 */       if (session != null)
/* 392:    */       {
/* 393:583 */         Object mutex = WebUtils.getSessionMutex(session);
/* 394:584 */         synchronized (mutex)
/* 395:    */         {
/* 396:585 */           return invokeHandlerMethod(request, response, handlerMethod);
/* 397:    */         }
/* 398:    */       }
/* 399:    */     }
/* 400:590 */     return invokeHandlerMethod(request, response, handlerMethod);
/* 401:    */   }
/* 402:    */   
/* 403:    */   private SessionAttributesHandler getSessionAttributesHandler(HandlerMethod handlerMethod)
/* 404:    */   {
/* 405:598 */     Class<?> handlerType = handlerMethod.getBeanType();
/* 406:599 */     SessionAttributesHandler sessionAttrHandler = (SessionAttributesHandler)this.sessionAttributesHandlerCache.get(handlerType);
/* 407:600 */     if (sessionAttrHandler == null) {
/* 408:601 */       synchronized (this.sessionAttributesHandlerCache)
/* 409:    */       {
/* 410:602 */         sessionAttrHandler = (SessionAttributesHandler)this.sessionAttributesHandlerCache.get(handlerType);
/* 411:603 */         if (sessionAttrHandler == null)
/* 412:    */         {
/* 413:604 */           sessionAttrHandler = new SessionAttributesHandler(handlerType, this.sessionAttributeStore);
/* 414:605 */           this.sessionAttributesHandlerCache.put(handlerType, sessionAttrHandler);
/* 415:    */         }
/* 416:    */       }
/* 417:    */     }
/* 418:609 */     return sessionAttrHandler;
/* 419:    */   }
/* 420:    */   
/* 421:    */   private ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod)
/* 422:    */     throws Exception
/* 423:    */   {
/* 424:618 */     ServletWebRequest webRequest = new ServletWebRequest(request, response);
/* 425:    */     
/* 426:620 */     WebDataBinderFactory binderFactory = getDataBinderFactory(handlerMethod);
/* 427:621 */     ModelFactory modelFactory = getModelFactory(handlerMethod, binderFactory);
/* 428:622 */     ServletInvocableHandlerMethod requestMappingMethod = createRequestMappingMethod(handlerMethod, binderFactory);
/* 429:    */     
/* 430:624 */     ModelAndViewContainer mavContainer = new ModelAndViewContainer();
/* 431:625 */     mavContainer.addAllAttributes(RequestContextUtils.getInputFlashMap(request));
/* 432:626 */     modelFactory.initModel(webRequest, mavContainer, requestMappingMethod);
/* 433:627 */     mavContainer.setIgnoreDefaultModelOnRedirect(this.ignoreDefaultModelOnRedirect);
/* 434:    */     
/* 435:629 */     requestMappingMethod.invokeAndHandle(webRequest, mavContainer, new Object[0]);
/* 436:630 */     modelFactory.updateModel(webRequest, mavContainer);
/* 437:632 */     if (mavContainer.isRequestHandled()) {
/* 438:633 */       return null;
/* 439:    */     }
/* 440:636 */     ModelMap model = mavContainer.getModel();
/* 441:637 */     ModelAndView mav = new ModelAndView(mavContainer.getViewName(), model);
/* 442:638 */     if (!mavContainer.isViewReference()) {
/* 443:639 */       mav.setView((View)mavContainer.getView());
/* 444:    */     }
/* 445:641 */     if ((model instanceof RedirectAttributes))
/* 446:    */     {
/* 447:642 */       Map<String, ?> flashAttributes = ((RedirectAttributes)model).getFlashAttributes();
/* 448:643 */       RequestContextUtils.getOutputFlashMap(request).putAll(flashAttributes);
/* 449:    */     }
/* 450:645 */     return mav;
/* 451:    */   }
/* 452:    */   
/* 453:    */   private ServletInvocableHandlerMethod createRequestMappingMethod(HandlerMethod handlerMethod, WebDataBinderFactory binderFactory)
/* 454:    */   {
/* 455:652 */     ServletInvocableHandlerMethod requestMethod = new ServletInvocableHandlerMethod(handlerMethod.getBean(), handlerMethod.getMethod());
/* 456:653 */     requestMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
/* 457:654 */     requestMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
/* 458:655 */     requestMethod.setDataBinderFactory(binderFactory);
/* 459:656 */     requestMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
/* 460:657 */     return requestMethod;
/* 461:    */   }
/* 462:    */   
/* 463:    */   private ModelFactory getModelFactory(HandlerMethod handlerMethod, WebDataBinderFactory binderFactory)
/* 464:    */   {
/* 465:661 */     SessionAttributesHandler sessionAttrHandler = getSessionAttributesHandler(handlerMethod);
/* 466:662 */     Class<?> handlerType = handlerMethod.getBeanType();
/* 467:663 */     ModelFactory modelFactory = (ModelFactory)this.modelFactoryCache.get(handlerType);
/* 468:664 */     if (modelFactory == null)
/* 469:    */     {
/* 470:665 */       List<InvocableHandlerMethod> attrMethods = new ArrayList();
/* 471:666 */       for (Method method : HandlerMethodSelector.selectMethods(handlerType, MODEL_ATTRIBUTE_METHODS))
/* 472:    */       {
/* 473:667 */         InvocableHandlerMethod attrMethod = new InvocableHandlerMethod(handlerMethod.getBean(), method);
/* 474:668 */         attrMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
/* 475:669 */         attrMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
/* 476:670 */         attrMethod.setDataBinderFactory(binderFactory);
/* 477:671 */         attrMethods.add(attrMethod);
/* 478:    */       }
/* 479:673 */       modelFactory = new ModelFactory(attrMethods, binderFactory, sessionAttrHandler);
/* 480:674 */       this.modelFactoryCache.put(handlerType, modelFactory);
/* 481:    */     }
/* 482:676 */     return modelFactory;
/* 483:    */   }
/* 484:    */   
/* 485:    */   private WebDataBinderFactory getDataBinderFactory(HandlerMethod handlerMethod)
/* 486:    */     throws Exception
/* 487:    */   {
/* 488:680 */     Class<?> handlerType = handlerMethod.getBeanType();
/* 489:681 */     WebDataBinderFactory binderFactory = (WebDataBinderFactory)this.dataBinderFactoryCache.get(handlerType);
/* 490:682 */     if (binderFactory == null)
/* 491:    */     {
/* 492:683 */       List<InvocableHandlerMethod> binderMethods = new ArrayList();
/* 493:684 */       for (Method method : HandlerMethodSelector.selectMethods(handlerType, INIT_BINDER_METHODS))
/* 494:    */       {
/* 495:685 */         InvocableHandlerMethod binderMethod = new InvocableHandlerMethod(handlerMethod.getBean(), method);
/* 496:686 */         binderMethod.setHandlerMethodArgumentResolvers(this.initBinderArgumentResolvers);
/* 497:687 */         binderMethod.setDataBinderFactory(new DefaultDataBinderFactory(this.webBindingInitializer));
/* 498:688 */         binderMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
/* 499:689 */         binderMethods.add(binderMethod);
/* 500:    */       }
/* 501:691 */       binderFactory = createDataBinderFactory(binderMethods);
/* 502:692 */       this.dataBinderFactoryCache.put(handlerType, binderFactory);
/* 503:    */     }
/* 504:694 */     return binderFactory;
/* 505:    */   }
/* 506:    */   
/* 507:    */   protected ServletRequestDataBinderFactory createDataBinderFactory(List<InvocableHandlerMethod> binderMethods)
/* 508:    */     throws Exception
/* 509:    */   {
/* 510:707 */     return new ServletRequestDataBinderFactory(binderMethods, getWebBindingInitializer());
/* 511:    */   }
/* 512:    */   
/* 513:713 */   public static final ReflectionUtils.MethodFilter INIT_BINDER_METHODS = new ReflectionUtils.MethodFilter()
/* 514:    */   {
/* 515:    */     public boolean matches(Method method)
/* 516:    */     {
/* 517:716 */       return AnnotationUtils.findAnnotation(method, InitBinder.class) != null;
/* 518:    */     }
/* 519:    */   };
/* 520:723 */   public static final ReflectionUtils.MethodFilter MODEL_ATTRIBUTE_METHODS = new ReflectionUtils.MethodFilter()
/* 521:    */   {
/* 522:    */     public boolean matches(Method method)
/* 523:    */     {
/* 524:727 */       return (AnnotationUtils.findAnnotation(method, RequestMapping.class) == null) && (AnnotationUtils.findAnnotation(method, ModelAttribute.class) != null);
/* 525:    */     }
/* 526:    */   };
/* 527:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
 * JD-Core Version:    0.7.0.1
 */