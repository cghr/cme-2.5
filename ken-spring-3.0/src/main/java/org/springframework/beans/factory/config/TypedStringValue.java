/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.BeanMetadataElement;
/*   4:    */ import org.springframework.util.Assert;
/*   5:    */ import org.springframework.util.ClassUtils;
/*   6:    */ import org.springframework.util.ObjectUtils;
/*   7:    */ 
/*   8:    */ public class TypedStringValue
/*   9:    */   implements BeanMetadataElement
/*  10:    */ {
/*  11:    */   private String value;
/*  12:    */   private volatile Object targetType;
/*  13:    */   private Object source;
/*  14:    */   private String specifiedTypeName;
/*  15:    */   private volatile boolean dynamic;
/*  16:    */   
/*  17:    */   public TypedStringValue(String value)
/*  18:    */   {
/*  19: 55 */     setValue(value);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public TypedStringValue(String value, Class targetType)
/*  23:    */   {
/*  24: 65 */     setValue(value);
/*  25: 66 */     setTargetType(targetType);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public TypedStringValue(String value, String targetTypeName)
/*  29:    */   {
/*  30: 76 */     setValue(value);
/*  31: 77 */     setTargetTypeName(targetTypeName);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setValue(String value)
/*  35:    */   {
/*  36: 88 */     this.value = value;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getValue()
/*  40:    */   {
/*  41: 95 */     return this.value;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setTargetType(Class targetType)
/*  45:    */   {
/*  46:105 */     Assert.notNull(targetType, "'targetType' must not be null");
/*  47:106 */     this.targetType = targetType;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public Class getTargetType()
/*  51:    */   {
/*  52:113 */     Object targetTypeValue = this.targetType;
/*  53:114 */     if (!(targetTypeValue instanceof Class)) {
/*  54:115 */       throw new IllegalStateException("Typed String value does not carry a resolved target type");
/*  55:    */     }
/*  56:117 */     return (Class)targetTypeValue;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void setTargetTypeName(String targetTypeName)
/*  60:    */   {
/*  61:124 */     Assert.notNull(targetTypeName, "'targetTypeName' must not be null");
/*  62:125 */     this.targetType = targetTypeName;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public String getTargetTypeName()
/*  66:    */   {
/*  67:132 */     Object targetTypeValue = this.targetType;
/*  68:133 */     if ((targetTypeValue instanceof Class)) {
/*  69:134 */       return ((Class)targetTypeValue).getName();
/*  70:    */     }
/*  71:137 */     return (String)targetTypeValue;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public boolean hasTargetType()
/*  75:    */   {
/*  76:145 */     return this.targetType instanceof Class;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public Class resolveTargetType(ClassLoader classLoader)
/*  80:    */     throws ClassNotFoundException
/*  81:    */   {
/*  82:157 */     if (this.targetType == null) {
/*  83:158 */       return null;
/*  84:    */     }
/*  85:160 */     Class resolvedClass = ClassUtils.forName(getTargetTypeName(), classLoader);
/*  86:161 */     this.targetType = resolvedClass;
/*  87:162 */     return resolvedClass;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setSource(Object source)
/*  91:    */   {
/*  92:171 */     this.source = source;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public Object getSource()
/*  96:    */   {
/*  97:175 */     return this.source;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setSpecifiedTypeName(String specifiedTypeName)
/* 101:    */   {
/* 102:182 */     this.specifiedTypeName = specifiedTypeName;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public String getSpecifiedTypeName()
/* 106:    */   {
/* 107:189 */     return this.specifiedTypeName;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setDynamic()
/* 111:    */   {
/* 112:197 */     this.dynamic = true;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public boolean isDynamic()
/* 116:    */   {
/* 117:204 */     return this.dynamic;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public boolean equals(Object other)
/* 121:    */   {
/* 122:210 */     if (this == other) {
/* 123:211 */       return true;
/* 124:    */     }
/* 125:213 */     if (!(other instanceof TypedStringValue)) {
/* 126:214 */       return false;
/* 127:    */     }
/* 128:216 */     TypedStringValue otherValue = (TypedStringValue)other;
/* 129:    */     
/* 130:218 */     return (ObjectUtils.nullSafeEquals(this.value, otherValue.value)) && (ObjectUtils.nullSafeEquals(this.targetType, otherValue.targetType));
/* 131:    */   }
/* 132:    */   
/* 133:    */   public int hashCode()
/* 134:    */   {
/* 135:223 */     return ObjectUtils.nullSafeHashCode(this.value) * 29 + ObjectUtils.nullSafeHashCode(this.targetType);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public String toString()
/* 139:    */   {
/* 140:228 */     return "TypedStringValue: value [" + this.value + "], target type [" + this.targetType + "]";
/* 141:    */   }
/* 142:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.TypedStringValue
 * JD-Core Version:    0.7.0.1
 */