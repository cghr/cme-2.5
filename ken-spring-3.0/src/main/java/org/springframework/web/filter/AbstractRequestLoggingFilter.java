/*   1:    */ package org.springframework.web.filter;
/*   2:    */ 
/*   3:    */ import java.io.BufferedReader;
/*   4:    */ import java.io.ByteArrayOutputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStreamReader;
/*   7:    */ import java.io.UnsupportedEncodingException;
/*   8:    */ import javax.servlet.FilterChain;
/*   9:    */ import javax.servlet.ServletException;
/*  10:    */ import javax.servlet.ServletInputStream;
/*  11:    */ import javax.servlet.http.HttpServletRequest;
/*  12:    */ import javax.servlet.http.HttpServletRequestWrapper;
/*  13:    */ import javax.servlet.http.HttpServletResponse;
/*  14:    */ import javax.servlet.http.HttpSession;
/*  15:    */ import org.springframework.util.Assert;
/*  16:    */ import org.springframework.util.StringUtils;
/*  17:    */ 
/*  18:    */ public abstract class AbstractRequestLoggingFilter
/*  19:    */   extends OncePerRequestFilter
/*  20:    */ {
/*  21:    */   public static final String DEFAULT_BEFORE_MESSAGE_PREFIX = "Before request [";
/*  22:    */   public static final String DEFAULT_BEFORE_MESSAGE_SUFFIX = "]";
/*  23:    */   public static final String DEFAULT_AFTER_MESSAGE_PREFIX = "After request [";
/*  24:    */   public static final String DEFAULT_AFTER_MESSAGE_SUFFIX = "]";
/*  25:    */   private static final int DEFAULT_MAX_PAYLOAD_LENGTH = 50;
/*  26: 70 */   private boolean includeQueryString = false;
/*  27: 72 */   private boolean includeClientInfo = false;
/*  28: 74 */   private boolean includePayload = false;
/*  29: 76 */   private int maxPayloadLength = 50;
/*  30: 78 */   private String beforeMessagePrefix = "Before request [";
/*  31: 80 */   private String beforeMessageSuffix = "]";
/*  32: 82 */   private String afterMessagePrefix = "After request [";
/*  33: 84 */   private String afterMessageSuffix = "]";
/*  34:    */   
/*  35:    */   public void setIncludeQueryString(boolean includeQueryString)
/*  36:    */   {
/*  37: 92 */     this.includeQueryString = includeQueryString;
/*  38:    */   }
/*  39:    */   
/*  40:    */   protected boolean isIncludeQueryString()
/*  41:    */   {
/*  42: 99 */     return this.includeQueryString;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setIncludeClientInfo(boolean includeClientInfo)
/*  46:    */   {
/*  47:108 */     this.includeClientInfo = includeClientInfo;
/*  48:    */   }
/*  49:    */   
/*  50:    */   protected boolean isIncludeClientInfo()
/*  51:    */   {
/*  52:115 */     return this.includeClientInfo;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setIncludePayload(boolean includePayload)
/*  56:    */   {
/*  57:125 */     this.includePayload = includePayload;
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected boolean isIncludePayload()
/*  61:    */   {
/*  62:132 */     return this.includePayload;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setMaxPayloadLength(int maxPayloadLength)
/*  66:    */   {
/*  67:139 */     Assert.isTrue(maxPayloadLength >= 0, "'maxPayloadLength' should be larger than or equal to 0");
/*  68:140 */     this.maxPayloadLength = maxPayloadLength;
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected int getMaxPayloadLength()
/*  72:    */   {
/*  73:147 */     return this.maxPayloadLength;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void setBeforeMessagePrefix(String beforeMessagePrefix)
/*  77:    */   {
/*  78:154 */     this.beforeMessagePrefix = beforeMessagePrefix;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setBeforeMessageSuffix(String beforeMessageSuffix)
/*  82:    */   {
/*  83:161 */     this.beforeMessageSuffix = beforeMessageSuffix;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setAfterMessagePrefix(String afterMessagePrefix)
/*  87:    */   {
/*  88:168 */     this.afterMessagePrefix = afterMessagePrefix;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setAfterMessageSuffix(String afterMessageSuffix)
/*  92:    */   {
/*  93:175 */     this.afterMessageSuffix = afterMessageSuffix;
/*  94:    */   }
/*  95:    */   
/*  96:    */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
/*  97:    */     throws ServletException, IOException
/*  98:    */   {
/*  99:188 */     if (isIncludePayload()) {
/* 100:189 */       request = new RequestCachingRequestWrapper(request, null);
/* 101:    */     }
/* 102:191 */     beforeRequest(request, getBeforeMessage(request));
/* 103:    */     try
/* 104:    */     {
/* 105:193 */       filterChain.doFilter(request, response);
/* 106:    */     }
/* 107:    */     finally
/* 108:    */     {
/* 109:196 */       afterRequest(request, getAfterMessage(request));
/* 110:    */     }
/* 111:    */   }
/* 112:    */   
/* 113:    */   private String getBeforeMessage(HttpServletRequest request)
/* 114:    */   {
/* 115:206 */     return createMessage(request, this.beforeMessagePrefix, this.beforeMessageSuffix);
/* 116:    */   }
/* 117:    */   
/* 118:    */   private String getAfterMessage(HttpServletRequest request)
/* 119:    */   {
/* 120:215 */     return createMessage(request, this.afterMessagePrefix, this.afterMessageSuffix);
/* 121:    */   }
/* 122:    */   
/* 123:    */   protected String createMessage(HttpServletRequest request, String prefix, String suffix)
/* 124:    */   {
/* 125:225 */     StringBuilder msg = new StringBuilder();
/* 126:226 */     msg.append(prefix);
/* 127:227 */     msg.append("uri=").append(request.getRequestURI());
/* 128:228 */     if (isIncludeQueryString()) {
/* 129:229 */       msg.append('?').append(request.getQueryString());
/* 130:    */     }
/* 131:231 */     if (isIncludeClientInfo())
/* 132:    */     {
/* 133:232 */       String client = request.getRemoteAddr();
/* 134:233 */       if (StringUtils.hasLength(client)) {
/* 135:234 */         msg.append(";client=").append(client);
/* 136:    */       }
/* 137:236 */       HttpSession session = request.getSession(false);
/* 138:237 */       if (session != null) {
/* 139:238 */         msg.append(";session=").append(session.getId());
/* 140:    */       }
/* 141:240 */       String user = request.getRemoteUser();
/* 142:241 */       if (user != null) {
/* 143:242 */         msg.append(";user=").append(user);
/* 144:    */       }
/* 145:    */     }
/* 146:245 */     if ((isIncludePayload()) && ((request instanceof RequestCachingRequestWrapper)))
/* 147:    */     {
/* 148:246 */       RequestCachingRequestWrapper wrapper = (RequestCachingRequestWrapper)request;
/* 149:247 */       byte[] buf = wrapper.toByteArray();
/* 150:248 */       if (buf.length > 0)
/* 151:    */       {
/* 152:249 */         int length = Math.min(buf.length, getMaxPayloadLength());
/* 153:    */         String payload;
/* 154:    */         try
/* 155:    */         {
/* 156:252 */           payload = new String(buf, 0, length, wrapper.getCharacterEncoding());
/* 157:    */         }
/* 158:    */         catch (UnsupportedEncodingException localUnsupportedEncodingException)
/* 159:    */         {
/* 160:    */           String payload;
/* 161:255 */           payload = "[unknown]";
/* 162:    */         }
/* 163:257 */         msg.append(";payload=").append(payload);
/* 164:    */       }
/* 165:    */     }
/* 166:261 */     msg.append(suffix);
/* 167:262 */     return msg.toString();
/* 168:    */   }
/* 169:    */   
/* 170:    */   protected abstract void beforeRequest(HttpServletRequest paramHttpServletRequest, String paramString);
/* 171:    */   
/* 172:    */   protected abstract void afterRequest(HttpServletRequest paramHttpServletRequest, String paramString);
/* 173:    */   
/* 174:    */   private static class RequestCachingRequestWrapper
/* 175:    */     extends HttpServletRequestWrapper
/* 176:    */   {
/* 177:283 */     private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 178:    */     private final ServletInputStream inputStream;
/* 179:    */     private BufferedReader reader;
/* 180:    */     
/* 181:    */     private RequestCachingRequestWrapper(HttpServletRequest request)
/* 182:    */       throws IOException
/* 183:    */     {
/* 184:290 */       super();
/* 185:291 */       this.inputStream = new RequestCachingInputStream(request.getInputStream(), null);
/* 186:    */     }
/* 187:    */     
/* 188:    */     public ServletInputStream getInputStream()
/* 189:    */       throws IOException
/* 190:    */     {
/* 191:296 */       return this.inputStream;
/* 192:    */     }
/* 193:    */     
/* 194:    */     public String getCharacterEncoding()
/* 195:    */     {
/* 196:301 */       return super.getCharacterEncoding() != null ? super.getCharacterEncoding() : 
/* 197:302 */         "ISO-8859-1";
/* 198:    */     }
/* 199:    */     
/* 200:    */     public BufferedReader getReader()
/* 201:    */       throws IOException
/* 202:    */     {
/* 203:307 */       if (this.reader == null) {
/* 204:308 */         this.reader = new BufferedReader(new InputStreamReader(this.inputStream, getCharacterEncoding()));
/* 205:    */       }
/* 206:310 */       return this.reader;
/* 207:    */     }
/* 208:    */     
/* 209:    */     private byte[] toByteArray()
/* 210:    */     {
/* 211:314 */       return this.bos.toByteArray();
/* 212:    */     }
/* 213:    */     
/* 214:    */     private class RequestCachingInputStream
/* 215:    */       extends ServletInputStream
/* 216:    */     {
/* 217:    */       private final ServletInputStream is;
/* 218:    */       
/* 219:    */       private RequestCachingInputStream(ServletInputStream is)
/* 220:    */       {
/* 221:322 */         this.is = is;
/* 222:    */       }
/* 223:    */       
/* 224:    */       public int read()
/* 225:    */         throws IOException
/* 226:    */       {
/* 227:327 */         int ch = this.is.read();
/* 228:328 */         if (ch != -1) {
/* 229:329 */           AbstractRequestLoggingFilter.RequestCachingRequestWrapper.this.bos.write(ch);
/* 230:    */         }
/* 231:331 */         return ch;
/* 232:    */       }
/* 233:    */     }
/* 234:    */   }
/* 235:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.filter.AbstractRequestLoggingFilter
 * JD-Core Version:    0.7.0.1
 */