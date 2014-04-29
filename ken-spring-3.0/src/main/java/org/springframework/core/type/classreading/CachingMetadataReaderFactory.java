/*   1:    */ package org.springframework.core.type.classreading;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.LinkedHashMap;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Map.Entry;
/*   7:    */ import org.springframework.core.io.Resource;
/*   8:    */ import org.springframework.core.io.ResourceLoader;
/*   9:    */ 
/*  10:    */ public class CachingMetadataReaderFactory
/*  11:    */   extends SimpleMetadataReaderFactory
/*  12:    */ {
/*  13:    */   public static final int DEFAULT_CACHE_LIMIT = 256;
/*  14: 41 */   private volatile int cacheLimit = 256;
/*  15: 44 */   private final Map<Resource, MetadataReader> classReaderCache = new LinkedHashMap(256, 0.75F, true)
/*  16:    */   {
/*  17:    */     protected boolean removeEldestEntry(Map.Entry<Resource, MetadataReader> eldest)
/*  18:    */     {
/*  19: 47 */       return size() > CachingMetadataReaderFactory.this.getCacheLimit();
/*  20:    */     }
/*  21:    */   };
/*  22:    */   
/*  23:    */   public CachingMetadataReaderFactory() {}
/*  24:    */   
/*  25:    */   public CachingMetadataReaderFactory(ResourceLoader resourceLoader)
/*  26:    */   {
/*  27: 65 */     super(resourceLoader);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public CachingMetadataReaderFactory(ClassLoader classLoader)
/*  31:    */   {
/*  32: 73 */     super(classLoader);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setCacheLimit(int cacheLimit)
/*  36:    */   {
/*  37: 82 */     this.cacheLimit = cacheLimit;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public int getCacheLimit()
/*  41:    */   {
/*  42: 89 */     return this.cacheLimit;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public MetadataReader getMetadataReader(Resource resource)
/*  46:    */     throws IOException
/*  47:    */   {
/*  48: 95 */     if (getCacheLimit() <= 0) {
/*  49: 96 */       return super.getMetadataReader(resource);
/*  50:    */     }
/*  51: 98 */     synchronized (this.classReaderCache)
/*  52:    */     {
/*  53: 99 */       MetadataReader metadataReader = (MetadataReader)this.classReaderCache.get(resource);
/*  54:100 */       if (metadataReader == null)
/*  55:    */       {
/*  56:101 */         metadataReader = super.getMetadataReader(resource);
/*  57:102 */         this.classReaderCache.put(resource, metadataReader);
/*  58:    */       }
/*  59:104 */       return metadataReader;
/*  60:    */     }
/*  61:    */   }
/*  62:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.classreading.CachingMetadataReaderFactory
 * JD-Core Version:    0.7.0.1
 */