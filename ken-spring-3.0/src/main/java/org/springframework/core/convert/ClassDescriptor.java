/*  1:   */ package org.springframework.core.convert;
/*  2:   */ 
/*  3:   */ import java.lang.annotation.Annotation;
/*  4:   */ 
/*  5:   */ class ClassDescriptor
/*  6:   */   extends AbstractDescriptor
/*  7:   */ {
/*  8:   */   ClassDescriptor(Class<?> type)
/*  9:   */   {
/* 10:28 */     super(type);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public Annotation[] getAnnotations()
/* 14:   */   {
/* 15:33 */     return TypeDescriptor.EMPTY_ANNOTATION_ARRAY;
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected Class<?> resolveCollectionElementType()
/* 19:   */   {
/* 20:38 */     return null;
/* 21:   */   }
/* 22:   */   
/* 23:   */   protected Class<?> resolveMapKeyType()
/* 24:   */   {
/* 25:43 */     return null;
/* 26:   */   }
/* 27:   */   
/* 28:   */   protected Class<?> resolveMapValueType()
/* 29:   */   {
/* 30:48 */     return null;
/* 31:   */   }
/* 32:   */   
/* 33:   */   protected AbstractDescriptor nested(Class<?> type, int typeIndex)
/* 34:   */   {
/* 35:53 */     return new ClassDescriptor(type);
/* 36:   */   }
/* 37:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.ClassDescriptor
 * JD-Core Version:    0.7.0.1
 */