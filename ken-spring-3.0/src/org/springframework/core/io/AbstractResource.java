/*   1:    */ package org.springframework.core.io;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileNotFoundException;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.net.URI;
/*   8:    */ import java.net.URISyntaxException;
/*   9:    */ import java.net.URL;
/*  10:    */ import org.springframework.core.NestedIOException;
/*  11:    */ import org.springframework.util.ResourceUtils;
/*  12:    */ 
/*  13:    */ public abstract class AbstractResource
/*  14:    */   implements Resource
/*  15:    */ {
/*  16:    */   public boolean exists()
/*  17:    */   {
/*  18:    */     try
/*  19:    */     {
/*  20: 51 */       return getFile().exists();
/*  21:    */     }
/*  22:    */     catch (IOException localIOException)
/*  23:    */     {
/*  24:    */       try
/*  25:    */       {
/*  26: 56 */         InputStream is = getInputStream();
/*  27: 57 */         is.close();
/*  28: 58 */         return true;
/*  29:    */       }
/*  30:    */       catch (Throwable localThrowable) {}
/*  31:    */     }
/*  32: 61 */     return false;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public boolean isReadable()
/*  36:    */   {
/*  37: 70 */     return true;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public boolean isOpen()
/*  41:    */   {
/*  42: 77 */     return false;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public URL getURL()
/*  46:    */     throws IOException
/*  47:    */   {
/*  48: 85 */     throw new FileNotFoundException(getDescription() + " cannot be resolved to URL");
/*  49:    */   }
/*  50:    */   
/*  51:    */   public URI getURI()
/*  52:    */     throws IOException
/*  53:    */   {
/*  54: 93 */     URL url = getURL();
/*  55:    */     try
/*  56:    */     {
/*  57: 95 */       return ResourceUtils.toURI(url);
/*  58:    */     }
/*  59:    */     catch (URISyntaxException ex)
/*  60:    */     {
/*  61: 98 */       throw new NestedIOException("Invalid URI [" + url + "]", ex);
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   public File getFile()
/*  66:    */     throws IOException
/*  67:    */   {
/*  68:107 */     throw new FileNotFoundException(getDescription() + " cannot be resolved to absolute file path");
/*  69:    */   }
/*  70:    */   
/*  71:    */   public long contentLength()
/*  72:    */     throws IOException
/*  73:    */   {
/*  74:116 */     return getFile().length();
/*  75:    */   }
/*  76:    */   
/*  77:    */   public long lastModified()
/*  78:    */     throws IOException
/*  79:    */   {
/*  80:125 */     long lastModified = getFileForLastModifiedCheck().lastModified();
/*  81:126 */     if (lastModified == 0L) {
/*  82:127 */       throw new FileNotFoundException(getDescription() + 
/*  83:128 */         " cannot be resolved in the file system for resolving its last-modified timestamp");
/*  84:    */     }
/*  85:130 */     return lastModified;
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected File getFileForLastModifiedCheck()
/*  89:    */     throws IOException
/*  90:    */   {
/*  91:141 */     return getFile();
/*  92:    */   }
/*  93:    */   
/*  94:    */   public Resource createRelative(String relativePath)
/*  95:    */     throws IOException
/*  96:    */   {
/*  97:149 */     throw new FileNotFoundException("Cannot create a relative resource for " + getDescription());
/*  98:    */   }
/*  99:    */   
/* 100:    */   public String getFilename()
/* 101:    */     throws IllegalStateException
/* 102:    */   {
/* 103:157 */     throw new IllegalStateException(getDescription() + " does not have a filename");
/* 104:    */   }
/* 105:    */   
/* 106:    */   public String toString()
/* 107:    */   {
/* 108:167 */     return getDescription();
/* 109:    */   }
/* 110:    */   
/* 111:    */   public boolean equals(Object obj)
/* 112:    */   {
/* 113:177 */     return (obj == this) || (((obj instanceof Resource)) && (((Resource)obj).getDescription().equals(getDescription())));
/* 114:    */   }
/* 115:    */   
/* 116:    */   public int hashCode()
/* 117:    */   {
/* 118:186 */     return getDescription().hashCode();
/* 119:    */   }
/* 120:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.AbstractResource
 * JD-Core Version:    0.7.0.1
 */