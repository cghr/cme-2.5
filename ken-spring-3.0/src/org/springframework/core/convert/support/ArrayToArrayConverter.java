/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import java.util.Arrays;
/*  4:   */ import java.util.Collections;
/*  5:   */ import java.util.Set;
/*  6:   */ import org.springframework.core.convert.ConversionService;
/*  7:   */ import org.springframework.core.convert.TypeDescriptor;
/*  8:   */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*  9:   */ import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
/* 10:   */ import org.springframework.util.ObjectUtils;
/* 11:   */ 
/* 12:   */ final class ArrayToArrayConverter
/* 13:   */   implements ConditionalGenericConverter
/* 14:   */ {
/* 15:   */   private final CollectionToArrayConverter helperConverter;
/* 16:   */   
/* 17:   */   public ArrayToArrayConverter(ConversionService conversionService)
/* 18:   */   {
/* 19:40 */     this.helperConverter = new CollectionToArrayConverter(conversionService);
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 23:   */   {
/* 24:44 */     return Collections.singleton(new GenericConverter.ConvertiblePair([Ljava.lang.Object.class, [Ljava.lang.Object.class));
/* 25:   */   }
/* 26:   */   
/* 27:   */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 28:   */   {
/* 29:48 */     return this.helperConverter.matches(sourceType, targetType);
/* 30:   */   }
/* 31:   */   
/* 32:   */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 33:   */   {
/* 34:52 */     return this.helperConverter.convert(Arrays.asList(ObjectUtils.toObjectArray(source)), sourceType, targetType);
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.ArrayToArrayConverter
 * JD-Core Version:    0.7.0.1
 */