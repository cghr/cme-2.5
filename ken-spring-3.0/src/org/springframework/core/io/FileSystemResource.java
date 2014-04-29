/*   1:    */ package org.springframework.core.io;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileInputStream;
/*   5:    */ import java.io.FileOutputStream;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.InputStream;
/*   8:    */ import java.io.OutputStream;
/*   9:    */ import java.net.URI;
/*  10:    */ import java.net.URL;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.util.StringUtils;
/*  13:    */ 
/*  14:    */ public class FileSystemResource
/*  15:    */   extends AbstractResource
/*  16:    */   implements WritableResource
/*  17:    */ {
/*  18:    */   private final File file;
/*  19:    */   private final String path;
/*  20:    */   
/*  21:    */   public FileSystemResource(File file)
/*  22:    */   {
/*  23: 59 */     Assert.notNull(file, "File must not be null");
/*  24: 60 */     this.file = file;
/*  25: 61 */     this.path = StringUtils.cleanPath(file.getPath());
/*  26:    */   }
/*  27:    */   
/*  28:    */   public FileSystemResource(String path)
/*  29:    */   {
/*  30: 75 */     Assert.notNull(path, "Path must not be null");
/*  31: 76 */     this.file = new File(path);
/*  32: 77 */     this.path = StringUtils.cleanPath(path);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public final String getPath()
/*  36:    */   {
/*  37: 84 */     return this.path;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public boolean exists()
/*  41:    */   {
/*  42: 94 */     return this.file.exists();
/*  43:    */   }
/*  44:    */   
/*  45:    */   public boolean isReadable()
/*  46:    */   {
/*  47:105 */     return (this.file.canRead()) && (!this.file.isDirectory());
/*  48:    */   }
/*  49:    */   
/*  50:    */   public InputStream getInputStream()
/*  51:    */     throws IOException
/*  52:    */   {
/*  53:113 */     return new FileInputStream(this.file);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public URL getURL()
/*  57:    */     throws IOException
/*  58:    */   {
/*  59:122 */     return this.file.toURI().toURL();
/*  60:    */   }
/*  61:    */   
/*  62:    */   public URI getURI()
/*  63:    */     throws IOException
/*  64:    */   {
/*  65:131 */     return this.file.toURI();
/*  66:    */   }
/*  67:    */   
/*  68:    */   public File getFile()
/*  69:    */   {
/*  70:139 */     return this.file;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public Resource createRelative(String relativePath)
/*  74:    */   {
/*  75:149 */     String pathToUse = StringUtils.applyRelativePath(this.path, relativePath);
/*  76:150 */     return new FileSystemResource(pathToUse);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public String getFilename()
/*  80:    */   {
/*  81:159 */     return this.file.getName();
/*  82:    */   }
/*  83:    */   
/*  84:    */   public String getDescription()
/*  85:    */   {
/*  86:168 */     return "file [" + this.file.getAbsolutePath() + "]";
/*  87:    */   }
/*  88:    */   
/*  89:    */   public boolean isWritable()
/*  90:    */   {
/*  91:181 */     return (this.file.canWrite()) && (!this.file.isDirectory());
/*  92:    */   }
/*  93:    */   
/*  94:    */   public OutputStream getOutputStream()
/*  95:    */     throws IOException
/*  96:    */   {
/*  97:189 */     return new FileOutputStream(this.file);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public boolean equals(Object obj)
/* 101:    */   {
/* 102:199 */     return (obj == this) || (((obj instanceof FileSystemResource)) && (this.path.equals(((FileSystemResource)obj).path)));
/* 103:    */   }
/* 104:    */   
/* 105:    */   public int hashCode()
/* 106:    */   {
/* 107:207 */     return this.path.hashCode();
/* 108:    */   }
/* 109:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.FileSystemResource
 * JD-Core Version:    0.7.0.1
 */