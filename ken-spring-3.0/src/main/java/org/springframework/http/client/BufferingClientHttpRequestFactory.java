/*  1:   */ package org.springframework.http.client;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.net.URI;
/*  5:   */ import org.springframework.http.HttpMethod;
/*  6:   */ 
/*  7:   */ public class BufferingClientHttpRequestFactory
/*  8:   */   extends AbstractClientHttpRequestFactoryWrapper
/*  9:   */ {
/* 10:   */   public BufferingClientHttpRequestFactory(ClientHttpRequestFactory requestFactory)
/* 11:   */   {
/* 12:35 */     super(requestFactory);
/* 13:   */   }
/* 14:   */   
/* 15:   */   protected ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod, ClientHttpRequestFactory requestFactory)
/* 16:   */     throws IOException
/* 17:   */   {
/* 18:41 */     ClientHttpRequest request = requestFactory.createRequest(uri, httpMethod);
/* 19:42 */     if (shouldBuffer(uri, httpMethod)) {
/* 20:43 */       return new BufferingClientHttpRequestWrapper(request);
/* 21:   */     }
/* 22:46 */     return request;
/* 23:   */   }
/* 24:   */   
/* 25:   */   protected boolean shouldBuffer(URI uri, HttpMethod httpMethod)
/* 26:   */   {
/* 27:61 */     return true;
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.BufferingClientHttpRequestFactory
 * JD-Core Version:    0.7.0.1
 */