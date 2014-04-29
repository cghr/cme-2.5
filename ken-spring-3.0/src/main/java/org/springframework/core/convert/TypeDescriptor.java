/*   1:    */ package org.springframework.core.convert;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.lang.reflect.Field;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.Map;
/*   8:    */ import org.springframework.core.MethodParameter;
/*   9:    */ import org.springframework.util.ClassUtils;
/*  10:    */ import org.springframework.util.ObjectUtils;
/*  11:    */ 
/*  12:    */ public class TypeDescriptor
/*  13:    */ {
/*  14: 37 */   static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];
/*  15: 39 */   private static final Map<Class<?>, TypeDescriptor> typeDescriptorCache = new HashMap();
/*  16:    */   private final Class<?> type;
/*  17:    */   private final TypeDescriptor elementTypeDescriptor;
/*  18:    */   private final TypeDescriptor mapKeyTypeDescriptor;
/*  19:    */   private final TypeDescriptor mapValueTypeDescriptor;
/*  20:    */   private final Annotation[] annotations;
/*  21:    */   
/*  22:    */   static
/*  23:    */   {
/*  24: 42 */     typeDescriptorCache.put(Boolean.TYPE, new TypeDescriptor(Boolean.TYPE));
/*  25: 43 */     typeDescriptorCache.put(Boolean.class, new TypeDescriptor(Boolean.class));
/*  26: 44 */     typeDescriptorCache.put(Byte.TYPE, new TypeDescriptor(Byte.TYPE));
/*  27: 45 */     typeDescriptorCache.put(Byte.class, new TypeDescriptor(Byte.class));
/*  28: 46 */     typeDescriptorCache.put(Character.TYPE, new TypeDescriptor(Character.TYPE));
/*  29: 47 */     typeDescriptorCache.put(Character.class, new TypeDescriptor(Character.class));
/*  30: 48 */     typeDescriptorCache.put(Short.TYPE, new TypeDescriptor(Short.TYPE));
/*  31: 49 */     typeDescriptorCache.put(Short.class, new TypeDescriptor(Short.class));
/*  32: 50 */     typeDescriptorCache.put(Integer.TYPE, new TypeDescriptor(Integer.TYPE));
/*  33: 51 */     typeDescriptorCache.put(Integer.class, new TypeDescriptor(Integer.class));
/*  34: 52 */     typeDescriptorCache.put(Long.TYPE, new TypeDescriptor(Long.TYPE));
/*  35: 53 */     typeDescriptorCache.put(Long.class, new TypeDescriptor(Long.class));
/*  36: 54 */     typeDescriptorCache.put(Float.TYPE, new TypeDescriptor(Float.TYPE));
/*  37: 55 */     typeDescriptorCache.put(Float.class, new TypeDescriptor(Float.class));
/*  38: 56 */     typeDescriptorCache.put(Double.TYPE, new TypeDescriptor(Double.TYPE));
/*  39: 57 */     typeDescriptorCache.put(Double.class, new TypeDescriptor(Double.class));
/*  40: 58 */     typeDescriptorCache.put(String.class, new TypeDescriptor(String.class));
/*  41:    */   }
/*  42:    */   
/*  43:    */   public TypeDescriptor(MethodParameter methodParameter)
/*  44:    */   {
/*  45: 77 */     this(new ParameterDescriptor(methodParameter));
/*  46:    */   }
/*  47:    */   
/*  48:    */   public TypeDescriptor(Field field)
/*  49:    */   {
/*  50: 86 */     this(new FieldDescriptor(field));
/*  51:    */   }
/*  52:    */   
/*  53:    */   public TypeDescriptor(Property property)
/*  54:    */   {
/*  55: 95 */     this(new BeanPropertyDescriptor(property));
/*  56:    */   }
/*  57:    */   
/*  58:    */   public static TypeDescriptor valueOf(Class<?> type)
/*  59:    */   {
/*  60:106 */     TypeDescriptor desc = (TypeDescriptor)typeDescriptorCache.get(type);
/*  61:107 */     return desc != null ? desc : new TypeDescriptor(type);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public static TypeDescriptor collection(Class<?> collectionType, TypeDescriptor elementTypeDescriptor)
/*  65:    */   {
/*  66:120 */     if (!Collection.class.isAssignableFrom(collectionType)) {
/*  67:121 */       throw new IllegalArgumentException("collectionType must be a java.util.Collection");
/*  68:    */     }
/*  69:123 */     return new TypeDescriptor(collectionType, elementTypeDescriptor);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static TypeDescriptor map(Class<?> mapType, TypeDescriptor keyTypeDescriptor, TypeDescriptor valueTypeDescriptor)
/*  73:    */   {
/*  74:137 */     if (!Map.class.isAssignableFrom(mapType)) {
/*  75:138 */       throw new IllegalArgumentException("mapType must be a java.util.Map");
/*  76:    */     }
/*  77:140 */     return new TypeDescriptor(mapType, keyTypeDescriptor, valueTypeDescriptor);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static TypeDescriptor nested(MethodParameter methodParameter, int nestingLevel)
/*  81:    */   {
/*  82:158 */     if (methodParameter.getNestingLevel() != 1) {
/*  83:159 */       throw new IllegalArgumentException("methodParameter nesting level must be 1: use the nestingLevel parameter to specify the desired nestingLevel for nested type traversal");
/*  84:    */     }
/*  85:161 */     return nested(new ParameterDescriptor(methodParameter), nestingLevel);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public static TypeDescriptor nested(Field field, int nestingLevel)
/*  89:    */   {
/*  90:178 */     return nested(new FieldDescriptor(field), nestingLevel);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public static TypeDescriptor nested(Property property, int nestingLevel)
/*  94:    */   {
/*  95:195 */     return nested(new BeanPropertyDescriptor(property), nestingLevel);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public static TypeDescriptor forObject(Object source)
/*  99:    */   {
/* 100:206 */     return source != null ? valueOf(source.getClass()) : null;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public Class<?> getType()
/* 104:    */   {
/* 105:217 */     return this.type;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public Class<?> getObjectType()
/* 109:    */   {
/* 110:225 */     return ClassUtils.resolvePrimitiveIfNecessary(getType());
/* 111:    */   }
/* 112:    */   
/* 113:    */   public TypeDescriptor narrow(Object value)
/* 114:    */   {
/* 115:240 */     if (value == null) {
/* 116:241 */       return this;
/* 117:    */     }
/* 118:243 */     return new TypeDescriptor(value.getClass(), this.elementTypeDescriptor, this.mapKeyTypeDescriptor, this.mapValueTypeDescriptor, this.annotations);
/* 119:    */   }
/* 120:    */   
/* 121:    */   public String getName()
/* 122:    */   {
/* 123:250 */     return ClassUtils.getQualifiedName(getType());
/* 124:    */   }
/* 125:    */   
/* 126:    */   public boolean isPrimitive()
/* 127:    */   {
/* 128:257 */     return getType().isPrimitive();
/* 129:    */   }
/* 130:    */   
/* 131:    */   public Annotation[] getAnnotations()
/* 132:    */   {
/* 133:265 */     return this.annotations;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public Annotation getAnnotation(Class<? extends Annotation> annotationType)
/* 137:    */   {
/* 138:273 */     for (Annotation annotation : getAnnotations()) {
/* 139:274 */       if (annotation.annotationType().equals(annotationType)) {
/* 140:275 */         return annotation;
/* 141:    */       }
/* 142:    */     }
/* 143:278 */     return null;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public boolean isAssignableTo(TypeDescriptor typeDescriptor)
/* 147:    */   {
/* 148:292 */     boolean typesAssignable = typeDescriptor.getObjectType().isAssignableFrom(getObjectType());
/* 149:293 */     if (!typesAssignable) {
/* 150:294 */       return false;
/* 151:    */     }
/* 152:296 */     if ((isArray()) && (typeDescriptor.isArray())) {
/* 153:297 */       return getElementTypeDescriptor().isAssignableTo(typeDescriptor.getElementTypeDescriptor());
/* 154:    */     }
/* 155:299 */     if ((isCollection()) && (typeDescriptor.isCollection())) {
/* 156:300 */       return isNestedAssignable(getElementTypeDescriptor(), typeDescriptor.getElementTypeDescriptor());
/* 157:    */     }
/* 158:302 */     if ((isMap()) && (typeDescriptor.isMap())) {
/* 159:304 */       return (isNestedAssignable(getMapKeyTypeDescriptor(), typeDescriptor.getMapKeyTypeDescriptor())) && (isNestedAssignable(getMapValueTypeDescriptor(), typeDescriptor.getMapValueTypeDescriptor()));
/* 160:    */     }
/* 161:307 */     return true;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public boolean isCollection()
/* 165:    */   {
/* 166:317 */     return Collection.class.isAssignableFrom(getType());
/* 167:    */   }
/* 168:    */   
/* 169:    */   public boolean isArray()
/* 170:    */   {
/* 171:324 */     return getType().isArray();
/* 172:    */   }
/* 173:    */   
/* 174:    */   public TypeDescriptor getElementTypeDescriptor()
/* 175:    */   {
/* 176:335 */     assertCollectionOrArray();
/* 177:336 */     return this.elementTypeDescriptor;
/* 178:    */   }
/* 179:    */   
/* 180:    */   public TypeDescriptor elementTypeDescriptor(Object element)
/* 181:    */   {
/* 182:351 */     return narrow(element, getElementTypeDescriptor());
/* 183:    */   }
/* 184:    */   
/* 185:    */   public boolean isMap()
/* 186:    */   {
/* 187:360 */     return Map.class.isAssignableFrom(getType());
/* 188:    */   }
/* 189:    */   
/* 190:    */   public TypeDescriptor getMapKeyTypeDescriptor()
/* 191:    */   {
/* 192:370 */     assertMap();
/* 193:371 */     return this.mapKeyTypeDescriptor;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public TypeDescriptor getMapKeyTypeDescriptor(Object mapKey)
/* 197:    */   {
/* 198:386 */     return narrow(mapKey, getMapKeyTypeDescriptor());
/* 199:    */   }
/* 200:    */   
/* 201:    */   public TypeDescriptor getMapValueTypeDescriptor()
/* 202:    */   {
/* 203:396 */     assertMap();
/* 204:397 */     return this.mapValueTypeDescriptor;
/* 205:    */   }
/* 206:    */   
/* 207:    */   public TypeDescriptor getMapValueTypeDescriptor(Object mapValue)
/* 208:    */   {
/* 209:411 */     return narrow(mapValue, getMapValueTypeDescriptor());
/* 210:    */   }
/* 211:    */   
/* 212:    */   @Deprecated
/* 213:    */   public Class<?> getElementType()
/* 214:    */   {
/* 215:424 */     return getElementTypeDescriptor().getType();
/* 216:    */   }
/* 217:    */   
/* 218:    */   @Deprecated
/* 219:    */   public Class<?> getMapKeyType()
/* 220:    */   {
/* 221:434 */     return getMapKeyTypeDescriptor().getType();
/* 222:    */   }
/* 223:    */   
/* 224:    */   @Deprecated
/* 225:    */   public Class<?> getMapValueType()
/* 226:    */   {
/* 227:444 */     return getMapValueTypeDescriptor().getType();
/* 228:    */   }
/* 229:    */   
/* 230:    */   TypeDescriptor(AbstractDescriptor descriptor)
/* 231:    */   {
/* 232:450 */     this.type = descriptor.getType();
/* 233:451 */     this.elementTypeDescriptor = descriptor.getElementTypeDescriptor();
/* 234:452 */     this.mapKeyTypeDescriptor = descriptor.getMapKeyTypeDescriptor();
/* 235:453 */     this.mapValueTypeDescriptor = descriptor.getMapValueTypeDescriptor();
/* 236:454 */     this.annotations = descriptor.getAnnotations();
/* 237:    */   }
/* 238:    */   
/* 239:    */   static Annotation[] nullSafeAnnotations(Annotation[] annotations)
/* 240:    */   {
/* 241:458 */     return annotations != null ? annotations : EMPTY_ANNOTATION_ARRAY;
/* 242:    */   }
/* 243:    */   
/* 244:    */   private TypeDescriptor(Class<?> type)
/* 245:    */   {
/* 246:465 */     this(new ClassDescriptor(type));
/* 247:    */   }
/* 248:    */   
/* 249:    */   private TypeDescriptor(Class<?> collectionType, TypeDescriptor elementTypeDescriptor)
/* 250:    */   {
/* 251:469 */     this(collectionType, elementTypeDescriptor, null, null, EMPTY_ANNOTATION_ARRAY);
/* 252:    */   }
/* 253:    */   
/* 254:    */   private TypeDescriptor(Class<?> mapType, TypeDescriptor keyTypeDescriptor, TypeDescriptor valueTypeDescriptor)
/* 255:    */   {
/* 256:473 */     this(mapType, null, keyTypeDescriptor, valueTypeDescriptor, EMPTY_ANNOTATION_ARRAY);
/* 257:    */   }
/* 258:    */   
/* 259:    */   private TypeDescriptor(Class<?> type, TypeDescriptor elementTypeDescriptor, TypeDescriptor mapKeyTypeDescriptor, TypeDescriptor mapValueTypeDescriptor, Annotation[] annotations)
/* 260:    */   {
/* 261:478 */     this.type = type;
/* 262:479 */     this.elementTypeDescriptor = elementTypeDescriptor;
/* 263:480 */     this.mapKeyTypeDescriptor = mapKeyTypeDescriptor;
/* 264:481 */     this.mapValueTypeDescriptor = mapValueTypeDescriptor;
/* 265:482 */     this.annotations = annotations;
/* 266:    */   }
/* 267:    */   
/* 268:    */   private static TypeDescriptor nested(AbstractDescriptor descriptor, int nestingLevel)
/* 269:    */   {
/* 270:486 */     for (int i = 0; i < nestingLevel; i++)
/* 271:    */     {
/* 272:487 */       descriptor = descriptor.nested();
/* 273:488 */       if (descriptor == null) {
/* 274:489 */         return null;
/* 275:    */       }
/* 276:    */     }
/* 277:492 */     return new TypeDescriptor(descriptor);
/* 278:    */   }
/* 279:    */   
/* 280:    */   private void assertCollectionOrArray()
/* 281:    */   {
/* 282:499 */     if ((!isCollection()) && (!isArray())) {
/* 283:500 */       throw new IllegalStateException("Not a java.util.Collection or Array");
/* 284:    */     }
/* 285:    */   }
/* 286:    */   
/* 287:    */   private void assertMap()
/* 288:    */   {
/* 289:505 */     if (!isMap()) {
/* 290:506 */       throw new IllegalStateException("Not a java.util.Map");
/* 291:    */     }
/* 292:    */   }
/* 293:    */   
/* 294:    */   private TypeDescriptor narrow(Object value, TypeDescriptor typeDescriptor)
/* 295:    */   {
/* 296:511 */     if (typeDescriptor != null) {
/* 297:512 */       return typeDescriptor.narrow(value);
/* 298:    */     }
/* 299:515 */     return value != null ? new TypeDescriptor(value.getClass(), null, null, null, this.annotations) : null;
/* 300:    */   }
/* 301:    */   
/* 302:    */   private boolean isNestedAssignable(TypeDescriptor nestedTypeDescriptor, TypeDescriptor otherNestedTypeDescriptor)
/* 303:    */   {
/* 304:520 */     if ((nestedTypeDescriptor == null) || (otherNestedTypeDescriptor == null)) {
/* 305:521 */       return true;
/* 306:    */     }
/* 307:523 */     return nestedTypeDescriptor.isAssignableTo(otherNestedTypeDescriptor);
/* 308:    */   }
/* 309:    */   
/* 310:    */   private String wildcard(TypeDescriptor typeDescriptor)
/* 311:    */   {
/* 312:527 */     return typeDescriptor != null ? typeDescriptor.toString() : "?";
/* 313:    */   }
/* 314:    */   
/* 315:    */   public boolean equals(Object obj)
/* 316:    */   {
/* 317:532 */     if (this == obj) {
/* 318:533 */       return true;
/* 319:    */     }
/* 320:535 */     if (!(obj instanceof TypeDescriptor)) {
/* 321:536 */       return false;
/* 322:    */     }
/* 323:538 */     TypeDescriptor other = (TypeDescriptor)obj;
/* 324:539 */     boolean annotatedTypeEquals = (ObjectUtils.nullSafeEquals(getType(), other.getType())) && (ObjectUtils.nullSafeEquals(getAnnotations(), other.getAnnotations()));
/* 325:540 */     if (!annotatedTypeEquals) {
/* 326:541 */       return false;
/* 327:    */     }
/* 328:543 */     if ((isCollection()) || (isArray())) {
/* 329:544 */       return ObjectUtils.nullSafeEquals(getElementTypeDescriptor(), other.getElementTypeDescriptor());
/* 330:    */     }
/* 331:546 */     if (isMap()) {
/* 332:547 */       return (ObjectUtils.nullSafeEquals(getMapKeyTypeDescriptor(), other.getMapKeyTypeDescriptor())) && (ObjectUtils.nullSafeEquals(getMapValueTypeDescriptor(), other.getMapValueTypeDescriptor()));
/* 333:    */     }
/* 334:550 */     return true;
/* 335:    */   }
/* 336:    */   
/* 337:    */   public int hashCode()
/* 338:    */   {
/* 339:555 */     return getType().hashCode();
/* 340:    */   }
/* 341:    */   
/* 342:    */   public String toString()
/* 343:    */   {
/* 344:559 */     StringBuilder builder = new StringBuilder();
/* 345:560 */     Annotation[] anns = getAnnotations();
/* 346:561 */     for (Annotation ann : anns) {
/* 347:562 */       builder.append("@").append(ann.annotationType().getName()).append(' ');
/* 348:    */     }
/* 349:564 */     builder.append(ClassUtils.getQualifiedName(getType()));
/* 350:565 */     if (isMap())
/* 351:    */     {
/* 352:566 */       builder.append("<").append(wildcard(getMapKeyTypeDescriptor()));
/* 353:567 */       builder.append(", ").append(wildcard(getMapValueTypeDescriptor())).append(">");
/* 354:    */     }
/* 355:569 */     else if (isCollection())
/* 356:    */     {
/* 357:570 */       builder.append("<").append(wildcard(getElementTypeDescriptor())).append(">");
/* 358:    */     }
/* 359:572 */     return builder.toString();
/* 360:    */   }
/* 361:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.TypeDescriptor
 * JD-Core Version:    0.7.0.1
 */