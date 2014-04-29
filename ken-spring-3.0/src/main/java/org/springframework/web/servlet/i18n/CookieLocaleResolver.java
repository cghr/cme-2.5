/*   1:    */ package org.springframework.web.servlet.i18n;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import javax.servlet.http.Cookie;
/*   5:    */ import javax.servlet.http.HttpServletRequest;
/*   6:    */ import javax.servlet.http.HttpServletResponse;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.springframework.util.StringUtils;
/*   9:    */ import org.springframework.web.servlet.LocaleResolver;
/*  10:    */ import org.springframework.web.util.CookieGenerator;
/*  11:    */ import org.springframework.web.util.WebUtils;
/*  12:    */ 
/*  13:    */ public class CookieLocaleResolver
/*  14:    */   extends CookieGenerator
/*  15:    */   implements LocaleResolver
/*  16:    */ {
/*  17: 57 */   public static final String LOCALE_REQUEST_ATTRIBUTE_NAME = CookieLocaleResolver.class.getName() + ".LOCALE";
/*  18: 62 */   public static final String DEFAULT_COOKIE_NAME = CookieLocaleResolver.class.getName() + ".LOCALE";
/*  19:    */   private Locale defaultLocale;
/*  20:    */   
/*  21:    */   public CookieLocaleResolver()
/*  22:    */   {
/*  23: 73 */     setCookieName(DEFAULT_COOKIE_NAME);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setDefaultLocale(Locale defaultLocale)
/*  27:    */   {
/*  28: 80 */     this.defaultLocale = defaultLocale;
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected Locale getDefaultLocale()
/*  32:    */   {
/*  33: 88 */     return this.defaultLocale;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public Locale resolveLocale(HttpServletRequest request)
/*  37:    */   {
/*  38: 94 */     Locale locale = (Locale)request.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME);
/*  39: 95 */     if (locale != null) {
/*  40: 96 */       return locale;
/*  41:    */     }
/*  42:100 */     Cookie cookie = WebUtils.getCookie(request, getCookieName());
/*  43:101 */     if (cookie != null)
/*  44:    */     {
/*  45:102 */       locale = StringUtils.parseLocaleString(cookie.getValue());
/*  46:103 */       if (this.logger.isDebugEnabled()) {
/*  47:104 */         this.logger.debug("Parsed cookie value [" + cookie.getValue() + "] into locale '" + locale + "'");
/*  48:    */       }
/*  49:106 */       if (locale != null)
/*  50:    */       {
/*  51:107 */         request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME, locale);
/*  52:108 */         return locale;
/*  53:    */       }
/*  54:    */     }
/*  55:112 */     return determineDefaultLocale(request);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale)
/*  59:    */   {
/*  60:116 */     if (locale != null)
/*  61:    */     {
/*  62:118 */       request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME, locale);
/*  63:119 */       addCookie(response, locale.toString());
/*  64:    */     }
/*  65:    */     else
/*  66:    */     {
/*  67:123 */       request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME, determineDefaultLocale(request));
/*  68:124 */       removeCookie(response);
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   protected Locale determineDefaultLocale(HttpServletRequest request)
/*  73:    */   {
/*  74:139 */     Locale defaultLocale = getDefaultLocale();
/*  75:140 */     if (defaultLocale == null) {
/*  76:141 */       defaultLocale = request.getLocale();
/*  77:    */     }
/*  78:143 */     return defaultLocale;
/*  79:    */   }
/*  80:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.i18n.CookieLocaleResolver
 * JD-Core Version:    0.7.0.1
 */