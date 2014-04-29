/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.Map;
/*   5:    */ 
/*   6:    */ public abstract class Assert
/*   7:    */ {
/*   8:    */   public static void isTrue(boolean expression, String message)
/*   9:    */   {
/*  10: 64 */     if (!expression) {
/*  11: 65 */       throw new IllegalArgumentException(message);
/*  12:    */     }
/*  13:    */   }
/*  14:    */   
/*  15:    */   public static void isTrue(boolean expression)
/*  16:    */   {
/*  17: 77 */     isTrue(expression, "[Assertion failed] - this expression must be true");
/*  18:    */   }
/*  19:    */   
/*  20:    */   public static void isNull(Object object, String message)
/*  21:    */   {
/*  22: 88 */     if (object != null) {
/*  23: 89 */       throw new IllegalArgumentException(message);
/*  24:    */     }
/*  25:    */   }
/*  26:    */   
/*  27:    */   public static void isNull(Object object)
/*  28:    */   {
/*  29:100 */     isNull(object, "[Assertion failed] - the object argument must be null");
/*  30:    */   }
/*  31:    */   
/*  32:    */   public static void notNull(Object object, String message)
/*  33:    */   {
/*  34:111 */     if (object == null) {
/*  35:112 */       throw new IllegalArgumentException(message);
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:    */   public static void notNull(Object object)
/*  40:    */   {
/*  41:123 */     notNull(object, "[Assertion failed] - this argument is required; it must not be null");
/*  42:    */   }
/*  43:    */   
/*  44:    */   public static void hasLength(String text, String message)
/*  45:    */   {
/*  46:135 */     if (!StringUtils.hasLength(text)) {
/*  47:136 */       throw new IllegalArgumentException(message);
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static void hasLength(String text)
/*  52:    */   {
/*  53:148 */     hasLength(text, 
/*  54:149 */       "[Assertion failed] - this String argument must have length; it must not be null or empty");
/*  55:    */   }
/*  56:    */   
/*  57:    */   public static void hasText(String text, String message)
/*  58:    */   {
/*  59:161 */     if (!StringUtils.hasText(text)) {
/*  60:162 */       throw new IllegalArgumentException(message);
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   public static void hasText(String text)
/*  65:    */   {
/*  66:174 */     hasText(text, 
/*  67:175 */       "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static void doesNotContain(String textToSearch, String substring, String message)
/*  71:    */   {
/*  72:186 */     if ((StringUtils.hasLength(textToSearch)) && (StringUtils.hasLength(substring)) && 
/*  73:187 */       (textToSearch.indexOf(substring) != -1)) {
/*  74:188 */       throw new IllegalArgumentException(message);
/*  75:    */     }
/*  76:    */   }
/*  77:    */   
/*  78:    */   public static void doesNotContain(String textToSearch, String substring)
/*  79:    */   {
/*  80:199 */     doesNotContain(textToSearch, substring, 
/*  81:200 */       "[Assertion failed] - this String argument must not contain the substring [" + substring + "]");
/*  82:    */   }
/*  83:    */   
/*  84:    */   public static void notEmpty(Object[] array, String message)
/*  85:    */   {
/*  86:213 */     if (ObjectUtils.isEmpty(array)) {
/*  87:214 */       throw new IllegalArgumentException(message);
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   public static void notEmpty(Object[] array)
/*  92:    */   {
/*  93:226 */     notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
/*  94:    */   }
/*  95:    */   
/*  96:    */   public static void noNullElements(Object[] array, String message)
/*  97:    */   {
/*  98:238 */     if (array != null) {
/*  99:239 */       for (int i = 0; i < array.length; i++) {
/* 100:240 */         if (array[i] == null) {
/* 101:241 */           throw new IllegalArgumentException(message);
/* 102:    */         }
/* 103:    */       }
/* 104:    */     }
/* 105:    */   }
/* 106:    */   
/* 107:    */   public static void noNullElements(Object[] array)
/* 108:    */   {
/* 109:255 */     noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
/* 110:    */   }
/* 111:    */   
/* 112:    */   public static void notEmpty(Collection collection, String message)
/* 113:    */   {
/* 114:267 */     if (CollectionUtils.isEmpty(collection)) {
/* 115:268 */       throw new IllegalArgumentException(message);
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   public static void notEmpty(Collection collection)
/* 120:    */   {
/* 121:280 */     notEmpty(collection, 
/* 122:281 */       "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
/* 123:    */   }
/* 124:    */   
/* 125:    */   public static void notEmpty(Map map, String message)
/* 126:    */   {
/* 127:293 */     if (CollectionUtils.isEmpty(map)) {
/* 128:294 */       throw new IllegalArgumentException(message);
/* 129:    */     }
/* 130:    */   }
/* 131:    */   
/* 132:    */   public static void notEmpty(Map map)
/* 133:    */   {
/* 134:306 */     notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
/* 135:    */   }
/* 136:    */   
/* 137:    */   public static void isInstanceOf(Class clazz, Object obj)
/* 138:    */   {
/* 139:319 */     isInstanceOf(clazz, obj, "");
/* 140:    */   }
/* 141:    */   
/* 142:    */   public static void isInstanceOf(Class type, Object obj, String message)
/* 143:    */   {
/* 144:335 */     notNull(type, "Type to check against must not be null");
/* 145:336 */     if (!type.isInstance(obj)) {
/* 146:337 */       throw new IllegalArgumentException(message + 
/* 147:338 */         "Object of class [" + (obj != null ? obj.getClass().getName() : "null") + 
/* 148:339 */         "] must be an instance of " + type);
/* 149:    */     }
/* 150:    */   }
/* 151:    */   
/* 152:    */   public static void isAssignable(Class superType, Class subType)
/* 153:    */   {
/* 154:351 */     isAssignable(superType, subType, "");
/* 155:    */   }
/* 156:    */   
/* 157:    */   public static void isAssignable(Class superType, Class subType, String message)
/* 158:    */   {
/* 159:366 */     notNull(superType, "Type to check against must not be null");
/* 160:367 */     if ((subType == null) || (!superType.isAssignableFrom(subType))) {
/* 161:368 */       throw new IllegalArgumentException(message + subType + " is not assignable to " + superType);
/* 162:    */     }
/* 163:    */   }
/* 164:    */   
/* 165:    */   public static void state(boolean expression, String message)
/* 166:    */   {
/* 167:383 */     if (!expression) {
/* 168:384 */       throw new IllegalStateException(message);
/* 169:    */     }
/* 170:    */   }
/* 171:    */   
/* 172:    */   public static void state(boolean expression)
/* 173:    */   {
/* 174:398 */     state(expression, "[Assertion failed] - this state invariant must be true");
/* 175:    */   }
/* 176:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.Assert
 * JD-Core Version:    0.7.0.1
 */