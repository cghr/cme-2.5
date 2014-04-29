/*  1:   */ package org.springframework.ui.context.support;
/*  2:   */ 
/*  3:   */ import org.springframework.ui.context.HierarchicalThemeSource;
/*  4:   */ import org.springframework.ui.context.Theme;
/*  5:   */ import org.springframework.ui.context.ThemeSource;
/*  6:   */ 
/*  7:   */ public class DelegatingThemeSource
/*  8:   */   implements HierarchicalThemeSource
/*  9:   */ {
/* 10:   */   private ThemeSource parentThemeSource;
/* 11:   */   
/* 12:   */   public void setParentThemeSource(ThemeSource parentThemeSource)
/* 13:   */   {
/* 14:40 */     this.parentThemeSource = parentThemeSource;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public ThemeSource getParentThemeSource()
/* 18:   */   {
/* 19:44 */     return this.parentThemeSource;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Theme getTheme(String themeName)
/* 23:   */   {
/* 24:49 */     if (this.parentThemeSource != null) {
/* 25:50 */       return this.parentThemeSource.getTheme(themeName);
/* 26:   */     }
/* 27:53 */     return null;
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ui.context.support.DelegatingThemeSource
 * JD-Core Version:    0.7.0.1
 */