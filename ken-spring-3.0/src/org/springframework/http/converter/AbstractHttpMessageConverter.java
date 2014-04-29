/*   1:    */ package org.springframework.http.converter;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Arrays;
/*   7:    */ import java.util.Collections;
/*   8:    */ import java.util.List;
/*   9:    */ import org.apache.commons.logging.Log;
/*  10:    */ import org.apache.commons.logging.LogFactory;
/*  11:    */ import org.springframework.http.HttpHeaders;
/*  12:    */ import org.springframework.http.HttpInputMessage;
/*  13:    */ import org.springframework.http.HttpOutputMessage;
/*  14:    */ import org.springframework.http.MediaType;
/*  15:    */ import org.springframework.util.Assert;
/*  16:    */ 
/*  17:    */ public abstract class AbstractHttpMessageConverter<T>
/*  18:    */   implements HttpMessageConverter<T>
/*  19:    */ {
/*  20: 48 */   protected final Log logger = LogFactory.getLog(getClass());
/*  21: 50 */   private List<MediaType> supportedMediaTypes = Collections.emptyList();
/*  22:    */   
/*  23:    */   protected AbstractHttpMessageConverter() {}
/*  24:    */   
/*  25:    */   protected AbstractHttpMessageConverter(MediaType supportedMediaType)
/*  26:    */   {
/*  27: 65 */     setSupportedMediaTypes(Collections.singletonList(supportedMediaType));
/*  28:    */   }
/*  29:    */   
/*  30:    */   protected AbstractHttpMessageConverter(MediaType... supportedMediaTypes)
/*  31:    */   {
/*  32: 73 */     setSupportedMediaTypes(Arrays.asList(supportedMediaTypes));
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes)
/*  36:    */   {
/*  37: 81 */     Assert.notEmpty(supportedMediaTypes, "'supportedMediaTypes' must not be empty");
/*  38: 82 */     this.supportedMediaTypes = new ArrayList(supportedMediaTypes);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public List<MediaType> getSupportedMediaTypes()
/*  42:    */   {
/*  43: 86 */     return Collections.unmodifiableList(this.supportedMediaTypes);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public boolean canRead(Class<?> clazz, MediaType mediaType)
/*  47:    */   {
/*  48: 97 */     return (supports(clazz)) && (canRead(mediaType));
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected boolean canRead(MediaType mediaType)
/*  52:    */   {
/*  53:108 */     if (mediaType == null) {
/*  54:109 */       return true;
/*  55:    */     }
/*  56:111 */     for (MediaType supportedMediaType : getSupportedMediaTypes()) {
/*  57:112 */       if (supportedMediaType.includes(mediaType)) {
/*  58:113 */         return true;
/*  59:    */       }
/*  60:    */     }
/*  61:116 */     return false;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public boolean canWrite(Class<?> clazz, MediaType mediaType)
/*  65:    */   {
/*  66:126 */     return (supports(clazz)) && (canWrite(mediaType));
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected boolean canWrite(MediaType mediaType)
/*  70:    */   {
/*  71:137 */     if ((mediaType == null) || (MediaType.ALL.equals(mediaType))) {
/*  72:138 */       return true;
/*  73:    */     }
/*  74:140 */     for (MediaType supportedMediaType : getSupportedMediaTypes()) {
/*  75:141 */       if (supportedMediaType.isCompatibleWith(mediaType)) {
/*  76:142 */         return true;
/*  77:    */       }
/*  78:    */     }
/*  79:145 */     return false;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public final T read(Class<? extends T> clazz, HttpInputMessage inputMessage)
/*  83:    */     throws IOException
/*  84:    */   {
/*  85:154 */     return readInternal(clazz, inputMessage);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public final void write(T t, MediaType contentType, HttpOutputMessage outputMessage)
/*  89:    */     throws IOException, HttpMessageNotWritableException
/*  90:    */   {
/*  91:166 */     HttpHeaders headers = outputMessage.getHeaders();
/*  92:167 */     if (headers.getContentType() == null)
/*  93:    */     {
/*  94:168 */       if ((contentType == null) || (contentType.isWildcardType()) || (contentType.isWildcardSubtype())) {
/*  95:169 */         contentType = getDefaultContentType(t);
/*  96:    */       }
/*  97:171 */       if (contentType != null) {
/*  98:172 */         headers.setContentType(contentType);
/*  99:    */       }
/* 100:    */     }
/* 101:175 */     if (headers.getContentLength() == -1L)
/* 102:    */     {
/* 103:176 */       Long contentLength = getContentLength(t, contentType);
/* 104:177 */       if (contentLength != null) {
/* 105:178 */         headers.setContentLength(contentLength.longValue());
/* 106:    */       }
/* 107:    */     }
/* 108:181 */     writeInternal(t, outputMessage);
/* 109:182 */     outputMessage.getBody().flush();
/* 110:    */   }
/* 111:    */   
/* 112:    */   protected MediaType getDefaultContentType(T t)
/* 113:    */   {
/* 114:195 */     List<MediaType> mediaTypes = getSupportedMediaTypes();
/* 115:196 */     return !mediaTypes.isEmpty() ? (MediaType)mediaTypes.get(0) : null;
/* 116:    */   }
/* 117:    */   
/* 118:    */   protected Long getContentLength(T t, MediaType contentType)
/* 119:    */   {
/* 120:207 */     return null;
/* 121:    */   }
/* 122:    */   
/* 123:    */   protected abstract boolean supports(Class<?> paramClass);
/* 124:    */   
/* 125:    */   protected abstract T readInternal(Class<? extends T> paramClass, HttpInputMessage paramHttpInputMessage)
/* 126:    */     throws IOException, HttpMessageNotReadableException;
/* 127:    */   
/* 128:    */   protected abstract void writeInternal(T paramT, HttpOutputMessage paramHttpOutputMessage)
/* 129:    */     throws IOException, HttpMessageNotWritableException;
/* 130:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.AbstractHttpMessageConverter
 * JD-Core Version:    0.7.0.1
 */