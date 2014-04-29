/*  1:   */ package org.springframework.web.client;
/*  2:   */ 
/*  3:   */ import org.springframework.core.NestedRuntimeException;
/*  4:   */ 
/*  5:   */ public class RestClientException
/*  6:   */   extends NestedRuntimeException
/*  7:   */ {
/*  8:   */   public RestClientException(String msg)
/*  9:   */   {
/* 10:34 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public RestClientException(String msg, Throwable ex)
/* 14:   */   {
/* 15:43 */     super(msg, ex);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.client.RestClientException
 * JD-Core Version:    0.7.0.1
 */