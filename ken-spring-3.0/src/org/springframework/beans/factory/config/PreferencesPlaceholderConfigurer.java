/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import java.util.Properties;
/*   4:    */ import java.util.prefs.BackingStoreException;
/*   5:    */ import java.util.prefs.Preferences;
/*   6:    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*   7:    */ import org.springframework.beans.factory.InitializingBean;
/*   8:    */ 
/*   9:    */ public class PreferencesPlaceholderConfigurer
/*  10:    */   extends PropertyPlaceholderConfigurer
/*  11:    */   implements InitializingBean
/*  12:    */ {
/*  13:    */   private String systemTreePath;
/*  14:    */   private String userTreePath;
/*  15:    */   private Preferences systemPrefs;
/*  16:    */   private Preferences userPrefs;
/*  17:    */   
/*  18:    */   public void setSystemTreePath(String systemTreePath)
/*  19:    */   {
/*  20: 61 */     this.systemTreePath = systemTreePath;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void setUserTreePath(String userTreePath)
/*  24:    */   {
/*  25: 69 */     this.userTreePath = userTreePath;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void afterPropertiesSet()
/*  29:    */   {
/*  30: 78 */     this.systemPrefs = (this.systemTreePath != null ? 
/*  31: 79 */       Preferences.systemRoot().node(this.systemTreePath) : Preferences.systemRoot());
/*  32: 80 */     this.userPrefs = (this.userTreePath != null ? 
/*  33: 81 */       Preferences.userRoot().node(this.userTreePath) : Preferences.userRoot());
/*  34:    */   }
/*  35:    */   
/*  36:    */   protected String resolvePlaceholder(String placeholder, Properties props)
/*  37:    */   {
/*  38: 91 */     String path = null;
/*  39: 92 */     String key = placeholder;
/*  40: 93 */     int endOfPath = placeholder.lastIndexOf('/');
/*  41: 94 */     if (endOfPath != -1)
/*  42:    */     {
/*  43: 95 */       path = placeholder.substring(0, endOfPath);
/*  44: 96 */       key = placeholder.substring(endOfPath + 1);
/*  45:    */     }
/*  46: 98 */     String value = resolvePlaceholder(path, key, this.userPrefs);
/*  47: 99 */     if (value == null)
/*  48:    */     {
/*  49:100 */       value = resolvePlaceholder(path, key, this.systemPrefs);
/*  50:101 */       if (value == null) {
/*  51:102 */         value = props.getProperty(placeholder);
/*  52:    */       }
/*  53:    */     }
/*  54:105 */     return value;
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected String resolvePlaceholder(String path, String key, Preferences preferences)
/*  58:    */   {
/*  59:116 */     if (path != null) {
/*  60:    */       try
/*  61:    */       {
/*  62:119 */         if (preferences.nodeExists(path)) {
/*  63:120 */           return preferences.node(path).get(key, null);
/*  64:    */         }
/*  65:123 */         return null;
/*  66:    */       }
/*  67:    */       catch (BackingStoreException ex)
/*  68:    */       {
/*  69:127 */         throw new BeanDefinitionStoreException("Cannot access specified node path [" + path + "]", ex);
/*  70:    */       }
/*  71:    */     }
/*  72:131 */     return preferences.get(key, null);
/*  73:    */   }
/*  74:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer
 * JD-Core Version:    0.7.0.1
 */