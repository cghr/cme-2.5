/*   1:    */ package org.springframework.remoting.httpinvoker;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.util.zip.GZIPInputStream;
/*   7:    */ import org.apache.commons.httpclient.Header;
/*   8:    */ import org.apache.commons.httpclient.HttpClient;
/*   9:    */ import org.apache.commons.httpclient.HttpConnectionManager;
/*  10:    */ import org.apache.commons.httpclient.HttpException;
/*  11:    */ import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
/*  12:    */ import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
/*  13:    */ import org.apache.commons.httpclient.methods.PostMethod;
/*  14:    */ import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
/*  15:    */ import org.springframework.context.i18n.LocaleContext;
/*  16:    */ import org.springframework.context.i18n.LocaleContextHolder;
/*  17:    */ import org.springframework.remoting.support.RemoteInvocationResult;
/*  18:    */ import org.springframework.util.Assert;
/*  19:    */ import org.springframework.util.StringUtils;
/*  20:    */ 
/*  21:    */ public class CommonsHttpInvokerRequestExecutor
/*  22:    */   extends AbstractHttpInvokerRequestExecutor
/*  23:    */ {
/*  24:    */   private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = 60000;
/*  25:    */   private HttpClient httpClient;
/*  26:    */   
/*  27:    */   public CommonsHttpInvokerRequestExecutor()
/*  28:    */   {
/*  29: 69 */     this.httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
/*  30: 70 */     setReadTimeout(60000);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public CommonsHttpInvokerRequestExecutor(HttpClient httpClient)
/*  34:    */   {
/*  35: 80 */     this.httpClient = httpClient;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setHttpClient(HttpClient httpClient)
/*  39:    */   {
/*  40: 88 */     this.httpClient = httpClient;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public HttpClient getHttpClient()
/*  44:    */   {
/*  45: 95 */     return this.httpClient;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setConnectTimeout(int timeout)
/*  49:    */   {
/*  50:105 */     Assert.isTrue(timeout >= 0, "Timeout must be a non-negative value");
/*  51:106 */     this.httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setReadTimeout(int timeout)
/*  55:    */   {
/*  56:117 */     Assert.isTrue(timeout >= 0, "Timeout must be a non-negative value");
/*  57:118 */     this.httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos)
/*  61:    */     throws IOException, ClassNotFoundException
/*  62:    */   {
/*  63:137 */     PostMethod postMethod = createPostMethod(config);
/*  64:    */     try
/*  65:    */     {
/*  66:139 */       setRequestBody(config, postMethod, baos);
/*  67:140 */       executePostMethod(config, getHttpClient(), postMethod);
/*  68:141 */       validateResponse(config, postMethod);
/*  69:142 */       InputStream responseBody = getResponseBody(config, postMethod);
/*  70:143 */       return readRemoteInvocationResult(responseBody, config.getCodebaseUrl());
/*  71:    */     }
/*  72:    */     finally
/*  73:    */     {
/*  74:147 */       postMethod.releaseConnection();
/*  75:    */     }
/*  76:    */   }
/*  77:    */   
/*  78:    */   protected PostMethod createPostMethod(HttpInvokerClientConfiguration config)
/*  79:    */     throws IOException
/*  80:    */   {
/*  81:161 */     PostMethod postMethod = new PostMethod(config.getServiceUrl());
/*  82:162 */     LocaleContext locale = LocaleContextHolder.getLocaleContext();
/*  83:163 */     if (locale != null) {
/*  84:164 */       postMethod.addRequestHeader("Accept-Language", StringUtils.toLanguageTag(locale.getLocale()));
/*  85:    */     }
/*  86:166 */     if (isAcceptGzipEncoding()) {
/*  87:167 */       postMethod.addRequestHeader("Accept-Encoding", "gzip");
/*  88:    */     }
/*  89:169 */     return postMethod;
/*  90:    */   }
/*  91:    */   
/*  92:    */   protected void setRequestBody(HttpInvokerClientConfiguration config, PostMethod postMethod, ByteArrayOutputStream baos)
/*  93:    */     throws IOException
/*  94:    */   {
/*  95:190 */     postMethod.setRequestEntity(new ByteArrayRequestEntity(baos.toByteArray(), getContentType()));
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected void executePostMethod(HttpInvokerClientConfiguration config, HttpClient httpClient, PostMethod postMethod)
/*  99:    */     throws IOException
/* 100:    */   {
/* 101:205 */     httpClient.executeMethod(postMethod);
/* 102:    */   }
/* 103:    */   
/* 104:    */   protected void validateResponse(HttpInvokerClientConfiguration config, PostMethod postMethod)
/* 105:    */     throws IOException
/* 106:    */   {
/* 107:222 */     if (postMethod.getStatusCode() >= 300) {
/* 108:223 */       throw new HttpException(
/* 109:224 */         "Did not receive successful HTTP response: status code = " + postMethod.getStatusCode() + 
/* 110:225 */         ", status message = [" + postMethod.getStatusText() + "]");
/* 111:    */     }
/* 112:    */   }
/* 113:    */   
/* 114:    */   protected InputStream getResponseBody(HttpInvokerClientConfiguration config, PostMethod postMethod)
/* 115:    */     throws IOException
/* 116:    */   {
/* 117:246 */     if (isGzipResponse(postMethod)) {
/* 118:247 */       return new GZIPInputStream(postMethod.getResponseBodyAsStream());
/* 119:    */     }
/* 120:250 */     return postMethod.getResponseBodyAsStream();
/* 121:    */   }
/* 122:    */   
/* 123:    */   protected boolean isGzipResponse(PostMethod postMethod)
/* 124:    */   {
/* 125:262 */     Header encodingHeader = postMethod.getResponseHeader("Content-Encoding");
/* 126:    */     
/* 127:264 */     return (encodingHeader != null) && (encodingHeader.getValue() != null) && (encodingHeader.getValue().toLowerCase().contains("gzip"));
/* 128:    */   }
/* 129:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor
 * JD-Core Version:    0.7.0.1
 */