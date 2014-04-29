/*   1:    */ package org.springframework.http.client;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.net.URI;
/*   5:    */ import org.apache.commons.httpclient.HttpClient;
/*   6:    */ import org.apache.commons.httpclient.HttpConnectionManager;
/*   7:    */ import org.apache.commons.httpclient.HttpMethodBase;
/*   8:    */ import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
/*   9:    */ import org.apache.commons.httpclient.methods.DeleteMethod;
/*  10:    */ import org.apache.commons.httpclient.methods.GetMethod;
/*  11:    */ import org.apache.commons.httpclient.methods.HeadMethod;
/*  12:    */ import org.apache.commons.httpclient.methods.OptionsMethod;
/*  13:    */ import org.apache.commons.httpclient.methods.PostMethod;
/*  14:    */ import org.apache.commons.httpclient.methods.PutMethod;
/*  15:    */ import org.apache.commons.httpclient.methods.TraceMethod;
/*  16:    */ import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
/*  17:    */ import org.springframework.beans.factory.DisposableBean;
/*  18:    */ import org.springframework.http.HttpMethod;
/*  19:    */ import org.springframework.util.Assert;
/*  20:    */ 
/*  21:    */ @Deprecated
/*  22:    */ public class CommonsClientHttpRequestFactory
/*  23:    */   implements ClientHttpRequestFactory, DisposableBean
/*  24:    */ {
/*  25:    */   private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = 60000;
/*  26:    */   private HttpClient httpClient;
/*  27:    */   
/*  28:    */   public CommonsClientHttpRequestFactory()
/*  29:    */   {
/*  30: 63 */     this.httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
/*  31: 64 */     setReadTimeout(60000);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public CommonsClientHttpRequestFactory(HttpClient httpClient)
/*  35:    */   {
/*  36: 73 */     Assert.notNull(httpClient, "httpClient must not be null");
/*  37: 74 */     this.httpClient = httpClient;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setHttpClient(HttpClient httpClient)
/*  41:    */   {
/*  42: 82 */     this.httpClient = httpClient;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public HttpClient getHttpClient()
/*  46:    */   {
/*  47: 89 */     return this.httpClient;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setConnectTimeout(int timeout)
/*  51:    */   {
/*  52: 99 */     Assert.isTrue(timeout >= 0, "Timeout must be a non-negative value");
/*  53:100 */     this.httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setReadTimeout(int timeout)
/*  57:    */   {
/*  58:110 */     Assert.isTrue(timeout >= 0, "Timeout must be a non-negative value");
/*  59:111 */     getHttpClient().getHttpConnectionManager().getParams().setSoTimeout(timeout);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod)
/*  63:    */     throws IOException
/*  64:    */   {
/*  65:116 */     HttpMethodBase commonsHttpMethod = createCommonsHttpMethod(httpMethod, uri.toString());
/*  66:117 */     postProcessCommonsHttpMethod(commonsHttpMethod);
/*  67:118 */     return new CommonsClientHttpRequest(getHttpClient(), commonsHttpMethod);
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected HttpMethodBase createCommonsHttpMethod(HttpMethod httpMethod, String uri)
/*  71:    */   {
/*  72:129 */     switch (httpMethod)
/*  73:    */     {
/*  74:    */     case DELETE: 
/*  75:131 */       return new GetMethod(uri);
/*  76:    */     case PUT: 
/*  77:133 */       return new DeleteMethod(uri);
/*  78:    */     case HEAD: 
/*  79:135 */       return new HeadMethod(uri);
/*  80:    */     case OPTIONS: 
/*  81:137 */       return new OptionsMethod(uri);
/*  82:    */     case GET: 
/*  83:139 */       return new PostMethod(uri);
/*  84:    */     case POST: 
/*  85:141 */       return new PutMethod(uri);
/*  86:    */     case TRACE: 
/*  87:143 */       return new TraceMethod(uri);
/*  88:    */     }
/*  89:145 */     throw new IllegalArgumentException("Invalid HTTP method: " + httpMethod);
/*  90:    */   }
/*  91:    */   
/*  92:    */   protected void postProcessCommonsHttpMethod(HttpMethodBase httpMethod) {}
/*  93:    */   
/*  94:    */   public void destroy()
/*  95:    */   {
/*  96:163 */     HttpConnectionManager connectionManager = getHttpClient().getHttpConnectionManager();
/*  97:164 */     if ((connectionManager instanceof MultiThreadedHttpConnectionManager)) {
/*  98:165 */       ((MultiThreadedHttpConnectionManager)connectionManager).shutdown();
/*  99:    */     }
/* 100:    */   }
/* 101:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.CommonsClientHttpRequestFactory
 * JD-Core Version:    0.7.0.1
 */