/*   1:    */ package org.springframework.core;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Array;
/*   4:    */ import java.lang.reflect.Field;
/*   5:    */ import java.lang.reflect.GenericArrayType;
/*   6:    */ import java.lang.reflect.Method;
/*   7:    */ import java.lang.reflect.ParameterizedType;
/*   8:    */ import java.lang.reflect.Type;
/*   9:    */ import java.lang.reflect.TypeVariable;
/*  10:    */ import java.lang.reflect.WildcardType;
/*  11:    */ import java.util.Collection;
/*  12:    */ import java.util.Map;
/*  13:    */ 
/*  14:    */ public abstract class GenericCollectionTypeResolver
/*  15:    */ {
/*  16:    */   public static Class<?> getCollectionType(Class<? extends Collection> collectionClass)
/*  17:    */   {
/*  18: 49 */     return extractTypeFromClass(collectionClass, Collection.class, 0);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public static Class<?> getMapKeyType(Class<? extends Map> mapClass)
/*  22:    */   {
/*  23: 59 */     return extractTypeFromClass(mapClass, Map.class, 0);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public static Class<?> getMapValueType(Class<? extends Map> mapClass)
/*  27:    */   {
/*  28: 69 */     return extractTypeFromClass(mapClass, Map.class, 1);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static Class<?> getCollectionFieldType(Field collectionField)
/*  32:    */   {
/*  33: 78 */     return getGenericFieldType(collectionField, Collection.class, 0, null, 1);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public static Class<?> getCollectionFieldType(Field collectionField, int nestingLevel)
/*  37:    */   {
/*  38: 90 */     return getGenericFieldType(collectionField, Collection.class, 0, null, nestingLevel);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public static Class<?> getCollectionFieldType(Field collectionField, int nestingLevel, Map<Integer, Integer> typeIndexesPerLevel)
/*  42:    */   {
/*  43:104 */     return getGenericFieldType(collectionField, Collection.class, 0, typeIndexesPerLevel, nestingLevel);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static Class<?> getMapKeyFieldType(Field mapField)
/*  47:    */   {
/*  48:113 */     return getGenericFieldType(mapField, Map.class, 0, null, 1);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static Class<?> getMapKeyFieldType(Field mapField, int nestingLevel)
/*  52:    */   {
/*  53:125 */     return getGenericFieldType(mapField, Map.class, 0, null, nestingLevel);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public static Class<?> getMapKeyFieldType(Field mapField, int nestingLevel, Map<Integer, Integer> typeIndexesPerLevel)
/*  57:    */   {
/*  58:139 */     return getGenericFieldType(mapField, Map.class, 0, typeIndexesPerLevel, nestingLevel);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public static Class<?> getMapValueFieldType(Field mapField)
/*  62:    */   {
/*  63:148 */     return getGenericFieldType(mapField, Map.class, 1, null, 1);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public static Class<?> getMapValueFieldType(Field mapField, int nestingLevel)
/*  67:    */   {
/*  68:160 */     return getGenericFieldType(mapField, Map.class, 1, null, nestingLevel);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public static Class<?> getMapValueFieldType(Field mapField, int nestingLevel, Map<Integer, Integer> typeIndexesPerLevel)
/*  72:    */   {
/*  73:174 */     return getGenericFieldType(mapField, Map.class, 1, typeIndexesPerLevel, nestingLevel);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public static Class<?> getCollectionParameterType(MethodParameter methodParam)
/*  77:    */   {
/*  78:183 */     return getGenericParameterType(methodParam, Collection.class, 0);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static Class<?> getMapKeyParameterType(MethodParameter methodParam)
/*  82:    */   {
/*  83:192 */     return getGenericParameterType(methodParam, Map.class, 0);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public static Class<?> getMapValueParameterType(MethodParameter methodParam)
/*  87:    */   {
/*  88:201 */     return getGenericParameterType(methodParam, Map.class, 1);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public static Class<?> getCollectionReturnType(Method method)
/*  92:    */   {
/*  93:210 */     return getGenericReturnType(method, Collection.class, 0, 1);
/*  94:    */   }
/*  95:    */   
/*  96:    */   public static Class<?> getCollectionReturnType(Method method, int nestingLevel)
/*  97:    */   {
/*  98:224 */     return getGenericReturnType(method, Collection.class, 0, nestingLevel);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public static Class<?> getMapKeyReturnType(Method method)
/* 102:    */   {
/* 103:233 */     return getGenericReturnType(method, Map.class, 0, 1);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public static Class<?> getMapKeyReturnType(Method method, int nestingLevel)
/* 107:    */   {
/* 108:245 */     return getGenericReturnType(method, Map.class, 0, nestingLevel);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public static Class<?> getMapValueReturnType(Method method)
/* 112:    */   {
/* 113:254 */     return getGenericReturnType(method, Map.class, 1, 1);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public static Class<?> getMapValueReturnType(Method method, int nestingLevel)
/* 117:    */   {
/* 118:266 */     return getGenericReturnType(method, Map.class, 1, nestingLevel);
/* 119:    */   }
/* 120:    */   
/* 121:    */   private static Class<?> getGenericParameterType(MethodParameter methodParam, Class<?> source, int typeIndex)
/* 122:    */   {
/* 123:279 */     return extractType(GenericTypeResolver.getTargetType(methodParam), source, typeIndex, 
/* 124:280 */       methodParam.typeVariableMap, methodParam.typeIndexesPerLevel, methodParam.getNestingLevel(), 1);
/* 125:    */   }
/* 126:    */   
/* 127:    */   private static Class<?> getGenericFieldType(Field field, Class<?> source, int typeIndex, Map<Integer, Integer> typeIndexesPerLevel, int nestingLevel)
/* 128:    */   {
/* 129:294 */     return extractType(field.getGenericType(), source, typeIndex, null, typeIndexesPerLevel, nestingLevel, 1);
/* 130:    */   }
/* 131:    */   
/* 132:    */   private static Class<?> getGenericReturnType(Method method, Class<?> source, int typeIndex, int nestingLevel)
/* 133:    */   {
/* 134:307 */     return extractType(method.getGenericReturnType(), source, typeIndex, null, null, nestingLevel, 1);
/* 135:    */   }
/* 136:    */   
/* 137:    */   private static Class<?> extractType(Type type, Class<?> source, int typeIndex, Map<TypeVariable, Type> typeVariableMap, Map<Integer, Integer> typeIndexesPerLevel, int nestingLevel, int currentLevel)
/* 138:    */   {
/* 139:323 */     Type resolvedType = type;
/* 140:324 */     if (((type instanceof TypeVariable)) && (typeVariableMap != null))
/* 141:    */     {
/* 142:325 */       Type mappedType = (Type)typeVariableMap.get((TypeVariable)type);
/* 143:326 */       if (mappedType != null) {
/* 144:327 */         resolvedType = mappedType;
/* 145:    */       }
/* 146:    */     }
/* 147:330 */     if ((resolvedType instanceof ParameterizedType)) {
/* 148:331 */       return extractTypeFromParameterizedType((ParameterizedType)resolvedType, source, typeIndex, typeVariableMap, typeIndexesPerLevel, 
/* 149:332 */         nestingLevel, currentLevel);
/* 150:    */     }
/* 151:334 */     if ((resolvedType instanceof Class)) {
/* 152:335 */       return extractTypeFromClass((Class)resolvedType, source, typeIndex, typeVariableMap, typeIndexesPerLevel, 
/* 153:336 */         nestingLevel, currentLevel);
/* 154:    */     }
/* 155:338 */     if ((resolvedType instanceof GenericArrayType))
/* 156:    */     {
/* 157:339 */       Type compType = ((GenericArrayType)resolvedType).getGenericComponentType();
/* 158:340 */       return extractType(compType, source, typeIndex, typeVariableMap, typeIndexesPerLevel, nestingLevel, currentLevel + 1);
/* 159:    */     }
/* 160:343 */     return null;
/* 161:    */   }
/* 162:    */   
/* 163:    */   private static Class<?> extractTypeFromParameterizedType(ParameterizedType ptype, Class<?> source, int typeIndex, Map<TypeVariable, Type> typeVariableMap, Map<Integer, Integer> typeIndexesPerLevel, int nestingLevel, int currentLevel)
/* 164:    */   {
/* 165:360 */     if (!(ptype.getRawType() instanceof Class)) {
/* 166:361 */       return null;
/* 167:    */     }
/* 168:363 */     Class rawType = (Class)ptype.getRawType();
/* 169:364 */     Type[] paramTypes = ptype.getActualTypeArguments();
/* 170:365 */     if (nestingLevel - currentLevel > 0)
/* 171:    */     {
/* 172:366 */       int nextLevel = currentLevel + 1;
/* 173:367 */       Integer currentTypeIndex = typeIndexesPerLevel != null ? (Integer)typeIndexesPerLevel.get(Integer.valueOf(nextLevel)) : null;
/* 174:    */       
/* 175:369 */       int indexToUse = currentTypeIndex != null ? currentTypeIndex.intValue() : paramTypes.length - 1;
/* 176:370 */       Type paramType = paramTypes[indexToUse];
/* 177:371 */       return extractType(paramType, source, typeIndex, typeVariableMap, typeIndexesPerLevel, nestingLevel, nextLevel);
/* 178:    */     }
/* 179:373 */     if ((source != null) && (!source.isAssignableFrom(rawType))) {
/* 180:374 */       return null;
/* 181:    */     }
/* 182:376 */     Class fromSuperclassOrInterface = extractTypeFromClass(rawType, source, typeIndex, typeVariableMap, typeIndexesPerLevel, 
/* 183:377 */       nestingLevel, currentLevel);
/* 184:378 */     if (fromSuperclassOrInterface != null) {
/* 185:379 */       return fromSuperclassOrInterface;
/* 186:    */     }
/* 187:381 */     if ((paramTypes == null) || (typeIndex >= paramTypes.length)) {
/* 188:382 */       return null;
/* 189:    */     }
/* 190:384 */     Type paramType = paramTypes[typeIndex];
/* 191:385 */     if (((paramType instanceof TypeVariable)) && (typeVariableMap != null))
/* 192:    */     {
/* 193:386 */       Type mappedType = (Type)typeVariableMap.get((TypeVariable)paramType);
/* 194:387 */       if (mappedType != null) {
/* 195:388 */         paramType = mappedType;
/* 196:    */       }
/* 197:    */     }
/* 198:391 */     if ((paramType instanceof WildcardType))
/* 199:    */     {
/* 200:392 */       WildcardType wildcardType = (WildcardType)paramType;
/* 201:393 */       Type[] upperBounds = wildcardType.getUpperBounds();
/* 202:394 */       if ((upperBounds != null) && (upperBounds.length > 0) && (!Object.class.equals(upperBounds[0])))
/* 203:    */       {
/* 204:395 */         paramType = upperBounds[0];
/* 205:    */       }
/* 206:    */       else
/* 207:    */       {
/* 208:398 */         Type[] lowerBounds = wildcardType.getLowerBounds();
/* 209:399 */         if ((lowerBounds != null) && (lowerBounds.length > 0) && (!Object.class.equals(lowerBounds[0]))) {
/* 210:400 */           paramType = lowerBounds[0];
/* 211:    */         }
/* 212:    */       }
/* 213:    */     }
/* 214:404 */     if ((paramType instanceof ParameterizedType)) {
/* 215:405 */       paramType = ((ParameterizedType)paramType).getRawType();
/* 216:    */     }
/* 217:407 */     if ((paramType instanceof GenericArrayType))
/* 218:    */     {
/* 219:409 */       Type compType = ((GenericArrayType)paramType).getGenericComponentType();
/* 220:410 */       if ((compType instanceof Class)) {
/* 221:411 */         return Array.newInstance((Class)compType, 0).getClass();
/* 222:    */       }
/* 223:    */     }
/* 224:414 */     else if ((paramType instanceof Class))
/* 225:    */     {
/* 226:416 */       return (Class)paramType;
/* 227:    */     }
/* 228:418 */     return null;
/* 229:    */   }
/* 230:    */   
/* 231:    */   private static Class<?> extractTypeFromClass(Class<?> clazz, Class<?> source, int typeIndex)
/* 232:    */   {
/* 233:429 */     return extractTypeFromClass(clazz, source, typeIndex, null, null, 1, 1);
/* 234:    */   }
/* 235:    */   
/* 236:    */   private static Class<?> extractTypeFromClass(Class<?> clazz, Class<?> source, int typeIndex, Map<TypeVariable, Type> typeVariableMap, Map<Integer, Integer> typeIndexesPerLevel, int nestingLevel, int currentLevel)
/* 237:    */   {
/* 238:445 */     if (clazz.getName().startsWith("java.util.")) {
/* 239:446 */       return null;
/* 240:    */     }
/* 241:448 */     if ((clazz.getSuperclass() != null) && (isIntrospectionCandidate(clazz.getSuperclass()))) {
/* 242:449 */       return extractType(clazz.getGenericSuperclass(), source, typeIndex, typeVariableMap, typeIndexesPerLevel, 
/* 243:450 */         nestingLevel, currentLevel);
/* 244:    */     }
/* 245:452 */     Type[] ifcs = clazz.getGenericInterfaces();
/* 246:453 */     if (ifcs != null) {
/* 247:454 */       for (Type ifc : ifcs)
/* 248:    */       {
/* 249:455 */         Type rawType = ifc;
/* 250:456 */         if ((ifc instanceof ParameterizedType)) {
/* 251:457 */           rawType = ((ParameterizedType)ifc).getRawType();
/* 252:    */         }
/* 253:459 */         if (((rawType instanceof Class)) && (isIntrospectionCandidate((Class)rawType))) {
/* 254:460 */           return extractType(ifc, source, typeIndex, typeVariableMap, typeIndexesPerLevel, nestingLevel, currentLevel);
/* 255:    */         }
/* 256:    */       }
/* 257:    */     }
/* 258:464 */     return null;
/* 259:    */   }
/* 260:    */   
/* 261:    */   private static boolean isIntrospectionCandidate(Class clazz)
/* 262:    */   {
/* 263:474 */     return (Collection.class.isAssignableFrom(clazz)) || (Map.class.isAssignableFrom(clazz));
/* 264:    */   }
/* 265:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.GenericCollectionTypeResolver
 * JD-Core Version:    0.7.0.1
 */