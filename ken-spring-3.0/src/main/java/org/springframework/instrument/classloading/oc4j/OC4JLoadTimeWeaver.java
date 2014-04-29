/*  1:   */ package org.springframework.instrument.classloading.oc4j;
/*  2:   */ 
/*  3:   */ import java.lang.instrument.ClassFileTransformer;
/*  4:   */ import org.springframework.instrument.classloading.LoadTimeWeaver;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ import org.springframework.util.ClassUtils;
/*  7:   */ 
/*  8:   */ public class OC4JLoadTimeWeaver
/*  9:   */   implements LoadTimeWeaver
/* 10:   */ {
/* 11:   */   private final OC4JClassLoaderAdapter classLoader;
/* 12:   */   
/* 13:   */   public OC4JLoadTimeWeaver()
/* 14:   */   {
/* 15:47 */     this(ClassUtils.getDefaultClassLoader());
/* 16:   */   }
/* 17:   */   
/* 18:   */   public OC4JLoadTimeWeaver(ClassLoader classLoader)
/* 19:   */   {
/* 20:56 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/* 21:57 */     this.classLoader = new OC4JClassLoaderAdapter(classLoader);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void addTransformer(ClassFileTransformer transformer)
/* 25:   */   {
/* 26:61 */     Assert.notNull(transformer, "Transformer must not be null");
/* 27:   */     
/* 28:   */ 
/* 29:64 */     this.classLoader.addTransformer(transformer);
/* 30:   */   }
/* 31:   */   
/* 32:   */   public ClassLoader getInstrumentableClassLoader()
/* 33:   */   {
/* 34:68 */     return this.classLoader.getClassLoader();
/* 35:   */   }
/* 36:   */   
/* 37:   */   public ClassLoader getThrowawayClassLoader()
/* 38:   */   {
/* 39:72 */     return this.classLoader.getThrowawayClassLoader();
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.oc4j.OC4JLoadTimeWeaver
 * JD-Core Version:    0.7.0.1
 */