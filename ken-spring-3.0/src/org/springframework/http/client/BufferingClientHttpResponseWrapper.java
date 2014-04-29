/*  1:   */ package org.springframework.http.client;
/*  2:   */ 
/*  3:   */ import java.io.ByteArrayInputStream;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.InputStream;
/*  6:   */ import org.springframework.http.HttpHeaders;
/*  7:   */ import org.springframework.http.HttpStatus;
/*  8:   */ import org.springframework.util.FileCopyUtils;
/*  9:   */ 
/* 10:   */ final class BufferingClientHttpResponseWrapper
/* 11:   */   implements ClientHttpResponse
/* 12:   */ {
/* 13:   */   private final ClientHttpResponse response;
/* 14:   */   private byte[] body;
/* 15:   */   
/* 16:   */   BufferingClientHttpResponseWrapper(ClientHttpResponse response)
/* 17:   */   {
/* 18:42 */     this.response = response;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public HttpStatus getStatusCode()
/* 22:   */     throws IOException
/* 23:   */   {
/* 24:47 */     return this.response.getStatusCode();
/* 25:   */   }
/* 26:   */   
/* 27:   */   public String getStatusText()
/* 28:   */     throws IOException
/* 29:   */   {
/* 30:51 */     return this.response.getStatusText();
/* 31:   */   }
/* 32:   */   
/* 33:   */   public HttpHeaders getHeaders()
/* 34:   */   {
/* 35:55 */     return this.response.getHeaders();
/* 36:   */   }
/* 37:   */   
/* 38:   */   public InputStream getBody()
/* 39:   */     throws IOException
/* 40:   */   {
/* 41:59 */     if (this.body == null) {
/* 42:60 */       this.body = FileCopyUtils.copyToByteArray(this.response.getBody());
/* 43:   */     }
/* 44:62 */     return new ByteArrayInputStream(this.body);
/* 45:   */   }
/* 46:   */   
/* 47:   */   public void close()
/* 48:   */   {
/* 49:66 */     this.response.close();
/* 50:   */   }
/* 51:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.BufferingClientHttpResponseWrapper
 * JD-Core Version:    0.7.0.1
 */