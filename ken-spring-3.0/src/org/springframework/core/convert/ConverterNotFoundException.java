/*  1:   */ package org.springframework.core.convert;
/*  2:   */ 
/*  3:   */ public final class ConverterNotFoundException
/*  4:   */   extends ConversionException
/*  5:   */ {
/*  6:   */   private final TypeDescriptor sourceType;
/*  7:   */   private final TypeDescriptor targetType;
/*  8:   */   
/*  9:   */   public ConverterNotFoundException(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 10:   */   {
/* 11:40 */     super("No converter found capable of converting from type " + sourceType + " to type " + targetType);
/* 12:41 */     this.sourceType = sourceType;
/* 13:42 */     this.targetType = targetType;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public TypeDescriptor getSourceType()
/* 17:   */   {
/* 18:50 */     return this.sourceType;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public TypeDescriptor getTargetType()
/* 22:   */   {
/* 23:57 */     return this.targetType;
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.ConverterNotFoundException
 * JD-Core Version:    0.7.0.1
 */