/*   1:    */ package org.springframework.web.servlet.mvc.multiaction;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationTargetException;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Map;
/*   9:    */ import javax.servlet.http.HttpServletRequest;
/*  10:    */ import javax.servlet.http.HttpServletResponse;
/*  11:    */ import javax.servlet.http.HttpSession;
/*  12:    */ import org.apache.commons.logging.Log;
/*  13:    */ import org.apache.commons.logging.LogFactory;
/*  14:    */ import org.springframework.beans.BeanUtils;
/*  15:    */ import org.springframework.util.Assert;
/*  16:    */ import org.springframework.util.ReflectionUtils;
/*  17:    */ import org.springframework.validation.ValidationUtils;
/*  18:    */ import org.springframework.validation.Validator;
/*  19:    */ import org.springframework.web.HttpSessionRequiredException;
/*  20:    */ import org.springframework.web.bind.ServletRequestDataBinder;
/*  21:    */ import org.springframework.web.bind.support.WebBindingInitializer;
/*  22:    */ import org.springframework.web.context.request.ServletWebRequest;
/*  23:    */ import org.springframework.web.servlet.ModelAndView;
/*  24:    */ import org.springframework.web.servlet.mvc.AbstractController;
/*  25:    */ import org.springframework.web.servlet.mvc.LastModified;
/*  26:    */ 
/*  27:    */ public class MultiActionController
/*  28:    */   extends AbstractController
/*  29:    */   implements LastModified
/*  30:    */ {
/*  31:    */   public static final String LAST_MODIFIED_METHOD_SUFFIX = "LastModified";
/*  32:    */   public static final String DEFAULT_COMMAND_NAME = "command";
/*  33:    */   public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";
/*  34:150 */   protected static final Log pageNotFoundLogger = LogFactory.getLog("org.springframework.web.servlet.PageNotFound");
/*  35:    */   private Object delegate;
/*  36:156 */   private MethodNameResolver methodNameResolver = new InternalPathMethodNameResolver();
/*  37:    */   private Validator[] validators;
/*  38:    */   private WebBindingInitializer webBindingInitializer;
/*  39:165 */   private final Map<String, Method> handlerMethodMap = new HashMap();
/*  40:168 */   private final Map<String, Method> lastModifiedMethodMap = new HashMap();
/*  41:171 */   private final Map<Class, Method> exceptionHandlerMap = new HashMap();
/*  42:    */   
/*  43:    */   public MultiActionController()
/*  44:    */   {
/*  45:179 */     this.delegate = this;
/*  46:180 */     registerHandlerMethods(this.delegate);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public MultiActionController(Object delegate)
/*  50:    */   {
/*  51:191 */     setDelegate(delegate);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public final void setDelegate(Object delegate)
/*  55:    */   {
/*  56:203 */     Assert.notNull(delegate, "Delegate must not be null");
/*  57:204 */     this.delegate = delegate;
/*  58:205 */     registerHandlerMethods(this.delegate);
/*  59:207 */     if (this.handlerMethodMap.isEmpty()) {
/*  60:208 */       throw new IllegalStateException("No handler methods in class [" + this.delegate.getClass() + "]");
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   public final void setMethodNameResolver(MethodNameResolver methodNameResolver)
/*  65:    */   {
/*  66:217 */     this.methodNameResolver = methodNameResolver;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public final MethodNameResolver getMethodNameResolver()
/*  70:    */   {
/*  71:224 */     return this.methodNameResolver;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public final void setValidators(Validator[] validators)
/*  75:    */   {
/*  76:232 */     this.validators = validators;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public final Validator[] getValidators()
/*  80:    */   {
/*  81:239 */     return this.validators;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public final void setWebBindingInitializer(WebBindingInitializer webBindingInitializer)
/*  85:    */   {
/*  86:249 */     this.webBindingInitializer = webBindingInitializer;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public final WebBindingInitializer getWebBindingInitializer()
/*  90:    */   {
/*  91:257 */     return this.webBindingInitializer;
/*  92:    */   }
/*  93:    */   
/*  94:    */   private void registerHandlerMethods(Object delegate)
/*  95:    */   {
/*  96:265 */     this.handlerMethodMap.clear();
/*  97:266 */     this.lastModifiedMethodMap.clear();
/*  98:267 */     this.exceptionHandlerMap.clear();
/*  99:    */     
/* 100:    */ 
/* 101:    */ 
/* 102:271 */     Method[] methods = delegate.getClass().getMethods();
/* 103:272 */     for (Method method : methods) {
/* 104:274 */       if (isExceptionHandlerMethod(method))
/* 105:    */       {
/* 106:275 */         registerExceptionHandlerMethod(method);
/* 107:    */       }
/* 108:277 */       else if (isHandlerMethod(method))
/* 109:    */       {
/* 110:278 */         registerHandlerMethod(method);
/* 111:279 */         registerLastModifiedMethodIfExists(delegate, method);
/* 112:    */       }
/* 113:    */     }
/* 114:    */   }
/* 115:    */   
/* 116:    */   private boolean isHandlerMethod(Method method)
/* 117:    */   {
/* 118:290 */     Class returnType = method.getReturnType();
/* 119:291 */     if ((ModelAndView.class.equals(returnType)) || (Map.class.equals(returnType)) || (String.class.equals(returnType)) || 
/* 120:292 */       (Void.TYPE.equals(returnType)))
/* 121:    */     {
/* 122:293 */       Class[] parameterTypes = method.getParameterTypes();
/* 123:    */       
/* 124:    */ 
/* 125:    */ 
/* 126:297 */       return (parameterTypes.length >= 2) && (HttpServletRequest.class.equals(parameterTypes[0])) && (HttpServletResponse.class.equals(parameterTypes[1])) && ((!"handleRequest".equals(method.getName())) || (parameterTypes.length != 2));
/* 127:    */     }
/* 128:299 */     return false;
/* 129:    */   }
/* 130:    */   
/* 131:    */   private boolean isExceptionHandlerMethod(Method method)
/* 132:    */   {
/* 133:308 */     return (isHandlerMethod(method)) && (method.getParameterTypes().length == 3) && (Throwable.class.isAssignableFrom(method.getParameterTypes()[2]));
/* 134:    */   }
/* 135:    */   
/* 136:    */   private void registerHandlerMethod(Method method)
/* 137:    */   {
/* 138:315 */     if (this.logger.isDebugEnabled()) {
/* 139:316 */       this.logger.debug("Found action method [" + method + "]");
/* 140:    */     }
/* 141:318 */     this.handlerMethodMap.put(method.getName(), method);
/* 142:    */   }
/* 143:    */   
/* 144:    */   private void registerLastModifiedMethodIfExists(Object delegate, Method method)
/* 145:    */   {
/* 146:    */     try
/* 147:    */     {
/* 148:328 */       Method lastModifiedMethod = delegate.getClass().getMethod(
/* 149:329 */         method.getName() + "LastModified", 
/* 150:330 */         new Class[] { HttpServletRequest.class });
/* 151:331 */       Class returnType = lastModifiedMethod.getReturnType();
/* 152:332 */       if ((!Long.TYPE.equals(returnType)) && (!Long.class.equals(returnType))) {
/* 153:333 */         throw new IllegalStateException("last-modified method [" + lastModifiedMethod + 
/* 154:334 */           "] declares an invalid return type - needs to be 'long' or 'Long'");
/* 155:    */       }
/* 156:337 */       this.lastModifiedMethodMap.put(method.getName(), lastModifiedMethod);
/* 157:338 */       if (this.logger.isDebugEnabled()) {
/* 158:339 */         this.logger.debug("Found last-modified method for handler method [" + method + "]");
/* 159:    */       }
/* 160:    */     }
/* 161:    */     catch (NoSuchMethodException localNoSuchMethodException) {}
/* 162:    */   }
/* 163:    */   
/* 164:    */   private void registerExceptionHandlerMethod(Method method)
/* 165:    */   {
/* 166:351 */     this.exceptionHandlerMap.put(method.getParameterTypes()[2], method);
/* 167:352 */     if (this.logger.isDebugEnabled()) {
/* 168:353 */       this.logger.debug("Found exception handler method [" + method + "]");
/* 169:    */     }
/* 170:    */   }
/* 171:    */   
/* 172:    */   public long getLastModified(HttpServletRequest request)
/* 173:    */   {
/* 174:    */     try
/* 175:    */     {
/* 176:369 */       String handlerMethodName = this.methodNameResolver.getHandlerMethodName(request);
/* 177:370 */       Method lastModifiedMethod = (Method)this.lastModifiedMethodMap.get(handlerMethodName);
/* 178:371 */       if (lastModifiedMethod != null) {
/* 179:    */         try
/* 180:    */         {
/* 181:374 */           Long wrappedLong = (Long)lastModifiedMethod.invoke(this.delegate, new Object[] { request });
/* 182:375 */           return wrappedLong != null ? wrappedLong.longValue() : -1L;
/* 183:    */         }
/* 184:    */         catch (Exception ex)
/* 185:    */         {
/* 186:380 */           this.logger.error("Failed to invoke last-modified method", ex);
/* 187:    */         }
/* 188:    */       }
/* 189:389 */       return -1L;
/* 190:    */     }
/* 191:    */     catch (NoSuchRequestHandlingMethodException localNoSuchRequestHandlingMethodException) {}
/* 192:    */   }
/* 193:    */   
/* 194:    */   protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
/* 195:    */     throws Exception
/* 196:    */   {
/* 197:    */     try
/* 198:    */     {
/* 199:407 */       String methodName = this.methodNameResolver.getHandlerMethodName(request);
/* 200:408 */       return invokeNamedMethod(methodName, request, response);
/* 201:    */     }
/* 202:    */     catch (NoSuchRequestHandlingMethodException ex)
/* 203:    */     {
/* 204:411 */       return handleNoSuchRequestHandlingMethod(ex, request, response);
/* 205:    */     }
/* 206:    */   }
/* 207:    */   
/* 208:    */   protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response)
/* 209:    */     throws Exception
/* 210:    */   {
/* 211:430 */     pageNotFoundLogger.warn(ex.getMessage());
/* 212:431 */     response.sendError(404);
/* 213:432 */     return null;
/* 214:    */   }
/* 215:    */   
/* 216:    */   protected final ModelAndView invokeNamedMethod(String methodName, HttpServletRequest request, HttpServletResponse response)
/* 217:    */     throws Exception
/* 218:    */   {
/* 219:443 */     Method method = (Method)this.handlerMethodMap.get(methodName);
/* 220:444 */     if (method == null) {
/* 221:445 */       throw new NoSuchRequestHandlingMethodException(methodName, getClass());
/* 222:    */     }
/* 223:    */     try
/* 224:    */     {
/* 225:449 */       Class[] paramTypes = method.getParameterTypes();
/* 226:450 */       List<Object> params = new ArrayList(4);
/* 227:451 */       params.add(request);
/* 228:452 */       params.add(response);
/* 229:454 */       if ((paramTypes.length >= 3) && (paramTypes[2].equals(HttpSession.class)))
/* 230:    */       {
/* 231:455 */         HttpSession session = request.getSession(false);
/* 232:456 */         if (session == null) {
/* 233:457 */           throw new HttpSessionRequiredException(
/* 234:458 */             "Pre-existing session required for handler method '" + methodName + "'");
/* 235:    */         }
/* 236:460 */         params.add(session);
/* 237:    */       }
/* 238:464 */       if ((paramTypes.length >= 3) && 
/* 239:465 */         (!paramTypes[(paramTypes.length - 1)].equals(HttpSession.class)))
/* 240:    */       {
/* 241:466 */         Object command = newCommandObject(paramTypes[(paramTypes.length - 1)]);
/* 242:467 */         params.add(command);
/* 243:468 */         bind(request, command);
/* 244:    */       }
/* 245:471 */       Object returnValue = method.invoke(this.delegate, params.toArray(new Object[params.size()]));
/* 246:472 */       return massageReturnValueIfNecessary(returnValue);
/* 247:    */     }
/* 248:    */     catch (InvocationTargetException ex)
/* 249:    */     {
/* 250:476 */       return handleException(request, response, ex.getTargetException());
/* 251:    */     }
/* 252:    */     catch (Exception ex)
/* 253:    */     {
/* 254:480 */       return handleException(request, response, ex);
/* 255:    */     }
/* 256:    */   }
/* 257:    */   
/* 258:    */   private ModelAndView massageReturnValueIfNecessary(Object returnValue)
/* 259:    */   {
/* 260:491 */     if ((returnValue instanceof ModelAndView)) {
/* 261:492 */       return (ModelAndView)returnValue;
/* 262:    */     }
/* 263:494 */     if ((returnValue instanceof Map)) {
/* 264:495 */       return new ModelAndView().addAllObjects((Map)returnValue);
/* 265:    */     }
/* 266:497 */     if ((returnValue instanceof String)) {
/* 267:498 */       return new ModelAndView((String)returnValue);
/* 268:    */     }
/* 269:503 */     return null;
/* 270:    */   }
/* 271:    */   
/* 272:    */   protected Object newCommandObject(Class clazz)
/* 273:    */     throws Exception
/* 274:    */   {
/* 275:517 */     if (this.logger.isDebugEnabled()) {
/* 276:518 */       this.logger.debug("Creating new command of class [" + clazz.getName() + "]");
/* 277:    */     }
/* 278:520 */     return BeanUtils.instantiateClass(clazz);
/* 279:    */   }
/* 280:    */   
/* 281:    */   protected void bind(HttpServletRequest request, Object command)
/* 282:    */     throws Exception
/* 283:    */   {
/* 284:530 */     this.logger.debug("Binding request parameters onto MultiActionController command");
/* 285:531 */     ServletRequestDataBinder binder = createBinder(request, command);
/* 286:532 */     binder.bind(request);
/* 287:533 */     if (this.validators != null) {
/* 288:534 */       for (Validator validator : this.validators) {
/* 289:535 */         if (validator.supports(command.getClass())) {
/* 290:536 */           ValidationUtils.invokeValidator(validator, command, binder.getBindingResult());
/* 291:    */         }
/* 292:    */       }
/* 293:    */     }
/* 294:540 */     binder.closeNoCatch();
/* 295:    */   }
/* 296:    */   
/* 297:    */   protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object command)
/* 298:    */     throws Exception
/* 299:    */   {
/* 300:558 */     ServletRequestDataBinder binder = new ServletRequestDataBinder(command, getCommandName(command));
/* 301:559 */     initBinder(request, binder);
/* 302:560 */     return binder;
/* 303:    */   }
/* 304:    */   
/* 305:    */   protected String getCommandName(Object command)
/* 306:    */   {
/* 307:571 */     return "command";
/* 308:    */   }
/* 309:    */   
/* 310:    */   protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
/* 311:    */     throws Exception
/* 312:    */   {
/* 313:592 */     if (this.webBindingInitializer != null) {
/* 314:593 */       this.webBindingInitializer.initBinder(binder, new ServletWebRequest(request));
/* 315:    */     }
/* 316:    */   }
/* 317:    */   
/* 318:    */   protected Method getExceptionHandler(Throwable exception)
/* 319:    */   {
/* 320:605 */     Class exceptionClass = exception.getClass();
/* 321:606 */     if (this.logger.isDebugEnabled()) {
/* 322:607 */       this.logger.debug("Trying to find handler for exception class [" + exceptionClass.getName() + "]");
/* 323:    */     }
/* 324:609 */     Method handler = (Method)this.exceptionHandlerMap.get(exceptionClass);
/* 325:610 */     while ((handler == null) && (!exceptionClass.equals(Throwable.class)))
/* 326:    */     {
/* 327:611 */       if (this.logger.isDebugEnabled()) {
/* 328:612 */         this.logger.debug("Trying to find handler for exception superclass [" + exceptionClass.getName() + "]");
/* 329:    */       }
/* 330:614 */       exceptionClass = exceptionClass.getSuperclass();
/* 331:615 */       handler = (Method)this.exceptionHandlerMap.get(exceptionClass);
/* 332:    */     }
/* 333:617 */     return handler;
/* 334:    */   }
/* 335:    */   
/* 336:    */   private ModelAndView handleException(HttpServletRequest request, HttpServletResponse response, Throwable ex)
/* 337:    */     throws Exception
/* 338:    */   {
/* 339:631 */     Method handler = getExceptionHandler(ex);
/* 340:632 */     if (handler != null)
/* 341:    */     {
/* 342:633 */       if (this.logger.isDebugEnabled()) {
/* 343:634 */         this.logger.debug("Invoking exception handler [" + handler + "] for exception: " + ex);
/* 344:    */       }
/* 345:    */       try
/* 346:    */       {
/* 347:637 */         Object returnValue = handler.invoke(this.delegate, new Object[] { request, response, ex });
/* 348:638 */         return massageReturnValueIfNecessary(returnValue);
/* 349:    */       }
/* 350:    */       catch (InvocationTargetException ex2)
/* 351:    */       {
/* 352:641 */         this.logger.error("Original exception overridden by exception handling failure", ex);
/* 353:642 */         ReflectionUtils.rethrowException(ex2.getTargetException());
/* 354:    */       }
/* 355:    */       catch (Exception ex2)
/* 356:    */       {
/* 357:645 */         this.logger.error("Failed to invoke exception handler method", ex2);
/* 358:    */       }
/* 359:    */     }
/* 360:    */     else
/* 361:    */     {
/* 362:650 */       ReflectionUtils.rethrowException(ex);
/* 363:    */     }
/* 364:652 */     throw new IllegalStateException("Should never get here");
/* 365:    */   }
/* 366:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.multiaction.MultiActionController
 * JD-Core Version:    0.7.0.1
 */