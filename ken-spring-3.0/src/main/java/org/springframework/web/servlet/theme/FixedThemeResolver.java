/*  1:   */ package org.springframework.web.servlet.theme;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.HttpServletRequest;
/*  4:   */ import javax.servlet.http.HttpServletResponse;
/*  5:   */ 
/*  6:   */ public class FixedThemeResolver
/*  7:   */   extends AbstractThemeResolver
/*  8:   */ {
/*  9:   */   public String resolveThemeName(HttpServletRequest request)
/* 10:   */   {
/* 11:37 */     return getDefaultThemeName();
/* 12:   */   }
/* 13:   */   
/* 14:   */   public void setThemeName(HttpServletRequest request, HttpServletResponse response, String themeName)
/* 15:   */   {
/* 16:41 */     throw new UnsupportedOperationException("Cannot change theme - use a different theme resolution strategy");
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.theme.FixedThemeResolver
 * JD-Core Version:    0.7.0.1
 */