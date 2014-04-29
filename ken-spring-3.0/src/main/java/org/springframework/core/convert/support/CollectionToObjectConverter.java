/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import java.util.Collection;
/*  4:   */ import java.util.Collections;
/*  5:   */ import java.util.Iterator;
/*  6:   */ import java.util.Set;
/*  7:   */ import org.springframework.core.convert.ConversionService;
/*  8:   */ import org.springframework.core.convert.TypeDescriptor;
/*  9:   */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/* 10:   */ import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
/* 11:   */ 
/* 12:   */ final class CollectionToObjectConverter
/* 13:   */   implements ConditionalGenericConverter
/* 14:   */ {
/* 15:   */   private final ConversionService conversionService;
/* 16:   */   
/* 17:   */   public CollectionToObjectConverter(ConversionService conversionService)
/* 18:   */   {
/* 19:38 */     this.conversionService = conversionService;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 23:   */   {
/* 24:42 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Collection.class, Object.class));
/* 25:   */   }
/* 26:   */   
/* 27:   */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 28:   */   {
/* 29:46 */     return ConversionUtils.canConvertElements(sourceType.getElementTypeDescriptor(), targetType, this.conversionService);
/* 30:   */   }
/* 31:   */   
/* 32:   */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 33:   */   {
/* 34:50 */     if (source == null) {
/* 35:51 */       return null;
/* 36:   */     }
/* 37:53 */     if (sourceType.isAssignableTo(targetType)) {
/* 38:54 */       return source;
/* 39:   */     }
/* 40:56 */     Collection<?> sourceCollection = (Collection)source;
/* 41:57 */     if (sourceCollection.size() == 0) {
/* 42:58 */       return null;
/* 43:   */     }
/* 44:60 */     Object firstElement = sourceCollection.iterator().next();
/* 45:61 */     return this.conversionService.convert(firstElement, sourceType.elementTypeDescriptor(firstElement), targetType);
/* 46:   */   }
/* 47:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.CollectionToObjectConverter
 * JD-Core Version:    0.7.0.1
 */