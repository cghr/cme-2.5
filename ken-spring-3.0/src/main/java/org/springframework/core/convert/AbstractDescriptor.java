/*   1:    */ package org.springframework.core.convert;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.Map;
/*   6:    */ 
/*   7:    */ abstract class AbstractDescriptor
/*   8:    */ {
/*   9:    */   private final Class<?> type;
/*  10:    */   
/*  11:    */   protected AbstractDescriptor(Class<?> type)
/*  12:    */   {
/*  13: 32 */     if (type == null) {
/*  14: 33 */       throw new IllegalArgumentException("type cannot be null");
/*  15:    */     }
/*  16: 35 */     this.type = type;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public Class<?> getType()
/*  20:    */   {
/*  21: 39 */     return this.type;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public TypeDescriptor getElementTypeDescriptor()
/*  25:    */   {
/*  26: 43 */     if (isCollection())
/*  27:    */     {
/*  28: 44 */       Class<?> elementType = resolveCollectionElementType();
/*  29: 45 */       return elementType != null ? new TypeDescriptor(nested(elementType, 0)) : null;
/*  30:    */     }
/*  31: 47 */     if (isArray())
/*  32:    */     {
/*  33: 48 */       Class<?> elementType = getType().getComponentType();
/*  34: 49 */       return new TypeDescriptor(nested(elementType, 0));
/*  35:    */     }
/*  36: 52 */     return null;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public TypeDescriptor getMapKeyTypeDescriptor()
/*  40:    */   {
/*  41: 57 */     if (isMap())
/*  42:    */     {
/*  43: 58 */       Class<?> keyType = resolveMapKeyType();
/*  44: 59 */       return keyType != null ? new TypeDescriptor(nested(keyType, 0)) : null;
/*  45:    */     }
/*  46: 62 */     return null;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public TypeDescriptor getMapValueTypeDescriptor()
/*  50:    */   {
/*  51: 67 */     if (isMap())
/*  52:    */     {
/*  53: 68 */       Class<?> valueType = resolveMapValueType();
/*  54: 69 */       return valueType != null ? new TypeDescriptor(nested(valueType, 1)) : null;
/*  55:    */     }
/*  56: 72 */     return null;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public abstract Annotation[] getAnnotations();
/*  60:    */   
/*  61:    */   public AbstractDescriptor nested()
/*  62:    */   {
/*  63: 79 */     if (isCollection())
/*  64:    */     {
/*  65: 80 */       Class<?> elementType = resolveCollectionElementType();
/*  66: 81 */       return elementType != null ? nested(elementType, 0) : null;
/*  67:    */     }
/*  68: 83 */     if (isArray()) {
/*  69: 84 */       return nested(getType().getComponentType(), 0);
/*  70:    */     }
/*  71: 86 */     if (isMap())
/*  72:    */     {
/*  73: 87 */       Class<?> mapValueType = resolveMapValueType();
/*  74: 88 */       return mapValueType != null ? nested(mapValueType, 1) : null;
/*  75:    */     }
/*  76: 91 */     throw new IllegalStateException("Not a collection, array, or map: cannot resolve nested value types");
/*  77:    */   }
/*  78:    */   
/*  79:    */   protected abstract Class<?> resolveCollectionElementType();
/*  80:    */   
/*  81:    */   protected abstract Class<?> resolveMapKeyType();
/*  82:    */   
/*  83:    */   protected abstract Class<?> resolveMapValueType();
/*  84:    */   
/*  85:    */   protected abstract AbstractDescriptor nested(Class<?> paramClass, int paramInt);
/*  86:    */   
/*  87:    */   private boolean isCollection()
/*  88:    */   {
/*  89:108 */     return Collection.class.isAssignableFrom(getType());
/*  90:    */   }
/*  91:    */   
/*  92:    */   private boolean isArray()
/*  93:    */   {
/*  94:112 */     return getType().isArray();
/*  95:    */   }
/*  96:    */   
/*  97:    */   private boolean isMap()
/*  98:    */   {
/*  99:116 */     return Map.class.isAssignableFrom(getType());
/* 100:    */   }
/* 101:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.AbstractDescriptor
 * JD-Core Version:    0.7.0.1
 */