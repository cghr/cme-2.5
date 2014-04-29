/*   1:    */ package org.springframework.ui.context.support;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.apache.commons.logging.Log;
/*   6:    */ import org.apache.commons.logging.LogFactory;
/*   7:    */ import org.springframework.context.HierarchicalMessageSource;
/*   8:    */ import org.springframework.context.MessageSource;
/*   9:    */ import org.springframework.context.support.ResourceBundleMessageSource;
/*  10:    */ import org.springframework.ui.context.HierarchicalThemeSource;
/*  11:    */ import org.springframework.ui.context.Theme;
/*  12:    */ import org.springframework.ui.context.ThemeSource;
/*  13:    */ 
/*  14:    */ public class ResourceBundleThemeSource
/*  15:    */   implements HierarchicalThemeSource
/*  16:    */ {
/*  17: 46 */   protected final Log logger = LogFactory.getLog(getClass());
/*  18:    */   private ThemeSource parentThemeSource;
/*  19: 50 */   private String basenamePrefix = "";
/*  20: 53 */   private final Map<String, Theme> themeCache = new HashMap();
/*  21:    */   
/*  22:    */   public void setParentThemeSource(ThemeSource parent)
/*  23:    */   {
/*  24: 57 */     this.parentThemeSource = parent;
/*  25: 61 */     synchronized (this.themeCache)
/*  26:    */     {
/*  27: 62 */       for (Theme theme : this.themeCache.values()) {
/*  28: 63 */         initParent(theme);
/*  29:    */       }
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   public ThemeSource getParentThemeSource()
/*  34:    */   {
/*  35: 69 */     return this.parentThemeSource;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setBasenamePrefix(String basenamePrefix)
/*  39:    */   {
/*  40: 83 */     this.basenamePrefix = (basenamePrefix != null ? basenamePrefix : "");
/*  41:    */   }
/*  42:    */   
/*  43:    */   public Theme getTheme(String themeName)
/*  44:    */   {
/*  45: 97 */     if (themeName == null) {
/*  46: 98 */       return null;
/*  47:    */     }
/*  48:100 */     synchronized (this.themeCache)
/*  49:    */     {
/*  50:101 */       Theme theme = (Theme)this.themeCache.get(themeName);
/*  51:102 */       if (theme == null)
/*  52:    */       {
/*  53:103 */         String basename = this.basenamePrefix + themeName;
/*  54:104 */         MessageSource messageSource = createMessageSource(basename);
/*  55:105 */         theme = new SimpleTheme(themeName, messageSource);
/*  56:106 */         initParent(theme);
/*  57:107 */         this.themeCache.put(themeName, theme);
/*  58:108 */         if (this.logger.isDebugEnabled()) {
/*  59:109 */           this.logger.debug("Theme created: name '" + themeName + "', basename [" + basename + "]");
/*  60:    */         }
/*  61:    */       }
/*  62:112 */       return theme;
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected MessageSource createMessageSource(String basename)
/*  67:    */   {
/*  68:128 */     ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
/*  69:129 */     messageSource.setBasename(basename);
/*  70:130 */     return messageSource;
/*  71:    */   }
/*  72:    */   
/*  73:    */   protected void initParent(Theme theme)
/*  74:    */   {
/*  75:139 */     if ((theme.getMessageSource() instanceof HierarchicalMessageSource))
/*  76:    */     {
/*  77:140 */       HierarchicalMessageSource messageSource = (HierarchicalMessageSource)theme.getMessageSource();
/*  78:141 */       if ((getParentThemeSource() != null) && (messageSource.getParentMessageSource() == null))
/*  79:    */       {
/*  80:142 */         Theme parentTheme = getParentThemeSource().getTheme(theme.getName());
/*  81:143 */         if (parentTheme != null) {
/*  82:144 */           messageSource.setParentMessageSource(parentTheme.getMessageSource());
/*  83:    */         }
/*  84:    */       }
/*  85:    */     }
/*  86:    */   }
/*  87:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ui.context.support.ResourceBundleThemeSource
 * JD-Core Version:    0.7.0.1
 */