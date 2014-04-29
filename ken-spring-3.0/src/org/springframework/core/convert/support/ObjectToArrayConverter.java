/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Array;
/*  4:   */ import java.util.Collections;
/*  5:   */ import java.util.Set;
/*  6:   */ import org.springframework.core.convert.ConversionService;
/*  7:   */ import org.springframework.core.convert.TypeDescriptor;
/*  8:   */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*  9:   */ import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
/* 10:   */ 
/* 11:   */ final class ObjectToArrayConverter
/* 12:   */   implements ConditionalGenericConverter
/* 13:   */ {
/* 14:   */   private final ConversionService conversionService;
/* 15:   */   
/* 16:   */   public ObjectToArrayConverter(ConversionService conversionService)
/* 17:   */   {
/* 18:39 */     this.conversionService = conversionService;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 22:   */   {
/* 23:43 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object.class, [Ljava.lang.Object.class));
/* 24:   */   }
/* 25:   */   
/* 26:   */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 27:   */   {
/* 28:47 */     return ConversionUtils.canConvertElements(sourceType, targetType.getElementTypeDescriptor(), this.conversionService);
/* 29:   */   }
/* 30:   */   
/* 31:   */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 32:   */   {
/* 33:51 */     if (source == null) {
/* 34:52 */       return null;
/* 35:   */     }
/* 36:54 */     Object target = Array.newInstance(targetType.getElementTypeDescriptor().getType(), 1);
/* 37:55 */     Object targetElement = this.conversionService.convert(source, sourceType, targetType.getElementTypeDescriptor());
/* 38:56 */     Array.set(target, 0, targetElement);
/* 39:57 */     return target;
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.ObjectToArrayConverter
 * JD-Core Version:    0.7.0.1
 */