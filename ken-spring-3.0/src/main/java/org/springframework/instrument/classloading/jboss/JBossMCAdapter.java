/*   1:    */ package org.springframework.instrument.classloading.jboss;
/*   2:    */ 
/*   3:    */ import java.lang.instrument.ClassFileTransformer;
/*   4:    */ import java.lang.reflect.InvocationHandler;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.lang.reflect.Proxy;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ import org.springframework.util.ReflectionUtils;
/*   9:    */ 
/*  10:    */ class JBossMCAdapter
/*  11:    */   implements JBossClassLoaderAdapter
/*  12:    */ {
/*  13:    */   private static final String TRANSLATOR_NAME = "org.jboss.util.loading.Translator";
/*  14:    */   private static final String POLICY_NAME = "org.jboss.classloader.spi.base.BaseClassLoaderPolicy";
/*  15:    */   private static final String DOMAIN_NAME = "org.jboss.classloader.spi.base.BaseClassLoaderDomain";
/*  16:    */   private static final String DEDICATED_SYSTEM = "org.jboss.classloader.spi.ClassLoaderSystem";
/*  17:    */   private static final String LOADER_NAME = "org.jboss.classloader.spi.base.BaseClassLoader";
/*  18:    */   private static final String GET_POLICY = "getPolicy";
/*  19:    */   private static final String GET_DOMAIN = "getClassLoaderDomain";
/*  20:    */   private static final String GET_SYSTEM = "getClassLoaderSystem";
/*  21:    */   private static final String ADD_TRANSLATOR_NAME = "addTranslator";
/*  22:    */   private static final String SET_TRANSLATOR_NAME = "setTranslator";
/*  23:    */   private final ClassLoader classLoader;
/*  24:    */   private final Class<?> translatorClass;
/*  25:    */   private final Method addTranslator;
/*  26:    */   private final Object target;
/*  27:    */   
/*  28:    */   JBossMCAdapter(ClassLoader classLoader)
/*  29:    */   {
/*  30: 56 */     Class<?> clazzLoaderType = null;
/*  31:    */     try
/*  32:    */     {
/*  33: 59 */       clazzLoaderType = classLoader.loadClass("org.jboss.classloader.spi.base.BaseClassLoader");
/*  34:    */       
/*  35: 61 */       ClassLoader clazzLoader = null;
/*  36: 63 */       for (ClassLoader cl = classLoader; (cl != null) && (clazzLoader == null); cl = cl.getParent()) {
/*  37: 64 */         if (clazzLoaderType.isInstance(cl)) {
/*  38: 65 */           clazzLoader = cl;
/*  39:    */         }
/*  40:    */       }
/*  41: 69 */       if (clazzLoader == null) {
/*  42: 70 */         throw new IllegalArgumentException(classLoader + " and its parents are not suitable ClassLoaders: " + 
/*  43: 71 */           "A [" + "org.jboss.classloader.spi.base.BaseClassLoader" + "] implementation is required.");
/*  44:    */       }
/*  45: 74 */       this.classLoader = clazzLoader;
/*  46:    */       
/*  47:    */ 
/*  48: 77 */       classLoader = clazzLoader.getClass().getClassLoader();
/*  49:    */       
/*  50:    */ 
/*  51: 80 */       Method method = clazzLoaderType.getDeclaredMethod("getPolicy", new Class[0]);
/*  52: 81 */       ReflectionUtils.makeAccessible(method);
/*  53: 82 */       Object policy = method.invoke(this.classLoader, new Object[0]);
/*  54:    */       
/*  55: 84 */       Object addTarget = null;
/*  56: 85 */       Method addMethod = null;
/*  57:    */       
/*  58:    */ 
/*  59:    */ 
/*  60: 89 */       this.translatorClass = classLoader.loadClass("org.jboss.util.loading.Translator");
/*  61: 90 */       Class<?> clazz = classLoader.loadClass("org.jboss.classloader.spi.base.BaseClassLoaderPolicy");
/*  62:    */       try
/*  63:    */       {
/*  64: 92 */         addMethod = clazz.getDeclaredMethod("addTranslator", new Class[] { this.translatorClass });
/*  65: 93 */         addTarget = policy;
/*  66:    */       }
/*  67:    */       catch (NoSuchMethodException localNoSuchMethodException) {}
/*  68: 98 */       if (addMethod == null)
/*  69:    */       {
/*  70:101 */         method = clazz.getDeclaredMethod("getClassLoaderDomain", new Class[0]);
/*  71:102 */         ReflectionUtils.makeAccessible(method);
/*  72:103 */         Object domain = method.invoke(policy, new Object[0]);
/*  73:    */         
/*  74:    */ 
/*  75:106 */         clazz = classLoader.loadClass("org.jboss.classloader.spi.base.BaseClassLoaderDomain");
/*  76:107 */         method = clazz.getDeclaredMethod("getClassLoaderSystem", new Class[0]);
/*  77:108 */         ReflectionUtils.makeAccessible(method);
/*  78:109 */         Object system = method.invoke(domain, new Object[0]);
/*  79:    */         
/*  80:    */ 
/*  81:112 */         clazz = classLoader.loadClass("org.jboss.classloader.spi.ClassLoaderSystem");
/*  82:113 */         Assert.isInstanceOf(clazz, system, "JBoss LoadTimeWeaver requires JBoss loader system of type " + 
/*  83:114 */           clazz.getName() + " on JBoss 5.0.x");
/*  84:    */         
/*  85:    */ 
/*  86:117 */         addMethod = clazz.getDeclaredMethod("setTranslator", new Class[] { this.translatorClass });
/*  87:118 */         addTarget = system;
/*  88:    */       }
/*  89:121 */       this.addTranslator = addMethod;
/*  90:122 */       this.target = addTarget;
/*  91:    */     }
/*  92:    */     catch (Exception ex)
/*  93:    */     {
/*  94:125 */       throw new IllegalStateException(
/*  95:126 */         "Could not initialize JBoss LoadTimeWeaver because the JBoss 5 API classes are not available", ex);
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void addTransformer(ClassFileTransformer transformer)
/* 100:    */   {
/* 101:131 */     InvocationHandler adapter = new JBossMCTranslatorAdapter(transformer);
/* 102:132 */     Object adapterInstance = Proxy.newProxyInstance(this.translatorClass.getClassLoader(), 
/* 103:133 */       new Class[] { this.translatorClass }, adapter);
/* 104:    */     try
/* 105:    */     {
/* 106:136 */       this.addTranslator.invoke(this.target, new Object[] { adapterInstance });
/* 107:    */     }
/* 108:    */     catch (Exception ex)
/* 109:    */     {
/* 110:138 */       throw new IllegalStateException("Could not add transformer on JBoss 5/6 classloader " + this.classLoader, ex);
/* 111:    */     }
/* 112:    */   }
/* 113:    */   
/* 114:    */   public ClassLoader getInstrumentableClassLoader()
/* 115:    */   {
/* 116:143 */     return this.classLoader;
/* 117:    */   }
/* 118:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.jboss.JBossMCAdapter
 * JD-Core Version:    0.7.0.1
 */