/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Constructor;
/*  4:   */ import java.lang.reflect.InvocationTargetException;
/*  5:   */ import java.lang.reflect.Method;
/*  6:   */ import java.util.Collections;
/*  7:   */ import java.util.Set;
/*  8:   */ import org.springframework.core.convert.ConversionFailedException;
/*  9:   */ import org.springframework.core.convert.TypeDescriptor;
/* 10:   */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/* 11:   */ import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
/* 12:   */ import org.springframework.util.ClassUtils;
/* 13:   */ import org.springframework.util.ReflectionUtils;
/* 14:   */ 
/* 15:   */ final class ObjectToObjectConverter
/* 16:   */   implements ConditionalGenericConverter
/* 17:   */ {
/* 18:   */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 19:   */   {
/* 20:47 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object.class, Object.class));
/* 21:   */   }
/* 22:   */   
/* 23:   */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 24:   */   {
/* 25:51 */     return (!sourceType.equals(targetType)) && (hasValueOfMethodOrConstructor(targetType.getType(), sourceType.getType()));
/* 26:   */   }
/* 27:   */   
/* 28:   */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 29:   */   {
/* 30:55 */     Class<?> sourceClass = sourceType.getType();
/* 31:56 */     Class<?> targetClass = targetType.getType();
/* 32:57 */     Method method = getValueOfMethodOn(targetClass, sourceClass);
/* 33:   */     try
/* 34:   */     {
/* 35:59 */       if (method != null)
/* 36:   */       {
/* 37:60 */         ReflectionUtils.makeAccessible(method);
/* 38:61 */         return method.invoke(null, new Object[] { source });
/* 39:   */       }
/* 40:64 */       Constructor<?> constructor = getConstructor(targetClass, sourceClass);
/* 41:65 */       if (constructor != null) {
/* 42:66 */         return constructor.newInstance(new Object[] { source });
/* 43:   */       }
/* 44:   */     }
/* 45:   */     catch (InvocationTargetException ex)
/* 46:   */     {
/* 47:71 */       throw new ConversionFailedException(sourceType, targetType, source, ex.getTargetException());
/* 48:   */     }
/* 49:   */     catch (Throwable ex)
/* 50:   */     {
/* 51:74 */       throw new ConversionFailedException(sourceType, targetType, source, ex);
/* 52:   */     }
/* 53:76 */     throw new IllegalStateException("No static valueOf(" + sourceClass.getName() + 
/* 54:77 */       ") method or Constructor(" + sourceClass.getName() + ") exists on " + targetClass.getName());
/* 55:   */   }
/* 56:   */   
/* 57:   */   static boolean hasValueOfMethodOrConstructor(Class<?> clazz, Class<?> sourceParameterType)
/* 58:   */   {
/* 59:81 */     return (getValueOfMethodOn(clazz, sourceParameterType) != null) || (getConstructor(clazz, sourceParameterType) != null);
/* 60:   */   }
/* 61:   */   
/* 62:   */   private static Method getValueOfMethodOn(Class<?> clazz, Class<?> sourceParameterType)
/* 63:   */   {
/* 64:85 */     return ClassUtils.getStaticMethod(clazz, "valueOf", new Class[] { sourceParameterType });
/* 65:   */   }
/* 66:   */   
/* 67:   */   private static Constructor<?> getConstructor(Class<?> clazz, Class<?> sourceParameterType)
/* 68:   */   {
/* 69:89 */     return ClassUtils.getConstructorIfAvailable(clazz, new Class[] { sourceParameterType });
/* 70:   */   }
/* 71:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.ObjectToObjectConverter
 * JD-Core Version:    0.7.0.1
 */