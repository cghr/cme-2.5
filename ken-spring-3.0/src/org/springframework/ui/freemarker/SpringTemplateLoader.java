/*  1:   */ package org.springframework.ui.freemarker;
/*  2:   */ 
/*  3:   */ import freemarker.cache.TemplateLoader;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.InputStreamReader;
/*  6:   */ import java.io.Reader;
/*  7:   */ import org.apache.commons.logging.Log;
/*  8:   */ import org.apache.commons.logging.LogFactory;
/*  9:   */ import org.springframework.core.io.Resource;
/* 10:   */ import org.springframework.core.io.ResourceLoader;
/* 11:   */ 
/* 12:   */ public class SpringTemplateLoader
/* 13:   */   implements TemplateLoader
/* 14:   */ {
/* 15:42 */   protected final Log logger = LogFactory.getLog(getClass());
/* 16:   */   private final ResourceLoader resourceLoader;
/* 17:   */   private final String templateLoaderPath;
/* 18:   */   
/* 19:   */   public SpringTemplateLoader(ResourceLoader resourceLoader, String templateLoaderPath)
/* 20:   */   {
/* 21:55 */     this.resourceLoader = resourceLoader;
/* 22:56 */     if (!templateLoaderPath.endsWith("/")) {
/* 23:57 */       templateLoaderPath = templateLoaderPath + "/";
/* 24:   */     }
/* 25:59 */     this.templateLoaderPath = templateLoaderPath;
/* 26:60 */     if (this.logger.isInfoEnabled()) {
/* 27:61 */       this.logger.info("SpringTemplateLoader for FreeMarker: using resource loader [" + this.resourceLoader + 
/* 28:62 */         "] and template loader path [" + this.templateLoaderPath + "]");
/* 29:   */     }
/* 30:   */   }
/* 31:   */   
/* 32:   */   public Object findTemplateSource(String name)
/* 33:   */     throws IOException
/* 34:   */   {
/* 35:67 */     if (this.logger.isDebugEnabled()) {
/* 36:68 */       this.logger.debug("Looking for FreeMarker template with name [" + name + "]");
/* 37:   */     }
/* 38:70 */     Resource resource = this.resourceLoader.getResource(this.templateLoaderPath + name);
/* 39:71 */     return resource.exists() ? resource : null;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public Reader getReader(Object templateSource, String encoding)
/* 43:   */     throws IOException
/* 44:   */   {
/* 45:75 */     Resource resource = (Resource)templateSource;
/* 46:   */     try
/* 47:   */     {
/* 48:77 */       return new InputStreamReader(resource.getInputStream(), encoding);
/* 49:   */     }
/* 50:   */     catch (IOException ex)
/* 51:   */     {
/* 52:80 */       if (this.logger.isDebugEnabled()) {
/* 53:81 */         this.logger.debug("Could not find FreeMarker template: " + resource);
/* 54:   */       }
/* 55:83 */       throw ex;
/* 56:   */     }
/* 57:   */   }
/* 58:   */   
/* 59:   */   public long getLastModified(Object templateSource)
/* 60:   */   {
/* 61:89 */     Resource resource = (Resource)templateSource;
/* 62:   */     try
/* 63:   */     {
/* 64:91 */       return resource.lastModified();
/* 65:   */     }
/* 66:   */     catch (IOException ex)
/* 67:   */     {
/* 68:94 */       if (this.logger.isDebugEnabled()) {
/* 69:95 */         this.logger.debug("Could not obtain last-modified timestamp for FreeMarker template in " + 
/* 70:96 */           resource + ": " + ex);
/* 71:   */       }
/* 72:   */     }
/* 73:98 */     return -1L;
/* 74:   */   }
/* 75:   */   
/* 76:   */   public void closeTemplateSource(Object templateSource)
/* 77:   */     throws IOException
/* 78:   */   {}
/* 79:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ui.freemarker.SpringTemplateLoader
 * JD-Core Version:    0.7.0.1
 */