/*   1:    */ package org.springframework.web.util;
/*   2:    */ 
/*   3:    */ import javax.servlet.http.Cookie;
/*   4:    */ import javax.servlet.http.HttpServletResponse;
/*   5:    */ import org.apache.commons.logging.Log;
/*   6:    */ import org.apache.commons.logging.LogFactory;
/*   7:    */ 
/*   8:    */ public class CookieGenerator
/*   9:    */ {
/*  10:    */   public static final String DEFAULT_COOKIE_PATH = "/";
/*  11:    */   @Deprecated
/*  12:    */   public static final int DEFAULT_COOKIE_MAX_AGE = 2147483647;
/*  13: 55 */   protected final Log logger = LogFactory.getLog(getClass());
/*  14:    */   private String cookieName;
/*  15:    */   private String cookieDomain;
/*  16: 61 */   private String cookiePath = "/";
/*  17: 63 */   private Integer cookieMaxAge = null;
/*  18: 65 */   private boolean cookieSecure = false;
/*  19:    */   
/*  20:    */   public void setCookieName(String cookieName)
/*  21:    */   {
/*  22: 72 */     this.cookieName = cookieName;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public String getCookieName()
/*  26:    */   {
/*  27: 79 */     return this.cookieName;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setCookieDomain(String cookieDomain)
/*  31:    */   {
/*  32: 87 */     this.cookieDomain = cookieDomain;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public String getCookieDomain()
/*  36:    */   {
/*  37: 94 */     return this.cookieDomain;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setCookiePath(String cookiePath)
/*  41:    */   {
/*  42:102 */     this.cookiePath = cookiePath;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public String getCookiePath()
/*  46:    */   {
/*  47:109 */     return this.cookiePath;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setCookieMaxAge(Integer cookieMaxAge)
/*  51:    */   {
/*  52:117 */     this.cookieMaxAge = cookieMaxAge;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public Integer getCookieMaxAge()
/*  56:    */   {
/*  57:124 */     return this.cookieMaxAge;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setCookieSecure(boolean cookieSecure)
/*  61:    */   {
/*  62:133 */     this.cookieSecure = cookieSecure;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public boolean isCookieSecure()
/*  66:    */   {
/*  67:141 */     return this.cookieSecure;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void addCookie(HttpServletResponse response, String cookieValue)
/*  71:    */   {
/*  72:157 */     Cookie cookie = createCookie(cookieValue);
/*  73:158 */     Integer maxAge = getCookieMaxAge();
/*  74:159 */     if (maxAge != null) {
/*  75:160 */       cookie.setMaxAge(maxAge.intValue());
/*  76:    */     }
/*  77:162 */     if (isCookieSecure()) {
/*  78:163 */       cookie.setSecure(true);
/*  79:    */     }
/*  80:165 */     response.addCookie(cookie);
/*  81:166 */     if (this.logger.isDebugEnabled()) {
/*  82:167 */       this.logger.debug("Added cookie with name [" + getCookieName() + "] and value [" + cookieValue + "]");
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void removeCookie(HttpServletResponse response)
/*  87:    */   {
/*  88:181 */     Cookie cookie = createCookie("");
/*  89:182 */     cookie.setMaxAge(0);
/*  90:183 */     response.addCookie(cookie);
/*  91:184 */     if (this.logger.isDebugEnabled()) {
/*  92:185 */       this.logger.debug("Removed cookie with name [" + getCookieName() + "]");
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   protected Cookie createCookie(String cookieValue)
/*  97:    */   {
/*  98:199 */     Cookie cookie = new Cookie(getCookieName(), cookieValue);
/*  99:200 */     if (getCookieDomain() != null) {
/* 100:201 */       cookie.setDomain(getCookieDomain());
/* 101:    */     }
/* 102:203 */     cookie.setPath(getCookiePath());
/* 103:204 */     return cookie;
/* 104:    */   }
/* 105:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.util.CookieGenerator
 * JD-Core Version:    0.7.0.1
 */