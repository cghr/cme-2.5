/*   1:    */ package org.springframework.core;
/*   2:    */ 
/*   3:    */ import java.lang.ref.Reference;
/*   4:    */ import java.lang.ref.WeakReference;
/*   5:    */ import java.lang.reflect.Array;
/*   6:    */ import java.lang.reflect.GenericArrayType;
/*   7:    */ import java.lang.reflect.Method;
/*   8:    */ import java.lang.reflect.ParameterizedType;
/*   9:    */ import java.lang.reflect.Type;
/*  10:    */ import java.lang.reflect.TypeVariable;
/*  11:    */ import java.lang.reflect.WildcardType;
/*  12:    */ import java.util.Collections;
/*  13:    */ import java.util.HashMap;
/*  14:    */ import java.util.Map;
/*  15:    */ import java.util.WeakHashMap;
/*  16:    */ import org.springframework.util.Assert;
/*  17:    */ 
/*  18:    */ public abstract class GenericTypeResolver
/*  19:    */ {
/*  20: 52 */   private static final Map<Class, Reference<Map<TypeVariable, Type>>> typeVariableCache = Collections.synchronizedMap(new WeakHashMap());
/*  21:    */   
/*  22:    */   public static Type getTargetType(MethodParameter methodParam)
/*  23:    */   {
/*  24: 61 */     Assert.notNull(methodParam, "MethodParameter must not be null");
/*  25: 62 */     if (methodParam.getConstructor() != null) {
/*  26: 63 */       return methodParam.getConstructor().getGenericParameterTypes()[methodParam.getParameterIndex()];
/*  27:    */     }
/*  28: 66 */     if (methodParam.getParameterIndex() >= 0) {
/*  29: 67 */       return methodParam.getMethod().getGenericParameterTypes()[methodParam.getParameterIndex()];
/*  30:    */     }
/*  31: 70 */     return methodParam.getMethod().getGenericReturnType();
/*  32:    */   }
/*  33:    */   
/*  34:    */   public static Class<?> resolveParameterType(MethodParameter methodParam, Class clazz)
/*  35:    */   {
/*  36: 82 */     Type genericType = getTargetType(methodParam);
/*  37: 83 */     Assert.notNull(clazz, "Class must not be null");
/*  38: 84 */     Map<TypeVariable, Type> typeVariableMap = getTypeVariableMap(clazz);
/*  39: 85 */     Type rawType = getRawType(genericType, typeVariableMap);
/*  40: 86 */     Class result = (rawType instanceof Class) ? (Class)rawType : methodParam.getParameterType();
/*  41: 87 */     methodParam.setParameterType(result);
/*  42: 88 */     methodParam.typeVariableMap = typeVariableMap;
/*  43: 89 */     return result;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static Class<?> resolveReturnType(Method method, Class clazz)
/*  47:    */   {
/*  48: 99 */     Assert.notNull(method, "Method must not be null");
/*  49:100 */     Type genericType = method.getGenericReturnType();
/*  50:101 */     Assert.notNull(clazz, "Class must not be null");
/*  51:102 */     Map<TypeVariable, Type> typeVariableMap = getTypeVariableMap(clazz);
/*  52:103 */     Type rawType = getRawType(genericType, typeVariableMap);
/*  53:104 */     return (rawType instanceof Class) ? (Class)rawType : method.getReturnType();
/*  54:    */   }
/*  55:    */   
/*  56:    */   public static Class<?> resolveReturnTypeArgument(Method method, Class<?> genericIfc)
/*  57:    */   {
/*  58:117 */     Type returnType = method.getReturnType();
/*  59:118 */     Type genericReturnType = method.getGenericReturnType();
/*  60:119 */     if (returnType.equals(genericIfc)) {
/*  61:120 */       if ((genericReturnType instanceof ParameterizedType))
/*  62:    */       {
/*  63:121 */         ParameterizedType targetType = (ParameterizedType)genericReturnType;
/*  64:122 */         Type[] actualTypeArguments = targetType.getActualTypeArguments();
/*  65:123 */         Type typeArg = actualTypeArguments[0];
/*  66:124 */         if (!(typeArg instanceof WildcardType)) {
/*  67:125 */           return (Class)typeArg;
/*  68:    */         }
/*  69:    */       }
/*  70:    */       else
/*  71:    */       {
/*  72:129 */         return null;
/*  73:    */       }
/*  74:    */     }
/*  75:132 */     return resolveTypeArgument((Class)returnType, genericIfc);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public static Class<?> resolveTypeArgument(Class clazz, Class genericIfc)
/*  79:    */   {
/*  80:144 */     Class[] typeArgs = resolveTypeArguments(clazz, genericIfc);
/*  81:145 */     if (typeArgs == null) {
/*  82:146 */       return null;
/*  83:    */     }
/*  84:148 */     if (typeArgs.length != 1) {
/*  85:149 */       throw new IllegalArgumentException("Expected 1 type argument on generic interface [" + 
/*  86:150 */         genericIfc.getName() + "] but found " + typeArgs.length);
/*  87:    */     }
/*  88:152 */     return typeArgs[0];
/*  89:    */   }
/*  90:    */   
/*  91:    */   public static Class[] resolveTypeArguments(Class clazz, Class genericIfc)
/*  92:    */   {
/*  93:165 */     return doResolveTypeArguments(clazz, clazz, genericIfc);
/*  94:    */   }
/*  95:    */   
/*  96:    */   private static Class[] doResolveTypeArguments(Class ownerClass, Class classToIntrospect, Class genericIfc)
/*  97:    */   {
/*  98:169 */     while (classToIntrospect != null)
/*  99:    */     {
/* 100:170 */       if (genericIfc.isInterface())
/* 101:    */       {
/* 102:171 */         Type[] ifcs = classToIntrospect.getGenericInterfaces();
/* 103:172 */         for (Type ifc : ifcs)
/* 104:    */         {
/* 105:173 */           Class[] result = doResolveTypeArguments(ownerClass, ifc, genericIfc);
/* 106:174 */           if (result != null) {
/* 107:175 */             return result;
/* 108:    */           }
/* 109:    */         }
/* 110:    */       }
/* 111:    */       else
/* 112:    */       {
/* 113:180 */         Class[] result = doResolveTypeArguments(
/* 114:181 */           ownerClass, classToIntrospect.getGenericSuperclass(), genericIfc);
/* 115:182 */         if (result != null) {
/* 116:183 */           return result;
/* 117:    */         }
/* 118:    */       }
/* 119:186 */       classToIntrospect = classToIntrospect.getSuperclass();
/* 120:    */     }
/* 121:188 */     return null;
/* 122:    */   }
/* 123:    */   
/* 124:    */   private static Class[] doResolveTypeArguments(Class ownerClass, Type ifc, Class genericIfc)
/* 125:    */   {
/* 126:192 */     if ((ifc instanceof ParameterizedType))
/* 127:    */     {
/* 128:193 */       ParameterizedType paramIfc = (ParameterizedType)ifc;
/* 129:194 */       Type rawType = paramIfc.getRawType();
/* 130:195 */       if (genericIfc.equals(rawType))
/* 131:    */       {
/* 132:196 */         Type[] typeArgs = paramIfc.getActualTypeArguments();
/* 133:197 */         Class[] result = new Class[typeArgs.length];
/* 134:198 */         for (int i = 0; i < typeArgs.length; i++)
/* 135:    */         {
/* 136:199 */           Type arg = typeArgs[i];
/* 137:200 */           result[i] = extractClass(ownerClass, arg);
/* 138:    */         }
/* 139:202 */         return result;
/* 140:    */       }
/* 141:204 */       if (genericIfc.isAssignableFrom((Class)rawType)) {
/* 142:205 */         return doResolveTypeArguments(ownerClass, (Class)rawType, genericIfc);
/* 143:    */       }
/* 144:    */     }
/* 145:208 */     else if (genericIfc.isAssignableFrom((Class)ifc))
/* 146:    */     {
/* 147:209 */       return doResolveTypeArguments(ownerClass, (Class)ifc, genericIfc);
/* 148:    */     }
/* 149:211 */     return null;
/* 150:    */   }
/* 151:    */   
/* 152:    */   private static Class extractClass(Class ownerClass, Type arg)
/* 153:    */   {
/* 154:218 */     if ((arg instanceof ParameterizedType)) {
/* 155:219 */       return extractClass(ownerClass, ((ParameterizedType)arg).getRawType());
/* 156:    */     }
/* 157:221 */     if ((arg instanceof GenericArrayType))
/* 158:    */     {
/* 159:222 */       GenericArrayType gat = (GenericArrayType)arg;
/* 160:223 */       Type gt = gat.getGenericComponentType();
/* 161:224 */       Class<?> componentClass = extractClass(ownerClass, gt);
/* 162:225 */       return Array.newInstance(componentClass, 0).getClass();
/* 163:    */     }
/* 164:227 */     if ((arg instanceof TypeVariable))
/* 165:    */     {
/* 166:228 */       TypeVariable tv = (TypeVariable)arg;
/* 167:229 */       arg = (Type)getTypeVariableMap(ownerClass).get(tv);
/* 168:230 */       if (arg == null) {
/* 169:231 */         arg = extractBoundForTypeVariable(tv);
/* 170:    */       } else {
/* 171:234 */         arg = extractClass(ownerClass, arg);
/* 172:    */       }
/* 173:    */     }
/* 174:237 */     return (arg instanceof Class) ? (Class)arg : Object.class;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public static Class resolveType(Type genericType, Map<TypeVariable, Type> typeVariableMap)
/* 178:    */   {
/* 179:248 */     Type rawType = getRawType(genericType, typeVariableMap);
/* 180:249 */     return (rawType instanceof Class) ? (Class)rawType : Object.class;
/* 181:    */   }
/* 182:    */   
/* 183:    */   static Type getRawType(Type genericType, Map<TypeVariable, Type> typeVariableMap)
/* 184:    */   {
/* 185:259 */     Type resolvedType = genericType;
/* 186:260 */     if ((genericType instanceof TypeVariable))
/* 187:    */     {
/* 188:261 */       TypeVariable tv = (TypeVariable)genericType;
/* 189:262 */       resolvedType = (Type)typeVariableMap.get(tv);
/* 190:263 */       if (resolvedType == null) {
/* 191:264 */         resolvedType = extractBoundForTypeVariable(tv);
/* 192:    */       }
/* 193:    */     }
/* 194:267 */     if ((resolvedType instanceof ParameterizedType)) {
/* 195:268 */       return ((ParameterizedType)resolvedType).getRawType();
/* 196:    */     }
/* 197:271 */     return resolvedType;
/* 198:    */   }
/* 199:    */   
/* 200:    */   public static Map<TypeVariable, Type> getTypeVariableMap(Class clazz)
/* 201:    */   {
/* 202:281 */     Reference<Map<TypeVariable, Type>> ref = (Reference)typeVariableCache.get(clazz);
/* 203:282 */     Map<TypeVariable, Type> typeVariableMap = ref != null ? (Map)ref.get() : null;
/* 204:284 */     if (typeVariableMap == null)
/* 205:    */     {
/* 206:285 */       typeVariableMap = new HashMap();
/* 207:    */       
/* 208:    */ 
/* 209:288 */       extractTypeVariablesFromGenericInterfaces(clazz.getGenericInterfaces(), typeVariableMap);
/* 210:    */       
/* 211:    */ 
/* 212:291 */       Type genericType = clazz.getGenericSuperclass();
/* 213:292 */       Class type = clazz.getSuperclass();
/* 214:293 */       while ((type != null) && (!Object.class.equals(type)))
/* 215:    */       {
/* 216:294 */         if ((genericType instanceof ParameterizedType))
/* 217:    */         {
/* 218:295 */           ParameterizedType pt = (ParameterizedType)genericType;
/* 219:296 */           populateTypeMapFromParameterizedType(pt, typeVariableMap);
/* 220:    */         }
/* 221:298 */         extractTypeVariablesFromGenericInterfaces(type.getGenericInterfaces(), typeVariableMap);
/* 222:299 */         genericType = type.getGenericSuperclass();
/* 223:300 */         type = type.getSuperclass();
/* 224:    */       }
/* 225:304 */       type = clazz;
/* 226:305 */       while (type.isMemberClass())
/* 227:    */       {
/* 228:306 */         genericType = type.getGenericSuperclass();
/* 229:307 */         if ((genericType instanceof ParameterizedType))
/* 230:    */         {
/* 231:308 */           ParameterizedType pt = (ParameterizedType)genericType;
/* 232:309 */           populateTypeMapFromParameterizedType(pt, typeVariableMap);
/* 233:    */         }
/* 234:311 */         type = type.getEnclosingClass();
/* 235:    */       }
/* 236:314 */       typeVariableCache.put(clazz, new WeakReference(typeVariableMap));
/* 237:    */     }
/* 238:317 */     return typeVariableMap;
/* 239:    */   }
/* 240:    */   
/* 241:    */   static Type extractBoundForTypeVariable(TypeVariable typeVariable)
/* 242:    */   {
/* 243:324 */     Type[] bounds = typeVariable.getBounds();
/* 244:325 */     if (bounds.length == 0) {
/* 245:326 */       return Object.class;
/* 246:    */     }
/* 247:328 */     Type bound = bounds[0];
/* 248:329 */     if ((bound instanceof TypeVariable)) {
/* 249:330 */       bound = extractBoundForTypeVariable((TypeVariable)bound);
/* 250:    */     }
/* 251:332 */     return bound;
/* 252:    */   }
/* 253:    */   
/* 254:    */   private static void extractTypeVariablesFromGenericInterfaces(Type[] genericInterfaces, Map<TypeVariable, Type> typeVariableMap)
/* 255:    */   {
/* 256:336 */     Type[] arrayOfType = genericInterfaces;int j = genericInterfaces.length;
/* 257:336 */     for (int i = 0; i < j; i++)
/* 258:    */     {
/* 259:336 */       Type genericInterface = arrayOfType[i];
/* 260:337 */       if ((genericInterface instanceof ParameterizedType))
/* 261:    */       {
/* 262:338 */         ParameterizedType pt = (ParameterizedType)genericInterface;
/* 263:339 */         populateTypeMapFromParameterizedType(pt, typeVariableMap);
/* 264:340 */         if ((pt.getRawType() instanceof Class)) {
/* 265:341 */           extractTypeVariablesFromGenericInterfaces(
/* 266:342 */             ((Class)pt.getRawType()).getGenericInterfaces(), typeVariableMap);
/* 267:    */         }
/* 268:    */       }
/* 269:345 */       else if ((genericInterface instanceof Class))
/* 270:    */       {
/* 271:346 */         extractTypeVariablesFromGenericInterfaces(
/* 272:347 */           ((Class)genericInterface).getGenericInterfaces(), typeVariableMap);
/* 273:    */       }
/* 274:    */     }
/* 275:    */   }
/* 276:    */   
/* 277:    */   private static void populateTypeMapFromParameterizedType(ParameterizedType type, Map<TypeVariable, Type> typeVariableMap)
/* 278:    */   {
/* 279:369 */     if ((type.getRawType() instanceof Class))
/* 280:    */     {
/* 281:370 */       Type[] actualTypeArguments = type.getActualTypeArguments();
/* 282:371 */       TypeVariable[] typeVariables = ((Class)type.getRawType()).getTypeParameters();
/* 283:372 */       for (int i = 0; i < actualTypeArguments.length; i++)
/* 284:    */       {
/* 285:373 */         Type actualTypeArgument = actualTypeArguments[i];
/* 286:374 */         TypeVariable variable = typeVariables[i];
/* 287:375 */         if ((actualTypeArgument instanceof Class))
/* 288:    */         {
/* 289:376 */           typeVariableMap.put(variable, actualTypeArgument);
/* 290:    */         }
/* 291:378 */         else if ((actualTypeArgument instanceof GenericArrayType))
/* 292:    */         {
/* 293:379 */           typeVariableMap.put(variable, actualTypeArgument);
/* 294:    */         }
/* 295:381 */         else if ((actualTypeArgument instanceof ParameterizedType))
/* 296:    */         {
/* 297:382 */           typeVariableMap.put(variable, actualTypeArgument);
/* 298:    */         }
/* 299:384 */         else if ((actualTypeArgument instanceof TypeVariable))
/* 300:    */         {
/* 301:387 */           TypeVariable typeVariableArgument = (TypeVariable)actualTypeArgument;
/* 302:388 */           Type resolvedType = (Type)typeVariableMap.get(typeVariableArgument);
/* 303:389 */           if (resolvedType == null) {
/* 304:390 */             resolvedType = extractBoundForTypeVariable(typeVariableArgument);
/* 305:    */           }
/* 306:392 */           typeVariableMap.put(variable, resolvedType);
/* 307:    */         }
/* 308:    */       }
/* 309:    */     }
/* 310:    */   }
/* 311:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.GenericTypeResolver
 * JD-Core Version:    0.7.0.1
 */