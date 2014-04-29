/*   1:    */ package org.springframework.beans.propertyeditors;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditorSupport;
/*   4:    */ import java.util.Locale;
/*   5:    */ import java.util.ResourceBundle;
/*   6:    */ import org.springframework.util.Assert;
/*   7:    */ import org.springframework.util.StringUtils;
/*   8:    */ 
/*   9:    */ public class ResourceBundleEditor
/*  10:    */   extends PropertyEditorSupport
/*  11:    */ {
/*  12:    */   public static final String BASE_NAME_SEPARATOR = "_";
/*  13:    */   
/*  14:    */   public void setAsText(String text)
/*  15:    */     throws IllegalArgumentException
/*  16:    */   {
/*  17: 84 */     Assert.hasText(text, "'text' must not be empty");
/*  18:    */     
/*  19: 86 */     String rawBaseName = text.trim();
/*  20: 87 */     int indexOfBaseNameSeparator = rawBaseName.indexOf("_");
/*  21:    */     ResourceBundle bundle;
/*  22:    */     ResourceBundle bundle;
/*  23: 88 */     if (indexOfBaseNameSeparator == -1)
/*  24:    */     {
/*  25: 89 */       bundle = ResourceBundle.getBundle(rawBaseName);
/*  26:    */     }
/*  27:    */     else
/*  28:    */     {
/*  29: 92 */       String baseName = rawBaseName.substring(0, indexOfBaseNameSeparator);
/*  30: 93 */       if (!StringUtils.hasText(baseName)) {
/*  31: 94 */         throw new IllegalArgumentException("Bad ResourceBundle name : received '" + text + "' as argument to 'setAsText(String value)'.");
/*  32:    */       }
/*  33: 96 */       String localeString = rawBaseName.substring(indexOfBaseNameSeparator + 1);
/*  34: 97 */       Locale locale = StringUtils.parseLocaleString(localeString);
/*  35: 98 */       bundle = StringUtils.hasText(localeString) ? 
/*  36: 99 */         ResourceBundle.getBundle(baseName, locale) : 
/*  37:100 */         ResourceBundle.getBundle(baseName);
/*  38:    */     }
/*  39:102 */     setValue(bundle);
/*  40:    */   }
/*  41:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.ResourceBundleEditor
 * JD-Core Version:    0.7.0.1
 */