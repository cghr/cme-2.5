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
/* 11:   */ import org.springframework.util.StringUtils;
/* 12:   */ 
/* 13:   */ final class StringToCollectionConverter
/* 14:   */   implements ConditionalGenericConverter
/* 15:   */ {
/* 16:   */   private final ConversionService conversionService;
/* 17:   */   
/* 18:   */   public StringToCollectionConverter(ConversionService conversionService)
/* 19:   */   {
/* 20:41 */     this.conversionService = conversionService;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 24:   */   {
/* 25:45 */     return Collections.singleton(new GenericConverter.ConvertiblePair(String.class, Collection.class));
/* 26:   */   }
/* 27:   */   
/* 28:   */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 29:   */   {
/* 30:49 */     if (targetType.getElementTypeDescriptor() != null) {
/* 31:50 */       return this.conversionService.canConvert(sourceType, targetType.getElementTypeDescriptor());
/* 32:   */     }
/* 33:52 */     return true;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 37:   */   {
/* 38:58 */     if (source == null) {
/* 39:59 */       return null;
/* 40:   */     }
/* 41:61 */     String string = (String)source;
/* 42:62 */     String[] fields = StringUtils.commaDelimitedListToStringArray(string);
/* 43:63 */     Collection<Object> target = CollectionFactory.createCollection(targetType.getType(), fields.length);
/* 44:64 */     if (targetType.getElementTypeDescriptor() == null) {
/* 45:65 */       for (String field : fields) {
/* 46:66 */         target.add(field.trim());
/* 47:   */       }
/* 48:   */     } else {
/* 49:69 */       for (String field : fields)
/* 50:   */       {
/* 51:70 */         Object targetElement = this.conversionService.convert(field.trim(), sourceType, targetType.getElementTypeDescriptor());
/* 52:71 */         target.add(targetElement);
/* 53:   */       }
/* 54:   */     }
/* 55:74 */     return target;
/* 56:   */   }
/* 57:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.StringToCollectionConverter
 * JD-Core Version:    0.7.0.1
 */