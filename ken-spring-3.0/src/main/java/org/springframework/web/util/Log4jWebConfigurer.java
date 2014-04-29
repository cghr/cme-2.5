/*   1:    */ package org.springframework.web.util;
/*   2:    */ 
/*   3:    */ import java.io.FileNotFoundException;
/*   4:    */ import javax.servlet.ServletContext;
/*   5:    */ import org.springframework.util.Log4jConfigurer;
/*   6:    */ import org.springframework.util.ResourceUtils;
/*   7:    */ import org.springframework.util.SystemPropertyUtils;
/*   8:    */ 
/*   9:    */ public abstract class Log4jWebConfigurer
/*  10:    */ {
/*  11:    */   public static final String CONFIG_LOCATION_PARAM = "log4jConfigLocation";
/*  12:    */   public static final String REFRESH_INTERVAL_PARAM = "log4jRefreshInterval";
/*  13:    */   public static final String EXPOSE_WEB_APP_ROOT_PARAM = "log4jExposeWebAppRoot";
/*  14:    */   
/*  15:    */   public static void initLogging(ServletContext servletContext)
/*  16:    */   {
/*  17:116 */     if (exposeWebAppRoot(servletContext)) {
/*  18:117 */       WebUtils.setWebAppRootSystemProperty(servletContext);
/*  19:    */     }
/*  20:121 */     String location = servletContext.getInitParameter("log4jConfigLocation");
/*  21:122 */     if (location != null) {
/*  22:    */       try
/*  23:    */       {
/*  24:127 */         if (!ResourceUtils.isUrl(location))
/*  25:    */         {
/*  26:129 */           location = SystemPropertyUtils.resolvePlaceholders(location);
/*  27:130 */           location = WebUtils.getRealPath(servletContext, location);
/*  28:    */         }
/*  29:134 */         servletContext.log("Initializing log4j from [" + location + "]");
/*  30:    */         
/*  31:    */ 
/*  32:137 */         String intervalString = servletContext.getInitParameter("log4jRefreshInterval");
/*  33:138 */         if (intervalString != null) {
/*  34:    */           try
/*  35:    */           {
/*  36:142 */             long refreshInterval = Long.parseLong(intervalString);
/*  37:143 */             Log4jConfigurer.initLogging(location, refreshInterval);
/*  38:    */           }
/*  39:    */           catch (NumberFormatException ex)
/*  40:    */           {
/*  41:146 */             throw new IllegalArgumentException("Invalid 'log4jRefreshInterval' parameter: " + ex.getMessage());
/*  42:    */           }
/*  43:    */         } else {
/*  44:151 */           Log4jConfigurer.initLogging(location);
/*  45:    */         }
/*  46:    */       }
/*  47:    */       catch (FileNotFoundException ex)
/*  48:    */       {
/*  49:155 */         throw new IllegalArgumentException("Invalid 'log4jConfigLocation' parameter: " + ex.getMessage());
/*  50:    */       }
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   public static void shutdownLogging(ServletContext servletContext)
/*  55:    */   {
/*  56:167 */     servletContext.log("Shutting down log4j");
/*  57:    */     try
/*  58:    */     {
/*  59:169 */       Log4jConfigurer.shutdownLogging();
/*  60:    */     }
/*  61:    */     finally
/*  62:    */     {
/*  63:173 */       if (exposeWebAppRoot(servletContext)) {
/*  64:174 */         WebUtils.removeWebAppRootSystemProperty(servletContext);
/*  65:    */       }
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   private static boolean exposeWebAppRoot(ServletContext servletContext)
/*  70:    */   {
/*  71:185 */     String exposeWebAppRootParam = servletContext.getInitParameter("log4jExposeWebAppRoot");
/*  72:186 */     return (exposeWebAppRootParam == null) || (Boolean.valueOf(exposeWebAppRootParam).booleanValue());
/*  73:    */   }
/*  74:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.util.Log4jWebConfigurer
 * JD-Core Version:    0.7.0.1
 */