/*   1:    */ package org.springframework.web.servlet.mvc.method.annotation.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.Comparator;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.LinkedHashSet;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Map;
/*  11:    */ import java.util.Set;
/*  12:    */ import javax.servlet.http.HttpServletRequest;
/*  13:    */ import javax.servlet.http.HttpServletResponse;
/*  14:    */ import org.apache.commons.logging.Log;
/*  15:    */ import org.springframework.core.MethodParameter;
/*  16:    */ import org.springframework.http.HttpHeaders;
/*  17:    */ import org.springframework.http.HttpInputMessage;
/*  18:    */ import org.springframework.http.MediaType;
/*  19:    */ import org.springframework.http.converter.HttpMessageConverter;
/*  20:    */ import org.springframework.http.server.ServletServerHttpRequest;
/*  21:    */ import org.springframework.http.server.ServletServerHttpResponse;
/*  22:    */ import org.springframework.util.CollectionUtils;
/*  23:    */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*  24:    */ import org.springframework.web.context.request.NativeWebRequest;
/*  25:    */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*  26:    */ import org.springframework.web.servlet.HandlerMapping;
/*  27:    */ 
/*  28:    */ public abstract class AbstractMessageConverterMethodProcessor
/*  29:    */   extends AbstractMessageConverterMethodArgumentResolver
/*  30:    */   implements HandlerMethodReturnValueHandler
/*  31:    */ {
/*  32: 54 */   private static final MediaType MEDIA_TYPE_APPLICATION = new MediaType("application");
/*  33:    */   
/*  34:    */   protected AbstractMessageConverterMethodProcessor(List<HttpMessageConverter<?>> messageConverters)
/*  35:    */   {
/*  36: 57 */     super(messageConverters);
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest)
/*  40:    */   {
/*  41: 67 */     HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class);
/*  42: 68 */     return new ServletServerHttpResponse(response);
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected <T> void writeWithMessageConverters(T returnValue, MethodParameter returnType, NativeWebRequest webRequest)
/*  46:    */     throws IOException, HttpMediaTypeNotAcceptableException
/*  47:    */   {
/*  48: 79 */     ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
/*  49: 80 */     ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
/*  50: 81 */     writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected <T> void writeWithMessageConverters(T returnValue, MethodParameter returnType, ServletServerHttpRequest inputMessage, ServletServerHttpResponse outputMessage)
/*  54:    */     throws IOException, HttpMediaTypeNotAcceptableException
/*  55:    */   {
/*  56:102 */     Class<?> returnValueClass = returnValue.getClass();
/*  57:    */     
/*  58:104 */     List<MediaType> acceptableMediaTypes = getAcceptableMediaTypes(inputMessage);
/*  59:105 */     List<MediaType> producibleMediaTypes = getProducibleMediaTypes(inputMessage.getServletRequest(), returnValueClass);
/*  60:    */     
/*  61:107 */     Set<MediaType> compatibleMediaTypes = new LinkedHashSet();
/*  62:    */     Iterator localIterator2;
/*  63:108 */     for (Iterator localIterator1 = acceptableMediaTypes.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/*  64:    */     {
/*  65:108 */       MediaType a = (MediaType)localIterator1.next();
/*  66:109 */       localIterator2 = producibleMediaTypes.iterator(); continue;MediaType p = (MediaType)localIterator2.next();
/*  67:110 */       if (a.isCompatibleWith(p)) {
/*  68:111 */         compatibleMediaTypes.add(getMostSpecificMediaType(a, p));
/*  69:    */       }
/*  70:    */     }
/*  71:115 */     if (compatibleMediaTypes.isEmpty()) {
/*  72:116 */       throw new HttpMediaTypeNotAcceptableException(this.allSupportedMediaTypes);
/*  73:    */     }
/*  74:119 */     List<MediaType> mediaTypes = new ArrayList(compatibleMediaTypes);
/*  75:120 */     MediaType.sortBySpecificity(mediaTypes);
/*  76:    */     
/*  77:122 */     MediaType selectedMediaType = null;
/*  78:123 */     for (MediaType mediaType : mediaTypes)
/*  79:    */     {
/*  80:124 */       if (mediaType.isConcrete())
/*  81:    */       {
/*  82:125 */         selectedMediaType = mediaType;
/*  83:126 */         break;
/*  84:    */       }
/*  85:128 */       if ((mediaType.equals(MediaType.ALL)) || (mediaType.equals(MEDIA_TYPE_APPLICATION)))
/*  86:    */       {
/*  87:129 */         selectedMediaType = MediaType.APPLICATION_OCTET_STREAM;
/*  88:130 */         break;
/*  89:    */       }
/*  90:    */     }
/*  91:134 */     if (selectedMediaType != null) {
/*  92:135 */       for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
/*  93:136 */         if (messageConverter.canWrite(returnValueClass, selectedMediaType))
/*  94:    */         {
/*  95:137 */           messageConverter.write(returnValue, selectedMediaType, outputMessage);
/*  96:138 */           if (this.logger.isDebugEnabled()) {
/*  97:139 */             this.logger.debug("Written [" + returnValue + "] as \"" + selectedMediaType + "\" using [" + 
/*  98:140 */               messageConverter + "]");
/*  99:    */           }
/* 100:142 */           return;
/* 101:    */         }
/* 102:    */       }
/* 103:    */     }
/* 104:146 */     throw new HttpMediaTypeNotAcceptableException(this.allSupportedMediaTypes);
/* 105:    */   }
/* 106:    */   
/* 107:    */   protected List<MediaType> getProducibleMediaTypes(HttpServletRequest request, Class<?> returnValueClass)
/* 108:    */   {
/* 109:159 */     Set<MediaType> mediaTypes = (Set)request.getAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
/* 110:160 */     if (!CollectionUtils.isEmpty(mediaTypes)) {
/* 111:161 */       return new ArrayList(mediaTypes);
/* 112:    */     }
/* 113:163 */     if (!this.allSupportedMediaTypes.isEmpty())
/* 114:    */     {
/* 115:164 */       List<MediaType> result = new ArrayList();
/* 116:165 */       for (HttpMessageConverter<?> converter : this.messageConverters) {
/* 117:166 */         if (converter.canWrite(returnValueClass, null)) {
/* 118:167 */           result.addAll(converter.getSupportedMediaTypes());
/* 119:    */         }
/* 120:    */       }
/* 121:170 */       return result;
/* 122:    */     }
/* 123:173 */     return Collections.singletonList(MediaType.ALL);
/* 124:    */   }
/* 125:    */   
/* 126:    */   private List<MediaType> getAcceptableMediaTypes(HttpInputMessage inputMessage)
/* 127:    */   {
/* 128:178 */     List<MediaType> result = inputMessage.getHeaders().getAccept();
/* 129:179 */     return result.isEmpty() ? Collections.singletonList(MediaType.ALL) : result;
/* 130:    */   }
/* 131:    */   
/* 132:    */   private MediaType getMostSpecificMediaType(MediaType type1, MediaType type2)
/* 133:    */   {
/* 134:186 */     double quality = type1.getQualityValue();
/* 135:187 */     Map<String, String> params = Collections.singletonMap("q", String.valueOf(quality));
/* 136:188 */     MediaType t1 = new MediaType(type1, params);
/* 137:189 */     MediaType t2 = new MediaType(type2, params);
/* 138:190 */     return MediaType.SPECIFICITY_COMPARATOR.compare(t1, t2) <= 0 ? type1 : type2;
/* 139:    */   }
/* 140:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.support.AbstractMessageConverterMethodProcessor
 * JD-Core Version:    0.7.0.1
 */