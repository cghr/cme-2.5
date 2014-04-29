/*   1:    */ package org.springframework.web.servlet.theme;
/*   2:    */ 
/*   3:    */ import javax.servlet.http.Cookie;
/*   4:    */ import javax.servlet.http.HttpServletRequest;
/*   5:    */ import javax.servlet.http.HttpServletResponse;
/*   6:    */ import org.springframework.web.servlet.ThemeResolver;
/*   7:    */ import org.springframework.web.util.CookieGenerator;
/*   8:    */ import org.springframework.web.util.WebUtils;
/*   9:    */ 
/*  10:    */ public class CookieThemeResolver
/*  11:    */   extends CookieGenerator
/*  12:    */   implements ThemeResolver
/*  13:    */ {
/*  14:    */   public static final String ORIGINAL_DEFAULT_THEME_NAME = "theme";
/*  15: 51 */   public static final String THEME_REQUEST_ATTRIBUTE_NAME = CookieThemeResolver.class.getName() + ".THEME";
/*  16: 53 */   public static final String DEFAULT_COOKIE_NAME = CookieThemeResolver.class.getName() + ".THEME";
/*  17: 56 */   private String defaultThemeName = "theme";
/*  18:    */   
/*  19:    */   public CookieThemeResolver()
/*  20:    */   {
/*  21: 60 */     setCookieName(DEFAULT_COOKIE_NAME);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setDefaultThemeName(String defaultThemeName)
/*  25:    */   {
/*  26: 68 */     this.defaultThemeName = defaultThemeName;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public String getDefaultThemeName()
/*  30:    */   {
/*  31: 75 */     return this.defaultThemeName;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public String resolveThemeName(HttpServletRequest request)
/*  35:    */   {
/*  36: 81 */     String theme = (String)request.getAttribute(THEME_REQUEST_ATTRIBUTE_NAME);
/*  37: 82 */     if (theme != null) {
/*  38: 83 */       return theme;
/*  39:    */     }
/*  40: 87 */     Cookie cookie = WebUtils.getCookie(request, getCookieName());
/*  41: 88 */     if (cookie != null) {
/*  42: 89 */       return cookie.getValue();
/*  43:    */     }
/*  44: 93 */     return getDefaultThemeName();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setThemeName(HttpServletRequest request, HttpServletResponse response, String themeName)
/*  48:    */   {
/*  49: 97 */     if (themeName != null)
/*  50:    */     {
/*  51: 99 */       request.setAttribute(THEME_REQUEST_ATTRIBUTE_NAME, themeName);
/*  52:100 */       addCookie(response, themeName);
/*  53:    */     }
/*  54:    */     else
/*  55:    */     {
/*  56:105 */       request.setAttribute(THEME_REQUEST_ATTRIBUTE_NAME, getDefaultThemeName());
/*  57:106 */       removeCookie(response);
/*  58:    */     }
/*  59:    */   }
/*  60:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.theme.CookieThemeResolver
 * JD-Core Version:    0.7.0.1
 */