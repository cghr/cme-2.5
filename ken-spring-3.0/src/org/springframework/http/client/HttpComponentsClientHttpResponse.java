/*  1:   */ package org.springframework.http.client;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.InputStream;
/*  5:   */ import org.apache.http.Header;
/*  6:   */ import org.apache.http.HttpEntity;
/*  7:   */ import org.apache.http.HttpResponse;
/*  8:   */ import org.apache.http.StatusLine;
/*  9:   */ import org.apache.http.util.EntityUtils;
/* 10:   */ import org.springframework.http.HttpHeaders;
/* 11:   */ import org.springframework.http.HttpStatus;
/* 12:   */ 
/* 13:   */ final class HttpComponentsClientHttpResponse
/* 14:   */   implements ClientHttpResponse
/* 15:   */ {
/* 16:   */   private final HttpResponse httpResponse;
/* 17:   */   private HttpHeaders headers;
/* 18:   */   
/* 19:   */   public HttpComponentsClientHttpResponse(HttpResponse httpResponse)
/* 20:   */   {
/* 21:49 */     this.httpResponse = httpResponse;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public HttpStatus getStatusCode()
/* 25:   */     throws IOException
/* 26:   */   {
/* 27:54 */     return HttpStatus.valueOf(this.httpResponse.getStatusLine().getStatusCode());
/* 28:   */   }
/* 29:   */   
/* 30:   */   public String getStatusText()
/* 31:   */     throws IOException
/* 32:   */   {
/* 33:58 */     return this.httpResponse.getStatusLine().getReasonPhrase();
/* 34:   */   }
/* 35:   */   
/* 36:   */   public HttpHeaders getHeaders()
/* 37:   */   {
/* 38:62 */     if (this.headers == null)
/* 39:   */     {
/* 40:63 */       this.headers = new HttpHeaders();
/* 41:64 */       for (Header header : this.httpResponse.getAllHeaders()) {
/* 42:65 */         this.headers.add(header.getName(), header.getValue());
/* 43:   */       }
/* 44:   */     }
/* 45:68 */     return this.headers;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public InputStream getBody()
/* 49:   */     throws IOException
/* 50:   */   {
/* 51:72 */     HttpEntity entity = this.httpResponse.getEntity();
/* 52:73 */     return entity != null ? entity.getContent() : null;
/* 53:   */   }
/* 54:   */   
/* 55:   */   public void close()
/* 56:   */   {
/* 57:77 */     HttpEntity entity = this.httpResponse.getEntity();
/* 58:78 */     if (entity != null) {
/* 59:   */       try
/* 60:   */       {
/* 61:81 */         EntityUtils.consume(entity);
/* 62:   */       }
/* 63:   */       catch (IOException localIOException) {}
/* 64:   */     }
/* 65:   */   }
/* 66:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.HttpComponentsClientHttpResponse
 * JD-Core Version:    0.7.0.1
 */