/*  1:   */ package org.springframework.ui.context.support;
/*  2:   */ 
/*  3:   */ import org.apache.commons.logging.Log;
/*  4:   */ import org.apache.commons.logging.LogFactory;
/*  5:   */ import org.springframework.context.ApplicationContext;
/*  6:   */ import org.springframework.ui.context.HierarchicalThemeSource;
/*  7:   */ import org.springframework.ui.context.ThemeSource;
/*  8:   */ 
/*  9:   */ public abstract class UiApplicationContextUtils
/* 10:   */ {
/* 11:   */   public static final String THEME_SOURCE_BEAN_NAME = "themeSource";
/* 12:45 */   private static final Log logger = LogFactory.getLog(UiApplicationContextUtils.class);
/* 13:   */   
/* 14:   */   public static ThemeSource initThemeSource(ApplicationContext context)
/* 15:   */   {
/* 16:57 */     if (context.containsLocalBean("themeSource"))
/* 17:   */     {
/* 18:58 */       ThemeSource themeSource = (ThemeSource)context.getBean("themeSource", ThemeSource.class);
/* 19:60 */       if (((context.getParent() instanceof ThemeSource)) && ((themeSource instanceof HierarchicalThemeSource)))
/* 20:   */       {
/* 21:61 */         HierarchicalThemeSource hts = (HierarchicalThemeSource)themeSource;
/* 22:62 */         if (hts.getParentThemeSource() == null) {
/* 23:65 */           hts.setParentThemeSource((ThemeSource)context.getParent());
/* 24:   */         }
/* 25:   */       }
/* 26:68 */       if (logger.isDebugEnabled()) {
/* 27:69 */         logger.debug("Using ThemeSource [" + themeSource + "]");
/* 28:   */       }
/* 29:71 */       return themeSource;
/* 30:   */     }
/* 31:76 */     HierarchicalThemeSource themeSource = null;
/* 32:77 */     if ((context.getParent() instanceof ThemeSource))
/* 33:   */     {
/* 34:78 */       themeSource = new DelegatingThemeSource();
/* 35:79 */       themeSource.setParentThemeSource((ThemeSource)context.getParent());
/* 36:   */     }
/* 37:   */     else
/* 38:   */     {
/* 39:82 */       themeSource = new ResourceBundleThemeSource();
/* 40:   */     }
/* 41:84 */     if (logger.isDebugEnabled()) {
/* 42:85 */       logger.debug("Unable to locate ThemeSource with name 'themeSource': using default [" + 
/* 43:86 */         themeSource + "]");
/* 44:   */     }
/* 45:88 */     return themeSource;
/* 46:   */   }
/* 47:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ui.context.support.UiApplicationContextUtils
 * JD-Core Version:    0.7.0.1
 */