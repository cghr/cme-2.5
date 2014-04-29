/*   1:    */ package org.springframework.instrument.classloading.websphere;
/*   2:    */ 
/*   3:    */ import java.lang.instrument.ClassFileTransformer;
/*   4:    */ import java.lang.reflect.Constructor;
/*   5:    */ import java.lang.reflect.Field;
/*   6:    */ import java.lang.reflect.InvocationHandler;
/*   7:    */ import java.lang.reflect.InvocationTargetException;
/*   8:    */ import java.lang.reflect.Method;
/*   9:    */ import java.lang.reflect.Proxy;
/*  10:    */ import java.util.List;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ 
/*  13:    */ class WebSphereClassLoaderAdapter
/*  14:    */ {
/*  15:    */   private static final String COMPOUND_CLASS_LOADER_NAME = "com.ibm.ws.classloader.CompoundClassLoader";
/*  16:    */   private static final String CLASS_PRE_PROCESSOR_NAME = "com.ibm.websphere.classloader.ClassLoaderInstancePreDefinePlugin";
/*  17:    */   private static final String PLUGINS_FIELD = "preDefinePlugins";
/*  18:    */   private ClassLoader classLoader;
/*  19:    */   private Class<?> wsPreProcessorClass;
/*  20:    */   private Method addPreDefinePlugin;
/*  21:    */   private Constructor<? extends ClassLoader> cloneConstructor;
/*  22:    */   private Field transformerList;
/*  23:    */   
/*  24:    */   public WebSphereClassLoaderAdapter(ClassLoader classLoader)
/*  25:    */   {
/*  26: 52 */     Class<?> wsCompoundClassLoaderClass = null;
/*  27:    */     try
/*  28:    */     {
/*  29: 54 */       wsCompoundClassLoaderClass = classLoader.loadClass("com.ibm.ws.classloader.CompoundClassLoader");
/*  30: 55 */       this.cloneConstructor = classLoader.getClass().getDeclaredConstructor(new Class[] { wsCompoundClassLoaderClass });
/*  31: 56 */       this.cloneConstructor.setAccessible(true);
/*  32:    */       
/*  33: 58 */       this.wsPreProcessorClass = classLoader.loadClass("com.ibm.websphere.classloader.ClassLoaderInstancePreDefinePlugin");
/*  34: 59 */       this.addPreDefinePlugin = classLoader.getClass().getMethod("addPreDefinePlugin", new Class[] { this.wsPreProcessorClass });
/*  35: 60 */       this.transformerList = wsCompoundClassLoaderClass.getDeclaredField("preDefinePlugins");
/*  36: 61 */       this.transformerList.setAccessible(true);
/*  37:    */     }
/*  38:    */     catch (Exception ex)
/*  39:    */     {
/*  40: 64 */       throw new IllegalStateException(
/*  41: 65 */         "Could not initialize WebSphere LoadTimeWeaver because WebSphere 7 API classes are not available", 
/*  42: 66 */         ex);
/*  43:    */     }
/*  44: 68 */     Assert.isInstanceOf(wsCompoundClassLoaderClass, classLoader, 
/*  45: 69 */       "ClassLoader must be instance of [com.ibm.ws.classloader.CompoundClassLoader]");
/*  46: 70 */     this.classLoader = classLoader;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public ClassLoader getClassLoader()
/*  50:    */   {
/*  51: 74 */     return this.classLoader;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void addTransformer(ClassFileTransformer transformer)
/*  55:    */   {
/*  56: 78 */     Assert.notNull(transformer, "ClassFileTransformer must not be null");
/*  57:    */     try
/*  58:    */     {
/*  59: 80 */       InvocationHandler adapter = new WebSphereClassPreDefinePlugin(transformer);
/*  60: 81 */       Object adapterInstance = Proxy.newProxyInstance(this.wsPreProcessorClass.getClassLoader(), 
/*  61: 82 */         new Class[] { this.wsPreProcessorClass }, adapter);
/*  62: 83 */       this.addPreDefinePlugin.invoke(this.classLoader, new Object[] { adapterInstance });
/*  63:    */     }
/*  64:    */     catch (InvocationTargetException ex)
/*  65:    */     {
/*  66: 87 */       throw new IllegalStateException("WebSphere addPreDefinePlugin method threw exception", ex.getCause());
/*  67:    */     }
/*  68:    */     catch (Exception ex)
/*  69:    */     {
/*  70: 90 */       throw new IllegalStateException("Could not invoke WebSphere addPreDefinePlugin method", ex);
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   public ClassLoader getThrowawayClassLoader()
/*  75:    */   {
/*  76:    */     try
/*  77:    */     {
/*  78: 97 */       ClassLoader loader = (ClassLoader)this.cloneConstructor.newInstance(new Object[] { getClassLoader() });
/*  79:    */       
/*  80: 99 */       List list = (List)this.transformerList.get(loader);
/*  81:100 */       list.clear();
/*  82:101 */       return loader;
/*  83:    */     }
/*  84:    */     catch (InvocationTargetException ex)
/*  85:    */     {
/*  86:104 */       throw new IllegalStateException("WebSphere CompoundClassLoader constructor failed", ex.getCause());
/*  87:    */     }
/*  88:    */     catch (Exception ex)
/*  89:    */     {
/*  90:107 */       throw new IllegalStateException("Could not construct WebSphere CompoundClassLoader", ex);
/*  91:    */     }
/*  92:    */   }
/*  93:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.websphere.WebSphereClassLoaderAdapter
 * JD-Core Version:    0.7.0.1
 */