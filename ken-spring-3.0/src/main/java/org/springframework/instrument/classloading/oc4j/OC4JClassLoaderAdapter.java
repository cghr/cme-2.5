/*  1:   */ package org.springframework.instrument.classloading.oc4j;
/*  2:   */ 
/*  3:   */ import java.lang.instrument.ClassFileTransformer;
/*  4:   */ import java.lang.reflect.InvocationTargetException;
/*  5:   */ import java.lang.reflect.Method;
/*  6:   */ import java.lang.reflect.Proxy;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ 
/*  9:   */ class OC4JClassLoaderAdapter
/* 10:   */ {
/* 11:   */   private static final String CL_UTILS = "oracle.classloader.util.ClassLoaderUtilities";
/* 12:   */   private static final String PREPROCESS_UTILS = "oracle.classloader.util.ClassPreprocessor";
/* 13:   */   private final ClassLoader classLoader;
/* 14:   */   private final Class<?> processorClass;
/* 15:   */   private final Method addTransformer;
/* 16:   */   private final Method copy;
/* 17:   */   
/* 18:   */   public OC4JClassLoaderAdapter(ClassLoader classLoader)
/* 19:   */   {
/* 20:   */     try
/* 21:   */     {
/* 22:46 */       Class<?> utilClass = classLoader.loadClass("oracle.classloader.util.ClassLoaderUtilities");
/* 23:47 */       this.processorClass = classLoader.loadClass("oracle.classloader.util.ClassPreprocessor");
/* 24:   */       
/* 25:49 */       this.addTransformer = utilClass
/* 26:50 */         .getMethod("addPreprocessor", new Class[] { ClassLoader.class, this.processorClass });
/* 27:51 */       this.copy = utilClass.getMethod("copy", new Class[] { ClassLoader.class });
/* 28:   */     }
/* 29:   */     catch (Exception ex)
/* 30:   */     {
/* 31:54 */       throw new IllegalStateException(
/* 32:55 */         "Could not initialize OC4J LoadTimeWeaver because OC4J API classes are not available", ex);
/* 33:   */     }
/* 34:58 */     this.classLoader = classLoader;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public void addTransformer(ClassFileTransformer transformer)
/* 38:   */   {
/* 39:62 */     Assert.notNull(transformer, "ClassFileTransformer must not be null");
/* 40:   */     try
/* 41:   */     {
/* 42:64 */       OC4JClassPreprocessorAdapter adapter = new OC4JClassPreprocessorAdapter(transformer);
/* 43:65 */       Object adapterInstance = Proxy.newProxyInstance(this.processorClass.getClassLoader(), 
/* 44:66 */         new Class[] { this.processorClass }, adapter);
/* 45:67 */       this.addTransformer.invoke(null, new Object[] { this.classLoader, adapterInstance });
/* 46:   */     }
/* 47:   */     catch (InvocationTargetException ex)
/* 48:   */     {
/* 49:69 */       throw new IllegalStateException("OC4J addPreprocessor method threw exception", ex.getCause());
/* 50:   */     }
/* 51:   */     catch (Exception ex)
/* 52:   */     {
/* 53:71 */       throw new IllegalStateException("Could not invoke OC4J addPreprocessor method", ex);
/* 54:   */     }
/* 55:   */   }
/* 56:   */   
/* 57:   */   public ClassLoader getClassLoader()
/* 58:   */   {
/* 59:76 */     return this.classLoader;
/* 60:   */   }
/* 61:   */   
/* 62:   */   public ClassLoader getThrowawayClassLoader()
/* 63:   */   {
/* 64:   */     try
/* 65:   */     {
/* 66:81 */       return (ClassLoader)this.copy.invoke(null, new Object[] { this.classLoader });
/* 67:   */     }
/* 68:   */     catch (InvocationTargetException ex)
/* 69:   */     {
/* 70:83 */       throw new IllegalStateException("OC4J copy method failed", ex.getCause());
/* 71:   */     }
/* 72:   */     catch (Exception ex)
/* 73:   */     {
/* 74:85 */       throw new IllegalStateException("Could not copy OC4J classloader", ex);
/* 75:   */     }
/* 76:   */   }
/* 77:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.oc4j.OC4JClassLoaderAdapter
 * JD-Core Version:    0.7.0.1
 */