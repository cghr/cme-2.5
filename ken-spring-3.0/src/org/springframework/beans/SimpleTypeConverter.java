/*  1:   */ package org.springframework.beans;
/*  2:   */ 
/*  3:   */ import org.springframework.core.MethodParameter;
/*  4:   */ import org.springframework.core.convert.ConversionException;
/*  5:   */ import org.springframework.core.convert.ConverterNotFoundException;
/*  6:   */ 
/*  7:   */ public class SimpleTypeConverter
/*  8:   */   extends PropertyEditorRegistrySupport
/*  9:   */   implements TypeConverter
/* 10:   */ {
/* 11:34 */   private final TypeConverterDelegate typeConverterDelegate = new TypeConverterDelegate(this);
/* 12:   */   
/* 13:   */   public SimpleTypeConverter()
/* 14:   */   {
/* 15:38 */     registerDefaultEditors();
/* 16:   */   }
/* 17:   */   
/* 18:   */   public <T> T convertIfNecessary(Object value, Class<T> requiredType)
/* 19:   */     throws TypeMismatchException
/* 20:   */   {
/* 21:43 */     return convertIfNecessary(value, requiredType, null);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public <T> T convertIfNecessary(Object value, Class<T> requiredType, MethodParameter methodParam)
/* 25:   */     throws TypeMismatchException
/* 26:   */   {
/* 27:   */     try
/* 28:   */     {
/* 29:49 */       return this.typeConverterDelegate.convertIfNecessary(value, requiredType, methodParam);
/* 30:   */     }
/* 31:   */     catch (ConverterNotFoundException ex)
/* 32:   */     {
/* 33:52 */       throw new ConversionNotSupportedException(value, requiredType, ex);
/* 34:   */     }
/* 35:   */     catch (ConversionException ex)
/* 36:   */     {
/* 37:55 */       throw new TypeMismatchException(value, requiredType, ex);
/* 38:   */     }
/* 39:   */     catch (IllegalStateException ex)
/* 40:   */     {
/* 41:58 */       throw new ConversionNotSupportedException(value, requiredType, ex);
/* 42:   */     }
/* 43:   */     catch (IllegalArgumentException ex)
/* 44:   */     {
/* 45:61 */       throw new TypeMismatchException(value, requiredType, ex);
/* 46:   */     }
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.SimpleTypeConverter
 * JD-Core Version:    0.7.0.1
 */