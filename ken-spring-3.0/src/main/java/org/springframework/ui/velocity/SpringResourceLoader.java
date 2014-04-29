/*   1:    */ package org.springframework.ui.velocity;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.util.Arrays;
/*   6:    */ import org.apache.commons.collections.ExtendedProperties;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.apache.velocity.exception.ResourceNotFoundException;
/*  10:    */ import org.apache.velocity.runtime.RuntimeServices;
/*  11:    */ import org.springframework.util.StringUtils;
/*  12:    */ 
/*  13:    */ public class SpringResourceLoader
/*  14:    */   extends org.apache.velocity.runtime.resource.loader.ResourceLoader
/*  15:    */ {
/*  16:    */   public static final String NAME = "spring";
/*  17:    */   public static final String SPRING_RESOURCE_LOADER_CLASS = "spring.resource.loader.class";
/*  18:    */   public static final String SPRING_RESOURCE_LOADER_CACHE = "spring.resource.loader.cache";
/*  19:    */   public static final String SPRING_RESOURCE_LOADER = "spring.resource.loader";
/*  20:    */   public static final String SPRING_RESOURCE_LOADER_PATH = "spring.resource.loader.path";
/*  21: 64 */   protected final Log logger = LogFactory.getLog(getClass());
/*  22:    */   private org.springframework.core.io.ResourceLoader resourceLoader;
/*  23:    */   private String[] resourceLoaderPaths;
/*  24:    */   
/*  25:    */   public void init(ExtendedProperties configuration)
/*  26:    */   {
/*  27: 73 */     this.resourceLoader = ((org.springframework.core.io.ResourceLoader)
/*  28: 74 */       this.rsvc.getApplicationAttribute("spring.resource.loader"));
/*  29: 75 */     String resourceLoaderPath = (String)this.rsvc.getApplicationAttribute("spring.resource.loader.path");
/*  30: 76 */     if (this.resourceLoader == null) {
/*  31: 77 */       throw new IllegalArgumentException(
/*  32: 78 */         "'resourceLoader' application attribute must be present for SpringResourceLoader");
/*  33:    */     }
/*  34: 80 */     if (resourceLoaderPath == null) {
/*  35: 81 */       throw new IllegalArgumentException(
/*  36: 82 */         "'resourceLoaderPath' application attribute must be present for SpringResourceLoader");
/*  37:    */     }
/*  38: 84 */     this.resourceLoaderPaths = StringUtils.commaDelimitedListToStringArray(resourceLoaderPath);
/*  39: 85 */     for (int i = 0; i < this.resourceLoaderPaths.length; i++)
/*  40:    */     {
/*  41: 86 */       String path = this.resourceLoaderPaths[i];
/*  42: 87 */       if (!path.endsWith("/")) {
/*  43: 88 */         this.resourceLoaderPaths[i] = (path + "/");
/*  44:    */       }
/*  45:    */     }
/*  46: 91 */     if (this.logger.isInfoEnabled()) {
/*  47: 92 */       this.logger.info("SpringResourceLoader for Velocity: using resource loader [" + this.resourceLoader + 
/*  48: 93 */         "] and resource loader paths " + Arrays.asList(this.resourceLoaderPaths));
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   public InputStream getResourceStream(String source)
/*  53:    */     throws ResourceNotFoundException
/*  54:    */   {
/*  55: 99 */     if (this.logger.isDebugEnabled()) {
/*  56:100 */       this.logger.debug("Looking for Velocity resource with name [" + source + "]");
/*  57:    */     }
/*  58:102 */     for (int i = 0; i < this.resourceLoaderPaths.length; i++)
/*  59:    */     {
/*  60:103 */       org.springframework.core.io.Resource resource = 
/*  61:104 */         this.resourceLoader.getResource(this.resourceLoaderPaths[i] + source);
/*  62:    */       try
/*  63:    */       {
/*  64:106 */         return resource.getInputStream();
/*  65:    */       }
/*  66:    */       catch (IOException localIOException)
/*  67:    */       {
/*  68:109 */         if (this.logger.isDebugEnabled()) {
/*  69:110 */           this.logger.debug("Could not find Velocity resource: " + resource);
/*  70:    */         }
/*  71:    */       }
/*  72:    */     }
/*  73:114 */     throw new ResourceNotFoundException(
/*  74:115 */       "Could not find resource [" + source + "] in Spring resource loader path");
/*  75:    */   }
/*  76:    */   
/*  77:    */   public boolean isSourceModified(org.apache.velocity.runtime.resource.Resource resource)
/*  78:    */   {
/*  79:120 */     return false;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public long getLastModified(org.apache.velocity.runtime.resource.Resource resource)
/*  83:    */   {
/*  84:125 */     return 0L;
/*  85:    */   }
/*  86:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ui.velocity.SpringResourceLoader
 * JD-Core Version:    0.7.0.1
 */