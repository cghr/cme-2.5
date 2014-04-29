/*  1:   */ package org.springframework.web.servlet.i18n;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import javax.servlet.http.HttpServletRequest;
/*  5:   */ import javax.servlet.http.HttpServletResponse;
/*  6:   */ import org.springframework.web.util.WebUtils;
/*  7:   */ 
/*  8:   */ public class SessionLocaleResolver
/*  9:   */   extends AbstractLocaleResolver
/* 10:   */ {
/* 11:52 */   public static final String LOCALE_SESSION_ATTRIBUTE_NAME = SessionLocaleResolver.class.getName() + ".LOCALE";
/* 12:   */   
/* 13:   */   public Locale resolveLocale(HttpServletRequest request)
/* 14:   */   {
/* 15:56 */     Locale locale = (Locale)WebUtils.getSessionAttribute(request, LOCALE_SESSION_ATTRIBUTE_NAME);
/* 16:57 */     if (locale == null) {
/* 17:58 */       locale = determineDefaultLocale(request);
/* 18:   */     }
/* 19:60 */     return locale;
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected Locale determineDefaultLocale(HttpServletRequest request)
/* 23:   */   {
/* 24:74 */     Locale defaultLocale = getDefaultLocale();
/* 25:75 */     if (defaultLocale == null) {
/* 26:76 */       defaultLocale = request.getLocale();
/* 27:   */     }
/* 28:78 */     return defaultLocale;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale)
/* 32:   */   {
/* 33:82 */     WebUtils.setSessionAttribute(request, LOCALE_SESSION_ATTRIBUTE_NAME, locale);
/* 34:   */   }
/* 35:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.i18n.SessionLocaleResolver
 * JD-Core Version:    0.7.0.1
 */