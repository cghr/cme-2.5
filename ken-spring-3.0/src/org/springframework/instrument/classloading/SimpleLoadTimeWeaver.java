/*  1:   */ package org.springframework.instrument.classloading;
/*  2:   */ 
/*  3:   */ import java.lang.instrument.ClassFileTransformer;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ import org.springframework.util.ClassUtils;
/*  6:   */ 
/*  7:   */ public class SimpleLoadTimeWeaver
/*  8:   */   implements LoadTimeWeaver
/*  9:   */ {
/* 10:   */   private final SimpleInstrumentableClassLoader classLoader;
/* 11:   */   
/* 12:   */   public SimpleLoadTimeWeaver()
/* 13:   */   {
/* 14:50 */     this.classLoader = new SimpleInstrumentableClassLoader(ClassUtils.getDefaultClassLoader());
/* 15:   */   }
/* 16:   */   
/* 17:   */   public SimpleLoadTimeWeaver(SimpleInstrumentableClassLoader classLoader)
/* 18:   */   {
/* 19:60 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/* 20:61 */     this.classLoader = classLoader;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void addTransformer(ClassFileTransformer transformer)
/* 24:   */   {
/* 25:66 */     this.classLoader.addTransformer(transformer);
/* 26:   */   }
/* 27:   */   
/* 28:   */   public ClassLoader getInstrumentableClassLoader()
/* 29:   */   {
/* 30:70 */     return this.classLoader;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public ClassLoader getThrowawayClassLoader()
/* 34:   */   {
/* 35:77 */     return new SimpleThrowawayClassLoader(getInstrumentableClassLoader());
/* 36:   */   }
/* 37:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.SimpleLoadTimeWeaver
 * JD-Core Version:    0.7.0.1
 */