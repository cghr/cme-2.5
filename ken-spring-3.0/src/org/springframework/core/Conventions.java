/*   1:    */ package org.springframework.core;
/*   2:    */ 
/*   3:    */ import java.io.Externalizable;
/*   4:    */ import java.io.Serializable;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.lang.reflect.Proxy;
/*   7:    */ import java.util.Collection;
/*   8:    */ import java.util.HashSet;
/*   9:    */ import java.util.Iterator;
/*  10:    */ import java.util.Set;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.util.ClassUtils;
/*  13:    */ 
/*  14:    */ public abstract class Conventions
/*  15:    */ {
/*  16:    */   private static final String PLURAL_SUFFIX = "List";
/*  17: 51 */   private static final Set<Class> ignoredInterfaces = new HashSet();
/*  18:    */   
/*  19:    */   static
/*  20:    */   {
/*  21: 54 */     ignoredInterfaces.add(Serializable.class);
/*  22: 55 */     ignoredInterfaces.add(Externalizable.class);
/*  23: 56 */     ignoredInterfaces.add(Cloneable.class);
/*  24: 57 */     ignoredInterfaces.add(Comparable.class);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public static String getVariableName(Object value)
/*  28:    */   {
/*  29: 77 */     Assert.notNull(value, "Value must not be null");
/*  30:    */     
/*  31: 79 */     boolean pluralize = false;
/*  32:    */     Class valueClass;
/*  33: 81 */     if (value.getClass().isArray())
/*  34:    */     {
/*  35: 82 */       Class valueClass = value.getClass().getComponentType();
/*  36: 83 */       pluralize = true;
/*  37:    */     }
/*  38: 85 */     else if ((value instanceof Collection))
/*  39:    */     {
/*  40: 86 */       Collection collection = (Collection)value;
/*  41: 87 */       if (collection.isEmpty()) {
/*  42: 88 */         throw new IllegalArgumentException("Cannot generate variable name for an empty Collection");
/*  43:    */       }
/*  44: 90 */       Object valueToCheck = peekAhead(collection);
/*  45: 91 */       Class valueClass = getClassForValue(valueToCheck);
/*  46: 92 */       pluralize = true;
/*  47:    */     }
/*  48:    */     else
/*  49:    */     {
/*  50: 95 */       valueClass = getClassForValue(value);
/*  51:    */     }
/*  52: 98 */     String name = ClassUtils.getShortNameAsProperty(valueClass);
/*  53: 99 */     return pluralize ? pluralize(name) : name;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public static String getVariableNameForParameter(MethodParameter parameter)
/*  57:    */   {
/*  58:109 */     Assert.notNull(parameter, "MethodParameter must not be null");
/*  59:    */     
/*  60:111 */     boolean pluralize = false;
/*  61:    */     Class valueClass;
/*  62:113 */     if (parameter.getParameterType().isArray())
/*  63:    */     {
/*  64:114 */       Class valueClass = parameter.getParameterType().getComponentType();
/*  65:115 */       pluralize = true;
/*  66:    */     }
/*  67:117 */     else if (Collection.class.isAssignableFrom(parameter.getParameterType()))
/*  68:    */     {
/*  69:118 */       Class valueClass = GenericCollectionTypeResolver.getCollectionParameterType(parameter);
/*  70:119 */       if (valueClass == null) {
/*  71:120 */         throw new IllegalArgumentException(
/*  72:121 */           "Cannot generate variable name for non-typed Collection parameter type");
/*  73:    */       }
/*  74:123 */       pluralize = true;
/*  75:    */     }
/*  76:    */     else
/*  77:    */     {
/*  78:126 */       valueClass = parameter.getParameterType();
/*  79:    */     }
/*  80:129 */     String name = ClassUtils.getShortNameAsProperty(valueClass);
/*  81:130 */     return pluralize ? pluralize(name) : name;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public static String getVariableNameForReturnType(Method method)
/*  85:    */   {
/*  86:140 */     return getVariableNameForReturnType(method, method.getReturnType(), null);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public static String getVariableNameForReturnType(Method method, Object value)
/*  90:    */   {
/*  91:153 */     return getVariableNameForReturnType(method, method.getReturnType(), value);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public static String getVariableNameForReturnType(Method method, Class resolvedType, Object value)
/*  95:    */   {
/*  96:167 */     Assert.notNull(method, "Method must not be null");
/*  97:169 */     if (Object.class.equals(resolvedType))
/*  98:    */     {
/*  99:170 */       if (value == null) {
/* 100:171 */         throw new IllegalArgumentException("Cannot generate variable name for an Object return type with null value");
/* 101:    */       }
/* 102:173 */       return getVariableName(value);
/* 103:    */     }
/* 104:177 */     boolean pluralize = false;
/* 105:    */     Class valueClass;
/* 106:179 */     if (resolvedType.isArray())
/* 107:    */     {
/* 108:180 */       Class valueClass = resolvedType.getComponentType();
/* 109:181 */       pluralize = true;
/* 110:    */     }
/* 111:183 */     else if (Collection.class.isAssignableFrom(resolvedType))
/* 112:    */     {
/* 113:184 */       Class valueClass = GenericCollectionTypeResolver.getCollectionReturnType(method);
/* 114:185 */       if (valueClass == null)
/* 115:    */       {
/* 116:186 */         if (!(value instanceof Collection)) {
/* 117:187 */           throw new IllegalArgumentException(
/* 118:188 */             "Cannot generate variable name for non-typed Collection return type and a non-Collection value");
/* 119:    */         }
/* 120:190 */         Collection collection = (Collection)value;
/* 121:191 */         if (collection.isEmpty()) {
/* 122:192 */           throw new IllegalArgumentException(
/* 123:193 */             "Cannot generate variable name for non-typed Collection return type and an empty Collection value");
/* 124:    */         }
/* 125:195 */         Object valueToCheck = peekAhead(collection);
/* 126:196 */         valueClass = getClassForValue(valueToCheck);
/* 127:    */       }
/* 128:198 */       pluralize = true;
/* 129:    */     }
/* 130:    */     else
/* 131:    */     {
/* 132:201 */       valueClass = resolvedType;
/* 133:    */     }
/* 134:204 */     String name = ClassUtils.getShortNameAsProperty(valueClass);
/* 135:205 */     return pluralize ? pluralize(name) : name;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public static String attributeNameToPropertyName(String attributeName)
/* 139:    */   {
/* 140:214 */     Assert.notNull(attributeName, "'attributeName' must not be null");
/* 141:215 */     if (!attributeName.contains("-")) {
/* 142:216 */       return attributeName;
/* 143:    */     }
/* 144:218 */     char[] chars = attributeName.toCharArray();
/* 145:219 */     char[] result = new char[chars.length - 1];
/* 146:220 */     int currPos = 0;
/* 147:221 */     boolean upperCaseNext = false;
/* 148:222 */     for (char c : chars) {
/* 149:223 */       if (c == '-')
/* 150:    */       {
/* 151:224 */         upperCaseNext = true;
/* 152:    */       }
/* 153:226 */       else if (upperCaseNext)
/* 154:    */       {
/* 155:227 */         result[(currPos++)] = Character.toUpperCase(c);
/* 156:228 */         upperCaseNext = false;
/* 157:    */       }
/* 158:    */       else
/* 159:    */       {
/* 160:231 */         result[(currPos++)] = c;
/* 161:    */       }
/* 162:    */     }
/* 163:234 */     return new String(result, 0, currPos);
/* 164:    */   }
/* 165:    */   
/* 166:    */   public static String getQualifiedAttributeName(Class enclosingClass, String attributeName)
/* 167:    */   {
/* 168:243 */     Assert.notNull(enclosingClass, "'enclosingClass' must not be null");
/* 169:244 */     Assert.notNull(attributeName, "'attributeName' must not be null");
/* 170:245 */     return enclosingClass.getName() + "." + attributeName;
/* 171:    */   }
/* 172:    */   
/* 173:    */   private static Class getClassForValue(Object value)
/* 174:    */   {
/* 175:259 */     Class valueClass = value.getClass();
/* 176:260 */     if (Proxy.isProxyClass(valueClass))
/* 177:    */     {
/* 178:261 */       Class[] ifcs = valueClass.getInterfaces();
/* 179:262 */       for (Class ifc : ifcs) {
/* 180:263 */         if (!ignoredInterfaces.contains(ifc)) {
/* 181:264 */           return ifc;
/* 182:    */         }
/* 183:    */       }
/* 184:    */     }
/* 185:268 */     else if ((valueClass.getName().lastIndexOf('$') != -1) && (valueClass.getDeclaringClass() == null))
/* 186:    */     {
/* 187:271 */       valueClass = valueClass.getSuperclass();
/* 188:    */     }
/* 189:273 */     return valueClass;
/* 190:    */   }
/* 191:    */   
/* 192:    */   private static String pluralize(String name)
/* 193:    */   {
/* 194:280 */     return name + "List";
/* 195:    */   }
/* 196:    */   
/* 197:    */   private static Object peekAhead(Collection collection)
/* 198:    */   {
/* 199:289 */     Iterator it = collection.iterator();
/* 200:290 */     if (!it.hasNext()) {
/* 201:291 */       throw new IllegalStateException(
/* 202:292 */         "Unable to peek ahead in non-empty collection - no element found");
/* 203:    */     }
/* 204:294 */     Object value = it.next();
/* 205:295 */     if (value == null) {
/* 206:296 */       throw new IllegalStateException(
/* 207:297 */         "Unable to peek ahead in non-empty collection - only null element found");
/* 208:    */     }
/* 209:299 */     return value;
/* 210:    */   }
/* 211:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.Conventions
 * JD-Core Version:    0.7.0.1
 */