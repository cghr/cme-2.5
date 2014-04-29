/*  1:   */ package org.springframework.http.client;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.OutputStream;
/*  5:   */ import java.net.URI;
/*  6:   */ import org.springframework.http.HttpHeaders;
/*  7:   */ import org.springframework.http.HttpMethod;
/*  8:   */ import org.springframework.util.Assert;
/*  9:   */ import org.springframework.util.FileCopyUtils;
/* 10:   */ 
/* 11:   */ final class BufferingClientHttpRequestWrapper
/* 12:   */   extends AbstractBufferingClientHttpRequest
/* 13:   */ {
/* 14:   */   private final ClientHttpRequest request;
/* 15:   */   
/* 16:   */   BufferingClientHttpRequestWrapper(ClientHttpRequest request)
/* 17:   */   {
/* 18:40 */     Assert.notNull(request, "'request' must not be null");
/* 19:41 */     this.request = request;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public HttpMethod getMethod()
/* 23:   */   {
/* 24:46 */     return this.request.getMethod();
/* 25:   */   }
/* 26:   */   
/* 27:   */   public URI getURI()
/* 28:   */   {
/* 29:50 */     return this.request.getURI();
/* 30:   */   }
/* 31:   */   
/* 32:   */   protected ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput)
/* 33:   */     throws IOException
/* 34:   */   {
/* 35:55 */     this.request.getHeaders().putAll(headers);
/* 36:56 */     OutputStream body = this.request.getBody();
/* 37:57 */     FileCopyUtils.copy(bufferedOutput, body);
/* 38:58 */     ClientHttpResponse response = this.request.execute();
/* 39:59 */     return new BufferingClientHttpResponseWrapper(response);
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.BufferingClientHttpRequestWrapper
 * JD-Core Version:    0.7.0.1
 */