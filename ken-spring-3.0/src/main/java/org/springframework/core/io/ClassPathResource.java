/*   1:    */ package org.springframework.core.io;
/*   2:    */ 
/*   3:    */ import java.io.FileNotFoundException;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.net.URL;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ import org.springframework.util.ClassUtils;
/*   9:    */ import org.springframework.util.ObjectUtils;
/*  10:    */ import org.springframework.util.StringUtils;
/*  11:    */ 
/*  12:    */ public class ClassPathResource
/*  13:    */   extends AbstractFileResolvingResource
/*  14:    */ {
/*  15:    */   private final String path;
/*  16:    */   private ClassLoader classLoader;
/*  17:    */   private Class<?> clazz;
/*  18:    */   
/*  19:    */   public ClassPathResource(String path)
/*  20:    */   {
/*  21: 62 */     this(path, null);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public ClassPathResource(String path, ClassLoader classLoader)
/*  25:    */   {
/*  26: 75 */     Assert.notNull(path, "Path must not be null");
/*  27: 76 */     String pathToUse = StringUtils.cleanPath(path);
/*  28: 77 */     if (pathToUse.startsWith("/")) {
/*  29: 78 */       pathToUse = pathToUse.substring(1);
/*  30:    */     }
/*  31: 80 */     this.path = pathToUse;
/*  32: 81 */     this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
/*  33:    */   }
/*  34:    */   
/*  35:    */   public ClassPathResource(String path, Class<?> clazz)
/*  36:    */   {
/*  37: 93 */     Assert.notNull(path, "Path must not be null");
/*  38: 94 */     this.path = StringUtils.cleanPath(path);
/*  39: 95 */     this.clazz = clazz;
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected ClassPathResource(String path, ClassLoader classLoader, Class<?> clazz)
/*  43:    */   {
/*  44:106 */     this.path = StringUtils.cleanPath(path);
/*  45:107 */     this.classLoader = classLoader;
/*  46:108 */     this.clazz = clazz;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public final String getPath()
/*  50:    */   {
/*  51:116 */     return this.path;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public final ClassLoader getClassLoader()
/*  55:    */   {
/*  56:123 */     return this.classLoader != null ? this.classLoader : this.clazz.getClassLoader();
/*  57:    */   }
/*  58:    */   
/*  59:    */   public boolean exists()
/*  60:    */   {
/*  61:    */     URL url;
/*  62:    */     URL url;
/*  63:135 */     if (this.clazz != null) {
/*  64:136 */       url = this.clazz.getResource(this.path);
/*  65:    */     } else {
/*  66:139 */       url = this.classLoader.getResource(this.path);
/*  67:    */     }
/*  68:141 */     return url != null;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public InputStream getInputStream()
/*  72:    */     throws IOException
/*  73:    */   {
/*  74:    */     InputStream is;
/*  75:    */     InputStream is;
/*  76:151 */     if (this.clazz != null) {
/*  77:152 */       is = this.clazz.getResourceAsStream(this.path);
/*  78:    */     } else {
/*  79:155 */       is = this.classLoader.getResourceAsStream(this.path);
/*  80:    */     }
/*  81:157 */     if (is == null) {
/*  82:158 */       throw new FileNotFoundException(
/*  83:159 */         getDescription() + " cannot be opened because it does not exist");
/*  84:    */     }
/*  85:161 */     return is;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public URL getURL()
/*  89:    */     throws IOException
/*  90:    */   {
/*  91:    */     URL url;
/*  92:    */     URL url;
/*  93:172 */     if (this.clazz != null) {
/*  94:173 */       url = this.clazz.getResource(this.path);
/*  95:    */     } else {
/*  96:176 */       url = this.classLoader.getResource(this.path);
/*  97:    */     }
/*  98:178 */     if (url == null) {
/*  99:179 */       throw new FileNotFoundException(
/* 100:180 */         getDescription() + " cannot be resolved to URL because it does not exist");
/* 101:    */     }
/* 102:182 */     return url;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public Resource createRelative(String relativePath)
/* 106:    */   {
/* 107:192 */     String pathToUse = StringUtils.applyRelativePath(this.path, relativePath);
/* 108:193 */     return new ClassPathResource(pathToUse, this.classLoader, this.clazz);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public String getFilename()
/* 112:    */   {
/* 113:203 */     return StringUtils.getFilename(this.path);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public String getDescription()
/* 117:    */   {
/* 118:210 */     StringBuilder builder = new StringBuilder("class path resource [");
/* 119:212 */     if (this.clazz != null)
/* 120:    */     {
/* 121:213 */       builder.append(ClassUtils.classPackageAsResourcePath(this.clazz));
/* 122:214 */       builder.append('/');
/* 123:    */     }
/* 124:217 */     builder.append(this.path);
/* 125:218 */     builder.append(']');
/* 126:219 */     return builder.toString();
/* 127:    */   }
/* 128:    */   
/* 129:    */   public boolean equals(Object obj)
/* 130:    */   {
/* 131:228 */     if (obj == this) {
/* 132:229 */       return true;
/* 133:    */     }
/* 134:231 */     if ((obj instanceof ClassPathResource))
/* 135:    */     {
/* 136:232 */       ClassPathResource otherRes = (ClassPathResource)obj;
/* 137:    */       
/* 138:    */ 
/* 139:235 */       return (this.path.equals(otherRes.path)) && (ObjectUtils.nullSafeEquals(this.classLoader, otherRes.classLoader)) && (ObjectUtils.nullSafeEquals(this.clazz, otherRes.clazz));
/* 140:    */     }
/* 141:237 */     return false;
/* 142:    */   }
/* 143:    */   
/* 144:    */   public int hashCode()
/* 145:    */   {
/* 146:246 */     return this.path.hashCode();
/* 147:    */   }
/* 148:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.ClassPathResource
 * JD-Core Version:    0.7.0.1
 */