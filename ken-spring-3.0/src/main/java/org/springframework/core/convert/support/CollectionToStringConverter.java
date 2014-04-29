/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import java.util.Collection;
/*  4:   */ import java.util.Collections;
/*  5:   */ import java.util.Set;
/*  6:   */ import org.springframework.core.convert.ConversionService;
/*  7:   */ import org.springframework.core.convert.TypeDescriptor;
/*  8:   */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*  9:   */ import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
/* 10:   */ 
/* 11:   */ final class CollectionToStringConverter
/* 12:   */   implements ConditionalGenericConverter
/* 13:   */ {
/* 14:   */   private static final String DELIMITER = ",";
/* 15:   */   private final ConversionService conversionService;
/* 16:   */   
/* 17:   */   public CollectionToStringConverter(ConversionService conversionService)
/* 18:   */   {
/* 19:40 */     this.conversionService = conversionService;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 23:   */   {
/* 24:44 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Collection.class, String.class));
/* 25:   */   }
/* 26:   */   
/* 27:   */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 28:   */   {
/* 29:48 */     return ConversionUtils.canConvertElements(sourceType.getElementTypeDescriptor(), targetType, this.conversionService);
/* 30:   */   }
/* 31:   */   
/* 32:   */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 33:   */   {
/* 34:52 */     if (source == null) {
/* 35:53 */       return null;
/* 36:   */     }
/* 37:55 */     Collection<?> sourceCollection = (Collection)source;
/* 38:56 */     if (sourceCollection.size() == 0) {
/* 39:57 */       return "";
/* 40:   */     }
/* 41:59 */     StringBuilder sb = new StringBuilder();
/* 42:60 */     int i = 0;
/* 43:61 */     for (Object sourceElement : sourceCollection)
/* 44:   */     {
/* 45:62 */       if (i > 0) {
/* 46:63 */         sb.append(",");
/* 47:   */       }
/* 48:65 */       Object targetElement = this.conversionService.convert(sourceElement, sourceType.elementTypeDescriptor(sourceElement), targetType);
/* 49:66 */       sb.append(targetElement);
/* 50:67 */       i++;
/* 51:   */     }
/* 52:69 */     return sb.toString();
/* 53:   */   }
/* 54:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.CollectionToStringConverter
 * JD-Core Version:    0.7.0.1
 */