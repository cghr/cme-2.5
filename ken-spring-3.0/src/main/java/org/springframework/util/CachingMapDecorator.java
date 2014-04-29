/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.lang.ref.Reference;
/*   5:    */ import java.lang.ref.WeakReference;
/*   6:    */ import java.util.Collection;
/*   7:    */ import java.util.Collections;
/*   8:    */ import java.util.HashMap;
/*   9:    */ import java.util.Iterator;
/*  10:    */ import java.util.LinkedHashMap;
/*  11:    */ import java.util.LinkedHashSet;
/*  12:    */ import java.util.LinkedList;
/*  13:    */ import java.util.Map;
/*  14:    */ import java.util.Map.Entry;
/*  15:    */ import java.util.Set;
/*  16:    */ import java.util.WeakHashMap;
/*  17:    */ 
/*  18:    */ public abstract class CachingMapDecorator<K, V>
/*  19:    */   implements Map<K, V>, Serializable
/*  20:    */ {
/*  21: 47 */   private static Object NULL_VALUE = new Object();
/*  22:    */   private final Map<K, Object> targetMap;
/*  23:    */   private final boolean synchronize;
/*  24:    */   private final boolean weak;
/*  25:    */   
/*  26:    */   public CachingMapDecorator()
/*  27:    */   {
/*  28: 62 */     this(false);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public CachingMapDecorator(boolean weak)
/*  32:    */   {
/*  33: 71 */     Map<K, Object> internalMap = weak ? new WeakHashMap() : new HashMap();
/*  34: 72 */     this.targetMap = Collections.synchronizedMap(internalMap);
/*  35: 73 */     this.synchronize = true;
/*  36: 74 */     this.weak = weak;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public CachingMapDecorator(boolean weak, int size)
/*  40:    */   {
/*  41: 84 */     Map<K, Object> internalMap = weak ? new WeakHashMap(size) : new HashMap(size);
/*  42: 85 */     this.targetMap = Collections.synchronizedMap(internalMap);
/*  43: 86 */     this.synchronize = true;
/*  44: 87 */     this.weak = weak;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public CachingMapDecorator(Map<K, V> targetMap)
/*  48:    */   {
/*  49: 97 */     this(targetMap, false, false);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public CachingMapDecorator(Map<K, V> targetMap, boolean synchronize, boolean weak)
/*  53:    */   {
/*  54:110 */     Assert.notNull(targetMap, "'targetMap' must not be null");
/*  55:111 */     this.targetMap = (synchronize ? Collections.synchronizedMap(targetMap) : targetMap);
/*  56:112 */     this.synchronize = synchronize;
/*  57:113 */     this.weak = weak;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public int size()
/*  61:    */   {
/*  62:118 */     return this.targetMap.size();
/*  63:    */   }
/*  64:    */   
/*  65:    */   public boolean isEmpty()
/*  66:    */   {
/*  67:122 */     return this.targetMap.isEmpty();
/*  68:    */   }
/*  69:    */   
/*  70:    */   public boolean containsKey(Object key)
/*  71:    */   {
/*  72:126 */     return this.targetMap.containsKey(key);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public boolean containsValue(Object value)
/*  76:    */   {
/*  77:130 */     Object valueToCheck = value != null ? value : NULL_VALUE;
/*  78:131 */     if (this.synchronize) {
/*  79:132 */       synchronized (this.targetMap)
/*  80:    */       {
/*  81:133 */         return containsValueOrReference(valueToCheck);
/*  82:    */       }
/*  83:    */     }
/*  84:137 */     return containsValueOrReference(valueToCheck);
/*  85:    */   }
/*  86:    */   
/*  87:    */   private boolean containsValueOrReference(Object value)
/*  88:    */   {
/*  89:142 */     if (this.targetMap.containsValue(value)) {
/*  90:143 */       return true;
/*  91:    */     }
/*  92:145 */     for (Object mapVal : this.targetMap.values()) {
/*  93:146 */       if (((mapVal instanceof Reference)) && (value.equals(((Reference)mapVal).get()))) {
/*  94:147 */         return true;
/*  95:    */       }
/*  96:    */     }
/*  97:150 */     return false;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public V remove(Object key)
/* 101:    */   {
/* 102:154 */     return unwrapReturnValue(this.targetMap.remove(key));
/* 103:    */   }
/* 104:    */   
/* 105:    */   private V unwrapReturnValue(Object value)
/* 106:    */   {
/* 107:159 */     Object returnValue = value;
/* 108:160 */     if ((returnValue instanceof Reference)) {
/* 109:161 */       returnValue = ((Reference)returnValue).get();
/* 110:    */     }
/* 111:163 */     return returnValue == NULL_VALUE ? null : returnValue;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void putAll(Map<? extends K, ? extends V> map)
/* 115:    */   {
/* 116:167 */     this.targetMap.putAll(map);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void clear()
/* 120:    */   {
/* 121:171 */     this.targetMap.clear();
/* 122:    */   }
/* 123:    */   
/* 124:    */   public Set<K> keySet()
/* 125:    */   {
/* 126:175 */     if (this.synchronize) {
/* 127:176 */       synchronized (this.targetMap)
/* 128:    */       {
/* 129:177 */         return new LinkedHashSet((Collection)this.targetMap.keySet());
/* 130:    */       }
/* 131:    */     }
/* 132:181 */     return new LinkedHashSet((Collection)this.targetMap.keySet());
/* 133:    */   }
/* 134:    */   
/* 135:    */   public Collection<V> values()
/* 136:    */   {
/* 137:186 */     if (this.synchronize) {
/* 138:187 */       synchronized (this.targetMap)
/* 139:    */       {
/* 140:188 */         return valuesCopy();
/* 141:    */       }
/* 142:    */     }
/* 143:192 */     return valuesCopy();
/* 144:    */   }
/* 145:    */   
/* 146:    */   private Collection<V> valuesCopy()
/* 147:    */   {
/* 148:198 */     LinkedList<V> values = new LinkedList();
/* 149:199 */     for (Iterator<Object> it = this.targetMap.values().iterator(); it.hasNext();)
/* 150:    */     {
/* 151:200 */       Object value = it.next();
/* 152:201 */       if ((value instanceof Reference))
/* 153:    */       {
/* 154:202 */         value = ((Reference)value).get();
/* 155:203 */         if (value == null)
/* 156:    */         {
/* 157:204 */           it.remove();
/* 158:205 */           continue;
/* 159:    */         }
/* 160:    */       }
/* 161:208 */       values.add(value == NULL_VALUE ? null : value);
/* 162:    */     }
/* 163:210 */     return values;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public Set<Map.Entry<K, V>> entrySet()
/* 167:    */   {
/* 168:214 */     if (this.synchronize) {
/* 169:215 */       synchronized (this.targetMap)
/* 170:    */       {
/* 171:216 */         return entryCopy();
/* 172:    */       }
/* 173:    */     }
/* 174:220 */     return entryCopy();
/* 175:    */   }
/* 176:    */   
/* 177:    */   private Set<Map.Entry<K, V>> entryCopy()
/* 178:    */   {
/* 179:226 */     Map<K, V> entries = new LinkedHashMap();
/* 180:227 */     for (Iterator<Map.Entry<K, Object>> it = this.targetMap.entrySet().iterator(); it.hasNext();)
/* 181:    */     {
/* 182:228 */       Map.Entry<K, Object> entry = (Map.Entry)it.next();
/* 183:229 */       Object value = entry.getValue();
/* 184:230 */       if ((value instanceof Reference))
/* 185:    */       {
/* 186:231 */         value = ((Reference)value).get();
/* 187:232 */         if (value == null)
/* 188:    */         {
/* 189:233 */           it.remove();
/* 190:234 */           continue;
/* 191:    */         }
/* 192:    */       }
/* 193:237 */       entries.put(entry.getKey(), value == NULL_VALUE ? null : value);
/* 194:    */     }
/* 195:239 */     return entries.entrySet();
/* 196:    */   }
/* 197:    */   
/* 198:    */   public V put(K key, V value)
/* 199:    */   {
/* 200:249 */     Object newValue = value;
/* 201:250 */     if (value == null) {
/* 202:251 */       newValue = NULL_VALUE;
/* 203:253 */     } else if (useWeakValue(key, value)) {
/* 204:254 */       newValue = new WeakReference(newValue);
/* 205:    */     }
/* 206:256 */     return unwrapReturnValue(this.targetMap.put(key, newValue));
/* 207:    */   }
/* 208:    */   
/* 209:    */   protected boolean useWeakValue(K key, V value)
/* 210:    */   {
/* 211:268 */     return this.weak;
/* 212:    */   }
/* 213:    */   
/* 214:    */   public V get(Object key)
/* 215:    */   {
/* 216:282 */     Object value = this.targetMap.get(key);
/* 217:283 */     if ((value instanceof Reference)) {
/* 218:284 */       value = ((Reference)value).get();
/* 219:    */     }
/* 220:286 */     if (value == null)
/* 221:    */     {
/* 222:287 */       V newValue = create(key);
/* 223:288 */       put(key, newValue);
/* 224:289 */       return newValue;
/* 225:    */     }
/* 226:291 */     return value == NULL_VALUE ? null : value;
/* 227:    */   }
/* 228:    */   
/* 229:    */   protected abstract V create(K paramK);
/* 230:    */   
/* 231:    */   public String toString()
/* 232:    */   {
/* 233:305 */     return "CachingMapDecorator [" + getClass().getName() + "]:" + this.targetMap;
/* 234:    */   }
/* 235:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.CachingMapDecorator
 * JD-Core Version:    0.7.0.1
 */