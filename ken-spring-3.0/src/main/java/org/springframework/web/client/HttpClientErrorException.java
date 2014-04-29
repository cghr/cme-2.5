/*  1:   */ package org.springframework.web.client;
/*  2:   */ 
/*  3:   */ import java.nio.charset.Charset;
/*  4:   */ import org.springframework.http.HttpStatus;
/*  5:   */ 
/*  6:   */ public class HttpClientErrorException
/*  7:   */   extends HttpStatusCodeException
/*  8:   */ {
/*  9:   */   public HttpClientErrorException(HttpStatus statusCode)
/* 10:   */   {
/* 11:37 */     super(statusCode);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public HttpClientErrorException(HttpStatus statusCode, String statusText)
/* 15:   */   {
/* 16:46 */     super(statusCode, statusText);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public HttpClientErrorException(HttpStatus statusCode, String statusText, byte[] responseBody, Charset responseCharset)
/* 20:   */   {
/* 21:62 */     super(statusCode, statusText, responseBody, responseCharset);
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.client.HttpClientErrorException
 * JD-Core Version:    0.7.0.1
 */