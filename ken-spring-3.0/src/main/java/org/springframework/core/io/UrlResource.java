/*   1:    */ package org.springframework.core.io;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.net.HttpURLConnection;
/*   7:    */ import java.net.MalformedURLException;
/*   8:    */ import java.net.URI;
/*   9:    */ import java.net.URL;
/*  10:    */ import java.net.URLConnection;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.util.StringUtils;
/*  13:    */ 
/*  14:    */ public class UrlResource
/*  15:    */   extends AbstractFileResolvingResource
/*  16:    */ {
/*  17:    */   private final URL url;
/*  18:    */   private final URL cleanedUrl;
/*  19:    */   private final URI uri;
/*  20:    */   
/*  21:    */   public UrlResource(URL url)
/*  22:    */   {
/*  23: 63 */     Assert.notNull(url, "URL must not be null");
/*  24: 64 */     this.url = url;
/*  25: 65 */     this.cleanedUrl = getCleanedUrl(this.url, url.toString());
/*  26: 66 */     this.uri = null;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public UrlResource(URI uri)
/*  30:    */     throws MalformedURLException
/*  31:    */   {
/*  32: 75 */     Assert.notNull(uri, "URI must not be null");
/*  33: 76 */     this.url = uri.toURL();
/*  34: 77 */     this.cleanedUrl = getCleanedUrl(this.url, uri.toString());
/*  35: 78 */     this.uri = uri;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public UrlResource(String path)
/*  39:    */     throws MalformedURLException
/*  40:    */   {
/*  41: 87 */     Assert.notNull(path, "Path must not be null");
/*  42: 88 */     this.url = new URL(path);
/*  43: 89 */     this.cleanedUrl = getCleanedUrl(this.url, path);
/*  44: 90 */     this.uri = null;
/*  45:    */   }
/*  46:    */   
/*  47:    */   private URL getCleanedUrl(URL originalUrl, String originalPath)
/*  48:    */   {
/*  49:    */     try
/*  50:    */     {
/*  51:102 */       return new URL(StringUtils.cleanPath(originalPath));
/*  52:    */     }
/*  53:    */     catch (MalformedURLException localMalformedURLException) {}
/*  54:107 */     return originalUrl;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public InputStream getInputStream()
/*  58:    */     throws IOException
/*  59:    */   {
/*  60:121 */     URLConnection con = this.url.openConnection();
/*  61:122 */     con.setUseCaches(false);
/*  62:    */     try
/*  63:    */     {
/*  64:124 */       return con.getInputStream();
/*  65:    */     }
/*  66:    */     catch (IOException ex)
/*  67:    */     {
/*  68:128 */       if ((con instanceof HttpURLConnection)) {
/*  69:129 */         ((HttpURLConnection)con).disconnect();
/*  70:    */       }
/*  71:131 */       throw ex;
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   public URL getURL()
/*  76:    */     throws IOException
/*  77:    */   {
/*  78:140 */     return this.url;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public URI getURI()
/*  82:    */     throws IOException
/*  83:    */   {
/*  84:149 */     if (this.uri != null) {
/*  85:150 */       return this.uri;
/*  86:    */     }
/*  87:153 */     return super.getURI();
/*  88:    */   }
/*  89:    */   
/*  90:    */   public File getFile()
/*  91:    */     throws IOException
/*  92:    */   {
/*  93:164 */     if (this.uri != null) {
/*  94:165 */       return super.getFile(this.uri);
/*  95:    */     }
/*  96:168 */     return super.getFile();
/*  97:    */   }
/*  98:    */   
/*  99:    */   public Resource createRelative(String relativePath)
/* 100:    */     throws MalformedURLException
/* 101:    */   {
/* 102:179 */     if (relativePath.startsWith("/")) {
/* 103:180 */       relativePath = relativePath.substring(1);
/* 104:    */     }
/* 105:182 */     return new UrlResource(new URL(this.url, relativePath));
/* 106:    */   }
/* 107:    */   
/* 108:    */   public String getFilename()
/* 109:    */   {
/* 110:192 */     return new File(this.url.getFile()).getName();
/* 111:    */   }
/* 112:    */   
/* 113:    */   public String getDescription()
/* 114:    */   {
/* 115:199 */     return "URL [" + this.url + "]";
/* 116:    */   }
/* 117:    */   
/* 118:    */   public boolean equals(Object obj)
/* 119:    */   {
/* 120:209 */     return (obj == this) || (((obj instanceof UrlResource)) && (this.cleanedUrl.equals(((UrlResource)obj).cleanedUrl)));
/* 121:    */   }
/* 122:    */   
/* 123:    */   public int hashCode()
/* 124:    */   {
/* 125:217 */     return this.cleanedUrl.hashCode();
/* 126:    */   }
/* 127:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.UrlResource
 * JD-Core Version:    0.7.0.1
 */