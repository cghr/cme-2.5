/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import java.beans.ConstructorProperties;
/*   4:    */ import java.lang.reflect.Constructor;
/*   5:    */ import java.lang.reflect.Member;
/*   6:    */ import java.lang.reflect.Method;
/*   7:    */ import java.lang.reflect.Modifier;
/*   8:    */ import java.security.AccessController;
/*   9:    */ import java.security.PrivilegedAction;
/*  10:    */ import java.util.ArrayList;
/*  11:    */ import java.util.Arrays;
/*  12:    */ import java.util.HashSet;
/*  13:    */ import java.util.LinkedHashSet;
/*  14:    */ import java.util.LinkedList;
/*  15:    */ import java.util.List;
/*  16:    */ import java.util.Map;
/*  17:    */ import java.util.Map.Entry;
/*  18:    */ import java.util.Set;
/*  19:    */ import org.apache.commons.logging.Log;
/*  20:    */ import org.springframework.beans.BeanMetadataElement;
/*  21:    */ import org.springframework.beans.BeanWrapper;
/*  22:    */ import org.springframework.beans.BeanWrapperImpl;
/*  23:    */ import org.springframework.beans.BeansException;
/*  24:    */ import org.springframework.beans.TypeConverter;
/*  25:    */ import org.springframework.beans.TypeMismatchException;
/*  26:    */ import org.springframework.beans.factory.BeanCreationException;
/*  27:    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*  28:    */ import org.springframework.beans.factory.UnsatisfiedDependencyException;
/*  29:    */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*  30:    */ import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
/*  31:    */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*  32:    */ import org.springframework.core.GenericTypeResolver;
/*  33:    */ import org.springframework.core.MethodParameter;
/*  34:    */ import org.springframework.core.ParameterNameDiscoverer;
/*  35:    */ import org.springframework.util.ClassUtils;
/*  36:    */ import org.springframework.util.MethodInvoker;
/*  37:    */ import org.springframework.util.ObjectUtils;
/*  38:    */ import org.springframework.util.ReflectionUtils;
/*  39:    */ import org.springframework.util.StringUtils;
/*  40:    */ 
/*  41:    */ class ConstructorResolver
/*  42:    */ {
/*  43:    */   private static final String CONSTRUCTOR_PROPERTIES_CLASS_NAME = "java.beans.ConstructorProperties";
/*  44: 77 */   private static final boolean constructorPropertiesAnnotationAvailable = ClassUtils.isPresent("java.beans.ConstructorProperties", ConstructorResolver.class.getClassLoader());
/*  45:    */   private final AbstractAutowireCapableBeanFactory beanFactory;
/*  46:    */   
/*  47:    */   public ConstructorResolver(AbstractAutowireCapableBeanFactory beanFactory)
/*  48:    */   {
/*  49: 87 */     this.beanFactory = beanFactory;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public BeanWrapper autowireConstructor(final String beanName, final RootBeanDefinition mbd, Constructor[] chosenCtors, Object[] explicitArgs)
/*  53:    */   {
/*  54:108 */     BeanWrapperImpl bw = new BeanWrapperImpl();
/*  55:109 */     this.beanFactory.initBeanWrapper(bw);
/*  56:    */     
/*  57:111 */     Constructor constructorToUse = null;
/*  58:112 */     ArgumentsHolder argsHolderToUse = null;
/*  59:113 */     Object[] argsToUse = (Object[])null;
/*  60:115 */     if (explicitArgs != null)
/*  61:    */     {
/*  62:116 */       argsToUse = explicitArgs;
/*  63:    */     }
/*  64:    */     else
/*  65:    */     {
/*  66:119 */       Object[] argsToResolve = (Object[])null;
/*  67:120 */       synchronized (mbd.constructorArgumentLock)
/*  68:    */       {
/*  69:121 */         constructorToUse = (Constructor)mbd.resolvedConstructorOrFactoryMethod;
/*  70:122 */         if ((constructorToUse != null) && (mbd.constructorArgumentsResolved))
/*  71:    */         {
/*  72:124 */           argsToUse = mbd.resolvedConstructorArguments;
/*  73:125 */           if (argsToUse == null) {
/*  74:126 */             argsToResolve = mbd.preparedConstructorArguments;
/*  75:    */           }
/*  76:    */         }
/*  77:    */       }
/*  78:130 */       if (argsToResolve != null) {
/*  79:131 */         argsToUse = resolvePreparedArguments(beanName, mbd, bw, constructorToUse, argsToResolve);
/*  80:    */       }
/*  81:    */     }
/*  82:135 */     if (constructorToUse == null)
/*  83:    */     {
/*  84:137 */       boolean autowiring = (chosenCtors != null) || 
/*  85:138 */         (mbd.getResolvedAutowireMode() == 3);
/*  86:139 */       ConstructorArgumentValues resolvedValues = null;
/*  87:    */       int minNrOfArgs;
/*  88:    */       int minNrOfArgs;
/*  89:142 */       if (explicitArgs != null)
/*  90:    */       {
/*  91:143 */         minNrOfArgs = explicitArgs.length;
/*  92:    */       }
/*  93:    */       else
/*  94:    */       {
/*  95:146 */         ConstructorArgumentValues cargs = mbd.getConstructorArgumentValues();
/*  96:147 */         resolvedValues = new ConstructorArgumentValues();
/*  97:148 */         minNrOfArgs = resolveConstructorArguments(beanName, mbd, bw, cargs, resolvedValues);
/*  98:    */       }
/*  99:152 */       Constructor[] candidates = chosenCtors;
/* 100:153 */       if (candidates == null)
/* 101:    */       {
/* 102:154 */         Class beanClass = mbd.getBeanClass();
/* 103:    */         try
/* 104:    */         {
/* 105:156 */           candidates = mbd.isNonPublicAccessAllowed() ? 
/* 106:157 */             beanClass.getDeclaredConstructors() : beanClass.getConstructors();
/* 107:    */         }
/* 108:    */         catch (Throwable ex)
/* 109:    */         {
/* 110:160 */           throw new BeanCreationException(mbd.getResourceDescription(), beanName, 
/* 111:161 */             "Resolution of declared constructors on bean Class [" + beanClass.getName() + 
/* 112:162 */             "] from ClassLoader [" + beanClass.getClassLoader() + "] failed", ex);
/* 113:    */         }
/* 114:    */       }
/* 115:165 */       AutowireUtils.sortConstructors(candidates);
/* 116:166 */       int minTypeDiffWeight = 2147483647;
/* 117:167 */       Set<Constructor> ambiguousConstructors = null;
/* 118:168 */       List<Exception> causes = null;
/* 119:170 */       for (int i = 0; i < candidates.length; i++)
/* 120:    */       {
/* 121:171 */         Constructor<?> candidate = candidates[i];
/* 122:172 */         Class[] paramTypes = candidate.getParameterTypes();
/* 123:174 */         if ((constructorToUse != null) && (argsToUse.length > paramTypes.length)) {
/* 124:    */           break;
/* 125:    */         }
/* 126:179 */         if (paramTypes.length >= minNrOfArgs)
/* 127:    */         {
/* 128:    */           ArgumentsHolder argsHolder;
/* 129:184 */           if (resolvedValues != null)
/* 130:    */           {
/* 131:    */             try
/* 132:    */             {
/* 133:186 */               String[] paramNames = (String[])null;
/* 134:187 */               if (constructorPropertiesAnnotationAvailable) {
/* 135:188 */                 paramNames = ConstructorPropertiesChecker.evaluateAnnotation(candidate, paramTypes.length);
/* 136:    */               }
/* 137:190 */               if (paramNames == null)
/* 138:    */               {
/* 139:191 */                 ParameterNameDiscoverer pnd = this.beanFactory.getParameterNameDiscoverer();
/* 140:192 */                 if (pnd != null) {
/* 141:193 */                   paramNames = pnd.getParameterNames(candidate);
/* 142:    */                 }
/* 143:    */               }
/* 144:196 */               argsHolder = createArgumentArray(
/* 145:197 */                 beanName, mbd, resolvedValues, bw, paramTypes, paramNames, candidate, autowiring);
/* 146:    */             }
/* 147:    */             catch (UnsatisfiedDependencyException ex)
/* 148:    */             {
/* 149:    */               ArgumentsHolder argsHolder;
/* 150:200 */               if (this.beanFactory.logger.isTraceEnabled()) {
/* 151:201 */                 this.beanFactory.logger.trace(
/* 152:202 */                   "Ignoring constructor [" + candidate + "] of bean '" + beanName + "': " + ex);
/* 153:    */               }
/* 154:204 */               if ((i == candidates.length - 1) && (constructorToUse == null))
/* 155:    */               {
/* 156:205 */                 if (causes != null) {
/* 157:206 */                   for (Exception cause : causes) {
/* 158:207 */                     this.beanFactory.onSuppressedException(cause);
/* 159:    */                   }
/* 160:    */                 }
/* 161:210 */                 throw ex;
/* 162:    */               }
/* 163:214 */               if (causes == null) {
/* 164:215 */                 causes = new LinkedList();
/* 165:    */               }
/* 166:217 */               causes.add(ex);
/* 167:218 */               continue;
/* 168:    */             }
/* 169:    */           }
/* 170:    */           else
/* 171:    */           {
/* 172:224 */             if (paramTypes.length != explicitArgs.length) {
/* 173:    */               continue;
/* 174:    */             }
/* 175:227 */             argsHolder = new ArgumentsHolder(explicitArgs);
/* 176:    */           }
/* 177:230 */           int typeDiffWeight = mbd.isLenientConstructorResolution() ? 
/* 178:231 */             argsHolder.getTypeDifferenceWeight(paramTypes) : argsHolder.getAssignabilityWeight(paramTypes);
/* 179:233 */           if (typeDiffWeight < minTypeDiffWeight)
/* 180:    */           {
/* 181:234 */             constructorToUse = candidate;
/* 182:235 */             argsHolderToUse = argsHolder;
/* 183:236 */             argsToUse = argsHolder.arguments;
/* 184:237 */             minTypeDiffWeight = typeDiffWeight;
/* 185:238 */             ambiguousConstructors = null;
/* 186:    */           }
/* 187:240 */           else if ((constructorToUse != null) && (typeDiffWeight == minTypeDiffWeight))
/* 188:    */           {
/* 189:241 */             if (ambiguousConstructors == null)
/* 190:    */             {
/* 191:242 */               ambiguousConstructors = new LinkedHashSet();
/* 192:243 */               ambiguousConstructors.add(constructorToUse);
/* 193:    */             }
/* 194:245 */             ambiguousConstructors.add(candidate);
/* 195:    */           }
/* 196:    */         }
/* 197:    */       }
/* 198:249 */       if (constructorToUse == null) {
/* 199:250 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, 
/* 200:251 */           "Could not resolve matching constructor (hint: specify index/type/name arguments for simple parameters to avoid type ambiguities)");
/* 201:    */       }
/* 202:254 */       if ((ambiguousConstructors != null) && (!mbd.isLenientConstructorResolution())) {
/* 203:255 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, 
/* 204:256 */           "Ambiguous constructor matches found in bean '" + beanName + "' " + 
/* 205:257 */           "(hint: specify index/type/name arguments for simple parameters to avoid type ambiguities): " + 
/* 206:258 */           ambiguousConstructors);
/* 207:    */       }
/* 208:261 */       if (explicitArgs == null) {
/* 209:262 */         argsHolderToUse.storeCache(mbd, constructorToUse);
/* 210:    */       }
/* 211:    */     }
/* 212:    */     try
/* 213:    */     {
/* 214:    */       Object beanInstance;
/* 215:    */       Object beanInstance;
/* 216:269 */       if (System.getSecurityManager() != null)
/* 217:    */       {
/* 218:270 */         final Constructor ctorToUse = constructorToUse;
/* 219:271 */         final Object[] argumentsToUse = argsToUse;
/* 220:272 */         beanInstance = AccessController.doPrivileged(new PrivilegedAction()
/* 221:    */         {
/* 222:    */           public Object run()
/* 223:    */           {
/* 224:274 */             return ConstructorResolver.this.beanFactory.getInstantiationStrategy().instantiate(
/* 225:275 */               mbd, beanName, ConstructorResolver.this.beanFactory, ctorToUse, argumentsToUse);
/* 226:    */           }
/* 227:277 */         }, this.beanFactory.getAccessControlContext());
/* 228:    */       }
/* 229:    */       else
/* 230:    */       {
/* 231:280 */         beanInstance = this.beanFactory.getInstantiationStrategy().instantiate(
/* 232:281 */           mbd, beanName, this.beanFactory, constructorToUse, argsToUse);
/* 233:    */       }
/* 234:284 */       bw.setWrappedInstance(beanInstance);
/* 235:285 */       return bw;
/* 236:    */     }
/* 237:    */     catch (Throwable ex)
/* 238:    */     {
/* 239:288 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Instantiation of bean failed", ex);
/* 240:    */     }
/* 241:    */   }
/* 242:    */   
/* 243:    */   public void resolveFactoryMethodIfPossible(RootBeanDefinition mbd)
/* 244:    */   {
/* 245:    */     Class factoryClass;
/* 246:299 */     if (mbd.getFactoryBeanName() != null) {
/* 247:300 */       factoryClass = this.beanFactory.getType(mbd.getFactoryBeanName());
/* 248:    */     } else {
/* 249:303 */       factoryClass = mbd.getBeanClass();
/* 250:    */     }
/* 251:305 */     Class factoryClass = ClassUtils.getUserClass(factoryClass);
/* 252:306 */     Method[] candidates = ReflectionUtils.getAllDeclaredMethods(factoryClass);
/* 253:307 */     Method uniqueCandidate = null;
/* 254:308 */     for (Method candidate : candidates) {
/* 255:309 */       if (mbd.isFactoryMethod(candidate)) {
/* 256:310 */         if (uniqueCandidate == null)
/* 257:    */         {
/* 258:311 */           uniqueCandidate = candidate;
/* 259:    */         }
/* 260:313 */         else if (!Arrays.equals(uniqueCandidate.getParameterTypes(), candidate.getParameterTypes()))
/* 261:    */         {
/* 262:314 */           uniqueCandidate = null;
/* 263:315 */           break;
/* 264:    */         }
/* 265:    */       }
/* 266:    */     }
/* 267:319 */     synchronized (mbd.constructorArgumentLock)
/* 268:    */     {
/* 269:320 */       mbd.resolvedConstructorOrFactoryMethod = uniqueCandidate;
/* 270:    */     }
/* 271:    */   }
/* 272:    */   
/* 273:    */   public BeanWrapper instantiateUsingFactoryMethod(final String beanName, final RootBeanDefinition mbd, Object[] explicitArgs)
/* 274:    */   {
/* 275:340 */     BeanWrapperImpl bw = new BeanWrapperImpl();
/* 276:341 */     this.beanFactory.initBeanWrapper(bw);
/* 277:    */     
/* 278:    */ 
/* 279:    */ 
/* 280:    */ 
/* 281:    */ 
/* 282:347 */     String factoryBeanName = mbd.getFactoryBeanName();
/* 283:    */     boolean isStatic;
/* 284:    */     Object factoryBean;
/* 285:    */     Class factoryClass;
/* 286:    */     boolean isStatic;
/* 287:348 */     if (factoryBeanName != null)
/* 288:    */     {
/* 289:349 */       if (factoryBeanName.equals(beanName)) {
/* 290:350 */         throw new BeanDefinitionStoreException(mbd.getResourceDescription(), beanName, 
/* 291:351 */           "factory-bean reference points back to the same bean definition");
/* 292:    */       }
/* 293:353 */       Object factoryBean = this.beanFactory.getBean(factoryBeanName);
/* 294:354 */       if (factoryBean == null) {
/* 295:355 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, 
/* 296:356 */           "factory-bean '" + factoryBeanName + "' returned null");
/* 297:    */       }
/* 298:358 */       Class factoryClass = factoryBean.getClass();
/* 299:359 */       isStatic = false;
/* 300:    */     }
/* 301:    */     else
/* 302:    */     {
/* 303:363 */       if (!mbd.hasBeanClass()) {
/* 304:364 */         throw new BeanDefinitionStoreException(mbd.getResourceDescription(), beanName, 
/* 305:365 */           "bean definition declares neither a bean class nor a factory-bean reference");
/* 306:    */       }
/* 307:367 */       factoryBean = null;
/* 308:368 */       factoryClass = mbd.getBeanClass();
/* 309:369 */       isStatic = true;
/* 310:    */     }
/* 311:372 */     Method factoryMethodToUse = null;
/* 312:373 */     ArgumentsHolder argsHolderToUse = null;
/* 313:374 */     Object[] argsToUse = (Object[])null;
/* 314:376 */     if (explicitArgs != null)
/* 315:    */     {
/* 316:377 */       argsToUse = explicitArgs;
/* 317:    */     }
/* 318:    */     else
/* 319:    */     {
/* 320:380 */       Object[] argsToResolve = (Object[])null;
/* 321:381 */       synchronized (mbd.constructorArgumentLock)
/* 322:    */       {
/* 323:382 */         factoryMethodToUse = (Method)mbd.resolvedConstructorOrFactoryMethod;
/* 324:383 */         if ((factoryMethodToUse != null) && (mbd.constructorArgumentsResolved))
/* 325:    */         {
/* 326:385 */           argsToUse = mbd.resolvedConstructorArguments;
/* 327:386 */           if (argsToUse == null) {
/* 328:387 */             argsToResolve = mbd.preparedConstructorArguments;
/* 329:    */           }
/* 330:    */         }
/* 331:    */       }
/* 332:391 */       if (argsToResolve != null) {
/* 333:392 */         argsToUse = resolvePreparedArguments(beanName, mbd, bw, factoryMethodToUse, argsToResolve);
/* 334:    */       }
/* 335:    */     }
/* 336:396 */     if ((factoryMethodToUse == null) || (argsToUse == null))
/* 337:    */     {
/* 338:399 */       factoryClass = ClassUtils.getUserClass(factoryClass);
/* 339:    */       
/* 340:    */ 
/* 341:402 */       final Class factoryClazz = factoryClass;
/* 342:    */       Method[] rawCandidates;
/* 343:    */       Method[] rawCandidates;
/* 344:403 */       if (System.getSecurityManager() != null) {
/* 345:404 */         rawCandidates = (Method[])AccessController.doPrivileged(new PrivilegedAction()
/* 346:    */         {
/* 347:    */           public Method[] run()
/* 348:    */           {
/* 349:406 */             return mbd.isNonPublicAccessAllowed() ? 
/* 350:407 */               ReflectionUtils.getAllDeclaredMethods(factoryClazz) : factoryClazz.getMethods();
/* 351:    */           }
/* 352:    */         });
/* 353:    */       } else {
/* 354:412 */         rawCandidates = mbd.isNonPublicAccessAllowed() ? 
/* 355:413 */           ReflectionUtils.getAllDeclaredMethods(factoryClazz) : factoryClazz.getMethods();
/* 356:    */       }
/* 357:416 */       List<Method> candidateSet = new ArrayList();
/* 358:417 */       for (Method candidate : rawCandidates) {
/* 359:418 */         if ((Modifier.isStatic(candidate.getModifiers()) == isStatic) && 
/* 360:419 */           (candidate.getName().equals(mbd.getFactoryMethodName())) && 
/* 361:420 */           (mbd.isFactoryMethod(candidate))) {
/* 362:421 */           candidateSet.add(candidate);
/* 363:    */         }
/* 364:    */       }
/* 365:424 */       Method[] candidates = (Method[])candidateSet.toArray(new Method[candidateSet.size()]);
/* 366:425 */       AutowireUtils.sortFactoryMethods(candidates);
/* 367:    */       
/* 368:427 */       ConstructorArgumentValues resolvedValues = null;
/* 369:428 */       boolean autowiring = mbd.getResolvedAutowireMode() == 3;
/* 370:429 */       int minTypeDiffWeight = 2147483647;
/* 371:430 */       Set<Method> ambiguousFactoryMethods = null;
/* 372:    */       int minNrOfArgs;
/* 373:    */       int minNrOfArgs;
/* 374:433 */       if (explicitArgs != null)
/* 375:    */       {
/* 376:434 */         minNrOfArgs = explicitArgs.length;
/* 377:    */       }
/* 378:    */       else
/* 379:    */       {
/* 380:439 */         ConstructorArgumentValues cargs = mbd.getConstructorArgumentValues();
/* 381:440 */         resolvedValues = new ConstructorArgumentValues();
/* 382:441 */         minNrOfArgs = resolveConstructorArguments(beanName, mbd, bw, cargs, resolvedValues);
/* 383:    */       }
/* 384:444 */       List<Exception> causes = null;
/* 385:    */       int typeDiffWeight;
/* 386:446 */       for (int i = 0; i < candidates.length; i++)
/* 387:    */       {
/* 388:447 */         Method candidate = candidates[i];
/* 389:448 */         Class[] paramTypes = candidate.getParameterTypes();
/* 390:450 */         if (paramTypes.length >= minNrOfArgs)
/* 391:    */         {
/* 392:    */           ArgumentsHolder argsHolder;
/* 393:453 */           if (resolvedValues != null)
/* 394:    */           {
/* 395:    */             try
/* 396:    */             {
/* 397:456 */               String[] paramNames = (String[])null;
/* 398:457 */               ParameterNameDiscoverer pnd = this.beanFactory.getParameterNameDiscoverer();
/* 399:458 */               if (pnd != null) {
/* 400:459 */                 paramNames = pnd.getParameterNames(candidate);
/* 401:    */               }
/* 402:461 */               argsHolder = createArgumentArray(
/* 403:462 */                 beanName, mbd, resolvedValues, bw, paramTypes, paramNames, candidate, autowiring);
/* 404:    */             }
/* 405:    */             catch (UnsatisfiedDependencyException ex)
/* 406:    */             {
/* 407:    */               ArgumentsHolder argsHolder;
/* 408:465 */               if (this.beanFactory.logger.isTraceEnabled()) {
/* 409:466 */                 this.beanFactory.logger.trace("Ignoring factory method [" + candidate + 
/* 410:467 */                   "] of bean '" + beanName + "': " + ex);
/* 411:    */               }
/* 412:469 */               if ((i == candidates.length - 1) && (argsHolderToUse == null))
/* 413:    */               {
/* 414:470 */                 if (causes != null) {
/* 415:471 */                   for (Exception cause : causes) {
/* 416:472 */                     this.beanFactory.onSuppressedException(cause);
/* 417:    */                   }
/* 418:    */                 }
/* 419:475 */                 throw ex;
/* 420:    */               }
/* 421:479 */               if (causes == null) {
/* 422:480 */                 causes = new LinkedList();
/* 423:    */               }
/* 424:482 */               causes.add(ex);
/* 425:483 */               continue;
/* 426:    */             }
/* 427:    */           }
/* 428:    */           else
/* 429:    */           {
/* 430:490 */             if (paramTypes.length != explicitArgs.length) {
/* 431:    */               continue;
/* 432:    */             }
/* 433:493 */             argsHolder = new ArgumentsHolder(explicitArgs);
/* 434:    */           }
/* 435:496 */           typeDiffWeight = mbd.isLenientConstructorResolution() ? 
/* 436:497 */             argsHolder.getTypeDifferenceWeight(paramTypes) : argsHolder.getAssignabilityWeight(paramTypes);
/* 437:499 */           if (typeDiffWeight < minTypeDiffWeight)
/* 438:    */           {
/* 439:500 */             factoryMethodToUse = candidate;
/* 440:501 */             argsHolderToUse = argsHolder;
/* 441:502 */             argsToUse = argsHolder.arguments;
/* 442:503 */             minTypeDiffWeight = typeDiffWeight;
/* 443:504 */             ambiguousFactoryMethods = null;
/* 444:    */           }
/* 445:506 */           else if ((factoryMethodToUse != null) && (typeDiffWeight == minTypeDiffWeight))
/* 446:    */           {
/* 447:507 */             if (ambiguousFactoryMethods == null)
/* 448:    */             {
/* 449:508 */               ambiguousFactoryMethods = new LinkedHashSet();
/* 450:509 */               ambiguousFactoryMethods.add(factoryMethodToUse);
/* 451:    */             }
/* 452:511 */             ambiguousFactoryMethods.add(candidate);
/* 453:    */           }
/* 454:    */         }
/* 455:    */       }
/* 456:516 */       if (factoryMethodToUse == null)
/* 457:    */       {
/* 458:517 */         boolean hasArgs = resolvedValues.getArgumentCount() > 0;
/* 459:518 */         String argDesc = "";
/* 460:519 */         if (hasArgs)
/* 461:    */         {
/* 462:520 */           List<String> argTypes = new ArrayList();
/* 463:521 */           for (ConstructorArgumentValues.ValueHolder value : resolvedValues.getIndexedArgumentValues().values())
/* 464:    */           {
/* 465:522 */             String argType = value.getType() != null ? 
/* 466:523 */               ClassUtils.getShortName(value.getType()) : value.getValue().getClass().getSimpleName();
/* 467:524 */             argTypes.add(argType);
/* 468:    */           }
/* 469:526 */           argDesc = StringUtils.collectionToCommaDelimitedString(argTypes);
/* 470:    */         }
/* 471:528 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, 
/* 472:529 */           "No matching factory method found: " + (
/* 473:530 */           mbd.getFactoryBeanName() != null ? 
/* 474:531 */           "factory bean '" + mbd.getFactoryBeanName() + "'; " : "") + 
/* 475:532 */           "factory method '" + mbd.getFactoryMethodName() + "(" + argDesc + ")'. " + 
/* 476:533 */           "Check that a method with the specified name " + (
/* 477:534 */           hasArgs ? "and arguments " : "") + 
/* 478:535 */           "exists and that it is " + (
/* 479:536 */           isStatic ? "static" : "non-static") + ".");
/* 480:    */       }
/* 481:538 */       if (Void.TYPE.equals(factoryMethodToUse.getReturnType())) {
/* 482:539 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, 
/* 483:540 */           "Invalid factory method '" + mbd.getFactoryMethodName() + 
/* 484:541 */           "': needs to have a non-void return type!");
/* 485:    */       }
/* 486:543 */       if ((ambiguousFactoryMethods != null) && (!mbd.isLenientConstructorResolution())) {
/* 487:544 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, 
/* 488:545 */           "Ambiguous factory method matches found in bean '" + beanName + "' " + 
/* 489:546 */           "(hint: specify index/type/name arguments for simple parameters to avoid type ambiguities): " + 
/* 490:547 */           ambiguousFactoryMethods);
/* 491:    */       }
/* 492:550 */       if ((explicitArgs == null) && (argsHolderToUse != null)) {
/* 493:551 */         argsHolderToUse.storeCache(mbd, factoryMethodToUse);
/* 494:    */       }
/* 495:    */     }
/* 496:    */     try
/* 497:    */     {
/* 498:    */       Object beanInstance;
/* 499:    */       Object beanInstance;
/* 500:558 */       if (System.getSecurityManager() != null)
/* 501:    */       {
/* 502:559 */         final Object fb = factoryBean;
/* 503:560 */         final Method factoryMethod = factoryMethodToUse;
/* 504:561 */         final Object[] args = argsToUse;
/* 505:562 */         beanInstance = AccessController.doPrivileged(new PrivilegedAction()
/* 506:    */         {
/* 507:    */           public Object run()
/* 508:    */           {
/* 509:564 */             return ConstructorResolver.this.beanFactory.getInstantiationStrategy().instantiate(
/* 510:565 */               mbd, beanName, ConstructorResolver.this.beanFactory, fb, factoryMethod, args);
/* 511:    */           }
/* 512:567 */         }, this.beanFactory.getAccessControlContext());
/* 513:    */       }
/* 514:    */       else
/* 515:    */       {
/* 516:570 */         beanInstance = this.beanFactory.getInstantiationStrategy().instantiate(
/* 517:571 */           mbd, beanName, this.beanFactory, factoryBean, factoryMethodToUse, argsToUse);
/* 518:    */       }
/* 519:574 */       if (beanInstance == null) {
/* 520:575 */         return null;
/* 521:    */       }
/* 522:577 */       bw.setWrappedInstance(beanInstance);
/* 523:578 */       return bw;
/* 524:    */     }
/* 525:    */     catch (Throwable ex)
/* 526:    */     {
/* 527:581 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Instantiation of bean failed", ex);
/* 528:    */     }
/* 529:    */   }
/* 530:    */   
/* 531:    */   private int resolveConstructorArguments(String beanName, RootBeanDefinition mbd, BeanWrapper bw, ConstructorArgumentValues cargs, ConstructorArgumentValues resolvedValues)
/* 532:    */   {
/* 533:594 */     TypeConverter converter = this.beanFactory.getCustomTypeConverter() != null ? 
/* 534:595 */       this.beanFactory.getCustomTypeConverter() : bw;
/* 535:596 */     BeanDefinitionValueResolver valueResolver = 
/* 536:597 */       new BeanDefinitionValueResolver(this.beanFactory, beanName, mbd, converter);
/* 537:    */     
/* 538:599 */     int minNrOfArgs = cargs.getArgumentCount();
/* 539:601 */     for (Map.Entry<Integer, ConstructorArgumentValues.ValueHolder> entry : cargs.getIndexedArgumentValues().entrySet())
/* 540:    */     {
/* 541:602 */       int index = ((Integer)entry.getKey()).intValue();
/* 542:603 */       if (index < 0) {
/* 543:604 */         throw new BeanCreationException(mbd.getResourceDescription(), beanName, 
/* 544:605 */           "Invalid constructor argument index: " + index);
/* 545:    */       }
/* 546:607 */       if (index > minNrOfArgs) {
/* 547:608 */         minNrOfArgs = index + 1;
/* 548:    */       }
/* 549:610 */       ConstructorArgumentValues.ValueHolder valueHolder = (ConstructorArgumentValues.ValueHolder)entry.getValue();
/* 550:611 */       if (valueHolder.isConverted())
/* 551:    */       {
/* 552:612 */         resolvedValues.addIndexedArgumentValue(index, valueHolder);
/* 553:    */       }
/* 554:    */       else
/* 555:    */       {
/* 556:615 */         Object resolvedValue = 
/* 557:616 */           valueResolver.resolveValueIfNecessary("constructor argument", valueHolder.getValue());
/* 558:617 */         ConstructorArgumentValues.ValueHolder resolvedValueHolder = 
/* 559:618 */           new ConstructorArgumentValues.ValueHolder(resolvedValue, valueHolder.getType(), valueHolder.getName());
/* 560:619 */         resolvedValueHolder.setSource(valueHolder);
/* 561:620 */         resolvedValues.addIndexedArgumentValue(index, resolvedValueHolder);
/* 562:    */       }
/* 563:    */     }
/* 564:624 */     for (ConstructorArgumentValues.ValueHolder valueHolder : cargs.getGenericArgumentValues()) {
/* 565:625 */       if (valueHolder.isConverted())
/* 566:    */       {
/* 567:626 */         resolvedValues.addGenericArgumentValue(valueHolder);
/* 568:    */       }
/* 569:    */       else
/* 570:    */       {
/* 571:629 */         Object resolvedValue = 
/* 572:630 */           valueResolver.resolveValueIfNecessary("constructor argument", valueHolder.getValue());
/* 573:631 */         ConstructorArgumentValues.ValueHolder resolvedValueHolder = 
/* 574:632 */           new ConstructorArgumentValues.ValueHolder(resolvedValue, valueHolder.getType(), valueHolder.getName());
/* 575:633 */         resolvedValueHolder.setSource(valueHolder);
/* 576:634 */         resolvedValues.addGenericArgumentValue(resolvedValueHolder);
/* 577:    */       }
/* 578:    */     }
/* 579:638 */     return minNrOfArgs;
/* 580:    */   }
/* 581:    */   
/* 582:    */   private ArgumentsHolder createArgumentArray(String beanName, RootBeanDefinition mbd, ConstructorArgumentValues resolvedValues, BeanWrapper bw, Class[] paramTypes, String[] paramNames, Object methodOrCtor, boolean autowiring)
/* 583:    */     throws UnsatisfiedDependencyException
/* 584:    */   {
/* 585:650 */     String methodType = (methodOrCtor instanceof Constructor) ? "constructor" : "factory method";
/* 586:651 */     TypeConverter converter = this.beanFactory.getCustomTypeConverter() != null ? 
/* 587:652 */       this.beanFactory.getCustomTypeConverter() : bw;
/* 588:    */     
/* 589:654 */     ArgumentsHolder args = new ArgumentsHolder(paramTypes.length);
/* 590:655 */     Set<ConstructorArgumentValues.ValueHolder> usedValueHolders = 
/* 591:656 */       new HashSet(paramTypes.length);
/* 592:657 */     Set<String> autowiredBeanNames = new LinkedHashSet(4);
/* 593:    */     Class<?> paramType;
/* 594:659 */     for (int paramIndex = 0; paramIndex < paramTypes.length; paramIndex++)
/* 595:    */     {
/* 596:660 */       paramType = paramTypes[paramIndex];
/* 597:661 */       String paramName = paramNames != null ? paramNames[paramIndex] : null;
/* 598:    */       
/* 599:663 */       ConstructorArgumentValues.ValueHolder valueHolder = 
/* 600:664 */         resolvedValues.getArgumentValue(paramIndex, paramType, paramName, usedValueHolders);
/* 601:668 */       if ((valueHolder == null) && (!autowiring)) {
/* 602:669 */         valueHolder = resolvedValues.getGenericArgumentValue(null, null, usedValueHolders);
/* 603:    */       }
/* 604:671 */       if (valueHolder != null)
/* 605:    */       {
/* 606:674 */         usedValueHolders.add(valueHolder);
/* 607:675 */         Object originalValue = valueHolder.getValue();
/* 608:677 */         if (valueHolder.isConverted())
/* 609:    */         {
/* 610:678 */           Object convertedValue = valueHolder.getConvertedValue();
/* 611:679 */           args.preparedArguments[paramIndex] = convertedValue;
/* 612:    */         }
/* 613:    */         else
/* 614:    */         {
/* 615:682 */           ConstructorArgumentValues.ValueHolder sourceHolder = 
/* 616:683 */             (ConstructorArgumentValues.ValueHolder)valueHolder.getSource();
/* 617:684 */           Object sourceValue = sourceHolder.getValue();
/* 618:    */           try
/* 619:    */           {
/* 620:686 */             Object convertedValue = converter.convertIfNecessary(originalValue, paramType, 
/* 621:687 */               MethodParameter.forMethodOrConstructor(methodOrCtor, paramIndex));
/* 622:    */             
/* 623:    */ 
/* 624:    */ 
/* 625:    */ 
/* 626:    */ 
/* 627:    */ 
/* 628:    */ 
/* 629:    */ 
/* 630:    */ 
/* 631:697 */             args.resolveNecessary = true;
/* 632:698 */             args.preparedArguments[paramIndex] = sourceValue;
/* 633:    */           }
/* 634:    */           catch (TypeMismatchException ex)
/* 635:    */           {
/* 636:702 */             throw new UnsatisfiedDependencyException(
/* 637:703 */               mbd.getResourceDescription(), beanName, paramIndex, paramType, 
/* 638:704 */               "Could not convert " + methodType + " argument value of type [" + 
/* 639:705 */               ObjectUtils.nullSafeClassName(valueHolder.getValue()) + 
/* 640:706 */               "] to required type [" + paramType.getName() + "]: " + ex.getMessage());
/* 641:    */           }
/* 642:    */         }
/* 643:    */         Object convertedValue;
/* 644:709 */         args.arguments[paramIndex] = convertedValue;
/* 645:710 */         args.rawArguments[paramIndex] = originalValue;
/* 646:    */       }
/* 647:    */       else
/* 648:    */       {
/* 649:715 */         if (!autowiring) {
/* 650:716 */           throw new UnsatisfiedDependencyException(
/* 651:717 */             mbd.getResourceDescription(), beanName, paramIndex, paramType, 
/* 652:718 */             "Ambiguous " + methodType + " argument types - " + 
/* 653:719 */             "did you specify the correct bean references as " + methodType + " arguments?");
/* 654:    */         }
/* 655:    */         try
/* 656:    */         {
/* 657:722 */           MethodParameter param = MethodParameter.forMethodOrConstructor(methodOrCtor, paramIndex);
/* 658:723 */           Object autowiredArgument = resolveAutowiredArgument(param, beanName, autowiredBeanNames, converter);
/* 659:724 */           args.rawArguments[paramIndex] = autowiredArgument;
/* 660:725 */           args.arguments[paramIndex] = autowiredArgument;
/* 661:726 */           args.preparedArguments[paramIndex] = new AutowiredArgumentMarker(null);
/* 662:727 */           args.resolveNecessary = true;
/* 663:    */         }
/* 664:    */         catch (BeansException ex)
/* 665:    */         {
/* 666:730 */           throw new UnsatisfiedDependencyException(
/* 667:731 */             mbd.getResourceDescription(), beanName, paramIndex, paramType, ex);
/* 668:    */         }
/* 669:    */       }
/* 670:    */     }
/* 671:736 */     for (String autowiredBeanName : autowiredBeanNames)
/* 672:    */     {
/* 673:737 */       this.beanFactory.registerDependentBean(autowiredBeanName, beanName);
/* 674:738 */       if (this.beanFactory.logger.isDebugEnabled()) {
/* 675:739 */         this.beanFactory.logger.debug("Autowiring by type from bean name '" + beanName + 
/* 676:740 */           "' via " + methodType + " to bean named '" + autowiredBeanName + "'");
/* 677:    */       }
/* 678:    */     }
/* 679:744 */     return args;
/* 680:    */   }
/* 681:    */   
/* 682:    */   private Object[] resolvePreparedArguments(String beanName, RootBeanDefinition mbd, BeanWrapper bw, Member methodOrCtor, Object[] argsToResolve)
/* 683:    */   {
/* 684:753 */     Class[] paramTypes = (methodOrCtor instanceof Method) ? 
/* 685:754 */       ((Method)methodOrCtor).getParameterTypes() : ((Constructor)methodOrCtor).getParameterTypes();
/* 686:755 */     TypeConverter converter = this.beanFactory.getCustomTypeConverter() != null ? 
/* 687:756 */       this.beanFactory.getCustomTypeConverter() : bw;
/* 688:757 */     BeanDefinitionValueResolver valueResolver = 
/* 689:758 */       new BeanDefinitionValueResolver(this.beanFactory, beanName, mbd, converter);
/* 690:759 */     Object[] resolvedArgs = new Object[argsToResolve.length];
/* 691:760 */     for (int argIndex = 0; argIndex < argsToResolve.length; argIndex++)
/* 692:    */     {
/* 693:761 */       Object argValue = argsToResolve[argIndex];
/* 694:762 */       MethodParameter methodParam = MethodParameter.forMethodOrConstructor(methodOrCtor, argIndex);
/* 695:763 */       GenericTypeResolver.resolveParameterType(methodParam, methodOrCtor.getDeclaringClass());
/* 696:764 */       if ((argValue instanceof AutowiredArgumentMarker)) {
/* 697:765 */         argValue = resolveAutowiredArgument(methodParam, beanName, null, converter);
/* 698:767 */       } else if ((argValue instanceof BeanMetadataElement)) {
/* 699:768 */         argValue = valueResolver.resolveValueIfNecessary("constructor argument", argValue);
/* 700:770 */       } else if ((argValue instanceof String)) {
/* 701:771 */         argValue = this.beanFactory.evaluateBeanDefinitionString((String)argValue, mbd);
/* 702:    */       }
/* 703:773 */       Class<?> paramType = paramTypes[argIndex];
/* 704:    */       try
/* 705:    */       {
/* 706:775 */         resolvedArgs[argIndex] = converter.convertIfNecessary(argValue, paramType, methodParam);
/* 707:    */       }
/* 708:    */       catch (TypeMismatchException ex)
/* 709:    */       {
/* 710:778 */         String methodType = (methodOrCtor instanceof Constructor) ? "constructor" : "factory method";
/* 711:779 */         throw new UnsatisfiedDependencyException(
/* 712:780 */           mbd.getResourceDescription(), beanName, argIndex, paramType, 
/* 713:781 */           "Could not convert " + methodType + " argument value of type [" + 
/* 714:782 */           ObjectUtils.nullSafeClassName(argValue) + 
/* 715:783 */           "] to required type [" + paramType.getName() + "]: " + ex.getMessage());
/* 716:    */       }
/* 717:    */     }
/* 718:786 */     return resolvedArgs;
/* 719:    */   }
/* 720:    */   
/* 721:    */   protected Object resolveAutowiredArgument(MethodParameter param, String beanName, Set<String> autowiredBeanNames, TypeConverter typeConverter)
/* 722:    */   {
/* 723:795 */     return this.beanFactory.resolveDependency(
/* 724:796 */       new DependencyDescriptor(param, true), beanName, autowiredBeanNames, typeConverter);
/* 725:    */   }
/* 726:    */   
/* 727:    */   private static class ArgumentsHolder
/* 728:    */   {
/* 729:    */     public final Object[] rawArguments;
/* 730:    */     public final Object[] arguments;
/* 731:    */     public final Object[] preparedArguments;
/* 732:811 */     public boolean resolveNecessary = false;
/* 733:    */     
/* 734:    */     public ArgumentsHolder(int size)
/* 735:    */     {
/* 736:814 */       this.rawArguments = new Object[size];
/* 737:815 */       this.arguments = new Object[size];
/* 738:816 */       this.preparedArguments = new Object[size];
/* 739:    */     }
/* 740:    */     
/* 741:    */     public ArgumentsHolder(Object[] args)
/* 742:    */     {
/* 743:820 */       this.rawArguments = args;
/* 744:821 */       this.arguments = args;
/* 745:822 */       this.preparedArguments = args;
/* 746:    */     }
/* 747:    */     
/* 748:    */     public int getTypeDifferenceWeight(Class[] paramTypes)
/* 749:    */     {
/* 750:830 */       int typeDiffWeight = MethodInvoker.getTypeDifferenceWeight(paramTypes, this.arguments);
/* 751:831 */       int rawTypeDiffWeight = MethodInvoker.getTypeDifferenceWeight(paramTypes, this.rawArguments) - 1024;
/* 752:832 */       return rawTypeDiffWeight < typeDiffWeight ? rawTypeDiffWeight : typeDiffWeight;
/* 753:    */     }
/* 754:    */     
/* 755:    */     public int getAssignabilityWeight(Class[] paramTypes)
/* 756:    */     {
/* 757:836 */       for (int i = 0; i < paramTypes.length; i++) {
/* 758:837 */         if (!ClassUtils.isAssignableValue(paramTypes[i], this.arguments[i])) {
/* 759:838 */           return 2147483647;
/* 760:    */         }
/* 761:    */       }
/* 762:841 */       for (int i = 0; i < paramTypes.length; i++) {
/* 763:842 */         if (!ClassUtils.isAssignableValue(paramTypes[i], this.rawArguments[i])) {
/* 764:843 */           return 2147483135;
/* 765:    */         }
/* 766:    */       }
/* 767:846 */       return 2147482623;
/* 768:    */     }
/* 769:    */     
/* 770:    */     public void storeCache(RootBeanDefinition mbd, Object constructorOrFactoryMethod)
/* 771:    */     {
/* 772:850 */       synchronized (mbd.constructorArgumentLock)
/* 773:    */       {
/* 774:851 */         mbd.resolvedConstructorOrFactoryMethod = constructorOrFactoryMethod;
/* 775:852 */         mbd.constructorArgumentsResolved = true;
/* 776:853 */         if (this.resolveNecessary) {
/* 777:854 */           mbd.preparedConstructorArguments = this.preparedArguments;
/* 778:    */         } else {
/* 779:857 */           mbd.resolvedConstructorArguments = this.arguments;
/* 780:    */         }
/* 781:    */       }
/* 782:    */     }
/* 783:    */   }
/* 784:    */   
/* 785:    */   private static class AutowiredArgumentMarker {}
/* 786:    */   
/* 787:    */   private static class ConstructorPropertiesChecker
/* 788:    */   {
/* 789:    */     public static String[] evaluateAnnotation(Constructor<?> candidate, int paramCount)
/* 790:    */     {
/* 791:877 */       ConstructorProperties cp = (ConstructorProperties)candidate.getAnnotation(ConstructorProperties.class);
/* 792:878 */       if (cp != null)
/* 793:    */       {
/* 794:879 */         String[] names = cp.value();
/* 795:880 */         if (names.length != paramCount) {
/* 796:881 */           throw new IllegalStateException("Constructor annotated with @ConstructorProperties but not corresponding to actual number of parameters (" + 
/* 797:882 */             paramCount + "): " + candidate);
/* 798:    */         }
/* 799:884 */         return names;
/* 800:    */       }
/* 801:887 */       return null;
/* 802:    */     }
/* 803:    */   }
/* 804:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.ConstructorResolver
 * JD-Core Version:    0.7.0.1
 */