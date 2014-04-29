/*  1:   */ package org.springframework.http.converter;
/*  2:   */ 
/*  3:   */ import org.springframework.core.NestedRuntimeException;
/*  4:   */ 
/*  5:   */ public class HttpMessageConversionException
/*  6:   */   extends NestedRuntimeException
/*  7:   */ {
/*  8:   */   public HttpMessageConversionException(String msg)
/*  9:   */   {
/* 10:35 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public HttpMessageConversionException(String msg, Throwable cause)
/* 14:   */   {
/* 15:45 */     super(msg, cause);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.HttpMessageConversionException
 * JD-Core Version:    0.7.0.1
 */