/*  1:   */ package org.springframework.web;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ import org.springframework.http.MediaType;
/*  5:   */ 
/*  6:   */ public class HttpMediaTypeNotAcceptableException
/*  7:   */   extends HttpMediaTypeException
/*  8:   */ {
/*  9:   */   public HttpMediaTypeNotAcceptableException(String message)
/* 10:   */   {
/* 11:36 */     super(message);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public HttpMediaTypeNotAcceptableException(List<MediaType> supportedMediaTypes)
/* 15:   */   {
/* 16:44 */     super("Could not find acceptable representation", supportedMediaTypes);
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.HttpMediaTypeNotAcceptableException
 * JD-Core Version:    0.7.0.1
 */