/*   1:    */ package org.springframework.core.type.classreading;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.lang.reflect.Array;
/*   5:    */ import java.lang.reflect.Field;
/*   6:    */ import java.lang.reflect.Method;
/*   7:    */ import java.util.LinkedHashMap;
/*   8:    */ import java.util.LinkedHashSet;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.Set;
/*  11:    */ import org.springframework.asm.AnnotationVisitor;
/*  12:    */ import org.springframework.asm.Type;
/*  13:    */ import org.springframework.asm.commons.EmptyVisitor;
/*  14:    */ import org.springframework.core.annotation.AnnotationUtils;
/*  15:    */ import org.springframework.util.ObjectUtils;
/*  16:    */ import org.springframework.util.ReflectionUtils;
/*  17:    */ 
/*  18:    */ final class AnnotationAttributesReadingVisitor
/*  19:    */   implements AnnotationVisitor
/*  20:    */ {
/*  21:    */   private final String annotationType;
/*  22:    */   private final Map<String, Map<String, Object>> attributesMap;
/*  23:    */   private final Map<String, Set<String>> metaAnnotationMap;
/*  24:    */   private final ClassLoader classLoader;
/*  25: 51 */   private final Map<String, Object> localAttributes = new LinkedHashMap();
/*  26:    */   
/*  27:    */   public AnnotationAttributesReadingVisitor(String annotationType, Map<String, Map<String, Object>> attributesMap, Map<String, Set<String>> metaAnnotationMap, ClassLoader classLoader)
/*  28:    */   {
/*  29: 58 */     this.annotationType = annotationType;
/*  30: 59 */     this.attributesMap = attributesMap;
/*  31: 60 */     this.metaAnnotationMap = metaAnnotationMap;
/*  32: 61 */     this.classLoader = classLoader;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void visit(String name, Object value)
/*  36:    */   {
/*  37: 66 */     this.localAttributes.put(name, value);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void visitEnum(String name, String desc, String value)
/*  41:    */   {
/*  42: 70 */     Object valueToUse = value;
/*  43:    */     try
/*  44:    */     {
/*  45: 72 */       Class<?> enumType = this.classLoader.loadClass(Type.getType(desc).getClassName());
/*  46: 73 */       Field enumConstant = ReflectionUtils.findField(enumType, value);
/*  47: 74 */       if (enumConstant != null) {
/*  48: 75 */         valueToUse = enumConstant.get(null);
/*  49:    */       }
/*  50:    */     }
/*  51:    */     catch (Exception localException) {}
/*  52: 81 */     this.localAttributes.put(name, valueToUse);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public AnnotationVisitor visitAnnotation(String name, String desc)
/*  56:    */   {
/*  57: 85 */     return new EmptyVisitor();
/*  58:    */   }
/*  59:    */   
/*  60:    */   public AnnotationVisitor visitArray(final String attrName)
/*  61:    */   {
/*  62: 89 */     new AnnotationVisitor()
/*  63:    */     {
/*  64:    */       public void visit(String name, Object value)
/*  65:    */       {
/*  66: 91 */         Object newValue = value;
/*  67: 92 */         Object existingValue = AnnotationAttributesReadingVisitor.this.localAttributes.get(attrName);
/*  68: 93 */         if (existingValue != null)
/*  69:    */         {
/*  70: 94 */           newValue = ObjectUtils.addObjectToArray((Object[])existingValue, newValue);
/*  71:    */         }
/*  72:    */         else
/*  73:    */         {
/*  74: 97 */           Object[] newArray = (Object[])Array.newInstance(newValue.getClass(), 1);
/*  75: 98 */           newArray[0] = newValue;
/*  76: 99 */           newValue = newArray;
/*  77:    */         }
/*  78:101 */         AnnotationAttributesReadingVisitor.this.localAttributes.put(attrName, newValue);
/*  79:    */       }
/*  80:    */       
/*  81:    */       public void visitEnum(String name, String desc, String value) {}
/*  82:    */       
/*  83:    */       public AnnotationVisitor visitAnnotation(String name, String desc)
/*  84:    */       {
/*  85:106 */         return new EmptyVisitor();
/*  86:    */       }
/*  87:    */       
/*  88:    */       public AnnotationVisitor visitArray(String name)
/*  89:    */       {
/*  90:109 */         return new EmptyVisitor();
/*  91:    */       }
/*  92:    */       
/*  93:    */       public void visitEnd() {}
/*  94:    */     };
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void visitEnd()
/*  98:    */   {
/*  99:117 */     this.attributesMap.put(this.annotationType, this.localAttributes);
/* 100:    */     try
/* 101:    */     {
/* 102:119 */       Class<?> annotationClass = this.classLoader.loadClass(this.annotationType);
/* 103:    */       
/* 104:121 */       Method[] annotationAttributes = annotationClass.getMethods();
/* 105:    */       String attributeName;
/* 106:122 */       for (Method annotationAttribute : annotationAttributes)
/* 107:    */       {
/* 108:123 */         attributeName = annotationAttribute.getName();
/* 109:124 */         Object defaultValue = annotationAttribute.getDefaultValue();
/* 110:125 */         if ((defaultValue != null) && (!this.localAttributes.containsKey(attributeName))) {
/* 111:126 */           this.localAttributes.put(attributeName, defaultValue);
/* 112:    */         }
/* 113:    */       }
/* 114:130 */       Set<String> metaAnnotationTypeNames = new LinkedHashSet();
/* 115:131 */       for (Annotation metaAnnotation : annotationClass.getAnnotations())
/* 116:    */       {
/* 117:132 */         metaAnnotationTypeNames.add(metaAnnotation.annotationType().getName());
/* 118:133 */         if (!this.attributesMap.containsKey(metaAnnotation.annotationType().getName())) {
/* 119:134 */           this.attributesMap.put(metaAnnotation.annotationType().getName(), 
/* 120:135 */             AnnotationUtils.getAnnotationAttributes(metaAnnotation, true));
/* 121:    */         }
/* 122:137 */         for (Annotation metaMetaAnnotation : metaAnnotation.annotationType().getAnnotations()) {
/* 123:138 */           metaAnnotationTypeNames.add(metaMetaAnnotation.annotationType().getName());
/* 124:    */         }
/* 125:    */       }
/* 126:141 */       if (this.metaAnnotationMap != null) {
/* 127:142 */         this.metaAnnotationMap.put(this.annotationType, metaAnnotationTypeNames);
/* 128:    */       }
/* 129:    */     }
/* 130:    */     catch (ClassNotFoundException localClassNotFoundException) {}
/* 131:    */   }
/* 132:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.classreading.AnnotationAttributesReadingVisitor
 * JD-Core Version:    0.7.0.1
 */