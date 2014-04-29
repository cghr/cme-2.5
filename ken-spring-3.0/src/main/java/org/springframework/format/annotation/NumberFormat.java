/*  1:   */ package org.springframework.format.annotation;
/*  2:   */ 
/*  3:   */ import java.lang.annotation.Annotation;
/*  4:   */ import java.lang.annotation.Retention;
/*  5:   */ import java.lang.annotation.RetentionPolicy;
/*  6:   */ import java.lang.annotation.Target;
/*  7:   */ 
/*  8:   */ @Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.PARAMETER})
/*  9:   */ @Retention(RetentionPolicy.RUNTIME)
/* 10:   */ public @interface NumberFormat
/* 11:   */ {
/* 12:   */   Style style() default Style.NUMBER;
/* 13:   */   
/* 14:   */   String pattern() default "";
/* 15:   */   
/* 16:   */   public static enum Style
/* 17:   */   {
/* 18:69 */     NUMBER,  CURRENCY,  PERCENT;
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.annotation.NumberFormat
 * JD-Core Version:    0.7.0.1
 */