/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileNotFoundException;
/*   5:    */ import java.net.MalformedURLException;
/*   6:    */ import java.net.URI;
/*   7:    */ import java.net.URISyntaxException;
/*   8:    */ import java.net.URL;
/*   9:    */ 
/*  10:    */ public abstract class ResourceUtils
/*  11:    */ {
/*  12:    */   public static final String CLASSPATH_URL_PREFIX = "classpath:";
/*  13:    */   public static final String FILE_URL_PREFIX = "file:";
/*  14:    */   public static final String URL_PROTOCOL_FILE = "file";
/*  15:    */   public static final String URL_PROTOCOL_JAR = "jar";
/*  16:    */   public static final String URL_PROTOCOL_ZIP = "zip";
/*  17:    */   public static final String URL_PROTOCOL_VFSZIP = "vfszip";
/*  18:    */   public static final String URL_PROTOCOL_VFS = "vfs";
/*  19:    */   public static final String URL_PROTOCOL_WSJAR = "wsjar";
/*  20:    */   public static final String URL_PROTOCOL_CODE_SOURCE = "code-source";
/*  21:    */   public static final String JAR_URL_SEPARATOR = "!/";
/*  22:    */   
/*  23:    */   public static boolean isUrl(String resourceLocation)
/*  24:    */   {
/*  25: 93 */     if (resourceLocation == null) {
/*  26: 94 */       return false;
/*  27:    */     }
/*  28: 96 */     if (resourceLocation.startsWith("classpath:")) {
/*  29: 97 */       return true;
/*  30:    */     }
/*  31:    */     try
/*  32:    */     {
/*  33:100 */       new URL(resourceLocation);
/*  34:101 */       return true;
/*  35:    */     }
/*  36:    */     catch (MalformedURLException localMalformedURLException) {}
/*  37:104 */     return false;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static URL getURL(String resourceLocation)
/*  41:    */     throws FileNotFoundException
/*  42:    */   {
/*  43:118 */     Assert.notNull(resourceLocation, "Resource location must not be null");
/*  44:119 */     if (resourceLocation.startsWith("classpath:"))
/*  45:    */     {
/*  46:120 */       String path = resourceLocation.substring("classpath:".length());
/*  47:121 */       URL url = ClassUtils.getDefaultClassLoader().getResource(path);
/*  48:122 */       if (url == null)
/*  49:    */       {
/*  50:123 */         String description = "class path resource [" + path + "]";
/*  51:124 */         throw new FileNotFoundException(
/*  52:125 */           description + " cannot be resolved to URL because it does not exist");
/*  53:    */       }
/*  54:127 */       return url;
/*  55:    */     }
/*  56:    */     try
/*  57:    */     {
/*  58:131 */       return new URL(resourceLocation);
/*  59:    */     }
/*  60:    */     catch (MalformedURLException localMalformedURLException1)
/*  61:    */     {
/*  62:    */       try
/*  63:    */       {
/*  64:136 */         return new File(resourceLocation).toURI().toURL();
/*  65:    */       }
/*  66:    */       catch (MalformedURLException localMalformedURLException2)
/*  67:    */       {
/*  68:139 */         throw new FileNotFoundException("Resource location [" + resourceLocation + 
/*  69:140 */           "] is neither a URL not a well-formed file path");
/*  70:    */       }
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   public static File getFile(String resourceLocation)
/*  75:    */     throws FileNotFoundException
/*  76:    */   {
/*  77:157 */     Assert.notNull(resourceLocation, "Resource location must not be null");
/*  78:158 */     if (resourceLocation.startsWith("classpath:"))
/*  79:    */     {
/*  80:159 */       String path = resourceLocation.substring("classpath:".length());
/*  81:160 */       String description = "class path resource [" + path + "]";
/*  82:161 */       URL url = ClassUtils.getDefaultClassLoader().getResource(path);
/*  83:162 */       if (url == null) {
/*  84:163 */         throw new FileNotFoundException(
/*  85:164 */           description + " cannot be resolved to absolute file path " + 
/*  86:165 */           "because it does not reside in the file system");
/*  87:    */       }
/*  88:167 */       return getFile(url, description);
/*  89:    */     }
/*  90:    */     try
/*  91:    */     {
/*  92:171 */       return getFile(new URL(resourceLocation));
/*  93:    */     }
/*  94:    */     catch (MalformedURLException localMalformedURLException) {}
/*  95:175 */     return new File(resourceLocation);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public static File getFile(URL resourceUrl)
/*  99:    */     throws FileNotFoundException
/* 100:    */   {
/* 101:188 */     return getFile(resourceUrl, "URL");
/* 102:    */   }
/* 103:    */   
/* 104:    */   public static File getFile(URL resourceUrl, String description)
/* 105:    */     throws FileNotFoundException
/* 106:    */   {
/* 107:202 */     Assert.notNull(resourceUrl, "Resource URL must not be null");
/* 108:203 */     if (!"file".equals(resourceUrl.getProtocol())) {
/* 109:204 */       throw new FileNotFoundException(
/* 110:205 */         description + " cannot be resolved to absolute file path " + 
/* 111:206 */         "because it does not reside in the file system: " + resourceUrl);
/* 112:    */     }
/* 113:    */     try
/* 114:    */     {
/* 115:209 */       return new File(toURI(resourceUrl).getSchemeSpecificPart());
/* 116:    */     }
/* 117:    */     catch (URISyntaxException localURISyntaxException) {}
/* 118:213 */     return new File(resourceUrl.getFile());
/* 119:    */   }
/* 120:    */   
/* 121:    */   public static File getFile(URI resourceUri)
/* 122:    */     throws FileNotFoundException
/* 123:    */   {
/* 124:226 */     return getFile(resourceUri, "URI");
/* 125:    */   }
/* 126:    */   
/* 127:    */   public static File getFile(URI resourceUri, String description)
/* 128:    */     throws FileNotFoundException
/* 129:    */   {
/* 130:240 */     Assert.notNull(resourceUri, "Resource URI must not be null");
/* 131:241 */     if (!"file".equals(resourceUri.getScheme())) {
/* 132:242 */       throw new FileNotFoundException(
/* 133:243 */         description + " cannot be resolved to absolute file path " + 
/* 134:244 */         "because it does not reside in the file system: " + resourceUri);
/* 135:    */     }
/* 136:246 */     return new File(resourceUri.getSchemeSpecificPart());
/* 137:    */   }
/* 138:    */   
/* 139:    */   public static boolean isFileURL(URL url)
/* 140:    */   {
/* 141:256 */     String protocol = url.getProtocol();
/* 142:257 */     return ("file".equals(protocol)) || (protocol.startsWith("vfs"));
/* 143:    */   }
/* 144:    */   
/* 145:    */   public static boolean isJarURL(URL url)
/* 146:    */   {
/* 147:270 */     String protocol = url.getProtocol();
/* 148:    */     
/* 149:    */ 
/* 150:    */ 
/* 151:274 */     return ("jar".equals(protocol)) || ("zip".equals(protocol)) || ("wsjar".equals(protocol)) || (("code-source".equals(protocol)) && (url.getPath().contains("!/")));
/* 152:    */   }
/* 153:    */   
/* 154:    */   public static URL extractJarFileURL(URL jarUrl)
/* 155:    */     throws MalformedURLException
/* 156:    */   {
/* 157:285 */     String urlFile = jarUrl.getFile();
/* 158:286 */     int separatorIndex = urlFile.indexOf("!/");
/* 159:287 */     if (separatorIndex != -1)
/* 160:    */     {
/* 161:288 */       String jarFile = urlFile.substring(0, separatorIndex);
/* 162:    */       try
/* 163:    */       {
/* 164:290 */         return new URL(jarFile);
/* 165:    */       }
/* 166:    */       catch (MalformedURLException localMalformedURLException)
/* 167:    */       {
/* 168:295 */         if (!jarFile.startsWith("/")) {
/* 169:296 */           jarFile = "/" + jarFile;
/* 170:    */         }
/* 171:298 */         return new URL("file:" + jarFile);
/* 172:    */       }
/* 173:    */     }
/* 174:302 */     return jarUrl;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public static URI toURI(URL url)
/* 178:    */     throws URISyntaxException
/* 179:    */   {
/* 180:317 */     return toURI(url.toString());
/* 181:    */   }
/* 182:    */   
/* 183:    */   public static URI toURI(String location)
/* 184:    */     throws URISyntaxException
/* 185:    */   {
/* 186:328 */     return new URI(StringUtils.replace(location, " ", "%20"));
/* 187:    */   }
/* 188:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.ResourceUtils
 * JD-Core Version:    0.7.0.1
 */