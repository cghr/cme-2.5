/*  1:   */ package org.springframework.core.type.classreading;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.InputStream;
/*  5:   */ import org.springframework.asm.ClassReader;
/*  6:   */ import org.springframework.core.io.Resource;
/*  7:   */ import org.springframework.core.type.AnnotationMetadata;
/*  8:   */ import org.springframework.core.type.ClassMetadata;
/*  9:   */ 
/* 10:   */ final class SimpleMetadataReader
/* 11:   */   implements MetadataReader
/* 12:   */ {
/* 13:   */   private final Resource resource;
/* 14:   */   private final ClassMetadata classMetadata;
/* 15:   */   private final AnnotationMetadata annotationMetadata;
/* 16:   */   
/* 17:   */   SimpleMetadataReader(Resource resource, ClassLoader classLoader)
/* 18:   */     throws IOException
/* 19:   */   {
/* 20:45 */     InputStream is = resource.getInputStream();
/* 21:46 */     ClassReader classReader = null;
/* 22:   */     try
/* 23:   */     {
/* 24:48 */       classReader = new ClassReader(is);
/* 25:   */     }
/* 26:   */     finally
/* 27:   */     {
/* 28:50 */       is.close();
/* 29:   */     }
/* 30:53 */     AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor(classLoader);
/* 31:54 */     classReader.accept(visitor, true);
/* 32:   */     
/* 33:56 */     this.annotationMetadata = visitor;
/* 34:   */     
/* 35:58 */     this.classMetadata = visitor;
/* 36:59 */     this.resource = resource;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public Resource getResource()
/* 40:   */   {
/* 41:63 */     return this.resource;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public ClassMetadata getClassMetadata()
/* 45:   */   {
/* 46:67 */     return this.classMetadata;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public AnnotationMetadata getAnnotationMetadata()
/* 50:   */   {
/* 51:71 */     return this.annotationMetadata;
/* 52:   */   }
/* 53:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.classreading.SimpleMetadataReader
 * JD-Core Version:    0.7.0.1
 */