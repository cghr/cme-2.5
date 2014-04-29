/*  1:   */ package org.springframework.http.client;
/*  2:   */ 
/*  3:   */ import java.net.URI;
/*  4:   */ import java.util.Collections;
/*  5:   */ import java.util.List;
/*  6:   */ import org.springframework.http.HttpMethod;
/*  7:   */ 
/*  8:   */ public class InterceptingClientHttpRequestFactory
/*  9:   */   extends AbstractClientHttpRequestFactoryWrapper
/* 10:   */ {
/* 11:   */   private final List<ClientHttpRequestInterceptor> interceptors;
/* 12:   */   
/* 13:   */   public InterceptingClientHttpRequestFactory(ClientHttpRequestFactory requestFactory, List<ClientHttpRequestInterceptor> interceptors)
/* 14:   */   {
/* 15:43 */     super(requestFactory);
/* 16:44 */     this.interceptors = (interceptors != null ? interceptors : Collections.emptyList());
/* 17:   */   }
/* 18:   */   
/* 19:   */   protected ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod, ClientHttpRequestFactory requestFactory)
/* 20:   */   {
/* 21:49 */     return new InterceptingClientHttpRequest(requestFactory, this.interceptors, uri, httpMethod);
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.InterceptingClientHttpRequestFactory
 * JD-Core Version:    0.7.0.1
 */