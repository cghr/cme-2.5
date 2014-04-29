/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.ObjectInputStream;
/*   5:    */ import java.io.Serializable;
/*   6:    */ import java.lang.annotation.Annotation;
/*   7:    */ import java.lang.reflect.Constructor;
/*   8:    */ import java.lang.reflect.Field;
/*   9:    */ import java.lang.reflect.Method;
/*  10:    */ import java.lang.reflect.Type;
/*  11:    */ import org.springframework.core.GenericCollectionTypeResolver;
/*  12:    */ import org.springframework.core.MethodParameter;
/*  13:    */ import org.springframework.core.ParameterNameDiscoverer;
/*  14:    */ import org.springframework.util.Assert;
/*  15:    */ 
/*  16:    */ public class DependencyDescriptor
/*  17:    */   implements Serializable
/*  18:    */ {
/*  19:    */   private transient MethodParameter methodParameter;
/*  20:    */   private transient Field field;
/*  21:    */   private Class declaringClass;
/*  22:    */   private String methodName;
/*  23:    */   private Class[] parameterTypes;
/*  24:    */   private int parameterIndex;
/*  25:    */   private String fieldName;
/*  26:    */   private final boolean required;
/*  27:    */   private final boolean eager;
/*  28:    */   private transient Annotation[] fieldAnnotations;
/*  29:    */   
/*  30:    */   public DependencyDescriptor(MethodParameter methodParameter, boolean required)
/*  31:    */   {
/*  32: 69 */     this(methodParameter, required, true);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public DependencyDescriptor(MethodParameter methodParameter, boolean required, boolean eager)
/*  36:    */   {
/*  37: 80 */     Assert.notNull(methodParameter, "MethodParameter must not be null");
/*  38: 81 */     this.methodParameter = methodParameter;
/*  39: 82 */     this.declaringClass = methodParameter.getDeclaringClass();
/*  40: 83 */     if (this.methodParameter.getMethod() != null)
/*  41:    */     {
/*  42: 84 */       this.methodName = methodParameter.getMethod().getName();
/*  43: 85 */       this.parameterTypes = methodParameter.getMethod().getParameterTypes();
/*  44:    */     }
/*  45:    */     else
/*  46:    */     {
/*  47: 88 */       this.parameterTypes = methodParameter.getConstructor().getParameterTypes();
/*  48:    */     }
/*  49: 90 */     this.parameterIndex = methodParameter.getParameterIndex();
/*  50: 91 */     this.required = required;
/*  51: 92 */     this.eager = eager;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public DependencyDescriptor(Field field, boolean required)
/*  55:    */   {
/*  56:102 */     this(field, required, true);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public DependencyDescriptor(Field field, boolean required, boolean eager)
/*  60:    */   {
/*  61:113 */     Assert.notNull(field, "Field must not be null");
/*  62:114 */     this.field = field;
/*  63:115 */     this.declaringClass = field.getDeclaringClass();
/*  64:116 */     this.fieldName = field.getName();
/*  65:117 */     this.required = required;
/*  66:118 */     this.eager = eager;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public MethodParameter getMethodParameter()
/*  70:    */   {
/*  71:128 */     return this.methodParameter;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public Field getField()
/*  75:    */   {
/*  76:137 */     return this.field;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public boolean isRequired()
/*  80:    */   {
/*  81:144 */     return this.required;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public boolean isEager()
/*  85:    */   {
/*  86:152 */     return this.eager;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void initParameterNameDiscovery(ParameterNameDiscoverer parameterNameDiscoverer)
/*  90:    */   {
/*  91:163 */     if (this.methodParameter != null) {
/*  92:164 */       this.methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   public String getDependencyName()
/*  97:    */   {
/*  98:173 */     return this.field != null ? this.field.getName() : this.methodParameter.getParameterName();
/*  99:    */   }
/* 100:    */   
/* 101:    */   public Class<?> getDependencyType()
/* 102:    */   {
/* 103:181 */     return this.field != null ? this.field.getType() : this.methodParameter.getParameterType();
/* 104:    */   }
/* 105:    */   
/* 106:    */   public Type getGenericDependencyType()
/* 107:    */   {
/* 108:189 */     return this.field != null ? this.field.getGenericType() : this.methodParameter.getGenericParameterType();
/* 109:    */   }
/* 110:    */   
/* 111:    */   public Class<?> getCollectionType()
/* 112:    */   {
/* 113:197 */     return this.field != null ? 
/* 114:198 */       GenericCollectionTypeResolver.getCollectionFieldType(this.field) : 
/* 115:199 */       GenericCollectionTypeResolver.getCollectionParameterType(this.methodParameter);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public Class<?> getMapKeyType()
/* 119:    */   {
/* 120:207 */     return this.field != null ? 
/* 121:208 */       GenericCollectionTypeResolver.getMapKeyFieldType(this.field) : 
/* 122:209 */       GenericCollectionTypeResolver.getMapKeyParameterType(this.methodParameter);
/* 123:    */   }
/* 124:    */   
/* 125:    */   public Class<?> getMapValueType()
/* 126:    */   {
/* 127:217 */     return this.field != null ? 
/* 128:218 */       GenericCollectionTypeResolver.getMapValueFieldType(this.field) : 
/* 129:219 */       GenericCollectionTypeResolver.getMapValueParameterType(this.methodParameter);
/* 130:    */   }
/* 131:    */   
/* 132:    */   public Annotation[] getAnnotations()
/* 133:    */   {
/* 134:226 */     if (this.field != null)
/* 135:    */     {
/* 136:227 */       if (this.fieldAnnotations == null) {
/* 137:228 */         this.fieldAnnotations = this.field.getAnnotations();
/* 138:    */       }
/* 139:230 */       return this.fieldAnnotations;
/* 140:    */     }
/* 141:233 */     return this.methodParameter.getParameterAnnotations();
/* 142:    */   }
/* 143:    */   
/* 144:    */   private void readObject(ObjectInputStream ois)
/* 145:    */     throws IOException, ClassNotFoundException
/* 146:    */   {
/* 147:244 */     ois.defaultReadObject();
/* 148:    */     try
/* 149:    */     {
/* 150:248 */       if (this.fieldName != null) {
/* 151:249 */         this.field = this.declaringClass.getDeclaredField(this.fieldName);
/* 152:251 */       } else if (this.methodName != null) {
/* 153:252 */         this.methodParameter = new MethodParameter(
/* 154:253 */           this.declaringClass.getDeclaredMethod(this.methodName, this.parameterTypes), this.parameterIndex);
/* 155:    */       } else {
/* 156:256 */         this.methodParameter = new MethodParameter(
/* 157:257 */           this.declaringClass.getDeclaredConstructor(this.parameterTypes), this.parameterIndex);
/* 158:    */       }
/* 159:    */     }
/* 160:    */     catch (Throwable ex)
/* 161:    */     {
/* 162:261 */       throw new IllegalStateException("Could not find original class structure", ex);
/* 163:    */     }
/* 164:    */   }
/* 165:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.DependencyDescriptor
 * JD-Core Version:    0.7.0.1
 */