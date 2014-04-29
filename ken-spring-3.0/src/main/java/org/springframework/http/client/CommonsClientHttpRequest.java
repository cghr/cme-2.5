/*  1:   */ package org.springframework.http.client;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.util.Iterator;
/*  5:   */ import java.util.List;
/*  6:   */ import java.util.Map.Entry;
/*  7:   */ import java.util.Set;
/*  8:   */ import org.apache.commons.httpclient.HttpClient;
/*  9:   */ import org.apache.commons.httpclient.HttpMethodBase;
/* 10:   */ import org.apache.commons.httpclient.URIException;
/* 11:   */ import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
/* 12:   */ import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
/* 13:   */ import org.apache.commons.httpclient.methods.RequestEntity;
/* 14:   */ import org.springframework.http.HttpHeaders;
/* 15:   */ import org.springframework.http.HttpMethod;
/* 16:   */ 
/* 17:   */ @Deprecated
/* 18:   */ final class CommonsClientHttpRequest
/* 19:   */   extends AbstractBufferingClientHttpRequest
/* 20:   */ {
/* 21:   */   private final HttpClient httpClient;
/* 22:   */   private final HttpMethodBase httpMethod;
/* 23:   */   
/* 24:   */   CommonsClientHttpRequest(HttpClient httpClient, HttpMethodBase httpMethod)
/* 25:   */   {
/* 26:54 */     this.httpClient = httpClient;
/* 27:55 */     this.httpMethod = httpMethod;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public HttpMethod getMethod()
/* 31:   */   {
/* 32:60 */     return HttpMethod.valueOf(this.httpMethod.getName());
/* 33:   */   }
/* 34:   */   
/* 35:   */   public java.net.URI getURI()
/* 36:   */   {
/* 37:   */     try
/* 38:   */     {
/* 39:65 */       return java.net.URI.create(this.httpMethod.getURI().getEscapedURI());
/* 40:   */     }
/* 41:   */     catch (URIException ex)
/* 42:   */     {
/* 43:68 */       throw new IllegalStateException("Could not get HttpMethod URI: " + ex.getMessage(), ex);
/* 44:   */     }
/* 45:   */   }
/* 46:   */   
/* 47:   */   public ClientHttpResponse executeInternal(HttpHeaders headers, byte[] output)
/* 48:   */     throws IOException
/* 49:   */   {
/* 50:   */     Iterator localIterator2;
/* 51:74 */     for (Iterator localIterator1 = headers.entrySet().iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/* 52:   */     {
/* 53:74 */       Map.Entry<String, List<String>> entry = (Map.Entry)localIterator1.next();
/* 54:75 */       String headerName = (String)entry.getKey();
/* 55:76 */       localIterator2 = ((List)entry.getValue()).iterator(); continue;String headerValue = (String)localIterator2.next();
/* 56:77 */       this.httpMethod.addRequestHeader(headerName, headerValue);
/* 57:   */     }
/* 58:80 */     if ((this.httpMethod instanceof EntityEnclosingMethod))
/* 59:   */     {
/* 60:81 */       EntityEnclosingMethod entityEnclosingMethod = (EntityEnclosingMethod)this.httpMethod;
/* 61:82 */       Object requestEntity = new ByteArrayRequestEntity(output);
/* 62:83 */       entityEnclosingMethod.setRequestEntity((RequestEntity)requestEntity);
/* 63:   */     }
/* 64:85 */     this.httpClient.executeMethod(this.httpMethod);
/* 65:86 */     return new CommonsClientHttpResponse(this.httpMethod);
/* 66:   */   }
/* 67:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.CommonsClientHttpRequest
 * JD-Core Version:    0.7.0.1
 */