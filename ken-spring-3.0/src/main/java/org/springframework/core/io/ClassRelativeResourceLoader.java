/*  1:   */ package org.springframework.core.io;
/*  2:   */ 
/*  3:   */ import org.springframework.util.Assert;
/*  4:   */ import org.springframework.util.StringUtils;
/*  5:   */ 
/*  6:   */ public class ClassRelativeResourceLoader
/*  7:   */   extends DefaultResourceLoader
/*  8:   */ {
/*  9:   */   private final Class clazz;
/* 10:   */   
/* 11:   */   public ClassRelativeResourceLoader(Class clazz)
/* 12:   */   {
/* 13:41 */     Assert.notNull(clazz, "Class must not be null");
/* 14:42 */     this.clazz = clazz;
/* 15:43 */     setClassLoader(clazz.getClassLoader());
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected Resource getResourceByPath(String path)
/* 19:   */   {
/* 20:47 */     return new ClassRelativeContextResource(path, this.clazz);
/* 21:   */   }
/* 22:   */   
/* 23:   */   private static class ClassRelativeContextResource
/* 24:   */     extends ClassPathResource
/* 25:   */     implements ContextResource
/* 26:   */   {
/* 27:   */     private final Class clazz;
/* 28:   */     
/* 29:   */     public ClassRelativeContextResource(String path, Class clazz)
/* 30:   */     {
/* 31:60 */       super(clazz);
/* 32:61 */       this.clazz = clazz;
/* 33:   */     }
/* 34:   */     
/* 35:   */     public String getPathWithinContext()
/* 36:   */     {
/* 37:65 */       return getPath();
/* 38:   */     }
/* 39:   */     
/* 40:   */     public Resource createRelative(String relativePath)
/* 41:   */     {
/* 42:70 */       String pathToUse = StringUtils.applyRelativePath(getPath(), relativePath);
/* 43:71 */       return new ClassRelativeContextResource(pathToUse, this.clazz);
/* 44:   */     }
/* 45:   */   }
/* 46:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.ClassRelativeResourceLoader
 * JD-Core Version:    0.7.0.1
 */