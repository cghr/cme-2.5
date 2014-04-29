/*   1:    */ package org.springframework.core.convert;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.lang.reflect.Field;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.util.Collection;
/*   7:    */ import java.util.LinkedHashMap;
/*   8:    */ import java.util.Map;
/*   9:    */ import org.springframework.core.GenericTypeResolver;
/*  10:    */ import org.springframework.core.MethodParameter;
/*  11:    */ import org.springframework.util.ReflectionUtils;
/*  12:    */ import org.springframework.util.StringUtils;
/*  13:    */ 
/*  14:    */ public final class Property
/*  15:    */ {
/*  16:    */   private final Class<?> objectType;
/*  17:    */   private final Method readMethod;
/*  18:    */   private final Method writeMethod;
/*  19:    */   private final String name;
/*  20:    */   private final MethodParameter methodParameter;
/*  21:    */   private final Annotation[] annotations;
/*  22:    */   
/*  23:    */   public Property(Class<?> objectType, Method readMethod, Method writeMethod)
/*  24:    */   {
/*  25: 53 */     this.objectType = objectType;
/*  26: 54 */     this.readMethod = readMethod;
/*  27: 55 */     this.writeMethod = writeMethod;
/*  28: 56 */     this.methodParameter = resolveMethodParameter();
/*  29: 57 */     this.name = resolveName();
/*  30: 58 */     this.annotations = resolveAnnotations();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public Class<?> getObjectType()
/*  34:    */   {
/*  35: 65 */     return this.objectType;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public String getName()
/*  39:    */   {
/*  40: 72 */     return this.name;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public Class<?> getType()
/*  44:    */   {
/*  45: 79 */     return this.methodParameter.getParameterType();
/*  46:    */   }
/*  47:    */   
/*  48:    */   public Method getReadMethod()
/*  49:    */   {
/*  50: 86 */     return this.readMethod;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public Method getWriteMethod()
/*  54:    */   {
/*  55: 93 */     return this.writeMethod;
/*  56:    */   }
/*  57:    */   
/*  58:    */   MethodParameter getMethodParameter()
/*  59:    */   {
/*  60: 99 */     return this.methodParameter;
/*  61:    */   }
/*  62:    */   
/*  63:    */   Annotation[] getAnnotations()
/*  64:    */   {
/*  65:103 */     return this.annotations;
/*  66:    */   }
/*  67:    */   
/*  68:    */   private String resolveName()
/*  69:    */   {
/*  70:109 */     if (this.readMethod != null)
/*  71:    */     {
/*  72:110 */       int index = this.readMethod.getName().indexOf("get");
/*  73:111 */       if (index != -1)
/*  74:    */       {
/*  75:112 */         index += 3;
/*  76:    */       }
/*  77:    */       else
/*  78:    */       {
/*  79:114 */         index = this.readMethod.getName().indexOf("is");
/*  80:115 */         if (index == -1) {
/*  81:116 */           throw new IllegalArgumentException("Not a getter method");
/*  82:    */         }
/*  83:118 */         index += 2;
/*  84:    */       }
/*  85:120 */       return StringUtils.uncapitalize(this.readMethod.getName().substring(index));
/*  86:    */     }
/*  87:122 */     int index = this.writeMethod.getName().indexOf("set") + 3;
/*  88:123 */     if (index == -1) {
/*  89:124 */       throw new IllegalArgumentException("Not a setter method");
/*  90:    */     }
/*  91:126 */     return StringUtils.uncapitalize(this.writeMethod.getName().substring(index));
/*  92:    */   }
/*  93:    */   
/*  94:    */   private MethodParameter resolveMethodParameter()
/*  95:    */   {
/*  96:131 */     MethodParameter read = resolveReadMethodParameter();
/*  97:132 */     MethodParameter write = resolveWriteMethodParameter();
/*  98:133 */     if ((read == null) && (write == null)) {
/*  99:134 */       throw new IllegalStateException("Property is neither readable nor writeable");
/* 100:    */     }
/* 101:136 */     if ((read != null) && (write != null) && (!write.getParameterType().isAssignableFrom(read.getParameterType()))) {
/* 102:137 */       throw new IllegalStateException("Write parameter is not assignable from read parameter");
/* 103:    */     }
/* 104:139 */     return read != null ? read : write;
/* 105:    */   }
/* 106:    */   
/* 107:    */   private MethodParameter resolveReadMethodParameter()
/* 108:    */   {
/* 109:143 */     if (getReadMethod() == null) {
/* 110:144 */       return null;
/* 111:    */     }
/* 112:146 */     return resolveParameterType(new MethodParameter(getReadMethod(), -1));
/* 113:    */   }
/* 114:    */   
/* 115:    */   private MethodParameter resolveWriteMethodParameter()
/* 116:    */   {
/* 117:150 */     if (getWriteMethod() == null) {
/* 118:151 */       return null;
/* 119:    */     }
/* 120:153 */     return resolveParameterType(new MethodParameter(getWriteMethod(), 0));
/* 121:    */   }
/* 122:    */   
/* 123:    */   private MethodParameter resolveParameterType(MethodParameter parameter)
/* 124:    */   {
/* 125:158 */     GenericTypeResolver.resolveParameterType(parameter, getObjectType());
/* 126:159 */     return parameter;
/* 127:    */   }
/* 128:    */   
/* 129:    */   private Annotation[] resolveAnnotations()
/* 130:    */   {
/* 131:163 */     Map<Class<?>, Annotation> annMap = new LinkedHashMap();
/* 132:164 */     Method readMethod = getReadMethod();
/* 133:165 */     if (readMethod != null) {
/* 134:166 */       for (Annotation ann : readMethod.getAnnotations()) {
/* 135:167 */         annMap.put(ann.annotationType(), ann);
/* 136:    */       }
/* 137:    */     }
/* 138:170 */     Method writeMethod = getWriteMethod();
/* 139:171 */     if (writeMethod != null) {
/* 140:172 */       for (Annotation ann : writeMethod.getAnnotations()) {
/* 141:173 */         annMap.put(ann.annotationType(), ann);
/* 142:    */       }
/* 143:    */     }
/* 144:176 */     Field field = getField();
/* 145:177 */     if (field != null) {
/* 146:178 */       for (Annotation ann : field.getAnnotations()) {
/* 147:179 */         annMap.put(ann.annotationType(), ann);
/* 148:    */       }
/* 149:    */     }
/* 150:182 */     return (Annotation[])annMap.values().toArray(new Annotation[annMap.size()]);
/* 151:    */   }
/* 152:    */   
/* 153:    */   private Field getField()
/* 154:    */   {
/* 155:186 */     String name = getName();
/* 156:187 */     if (!StringUtils.hasLength(name)) {
/* 157:188 */       return null;
/* 158:    */     }
/* 159:190 */     Class<?> declaringClass = declaringClass();
/* 160:191 */     Field field = ReflectionUtils.findField(declaringClass, name);
/* 161:192 */     if (field == null)
/* 162:    */     {
/* 163:194 */       field = ReflectionUtils.findField(declaringClass, 
/* 164:195 */         name.substring(0, 1).toLowerCase() + name.substring(1));
/* 165:196 */       if (field == null) {
/* 166:197 */         field = ReflectionUtils.findField(declaringClass, 
/* 167:198 */           name.substring(0, 1).toUpperCase() + name.substring(1));
/* 168:    */       }
/* 169:    */     }
/* 170:201 */     return field;
/* 171:    */   }
/* 172:    */   
/* 173:    */   private Class<?> declaringClass()
/* 174:    */   {
/* 175:205 */     if (getReadMethod() != null) {
/* 176:206 */       return getReadMethod().getDeclaringClass();
/* 177:    */     }
/* 178:208 */     return getWriteMethod().getDeclaringClass();
/* 179:    */   }
/* 180:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.Property
 * JD-Core Version:    0.7.0.1
 */