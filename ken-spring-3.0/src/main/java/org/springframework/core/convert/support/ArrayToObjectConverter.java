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
/* 11:   */ final class ArrayToObjectConverter
/* 12:   */   implements ConditionalGenericConverter
/* 13:   */ {
/* 14:   */   private final ConversionService conversionService;
/* 15:   */   
/* 16:   */   public ArrayToObjectConverter(ConversionService conversionService)
/* 17:   */   {
/* 18:38 */     this.conversionService = conversionService;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 22:   */   {
/* 23:42 */     return Collections.singleton(new GenericConverter.ConvertiblePair([Ljava.lang.Object.class, Object.class));
/* 24:   */   }
/* 25:   */   
/* 26:   */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 27:   */   {
/* 28:46 */     return ConversionUtils.canConvertElements(sourceType.getElementTypeDescriptor(), targetType, this.conversionService);
/* 29:   */   }
/* 30:   */   
/* 31:   */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 32:   */   {
/* 33:50 */     if (source == null) {
/* 34:51 */       return null;
/* 35:   */     }
/* 36:53 */     if (sourceType.isAssignableTo(targetType)) {
/* 37:54 */       return source;
/* 38:   */     }
/* 39:56 */     if (Array.getLength(source) == 0) {
/* 40:57 */       return null;
/* 41:   */     }
/* 42:59 */     Object firstElement = Array.get(source, 0);
/* 43:60 */     return this.conversionService.convert(firstElement, sourceType.elementTypeDescriptor(firstElement), targetType);
/* 44:   */   }
/* 45:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.ArrayToObjectConverter
 * JD-Core Version:    0.7.0.1
 */