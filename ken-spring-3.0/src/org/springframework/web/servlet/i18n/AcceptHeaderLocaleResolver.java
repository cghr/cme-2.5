/*  1:   */ package org.springframework.web.servlet.i18n;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import javax.servlet.http.HttpServletRequest;
/*  5:   */ import javax.servlet.http.HttpServletResponse;
/*  6:   */ import org.springframework.web.servlet.LocaleResolver;
/*  7:   */ 
/*  8:   */ public class AcceptHeaderLocaleResolver
/*  9:   */   implements LocaleResolver
/* 10:   */ {
/* 11:   */   public Locale resolveLocale(HttpServletRequest request)
/* 12:   */   {
/* 13:41 */     return request.getLocale();
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale)
/* 17:   */   {
/* 18:45 */     throw new UnsupportedOperationException(
/* 19:46 */       "Cannot change HTTP accept header - use a different locale resolution strategy");
/* 20:   */   }
/* 21:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
 * JD-Core Version:    0.7.0.1
 */