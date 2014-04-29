/*   1:    */ package org.springframework.http.client;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.net.HttpURLConnection;
/*   5:    */ import java.net.Proxy;
/*   6:    */ import java.net.URI;
/*   7:    */ import java.net.URL;
/*   8:    */ import java.net.URLConnection;
/*   9:    */ import org.springframework.http.HttpMethod;
/*  10:    */ import org.springframework.util.Assert;
/*  11:    */ 
/*  12:    */ public class SimpleClientHttpRequestFactory
/*  13:    */   implements ClientHttpRequestFactory
/*  14:    */ {
/*  15:    */   private static final int DEFAULT_CHUNK_SIZE = 4096;
/*  16:    */   private Proxy proxy;
/*  17: 45 */   private boolean bufferRequestBody = true;
/*  18: 47 */   private int chunkSize = 4096;
/*  19: 49 */   private int connectTimeout = -1;
/*  20: 51 */   private int readTimeout = -1;
/*  21:    */   
/*  22:    */   public void setProxy(Proxy proxy)
/*  23:    */   {
/*  24: 58 */     this.proxy = proxy;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setBufferRequestBody(boolean bufferRequestBody)
/*  28:    */   {
/*  29: 73 */     this.bufferRequestBody = bufferRequestBody;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setChunkSize(int chunkSize)
/*  33:    */   {
/*  34: 84 */     this.chunkSize = chunkSize;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setConnectTimeout(int connectTimeout)
/*  38:    */   {
/*  39: 94 */     this.connectTimeout = connectTimeout;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setReadTimeout(int readTimeout)
/*  43:    */   {
/*  44:104 */     this.readTimeout = readTimeout;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod)
/*  48:    */     throws IOException
/*  49:    */   {
/*  50:109 */     HttpURLConnection connection = openConnection(uri.toURL(), this.proxy);
/*  51:110 */     prepareConnection(connection, httpMethod.name());
/*  52:111 */     if (this.bufferRequestBody) {
/*  53:112 */       return new SimpleBufferingClientHttpRequest(connection);
/*  54:    */     }
/*  55:115 */     return new SimpleStreamingClientHttpRequest(connection, this.chunkSize);
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected HttpURLConnection openConnection(URL url, Proxy proxy)
/*  59:    */     throws IOException
/*  60:    */   {
/*  61:129 */     URLConnection urlConnection = proxy != null ? url.openConnection(proxy) : url.openConnection();
/*  62:130 */     Assert.isInstanceOf(HttpURLConnection.class, urlConnection);
/*  63:131 */     return (HttpURLConnection)urlConnection;
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected void prepareConnection(HttpURLConnection connection, String httpMethod)
/*  67:    */     throws IOException
/*  68:    */   {
/*  69:142 */     if (this.connectTimeout >= 0) {
/*  70:143 */       connection.setConnectTimeout(this.connectTimeout);
/*  71:    */     }
/*  72:145 */     if (this.readTimeout >= 0) {
/*  73:146 */       connection.setReadTimeout(this.readTimeout);
/*  74:    */     }
/*  75:148 */     connection.setDoInput(true);
/*  76:149 */     if ("GET".equals(httpMethod)) {
/*  77:150 */       connection.setInstanceFollowRedirects(true);
/*  78:    */     } else {
/*  79:153 */       connection.setInstanceFollowRedirects(false);
/*  80:    */     }
/*  81:155 */     if (("PUT".equals(httpMethod)) || ("POST".equals(httpMethod))) {
/*  82:156 */       connection.setDoOutput(true);
/*  83:    */     } else {
/*  84:159 */       connection.setDoOutput(false);
/*  85:    */     }
/*  86:161 */     connection.setRequestMethod(httpMethod);
/*  87:    */   }
/*  88:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.SimpleClientHttpRequestFactory
 * JD-Core Version:    0.7.0.1
 */