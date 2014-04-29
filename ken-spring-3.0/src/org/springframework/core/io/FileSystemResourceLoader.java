/*  1:   */ package org.springframework.core.io;
/*  2:   */ 
/*  3:   */ public class FileSystemResourceLoader
/*  4:   */   extends DefaultResourceLoader
/*  5:   */ {
/*  6:   */   protected Resource getResourceByPath(String path)
/*  7:   */   {
/*  8:51 */     if ((path != null) && (path.startsWith("/"))) {
/*  9:52 */       path = path.substring(1);
/* 10:   */     }
/* 11:54 */     return new FileSystemContextResource(path);
/* 12:   */   }
/* 13:   */   
/* 14:   */   private static class FileSystemContextResource
/* 15:   */     extends FileSystemResource
/* 16:   */     implements ContextResource
/* 17:   */   {
/* 18:   */     public FileSystemContextResource(String path)
/* 19:   */     {
/* 20:65 */       super();
/* 21:   */     }
/* 22:   */     
/* 23:   */     public String getPathWithinContext()
/* 24:   */     {
/* 25:69 */       return getPath();
/* 26:   */     }
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.FileSystemResourceLoader
 * JD-Core Version:    0.7.0.1
 */