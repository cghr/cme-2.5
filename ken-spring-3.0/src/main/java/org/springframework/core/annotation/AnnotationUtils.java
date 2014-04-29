/*   1:    */ package org.springframework.core.annotation;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.lang.annotation.Annotation;
/*   5:    */ import java.lang.reflect.AnnotatedElement;
/*   6:    */ import java.lang.reflect.Method;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.Arrays;
/*   9:    */ import java.util.HashMap;
/*  10:    */ import java.util.List;
/*  11:    */ import java.util.Map;
/*  12:    */ import java.util.WeakHashMap;
/*  13:    */ import org.springframework.core.BridgeMethodResolver;
/*  14:    */ import org.springframework.core.type.AnnotationMetadata;
/*  15:    */ import org.springframework.core.type.classreading.MetadataReader;
/*  16:    */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*  17:    */ import org.springframework.util.Assert;
/*  18:    */ 
/*  19:    */ public abstract class AnnotationUtils
/*  20:    */ {
/*  21:    */   static final String VALUE = "value";
/*  22: 63 */   private static final Map<Class<?>, Boolean> annotatedInterfaceCache = new WeakHashMap();
/*  23:    */   
/*  24:    */   public static <T extends Annotation> T getAnnotation(AnnotatedElement ae, Class<T> annotationType)
/*  25:    */   {
/*  26: 76 */     T ann = ae.getAnnotation(annotationType);
/*  27: 77 */     if (ann == null) {
/*  28: 78 */       for (Annotation metaAnn : ae.getAnnotations())
/*  29:    */       {
/*  30: 79 */         ann = metaAnn.annotationType().getAnnotation(annotationType);
/*  31: 80 */         if (ann != null) {
/*  32:    */           break;
/*  33:    */         }
/*  34:    */       }
/*  35:    */     }
/*  36: 85 */     return ann;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public static Annotation[] getAnnotations(Method method)
/*  40:    */   {
/*  41: 96 */     return BridgeMethodResolver.findBridgedMethod(method).getAnnotations();
/*  42:    */   }
/*  43:    */   
/*  44:    */   public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType)
/*  45:    */   {
/*  46:108 */     Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
/*  47:109 */     A ann = resolvedMethod.getAnnotation(annotationType);
/*  48:110 */     if (ann == null) {
/*  49:111 */       for (Annotation metaAnn : resolvedMethod.getAnnotations())
/*  50:    */       {
/*  51:112 */         ann = metaAnn.annotationType().getAnnotation(annotationType);
/*  52:113 */         if (ann != null) {
/*  53:    */           break;
/*  54:    */         }
/*  55:    */       }
/*  56:    */     }
/*  57:118 */     return ann;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public static <A extends Annotation> A findAnnotation(Method method, Class<A> annotationType)
/*  61:    */   {
/*  62:130 */     A annotation = getAnnotation(method, annotationType);
/*  63:131 */     Class<?> cl = method.getDeclaringClass();
/*  64:132 */     if (annotation == null) {
/*  65:133 */       annotation = searchOnInterfaces(method, annotationType, cl.getInterfaces());
/*  66:    */     }
/*  67:135 */     while (annotation == null)
/*  68:    */     {
/*  69:136 */       cl = cl.getSuperclass();
/*  70:137 */       if ((cl == null) || (cl == Object.class)) {
/*  71:    */         break;
/*  72:    */       }
/*  73:    */       try
/*  74:    */       {
/*  75:141 */         Method equivalentMethod = cl.getDeclaredMethod(method.getName(), method.getParameterTypes());
/*  76:142 */         annotation = getAnnotation(equivalentMethod, annotationType);
/*  77:143 */         if (annotation == null) {
/*  78:144 */           annotation = searchOnInterfaces(method, annotationType, cl.getInterfaces());
/*  79:    */         }
/*  80:    */       }
/*  81:    */       catch (NoSuchMethodException localNoSuchMethodException) {}
/*  82:    */     }
/*  83:151 */     return annotation;
/*  84:    */   }
/*  85:    */   
/*  86:    */   private static <A extends Annotation> A searchOnInterfaces(Method method, Class<A> annotationType, Class<?>[] ifcs)
/*  87:    */   {
/*  88:155 */     A annotation = null;
/*  89:156 */     for (Class<?> iface : ifcs) {
/*  90:157 */       if (isInterfaceWithAnnotatedMethods(iface))
/*  91:    */       {
/*  92:    */         try
/*  93:    */         {
/*  94:159 */           Method equivalentMethod = iface.getMethod(method.getName(), method.getParameterTypes());
/*  95:160 */           annotation = getAnnotation(equivalentMethod, annotationType);
/*  96:    */         }
/*  97:    */         catch (NoSuchMethodException localNoSuchMethodException) {}
/*  98:165 */         if (annotation != null) {
/*  99:    */           break;
/* 100:    */         }
/* 101:    */       }
/* 102:    */     }
/* 103:170 */     return annotation;
/* 104:    */   }
/* 105:    */   
/* 106:    */   private static boolean isInterfaceWithAnnotatedMethods(Class<?> iface)
/* 107:    */   {
/* 108:174 */     synchronized (annotatedInterfaceCache)
/* 109:    */     {
/* 110:175 */       Boolean flag = (Boolean)annotatedInterfaceCache.get(iface);
/* 111:176 */       if (flag != null) {
/* 112:177 */         return flag.booleanValue();
/* 113:    */       }
/* 114:179 */       boolean found = false;
/* 115:180 */       for (Method ifcMethod : iface.getMethods()) {
/* 116:181 */         if (ifcMethod.getAnnotations().length > 0)
/* 117:    */         {
/* 118:182 */           found = true;
/* 119:183 */           break;
/* 120:    */         }
/* 121:    */       }
/* 122:186 */       annotatedInterfaceCache.put(iface, Boolean.valueOf(found));
/* 123:187 */       return found;
/* 124:    */     }
/* 125:    */   }
/* 126:    */   
/* 127:    */   public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType)
/* 128:    */   {
/* 129:207 */     Assert.notNull(clazz, "Class must not be null");
/* 130:208 */     A annotation = clazz.getAnnotation(annotationType);
/* 131:209 */     if (annotation != null) {
/* 132:210 */       return annotation;
/* 133:    */     }
/* 134:212 */     for (Class<?> ifc : clazz.getInterfaces())
/* 135:    */     {
/* 136:213 */       annotation = findAnnotation(ifc, annotationType);
/* 137:214 */       if (annotation != null) {
/* 138:215 */         return annotation;
/* 139:    */       }
/* 140:    */     }
/* 141:218 */     if (!Annotation.class.isAssignableFrom(clazz)) {
/* 142:219 */       for (Annotation ann : clazz.getAnnotations())
/* 143:    */       {
/* 144:220 */         annotation = findAnnotation(ann.annotationType(), annotationType);
/* 145:221 */         if (annotation != null) {
/* 146:222 */           return annotation;
/* 147:    */         }
/* 148:    */       }
/* 149:    */     }
/* 150:226 */     Class<?> superClass = clazz.getSuperclass();
/* 151:227 */     if ((superClass == null) || (superClass == Object.class)) {
/* 152:228 */       return null;
/* 153:    */     }
/* 154:230 */     return findAnnotation(superClass, annotationType);
/* 155:    */   }
/* 156:    */   
/* 157:    */   public static Class<?> findAnnotationDeclaringClass(Class<? extends Annotation> annotationType, Class<?> clazz)
/* 158:    */   {
/* 159:253 */     Assert.notNull(annotationType, "Annotation type must not be null");
/* 160:254 */     if ((clazz == null) || (clazz.equals(Object.class))) {
/* 161:255 */       return null;
/* 162:    */     }
/* 163:257 */     return isAnnotationDeclaredLocally(annotationType, clazz) ? clazz : 
/* 164:258 */       findAnnotationDeclaringClass(annotationType, clazz.getSuperclass());
/* 165:    */   }
/* 166:    */   
/* 167:    */   public static boolean isAnnotationDeclaredLocally(Class<? extends Annotation> annotationType, Class<?> clazz)
/* 168:    */   {
/* 169:276 */     Assert.notNull(annotationType, "Annotation type must not be null");
/* 170:277 */     Assert.notNull(clazz, "Class must not be null");
/* 171:278 */     boolean declaredLocally = false;
/* 172:279 */     for (Annotation annotation : Arrays.asList(clazz.getDeclaredAnnotations())) {
/* 173:280 */       if (annotation.annotationType().equals(annotationType))
/* 174:    */       {
/* 175:281 */         declaredLocally = true;
/* 176:282 */         break;
/* 177:    */       }
/* 178:    */     }
/* 179:285 */     return declaredLocally;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public static boolean isAnnotationInherited(Class<? extends Annotation> annotationType, Class<?> clazz)
/* 183:    */   {
/* 184:304 */     Assert.notNull(annotationType, "Annotation type must not be null");
/* 185:305 */     Assert.notNull(clazz, "Class must not be null");
/* 186:306 */     return (clazz.isAnnotationPresent(annotationType)) && (!isAnnotationDeclaredLocally(annotationType, clazz));
/* 187:    */   }
/* 188:    */   
/* 189:    */   public static Map<String, Object> getAnnotationAttributes(Annotation annotation)
/* 190:    */   {
/* 191:316 */     return getAnnotationAttributes(annotation, false);
/* 192:    */   }
/* 193:    */   
/* 194:    */   public static Map<String, Object> getAnnotationAttributes(Annotation annotation, boolean classValuesAsString)
/* 195:    */   {
/* 196:328 */     Map<String, Object> attrs = new HashMap();
/* 197:329 */     Method[] methods = annotation.annotationType().getDeclaredMethods();
/* 198:330 */     for (Method method : methods) {
/* 199:331 */       if ((method.getParameterTypes().length == 0) && (method.getReturnType() != Void.TYPE)) {
/* 200:    */         try
/* 201:    */         {
/* 202:333 */           Object value = method.invoke(annotation, new Object[0]);
/* 203:334 */           if (classValuesAsString) {
/* 204:335 */             if ((value instanceof Class))
/* 205:    */             {
/* 206:336 */               value = ((Class)value).getName();
/* 207:    */             }
/* 208:338 */             else if ((value instanceof Class[]))
/* 209:    */             {
/* 210:339 */               Class[] clazzArray = (Class[])value;
/* 211:340 */               String[] newValue = new String[clazzArray.length];
/* 212:341 */               for (int i = 0; i < clazzArray.length; i++) {
/* 213:342 */                 newValue[i] = clazzArray[i].getName();
/* 214:    */               }
/* 215:344 */               value = newValue;
/* 216:    */             }
/* 217:    */           }
/* 218:347 */           attrs.put(method.getName(), value);
/* 219:    */         }
/* 220:    */         catch (Exception ex)
/* 221:    */         {
/* 222:350 */           throw new IllegalStateException("Could not obtain annotation attribute values", ex);
/* 223:    */         }
/* 224:    */       }
/* 225:    */     }
/* 226:354 */     return attrs;
/* 227:    */   }
/* 228:    */   
/* 229:    */   public static List<Map<String, Object>> findAllAnnotationAttributes(Class<? extends Annotation> targetAnnotation, String annotatedClassName, boolean classValuesAsString, MetadataReaderFactory metadataReaderFactory)
/* 230:    */     throws IOException
/* 231:    */   {
/* 232:373 */     List<Map<String, Object>> allAttribs = new ArrayList();
/* 233:    */     
/* 234:375 */     MetadataReader reader = metadataReaderFactory.getMetadataReader(annotatedClassName);
/* 235:376 */     AnnotationMetadata metadata = reader.getAnnotationMetadata();
/* 236:377 */     String targetAnnotationType = targetAnnotation.getName();
/* 237:379 */     for (String annotationType : metadata.getAnnotationTypes()) {
/* 238:380 */       if (!annotationType.equals(targetAnnotationType))
/* 239:    */       {
/* 240:383 */         MetadataReader metaReader = metadataReaderFactory.getMetadataReader(annotationType);
/* 241:384 */         Map<String, Object> targetAttribs = 
/* 242:385 */           metaReader.getAnnotationMetadata().getAnnotationAttributes(targetAnnotationType, classValuesAsString);
/* 243:386 */         if (targetAttribs != null) {
/* 244:387 */           allAttribs.add(targetAttribs);
/* 245:    */         }
/* 246:    */       }
/* 247:    */     }
/* 248:391 */     Map<String, Object> localAttribs = 
/* 249:392 */       metadata.getAnnotationAttributes(targetAnnotationType, classValuesAsString);
/* 250:393 */     if (localAttribs != null) {
/* 251:394 */       allAttribs.add(localAttribs);
/* 252:    */     }
/* 253:397 */     return allAttribs;
/* 254:    */   }
/* 255:    */   
/* 256:    */   public static Object getValue(Annotation annotation)
/* 257:    */   {
/* 258:408 */     return getValue(annotation, "value");
/* 259:    */   }
/* 260:    */   
/* 261:    */   public static Object getValue(Annotation annotation, String attributeName)
/* 262:    */   {
/* 263:    */     try
/* 264:    */     {
/* 265:420 */       Method method = annotation.annotationType().getDeclaredMethod(attributeName, new Class[0]);
/* 266:421 */       return method.invoke(annotation, new Object[0]);
/* 267:    */     }
/* 268:    */     catch (Exception localException) {}
/* 269:424 */     return null;
/* 270:    */   }
/* 271:    */   
/* 272:    */   public static Object getDefaultValue(Annotation annotation)
/* 273:    */   {
/* 274:436 */     return getDefaultValue(annotation, "value");
/* 275:    */   }
/* 276:    */   
/* 277:    */   public static Object getDefaultValue(Annotation annotation, String attributeName)
/* 278:    */   {
/* 279:447 */     return getDefaultValue(annotation.annotationType(), attributeName);
/* 280:    */   }
/* 281:    */   
/* 282:    */   public static Object getDefaultValue(Class<? extends Annotation> annotationType)
/* 283:    */   {
/* 284:458 */     return getDefaultValue(annotationType, "value");
/* 285:    */   }
/* 286:    */   
/* 287:    */   public static Object getDefaultValue(Class<? extends Annotation> annotationType, String attributeName)
/* 288:    */   {
/* 289:    */     try
/* 290:    */     {
/* 291:470 */       Method method = annotationType.getDeclaredMethod(attributeName, new Class[0]);
/* 292:471 */       return method.getDefaultValue();
/* 293:    */     }
/* 294:    */     catch (Exception localException) {}
/* 295:474 */     return null;
/* 296:    */   }
/* 297:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.annotation.AnnotationUtils
 * JD-Core Version:    0.7.0.1
 */