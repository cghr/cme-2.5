/*   1:    */ package org.springframework.context.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Map;
/*   6:    */ import org.springframework.core.DecoratingClassLoader;
/*   7:    */ import org.springframework.core.OverridingClassLoader;
/*   8:    */ import org.springframework.core.SmartClassLoader;
/*   9:    */ import org.springframework.util.ReflectionUtils;
/*  10:    */ 
/*  11:    */ class ContextTypeMatchClassLoader
/*  12:    */   extends DecoratingClassLoader
/*  13:    */   implements SmartClassLoader
/*  14:    */ {
/*  15:    */   private static Method findLoadedClassMethod;
/*  16:    */   
/*  17:    */   static
/*  18:    */   {
/*  19:    */     try
/*  20:    */     {
/*  21: 45 */       findLoadedClassMethod = ClassLoader.class.getDeclaredMethod("findLoadedClass", new Class[] { String.class });
/*  22:    */     }
/*  23:    */     catch (NoSuchMethodException localNoSuchMethodException)
/*  24:    */     {
/*  25: 48 */       throw new IllegalStateException("Invalid [java.lang.ClassLoader] class: no 'findLoadedClass' method defined!");
/*  26:    */     }
/*  27:    */   }
/*  28:    */   
/*  29: 54 */   private final Map<String, byte[]> bytesCache = new HashMap();
/*  30:    */   
/*  31:    */   public ContextTypeMatchClassLoader(ClassLoader parent)
/*  32:    */   {
/*  33: 58 */     super(parent);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public Class loadClass(String name)
/*  37:    */     throws ClassNotFoundException
/*  38:    */   {
/*  39: 63 */     return new ContextOverridingClassLoader(getParent()).loadClass(name);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public boolean isClassReloadable(Class clazz)
/*  43:    */   {
/*  44: 67 */     return clazz.getClassLoader() instanceof ContextOverridingClassLoader;
/*  45:    */   }
/*  46:    */   
/*  47:    */   private class ContextOverridingClassLoader
/*  48:    */     extends OverridingClassLoader
/*  49:    */   {
/*  50:    */     public ContextOverridingClassLoader(ClassLoader parent)
/*  51:    */     {
/*  52: 78 */       super();
/*  53:    */     }
/*  54:    */     
/*  55:    */     protected boolean isEligibleForOverriding(String className)
/*  56:    */     {
/*  57: 83 */       if ((isExcluded(className)) || (ContextTypeMatchClassLoader.this.isExcluded(className))) {
/*  58: 84 */         return false;
/*  59:    */       }
/*  60: 86 */       ReflectionUtils.makeAccessible(ContextTypeMatchClassLoader.findLoadedClassMethod);
/*  61: 87 */       ClassLoader parent = getParent();
/*  62: 88 */       while (parent != null)
/*  63:    */       {
/*  64: 89 */         if (ReflectionUtils.invokeMethod(ContextTypeMatchClassLoader.findLoadedClassMethod, parent, new Object[] { className }) != null) {
/*  65: 90 */           return false;
/*  66:    */         }
/*  67: 92 */         parent = parent.getParent();
/*  68:    */       }
/*  69: 94 */       return true;
/*  70:    */     }
/*  71:    */     
/*  72:    */     protected Class loadClassForOverriding(String name)
/*  73:    */       throws ClassNotFoundException
/*  74:    */     {
/*  75: 99 */       byte[] bytes = (byte[])ContextTypeMatchClassLoader.this.bytesCache.get(name);
/*  76:100 */       if (bytes == null)
/*  77:    */       {
/*  78:101 */         bytes = loadBytesForClass(name);
/*  79:102 */         if (bytes != null) {
/*  80:103 */           ContextTypeMatchClassLoader.this.bytesCache.put(name, bytes);
/*  81:    */         } else {
/*  82:106 */           return null;
/*  83:    */         }
/*  84:    */       }
/*  85:109 */       return defineClass(name, bytes, 0, bytes.length);
/*  86:    */     }
/*  87:    */   }
/*  88:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.ContextTypeMatchClassLoader
 * JD-Core Version:    0.7.0.1
 */