/*   1:    */ package org.springframework.scheduling.quartz;
/*   2:    */ 
/*   3:    */ import java.io.FileNotFoundException;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.net.URL;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.quartz.spi.ClassLoadHelper;
/*  10:    */ import org.springframework.core.io.DefaultResourceLoader;
/*  11:    */ import org.springframework.core.io.Resource;
/*  12:    */ import org.springframework.core.io.ResourceLoader;
/*  13:    */ 
/*  14:    */ public class ResourceLoaderClassLoadHelper
/*  15:    */   implements ClassLoadHelper
/*  16:    */ {
/*  17: 43 */   protected static final Log logger = LogFactory.getLog(ResourceLoaderClassLoadHelper.class);
/*  18:    */   private ResourceLoader resourceLoader;
/*  19:    */   
/*  20:    */   public ResourceLoaderClassLoadHelper() {}
/*  21:    */   
/*  22:    */   public ResourceLoaderClassLoadHelper(ResourceLoader resourceLoader)
/*  23:    */   {
/*  24: 61 */     this.resourceLoader = resourceLoader;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void initialize()
/*  28:    */   {
/*  29: 66 */     if (this.resourceLoader == null)
/*  30:    */     {
/*  31: 67 */       this.resourceLoader = SchedulerFactoryBean.getConfigTimeResourceLoader();
/*  32: 68 */       if (this.resourceLoader == null) {
/*  33: 69 */         this.resourceLoader = new DefaultResourceLoader();
/*  34:    */       }
/*  35:    */     }
/*  36:    */   }
/*  37:    */   
/*  38:    */   public Class loadClass(String name)
/*  39:    */     throws ClassNotFoundException
/*  40:    */   {
/*  41: 75 */     return this.resourceLoader.getClassLoader().loadClass(name);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public URL getResource(String name)
/*  45:    */   {
/*  46: 79 */     Resource resource = this.resourceLoader.getResource(name);
/*  47:    */     try
/*  48:    */     {
/*  49: 81 */       return resource.getURL();
/*  50:    */     }
/*  51:    */     catch (FileNotFoundException localFileNotFoundException)
/*  52:    */     {
/*  53: 84 */       return null;
/*  54:    */     }
/*  55:    */     catch (IOException localIOException)
/*  56:    */     {
/*  57: 87 */       logger.warn("Could not load " + resource);
/*  58:    */     }
/*  59: 88 */     return null;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public InputStream getResourceAsStream(String name)
/*  63:    */   {
/*  64: 93 */     Resource resource = this.resourceLoader.getResource(name);
/*  65:    */     try
/*  66:    */     {
/*  67: 95 */       return resource.getInputStream();
/*  68:    */     }
/*  69:    */     catch (FileNotFoundException localFileNotFoundException)
/*  70:    */     {
/*  71: 98 */       return null;
/*  72:    */     }
/*  73:    */     catch (IOException localIOException)
/*  74:    */     {
/*  75:101 */       logger.warn("Could not load " + resource);
/*  76:    */     }
/*  77:102 */     return null;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public ClassLoader getClassLoader()
/*  81:    */   {
/*  82:107 */     return this.resourceLoader.getClassLoader();
/*  83:    */   }
/*  84:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.quartz.ResourceLoaderClassLoadHelper
 * JD-Core Version:    0.7.0.1
 */