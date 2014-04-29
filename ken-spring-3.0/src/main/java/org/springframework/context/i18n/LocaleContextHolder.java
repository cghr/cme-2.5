/*   1:    */ package org.springframework.context.i18n;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import org.springframework.core.NamedInheritableThreadLocal;
/*   5:    */ import org.springframework.core.NamedThreadLocal;
/*   6:    */ 
/*   7:    */ public abstract class LocaleContextHolder
/*   8:    */ {
/*   9: 45 */   private static final ThreadLocal<LocaleContext> localeContextHolder = new NamedThreadLocal("Locale context");
/*  10: 48 */   private static final ThreadLocal<LocaleContext> inheritableLocaleContextHolder = new NamedInheritableThreadLocal("Locale context");
/*  11:    */   
/*  12:    */   public static void resetLocaleContext()
/*  13:    */   {
/*  14: 55 */     localeContextHolder.remove();
/*  15: 56 */     inheritableLocaleContextHolder.remove();
/*  16:    */   }
/*  17:    */   
/*  18:    */   public static void setLocaleContext(LocaleContext localeContext)
/*  19:    */   {
/*  20: 65 */     setLocaleContext(localeContext, false);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public static void setLocaleContext(LocaleContext localeContext, boolean inheritable)
/*  24:    */   {
/*  25: 76 */     if (localeContext == null)
/*  26:    */     {
/*  27: 77 */       resetLocaleContext();
/*  28:    */     }
/*  29: 80 */     else if (inheritable)
/*  30:    */     {
/*  31: 81 */       inheritableLocaleContextHolder.set(localeContext);
/*  32: 82 */       localeContextHolder.remove();
/*  33:    */     }
/*  34:    */     else
/*  35:    */     {
/*  36: 85 */       localeContextHolder.set(localeContext);
/*  37: 86 */       inheritableLocaleContextHolder.remove();
/*  38:    */     }
/*  39:    */   }
/*  40:    */   
/*  41:    */   public static LocaleContext getLocaleContext()
/*  42:    */   {
/*  43: 96 */     LocaleContext localeContext = (LocaleContext)localeContextHolder.get();
/*  44: 97 */     if (localeContext == null) {
/*  45: 98 */       localeContext = (LocaleContext)inheritableLocaleContextHolder.get();
/*  46:    */     }
/*  47:100 */     return localeContext;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public static void setLocale(Locale locale)
/*  51:    */   {
/*  52:112 */     setLocale(locale, false);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static void setLocale(Locale locale, boolean inheritable)
/*  56:    */   {
/*  57:125 */     LocaleContext localeContext = locale != null ? new SimpleLocaleContext(locale) : null;
/*  58:126 */     setLocaleContext(localeContext, inheritable);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public static Locale getLocale()
/*  62:    */   {
/*  63:138 */     LocaleContext localeContext = getLocaleContext();
/*  64:139 */     return localeContext != null ? localeContext.getLocale() : Locale.getDefault();
/*  65:    */   }
/*  66:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.i18n.LocaleContextHolder
 * JD-Core Version:    0.7.0.1
 */