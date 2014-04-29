/*  1:   */ package org.springframework.http.client;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.InputStream;
/*  5:   */ import org.apache.commons.httpclient.Header;
/*  6:   */ import org.apache.commons.httpclient.HttpMethod;
/*  7:   */ import org.springframework.http.HttpHeaders;
/*  8:   */ import org.springframework.http.HttpStatus;
/*  9:   */ 
/* 10:   */ @Deprecated
/* 11:   */ final class CommonsClientHttpResponse
/* 12:   */   implements ClientHttpResponse
/* 13:   */ {
/* 14:   */   private final HttpMethod httpMethod;
/* 15:   */   private HttpHeaders headers;
/* 16:   */   
/* 17:   */   CommonsClientHttpResponse(HttpMethod httpMethod)
/* 18:   */   {
/* 19:48 */     this.httpMethod = httpMethod;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public HttpStatus getStatusCode()
/* 23:   */   {
/* 24:53 */     return HttpStatus.valueOf(this.httpMethod.getStatusCode());
/* 25:   */   }
/* 26:   */   
/* 27:   */   public String getStatusText()
/* 28:   */   {
/* 29:57 */     return this.httpMethod.getStatusText();
/* 30:   */   }
/* 31:   */   
/* 32:   */   public HttpHeaders getHeaders()
/* 33:   */   {
/* 34:61 */     if (this.headers == null)
/* 35:   */     {
/* 36:62 */       this.headers = new HttpHeaders();
/* 37:63 */       for (Header header : this.httpMethod.getResponseHeaders()) {
/* 38:64 */         this.headers.add(header.getName(), header.getValue());
/* 39:   */       }
/* 40:   */     }
/* 41:67 */     return this.headers;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public InputStream getBody()
/* 45:   */     throws IOException
/* 46:   */   {
/* 47:71 */     return this.httpMethod.getResponseBodyAsStream();
/* 48:   */   }
/* 49:   */   
/* 50:   */   public void close()
/* 51:   */   {
/* 52:75 */     this.httpMethod.releaseConnection();
/* 53:   */   }
/* 54:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.CommonsClientHttpResponse
 * JD-Core Version:    0.7.0.1
 */