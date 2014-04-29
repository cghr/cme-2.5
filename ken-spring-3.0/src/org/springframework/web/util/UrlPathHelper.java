/*   1:    */ package org.springframework.web.util;
/*   2:    */ 
/*   3:    */ import java.io.UnsupportedEncodingException;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.net.URLDecoder;
/*   6:    */ import java.util.Properties;
/*   7:    */ import javax.servlet.http.HttpServletRequest;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.apache.commons.logging.LogFactory;
/*  10:    */ import org.springframework.util.StringUtils;
/*  11:    */ 
/*  12:    */ public class UrlPathHelper
/*  13:    */ {
/*  14:    */   private static final String WEBSPHERE_URI_ATTRIBUTE = "com.ibm.websphere.servlet.uri_non_decoded";
/*  15: 51 */   private static final Log logger = LogFactory.getLog(UrlPathHelper.class);
/*  16:    */   static volatile Boolean websphereComplianceFlag;
/*  17: 56 */   private boolean alwaysUseFullPath = false;
/*  18: 58 */   private boolean urlDecode = true;
/*  19: 60 */   private String defaultEncoding = "ISO-8859-1";
/*  20:    */   
/*  21:    */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath)
/*  22:    */   {
/*  23: 70 */     this.alwaysUseFullPath = alwaysUseFullPath;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setUrlDecode(boolean urlDecode)
/*  27:    */   {
/*  28: 88 */     this.urlDecode = urlDecode;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setDefaultEncoding(String defaultEncoding)
/*  32:    */   {
/*  33:105 */     this.defaultEncoding = defaultEncoding;
/*  34:    */   }
/*  35:    */   
/*  36:    */   protected String getDefaultEncoding()
/*  37:    */   {
/*  38:112 */     return this.defaultEncoding;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public String getLookupPathForRequest(HttpServletRequest request)
/*  42:    */   {
/*  43:127 */     if (this.alwaysUseFullPath) {
/*  44:128 */       return getPathWithinApplication(request);
/*  45:    */     }
/*  46:131 */     String rest = getPathWithinServletMapping(request);
/*  47:132 */     if (!"".equals(rest)) {
/*  48:133 */       return rest;
/*  49:    */     }
/*  50:136 */     return getPathWithinApplication(request);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public String getPathWithinServletMapping(HttpServletRequest request)
/*  54:    */   {
/*  55:152 */     String pathWithinApp = getPathWithinApplication(request);
/*  56:153 */     String servletPath = getServletPath(request);
/*  57:154 */     if (pathWithinApp.startsWith(servletPath)) {
/*  58:156 */       return pathWithinApp.substring(servletPath.length());
/*  59:    */     }
/*  60:163 */     String pathInfo = request.getPathInfo();
/*  61:164 */     return pathInfo != null ? pathInfo : servletPath;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public String getPathWithinApplication(HttpServletRequest request)
/*  65:    */   {
/*  66:175 */     String contextPath = getContextPath(request);
/*  67:176 */     String requestUri = getRequestUri(request);
/*  68:177 */     if (StringUtils.startsWithIgnoreCase(requestUri, contextPath))
/*  69:    */     {
/*  70:179 */       String path = requestUri.substring(contextPath.length());
/*  71:180 */       return StringUtils.hasText(path) ? path : "/";
/*  72:    */     }
/*  73:184 */     return requestUri;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public String getRequestUri(HttpServletRequest request)
/*  77:    */   {
/*  78:201 */     String uri = (String)request.getAttribute("javax.servlet.include.request_uri");
/*  79:202 */     if (uri == null) {
/*  80:203 */       uri = request.getRequestURI();
/*  81:    */     }
/*  82:205 */     return decodeAndCleanUriString(request, uri);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public String getContextPath(HttpServletRequest request)
/*  86:    */   {
/*  87:217 */     String contextPath = (String)request.getAttribute("javax.servlet.include.context_path");
/*  88:218 */     if (contextPath == null) {
/*  89:219 */       contextPath = request.getContextPath();
/*  90:    */     }
/*  91:221 */     if ("/".equals(contextPath)) {
/*  92:223 */       contextPath = "";
/*  93:    */     }
/*  94:225 */     return decodeRequestString(request, contextPath);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public String getServletPath(HttpServletRequest request)
/*  98:    */   {
/*  99:237 */     String servletPath = (String)request.getAttribute("javax.servlet.include.servlet_path");
/* 100:238 */     if (servletPath == null) {
/* 101:239 */       servletPath = request.getServletPath();
/* 102:    */     }
/* 103:241 */     if ((servletPath.length() > 1) && (servletPath.endsWith("/")) && 
/* 104:242 */       (shouldRemoveTrailingServletPathSlash(request))) {
/* 105:246 */       servletPath = servletPath.substring(0, servletPath.length() - 1);
/* 106:    */     }
/* 107:248 */     return servletPath;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public String getOriginatingRequestUri(HttpServletRequest request)
/* 111:    */   {
/* 112:257 */     String uri = (String)request.getAttribute("com.ibm.websphere.servlet.uri_non_decoded");
/* 113:258 */     if (uri == null)
/* 114:    */     {
/* 115:259 */       uri = (String)request.getAttribute("javax.servlet.forward.request_uri");
/* 116:260 */       if (uri == null) {
/* 117:261 */         uri = request.getRequestURI();
/* 118:    */       }
/* 119:    */     }
/* 120:264 */     return decodeAndCleanUriString(request, uri);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public String getOriginatingContextPath(HttpServletRequest request)
/* 124:    */   {
/* 125:276 */     String contextPath = (String)request.getAttribute("javax.servlet.forward.context_path");
/* 126:277 */     if (contextPath == null) {
/* 127:278 */       contextPath = request.getContextPath();
/* 128:    */     }
/* 129:280 */     return decodeRequestString(request, contextPath);
/* 130:    */   }
/* 131:    */   
/* 132:    */   public String getOriginatingQueryString(HttpServletRequest request)
/* 133:    */   {
/* 134:290 */     if ((request.getAttribute("javax.servlet.forward.request_uri") != null) || 
/* 135:291 */       (request.getAttribute("javax.servlet.error.request_uri") != null)) {
/* 136:292 */       return (String)request.getAttribute("javax.servlet.forward.query_string");
/* 137:    */     }
/* 138:295 */     return request.getQueryString();
/* 139:    */   }
/* 140:    */   
/* 141:    */   private String decodeAndCleanUriString(HttpServletRequest request, String uri)
/* 142:    */   {
/* 143:303 */     uri = decodeRequestString(request, uri);
/* 144:304 */     int semicolonIndex = uri.indexOf(';');
/* 145:305 */     return semicolonIndex != -1 ? uri.substring(0, semicolonIndex) : uri;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public String decodeRequestString(HttpServletRequest request, String source)
/* 149:    */   {
/* 150:321 */     if (this.urlDecode)
/* 151:    */     {
/* 152:322 */       String enc = determineEncoding(request);
/* 153:    */       try
/* 154:    */       {
/* 155:324 */         return UriUtils.decode(source, enc);
/* 156:    */       }
/* 157:    */       catch (UnsupportedEncodingException ex)
/* 158:    */       {
/* 159:327 */         if (logger.isWarnEnabled()) {
/* 160:328 */           logger.warn("Could not decode request string [" + source + "] with encoding '" + enc + 
/* 161:329 */             "': falling back to platform default encoding; exception message: " + ex.getMessage());
/* 162:    */         }
/* 163:331 */         return URLDecoder.decode(source);
/* 164:    */       }
/* 165:    */     }
/* 166:334 */     return source;
/* 167:    */   }
/* 168:    */   
/* 169:    */   protected String determineEncoding(HttpServletRequest request)
/* 170:    */   {
/* 171:348 */     String enc = request.getCharacterEncoding();
/* 172:349 */     if (enc == null) {
/* 173:350 */       enc = getDefaultEncoding();
/* 174:    */     }
/* 175:352 */     return enc;
/* 176:    */   }
/* 177:    */   
/* 178:    */   private boolean shouldRemoveTrailingServletPathSlash(HttpServletRequest request)
/* 179:    */   {
/* 180:357 */     if (request.getAttribute("com.ibm.websphere.servlet.uri_non_decoded") == null) {
/* 181:361 */       return false;
/* 182:    */     }
/* 183:363 */     if (websphereComplianceFlag == null)
/* 184:    */     {
/* 185:364 */       ClassLoader classLoader = UrlPathHelper.class.getClassLoader();
/* 186:365 */       String className = "com.ibm.ws.webcontainer.WebContainer";
/* 187:366 */       String methodName = "getWebContainerProperties";
/* 188:367 */       String propName = "com.ibm.ws.webcontainer.removetrailingservletpathslash";
/* 189:368 */       boolean flag = false;
/* 190:    */       try
/* 191:    */       {
/* 192:370 */         Class<?> cl = classLoader.loadClass(className);
/* 193:371 */         Properties prop = (Properties)cl.getMethod(methodName, new Class[0]).invoke(null, new Object[0]);
/* 194:372 */         flag = Boolean.parseBoolean(prop.getProperty(propName));
/* 195:    */       }
/* 196:    */       catch (Throwable ex)
/* 197:    */       {
/* 198:375 */         if (logger.isDebugEnabled()) {
/* 199:376 */           logger.debug("Could not introspect WebSphere web container properties: " + ex);
/* 200:    */         }
/* 201:    */       }
/* 202:379 */       websphereComplianceFlag = Boolean.valueOf(flag);
/* 203:    */     }
/* 204:383 */     return !websphereComplianceFlag.booleanValue();
/* 205:    */   }
/* 206:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.util.UrlPathHelper
 * JD-Core Version:    0.7.0.1
 */