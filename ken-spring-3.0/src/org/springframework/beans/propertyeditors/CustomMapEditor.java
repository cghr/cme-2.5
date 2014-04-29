/*   1:    */ package org.springframework.beans.propertyeditors;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditorSupport;
/*   4:    */ import java.util.LinkedHashMap;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Map.Entry;
/*   7:    */ import java.util.SortedMap;
/*   8:    */ import java.util.TreeMap;
/*   9:    */ 
/*  10:    */ public class CustomMapEditor
/*  11:    */   extends PropertyEditorSupport
/*  12:    */ {
/*  13:    */   private final Class mapType;
/*  14:    */   private final boolean nullAsEmptyMap;
/*  15:    */   
/*  16:    */   public CustomMapEditor(Class mapType)
/*  17:    */   {
/*  18: 52 */     this(mapType, false);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public CustomMapEditor(Class mapType, boolean nullAsEmptyMap)
/*  22:    */   {
/*  23: 73 */     if (mapType == null) {
/*  24: 74 */       throw new IllegalArgumentException("Map type is required");
/*  25:    */     }
/*  26: 76 */     if (!Map.class.isAssignableFrom(mapType)) {
/*  27: 77 */       throw new IllegalArgumentException(
/*  28: 78 */         "Map type [" + mapType.getName() + "] does not implement [java.util.Map]");
/*  29:    */     }
/*  30: 80 */     this.mapType = mapType;
/*  31: 81 */     this.nullAsEmptyMap = nullAsEmptyMap;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setAsText(String text)
/*  35:    */     throws IllegalArgumentException
/*  36:    */   {
/*  37: 90 */     setValue(text);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setValue(Object value)
/*  41:    */   {
/*  42: 98 */     if ((value == null) && (this.nullAsEmptyMap))
/*  43:    */     {
/*  44: 99 */       super.setValue(createMap(this.mapType, 0));
/*  45:    */     }
/*  46:101 */     else if ((value == null) || ((this.mapType.isInstance(value)) && (!alwaysCreateNewMap())))
/*  47:    */     {
/*  48:103 */       super.setValue(value);
/*  49:    */     }
/*  50:105 */     else if ((value instanceof Map))
/*  51:    */     {
/*  52:107 */       Map<?, ?> source = (Map)value;
/*  53:108 */       Map target = createMap(this.mapType, source.size());
/*  54:109 */       for (Map.Entry entry : source.entrySet()) {
/*  55:110 */         target.put(convertKey(entry.getKey()), convertValue(entry.getValue()));
/*  56:    */       }
/*  57:112 */       super.setValue(target);
/*  58:    */     }
/*  59:    */     else
/*  60:    */     {
/*  61:115 */       throw new IllegalArgumentException("Value cannot be converted to Map: " + value);
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected Map createMap(Class mapType, int initialCapacity)
/*  66:    */   {
/*  67:127 */     if (!mapType.isInterface()) {
/*  68:    */       try
/*  69:    */       {
/*  70:129 */         return (Map)mapType.newInstance();
/*  71:    */       }
/*  72:    */       catch (Exception ex)
/*  73:    */       {
/*  74:132 */         throw new IllegalArgumentException(
/*  75:133 */           "Could not instantiate map class [" + mapType.getName() + "]: " + ex.getMessage());
/*  76:    */       }
/*  77:    */     }
/*  78:136 */     if (SortedMap.class.equals(mapType)) {
/*  79:137 */       return new TreeMap();
/*  80:    */     }
/*  81:140 */     return new LinkedHashMap(initialCapacity);
/*  82:    */   }
/*  83:    */   
/*  84:    */   protected boolean alwaysCreateNewMap()
/*  85:    */   {
/*  86:153 */     return false;
/*  87:    */   }
/*  88:    */   
/*  89:    */   protected Object convertKey(Object key)
/*  90:    */   {
/*  91:170 */     return key;
/*  92:    */   }
/*  93:    */   
/*  94:    */   protected Object convertValue(Object value)
/*  95:    */   {
/*  96:187 */     return value;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public String getAsText()
/* 100:    */   {
/* 101:197 */     return null;
/* 102:    */   }
/* 103:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.CustomMapEditor
 * JD-Core Version:    0.7.0.1
 */