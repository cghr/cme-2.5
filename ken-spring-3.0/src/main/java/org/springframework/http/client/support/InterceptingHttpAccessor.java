/*  1:   */ package org.springframework.http.client.support;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import org.springframework.http.client.ClientHttpRequestFactory;
/*  6:   */ import org.springframework.http.client.ClientHttpRequestInterceptor;
/*  7:   */ import org.springframework.http.client.InterceptingClientHttpRequestFactory;
/*  8:   */ import org.springframework.util.CollectionUtils;
/*  9:   */ 
/* 10:   */ public abstract class InterceptingHttpAccessor
/* 11:   */   extends HttpAccessor
/* 12:   */ {
/* 13:37 */   private List<ClientHttpRequestInterceptor> interceptors = new ArrayList();
/* 14:   */   
/* 15:   */   public void setInterceptors(List<ClientHttpRequestInterceptor> interceptors)
/* 16:   */   {
/* 17:43 */     this.interceptors = interceptors;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public List<ClientHttpRequestInterceptor> getInterceptors()
/* 21:   */   {
/* 22:50 */     return this.interceptors;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public ClientHttpRequestFactory getRequestFactory()
/* 26:   */   {
/* 27:55 */     ClientHttpRequestFactory delegate = super.getRequestFactory();
/* 28:56 */     if (!CollectionUtils.isEmpty(getInterceptors())) {
/* 29:57 */       return new InterceptingClientHttpRequestFactory(delegate, getInterceptors());
/* 30:   */     }
/* 31:60 */     return delegate;
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.support.InterceptingHttpAccessor
 * JD-Core Version:    0.7.0.1
 */