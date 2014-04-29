/*  1:   */ package org.springframework.http.client;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.InputStream;
/*  5:   */ import java.net.HttpURLConnection;
/*  6:   */ import org.springframework.http.HttpHeaders;
/*  7:   */ import org.springframework.http.HttpStatus;
/*  8:   */ import org.springframework.util.StringUtils;
/*  9:   */ 
/* 10:   */ final class SimpleClientHttpResponse
/* 11:   */   implements ClientHttpResponse
/* 12:   */ {
/* 13:   */   private final HttpURLConnection connection;
/* 14:   */   private HttpHeaders headers;
/* 15:   */   
/* 16:   */   SimpleClientHttpResponse(HttpURLConnection connection)
/* 17:   */   {
/* 18:43 */     this.connection = connection;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public HttpStatus getStatusCode()
/* 22:   */     throws IOException
/* 23:   */   {
/* 24:48 */     return HttpStatus.valueOf(this.connection.getResponseCode());
/* 25:   */   }
/* 26:   */   
/* 27:   */   public String getStatusText()
/* 28:   */     throws IOException
/* 29:   */   {
/* 30:52 */     return this.connection.getResponseMessage();
/* 31:   */   }
/* 32:   */   
/* 33:   */   public HttpHeaders getHeaders()
/* 34:   */   {
/* 35:56 */     if (this.headers == null)
/* 36:   */     {
/* 37:57 */       this.headers = new HttpHeaders();
/* 38:   */       
/* 39:59 */       String name = this.connection.getHeaderFieldKey(0);
/* 40:60 */       if (StringUtils.hasLength(name)) {
/* 41:61 */         this.headers.add(name, this.connection.getHeaderField(0));
/* 42:   */       }
/* 43:63 */       int i = 1;
/* 44:   */       for (;;)
/* 45:   */       {
/* 46:65 */         name = this.connection.getHeaderFieldKey(i);
/* 47:66 */         if (!StringUtils.hasLength(name)) {
/* 48:   */           break;
/* 49:   */         }
/* 50:69 */         this.headers.add(name, this.connection.getHeaderField(i));
/* 51:70 */         i++;
/* 52:   */       }
/* 53:   */     }
/* 54:73 */     return this.headers;
/* 55:   */   }
/* 56:   */   
/* 57:   */   public InputStream getBody()
/* 58:   */     throws IOException
/* 59:   */   {
/* 60:77 */     InputStream errorStream = this.connection.getErrorStream();
/* 61:78 */     return errorStream != null ? errorStream : this.connection.getInputStream();
/* 62:   */   }
/* 63:   */   
/* 64:   */   public void close()
/* 65:   */   {
/* 66:82 */     this.connection.disconnect();
/* 67:   */   }
/* 68:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.SimpleClientHttpResponse
 * JD-Core Version:    0.7.0.1
 */