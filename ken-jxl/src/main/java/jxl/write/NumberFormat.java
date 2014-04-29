/*   1:    */ package jxl.write;
/*   2:    */ 
/*   3:    */ import java.text.DecimalFormat;
/*   4:    */ import jxl.biff.DisplayFormat;
/*   5:    */ import jxl.write.biff.NumberFormatRecord;
/*   6:    */ import jxl.write.biff.NumberFormatRecord.NonValidatingFormat;
/*   7:    */ 
/*   8:    */ public class NumberFormat
/*   9:    */   extends NumberFormatRecord
/*  10:    */   implements DisplayFormat
/*  11:    */ {
/*  12: 43 */   public static final NumberFormatRecord.NonValidatingFormat COMPLEX_FORMAT = new NumberFormatRecord.NonValidatingFormat();
/*  13:    */   public static final String CURRENCY_EURO_PREFIX = "[$�-2]";
/*  14:    */   public static final String CURRENCY_EURO_SUFFIX = "[$�-1]";
/*  15:    */   public static final String CURRENCY_POUND = "�";
/*  16:    */   public static final String CURRENCY_JAPANESE_YEN = "[$�-411]";
/*  17:    */   public static final String CURRENCY_DOLLAR = "[$$-409]";
/*  18:    */   public static final String FRACTION_THREE_DIGITS = "???/???";
/*  19:    */   public static final String FRACTION_HALVES = "?/2";
/*  20:    */   public static final String FRACTION_QUARTERS = "?/4";
/*  21:    */   public static final String FRACTIONS_EIGHTHS = "?/8";
/*  22:    */   public static final String FRACTION_SIXTEENTHS = "?/16";
/*  23:    */   public static final String FRACTION_TENTHS = "?/10";
/*  24:    */   public static final String FRACTION_HUNDREDTHS = "?/100";
/*  25:    */   
/*  26:    */   public NumberFormat(String format)
/*  27:    */   {
/*  28:116 */     super(format);
/*  29:    */     
/*  30:    */ 
/*  31:119 */     DecimalFormat df = new DecimalFormat(format);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public NumberFormat(String format, NumberFormatRecord.NonValidatingFormat dummy)
/*  35:    */   {
/*  36:137 */     super(format, dummy);
/*  37:    */   }
/*  38:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.NumberFormat
 * JD-Core Version:    0.7.0.1
 */