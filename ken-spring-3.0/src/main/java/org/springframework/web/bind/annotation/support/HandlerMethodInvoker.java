/*   1:    */ package org.springframework.web.bind.annotation.support;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.lang.reflect.Array;
/*   5:    */ import java.lang.reflect.GenericArrayType;
/*   6:    */ import java.lang.reflect.InvocationTargetException;
/*   7:    */ import java.lang.reflect.Method;
/*   8:    */ import java.lang.reflect.ParameterizedType;
/*   9:    */ import java.lang.reflect.Type;
/*  10:    */ import java.util.ArrayList;
/*  11:    */ import java.util.Arrays;
/*  12:    */ import java.util.Collection;
/*  13:    */ import java.util.Iterator;
/*  14:    */ import java.util.LinkedHashMap;
/*  15:    */ import java.util.List;
/*  16:    */ import java.util.Map;
/*  17:    */ import java.util.Map.Entry;
/*  18:    */ import java.util.Set;
/*  19:    */ import org.apache.commons.logging.Log;
/*  20:    */ import org.apache.commons.logging.LogFactory;
/*  21:    */ import org.springframework.beans.BeanUtils;
/*  22:    */ import org.springframework.beans.factory.annotation.Value;
/*  23:    */ import org.springframework.core.BridgeMethodResolver;
/*  24:    */ import org.springframework.core.Conventions;
/*  25:    */ import org.springframework.core.GenericTypeResolver;
/*  26:    */ import org.springframework.core.MethodParameter;
/*  27:    */ import org.springframework.core.ParameterNameDiscoverer;
/*  28:    */ import org.springframework.core.annotation.AnnotationUtils;
/*  29:    */ import org.springframework.http.HttpEntity;
/*  30:    */ import org.springframework.http.HttpHeaders;
/*  31:    */ import org.springframework.http.HttpInputMessage;
/*  32:    */ import org.springframework.http.HttpOutputMessage;
/*  33:    */ import org.springframework.http.MediaType;
/*  34:    */ import org.springframework.http.converter.HttpMessageConverter;
/*  35:    */ import org.springframework.ui.ExtendedModelMap;
/*  36:    */ import org.springframework.ui.Model;
/*  37:    */ import org.springframework.util.Assert;
/*  38:    */ import org.springframework.util.ClassUtils;
/*  39:    */ import org.springframework.util.LinkedMultiValueMap;
/*  40:    */ import org.springframework.util.MultiValueMap;
/*  41:    */ import org.springframework.util.ReflectionUtils;
/*  42:    */ import org.springframework.validation.BindException;
/*  43:    */ import org.springframework.validation.BindingResult;
/*  44:    */ import org.springframework.validation.Errors;
/*  45:    */ import org.springframework.web.HttpMediaTypeNotSupportedException;
/*  46:    */ import org.springframework.web.bind.WebDataBinder;
/*  47:    */ import org.springframework.web.bind.annotation.CookieValue;
/*  48:    */ import org.springframework.web.bind.annotation.InitBinder;
/*  49:    */ import org.springframework.web.bind.annotation.ModelAttribute;
/*  50:    */ import org.springframework.web.bind.annotation.PathVariable;
/*  51:    */ import org.springframework.web.bind.annotation.RequestBody;
/*  52:    */ import org.springframework.web.bind.annotation.RequestHeader;
/*  53:    */ import org.springframework.web.bind.annotation.RequestParam;
/*  54:    */ import org.springframework.web.bind.support.DefaultSessionAttributeStore;
/*  55:    */ import org.springframework.web.bind.support.SessionAttributeStore;
/*  56:    */ import org.springframework.web.bind.support.SessionStatus;
/*  57:    */ import org.springframework.web.bind.support.SimpleSessionStatus;
/*  58:    */ import org.springframework.web.bind.support.WebArgumentResolver;
/*  59:    */ import org.springframework.web.bind.support.WebBindingInitializer;
/*  60:    */ import org.springframework.web.bind.support.WebRequestDataBinder;
/*  61:    */ import org.springframework.web.context.request.NativeWebRequest;
/*  62:    */ import org.springframework.web.context.request.WebRequest;
/*  63:    */ import org.springframework.web.multipart.MultipartFile;
/*  64:    */ import org.springframework.web.multipart.MultipartRequest;
/*  65:    */ 
/*  66:    */ public class HandlerMethodInvoker
/*  67:    */ {
/*  68: 98 */   private static final String MODEL_KEY_PREFIX_STALE = SessionAttributeStore.class.getName() + ".STALE.";
/*  69:101 */   private static final Log logger = LogFactory.getLog(HandlerMethodInvoker.class);
/*  70:    */   private final HandlerMethodResolver methodResolver;
/*  71:    */   private final WebBindingInitializer bindingInitializer;
/*  72:    */   private final SessionAttributeStore sessionAttributeStore;
/*  73:    */   private final ParameterNameDiscoverer parameterNameDiscoverer;
/*  74:    */   private final WebArgumentResolver[] customArgumentResolvers;
/*  75:    */   private final HttpMessageConverter[] messageConverters;
/*  76:115 */   private final SimpleSessionStatus sessionStatus = new SimpleSessionStatus();
/*  77:    */   
/*  78:    */   public HandlerMethodInvoker(HandlerMethodResolver methodResolver)
/*  79:    */   {
/*  80:119 */     this(methodResolver, null);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public HandlerMethodInvoker(HandlerMethodResolver methodResolver, WebBindingInitializer bindingInitializer)
/*  84:    */   {
/*  85:123 */     this(methodResolver, bindingInitializer, new DefaultSessionAttributeStore(), null, null, null);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public HandlerMethodInvoker(HandlerMethodResolver methodResolver, WebBindingInitializer bindingInitializer, SessionAttributeStore sessionAttributeStore, ParameterNameDiscoverer parameterNameDiscoverer, WebArgumentResolver[] customArgumentResolvers, HttpMessageConverter[] messageConverters)
/*  89:    */   {
/*  90:130 */     this.methodResolver = methodResolver;
/*  91:131 */     this.bindingInitializer = bindingInitializer;
/*  92:132 */     this.sessionAttributeStore = sessionAttributeStore;
/*  93:133 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*  94:134 */     this.customArgumentResolvers = customArgumentResolvers;
/*  95:135 */     this.messageConverters = messageConverters;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public final Object invokeHandlerMethod(Method handlerMethod, Object handler, NativeWebRequest webRequest, ExtendedModelMap implicitModel)
/*  99:    */     throws Exception
/* 100:    */   {
/* 101:142 */     Method handlerMethodToInvoke = BridgeMethodResolver.findBridgedMethod(handlerMethod);
/* 102:    */     try
/* 103:    */     {
/* 104:144 */       boolean debug = logger.isDebugEnabled();
/* 105:145 */       for (String attrName : this.methodResolver.getActualSessionAttributeNames())
/* 106:    */       {
/* 107:146 */         Object attrValue = this.sessionAttributeStore.retrieveAttribute(webRequest, attrName);
/* 108:147 */         if (attrValue != null) {
/* 109:148 */           implicitModel.addAttribute(attrName, attrValue);
/* 110:    */         }
/* 111:    */       }
/* 112:151 */       for (Method attributeMethod : this.methodResolver.getModelAttributeMethods())
/* 113:    */       {
/* 114:152 */         Method attributeMethodToInvoke = BridgeMethodResolver.findBridgedMethod(attributeMethod);
/* 115:153 */         Object[] args = resolveHandlerArguments(attributeMethodToInvoke, handler, webRequest, implicitModel);
/* 116:154 */         if (debug) {
/* 117:155 */           logger.debug("Invoking model attribute method: " + attributeMethodToInvoke);
/* 118:    */         }
/* 119:157 */         String attrName = ((ModelAttribute)AnnotationUtils.findAnnotation(attributeMethod, ModelAttribute.class)).value();
/* 120:158 */         if (("".equals(attrName)) || (!implicitModel.containsAttribute(attrName)))
/* 121:    */         {
/* 122:161 */           ReflectionUtils.makeAccessible(attributeMethodToInvoke);
/* 123:162 */           Object attrValue = attributeMethodToInvoke.invoke(handler, args);
/* 124:163 */           if ("".equals(attrName))
/* 125:    */           {
/* 126:164 */             Class resolvedType = GenericTypeResolver.resolveReturnType(attributeMethodToInvoke, handler.getClass());
/* 127:165 */             attrName = Conventions.getVariableNameForReturnType(attributeMethodToInvoke, resolvedType, attrValue);
/* 128:    */           }
/* 129:167 */           if (!implicitModel.containsAttribute(attrName)) {
/* 130:168 */             implicitModel.addAttribute(attrName, attrValue);
/* 131:    */           }
/* 132:    */         }
/* 133:    */       }
/* 134:171 */       Object[] args = resolveHandlerArguments(handlerMethodToInvoke, handler, webRequest, implicitModel);
/* 135:172 */       if (debug) {
/* 136:173 */         logger.debug("Invoking request handler method: " + handlerMethodToInvoke);
/* 137:    */       }
/* 138:175 */       ReflectionUtils.makeAccessible(handlerMethodToInvoke);
/* 139:176 */       return handlerMethodToInvoke.invoke(handler, args);
/* 140:    */     }
/* 141:    */     catch (IllegalStateException ex)
/* 142:    */     {
/* 143:181 */       throw new HandlerMethodInvocationException(handlerMethodToInvoke, ex);
/* 144:    */     }
/* 145:    */     catch (InvocationTargetException ex)
/* 146:    */     {
/* 147:185 */       ReflectionUtils.rethrowException(ex.getTargetException());
/* 148:    */     }
/* 149:186 */     return null;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public final void updateModelAttributes(Object handler, Map<String, Object> mavModel, ExtendedModelMap implicitModel, NativeWebRequest webRequest)
/* 153:    */     throws Exception
/* 154:    */   {
/* 155:193 */     if ((this.methodResolver.hasSessionAttributes()) && (this.sessionStatus.isComplete())) {
/* 156:194 */       for (String attrName : this.methodResolver.getActualSessionAttributeNames()) {
/* 157:195 */         this.sessionAttributeStore.cleanupAttribute(webRequest, attrName);
/* 158:    */       }
/* 159:    */     }
/* 160:201 */     Map<String, Object> model = mavModel != null ? mavModel : implicitModel;
/* 161:202 */     if (model != null) {
/* 162:    */       try
/* 163:    */       {
/* 164:204 */         String[] originalAttrNames = (String[])model.keySet().toArray(new String[model.size()]);
/* 165:205 */         for (String attrName : originalAttrNames)
/* 166:    */         {
/* 167:206 */           Object attrValue = model.get(attrName);
/* 168:207 */           boolean isSessionAttr = this.methodResolver.isSessionAttribute(
/* 169:208 */             attrName, attrValue != null ? attrValue.getClass() : null);
/* 170:209 */           if (isSessionAttr) {
/* 171:210 */             if (this.sessionStatus.isComplete()) {
/* 172:211 */               implicitModel.put(MODEL_KEY_PREFIX_STALE + attrName, Boolean.TRUE);
/* 173:213 */             } else if (!implicitModel.containsKey(MODEL_KEY_PREFIX_STALE + attrName)) {
/* 174:214 */               this.sessionAttributeStore.storeAttribute(webRequest, attrName, attrValue);
/* 175:    */             }
/* 176:    */           }
/* 177:217 */           if ((!attrName.startsWith(BindingResult.MODEL_KEY_PREFIX)) && (
/* 178:218 */             (isSessionAttr) || (isBindingCandidate(attrValue))))
/* 179:    */           {
/* 180:219 */             String bindingResultKey = BindingResult.MODEL_KEY_PREFIX + attrName;
/* 181:220 */             if ((mavModel != null) && (!model.containsKey(bindingResultKey)))
/* 182:    */             {
/* 183:221 */               WebDataBinder binder = createBinder(webRequest, attrValue, attrName);
/* 184:222 */               initBinder(handler, attrName, binder, webRequest);
/* 185:223 */               mavModel.put(bindingResultKey, binder.getBindingResult());
/* 186:    */             }
/* 187:    */           }
/* 188:    */         }
/* 189:    */       }
/* 190:    */       catch (InvocationTargetException ex)
/* 191:    */       {
/* 192:230 */         ReflectionUtils.rethrowException(ex.getTargetException());
/* 193:    */       }
/* 194:    */     }
/* 195:    */   }
/* 196:    */   
/* 197:    */   private Object[] resolveHandlerArguments(Method handlerMethod, Object handler, NativeWebRequest webRequest, ExtendedModelMap implicitModel)
/* 198:    */     throws Exception
/* 199:    */   {
/* 200:239 */     Class[] paramTypes = handlerMethod.getParameterTypes();
/* 201:240 */     Object[] args = new Object[paramTypes.length];
/* 202:242 */     for (int i = 0; i < args.length; i++)
/* 203:    */     {
/* 204:243 */       MethodParameter methodParam = new MethodParameter(handlerMethod, i);
/* 205:244 */       methodParam.initParameterNameDiscovery(this.parameterNameDiscoverer);
/* 206:245 */       GenericTypeResolver.resolveParameterType(methodParam, handler.getClass());
/* 207:246 */       String paramName = null;
/* 208:247 */       String headerName = null;
/* 209:248 */       boolean requestBodyFound = false;
/* 210:249 */       String cookieName = null;
/* 211:250 */       String pathVarName = null;
/* 212:251 */       String attrName = null;
/* 213:252 */       boolean required = false;
/* 214:253 */       String defaultValue = null;
/* 215:254 */       boolean validate = false;
/* 216:255 */       int annotationsFound = 0;
/* 217:256 */       Annotation[] paramAnns = methodParam.getParameterAnnotations();
/* 218:258 */       for (Annotation paramAnn : paramAnns) {
/* 219:259 */         if (RequestParam.class.isInstance(paramAnn))
/* 220:    */         {
/* 221:260 */           RequestParam requestParam = (RequestParam)paramAnn;
/* 222:261 */           paramName = requestParam.value();
/* 223:262 */           required = requestParam.required();
/* 224:263 */           defaultValue = parseDefaultValueAttribute(requestParam.defaultValue());
/* 225:264 */           annotationsFound++;
/* 226:    */         }
/* 227:266 */         else if (RequestHeader.class.isInstance(paramAnn))
/* 228:    */         {
/* 229:267 */           RequestHeader requestHeader = (RequestHeader)paramAnn;
/* 230:268 */           headerName = requestHeader.value();
/* 231:269 */           required = requestHeader.required();
/* 232:270 */           defaultValue = parseDefaultValueAttribute(requestHeader.defaultValue());
/* 233:271 */           annotationsFound++;
/* 234:    */         }
/* 235:273 */         else if (RequestBody.class.isInstance(paramAnn))
/* 236:    */         {
/* 237:274 */           requestBodyFound = true;
/* 238:275 */           annotationsFound++;
/* 239:    */         }
/* 240:277 */         else if (CookieValue.class.isInstance(paramAnn))
/* 241:    */         {
/* 242:278 */           CookieValue cookieValue = (CookieValue)paramAnn;
/* 243:279 */           cookieName = cookieValue.value();
/* 244:280 */           required = cookieValue.required();
/* 245:281 */           defaultValue = parseDefaultValueAttribute(cookieValue.defaultValue());
/* 246:282 */           annotationsFound++;
/* 247:    */         }
/* 248:284 */         else if (PathVariable.class.isInstance(paramAnn))
/* 249:    */         {
/* 250:285 */           PathVariable pathVar = (PathVariable)paramAnn;
/* 251:286 */           pathVarName = pathVar.value();
/* 252:287 */           annotationsFound++;
/* 253:    */         }
/* 254:289 */         else if (ModelAttribute.class.isInstance(paramAnn))
/* 255:    */         {
/* 256:290 */           ModelAttribute attr = (ModelAttribute)paramAnn;
/* 257:291 */           attrName = attr.value();
/* 258:292 */           annotationsFound++;
/* 259:    */         }
/* 260:294 */         else if (Value.class.isInstance(paramAnn))
/* 261:    */         {
/* 262:295 */           defaultValue = ((Value)paramAnn).value();
/* 263:    */         }
/* 264:297 */         else if ("Valid".equals(paramAnn.annotationType().getSimpleName()))
/* 265:    */         {
/* 266:298 */           validate = true;
/* 267:    */         }
/* 268:    */       }
/* 269:302 */       if (annotationsFound > 1) {
/* 270:303 */         throw new IllegalStateException("Handler parameter annotations are exclusive choices - do not specify more than one such annotation on the same parameter: " + 
/* 271:304 */           handlerMethod);
/* 272:    */       }
/* 273:307 */       if (annotationsFound == 0)
/* 274:    */       {
/* 275:308 */         Object argValue = resolveCommonArgument(methodParam, webRequest);
/* 276:309 */         if (argValue != WebArgumentResolver.UNRESOLVED)
/* 277:    */         {
/* 278:310 */           args[i] = argValue;
/* 279:    */         }
/* 280:312 */         else if (defaultValue != null)
/* 281:    */         {
/* 282:313 */           args[i] = resolveDefaultValue(defaultValue);
/* 283:    */         }
/* 284:    */         else
/* 285:    */         {
/* 286:316 */           Class paramType = methodParam.getParameterType();
/* 287:317 */           if ((Model.class.isAssignableFrom(paramType)) || (Map.class.isAssignableFrom(paramType)))
/* 288:    */           {
/* 289:318 */             args[i] = implicitModel;
/* 290:    */           }
/* 291:320 */           else if (SessionStatus.class.isAssignableFrom(paramType))
/* 292:    */           {
/* 293:321 */             args[i] = this.sessionStatus;
/* 294:    */           }
/* 295:323 */           else if (HttpEntity.class.isAssignableFrom(paramType))
/* 296:    */           {
/* 297:324 */             args[i] = resolveHttpEntityRequest(methodParam, webRequest);
/* 298:    */           }
/* 299:    */           else
/* 300:    */           {
/* 301:326 */             if (Errors.class.isAssignableFrom(paramType)) {
/* 302:327 */               throw new IllegalStateException("Errors/BindingResult argument declared without preceding model attribute. Check your handler method signature!");
/* 303:    */             }
/* 304:330 */             if (BeanUtils.isSimpleProperty(paramType)) {
/* 305:331 */               paramName = "";
/* 306:    */             } else {
/* 307:334 */               attrName = "";
/* 308:    */             }
/* 309:    */           }
/* 310:    */         }
/* 311:    */       }
/* 312:339 */       if (paramName != null)
/* 313:    */       {
/* 314:340 */         args[i] = resolveRequestParam(paramName, required, defaultValue, methodParam, webRequest, handler);
/* 315:    */       }
/* 316:342 */       else if (headerName != null)
/* 317:    */       {
/* 318:343 */         args[i] = resolveRequestHeader(headerName, required, defaultValue, methodParam, webRequest, handler);
/* 319:    */       }
/* 320:345 */       else if (requestBodyFound)
/* 321:    */       {
/* 322:346 */         args[i] = resolveRequestBody(methodParam, webRequest, handler);
/* 323:    */       }
/* 324:348 */       else if (cookieName != null)
/* 325:    */       {
/* 326:349 */         args[i] = resolveCookieValue(cookieName, required, defaultValue, methodParam, webRequest, handler);
/* 327:    */       }
/* 328:351 */       else if (pathVarName != null)
/* 329:    */       {
/* 330:352 */         args[i] = resolvePathVariable(pathVarName, methodParam, webRequest, handler);
/* 331:    */       }
/* 332:354 */       else if (attrName != null)
/* 333:    */       {
/* 334:355 */         WebDataBinder binder = 
/* 335:356 */           resolveModelAttribute(attrName, methodParam, implicitModel, webRequest, handler);
/* 336:357 */         boolean assignBindingResult = (args.length > i + 1) && (Errors.class.isAssignableFrom(paramTypes[(i + 1)]));
/* 337:358 */         if (binder.getTarget() != null) {
/* 338:359 */           doBind(binder, webRequest, validate, !assignBindingResult);
/* 339:    */         }
/* 340:361 */         args[i] = binder.getTarget();
/* 341:362 */         if (assignBindingResult)
/* 342:    */         {
/* 343:363 */           args[(i + 1)] = binder.getBindingResult();
/* 344:364 */           i++;
/* 345:    */         }
/* 346:366 */         implicitModel.putAll(binder.getBindingResult().getModel());
/* 347:    */       }
/* 348:    */     }
/* 349:370 */     return args;
/* 350:    */   }
/* 351:    */   
/* 352:    */   protected void initBinder(Object handler, String attrName, WebDataBinder binder, NativeWebRequest webRequest)
/* 353:    */     throws Exception
/* 354:    */   {
/* 355:376 */     if (this.bindingInitializer != null) {
/* 356:377 */       this.bindingInitializer.initBinder(binder, webRequest);
/* 357:    */     }
/* 358:379 */     if (handler != null)
/* 359:    */     {
/* 360:380 */       Set<Method> initBinderMethods = this.methodResolver.getInitBinderMethods();
/* 361:381 */       if (!initBinderMethods.isEmpty())
/* 362:    */       {
/* 363:382 */         boolean debug = logger.isDebugEnabled();
/* 364:383 */         for (Method initBinderMethod : initBinderMethods)
/* 365:    */         {
/* 366:384 */           Method methodToInvoke = BridgeMethodResolver.findBridgedMethod(initBinderMethod);
/* 367:385 */           String[] targetNames = ((InitBinder)AnnotationUtils.findAnnotation(initBinderMethod, InitBinder.class)).value();
/* 368:386 */           if ((targetNames.length == 0) || (Arrays.asList(targetNames).contains(attrName)))
/* 369:    */           {
/* 370:387 */             Object[] initBinderArgs = 
/* 371:388 */               resolveInitBinderArguments(handler, methodToInvoke, binder, webRequest);
/* 372:389 */             if (debug) {
/* 373:390 */               logger.debug("Invoking init-binder method: " + methodToInvoke);
/* 374:    */             }
/* 375:392 */             ReflectionUtils.makeAccessible(methodToInvoke);
/* 376:393 */             Object returnValue = methodToInvoke.invoke(handler, initBinderArgs);
/* 377:394 */             if (returnValue != null) {
/* 378:395 */               throw new IllegalStateException(
/* 379:396 */                 "InitBinder methods must not have a return value: " + methodToInvoke);
/* 380:    */             }
/* 381:    */           }
/* 382:    */         }
/* 383:    */       }
/* 384:    */     }
/* 385:    */   }
/* 386:    */   
/* 387:    */   private Object[] resolveInitBinderArguments(Object handler, Method initBinderMethod, WebDataBinder binder, NativeWebRequest webRequest)
/* 388:    */     throws Exception
/* 389:    */   {
/* 390:407 */     Class[] initBinderParams = initBinderMethod.getParameterTypes();
/* 391:408 */     Object[] initBinderArgs = new Object[initBinderParams.length];
/* 392:410 */     for (int i = 0; i < initBinderArgs.length; i++)
/* 393:    */     {
/* 394:411 */       MethodParameter methodParam = new MethodParameter(initBinderMethod, i);
/* 395:412 */       methodParam.initParameterNameDiscovery(this.parameterNameDiscoverer);
/* 396:413 */       GenericTypeResolver.resolveParameterType(methodParam, handler.getClass());
/* 397:414 */       String paramName = null;
/* 398:415 */       boolean paramRequired = false;
/* 399:416 */       String paramDefaultValue = null;
/* 400:417 */       String pathVarName = null;
/* 401:418 */       Annotation[] paramAnns = methodParam.getParameterAnnotations();
/* 402:420 */       for (Annotation paramAnn : paramAnns)
/* 403:    */       {
/* 404:421 */         if (RequestParam.class.isInstance(paramAnn))
/* 405:    */         {
/* 406:422 */           RequestParam requestParam = (RequestParam)paramAnn;
/* 407:423 */           paramName = requestParam.value();
/* 408:424 */           paramRequired = requestParam.required();
/* 409:425 */           paramDefaultValue = parseDefaultValueAttribute(requestParam.defaultValue());
/* 410:426 */           break;
/* 411:    */         }
/* 412:428 */         if (ModelAttribute.class.isInstance(paramAnn)) {
/* 413:429 */           throw new IllegalStateException(
/* 414:430 */             "@ModelAttribute is not supported on @InitBinder methods: " + initBinderMethod);
/* 415:    */         }
/* 416:432 */         if (PathVariable.class.isInstance(paramAnn))
/* 417:    */         {
/* 418:433 */           PathVariable pathVar = (PathVariable)paramAnn;
/* 419:434 */           pathVarName = pathVar.value();
/* 420:    */         }
/* 421:    */       }
/* 422:438 */       if ((paramName == null) && (pathVarName == null))
/* 423:    */       {
/* 424:439 */         Object argValue = resolveCommonArgument(methodParam, webRequest);
/* 425:440 */         if (argValue != WebArgumentResolver.UNRESOLVED)
/* 426:    */         {
/* 427:441 */           initBinderArgs[i] = argValue;
/* 428:    */         }
/* 429:    */         else
/* 430:    */         {
/* 431:444 */           Class paramType = initBinderParams[i];
/* 432:445 */           if (paramType.isInstance(binder)) {
/* 433:446 */             initBinderArgs[i] = binder;
/* 434:448 */           } else if (BeanUtils.isSimpleProperty(paramType)) {
/* 435:449 */             paramName = "";
/* 436:    */           } else {
/* 437:452 */             throw new IllegalStateException("Unsupported argument [" + paramType.getName() + 
/* 438:453 */               "] for @InitBinder method: " + initBinderMethod);
/* 439:    */           }
/* 440:    */         }
/* 441:    */       }
/* 442:458 */       if (paramName != null) {
/* 443:459 */         initBinderArgs[i] = 
/* 444:460 */           resolveRequestParam(paramName, paramRequired, paramDefaultValue, methodParam, webRequest, null);
/* 445:462 */       } else if (pathVarName != null) {
/* 446:463 */         initBinderArgs[i] = resolvePathVariable(pathVarName, methodParam, webRequest, null);
/* 447:    */       }
/* 448:    */     }
/* 449:467 */     return initBinderArgs;
/* 450:    */   }
/* 451:    */   
/* 452:    */   private Object resolveRequestParam(String paramName, boolean required, String defaultValue, MethodParameter methodParam, NativeWebRequest webRequest, Object handlerForInitBinderCall)
/* 453:    */     throws Exception
/* 454:    */   {
/* 455:475 */     Class<?> paramType = methodParam.getParameterType();
/* 456:476 */     if ((Map.class.isAssignableFrom(paramType)) && (paramName.length() == 0)) {
/* 457:477 */       return resolveRequestParamMap(paramType, webRequest);
/* 458:    */     }
/* 459:479 */     if (paramName.length() == 0) {
/* 460:480 */       paramName = getRequiredParameterName(methodParam);
/* 461:    */     }
/* 462:482 */     Object paramValue = null;
/* 463:483 */     MultipartRequest multipartRequest = (MultipartRequest)webRequest.getNativeRequest(MultipartRequest.class);
/* 464:484 */     if (multipartRequest != null)
/* 465:    */     {
/* 466:485 */       List<MultipartFile> files = multipartRequest.getFiles(paramName);
/* 467:486 */       if (!files.isEmpty()) {
/* 468:487 */         paramValue = files.size() == 1 ? files.get(0) : files;
/* 469:    */       }
/* 470:    */     }
/* 471:490 */     if (paramValue == null)
/* 472:    */     {
/* 473:491 */       String[] paramValues = webRequest.getParameterValues(paramName);
/* 474:492 */       if (paramValues != null) {
/* 475:493 */         paramValue = paramValues.length == 1 ? paramValues[0] : paramValues;
/* 476:    */       }
/* 477:    */     }
/* 478:496 */     if (paramValue == null)
/* 479:    */     {
/* 480:497 */       if (defaultValue != null) {
/* 481:498 */         paramValue = resolveDefaultValue(defaultValue);
/* 482:500 */       } else if (required) {
/* 483:501 */         raiseMissingParameterException(paramName, paramType);
/* 484:    */       }
/* 485:503 */       paramValue = checkValue(paramName, paramValue, paramType);
/* 486:    */     }
/* 487:505 */     WebDataBinder binder = createBinder(webRequest, null, paramName);
/* 488:506 */     initBinder(handlerForInitBinderCall, paramName, binder, webRequest);
/* 489:507 */     return binder.convertIfNecessary(paramValue, paramType, methodParam);
/* 490:    */   }
/* 491:    */   
/* 492:    */   private Map resolveRequestParamMap(Class<? extends Map> mapType, NativeWebRequest webRequest)
/* 493:    */   {
/* 494:511 */     Map<String, String[]> parameterMap = webRequest.getParameterMap();
/* 495:    */     Iterator localIterator;
/* 496:512 */     if (MultiValueMap.class.isAssignableFrom(mapType))
/* 497:    */     {
/* 498:513 */       MultiValueMap<String, String> result = new LinkedMultiValueMap(parameterMap.size());
/* 499:    */       int j;
/* 500:    */       int i;
/* 501:514 */       for (localIterator = parameterMap.entrySet().iterator(); localIterator.hasNext(); i < j)
/* 502:    */       {
/* 503:514 */         Map.Entry<String, String[]> entry = (Map.Entry)localIterator.next();
/* 504:    */         String[] arrayOfString;
/* 505:515 */         j = (arrayOfString = (String[])entry.getValue()).length;i = 0; continue;String value = arrayOfString[i];
/* 506:516 */         result.add((String)entry.getKey(), value);i++;
/* 507:    */       }
/* 508:519 */       return result;
/* 509:    */     }
/* 510:522 */     Map<String, String> result = new LinkedHashMap(parameterMap.size());
/* 511:523 */     for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
/* 512:524 */       if (((String[])entry.getValue()).length > 0) {
/* 513:525 */         result.put((String)entry.getKey(), ((String[])entry.getValue())[0]);
/* 514:    */       }
/* 515:    */     }
/* 516:528 */     return result;
/* 517:    */   }
/* 518:    */   
/* 519:    */   private Object resolveRequestHeader(String headerName, boolean required, String defaultValue, MethodParameter methodParam, NativeWebRequest webRequest, Object handlerForInitBinderCall)
/* 520:    */     throws Exception
/* 521:    */   {
/* 522:537 */     Class<?> paramType = methodParam.getParameterType();
/* 523:538 */     if (Map.class.isAssignableFrom(paramType)) {
/* 524:539 */       return resolveRequestHeaderMap(paramType, webRequest);
/* 525:    */     }
/* 526:541 */     if (headerName.length() == 0) {
/* 527:542 */       headerName = getRequiredParameterName(methodParam);
/* 528:    */     }
/* 529:544 */     Object headerValue = null;
/* 530:545 */     String[] headerValues = webRequest.getHeaderValues(headerName);
/* 531:546 */     if (headerValues != null) {
/* 532:547 */       headerValue = headerValues.length == 1 ? headerValues[0] : headerValues;
/* 533:    */     }
/* 534:549 */     if (headerValue == null)
/* 535:    */     {
/* 536:550 */       if (defaultValue != null) {
/* 537:551 */         headerValue = resolveDefaultValue(defaultValue);
/* 538:553 */       } else if (required) {
/* 539:554 */         raiseMissingHeaderException(headerName, paramType);
/* 540:    */       }
/* 541:556 */       headerValue = checkValue(headerName, headerValue, paramType);
/* 542:    */     }
/* 543:558 */     WebDataBinder binder = createBinder(webRequest, null, headerName);
/* 544:559 */     initBinder(handlerForInitBinderCall, headerName, binder, webRequest);
/* 545:560 */     return binder.convertIfNecessary(headerValue, paramType, methodParam);
/* 546:    */   }
/* 547:    */   
/* 548:    */   private Map resolveRequestHeaderMap(Class<? extends Map> mapType, NativeWebRequest webRequest)
/* 549:    */   {
/* 550:564 */     if (MultiValueMap.class.isAssignableFrom(mapType))
/* 551:    */     {
/* 552:    */       MultiValueMap<String, String> result;
/* 553:    */       MultiValueMap<String, String> result;
/* 554:566 */       if (HttpHeaders.class.isAssignableFrom(mapType)) {
/* 555:567 */         result = new HttpHeaders();
/* 556:    */       } else {
/* 557:570 */         result = new LinkedMultiValueMap();
/* 558:    */       }
/* 559:    */       int j;
/* 560:    */       int i;
/* 561:572 */       for (Iterator<String> iterator = webRequest.getHeaderNames(); iterator.hasNext(); i < j)
/* 562:    */       {
/* 563:573 */         String headerName = (String)iterator.next();
/* 564:    */         String[] arrayOfString;
/* 565:574 */         j = (arrayOfString = webRequest.getHeaderValues(headerName)).length;i = 0; continue;String headerValue = arrayOfString[i];
/* 566:575 */         result.add(headerName, headerValue);i++;
/* 567:    */       }
/* 568:578 */       return result;
/* 569:    */     }
/* 570:581 */     Map<String, String> result = new LinkedHashMap();
/* 571:582 */     for (Iterator<String> iterator = webRequest.getHeaderNames(); iterator.hasNext();)
/* 572:    */     {
/* 573:583 */       String headerName = (String)iterator.next();
/* 574:584 */       String headerValue = webRequest.getHeader(headerName);
/* 575:585 */       result.put(headerName, headerValue);
/* 576:    */     }
/* 577:587 */     return result;
/* 578:    */   }
/* 579:    */   
/* 580:    */   protected Object resolveRequestBody(MethodParameter methodParam, NativeWebRequest webRequest, Object handler)
/* 581:    */     throws Exception
/* 582:    */   {
/* 583:597 */     return readWithMessageConverters(methodParam, createHttpInputMessage(webRequest), methodParam.getParameterType());
/* 584:    */   }
/* 585:    */   
/* 586:    */   private HttpEntity resolveHttpEntityRequest(MethodParameter methodParam, NativeWebRequest webRequest)
/* 587:    */     throws Exception
/* 588:    */   {
/* 589:603 */     HttpInputMessage inputMessage = createHttpInputMessage(webRequest);
/* 590:604 */     Class<?> paramType = getHttpEntityType(methodParam);
/* 591:605 */     Object body = readWithMessageConverters(methodParam, inputMessage, paramType);
/* 592:606 */     return new HttpEntity(body, inputMessage.getHeaders());
/* 593:    */   }
/* 594:    */   
/* 595:    */   private Object readWithMessageConverters(MethodParameter methodParam, HttpInputMessage inputMessage, Class paramType)
/* 596:    */     throws Exception
/* 597:    */   {
/* 598:612 */     MediaType contentType = inputMessage.getHeaders().getContentType();
/* 599:613 */     if (contentType == null)
/* 600:    */     {
/* 601:614 */       StringBuilder builder = new StringBuilder(ClassUtils.getShortName(methodParam.getParameterType()));
/* 602:615 */       String paramName = methodParam.getParameterName();
/* 603:616 */       if (paramName != null)
/* 604:    */       {
/* 605:617 */         builder.append(' ');
/* 606:618 */         builder.append(paramName);
/* 607:    */       }
/* 608:620 */       throw new HttpMediaTypeNotSupportedException(
/* 609:621 */         "Cannot extract parameter (" + builder.toString() + "): no Content-Type found");
/* 610:    */     }
/* 611:624 */     List<MediaType> allSupportedMediaTypes = new ArrayList();
/* 612:625 */     if (this.messageConverters != null) {
/* 613:626 */       for (HttpMessageConverter<?> messageConverter : this.messageConverters)
/* 614:    */       {
/* 615:627 */         allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
/* 616:628 */         if (messageConverter.canRead(paramType, contentType))
/* 617:    */         {
/* 618:629 */           if (logger.isDebugEnabled()) {
/* 619:630 */             logger.debug("Reading [" + paramType.getName() + "] as \"" + contentType + 
/* 620:631 */               "\" using [" + messageConverter + "]");
/* 621:    */           }
/* 622:633 */           return messageConverter.read(paramType, inputMessage);
/* 623:    */         }
/* 624:    */       }
/* 625:    */     }
/* 626:637 */     throw new HttpMediaTypeNotSupportedException(contentType, allSupportedMediaTypes);
/* 627:    */   }
/* 628:    */   
/* 629:    */   private Class<?> getHttpEntityType(MethodParameter methodParam)
/* 630:    */   {
/* 631:641 */     Assert.isAssignable(HttpEntity.class, methodParam.getParameterType());
/* 632:642 */     ParameterizedType type = (ParameterizedType)methodParam.getGenericParameterType();
/* 633:643 */     if (type.getActualTypeArguments().length == 1)
/* 634:    */     {
/* 635:644 */       Type typeArgument = type.getActualTypeArguments()[0];
/* 636:645 */       if ((typeArgument instanceof Class)) {
/* 637:646 */         return (Class)typeArgument;
/* 638:    */       }
/* 639:648 */       if ((typeArgument instanceof GenericArrayType))
/* 640:    */       {
/* 641:649 */         Type componentType = ((GenericArrayType)typeArgument).getGenericComponentType();
/* 642:650 */         if ((componentType instanceof Class))
/* 643:    */         {
/* 644:652 */           Object array = Array.newInstance((Class)componentType, 0);
/* 645:653 */           return array.getClass();
/* 646:    */         }
/* 647:    */       }
/* 648:    */     }
/* 649:657 */     throw new IllegalArgumentException(
/* 650:658 */       "HttpEntity parameter (" + methodParam.getParameterName() + ") is not parameterized");
/* 651:    */   }
/* 652:    */   
/* 653:    */   private Object resolveCookieValue(String cookieName, boolean required, String defaultValue, MethodParameter methodParam, NativeWebRequest webRequest, Object handlerForInitBinderCall)
/* 654:    */     throws Exception
/* 655:    */   {
/* 656:666 */     Class<?> paramType = methodParam.getParameterType();
/* 657:667 */     if (cookieName.length() == 0) {
/* 658:668 */       cookieName = getRequiredParameterName(methodParam);
/* 659:    */     }
/* 660:670 */     Object cookieValue = resolveCookieValue(cookieName, paramType, webRequest);
/* 661:671 */     if (cookieValue == null)
/* 662:    */     {
/* 663:672 */       if (defaultValue != null) {
/* 664:673 */         cookieValue = resolveDefaultValue(defaultValue);
/* 665:675 */       } else if (required) {
/* 666:676 */         raiseMissingCookieException(cookieName, paramType);
/* 667:    */       }
/* 668:678 */       cookieValue = checkValue(cookieName, cookieValue, paramType);
/* 669:    */     }
/* 670:680 */     WebDataBinder binder = createBinder(webRequest, null, cookieName);
/* 671:681 */     initBinder(handlerForInitBinderCall, cookieName, binder, webRequest);
/* 672:682 */     return binder.convertIfNecessary(cookieValue, paramType, methodParam);
/* 673:    */   }
/* 674:    */   
/* 675:    */   protected Object resolveCookieValue(String cookieName, Class paramType, NativeWebRequest webRequest)
/* 676:    */     throws Exception
/* 677:    */   {
/* 678:692 */     throw new UnsupportedOperationException("@CookieValue not supported");
/* 679:    */   }
/* 680:    */   
/* 681:    */   private Object resolvePathVariable(String pathVarName, MethodParameter methodParam, NativeWebRequest webRequest, Object handlerForInitBinderCall)
/* 682:    */     throws Exception
/* 683:    */   {
/* 684:698 */     Class<?> paramType = methodParam.getParameterType();
/* 685:699 */     if (pathVarName.length() == 0) {
/* 686:700 */       pathVarName = getRequiredParameterName(methodParam);
/* 687:    */     }
/* 688:702 */     String pathVarValue = resolvePathVariable(pathVarName, paramType, webRequest);
/* 689:703 */     WebDataBinder binder = createBinder(webRequest, null, pathVarName);
/* 690:704 */     initBinder(handlerForInitBinderCall, pathVarName, binder, webRequest);
/* 691:705 */     return binder.convertIfNecessary(pathVarValue, paramType, methodParam);
/* 692:    */   }
/* 693:    */   
/* 694:    */   protected String resolvePathVariable(String pathVarName, Class paramType, NativeWebRequest webRequest)
/* 695:    */     throws Exception
/* 696:    */   {
/* 697:715 */     throw new UnsupportedOperationException("@PathVariable not supported");
/* 698:    */   }
/* 699:    */   
/* 700:    */   private String getRequiredParameterName(MethodParameter methodParam)
/* 701:    */   {
/* 702:719 */     String name = methodParam.getParameterName();
/* 703:720 */     if (name == null) {
/* 704:721 */       throw new IllegalStateException(
/* 705:722 */         "No parameter name specified for argument of type [" + methodParam.getParameterType().getName() + 
/* 706:723 */         "], and no parameter name information found in class file either.");
/* 707:    */     }
/* 708:725 */     return name;
/* 709:    */   }
/* 710:    */   
/* 711:    */   private Object checkValue(String name, Object value, Class paramType)
/* 712:    */   {
/* 713:729 */     if (value == null)
/* 714:    */     {
/* 715:730 */       if (Boolean.TYPE.equals(paramType)) {
/* 716:731 */         return Boolean.FALSE;
/* 717:    */       }
/* 718:733 */       if (paramType.isPrimitive()) {
/* 719:734 */         throw new IllegalStateException("Optional " + paramType + " parameter '" + name + 
/* 720:735 */           "' is not present but cannot be translated into a null value due to being declared as a " + 
/* 721:736 */           "primitive type. Consider declaring it as object wrapper for the corresponding primitive type.");
/* 722:    */       }
/* 723:    */     }
/* 724:739 */     return value;
/* 725:    */   }
/* 726:    */   
/* 727:    */   private WebDataBinder resolveModelAttribute(String attrName, MethodParameter methodParam, ExtendedModelMap implicitModel, NativeWebRequest webRequest, Object handler)
/* 728:    */     throws Exception
/* 729:    */   {
/* 730:746 */     String name = attrName;
/* 731:747 */     if ("".equals(name)) {
/* 732:748 */       name = Conventions.getVariableNameForParameter(methodParam);
/* 733:    */     }
/* 734:750 */     Class<?> paramType = methodParam.getParameterType();
/* 735:    */     Object bindObject;
/* 736:    */     Object bindObject;
/* 737:752 */     if (implicitModel.containsKey(name))
/* 738:    */     {
/* 739:753 */       bindObject = implicitModel.get(name);
/* 740:    */     }
/* 741:755 */     else if (this.methodResolver.isSessionAttribute(name, paramType))
/* 742:    */     {
/* 743:756 */       Object bindObject = this.sessionAttributeStore.retrieveAttribute(webRequest, name);
/* 744:757 */       if (bindObject == null) {
/* 745:758 */         raiseSessionRequiredException("Session attribute '" + name + "' required - not found in session");
/* 746:    */       }
/* 747:    */     }
/* 748:    */     else
/* 749:    */     {
/* 750:762 */       bindObject = BeanUtils.instantiateClass(paramType);
/* 751:    */     }
/* 752:764 */     WebDataBinder binder = createBinder(webRequest, bindObject, name);
/* 753:765 */     initBinder(handler, name, binder, webRequest);
/* 754:766 */     return binder;
/* 755:    */   }
/* 756:    */   
/* 757:    */   protected boolean isBindingCandidate(Object value)
/* 758:    */   {
/* 759:776 */     return (value != null) && (!value.getClass().isArray()) && (!(value instanceof Collection)) && (!(value instanceof Map)) && (!BeanUtils.isSimpleValueType(value.getClass()));
/* 760:    */   }
/* 761:    */   
/* 762:    */   protected void raiseMissingParameterException(String paramName, Class paramType)
/* 763:    */     throws Exception
/* 764:    */   {
/* 765:780 */     throw new IllegalStateException("Missing parameter '" + paramName + "' of type [" + paramType.getName() + "]");
/* 766:    */   }
/* 767:    */   
/* 768:    */   protected void raiseMissingHeaderException(String headerName, Class paramType)
/* 769:    */     throws Exception
/* 770:    */   {
/* 771:784 */     throw new IllegalStateException("Missing header '" + headerName + "' of type [" + paramType.getName() + "]");
/* 772:    */   }
/* 773:    */   
/* 774:    */   protected void raiseMissingCookieException(String cookieName, Class paramType)
/* 775:    */     throws Exception
/* 776:    */   {
/* 777:788 */     throw new IllegalStateException(
/* 778:789 */       "Missing cookie value '" + cookieName + "' of type [" + paramType.getName() + "]");
/* 779:    */   }
/* 780:    */   
/* 781:    */   protected void raiseSessionRequiredException(String message)
/* 782:    */     throws Exception
/* 783:    */   {
/* 784:793 */     throw new IllegalStateException(message);
/* 785:    */   }
/* 786:    */   
/* 787:    */   protected WebDataBinder createBinder(NativeWebRequest webRequest, Object target, String objectName)
/* 788:    */     throws Exception
/* 789:    */   {
/* 790:799 */     return new WebRequestDataBinder(target, objectName);
/* 791:    */   }
/* 792:    */   
/* 793:    */   private void doBind(WebDataBinder binder, NativeWebRequest webRequest, boolean validate, boolean failOnErrors)
/* 794:    */     throws Exception
/* 795:    */   {
/* 796:805 */     doBind(binder, webRequest);
/* 797:806 */     if (validate) {
/* 798:807 */       binder.validate();
/* 799:    */     }
/* 800:809 */     if ((failOnErrors) && (binder.getBindingResult().hasErrors())) {
/* 801:810 */       throw new BindException(binder.getBindingResult());
/* 802:    */     }
/* 803:    */   }
/* 804:    */   
/* 805:    */   protected void doBind(WebDataBinder binder, NativeWebRequest webRequest)
/* 806:    */     throws Exception
/* 807:    */   {
/* 808:815 */     ((WebRequestDataBinder)binder).bind(webRequest);
/* 809:    */   }
/* 810:    */   
/* 811:    */   protected HttpInputMessage createHttpInputMessage(NativeWebRequest webRequest)
/* 812:    */     throws Exception
/* 813:    */   {
/* 814:823 */     throw new UnsupportedOperationException("@RequestBody not supported");
/* 815:    */   }
/* 816:    */   
/* 817:    */   protected HttpOutputMessage createHttpOutputMessage(NativeWebRequest webRequest)
/* 818:    */     throws Exception
/* 819:    */   {
/* 820:831 */     throw new UnsupportedOperationException("@Body not supported");
/* 821:    */   }
/* 822:    */   
/* 823:    */   protected String parseDefaultValueAttribute(String value)
/* 824:    */   {
/* 825:835 */     return "\n\t\t\n\t\t\n\n\t\t\t\t\n".equals(value) ? null : value;
/* 826:    */   }
/* 827:    */   
/* 828:    */   protected Object resolveDefaultValue(String value)
/* 829:    */   {
/* 830:839 */     return value;
/* 831:    */   }
/* 832:    */   
/* 833:    */   protected Object resolveCommonArgument(MethodParameter methodParameter, NativeWebRequest webRequest)
/* 834:    */     throws Exception
/* 835:    */   {
/* 836:846 */     if (this.customArgumentResolvers != null) {
/* 837:847 */       for (WebArgumentResolver argumentResolver : this.customArgumentResolvers)
/* 838:    */       {
/* 839:848 */         Object value = argumentResolver.resolveArgument(methodParameter, webRequest);
/* 840:849 */         if (value != WebArgumentResolver.UNRESOLVED) {
/* 841:850 */           return value;
/* 842:    */         }
/* 843:    */       }
/* 844:    */     }
/* 845:856 */     Class paramType = methodParameter.getParameterType();
/* 846:857 */     Object value = resolveStandardArgument(paramType, webRequest);
/* 847:858 */     if ((value != WebArgumentResolver.UNRESOLVED) && (!ClassUtils.isAssignableValue(paramType, value))) {
/* 848:859 */       throw new IllegalStateException("Standard argument type [" + paramType.getName() + 
/* 849:860 */         "] resolved to incompatible value of type [" + (value != null ? value.getClass() : null) + 
/* 850:861 */         "]. Consider declaring the argument type in a less specific fashion.");
/* 851:    */     }
/* 852:863 */     return value;
/* 853:    */   }
/* 854:    */   
/* 855:    */   protected Object resolveStandardArgument(Class<?> parameterType, NativeWebRequest webRequest)
/* 856:    */     throws Exception
/* 857:    */   {
/* 858:867 */     if (WebRequest.class.isAssignableFrom(parameterType)) {
/* 859:868 */       return webRequest;
/* 860:    */     }
/* 861:870 */     return WebArgumentResolver.UNRESOLVED;
/* 862:    */   }
/* 863:    */   
/* 864:    */   protected final void addReturnValueAsModelAttribute(Method handlerMethod, Class handlerType, Object returnValue, ExtendedModelMap implicitModel)
/* 865:    */   {
/* 866:876 */     ModelAttribute attr = (ModelAttribute)AnnotationUtils.findAnnotation(handlerMethod, ModelAttribute.class);
/* 867:877 */     String attrName = attr != null ? attr.value() : "";
/* 868:878 */     if ("".equals(attrName))
/* 869:    */     {
/* 870:879 */       Class resolvedType = GenericTypeResolver.resolveReturnType(handlerMethod, handlerType);
/* 871:880 */       attrName = Conventions.getVariableNameForReturnType(handlerMethod, resolvedType, returnValue);
/* 872:    */     }
/* 873:882 */     implicitModel.addAttribute(attrName, returnValue);
/* 874:    */   }
/* 875:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.annotation.support.HandlerMethodInvoker
 * JD-Core Version:    0.7.0.1
 */