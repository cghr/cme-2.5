/*   1:    */ package org.springframework.beans;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyDescriptor;
/*   4:    */ import java.beans.PropertyEditor;
/*   5:    */ import java.lang.reflect.Constructor;
/*   6:    */ import java.lang.reflect.InvocationTargetException;
/*   7:    */ import java.lang.reflect.Method;
/*   8:    */ import java.lang.reflect.Modifier;
/*   9:    */ import java.net.URI;
/*  10:    */ import java.net.URL;
/*  11:    */ import java.util.Arrays;
/*  12:    */ import java.util.Collections;
/*  13:    */ import java.util.Date;
/*  14:    */ import java.util.List;
/*  15:    */ import java.util.Locale;
/*  16:    */ import java.util.Map;
/*  17:    */ import java.util.WeakHashMap;
/*  18:    */ import org.apache.commons.logging.Log;
/*  19:    */ import org.apache.commons.logging.LogFactory;
/*  20:    */ import org.springframework.core.MethodParameter;
/*  21:    */ import org.springframework.util.Assert;
/*  22:    */ import org.springframework.util.ClassUtils;
/*  23:    */ import org.springframework.util.ReflectionUtils;
/*  24:    */ import org.springframework.util.StringUtils;
/*  25:    */ 
/*  26:    */ public abstract class BeanUtils
/*  27:    */ {
/*  28: 58 */   private static final Log logger = LogFactory.getLog(BeanUtils.class);
/*  29: 61 */   private static final Map<Class<?>, Boolean> unknownEditorTypes = Collections.synchronizedMap(new WeakHashMap());
/*  30:    */   
/*  31:    */   public static <T> T instantiate(Class<T> clazz)
/*  32:    */     throws BeanInstantiationException
/*  33:    */   {
/*  34: 73 */     Assert.notNull(clazz, "Class must not be null");
/*  35: 74 */     if (clazz.isInterface()) {
/*  36: 75 */       throw new BeanInstantiationException(clazz, "Specified class is an interface");
/*  37:    */     }
/*  38:    */     try
/*  39:    */     {
/*  40: 78 */       return clazz.newInstance();
/*  41:    */     }
/*  42:    */     catch (InstantiationException ex)
/*  43:    */     {
/*  44: 81 */       throw new BeanInstantiationException(clazz, "Is it an abstract class?", ex);
/*  45:    */     }
/*  46:    */     catch (IllegalAccessException ex)
/*  47:    */     {
/*  48: 84 */       throw new BeanInstantiationException(clazz, "Is the constructor accessible?", ex);
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static <T> T instantiateClass(Class<T> clazz)
/*  53:    */     throws BeanInstantiationException
/*  54:    */   {
/*  55: 99 */     Assert.notNull(clazz, "Class must not be null");
/*  56:100 */     if (clazz.isInterface()) {
/*  57:101 */       throw new BeanInstantiationException(clazz, "Specified class is an interface");
/*  58:    */     }
/*  59:    */     try
/*  60:    */     {
/*  61:104 */       return instantiateClass(clazz.getDeclaredConstructor(new Class[0]), new Object[0]);
/*  62:    */     }
/*  63:    */     catch (NoSuchMethodException ex)
/*  64:    */     {
/*  65:107 */       throw new BeanInstantiationException(clazz, "No default constructor found", ex);
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static <T> T instantiateClass(Class<?> clazz, Class<T> assignableTo)
/*  70:    */     throws BeanInstantiationException
/*  71:    */   {
/*  72:128 */     Assert.isAssignable(assignableTo, clazz);
/*  73:129 */     return instantiateClass(clazz);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public static <T> T instantiateClass(Constructor<T> ctor, Object... args)
/*  77:    */     throws BeanInstantiationException
/*  78:    */   {
/*  79:144 */     Assert.notNull(ctor, "Constructor must not be null");
/*  80:    */     try
/*  81:    */     {
/*  82:146 */       ReflectionUtils.makeAccessible(ctor);
/*  83:147 */       return ctor.newInstance(args);
/*  84:    */     }
/*  85:    */     catch (InstantiationException ex)
/*  86:    */     {
/*  87:150 */       throw new BeanInstantiationException(ctor.getDeclaringClass(), 
/*  88:151 */         "Is it an abstract class?", ex);
/*  89:    */     }
/*  90:    */     catch (IllegalAccessException ex)
/*  91:    */     {
/*  92:154 */       throw new BeanInstantiationException(ctor.getDeclaringClass(), 
/*  93:155 */         "Is the constructor accessible?", ex);
/*  94:    */     }
/*  95:    */     catch (IllegalArgumentException ex)
/*  96:    */     {
/*  97:158 */       throw new BeanInstantiationException(ctor.getDeclaringClass(), 
/*  98:159 */         "Illegal arguments for constructor", ex);
/*  99:    */     }
/* 100:    */     catch (InvocationTargetException ex)
/* 101:    */     {
/* 102:162 */       throw new BeanInstantiationException(ctor.getDeclaringClass(), 
/* 103:163 */         "Constructor threw exception", ex.getTargetException());
/* 104:    */     }
/* 105:    */   }
/* 106:    */   
/* 107:    */   public static Method findMethod(Class<?> clazz, String methodName, Class<?>... paramTypes)
/* 108:    */   {
/* 109:    */     try
/* 110:    */     {
/* 111:183 */       return clazz.getMethod(methodName, paramTypes);
/* 112:    */     }
/* 113:    */     catch (NoSuchMethodException localNoSuchMethodException) {}
/* 114:186 */     return findDeclaredMethod(clazz, methodName, paramTypes);
/* 115:    */   }
/* 116:    */   
/* 117:    */   public static Method findDeclaredMethod(Class<?> clazz, String methodName, Class<?>[] paramTypes)
/* 118:    */   {
/* 119:    */     try
/* 120:    */     {
/* 121:203 */       return clazz.getDeclaredMethod(methodName, paramTypes);
/* 122:    */     }
/* 123:    */     catch (NoSuchMethodException localNoSuchMethodException)
/* 124:    */     {
/* 125:206 */       if (clazz.getSuperclass() != null) {
/* 126:207 */         return findDeclaredMethod(clazz.getSuperclass(), methodName, paramTypes);
/* 127:    */       }
/* 128:    */     }
/* 129:209 */     return null;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public static Method findMethodWithMinimalParameters(Class<?> clazz, String methodName)
/* 133:    */     throws IllegalArgumentException
/* 134:    */   {
/* 135:231 */     Method targetMethod = findMethodWithMinimalParameters(clazz.getMethods(), methodName);
/* 136:232 */     if (targetMethod == null) {
/* 137:233 */       targetMethod = findDeclaredMethodWithMinimalParameters(clazz, methodName);
/* 138:    */     }
/* 139:235 */     return targetMethod;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public static Method findDeclaredMethodWithMinimalParameters(Class<?> clazz, String methodName)
/* 143:    */     throws IllegalArgumentException
/* 144:    */   {
/* 145:253 */     Method targetMethod = findMethodWithMinimalParameters(clazz.getDeclaredMethods(), methodName);
/* 146:254 */     if ((targetMethod == null) && (clazz.getSuperclass() != null)) {
/* 147:255 */       targetMethod = findDeclaredMethodWithMinimalParameters(clazz.getSuperclass(), methodName);
/* 148:    */     }
/* 149:257 */     return targetMethod;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public static Method findMethodWithMinimalParameters(Method[] methods, String methodName)
/* 153:    */     throws IllegalArgumentException
/* 154:    */   {
/* 155:272 */     Method targetMethod = null;
/* 156:273 */     int numMethodsFoundWithCurrentMinimumArgs = 0;
/* 157:274 */     Method[] arrayOfMethod = methods;int j = methods.length;
/* 158:274 */     for (int i = 0; i < j; i++)
/* 159:    */     {
/* 160:274 */       Method method = arrayOfMethod[i];
/* 161:275 */       if (method.getName().equals(methodName))
/* 162:    */       {
/* 163:276 */         int numParams = method.getParameterTypes().length;
/* 164:277 */         if ((targetMethod == null) || (numParams < targetMethod.getParameterTypes().length))
/* 165:    */         {
/* 166:278 */           targetMethod = method;
/* 167:279 */           numMethodsFoundWithCurrentMinimumArgs = 1;
/* 168:    */         }
/* 169:282 */         else if (targetMethod.getParameterTypes().length == numParams)
/* 170:    */         {
/* 171:284 */           numMethodsFoundWithCurrentMinimumArgs++;
/* 172:    */         }
/* 173:    */       }
/* 174:    */     }
/* 175:289 */     if (numMethodsFoundWithCurrentMinimumArgs > 1) {
/* 176:290 */       throw new IllegalArgumentException("Cannot resolve method '" + methodName + 
/* 177:291 */         "' to a unique method. Attempted to resolve to overloaded method with " + 
/* 178:292 */         "the least number of parameters, but there were " + 
/* 179:293 */         numMethodsFoundWithCurrentMinimumArgs + " candidates.");
/* 180:    */     }
/* 181:295 */     return targetMethod;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public static Method resolveSignature(String signature, Class<?> clazz)
/* 185:    */   {
/* 186:317 */     Assert.hasText(signature, "'signature' must not be empty");
/* 187:318 */     Assert.notNull(clazz, "Class must not be null");
/* 188:    */     
/* 189:320 */     int firstParen = signature.indexOf("(");
/* 190:321 */     int lastParen = signature.indexOf(")");
/* 191:323 */     if ((firstParen > -1) && (lastParen == -1)) {
/* 192:324 */       throw new IllegalArgumentException("Invalid method signature '" + signature + 
/* 193:325 */         "': expected closing ')' for args list");
/* 194:    */     }
/* 195:327 */     if ((lastParen > -1) && (firstParen == -1)) {
/* 196:328 */       throw new IllegalArgumentException("Invalid method signature '" + signature + 
/* 197:329 */         "': expected opening '(' for args list");
/* 198:    */     }
/* 199:331 */     if ((firstParen == -1) && (lastParen == -1)) {
/* 200:332 */       return findMethodWithMinimalParameters(clazz, signature);
/* 201:    */     }
/* 202:335 */     String methodName = signature.substring(0, firstParen);
/* 203:336 */     String[] parameterTypeNames = 
/* 204:337 */       StringUtils.commaDelimitedListToStringArray(signature.substring(firstParen + 1, lastParen));
/* 205:338 */     Class[] parameterTypes = new Class[parameterTypeNames.length];
/* 206:339 */     for (int i = 0; i < parameterTypeNames.length; i++)
/* 207:    */     {
/* 208:340 */       String parameterTypeName = parameterTypeNames[i].trim();
/* 209:    */       try
/* 210:    */       {
/* 211:342 */         parameterTypes[i] = ClassUtils.forName(parameterTypeName, clazz.getClassLoader());
/* 212:    */       }
/* 213:    */       catch (Throwable ex)
/* 214:    */       {
/* 215:345 */         throw new IllegalArgumentException("Invalid method signature: unable to resolve type [" + 
/* 216:346 */           parameterTypeName + "] for argument " + i + ". Root cause: " + ex);
/* 217:    */       }
/* 218:    */     }
/* 219:349 */     return findMethod(clazz, methodName, parameterTypes);
/* 220:    */   }
/* 221:    */   
/* 222:    */   public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz)
/* 223:    */     throws BeansException
/* 224:    */   {
/* 225:361 */     CachedIntrospectionResults cr = CachedIntrospectionResults.forClass(clazz);
/* 226:362 */     return cr.getPropertyDescriptors();
/* 227:    */   }
/* 228:    */   
/* 229:    */   public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String propertyName)
/* 230:    */     throws BeansException
/* 231:    */   {
/* 232:375 */     CachedIntrospectionResults cr = CachedIntrospectionResults.forClass(clazz);
/* 233:376 */     return cr.getPropertyDescriptor(propertyName);
/* 234:    */   }
/* 235:    */   
/* 236:    */   public static PropertyDescriptor findPropertyForMethod(Method method)
/* 237:    */     throws BeansException
/* 238:    */   {
/* 239:388 */     Assert.notNull(method, "Method must not be null");
/* 240:389 */     PropertyDescriptor[] pds = getPropertyDescriptors(method.getDeclaringClass());
/* 241:390 */     for (PropertyDescriptor pd : pds) {
/* 242:391 */       if ((method.equals(pd.getReadMethod())) || (method.equals(pd.getWriteMethod()))) {
/* 243:392 */         return pd;
/* 244:    */       }
/* 245:    */     }
/* 246:395 */     return null;
/* 247:    */   }
/* 248:    */   
/* 249:    */   public static PropertyEditor findEditorByConvention(Class<?> targetType)
/* 250:    */   {
/* 251:408 */     if ((targetType == null) || (targetType.isArray()) || (unknownEditorTypes.containsKey(targetType))) {
/* 252:409 */       return null;
/* 253:    */     }
/* 254:411 */     ClassLoader cl = targetType.getClassLoader();
/* 255:412 */     if (cl == null) {
/* 256:    */       try
/* 257:    */       {
/* 258:414 */         cl = ClassLoader.getSystemClassLoader();
/* 259:415 */         if (cl == null) {
/* 260:416 */           return null;
/* 261:    */         }
/* 262:    */       }
/* 263:    */       catch (Throwable ex)
/* 264:    */       {
/* 265:421 */         if (logger.isDebugEnabled()) {
/* 266:422 */           logger.debug("Could not access system ClassLoader: " + ex);
/* 267:    */         }
/* 268:424 */         return null;
/* 269:    */       }
/* 270:    */     }
/* 271:427 */     String editorName = targetType.getName() + "Editor";
/* 272:    */     try
/* 273:    */     {
/* 274:429 */       Class<?> editorClass = cl.loadClass(editorName);
/* 275:430 */       if (!PropertyEditor.class.isAssignableFrom(editorClass))
/* 276:    */       {
/* 277:431 */         if (logger.isWarnEnabled()) {
/* 278:432 */           logger.warn("Editor class [" + editorName + 
/* 279:433 */             "] does not implement [java.beans.PropertyEditor] interface");
/* 280:    */         }
/* 281:435 */         unknownEditorTypes.put(targetType, Boolean.TRUE);
/* 282:436 */         return null;
/* 283:    */       }
/* 284:438 */       return (PropertyEditor)instantiateClass(editorClass);
/* 285:    */     }
/* 286:    */     catch (ClassNotFoundException localClassNotFoundException)
/* 287:    */     {
/* 288:441 */       if (logger.isDebugEnabled()) {
/* 289:442 */         logger.debug("No property editor [" + editorName + "] found for type " + 
/* 290:443 */           targetType.getName() + " according to 'Editor' suffix convention");
/* 291:    */       }
/* 292:445 */       unknownEditorTypes.put(targetType, Boolean.TRUE);
/* 293:    */     }
/* 294:446 */     return null;
/* 295:    */   }
/* 296:    */   
/* 297:    */   public static Class<?> findPropertyType(String propertyName, Class<?>[] beanClasses)
/* 298:    */   {
/* 299:458 */     if (beanClasses != null) {
/* 300:459 */       for (Class<?> beanClass : beanClasses)
/* 301:    */       {
/* 302:460 */         PropertyDescriptor pd = getPropertyDescriptor(beanClass, propertyName);
/* 303:461 */         if (pd != null) {
/* 304:462 */           return pd.getPropertyType();
/* 305:    */         }
/* 306:    */       }
/* 307:    */     }
/* 308:466 */     return Object.class;
/* 309:    */   }
/* 310:    */   
/* 311:    */   public static MethodParameter getWriteMethodParameter(PropertyDescriptor pd)
/* 312:    */   {
/* 313:476 */     if ((pd instanceof GenericTypeAwarePropertyDescriptor)) {
/* 314:477 */       return new MethodParameter(
/* 315:478 */         ((GenericTypeAwarePropertyDescriptor)pd).getWriteMethodParameter());
/* 316:    */     }
/* 317:481 */     return new MethodParameter(pd.getWriteMethod(), 0);
/* 318:    */   }
/* 319:    */   
/* 320:    */   public static boolean isSimpleProperty(Class<?> clazz)
/* 321:    */   {
/* 322:496 */     Assert.notNull(clazz, "Class must not be null");
/* 323:497 */     return (isSimpleValueType(clazz)) || ((clazz.isArray()) && (isSimpleValueType(clazz.getComponentType())));
/* 324:    */   }
/* 325:    */   
/* 326:    */   public static boolean isSimpleValueType(Class<?> clazz)
/* 327:    */   {
/* 328:513 */     return (ClassUtils.isPrimitiveOrWrapper(clazz)) || (clazz.isEnum()) || (CharSequence.class.isAssignableFrom(clazz)) || (Number.class.isAssignableFrom(clazz)) || (Date.class.isAssignableFrom(clazz)) || (clazz.equals(URI.class)) || (clazz.equals(URL.class)) || (clazz.equals(Locale.class)) || (clazz.equals(Class.class));
/* 329:    */   }
/* 330:    */   
/* 331:    */   public static void copyProperties(Object source, Object target)
/* 332:    */     throws BeansException
/* 333:    */   {
/* 334:530 */     copyProperties(source, target, null, null);
/* 335:    */   }
/* 336:    */   
/* 337:    */   public static void copyProperties(Object source, Object target, Class<?> editable)
/* 338:    */     throws BeansException
/* 339:    */   {
/* 340:550 */     copyProperties(source, target, editable, null);
/* 341:    */   }
/* 342:    */   
/* 343:    */   public static void copyProperties(Object source, Object target, String[] ignoreProperties)
/* 344:    */     throws BeansException
/* 345:    */   {
/* 346:570 */     copyProperties(source, target, null, ignoreProperties);
/* 347:    */   }
/* 348:    */   
/* 349:    */   private static void copyProperties(Object source, Object target, Class<?> editable, String[] ignoreProperties)
/* 350:    */     throws BeansException
/* 351:    */   {
/* 352:588 */     Assert.notNull(source, "Source must not be null");
/* 353:589 */     Assert.notNull(target, "Target must not be null");
/* 354:    */     
/* 355:591 */     Class<?> actualEditable = target.getClass();
/* 356:592 */     if (editable != null)
/* 357:    */     {
/* 358:593 */       if (!editable.isInstance(target)) {
/* 359:594 */         throw new IllegalArgumentException("Target class [" + target.getClass().getName() + 
/* 360:595 */           "] not assignable to Editable class [" + editable.getName() + "]");
/* 361:    */       }
/* 362:597 */       actualEditable = editable;
/* 363:    */     }
/* 364:599 */     PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
/* 365:600 */     List<String> ignoreList = ignoreProperties != null ? Arrays.asList(ignoreProperties) : null;
/* 366:602 */     for (PropertyDescriptor targetPd : targetPds) {
/* 367:603 */       if ((targetPd.getWriteMethod() != null) && (
/* 368:604 */         (ignoreProperties == null) || (!ignoreList.contains(targetPd.getName()))))
/* 369:    */       {
/* 370:605 */         PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
/* 371:606 */         if ((sourcePd != null) && (sourcePd.getReadMethod() != null)) {
/* 372:    */           try
/* 373:    */           {
/* 374:608 */             Method readMethod = sourcePd.getReadMethod();
/* 375:609 */             if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
/* 376:610 */               readMethod.setAccessible(true);
/* 377:    */             }
/* 378:612 */             Object value = readMethod.invoke(source, new Object[0]);
/* 379:613 */             Method writeMethod = targetPd.getWriteMethod();
/* 380:614 */             if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
/* 381:615 */               writeMethod.setAccessible(true);
/* 382:    */             }
/* 383:617 */             writeMethod.invoke(target, new Object[] { value });
/* 384:    */           }
/* 385:    */           catch (Throwable ex)
/* 386:    */           {
/* 387:620 */             throw new FatalBeanException("Could not copy properties from source to target", ex);
/* 388:    */           }
/* 389:    */         }
/* 390:    */       }
/* 391:    */     }
/* 392:    */   }
/* 393:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.BeanUtils
 * JD-Core Version:    0.7.0.1
 */