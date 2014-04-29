/*  1:   */ package org.springframework.format.annotation;
/*  2:   */ 
/*  3:   */ import java.lang.annotation.Annotation;
/*  4:   */ import java.lang.annotation.Retention;
/*  5:   */ import java.lang.annotation.RetentionPolicy;
/*  6:   */ import java.lang.annotation.Target;
/*  7:   */ 
/*  8:   */ @Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.PARAMETER})
/*  9:   */ @Retention(RetentionPolicy.RUNTIME)
/* 10:   */ public @interface DateTimeFormat
/* 11:   */ {
/* 12:   */   String style() default "SS";
/* 13:   */   
/* 14:   */   ISO iso() default ISO.NONE;
/* 15:   */   
/* 16:   */   String pattern() default "";
/* 17:   */   
/* 18:   */   public static enum ISO
/* 19:   */   {
/* 20:82 */     DATE,  TIME,  DATE_TIME,  NONE;
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.annotation.DateTimeFormat
 * JD-Core Version:    0.7.0.1
 */