/*  1:   */ package org.springframework.web.servlet.theme;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.HttpServletRequest;
/*  4:   */ import javax.servlet.http.HttpServletResponse;
/*  5:   */ import org.springframework.web.util.WebUtils;
/*  6:   */ 
/*  7:   */ public class SessionThemeResolver
/*  8:   */   extends AbstractThemeResolver
/*  9:   */ {
/* 10:47 */   public static final String THEME_SESSION_ATTRIBUTE_NAME = SessionThemeResolver.class.getName() + ".THEME";
/* 11:   */   
/* 12:   */   public String resolveThemeName(HttpServletRequest request)
/* 13:   */   {
/* 14:50 */     String theme = (String)WebUtils.getSessionAttribute(request, THEME_SESSION_ATTRIBUTE_NAME);
/* 15:   */     
/* 16:52 */     return theme != null ? theme : getDefaultThemeName();
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void setThemeName(HttpServletRequest request, HttpServletResponse response, String themeName)
/* 20:   */   {
/* 21:56 */     WebUtils.setSessionAttribute(request, THEME_SESSION_ATTRIBUTE_NAME, themeName);
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.theme.SessionThemeResolver
 * JD-Core Version:    0.7.0.1
 */