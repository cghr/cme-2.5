/*   1:    */ package org.springframework.core.env;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.Map.Entry;
/*   6:    */ import java.util.Set;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ 
/*   9:    */ abstract class ReadOnlySystemAttributesMap
/*  10:    */   implements Map<String, String>
/*  11:    */ {
/*  12:    */   public boolean containsKey(Object key)
/*  13:    */   {
/*  14: 39 */     return get(key) != null;
/*  15:    */   }
/*  16:    */   
/*  17:    */   public String get(Object key)
/*  18:    */   {
/*  19: 47 */     Assert.isInstanceOf(String.class, key, 
/*  20: 48 */       String.format("expected key [%s] to be of type String, got %s", new Object[] {
/*  21: 49 */       key, key.getClass().getName() }));
/*  22:    */     
/*  23: 51 */     return getSystemAttribute((String)key);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public boolean isEmpty()
/*  27:    */   {
/*  28: 55 */     return false;
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected abstract String getSystemAttribute(String paramString);
/*  32:    */   
/*  33:    */   public int size()
/*  34:    */   {
/*  35: 68 */     throw new UnsupportedOperationException();
/*  36:    */   }
/*  37:    */   
/*  38:    */   public String put(String key, String value)
/*  39:    */   {
/*  40: 72 */     throw new UnsupportedOperationException();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public boolean containsValue(Object value)
/*  44:    */   {
/*  45: 76 */     throw new UnsupportedOperationException();
/*  46:    */   }
/*  47:    */   
/*  48:    */   public String remove(Object key)
/*  49:    */   {
/*  50: 80 */     throw new UnsupportedOperationException();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void clear()
/*  54:    */   {
/*  55: 84 */     throw new UnsupportedOperationException();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public Set<String> keySet()
/*  59:    */   {
/*  60: 88 */     throw new UnsupportedOperationException();
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void putAll(Map<? extends String, ? extends String> m)
/*  64:    */   {
/*  65: 92 */     throw new UnsupportedOperationException();
/*  66:    */   }
/*  67:    */   
/*  68:    */   public Collection<String> values()
/*  69:    */   {
/*  70: 96 */     throw new UnsupportedOperationException();
/*  71:    */   }
/*  72:    */   
/*  73:    */   public Set<Map.Entry<String, String>> entrySet()
/*  74:    */   {
/*  75:100 */     throw new UnsupportedOperationException();
/*  76:    */   }
/*  77:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.ReadOnlySystemAttributesMap
 * JD-Core Version:    0.7.0.1
 */