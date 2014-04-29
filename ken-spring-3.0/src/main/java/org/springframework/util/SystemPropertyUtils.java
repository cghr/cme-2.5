/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ 
/*   5:    */ public abstract class SystemPropertyUtils
/*   6:    */ {
/*   7:    */   public static final String PLACEHOLDER_PREFIX = "${";
/*   8:    */   public static final String PLACEHOLDER_SUFFIX = "}";
/*   9:    */   public static final String VALUE_SEPARATOR = ":";
/*  10: 49 */   private static final PropertyPlaceholderHelper strictHelper = new PropertyPlaceholderHelper("${", "}", ":", false);
/*  11: 52 */   private static final PropertyPlaceholderHelper nonStrictHelper = new PropertyPlaceholderHelper("${", "}", ":", true);
/*  12:    */   
/*  13:    */   public static String resolvePlaceholders(String text)
/*  14:    */   {
/*  15: 64 */     return resolvePlaceholders(text, false);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public static String resolvePlaceholders(String text, boolean ignoreUnresolvablePlaceholders)
/*  19:    */   {
/*  20: 79 */     PropertyPlaceholderHelper helper = ignoreUnresolvablePlaceholders ? nonStrictHelper : strictHelper;
/*  21: 80 */     return helper.replacePlaceholders(text, new SystemPropertyPlaceholderResolver(text));
/*  22:    */   }
/*  23:    */   
/*  24:    */   private static class SystemPropertyPlaceholderResolver
/*  25:    */     implements PropertyPlaceholderHelper.PlaceholderResolver
/*  26:    */   {
/*  27:    */     private final String text;
/*  28:    */     
/*  29:    */     public SystemPropertyPlaceholderResolver(String text)
/*  30:    */     {
/*  31: 89 */       this.text = text;
/*  32:    */     }
/*  33:    */     
/*  34:    */     public String resolvePlaceholder(String placeholderName)
/*  35:    */     {
/*  36:    */       try
/*  37:    */       {
/*  38: 94 */         String propVal = System.getProperty(placeholderName);
/*  39: 95 */         if (propVal == null) {}
/*  40: 97 */         return System.getenv(placeholderName);
/*  41:    */       }
/*  42:    */       catch (Throwable ex)
/*  43:    */       {
/*  44:102 */         System.err.println("Could not resolve placeholder '" + placeholderName + "' in [" + 
/*  45:103 */           this.text + "] as system property: " + ex);
/*  46:    */       }
/*  47:104 */       return null;
/*  48:    */     }
/*  49:    */   }
/*  50:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.SystemPropertyUtils
 * JD-Core Version:    0.7.0.1
 */