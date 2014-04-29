/*  1:   */ package org.springframework.web.servlet.theme;
/*  2:   */ 
/*  3:   */ import org.springframework.web.servlet.ThemeResolver;
/*  4:   */ 
/*  5:   */ public abstract class AbstractThemeResolver
/*  6:   */   implements ThemeResolver
/*  7:   */ {
/*  8:   */   public static final String ORIGINAL_DEFAULT_THEME_NAME = "theme";
/*  9:36 */   private String defaultThemeName = "theme";
/* 10:   */   
/* 11:   */   public void setDefaultThemeName(String defaultThemeName)
/* 12:   */   {
/* 13:44 */     this.defaultThemeName = defaultThemeName;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public String getDefaultThemeName()
/* 17:   */   {
/* 18:51 */     return this.defaultThemeName;
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.theme.AbstractThemeResolver
 * JD-Core Version:    0.7.0.1
 */