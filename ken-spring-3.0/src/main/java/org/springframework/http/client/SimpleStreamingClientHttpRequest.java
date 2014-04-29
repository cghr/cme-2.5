/*   1:    */ package org.springframework.http.client;
/*   2:    */ 
/*   3:    */ import java.io.FilterOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.net.HttpURLConnection;
/*   7:    */ import java.net.URI;
/*   8:    */ import java.net.URISyntaxException;
/*   9:    */ import java.net.URL;
/*  10:    */ import java.util.Iterator;
/*  11:    */ import java.util.List;
/*  12:    */ import java.util.Map.Entry;
/*  13:    */ import java.util.Set;
/*  14:    */ import org.springframework.http.HttpHeaders;
/*  15:    */ import org.springframework.http.HttpMethod;
/*  16:    */ 
/*  17:    */ final class SimpleStreamingClientHttpRequest
/*  18:    */   extends AbstractClientHttpRequest
/*  19:    */ {
/*  20:    */   private final HttpURLConnection connection;
/*  21:    */   private final int chunkSize;
/*  22:    */   private OutputStream body;
/*  23:    */   
/*  24:    */   SimpleStreamingClientHttpRequest(HttpURLConnection connection, int chunkSize)
/*  25:    */   {
/*  26: 49 */     this.connection = connection;
/*  27: 50 */     this.chunkSize = chunkSize;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public HttpMethod getMethod()
/*  31:    */   {
/*  32: 54 */     return HttpMethod.valueOf(this.connection.getRequestMethod());
/*  33:    */   }
/*  34:    */   
/*  35:    */   public URI getURI()
/*  36:    */   {
/*  37:    */     try
/*  38:    */     {
/*  39: 59 */       return this.connection.getURL().toURI();
/*  40:    */     }
/*  41:    */     catch (URISyntaxException ex)
/*  42:    */     {
/*  43: 62 */       throw new IllegalStateException("Could not get HttpURLConnection URI: " + ex.getMessage(), ex);
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected OutputStream getBodyInternal(HttpHeaders headers)
/*  48:    */     throws IOException
/*  49:    */   {
/*  50: 68 */     if (this.body == null)
/*  51:    */     {
/*  52: 69 */       int contentLength = (int)headers.getContentLength();
/*  53: 70 */       if (contentLength >= 0) {
/*  54: 71 */         this.connection.setFixedLengthStreamingMode(contentLength);
/*  55:    */       } else {
/*  56: 74 */         this.connection.setChunkedStreamingMode(this.chunkSize);
/*  57:    */       }
/*  58:    */       Iterator localIterator2;
/*  59: 76 */       for (Iterator localIterator1 = headers.entrySet().iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/*  60:    */       {
/*  61: 76 */         Map.Entry<String, List<String>> entry = (Map.Entry)localIterator1.next();
/*  62: 77 */         String headerName = (String)entry.getKey();
/*  63: 78 */         localIterator2 = ((List)entry.getValue()).iterator(); continue;String headerValue = (String)localIterator2.next();
/*  64: 79 */         this.connection.addRequestProperty(headerName, headerValue);
/*  65:    */       }
/*  66: 82 */       this.connection.connect();
/*  67: 83 */       this.body = this.connection.getOutputStream();
/*  68:    */     }
/*  69: 85 */     return new NonClosingOutputStream(this.body, null);
/*  70:    */   }
/*  71:    */   
/*  72:    */   protected ClientHttpResponse executeInternal(HttpHeaders headers)
/*  73:    */     throws IOException
/*  74:    */   {
/*  75:    */     try
/*  76:    */     {
/*  77: 91 */       if (this.body != null) {
/*  78: 92 */         this.body.close();
/*  79:    */       }
/*  80:    */     }
/*  81:    */     catch (IOException localIOException) {}
/*  82: 98 */     return new SimpleClientHttpResponse(this.connection);
/*  83:    */   }
/*  84:    */   
/*  85:    */   private static class NonClosingOutputStream
/*  86:    */     extends FilterOutputStream
/*  87:    */   {
/*  88:    */     private NonClosingOutputStream(OutputStream out)
/*  89:    */     {
/*  90:105 */       super();
/*  91:    */     }
/*  92:    */     
/*  93:    */     public void close()
/*  94:    */       throws IOException
/*  95:    */     {}
/*  96:    */   }
/*  97:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.SimpleStreamingClientHttpRequest
 * JD-Core Version:    0.7.0.1
 */