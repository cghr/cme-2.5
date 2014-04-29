/*   1:    */ package org.springframework.http;
/*   2:    */ 
/*   3:    */ import org.springframework.util.MultiValueMap;
/*   4:    */ 
/*   5:    */ public class ResponseEntity<T>
/*   6:    */   extends HttpEntity<T>
/*   7:    */ {
/*   8:    */   private final HttpStatus statusCode;
/*   9:    */   
/*  10:    */   public ResponseEntity(HttpStatus statusCode)
/*  11:    */   {
/*  12: 55 */     this.statusCode = statusCode;
/*  13:    */   }
/*  14:    */   
/*  15:    */   public ResponseEntity(T body, HttpStatus statusCode)
/*  16:    */   {
/*  17: 64 */     super(body);
/*  18: 65 */     this.statusCode = statusCode;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public ResponseEntity(MultiValueMap<String, String> headers, HttpStatus statusCode)
/*  22:    */   {
/*  23: 74 */     super(headers);
/*  24: 75 */     this.statusCode = statusCode;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public ResponseEntity(T body, MultiValueMap<String, String> headers, HttpStatus statusCode)
/*  28:    */   {
/*  29: 85 */     super(body, headers);
/*  30: 86 */     this.statusCode = statusCode;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public HttpStatus getStatusCode()
/*  34:    */   {
/*  35: 94 */     return this.statusCode;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public String toString()
/*  39:    */   {
/*  40: 99 */     StringBuilder builder = new StringBuilder("<");
/*  41:100 */     builder.append(this.statusCode.toString());
/*  42:101 */     builder.append(' ');
/*  43:102 */     builder.append(this.statusCode.getReasonPhrase());
/*  44:103 */     builder.append(',');
/*  45:104 */     T body = getBody();
/*  46:105 */     HttpHeaders headers = getHeaders();
/*  47:106 */     if (body != null)
/*  48:    */     {
/*  49:107 */       builder.append(body);
/*  50:108 */       if (headers != null) {
/*  51:109 */         builder.append(',');
/*  52:    */       }
/*  53:    */     }
/*  54:112 */     if (headers != null) {
/*  55:113 */       builder.append(headers);
/*  56:    */     }
/*  57:115 */     builder.append('>');
/*  58:116 */     return builder.toString();
/*  59:    */   }
/*  60:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.ResponseEntity
 * JD-Core Version:    0.7.0.1
 */