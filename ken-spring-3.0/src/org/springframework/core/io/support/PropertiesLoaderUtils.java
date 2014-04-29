/*   1:    */ package org.springframework.core.io.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.net.URL;
/*   6:    */ import java.net.URLConnection;
/*   7:    */ import java.util.Enumeration;
/*   8:    */ import java.util.Properties;
/*   9:    */ import org.springframework.core.io.Resource;
/*  10:    */ import org.springframework.util.Assert;
/*  11:    */ import org.springframework.util.ClassUtils;
/*  12:    */ 
/*  13:    */ public abstract class PropertiesLoaderUtils
/*  14:    */ {
/*  15:    */   public static Properties loadProperties(Resource resource)
/*  16:    */     throws IOException
/*  17:    */   {
/*  18: 51 */     Properties props = new Properties();
/*  19: 52 */     fillProperties(props, resource);
/*  20: 53 */     return props;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public static void fillProperties(Properties props, Resource resource)
/*  24:    */     throws IOException
/*  25:    */   {
/*  26: 63 */     InputStream is = resource.getInputStream();
/*  27:    */     try
/*  28:    */     {
/*  29: 65 */       props.load(is);
/*  30:    */     }
/*  31:    */     finally
/*  32:    */     {
/*  33: 68 */       is.close();
/*  34:    */     }
/*  35:    */   }
/*  36:    */   
/*  37:    */   public static Properties loadAllProperties(String resourceName)
/*  38:    */     throws IOException
/*  39:    */   {
/*  40: 82 */     return loadAllProperties(resourceName, null);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public static Properties loadAllProperties(String resourceName, ClassLoader classLoader)
/*  44:    */     throws IOException
/*  45:    */   {
/*  46: 97 */     Assert.notNull(resourceName, "Resource name must not be null");
/*  47: 98 */     ClassLoader clToUse = classLoader;
/*  48: 99 */     if (clToUse == null) {
/*  49:100 */       clToUse = ClassUtils.getDefaultClassLoader();
/*  50:    */     }
/*  51:102 */     Properties properties = new Properties();
/*  52:103 */     Enumeration urls = clToUse.getResources(resourceName);
/*  53:104 */     while (urls.hasMoreElements())
/*  54:    */     {
/*  55:105 */       URL url = (URL)urls.nextElement();
/*  56:106 */       InputStream is = null;
/*  57:    */       try
/*  58:    */       {
/*  59:108 */         URLConnection con = url.openConnection();
/*  60:109 */         con.setUseCaches(false);
/*  61:110 */         is = con.getInputStream();
/*  62:111 */         properties.load(is);
/*  63:    */       }
/*  64:    */       finally
/*  65:    */       {
/*  66:114 */         if (is != null) {
/*  67:115 */           is.close();
/*  68:    */         }
/*  69:    */       }
/*  70:    */     }
/*  71:119 */     return properties;
/*  72:    */   }
/*  73:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.support.PropertiesLoaderUtils
 * JD-Core Version:    0.7.0.1
 */