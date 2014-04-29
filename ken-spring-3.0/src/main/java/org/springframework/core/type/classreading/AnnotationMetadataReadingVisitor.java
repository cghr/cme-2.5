/*   1:    */ package org.springframework.core.type.classreading;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.LinkedHashMap;
/*   5:    */ import java.util.LinkedHashSet;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Map.Entry;
/*   9:    */ import java.util.Set;
/*  10:    */ import org.springframework.asm.AnnotationVisitor;
/*  11:    */ import org.springframework.asm.MethodVisitor;
/*  12:    */ import org.springframework.asm.Type;
/*  13:    */ import org.springframework.core.type.AnnotationMetadata;
/*  14:    */ import org.springframework.core.type.MethodMetadata;
/*  15:    */ import org.springframework.util.CollectionUtils;
/*  16:    */ import org.springframework.util.LinkedMultiValueMap;
/*  17:    */ import org.springframework.util.MultiValueMap;
/*  18:    */ 
/*  19:    */ final class AnnotationMetadataReadingVisitor
/*  20:    */   extends ClassMetadataReadingVisitor
/*  21:    */   implements AnnotationMetadata
/*  22:    */ {
/*  23:    */   private final ClassLoader classLoader;
/*  24: 49 */   private final Set<String> annotationSet = new LinkedHashSet();
/*  25: 51 */   private final Map<String, Set<String>> metaAnnotationMap = new LinkedHashMap(4);
/*  26: 53 */   private final Map<String, Map<String, Object>> attributeMap = new LinkedHashMap(4);
/*  27: 55 */   private final MultiValueMap<String, MethodMetadata> methodMetadataMap = new LinkedMultiValueMap();
/*  28:    */   
/*  29:    */   public AnnotationMetadataReadingVisitor(ClassLoader classLoader)
/*  30:    */   {
/*  31: 59 */     this.classLoader = classLoader;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
/*  35:    */   {
/*  36: 65 */     return new MethodMetadataReadingVisitor(name, access, getClassName(), this.classLoader, this.methodMetadataMap);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public AnnotationVisitor visitAnnotation(String desc, boolean visible)
/*  40:    */   {
/*  41: 70 */     String className = Type.getType(desc).getClassName();
/*  42: 71 */     this.annotationSet.add(className);
/*  43: 72 */     return new AnnotationAttributesReadingVisitor(className, this.attributeMap, this.metaAnnotationMap, this.classLoader);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public Set<String> getAnnotationTypes()
/*  47:    */   {
/*  48: 77 */     return this.annotationSet;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public Set<String> getMetaAnnotationTypes(String annotationType)
/*  52:    */   {
/*  53: 81 */     return (Set)this.metaAnnotationMap.get(annotationType);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public boolean hasAnnotation(String annotationType)
/*  57:    */   {
/*  58: 85 */     return this.annotationSet.contains(annotationType);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public boolean hasMetaAnnotation(String metaAnnotationType)
/*  62:    */   {
/*  63: 89 */     Collection<Set<String>> allMetaTypes = this.metaAnnotationMap.values();
/*  64: 90 */     for (Set<String> metaTypes : allMetaTypes) {
/*  65: 91 */       if (metaTypes.contains(metaAnnotationType)) {
/*  66: 92 */         return true;
/*  67:    */       }
/*  68:    */     }
/*  69: 95 */     return false;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public boolean isAnnotated(String annotationType)
/*  73:    */   {
/*  74: 99 */     return this.attributeMap.containsKey(annotationType);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public Map<String, Object> getAnnotationAttributes(String annotationType)
/*  78:    */   {
/*  79:103 */     return getAnnotationAttributes(annotationType, false);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public Map<String, Object> getAnnotationAttributes(String annotationType, boolean classValuesAsString)
/*  83:    */   {
/*  84:107 */     Map<String, Object> raw = (Map)this.attributeMap.get(annotationType);
/*  85:108 */     if (raw == null) {
/*  86:109 */       return null;
/*  87:    */     }
/*  88:111 */     Map<String, Object> result = new LinkedHashMap(raw.size());
/*  89:112 */     for (Map.Entry<String, Object> entry : raw.entrySet()) {
/*  90:    */       try
/*  91:    */       {
/*  92:114 */         Object value = entry.getValue();
/*  93:115 */         if ((value instanceof Type))
/*  94:    */         {
/*  95:116 */           value = classValuesAsString ? ((Type)value).getClassName() : 
/*  96:117 */             this.classLoader.loadClass(((Type)value).getClassName());
/*  97:    */         }
/*  98:119 */         else if ((value instanceof Type[]))
/*  99:    */         {
/* 100:120 */           Type[] array = (Type[])value;
/* 101:121 */           Object[] convArray = classValuesAsString ? new String[array.length] : new Class[array.length];
/* 102:122 */           for (int i = 0; i < array.length; i++) {
/* 103:123 */             convArray[i] = (classValuesAsString ? array[i].getClassName() : 
/* 104:124 */               this.classLoader.loadClass(array[i].getClassName()));
/* 105:    */           }
/* 106:126 */           value = convArray;
/* 107:    */         }
/* 108:128 */         else if (classValuesAsString)
/* 109:    */         {
/* 110:129 */           if ((value instanceof Class))
/* 111:    */           {
/* 112:130 */             value = ((Class)value).getName();
/* 113:    */           }
/* 114:132 */           else if ((value instanceof Class[]))
/* 115:    */           {
/* 116:133 */             Class[] clazzArray = (Class[])value;
/* 117:134 */             String[] newValue = new String[clazzArray.length];
/* 118:135 */             for (int i = 0; i < clazzArray.length; i++) {
/* 119:136 */               newValue[i] = clazzArray[i].getName();
/* 120:    */             }
/* 121:138 */             value = newValue;
/* 122:    */           }
/* 123:    */         }
/* 124:141 */         result.put((String)entry.getKey(), value);
/* 125:    */       }
/* 126:    */       catch (Exception localException) {}
/* 127:    */     }
/* 128:147 */     return result;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public boolean hasAnnotatedMethods(String annotationType)
/* 132:    */   {
/* 133:151 */     return this.methodMetadataMap.containsKey(annotationType);
/* 134:    */   }
/* 135:    */   
/* 136:    */   public Set<MethodMetadata> getAnnotatedMethods(String annotationType)
/* 137:    */   {
/* 138:155 */     List<MethodMetadata> list = (List)this.methodMetadataMap.get(annotationType);
/* 139:156 */     if (CollectionUtils.isEmpty(list)) {
/* 140:157 */       return new LinkedHashSet(0);
/* 141:    */     }
/* 142:159 */     Set<MethodMetadata> annotatedMethods = new LinkedHashSet(list.size());
/* 143:160 */     annotatedMethods.addAll(list);
/* 144:161 */     return annotatedMethods;
/* 145:    */   }
/* 146:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor
 * JD-Core Version:    0.7.0.1
 */