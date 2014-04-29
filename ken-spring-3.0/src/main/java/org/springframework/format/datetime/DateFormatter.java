/*   1:    */ package org.springframework.format.datetime;
/*   2:    */ 
/*   3:    */ import java.text.DateFormat;
/*   4:    */ import java.text.ParseException;
/*   5:    */ import java.text.SimpleDateFormat;
/*   6:    */ import java.util.Date;
/*   7:    */ import java.util.Locale;
/*   8:    */ import java.util.TimeZone;
/*   9:    */ import org.springframework.format.Formatter;
/*  10:    */ 
/*  11:    */ public class DateFormatter
/*  12:    */   implements Formatter<Date>
/*  13:    */ {
/*  14:    */   private String pattern;
/*  15: 41 */   private int style = 2;
/*  16:    */   private TimeZone timeZone;
/*  17: 45 */   private boolean lenient = false;
/*  18:    */   
/*  19:    */   public DateFormatter() {}
/*  20:    */   
/*  21:    */   public DateFormatter(String pattern)
/*  22:    */   {
/*  23: 58 */     this.pattern = pattern;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setPattern(String pattern)
/*  27:    */   {
/*  28: 67 */     this.pattern = pattern;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setStyle(int style)
/*  32:    */   {
/*  33: 80 */     this.style = style;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setTimeZone(TimeZone timeZone)
/*  37:    */   {
/*  38: 87 */     this.timeZone = timeZone;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setLenient(boolean lenient)
/*  42:    */   {
/*  43: 96 */     this.lenient = lenient;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public String print(Date date, Locale locale)
/*  47:    */   {
/*  48:101 */     return getDateFormat(locale).format(date);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public Date parse(String text, Locale locale)
/*  52:    */     throws ParseException
/*  53:    */   {
/*  54:105 */     return getDateFormat(locale).parse(text);
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected DateFormat getDateFormat(Locale locale)
/*  58:    */   {
/*  59:    */     DateFormat dateFormat;
/*  60:    */     DateFormat dateFormat;
/*  61:111 */     if (this.pattern != null) {
/*  62:112 */       dateFormat = new SimpleDateFormat(this.pattern, locale);
/*  63:    */     } else {
/*  64:115 */       dateFormat = DateFormat.getDateInstance(this.style, locale);
/*  65:    */     }
/*  66:117 */     if (this.timeZone != null) {
/*  67:118 */       dateFormat.setTimeZone(this.timeZone);
/*  68:    */     }
/*  69:120 */     dateFormat.setLenient(this.lenient);
/*  70:121 */     return dateFormat;
/*  71:    */   }
/*  72:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.datetime.DateFormatter
 * JD-Core Version:    0.7.0.1
 */