/*   1:    */ package org.springframework.format.number;
/*   2:    */ 
/*   3:    */ import java.math.BigDecimal;
/*   4:    */ import java.math.RoundingMode;
/*   5:    */ import java.text.DecimalFormat;
/*   6:    */ import java.text.NumberFormat;
/*   7:    */ import java.text.ParseException;
/*   8:    */ import java.util.Currency;
/*   9:    */ import java.util.Locale;
/*  10:    */ import org.springframework.util.ClassUtils;
/*  11:    */ 
/*  12:    */ public class CurrencyFormatter
/*  13:    */   extends AbstractNumberFormatter
/*  14:    */ {
/*  15: 45 */   private static final boolean roundingModeOnDecimalFormat = ClassUtils.hasMethod(DecimalFormat.class, "setRoundingMode", new Class[] { RoundingMode.class });
/*  16: 47 */   private int fractionDigits = 2;
/*  17:    */   private RoundingMode roundingMode;
/*  18:    */   private Currency currency;
/*  19:    */   
/*  20:    */   public void setFractionDigits(int fractionDigits)
/*  21:    */   {
/*  22: 59 */     this.fractionDigits = fractionDigits;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setRoundingMode(RoundingMode roundingMode)
/*  26:    */   {
/*  27: 67 */     this.roundingMode = roundingMode;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setCurrency(Currency currency)
/*  31:    */   {
/*  32: 74 */     this.currency = currency;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public BigDecimal parse(String text, Locale locale)
/*  36:    */     throws ParseException
/*  37:    */   {
/*  38: 79 */     BigDecimal decimal = (BigDecimal)super.parse(text, locale);
/*  39: 80 */     if (decimal != null) {
/*  40: 81 */       if (this.roundingMode != null) {
/*  41: 82 */         decimal = decimal.setScale(this.fractionDigits, this.roundingMode);
/*  42:    */       } else {
/*  43: 85 */         decimal = decimal.setScale(this.fractionDigits);
/*  44:    */       }
/*  45:    */     }
/*  46: 88 */     return decimal;
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected NumberFormat getNumberFormat(Locale locale)
/*  50:    */   {
/*  51: 92 */     DecimalFormat format = (DecimalFormat)NumberFormat.getCurrencyInstance(locale);
/*  52: 93 */     format.setParseBigDecimal(true);
/*  53: 94 */     format.setMaximumFractionDigits(this.fractionDigits);
/*  54: 95 */     format.setMinimumFractionDigits(this.fractionDigits);
/*  55: 96 */     if ((this.roundingMode != null) && (roundingModeOnDecimalFormat)) {
/*  56: 97 */       format.setRoundingMode(this.roundingMode);
/*  57:    */     }
/*  58: 99 */     if (this.currency != null) {
/*  59:100 */       format.setCurrency(this.currency);
/*  60:    */     }
/*  61:102 */     return format;
/*  62:    */   }
/*  63:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.number.CurrencyFormatter
 * JD-Core Version:    0.7.0.1
 */