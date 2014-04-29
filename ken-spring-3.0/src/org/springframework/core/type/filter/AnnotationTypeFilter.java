/*  1:   */ package org.springframework.core.type.filter;
/*  2:   */ 
/*  3:   */ import java.lang.annotation.Annotation;
/*  4:   */ import java.lang.annotation.Inherited;
/*  5:   */ import org.springframework.core.type.AnnotationMetadata;
/*  6:   */ import org.springframework.core.type.classreading.MetadataReader;
/*  7:   */ 
/*  8:   */ public class AnnotationTypeFilter
/*  9:   */   extends AbstractTypeHierarchyTraversingFilter
/* 10:   */ {
/* 11:   */   private final Class<? extends Annotation> annotationType;
/* 12:   */   private final boolean considerMetaAnnotations;
/* 13:   */   
/* 14:   */   public AnnotationTypeFilter(Class<? extends Annotation> annotationType)
/* 15:   */   {
/* 16:52 */     this(annotationType, true);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public AnnotationTypeFilter(Class<? extends Annotation> annotationType, boolean considerMetaAnnotations)
/* 20:   */   {
/* 21:62 */     this(annotationType, considerMetaAnnotations, false);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public AnnotationTypeFilter(Class<? extends Annotation> annotationType, boolean considerMetaAnnotations, boolean considerInterfaces)
/* 25:   */   {
/* 26:72 */     super(annotationType.isAnnotationPresent(Inherited.class), considerInterfaces);
/* 27:73 */     this.annotationType = annotationType;
/* 28:74 */     this.considerMetaAnnotations = considerMetaAnnotations;
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected boolean matchSelf(MetadataReader metadataReader)
/* 32:   */   {
/* 33:80 */     AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
/* 34:   */     
/* 35:82 */     return (metadata.hasAnnotation(this.annotationType.getName())) || ((this.considerMetaAnnotations) && (metadata.hasMetaAnnotation(this.annotationType.getName())));
/* 36:   */   }
/* 37:   */   
/* 38:   */   protected Boolean matchSuperClass(String superClassName)
/* 39:   */   {
/* 40:87 */     if (Object.class.getName().equals(superClassName)) {
/* 41:88 */       return Boolean.FALSE;
/* 42:   */     }
/* 43:90 */     if (superClassName.startsWith("java.")) {
/* 44:   */       try
/* 45:   */       {
/* 46:92 */         Class<?> clazz = getClass().getClassLoader().loadClass(superClassName);
/* 47:93 */         if (clazz.getAnnotation(this.annotationType) != null) {
/* 48:93 */           return Boolean.valueOf(true);
/* 49:   */         }
/* 50:93 */         return Boolean.valueOf(false);
/* 51:   */       }
/* 52:   */       catch (ClassNotFoundException localClassNotFoundException) {}
/* 53:   */     }
/* 54:99 */     return null;
/* 55:   */   }
/* 56:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.filter.AnnotationTypeFilter
 * JD-Core Version:    0.7.0.1
 */