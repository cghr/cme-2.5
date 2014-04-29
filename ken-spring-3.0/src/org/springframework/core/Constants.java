/*   1:    */ package org.springframework.core;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Field;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.HashSet;
/*   6:    */ import java.util.Locale;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Map.Entry;
/*   9:    */ import java.util.Set;
/*  10:    */ import org.springframework.util.Assert;
/*  11:    */ import org.springframework.util.ReflectionUtils;
/*  12:    */ 
/*  13:    */ public class Constants
/*  14:    */ {
/*  15:    */   private final String className;
/*  16: 52 */   private final Map<String, Object> fieldCache = new HashMap();
/*  17:    */   
/*  18:    */   public Constants(Class clazz)
/*  19:    */   {
/*  20: 62 */     Assert.notNull(clazz);
/*  21: 63 */     this.className = clazz.getName();
/*  22: 64 */     Field[] fields = clazz.getFields();
/*  23: 65 */     for (Field field : fields) {
/*  24: 66 */       if (ReflectionUtils.isPublicStaticFinal(field))
/*  25:    */       {
/*  26: 67 */         String name = field.getName();
/*  27:    */         try
/*  28:    */         {
/*  29: 69 */           Object value = field.get(null);
/*  30: 70 */           this.fieldCache.put(name, value);
/*  31:    */         }
/*  32:    */         catch (IllegalAccessException localIllegalAccessException) {}
/*  33:    */       }
/*  34:    */     }
/*  35:    */   }
/*  36:    */   
/*  37:    */   public final String getClassName()
/*  38:    */   {
/*  39: 84 */     return this.className;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public final int getSize()
/*  43:    */   {
/*  44: 91 */     return this.fieldCache.size();
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected final Map<String, Object> getFieldCache()
/*  48:    */   {
/*  49: 99 */     return this.fieldCache;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public Number asNumber(String code)
/*  53:    */     throws ConstantException
/*  54:    */   {
/*  55:112 */     Object obj = asObject(code);
/*  56:113 */     if (!(obj instanceof Number)) {
/*  57:114 */       throw new ConstantException(this.className, code, "not a Number");
/*  58:    */     }
/*  59:116 */     return (Number)obj;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public String asString(String code)
/*  63:    */     throws ConstantException
/*  64:    */   {
/*  65:128 */     return asObject(code).toString();
/*  66:    */   }
/*  67:    */   
/*  68:    */   public Object asObject(String code)
/*  69:    */     throws ConstantException
/*  70:    */   {
/*  71:140 */     Assert.notNull(code, "Code must not be null");
/*  72:141 */     String codeToUse = code.toUpperCase(Locale.ENGLISH);
/*  73:142 */     Object val = this.fieldCache.get(codeToUse);
/*  74:143 */     if (val == null) {
/*  75:144 */       throw new ConstantException(this.className, codeToUse, "not found");
/*  76:    */     }
/*  77:146 */     return val;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public Set<String> getNames(String namePrefix)
/*  81:    */   {
/*  82:161 */     String prefixToUse = namePrefix != null ? namePrefix.trim().toUpperCase(Locale.ENGLISH) : "";
/*  83:162 */     Set<String> names = new HashSet();
/*  84:163 */     for (String code : this.fieldCache.keySet()) {
/*  85:164 */       if (code.startsWith(prefixToUse)) {
/*  86:165 */         names.add(code);
/*  87:    */       }
/*  88:    */     }
/*  89:168 */     return names;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public Set<String> getNamesForProperty(String propertyName)
/*  93:    */   {
/*  94:179 */     return getNames(propertyToConstantNamePrefix(propertyName));
/*  95:    */   }
/*  96:    */   
/*  97:    */   public Set getNamesForSuffix(String nameSuffix)
/*  98:    */   {
/*  99:193 */     String suffixToUse = nameSuffix != null ? nameSuffix.trim().toUpperCase(Locale.ENGLISH) : "";
/* 100:194 */     Set<String> names = new HashSet();
/* 101:195 */     for (String code : this.fieldCache.keySet()) {
/* 102:196 */       if (code.endsWith(suffixToUse)) {
/* 103:197 */         names.add(code);
/* 104:    */       }
/* 105:    */     }
/* 106:200 */     return names;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public Set<Object> getValues(String namePrefix)
/* 110:    */   {
/* 111:215 */     String prefixToUse = namePrefix != null ? namePrefix.trim().toUpperCase(Locale.ENGLISH) : "";
/* 112:216 */     Set<Object> values = new HashSet();
/* 113:217 */     for (String code : this.fieldCache.keySet()) {
/* 114:218 */       if (code.startsWith(prefixToUse)) {
/* 115:219 */         values.add(this.fieldCache.get(code));
/* 116:    */       }
/* 117:    */     }
/* 118:222 */     return values;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public Set<Object> getValuesForProperty(String propertyName)
/* 122:    */   {
/* 123:233 */     return getValues(propertyToConstantNamePrefix(propertyName));
/* 124:    */   }
/* 125:    */   
/* 126:    */   public Set<Object> getValuesForSuffix(String nameSuffix)
/* 127:    */   {
/* 128:247 */     String suffixToUse = nameSuffix != null ? nameSuffix.trim().toUpperCase(Locale.ENGLISH) : "";
/* 129:248 */     Set<Object> values = new HashSet();
/* 130:249 */     for (String code : this.fieldCache.keySet()) {
/* 131:250 */       if (code.endsWith(suffixToUse)) {
/* 132:251 */         values.add(this.fieldCache.get(code));
/* 133:    */       }
/* 134:    */     }
/* 135:254 */     return values;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public String toCode(Object value, String namePrefix)
/* 139:    */     throws ConstantException
/* 140:    */   {
/* 141:267 */     String prefixToUse = namePrefix != null ? namePrefix.trim().toUpperCase(Locale.ENGLISH) : null;
/* 142:268 */     for (Map.Entry<String, Object> entry : this.fieldCache.entrySet()) {
/* 143:269 */       if ((((String)entry.getKey()).startsWith(prefixToUse)) && (entry.getValue().equals(value))) {
/* 144:270 */         return (String)entry.getKey();
/* 145:    */       }
/* 146:    */     }
/* 147:273 */     throw new ConstantException(this.className, prefixToUse, value);
/* 148:    */   }
/* 149:    */   
/* 150:    */   public String toCodeForProperty(Object value, String propertyName)
/* 151:    */     throws ConstantException
/* 152:    */   {
/* 153:286 */     return toCode(value, propertyToConstantNamePrefix(propertyName));
/* 154:    */   }
/* 155:    */   
/* 156:    */   public String toCodeForSuffix(Object value, String nameSuffix)
/* 157:    */     throws ConstantException
/* 158:    */   {
/* 159:298 */     String suffixToUse = nameSuffix != null ? nameSuffix.trim().toUpperCase(Locale.ENGLISH) : null;
/* 160:299 */     for (Map.Entry<String, Object> entry : this.fieldCache.entrySet()) {
/* 161:300 */       if ((((String)entry.getKey()).endsWith(suffixToUse)) && (entry.getValue().equals(value))) {
/* 162:301 */         return (String)entry.getKey();
/* 163:    */       }
/* 164:    */     }
/* 165:304 */     throw new ConstantException(this.className, suffixToUse, value);
/* 166:    */   }
/* 167:    */   
/* 168:    */   public String propertyToConstantNamePrefix(String propertyName)
/* 169:    */   {
/* 170:322 */     StringBuilder parsedPrefix = new StringBuilder();
/* 171:323 */     for (int i = 0; i < propertyName.length(); i++)
/* 172:    */     {
/* 173:324 */       char c = propertyName.charAt(i);
/* 174:325 */       if (Character.isUpperCase(c))
/* 175:    */       {
/* 176:326 */         parsedPrefix.append("_");
/* 177:327 */         parsedPrefix.append(c);
/* 178:    */       }
/* 179:    */       else
/* 180:    */       {
/* 181:330 */         parsedPrefix.append(Character.toUpperCase(c));
/* 182:    */       }
/* 183:    */     }
/* 184:333 */     return parsedPrefix.toString();
/* 185:    */   }
/* 186:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.Constants
 * JD-Core Version:    0.7.0.1
 */