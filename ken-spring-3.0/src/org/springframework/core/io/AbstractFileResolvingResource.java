/*   1:    */ package org.springframework.core.io;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.net.HttpURLConnection;
/*   7:    */ import java.net.URI;
/*   8:    */ import java.net.URL;
/*   9:    */ import java.net.URLConnection;
/*  10:    */ import org.springframework.util.ResourceUtils;
/*  11:    */ 
/*  12:    */ public abstract class AbstractFileResolvingResource
/*  13:    */   extends AbstractResource
/*  14:    */ {
/*  15:    */   public File getFile()
/*  16:    */     throws IOException
/*  17:    */   {
/*  18: 48 */     URL url = getURL();
/*  19: 49 */     if (url.getProtocol().startsWith("vfs")) {
/*  20: 50 */       return VfsResourceDelegate.getResource(url).getFile();
/*  21:    */     }
/*  22: 52 */     return ResourceUtils.getFile(url, getDescription());
/*  23:    */   }
/*  24:    */   
/*  25:    */   protected File getFileForLastModifiedCheck()
/*  26:    */     throws IOException
/*  27:    */   {
/*  28: 61 */     URL url = getURL();
/*  29: 62 */     if (ResourceUtils.isJarURL(url))
/*  30:    */     {
/*  31: 63 */       URL actualUrl = ResourceUtils.extractJarFileURL(url);
/*  32: 64 */       if (actualUrl.getProtocol().startsWith("vfs")) {
/*  33: 65 */         return VfsResourceDelegate.getResource(actualUrl).getFile();
/*  34:    */       }
/*  35: 67 */       return ResourceUtils.getFile(actualUrl, "Jar URL");
/*  36:    */     }
/*  37: 70 */     return getFile();
/*  38:    */   }
/*  39:    */   
/*  40:    */   protected File getFile(URI uri)
/*  41:    */     throws IOException
/*  42:    */   {
/*  43: 80 */     if (uri.getScheme().startsWith("vfs")) {
/*  44: 81 */       return VfsResourceDelegate.getResource(uri).getFile();
/*  45:    */     }
/*  46: 83 */     return ResourceUtils.getFile(uri, getDescription());
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean exists()
/*  50:    */   {
/*  51:    */     try
/*  52:    */     {
/*  53: 90 */       URL url = getURL();
/*  54: 91 */       if (ResourceUtils.isFileURL(url)) {
/*  55: 93 */         return getFile().exists();
/*  56:    */       }
/*  57: 97 */       URLConnection con = url.openConnection();
/*  58: 98 */       con.setUseCaches(false);
/*  59: 99 */       HttpURLConnection httpCon = 
/*  60:100 */         (con instanceof HttpURLConnection) ? (HttpURLConnection)con : null;
/*  61:101 */       if (httpCon != null)
/*  62:    */       {
/*  63:102 */         httpCon.setRequestMethod("HEAD");
/*  64:103 */         if (httpCon.getResponseCode() == 200) {
/*  65:104 */           return true;
/*  66:    */         }
/*  67:    */       }
/*  68:107 */       if (con.getContentLength() >= 0) {
/*  69:108 */         return true;
/*  70:    */       }
/*  71:110 */       if (httpCon != null)
/*  72:    */       {
/*  73:112 */         httpCon.disconnect();
/*  74:113 */         return false;
/*  75:    */       }
/*  76:117 */       InputStream is = getInputStream();
/*  77:118 */       is.close();
/*  78:119 */       return true;
/*  79:    */     }
/*  80:    */     catch (IOException localIOException) {}
/*  81:124 */     return false;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public boolean isReadable()
/*  85:    */   {
/*  86:    */     try
/*  87:    */     {
/*  88:131 */       URL url = getURL();
/*  89:132 */       if (ResourceUtils.isFileURL(url))
/*  90:    */       {
/*  91:134 */         File file = getFile();
/*  92:135 */         return (file.canRead()) && (!file.isDirectory());
/*  93:    */       }
/*  94:138 */       return true;
/*  95:    */     }
/*  96:    */     catch (IOException localIOException) {}
/*  97:142 */     return false;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public long contentLength()
/* 101:    */     throws IOException
/* 102:    */   {
/* 103:148 */     URL url = getURL();
/* 104:149 */     if (ResourceUtils.isFileURL(url)) {
/* 105:151 */       return super.contentLength();
/* 106:    */     }
/* 107:155 */     URLConnection con = url.openConnection();
/* 108:156 */     con.setUseCaches(false);
/* 109:157 */     if ((con instanceof HttpURLConnection)) {
/* 110:158 */       ((HttpURLConnection)con).setRequestMethod("HEAD");
/* 111:    */     }
/* 112:160 */     return con.getContentLength();
/* 113:    */   }
/* 114:    */   
/* 115:    */   public long lastModified()
/* 116:    */     throws IOException
/* 117:    */   {
/* 118:166 */     URL url = getURL();
/* 119:167 */     if ((ResourceUtils.isFileURL(url)) || (ResourceUtils.isJarURL(url))) {
/* 120:169 */       return super.lastModified();
/* 121:    */     }
/* 122:173 */     URLConnection con = url.openConnection();
/* 123:174 */     con.setUseCaches(false);
/* 124:175 */     if ((con instanceof HttpURLConnection)) {
/* 125:176 */       ((HttpURLConnection)con).setRequestMethod("HEAD");
/* 126:    */     }
/* 127:178 */     return con.getLastModified();
/* 128:    */   }
/* 129:    */   
/* 130:    */   private static class VfsResourceDelegate
/* 131:    */   {
/* 132:    */     public static Resource getResource(URL url)
/* 133:    */       throws IOException
/* 134:    */     {
/* 135:189 */       return new VfsResource(VfsUtils.getRoot(url));
/* 136:    */     }
/* 137:    */     
/* 138:    */     public static Resource getResource(URI uri)
/* 139:    */       throws IOException
/* 140:    */     {
/* 141:193 */       return new VfsResource(VfsUtils.getRoot(uri));
/* 142:    */     }
/* 143:    */   }
/* 144:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.AbstractFileResolvingResource
 * JD-Core Version:    0.7.0.1
 */