/*   1:    */ package org.springframework.instrument.classloading;
/*   2:    */ 
/*   3:    */ import java.lang.instrument.ClassFileTransformer;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import org.apache.commons.logging.Log;
/*   6:    */ import org.apache.commons.logging.LogFactory;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ import org.springframework.util.ClassUtils;
/*   9:    */ import org.springframework.util.ReflectionUtils;
/*  10:    */ 
/*  11:    */ public class ReflectiveLoadTimeWeaver
/*  12:    */   implements LoadTimeWeaver
/*  13:    */ {
/*  14:    */   private static final String ADD_TRANSFORMER_METHOD_NAME = "addTransformer";
/*  15:    */   private static final String GET_THROWAWAY_CLASS_LOADER_METHOD_NAME = "getThrowawayClassLoader";
/*  16: 71 */   private static final Log logger = LogFactory.getLog(ReflectiveLoadTimeWeaver.class);
/*  17:    */   private final ClassLoader classLoader;
/*  18:    */   private final Method addTransformerMethod;
/*  19:    */   private final Method getThrowawayClassLoaderMethod;
/*  20:    */   
/*  21:    */   public ReflectiveLoadTimeWeaver()
/*  22:    */   {
/*  23: 86 */     this(ClassUtils.getDefaultClassLoader());
/*  24:    */   }
/*  25:    */   
/*  26:    */   public ReflectiveLoadTimeWeaver(ClassLoader classLoader)
/*  27:    */   {
/*  28: 97 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/*  29: 98 */     this.classLoader = classLoader;
/*  30: 99 */     this.addTransformerMethod = ClassUtils.getMethodIfAvailable(
/*  31:100 */       this.classLoader.getClass(), "addTransformer", 
/*  32:101 */       new Class[] { ClassFileTransformer.class });
/*  33:102 */     if (this.addTransformerMethod == null) {
/*  34:103 */       throw new IllegalStateException(
/*  35:104 */         "ClassLoader [" + classLoader.getClass().getName() + "] does NOT provide an " + 
/*  36:105 */         "'addTransformer(ClassFileTransformer)' method.");
/*  37:    */     }
/*  38:107 */     this.getThrowawayClassLoaderMethod = ClassUtils.getMethodIfAvailable(
/*  39:108 */       this.classLoader.getClass(), "getThrowawayClassLoader", 
/*  40:109 */       new Class[0]);
/*  41:111 */     if ((this.getThrowawayClassLoaderMethod == null) && 
/*  42:112 */       (logger.isInfoEnabled())) {
/*  43:113 */       logger.info("The ClassLoader [" + classLoader.getClass().getName() + "] does NOT provide a " + 
/*  44:114 */         "'getThrowawayClassLoader()' method; SimpleThrowawayClassLoader will be used instead.");
/*  45:    */     }
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void addTransformer(ClassFileTransformer transformer)
/*  49:    */   {
/*  50:121 */     Assert.notNull(transformer, "Transformer must not be null");
/*  51:122 */     ReflectionUtils.invokeMethod(this.addTransformerMethod, this.classLoader, new Object[] { transformer });
/*  52:    */   }
/*  53:    */   
/*  54:    */   public ClassLoader getInstrumentableClassLoader()
/*  55:    */   {
/*  56:126 */     return this.classLoader;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public ClassLoader getThrowawayClassLoader()
/*  60:    */   {
/*  61:130 */     if (this.getThrowawayClassLoaderMethod != null) {
/*  62:131 */       return (ClassLoader)ReflectionUtils.invokeMethod(this.getThrowawayClassLoaderMethod, this.classLoader, 
/*  63:132 */         new Object[0]);
/*  64:    */     }
/*  65:135 */     return new SimpleThrowawayClassLoader(this.classLoader);
/*  66:    */   }
/*  67:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.ReflectiveLoadTimeWeaver
 * JD-Core Version:    0.7.0.1
 */