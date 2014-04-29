/*  1:   */ package org.springframework.instrument.classloading.jboss;
/*  2:   */ 
/*  3:   */ import java.lang.instrument.ClassFileTransformer;
/*  4:   */ import java.lang.reflect.Field;
/*  5:   */ import java.lang.reflect.Method;
/*  6:   */ import org.springframework.util.Assert;
/*  7:   */ import org.springframework.util.ReflectionUtils;
/*  8:   */ 
/*  9:   */ class JBossModulesAdapter
/* 10:   */   implements JBossClassLoaderAdapter
/* 11:   */ {
/* 12:   */   private static final String TRANSFORMER_FIELD_NAME = "transformer";
/* 13:   */   private static final String TRANSFORMER_ADD_METHOD_NAME = "addTransformer";
/* 14:   */   private static final String DELEGATING_TRANSFORMER_CLASS_NAME = "org.jboss.as.server.deployment.module.DelegatingClassFileTransformer";
/* 15:   */   private final ClassLoader classLoader;
/* 16:   */   private final Method addTransformer;
/* 17:   */   private final Object delegatingTransformer;
/* 18:   */   
/* 19:   */   public JBossModulesAdapter(ClassLoader loader)
/* 20:   */   {
/* 21:42 */     this.classLoader = loader;
/* 22:   */     try
/* 23:   */     {
/* 24:45 */       Field transformers = ReflectionUtils.findField(this.classLoader.getClass(), "transformer");
/* 25:46 */       transformers.setAccessible(true);
/* 26:   */       
/* 27:48 */       this.delegatingTransformer = transformers.get(this.classLoader);
/* 28:   */       
/* 29:50 */       Assert.state(this.delegatingTransformer.getClass().getName().equals("org.jboss.as.server.deployment.module.DelegatingClassFileTransformer"), 
/* 30:51 */         "Transformer not of the expected type: " + this.delegatingTransformer.getClass().getName());
/* 31:52 */       this.addTransformer = 
/* 32:53 */         ReflectionUtils.findMethod(this.delegatingTransformer.getClass(), "addTransformer", new Class[] {ClassFileTransformer.class });
/* 33:54 */       this.addTransformer.setAccessible(true);
/* 34:   */     }
/* 35:   */     catch (Exception ex)
/* 36:   */     {
/* 37:56 */       throw new IllegalStateException("Could not initialize JBoss 7 LoadTimeWeaver", ex);
/* 38:   */     }
/* 39:   */   }
/* 40:   */   
/* 41:   */   public void addTransformer(ClassFileTransformer transformer)
/* 42:   */   {
/* 43:   */     try
/* 44:   */     {
/* 45:62 */       this.addTransformer.invoke(this.delegatingTransformer, new Object[] { transformer });
/* 46:   */     }
/* 47:   */     catch (Exception ex)
/* 48:   */     {
/* 49:64 */       throw new IllegalStateException("Could not add transformer on JBoss 7 classloader " + this.classLoader, ex);
/* 50:   */     }
/* 51:   */   }
/* 52:   */   
/* 53:   */   public ClassLoader getInstrumentableClassLoader()
/* 54:   */   {
/* 55:69 */     return this.classLoader;
/* 56:   */   }
/* 57:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.jboss.JBossModulesAdapter
 * JD-Core Version:    0.7.0.1
 */