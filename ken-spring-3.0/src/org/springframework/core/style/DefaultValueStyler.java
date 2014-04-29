/*   1:    */ package org.springframework.core.style;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Map.Entry;
/*   9:    */ import java.util.Set;
/*  10:    */ import org.springframework.util.ClassUtils;
/*  11:    */ import org.springframework.util.ObjectUtils;
/*  12:    */ 
/*  13:    */ public class DefaultValueStyler
/*  14:    */   implements ValueStyler
/*  15:    */ {
/*  16:    */   private static final String EMPTY = "[empty]";
/*  17:    */   private static final String NULL = "[null]";
/*  18:    */   private static final String COLLECTION = "collection";
/*  19:    */   private static final String SET = "set";
/*  20:    */   private static final String LIST = "list";
/*  21:    */   private static final String MAP = "map";
/*  22:    */   private static final String ARRAY = "array";
/*  23:    */   
/*  24:    */   public String style(Object value)
/*  25:    */   {
/*  26: 52 */     if (value == null) {
/*  27: 53 */       return "[null]";
/*  28:    */     }
/*  29: 55 */     if ((value instanceof String)) {
/*  30: 56 */       return "'" + value + "'";
/*  31:    */     }
/*  32: 58 */     if ((value instanceof Class)) {
/*  33: 59 */       return ClassUtils.getShortName((Class)value);
/*  34:    */     }
/*  35: 61 */     if ((value instanceof Method))
/*  36:    */     {
/*  37: 62 */       Method method = (Method)value;
/*  38: 63 */       return method.getName() + "@" + ClassUtils.getShortName(method.getDeclaringClass());
/*  39:    */     }
/*  40: 65 */     if ((value instanceof Map)) {
/*  41: 66 */       return style((Map)value);
/*  42:    */     }
/*  43: 68 */     if ((value instanceof Map.Entry)) {
/*  44: 69 */       return style((Map.Entry)value);
/*  45:    */     }
/*  46: 71 */     if ((value instanceof Collection)) {
/*  47: 72 */       return style((Collection)value);
/*  48:    */     }
/*  49: 74 */     if (value.getClass().isArray()) {
/*  50: 75 */       return styleArray(ObjectUtils.toObjectArray(value));
/*  51:    */     }
/*  52: 78 */     return String.valueOf(value);
/*  53:    */   }
/*  54:    */   
/*  55:    */   private String style(Map value)
/*  56:    */   {
/*  57: 83 */     StringBuilder result = new StringBuilder(value.size() * 8 + 16);
/*  58: 84 */     result.append("map[");
/*  59: 85 */     for (Iterator it = value.entrySet().iterator(); it.hasNext();)
/*  60:    */     {
/*  61: 86 */       Map.Entry entry = (Map.Entry)it.next();
/*  62: 87 */       result.append(style(entry));
/*  63: 88 */       if (it.hasNext()) {
/*  64: 89 */         result.append(',').append(' ');
/*  65:    */       }
/*  66:    */     }
/*  67: 92 */     if (value.isEmpty()) {
/*  68: 93 */       result.append("[empty]");
/*  69:    */     }
/*  70: 95 */     result.append("]");
/*  71: 96 */     return result.toString();
/*  72:    */   }
/*  73:    */   
/*  74:    */   private String style(Map.Entry value)
/*  75:    */   {
/*  76:100 */     return style(value.getKey()) + " -> " + style(value.getValue());
/*  77:    */   }
/*  78:    */   
/*  79:    */   private String style(Collection value)
/*  80:    */   {
/*  81:104 */     StringBuilder result = new StringBuilder(value.size() * 8 + 16);
/*  82:105 */     result.append(getCollectionTypeString(value)).append('[');
/*  83:106 */     for (Iterator i = value.iterator(); i.hasNext();)
/*  84:    */     {
/*  85:107 */       result.append(style(i.next()));
/*  86:108 */       if (i.hasNext()) {
/*  87:109 */         result.append(',').append(' ');
/*  88:    */       }
/*  89:    */     }
/*  90:112 */     if (value.isEmpty()) {
/*  91:113 */       result.append("[empty]");
/*  92:    */     }
/*  93:115 */     result.append("]");
/*  94:116 */     return result.toString();
/*  95:    */   }
/*  96:    */   
/*  97:    */   private String getCollectionTypeString(Collection value)
/*  98:    */   {
/*  99:120 */     if ((value instanceof List)) {
/* 100:121 */       return "list";
/* 101:    */     }
/* 102:123 */     if ((value instanceof Set)) {
/* 103:124 */       return "set";
/* 104:    */     }
/* 105:127 */     return "collection";
/* 106:    */   }
/* 107:    */   
/* 108:    */   private String styleArray(Object[] array)
/* 109:    */   {
/* 110:132 */     StringBuilder result = new StringBuilder(array.length * 8 + 16);
/* 111:133 */     result.append("array<").append(ClassUtils.getShortName(array.getClass().getComponentType())).append(">[");
/* 112:134 */     for (int i = 0; i < array.length - 1; i++)
/* 113:    */     {
/* 114:135 */       result.append(style(array[i]));
/* 115:136 */       result.append(',').append(' ');
/* 116:    */     }
/* 117:138 */     if (array.length > 0) {
/* 118:139 */       result.append(style(array[(array.length - 1)]));
/* 119:    */     } else {
/* 120:142 */       result.append("[empty]");
/* 121:    */     }
/* 122:144 */     result.append("]");
/* 123:145 */     return result.toString();
/* 124:    */   }
/* 125:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.style.DefaultValueStyler
 * JD-Core Version:    0.7.0.1
 */