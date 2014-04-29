/*   1:    */ package org.springframework.remoting.httpinvoker;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.util.zip.GZIPInputStream;
/*   7:    */ import org.apache.http.Header;
/*   8:    */ import org.apache.http.HttpEntity;
/*   9:    */ import org.apache.http.HttpResponse;
/*  10:    */ import org.apache.http.NoHttpResponseException;
/*  11:    */ import org.apache.http.StatusLine;
/*  12:    */ import org.apache.http.client.HttpClient;
/*  13:    */ import org.apache.http.client.methods.HttpPost;
/*  14:    */ import org.apache.http.conn.scheme.PlainSocketFactory;
/*  15:    */ import org.apache.http.conn.scheme.Scheme;
/*  16:    */ import org.apache.http.conn.scheme.SchemeRegistry;
/*  17:    */ import org.apache.http.conn.ssl.SSLSocketFactory;
/*  18:    */ import org.apache.http.entity.ByteArrayEntity;
/*  19:    */ import org.apache.http.impl.client.DefaultHttpClient;
/*  20:    */ import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
/*  21:    */ import org.apache.http.params.HttpParams;
/*  22:    */ import org.springframework.context.i18n.LocaleContext;
/*  23:    */ import org.springframework.context.i18n.LocaleContextHolder;
/*  24:    */ import org.springframework.remoting.support.RemoteInvocationResult;
/*  25:    */ import org.springframework.util.Assert;
/*  26:    */ import org.springframework.util.StringUtils;
/*  27:    */ 
/*  28:    */ public class HttpComponentsHttpInvokerRequestExecutor
/*  29:    */   extends AbstractHttpInvokerRequestExecutor
/*  30:    */ {
/*  31:    */   private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;
/*  32:    */   private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5;
/*  33:    */   private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = 60000;
/*  34:    */   private HttpClient httpClient;
/*  35:    */   
/*  36:    */   public HttpComponentsHttpInvokerRequestExecutor()
/*  37:    */   {
/*  38: 74 */     SchemeRegistry schemeRegistry = new SchemeRegistry();
/*  39: 75 */     schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
/*  40: 76 */     schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
/*  41:    */     
/*  42: 78 */     ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager(schemeRegistry);
/*  43: 79 */     connectionManager.setMaxTotal(100);
/*  44: 80 */     connectionManager.setDefaultMaxPerRoute(5);
/*  45:    */     
/*  46: 82 */     this.httpClient = new DefaultHttpClient(connectionManager);
/*  47: 83 */     setReadTimeout(60000);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public HttpComponentsHttpInvokerRequestExecutor(HttpClient httpClient)
/*  51:    */   {
/*  52: 92 */     this.httpClient = httpClient;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setHttpClient(HttpClient httpClient)
/*  56:    */   {
/*  57:100 */     this.httpClient = httpClient;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public HttpClient getHttpClient()
/*  61:    */   {
/*  62:107 */     return this.httpClient;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setConnectTimeout(int timeout)
/*  66:    */   {
/*  67:116 */     Assert.isTrue(timeout >= 0, "Timeout must be a non-negative value");
/*  68:117 */     getHttpClient().getParams().setIntParameter("http.connection.timeout", timeout);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setReadTimeout(int timeout)
/*  72:    */   {
/*  73:127 */     Assert.isTrue(timeout >= 0, "Timeout must be a non-negative value");
/*  74:128 */     getHttpClient().getParams().setIntParameter("http.socket.timeout", timeout);
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos)
/*  78:    */     throws IOException, ClassNotFoundException
/*  79:    */   {
/*  80:147 */     HttpPost postMethod = createHttpPost(config);
/*  81:148 */     setRequestBody(config, postMethod, baos);
/*  82:149 */     HttpResponse response = executeHttpPost(config, getHttpClient(), postMethod);
/*  83:150 */     validateResponse(config, response);
/*  84:151 */     InputStream responseBody = getResponseBody(config, response);
/*  85:152 */     return readRemoteInvocationResult(responseBody, config.getCodebaseUrl());
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected HttpPost createHttpPost(HttpInvokerClientConfiguration config)
/*  89:    */     throws IOException
/*  90:    */   {
/*  91:165 */     HttpPost httpPost = new HttpPost(config.getServiceUrl());
/*  92:166 */     LocaleContext locale = LocaleContextHolder.getLocaleContext();
/*  93:167 */     if (locale != null) {
/*  94:168 */       httpPost.addHeader("Accept-Language", StringUtils.toLanguageTag(locale.getLocale()));
/*  95:    */     }
/*  96:170 */     if (isAcceptGzipEncoding()) {
/*  97:171 */       httpPost.addHeader("Accept-Encoding", "gzip");
/*  98:    */     }
/*  99:173 */     return httpPost;
/* 100:    */   }
/* 101:    */   
/* 102:    */   protected void setRequestBody(HttpInvokerClientConfiguration config, HttpPost httpPost, ByteArrayOutputStream baos)
/* 103:    */     throws IOException
/* 104:    */   {
/* 105:191 */     ByteArrayEntity entity = new ByteArrayEntity(baos.toByteArray());
/* 106:192 */     entity.setContentType(getContentType());
/* 107:193 */     httpPost.setEntity(entity);
/* 108:    */   }
/* 109:    */   
/* 110:    */   protected HttpResponse executeHttpPost(HttpInvokerClientConfiguration config, HttpClient httpClient, HttpPost httpPost)
/* 111:    */     throws IOException
/* 112:    */   {
/* 113:208 */     return httpClient.execute(httpPost);
/* 114:    */   }
/* 115:    */   
/* 116:    */   protected void validateResponse(HttpInvokerClientConfiguration config, HttpResponse response)
/* 117:    */     throws IOException
/* 118:    */   {
/* 119:223 */     StatusLine status = response.getStatusLine();
/* 120:224 */     if (status.getStatusCode() >= 300) {
/* 121:225 */       throw new NoHttpResponseException(
/* 122:226 */         "Did not receive successful HTTP response: status code = " + status.getStatusCode() + 
/* 123:227 */         ", status message = [" + status.getReasonPhrase() + "]");
/* 124:    */     }
/* 125:    */   }
/* 126:    */   
/* 127:    */   protected InputStream getResponseBody(HttpInvokerClientConfiguration config, HttpResponse httpResponse)
/* 128:    */     throws IOException
/* 129:    */   {
/* 130:246 */     if (isGzipResponse(httpResponse)) {
/* 131:247 */       return new GZIPInputStream(httpResponse.getEntity().getContent());
/* 132:    */     }
/* 133:250 */     return httpResponse.getEntity().getContent();
/* 134:    */   }
/* 135:    */   
/* 136:    */   protected boolean isGzipResponse(HttpResponse httpResponse)
/* 137:    */   {
/* 138:262 */     Header encodingHeader = httpResponse.getFirstHeader("Content-Encoding");
/* 139:    */     
/* 140:264 */     return (encodingHeader != null) && (encodingHeader.getValue() != null) && (encodingHeader.getValue().toLowerCase().contains("gzip"));
/* 141:    */   }
/* 142:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.httpinvoker.HttpComponentsHttpInvokerRequestExecutor
 * JD-Core Version:    0.7.0.1
 */