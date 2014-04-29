/*   1:    */ package org.springframework.remoting.httpinvoker;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.net.HttpURLConnection;
/*   7:    */ import java.net.URL;
/*   8:    */ import java.net.URLConnection;
/*   9:    */ import java.util.zip.GZIPInputStream;
/*  10:    */ import org.springframework.context.i18n.LocaleContext;
/*  11:    */ import org.springframework.context.i18n.LocaleContextHolder;
/*  12:    */ import org.springframework.remoting.support.RemoteInvocationResult;
/*  13:    */ import org.springframework.util.StringUtils;
/*  14:    */ 
/*  15:    */ public class SimpleHttpInvokerRequestExecutor
/*  16:    */   extends AbstractHttpInvokerRequestExecutor
/*  17:    */ {
/*  18: 48 */   private int connectTimeout = -1;
/*  19: 50 */   private int readTimeout = -1;
/*  20:    */   
/*  21:    */   public void setConnectTimeout(int connectTimeout)
/*  22:    */   {
/*  23: 60 */     this.connectTimeout = connectTimeout;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setReadTimeout(int readTimeout)
/*  27:    */   {
/*  28: 70 */     this.readTimeout = readTimeout;
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos)
/*  32:    */     throws IOException, ClassNotFoundException
/*  33:    */   {
/*  34: 89 */     HttpURLConnection con = openConnection(config);
/*  35: 90 */     prepareConnection(con, baos.size());
/*  36: 91 */     writeRequestBody(config, con, baos);
/*  37: 92 */     validateResponse(config, con);
/*  38: 93 */     InputStream responseBody = readResponseBody(config, con);
/*  39:    */     
/*  40: 95 */     return readRemoteInvocationResult(responseBody, config.getCodebaseUrl());
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected HttpURLConnection openConnection(HttpInvokerClientConfiguration config)
/*  44:    */     throws IOException
/*  45:    */   {
/*  46:107 */     URLConnection con = new URL(config.getServiceUrl()).openConnection();
/*  47:108 */     if (!(con instanceof HttpURLConnection)) {
/*  48:109 */       throw new IOException("Service URL [" + config.getServiceUrl() + "] is not an HTTP URL");
/*  49:    */     }
/*  50:111 */     return (HttpURLConnection)con;
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected void prepareConnection(HttpURLConnection connection, int contentLength)
/*  54:    */     throws IOException
/*  55:    */   {
/*  56:126 */     if (this.connectTimeout >= 0) {
/*  57:127 */       connection.setConnectTimeout(this.connectTimeout);
/*  58:    */     }
/*  59:129 */     if (this.readTimeout >= 0) {
/*  60:130 */       connection.setReadTimeout(this.readTimeout);
/*  61:    */     }
/*  62:132 */     connection.setDoOutput(true);
/*  63:133 */     connection.setRequestMethod("POST");
/*  64:134 */     connection.setRequestProperty("Content-Type", getContentType());
/*  65:135 */     connection.setRequestProperty("Content-Length", Integer.toString(contentLength));
/*  66:136 */     LocaleContext locale = LocaleContextHolder.getLocaleContext();
/*  67:137 */     if (locale != null) {
/*  68:138 */       connection.setRequestProperty("Accept-Language", StringUtils.toLanguageTag(locale.getLocale()));
/*  69:    */     }
/*  70:140 */     if (isAcceptGzipEncoding()) {
/*  71:141 */       connection.setRequestProperty("Accept-Encoding", "gzip");
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected void writeRequestBody(HttpInvokerClientConfiguration config, HttpURLConnection con, ByteArrayOutputStream baos)
/*  76:    */     throws IOException
/*  77:    */   {
/*  78:162 */     baos.writeTo(con.getOutputStream());
/*  79:    */   }
/*  80:    */   
/*  81:    */   protected void validateResponse(HttpInvokerClientConfiguration config, HttpURLConnection con)
/*  82:    */     throws IOException
/*  83:    */   {
/*  84:178 */     if (con.getResponseCode() >= 300) {
/*  85:179 */       throw new IOException(
/*  86:180 */         "Did not receive successful HTTP response: status code = " + con.getResponseCode() + 
/*  87:181 */         ", status message = [" + con.getResponseMessage() + "]");
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected InputStream readResponseBody(HttpInvokerClientConfiguration config, HttpURLConnection con)
/*  92:    */     throws IOException
/*  93:    */   {
/*  94:204 */     if (isGzipResponse(con)) {
/*  95:206 */       return new GZIPInputStream(con.getInputStream());
/*  96:    */     }
/*  97:210 */     return con.getInputStream();
/*  98:    */   }
/*  99:    */   
/* 100:    */   protected boolean isGzipResponse(HttpURLConnection con)
/* 101:    */   {
/* 102:221 */     String encodingHeader = con.getHeaderField("Content-Encoding");
/* 103:222 */     return (encodingHeader != null) && (encodingHeader.toLowerCase().contains("gzip"));
/* 104:    */   }
/* 105:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor
 * JD-Core Version:    0.7.0.1
 */