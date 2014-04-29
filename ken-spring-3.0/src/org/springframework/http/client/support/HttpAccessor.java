/*  1:   */ package org.springframework.http.client.support;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.net.URI;
/*  5:   */ import org.apache.commons.logging.Log;
/*  6:   */ import org.apache.commons.logging.LogFactory;
/*  7:   */ import org.springframework.http.HttpMethod;
/*  8:   */ import org.springframework.http.client.ClientHttpRequest;
/*  9:   */ import org.springframework.http.client.ClientHttpRequestFactory;
/* 10:   */ import org.springframework.http.client.SimpleClientHttpRequestFactory;
/* 11:   */ import org.springframework.util.Assert;
/* 12:   */ 
/* 13:   */ public abstract class HttpAccessor
/* 14:   */ {
/* 15:47 */   protected final Log logger = LogFactory.getLog(getClass());
/* 16:49 */   private ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
/* 17:   */   
/* 18:   */   public void setRequestFactory(ClientHttpRequestFactory requestFactory)
/* 19:   */   {
/* 20:56 */     Assert.notNull(requestFactory, "'requestFactory' must not be null");
/* 21:57 */     this.requestFactory = requestFactory;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public ClientHttpRequestFactory getRequestFactory()
/* 25:   */   {
/* 26:64 */     return this.requestFactory;
/* 27:   */   }
/* 28:   */   
/* 29:   */   protected ClientHttpRequest createRequest(URI url, HttpMethod method)
/* 30:   */     throws IOException
/* 31:   */   {
/* 32:76 */     ClientHttpRequest request = getRequestFactory().createRequest(url, method);
/* 33:77 */     if (this.logger.isDebugEnabled()) {
/* 34:78 */       this.logger.debug("Created " + method.name() + " request for \"" + url + "\"");
/* 35:   */     }
/* 36:80 */     return request;
/* 37:   */   }
/* 38:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.support.HttpAccessor
 * JD-Core Version:    0.7.0.1
 */