/*  1:   */ package org.springframework.core.convert;
/*  2:   */ 
/*  3:   */ import org.springframework.core.NestedRuntimeException;
/*  4:   */ 
/*  5:   */ public abstract class ConversionException
/*  6:   */   extends NestedRuntimeException
/*  7:   */ {
/*  8:   */   public ConversionException(String message)
/*  9:   */   {
/* 10:35 */     super(message);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public ConversionException(String message, Throwable cause)
/* 14:   */   {
/* 15:44 */     super(message, cause);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.ConversionException
 * JD-Core Version:    0.7.0.1
 */