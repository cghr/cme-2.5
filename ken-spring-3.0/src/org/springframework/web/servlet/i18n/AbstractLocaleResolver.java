/*  1:   */ package org.springframework.web.servlet.i18n;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import org.springframework.web.servlet.LocaleResolver;
/*  5:   */ 
/*  6:   */ public abstract class AbstractLocaleResolver
/*  7:   */   implements LocaleResolver
/*  8:   */ {
/*  9:   */   private Locale defaultLocale;
/* 10:   */   
/* 11:   */   public void setDefaultLocale(Locale defaultLocale)
/* 12:   */   {
/* 13:39 */     this.defaultLocale = defaultLocale;
/* 14:   */   }
/* 15:   */   
/* 16:   */   protected Locale getDefaultLocale()
/* 17:   */   {
/* 18:46 */     return this.defaultLocale;
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.i18n.AbstractLocaleResolver
 * JD-Core Version:    0.7.0.1
 */