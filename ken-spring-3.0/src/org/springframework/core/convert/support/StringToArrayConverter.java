/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Array;
/*  4:   */ import java.util.Collections;
/*  5:   */ import java.util.Set;
/*  6:   */ import org.springframework.core.convert.ConversionService;
/*  7:   */ import org.springframework.core.convert.TypeDescriptor;
/*  8:   */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*  9:   */ import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
/* 10:   */ import org.springframework.util.StringUtils;
/* 11:   */ 
/* 12:   */ final class StringToArrayConverter
/* 13:   */   implements ConditionalGenericConverter
/* 14:   */ {
/* 15:   */   private final ConversionService conversionService;
/* 16:   */   
/* 17:   */   public StringToArrayConverter(ConversionService conversionService)
/* 18:   */   {
/* 19:40 */     this.conversionService = conversionService;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 23:   */   {
/* 24:44 */     return Collections.singleton(new GenericConverter.ConvertiblePair(String.class, [Ljava.lang.Object.class));
/* 25:   */   }
/* 26:   */   
/* 27:   */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 28:   */   {
/* 29:48 */     return this.conversionService.canConvert(sourceType, targetType.getElementTypeDescriptor());
/* 30:   */   }
/* 31:   */   
/* 32:   */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 33:   */   {
/* 34:52 */     if (source == null) {
/* 35:53 */       return null;
/* 36:   */     }
/* 37:55 */     String string = (String)source;
/* 38:56 */     String[] fields = StringUtils.commaDelimitedListToStringArray(string);
/* 39:57 */     Object target = Array.newInstance(targetType.getElementTypeDescriptor().getType(), fields.length);
/* 40:58 */     for (int i = 0; i < fields.length; i++)
/* 41:   */     {
/* 42:59 */       String sourceElement = fields[i];
/* 43:60 */       Object targetElement = this.conversionService.convert(sourceElement.trim(), sourceType, targetType.getElementTypeDescriptor());
/* 44:61 */       Array.set(target, i, targetElement);
/* 45:   */     }
/* 46:63 */     return target;
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.StringToArrayConverter
 * JD-Core Version:    0.7.0.1
 */