/*   1:    */ package org.springframework.core;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.NotSerializableException;
/*   6:    */ import java.io.ObjectInputStream;
/*   7:    */ import java.io.ObjectStreamClass;
/*   8:    */ import java.lang.reflect.Proxy;
/*   9:    */ import org.springframework.util.ClassUtils;
/*  10:    */ 
/*  11:    */ public class ConfigurableObjectInputStream
/*  12:    */   extends ObjectInputStream
/*  13:    */ {
/*  14:    */   private final ClassLoader classLoader;
/*  15:    */   private final boolean acceptProxyClasses;
/*  16:    */   
/*  17:    */   public ConfigurableObjectInputStream(InputStream in, ClassLoader classLoader)
/*  18:    */     throws IOException
/*  19:    */   {
/*  20: 50 */     this(in, classLoader, true);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public ConfigurableObjectInputStream(InputStream in, ClassLoader classLoader, boolean acceptProxyClasses)
/*  24:    */     throws IOException
/*  25:    */   {
/*  26: 64 */     super(in);
/*  27: 65 */     this.classLoader = classLoader;
/*  28: 66 */     this.acceptProxyClasses = acceptProxyClasses;
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected Class resolveClass(ObjectStreamClass classDesc)
/*  32:    */     throws IOException, ClassNotFoundException
/*  33:    */   {
/*  34:    */     try
/*  35:    */     {
/*  36: 73 */       if (this.classLoader != null) {
/*  37: 75 */         return ClassUtils.forName(classDesc.getName(), this.classLoader);
/*  38:    */       }
/*  39: 79 */       return super.resolveClass(classDesc);
/*  40:    */     }
/*  41:    */     catch (ClassNotFoundException ex)
/*  42:    */     {
/*  43: 83 */       return resolveFallbackIfPossible(classDesc.getName(), ex);
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected Class resolveProxyClass(String[] interfaces)
/*  48:    */     throws IOException, ClassNotFoundException
/*  49:    */   {
/*  50: 89 */     if (!this.acceptProxyClasses) {
/*  51: 90 */       throw new NotSerializableException("Not allowed to accept serialized proxy classes");
/*  52:    */     }
/*  53: 92 */     if (this.classLoader != null)
/*  54:    */     {
/*  55: 94 */       Class[] resolvedInterfaces = new Class[interfaces.length];
/*  56: 95 */       for (int i = 0; i < interfaces.length; i++) {
/*  57:    */         try
/*  58:    */         {
/*  59: 97 */           resolvedInterfaces[i] = ClassUtils.forName(interfaces[i], this.classLoader);
/*  60:    */         }
/*  61:    */         catch (ClassNotFoundException ex)
/*  62:    */         {
/*  63:100 */           resolvedInterfaces[i] = resolveFallbackIfPossible(interfaces[i], ex);
/*  64:    */         }
/*  65:    */       }
/*  66:    */       try
/*  67:    */       {
/*  68:104 */         return Proxy.getProxyClass(this.classLoader, resolvedInterfaces);
/*  69:    */       }
/*  70:    */       catch (IllegalArgumentException ex)
/*  71:    */       {
/*  72:107 */         throw new ClassNotFoundException(null, ex);
/*  73:    */       }
/*  74:    */     }
/*  75:    */     try
/*  76:    */     {
/*  77:113 */       return super.resolveProxyClass(interfaces);
/*  78:    */     }
/*  79:    */     catch (ClassNotFoundException ex)
/*  80:    */     {
/*  81:116 */       Class[] resolvedInterfaces = new Class[interfaces.length];
/*  82:117 */       for (int i = 0; i < interfaces.length; i++) {
/*  83:118 */         resolvedInterfaces[i] = resolveFallbackIfPossible(interfaces[i], ex);
/*  84:    */       }
/*  85:120 */       return Proxy.getProxyClass(getFallbackClassLoader(), resolvedInterfaces);
/*  86:    */     }
/*  87:    */   }
/*  88:    */   
/*  89:    */   protected Class resolveFallbackIfPossible(String className, ClassNotFoundException ex)
/*  90:    */     throws IOException, ClassNotFoundException
/*  91:    */   {
/*  92:137 */     throw ex;
/*  93:    */   }
/*  94:    */   
/*  95:    */   protected ClassLoader getFallbackClassLoader()
/*  96:    */     throws IOException
/*  97:    */   {
/*  98:146 */     return null;
/*  99:    */   }
/* 100:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.ConfigurableObjectInputStream
 * JD-Core Version:    0.7.0.1
 */