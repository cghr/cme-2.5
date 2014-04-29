/*  1:   */ package org.springframework.web.client;
/*  2:   */ 
/*  3:   */ import java.nio.charset.Charset;
/*  4:   */ import org.springframework.http.HttpStatus;
/*  5:   */ 
/*  6:   */ public class HttpServerErrorException
/*  7:   */   extends HttpStatusCodeException
/*  8:   */ {
/*  9:   */   public HttpServerErrorException(HttpStatus statusCode)
/* 10:   */   {
/* 11:38 */     super(statusCode);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public HttpServerErrorException(HttpStatus statusCode, String statusText)
/* 15:   */   {
/* 16:48 */     super(statusCode, statusText);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public HttpServerErrorException(HttpStatus statusCode, String statusText, byte[] responseBody, Charset responseCharset)
/* 20:   */   {
/* 21:65 */     super(statusCode, statusText, responseBody, responseCharset);
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.client.HttpServerErrorException
 * JD-Core Version:    0.7.0.1
 */