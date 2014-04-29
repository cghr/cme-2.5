/*   1:    */ package org.springframework.web.servlet.mvc.method.annotation.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.LinkedHashSet;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Set;
/*   9:    */ import javax.servlet.http.HttpServletRequest;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.apache.commons.logging.LogFactory;
/*  12:    */ import org.springframework.core.MethodParameter;
/*  13:    */ import org.springframework.http.HttpHeaders;
/*  14:    */ import org.springframework.http.HttpInputMessage;
/*  15:    */ import org.springframework.http.MediaType;
/*  16:    */ import org.springframework.http.converter.HttpMessageConverter;
/*  17:    */ import org.springframework.http.server.ServletServerHttpRequest;
/*  18:    */ import org.springframework.util.Assert;
/*  19:    */ import org.springframework.web.HttpMediaTypeNotSupportedException;
/*  20:    */ import org.springframework.web.context.request.NativeWebRequest;
/*  21:    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*  22:    */ 
/*  23:    */ public abstract class AbstractMessageConverterMethodArgumentResolver
/*  24:    */   implements HandlerMethodArgumentResolver
/*  25:    */ {
/*  26: 50 */   protected final Log logger = LogFactory.getLog(getClass());
/*  27:    */   protected final List<HttpMessageConverter<?>> messageConverters;
/*  28:    */   protected final List<MediaType> allSupportedMediaTypes;
/*  29:    */   
/*  30:    */   public AbstractMessageConverterMethodArgumentResolver(List<HttpMessageConverter<?>> messageConverters)
/*  31:    */   {
/*  32: 57 */     Assert.notEmpty(messageConverters, "'messageConverters' must not be empty");
/*  33: 58 */     this.messageConverters = messageConverters;
/*  34: 59 */     this.allSupportedMediaTypes = getAllSupportedMediaTypes(messageConverters);
/*  35:    */   }
/*  36:    */   
/*  37:    */   private static List<MediaType> getAllSupportedMediaTypes(List<HttpMessageConverter<?>> messageConverters)
/*  38:    */   {
/*  39: 67 */     Set<MediaType> allSupportedMediaTypes = new LinkedHashSet();
/*  40: 68 */     for (HttpMessageConverter<?> messageConverter : messageConverters) {
/*  41: 69 */       allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
/*  42:    */     }
/*  43: 71 */     List<MediaType> result = new ArrayList(allSupportedMediaTypes);
/*  44: 72 */     MediaType.sortBySpecificity(result);
/*  45: 73 */     return Collections.unmodifiableList(result);
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected <T> Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter methodParam, Class<T> paramType)
/*  49:    */     throws IOException, HttpMediaTypeNotSupportedException
/*  50:    */   {
/*  51: 90 */     HttpInputMessage inputMessage = createInputMessage(webRequest);
/*  52: 91 */     return readWithMessageConverters(inputMessage, methodParam, paramType);
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected <T> Object readWithMessageConverters(HttpInputMessage inputMessage, MethodParameter methodParam, Class<T> paramType)
/*  56:    */     throws IOException, HttpMediaTypeNotSupportedException
/*  57:    */   {
/*  58:109 */     MediaType contentType = inputMessage.getHeaders().getContentType();
/*  59:110 */     if (contentType == null) {
/*  60:111 */       contentType = MediaType.APPLICATION_OCTET_STREAM;
/*  61:    */     }
/*  62:114 */     for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
/*  63:115 */       if (messageConverter.canRead(paramType, contentType))
/*  64:    */       {
/*  65:116 */         if (this.logger.isDebugEnabled()) {
/*  66:117 */           this.logger.debug("Reading [" + paramType.getName() + "] as \"" + contentType + "\" using [" + 
/*  67:118 */             messageConverter + "]");
/*  68:    */         }
/*  69:120 */         return messageConverter.read(paramType, inputMessage);
/*  70:    */       }
/*  71:    */     }
/*  72:124 */     throw new HttpMediaTypeNotSupportedException(contentType, this.allSupportedMediaTypes);
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected ServletServerHttpRequest createInputMessage(NativeWebRequest webRequest)
/*  76:    */   {
/*  77:134 */     HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/*  78:135 */     return new ServletServerHttpRequest(servletRequest);
/*  79:    */   }
/*  80:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.support.AbstractMessageConverterMethodArgumentResolver
 * JD-Core Version:    0.7.0.1
 */