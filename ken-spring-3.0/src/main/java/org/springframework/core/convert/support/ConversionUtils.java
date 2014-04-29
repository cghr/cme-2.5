/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.convert.ConversionFailedException;
/*  4:   */ import org.springframework.core.convert.ConversionService;
/*  5:   */ import org.springframework.core.convert.TypeDescriptor;
/*  6:   */ import org.springframework.core.convert.converter.GenericConverter;
/*  7:   */ 
/*  8:   */ abstract class ConversionUtils
/*  9:   */ {
/* 10:   */   public static Object invokeConverter(GenericConverter converter, Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 11:   */   {
/* 12:   */     try
/* 13:   */     {
/* 14:35 */       return converter.convert(source, sourceType, targetType);
/* 15:   */     }
/* 16:   */     catch (ConversionFailedException ex)
/* 17:   */     {
/* 18:38 */       throw ex;
/* 19:   */     }
/* 20:   */     catch (Exception ex)
/* 21:   */     {
/* 22:41 */       throw new ConversionFailedException(sourceType, targetType, source, ex);
/* 23:   */     }
/* 24:   */   }
/* 25:   */   
/* 26:   */   public static boolean canConvertElements(TypeDescriptor sourceElementType, TypeDescriptor targetElementType, ConversionService conversionService)
/* 27:   */   {
/* 28:46 */     if (targetElementType == null) {
/* 29:48 */       return true;
/* 30:   */     }
/* 31:50 */     if (sourceElementType == null) {
/* 32:52 */       return true;
/* 33:   */     }
/* 34:54 */     if (conversionService.canConvert(sourceElementType, targetElementType)) {
/* 35:56 */       return true;
/* 36:   */     }
/* 37:58 */     if (sourceElementType.getType().isAssignableFrom(targetElementType.getType())) {
/* 38:60 */       return true;
/* 39:   */     }
/* 40:64 */     return false;
/* 41:   */   }
/* 42:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.ConversionUtils
 * JD-Core Version:    0.7.0.1
 */