/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import java.util.Collection;
/*  4:   */ import java.util.Collections;
/*  5:   */ import java.util.Set;
/*  6:   */ import org.springframework.core.CollectionFactory;
/*  7:   */ import org.springframework.core.convert.ConversionService;
/*  8:   */ import org.springframework.core.convert.TypeDescriptor;
/*  9:   */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/* 10:   */ import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
/* 11:   */ 
/* 12:   */ final class CollectionToCollectionConverter
/* 13:   */   implements ConditionalGenericConverter
/* 14:   */ {
/* 15:   */   private final ConversionService conversionService;
/* 16:   */   
/* 17:   */   public CollectionToCollectionConverter(ConversionService conversionService)
/* 18:   */   {
/* 19:44 */     this.conversionService = conversionService;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 23:   */   {
/* 24:48 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Collection.class, Collection.class));
/* 25:   */   }
/* 26:   */   
/* 27:   */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 28:   */   {
/* 29:52 */     return ConversionUtils.canConvertElements(sourceType.getElementTypeDescriptor(), targetType.getElementTypeDescriptor(), this.conversionService);
/* 30:   */   }
/* 31:   */   
/* 32:   */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 33:   */   {
/* 34:57 */     if (source == null) {
/* 35:58 */       return null;
/* 36:   */     }
/* 37:60 */     boolean copyRequired = !targetType.getType().isInstance(source);
/* 38:61 */     Collection<?> sourceCollection = (Collection)source;
/* 39:62 */     Collection<Object> target = CollectionFactory.createCollection(targetType.getType(), sourceCollection.size());
/* 40:63 */     if (targetType.getElementTypeDescriptor() == null) {
/* 41:64 */       for (Object element : sourceCollection) {
/* 42:65 */         target.add(element);
/* 43:   */       }
/* 44:   */     } else {
/* 45:69 */       for (Object sourceElement : sourceCollection)
/* 46:   */       {
/* 47:70 */         Object targetElement = this.conversionService.convert(sourceElement, sourceType.elementTypeDescriptor(sourceElement), targetType.getElementTypeDescriptor());
/* 48:71 */         target.add(targetElement);
/* 49:72 */         if (sourceElement != targetElement) {
/* 50:73 */           copyRequired = true;
/* 51:   */         }
/* 52:   */       }
/* 53:   */     }
/* 54:77 */     return copyRequired ? target : source;
/* 55:   */   }
/* 56:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.CollectionToCollectionConverter
 * JD-Core Version:    0.7.0.1
 */