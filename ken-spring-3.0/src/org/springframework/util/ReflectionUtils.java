/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Constructor;
/*   4:    */ import java.lang.reflect.Field;
/*   5:    */ import java.lang.reflect.InvocationTargetException;
/*   6:    */ import java.lang.reflect.Method;
/*   7:    */ import java.lang.reflect.Modifier;
/*   8:    */ import java.lang.reflect.UndeclaredThrowableException;
/*   9:    */ import java.sql.SQLException;
/*  10:    */ import java.util.ArrayList;
/*  11:    */ import java.util.Arrays;
/*  12:    */ import java.util.List;
/*  13:    */ import java.util.regex.Matcher;
/*  14:    */ import java.util.regex.Pattern;
/*  15:    */ 
/*  16:    */ public abstract class ReflectionUtils
/*  17:    */ {
/*  18: 47 */   private static final Pattern CGLIB_RENAMED_METHOD_PATTERN = Pattern.compile("CGLIB\\$(.+)\\$\\d+");
/*  19:    */   
/*  20:    */   public static Field findField(Class<?> clazz, String name)
/*  21:    */   {
/*  22: 57 */     return findField(clazz, name, null);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static Field findField(Class<?> clazz, String name, Class<?> type)
/*  26:    */   {
/*  27: 70 */     Assert.notNull(clazz, "Class must not be null");
/*  28: 71 */     Assert.isTrue((name != null) || (type != null), "Either name or type of the field must be specified");
/*  29: 72 */     Class<?> searchType = clazz;
/*  30: 73 */     while ((!Object.class.equals(searchType)) && (searchType != null))
/*  31:    */     {
/*  32: 74 */       Field[] fields = searchType.getDeclaredFields();
/*  33: 75 */       for (Field field : fields) {
/*  34: 76 */         if (((name == null) || (name.equals(field.getName()))) && ((type == null) || (type.equals(field.getType())))) {
/*  35: 77 */           return field;
/*  36:    */         }
/*  37:    */       }
/*  38: 80 */       searchType = searchType.getSuperclass();
/*  39:    */     }
/*  40: 82 */     return null;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public static void setField(Field field, Object target, Object value)
/*  44:    */   {
/*  45:    */     try
/*  46:    */     {
/*  47: 97 */       field.set(target, value);
/*  48:    */     }
/*  49:    */     catch (IllegalAccessException ex)
/*  50:    */     {
/*  51:100 */       handleReflectionException(ex);
/*  52:101 */       throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass().getName() + ": " + 
/*  53:102 */         ex.getMessage());
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public static Object getField(Field field, Object target)
/*  58:    */   {
/*  59:    */     try
/*  60:    */     {
/*  61:118 */       return field.get(target);
/*  62:    */     }
/*  63:    */     catch (IllegalAccessException ex)
/*  64:    */     {
/*  65:121 */       handleReflectionException(ex);
/*  66:122 */       throw new IllegalStateException(
/*  67:123 */         "Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
/*  68:    */     }
/*  69:    */   }
/*  70:    */   
/*  71:    */   public static Method findMethod(Class<?> clazz, String name)
/*  72:    */   {
/*  73:136 */     return findMethod(clazz, name, new Class[0]);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes)
/*  77:    */   {
/*  78:150 */     Assert.notNull(clazz, "Class must not be null");
/*  79:151 */     Assert.notNull(name, "Method name must not be null");
/*  80:152 */     Class<?> searchType = clazz;
/*  81:153 */     while (searchType != null)
/*  82:    */     {
/*  83:154 */       Method[] methods = searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods();
/*  84:155 */       for (Method method : methods) {
/*  85:156 */         if ((name.equals(method.getName())) && (
/*  86:157 */           (paramTypes == null) || (Arrays.equals(paramTypes, method.getParameterTypes())))) {
/*  87:158 */           return method;
/*  88:    */         }
/*  89:    */       }
/*  90:161 */       searchType = searchType.getSuperclass();
/*  91:    */     }
/*  92:163 */     return null;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public static Object invokeMethod(Method method, Object target)
/*  96:    */   {
/*  97:176 */     return invokeMethod(method, target, new Object[0]);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public static Object invokeMethod(Method method, Object target, Object... args)
/* 101:    */   {
/* 102:    */     try
/* 103:    */     {
/* 104:191 */       return method.invoke(target, args);
/* 105:    */     }
/* 106:    */     catch (Exception ex)
/* 107:    */     {
/* 108:194 */       handleReflectionException(ex);
/* 109:    */       
/* 110:196 */       throw new IllegalStateException("Should never get here");
/* 111:    */     }
/* 112:    */   }
/* 113:    */   
/* 114:    */   public static Object invokeJdbcMethod(Method method, Object target)
/* 115:    */     throws SQLException
/* 116:    */   {
/* 117:209 */     return invokeJdbcMethod(method, target, new Object[0]);
/* 118:    */   }
/* 119:    */   
/* 120:    */   public static Object invokeJdbcMethod(Method method, Object target, Object... args)
/* 121:    */     throws SQLException
/* 122:    */   {
/* 123:    */     try
/* 124:    */     {
/* 125:224 */       return method.invoke(target, args);
/* 126:    */     }
/* 127:    */     catch (IllegalAccessException ex)
/* 128:    */     {
/* 129:227 */       handleReflectionException(ex);
/* 130:    */     }
/* 131:    */     catch (InvocationTargetException ex)
/* 132:    */     {
/* 133:230 */       if ((ex.getTargetException() instanceof SQLException)) {
/* 134:231 */         throw ((SQLException)ex.getTargetException());
/* 135:    */       }
/* 136:233 */       handleInvocationTargetException(ex);
/* 137:    */     }
/* 138:235 */     throw new IllegalStateException("Should never get here");
/* 139:    */   }
/* 140:    */   
/* 141:    */   public static void handleReflectionException(Exception ex)
/* 142:    */   {
/* 143:247 */     if ((ex instanceof NoSuchMethodException)) {
/* 144:248 */       throw new IllegalStateException("Method not found: " + ex.getMessage());
/* 145:    */     }
/* 146:250 */     if ((ex instanceof IllegalAccessException)) {
/* 147:251 */       throw new IllegalStateException("Could not access method: " + ex.getMessage());
/* 148:    */     }
/* 149:253 */     if ((ex instanceof InvocationTargetException)) {
/* 150:254 */       handleInvocationTargetException((InvocationTargetException)ex);
/* 151:    */     }
/* 152:256 */     if ((ex instanceof RuntimeException)) {
/* 153:257 */       throw ((RuntimeException)ex);
/* 154:    */     }
/* 155:259 */     throw new UndeclaredThrowableException(ex);
/* 156:    */   }
/* 157:    */   
/* 158:    */   public static void handleInvocationTargetException(InvocationTargetException ex)
/* 159:    */   {
/* 160:270 */     rethrowRuntimeException(ex.getTargetException());
/* 161:    */   }
/* 162:    */   
/* 163:    */   public static void rethrowRuntimeException(Throwable ex)
/* 164:    */   {
/* 165:285 */     if ((ex instanceof RuntimeException)) {
/* 166:286 */       throw ((RuntimeException)ex);
/* 167:    */     }
/* 168:288 */     if ((ex instanceof Error)) {
/* 169:289 */       throw ((Error)ex);
/* 170:    */     }
/* 171:291 */     throw new UndeclaredThrowableException(ex);
/* 172:    */   }
/* 173:    */   
/* 174:    */   public static void rethrowException(Throwable ex)
/* 175:    */     throws Exception
/* 176:    */   {
/* 177:306 */     if ((ex instanceof Exception)) {
/* 178:307 */       throw ((Exception)ex);
/* 179:    */     }
/* 180:309 */     if ((ex instanceof Error)) {
/* 181:310 */       throw ((Error)ex);
/* 182:    */     }
/* 183:312 */     throw new UndeclaredThrowableException(ex);
/* 184:    */   }
/* 185:    */   
/* 186:    */   public static boolean declaresException(Method method, Class<?> exceptionType)
/* 187:    */   {
/* 188:325 */     Assert.notNull(method, "Method must not be null");
/* 189:326 */     Class[] declaredExceptions = method.getExceptionTypes();
/* 190:327 */     for (Class<?> declaredException : declaredExceptions) {
/* 191:328 */       if (declaredException.isAssignableFrom(exceptionType)) {
/* 192:329 */         return true;
/* 193:    */       }
/* 194:    */     }
/* 195:332 */     return false;
/* 196:    */   }
/* 197:    */   
/* 198:    */   public static boolean isPublicStaticFinal(Field field)
/* 199:    */   {
/* 200:340 */     int modifiers = field.getModifiers();
/* 201:341 */     return (Modifier.isPublic(modifiers)) && (Modifier.isStatic(modifiers)) && (Modifier.isFinal(modifiers));
/* 202:    */   }
/* 203:    */   
/* 204:    */   public static boolean isEqualsMethod(Method method)
/* 205:    */   {
/* 206:349 */     if ((method == null) || (!method.getName().equals("equals"))) {
/* 207:350 */       return false;
/* 208:    */     }
/* 209:352 */     Class[] paramTypes = method.getParameterTypes();
/* 210:353 */     return (paramTypes.length == 1) && (paramTypes[0] == Object.class);
/* 211:    */   }
/* 212:    */   
/* 213:    */   public static boolean isHashCodeMethod(Method method)
/* 214:    */   {
/* 215:361 */     return (method != null) && (method.getName().equals("hashCode")) && (method.getParameterTypes().length == 0);
/* 216:    */   }
/* 217:    */   
/* 218:    */   public static boolean isToStringMethod(Method method)
/* 219:    */   {
/* 220:369 */     return (method != null) && (method.getName().equals("toString")) && (method.getParameterTypes().length == 0);
/* 221:    */   }
/* 222:    */   
/* 223:    */   public static boolean isObjectMethod(Method method)
/* 224:    */   {
/* 225:    */     try
/* 226:    */     {
/* 227:377 */       Object.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
/* 228:378 */       return true;
/* 229:    */     }
/* 230:    */     catch (SecurityException localSecurityException)
/* 231:    */     {
/* 232:380 */       return false;
/* 233:    */     }
/* 234:    */     catch (NoSuchMethodException localNoSuchMethodException) {}
/* 235:382 */     return false;
/* 236:    */   }
/* 237:    */   
/* 238:    */   public static boolean isCglibRenamedMethod(Method renamedMethod)
/* 239:    */   {
/* 240:393 */     return CGLIB_RENAMED_METHOD_PATTERN.matcher(renamedMethod.getName()).matches();
/* 241:    */   }
/* 242:    */   
/* 243:    */   public static void makeAccessible(Field field)
/* 244:    */   {
/* 245:405 */     if (((!Modifier.isPublic(field.getModifiers())) || (!Modifier.isPublic(field.getDeclaringClass().getModifiers())) || 
/* 246:406 */       (Modifier.isFinal(field.getModifiers()))) && (!field.isAccessible())) {
/* 247:407 */       field.setAccessible(true);
/* 248:    */     }
/* 249:    */   }
/* 250:    */   
/* 251:    */   public static void makeAccessible(Method method)
/* 252:    */   {
/* 253:420 */     if (((!Modifier.isPublic(method.getModifiers())) || (!Modifier.isPublic(method.getDeclaringClass().getModifiers()))) && 
/* 254:421 */       (!method.isAccessible())) {
/* 255:422 */       method.setAccessible(true);
/* 256:    */     }
/* 257:    */   }
/* 258:    */   
/* 259:    */   public static void makeAccessible(Constructor<?> ctor)
/* 260:    */   {
/* 261:435 */     if (((!Modifier.isPublic(ctor.getModifiers())) || (!Modifier.isPublic(ctor.getDeclaringClass().getModifiers()))) && 
/* 262:436 */       (!ctor.isAccessible())) {
/* 263:437 */       ctor.setAccessible(true);
/* 264:    */     }
/* 265:    */   }
/* 266:    */   
/* 267:    */   public static void doWithMethods(Class<?> clazz, MethodCallback mc)
/* 268:    */     throws IllegalArgumentException
/* 269:    */   {
/* 270:451 */     doWithMethods(clazz, mc, null);
/* 271:    */   }
/* 272:    */   
/* 273:    */   public static void doWithMethods(Class<?> clazz, MethodCallback mc, MethodFilter mf)
/* 274:    */     throws IllegalArgumentException
/* 275:    */   {
/* 276:467 */     Method[] methods = clazz.getDeclaredMethods();
/* 277:468 */     for (Method method : methods) {
/* 278:469 */       if ((mf == null) || (mf.matches(method))) {
/* 279:    */         try
/* 280:    */         {
/* 281:473 */           mc.doWith(method);
/* 282:    */         }
/* 283:    */         catch (IllegalAccessException ex)
/* 284:    */         {
/* 285:476 */           throw new IllegalStateException("Shouldn't be illegal to access method '" + method.getName() + 
/* 286:477 */             "': " + ex);
/* 287:    */         }
/* 288:    */       }
/* 289:    */     }
/* 290:480 */     if (clazz.getSuperclass() != null) {
/* 291:481 */       doWithMethods(clazz.getSuperclass(), mc, mf);
/* 292:483 */     } else if (clazz.isInterface()) {
/* 293:484 */       for (Class<?> superIfc : clazz.getInterfaces()) {
/* 294:485 */         doWithMethods(superIfc, mc, mf);
/* 295:    */       }
/* 296:    */     }
/* 297:    */   }
/* 298:    */   
/* 299:    */   public static Method[] getAllDeclaredMethods(Class<?> leafClass)
/* 300:    */     throws IllegalArgumentException
/* 301:    */   {
/* 302:495 */     List<Method> methods = new ArrayList(32);
/* 303:496 */     doWithMethods(leafClass, new MethodCallback()
/* 304:    */     {
/* 305:    */       public void doWith(Method method)
/* 306:    */       {
/* 307:498 */         ReflectionUtils.this.add(method);
/* 308:    */       }
/* 309:500 */     });
/* 310:501 */     return (Method[])methods.toArray(new Method[methods.size()]);
/* 311:    */   }
/* 312:    */   
/* 313:    */   public static Method[] getUniqueDeclaredMethods(Class<?> leafClass)
/* 314:    */     throws IllegalArgumentException
/* 315:    */   {
/* 316:510 */     List<Method> methods = new ArrayList(32);
/* 317:511 */     doWithMethods(leafClass, new MethodCallback()
/* 318:    */     {
/* 319:    */       public void doWith(Method method)
/* 320:    */       {
/* 321:513 */         boolean knownSignature = false;
/* 322:514 */         Method methodBeingOverriddenWithCovariantReturnType = null;
/* 323:516 */         for (Method existingMethod : ReflectionUtils.this) {
/* 324:517 */           if ((method.getName().equals(existingMethod.getName())) && 
/* 325:518 */             (Arrays.equals(method.getParameterTypes(), existingMethod.getParameterTypes())))
/* 326:    */           {
/* 327:520 */             if ((existingMethod.getReturnType() != method.getReturnType()) && 
/* 328:521 */               (existingMethod.getReturnType().isAssignableFrom(method.getReturnType())))
/* 329:    */             {
/* 330:522 */               methodBeingOverriddenWithCovariantReturnType = existingMethod; break;
/* 331:    */             }
/* 332:524 */             knownSignature = true;
/* 333:    */             
/* 334:526 */             break;
/* 335:    */           }
/* 336:    */         }
/* 337:529 */         if (methodBeingOverriddenWithCovariantReturnType != null) {
/* 338:530 */           ReflectionUtils.this.remove(methodBeingOverriddenWithCovariantReturnType);
/* 339:    */         }
/* 340:532 */         if ((!knownSignature) && (!ReflectionUtils.isCglibRenamedMethod(method))) {
/* 341:533 */           ReflectionUtils.this.add(method);
/* 342:    */         }
/* 343:    */       }
/* 344:536 */     });
/* 345:537 */     return (Method[])methods.toArray(new Method[methods.size()]);
/* 346:    */   }
/* 347:    */   
/* 348:    */   public static void doWithFields(Class<?> clazz, FieldCallback fc)
/* 349:    */     throws IllegalArgumentException
/* 350:    */   {
/* 351:547 */     doWithFields(clazz, fc, null);
/* 352:    */   }
/* 353:    */   
/* 354:    */   public static void doWithFields(Class<?> clazz, FieldCallback fc, FieldFilter ff)
/* 355:    */     throws IllegalArgumentException
/* 356:    */   {
/* 357:561 */     Class<?> targetClass = clazz;
/* 358:    */     do
/* 359:    */     {
/* 360:563 */       Field[] fields = targetClass.getDeclaredFields();
/* 361:564 */       for (Field field : fields) {
/* 362:566 */         if ((ff == null) || (ff.matches(field))) {
/* 363:    */           try
/* 364:    */           {
/* 365:570 */             fc.doWith(field);
/* 366:    */           }
/* 367:    */           catch (IllegalAccessException ex)
/* 368:    */           {
/* 369:573 */             throw new IllegalStateException(
/* 370:574 */               "Shouldn't be illegal to access field '" + field.getName() + "': " + ex);
/* 371:    */           }
/* 372:    */         }
/* 373:    */       }
/* 374:577 */       targetClass = targetClass.getSuperclass();
/* 375:579 */     } while ((targetClass != null) && (targetClass != Object.class));
/* 376:    */   }
/* 377:    */   
/* 378:    */   public static void shallowCopyFieldState(Object src, final Object dest)
/* 379:    */     throws IllegalArgumentException
/* 380:    */   {
/* 381:589 */     if (src == null) {
/* 382:590 */       throw new IllegalArgumentException("Source for field copy cannot be null");
/* 383:    */     }
/* 384:592 */     if (dest == null) {
/* 385:593 */       throw new IllegalArgumentException("Destination for field copy cannot be null");
/* 386:    */     }
/* 387:595 */     if (!src.getClass().isAssignableFrom(dest.getClass())) {
/* 388:596 */       throw new IllegalArgumentException("Destination class [" + dest.getClass().getName() + 
/* 389:597 */         "] must be same or subclass as source class [" + src.getClass().getName() + "]");
/* 390:    */     }
/* 391:599 */     doWithFields(src.getClass(), new FieldCallback()
/* 392:    */     {
/* 393:    */       public void doWith(Field field)
/* 394:    */         throws IllegalArgumentException, IllegalAccessException
/* 395:    */       {
/* 396:601 */         ReflectionUtils.makeAccessible(field);
/* 397:602 */         Object srcValue = field.get(ReflectionUtils.this);
/* 398:603 */         field.set(dest, srcValue);
/* 399:    */       }
/* 400:605 */     }, COPYABLE_FIELDS);
/* 401:    */   }
/* 402:    */   
/* 403:664 */   public static FieldFilter COPYABLE_FIELDS = new FieldFilter()
/* 404:    */   {
/* 405:    */     public boolean matches(Field field)
/* 406:    */     {
/* 407:667 */       return (!Modifier.isStatic(field.getModifiers())) && (!Modifier.isFinal(field.getModifiers()));
/* 408:    */     }
/* 409:    */   };
/* 410:675 */   public static MethodFilter NON_BRIDGED_METHODS = new MethodFilter()
/* 411:    */   {
/* 412:    */     public boolean matches(Method method)
/* 413:    */     {
/* 414:678 */       return !method.isBridge();
/* 415:    */     }
/* 416:    */   };
/* 417:687 */   public static MethodFilter USER_DECLARED_METHODS = new MethodFilter()
/* 418:    */   {
/* 419:    */     public boolean matches(Method method)
/* 420:    */     {
/* 421:690 */       return (!method.isBridge()) && (method.getDeclaringClass() != Object.class);
/* 422:    */     }
/* 423:    */   };
/* 424:    */   
/* 425:    */   public static abstract interface FieldCallback
/* 426:    */   {
/* 427:    */     public abstract void doWith(Field paramField)
/* 428:    */       throws IllegalArgumentException, IllegalAccessException;
/* 429:    */   }
/* 430:    */   
/* 431:    */   public static abstract interface FieldFilter
/* 432:    */   {
/* 433:    */     public abstract boolean matches(Field paramField);
/* 434:    */   }
/* 435:    */   
/* 436:    */   public static abstract interface MethodCallback
/* 437:    */   {
/* 438:    */     public abstract void doWith(Method paramMethod)
/* 439:    */       throws IllegalArgumentException, IllegalAccessException;
/* 440:    */   }
/* 441:    */   
/* 442:    */   public static abstract interface MethodFilter
/* 443:    */   {
/* 444:    */     public abstract boolean matches(Method paramMethod);
/* 445:    */   }
/* 446:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.ReflectionUtils
 * JD-Core Version:    0.7.0.1
 */