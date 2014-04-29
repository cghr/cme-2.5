/*  1:   */ package org.springframework.web.servlet.theme;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletException;
/*  4:   */ import javax.servlet.http.HttpServletRequest;
/*  5:   */ import javax.servlet.http.HttpServletResponse;
/*  6:   */ import org.springframework.web.servlet.ThemeResolver;
/*  7:   */ import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
/*  8:   */ import org.springframework.web.servlet.support.RequestContextUtils;
/*  9:   */ 
/* 10:   */ public class ThemeChangeInterceptor
/* 11:   */   extends HandlerInterceptorAdapter
/* 12:   */ {
/* 13:   */   public static final String DEFAULT_PARAM_NAME = "theme";
/* 14:42 */   private String paramName = "theme";
/* 15:   */   
/* 16:   */   public void setParamName(String paramName)
/* 17:   */   {
/* 18:50 */     this.paramName = paramName;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public String getParamName()
/* 22:   */   {
/* 23:58 */     return this.paramName;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/* 27:   */     throws ServletException
/* 28:   */   {
/* 29:66 */     ThemeResolver themeResolver = RequestContextUtils.getThemeResolver(request);
/* 30:67 */     if (themeResolver == null) {
/* 31:68 */       throw new IllegalStateException("No ThemeResolver found: not in a DispatcherServlet request?");
/* 32:   */     }
/* 33:70 */     String newTheme = request.getParameter(this.paramName);
/* 34:71 */     if (newTheme != null) {
/* 35:72 */       themeResolver.setThemeName(request, response, newTheme);
/* 36:   */     }
/* 37:75 */     return true;
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.theme.ThemeChangeInterceptor
 * JD-Core Version:    0.7.0.1
 */