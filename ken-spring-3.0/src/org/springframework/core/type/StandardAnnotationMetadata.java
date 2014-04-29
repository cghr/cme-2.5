/*   1:    */ package org.springframework.core.type;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.util.LinkedHashSet;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Set;
/*   8:    */ import org.springframework.core.annotation.AnnotationUtils;
/*   9:    */ 
/*  10:    */ public class StandardAnnotationMetadata
/*  11:    */   extends StandardClassMetadata
/*  12:    */   implements AnnotationMetadata
/*  13:    */ {
/*  14:    */   public StandardAnnotationMetadata(Class introspectedClass)
/*  15:    */   {
/*  16: 42 */     super(introspectedClass);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public Set<String> getAnnotationTypes()
/*  20:    */   {
/*  21: 47 */     Set<String> types = new LinkedHashSet();
/*  22: 48 */     Annotation[] anns = getIntrospectedClass().getAnnotations();
/*  23: 49 */     for (Annotation ann : anns) {
/*  24: 50 */       types.add(ann.annotationType().getName());
/*  25:    */     }
/*  26: 52 */     return types;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public Set<String> getMetaAnnotationTypes(String annotationType)
/*  30:    */   {
/*  31: 56 */     Annotation[] anns = getIntrospectedClass().getAnnotations();
/*  32: 57 */     for (Annotation ann : anns) {
/*  33: 58 */       if (ann.annotationType().getName().equals(annotationType))
/*  34:    */       {
/*  35: 59 */         Set<String> types = new LinkedHashSet();
/*  36: 60 */         Annotation[] metaAnns = ann.annotationType().getAnnotations();
/*  37: 61 */         for (Annotation metaAnn : metaAnns)
/*  38:    */         {
/*  39: 62 */           types.add(metaAnn.annotationType().getName());
/*  40: 63 */           for (Annotation metaMetaAnn : metaAnn.annotationType().getAnnotations()) {
/*  41: 64 */             types.add(metaMetaAnn.annotationType().getName());
/*  42:    */           }
/*  43:    */         }
/*  44: 67 */         return types;
/*  45:    */       }
/*  46:    */     }
/*  47: 70 */     return null;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public boolean hasAnnotation(String annotationType)
/*  51:    */   {
/*  52: 74 */     Annotation[] anns = getIntrospectedClass().getAnnotations();
/*  53: 75 */     for (Annotation ann : anns) {
/*  54: 76 */       if (ann.annotationType().getName().equals(annotationType)) {
/*  55: 77 */         return true;
/*  56:    */       }
/*  57:    */     }
/*  58: 80 */     return false;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public boolean hasMetaAnnotation(String annotationType)
/*  62:    */   {
/*  63: 84 */     Annotation[] anns = getIntrospectedClass().getAnnotations();
/*  64: 85 */     for (Annotation ann : anns)
/*  65:    */     {
/*  66: 86 */       Annotation[] metaAnns = ann.annotationType().getAnnotations();
/*  67: 87 */       for (Annotation metaAnn : metaAnns)
/*  68:    */       {
/*  69: 88 */         if (metaAnn.annotationType().getName().equals(annotationType)) {
/*  70: 89 */           return true;
/*  71:    */         }
/*  72: 91 */         for (Annotation metaMetaAnn : metaAnn.annotationType().getAnnotations()) {
/*  73: 92 */           if (metaMetaAnn.annotationType().getName().equals(annotationType)) {
/*  74: 93 */             return true;
/*  75:    */           }
/*  76:    */         }
/*  77:    */       }
/*  78:    */     }
/*  79: 98 */     return false;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public boolean isAnnotated(String annotationType)
/*  83:    */   {
/*  84:102 */     Annotation[] anns = getIntrospectedClass().getAnnotations();
/*  85:103 */     for (Annotation ann : anns)
/*  86:    */     {
/*  87:104 */       if (ann.annotationType().getName().equals(annotationType)) {
/*  88:105 */         return true;
/*  89:    */       }
/*  90:107 */       for (Annotation metaAnn : ann.annotationType().getAnnotations()) {
/*  91:108 */         if (metaAnn.annotationType().getName().equals(annotationType)) {
/*  92:109 */           return true;
/*  93:    */         }
/*  94:    */       }
/*  95:    */     }
/*  96:113 */     return false;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public Map<String, Object> getAnnotationAttributes(String annotationType)
/* 100:    */   {
/* 101:117 */     return getAnnotationAttributes(annotationType, false);
/* 102:    */   }
/* 103:    */   
/* 104:    */   public Map<String, Object> getAnnotationAttributes(String annotationType, boolean classValuesAsString)
/* 105:    */   {
/* 106:121 */     Annotation[] anns = getIntrospectedClass().getAnnotations();
/* 107:122 */     for (Annotation ann : anns)
/* 108:    */     {
/* 109:123 */       if (ann.annotationType().getName().equals(annotationType)) {
/* 110:124 */         return AnnotationUtils.getAnnotationAttributes(ann, classValuesAsString);
/* 111:    */       }
/* 112:126 */       for (Annotation metaAnn : ann.annotationType().getAnnotations()) {
/* 113:127 */         if (metaAnn.annotationType().getName().equals(annotationType)) {
/* 114:128 */           return AnnotationUtils.getAnnotationAttributes(metaAnn, classValuesAsString);
/* 115:    */         }
/* 116:    */       }
/* 117:    */     }
/* 118:132 */     return null;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public boolean hasAnnotatedMethods(String annotationType)
/* 122:    */   {
/* 123:136 */     Method[] methods = getIntrospectedClass().getDeclaredMethods();
/* 124:137 */     for (Method method : methods) {
/* 125:138 */       for (Annotation ann : method.getAnnotations())
/* 126:    */       {
/* 127:139 */         if (ann.annotationType().getName().equals(annotationType)) {
/* 128:140 */           return true;
/* 129:    */         }
/* 130:143 */         for (Annotation metaAnn : ann.annotationType().getAnnotations()) {
/* 131:144 */           if (metaAnn.annotationType().getName().equals(annotationType)) {
/* 132:145 */             return true;
/* 133:    */           }
/* 134:    */         }
/* 135:    */       }
/* 136:    */     }
/* 137:151 */     return false;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public Set<MethodMetadata> getAnnotatedMethods(String annotationType)
/* 141:    */   {
/* 142:155 */     Method[] methods = getIntrospectedClass().getDeclaredMethods();
/* 143:156 */     Set<MethodMetadata> annotatedMethods = new LinkedHashSet();
/* 144:157 */     for (Method method : methods) {
/* 145:158 */       for (Annotation ann : method.getAnnotations())
/* 146:    */       {
/* 147:159 */         if (ann.annotationType().getName().equals(annotationType))
/* 148:    */         {
/* 149:160 */           annotatedMethods.add(new StandardMethodMetadata(method));
/* 150:161 */           break;
/* 151:    */         }
/* 152:164 */         for (Annotation metaAnn : ann.annotationType().getAnnotations()) {
/* 153:165 */           if (metaAnn.annotationType().getName().equals(annotationType))
/* 154:    */           {
/* 155:166 */             annotatedMethods.add(new StandardMethodMetadata(method));
/* 156:167 */             break;
/* 157:    */           }
/* 158:    */         }
/* 159:    */       }
/* 160:    */     }
/* 161:173 */     return annotatedMethods;
/* 162:    */   }
/* 163:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.StandardAnnotationMetadata
 * JD-Core Version:    0.7.0.1
 */