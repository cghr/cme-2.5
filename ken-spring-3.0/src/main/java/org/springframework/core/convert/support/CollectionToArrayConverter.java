/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Array;
/*  4:   */ import java.util.Collection;
/*  5:   */ import java.util.Collections;
/*  6:   */ import java.util.Set;
/*  7:   */ import org.springframework.core.convert.ConversionService;
/*  8:   */ import org.springframework.core.convert.TypeDescriptor;
/*  9:   */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/* 10:   */ import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
/* 11:   */ 
/* 12:   */ final class CollectionToArrayConverter
/* 13:   */   implements ConditionalGenericConverter
/* 14:   */ {
/* 15:   */   private final ConversionService conversionService;
/* 16:   */   
/* 17:   */   public CollectionToArrayConverter(ConversionService conversionService)
/* 18:   */   {
/* 19:44 */     this.conversionService = conversionService;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 23:   */   {
/* 24:48 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Collection.class, [Ljava.lang.Object.class));
/* 25:   */   }
/* 26:   */   
/* 27:   */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 28:   */   {
/* 29:52 */     return ConversionUtils.canConvertElements(sourceType.getElementTypeDescriptor(), targetType.getElementTypeDescriptor(), this.conversionService);
/* 30:   */   }
/* 31:   */   
/* 32:   */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 33:   */   {
/* 34:56 */     if (source == null) {
/* 35:57 */       return null;
/* 36:   */     }
/* 37:59 */     Collection<?> sourceCollection = (Collection)source;
/* 38:60 */     Object array = Array.newInstance(targetType.getElementTypeDescriptor().getType(), sourceCollection.size());
/* 39:61 */     int i = 0;
/* 40:62 */     for (Object sourceElement : sourceCollection)
/* 41:   */     {
/* 42:63 */       Object targetElement = this.conversionService.convert(sourceElement, sourceType.elementTypeDescriptor(sourceElement), targetType.getElementTypeDescriptor());
/* 43:64 */       Array.set(array, i++, targetElement);
/* 44:   */     }
/* 45:66 */     return array;
/* 46:   */   }
/* 47:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.CollectionToArrayConverter
 * JD-Core Version:    0.7.0.1
 */