/*   1:    */ package org.springframework.instrument.classloading.weblogic;
/*   2:    */ 
/*   3:    */ import java.lang.instrument.ClassFileTransformer;
/*   4:    */ import java.lang.reflect.Constructor;
/*   5:    */ import java.lang.reflect.InvocationHandler;
/*   6:    */ import java.lang.reflect.InvocationTargetException;
/*   7:    */ import java.lang.reflect.Method;
/*   8:    */ import java.lang.reflect.Proxy;
/*   9:    */ import org.springframework.util.Assert;
/*  10:    */ 
/*  11:    */ class WebLogicClassLoaderAdapter
/*  12:    */ {
/*  13:    */   private static final String GENERIC_CLASS_LOADER_NAME = "weblogic.utils.classloaders.GenericClassLoader";
/*  14:    */   private static final String CLASS_PRE_PROCESSOR_NAME = "weblogic.utils.classloaders.ClassPreProcessor";
/*  15:    */   private final ClassLoader classLoader;
/*  16:    */   private final Class wlPreProcessorClass;
/*  17:    */   private final Method addPreProcessorMethod;
/*  18:    */   private final Method getClassFinderMethod;
/*  19:    */   private final Method getParentMethod;
/*  20:    */   private final Constructor wlGenericClassLoaderConstructor;
/*  21:    */   
/*  22:    */   public WebLogicClassLoaderAdapter(ClassLoader classLoader)
/*  23:    */   {
/*  24: 58 */     Class wlGenericClassLoaderClass = null;
/*  25:    */     try
/*  26:    */     {
/*  27: 60 */       wlGenericClassLoaderClass = classLoader.loadClass("weblogic.utils.classloaders.GenericClassLoader");
/*  28: 61 */       this.wlPreProcessorClass = classLoader.loadClass("weblogic.utils.classloaders.ClassPreProcessor");
/*  29: 62 */       this.addPreProcessorMethod = classLoader.getClass()
/*  30: 63 */         .getMethod("addInstanceClassPreProcessor", new Class[] { this.wlPreProcessorClass });
/*  31: 64 */       this.getClassFinderMethod = classLoader.getClass().getMethod("getClassFinder", new Class[0]);
/*  32: 65 */       this.getParentMethod = classLoader.getClass().getMethod("getParent", new Class[0]);
/*  33: 66 */       this.wlGenericClassLoaderConstructor = wlGenericClassLoaderClass
/*  34: 67 */         .getConstructor(new Class[] {this.getClassFinderMethod.getReturnType(), ClassLoader.class });
/*  35:    */     }
/*  36:    */     catch (Exception ex)
/*  37:    */     {
/*  38: 70 */       throw new IllegalStateException(
/*  39: 71 */         "Could not initialize WebLogic LoadTimeWeaver because WebLogic 10 API classes are not available", ex);
/*  40:    */     }
/*  41: 73 */     Assert.isInstanceOf(wlGenericClassLoaderClass, classLoader, 
/*  42: 74 */       "ClassLoader must be instance of [" + wlGenericClassLoaderClass.getName() + "]");
/*  43: 75 */     this.classLoader = classLoader;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void addTransformer(ClassFileTransformer transformer)
/*  47:    */   {
/*  48: 80 */     Assert.notNull(transformer, "ClassFileTransformer must not be null");
/*  49:    */     try
/*  50:    */     {
/*  51: 82 */       InvocationHandler adapter = new WebLogicClassPreProcessorAdapter(transformer, this.classLoader);
/*  52: 83 */       Object adapterInstance = Proxy.newProxyInstance(this.wlPreProcessorClass.getClassLoader(), 
/*  53: 84 */         new Class[] { this.wlPreProcessorClass }, adapter);
/*  54: 85 */       this.addPreProcessorMethod.invoke(this.classLoader, new Object[] { adapterInstance });
/*  55:    */     }
/*  56:    */     catch (InvocationTargetException ex)
/*  57:    */     {
/*  58: 88 */       throw new IllegalStateException("WebLogic addInstanceClassPreProcessor method threw exception", ex.getCause());
/*  59:    */     }
/*  60:    */     catch (Exception ex)
/*  61:    */     {
/*  62: 91 */       throw new IllegalStateException("Could not invoke WebLogic addInstanceClassPreProcessor method", ex);
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   public ClassLoader getClassLoader()
/*  67:    */   {
/*  68: 96 */     return this.classLoader;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public ClassLoader getThrowawayClassLoader()
/*  72:    */   {
/*  73:    */     try
/*  74:    */     {
/*  75:101 */       Object classFinder = this.getClassFinderMethod.invoke(this.classLoader, new Object[0]);
/*  76:102 */       Object parent = this.getParentMethod.invoke(this.classLoader, new Object[0]);
/*  77:    */       
/*  78:104 */       return (ClassLoader)this.wlGenericClassLoaderConstructor.newInstance(new Object[] { classFinder, parent });
/*  79:    */     }
/*  80:    */     catch (InvocationTargetException ex)
/*  81:    */     {
/*  82:107 */       throw new IllegalStateException("WebLogic GenericClassLoader constructor failed", ex.getCause());
/*  83:    */     }
/*  84:    */     catch (Exception ex)
/*  85:    */     {
/*  86:110 */       throw new IllegalStateException("Could not construct WebLogic GenericClassLoader", ex);
/*  87:    */     }
/*  88:    */   }
/*  89:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.weblogic.WebLogicClassLoaderAdapter
 * JD-Core Version:    0.7.0.1
 */