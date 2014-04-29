/*   1:    */ package org.springframework.web.servlet.support;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import java.util.Map;
/*   5:    */ import javax.servlet.ServletContext;
/*   6:    */ import javax.servlet.ServletRequest;
/*   7:    */ import javax.servlet.http.HttpServletRequest;
/*   8:    */ import org.springframework.ui.context.Theme;
/*   9:    */ import org.springframework.ui.context.ThemeSource;
/*  10:    */ import org.springframework.web.context.WebApplicationContext;
/*  11:    */ import org.springframework.web.context.support.WebApplicationContextUtils;
/*  12:    */ import org.springframework.web.servlet.DispatcherServlet;
/*  13:    */ import org.springframework.web.servlet.FlashMap;
/*  14:    */ import org.springframework.web.servlet.FlashMapManager;
/*  15:    */ import org.springframework.web.servlet.LocaleResolver;
/*  16:    */ import org.springframework.web.servlet.ThemeResolver;
/*  17:    */ 
/*  18:    */ public abstract class RequestContextUtils
/*  19:    */ {
/*  20:    */   public static WebApplicationContext getWebApplicationContext(ServletRequest request)
/*  21:    */     throws IllegalStateException
/*  22:    */   {
/*  23: 60 */     return getWebApplicationContext(request, null);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public static WebApplicationContext getWebApplicationContext(ServletRequest request, ServletContext servletContext)
/*  27:    */     throws IllegalStateException
/*  28:    */   {
/*  29: 79 */     WebApplicationContext webApplicationContext = (WebApplicationContext)request.getAttribute(
/*  30: 80 */       DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/*  31: 81 */     if (webApplicationContext == null)
/*  32:    */     {
/*  33: 82 */       if (servletContext == null) {
/*  34: 83 */         throw new IllegalStateException("No WebApplicationContext found: not in a DispatcherServlet request?");
/*  35:    */       }
/*  36: 85 */       webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
/*  37:    */     }
/*  38: 87 */     return webApplicationContext;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public static LocaleResolver getLocaleResolver(HttpServletRequest request)
/*  42:    */   {
/*  43: 97 */     return (LocaleResolver)request.getAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static Locale getLocale(HttpServletRequest request)
/*  47:    */   {
/*  48:111 */     LocaleResolver localeResolver = getLocaleResolver(request);
/*  49:112 */     if (localeResolver != null) {
/*  50:113 */       return localeResolver.resolveLocale(request);
/*  51:    */     }
/*  52:116 */     return request.getLocale();
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static ThemeResolver getThemeResolver(HttpServletRequest request)
/*  56:    */   {
/*  57:127 */     return (ThemeResolver)request.getAttribute(DispatcherServlet.THEME_RESOLVER_ATTRIBUTE);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public static ThemeSource getThemeSource(HttpServletRequest request)
/*  61:    */   {
/*  62:137 */     return (ThemeSource)request.getAttribute(DispatcherServlet.THEME_SOURCE_ATTRIBUTE);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public static Theme getTheme(HttpServletRequest request)
/*  66:    */   {
/*  67:148 */     ThemeResolver themeResolver = getThemeResolver(request);
/*  68:149 */     ThemeSource themeSource = getThemeSource(request);
/*  69:150 */     if ((themeResolver != null) && (themeSource != null))
/*  70:    */     {
/*  71:151 */       String themeName = themeResolver.resolveThemeName(request);
/*  72:152 */       return themeSource.getTheme(themeName);
/*  73:    */     }
/*  74:155 */     return null;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static Map<String, ?> getInputFlashMap(HttpServletRequest request)
/*  78:    */   {
/*  79:168 */     return (Map)request.getAttribute(FlashMapManager.INPUT_FLASH_MAP_ATTRIBUTE);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public static FlashMap getOutputFlashMap(HttpServletRequest request)
/*  83:    */   {
/*  84:178 */     return (FlashMap)request.getAttribute(FlashMapManager.OUTPUT_FLASH_MAP_ATTRIBUTE);
/*  85:    */   }
/*  86:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.support.RequestContextUtils
 * JD-Core Version:    0.7.0.1
 */