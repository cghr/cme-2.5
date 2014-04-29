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
/* 12:   */ final class ObjectToCollectionConverter
/* 13:   */   implements ConditionalGenericConverter
/* 14:   */ {
/* 15:   */   private final ConversionService conversionService;
/* 16:   */   
/* 17:   */   public ObjectToCollectionConverter(ConversionService conversionService)
/* 18:   */   {
/* 19:41 */     this.conversionService = conversionService;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 23:   */   {
/* 24:45 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object.class, Collection.class));
/* 25:   */   }
/* 26:   */   
/* 27:   */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 28:   */   {
/* 29:49 */     return ConversionUtils.canConvertElements(sourceType, targetType.getElementTypeDescriptor(), this.conversionService);
/* 30:   */   }
/* 31:   */   
/* 32:   */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 33:   */   {
/* 34:54 */     if (source == null) {
/* 35:55 */       return null;
/* 36:   */     }
/* 37:57 */     Collection<Object> target = CollectionFactory.createCollection(targetType.getType(), 1);
/* 38:58 */     if ((targetType.getElementTypeDescriptor() == null) || (targetType.getElementTypeDescriptor().isCollection()))
/* 39:   */     {
/* 40:59 */       target.add(source);
/* 41:   */     }
/* 42:   */     else
/* 43:   */     {
/* 44:62 */       Object singleElement = this.conversionService.convert(source, sourceType, targetType.getElementTypeDescriptor());
/* 45:63 */       target.add(singleElement);
/* 46:   */     }
/* 47:65 */     return target;
/* 48:   */   }
/* 49:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.ObjectToCollectionConverter
 * JD-Core Version:    0.7.0.1
 */