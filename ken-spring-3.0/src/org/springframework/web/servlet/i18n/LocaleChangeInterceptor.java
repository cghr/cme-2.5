/*  1:   */ package org.springframework.web.servlet.i18n;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletException;
/*  4:   */ import javax.servlet.http.HttpServletRequest;
/*  5:   */ import javax.servlet.http.HttpServletResponse;
/*  6:   */ import org.springframework.util.StringUtils;
/*  7:   */ import org.springframework.web.servlet.LocaleResolver;
/*  8:   */ import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
/*  9:   */ import org.springframework.web.servlet.support.RequestContextUtils;
/* 10:   */ 
/* 11:   */ public class LocaleChangeInterceptor
/* 12:   */   extends HandlerInterceptorAdapter
/* 13:   */ {
/* 14:   */   public static final String DEFAULT_PARAM_NAME = "locale";
/* 15:43 */   private String paramName = "locale";
/* 16:   */   
/* 17:   */   public void setParamName(String paramName)
/* 18:   */   {
/* 19:51 */     this.paramName = paramName;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public String getParamName()
/* 23:   */   {
/* 24:59 */     return this.paramName;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/* 28:   */     throws ServletException
/* 29:   */   {
/* 30:67 */     String newLocale = request.getParameter(this.paramName);
/* 31:68 */     if (newLocale != null)
/* 32:   */     {
/* 33:69 */       LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
/* 34:70 */       if (localeResolver == null) {
/* 35:71 */         throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
/* 36:   */       }
/* 37:73 */       localeResolver.setLocale(request, response, StringUtils.parseLocaleString(newLocale));
/* 38:   */     }
/* 39:76 */     return true;
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.i18n.LocaleChangeInterceptor
 * JD-Core Version:    0.7.0.1
 */