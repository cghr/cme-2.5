/*  1:   */ package org.springframework.http.server;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.OutputStream;
/*  5:   */ import java.util.Iterator;
/*  6:   */ import java.util.List;
/*  7:   */ import java.util.Map.Entry;
/*  8:   */ import java.util.Set;
/*  9:   */ import javax.servlet.http.HttpServletResponse;
/* 10:   */ import org.springframework.http.HttpHeaders;
/* 11:   */ import org.springframework.http.HttpStatus;
/* 12:   */ import org.springframework.util.Assert;
/* 13:   */ 
/* 14:   */ public class ServletServerHttpResponse
/* 15:   */   implements ServerHttpResponse
/* 16:   */ {
/* 17:   */   private final HttpServletResponse servletResponse;
/* 18:39 */   private final HttpHeaders headers = new HttpHeaders();
/* 19:41 */   private boolean headersWritten = false;
/* 20:   */   
/* 21:   */   public ServletServerHttpResponse(HttpServletResponse servletResponse)
/* 22:   */   {
/* 23:49 */     Assert.notNull(servletResponse, "'servletResponse' must not be null");
/* 24:50 */     this.servletResponse = servletResponse;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public HttpServletResponse getServletResponse()
/* 28:   */   {
/* 29:58 */     return this.servletResponse;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void setStatusCode(HttpStatus status)
/* 33:   */   {
/* 34:62 */     this.servletResponse.setStatus(status.value());
/* 35:   */   }
/* 36:   */   
/* 37:   */   public HttpHeaders getHeaders()
/* 38:   */   {
/* 39:66 */     return this.headersWritten ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public OutputStream getBody()
/* 43:   */     throws IOException
/* 44:   */   {
/* 45:70 */     writeHeaders();
/* 46:71 */     return this.servletResponse.getOutputStream();
/* 47:   */   }
/* 48:   */   
/* 49:   */   public void close()
/* 50:   */   {
/* 51:75 */     writeHeaders();
/* 52:   */   }
/* 53:   */   
/* 54:   */   private void writeHeaders()
/* 55:   */   {
/* 56:79 */     if (!this.headersWritten)
/* 57:   */     {
/* 58:   */       Iterator localIterator2;
/* 59:80 */       for (Iterator localIterator1 = this.headers.entrySet().iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/* 60:   */       {
/* 61:80 */         Map.Entry<String, List<String>> entry = (Map.Entry)localIterator1.next();
/* 62:81 */         String headerName = (String)entry.getKey();
/* 63:82 */         localIterator2 = ((List)entry.getValue()).iterator(); continue;String headerValue = (String)localIterator2.next();
/* 64:83 */         this.servletResponse.addHeader(headerName, headerValue);
/* 65:   */       }
/* 66:86 */       this.headersWritten = true;
/* 67:   */     }
/* 68:   */   }
/* 69:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.server.ServletServerHttpResponse
 * JD-Core Version:    0.7.0.1
 */