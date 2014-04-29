/*  1:   */ package org.springframework.instrument.classloading.weblogic;
/*  2:   */ 
/*  3:   */ import java.lang.instrument.ClassFileTransformer;
/*  4:   */ import java.lang.instrument.IllegalClassFormatException;
/*  5:   */ import java.lang.reflect.InvocationHandler;
/*  6:   */ import java.lang.reflect.Method;
/*  7:   */ import java.util.Hashtable;
/*  8:   */ 
/*  9:   */ class WebLogicClassPreProcessorAdapter
/* 10:   */   implements InvocationHandler
/* 11:   */ {
/* 12:   */   private final ClassFileTransformer transformer;
/* 13:   */   private final ClassLoader loader;
/* 14:   */   
/* 15:   */   public WebLogicClassPreProcessorAdapter(ClassFileTransformer transformer, ClassLoader loader)
/* 16:   */   {
/* 17:48 */     this.transformer = transformer;
/* 18:49 */     this.loader = loader;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public Object invoke(Object proxy, Method method, Object[] args)
/* 22:   */     throws Throwable
/* 23:   */   {
/* 24:53 */     String name = method.getName();
/* 25:55 */     if ("equals".equals(name)) {
/* 26:56 */       return Boolean.valueOf(proxy == args[0]);
/* 27:   */     }
/* 28:57 */     if ("hashCode".equals(name)) {
/* 29:58 */       return Integer.valueOf(hashCode());
/* 30:   */     }
/* 31:59 */     if ("toString".equals(name)) {
/* 32:60 */       return toString();
/* 33:   */     }
/* 34:61 */     if ("initialize".equals(name))
/* 35:   */     {
/* 36:62 */       initialize((Hashtable)args[0]);
/* 37:63 */       return null;
/* 38:   */     }
/* 39:64 */     if ("preProcess".equals(name)) {
/* 40:65 */       return preProcess((String)args[0], (byte[])args[1]);
/* 41:   */     }
/* 42:67 */     throw new IllegalArgumentException("Unknown method: " + method);
/* 43:   */   }
/* 44:   */   
/* 45:   */   public void initialize(Hashtable params) {}
/* 46:   */   
/* 47:   */   public byte[] preProcess(String className, byte[] classBytes)
/* 48:   */   {
/* 49:   */     try
/* 50:   */     {
/* 51:76 */       byte[] result = this.transformer.transform(this.loader, className, null, null, classBytes);
/* 52:77 */       return result != null ? result : classBytes;
/* 53:   */     }
/* 54:   */     catch (IllegalClassFormatException ex)
/* 55:   */     {
/* 56:79 */       throw new IllegalStateException("Cannot transform due to illegal class format", ex);
/* 57:   */     }
/* 58:   */   }
/* 59:   */   
/* 60:   */   public String toString()
/* 61:   */   {
/* 62:85 */     StringBuilder builder = new StringBuilder(getClass().getName());
/* 63:86 */     builder.append(" for transformer: ");
/* 64:87 */     builder.append(this.transformer);
/* 65:88 */     return builder.toString();
/* 66:   */   }
/* 67:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.weblogic.WebLogicClassPreProcessorAdapter
 * JD-Core Version:    0.7.0.1
 */