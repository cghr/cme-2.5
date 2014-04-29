/*   1:    */ package org.springframework.http;
/*   2:    */ 
/*   3:    */ import org.springframework.util.MultiValueMap;
/*   4:    */ 
/*   5:    */ public class HttpEntity<T>
/*   6:    */ {
/*   7: 58 */   public static final HttpEntity EMPTY = new HttpEntity();
/*   8:    */   private final HttpHeaders headers;
/*   9:    */   private final T body;
/*  10:    */   
/*  11:    */   protected HttpEntity()
/*  12:    */   {
/*  13: 70 */     this(null, null);
/*  14:    */   }
/*  15:    */   
/*  16:    */   public HttpEntity(T body)
/*  17:    */   {
/*  18: 78 */     this(body, null);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public HttpEntity(MultiValueMap<String, String> headers)
/*  22:    */   {
/*  23: 86 */     this(null, headers);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public HttpEntity(T body, MultiValueMap<String, String> headers)
/*  27:    */   {
/*  28: 95 */     this.body = body;
/*  29: 96 */     HttpHeaders tempHeaders = new HttpHeaders();
/*  30: 97 */     if (headers != null) {
/*  31: 98 */       tempHeaders.putAll(headers);
/*  32:    */     }
/*  33:100 */     this.headers = HttpHeaders.readOnlyHttpHeaders(tempHeaders);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public HttpHeaders getHeaders()
/*  37:    */   {
/*  38:108 */     return this.headers;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public T getBody()
/*  42:    */   {
/*  43:115 */     return this.body;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public boolean hasBody()
/*  47:    */   {
/*  48:122 */     return this.body != null;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public String toString()
/*  52:    */   {
/*  53:127 */     StringBuilder builder = new StringBuilder("<");
/*  54:128 */     if (this.body != null)
/*  55:    */     {
/*  56:129 */       builder.append(this.body);
/*  57:130 */       if (this.headers != null) {
/*  58:131 */         builder.append(',');
/*  59:    */       }
/*  60:    */     }
/*  61:134 */     if (this.headers != null) {
/*  62:135 */       builder.append(this.headers);
/*  63:    */     }
/*  64:137 */     builder.append('>');
/*  65:138 */     return builder.toString();
/*  66:    */   }
/*  67:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.HttpEntity
 * JD-Core Version:    0.7.0.1
 */