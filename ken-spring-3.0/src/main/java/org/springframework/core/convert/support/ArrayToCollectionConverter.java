/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Array;
/*  4:   */ import java.util.Collection;
/*  5:   */ import java.util.Collections;
/*  6:   */ import java.util.Set;
/*  7:   */ import org.springframework.core.CollectionFactory;
/*  8:   */ import org.springframework.core.convert.ConversionService;
/*  9:   */ import org.springframework.core.convert.TypeDescriptor;
/* 10:   */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/* 11:   */ import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
/* 12:   */ 
/* 13:   */ final class ArrayToCollectionConverter
/* 14:   */   implements ConditionalGenericConverter
/* 15:   */ {
/* 16:   */   private final ConversionService conversionService;
/* 17:   */   
/* 18:   */   public ArrayToCollectionConverter(ConversionService conversionService)
/* 19:   */   {
/* 20:44 */     this.conversionService = conversionService;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 24:   */   {
/* 25:48 */     return Collections.singleton(new GenericConverter.ConvertiblePair([Ljava.lang.Object.class, Collection.class));
/* 26:   */   }
/* 27:   */   
/* 28:   */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 29:   */   {
/* 30:52 */     return ConversionUtils.canConvertElements(sourceType.getElementTypeDescriptor(), targetType.getElementTypeDescriptor(), this.conversionService);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 34:   */   {
/* 35:57 */     if (source == null) {
/* 36:58 */       return null;
/* 37:   */     }
/* 38:60 */     int length = Array.getLength(source);
/* 39:61 */     Collection<Object> target = CollectionFactory.createCollection(targetType.getType(), length);
/* 40:62 */     if (targetType.getElementTypeDescriptor() == null) {
/* 41:63 */       for (int i = 0; i < length; i++)
/* 42:   */       {
/* 43:64 */         Object sourceElement = Array.get(source, i);
/* 44:65 */         target.add(sourceElement);
/* 45:   */       }
/* 46:   */     } else {
/* 47:69 */       for (int i = 0; i < length; i++)
/* 48:   */       {
/* 49:70 */         Object sourceElement = Array.get(source, i);
/* 50:71 */         Object targetElement = this.conversionService.convert(sourceElement, sourceType.elementTypeDescriptor(sourceElement), targetType.getElementTypeDescriptor());
/* 51:72 */         target.add(targetElement);
/* 52:   */       }
/* 53:   */     }
/* 54:75 */     return target;
/* 55:   */   }
/* 56:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.ArrayToCollectionConverter
 * JD-Core Version:    0.7.0.1
 */