/*  1:   */ package org.springframework.http.client;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.net.URI;
/*  5:   */ import java.util.Iterator;
/*  6:   */ import java.util.List;
/*  7:   */ import java.util.Map.Entry;
/*  8:   */ import java.util.Set;
/*  9:   */ import org.apache.http.HttpEntity;
/* 10:   */ import org.apache.http.HttpEntityEnclosingRequest;
/* 11:   */ import org.apache.http.HttpResponse;
/* 12:   */ import org.apache.http.client.HttpClient;
/* 13:   */ import org.apache.http.client.methods.HttpUriRequest;
/* 14:   */ import org.apache.http.entity.ByteArrayEntity;
/* 15:   */ import org.springframework.http.HttpHeaders;
/* 16:   */ import org.springframework.http.HttpMethod;
/* 17:   */ 
/* 18:   */ final class HttpComponentsClientHttpRequest
/* 19:   */   extends AbstractBufferingClientHttpRequest
/* 20:   */ {
/* 21:   */   private final HttpClient httpClient;
/* 22:   */   private final HttpUriRequest httpRequest;
/* 23:   */   
/* 24:   */   public HttpComponentsClientHttpRequest(HttpClient httpClient, HttpUriRequest httpRequest)
/* 25:   */   {
/* 26:54 */     this.httpClient = httpClient;
/* 27:55 */     this.httpRequest = httpRequest;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public HttpMethod getMethod()
/* 31:   */   {
/* 32:60 */     return HttpMethod.valueOf(this.httpRequest.getMethod());
/* 33:   */   }
/* 34:   */   
/* 35:   */   public URI getURI()
/* 36:   */   {
/* 37:64 */     return this.httpRequest.getURI();
/* 38:   */   }
/* 39:   */   
/* 40:   */   protected ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput)
/* 41:   */     throws IOException
/* 42:   */   {
/* 43:   */     Iterator localIterator2;
/* 44:   */     label110:
/* 45:70 */     for (Iterator localIterator1 = headers.entrySet().iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/* 46:   */     {
/* 47:70 */       Map.Entry<String, List<String>> entry = (Map.Entry)localIterator1.next();
/* 48:71 */       String headerName = (String)entry.getKey();
/* 49:72 */       if ((headerName.equalsIgnoreCase("Content-Length")) || 
/* 50:73 */         (headerName.equalsIgnoreCase("Transfer-Encoding"))) {
/* 51:   */         break label110;
/* 52:   */       }
/* 53:74 */       localIterator2 = ((List)entry.getValue()).iterator(); continue;String headerValue = (String)localIterator2.next();
/* 54:75 */       this.httpRequest.addHeader(headerName, headerValue);
/* 55:   */     }
/* 56:79 */     if ((this.httpRequest instanceof HttpEntityEnclosingRequest))
/* 57:   */     {
/* 58:80 */       HttpEntityEnclosingRequest entityEnclosingRequest = (HttpEntityEnclosingRequest)this.httpRequest;
/* 59:81 */       Object requestEntity = new ByteArrayEntity(bufferedOutput);
/* 60:82 */       entityEnclosingRequest.setEntity((HttpEntity)requestEntity);
/* 61:   */     }
/* 62:84 */     HttpResponse httpResponse = this.httpClient.execute(this.httpRequest);
/* 63:85 */     return new HttpComponentsClientHttpResponse(httpResponse);
/* 64:   */   }
/* 65:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.HttpComponentsClientHttpRequest
 * JD-Core Version:    0.7.0.1
 */