/*  1:   */ package org.springframework.http.client;
/*  2:   */ 
/*  3:   */ import java.io.ByteArrayOutputStream;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.OutputStream;
/*  6:   */ import org.springframework.http.HttpHeaders;
/*  7:   */ 
/*  8:   */ abstract class AbstractBufferingClientHttpRequest
/*  9:   */   extends AbstractClientHttpRequest
/* 10:   */ {
/* 11:33 */   private ByteArrayOutputStream bufferedOutput = new ByteArrayOutputStream();
/* 12:   */   
/* 13:   */   protected OutputStream getBodyInternal(HttpHeaders headers)
/* 14:   */     throws IOException
/* 15:   */   {
/* 16:37 */     return this.bufferedOutput;
/* 17:   */   }
/* 18:   */   
/* 19:   */   protected ClientHttpResponse executeInternal(HttpHeaders headers)
/* 20:   */     throws IOException
/* 21:   */   {
/* 22:42 */     byte[] bytes = this.bufferedOutput.toByteArray();
/* 23:43 */     if (headers.getContentLength() == -1L) {
/* 24:44 */       headers.setContentLength(bytes.length);
/* 25:   */     }
/* 26:46 */     ClientHttpResponse result = executeInternal(headers, bytes);
/* 27:47 */     this.bufferedOutput = null;
/* 28:48 */     return result;
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected abstract ClientHttpResponse executeInternal(HttpHeaders paramHttpHeaders, byte[] paramArrayOfByte)
/* 32:   */     throws IOException;
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.AbstractBufferingClientHttpRequest
 * JD-Core Version:    0.7.0.1
 */