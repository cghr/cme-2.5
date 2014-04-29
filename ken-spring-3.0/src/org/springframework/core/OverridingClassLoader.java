/*   1:    */ package org.springframework.core;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import org.springframework.util.FileCopyUtils;
/*   6:    */ 
/*   7:    */ public class OverridingClassLoader
/*   8:    */   extends DecoratingClassLoader
/*   9:    */ {
/*  10: 40 */   public static final String[] DEFAULT_EXCLUDED_PACKAGES = { "java.", "javax.", "sun.", "oracle." };
/*  11:    */   private static final String CLASS_FILE_SUFFIX = ".class";
/*  12:    */   
/*  13:    */   public OverridingClassLoader(ClassLoader parent)
/*  14:    */   {
/*  15: 50 */     super(parent);
/*  16: 51 */     for (String packageName : DEFAULT_EXCLUDED_PACKAGES) {
/*  17: 52 */       excludePackage(packageName);
/*  18:    */     }
/*  19:    */   }
/*  20:    */   
/*  21:    */   protected Class loadClass(String name, boolean resolve)
/*  22:    */     throws ClassNotFoundException
/*  23:    */   {
/*  24: 59 */     Class result = null;
/*  25: 60 */     if (isEligibleForOverriding(name)) {
/*  26: 61 */       result = loadClassForOverriding(name);
/*  27:    */     }
/*  28: 63 */     if (result != null)
/*  29:    */     {
/*  30: 64 */       if (resolve) {
/*  31: 65 */         resolveClass(result);
/*  32:    */       }
/*  33: 67 */       return result;
/*  34:    */     }
/*  35: 70 */     return super.loadClass(name, resolve);
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected boolean isEligibleForOverriding(String className)
/*  39:    */   {
/*  40: 82 */     return !isExcluded(className);
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected Class loadClassForOverriding(String name)
/*  44:    */     throws ClassNotFoundException
/*  45:    */   {
/*  46: 94 */     Class result = findLoadedClass(name);
/*  47: 95 */     if (result == null)
/*  48:    */     {
/*  49: 96 */       byte[] bytes = loadBytesForClass(name);
/*  50: 97 */       if (bytes != null) {
/*  51: 98 */         result = defineClass(name, bytes, 0, bytes.length);
/*  52:    */       }
/*  53:    */     }
/*  54:101 */     return result;
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected byte[] loadBytesForClass(String name)
/*  58:    */     throws ClassNotFoundException
/*  59:    */   {
/*  60:115 */     InputStream is = openStreamForClass(name);
/*  61:116 */     if (is == null) {
/*  62:117 */       return null;
/*  63:    */     }
/*  64:    */     try
/*  65:    */     {
/*  66:121 */       byte[] bytes = FileCopyUtils.copyToByteArray(is);
/*  67:    */       
/*  68:123 */       return transformIfNecessary(name, bytes);
/*  69:    */     }
/*  70:    */     catch (IOException ex)
/*  71:    */     {
/*  72:126 */       throw new ClassNotFoundException("Cannot load resource for class [" + name + "]", ex);
/*  73:    */     }
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected InputStream openStreamForClass(String name)
/*  77:    */   {
/*  78:138 */     String internalName = name.replace('.', '/') + ".class";
/*  79:139 */     return getParent().getResourceAsStream(internalName);
/*  80:    */   }
/*  81:    */   
/*  82:    */   protected byte[] transformIfNecessary(String name, byte[] bytes)
/*  83:    */   {
/*  84:152 */     return bytes;
/*  85:    */   }
/*  86:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.OverridingClassLoader
 * JD-Core Version:    0.7.0.1
 */