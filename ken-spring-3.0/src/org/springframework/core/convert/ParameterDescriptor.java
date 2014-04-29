/*  1:   */ package org.springframework.core.convert;
/*  2:   */ 
/*  3:   */ import java.lang.annotation.Annotation;
/*  4:   */ import org.springframework.core.GenericCollectionTypeResolver;
/*  5:   */ import org.springframework.core.MethodParameter;
/*  6:   */ 
/*  7:   */ class ParameterDescriptor
/*  8:   */   extends AbstractDescriptor
/*  9:   */ {
/* 10:   */   private final MethodParameter methodParameter;
/* 11:   */   
/* 12:   */   public ParameterDescriptor(MethodParameter methodParameter)
/* 13:   */   {
/* 14:34 */     super(methodParameter.getParameterType());
/* 15:35 */     if (methodParameter.getNestingLevel() != 1) {
/* 16:36 */       throw new IllegalArgumentException("MethodParameter argument must have its nestingLevel set to 1");
/* 17:   */     }
/* 18:38 */     this.methodParameter = methodParameter;
/* 19:   */   }
/* 20:   */   
/* 21:   */   private ParameterDescriptor(Class<?> type, MethodParameter methodParameter)
/* 22:   */   {
/* 23:42 */     super(type);
/* 24:43 */     this.methodParameter = methodParameter;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public Annotation[] getAnnotations()
/* 28:   */   {
/* 29:49 */     if (this.methodParameter.getParameterIndex() == -1) {
/* 30:50 */       return TypeDescriptor.nullSafeAnnotations(this.methodParameter.getMethodAnnotations());
/* 31:   */     }
/* 32:53 */     return TypeDescriptor.nullSafeAnnotations(this.methodParameter.getParameterAnnotations());
/* 33:   */   }
/* 34:   */   
/* 35:   */   protected Class<?> resolveCollectionElementType()
/* 36:   */   {
/* 37:59 */     return GenericCollectionTypeResolver.getCollectionParameterType(this.methodParameter);
/* 38:   */   }
/* 39:   */   
/* 40:   */   protected Class<?> resolveMapKeyType()
/* 41:   */   {
/* 42:64 */     return GenericCollectionTypeResolver.getMapKeyParameterType(this.methodParameter);
/* 43:   */   }
/* 44:   */   
/* 45:   */   protected Class<?> resolveMapValueType()
/* 46:   */   {
/* 47:69 */     return GenericCollectionTypeResolver.getMapValueParameterType(this.methodParameter);
/* 48:   */   }
/* 49:   */   
/* 50:   */   protected AbstractDescriptor nested(Class<?> type, int typeIndex)
/* 51:   */   {
/* 52:74 */     MethodParameter methodParameter = new MethodParameter(this.methodParameter);
/* 53:75 */     methodParameter.increaseNestingLevel();
/* 54:76 */     methodParameter.setTypeIndexForCurrentLevel(typeIndex);
/* 55:77 */     return new ParameterDescriptor(type, methodParameter);
/* 56:   */   }
/* 57:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.ParameterDescriptor
 * JD-Core Version:    0.7.0.1
 */