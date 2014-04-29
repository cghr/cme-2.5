/*  1:   */ package org.springframework.web.servlet.i18n;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import javax.servlet.http.HttpServletRequest;
/*  5:   */ import javax.servlet.http.HttpServletResponse;
/*  6:   */ 
/*  7:   */ public class FixedLocaleResolver
/*  8:   */   extends AbstractLocaleResolver
/*  9:   */ {
/* 10:   */   public FixedLocaleResolver() {}
/* 11:   */   
/* 12:   */   public FixedLocaleResolver(Locale locale)
/* 13:   */   {
/* 14:51 */     setDefaultLocale(locale);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public Locale resolveLocale(HttpServletRequest request)
/* 18:   */   {
/* 19:56 */     Locale locale = getDefaultLocale();
/* 20:57 */     if (locale == null) {
/* 21:58 */       locale = Locale.getDefault();
/* 22:   */     }
/* 23:60 */     return locale;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale)
/* 27:   */   {
/* 28:64 */     throw new UnsupportedOperationException(
/* 29:65 */       "Cannot change fixed locale - use a different locale resolution strategy");
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.i18n.FixedLocaleResolver
 * JD-Core Version:    0.7.0.1
 */