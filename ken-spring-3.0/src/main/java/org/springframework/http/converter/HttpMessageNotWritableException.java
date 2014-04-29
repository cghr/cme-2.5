/*  1:   */ package org.springframework.http.converter;
/*  2:   */ 
/*  3:   */ public class HttpMessageNotWritableException
/*  4:   */   extends HttpMessageConversionException
/*  5:   */ {
/*  6:   */   public HttpMessageNotWritableException(String msg)
/*  7:   */   {
/*  8:34 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public HttpMessageNotWritableException(String msg, Throwable cause)
/* 12:   */   {
/* 13:44 */     super(msg, cause);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.HttpMessageNotWritableException
 * JD-Core Version:    0.7.0.1
 */