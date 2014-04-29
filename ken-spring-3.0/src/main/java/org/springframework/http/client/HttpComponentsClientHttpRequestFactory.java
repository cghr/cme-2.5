/*   1:    */ package org.springframework.http.client;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.net.URI;
/*   5:    */ import org.apache.http.client.HttpClient;
/*   6:    */ import org.apache.http.client.methods.HttpDelete;
/*   7:    */ import org.apache.http.client.methods.HttpGet;
/*   8:    */ import org.apache.http.client.methods.HttpHead;
/*   9:    */ import org.apache.http.client.methods.HttpOptions;
/*  10:    */ import org.apache.http.client.methods.HttpPost;
/*  11:    */ import org.apache.http.client.methods.HttpPut;
/*  12:    */ import org.apache.http.client.methods.HttpTrace;
/*  13:    */ import org.apache.http.client.methods.HttpUriRequest;
/*  14:    */ import org.apache.http.conn.ClientConnectionManager;
/*  15:    */ import org.apache.http.conn.scheme.PlainSocketFactory;
/*  16:    */ import org.apache.http.conn.scheme.Scheme;
/*  17:    */ import org.apache.http.conn.scheme.SchemeRegistry;
/*  18:    */ import org.apache.http.conn.ssl.SSLSocketFactory;
/*  19:    */ import org.apache.http.impl.client.DefaultHttpClient;
/*  20:    */ import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
/*  21:    */ import org.apache.http.params.HttpParams;
/*  22:    */ import org.springframework.beans.factory.DisposableBean;
/*  23:    */ import org.springframework.http.HttpMethod;
/*  24:    */ import org.springframework.util.Assert;
/*  25:    */ 
/*  26:    */ public class HttpComponentsClientHttpRequestFactory
/*  27:    */   implements ClientHttpRequestFactory, DisposableBean
/*  28:    */ {
/*  29:    */   private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;
/*  30:    */   private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5;
/*  31:    */   private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = 60000;
/*  32:    */   private HttpClient httpClient;
/*  33:    */   
/*  34:    */   public HttpComponentsClientHttpRequestFactory()
/*  35:    */   {
/*  36: 71 */     SchemeRegistry schemeRegistry = new SchemeRegistry();
/*  37: 72 */     schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
/*  38: 73 */     schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
/*  39:    */     
/*  40: 75 */     ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager(schemeRegistry);
/*  41: 76 */     connectionManager.setMaxTotal(100);
/*  42: 77 */     connectionManager.setDefaultMaxPerRoute(5);
/*  43:    */     
/*  44: 79 */     this.httpClient = new DefaultHttpClient(connectionManager);
/*  45: 80 */     setReadTimeout(60000);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public HttpComponentsClientHttpRequestFactory(HttpClient httpClient)
/*  49:    */   {
/*  50: 90 */     Assert.notNull(httpClient, "HttpClient must not be null");
/*  51: 91 */     this.httpClient = httpClient;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setHttpClient(HttpClient httpClient)
/*  55:    */   {
/*  56: 99 */     this.httpClient = httpClient;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public HttpClient getHttpClient()
/*  60:    */   {
/*  61:106 */     return this.httpClient;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void setConnectTimeout(int timeout)
/*  65:    */   {
/*  66:115 */     Assert.isTrue(timeout >= 0, "Timeout must be a non-negative value");
/*  67:116 */     getHttpClient().getParams().setIntParameter("http.connection.timeout", timeout);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void setReadTimeout(int timeout)
/*  71:    */   {
/*  72:125 */     Assert.isTrue(timeout >= 0, "Timeout must be a non-negative value");
/*  73:126 */     getHttpClient().getParams().setIntParameter("http.socket.timeout", timeout);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod)
/*  77:    */     throws IOException
/*  78:    */   {
/*  79:131 */     HttpUriRequest httpRequest = createHttpUriRequest(httpMethod, uri);
/*  80:132 */     postProcessHttpRequest(httpRequest);
/*  81:133 */     return new HttpComponentsClientHttpRequest(getHttpClient(), httpRequest);
/*  82:    */   }
/*  83:    */   
/*  84:    */   protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri)
/*  85:    */   {
/*  86:143 */     switch (httpMethod)
/*  87:    */     {
/*  88:    */     case DELETE: 
/*  89:145 */       return new HttpGet(uri);
/*  90:    */     case PUT: 
/*  91:147 */       return new HttpDelete(uri);
/*  92:    */     case HEAD: 
/*  93:149 */       return new HttpHead(uri);
/*  94:    */     case OPTIONS: 
/*  95:151 */       return new HttpOptions(uri);
/*  96:    */     case GET: 
/*  97:153 */       return new HttpPost(uri);
/*  98:    */     case POST: 
/*  99:155 */       return new HttpPut(uri);
/* 100:    */     case TRACE: 
/* 101:157 */       return new HttpTrace(uri);
/* 102:    */     }
/* 103:159 */     throw new IllegalArgumentException("Invalid HTTP method: " + httpMethod);
/* 104:    */   }
/* 105:    */   
/* 106:    */   protected void postProcessHttpRequest(HttpUriRequest request) {}
/* 107:    */   
/* 108:    */   public void destroy()
/* 109:    */   {
/* 110:178 */     getHttpClient().getConnectionManager().shutdown();
/* 111:    */   }
/* 112:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.HttpComponentsClientHttpRequestFactory
 * JD-Core Version:    0.7.0.1
 */