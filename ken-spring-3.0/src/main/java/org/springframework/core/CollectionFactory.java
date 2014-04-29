/*   1:    */ package org.springframework.core;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.HashSet;
/*   7:    */ import java.util.IdentityHashMap;
/*   8:    */ import java.util.LinkedHashMap;
/*   9:    */ import java.util.LinkedHashSet;
/*  10:    */ import java.util.LinkedList;
/*  11:    */ import java.util.List;
/*  12:    */ import java.util.Map;
/*  13:    */ import java.util.Set;
/*  14:    */ import java.util.SortedMap;
/*  15:    */ import java.util.SortedSet;
/*  16:    */ import java.util.TreeMap;
/*  17:    */ import java.util.TreeSet;
/*  18:    */ import java.util.concurrent.ConcurrentHashMap;
/*  19:    */ import java.util.concurrent.CopyOnWriteArraySet;
/*  20:    */ import org.springframework.util.LinkedCaseInsensitiveMap;
/*  21:    */ 
/*  22:    */ public abstract class CollectionFactory
/*  23:    */ {
/*  24: 53 */   private static Class navigableSetClass = null;
/*  25: 55 */   private static Class navigableMapClass = null;
/*  26: 57 */   private static final Set<Class> approximableCollectionTypes = new HashSet(10);
/*  27: 59 */   private static final Set<Class> approximableMapTypes = new HashSet(6);
/*  28:    */   
/*  29:    */   static
/*  30:    */   {
/*  31: 64 */     approximableCollectionTypes.add(Collection.class);
/*  32: 65 */     approximableCollectionTypes.add(List.class);
/*  33: 66 */     approximableCollectionTypes.add(Set.class);
/*  34: 67 */     approximableCollectionTypes.add(SortedSet.class);
/*  35: 68 */     approximableMapTypes.add(Map.class);
/*  36: 69 */     approximableMapTypes.add(SortedMap.class);
/*  37:    */     
/*  38:    */ 
/*  39: 72 */     ClassLoader cl = CollectionFactory.class.getClassLoader();
/*  40:    */     try
/*  41:    */     {
/*  42: 74 */       navigableSetClass = cl.loadClass("java.util.NavigableSet");
/*  43: 75 */       navigableMapClass = cl.loadClass("java.util.NavigableMap");
/*  44: 76 */       approximableCollectionTypes.add(navigableSetClass);
/*  45: 77 */       approximableMapTypes.add(navigableMapClass);
/*  46:    */     }
/*  47:    */     catch (ClassNotFoundException localClassNotFoundException) {}
/*  48: 84 */     approximableCollectionTypes.add(ArrayList.class);
/*  49: 85 */     approximableCollectionTypes.add(LinkedList.class);
/*  50: 86 */     approximableCollectionTypes.add(HashSet.class);
/*  51: 87 */     approximableCollectionTypes.add(LinkedHashSet.class);
/*  52: 88 */     approximableCollectionTypes.add(TreeSet.class);
/*  53: 89 */     approximableMapTypes.add(HashMap.class);
/*  54: 90 */     approximableMapTypes.add(LinkedHashMap.class);
/*  55: 91 */     approximableMapTypes.add(TreeMap.class);
/*  56:    */   }
/*  57:    */   
/*  58:    */   @Deprecated
/*  59:    */   public static <T> Set<T> createLinkedSetIfPossible(int initialCapacity)
/*  60:    */   {
/*  61:105 */     return new LinkedHashSet(initialCapacity);
/*  62:    */   }
/*  63:    */   
/*  64:    */   @Deprecated
/*  65:    */   public static <T> Set<T> createCopyOnWriteSet()
/*  66:    */   {
/*  67:117 */     return new CopyOnWriteArraySet();
/*  68:    */   }
/*  69:    */   
/*  70:    */   @Deprecated
/*  71:    */   public static <K, V> Map<K, V> createLinkedMapIfPossible(int initialCapacity)
/*  72:    */   {
/*  73:130 */     return new LinkedHashMap(initialCapacity);
/*  74:    */   }
/*  75:    */   
/*  76:    */   @Deprecated
/*  77:    */   public static Map createLinkedCaseInsensitiveMapIfPossible(int initialCapacity)
/*  78:    */   {
/*  79:142 */     return new LinkedCaseInsensitiveMap(initialCapacity);
/*  80:    */   }
/*  81:    */   
/*  82:    */   @Deprecated
/*  83:    */   public static Map createIdentityMapIfPossible(int initialCapacity)
/*  84:    */   {
/*  85:155 */     return new IdentityHashMap(initialCapacity);
/*  86:    */   }
/*  87:    */   
/*  88:    */   @Deprecated
/*  89:    */   public static Map createConcurrentMapIfPossible(int initialCapacity)
/*  90:    */   {
/*  91:168 */     return new ConcurrentHashMap(initialCapacity);
/*  92:    */   }
/*  93:    */   
/*  94:    */   @Deprecated
/*  95:    */   public static ConcurrentMap createConcurrentMap(int initialCapacity)
/*  96:    */   {
/*  97:181 */     return new JdkConcurrentHashMap(initialCapacity, null);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public static boolean isApproximableCollectionType(Class<?> collectionType)
/* 101:    */   {
/* 102:192 */     return (collectionType != null) && (approximableCollectionTypes.contains(collectionType));
/* 103:    */   }
/* 104:    */   
/* 105:    */   public static Collection createApproximateCollection(Object collection, int initialCapacity)
/* 106:    */   {
/* 107:208 */     if ((collection instanceof LinkedList)) {
/* 108:209 */       return new LinkedList();
/* 109:    */     }
/* 110:211 */     if ((collection instanceof List)) {
/* 111:212 */       return new ArrayList(initialCapacity);
/* 112:    */     }
/* 113:214 */     if ((collection instanceof SortedSet)) {
/* 114:215 */       return new TreeSet(((SortedSet)collection).comparator());
/* 115:    */     }
/* 116:218 */     return new LinkedHashSet(initialCapacity);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public static Collection createCollection(Class<?> collectionType, int initialCapacity)
/* 120:    */   {
/* 121:234 */     if (collectionType.isInterface())
/* 122:    */     {
/* 123:235 */       if (List.class.equals(collectionType)) {
/* 124:236 */         return new ArrayList(initialCapacity);
/* 125:    */       }
/* 126:238 */       if ((SortedSet.class.equals(collectionType)) || (collectionType.equals(navigableSetClass))) {
/* 127:239 */         return new TreeSet();
/* 128:    */       }
/* 129:241 */       if ((Set.class.equals(collectionType)) || (Collection.class.equals(collectionType))) {
/* 130:242 */         return new LinkedHashSet(initialCapacity);
/* 131:    */       }
/* 132:245 */       throw new IllegalArgumentException("Unsupported Collection interface: " + collectionType.getName());
/* 133:    */     }
/* 134:249 */     if (!Collection.class.isAssignableFrom(collectionType)) {
/* 135:250 */       throw new IllegalArgumentException("Unsupported Collection type: " + collectionType.getName());
/* 136:    */     }
/* 137:    */     try
/* 138:    */     {
/* 139:253 */       return (Collection)collectionType.newInstance();
/* 140:    */     }
/* 141:    */     catch (Exception localException)
/* 142:    */     {
/* 143:256 */       throw new IllegalArgumentException("Could not instantiate Collection type: " + collectionType.getName());
/* 144:    */     }
/* 145:    */   }
/* 146:    */   
/* 147:    */   public static boolean isApproximableMapType(Class<?> mapType)
/* 148:    */   {
/* 149:269 */     return (mapType != null) && (approximableMapTypes.contains(mapType));
/* 150:    */   }
/* 151:    */   
/* 152:    */   public static Map createApproximateMap(Object map, int initialCapacity)
/* 153:    */   {
/* 154:283 */     if ((map instanceof SortedMap)) {
/* 155:284 */       return new TreeMap(((SortedMap)map).comparator());
/* 156:    */     }
/* 157:287 */     return new LinkedHashMap(initialCapacity);
/* 158:    */   }
/* 159:    */   
/* 160:    */   public static Map createMap(Class<?> mapType, int initialCapacity)
/* 161:    */   {
/* 162:301 */     if (mapType.isInterface())
/* 163:    */     {
/* 164:302 */       if (Map.class.equals(mapType)) {
/* 165:303 */         return new LinkedHashMap(initialCapacity);
/* 166:    */       }
/* 167:305 */       if ((SortedMap.class.equals(mapType)) || (mapType.equals(navigableMapClass))) {
/* 168:306 */         return new TreeMap();
/* 169:    */       }
/* 170:309 */       throw new IllegalArgumentException("Unsupported Map interface: " + mapType.getName());
/* 171:    */     }
/* 172:313 */     if (!Map.class.isAssignableFrom(mapType)) {
/* 173:314 */       throw new IllegalArgumentException("Unsupported Map type: " + mapType.getName());
/* 174:    */     }
/* 175:    */     try
/* 176:    */     {
/* 177:317 */       return (Map)mapType.newInstance();
/* 178:    */     }
/* 179:    */     catch (Exception localException)
/* 180:    */     {
/* 181:320 */       throw new IllegalArgumentException("Could not instantiate Map type: " + mapType.getName());
/* 182:    */     }
/* 183:    */   }
/* 184:    */   
/* 185:    */   @Deprecated
/* 186:    */   private static class JdkConcurrentHashMap
/* 187:    */     extends ConcurrentHashMap
/* 188:    */     implements ConcurrentMap
/* 189:    */   {
/* 190:    */     private JdkConcurrentHashMap(int initialCapacity)
/* 191:    */     {
/* 192:333 */       super();
/* 193:    */     }
/* 194:    */   }
/* 195:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.CollectionFactory
 * JD-Core Version:    0.7.0.1
 */