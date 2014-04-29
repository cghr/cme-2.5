/*  1:   */ package org.springframework.core.convert;
/*  2:   */ 
/*  3:   */ import org.springframework.util.ObjectUtils;
/*  4:   */ 
/*  5:   */ public final class ConversionFailedException
/*  6:   */   extends ConversionException
/*  7:   */ {
/*  8:   */   private final TypeDescriptor sourceType;
/*  9:   */   private final TypeDescriptor targetType;
/* 10:   */   private final Object value;
/* 11:   */   
/* 12:   */   public ConversionFailedException(TypeDescriptor sourceType, TypeDescriptor targetType, Object value, Throwable cause)
/* 13:   */   {
/* 14:46 */     super("Failed to convert from type " + sourceType + " to type " + targetType + " for value '" + ObjectUtils.nullSafeToString(value) + "'", cause);
/* 15:47 */     this.sourceType = sourceType;
/* 16:48 */     this.targetType = targetType;
/* 17:49 */     this.value = value;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public TypeDescriptor getSourceType()
/* 21:   */   {
/* 22:57 */     return this.sourceType;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public TypeDescriptor getTargetType()
/* 26:   */   {
/* 27:64 */     return this.targetType;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public Object getValue()
/* 31:   */   {
/* 32:71 */     return this.value;
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.ConversionFailedException
 * JD-Core Version:    0.7.0.1
 */