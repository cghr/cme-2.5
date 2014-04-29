/*  1:   */ package org.springframework.instrument.classloading.websphere;
/*  2:   */ 
/*  3:   */ import java.lang.instrument.ClassFileTransformer;
/*  4:   */ import java.lang.reflect.InvocationHandler;
/*  5:   */ import java.lang.reflect.Method;
/*  6:   */ import java.security.CodeSource;
/*  7:   */ import org.springframework.util.FileCopyUtils;
/*  8:   */ 
/*  9:   */ class WebSphereClassPreDefinePlugin
/* 10:   */   implements InvocationHandler
/* 11:   */ {
/* 12:   */   private final ClassFileTransformer transformer;
/* 13:   */   
/* 14:   */   public WebSphereClassPreDefinePlugin(ClassFileTransformer transformer)
/* 15:   */   {
/* 16:46 */     this.transformer = transformer;
/* 17:47 */     ClassLoader classLoader = transformer.getClass().getClassLoader();
/* 18:   */     try
/* 19:   */     {
/* 20:51 */       String dummyClass = Dummy.class.getName().replace('.', '/');
/* 21:52 */       byte[] bytes = FileCopyUtils.copyToByteArray(classLoader.getResourceAsStream(dummyClass + ".class"));
/* 22:53 */       transformer.transform(classLoader, dummyClass, null, null, bytes);
/* 23:   */     }
/* 24:   */     catch (Throwable ex)
/* 25:   */     {
/* 26:56 */       throw new IllegalArgumentException("Cannot load transformer", ex);
/* 27:   */     }
/* 28:   */   }
/* 29:   */   
/* 30:   */   public Object invoke(Object proxy, Method method, Object[] args)
/* 31:   */     throws Throwable
/* 32:   */   {
/* 33:62 */     String name = method.getName();
/* 34:63 */     if ("equals".equals(name))
/* 35:   */     {
/* 36:64 */       if (proxy == args[0]) {
/* 37:64 */         return Boolean.valueOf(true);
/* 38:   */       }
/* 39:64 */       return Boolean.valueOf(false);
/* 40:   */     }
/* 41:66 */     if ("hashCode".equals(name)) {
/* 42:67 */       return Integer.valueOf(hashCode());
/* 43:   */     }
/* 44:69 */     if ("toString".equals(name)) {
/* 45:70 */       return toString();
/* 46:   */     }
/* 47:72 */     if ("transformClass".equals(name)) {
/* 48:73 */       return transform((String)args[0], (byte[])args[1], (CodeSource)args[2], (ClassLoader)args[3]);
/* 49:   */     }
/* 50:76 */     throw new IllegalArgumentException("Unknown method: " + method);
/* 51:   */   }
/* 52:   */   
/* 53:   */   protected byte[] transform(String className, byte[] classfileBuffer, CodeSource codeSource, ClassLoader classLoader)
/* 54:   */     throws Exception
/* 55:   */   {
/* 56:84 */     byte[] result = this.transformer.transform(classLoader, className.replace('.', '/'), null, null, classfileBuffer);
/* 57:85 */     return result != null ? result : classfileBuffer;
/* 58:   */   }
/* 59:   */   
/* 60:   */   public String toString()
/* 61:   */   {
/* 62:90 */     StringBuilder builder = new StringBuilder(getClass().getName());
/* 63:91 */     builder.append(" for transformer: ");
/* 64:92 */     builder.append(this.transformer);
/* 65:93 */     return builder.toString();
/* 66:   */   }
/* 67:   */   
/* 68:   */   private static class Dummy {}
/* 69:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.websphere.WebSphereClassPreDefinePlugin
 * JD-Core Version:    0.7.0.1
 */