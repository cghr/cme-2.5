/*   1:    */ package org.springframework.remoting.rmi;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.rmi.server.RMIClassLoader;
/*   6:    */ import org.springframework.core.ConfigurableObjectInputStream;
/*   7:    */ 
/*   8:    */ public class CodebaseAwareObjectInputStream
/*   9:    */   extends ConfigurableObjectInputStream
/*  10:    */ {
/*  11:    */   private final String codebaseUrl;
/*  12:    */   
/*  13:    */   public CodebaseAwareObjectInputStream(InputStream in, String codebaseUrl)
/*  14:    */     throws IOException
/*  15:    */   {
/*  16: 66 */     this(in, null, codebaseUrl);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public CodebaseAwareObjectInputStream(InputStream in, ClassLoader classLoader, String codebaseUrl)
/*  20:    */     throws IOException
/*  21:    */   {
/*  22: 81 */     super(in, classLoader);
/*  23: 82 */     this.codebaseUrl = codebaseUrl;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public CodebaseAwareObjectInputStream(InputStream in, ClassLoader classLoader, boolean acceptProxyClasses)
/*  27:    */     throws IOException
/*  28:    */   {
/*  29: 97 */     super(in, classLoader, acceptProxyClasses);
/*  30: 98 */     this.codebaseUrl = null;
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected Class resolveFallbackIfPossible(String className, ClassNotFoundException ex)
/*  34:    */     throws IOException, ClassNotFoundException
/*  35:    */   {
/*  36:108 */     if (this.codebaseUrl == null) {
/*  37:109 */       throw ex;
/*  38:    */     }
/*  39:111 */     return RMIClassLoader.loadClass(this.codebaseUrl, className);
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected ClassLoader getFallbackClassLoader()
/*  43:    */     throws IOException
/*  44:    */   {
/*  45:116 */     return RMIClassLoader.getClassLoader(this.codebaseUrl);
/*  46:    */   }
/*  47:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.rmi.CodebaseAwareObjectInputStream
 * JD-Core Version:    0.7.0.1
 */