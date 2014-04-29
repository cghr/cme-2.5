/*  1:   */ package org.springframework.context.i18n;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ 
/*  6:   */ public class SimpleLocaleContext
/*  7:   */   implements LocaleContext
/*  8:   */ {
/*  9:   */   private final Locale locale;
/* 10:   */   
/* 11:   */   public SimpleLocaleContext(Locale locale)
/* 12:   */   {
/* 13:41 */     Assert.notNull(locale, "Locale must not be null");
/* 14:42 */     this.locale = locale;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public Locale getLocale()
/* 18:   */   {
/* 19:46 */     return this.locale;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public String toString()
/* 23:   */   {
/* 24:51 */     return this.locale.toString();
/* 25:   */   }
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.i18n.SimpleLocaleContext
 * JD-Core Version:    0.7.0.1
 */