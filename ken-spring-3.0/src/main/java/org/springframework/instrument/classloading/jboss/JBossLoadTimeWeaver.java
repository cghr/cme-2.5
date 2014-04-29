/*  1:   */ package org.springframework.instrument.classloading.jboss;
/*  2:   */ 
/*  3:   */ import java.lang.instrument.ClassFileTransformer;
/*  4:   */ import org.springframework.instrument.classloading.LoadTimeWeaver;
/*  5:   */ import org.springframework.instrument.classloading.SimpleThrowawayClassLoader;
/*  6:   */ import org.springframework.util.Assert;
/*  7:   */ import org.springframework.util.ClassUtils;
/*  8:   */ 
/*  9:   */ public class JBossLoadTimeWeaver
/* 10:   */   implements LoadTimeWeaver
/* 11:   */ {
/* 12:   */   private final JBossClassLoaderAdapter adapter;
/* 13:   */   
/* 14:   */   public JBossLoadTimeWeaver()
/* 15:   */   {
/* 16:52 */     this(ClassUtils.getDefaultClassLoader());
/* 17:   */   }
/* 18:   */   
/* 19:   */   public JBossLoadTimeWeaver(ClassLoader classLoader)
/* 20:   */   {
/* 21:62 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/* 22:63 */     String loaderClassName = classLoader.getClass().getName();
/* 23:65 */     if (loaderClassName.startsWith("org.jboss.classloader")) {
/* 24:67 */       this.adapter = new JBossMCAdapter(classLoader);
/* 25:69 */     } else if (loaderClassName.startsWith("org.jboss.modules")) {
/* 26:71 */       this.adapter = new JBossModulesAdapter(classLoader);
/* 27:   */     } else {
/* 28:74 */       throw new IllegalArgumentException("Unexpected ClassLoader type: " + loaderClassName);
/* 29:   */     }
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void addTransformer(ClassFileTransformer transformer)
/* 33:   */   {
/* 34:80 */     this.adapter.addTransformer(transformer);
/* 35:   */   }
/* 36:   */   
/* 37:   */   public ClassLoader getInstrumentableClassLoader()
/* 38:   */   {
/* 39:84 */     return this.adapter.getInstrumentableClassLoader();
/* 40:   */   }
/* 41:   */   
/* 42:   */   public ClassLoader getThrowawayClassLoader()
/* 43:   */   {
/* 44:88 */     return new SimpleThrowawayClassLoader(getInstrumentableClassLoader());
/* 45:   */   }
/* 46:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.jboss.JBossLoadTimeWeaver
 * JD-Core Version:    0.7.0.1
 */