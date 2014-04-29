/*  1:   */ package org.springframework.http.client;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.net.URI;
/*  5:   */ import java.util.Iterator;
/*  6:   */ import java.util.List;
/*  7:   */ import org.springframework.http.HttpHeaders;
/*  8:   */ import org.springframework.http.HttpMethod;
/*  9:   */ import org.springframework.http.HttpRequest;
/* 10:   */ import org.springframework.util.FileCopyUtils;
/* 11:   */ 
/* 12:   */ class InterceptingClientHttpRequest
/* 13:   */   extends AbstractBufferingClientHttpRequest
/* 14:   */ {
/* 15:   */   private final ClientHttpRequestFactory requestFactory;
/* 16:   */   private final List<ClientHttpRequestInterceptor> interceptors;
/* 17:   */   private HttpMethod method;
/* 18:   */   private URI uri;
/* 19:   */   
/* 20:   */   protected InterceptingClientHttpRequest(ClientHttpRequestFactory requestFactory, List<ClientHttpRequestInterceptor> interceptors, URI uri, HttpMethod method)
/* 21:   */   {
/* 22:49 */     this.requestFactory = requestFactory;
/* 23:50 */     this.interceptors = interceptors;
/* 24:51 */     this.method = method;
/* 25:52 */     this.uri = uri;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public HttpMethod getMethod()
/* 29:   */   {
/* 30:56 */     return this.method;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public URI getURI()
/* 34:   */   {
/* 35:60 */     return this.uri;
/* 36:   */   }
/* 37:   */   
/* 38:   */   protected final ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput)
/* 39:   */     throws IOException
/* 40:   */   {
/* 41:65 */     RequestExecution requestExecution = new RequestExecution(null);
/* 42:   */     
/* 43:67 */     return requestExecution.execute(this, bufferedOutput);
/* 44:   */   }
/* 45:   */   
/* 46:   */   private class RequestExecution
/* 47:   */     implements ClientHttpRequestExecution
/* 48:   */   {
/* 49:75 */     private final Iterator<ClientHttpRequestInterceptor> iterator = InterceptingClientHttpRequest.this.interceptors.iterator();
/* 50:   */     
/* 51:   */     private RequestExecution() {}
/* 52:   */     
/* 53:   */     public ClientHttpResponse execute(HttpRequest request, byte[] body)
/* 54:   */       throws IOException
/* 55:   */     {
/* 56:79 */       if (this.iterator.hasNext())
/* 57:   */       {
/* 58:80 */         ClientHttpRequestInterceptor nextInterceptor = (ClientHttpRequestInterceptor)this.iterator.next();
/* 59:81 */         return nextInterceptor.intercept(request, body, this);
/* 60:   */       }
/* 61:84 */       ClientHttpRequest delegate = InterceptingClientHttpRequest.this.requestFactory.createRequest(request.getURI(), request.getMethod());
/* 62:   */       
/* 63:86 */       delegate.getHeaders().putAll(request.getHeaders());
/* 64:88 */       if (body.length > 0) {
/* 65:89 */         FileCopyUtils.copy(body, delegate.getBody());
/* 66:   */       }
/* 67:91 */       return delegate.execute();
/* 68:   */     }
/* 69:   */   }
/* 70:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.InterceptingClientHttpRequest
 * JD-Core Version:    0.7.0.1
 */