/*   1:    */ package org.springframework.core.convert.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Array;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.Collections;
/*   7:    */ import java.util.HashMap;
/*   8:    */ import java.util.HashSet;
/*   9:    */ import java.util.Iterator;
/*  10:    */ import java.util.LinkedHashSet;
/*  11:    */ import java.util.LinkedList;
/*  12:    */ import java.util.List;
/*  13:    */ import java.util.Map;
/*  14:    */ import java.util.Set;
/*  15:    */ import java.util.concurrent.ConcurrentHashMap;
/*  16:    */ import org.springframework.core.GenericTypeResolver;
/*  17:    */ import org.springframework.core.convert.ConversionFailedException;
/*  18:    */ import org.springframework.core.convert.ConverterNotFoundException;
/*  19:    */ import org.springframework.core.convert.TypeDescriptor;
/*  20:    */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*  21:    */ import org.springframework.core.convert.converter.Converter;
/*  22:    */ import org.springframework.core.convert.converter.ConverterFactory;
/*  23:    */ import org.springframework.core.convert.converter.GenericConverter;
/*  24:    */ import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
/*  25:    */ import org.springframework.util.Assert;
/*  26:    */ import org.springframework.util.ClassUtils;
/*  27:    */ 
/*  28:    */ public class GenericConversionService
/*  29:    */   implements ConfigurableConversionService
/*  30:    */ {
/*  31: 58 */   private static final GenericConverter NO_OP_CONVERTER = new GenericConverter()
/*  32:    */   {
/*  33:    */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/*  34:    */     {
/*  35: 60 */       return null;
/*  36:    */     }
/*  37:    */     
/*  38:    */     public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/*  39:    */     {
/*  40: 63 */       return source;
/*  41:    */     }
/*  42:    */     
/*  43:    */     public String toString()
/*  44:    */     {
/*  45: 66 */       return "NO_OP";
/*  46:    */     }
/*  47:    */   };
/*  48: 70 */   private static final GenericConverter NO_MATCH = new GenericConverter()
/*  49:    */   {
/*  50:    */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/*  51:    */     {
/*  52: 72 */       throw new UnsupportedOperationException();
/*  53:    */     }
/*  54:    */     
/*  55:    */     public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/*  56:    */     {
/*  57: 75 */       throw new UnsupportedOperationException();
/*  58:    */     }
/*  59:    */     
/*  60:    */     public String toString()
/*  61:    */     {
/*  62: 78 */       return "NO_MATCH";
/*  63:    */     }
/*  64:    */   };
/*  65: 84 */   private final Map<Class<?>, Map<Class<?>, MatchableConverters>> converters = new HashMap(36);
/*  66: 87 */   private final Map<ConverterCacheKey, GenericConverter> converterCache = new ConcurrentHashMap();
/*  67:    */   
/*  68:    */   public void addConverter(Converter<?, ?> converter)
/*  69:    */   {
/*  70: 93 */     GenericConverter.ConvertiblePair typeInfo = getRequiredTypeInfo(converter, Converter.class);
/*  71: 94 */     if (typeInfo == null) {
/*  72: 95 */       throw new IllegalArgumentException("Unable to the determine sourceType <S> and targetType <T> which your Converter<S, T> converts between; declare these generic types.");
/*  73:    */     }
/*  74: 98 */     addConverter(new ConverterAdapter(typeInfo, converter));
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void addConverter(Class<?> sourceType, Class<?> targetType, Converter<?, ?> converter)
/*  78:    */   {
/*  79:102 */     GenericConverter.ConvertiblePair typeInfo = new GenericConverter.ConvertiblePair(sourceType, targetType);
/*  80:103 */     addConverter(new ConverterAdapter(typeInfo, converter));
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void addConverter(GenericConverter converter)
/*  84:    */   {
/*  85:107 */     Set<GenericConverter.ConvertiblePair> convertibleTypes = converter.getConvertibleTypes();
/*  86:108 */     for (GenericConverter.ConvertiblePair convertibleType : convertibleTypes) {
/*  87:109 */       getMatchableConverters(convertibleType.getSourceType(), convertibleType.getTargetType()).add(converter);
/*  88:    */     }
/*  89:111 */     invalidateCache();
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void addConverterFactory(ConverterFactory<?, ?> converterFactory)
/*  93:    */   {
/*  94:115 */     GenericConverter.ConvertiblePair typeInfo = getRequiredTypeInfo(converterFactory, ConverterFactory.class);
/*  95:116 */     if (typeInfo == null) {
/*  96:117 */       throw new IllegalArgumentException("Unable to the determine sourceType <S> and targetRangeType R which your ConverterFactory<S, R> converts between; declare these generic types.");
/*  97:    */     }
/*  98:120 */     addConverter(new ConverterFactoryAdapter(typeInfo, converterFactory));
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void removeConvertible(Class<?> sourceType, Class<?> targetType)
/* 102:    */   {
/* 103:124 */     getSourceConverterMap(sourceType).remove(targetType);
/* 104:125 */     invalidateCache();
/* 105:    */   }
/* 106:    */   
/* 107:    */   public boolean canConvert(Class<?> sourceType, Class<?> targetType)
/* 108:    */   {
/* 109:132 */     if (targetType == null) {
/* 110:133 */       throw new IllegalArgumentException("The targetType to convert to cannot be null");
/* 111:    */     }
/* 112:135 */     return canConvert(sourceType != null ? TypeDescriptor.valueOf(sourceType) : null, TypeDescriptor.valueOf(targetType));
/* 113:    */   }
/* 114:    */   
/* 115:    */   public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 116:    */   {
/* 117:139 */     if (targetType == null) {
/* 118:140 */       throw new IllegalArgumentException("The targetType to convert to cannot be null");
/* 119:    */     }
/* 120:142 */     if (sourceType == null) {
/* 121:143 */       return true;
/* 122:    */     }
/* 123:145 */     GenericConverter converter = getConverter(sourceType, targetType);
/* 124:146 */     return converter != null;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public <T> T convert(Object source, Class<T> targetType)
/* 128:    */   {
/* 129:151 */     if (targetType == null) {
/* 130:152 */       throw new IllegalArgumentException("The targetType to convert to cannot be null");
/* 131:    */     }
/* 132:154 */     return convert(source, TypeDescriptor.forObject(source), TypeDescriptor.valueOf(targetType));
/* 133:    */   }
/* 134:    */   
/* 135:    */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 136:    */   {
/* 137:158 */     if (targetType == null) {
/* 138:159 */       throw new IllegalArgumentException("The targetType to convert to cannot be null");
/* 139:    */     }
/* 140:161 */     if (sourceType == null)
/* 141:    */     {
/* 142:162 */       Assert.isTrue(source == null, "The source must be [null] if sourceType == [null]");
/* 143:163 */       return handleResult(sourceType, targetType, convertNullSource(sourceType, targetType));
/* 144:    */     }
/* 145:165 */     if ((source != null) && (!sourceType.getObjectType().isInstance(source))) {
/* 146:166 */       throw new IllegalArgumentException("The source to convert from must be an instance of " + sourceType + "; instead it was a " + source.getClass().getName());
/* 147:    */     }
/* 148:168 */     GenericConverter converter = getConverter(sourceType, targetType);
/* 149:169 */     if (converter != null) {
/* 150:170 */       return handleResult(sourceType, targetType, ConversionUtils.invokeConverter(converter, source, sourceType, targetType));
/* 151:    */     }
/* 152:172 */     return handleConverterNotFound(source, sourceType, targetType);
/* 153:    */   }
/* 154:    */   
/* 155:    */   public Object convert(Object source, TypeDescriptor targetType)
/* 156:    */   {
/* 157:187 */     return convert(source, TypeDescriptor.forObject(source), targetType);
/* 158:    */   }
/* 159:    */   
/* 160:    */   public String toString()
/* 161:    */   {
/* 162:191 */     List<String> converterStrings = new ArrayList();
/* 163:    */     Iterator localIterator2;
/* 164:    */     MatchableConverters matchable;
/* 165:192 */     for (Iterator localIterator1 = this.converters.values().iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/* 166:    */     {
/* 167:192 */       Map<Class<?>, MatchableConverters> targetConverters = (Map)localIterator1.next();
/* 168:193 */       localIterator2 = targetConverters.values().iterator(); continue;matchable = (MatchableConverters)localIterator2.next();
/* 169:194 */       converterStrings.add(matchable.toString());
/* 170:    */     }
/* 171:197 */     Collections.sort(converterStrings);
/* 172:198 */     StringBuilder builder = new StringBuilder();
/* 173:199 */     builder.append("ConversionService converters = ").append("\n");
/* 174:200 */     for (String converterString : converterStrings)
/* 175:    */     {
/* 176:201 */       builder.append("\t");
/* 177:202 */       builder.append(converterString);
/* 178:203 */       builder.append("\n");
/* 179:    */     }
/* 180:205 */     return builder.toString();
/* 181:    */   }
/* 182:    */   
/* 183:    */   protected Object convertNullSource(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 184:    */   {
/* 185:220 */     return null;
/* 186:    */   }
/* 187:    */   
/* 188:    */   protected GenericConverter getConverter(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 189:    */   {
/* 190:235 */     ConverterCacheKey key = new ConverterCacheKey(sourceType, targetType);
/* 191:236 */     GenericConverter converter = (GenericConverter)this.converterCache.get(key);
/* 192:237 */     if (converter != null) {
/* 193:238 */       return converter != NO_MATCH ? converter : null;
/* 194:    */     }
/* 195:241 */     converter = findConverterForClassPair(sourceType, targetType);
/* 196:242 */     if (converter == null) {
/* 197:243 */       converter = getDefaultConverter(sourceType, targetType);
/* 198:    */     }
/* 199:245 */     if (converter != null)
/* 200:    */     {
/* 201:246 */       this.converterCache.put(key, converter);
/* 202:247 */       return converter;
/* 203:    */     }
/* 204:250 */     this.converterCache.put(key, NO_MATCH);
/* 205:251 */     return null;
/* 206:    */   }
/* 207:    */   
/* 208:    */   protected GenericConverter getDefaultConverter(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 209:    */   {
/* 210:266 */     return sourceType.isAssignableTo(targetType) ? NO_OP_CONVERTER : null;
/* 211:    */   }
/* 212:    */   
/* 213:    */   private GenericConverter.ConvertiblePair getRequiredTypeInfo(Object converter, Class<?> genericIfc)
/* 214:    */   {
/* 215:272 */     Class[] args = GenericTypeResolver.resolveTypeArguments(converter.getClass(), genericIfc);
/* 216:273 */     return args != null ? new GenericConverter.ConvertiblePair(args[0], args[1]) : null;
/* 217:    */   }
/* 218:    */   
/* 219:    */   private MatchableConverters getMatchableConverters(Class<?> sourceType, Class<?> targetType)
/* 220:    */   {
/* 221:277 */     Map<Class<?>, MatchableConverters> sourceMap = getSourceConverterMap(sourceType);
/* 222:278 */     MatchableConverters matchable = (MatchableConverters)sourceMap.get(targetType);
/* 223:279 */     if (matchable == null)
/* 224:    */     {
/* 225:280 */       matchable = new MatchableConverters(null);
/* 226:281 */       sourceMap.put(targetType, matchable);
/* 227:    */     }
/* 228:283 */     return matchable;
/* 229:    */   }
/* 230:    */   
/* 231:    */   private void invalidateCache()
/* 232:    */   {
/* 233:287 */     this.converterCache.clear();
/* 234:    */   }
/* 235:    */   
/* 236:    */   private Map<Class<?>, MatchableConverters> getSourceConverterMap(Class<?> sourceType)
/* 237:    */   {
/* 238:291 */     Map<Class<?>, MatchableConverters> sourceMap = (Map)this.converters.get(sourceType);
/* 239:292 */     if (sourceMap == null)
/* 240:    */     {
/* 241:293 */       sourceMap = new HashMap();
/* 242:294 */       this.converters.put(sourceType, sourceMap);
/* 243:    */     }
/* 244:296 */     return sourceMap;
/* 245:    */   }
/* 246:    */   
/* 247:    */   private GenericConverter findConverterForClassPair(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 248:    */   {
/* 249:300 */     Class<?> sourceObjectType = sourceType.getObjectType();
/* 250:    */     int j;
/* 251:301 */     if (sourceObjectType.isInterface())
/* 252:    */     {
/* 253:302 */       LinkedList<Class<?>> classQueue = new LinkedList();
/* 254:303 */       classQueue.addFirst(sourceObjectType);
/* 255:    */       int i;
/* 256:304 */       for (; !classQueue.isEmpty(); i < j)
/* 257:    */       {
/* 258:305 */         Class<?> currentClass = (Class)classQueue.removeLast();
/* 259:306 */         Map<Class<?>, MatchableConverters> converters = getTargetConvertersForSource(currentClass);
/* 260:307 */         GenericConverter converter = getMatchingConverterForTarget(sourceType, targetType, converters);
/* 261:308 */         if (converter != null) {
/* 262:309 */           return converter;
/* 263:    */         }
/* 264:311 */         Class[] interfaces = currentClass.getInterfaces();
/* 265:    */         Class[] arrayOfClass1;
/* 266:312 */         j = (arrayOfClass1 = interfaces).length;i = 0; continue;Class<?> ifc = arrayOfClass1[i];
/* 267:313 */         classQueue.addFirst(ifc);i++;
/* 268:    */       }
/* 269:316 */       Map<Class<?>, MatchableConverters> objectConverters = getTargetConvertersForSource(Object.class);
/* 270:317 */       return getMatchingConverterForTarget(sourceType, targetType, objectConverters);
/* 271:    */     }
/* 272:319 */     if (sourceObjectType.isArray())
/* 273:    */     {
/* 274:320 */       LinkedList<Class<?>> classQueue = new LinkedList();
/* 275:321 */       classQueue.addFirst(sourceObjectType);
/* 276:322 */       while (!classQueue.isEmpty())
/* 277:    */       {
/* 278:323 */         Class<?> currentClass = (Class)classQueue.removeLast();
/* 279:324 */         Map<Class<?>, MatchableConverters> converters = getTargetConvertersForSource(currentClass);
/* 280:325 */         GenericConverter converter = getMatchingConverterForTarget(sourceType, targetType, converters);
/* 281:326 */         if (converter != null) {
/* 282:327 */           return converter;
/* 283:    */         }
/* 284:329 */         Class<?> componentType = ClassUtils.resolvePrimitiveIfNecessary(currentClass.getComponentType());
/* 285:330 */         if (componentType.getSuperclass() != null) {
/* 286:331 */           classQueue.addFirst(Array.newInstance(componentType.getSuperclass(), 0).getClass());
/* 287:333 */         } else if (componentType.isInterface()) {
/* 288:334 */           classQueue.addFirst([Ljava.lang.Object.class);
/* 289:    */         }
/* 290:    */       }
/* 291:337 */       return null;
/* 292:    */     }
/* 293:339 */     HashSet<Class<?>> interfaces = new LinkedHashSet();
/* 294:340 */     LinkedList<Class<?>> classQueue = new LinkedList();
/* 295:341 */     classQueue.addFirst(sourceObjectType);
/* 296:    */     Map<Class<?>, MatchableConverters> converters;
/* 297:    */     int k;
/* 298:342 */     for (; !classQueue.isEmpty(); j < k)
/* 299:    */     {
/* 300:343 */       Class<?> currentClass = (Class)classQueue.removeLast();
/* 301:344 */       converters = getTargetConvertersForSource(currentClass);
/* 302:345 */       GenericConverter converter = getMatchingConverterForTarget(sourceType, targetType, converters);
/* 303:346 */       if (converter != null) {
/* 304:347 */         return converter;
/* 305:    */       }
/* 306:349 */       Class<?> superClass = currentClass.getSuperclass();
/* 307:350 */       if ((superClass != null) && (superClass != Object.class)) {
/* 308:351 */         classQueue.addFirst(superClass);
/* 309:    */       }
/* 310:    */       Class[] arrayOfClass2;
/* 311:353 */       k = (arrayOfClass2 = currentClass.getInterfaces()).length;j = 0; continue;Class<?> interfaceType = arrayOfClass2[j];
/* 312:354 */       addInterfaceHierarchy(interfaceType, interfaces);j++;
/* 313:    */     }
/* 314:357 */     for (Class<?> interfaceType : interfaces)
/* 315:    */     {
/* 316:358 */       Map<Class<?>, MatchableConverters> converters = getTargetConvertersForSource(interfaceType);
/* 317:359 */       GenericConverter converter = getMatchingConverterForTarget(sourceType, targetType, converters);
/* 318:360 */       if (converter != null) {
/* 319:361 */         return converter;
/* 320:    */       }
/* 321:    */     }
/* 322:364 */     Map<Class<?>, MatchableConverters> objectConverters = getTargetConvertersForSource(Object.class);
/* 323:365 */     return getMatchingConverterForTarget(sourceType, targetType, objectConverters);
/* 324:    */   }
/* 325:    */   
/* 326:    */   private Map<Class<?>, MatchableConverters> getTargetConvertersForSource(Class<?> sourceType)
/* 327:    */   {
/* 328:370 */     Map<Class<?>, MatchableConverters> converters = (Map)this.converters.get(sourceType);
/* 329:371 */     if (converters == null) {
/* 330:372 */       converters = Collections.emptyMap();
/* 331:    */     }
/* 332:374 */     return converters;
/* 333:    */   }
/* 334:    */   
/* 335:    */   private GenericConverter getMatchingConverterForTarget(TypeDescriptor sourceType, TypeDescriptor targetType, Map<Class<?>, MatchableConverters> converters)
/* 336:    */   {
/* 337:379 */     Class<?> targetObjectType = targetType.getObjectType();
/* 338:    */     int j;
/* 339:380 */     if (targetObjectType.isInterface())
/* 340:    */     {
/* 341:381 */       LinkedList<Class<?>> classQueue = new LinkedList();
/* 342:382 */       classQueue.addFirst(targetObjectType);
/* 343:    */       int i;
/* 344:383 */       for (; !classQueue.isEmpty(); i < j)
/* 345:    */       {
/* 346:384 */         Class<?> currentClass = (Class)classQueue.removeLast();
/* 347:385 */         MatchableConverters matchable = (MatchableConverters)converters.get(currentClass);
/* 348:386 */         GenericConverter converter = matchConverter(matchable, sourceType, targetType);
/* 349:387 */         if (converter != null) {
/* 350:388 */           return converter;
/* 351:    */         }
/* 352:390 */         Class[] interfaces = currentClass.getInterfaces();
/* 353:    */         Class[] arrayOfClass1;
/* 354:391 */         j = (arrayOfClass1 = interfaces).length;i = 0; continue;Class<?> ifc = arrayOfClass1[i];
/* 355:392 */         classQueue.addFirst(ifc);i++;
/* 356:    */       }
/* 357:395 */       return matchConverter((MatchableConverters)converters.get(Object.class), sourceType, targetType);
/* 358:    */     }
/* 359:396 */     if (targetObjectType.isArray())
/* 360:    */     {
/* 361:397 */       LinkedList<Class<?>> classQueue = new LinkedList();
/* 362:398 */       classQueue.addFirst(targetObjectType);
/* 363:399 */       while (!classQueue.isEmpty())
/* 364:    */       {
/* 365:400 */         Class<?> currentClass = (Class)classQueue.removeLast();
/* 366:401 */         MatchableConverters matchable = (MatchableConverters)converters.get(currentClass);
/* 367:402 */         GenericConverter converter = matchConverter(matchable, sourceType, targetType);
/* 368:403 */         if (converter != null) {
/* 369:404 */           return converter;
/* 370:    */         }
/* 371:406 */         Class<?> componentType = ClassUtils.resolvePrimitiveIfNecessary(currentClass.getComponentType());
/* 372:407 */         if (componentType.getSuperclass() != null) {
/* 373:408 */           classQueue.addFirst(Array.newInstance(componentType.getSuperclass(), 0).getClass());
/* 374:410 */         } else if (componentType.isInterface()) {
/* 375:411 */           classQueue.addFirst([Ljava.lang.Object.class);
/* 376:    */         }
/* 377:    */       }
/* 378:414 */       return null;
/* 379:    */     }
/* 380:417 */     Set<Class<?>> interfaces = new LinkedHashSet();
/* 381:418 */     LinkedList<Class<?>> classQueue = new LinkedList();
/* 382:419 */     classQueue.addFirst(targetObjectType);
/* 383:    */     MatchableConverters matchable;
/* 384:    */     int k;
/* 385:420 */     for (; !classQueue.isEmpty(); j < k)
/* 386:    */     {
/* 387:421 */       Class<?> currentClass = (Class)classQueue.removeLast();
/* 388:422 */       matchable = (MatchableConverters)converters.get(currentClass);
/* 389:423 */       GenericConverter converter = matchConverter(matchable, sourceType, targetType);
/* 390:424 */       if (converter != null) {
/* 391:425 */         return converter;
/* 392:    */       }
/* 393:427 */       Class<?> superClass = currentClass.getSuperclass();
/* 394:428 */       if ((superClass != null) && (superClass != Object.class)) {
/* 395:429 */         classQueue.addFirst(superClass);
/* 396:    */       }
/* 397:    */       Class[] arrayOfClass2;
/* 398:431 */       k = (arrayOfClass2 = currentClass.getInterfaces()).length;j = 0; continue;Class<?> interfaceType = arrayOfClass2[j];
/* 399:432 */       addInterfaceHierarchy(interfaceType, interfaces);j++;
/* 400:    */     }
/* 401:435 */     for (Class<?> interfaceType : interfaces)
/* 402:    */     {
/* 403:436 */       MatchableConverters matchable = (MatchableConverters)converters.get(interfaceType);
/* 404:437 */       GenericConverter converter = matchConverter(matchable, sourceType, targetType);
/* 405:438 */       if (converter != null) {
/* 406:439 */         return converter;
/* 407:    */       }
/* 408:    */     }
/* 409:442 */     return matchConverter((MatchableConverters)converters.get(Object.class), sourceType, targetType);
/* 410:    */   }
/* 411:    */   
/* 412:    */   private void addInterfaceHierarchy(Class<?> interfaceType, Set<Class<?>> interfaces)
/* 413:    */   {
/* 414:447 */     interfaces.add(interfaceType);
/* 415:448 */     for (Class<?> inheritedInterface : interfaceType.getInterfaces()) {
/* 416:449 */       addInterfaceHierarchy(inheritedInterface, interfaces);
/* 417:    */     }
/* 418:    */   }
/* 419:    */   
/* 420:    */   private GenericConverter matchConverter(MatchableConverters matchable, TypeDescriptor sourceFieldType, TypeDescriptor targetFieldType)
/* 421:    */   {
/* 422:455 */     if (matchable == null) {
/* 423:456 */       return null;
/* 424:    */     }
/* 425:458 */     return matchable.matchConverter(sourceFieldType, targetFieldType);
/* 426:    */   }
/* 427:    */   
/* 428:    */   private Object handleConverterNotFound(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 429:    */   {
/* 430:462 */     if (source == null)
/* 431:    */     {
/* 432:463 */       assertNotPrimitiveTargetType(sourceType, targetType);
/* 433:464 */       return source;
/* 434:    */     }
/* 435:466 */     if (sourceType.isAssignableTo(targetType)) {
/* 436:467 */       return source;
/* 437:    */     }
/* 438:470 */     throw new ConverterNotFoundException(sourceType, targetType);
/* 439:    */   }
/* 440:    */   
/* 441:    */   private Object handleResult(TypeDescriptor sourceType, TypeDescriptor targetType, Object result)
/* 442:    */   {
/* 443:475 */     if (result == null) {
/* 444:476 */       assertNotPrimitiveTargetType(sourceType, targetType);
/* 445:    */     }
/* 446:478 */     return result;
/* 447:    */   }
/* 448:    */   
/* 449:    */   private void assertNotPrimitiveTargetType(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 450:    */   {
/* 451:481 */     if (targetType.isPrimitive()) {
/* 452:482 */       throw new ConversionFailedException(sourceType, targetType, null, 
/* 453:483 */         new IllegalArgumentException("A null value cannot be assigned to a primitive type"));
/* 454:    */     }
/* 455:    */   }
/* 456:    */   
/* 457:    */   private final class ConverterAdapter
/* 458:    */     implements GenericConverter
/* 459:    */   {
/* 460:    */     private final GenericConverter.ConvertiblePair typeInfo;
/* 461:    */     private final Converter<Object, Object> converter;
/* 462:    */     
/* 463:    */     public ConverterAdapter(Converter<?, ?> typeInfo)
/* 464:    */     {
/* 465:495 */       this.converter = converter;
/* 466:496 */       this.typeInfo = typeInfo;
/* 467:    */     }
/* 468:    */     
/* 469:    */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 470:    */     {
/* 471:500 */       return Collections.singleton(this.typeInfo);
/* 472:    */     }
/* 473:    */     
/* 474:    */     public boolean matchesTargetType(TypeDescriptor targetType)
/* 475:    */     {
/* 476:504 */       return this.typeInfo.getTargetType().equals(targetType.getObjectType());
/* 477:    */     }
/* 478:    */     
/* 479:    */     public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 480:    */     {
/* 481:508 */       if (source == null) {
/* 482:509 */         return GenericConversionService.this.convertNullSource(sourceType, targetType);
/* 483:    */       }
/* 484:511 */       return this.converter.convert(source);
/* 485:    */     }
/* 486:    */     
/* 487:    */     public String toString()
/* 488:    */     {
/* 489:515 */       return 
/* 490:516 */         this.typeInfo.getSourceType().getName() + " -> " + this.typeInfo.getTargetType().getName() + " : " + this.converter.toString();
/* 491:    */     }
/* 492:    */   }
/* 493:    */   
/* 494:    */   private final class ConverterFactoryAdapter
/* 495:    */     implements GenericConverter
/* 496:    */   {
/* 497:    */     private final GenericConverter.ConvertiblePair typeInfo;
/* 498:    */     private final ConverterFactory<Object, Object> converterFactory;
/* 499:    */     
/* 500:    */     public ConverterFactoryAdapter(ConverterFactory<?, ?> typeInfo)
/* 501:    */     {
/* 502:530 */       this.converterFactory = converterFactory;
/* 503:531 */       this.typeInfo = typeInfo;
/* 504:    */     }
/* 505:    */     
/* 506:    */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 507:    */     {
/* 508:535 */       return Collections.singleton(this.typeInfo);
/* 509:    */     }
/* 510:    */     
/* 511:    */     public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 512:    */     {
/* 513:539 */       if (source == null) {
/* 514:540 */         return GenericConversionService.this.convertNullSource(sourceType, targetType);
/* 515:    */       }
/* 516:542 */       return this.converterFactory.getConverter(targetType.getObjectType()).convert(source);
/* 517:    */     }
/* 518:    */     
/* 519:    */     public String toString()
/* 520:    */     {
/* 521:546 */       return 
/* 522:547 */         this.typeInfo.getSourceType().getName() + " -> " + this.typeInfo.getTargetType().getName() + " : " + this.converterFactory.toString();
/* 523:    */     }
/* 524:    */   }
/* 525:    */   
/* 526:    */   private static class MatchableConverters
/* 527:    */   {
/* 528:    */     private LinkedList<ConditionalGenericConverter> conditionalConverters;
/* 529:    */     private GenericConverter defaultConverter;
/* 530:    */     
/* 531:    */     public void add(GenericConverter converter)
/* 532:    */     {
/* 533:559 */       if ((converter instanceof ConditionalGenericConverter))
/* 534:    */       {
/* 535:560 */         if (this.conditionalConverters == null) {
/* 536:561 */           this.conditionalConverters = new LinkedList();
/* 537:    */         }
/* 538:563 */         this.conditionalConverters.addFirst((ConditionalGenericConverter)converter);
/* 539:    */       }
/* 540:    */       else
/* 541:    */       {
/* 542:566 */         this.defaultConverter = converter;
/* 543:    */       }
/* 544:    */     }
/* 545:    */     
/* 546:    */     public GenericConverter matchConverter(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 547:    */     {
/* 548:571 */       if (this.conditionalConverters != null) {
/* 549:572 */         for (ConditionalGenericConverter conditional : this.conditionalConverters) {
/* 550:573 */           if (conditional.matches(sourceType, targetType)) {
/* 551:574 */             return conditional;
/* 552:    */           }
/* 553:    */         }
/* 554:    */       }
/* 555:578 */       if ((this.defaultConverter instanceof GenericConversionService.ConverterAdapter))
/* 556:    */       {
/* 557:579 */         GenericConversionService.ConverterAdapter adapter = (GenericConversionService.ConverterAdapter)this.defaultConverter;
/* 558:580 */         if (!adapter.matchesTargetType(targetType)) {
/* 559:581 */           return null;
/* 560:    */         }
/* 561:    */       }
/* 562:584 */       return this.defaultConverter;
/* 563:    */     }
/* 564:    */     
/* 565:    */     public String toString()
/* 566:    */     {
/* 567:588 */       if (this.conditionalConverters != null)
/* 568:    */       {
/* 569:589 */         StringBuilder builder = new StringBuilder();
/* 570:590 */         for (Iterator<ConditionalGenericConverter> it = this.conditionalConverters.iterator(); it.hasNext();)
/* 571:    */         {
/* 572:591 */           builder.append(it.next());
/* 573:592 */           if (it.hasNext()) {
/* 574:593 */             builder.append(", ");
/* 575:    */           }
/* 576:    */         }
/* 577:596 */         if (this.defaultConverter != null) {
/* 578:597 */           builder.append(", ").append(this.defaultConverter);
/* 579:    */         }
/* 580:599 */         return builder.toString();
/* 581:    */       }
/* 582:602 */       return this.defaultConverter.toString();
/* 583:    */     }
/* 584:    */   }
/* 585:    */   
/* 586:    */   private static final class ConverterCacheKey
/* 587:    */   {
/* 588:    */     private final TypeDescriptor sourceType;
/* 589:    */     private final TypeDescriptor targetType;
/* 590:    */     
/* 591:    */     public ConverterCacheKey(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 592:    */     {
/* 593:615 */       this.sourceType = sourceType;
/* 594:616 */       this.targetType = targetType;
/* 595:    */     }
/* 596:    */     
/* 597:    */     public boolean equals(Object other)
/* 598:    */     {
/* 599:620 */       if (this == other) {
/* 600:621 */         return true;
/* 601:    */       }
/* 602:623 */       if (!(other instanceof ConverterCacheKey)) {
/* 603:624 */         return false;
/* 604:    */       }
/* 605:626 */       ConverterCacheKey otherKey = (ConverterCacheKey)other;
/* 606:627 */       return (this.sourceType.equals(otherKey.sourceType)) && (this.targetType.equals(otherKey.targetType));
/* 607:    */     }
/* 608:    */     
/* 609:    */     public int hashCode()
/* 610:    */     {
/* 611:631 */       return this.sourceType.hashCode() * 29 + this.targetType.hashCode();
/* 612:    */     }
/* 613:    */     
/* 614:    */     public String toString()
/* 615:    */     {
/* 616:635 */       return "ConverterCacheKey [sourceType = " + this.sourceType + ", targetType = " + this.targetType + "]";
/* 617:    */     }
/* 618:    */   }
/* 619:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.GenericConversionService
 * JD-Core Version:    0.7.0.1
 */