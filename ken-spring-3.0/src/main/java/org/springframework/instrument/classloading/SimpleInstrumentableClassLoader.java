/*  1:   */ package org.springframework.instrument.classloading;
/*  2:   */ 
/*  3:   */ import java.lang.instrument.ClassFileTransformer;
/*  4:   */ import org.springframework.core.OverridingClassLoader;
/*  5:   */ 
/*  6:   */ public class SimpleInstrumentableClassLoader
/*  7:   */   extends OverridingClassLoader
/*  8:   */ {
/*  9:   */   private final WeavingTransformer weavingTransformer;
/* 10:   */   
/* 11:   */   public SimpleInstrumentableClassLoader(ClassLoader parent)
/* 12:   */   {
/* 13:44 */     super(parent);
/* 14:45 */     this.weavingTransformer = new WeavingTransformer(parent);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public void addTransformer(ClassFileTransformer transformer)
/* 18:   */   {
/* 19:55 */     this.weavingTransformer.addTransformer(transformer);
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected byte[] transformIfNecessary(String name, byte[] bytes)
/* 23:   */   {
/* 24:61 */     return this.weavingTransformer.transformIfNecessary(name, bytes);
/* 25:   */   }
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.SimpleInstrumentableClassLoader
 * JD-Core Version:    0.7.0.1
 */