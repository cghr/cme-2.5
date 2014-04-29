/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Method;
/*  4:   */ import java.lang.reflect.Modifier;
/*  5:   */ import java.util.Collections;
/*  6:   */ import java.util.Set;
/*  7:   */ import org.springframework.core.convert.ConversionService;
/*  8:   */ import org.springframework.core.convert.TypeDescriptor;
/*  9:   */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/* 10:   */ import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
/* 11:   */ import org.springframework.util.ClassUtils;
/* 12:   */ import org.springframework.util.ReflectionUtils;
/* 13:   */ 
/* 14:   */ final class IdToEntityConverter
/* 15:   */   implements ConditionalGenericConverter
/* 16:   */ {
/* 17:   */   private final ConversionService conversionService;
/* 18:   */   
/* 19:   */   public IdToEntityConverter(ConversionService conversionService)
/* 20:   */   {
/* 21:45 */     this.conversionService = conversionService;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 25:   */   {
/* 26:49 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object.class, Object.class));
/* 27:   */   }
/* 28:   */   
/* 29:   */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 30:   */   {
/* 31:53 */     Method finder = getFinder(targetType.getType());
/* 32:54 */     return (finder != null) && (this.conversionService.canConvert(sourceType, TypeDescriptor.valueOf(finder.getParameterTypes()[0])));
/* 33:   */   }
/* 34:   */   
/* 35:   */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 36:   */   {
/* 37:58 */     if (source == null) {
/* 38:59 */       return null;
/* 39:   */     }
/* 40:61 */     Method finder = getFinder(targetType.getType());
/* 41:62 */     Object id = this.conversionService.convert(source, sourceType, TypeDescriptor.valueOf(finder.getParameterTypes()[0]));
/* 42:63 */     return ReflectionUtils.invokeMethod(finder, source, new Object[] { id });
/* 43:   */   }
/* 44:   */   
/* 45:   */   private Method getFinder(Class<?> entityClass)
/* 46:   */   {
/* 47:67 */     String finderMethod = "find" + getEntityName(entityClass);
/* 48:68 */     Method[] methods = entityClass.getDeclaredMethods();
/* 49:69 */     for (Method method : methods) {
/* 50:70 */       if ((Modifier.isStatic(method.getModifiers())) && (method.getParameterTypes().length == 1) && (method.getReturnType().equals(entityClass)) && 
/* 51:71 */         (method.getName().equals(finderMethod))) {
/* 52:72 */         return method;
/* 53:   */       }
/* 54:   */     }
/* 55:76 */     return null;
/* 56:   */   }
/* 57:   */   
/* 58:   */   private String getEntityName(Class<?> entityClass)
/* 59:   */   {
/* 60:80 */     String shortName = ClassUtils.getShortName(entityClass);
/* 61:81 */     int lastDot = shortName.lastIndexOf('.');
/* 62:82 */     if (lastDot != -1) {
/* 63:83 */       return shortName.substring(lastDot + 1);
/* 64:   */     }
/* 65:86 */     return shortName;
/* 66:   */   }
/* 67:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.IdToEntityConverter
 * JD-Core Version:    0.7.0.1
 */