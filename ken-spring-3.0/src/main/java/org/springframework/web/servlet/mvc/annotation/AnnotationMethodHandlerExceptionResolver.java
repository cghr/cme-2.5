/*   1:    */ package org.springframework.web.servlet.mvc.annotation;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.io.Reader;
/*   7:    */ import java.io.Writer;
/*   8:    */ import java.lang.reflect.InvocationTargetException;
/*   9:    */ import java.lang.reflect.Method;
/*  10:    */ import java.security.Principal;
/*  11:    */ import java.util.ArrayList;
/*  12:    */ import java.util.Arrays;
/*  13:    */ import java.util.Collection;
/*  14:    */ import java.util.Collections;
/*  15:    */ import java.util.Iterator;
/*  16:    */ import java.util.List;
/*  17:    */ import java.util.Locale;
/*  18:    */ import java.util.Map;
/*  19:    */ import java.util.concurrent.ConcurrentHashMap;
/*  20:    */ import javax.servlet.ServletException;
/*  21:    */ import javax.servlet.ServletRequest;
/*  22:    */ import javax.servlet.ServletResponse;
/*  23:    */ import javax.servlet.http.HttpServletRequest;
/*  24:    */ import javax.servlet.http.HttpServletResponse;
/*  25:    */ import javax.servlet.http.HttpSession;
/*  26:    */ import org.apache.commons.logging.Log;
/*  27:    */ import org.springframework.core.ExceptionDepthComparator;
/*  28:    */ import org.springframework.core.GenericTypeResolver;
/*  29:    */ import org.springframework.core.MethodParameter;
/*  30:    */ import org.springframework.core.annotation.AnnotationUtils;
/*  31:    */ import org.springframework.http.HttpHeaders;
/*  32:    */ import org.springframework.http.HttpInputMessage;
/*  33:    */ import org.springframework.http.HttpOutputMessage;
/*  34:    */ import org.springframework.http.HttpStatus;
/*  35:    */ import org.springframework.http.MediaType;
/*  36:    */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*  37:    */ import org.springframework.http.converter.HttpMessageConverter;
/*  38:    */ import org.springframework.http.converter.StringHttpMessageConverter;
/*  39:    */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*  40:    */ import org.springframework.http.converter.xml.XmlAwareFormHttpMessageConverter;
/*  41:    */ import org.springframework.http.server.ServletServerHttpRequest;
/*  42:    */ import org.springframework.http.server.ServletServerHttpResponse;
/*  43:    */ import org.springframework.ui.Model;
/*  44:    */ import org.springframework.util.ClassUtils;
/*  45:    */ import org.springframework.util.ObjectUtils;
/*  46:    */ import org.springframework.util.ReflectionUtils;
/*  47:    */ import org.springframework.util.ReflectionUtils.MethodCallback;
/*  48:    */ import org.springframework.util.StringUtils;
/*  49:    */ import org.springframework.web.bind.annotation.ExceptionHandler;
/*  50:    */ import org.springframework.web.bind.annotation.ResponseBody;
/*  51:    */ import org.springframework.web.bind.annotation.ResponseStatus;
/*  52:    */ import org.springframework.web.bind.support.WebArgumentResolver;
/*  53:    */ import org.springframework.web.context.request.NativeWebRequest;
/*  54:    */ import org.springframework.web.context.request.ServletWebRequest;
/*  55:    */ import org.springframework.web.context.request.WebRequest;
/*  56:    */ import org.springframework.web.servlet.ModelAndView;
/*  57:    */ import org.springframework.web.servlet.View;
/*  58:    */ import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
/*  59:    */ import org.springframework.web.servlet.support.RequestContextUtils;
/*  60:    */ 
/*  61:    */ public class AnnotationMethodHandlerExceptionResolver
/*  62:    */   extends AbstractHandlerExceptionResolver
/*  63:    */ {
/*  64: 87 */   private static final Method NO_METHOD_FOUND = ClassUtils.getMethodIfAvailable(System.class, "currentTimeMillis", null);
/*  65: 90 */   private final Map<Class<?>, Map<Class<? extends Throwable>, Method>> exceptionHandlerCache = new ConcurrentHashMap();
/*  66:    */   private WebArgumentResolver[] customArgumentResolvers;
/*  67: 95 */   private HttpMessageConverter<?>[] messageConverters = { new ByteArrayHttpMessageConverter(), new StringHttpMessageConverter(), 
/*  68: 96 */     new SourceHttpMessageConverter(), new XmlAwareFormHttpMessageConverter() };
/*  69:    */   
/*  70:    */   public void setCustomArgumentResolver(WebArgumentResolver argumentResolver)
/*  71:    */   {
/*  72:105 */     this.customArgumentResolvers = new WebArgumentResolver[] { argumentResolver };
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void setCustomArgumentResolvers(WebArgumentResolver[] argumentResolvers)
/*  76:    */   {
/*  77:114 */     this.customArgumentResolvers = argumentResolvers;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void setMessageConverters(HttpMessageConverter<?>[] messageConverters)
/*  81:    */   {
/*  82:122 */     this.messageConverters = messageConverters;
/*  83:    */   }
/*  84:    */   
/*  85:    */   protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*  86:    */   {
/*  87:130 */     if (handler != null)
/*  88:    */     {
/*  89:131 */       Method handlerMethod = findBestExceptionHandlerMethod(handler, ex);
/*  90:132 */       if (handlerMethod != null)
/*  91:    */       {
/*  92:133 */         ServletWebRequest webRequest = new ServletWebRequest(request, response);
/*  93:    */         try
/*  94:    */         {
/*  95:135 */           Object[] args = resolveHandlerArguments(handlerMethod, handler, webRequest, ex);
/*  96:136 */           if (this.logger.isDebugEnabled()) {
/*  97:137 */             this.logger.debug("Invoking request handler method: " + handlerMethod);
/*  98:    */           }
/*  99:139 */           Object retVal = doInvokeMethod(handlerMethod, handler, args);
/* 100:140 */           return getModelAndView(handlerMethod, retVal, webRequest);
/* 101:    */         }
/* 102:    */         catch (Exception invocationEx)
/* 103:    */         {
/* 104:143 */           this.logger.error("Invoking request method resulted in exception : " + handlerMethod, invocationEx);
/* 105:    */         }
/* 106:    */       }
/* 107:    */     }
/* 108:147 */     return null;
/* 109:    */   }
/* 110:    */   
/* 111:    */   private Method findBestExceptionHandlerMethod(Object handler, Exception thrownException)
/* 112:    */   {
/* 113:157 */     final Class<?> handlerType = ClassUtils.getUserClass(handler);
/* 114:158 */     final Class<? extends Throwable> thrownExceptionType = thrownException.getClass();
/* 115:159 */     Method handlerMethod = null;
/* 116:    */     
/* 117:161 */     Map<Class<? extends Throwable>, Method> handlers = (Map)this.exceptionHandlerCache.get(handlerType);
/* 118:163 */     if (handlers != null)
/* 119:    */     {
/* 120:164 */       handlerMethod = (Method)handlers.get(thrownExceptionType);
/* 121:165 */       if (handlerMethod != null) {
/* 122:166 */         return handlerMethod == NO_METHOD_FOUND ? null : handlerMethod;
/* 123:    */       }
/* 124:    */     }
/* 125:    */     else
/* 126:    */     {
/* 127:170 */       handlers = new ConcurrentHashMap();
/* 128:171 */       this.exceptionHandlerCache.put(handlerType, handlers);
/* 129:    */     }
/* 130:174 */     final Map<Class<? extends Throwable>, Method> resolverMethods = handlers;
/* 131:    */     
/* 132:176 */     ReflectionUtils.doWithMethods(handlerType, new ReflectionUtils.MethodCallback()
/* 133:    */     {
/* 134:    */       public void doWith(Method method)
/* 135:    */       {
/* 136:178 */         method = ClassUtils.getMostSpecificMethod(method, handlerType);
/* 137:179 */         List<Class<? extends Throwable>> handledExceptions = AnnotationMethodHandlerExceptionResolver.this.getHandledExceptions(method);
/* 138:180 */         for (Class<? extends Throwable> handledException : handledExceptions) {
/* 139:181 */           if (handledException.isAssignableFrom(thrownExceptionType)) {
/* 140:182 */             if (!resolverMethods.containsKey(handledException))
/* 141:    */             {
/* 142:183 */               resolverMethods.put(handledException, method);
/* 143:    */             }
/* 144:    */             else
/* 145:    */             {
/* 146:186 */               Method oldMappedMethod = (Method)resolverMethods.get(handledException);
/* 147:187 */               if (!oldMappedMethod.equals(method)) {
/* 148:188 */                 throw new IllegalStateException(
/* 149:189 */                   "Ambiguous exception handler mapped for " + handledException + "]: {" + 
/* 150:190 */                   oldMappedMethod + ", " + method + "}.");
/* 151:    */               }
/* 152:    */             }
/* 153:    */           }
/* 154:    */         }
/* 155:    */       }
/* 156:197 */     });
/* 157:198 */     handlerMethod = getBestMatchingMethod(resolverMethods, thrownException);
/* 158:199 */     handlers.put(thrownExceptionType, handlerMethod == null ? NO_METHOD_FOUND : handlerMethod);
/* 159:200 */     return handlerMethod;
/* 160:    */   }
/* 161:    */   
/* 162:    */   protected List<Class<? extends Throwable>> getHandledExceptions(Method method)
/* 163:    */   {
/* 164:213 */     List<Class<? extends Throwable>> result = new ArrayList();
/* 165:214 */     ExceptionHandler exceptionHandler = (ExceptionHandler)AnnotationUtils.findAnnotation(method, ExceptionHandler.class);
/* 166:215 */     if (exceptionHandler != null) {
/* 167:216 */       if (!ObjectUtils.isEmpty(exceptionHandler.value())) {
/* 168:217 */         result.addAll((Collection)Arrays.asList(exceptionHandler.value()));
/* 169:    */       } else {
/* 170:220 */         for (Class<?> param : method.getParameterTypes()) {
/* 171:221 */           if (Throwable.class.isAssignableFrom(param)) {
/* 172:222 */             result.add(param);
/* 173:    */           }
/* 174:    */         }
/* 175:    */       }
/* 176:    */     }
/* 177:227 */     return result;
/* 178:    */   }
/* 179:    */   
/* 180:    */   private Method getBestMatchingMethod(Map<Class<? extends Throwable>, Method> resolverMethods, Exception thrownException)
/* 181:    */   {
/* 182:236 */     if (!resolverMethods.isEmpty())
/* 183:    */     {
/* 184:237 */       Class<? extends Throwable> closestMatch = 
/* 185:238 */         ExceptionDepthComparator.findClosestMatch((Collection)resolverMethods.keySet(), thrownException);
/* 186:239 */       return (Method)resolverMethods.get(closestMatch);
/* 187:    */     }
/* 188:242 */     return null;
/* 189:    */   }
/* 190:    */   
/* 191:    */   private Object[] resolveHandlerArguments(Method handlerMethod, Object handler, NativeWebRequest webRequest, Exception thrownException)
/* 192:    */     throws Exception
/* 193:    */   {
/* 194:252 */     Class[] paramTypes = handlerMethod.getParameterTypes();
/* 195:253 */     Object[] args = new Object[paramTypes.length];
/* 196:254 */     Class<?> handlerType = handler.getClass();
/* 197:255 */     for (int i = 0; i < args.length; i++)
/* 198:    */     {
/* 199:256 */       MethodParameter methodParam = new MethodParameter(handlerMethod, i);
/* 200:257 */       GenericTypeResolver.resolveParameterType(methodParam, handlerType);
/* 201:258 */       Class paramType = methodParam.getParameterType();
/* 202:259 */       Object argValue = resolveCommonArgument(methodParam, webRequest, thrownException);
/* 203:260 */       if (argValue != WebArgumentResolver.UNRESOLVED) {
/* 204:261 */         args[i] = argValue;
/* 205:    */       } else {
/* 206:264 */         throw new IllegalStateException("Unsupported argument [" + paramType.getName() + 
/* 207:265 */           "] for @ExceptionHandler method: " + handlerMethod);
/* 208:    */       }
/* 209:    */     }
/* 210:268 */     return args;
/* 211:    */   }
/* 212:    */   
/* 213:    */   protected Object resolveCommonArgument(MethodParameter methodParameter, NativeWebRequest webRequest, Exception thrownException)
/* 214:    */     throws Exception
/* 215:    */   {
/* 216:283 */     if (this.customArgumentResolvers != null) {
/* 217:284 */       for (WebArgumentResolver argumentResolver : this.customArgumentResolvers)
/* 218:    */       {
/* 219:285 */         Object value = argumentResolver.resolveArgument(methodParameter, webRequest);
/* 220:286 */         if (value != WebArgumentResolver.UNRESOLVED) {
/* 221:287 */           return value;
/* 222:    */         }
/* 223:    */       }
/* 224:    */     }
/* 225:293 */     Class paramType = methodParameter.getParameterType();
/* 226:294 */     Object value = resolveStandardArgument(paramType, webRequest, thrownException);
/* 227:295 */     if ((value != WebArgumentResolver.UNRESOLVED) && (!ClassUtils.isAssignableValue(paramType, value))) {
/* 228:296 */       throw new IllegalStateException(
/* 229:297 */         "Standard argument type [" + paramType.getName() + "] resolved to incompatible value of type [" + (
/* 230:298 */         value != null ? value.getClass() : null) + 
/* 231:299 */         "]. Consider declaring the argument type in a less specific fashion.");
/* 232:    */     }
/* 233:301 */     return value;
/* 234:    */   }
/* 235:    */   
/* 236:    */   protected Object resolveStandardArgument(Class parameterType, NativeWebRequest webRequest, Exception thrownException)
/* 237:    */     throws Exception
/* 238:    */   {
/* 239:317 */     if (parameterType.isInstance(thrownException)) {
/* 240:318 */       return thrownException;
/* 241:    */     }
/* 242:320 */     if (WebRequest.class.isAssignableFrom(parameterType)) {
/* 243:321 */       return webRequest;
/* 244:    */     }
/* 245:324 */     HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 246:325 */     HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class);
/* 247:327 */     if (ServletRequest.class.isAssignableFrom(parameterType)) {
/* 248:328 */       return request;
/* 249:    */     }
/* 250:330 */     if (ServletResponse.class.isAssignableFrom(parameterType)) {
/* 251:331 */       return response;
/* 252:    */     }
/* 253:333 */     if (HttpSession.class.isAssignableFrom(parameterType)) {
/* 254:334 */       return request.getSession();
/* 255:    */     }
/* 256:336 */     if (Principal.class.isAssignableFrom(parameterType)) {
/* 257:337 */       return request.getUserPrincipal();
/* 258:    */     }
/* 259:339 */     if (Locale.class.equals(parameterType)) {
/* 260:340 */       return RequestContextUtils.getLocale(request);
/* 261:    */     }
/* 262:342 */     if (InputStream.class.isAssignableFrom(parameterType)) {
/* 263:343 */       return request.getInputStream();
/* 264:    */     }
/* 265:345 */     if (Reader.class.isAssignableFrom(parameterType)) {
/* 266:346 */       return request.getReader();
/* 267:    */     }
/* 268:348 */     if (OutputStream.class.isAssignableFrom(parameterType)) {
/* 269:349 */       return response.getOutputStream();
/* 270:    */     }
/* 271:351 */     if (Writer.class.isAssignableFrom(parameterType)) {
/* 272:352 */       return response.getWriter();
/* 273:    */     }
/* 274:355 */     return WebArgumentResolver.UNRESOLVED;
/* 275:    */   }
/* 276:    */   
/* 277:    */   private Object doInvokeMethod(Method method, Object target, Object[] args)
/* 278:    */     throws Exception
/* 279:    */   {
/* 280:361 */     ReflectionUtils.makeAccessible(method);
/* 281:    */     try
/* 282:    */     {
/* 283:363 */       return method.invoke(target, args);
/* 284:    */     }
/* 285:    */     catch (InvocationTargetException ex)
/* 286:    */     {
/* 287:366 */       ReflectionUtils.rethrowException(ex.getTargetException());
/* 288:    */       
/* 289:368 */       throw new IllegalStateException("Should never get here");
/* 290:    */     }
/* 291:    */   }
/* 292:    */   
/* 293:    */   private ModelAndView getModelAndView(Method handlerMethod, Object returnValue, ServletWebRequest webRequest)
/* 294:    */     throws Exception
/* 295:    */   {
/* 296:375 */     ResponseStatus responseStatusAnn = (ResponseStatus)AnnotationUtils.findAnnotation(handlerMethod, ResponseStatus.class);
/* 297:376 */     if (responseStatusAnn != null)
/* 298:    */     {
/* 299:377 */       HttpStatus responseStatus = responseStatusAnn.value();
/* 300:378 */       String reason = responseStatusAnn.reason();
/* 301:379 */       if (!StringUtils.hasText(reason)) {
/* 302:380 */         webRequest.getResponse().setStatus(responseStatus.value());
/* 303:    */       } else {
/* 304:383 */         webRequest.getResponse().sendError(responseStatus.value(), reason);
/* 305:    */       }
/* 306:    */     }
/* 307:387 */     if ((returnValue != null) && (AnnotationUtils.findAnnotation(handlerMethod, ResponseBody.class) != null)) {
/* 308:388 */       return handleResponseBody(returnValue, webRequest);
/* 309:    */     }
/* 310:391 */     if ((returnValue instanceof ModelAndView)) {
/* 311:392 */       return (ModelAndView)returnValue;
/* 312:    */     }
/* 313:394 */     if ((returnValue instanceof Model)) {
/* 314:395 */       return new ModelAndView().addAllObjects(((Model)returnValue).asMap());
/* 315:    */     }
/* 316:397 */     if ((returnValue instanceof Map)) {
/* 317:398 */       return new ModelAndView().addAllObjects((Map)returnValue);
/* 318:    */     }
/* 319:400 */     if ((returnValue instanceof View)) {
/* 320:401 */       return new ModelAndView((View)returnValue);
/* 321:    */     }
/* 322:403 */     if ((returnValue instanceof String)) {
/* 323:404 */       return new ModelAndView((String)returnValue);
/* 324:    */     }
/* 325:406 */     if (returnValue == null) {
/* 326:407 */       return new ModelAndView();
/* 327:    */     }
/* 328:410 */     throw new IllegalArgumentException("Invalid handler method return value: " + returnValue);
/* 329:    */   }
/* 330:    */   
/* 331:    */   private ModelAndView handleResponseBody(Object returnValue, ServletWebRequest webRequest)
/* 332:    */     throws ServletException, IOException
/* 333:    */   {
/* 334:418 */     HttpInputMessage inputMessage = new ServletServerHttpRequest(webRequest.getRequest());
/* 335:419 */     List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
/* 336:420 */     if (acceptedMediaTypes.isEmpty()) {
/* 337:421 */       acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
/* 338:    */     }
/* 339:423 */     MediaType.sortByQualityValue(acceptedMediaTypes);
/* 340:424 */     HttpOutputMessage outputMessage = new ServletServerHttpResponse(webRequest.getResponse());
/* 341:425 */     Class<?> returnValueType = returnValue.getClass();
/* 342:426 */     if (this.messageConverters != null)
/* 343:    */     {
/* 344:    */       int j;
/* 345:    */       int i;
/* 346:427 */       for (Iterator localIterator = acceptedMediaTypes.iterator(); localIterator.hasNext(); i < j)
/* 347:    */       {
/* 348:427 */         MediaType acceptedMediaType = (MediaType)localIterator.next();
/* 349:    */         HttpMessageConverter[] arrayOfHttpMessageConverter;
/* 350:428 */         j = (arrayOfHttpMessageConverter = this.messageConverters).length;i = 0; continue;HttpMessageConverter messageConverter = arrayOfHttpMessageConverter[i];
/* 351:429 */         if (messageConverter.canWrite(returnValueType, acceptedMediaType))
/* 352:    */         {
/* 353:430 */           messageConverter.write(returnValue, acceptedMediaType, outputMessage);
/* 354:431 */           return new ModelAndView();
/* 355:    */         }
/* 356:428 */         i++;
/* 357:    */       }
/* 358:    */     }
/* 359:436 */     if (this.logger.isWarnEnabled()) {
/* 360:437 */       this.logger.warn("Could not find HttpMessageConverter that supports return type [" + returnValueType + "] and " + 
/* 361:438 */         acceptedMediaTypes);
/* 362:    */     }
/* 363:440 */     return null;
/* 364:    */   }
/* 365:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerExceptionResolver
 * JD-Core Version:    0.7.0.1
 */