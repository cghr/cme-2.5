/*  1:   */ package org.springframework.instrument.classloading.jboss;
/*  2:   */ 
/*  3:   */ import java.lang.instrument.ClassFileTransformer;
/*  4:   */ import java.lang.reflect.InvocationHandler;
/*  5:   */ import java.lang.reflect.Method;
/*  6:   */ import java.security.ProtectionDomain;
/*  7:   */ 
/*  8:   */ class JBossMCTranslatorAdapter
/*  9:   */   implements InvocationHandler
/* 10:   */ {
/* 11:   */   private final ClassFileTransformer transformer;
/* 12:   */   
/* 13:   */   public JBossMCTranslatorAdapter(ClassFileTransformer transformer)
/* 14:   */   {
/* 15:43 */     this.transformer = transformer;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public Object invoke(Object proxy, Method method, Object[] args)
/* 19:   */     throws Throwable
/* 20:   */   {
/* 21:47 */     String name = method.getName();
/* 22:49 */     if ("equals".equals(name)) {
/* 23:50 */       return Boolean.valueOf(proxy == args[0]);
/* 24:   */     }
/* 25:51 */     if ("hashCode".equals(name)) {
/* 26:52 */       return Integer.valueOf(hashCode());
/* 27:   */     }
/* 28:53 */     if ("toString".equals(name)) {
/* 29:54 */       return toString();
/* 30:   */     }
/* 31:55 */     if ("transform".equals(name)) {
/* 32:56 */       return transform((ClassLoader)args[0], (String)args[1], (Class)args[2], (ProtectionDomain)args[3], 
/* 33:57 */         (byte[])args[4]);
/* 34:   */     }
/* 35:58 */     if ("unregisterClassLoader".equals(name))
/* 36:   */     {
/* 37:59 */       unregisterClassLoader((ClassLoader)args[0]);
/* 38:60 */       return null;
/* 39:   */     }
/* 40:63 */     throw new IllegalArgumentException("Unknown method: " + method);
/* 41:   */   }
/* 42:   */   
/* 43:   */   public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
/* 44:   */     throws Exception
/* 45:   */   {
/* 46:69 */     return this.transformer.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
/* 47:   */   }
/* 48:   */   
/* 49:   */   public void unregisterClassLoader(ClassLoader loader) {}
/* 50:   */   
/* 51:   */   public String toString()
/* 52:   */   {
/* 53:77 */     StringBuilder builder = new StringBuilder(getClass().getName());
/* 54:78 */     builder.append(" for transformer: ");
/* 55:79 */     builder.append(this.transformer);
/* 56:80 */     return builder.toString();
/* 57:   */   }
/* 58:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.jboss.JBossMCTranslatorAdapter
 * JD-Core Version:    0.7.0.1
 */