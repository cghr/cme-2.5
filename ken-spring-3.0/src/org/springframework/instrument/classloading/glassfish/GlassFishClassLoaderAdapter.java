/*   1:    */ package org.springframework.instrument.classloading.glassfish;
/*   2:    */ 
/*   3:    */ import java.lang.instrument.ClassFileTransformer;
/*   4:    */ import java.lang.reflect.InvocationTargetException;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ 
/*   7:    */ class GlassFishClassLoaderAdapter
/*   8:    */ {
/*   9:    */   static final String INSTRUMENTABLE_CLASSLOADER_GLASSFISH_V2 = "com.sun.enterprise.loader.InstrumentableClassLoader";
/*  10:    */   static final String INSTRUMENTABLE_CLASSLOADER_GLASSFISH_V3 = "org.glassfish.api.deployment.InstrumentableClassLoader";
/*  11:    */   private static final String CLASS_TRANSFORMER = "javax.persistence.spi.ClassTransformer";
/*  12:    */   private final ClassLoader classLoader;
/*  13:    */   private final Method addTransformer;
/*  14:    */   private final Method copy;
/*  15:    */   private final boolean glassFishV3;
/*  16:    */   
/*  17:    */   public GlassFishClassLoaderAdapter(ClassLoader classLoader)
/*  18:    */   {
/*  19: 53 */     boolean glassV3 = false;
/*  20:    */     try
/*  21:    */     {
/*  22: 56 */       instrumentableLoaderClass = classLoader.loadClass("com.sun.enterprise.loader.InstrumentableClassLoader");
/*  23:    */     }
/*  24:    */     catch (ClassNotFoundException ex)
/*  25:    */     {
/*  26:    */       try
/*  27:    */       {
/*  28:    */         Class<?> instrumentableLoaderClass;
/*  29: 61 */         Class<?> instrumentableLoaderClass = classLoader.loadClass("org.glassfish.api.deployment.InstrumentableClassLoader");
/*  30: 62 */         glassV3 = true;
/*  31:    */       }
/*  32:    */       catch (ClassNotFoundException localClassNotFoundException1)
/*  33:    */       {
/*  34: 65 */         throw new IllegalStateException("Could not initialize GlassFish LoadTimeWeaver because GlassFish (V1, V2 or V3) API classes are not available", 
/*  35: 66 */           ex);
/*  36:    */       }
/*  37:    */     }
/*  38:    */     Class<?> instrumentableLoaderClass;
/*  39:    */     try
/*  40:    */     {
/*  41: 70 */       Class<?> classTransformerClass = 
/*  42: 71 */         glassV3 ? ClassFileTransformer.class : classLoader.loadClass("javax.persistence.spi.ClassTransformer");
/*  43:    */       
/*  44: 73 */       this.addTransformer = instrumentableLoaderClass.getMethod("addTransformer", new Class[] { classTransformerClass });
/*  45: 74 */       this.copy = instrumentableLoaderClass.getMethod("copy", new Class[0]);
/*  46:    */     }
/*  47:    */     catch (Exception ex)
/*  48:    */     {
/*  49: 77 */       throw new IllegalStateException(
/*  50: 78 */         "Could not initialize GlassFish LoadTimeWeaver because GlassFish API classes are not available", ex);
/*  51:    */     }
/*  52: 81 */     ClassLoader clazzLoader = null;
/*  53: 84 */     for (ClassLoader cl = classLoader; (cl != null) && (clazzLoader == null); cl = cl.getParent()) {
/*  54: 85 */       if (instrumentableLoaderClass.isInstance(cl)) {
/*  55: 86 */         clazzLoader = cl;
/*  56:    */       }
/*  57:    */     }
/*  58: 90 */     if (clazzLoader == null) {
/*  59: 91 */       throw new IllegalArgumentException(classLoader + " and its parents are not suitable ClassLoaders: A [" + 
/*  60: 92 */         instrumentableLoaderClass.getName() + "] implementation is required.");
/*  61:    */     }
/*  62: 95 */     this.classLoader = clazzLoader;
/*  63: 96 */     this.glassFishV3 = glassV3;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void addTransformer(ClassFileTransformer transformer)
/*  67:    */   {
/*  68:    */     try
/*  69:    */     {
/*  70:101 */       this.addTransformer.invoke(this.classLoader, new Object[] {
/*  71:102 */         this.glassFishV3 ? transformer : new ClassTransformerAdapter(transformer) });
/*  72:    */     }
/*  73:    */     catch (InvocationTargetException ex)
/*  74:    */     {
/*  75:105 */       throw new IllegalStateException("GlassFish addTransformer method threw exception ", ex.getCause());
/*  76:    */     }
/*  77:    */     catch (Exception ex)
/*  78:    */     {
/*  79:108 */       throw new IllegalStateException("Could not invoke GlassFish addTransformer method", ex);
/*  80:    */     }
/*  81:    */   }
/*  82:    */   
/*  83:    */   public ClassLoader getClassLoader()
/*  84:    */   {
/*  85:113 */     return this.classLoader;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public ClassLoader getThrowawayClassLoader()
/*  89:    */   {
/*  90:    */     try
/*  91:    */     {
/*  92:118 */       return (ClassLoader)this.copy.invoke(this.classLoader, new Object[0]);
/*  93:    */     }
/*  94:    */     catch (InvocationTargetException ex)
/*  95:    */     {
/*  96:121 */       throw new IllegalStateException("GlassFish copy method threw exception ", ex.getCause());
/*  97:    */     }
/*  98:    */     catch (Exception ex)
/*  99:    */     {
/* 100:124 */       throw new IllegalStateException("Could not invoke GlassFish copy method", ex);
/* 101:    */     }
/* 102:    */   }
/* 103:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.glassfish.GlassFishClassLoaderAdapter
 * JD-Core Version:    0.7.0.1
 */