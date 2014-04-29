/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import java.io.StringWriter;
/*  4:   */ import java.util.Collections;
/*  5:   */ import java.util.Set;
/*  6:   */ import org.springframework.core.convert.TypeDescriptor;
/*  7:   */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*  8:   */ import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
/*  9:   */ 
/* 10:   */ final class FallbackObjectToStringConverter
/* 11:   */   implements ConditionalGenericConverter
/* 12:   */ {
/* 13:   */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 14:   */   {
/* 15:39 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object.class, String.class));
/* 16:   */   }
/* 17:   */   
/* 18:   */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 19:   */   {
/* 20:43 */     Class<?> sourceClass = sourceType.getObjectType();
/* 21:   */     
/* 22:45 */     return (CharSequence.class.isAssignableFrom(sourceClass)) || (StringWriter.class.isAssignableFrom(sourceClass)) || (ObjectToObjectConverter.hasValueOfMethodOrConstructor(sourceClass, String.class));
/* 23:   */   }
/* 24:   */   
/* 25:   */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 26:   */   {
/* 27:49 */     return source != null ? source.toString() : null;
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.FallbackObjectToStringConverter
 * JD-Core Version:    0.7.0.1
 */