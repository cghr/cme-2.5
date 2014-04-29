/*  1:   */ package org.springframework.http.client.support;
/*  2:   */ 
/*  3:   */ import java.net.URI;
/*  4:   */ import org.springframework.http.HttpHeaders;
/*  5:   */ import org.springframework.http.HttpMethod;
/*  6:   */ import org.springframework.http.HttpRequest;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ 
/*  9:   */ public class HttpRequestWrapper
/* 10:   */   implements HttpRequest
/* 11:   */ {
/* 12:   */   private final HttpRequest request;
/* 13:   */   
/* 14:   */   public HttpRequestWrapper(HttpRequest request)
/* 15:   */   {
/* 16:44 */     Assert.notNull(request, "'request' must not be null");
/* 17:45 */     this.request = request;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public HttpRequest getRequest()
/* 21:   */   {
/* 22:52 */     return this.request;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public HttpMethod getMethod()
/* 26:   */   {
/* 27:59 */     return this.request.getMethod();
/* 28:   */   }
/* 29:   */   
/* 30:   */   public URI getURI()
/* 31:   */   {
/* 32:66 */     return this.request.getURI();
/* 33:   */   }
/* 34:   */   
/* 35:   */   public HttpHeaders getHeaders()
/* 36:   */   {
/* 37:73 */     return this.request.getHeaders();
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.support.HttpRequestWrapper
 * JD-Core Version:    0.7.0.1
 */