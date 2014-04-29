/*  1:   */ package org.springframework.web;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ import org.springframework.http.MediaType;
/*  5:   */ 
/*  6:   */ public class HttpMediaTypeNotSupportedException
/*  7:   */   extends HttpMediaTypeException
/*  8:   */ {
/*  9:   */   private final MediaType contentType;
/* 10:   */   
/* 11:   */   public HttpMediaTypeNotSupportedException(String message)
/* 12:   */   {
/* 13:39 */     super(message);
/* 14:40 */     this.contentType = null;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public HttpMediaTypeNotSupportedException(MediaType contentType, List<MediaType> supportedMediaTypes)
/* 18:   */   {
/* 19:49 */     this(contentType, supportedMediaTypes, "Content type '" + contentType + "' not supported");
/* 20:   */   }
/* 21:   */   
/* 22:   */   public HttpMediaTypeNotSupportedException(MediaType contentType, List<MediaType> supportedMediaTypes, String msg)
/* 23:   */   {
/* 24:59 */     super(msg, supportedMediaTypes);
/* 25:60 */     this.contentType = contentType;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public MediaType getContentType()
/* 29:   */   {
/* 30:67 */     return this.contentType;
/* 31:   */   }
/* 32:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.HttpMediaTypeNotSupportedException
 * JD-Core Version:    0.7.0.1
 */