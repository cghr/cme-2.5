/*  1:   */ package org.springframework.instrument.classloading.websphere;
/*  2:   */ 
/*  3:   */ import java.lang.instrument.ClassFileTransformer;
/*  4:   */ import org.springframework.instrument.classloading.LoadTimeWeaver;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ import org.springframework.util.ClassUtils;
/*  7:   */ 
/*  8:   */ public class WebSphereLoadTimeWeaver
/*  9:   */   implements LoadTimeWeaver
/* 10:   */ {
/* 11:   */   private final WebSphereClassLoaderAdapter classLoader;
/* 12:   */   
/* 13:   */   public WebSphereLoadTimeWeaver()
/* 14:   */   {
/* 15:43 */     this(ClassUtils.getDefaultClassLoader());
/* 16:   */   }
/* 17:   */   
/* 18:   */   public WebSphereLoadTimeWeaver(ClassLoader classLoader)
/* 19:   */   {
/* 20:53 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/* 21:54 */     this.classLoader = new WebSphereClassLoaderAdapter(classLoader);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void addTransformer(ClassFileTransformer transformer)
/* 25:   */   {
/* 26:59 */     this.classLoader.addTransformer(transformer);
/* 27:   */   }
/* 28:   */   
/* 29:   */   public ClassLoader getInstrumentableClassLoader()
/* 30:   */   {
/* 31:63 */     return this.classLoader.getClassLoader();
/* 32:   */   }
/* 33:   */   
/* 34:   */   public ClassLoader getThrowawayClassLoader()
/* 35:   */   {
/* 36:67 */     return this.classLoader.getThrowawayClassLoader();
/* 37:   */   }
/* 38:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.websphere.WebSphereLoadTimeWeaver
 * JD-Core Version:    0.7.0.1
 */