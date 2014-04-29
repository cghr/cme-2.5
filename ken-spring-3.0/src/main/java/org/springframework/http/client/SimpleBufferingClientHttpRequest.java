/*  1:   */ package org.springframework.http.client;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.net.HttpURLConnection;
/*  5:   */ import java.net.URI;
/*  6:   */ import java.net.URISyntaxException;
/*  7:   */ import java.net.URL;
/*  8:   */ import java.util.Iterator;
/*  9:   */ import java.util.List;
/* 10:   */ import java.util.Map.Entry;
/* 11:   */ import java.util.Set;
/* 12:   */ import org.springframework.http.HttpHeaders;
/* 13:   */ import org.springframework.http.HttpMethod;
/* 14:   */ import org.springframework.util.FileCopyUtils;
/* 15:   */ 
/* 16:   */ final class SimpleBufferingClientHttpRequest
/* 17:   */   extends AbstractBufferingClientHttpRequest
/* 18:   */ {
/* 19:   */   private final HttpURLConnection connection;
/* 20:   */   
/* 21:   */   SimpleBufferingClientHttpRequest(HttpURLConnection connection)
/* 22:   */   {
/* 23:44 */     this.connection = connection;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public HttpMethod getMethod()
/* 27:   */   {
/* 28:49 */     return HttpMethod.valueOf(this.connection.getRequestMethod());
/* 29:   */   }
/* 30:   */   
/* 31:   */   public URI getURI()
/* 32:   */   {
/* 33:   */     try
/* 34:   */     {
/* 35:54 */       return this.connection.getURL().toURI();
/* 36:   */     }
/* 37:   */     catch (URISyntaxException ex)
/* 38:   */     {
/* 39:57 */       throw new IllegalStateException("Could not get HttpURLConnection URI: " + ex.getMessage(), ex);
/* 40:   */     }
/* 41:   */   }
/* 42:   */   
/* 43:   */   protected ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput)
/* 44:   */     throws IOException
/* 45:   */   {
/* 46:   */     Iterator localIterator2;
/* 47:63 */     for (Iterator localIterator1 = headers.entrySet().iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/* 48:   */     {
/* 49:63 */       Map.Entry<String, List<String>> entry = (Map.Entry)localIterator1.next();
/* 50:64 */       String headerName = (String)entry.getKey();
/* 51:65 */       localIterator2 = ((List)entry.getValue()).iterator(); continue;String headerValue = (String)localIterator2.next();
/* 52:66 */       this.connection.addRequestProperty(headerName, headerValue);
/* 53:   */     }
/* 54:70 */     if (this.connection.getDoOutput()) {
/* 55:71 */       this.connection.setFixedLengthStreamingMode(bufferedOutput.length);
/* 56:   */     }
/* 57:73 */     this.connection.connect();
/* 58:74 */     if (this.connection.getDoOutput()) {
/* 59:75 */       FileCopyUtils.copy(bufferedOutput, this.connection.getOutputStream());
/* 60:   */     }
/* 61:78 */     return new SimpleClientHttpResponse(this.connection);
/* 62:   */   }
/* 63:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.SimpleBufferingClientHttpRequest
 * JD-Core Version:    0.7.0.1
 */