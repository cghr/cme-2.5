/*   1:    */ package org.springframework.web.client;
/*   2:    */ 
/*   3:    */ import java.io.UnsupportedEncodingException;
/*   4:    */ import java.nio.charset.Charset;
/*   5:    */ import org.springframework.http.HttpStatus;
/*   6:    */ 
/*   7:    */ public abstract class HttpStatusCodeException
/*   8:    */   extends RestClientException
/*   9:    */ {
/*  10: 32 */   private static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");
/*  11:    */   private final HttpStatus statusCode;
/*  12:    */   private final String statusText;
/*  13:    */   private final byte[] responseBody;
/*  14:    */   private final Charset responseCharset;
/*  15:    */   
/*  16:    */   protected HttpStatusCodeException(HttpStatus statusCode)
/*  17:    */   {
/*  18: 48 */     this(statusCode, statusCode.name(), null, null);
/*  19:    */   }
/*  20:    */   
/*  21:    */   protected HttpStatusCodeException(HttpStatus statusCode, String statusText)
/*  22:    */   {
/*  23: 58 */     this(statusCode, statusText, null, null);
/*  24:    */   }
/*  25:    */   
/*  26:    */   protected HttpStatusCodeException(HttpStatus statusCode, String statusText, byte[] responseBody, Charset responseCharset)
/*  27:    */   {
/*  28: 75 */     super(statusCode.value() + " " + statusText);
/*  29: 76 */     this.statusCode = statusCode;
/*  30: 77 */     this.statusText = statusText;
/*  31: 78 */     this.responseBody = (responseBody != null ? responseBody : new byte[0]);
/*  32: 79 */     this.responseCharset = (responseCharset != null ? responseCharset : DEFAULT_CHARSET);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public HttpStatus getStatusCode()
/*  36:    */   {
/*  37: 86 */     return this.statusCode;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public String getStatusText()
/*  41:    */   {
/*  42: 93 */     return this.statusText;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public byte[] getResponseBodyAsByteArray()
/*  46:    */   {
/*  47:102 */     return this.responseBody;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public String getResponseBodyAsString()
/*  51:    */   {
/*  52:    */     try
/*  53:    */     {
/*  54:112 */       return new String(this.responseBody, this.responseCharset.name());
/*  55:    */     }
/*  56:    */     catch (UnsupportedEncodingException ex)
/*  57:    */     {
/*  58:116 */       throw new InternalError(ex.getMessage());
/*  59:    */     }
/*  60:    */   }
/*  61:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.client.HttpStatusCodeException
 * JD-Core Version:    0.7.0.1
 */