/*   1:    */ package org.springframework.web.servlet.support;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.HashSet;
/*   6:    */ import java.util.Set;
/*   7:    */ import javax.servlet.ServletException;
/*   8:    */ import javax.servlet.http.HttpServletRequest;
/*   9:    */ import javax.servlet.http.HttpServletResponse;
/*  10:    */ import org.springframework.util.StringUtils;
/*  11:    */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*  12:    */ import org.springframework.web.HttpSessionRequiredException;
/*  13:    */ import org.springframework.web.context.support.WebApplicationObjectSupport;
/*  14:    */ 
/*  15:    */ public abstract class WebContentGenerator
/*  16:    */   extends WebApplicationObjectSupport
/*  17:    */ {
/*  18:    */   public static final String METHOD_GET = "GET";
/*  19:    */   public static final String METHOD_HEAD = "HEAD";
/*  20:    */   public static final String METHOD_POST = "POST";
/*  21:    */   private static final String HEADER_PRAGMA = "Pragma";
/*  22:    */   private static final String HEADER_EXPIRES = "Expires";
/*  23:    */   private static final String HEADER_CACHE_CONTROL = "Cache-Control";
/*  24:    */   private Set<String> supportedMethods;
/*  25: 69 */   private boolean requireSession = false;
/*  26: 72 */   private boolean useExpiresHeader = true;
/*  27: 75 */   private boolean useCacheControlHeader = true;
/*  28: 78 */   private boolean useCacheControlNoStore = true;
/*  29: 80 */   private int cacheSeconds = -1;
/*  30:    */   
/*  31:    */   public WebContentGenerator()
/*  32:    */   {
/*  33: 88 */     this(true);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public WebContentGenerator(boolean restrictDefaultSupportedMethods)
/*  37:    */   {
/*  38: 98 */     if (restrictDefaultSupportedMethods)
/*  39:    */     {
/*  40: 99 */       this.supportedMethods = new HashSet(4);
/*  41:100 */       this.supportedMethods.add("GET");
/*  42:101 */       this.supportedMethods.add("HEAD");
/*  43:102 */       this.supportedMethods.add("POST");
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   public WebContentGenerator(String... supportedMethods)
/*  48:    */   {
/*  49:111 */     this.supportedMethods = new HashSet((Collection)Arrays.asList(supportedMethods));
/*  50:    */   }
/*  51:    */   
/*  52:    */   public final void setSupportedMethods(String[] methods)
/*  53:    */   {
/*  54:121 */     if (methods != null) {
/*  55:122 */       this.supportedMethods = new HashSet((Collection)Arrays.asList(methods));
/*  56:    */     } else {
/*  57:125 */       this.supportedMethods = null;
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   public final String[] getSupportedMethods()
/*  62:    */   {
/*  63:133 */     return StringUtils.toStringArray(this.supportedMethods);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public final void setRequireSession(boolean requireSession)
/*  67:    */   {
/*  68:140 */     this.requireSession = requireSession;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public final boolean isRequireSession()
/*  72:    */   {
/*  73:147 */     return this.requireSession;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public final void setUseExpiresHeader(boolean useExpiresHeader)
/*  77:    */   {
/*  78:156 */     this.useExpiresHeader = useExpiresHeader;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public final boolean isUseExpiresHeader()
/*  82:    */   {
/*  83:163 */     return this.useExpiresHeader;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public final void setUseCacheControlHeader(boolean useCacheControlHeader)
/*  87:    */   {
/*  88:172 */     this.useCacheControlHeader = useCacheControlHeader;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public final boolean isUseCacheControlHeader()
/*  92:    */   {
/*  93:179 */     return this.useCacheControlHeader;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public final void setUseCacheControlNoStore(boolean useCacheControlNoStore)
/*  97:    */   {
/*  98:187 */     this.useCacheControlNoStore = useCacheControlNoStore;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public final boolean isUseCacheControlNoStore()
/* 102:    */   {
/* 103:194 */     return this.useCacheControlNoStore;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public final void setCacheSeconds(int seconds)
/* 107:    */   {
/* 108:205 */     this.cacheSeconds = seconds;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public final int getCacheSeconds()
/* 112:    */   {
/* 113:212 */     return this.cacheSeconds;
/* 114:    */   }
/* 115:    */   
/* 116:    */   protected final void checkAndPrepare(HttpServletRequest request, HttpServletResponse response, boolean lastModified)
/* 117:    */     throws ServletException
/* 118:    */   {
/* 119:229 */     checkAndPrepare(request, response, this.cacheSeconds, lastModified);
/* 120:    */   }
/* 121:    */   
/* 122:    */   protected final void checkAndPrepare(HttpServletRequest request, HttpServletResponse response, int cacheSeconds, boolean lastModified)
/* 123:    */     throws ServletException
/* 124:    */   {
/* 125:248 */     String method = request.getMethod();
/* 126:249 */     if ((this.supportedMethods != null) && (!this.supportedMethods.contains(method))) {
/* 127:250 */       throw new HttpRequestMethodNotSupportedException(
/* 128:251 */         method, StringUtils.toStringArray(this.supportedMethods));
/* 129:    */     }
/* 130:255 */     if ((this.requireSession) && 
/* 131:256 */       (request.getSession(false) == null)) {
/* 132:257 */       throw new HttpSessionRequiredException("Pre-existing session required but none found");
/* 133:    */     }
/* 134:263 */     applyCacheSeconds(response, cacheSeconds, lastModified);
/* 135:    */   }
/* 136:    */   
/* 137:    */   protected final void preventCaching(HttpServletResponse response)
/* 138:    */   {
/* 139:271 */     response.setHeader("Pragma", "no-cache");
/* 140:272 */     if (this.useExpiresHeader) {
/* 141:274 */       response.setDateHeader("Expires", 1L);
/* 142:    */     }
/* 143:276 */     if (this.useCacheControlHeader)
/* 144:    */     {
/* 145:279 */       response.setHeader("Cache-Control", "no-cache");
/* 146:280 */       if (this.useCacheControlNoStore) {
/* 147:281 */         response.addHeader("Cache-Control", "no-store");
/* 148:    */       }
/* 149:    */     }
/* 150:    */   }
/* 151:    */   
/* 152:    */   protected final void cacheForSeconds(HttpServletResponse response, int seconds)
/* 153:    */   {
/* 154:295 */     cacheForSeconds(response, seconds, false);
/* 155:    */   }
/* 156:    */   
/* 157:    */   protected final void cacheForSeconds(HttpServletResponse response, int seconds, boolean mustRevalidate)
/* 158:    */   {
/* 159:309 */     if (this.useExpiresHeader) {
/* 160:311 */       response.setDateHeader("Expires", System.currentTimeMillis() + seconds * 1000L);
/* 161:    */     }
/* 162:313 */     if (this.useCacheControlHeader)
/* 163:    */     {
/* 164:315 */       String headerValue = "max-age=" + seconds;
/* 165:316 */       if (mustRevalidate) {
/* 166:317 */         headerValue = headerValue + ", must-revalidate";
/* 167:    */       }
/* 168:319 */       response.setHeader("Cache-Control", headerValue);
/* 169:    */     }
/* 170:    */   }
/* 171:    */   
/* 172:    */   protected final void applyCacheSeconds(HttpServletResponse response, int seconds)
/* 173:    */   {
/* 174:334 */     applyCacheSeconds(response, seconds, false);
/* 175:    */   }
/* 176:    */   
/* 177:    */   protected final void applyCacheSeconds(HttpServletResponse response, int seconds, boolean mustRevalidate)
/* 178:    */   {
/* 179:350 */     if (seconds > 0) {
/* 180:351 */       cacheForSeconds(response, seconds, mustRevalidate);
/* 181:353 */     } else if (seconds == 0) {
/* 182:354 */       preventCaching(response);
/* 183:    */     }
/* 184:    */   }
/* 185:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.support.WebContentGenerator
 * JD-Core Version:    0.7.0.1
 */