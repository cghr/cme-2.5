/*   1:    */ package org.springframework.web.context.request;
/*   2:    */ 
/*   3:    */ import java.security.Principal;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.Locale;
/*   6:    */ import java.util.Map;
/*   7:    */ import javax.servlet.http.HttpServletRequest;
/*   8:    */ import javax.servlet.http.HttpServletResponse;
/*   9:    */ import javax.servlet.http.HttpSession;
/*  10:    */ import org.springframework.util.CollectionUtils;
/*  11:    */ import org.springframework.util.ObjectUtils;
/*  12:    */ import org.springframework.util.StringUtils;
/*  13:    */ import org.springframework.web.util.WebUtils;
/*  14:    */ 
/*  15:    */ public class ServletWebRequest
/*  16:    */   extends ServletRequestAttributes
/*  17:    */   implements NativeWebRequest
/*  18:    */ {
/*  19:    */   private static final String HEADER_ETAG = "ETag";
/*  20:    */   private static final String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";
/*  21:    */   private static final String HEADER_IF_NONE_MATCH = "If-None-Match";
/*  22:    */   private static final String HEADER_LAST_MODIFIED = "Last-Modified";
/*  23:    */   private static final String METHOD_GET = "GET";
/*  24:    */   private HttpServletResponse response;
/*  25: 53 */   private boolean notModified = false;
/*  26:    */   
/*  27:    */   public ServletWebRequest(HttpServletRequest request)
/*  28:    */   {
/*  29: 61 */     super(request);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public ServletWebRequest(HttpServletRequest request, HttpServletResponse response)
/*  33:    */   {
/*  34: 70 */     this(request);
/*  35: 71 */     this.response = response;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public final HttpServletResponse getResponse()
/*  39:    */   {
/*  40: 79 */     return this.response;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public Object getNativeRequest()
/*  44:    */   {
/*  45: 83 */     return getRequest();
/*  46:    */   }
/*  47:    */   
/*  48:    */   public Object getNativeResponse()
/*  49:    */   {
/*  50: 87 */     return getResponse();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public <T> T getNativeRequest(Class<T> requiredType)
/*  54:    */   {
/*  55: 92 */     return WebUtils.getNativeRequest(getRequest(), requiredType);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public <T> T getNativeResponse(Class<T> requiredType)
/*  59:    */   {
/*  60: 97 */     return WebUtils.getNativeResponse(getResponse(), requiredType);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public String getHeader(String headerName)
/*  64:    */   {
/*  65:102 */     return getRequest().getHeader(headerName);
/*  66:    */   }
/*  67:    */   
/*  68:    */   public String[] getHeaderValues(String headerName)
/*  69:    */   {
/*  70:107 */     String[] headerValues = StringUtils.toStringArray(getRequest().getHeaders(headerName));
/*  71:108 */     return !ObjectUtils.isEmpty(headerValues) ? headerValues : null;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public Iterator<String> getHeaderNames()
/*  75:    */   {
/*  76:113 */     return CollectionUtils.toIterator(getRequest().getHeaderNames());
/*  77:    */   }
/*  78:    */   
/*  79:    */   public String getParameter(String paramName)
/*  80:    */   {
/*  81:117 */     return getRequest().getParameter(paramName);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public String[] getParameterValues(String paramName)
/*  85:    */   {
/*  86:121 */     return getRequest().getParameterValues(paramName);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public Iterator<String> getParameterNames()
/*  90:    */   {
/*  91:126 */     return CollectionUtils.toIterator(getRequest().getParameterNames());
/*  92:    */   }
/*  93:    */   
/*  94:    */   public Map<String, String[]> getParameterMap()
/*  95:    */   {
/*  96:131 */     return getRequest().getParameterMap();
/*  97:    */   }
/*  98:    */   
/*  99:    */   public Locale getLocale()
/* 100:    */   {
/* 101:135 */     return getRequest().getLocale();
/* 102:    */   }
/* 103:    */   
/* 104:    */   public String getContextPath()
/* 105:    */   {
/* 106:139 */     return getRequest().getContextPath();
/* 107:    */   }
/* 108:    */   
/* 109:    */   public String getRemoteUser()
/* 110:    */   {
/* 111:143 */     return getRequest().getRemoteUser();
/* 112:    */   }
/* 113:    */   
/* 114:    */   public Principal getUserPrincipal()
/* 115:    */   {
/* 116:147 */     return getRequest().getUserPrincipal();
/* 117:    */   }
/* 118:    */   
/* 119:    */   public boolean isUserInRole(String role)
/* 120:    */   {
/* 121:151 */     return getRequest().isUserInRole(role);
/* 122:    */   }
/* 123:    */   
/* 124:    */   public boolean isSecure()
/* 125:    */   {
/* 126:155 */     return getRequest().isSecure();
/* 127:    */   }
/* 128:    */   
/* 129:    */   public boolean checkNotModified(long lastModifiedTimestamp)
/* 130:    */   {
/* 131:159 */     if ((lastModifiedTimestamp >= 0L) && (!this.notModified) && (
/* 132:160 */       (this.response == null) || (!this.response.containsHeader("Last-Modified"))))
/* 133:    */     {
/* 134:161 */       long ifModifiedSince = getRequest().getDateHeader("If-Modified-Since");
/* 135:162 */       this.notModified = (ifModifiedSince >= lastModifiedTimestamp / 1000L * 1000L);
/* 136:163 */       if (this.response != null) {
/* 137:164 */         if ((this.notModified) && ("GET".equals(getRequest().getMethod()))) {
/* 138:165 */           this.response.setStatus(304);
/* 139:    */         } else {
/* 140:168 */           this.response.setDateHeader("Last-Modified", lastModifiedTimestamp);
/* 141:    */         }
/* 142:    */       }
/* 143:    */     }
/* 144:172 */     return this.notModified;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public boolean checkNotModified(String eTag)
/* 148:    */   {
/* 149:176 */     if ((StringUtils.hasLength(eTag)) && (!this.notModified) && (
/* 150:177 */       (this.response == null) || (!this.response.containsHeader("ETag"))))
/* 151:    */     {
/* 152:178 */       String ifNoneMatch = getRequest().getHeader("If-None-Match");
/* 153:179 */       this.notModified = eTag.equals(ifNoneMatch);
/* 154:180 */       if (this.response != null) {
/* 155:181 */         if ((this.notModified) && ("GET".equals(getRequest().getMethod()))) {
/* 156:182 */           this.response.setStatus(304);
/* 157:    */         } else {
/* 158:185 */           this.response.setHeader("ETag", eTag);
/* 159:    */         }
/* 160:    */       }
/* 161:    */     }
/* 162:189 */     return this.notModified;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public boolean isNotModified()
/* 166:    */   {
/* 167:194 */     return this.notModified;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public String getDescription(boolean includeClientInfo)
/* 171:    */   {
/* 172:198 */     HttpServletRequest request = getRequest();
/* 173:199 */     StringBuilder sb = new StringBuilder();
/* 174:200 */     sb.append("uri=").append(request.getRequestURI());
/* 175:201 */     if (includeClientInfo)
/* 176:    */     {
/* 177:202 */       String client = request.getRemoteAddr();
/* 178:203 */       if (StringUtils.hasLength(client)) {
/* 179:204 */         sb.append(";client=").append(client);
/* 180:    */       }
/* 181:206 */       HttpSession session = request.getSession(false);
/* 182:207 */       if (session != null) {
/* 183:208 */         sb.append(";session=").append(session.getId());
/* 184:    */       }
/* 185:210 */       String user = request.getRemoteUser();
/* 186:211 */       if (StringUtils.hasLength(user)) {
/* 187:212 */         sb.append(";user=").append(user);
/* 188:    */       }
/* 189:    */     }
/* 190:215 */     return sb.toString();
/* 191:    */   }
/* 192:    */   
/* 193:    */   public String toString()
/* 194:    */   {
/* 195:221 */     return "ServletWebRequest: " + getDescription(true);
/* 196:    */   }
/* 197:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.request.ServletWebRequest
 * JD-Core Version:    0.7.0.1
 */