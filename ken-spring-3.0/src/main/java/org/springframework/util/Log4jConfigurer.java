/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileNotFoundException;
/*   5:    */ import java.net.URL;
/*   6:    */ import org.apache.log4j.LogManager;
/*   7:    */ import org.apache.log4j.PropertyConfigurator;
/*   8:    */ import org.apache.log4j.xml.DOMConfigurator;
/*   9:    */ 
/*  10:    */ public abstract class Log4jConfigurer
/*  11:    */ {
/*  12:    */   public static final String CLASSPATH_URL_PREFIX = "classpath:";
/*  13:    */   public static final String XML_FILE_EXTENSION = ".xml";
/*  14:    */   
/*  15:    */   public static void initLogging(String location)
/*  16:    */     throws FileNotFoundException
/*  17:    */   {
/*  18: 66 */     String resolvedLocation = SystemPropertyUtils.resolvePlaceholders(location);
/*  19: 67 */     URL url = ResourceUtils.getURL(resolvedLocation);
/*  20: 68 */     if (resolvedLocation.toLowerCase().endsWith(".xml")) {
/*  21: 69 */       DOMConfigurator.configure(url);
/*  22:    */     } else {
/*  23: 72 */       PropertyConfigurator.configure(url);
/*  24:    */     }
/*  25:    */   }
/*  26:    */   
/*  27:    */   public static void initLogging(String location, long refreshInterval)
/*  28:    */     throws FileNotFoundException
/*  29:    */   {
/*  30: 96 */     String resolvedLocation = SystemPropertyUtils.resolvePlaceholders(location);
/*  31: 97 */     File file = ResourceUtils.getFile(resolvedLocation);
/*  32: 98 */     if (!file.exists()) {
/*  33: 99 */       throw new FileNotFoundException("Log4j config file [" + resolvedLocation + "] not found");
/*  34:    */     }
/*  35:101 */     if (resolvedLocation.toLowerCase().endsWith(".xml")) {
/*  36:102 */       DOMConfigurator.configureAndWatch(file.getAbsolutePath(), refreshInterval);
/*  37:    */     } else {
/*  38:105 */       PropertyConfigurator.configureAndWatch(file.getAbsolutePath(), refreshInterval);
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public static void shutdownLogging() {}
/*  43:    */   
/*  44:    */   public static void setWorkingDirSystemProperty(String key)
/*  45:    */   {
/*  46:128 */     System.setProperty(key, new File("").getAbsolutePath());
/*  47:    */   }
/*  48:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.Log4jConfigurer
 * JD-Core Version:    0.7.0.1
 */