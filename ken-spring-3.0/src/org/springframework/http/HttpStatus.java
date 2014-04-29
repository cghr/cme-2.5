/*   1:    */ package org.springframework.http;
/*   2:    */ 
/*   3:    */ public enum HttpStatus
/*   4:    */ {
/*   5: 36 */   CONTINUE(100, "Continue"),  SWITCHING_PROTOCOLS(101, "Switching Protocols"),  PROCESSING(102, "Processing"),  OK(200, "OK"),  CREATED(201, "Created"),  ACCEPTED(202, "Accepted"),  NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),  NO_CONTENT(204, "No Content"),  RESET_CONTENT(205, "Reset Content"),  PARTIAL_CONTENT(206, "Partial Content"),  MULTI_STATUS(207, "Multi-Status"),  ALREADY_REPORTED(208, "Already Reported"),  IM_USED(226, "IM Used"),  MULTIPLE_CHOICES(300, "Multiple Choices"),  MOVED_PERMANENTLY(301, "Moved Permanently"),  FOUND(302, "Found"),  MOVED_TEMPORARILY(302, "Moved Temporarily"),  SEE_OTHER(303, "See Other"),  NOT_MODIFIED(304, "Not Modified"),  USE_PROXY(305, "Use Proxy"),  TEMPORARY_REDIRECT(307, "Temporary Redirect"),  BAD_REQUEST(400, "Bad Request"),  UNAUTHORIZED(401, "Unauthorized"),  PAYMENT_REQUIRED(402, "Payment Required"),  FORBIDDEN(403, "Forbidden"),  NOT_FOUND(404, "Not Found"),  METHOD_NOT_ALLOWED(405, "Method Not Allowed"),  NOT_ACCEPTABLE(406, "Not Acceptable"),  PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),  REQUEST_TIMEOUT(408, "Request Time-out"),  CONFLICT(409, "Conflict"),  GONE(410, "Gone"),  LENGTH_REQUIRED(411, "Length Required"),  PRECONDITION_FAILED(412, "Precondition Failed"),  REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),  REQUEST_URI_TOO_LONG(414, "Request-URI Too Large"),  UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),  REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested range not satisfiable"),  EXPECTATION_FAILED(417, "Expectation Failed"),  INSUFFICIENT_SPACE_ON_RESOURCE(419, "Insufficient Space On Resource"),  METHOD_FAILURE(420, "Method Failure"),  DESTINATION_LOCKED(421, "Destination Locked"),  UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),  LOCKED(423, "Locked"),  FAILED_DEPENDENCY(424, "Failed Dependency"),  UPGRADE_REQUIRED(426, "Upgrade Required"),  INTERNAL_SERVER_ERROR(500, "Internal Server Error"),  NOT_IMPLEMENTED(501, "Not Implemented"),  BAD_GATEWAY(502, "Bad Gateway"),  SERVICE_UNAVAILABLE(503, "Service Unavailable"),  GATEWAY_TIMEOUT(504, "Gateway Time-out"),  HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version not supported"),  VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),  INSUFFICIENT_STORAGE(507, "Insufficient Storage"),  LOOP_DETECTED(508, "Loop Detected"),  NOT_EXTENDED(510, "Not Extended");
/*   6:    */   
/*   7:    */   private final int value;
/*   8:    */   private final String reasonPhrase;
/*   9:    */   
/*  10:    */   private HttpStatus(int value, String reasonPhrase)
/*  11:    */   {
/*  12:332 */     this.value = value;
/*  13:333 */     this.reasonPhrase = reasonPhrase;
/*  14:    */   }
/*  15:    */   
/*  16:    */   public int value()
/*  17:    */   {
/*  18:340 */     return this.value;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public String getReasonPhrase()
/*  22:    */   {
/*  23:347 */     return this.reasonPhrase;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public Series series()
/*  27:    */   {
/*  28:355 */     return Series.valueOf(this);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public String toString()
/*  32:    */   {
/*  33:363 */     return Integer.toString(this.value);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public static HttpStatus valueOf(int statusCode)
/*  37:    */   {
/*  38:374 */     for (HttpStatus status : ) {
/*  39:375 */       if (status.value == statusCode) {
/*  40:376 */         return status;
/*  41:    */       }
/*  42:    */     }
/*  43:379 */     throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static enum Series
/*  47:    */   {
/*  48:389 */     INFORMATIONAL(1),  SUCCESSFUL(2),  REDIRECTION(3),  CLIENT_ERROR(4),  SERVER_ERROR(5);
/*  49:    */     
/*  50:    */     private final int value;
/*  51:    */     
/*  52:    */     private Series(int value)
/*  53:    */     {
/*  54:398 */       this.value = value;
/*  55:    */     }
/*  56:    */     
/*  57:    */     public int value()
/*  58:    */     {
/*  59:405 */       return this.value;
/*  60:    */     }
/*  61:    */     
/*  62:    */     private static Series valueOf(HttpStatus status)
/*  63:    */     {
/*  64:409 */       int seriesCode = status.value() / 100;
/*  65:410 */       for (Series series : values()) {
/*  66:411 */         if (series.value == seriesCode) {
/*  67:412 */           return series;
/*  68:    */         }
/*  69:    */       }
/*  70:415 */       throw new IllegalArgumentException("No matching constant for [" + status + "]");
/*  71:    */     }
/*  72:    */   }
/*  73:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.HttpStatus
 * JD-Core Version:    0.7.0.1
 */