/*  1:   */ package org.springframework.web;
/*  2:   */ 
/*  3:   */ import java.util.Collections;
/*  4:   */ import java.util.List;
/*  5:   */ import javax.servlet.ServletException;
/*  6:   */ import org.springframework.http.MediaType;
/*  7:   */ 
/*  8:   */ public abstract class HttpMediaTypeException
/*  9:   */   extends ServletException
/* 10:   */ {
/* 11:   */   private final List<MediaType> supportedMediaTypes;
/* 12:   */   
/* 13:   */   protected HttpMediaTypeException(String message)
/* 14:   */   {
/* 15:40 */     super(message);
/* 16:41 */     this.supportedMediaTypes = Collections.emptyList();
/* 17:   */   }
/* 18:   */   
/* 19:   */   protected HttpMediaTypeException(String message, List<MediaType> supportedMediaTypes)
/* 20:   */   {
/* 21:49 */     super(message);
/* 22:50 */     this.supportedMediaTypes = supportedMediaTypes;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public List<MediaType> getSupportedMediaTypes()
/* 26:   */   {
/* 27:57 */     return this.supportedMediaTypes;
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.HttpMediaTypeException
 * JD-Core Version:    0.7.0.1
 */