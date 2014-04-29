/*   1:    */ package org.springframework.instrument.classloading;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.lang.instrument.ClassFileTransformer;
/*   6:    */ import java.lang.instrument.IllegalClassFormatException;
/*   7:    */ import java.net.URL;
/*   8:    */ import java.util.Enumeration;
/*   9:    */ import java.util.HashMap;
/*  10:    */ import java.util.LinkedList;
/*  11:    */ import java.util.List;
/*  12:    */ import java.util.Map;
/*  13:    */ import org.springframework.core.DecoratingClassLoader;
/*  14:    */ import org.springframework.util.Assert;
/*  15:    */ import org.springframework.util.FileCopyUtils;
/*  16:    */ import org.springframework.util.StringUtils;
/*  17:    */ 
/*  18:    */ public class ShadowingClassLoader
/*  19:    */   extends DecoratingClassLoader
/*  20:    */ {
/*  21: 50 */   public static final String[] DEFAULT_EXCLUDED_PACKAGES = { "java.", "javax.", "sun.", "oracle.", "com.sun.", "com.ibm.", "COM.ibm.", 
/*  22: 51 */     "org.w3c.", "org.xml.", "org.dom4j.", "org.eclipse", "org.aspectj.", "net.sf.cglib.", 
/*  23: 52 */     "org.apache.xerces.", "org.apache.commons.logging." };
/*  24:    */   private final ClassLoader enclosingClassLoader;
/*  25: 57 */   private final List<ClassFileTransformer> classFileTransformers = new LinkedList();
/*  26: 59 */   private final Map<String, Class> classCache = new HashMap();
/*  27:    */   
/*  28:    */   public ShadowingClassLoader(ClassLoader enclosingClassLoader)
/*  29:    */   {
/*  30: 67 */     Assert.notNull(enclosingClassLoader, "Enclosing ClassLoader must not be null");
/*  31: 68 */     this.enclosingClassLoader = enclosingClassLoader;
/*  32: 69 */     for (String excludedPackage : DEFAULT_EXCLUDED_PACKAGES) {
/*  33: 70 */       excludePackage(excludedPackage);
/*  34:    */     }
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void addTransformer(ClassFileTransformer transformer)
/*  38:    */   {
/*  39: 81 */     Assert.notNull(transformer, "Transformer must not be null");
/*  40: 82 */     this.classFileTransformers.add(transformer);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void copyTransformers(ShadowingClassLoader other)
/*  44:    */   {
/*  45: 91 */     Assert.notNull(other, "Other ClassLoader must not be null");
/*  46: 92 */     this.classFileTransformers.addAll(other.classFileTransformers);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public Class<?> loadClass(String name)
/*  50:    */     throws ClassNotFoundException
/*  51:    */   {
/*  52: 98 */     if (shouldShadow(name))
/*  53:    */     {
/*  54: 99 */       Class cls = (Class)this.classCache.get(name);
/*  55:100 */       if (cls != null) {
/*  56:101 */         return cls;
/*  57:    */       }
/*  58:103 */       return doLoadClass(name);
/*  59:    */     }
/*  60:106 */     return this.enclosingClassLoader.loadClass(name);
/*  61:    */   }
/*  62:    */   
/*  63:    */   private boolean shouldShadow(String className)
/*  64:    */   {
/*  65:117 */     return (!className.equals(getClass().getName())) && (!className.endsWith("ShadowingClassLoader")) && (isEligibleForShadowing(className));
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected boolean isEligibleForShadowing(String className)
/*  69:    */   {
/*  70:128 */     return !isExcluded(className);
/*  71:    */   }
/*  72:    */   
/*  73:    */   private Class doLoadClass(String name)
/*  74:    */     throws ClassNotFoundException
/*  75:    */   {
/*  76:133 */     String internalName = StringUtils.replace(name, ".", "/") + ".class";
/*  77:134 */     InputStream is = this.enclosingClassLoader.getResourceAsStream(internalName);
/*  78:135 */     if (is == null) {
/*  79:136 */       throw new ClassNotFoundException(name);
/*  80:    */     }
/*  81:    */     try
/*  82:    */     {
/*  83:139 */       byte[] bytes = FileCopyUtils.copyToByteArray(is);
/*  84:140 */       bytes = applyTransformers(name, bytes);
/*  85:141 */       Class cls = defineClass(name, bytes, 0, bytes.length);
/*  86:143 */       if (cls.getPackage() == null)
/*  87:    */       {
/*  88:144 */         int packageSeparator = name.lastIndexOf('.');
/*  89:145 */         if (packageSeparator != -1)
/*  90:    */         {
/*  91:146 */           String packageName = name.substring(0, packageSeparator);
/*  92:147 */           definePackage(packageName, null, null, null, null, null, null, null);
/*  93:    */         }
/*  94:    */       }
/*  95:150 */       this.classCache.put(name, cls);
/*  96:151 */       return cls;
/*  97:    */     }
/*  98:    */     catch (IOException ex)
/*  99:    */     {
/* 100:154 */       throw new ClassNotFoundException("Cannot load resource for class [" + name + "]", ex);
/* 101:    */     }
/* 102:    */   }
/* 103:    */   
/* 104:    */   private byte[] applyTransformers(String name, byte[] bytes)
/* 105:    */   {
/* 106:159 */     String internalName = StringUtils.replace(name, ".", "/");
/* 107:    */     try
/* 108:    */     {
/* 109:161 */       for (ClassFileTransformer transformer : this.classFileTransformers)
/* 110:    */       {
/* 111:162 */         byte[] transformed = transformer.transform(this, internalName, null, null, bytes);
/* 112:163 */         bytes = transformed != null ? transformed : bytes;
/* 113:    */       }
/* 114:165 */       return bytes;
/* 115:    */     }
/* 116:    */     catch (IllegalClassFormatException ex)
/* 117:    */     {
/* 118:168 */       throw new IllegalStateException(ex);
/* 119:    */     }
/* 120:    */   }
/* 121:    */   
/* 122:    */   public URL getResource(String name)
/* 123:    */   {
/* 124:175 */     return this.enclosingClassLoader.getResource(name);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public InputStream getResourceAsStream(String name)
/* 128:    */   {
/* 129:180 */     return this.enclosingClassLoader.getResourceAsStream(name);
/* 130:    */   }
/* 131:    */   
/* 132:    */   public Enumeration<URL> getResources(String name)
/* 133:    */     throws IOException
/* 134:    */   {
/* 135:185 */     return this.enclosingClassLoader.getResources(name);
/* 136:    */   }
/* 137:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.ShadowingClassLoader
 * JD-Core Version:    0.7.0.1
 */