/*   1:    */ package org.springframework.instrument.classloading;
/*   2:    */ 
/*   3:    */ import java.lang.instrument.ClassFileTransformer;
/*   4:    */ import java.lang.instrument.IllegalClassFormatException;
/*   5:    */ import java.security.ProtectionDomain;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.List;
/*   8:    */ 
/*   9:    */ public class WeavingTransformer
/*  10:    */ {
/*  11:    */   private final ClassLoader classLoader;
/*  12: 41 */   private final List<ClassFileTransformer> transformers = new ArrayList();
/*  13:    */   
/*  14:    */   public WeavingTransformer(ClassLoader classLoader)
/*  15:    */   {
/*  16: 49 */     if (classLoader == null) {
/*  17: 50 */       throw new IllegalArgumentException("ClassLoader must not be null");
/*  18:    */     }
/*  19: 52 */     this.classLoader = classLoader;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public void addTransformer(ClassFileTransformer transformer)
/*  23:    */   {
/*  24: 61 */     if (transformer == null) {
/*  25: 62 */       throw new IllegalArgumentException("Transformer must not be null");
/*  26:    */     }
/*  27: 64 */     this.transformers.add(transformer);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public byte[] transformIfNecessary(String className, byte[] bytes)
/*  31:    */   {
/*  32: 77 */     String internalName = className.replace(".", "/");
/*  33: 78 */     return transformIfNecessary(className, internalName, bytes, null);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public byte[] transformIfNecessary(String className, String internalName, byte[] bytes, ProtectionDomain pd)
/*  37:    */   {
/*  38: 92 */     byte[] result = bytes;
/*  39: 93 */     for (ClassFileTransformer cft : this.transformers) {
/*  40:    */       try
/*  41:    */       {
/*  42: 95 */         byte[] transformed = cft.transform(this.classLoader, internalName, null, pd, result);
/*  43: 96 */         if (transformed != null) {
/*  44: 97 */           result = transformed;
/*  45:    */         }
/*  46:    */       }
/*  47:    */       catch (IllegalClassFormatException ex)
/*  48:    */       {
/*  49:101 */         throw new IllegalStateException("Class file transformation failed", ex);
/*  50:    */       }
/*  51:    */     }
/*  52:104 */     return result;
/*  53:    */   }
/*  54:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.WeavingTransformer
 * JD-Core Version:    0.7.0.1
 */