/*  1:   */ package org.springframework.format.number;
/*  2:   */ 
/*  3:   */ import java.text.DecimalFormat;
/*  4:   */ import java.text.NumberFormat;
/*  5:   */ import java.util.Locale;
/*  6:   */ 
/*  7:   */ public class PercentFormatter
/*  8:   */   extends AbstractNumberFormatter
/*  9:   */ {
/* 10:   */   protected NumberFormat getNumberFormat(Locale locale)
/* 11:   */   {
/* 12:38 */     NumberFormat format = NumberFormat.getPercentInstance(locale);
/* 13:39 */     if ((format instanceof DecimalFormat)) {
/* 14:40 */       ((DecimalFormat)format).setParseBigDecimal(true);
/* 15:   */     }
/* 16:42 */     return format;
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.number.PercentFormatter
 * JD-Core Version:    0.7.0.1
 */