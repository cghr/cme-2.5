/*   1:    */ package org.springframework.expression.spel.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Array;
/*   4:    */ import java.lang.reflect.Field;
/*   5:    */ import java.lang.reflect.Member;
/*   6:    */ import java.lang.reflect.Method;
/*   7:    */ import java.lang.reflect.Modifier;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.concurrent.ConcurrentHashMap;
/*  10:    */ import org.springframework.core.MethodParameter;
/*  11:    */ import org.springframework.core.convert.Property;
/*  12:    */ import org.springframework.core.convert.TypeDescriptor;
/*  13:    */ import org.springframework.expression.AccessException;
/*  14:    */ import org.springframework.expression.EvaluationContext;
/*  15:    */ import org.springframework.expression.EvaluationException;
/*  16:    */ import org.springframework.expression.PropertyAccessor;
/*  17:    */ import org.springframework.expression.TypeConverter;
/*  18:    */ import org.springframework.expression.TypedValue;
/*  19:    */ import org.springframework.util.ReflectionUtils;
/*  20:    */ import org.springframework.util.StringUtils;
/*  21:    */ 
/*  22:    */ public class ReflectivePropertyAccessor
/*  23:    */   implements PropertyAccessor
/*  24:    */ {
/*  25: 48 */   protected final Map<CacheKey, InvokerPair> readerCache = new ConcurrentHashMap();
/*  26: 50 */   protected final Map<CacheKey, Member> writerCache = new ConcurrentHashMap();
/*  27: 52 */   protected final Map<CacheKey, TypeDescriptor> typeDescriptorCache = new ConcurrentHashMap();
/*  28:    */   
/*  29:    */   public Class<?>[] getSpecificTargetClasses()
/*  30:    */   {
/*  31: 59 */     return null;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public boolean canRead(EvaluationContext context, Object target, String name)
/*  35:    */     throws AccessException
/*  36:    */   {
/*  37: 63 */     if (target == null) {
/*  38: 64 */       return false;
/*  39:    */     }
/*  40: 66 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/*  41: 67 */     if ((type.isArray()) && (name.equals("length"))) {
/*  42: 68 */       return true;
/*  43:    */     }
/*  44: 70 */     CacheKey cacheKey = new CacheKey(type, name);
/*  45: 71 */     if (this.readerCache.containsKey(cacheKey)) {
/*  46: 72 */       return true;
/*  47:    */     }
/*  48: 74 */     Method method = findGetterForProperty(name, type, target instanceof Class);
/*  49: 75 */     if (method != null)
/*  50:    */     {
/*  51: 78 */       Property property = new Property(type, method, null);
/*  52: 79 */       TypeDescriptor typeDescriptor = new TypeDescriptor(property);
/*  53: 80 */       this.readerCache.put(cacheKey, new InvokerPair(method, typeDescriptor));
/*  54: 81 */       this.typeDescriptorCache.put(cacheKey, typeDescriptor);
/*  55: 82 */       return true;
/*  56:    */     }
/*  57: 85 */     Field field = findField(name, type, target instanceof Class);
/*  58: 86 */     if (field != null)
/*  59:    */     {
/*  60: 87 */       TypeDescriptor typeDescriptor = new TypeDescriptor(field);
/*  61: 88 */       this.readerCache.put(cacheKey, new InvokerPair(field, typeDescriptor));
/*  62: 89 */       this.typeDescriptorCache.put(cacheKey, typeDescriptor);
/*  63: 90 */       return true;
/*  64:    */     }
/*  65: 93 */     return false;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public TypedValue read(EvaluationContext context, Object target, String name)
/*  69:    */     throws AccessException
/*  70:    */   {
/*  71: 97 */     if (target == null) {
/*  72: 98 */       throw new AccessException("Cannot read property of null target");
/*  73:    */     }
/*  74:100 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/*  75:102 */     if ((type.isArray()) && (name.equals("length")))
/*  76:    */     {
/*  77:103 */       if ((target instanceof Class)) {
/*  78:104 */         throw new AccessException("Cannot access length on array class itself");
/*  79:    */       }
/*  80:106 */       return new TypedValue(Integer.valueOf(Array.getLength(target)));
/*  81:    */     }
/*  82:109 */     CacheKey cacheKey = new CacheKey(type, name);
/*  83:110 */     InvokerPair invoker = (InvokerPair)this.readerCache.get(cacheKey);
/*  84:112 */     if ((invoker == null) || ((invoker.member instanceof Method)))
/*  85:    */     {
/*  86:113 */       Method method = (Method)(invoker != null ? invoker.member : null);
/*  87:114 */       if (method == null)
/*  88:    */       {
/*  89:115 */         method = findGetterForProperty(name, type, target instanceof Class);
/*  90:116 */         if (method != null)
/*  91:    */         {
/*  92:120 */           Property property = new Property(type, method, null);
/*  93:121 */           TypeDescriptor typeDescriptor = new TypeDescriptor(property);
/*  94:122 */           invoker = new InvokerPair(method, typeDescriptor);
/*  95:123 */           this.readerCache.put(cacheKey, invoker);
/*  96:    */         }
/*  97:    */       }
/*  98:126 */       if (method != null) {
/*  99:    */         try
/* 100:    */         {
/* 101:128 */           ReflectionUtils.makeAccessible(method);
/* 102:129 */           Object value = method.invoke(target, new Object[0]);
/* 103:130 */           return new TypedValue(value, invoker.typeDescriptor.narrow(value));
/* 104:    */         }
/* 105:    */         catch (Exception ex)
/* 106:    */         {
/* 107:133 */           throw new AccessException("Unable to access property '" + name + "' through getter", ex);
/* 108:    */         }
/* 109:    */       }
/* 110:    */     }
/* 111:138 */     if ((invoker == null) || ((invoker.member instanceof Field)))
/* 112:    */     {
/* 113:139 */       Field field = (Field)(invoker == null ? null : invoker.member);
/* 114:140 */       if (field == null)
/* 115:    */       {
/* 116:141 */         field = findField(name, type, target instanceof Class);
/* 117:142 */         if (field != null)
/* 118:    */         {
/* 119:143 */           invoker = new InvokerPair(field, new TypeDescriptor(field));
/* 120:144 */           this.readerCache.put(cacheKey, invoker);
/* 121:    */         }
/* 122:    */       }
/* 123:147 */       if (field != null) {
/* 124:    */         try
/* 125:    */         {
/* 126:149 */           ReflectionUtils.makeAccessible(field);
/* 127:150 */           Object value = field.get(target);
/* 128:151 */           return new TypedValue(value, invoker.typeDescriptor.narrow(value));
/* 129:    */         }
/* 130:    */         catch (Exception ex)
/* 131:    */         {
/* 132:154 */           throw new AccessException("Unable to access field: " + name, ex);
/* 133:    */         }
/* 134:    */       }
/* 135:    */     }
/* 136:159 */     throw new AccessException("Neither getter nor field found for property '" + name + "'");
/* 137:    */   }
/* 138:    */   
/* 139:    */   public boolean canWrite(EvaluationContext context, Object target, String name)
/* 140:    */     throws AccessException
/* 141:    */   {
/* 142:163 */     if (target == null) {
/* 143:164 */       return false;
/* 144:    */     }
/* 145:166 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/* 146:167 */     CacheKey cacheKey = new CacheKey(type, name);
/* 147:168 */     if (this.writerCache.containsKey(cacheKey)) {
/* 148:169 */       return true;
/* 149:    */     }
/* 150:171 */     Method method = findSetterForProperty(name, type, target instanceof Class);
/* 151:172 */     if (method != null)
/* 152:    */     {
/* 153:174 */       Property property = new Property(type, null, method);
/* 154:175 */       TypeDescriptor typeDescriptor = new TypeDescriptor(property);
/* 155:176 */       this.writerCache.put(cacheKey, method);
/* 156:177 */       this.typeDescriptorCache.put(cacheKey, typeDescriptor);
/* 157:178 */       return true;
/* 158:    */     }
/* 159:181 */     Field field = findField(name, type, target instanceof Class);
/* 160:182 */     if (field != null)
/* 161:    */     {
/* 162:183 */       this.writerCache.put(cacheKey, field);
/* 163:184 */       this.typeDescriptorCache.put(cacheKey, new TypeDescriptor(field));
/* 164:185 */       return true;
/* 165:    */     }
/* 166:188 */     return false;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void write(EvaluationContext context, Object target, String name, Object newValue)
/* 170:    */     throws AccessException
/* 171:    */   {
/* 172:192 */     if (target == null) {
/* 173:193 */       throw new AccessException("Cannot write property on null target");
/* 174:    */     }
/* 175:195 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/* 176:    */     
/* 177:197 */     Object possiblyConvertedNewValue = newValue;
/* 178:198 */     TypeDescriptor typeDescriptor = getTypeDescriptor(context, target, name);
/* 179:199 */     if (typeDescriptor != null) {
/* 180:    */       try
/* 181:    */       {
/* 182:201 */         possiblyConvertedNewValue = context.getTypeConverter().convertValue(
/* 183:202 */           newValue, TypeDescriptor.forObject(newValue), typeDescriptor);
/* 184:    */       }
/* 185:    */       catch (EvaluationException evaluationException)
/* 186:    */       {
/* 187:205 */         throw new AccessException("Type conversion failure", evaluationException);
/* 188:    */       }
/* 189:    */     }
/* 190:208 */     CacheKey cacheKey = new CacheKey(type, name);
/* 191:209 */     Member cachedMember = (Member)this.writerCache.get(cacheKey);
/* 192:211 */     if ((cachedMember == null) || ((cachedMember instanceof Method)))
/* 193:    */     {
/* 194:212 */       Method method = (Method)cachedMember;
/* 195:213 */       if (method == null)
/* 196:    */       {
/* 197:214 */         method = findSetterForProperty(name, type, target instanceof Class);
/* 198:215 */         if (method != null)
/* 199:    */         {
/* 200:216 */           cachedMember = method;
/* 201:217 */           this.writerCache.put(cacheKey, cachedMember);
/* 202:    */         }
/* 203:    */       }
/* 204:220 */       if (method != null) {
/* 205:    */         try
/* 206:    */         {
/* 207:222 */           ReflectionUtils.makeAccessible(method);
/* 208:223 */           method.invoke(target, new Object[] { possiblyConvertedNewValue });
/* 209:224 */           return;
/* 210:    */         }
/* 211:    */         catch (Exception ex)
/* 212:    */         {
/* 213:227 */           throw new AccessException("Unable to access property '" + name + "' through setter", ex);
/* 214:    */         }
/* 215:    */       }
/* 216:    */     }
/* 217:232 */     if ((cachedMember == null) || ((cachedMember instanceof Field)))
/* 218:    */     {
/* 219:233 */       Field field = (Field)cachedMember;
/* 220:234 */       if (field == null)
/* 221:    */       {
/* 222:235 */         field = findField(name, type, target instanceof Class);
/* 223:236 */         if (field != null)
/* 224:    */         {
/* 225:237 */           cachedMember = field;
/* 226:238 */           this.writerCache.put(cacheKey, cachedMember);
/* 227:    */         }
/* 228:    */       }
/* 229:241 */       if (field != null) {
/* 230:    */         try
/* 231:    */         {
/* 232:243 */           ReflectionUtils.makeAccessible(field);
/* 233:244 */           field.set(target, possiblyConvertedNewValue);
/* 234:245 */           return;
/* 235:    */         }
/* 236:    */         catch (Exception ex)
/* 237:    */         {
/* 238:248 */           throw new AccessException("Unable to access field: " + name, ex);
/* 239:    */         }
/* 240:    */       }
/* 241:    */     }
/* 242:253 */     throw new AccessException("Neither setter nor field found for property '" + name + "'");
/* 243:    */   }
/* 244:    */   
/* 245:    */   private TypeDescriptor getTypeDescriptor(EvaluationContext context, Object target, String name)
/* 246:    */   {
/* 247:257 */     if (target == null) {
/* 248:258 */       return null;
/* 249:    */     }
/* 250:260 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/* 251:262 */     if ((type.isArray()) && (name.equals("length"))) {
/* 252:263 */       return TypeDescriptor.valueOf(Integer.TYPE);
/* 253:    */     }
/* 254:265 */     CacheKey cacheKey = new CacheKey(type, name);
/* 255:266 */     TypeDescriptor typeDescriptor = (TypeDescriptor)this.typeDescriptorCache.get(cacheKey);
/* 256:267 */     if (typeDescriptor == null) {
/* 257:    */       try
/* 258:    */       {
/* 259:270 */         if (canRead(context, target, name)) {
/* 260:271 */           typeDescriptor = (TypeDescriptor)this.typeDescriptorCache.get(cacheKey);
/* 261:273 */         } else if (canWrite(context, target, name)) {
/* 262:274 */           typeDescriptor = (TypeDescriptor)this.typeDescriptorCache.get(cacheKey);
/* 263:    */         }
/* 264:    */       }
/* 265:    */       catch (AccessException localAccessException) {}
/* 266:    */     }
/* 267:281 */     return typeDescriptor;
/* 268:    */   }
/* 269:    */   
/* 270:    */   protected Method findGetterForProperty(String propertyName, Class<?> clazz, boolean mustBeStatic)
/* 271:    */   {
/* 272:289 */     Method[] ms = clazz.getMethods();
/* 273:    */     
/* 274:291 */     String getterName = "get" + StringUtils.capitalize(propertyName);
/* 275:292 */     for (Method method : ms) {
/* 276:293 */       if ((method.getName().equals(getterName)) && (method.getParameterTypes().length == 0) && (
/* 277:294 */         (!mustBeStatic) || (Modifier.isStatic(method.getModifiers())))) {
/* 278:295 */         return method;
/* 279:    */       }
/* 280:    */     }
/* 281:299 */     getterName = "is" + StringUtils.capitalize(propertyName);
/* 282:300 */     for (Method method : ms) {
/* 283:301 */       if ((method.getName().equals(getterName)) && (method.getParameterTypes().length == 0) && 
/* 284:302 */         (Boolean.TYPE.equals(method.getReturnType())) && (
/* 285:303 */         (!mustBeStatic) || (Modifier.isStatic(method.getModifiers())))) {
/* 286:304 */         return method;
/* 287:    */       }
/* 288:    */     }
/* 289:307 */     return null;
/* 290:    */   }
/* 291:    */   
/* 292:    */   protected Method findSetterForProperty(String propertyName, Class<?> clazz, boolean mustBeStatic)
/* 293:    */   {
/* 294:314 */     Method[] methods = clazz.getMethods();
/* 295:315 */     String setterName = "set" + StringUtils.capitalize(propertyName);
/* 296:316 */     for (Method method : methods) {
/* 297:317 */       if ((method.getName().equals(setterName)) && (method.getParameterTypes().length == 1) && (
/* 298:318 */         (!mustBeStatic) || (Modifier.isStatic(method.getModifiers())))) {
/* 299:319 */         return method;
/* 300:    */       }
/* 301:    */     }
/* 302:322 */     return null;
/* 303:    */   }
/* 304:    */   
/* 305:    */   protected Field findField(String name, Class<?> clazz, boolean mustBeStatic)
/* 306:    */   {
/* 307:329 */     Field[] fields = clazz.getFields();
/* 308:330 */     for (Field field : fields) {
/* 309:331 */       if ((field.getName().equals(name)) && ((!mustBeStatic) || (Modifier.isStatic(field.getModifiers())))) {
/* 310:332 */         return field;
/* 311:    */       }
/* 312:    */     }
/* 313:335 */     return null;
/* 314:    */   }
/* 315:    */   
/* 316:    */   private static class InvokerPair
/* 317:    */   {
/* 318:    */     final Member member;
/* 319:    */     final TypeDescriptor typeDescriptor;
/* 320:    */     
/* 321:    */     public InvokerPair(Member member, TypeDescriptor typeDescriptor)
/* 322:    */     {
/* 323:349 */       this.member = member;
/* 324:350 */       this.typeDescriptor = typeDescriptor;
/* 325:    */     }
/* 326:    */   }
/* 327:    */   
/* 328:    */   private static class CacheKey
/* 329:    */   {
/* 330:    */     private final Class clazz;
/* 331:    */     private final String name;
/* 332:    */     
/* 333:    */     public CacheKey(Class clazz, String name)
/* 334:    */     {
/* 335:362 */       this.clazz = clazz;
/* 336:363 */       this.name = name;
/* 337:    */     }
/* 338:    */     
/* 339:    */     public boolean equals(Object other)
/* 340:    */     {
/* 341:368 */       if (this == other) {
/* 342:369 */         return true;
/* 343:    */       }
/* 344:371 */       if (!(other instanceof CacheKey)) {
/* 345:372 */         return false;
/* 346:    */       }
/* 347:374 */       CacheKey otherKey = (CacheKey)other;
/* 348:375 */       return (this.clazz.equals(otherKey.clazz)) && (this.name.equals(otherKey.name));
/* 349:    */     }
/* 350:    */     
/* 351:    */     public int hashCode()
/* 352:    */     {
/* 353:380 */       return this.clazz.hashCode() * 29 + this.name.hashCode();
/* 354:    */     }
/* 355:    */   }
/* 356:    */   
/* 357:    */   public PropertyAccessor createOptimalAccessor(EvaluationContext eContext, Object target, String name)
/* 358:    */   {
/* 359:393 */     if (target == null) {
/* 360:394 */       return this;
/* 361:    */     }
/* 362:396 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/* 363:397 */     if (type.isArray()) {
/* 364:398 */       return this;
/* 365:    */     }
/* 366:401 */     CacheKey cacheKey = new CacheKey(type, name);
/* 367:402 */     InvokerPair invocationTarget = (InvokerPair)this.readerCache.get(cacheKey);
/* 368:404 */     if ((invocationTarget == null) || ((invocationTarget.member instanceof Method)))
/* 369:    */     {
/* 370:405 */       Method method = (Method)(invocationTarget == null ? null : invocationTarget.member);
/* 371:406 */       if (method == null)
/* 372:    */       {
/* 373:407 */         method = findGetterForProperty(name, type, target instanceof Class);
/* 374:408 */         if (method != null)
/* 375:    */         {
/* 376:409 */           invocationTarget = new InvokerPair(method, new TypeDescriptor(new MethodParameter(method, -1)));
/* 377:410 */           ReflectionUtils.makeAccessible(method);
/* 378:411 */           this.readerCache.put(cacheKey, invocationTarget);
/* 379:    */         }
/* 380:    */       }
/* 381:414 */       if (method != null) {
/* 382:415 */         return new OptimalPropertyAccessor(invocationTarget);
/* 383:    */       }
/* 384:    */     }
/* 385:419 */     if ((invocationTarget == null) || ((invocationTarget.member instanceof Field)))
/* 386:    */     {
/* 387:420 */       Field field = (Field)(invocationTarget == null ? null : invocationTarget.member);
/* 388:421 */       if (field == null)
/* 389:    */       {
/* 390:422 */         field = findField(name, type, target instanceof Class);
/* 391:423 */         if (field != null)
/* 392:    */         {
/* 393:424 */           invocationTarget = new InvokerPair(field, new TypeDescriptor(field));
/* 394:425 */           ReflectionUtils.makeAccessible(field);
/* 395:426 */           this.readerCache.put(cacheKey, invocationTarget);
/* 396:    */         }
/* 397:    */       }
/* 398:429 */       if (field != null) {
/* 399:430 */         return new OptimalPropertyAccessor(invocationTarget);
/* 400:    */       }
/* 401:    */     }
/* 402:433 */     return this;
/* 403:    */   }
/* 404:    */   
/* 405:    */   static class OptimalPropertyAccessor
/* 406:    */     implements PropertyAccessor
/* 407:    */   {
/* 408:    */     private final Member member;
/* 409:    */     private final TypeDescriptor typeDescriptor;
/* 410:    */     private final boolean needsToBeMadeAccessible;
/* 411:    */     
/* 412:    */     OptimalPropertyAccessor(ReflectivePropertyAccessor.InvokerPair target)
/* 413:    */     {
/* 414:448 */       this.member = target.member;
/* 415:449 */       this.typeDescriptor = target.typeDescriptor;
/* 416:450 */       if ((this.member instanceof Field))
/* 417:    */       {
/* 418:451 */         Field field = (Field)this.member;
/* 419:452 */         this.needsToBeMadeAccessible = (((!Modifier.isPublic(field.getModifiers())) || (!Modifier.isPublic(field.getDeclaringClass().getModifiers()))) && 
/* 420:453 */           (!field.isAccessible()));
/* 421:    */       }
/* 422:    */       else
/* 423:    */       {
/* 424:456 */         Method method = (Method)this.member;
/* 425:457 */         this.needsToBeMadeAccessible = (((!Modifier.isPublic(method.getModifiers())) || (!Modifier.isPublic(method.getDeclaringClass().getModifiers()))) && 
/* 426:458 */           (!method.isAccessible()));
/* 427:    */       }
/* 428:    */     }
/* 429:    */     
/* 430:    */     public Class[] getSpecificTargetClasses()
/* 431:    */     {
/* 432:463 */       throw new UnsupportedOperationException("Should not be called on an OptimalPropertyAccessor");
/* 433:    */     }
/* 434:    */     
/* 435:    */     public boolean canRead(EvaluationContext context, Object target, String name)
/* 436:    */       throws AccessException
/* 437:    */     {
/* 438:467 */       if (target == null) {
/* 439:468 */         return false;
/* 440:    */       }
/* 441:470 */       Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/* 442:471 */       if (type.isArray()) {
/* 443:472 */         return false;
/* 444:    */       }
/* 445:474 */       if ((this.member instanceof Method))
/* 446:    */       {
/* 447:475 */         Method method = (Method)this.member;
/* 448:476 */         String getterName = "get" + StringUtils.capitalize(name);
/* 449:477 */         if (getterName.equals(method.getName())) {
/* 450:478 */           return true;
/* 451:    */         }
/* 452:480 */         getterName = "is" + StringUtils.capitalize(name);
/* 453:481 */         return getterName.equals(method.getName());
/* 454:    */       }
/* 455:484 */       Field field = (Field)this.member;
/* 456:485 */       return field.getName().equals(name);
/* 457:    */     }
/* 458:    */     
/* 459:    */     public TypedValue read(EvaluationContext context, Object target, String name)
/* 460:    */       throws AccessException
/* 461:    */     {
/* 462:490 */       if ((this.member instanceof Method)) {
/* 463:    */         try
/* 464:    */         {
/* 465:492 */           if (this.needsToBeMadeAccessible) {
/* 466:493 */             ReflectionUtils.makeAccessible((Method)this.member);
/* 467:    */           }
/* 468:495 */           Object value = ((Method)this.member).invoke(target, new Object[0]);
/* 469:496 */           return new TypedValue(value, this.typeDescriptor.narrow(value));
/* 470:    */         }
/* 471:    */         catch (Exception ex)
/* 472:    */         {
/* 473:499 */           throw new AccessException("Unable to access property '" + name + "' through getter", ex);
/* 474:    */         }
/* 475:    */       }
/* 476:502 */       if ((this.member instanceof Field)) {
/* 477:    */         try
/* 478:    */         {
/* 479:504 */           if (this.needsToBeMadeAccessible) {
/* 480:505 */             ReflectionUtils.makeAccessible((Field)this.member);
/* 481:    */           }
/* 482:507 */           Object value = ((Field)this.member).get(target);
/* 483:508 */           return new TypedValue(value, this.typeDescriptor.narrow(value));
/* 484:    */         }
/* 485:    */         catch (Exception ex)
/* 486:    */         {
/* 487:511 */           throw new AccessException("Unable to access field: " + name, ex);
/* 488:    */         }
/* 489:    */       }
/* 490:514 */       throw new AccessException("Neither getter nor field found for property '" + name + "'");
/* 491:    */     }
/* 492:    */     
/* 493:    */     public boolean canWrite(EvaluationContext context, Object target, String name)
/* 494:    */       throws AccessException
/* 495:    */     {
/* 496:518 */       throw new UnsupportedOperationException("Should not be called on an OptimalPropertyAccessor");
/* 497:    */     }
/* 498:    */     
/* 499:    */     public void write(EvaluationContext context, Object target, String name, Object newValue)
/* 500:    */       throws AccessException
/* 501:    */     {
/* 502:523 */       throw new UnsupportedOperationException("Should not be called on an OptimalPropertyAccessor");
/* 503:    */     }
/* 504:    */   }
/* 505:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.support.ReflectivePropertyAccessor
 * JD-Core Version:    0.7.0.1
 */