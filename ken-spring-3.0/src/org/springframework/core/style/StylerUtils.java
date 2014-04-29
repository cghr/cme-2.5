/*  1:   */ package org.springframework.core.style;
/*  2:   */ 
/*  3:   */ public abstract class StylerUtils
/*  4:   */ {
/*  5:38 */   static final ValueStyler DEFAULT_VALUE_STYLER = new DefaultValueStyler();
/*  6:   */   
/*  7:   */   public static String style(Object value)
/*  8:   */   {
/*  9:47 */     return DEFAULT_VALUE_STYLER.style(value);
/* 10:   */   }
/* 11:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.style.StylerUtils
 * JD-Core Version:    0.7.0.1
 */