/*   1:    */ package org.springframework.web.client;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.commons.logging.Log;
/*   6:    */ import org.apache.commons.logging.LogFactory;
/*   7:    */ import org.springframework.http.HttpHeaders;
/*   8:    */ import org.springframework.http.HttpStatus;
/*   9:    */ import org.springframework.http.MediaType;
/*  10:    */ import org.springframework.http.client.ClientHttpResponse;
/*  11:    */ import org.springframework.http.converter.HttpMessageConverter;
/*  12:    */ import org.springframework.util.Assert;
/*  13:    */ 
/*  14:    */ public class HttpMessageConverterExtractor<T>
/*  15:    */   implements ResponseExtractor<T>
/*  16:    */ {
/*  17:    */   private final Class<T> responseType;
/*  18:    */   private final List<HttpMessageConverter<?>> messageConverters;
/*  19:    */   private final Log logger;
/*  20:    */   
/*  21:    */   public HttpMessageConverterExtractor(Class<T> responseType, List<HttpMessageConverter<?>> messageConverters)
/*  22:    */   {
/*  23: 52 */     this(responseType, messageConverters, LogFactory.getLog(HttpMessageConverterExtractor.class));
/*  24:    */   }
/*  25:    */   
/*  26:    */   HttpMessageConverterExtractor(Class<T> responseType, List<HttpMessageConverter<?>> messageConverters, Log logger)
/*  27:    */   {
/*  28: 56 */     Assert.notNull(responseType, "'responseType' must not be null");
/*  29: 57 */     Assert.notEmpty(messageConverters, "'messageConverters' must not be empty");
/*  30: 58 */     this.responseType = responseType;
/*  31: 59 */     this.messageConverters = messageConverters;
/*  32: 60 */     this.logger = logger;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public T extractData(ClientHttpResponse response)
/*  36:    */     throws IOException
/*  37:    */   {
/*  38: 65 */     if (!hasMessageBody(response)) {
/*  39: 66 */       return null;
/*  40:    */     }
/*  41: 68 */     MediaType contentType = response.getHeaders().getContentType();
/*  42: 69 */     if (contentType == null)
/*  43:    */     {
/*  44: 70 */       if (this.logger.isTraceEnabled()) {
/*  45: 71 */         this.logger.trace("No Content-Type header found, defaulting to application/octet-stream");
/*  46:    */       }
/*  47: 73 */       contentType = MediaType.APPLICATION_OCTET_STREAM;
/*  48:    */     }
/*  49: 75 */     for (HttpMessageConverter messageConverter : this.messageConverters) {
/*  50: 76 */       if (messageConverter.canRead(this.responseType, contentType))
/*  51:    */       {
/*  52: 77 */         if (this.logger.isDebugEnabled()) {
/*  53: 78 */           this.logger.debug("Reading [" + this.responseType.getName() + "] as \"" + contentType + 
/*  54: 79 */             "\" using [" + messageConverter + "]");
/*  55:    */         }
/*  56: 81 */         return messageConverter.read(this.responseType, response);
/*  57:    */       }
/*  58:    */     }
/*  59: 84 */     throw new RestClientException(
/*  60: 85 */       "Could not extract response: no suitable HttpMessageConverter found for response type [" + 
/*  61: 86 */       this.responseType.getName() + "] and content type [" + contentType + "]");
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected boolean hasMessageBody(ClientHttpResponse response)
/*  65:    */     throws IOException
/*  66:    */   {
/*  67: 99 */     HttpStatus responseStatus = response.getStatusCode();
/*  68:100 */     if ((responseStatus == HttpStatus.NO_CONTENT) || (responseStatus == HttpStatus.NOT_MODIFIED)) {
/*  69:101 */       return false;
/*  70:    */     }
/*  71:103 */     long contentLength = response.getHeaders().getContentLength();
/*  72:104 */     return contentLength != 0L;
/*  73:    */   }
/*  74:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.client.HttpMessageConverterExtractor
 * JD-Core Version:    0.7.0.1
 */