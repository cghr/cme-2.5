/*  1:   */ package org.springframework.http.converter;
/*  2:   */ 
/*  3:   */ public class HttpMessageNotReadableException
/*  4:   */   extends HttpMessageConversionException
/*  5:   */ {
/*  6:   */   public HttpMessageNotReadableException(String msg)
/*  7:   */   {
/*  8:34 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public HttpMessageNotReadableException(String msg, Throwable cause)
/* 12:   */   {
/* 13:44 */     super(msg, cause);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.HttpMessageNotReadableException
 * JD-Core Version:    0.7.0.1
 */