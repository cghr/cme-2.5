/*  1:   */ package org.springframework.core.convert;
/*  2:   */ 
/*  3:   */ import java.lang.annotation.Annotation;
/*  4:   */ import org.springframework.core.GenericCollectionTypeResolver;
/*  5:   */ import org.springframework.core.MethodParameter;
/*  6:   */ 
/*  7:   */ class BeanPropertyDescriptor
/*  8:   */   extends AbstractDescriptor
/*  9:   */ {
/* 10:   */   private final Property property;
/* 11:   */   private final MethodParameter methodParameter;
/* 12:   */   private final Annotation[] annotations;
/* 13:   */   
/* 14:   */   public BeanPropertyDescriptor(Property property)
/* 15:   */   {
/* 16:32 */     super(property.getType());
/* 17:33 */     this.property = property;
/* 18:34 */     this.methodParameter = property.getMethodParameter();
/* 19:35 */     this.annotations = property.getAnnotations();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Annotation[] getAnnotations()
/* 23:   */   {
/* 24:40 */     return this.annotations;
/* 25:   */   }
/* 26:   */   
/* 27:   */   protected Class<?> resolveCollectionElementType()
/* 28:   */   {
/* 29:45 */     return GenericCollectionTypeResolver.getCollectionParameterType(this.methodParameter);
/* 30:   */   }
/* 31:   */   
/* 32:   */   protected Class<?> resolveMapKeyType()
/* 33:   */   {
/* 34:50 */     return GenericCollectionTypeResolver.getMapKeyParameterType(this.methodParameter);
/* 35:   */   }
/* 36:   */   
/* 37:   */   protected Class<?> resolveMapValueType()
/* 38:   */   {
/* 39:55 */     return GenericCollectionTypeResolver.getMapValueParameterType(this.methodParameter);
/* 40:   */   }
/* 41:   */   
/* 42:   */   protected AbstractDescriptor nested(Class<?> type, int typeIndex)
/* 43:   */   {
/* 44:60 */     MethodParameter methodParameter = new MethodParameter(this.methodParameter);
/* 45:61 */     methodParameter.increaseNestingLevel();
/* 46:62 */     methodParameter.setTypeIndexForCurrentLevel(typeIndex);
/* 47:63 */     return new BeanPropertyDescriptor(type, this.property, methodParameter, this.annotations);
/* 48:   */   }
/* 49:   */   
/* 50:   */   private BeanPropertyDescriptor(Class<?> type, Property propertyDescriptor, MethodParameter methodParameter, Annotation[] annotations)
/* 51:   */   {
/* 52:69 */     super(type);
/* 53:70 */     this.property = propertyDescriptor;
/* 54:71 */     this.methodParameter = methodParameter;
/* 55:72 */     this.annotations = annotations;
/* 56:   */   }
/* 57:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.BeanPropertyDescriptor
 * JD-Core Version:    0.7.0.1
 */