/*  1:   */ package org.springframework.instrument.classloading.glassfish;
/*  2:   */ 
/*  3:   */ import java.lang.instrument.ClassFileTransformer;
/*  4:   */ import org.springframework.instrument.classloading.LoadTimeWeaver;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ import org.springframework.util.ClassUtils;
/*  7:   */ 
/*  8:   */ public class GlassFishLoadTimeWeaver
/*  9:   */   implements LoadTimeWeaver
/* 10:   */ {
/* 11:   */   private final GlassFishClassLoaderAdapter classLoader;
/* 12:   */   
/* 13:   */   public GlassFishLoadTimeWeaver()
/* 14:   */   {
/* 15:45 */     this(ClassUtils.getDefaultClassLoader());
/* 16:   */   }
/* 17:   */   
/* 18:   */   public GlassFishLoadTimeWeaver(ClassLoader classLoader)
/* 19:   */   {
/* 20:55 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/* 21:56 */     this.classLoader = new GlassFishClassLoaderAdapter(classLoader);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void addTransformer(ClassFileTransformer transformer)
/* 25:   */   {
/* 26:61 */     this.classLoader.addTransformer(transformer);
/* 27:   */   }
/* 28:   */   
/* 29:   */   public ClassLoader getInstrumentableClassLoader()
/* 30:   */   {
/* 31:65 */     return this.classLoader.getClassLoader();
/* 32:   */   }
/* 33:   */   
/* 34:   */   public ClassLoader getThrowawayClassLoader()
/* 35:   */   {
/* 36:69 */     return this.classLoader.getThrowawayClassLoader();
/* 37:   */   }
/* 38:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.glassfish.GlassFishLoadTimeWeaver
 * JD-Core Version:    0.7.0.1
 */