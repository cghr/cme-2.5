/*  1:   */ package org.springframework.core.io.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.io.ResourceLoader;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ import org.springframework.util.ResourceUtils;
/*  6:   */ 
/*  7:   */ public abstract class ResourcePatternUtils
/*  8:   */ {
/*  9:   */   public static boolean isUrl(String resourceLocation)
/* 10:   */   {
/* 11:48 */     return (resourceLocation != null) && ((resourceLocation.startsWith("classpath*:")) || (ResourceUtils.isUrl(resourceLocation)));
/* 12:   */   }
/* 13:   */   
/* 14:   */   public static ResourcePatternResolver getResourcePatternResolver(ResourceLoader resourceLoader)
/* 15:   */   {
/* 16:62 */     Assert.notNull(resourceLoader, "ResourceLoader must not be null");
/* 17:63 */     if ((resourceLoader instanceof ResourcePatternResolver)) {
/* 18:64 */       return (ResourcePatternResolver)resourceLoader;
/* 19:   */     }
/* 20:66 */     if (resourceLoader != null) {
/* 21:67 */       return new PathMatchingResourcePatternResolver(resourceLoader);
/* 22:   */     }
/* 23:70 */     return new PathMatchingResourcePatternResolver();
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.support.ResourcePatternUtils
 * JD-Core Version:    0.7.0.1
 */