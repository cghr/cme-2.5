/*  1:   */ package org.springframework.core.type.classreading;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import org.springframework.core.io.DefaultResourceLoader;
/*  5:   */ import org.springframework.core.io.Resource;
/*  6:   */ import org.springframework.core.io.ResourceLoader;
/*  7:   */ import org.springframework.util.ClassUtils;
/*  8:   */ 
/*  9:   */ public class SimpleMetadataReaderFactory
/* 10:   */   implements MetadataReaderFactory
/* 11:   */ {
/* 12:   */   private final ResourceLoader resourceLoader;
/* 13:   */   
/* 14:   */   public SimpleMetadataReaderFactory()
/* 15:   */   {
/* 16:42 */     this.resourceLoader = new DefaultResourceLoader();
/* 17:   */   }
/* 18:   */   
/* 19:   */   public SimpleMetadataReaderFactory(ResourceLoader resourceLoader)
/* 20:   */   {
/* 21:51 */     this.resourceLoader = (resourceLoader != null ? resourceLoader : new DefaultResourceLoader());
/* 22:   */   }
/* 23:   */   
/* 24:   */   public SimpleMetadataReaderFactory(ClassLoader classLoader)
/* 25:   */   {
/* 26:59 */     this.resourceLoader = 
/* 27:60 */       (classLoader != null ? new DefaultResourceLoader(classLoader) : new DefaultResourceLoader());
/* 28:   */   }
/* 29:   */   
/* 30:   */   public final ResourceLoader getResourceLoader()
/* 31:   */   {
/* 32:69 */     return this.resourceLoader;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public MetadataReader getMetadataReader(String className)
/* 36:   */     throws IOException
/* 37:   */   {
/* 38:74 */     String resourcePath = "classpath:" + 
/* 39:75 */       ClassUtils.convertClassNameToResourcePath(className) + ".class";
/* 40:76 */     return getMetadataReader(this.resourceLoader.getResource(resourcePath));
/* 41:   */   }
/* 42:   */   
/* 43:   */   public MetadataReader getMetadataReader(Resource resource)
/* 44:   */     throws IOException
/* 45:   */   {
/* 46:80 */     return new SimpleMetadataReader(resource, this.resourceLoader.getClassLoader());
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.classreading.SimpleMetadataReaderFactory
 * JD-Core Version:    0.7.0.1
 */