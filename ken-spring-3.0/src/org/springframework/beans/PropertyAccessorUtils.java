/*   1:    */ package org.springframework.beans;
/*   2:    */ 
/*   3:    */ public abstract class PropertyAccessorUtils
/*   4:    */ {
/*   5:    */   public static String getPropertyName(String propertyPath)
/*   6:    */   {
/*   7: 35 */     int separatorIndex = propertyPath.endsWith("]") ? 
/*   8: 36 */       propertyPath.indexOf('[') : -1;
/*   9: 37 */     return separatorIndex != -1 ? propertyPath.substring(0, separatorIndex) : propertyPath;
/*  10:    */   }
/*  11:    */   
/*  12:    */   public static boolean isNestedOrIndexedProperty(String propertyPath)
/*  13:    */   {
/*  14: 46 */     if (propertyPath == null) {
/*  15: 47 */       return false;
/*  16:    */     }
/*  17: 49 */     for (int i = 0; i < propertyPath.length(); i++)
/*  18:    */     {
/*  19: 50 */       char ch = propertyPath.charAt(i);
/*  20: 51 */       if ((ch == '.') || 
/*  21: 52 */         (ch == '[')) {
/*  22: 53 */         return true;
/*  23:    */       }
/*  24:    */     }
/*  25: 56 */     return false;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public static int getFirstNestedPropertySeparatorIndex(String propertyPath)
/*  29:    */   {
/*  30: 66 */     return getNestedPropertySeparatorIndex(propertyPath, false);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public static int getLastNestedPropertySeparatorIndex(String propertyPath)
/*  34:    */   {
/*  35: 76 */     return getNestedPropertySeparatorIndex(propertyPath, true);
/*  36:    */   }
/*  37:    */   
/*  38:    */   private static int getNestedPropertySeparatorIndex(String propertyPath, boolean last)
/*  39:    */   {
/*  40: 87 */     boolean inKey = false;
/*  41: 88 */     int length = propertyPath.length();
/*  42: 89 */     int i = last ? length - 1 : 0;
/*  43: 90 */     while (last ? i >= 0 : i < length)
/*  44:    */     {
/*  45: 91 */       switch (propertyPath.charAt(i))
/*  46:    */       {
/*  47:    */       case '[': 
/*  48:    */       case ']': 
/*  49: 94 */         inKey = !inKey;
/*  50: 95 */         break;
/*  51:    */       case '.': 
/*  52: 97 */         if (!inKey) {
/*  53: 98 */           return i;
/*  54:    */         }
/*  55:    */         break;
/*  56:    */       }
/*  57:101 */       if (last) {
/*  58:102 */         i--;
/*  59:    */       } else {
/*  60:105 */         i++;
/*  61:    */       }
/*  62:    */     }
/*  63:108 */     return -1;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public static boolean matchesProperty(String registeredPath, String propertyPath)
/*  67:    */   {
/*  68:119 */     if (!registeredPath.startsWith(propertyPath)) {
/*  69:120 */       return false;
/*  70:    */     }
/*  71:122 */     if (registeredPath.length() == propertyPath.length()) {
/*  72:123 */       return true;
/*  73:    */     }
/*  74:125 */     if (registeredPath.charAt(propertyPath.length()) != '[') {
/*  75:126 */       return false;
/*  76:    */     }
/*  77:128 */     return registeredPath.indexOf(']', propertyPath.length() + 1) == 
/*  78:129 */       registeredPath.length() - 1;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static String canonicalPropertyName(String propertyName)
/*  82:    */   {
/*  83:141 */     if (propertyName == null) {
/*  84:142 */       return "";
/*  85:    */     }
/*  86:145 */     StringBuilder sb = new StringBuilder(propertyName);
/*  87:146 */     int searchIndex = 0;
/*  88:147 */     while (searchIndex != -1)
/*  89:    */     {
/*  90:148 */       int keyStart = sb.indexOf("[", searchIndex);
/*  91:149 */       searchIndex = -1;
/*  92:150 */       if (keyStart != -1)
/*  93:    */       {
/*  94:151 */         int keyEnd = sb.indexOf(
/*  95:152 */           "]", keyStart + "[".length());
/*  96:153 */         if (keyEnd != -1)
/*  97:    */         {
/*  98:154 */           String key = sb.substring(keyStart + "[".length(), keyEnd);
/*  99:155 */           if (((key.startsWith("'")) && (key.endsWith("'"))) || ((key.startsWith("\"")) && (key.endsWith("\""))))
/* 100:    */           {
/* 101:156 */             sb.delete(keyStart + 1, keyStart + 2);
/* 102:157 */             sb.delete(keyEnd - 2, keyEnd - 1);
/* 103:158 */             keyEnd -= 2;
/* 104:    */           }
/* 105:160 */           searchIndex = keyEnd + "]".length();
/* 106:    */         }
/* 107:    */       }
/* 108:    */     }
/* 109:164 */     return sb.toString();
/* 110:    */   }
/* 111:    */   
/* 112:    */   public static String[] canonicalPropertyNames(String[] propertyNames)
/* 113:    */   {
/* 114:175 */     if (propertyNames == null) {
/* 115:176 */       return null;
/* 116:    */     }
/* 117:178 */     String[] result = new String[propertyNames.length];
/* 118:179 */     for (int i = 0; i < propertyNames.length; i++) {
/* 119:180 */       result[i] = canonicalPropertyName(propertyNames[i]);
/* 120:    */     }
/* 121:182 */     return result;
/* 122:    */   }
/* 123:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.PropertyAccessorUtils
 * JD-Core Version:    0.7.0.1
 */