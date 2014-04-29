/*  1:   */ package org.springframework.instrument.classloading.oc4j;
/*  2:   */ 
/*  3:   */ import java.lang.instrument.ClassFileTransformer;
/*  4:   */ import java.lang.instrument.IllegalClassFormatException;
/*  5:   */ import java.lang.reflect.InvocationHandler;
/*  6:   */ import java.lang.reflect.Method;
/*  7:   */ import java.security.ProtectionDomain;
/*  8:   */ 
/*  9:   */ class OC4JClassPreprocessorAdapter
/* 10:   */   implements InvocationHandler
/* 11:   */ {
/* 12:   */   private final ClassFileTransformer transformer;
/* 13:   */   
/* 14:   */   public OC4JClassPreprocessorAdapter(ClassFileTransformer transformer)
/* 15:   */   {
/* 16:43 */     this.transformer = transformer;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public Object invoke(Object proxy, Method method, Object[] args)
/* 20:   */     throws Throwable
/* 21:   */   {
/* 22:47 */     String name = method.getName();
/* 23:49 */     if ("equals".equals(name)) {
/* 24:50 */       return Boolean.valueOf(proxy == args[0]);
/* 25:   */     }
/* 26:51 */     if ("hashCode".equals(name)) {
/* 27:52 */       return Integer.valueOf(hashCode());
/* 28:   */     }
/* 29:53 */     if ("toString".equals(name)) {
/* 30:54 */       return toString();
/* 31:   */     }
/* 32:55 */     if ("initialize".equals(name))
/* 33:   */     {
/* 34:56 */       initialize(proxy, (ClassLoader)args[0]);
/* 35:57 */       return null;
/* 36:   */     }
/* 37:58 */     if ("processClass".equals(name)) {
/* 38:59 */       return processClass((String)args[0], (byte[])args[1], ((Integer)args[2]).intValue(), ((Integer)args[3]).intValue(), 
/* 39:60 */         (ProtectionDomain)args[4], (ClassLoader)args[5]);
/* 40:   */     }
/* 41:62 */     throw new IllegalArgumentException("Unknown method: " + method);
/* 42:   */   }
/* 43:   */   
/* 44:   */   public Object initialize(Object proxy, ClassLoader loader)
/* 45:   */   {
/* 46:70 */     return proxy;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public byte[] processClass(String className, byte[] origClassBytes, int offset, int length, ProtectionDomain pd, ClassLoader loader)
/* 50:   */   {
/* 51:   */     try
/* 52:   */     {
/* 53:76 */       byte[] tempArray = new byte[length];
/* 54:77 */       System.arraycopy(origClassBytes, offset, tempArray, 0, length);
/* 55:   */       
/* 56:   */ 
/* 57:   */ 
/* 58:81 */       byte[] result = this.transformer.transform(loader, className.replace('.', '/'), null, pd, tempArray);
/* 59:82 */       return result != null ? result : origClassBytes;
/* 60:   */     }
/* 61:   */     catch (IllegalClassFormatException ex)
/* 62:   */     {
/* 63:84 */       throw new IllegalStateException("Cannot transform because of illegal class format", ex);
/* 64:   */     }
/* 65:   */   }
/* 66:   */   
/* 67:   */   public String toString()
/* 68:   */   {
/* 69:90 */     StringBuilder builder = new StringBuilder(getClass().getName());
/* 70:91 */     builder.append(" for transformer: ");
/* 71:92 */     builder.append(this.transformer);
/* 72:93 */     return builder.toString();
/* 73:   */   }
/* 74:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.oc4j.OC4JClassPreprocessorAdapter
 * JD-Core Version:    0.7.0.1
 */