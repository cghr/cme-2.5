/*  1:   */ package org.springframework.format.number;
/*  2:   */ 
/*  3:   */ import java.text.NumberFormat;
/*  4:   */ import java.text.ParseException;
/*  5:   */ import java.text.ParsePosition;
/*  6:   */ import java.util.Locale;
/*  7:   */ import org.springframework.format.Formatter;
/*  8:   */ 
/*  9:   */ public abstract class AbstractNumberFormatter
/* 10:   */   implements Formatter<Number>
/* 11:   */ {
/* 12:36 */   private boolean lenient = false;
/* 13:   */   
/* 14:   */   public void setLenient(boolean lenient)
/* 15:   */   {
/* 16:44 */     this.lenient = lenient;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public String print(Number number, Locale locale)
/* 20:   */   {
/* 21:48 */     return getNumberFormat(locale).format(number);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public Number parse(String text, Locale locale)
/* 25:   */     throws ParseException
/* 26:   */   {
/* 27:52 */     NumberFormat format = getNumberFormat(locale);
/* 28:53 */     ParsePosition position = new ParsePosition(0);
/* 29:54 */     Number number = format.parse(text, position);
/* 30:55 */     if (position.getErrorIndex() != -1) {
/* 31:56 */       throw new ParseException(text, position.getIndex());
/* 32:   */     }
/* 33:58 */     if ((!this.lenient) && 
/* 34:59 */       (text.length() != position.getIndex())) {
/* 35:61 */       throw new ParseException(text, position.getIndex());
/* 36:   */     }
/* 37:64 */     return number;
/* 38:   */   }
/* 39:   */   
/* 40:   */   protected abstract NumberFormat getNumberFormat(Locale paramLocale);
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.number.AbstractNumberFormatter
 * JD-Core Version:    0.7.0.1
 */