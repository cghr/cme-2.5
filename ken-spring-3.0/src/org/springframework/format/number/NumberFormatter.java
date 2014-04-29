/*  1:   */ package org.springframework.format.number;
/*  2:   */ 
/*  3:   */ import java.text.DecimalFormat;
/*  4:   */ import java.text.NumberFormat;
/*  5:   */ import java.util.Locale;
/*  6:   */ 
/*  7:   */ public class NumberFormatter
/*  8:   */   extends AbstractNumberFormatter
/*  9:   */ {
/* 10:   */   private String pattern;
/* 11:   */   
/* 12:   */   public NumberFormatter() {}
/* 13:   */   
/* 14:   */   public NumberFormatter(String pattern)
/* 15:   */   {
/* 16:54 */     this.pattern = pattern;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void setPattern(String pattern)
/* 20:   */   {
/* 21:64 */     this.pattern = pattern;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public NumberFormat getNumberFormat(Locale locale)
/* 25:   */   {
/* 26:69 */     NumberFormat format = NumberFormat.getInstance(locale);
/* 27:70 */     if (!(format instanceof DecimalFormat))
/* 28:   */     {
/* 29:71 */       if (this.pattern != null) {
/* 30:72 */         throw new IllegalStateException("Cannot support pattern for non-DecimalFormat: " + format);
/* 31:   */       }
/* 32:74 */       return format;
/* 33:   */     }
/* 34:76 */     DecimalFormat decimalFormat = (DecimalFormat)format;
/* 35:77 */     decimalFormat.setParseBigDecimal(true);
/* 36:78 */     if (this.pattern != null) {
/* 37:79 */       decimalFormat.applyPattern(this.pattern);
/* 38:   */     }
/* 39:81 */     return decimalFormat;
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.number.NumberFormatter
 * JD-Core Version:    0.7.0.1
 */