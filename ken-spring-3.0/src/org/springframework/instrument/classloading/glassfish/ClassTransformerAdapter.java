/*  1:   */ package org.springframework.instrument.classloading.glassfish;
/*  2:   */ 
/*  3:   */ import java.lang.instrument.ClassFileTransformer;
/*  4:   */ import java.lang.instrument.IllegalClassFormatException;
/*  5:   */ import java.security.ProtectionDomain;
/*  6:   */ import javax.persistence.spi.ClassTransformer;
/*  7:   */ 
/*  8:   */ class ClassTransformerAdapter
/*  9:   */   implements ClassTransformer
/* 10:   */ {
/* 11:   */   private final ClassFileTransformer classFileTransformer;
/* 12:   */   
/* 13:   */   public ClassTransformerAdapter(ClassFileTransformer classFileTransformer)
/* 14:   */   {
/* 15:42 */     this.classFileTransformer = classFileTransformer;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
/* 19:   */     throws IllegalClassFormatException
/* 20:   */   {
/* 21:48 */     byte[] result = this.classFileTransformer.transform(loader, className, classBeingRedefined, protectionDomain, 
/* 22:49 */       classfileBuffer);
/* 23:   */     
/* 24:   */ 
/* 25:52 */     return result == classfileBuffer ? null : result;
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.glassfish.ClassTransformerAdapter
 * JD-Core Version:    0.7.0.1
 */