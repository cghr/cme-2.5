/*  1:   */ package org.springframework.http.client;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.net.URI;
/*  5:   */ import org.springframework.http.HttpMethod;
/*  6:   */ import org.springframework.util.Assert;
/*  7:   */ 
/*  8:   */ public abstract class AbstractClientHttpRequestFactoryWrapper
/*  9:   */   implements ClientHttpRequestFactory
/* 10:   */ {
/* 11:   */   private final ClientHttpRequestFactory requestFactory;
/* 12:   */   
/* 13:   */   protected AbstractClientHttpRequestFactoryWrapper(ClientHttpRequestFactory requestFactory)
/* 14:   */   {
/* 15:41 */     Assert.notNull(requestFactory, "'requestFactory' must not be null");
/* 16:42 */     this.requestFactory = requestFactory;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public final ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod)
/* 20:   */     throws IOException
/* 21:   */   {
/* 22:52 */     return createRequest(uri, httpMethod, this.requestFactory);
/* 23:   */   }
/* 24:   */   
/* 25:   */   protected abstract ClientHttpRequest createRequest(URI paramURI, HttpMethod paramHttpMethod, ClientHttpRequestFactory paramClientHttpRequestFactory)
/* 26:   */     throws IOException;
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.AbstractClientHttpRequestFactoryWrapper
 * JD-Core Version:    0.7.0.1
 */