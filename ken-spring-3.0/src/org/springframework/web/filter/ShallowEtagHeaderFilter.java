/*   1:    */ package org.springframework.web.filter;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.OutputStreamWriter;
/*   6:    */ import java.io.PrintWriter;
/*   7:    */ import java.io.UnsupportedEncodingException;
/*   8:    */ import javax.servlet.FilterChain;
/*   9:    */ import javax.servlet.ServletException;
/*  10:    */ import javax.servlet.ServletOutputStream;
/*  11:    */ import javax.servlet.http.HttpServletRequest;
/*  12:    */ import javax.servlet.http.HttpServletResponse;
/*  13:    */ import javax.servlet.http.HttpServletResponseWrapper;
/*  14:    */ import org.apache.commons.logging.Log;
/*  15:    */ import org.springframework.util.DigestUtils;
/*  16:    */ import org.springframework.util.FileCopyUtils;
/*  17:    */ 
/*  18:    */ public class ShallowEtagHeaderFilter
/*  19:    */   extends OncePerRequestFilter
/*  20:    */ {
/*  21: 48 */   private static String HEADER_ETAG = "ETag";
/*  22: 50 */   private static String HEADER_IF_NONE_MATCH = "If-None-Match";
/*  23:    */   
/*  24:    */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
/*  25:    */     throws ServletException, IOException
/*  26:    */   {
/*  27: 57 */     ShallowEtagResponseWrapper responseWrapper = new ShallowEtagResponseWrapper(response, null);
/*  28: 58 */     filterChain.doFilter(request, responseWrapper);
/*  29:    */     
/*  30: 60 */     byte[] body = responseWrapper.toByteArray();
/*  31: 61 */     int statusCode = responseWrapper.getStatusCode();
/*  32: 63 */     if (isEligibleForEtag(request, responseWrapper, statusCode, body))
/*  33:    */     {
/*  34: 64 */       String responseETag = generateETagHeaderValue(body);
/*  35: 65 */       response.setHeader(HEADER_ETAG, responseETag);
/*  36:    */       
/*  37: 67 */       String requestETag = request.getHeader(HEADER_IF_NONE_MATCH);
/*  38: 68 */       if (responseETag.equals(requestETag))
/*  39:    */       {
/*  40: 69 */         if (this.logger.isTraceEnabled()) {
/*  41: 70 */           this.logger.trace("ETag [" + responseETag + "] equal to If-None-Match, sending 304");
/*  42:    */         }
/*  43: 72 */         response.setStatus(304);
/*  44:    */       }
/*  45:    */       else
/*  46:    */       {
/*  47: 75 */         if (this.logger.isTraceEnabled()) {
/*  48: 76 */           this.logger.trace("ETag [" + responseETag + "] not equal to If-None-Match [" + requestETag + 
/*  49: 77 */             "], sending normal response");
/*  50:    */         }
/*  51: 79 */         copyBodyToResponse(body, response);
/*  52:    */       }
/*  53:    */     }
/*  54:    */     else
/*  55:    */     {
/*  56: 83 */       if (this.logger.isTraceEnabled()) {
/*  57: 84 */         this.logger.trace("Response with status code [" + statusCode + "] not eligible for ETag");
/*  58:    */       }
/*  59: 86 */       copyBodyToResponse(body, response);
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   private void copyBodyToResponse(byte[] body, HttpServletResponse response)
/*  64:    */     throws IOException
/*  65:    */   {
/*  66: 91 */     if (body.length > 0)
/*  67:    */     {
/*  68: 92 */       response.setContentLength(body.length);
/*  69: 93 */       FileCopyUtils.copy(body, response.getOutputStream());
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   protected boolean isEligibleForEtag(HttpServletRequest request, HttpServletResponse response, int responseStatusCode, byte[] responseBody)
/*  74:    */   {
/*  75:109 */     return (responseStatusCode >= 200) && (responseStatusCode < 300);
/*  76:    */   }
/*  77:    */   
/*  78:    */   protected String generateETagHeaderValue(byte[] bytes)
/*  79:    */   {
/*  80:120 */     StringBuilder builder = new StringBuilder("\"0");
/*  81:121 */     DigestUtils.appendMd5DigestAsHex(bytes, builder);
/*  82:122 */     builder.append('"');
/*  83:123 */     return builder.toString();
/*  84:    */   }
/*  85:    */   
/*  86:    */   private static class ShallowEtagResponseWrapper
/*  87:    */     extends HttpServletResponseWrapper
/*  88:    */   {
/*  89:134 */     private final ByteArrayOutputStream content = new ByteArrayOutputStream();
/*  90:136 */     private final ServletOutputStream outputStream = new ResponseServletOutputStream(null);
/*  91:    */     private PrintWriter writer;
/*  92:140 */     private int statusCode = 200;
/*  93:    */     
/*  94:    */     private ShallowEtagResponseWrapper(HttpServletResponse response)
/*  95:    */     {
/*  96:143 */       super();
/*  97:    */     }
/*  98:    */     
/*  99:    */     public void setStatus(int sc)
/* 100:    */     {
/* 101:148 */       super.setStatus(sc);
/* 102:149 */       this.statusCode = sc;
/* 103:    */     }
/* 104:    */     
/* 105:    */     public void setStatus(int sc, String sm)
/* 106:    */     {
/* 107:154 */       super.setStatus(sc, sm);
/* 108:155 */       this.statusCode = sc;
/* 109:    */     }
/* 110:    */     
/* 111:    */     public void sendError(int sc)
/* 112:    */       throws IOException
/* 113:    */     {
/* 114:160 */       super.sendError(sc);
/* 115:161 */       this.statusCode = sc;
/* 116:    */     }
/* 117:    */     
/* 118:    */     public void sendError(int sc, String msg)
/* 119:    */       throws IOException
/* 120:    */     {
/* 121:166 */       super.sendError(sc, msg);
/* 122:167 */       this.statusCode = sc;
/* 123:    */     }
/* 124:    */     
/* 125:    */     public void setContentLength(int len) {}
/* 126:    */     
/* 127:    */     public ServletOutputStream getOutputStream()
/* 128:    */     {
/* 129:176 */       return this.outputStream;
/* 130:    */     }
/* 131:    */     
/* 132:    */     public PrintWriter getWriter()
/* 133:    */       throws IOException
/* 134:    */     {
/* 135:181 */       if (this.writer == null)
/* 136:    */       {
/* 137:182 */         String characterEncoding = getCharacterEncoding();
/* 138:183 */         this.writer = (characterEncoding != null ? new ResponsePrintWriter(characterEncoding, null) : 
/* 139:184 */           new ResponsePrintWriter("ISO-8859-1", null));
/* 140:    */       }
/* 141:186 */       return this.writer;
/* 142:    */     }
/* 143:    */     
/* 144:    */     public void resetBuffer()
/* 145:    */     {
/* 146:191 */       this.content.reset();
/* 147:    */     }
/* 148:    */     
/* 149:    */     public void reset()
/* 150:    */     {
/* 151:196 */       super.reset();
/* 152:197 */       resetBuffer();
/* 153:    */     }
/* 154:    */     
/* 155:    */     private int getStatusCode()
/* 156:    */     {
/* 157:201 */       return this.statusCode;
/* 158:    */     }
/* 159:    */     
/* 160:    */     private byte[] toByteArray()
/* 161:    */     {
/* 162:205 */       return this.content.toByteArray();
/* 163:    */     }
/* 164:    */     
/* 165:    */     private class ResponseServletOutputStream
/* 166:    */       extends ServletOutputStream
/* 167:    */     {
/* 168:    */       private ResponseServletOutputStream() {}
/* 169:    */       
/* 170:    */       public void write(int b)
/* 171:    */         throws IOException
/* 172:    */       {
/* 173:212 */         ShallowEtagHeaderFilter.ShallowEtagResponseWrapper.this.content.write(b);
/* 174:    */       }
/* 175:    */       
/* 176:    */       public void write(byte[] b, int off, int len)
/* 177:    */         throws IOException
/* 178:    */       {
/* 179:217 */         ShallowEtagHeaderFilter.ShallowEtagResponseWrapper.this.content.write(b, off, len);
/* 180:    */       }
/* 181:    */     }
/* 182:    */     
/* 183:    */     private class ResponsePrintWriter
/* 184:    */       extends PrintWriter
/* 185:    */     {
/* 186:    */       private ResponsePrintWriter(String characterEncoding)
/* 187:    */         throws UnsupportedEncodingException
/* 188:    */       {
/* 189:224 */         super();
/* 190:    */       }
/* 191:    */       
/* 192:    */       public void write(char[] buf, int off, int len)
/* 193:    */       {
/* 194:229 */         super.write(buf, off, len);
/* 195:230 */         super.flush();
/* 196:    */       }
/* 197:    */       
/* 198:    */       public void write(String s, int off, int len)
/* 199:    */       {
/* 200:235 */         super.write(s, off, len);
/* 201:236 */         super.flush();
/* 202:    */       }
/* 203:    */       
/* 204:    */       public void write(int c)
/* 205:    */       {
/* 206:241 */         super.write(c);
/* 207:242 */         super.flush();
/* 208:    */       }
/* 209:    */     }
/* 210:    */   }
/* 211:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.filter.ShallowEtagHeaderFilter
 * JD-Core Version:    0.7.0.1
 */