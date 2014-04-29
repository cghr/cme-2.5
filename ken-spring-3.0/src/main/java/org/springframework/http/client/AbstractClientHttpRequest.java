/*  1:   */ package org.springframework.http.client;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.OutputStream;
/*  5:   */ import org.springframework.http.HttpHeaders;
/*  6:   */ import org.springframework.util.Assert;
/*  7:   */ 
/*  8:   */ public abstract class AbstractClientHttpRequest
/*  9:   */   implements ClientHttpRequest
/* 10:   */ {
/* 11:33 */   private final HttpHeaders headers = new HttpHeaders();
/* 12:35 */   private boolean executed = false;
/* 13:   */   
/* 14:   */   public final HttpHeaders getHeaders()
/* 15:   */   {
/* 16:39 */     return this.executed ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public final OutputStream getBody()
/* 20:   */     throws IOException
/* 21:   */   {
/* 22:43 */     checkExecuted();
/* 23:44 */     return getBodyInternal(this.headers);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public final ClientHttpResponse execute()
/* 27:   */     throws IOException
/* 28:   */   {
/* 29:48 */     checkExecuted();
/* 30:49 */     ClientHttpResponse result = executeInternal(this.headers);
/* 31:50 */     this.executed = true;
/* 32:51 */     return result;
/* 33:   */   }
/* 34:   */   
/* 35:   */   private void checkExecuted()
/* 36:   */   {
/* 37:55 */     Assert.state(!this.executed, "ClientHttpRequest already executed");
/* 38:   */   }
/* 39:   */   
/* 40:   */   protected abstract OutputStream getBodyInternal(HttpHeaders paramHttpHeaders)
/* 41:   */     throws IOException;
/* 42:   */   
/* 43:   */   protected abstract ClientHttpResponse executeInternal(HttpHeaders paramHttpHeaders)
/* 44:   */     throws IOException;
/* 45:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.AbstractClientHttpRequest
 * JD-Core Version:    0.7.0.1
 */