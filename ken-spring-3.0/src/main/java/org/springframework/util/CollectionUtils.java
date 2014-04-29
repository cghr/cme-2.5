/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Arrays;
/*   6:    */ import java.util.Collection;
/*   7:    */ import java.util.Collections;
/*   8:    */ import java.util.Enumeration;
/*   9:    */ import java.util.Iterator;
/*  10:    */ import java.util.LinkedHashMap;
/*  11:    */ import java.util.LinkedList;
/*  12:    */ import java.util.List;
/*  13:    */ import java.util.Map;
/*  14:    */ import java.util.Map.Entry;
/*  15:    */ import java.util.Properties;
/*  16:    */ import java.util.Set;
/*  17:    */ 
/*  18:    */ public abstract class CollectionUtils
/*  19:    */ {
/*  20:    */   public static boolean isEmpty(Collection collection)
/*  21:    */   {
/*  22: 51 */     return (collection == null) || (collection.isEmpty());
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static boolean isEmpty(Map map)
/*  26:    */   {
/*  27: 61 */     return (map == null) || (map.isEmpty());
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static List arrayToList(Object source)
/*  31:    */   {
/*  32: 74 */     return Arrays.asList(ObjectUtils.toObjectArray(source));
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static void mergeArrayIntoCollection(Object array, Collection collection)
/*  36:    */   {
/*  37: 84 */     if (collection == null) {
/*  38: 85 */       throw new IllegalArgumentException("Collection must not be null");
/*  39:    */     }
/*  40: 87 */     Object[] arr = ObjectUtils.toObjectArray(array);
/*  41: 88 */     for (Object elem : arr) {
/*  42: 89 */       collection.add(elem);
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static void mergePropertiesIntoMap(Properties props, Map map)
/*  47:    */   {
/*  48:103 */     if (map == null) {
/*  49:104 */       throw new IllegalArgumentException("Map must not be null");
/*  50:    */     }
/*  51:106 */     if (props != null) {
/*  52:107 */       for (Enumeration en = props.propertyNames(); en.hasMoreElements();)
/*  53:    */       {
/*  54:108 */         String key = (String)en.nextElement();
/*  55:109 */         Object value = props.getProperty(key);
/*  56:110 */         if (value == null) {
/*  57:112 */           value = props.get(key);
/*  58:    */         }
/*  59:114 */         map.put(key, value);
/*  60:    */       }
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   public static boolean contains(Iterator iterator, Object element)
/*  65:    */   {
/*  66:127 */     if (iterator != null) {
/*  67:128 */       while (iterator.hasNext())
/*  68:    */       {
/*  69:129 */         Object candidate = iterator.next();
/*  70:130 */         if (ObjectUtils.nullSafeEquals(candidate, element)) {
/*  71:131 */           return true;
/*  72:    */         }
/*  73:    */       }
/*  74:    */     }
/*  75:135 */     return false;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public static boolean contains(Enumeration enumeration, Object element)
/*  79:    */   {
/*  80:145 */     if (enumeration != null) {
/*  81:146 */       while (enumeration.hasMoreElements())
/*  82:    */       {
/*  83:147 */         Object candidate = enumeration.nextElement();
/*  84:148 */         if (ObjectUtils.nullSafeEquals(candidate, element)) {
/*  85:149 */           return true;
/*  86:    */         }
/*  87:    */       }
/*  88:    */     }
/*  89:153 */     return false;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public static boolean containsInstance(Collection collection, Object element)
/*  93:    */   {
/*  94:165 */     if (collection != null) {
/*  95:166 */       for (Object candidate : collection) {
/*  96:167 */         if (candidate == element) {
/*  97:168 */           return true;
/*  98:    */         }
/*  99:    */       }
/* 100:    */     }
/* 101:172 */     return false;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public static boolean containsAny(Collection source, Collection candidates)
/* 105:    */   {
/* 106:183 */     if ((isEmpty(source)) || (isEmpty(candidates))) {
/* 107:184 */       return false;
/* 108:    */     }
/* 109:186 */     for (Object candidate : candidates) {
/* 110:187 */       if (source.contains(candidate)) {
/* 111:188 */         return true;
/* 112:    */       }
/* 113:    */     }
/* 114:191 */     return false;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public static Object findFirstMatch(Collection source, Collection candidates)
/* 118:    */   {
/* 119:204 */     if ((isEmpty(source)) || (isEmpty(candidates))) {
/* 120:205 */       return null;
/* 121:    */     }
/* 122:207 */     for (Object candidate : candidates) {
/* 123:208 */       if (source.contains(candidate)) {
/* 124:209 */         return candidate;
/* 125:    */       }
/* 126:    */     }
/* 127:212 */     return null;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public static <T> T findValueOfType(Collection<?> collection, Class<T> type)
/* 131:    */   {
/* 132:224 */     if (isEmpty(collection)) {
/* 133:225 */       return null;
/* 134:    */     }
/* 135:227 */     T value = null;
/* 136:228 */     for (Object element : collection) {
/* 137:229 */       if ((type == null) || (type.isInstance(element)))
/* 138:    */       {
/* 139:230 */         if (value != null) {
/* 140:232 */           return null;
/* 141:    */         }
/* 142:234 */         value = element;
/* 143:    */       }
/* 144:    */     }
/* 145:237 */     return value;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public static Object findValueOfType(Collection<?> collection, Class<?>[] types)
/* 149:    */   {
/* 150:250 */     if ((isEmpty(collection)) || (ObjectUtils.isEmpty(types))) {
/* 151:251 */       return null;
/* 152:    */     }
/* 153:253 */     for (Class<?> type : types)
/* 154:    */     {
/* 155:254 */       Object value = findValueOfType(collection, type);
/* 156:255 */       if (value != null) {
/* 157:256 */         return value;
/* 158:    */       }
/* 159:    */     }
/* 160:259 */     return null;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public static boolean hasUniqueObject(Collection collection)
/* 164:    */   {
/* 165:269 */     if (isEmpty(collection)) {
/* 166:270 */       return false;
/* 167:    */     }
/* 168:272 */     boolean hasCandidate = false;
/* 169:273 */     Object candidate = null;
/* 170:274 */     for (Object elem : collection) {
/* 171:275 */       if (!hasCandidate)
/* 172:    */       {
/* 173:276 */         hasCandidate = true;
/* 174:277 */         candidate = elem;
/* 175:    */       }
/* 176:279 */       else if (candidate != elem)
/* 177:    */       {
/* 178:280 */         return false;
/* 179:    */       }
/* 180:    */     }
/* 181:283 */     return true;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public static Class<?> findCommonElementType(Collection collection)
/* 185:    */   {
/* 186:293 */     if (isEmpty(collection)) {
/* 187:294 */       return null;
/* 188:    */     }
/* 189:296 */     Class<?> candidate = null;
/* 190:297 */     for (Object val : collection) {
/* 191:298 */       if (val != null) {
/* 192:299 */         if (candidate == null) {
/* 193:300 */           candidate = val.getClass();
/* 194:302 */         } else if (candidate != val.getClass()) {
/* 195:303 */           return null;
/* 196:    */         }
/* 197:    */       }
/* 198:    */     }
/* 199:307 */     return candidate;
/* 200:    */   }
/* 201:    */   
/* 202:    */   public static <A, E extends A> A[] toArray(Enumeration<E> enumeration, A[] array)
/* 203:    */   {
/* 204:316 */     ArrayList<A> elements = new ArrayList();
/* 205:317 */     while (enumeration.hasMoreElements()) {
/* 206:318 */       elements.add(enumeration.nextElement());
/* 207:    */     }
/* 208:320 */     return elements.toArray(array);
/* 209:    */   }
/* 210:    */   
/* 211:    */   public static <E> Iterator<E> toIterator(Enumeration<E> enumeration)
/* 212:    */   {
/* 213:329 */     return new EnumerationIterator(enumeration);
/* 214:    */   }
/* 215:    */   
/* 216:    */   public static <K, V> MultiValueMap<K, V> toMultiValueMap(Map<K, List<V>> map)
/* 217:    */   {
/* 218:339 */     return new MultiValueMapAdapter(map);
/* 219:    */   }
/* 220:    */   
/* 221:    */   public static <K, V> MultiValueMap<K, V> unmodifiableMultiValueMap(MultiValueMap<? extends K, ? extends V> map)
/* 222:    */   {
/* 223:350 */     Assert.notNull(map, "'map' must not be null");
/* 224:351 */     Map<K, List<V>> result = new LinkedHashMap(map.size());
/* 225:352 */     for (Map.Entry<? extends K, ? extends List<? extends V>> entry : map.entrySet())
/* 226:    */     {
/* 227:353 */       List<V> values = Collections.unmodifiableList((List)entry.getValue());
/* 228:354 */       result.put(entry.getKey(), values);
/* 229:    */     }
/* 230:356 */     Map<K, List<V>> unmodifiableMap = Collections.unmodifiableMap(result);
/* 231:357 */     return toMultiValueMap(unmodifiableMap);
/* 232:    */   }
/* 233:    */   
/* 234:    */   private static class EnumerationIterator<E>
/* 235:    */     implements Iterator<E>
/* 236:    */   {
/* 237:    */     private Enumeration<E> enumeration;
/* 238:    */     
/* 239:    */     public EnumerationIterator(Enumeration<E> enumeration)
/* 240:    */     {
/* 241:370 */       this.enumeration = enumeration;
/* 242:    */     }
/* 243:    */     
/* 244:    */     public boolean hasNext()
/* 245:    */     {
/* 246:374 */       return this.enumeration.hasMoreElements();
/* 247:    */     }
/* 248:    */     
/* 249:    */     public E next()
/* 250:    */     {
/* 251:378 */       return this.enumeration.nextElement();
/* 252:    */     }
/* 253:    */     
/* 254:    */     public void remove()
/* 255:    */       throws UnsupportedOperationException
/* 256:    */     {
/* 257:382 */       throw new UnsupportedOperationException("Not supported");
/* 258:    */     }
/* 259:    */   }
/* 260:    */   
/* 261:    */   private static class MultiValueMapAdapter<K, V>
/* 262:    */     implements MultiValueMap<K, V>, Serializable
/* 263:    */   {
/* 264:    */     private final Map<K, List<V>> map;
/* 265:    */     
/* 266:    */     public MultiValueMapAdapter(Map<K, List<V>> map)
/* 267:    */     {
/* 268:394 */       Assert.notNull(map, "'map' must not be null");
/* 269:395 */       this.map = map;
/* 270:    */     }
/* 271:    */     
/* 272:    */     public void add(K key, V value)
/* 273:    */     {
/* 274:399 */       List<V> values = (List)this.map.get(key);
/* 275:400 */       if (values == null)
/* 276:    */       {
/* 277:401 */         values = new LinkedList();
/* 278:402 */         this.map.put(key, values);
/* 279:    */       }
/* 280:404 */       values.add(value);
/* 281:    */     }
/* 282:    */     
/* 283:    */     public V getFirst(K key)
/* 284:    */     {
/* 285:408 */       List<V> values = (List)this.map.get(key);
/* 286:409 */       return values != null ? values.get(0) : null;
/* 287:    */     }
/* 288:    */     
/* 289:    */     public void set(K key, V value)
/* 290:    */     {
/* 291:413 */       List<V> values = new LinkedList();
/* 292:414 */       values.add(value);
/* 293:415 */       this.map.put(key, values);
/* 294:    */     }
/* 295:    */     
/* 296:    */     public void setAll(Map<K, V> values)
/* 297:    */     {
/* 298:419 */       for (Map.Entry<K, V> entry : values.entrySet()) {
/* 299:420 */         set(entry.getKey(), entry.getValue());
/* 300:    */       }
/* 301:    */     }
/* 302:    */     
/* 303:    */     public Map<K, V> toSingleValueMap()
/* 304:    */     {
/* 305:425 */       LinkedHashMap<K, V> singleValueMap = new LinkedHashMap(this.map.size());
/* 306:426 */       for (Map.Entry<K, List<V>> entry : this.map.entrySet()) {
/* 307:427 */         singleValueMap.put(entry.getKey(), ((List)entry.getValue()).get(0));
/* 308:    */       }
/* 309:429 */       return singleValueMap;
/* 310:    */     }
/* 311:    */     
/* 312:    */     public int size()
/* 313:    */     {
/* 314:433 */       return this.map.size();
/* 315:    */     }
/* 316:    */     
/* 317:    */     public boolean isEmpty()
/* 318:    */     {
/* 319:437 */       return this.map.isEmpty();
/* 320:    */     }
/* 321:    */     
/* 322:    */     public boolean containsKey(Object key)
/* 323:    */     {
/* 324:441 */       return this.map.containsKey(key);
/* 325:    */     }
/* 326:    */     
/* 327:    */     public boolean containsValue(Object value)
/* 328:    */     {
/* 329:445 */       return this.map.containsValue(value);
/* 330:    */     }
/* 331:    */     
/* 332:    */     public List<V> get(Object key)
/* 333:    */     {
/* 334:449 */       return (List)this.map.get(key);
/* 335:    */     }
/* 336:    */     
/* 337:    */     public List<V> put(K key, List<V> value)
/* 338:    */     {
/* 339:453 */       return (List)this.map.put(key, value);
/* 340:    */     }
/* 341:    */     
/* 342:    */     public List<V> remove(Object key)
/* 343:    */     {
/* 344:457 */       return (List)this.map.remove(key);
/* 345:    */     }
/* 346:    */     
/* 347:    */     public void putAll(Map<? extends K, ? extends List<V>> m)
/* 348:    */     {
/* 349:461 */       this.map.putAll(m);
/* 350:    */     }
/* 351:    */     
/* 352:    */     public void clear()
/* 353:    */     {
/* 354:465 */       this.map.clear();
/* 355:    */     }
/* 356:    */     
/* 357:    */     public Set<K> keySet()
/* 358:    */     {
/* 359:469 */       return this.map.keySet();
/* 360:    */     }
/* 361:    */     
/* 362:    */     public Collection<List<V>> values()
/* 363:    */     {
/* 364:473 */       return this.map.values();
/* 365:    */     }
/* 366:    */     
/* 367:    */     public Set<Map.Entry<K, List<V>>> entrySet()
/* 368:    */     {
/* 369:477 */       return this.map.entrySet();
/* 370:    */     }
/* 371:    */     
/* 372:    */     public boolean equals(Object other)
/* 373:    */     {
/* 374:482 */       if (this == other) {
/* 375:483 */         return true;
/* 376:    */       }
/* 377:485 */       return this.map.equals(other);
/* 378:    */     }
/* 379:    */     
/* 380:    */     public int hashCode()
/* 381:    */     {
/* 382:490 */       return this.map.hashCode();
/* 383:    */     }
/* 384:    */     
/* 385:    */     public String toString()
/* 386:    */     {
/* 387:495 */       return this.map.toString();
/* 388:    */     }
/* 389:    */   }
/* 390:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.CollectionUtils
 * JD-Core Version:    0.7.0.1
 */