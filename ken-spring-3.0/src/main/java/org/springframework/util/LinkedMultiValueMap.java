/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.LinkedHashMap;
/*   6:    */ import java.util.LinkedList;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Map.Entry;
/*  10:    */ import java.util.Set;
/*  11:    */ 
/*  12:    */ public class LinkedMultiValueMap<K, V>
/*  13:    */   implements MultiValueMap<K, V>, Serializable
/*  14:    */ {
/*  15:    */   private static final long serialVersionUID = 3801124242820219131L;
/*  16:    */   private final Map<K, List<V>> targetMap;
/*  17:    */   
/*  18:    */   public LinkedMultiValueMap()
/*  19:    */   {
/*  20: 49 */     this.targetMap = new LinkedHashMap();
/*  21:    */   }
/*  22:    */   
/*  23:    */   public LinkedMultiValueMap(int initialCapacity)
/*  24:    */   {
/*  25: 58 */     this.targetMap = new LinkedHashMap(initialCapacity);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public LinkedMultiValueMap(Map<K, List<V>> otherMap)
/*  29:    */   {
/*  30: 67 */     this.targetMap = new LinkedHashMap(otherMap);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void add(K key, V value)
/*  34:    */   {
/*  35: 74 */     List<V> values = (List)this.targetMap.get(key);
/*  36: 75 */     if (values == null)
/*  37:    */     {
/*  38: 76 */       values = new LinkedList();
/*  39: 77 */       this.targetMap.put(key, values);
/*  40:    */     }
/*  41: 79 */     values.add(value);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public V getFirst(K key)
/*  45:    */   {
/*  46: 83 */     List<V> values = (List)this.targetMap.get(key);
/*  47: 84 */     return values != null ? values.get(0) : null;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void set(K key, V value)
/*  51:    */   {
/*  52: 88 */     List<V> values = new LinkedList();
/*  53: 89 */     values.add(value);
/*  54: 90 */     this.targetMap.put(key, values);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setAll(Map<K, V> values)
/*  58:    */   {
/*  59: 94 */     for (Map.Entry<K, V> entry : values.entrySet()) {
/*  60: 95 */       set(entry.getKey(), entry.getValue());
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   public Map<K, V> toSingleValueMap()
/*  65:    */   {
/*  66:100 */     LinkedHashMap<K, V> singleValueMap = new LinkedHashMap(this.targetMap.size());
/*  67:101 */     for (Map.Entry<K, List<V>> entry : this.targetMap.entrySet()) {
/*  68:102 */       singleValueMap.put(entry.getKey(), ((List)entry.getValue()).get(0));
/*  69:    */     }
/*  70:104 */     return singleValueMap;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public int size()
/*  74:    */   {
/*  75:111 */     return this.targetMap.size();
/*  76:    */   }
/*  77:    */   
/*  78:    */   public boolean isEmpty()
/*  79:    */   {
/*  80:115 */     return this.targetMap.isEmpty();
/*  81:    */   }
/*  82:    */   
/*  83:    */   public boolean containsKey(Object key)
/*  84:    */   {
/*  85:119 */     return this.targetMap.containsKey(key);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public boolean containsValue(Object value)
/*  89:    */   {
/*  90:123 */     return this.targetMap.containsValue(value);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public List<V> get(Object key)
/*  94:    */   {
/*  95:127 */     return (List)this.targetMap.get(key);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public List<V> put(K key, List<V> value)
/*  99:    */   {
/* 100:131 */     return (List)this.targetMap.put(key, value);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public List<V> remove(Object key)
/* 104:    */   {
/* 105:135 */     return (List)this.targetMap.remove(key);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void putAll(Map<? extends K, ? extends List<V>> m)
/* 109:    */   {
/* 110:139 */     this.targetMap.putAll(m);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void clear()
/* 114:    */   {
/* 115:143 */     this.targetMap.clear();
/* 116:    */   }
/* 117:    */   
/* 118:    */   public Set<K> keySet()
/* 119:    */   {
/* 120:147 */     return this.targetMap.keySet();
/* 121:    */   }
/* 122:    */   
/* 123:    */   public Collection<List<V>> values()
/* 124:    */   {
/* 125:151 */     return this.targetMap.values();
/* 126:    */   }
/* 127:    */   
/* 128:    */   public Set<Map.Entry<K, List<V>>> entrySet()
/* 129:    */   {
/* 130:155 */     return this.targetMap.entrySet();
/* 131:    */   }
/* 132:    */   
/* 133:    */   public boolean equals(Object obj)
/* 134:    */   {
/* 135:161 */     return this.targetMap.equals(obj);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public int hashCode()
/* 139:    */   {
/* 140:166 */     return this.targetMap.hashCode();
/* 141:    */   }
/* 142:    */   
/* 143:    */   public String toString()
/* 144:    */   {
/* 145:171 */     return this.targetMap.toString();
/* 146:    */   }
/* 147:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.LinkedMultiValueMap
 * JD-Core Version:    0.7.0.1
 */