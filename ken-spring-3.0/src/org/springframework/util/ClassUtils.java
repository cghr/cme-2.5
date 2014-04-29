/*    1:     */ package org.springframework.util;
/*    2:     */ 
/*    3:     */ import java.beans.Introspector;
/*    4:     */ import java.lang.reflect.Array;
/*    5:     */ import java.lang.reflect.Constructor;
/*    6:     */ import java.lang.reflect.Method;
/*    7:     */ import java.lang.reflect.Modifier;
/*    8:     */ import java.lang.reflect.Proxy;
/*    9:     */ import java.util.Arrays;
/*   10:     */ import java.util.Collection;
/*   11:     */ import java.util.Collections;
/*   12:     */ import java.util.HashMap;
/*   13:     */ import java.util.HashSet;
/*   14:     */ import java.util.Iterator;
/*   15:     */ import java.util.LinkedHashSet;
/*   16:     */ import java.util.Map;
/*   17:     */ import java.util.Map.Entry;
/*   18:     */ import java.util.Set;
/*   19:     */ 
/*   20:     */ public abstract class ClassUtils
/*   21:     */ {
/*   22:     */   public static final String ARRAY_SUFFIX = "[]";
/*   23:     */   private static final String INTERNAL_ARRAY_PREFIX = "[";
/*   24:     */   private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";
/*   25:     */   private static final char PACKAGE_SEPARATOR = '.';
/*   26:     */   private static final char INNER_CLASS_SEPARATOR = '$';
/*   27:     */   public static final String CGLIB_CLASS_SEPARATOR = "$$";
/*   28:     */   public static final String CLASS_FILE_SUFFIX = ".class";
/*   29:  78 */   private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new HashMap(8);
/*   30:  84 */   private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new HashMap(8);
/*   31:  90 */   private static final Map<String, Class<?>> primitiveTypeNameMap = new HashMap(32);
/*   32:  96 */   private static final Map<String, Class<?>> commonClassCache = new HashMap(32);
/*   33:     */   
/*   34:     */   static
/*   35:     */   {
/*   36: 100 */     primitiveWrapperTypeMap.put(Boolean.class, Boolean.TYPE);
/*   37: 101 */     primitiveWrapperTypeMap.put(Byte.class, Byte.TYPE);
/*   38: 102 */     primitiveWrapperTypeMap.put(Character.class, Character.TYPE);
/*   39: 103 */     primitiveWrapperTypeMap.put(Double.class, Double.TYPE);
/*   40: 104 */     primitiveWrapperTypeMap.put(Float.class, Float.TYPE);
/*   41: 105 */     primitiveWrapperTypeMap.put(Integer.class, Integer.TYPE);
/*   42: 106 */     primitiveWrapperTypeMap.put(Long.class, Long.TYPE);
/*   43: 107 */     primitiveWrapperTypeMap.put(Short.class, Short.TYPE);
/*   44: 109 */     for (Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperTypeMap.entrySet())
/*   45:     */     {
/*   46: 110 */       primitiveTypeToWrapperMap.put((Class)entry.getValue(), (Class)entry.getKey());
/*   47: 111 */       registerCommonClasses(new Class[] { (Class)entry.getKey() });
/*   48:     */     }
/*   49: 114 */     Set<Class<?>> primitiveTypes = new HashSet(32);
/*   50: 115 */     primitiveTypes.addAll(primitiveWrapperTypeMap.values());
/*   51: 116 */     primitiveTypes.addAll(
/*   52:     */     
/*   53: 118 */       (Collection)Arrays.asList(new Class[] {[Z.class, [B.class, [C.class, [D.class, [F.class, [I.class, [J.class, [S.class }));
/*   54: 119 */     primitiveTypes.add(Void.TYPE);
/*   55: 120 */     for (Object primitiveType : primitiveTypes) {
/*   56: 121 */       primitiveTypeNameMap.put(((Class)primitiveType).getName(), primitiveType);
/*   57:     */     }
/*   58: 125 */     registerCommonClasses(new Class[] { [Ljava.lang.Boolean.class, [Ljava.lang.Byte.class, [Ljava.lang.Character.class, [Ljava.lang.Double.class, [Ljava.lang.Float.class, [Ljava.lang.Integer.class, [Ljava.lang.Long.class, [Ljava.lang.Short.class });
/*   59:     */     
/*   60: 127 */     registerCommonClasses(new Class[] { Number.class, [Ljava.lang.Number.class, String.class, [Ljava.lang.String.class, Object.class, [Ljava.lang.Object.class, Class.class, [Ljava.lang.Class.class });
/*   61:     */     
/*   62: 129 */     registerCommonClasses(new Class[] { Throwable.class, Exception.class, RuntimeException.class, Error.class, StackTraceElement.class, [Ljava.lang.StackTraceElement.class });
/*   63:     */   }
/*   64:     */   
/*   65:     */   private static void registerCommonClasses(Class<?>... commonClasses)
/*   66:     */   {
/*   67: 137 */     Class[] arrayOfClass = commonClasses;int j = commonClasses.length;
/*   68: 137 */     for (int i = 0; i < j; i++)
/*   69:     */     {
/*   70: 137 */       Class<?> clazz = arrayOfClass[i];
/*   71: 138 */       commonClassCache.put(clazz.getName(), clazz);
/*   72:     */     }
/*   73:     */   }
/*   74:     */   
/*   75:     */   public static ClassLoader getDefaultClassLoader()
/*   76:     */   {
/*   77: 155 */     ClassLoader cl = null;
/*   78:     */     try
/*   79:     */     {
/*   80: 157 */       cl = Thread.currentThread().getContextClassLoader();
/*   81:     */     }
/*   82:     */     catch (Throwable localThrowable) {}
/*   83: 162 */     if (cl == null) {
/*   84: 164 */       cl = ClassUtils.class.getClassLoader();
/*   85:     */     }
/*   86: 166 */     return cl;
/*   87:     */   }
/*   88:     */   
/*   89:     */   public static ClassLoader overrideThreadContextClassLoader(ClassLoader classLoaderToUse)
/*   90:     */   {
/*   91: 177 */     Thread currentThread = Thread.currentThread();
/*   92: 178 */     ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();
/*   93: 179 */     if ((classLoaderToUse != null) && (!classLoaderToUse.equals(threadContextClassLoader)))
/*   94:     */     {
/*   95: 180 */       currentThread.setContextClassLoader(classLoaderToUse);
/*   96: 181 */       return threadContextClassLoader;
/*   97:     */     }
/*   98: 184 */     return null;
/*   99:     */   }
/*  100:     */   
/*  101:     */   @Deprecated
/*  102:     */   public static Class<?> forName(String name)
/*  103:     */     throws ClassNotFoundException, LinkageError
/*  104:     */   {
/*  105: 204 */     return forName(name, getDefaultClassLoader());
/*  106:     */   }
/*  107:     */   
/*  108:     */   public static Class<?> forName(String name, ClassLoader classLoader)
/*  109:     */     throws ClassNotFoundException, LinkageError
/*  110:     */   {
/*  111: 221 */     Assert.notNull(name, "Name must not be null");
/*  112:     */     
/*  113: 223 */     Class<?> clazz = resolvePrimitiveClassName(name);
/*  114: 224 */     if (clazz == null) {
/*  115: 225 */       clazz = (Class)commonClassCache.get(name);
/*  116:     */     }
/*  117: 227 */     if (clazz != null) {
/*  118: 228 */       return clazz;
/*  119:     */     }
/*  120: 232 */     if (name.endsWith("[]"))
/*  121:     */     {
/*  122: 233 */       String elementClassName = name.substring(0, name.length() - "[]".length());
/*  123: 234 */       Class<?> elementClass = forName(elementClassName, classLoader);
/*  124: 235 */       return Array.newInstance(elementClass, 0).getClass();
/*  125:     */     }
/*  126: 239 */     if ((name.startsWith("[L")) && (name.endsWith(";")))
/*  127:     */     {
/*  128: 240 */       String elementName = name.substring("[L".length(), name.length() - 1);
/*  129: 241 */       Class<?> elementClass = forName(elementName, classLoader);
/*  130: 242 */       return Array.newInstance(elementClass, 0).getClass();
/*  131:     */     }
/*  132: 246 */     if (name.startsWith("["))
/*  133:     */     {
/*  134: 247 */       String elementName = name.substring("[".length());
/*  135: 248 */       Class<?> elementClass = forName(elementName, classLoader);
/*  136: 249 */       return Array.newInstance(elementClass, 0).getClass();
/*  137:     */     }
/*  138: 252 */     ClassLoader classLoaderToUse = classLoader;
/*  139: 253 */     if (classLoaderToUse == null) {
/*  140: 254 */       classLoaderToUse = getDefaultClassLoader();
/*  141:     */     }
/*  142:     */     try
/*  143:     */     {
/*  144: 257 */       return classLoaderToUse.loadClass(name);
/*  145:     */     }
/*  146:     */     catch (ClassNotFoundException ex)
/*  147:     */     {
/*  148: 260 */       int lastDotIndex = name.lastIndexOf('.');
/*  149: 261 */       if (lastDotIndex != -1)
/*  150:     */       {
/*  151: 262 */         String innerClassName = name.substring(0, lastDotIndex) + '$' + name.substring(lastDotIndex + 1);
/*  152:     */         try
/*  153:     */         {
/*  154: 264 */           return classLoaderToUse.loadClass(innerClassName);
/*  155:     */         }
/*  156:     */         catch (ClassNotFoundException localClassNotFoundException1) {}
/*  157:     */       }
/*  158: 270 */       throw ex;
/*  159:     */     }
/*  160:     */   }
/*  161:     */   
/*  162:     */   public static Class<?> resolveClassName(String className, ClassLoader classLoader)
/*  163:     */     throws IllegalArgumentException
/*  164:     */   {
/*  165:     */     try
/*  166:     */     {
/*  167: 290 */       return forName(className, classLoader);
/*  168:     */     }
/*  169:     */     catch (ClassNotFoundException ex)
/*  170:     */     {
/*  171: 293 */       throw new IllegalArgumentException("Cannot find class [" + className + "]", ex);
/*  172:     */     }
/*  173:     */     catch (LinkageError ex)
/*  174:     */     {
/*  175: 296 */       throw new IllegalArgumentException(
/*  176: 297 */         "Error loading class [" + className + "]: problem with class file or dependent class.", ex);
/*  177:     */     }
/*  178:     */   }
/*  179:     */   
/*  180:     */   public static Class<?> resolvePrimitiveClassName(String name)
/*  181:     */   {
/*  182: 312 */     Class<?> result = null;
/*  183: 315 */     if ((name != null) && (name.length() <= 8)) {
/*  184: 317 */       result = (Class)primitiveTypeNameMap.get(name);
/*  185:     */     }
/*  186: 319 */     return result;
/*  187:     */   }
/*  188:     */   
/*  189:     */   @Deprecated
/*  190:     */   public static boolean isPresent(String className)
/*  191:     */   {
/*  192: 332 */     return isPresent(className, getDefaultClassLoader());
/*  193:     */   }
/*  194:     */   
/*  195:     */   public static boolean isPresent(String className, ClassLoader classLoader)
/*  196:     */   {
/*  197:     */     try
/*  198:     */     {
/*  199: 346 */       forName(className, classLoader);
/*  200: 347 */       return true;
/*  201:     */     }
/*  202:     */     catch (Throwable localThrowable) {}
/*  203: 351 */     return false;
/*  204:     */   }
/*  205:     */   
/*  206:     */   public static Class<?> getUserClass(Object instance)
/*  207:     */   {
/*  208: 363 */     Assert.notNull(instance, "Instance must not be null");
/*  209: 364 */     return getUserClass(instance.getClass());
/*  210:     */   }
/*  211:     */   
/*  212:     */   public static Class<?> getUserClass(Class<?> clazz)
/*  213:     */   {
/*  214: 374 */     if ((clazz != null) && (clazz.getName().contains("$$")))
/*  215:     */     {
/*  216: 375 */       Class<?> superClass = clazz.getSuperclass();
/*  217: 376 */       if ((superClass != null) && (!Object.class.equals(superClass))) {
/*  218: 377 */         return superClass;
/*  219:     */       }
/*  220:     */     }
/*  221: 380 */     return clazz;
/*  222:     */   }
/*  223:     */   
/*  224:     */   public static boolean isCacheSafe(Class<?> clazz, ClassLoader classLoader)
/*  225:     */   {
/*  226: 390 */     Assert.notNull(clazz, "Class must not be null");
/*  227: 391 */     ClassLoader target = clazz.getClassLoader();
/*  228: 392 */     if (target == null) {
/*  229: 393 */       return false;
/*  230:     */     }
/*  231: 395 */     ClassLoader cur = classLoader;
/*  232: 396 */     if (cur == target) {
/*  233: 397 */       return true;
/*  234:     */     }
/*  235: 399 */     while (cur != null)
/*  236:     */     {
/*  237: 400 */       cur = cur.getParent();
/*  238: 401 */       if (cur == target) {
/*  239: 402 */         return true;
/*  240:     */       }
/*  241:     */     }
/*  242: 405 */     return false;
/*  243:     */   }
/*  244:     */   
/*  245:     */   public static String getShortName(String className)
/*  246:     */   {
/*  247: 416 */     Assert.hasLength(className, "Class name must not be empty");
/*  248: 417 */     int lastDotIndex = className.lastIndexOf('.');
/*  249: 418 */     int nameEndIndex = className.indexOf("$$");
/*  250: 419 */     if (nameEndIndex == -1) {
/*  251: 420 */       nameEndIndex = className.length();
/*  252:     */     }
/*  253: 422 */     String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
/*  254: 423 */     shortName = shortName.replace('$', '.');
/*  255: 424 */     return shortName;
/*  256:     */   }
/*  257:     */   
/*  258:     */   public static String getShortName(Class<?> clazz)
/*  259:     */   {
/*  260: 433 */     return getShortName(getQualifiedName(clazz));
/*  261:     */   }
/*  262:     */   
/*  263:     */   public static String getShortNameAsProperty(Class<?> clazz)
/*  264:     */   {
/*  265: 444 */     String shortName = getShortName(clazz);
/*  266: 445 */     int dotIndex = shortName.lastIndexOf('.');
/*  267: 446 */     shortName = dotIndex != -1 ? shortName.substring(dotIndex + 1) : shortName;
/*  268: 447 */     return Introspector.decapitalize(shortName);
/*  269:     */   }
/*  270:     */   
/*  271:     */   public static String getClassFileName(Class<?> clazz)
/*  272:     */   {
/*  273: 457 */     Assert.notNull(clazz, "Class must not be null");
/*  274: 458 */     String className = clazz.getName();
/*  275: 459 */     int lastDotIndex = className.lastIndexOf('.');
/*  276: 460 */     return className.substring(lastDotIndex + 1) + ".class";
/*  277:     */   }
/*  278:     */   
/*  279:     */   public static String getPackageName(Class<?> clazz)
/*  280:     */   {
/*  281: 471 */     Assert.notNull(clazz, "Class must not be null");
/*  282: 472 */     String className = clazz.getName();
/*  283: 473 */     int lastDotIndex = className.lastIndexOf('.');
/*  284: 474 */     return lastDotIndex != -1 ? className.substring(0, lastDotIndex) : "";
/*  285:     */   }
/*  286:     */   
/*  287:     */   public static String getQualifiedName(Class<?> clazz)
/*  288:     */   {
/*  289: 484 */     Assert.notNull(clazz, "Class must not be null");
/*  290: 485 */     if (clazz.isArray()) {
/*  291: 486 */       return getQualifiedNameForArray(clazz);
/*  292:     */     }
/*  293: 489 */     return clazz.getName();
/*  294:     */   }
/*  295:     */   
/*  296:     */   private static String getQualifiedNameForArray(Class<?> clazz)
/*  297:     */   {
/*  298: 500 */     StringBuilder result = new StringBuilder();
/*  299: 501 */     while (clazz.isArray())
/*  300:     */     {
/*  301: 502 */       clazz = clazz.getComponentType();
/*  302: 503 */       result.append("[]");
/*  303:     */     }
/*  304: 505 */     result.insert(0, clazz.getName());
/*  305: 506 */     return result.toString();
/*  306:     */   }
/*  307:     */   
/*  308:     */   public static String getQualifiedMethodName(Method method)
/*  309:     */   {
/*  310: 516 */     Assert.notNull(method, "Method must not be null");
/*  311: 517 */     return method.getDeclaringClass().getName() + "." + method.getName();
/*  312:     */   }
/*  313:     */   
/*  314:     */   public static String getDescriptiveType(Object value)
/*  315:     */   {
/*  316: 528 */     if (value == null) {
/*  317: 529 */       return null;
/*  318:     */     }
/*  319: 531 */     Class<?> clazz = value.getClass();
/*  320: 532 */     if (Proxy.isProxyClass(clazz))
/*  321:     */     {
/*  322: 533 */       StringBuilder result = new StringBuilder(clazz.getName());
/*  323: 534 */       result.append(" implementing ");
/*  324: 535 */       Class[] ifcs = clazz.getInterfaces();
/*  325: 536 */       for (int i = 0; i < ifcs.length; i++)
/*  326:     */       {
/*  327: 537 */         result.append(ifcs[i].getName());
/*  328: 538 */         if (i < ifcs.length - 1) {
/*  329: 539 */           result.append(',');
/*  330:     */         }
/*  331:     */       }
/*  332: 542 */       return result.toString();
/*  333:     */     }
/*  334: 544 */     if (clazz.isArray()) {
/*  335: 545 */       return getQualifiedNameForArray(clazz);
/*  336:     */     }
/*  337: 548 */     return clazz.getName();
/*  338:     */   }
/*  339:     */   
/*  340:     */   public static boolean matchesTypeName(Class<?> clazz, String typeName)
/*  341:     */   {
/*  342: 560 */     return (typeName != null) && ((typeName.equals(clazz.getName())) || (typeName.equals(clazz.getSimpleName())) || ((clazz.isArray()) && (typeName.equals(getQualifiedNameForArray(clazz)))));
/*  343:     */   }
/*  344:     */   
/*  345:     */   public static boolean hasConstructor(Class<?> clazz, Class<?>... paramTypes)
/*  346:     */   {
/*  347: 573 */     return getConstructorIfAvailable(clazz, paramTypes) != null;
/*  348:     */   }
/*  349:     */   
/*  350:     */   public static <T> Constructor<T> getConstructorIfAvailable(Class<T> clazz, Class<?>... paramTypes)
/*  351:     */   {
/*  352: 586 */     Assert.notNull(clazz, "Class must not be null");
/*  353:     */     try
/*  354:     */     {
/*  355: 588 */       return clazz.getConstructor(paramTypes);
/*  356:     */     }
/*  357:     */     catch (NoSuchMethodException localNoSuchMethodException) {}
/*  358: 591 */     return null;
/*  359:     */   }
/*  360:     */   
/*  361:     */   public static boolean hasMethod(Class<?> clazz, String methodName, Class<?>... paramTypes)
/*  362:     */   {
/*  363: 605 */     return getMethodIfAvailable(clazz, methodName, paramTypes) != null;
/*  364:     */   }
/*  365:     */   
/*  366:     */   public static Method getMethodIfAvailable(Class<?> clazz, String methodName, Class<?>... paramTypes)
/*  367:     */   {
/*  368: 619 */     Assert.notNull(clazz, "Class must not be null");
/*  369: 620 */     Assert.notNull(methodName, "Method name must not be null");
/*  370:     */     try
/*  371:     */     {
/*  372: 622 */       return clazz.getMethod(methodName, paramTypes);
/*  373:     */     }
/*  374:     */     catch (NoSuchMethodException localNoSuchMethodException) {}
/*  375: 625 */     return null;
/*  376:     */   }
/*  377:     */   
/*  378:     */   public static int getMethodCountForName(Class<?> clazz, String methodName)
/*  379:     */   {
/*  380: 637 */     Assert.notNull(clazz, "Class must not be null");
/*  381: 638 */     Assert.notNull(methodName, "Method name must not be null");
/*  382: 639 */     int count = 0;
/*  383: 640 */     Method[] declaredMethods = clazz.getDeclaredMethods();
/*  384: 641 */     for (Method method : declaredMethods) {
/*  385: 642 */       if (methodName.equals(method.getName())) {
/*  386: 643 */         count++;
/*  387:     */       }
/*  388:     */     }
/*  389: 646 */     Class[] ifcs = clazz.getInterfaces();
/*  390: 647 */     for (Class<?> ifc : ifcs) {
/*  391: 648 */       count += getMethodCountForName(ifc, methodName);
/*  392:     */     }
/*  393: 650 */     if (clazz.getSuperclass() != null) {
/*  394: 651 */       count += getMethodCountForName(clazz.getSuperclass(), methodName);
/*  395:     */     }
/*  396: 653 */     return count;
/*  397:     */   }
/*  398:     */   
/*  399:     */   public static boolean hasAtLeastOneMethodWithName(Class<?> clazz, String methodName)
/*  400:     */   {
/*  401: 665 */     Assert.notNull(clazz, "Class must not be null");
/*  402: 666 */     Assert.notNull(methodName, "Method name must not be null");
/*  403: 667 */     Method[] declaredMethods = clazz.getDeclaredMethods();
/*  404: 668 */     for (Method method : declaredMethods) {
/*  405: 669 */       if (method.getName().equals(methodName)) {
/*  406: 670 */         return true;
/*  407:     */       }
/*  408:     */     }
/*  409: 673 */     Class[] ifcs = clazz.getInterfaces();
/*  410: 674 */     for (Class<?> ifc : ifcs) {
/*  411: 675 */       if (hasAtLeastOneMethodWithName(ifc, methodName)) {
/*  412: 676 */         return true;
/*  413:     */       }
/*  414:     */     }
/*  415: 679 */     return (clazz.getSuperclass() != null) && (hasAtLeastOneMethodWithName(clazz.getSuperclass(), methodName));
/*  416:     */   }
/*  417:     */   
/*  418:     */   public static Method getMostSpecificMethod(Method method, Class<?> targetClass)
/*  419:     */   {
/*  420: 700 */     Method specificMethod = null;
/*  421: 701 */     if ((method != null) && (isOverridable(method, targetClass)) && 
/*  422: 702 */       (targetClass != null) && (!targetClass.equals(method.getDeclaringClass()))) {
/*  423: 703 */       specificMethod = ReflectionUtils.findMethod(targetClass, method.getName(), method.getParameterTypes());
/*  424:     */     }
/*  425: 705 */     return specificMethod != null ? specificMethod : method;
/*  426:     */   }
/*  427:     */   
/*  428:     */   private static boolean isOverridable(Method method, Class targetClass)
/*  429:     */   {
/*  430: 714 */     if (Modifier.isPrivate(method.getModifiers())) {
/*  431: 715 */       return false;
/*  432:     */     }
/*  433: 717 */     if ((Modifier.isPublic(method.getModifiers())) || (Modifier.isProtected(method.getModifiers()))) {
/*  434: 718 */       return true;
/*  435:     */     }
/*  436: 720 */     return getPackageName(method.getDeclaringClass()).equals(getPackageName(targetClass));
/*  437:     */   }
/*  438:     */   
/*  439:     */   public static Method getStaticMethod(Class<?> clazz, String methodName, Class<?>... args)
/*  440:     */   {
/*  441: 732 */     Assert.notNull(clazz, "Class must not be null");
/*  442: 733 */     Assert.notNull(methodName, "Method name must not be null");
/*  443:     */     try
/*  444:     */     {
/*  445: 735 */       Method method = clazz.getMethod(methodName, args);
/*  446: 736 */       return Modifier.isStatic(method.getModifiers()) ? method : null;
/*  447:     */     }
/*  448:     */     catch (NoSuchMethodException localNoSuchMethodException) {}
/*  449: 739 */     return null;
/*  450:     */   }
/*  451:     */   
/*  452:     */   public static boolean isPrimitiveWrapper(Class<?> clazz)
/*  453:     */   {
/*  454: 751 */     Assert.notNull(clazz, "Class must not be null");
/*  455: 752 */     return primitiveWrapperTypeMap.containsKey(clazz);
/*  456:     */   }
/*  457:     */   
/*  458:     */   public static boolean isPrimitiveOrWrapper(Class<?> clazz)
/*  459:     */   {
/*  460: 763 */     Assert.notNull(clazz, "Class must not be null");
/*  461: 764 */     return (clazz.isPrimitive()) || (isPrimitiveWrapper(clazz));
/*  462:     */   }
/*  463:     */   
/*  464:     */   public static boolean isPrimitiveArray(Class<?> clazz)
/*  465:     */   {
/*  466: 774 */     Assert.notNull(clazz, "Class must not be null");
/*  467: 775 */     return (clazz.isArray()) && (clazz.getComponentType().isPrimitive());
/*  468:     */   }
/*  469:     */   
/*  470:     */   public static boolean isPrimitiveWrapperArray(Class<?> clazz)
/*  471:     */   {
/*  472: 785 */     Assert.notNull(clazz, "Class must not be null");
/*  473: 786 */     return (clazz.isArray()) && (isPrimitiveWrapper(clazz.getComponentType()));
/*  474:     */   }
/*  475:     */   
/*  476:     */   public static Class<?> resolvePrimitiveIfNecessary(Class<?> clazz)
/*  477:     */   {
/*  478: 796 */     Assert.notNull(clazz, "Class must not be null");
/*  479: 797 */     return (clazz.isPrimitive()) && (clazz != Void.TYPE) ? (Class)primitiveTypeToWrapperMap.get(clazz) : clazz;
/*  480:     */   }
/*  481:     */   
/*  482:     */   public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType)
/*  483:     */   {
/*  484: 810 */     Assert.notNull(lhsType, "Left-hand side type must not be null");
/*  485: 811 */     Assert.notNull(rhsType, "Right-hand side type must not be null");
/*  486: 812 */     if (lhsType.isAssignableFrom(rhsType)) {
/*  487: 813 */       return true;
/*  488:     */     }
/*  489: 815 */     if (lhsType.isPrimitive())
/*  490:     */     {
/*  491: 816 */       Class resolvedPrimitive = (Class)primitiveWrapperTypeMap.get(rhsType);
/*  492: 817 */       if ((resolvedPrimitive != null) && (lhsType.equals(resolvedPrimitive))) {
/*  493: 818 */         return true;
/*  494:     */       }
/*  495:     */     }
/*  496:     */     else
/*  497:     */     {
/*  498: 822 */       Class resolvedWrapper = (Class)primitiveTypeToWrapperMap.get(rhsType);
/*  499: 823 */       if ((resolvedWrapper != null) && (lhsType.isAssignableFrom(resolvedWrapper))) {
/*  500: 824 */         return true;
/*  501:     */       }
/*  502:     */     }
/*  503: 827 */     return false;
/*  504:     */   }
/*  505:     */   
/*  506:     */   public static boolean isAssignableValue(Class<?> type, Object value)
/*  507:     */   {
/*  508: 839 */     Assert.notNull(type, "Type must not be null");
/*  509: 840 */     return type.isPrimitive() ? false : value != null ? isAssignable(type, value.getClass()) : true;
/*  510:     */   }
/*  511:     */   
/*  512:     */   public static String convertResourcePathToClassName(String resourcePath)
/*  513:     */   {
/*  514: 850 */     Assert.notNull(resourcePath, "Resource path must not be null");
/*  515: 851 */     return resourcePath.replace('/', '.');
/*  516:     */   }
/*  517:     */   
/*  518:     */   public static String convertClassNameToResourcePath(String className)
/*  519:     */   {
/*  520: 860 */     Assert.notNull(className, "Class name must not be null");
/*  521: 861 */     return className.replace('.', '/');
/*  522:     */   }
/*  523:     */   
/*  524:     */   public static String addResourcePathToPackagePath(Class<?> clazz, String resourceName)
/*  525:     */   {
/*  526: 881 */     Assert.notNull(resourceName, "Resource name must not be null");
/*  527: 882 */     if (!resourceName.startsWith("/")) {
/*  528: 883 */       return classPackageAsResourcePath(clazz) + "/" + resourceName;
/*  529:     */     }
/*  530: 885 */     return classPackageAsResourcePath(clazz) + resourceName;
/*  531:     */   }
/*  532:     */   
/*  533:     */   public static String classPackageAsResourcePath(Class<?> clazz)
/*  534:     */   {
/*  535: 903 */     if (clazz == null) {
/*  536: 904 */       return "";
/*  537:     */     }
/*  538: 906 */     String className = clazz.getName();
/*  539: 907 */     int packageEndIndex = className.lastIndexOf('.');
/*  540: 908 */     if (packageEndIndex == -1) {
/*  541: 909 */       return "";
/*  542:     */     }
/*  543: 911 */     String packageName = className.substring(0, packageEndIndex);
/*  544: 912 */     return packageName.replace('.', '/');
/*  545:     */   }
/*  546:     */   
/*  547:     */   public static String classNamesToString(Class... classes)
/*  548:     */   {
/*  549: 925 */     return classNamesToString((Collection)Arrays.asList(classes));
/*  550:     */   }
/*  551:     */   
/*  552:     */   public static String classNamesToString(Collection<Class> classes)
/*  553:     */   {
/*  554: 938 */     if (CollectionUtils.isEmpty(classes)) {
/*  555: 939 */       return "[]";
/*  556:     */     }
/*  557: 941 */     StringBuilder sb = new StringBuilder("[");
/*  558: 942 */     for (Iterator<Class> it = classes.iterator(); it.hasNext();)
/*  559:     */     {
/*  560: 943 */       Class clazz = (Class)it.next();
/*  561: 944 */       sb.append(clazz.getName());
/*  562: 945 */       if (it.hasNext()) {
/*  563: 946 */         sb.append(", ");
/*  564:     */       }
/*  565:     */     }
/*  566: 949 */     sb.append("]");
/*  567: 950 */     return sb.toString();
/*  568:     */   }
/*  569:     */   
/*  570:     */   public static Class<?>[] toClassArray(Collection<Class<?>> collection)
/*  571:     */   {
/*  572: 961 */     if (collection == null) {
/*  573: 962 */       return null;
/*  574:     */     }
/*  575: 964 */     return (Class[])collection.toArray(new Class[collection.size()]);
/*  576:     */   }
/*  577:     */   
/*  578:     */   public static Class[] getAllInterfaces(Object instance)
/*  579:     */   {
/*  580: 974 */     Assert.notNull(instance, "Instance must not be null");
/*  581: 975 */     return getAllInterfacesForClass(instance.getClass());
/*  582:     */   }
/*  583:     */   
/*  584:     */   public static Class<?>[] getAllInterfacesForClass(Class<?> clazz)
/*  585:     */   {
/*  586: 986 */     return getAllInterfacesForClass(clazz, null);
/*  587:     */   }
/*  588:     */   
/*  589:     */   public static Class<?>[] getAllInterfacesForClass(Class<?> clazz, ClassLoader classLoader)
/*  590:     */   {
/*  591: 999 */     Set<Class> ifcs = getAllInterfacesForClassAsSet(clazz, classLoader);
/*  592:1000 */     return (Class[])ifcs.toArray(new Class[ifcs.size()]);
/*  593:     */   }
/*  594:     */   
/*  595:     */   public static Set<Class> getAllInterfacesAsSet(Object instance)
/*  596:     */   {
/*  597:1010 */     Assert.notNull(instance, "Instance must not be null");
/*  598:1011 */     return getAllInterfacesForClassAsSet(instance.getClass());
/*  599:     */   }
/*  600:     */   
/*  601:     */   public static Set<Class> getAllInterfacesForClassAsSet(Class clazz)
/*  602:     */   {
/*  603:1022 */     return getAllInterfacesForClassAsSet(clazz, null);
/*  604:     */   }
/*  605:     */   
/*  606:     */   public static Set<Class> getAllInterfacesForClassAsSet(Class clazz, ClassLoader classLoader)
/*  607:     */   {
/*  608:1035 */     Assert.notNull(clazz, "Class must not be null");
/*  609:1036 */     if ((clazz.isInterface()) && (isVisible(clazz, classLoader))) {
/*  610:1037 */       return Collections.singleton(clazz);
/*  611:     */     }
/*  612:1039 */     Set<Class> interfaces = new LinkedHashSet();
/*  613:1040 */     while (clazz != null)
/*  614:     */     {
/*  615:1041 */       Class[] ifcs = clazz.getInterfaces();
/*  616:1042 */       for (Class<?> ifc : ifcs) {
/*  617:1043 */         interfaces.addAll(getAllInterfacesForClassAsSet(ifc, classLoader));
/*  618:     */       }
/*  619:1045 */       clazz = clazz.getSuperclass();
/*  620:     */     }
/*  621:1047 */     return interfaces;
/*  622:     */   }
/*  623:     */   
/*  624:     */   public static Class<?> createCompositeInterface(Class<?>[] interfaces, ClassLoader classLoader)
/*  625:     */   {
/*  626:1060 */     Assert.notEmpty(interfaces, "Interfaces must not be empty");
/*  627:1061 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/*  628:1062 */     return Proxy.getProxyClass(classLoader, interfaces);
/*  629:     */   }
/*  630:     */   
/*  631:     */   public static boolean isVisible(Class<?> clazz, ClassLoader classLoader)
/*  632:     */   {
/*  633:1072 */     if (classLoader == null) {
/*  634:1073 */       return true;
/*  635:     */     }
/*  636:     */     try
/*  637:     */     {
/*  638:1076 */       Class<?> actualClass = classLoader.loadClass(clazz.getName());
/*  639:1077 */       return clazz == actualClass;
/*  640:     */     }
/*  641:     */     catch (ClassNotFoundException localClassNotFoundException) {}
/*  642:1082 */     return false;
/*  643:     */   }
/*  644:     */   
/*  645:     */   public static boolean isCglibProxy(Object object)
/*  646:     */   {
/*  647:1092 */     return isCglibProxyClass(object.getClass());
/*  648:     */   }
/*  649:     */   
/*  650:     */   public static boolean isCglibProxyClass(Class<?> clazz)
/*  651:     */   {
/*  652:1100 */     return (clazz != null) && (isCglibProxyClassName(clazz.getName()));
/*  653:     */   }
/*  654:     */   
/*  655:     */   public static boolean isCglibProxyClassName(String className)
/*  656:     */   {
/*  657:1108 */     return (className != null) && (className.contains("$$"));
/*  658:     */   }
/*  659:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.ClassUtils
 * JD-Core Version:    0.7.0.1
 */