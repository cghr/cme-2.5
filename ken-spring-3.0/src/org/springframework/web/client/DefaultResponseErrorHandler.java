/*  1:   */ package org.springframework.web.client;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.nio.charset.Charset;
/*  5:   */ import org.springframework.http.HttpHeaders;
/*  6:   */ import org.springframework.http.HttpStatus;
/*  7:   */ import org.springframework.http.HttpStatus.Series;
/*  8:   */ import org.springframework.http.MediaType;
/*  9:   */ import org.springframework.http.client.ClientHttpResponse;
/* 10:   */ import org.springframework.util.FileCopyUtils;
/* 11:   */ 
/* 12:   */ public class DefaultResponseErrorHandler
/* 13:   */   implements ResponseErrorHandler
/* 14:   */ {
/* 15:   */   public boolean hasError(ClientHttpResponse response)
/* 16:   */     throws IOException
/* 17:   */   {
/* 18:45 */     return hasError(response.getStatusCode());
/* 19:   */   }
/* 20:   */   
/* 21:   */   protected boolean hasError(HttpStatus statusCode)
/* 22:   */   {
/* 23:58 */     return (statusCode.series() == HttpStatus.Series.CLIENT_ERROR) || (statusCode.series() == HttpStatus.Series.SERVER_ERROR);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void handleError(ClientHttpResponse response)
/* 27:   */     throws IOException
/* 28:   */   {
/* 29:69 */     HttpStatus statusCode = response.getStatusCode();
/* 30:70 */     MediaType contentType = response.getHeaders().getContentType();
/* 31:71 */     Charset charset = contentType != null ? contentType.getCharSet() : null;
/* 32:72 */     byte[] body = getResponseBody(response);
/* 33:73 */     switch (statusCode.series())
/* 34:   */     {
/* 35:   */     case SERVER_ERROR: 
/* 36:75 */       throw new HttpClientErrorException(statusCode, response.getStatusText(), body, charset);
/* 37:   */     case SUCCESSFUL: 
/* 38:77 */       throw new HttpServerErrorException(statusCode, response.getStatusText(), body, charset);
/* 39:   */     }
/* 40:79 */     throw new RestClientException("Unknown status code [" + statusCode + "]");
/* 41:   */   }
/* 42:   */   
/* 43:   */   private byte[] getResponseBody(ClientHttpResponse response)
/* 44:   */   {
/* 45:   */     try
/* 46:   */     {
/* 47:85 */       return FileCopyUtils.copyToByteArray(response.getBody());
/* 48:   */     }
/* 49:   */     catch (IOException localIOException) {}
/* 50:88 */     return new byte[0];
/* 51:   */   }
/* 52:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.client.DefaultResponseErrorHandler
 * JD-Core Version:    0.7.0.1
 */