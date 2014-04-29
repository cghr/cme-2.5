/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.LinkedHashMap;
/*   5:    */ import java.util.Locale;
/*   6:    */ import java.util.Map;
/*   7:    */ 
/*   8:    */ public class LinkedCaseInsensitiveMap<V>
/*   9:    */   extends LinkedHashMap<String, V>
/*  10:    */ {
/*  11:    */   private final Map<String, String> caseInsensitiveKeys;
/*  12:    */   private final Locale locale;
/*  13:    */   
/*  14:    */   public LinkedCaseInsensitiveMap()
/*  15:    */   {
/*  16: 48 */     this(null);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public LinkedCaseInsensitiveMap(Locale locale)
/*  20:    */   {
/*  21: 59 */     this.caseInsensitiveKeys = new HashMap();
/*  22: 60 */     this.locale = (locale != null ? locale : Locale.getDefault());
/*  23:    */   }
/*  24:    */   
/*  25:    */   public LinkedCaseInsensitiveMap(int initialCapacity)
/*  26:    */   {
/*  27: 71 */     this(initialCapacity, null);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public LinkedCaseInsensitiveMap(int initialCapacity, Locale locale)
/*  31:    */   {
/*  32: 83 */     super(initialCapacity);
/*  33: 84 */     this.caseInsensitiveKeys = new HashMap(initialCapacity);
/*  34: 85 */     this.locale = (locale != null ? locale : Locale.getDefault());
/*  35:    */   }
/*  36:    */   
/*  37:    */   public V put(String key, V value)
/*  38:    */   {
/*  39: 91 */     this.caseInsensitiveKeys.put(convertKey(key), key);
/*  40: 92 */     return super.put(key, value);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public boolean containsKey(Object key)
/*  44:    */   {
/*  45: 97 */     return ((key instanceof String)) && (this.caseInsensitiveKeys.containsKey(convertKey((String)key)));
/*  46:    */   }
/*  47:    */   
/*  48:    */   public V get(Object key)
/*  49:    */   {
/*  50:102 */     if ((key instanceof String)) {
/*  51:103 */       return super.get(this.caseInsensitiveKeys.get(convertKey((String)key)));
/*  52:    */     }
/*  53:106 */     return null;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public V remove(Object key)
/*  57:    */   {
/*  58:112 */     if ((key instanceof String)) {
/*  59:113 */       return super.remove(this.caseInsensitiveKeys.remove(convertKey((String)key)));
/*  60:    */     }
/*  61:116 */     return null;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void clear()
/*  65:    */   {
/*  66:122 */     this.caseInsensitiveKeys.clear();
/*  67:123 */     super.clear();
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected String convertKey(String key)
/*  71:    */   {
/*  72:136 */     return key.toLowerCase(this.locale);
/*  73:    */   }
/*  74:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.LinkedCaseInsensitiveMap
 * JD-Core Version:    0.7.0.1
 */